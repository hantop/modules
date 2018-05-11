package com.fenlibao.p2p.controller.noversion.activity;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.activity.*;
import com.fenlibao.p2p.model.enums.activity.PrizeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2017年会抽奖活动
 */
@RestController
@RequestMapping("new/activity/annual/meeting")
public class AnnualMeetingController {

	private static final Logger logger = LogManager.getLogger(AnnualMeetingController.class);

	@Resource
	private ActivityService activityService;
	@Resource
	private UserTokenService userTokenService;

	@Resource
	private UserInfoService userInfoService;

	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
	@Resource
	private RedisService redisService;

	public void validate(HttpServletRequest request){
		String sdate=Config.get("activity.annualMeeting.startDate");
		Date startDate = DateUtil.StringToDate(sdate, "yyyy-MM-dd");
		String edate=Config.get("activity.annualMeeting.endDate");
		Date endDate = DateUtil.StringToDate(edate, "yyyy-MM-dd");
		Date now = new Date();
		if (now.before(startDate) || now.after(endDate)) {
			throw new BusinessException("403","当前时间不在抽奖活动范围");
		}
		String annualMeetingAuthorization = request.getParameter("key");
		String authorization = Config.get("activity.annualMeeting.authorization");
		if(annualMeetingAuthorization==null || !authorization.equals(annualMeetingAuthorization)){
			throw new BusinessException("403","未授权请求");
		}
	}
	/**
	 *  获取奖品列表
	 */
	@RequestMapping(value = "prize", method = RequestMethod.POST)
	HttpResponse prize(@ModelAttribute BaseRequestForm paramForm,HttpServletRequest request) {
		//校验授权
		this.validate(request);
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<AnnualMeetingPrize> items= activityService.getAnnualMeetingPrize();
		Map map=new HashMap();
		map.put("items",items);
		response.setData(map);
		return response;
	}

	/**
	 *  指定中奖名单
	 */
	@RequestMapping(value = "designated", method = RequestMethod.POST)
	HttpResponse designated(@ModelAttribute BaseRequestForm paramForm,HttpServletRequest request,
							String phoneNum, String name, String prizeCode) {
		//校验授权
		this.validate(request);
		HttpResponse response = new HttpResponse();
		String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(AnnualMeeting.ACTIVITY_CODE);
		if (redisService.existsKey(requestCacheKey)) {
			response.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
			return response;
		}
		if (!paramForm.validate() || StringUtils.isBlank(phoneNum)
				|| StringUtils.isBlank(name) || StringUtils.isBlank(prizeCode)) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			//插入指定名单
			int re=activityService.saveDesignated(phoneNum,prizeCode,name);
			if(re!=1){
				throw new BusinessException("数据插入失败");
			}
		}catch (Exception e){
			logger.error("年会指定中奖名单异常：[{}]"+e.getMessage());
			throw e;
		}finally {
			redisService.removeKey(requestCacheKey);
		}
		return response;
	}


	/**
	 *  抽奖--手机
	 * @param paramForm
	 * @param prizeType 1.红米；2.返现红包；3.iphone7
	 * @param qty 抽奖数量
     * @return
	 * 投资100以上可参加抽奖红米
	 * 投资1000以上可参加抽奖苹果
     */
	@RequestMapping(value = "phone/draw", method = RequestMethod.POST)
	HttpResponse phone(@ModelAttribute BaseRequestForm paramForm,HttpServletRequest request,
					   Integer prizeType, Integer qty) {
		//校验授权
		this.validate(request);
		HttpResponse response = new HttpResponse();

		String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(AnnualMeeting.ACTIVITY_CODE);
			if (redisService.existsKey(requestCacheKey)) {
			response.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
			return response;
		}
		if (!paramForm.validate() || prizeType==null
				|| qty==null ) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<AnnualMettingRecord> recordList=new ArrayList<>();
		try {
			if(prizeType==PrizeEnum.HM.getCode()){
				//抽奖红米手机
				recordList=activityService.getDrawHM(qty);
				//保存中奖名单
					activityService.saveAnnualMeetingHmRecord(recordList);
					Map map=new HashMap();
					map.put("items",recordList);
					response.setData(map);
					return response;

			}else if(prizeType==PrizeEnum.IPHONE.getCode()){
				qty=1;//苹果限制每次只能抽一台
				//抽取苹果手机并保存
				AnnualMettingRecord annualMettingRecord=activityService.getDrawIphone(qty);//抽奖苹果手机
				if(annualMettingRecord!=null){
					recordList.add(annualMettingRecord);
				}
			}
			Map map=new HashMap();
			map.put("items",recordList);
			response.setData(map);
		}catch (Exception e){
			e.printStackTrace();
			logger.error("年会红米手机抽奖异常数据：[{}]"+e.getMessage()+ JSON.toJSONString(recordList));
			throw e;
		}finally {
			redisService.removeKey(requestCacheKey);
		}

		return response;

	}


	/**
	 *  抽奖--现金红包
	 * @param paramForm
	 * @param prizeType 1.红米；2.返现红包；3.iphone7
	 * @param qty 抽奖数量
	 * @return
	 */
	@RequestMapping(value = "xjhb/draw", method = RequestMethod.POST)
	HttpResponse xjhb(@ModelAttribute BaseRequestForm paramForm,HttpServletRequest request,
					  Integer prizeType, Integer qty) {
					//校验授权
					this.validate(request);
					HttpResponse response = new HttpResponse();

					if (!paramForm.validate() || prizeType==null
							|| qty==null ) {
						logger.info("request param is empty");
						response.setCodeMessage(ResponseCode.EMPTY_PARAM);
						return response;
					}

					//最终中奖信息
					final List<AnnualMettingRecord> finalAnnualMettingRecords = new ArrayList<>();

					try{
						//获取指定人员名单
						List<AnnualMeetingDesignated> designatedList = activityService.getDesignatedistForRedPacket(Integer.parseInt(Config.get("annualMeeting.specialWinners.numPerTime")));
						int specialWinnersNum = designatedList.size();

						//普通中奖用户数量
						int normalWinnersNum = qty - specialWinnersNum;
						//获取剩余奖品信息
						List<AnnualMeetingPrize> effectivePrizeInfos = activityService.getAnnualMeetingPrize(prizeType.toString());
						//模拟抽奖，获取奖品
						List<AnnualMeetingPrize> finalWinningPrizes = activityService.lottery(effectivePrizeInfos, prizeType.toString(), normalWinnersNum);

						List<AnnualMettingRecord> annualMettingRecords = new ArrayList<>();

						if(finalWinningPrizes.size() > 0){
							//获取中奖用户
							List<AnnualMettingParticipant> list = activityService.drawRedPacket(finalWinningPrizes.size());
							for(int i = 0; i < list.size(); i++){
								AnnualMettingParticipant amParticipant = list.get(i);
								AnnualMeetingPrize amPrize = finalWinningPrizes.get(i);
								AnnualMettingRecord amr = new AnnualMettingRecord(
										amParticipant.getName(),
										amParticipant.getPhone(),
										amPrize.getType(),
										amPrize.getPrizeCode(),
										"1"
								);
								amr.setAmout(amPrize.getAmout());
								amr.setPrizeName(amPrize.getPrizeName());
								annualMettingRecords.add(amr);
							}
						}

						//组装指定用户
						for(AnnualMeetingDesignated amd : designatedList){
							AnnualMettingRecord amr = new AnnualMettingRecord(
									amd.getName(),
									amd.getPhone(),
									prizeType.toString(),
									amd.getPrizeCode(),
									amd.getRealFlag()
							);
							AnnualMeetingPrize amp = activityService.getAnnualMeetingPrize(prizeType.toString(), amd.getPrizeCode());
							amr.setAmout(amp.getAmout());
							amr.setPrizeName(amp.getPrizeName());
							annualMettingRecords.add(amr);
						}

						for(AnnualMettingRecord amr : annualMettingRecords){
							try{
								activityService.saveAnnualMettingRecordsAndUpdatePrizeNum(amr);
								finalAnnualMettingRecords.add(amr);
							}catch (Throwable e){
					logger.error("保存抽红包中奖记录失败："+amr.getName()+"|"+amr.getPhone()+"|"+amr.getPrizeCode()+"|"+amr.getType()+"|"+amr.getRealFlag(),e);
				}
			}

			fixedThreadPool.execute(new Runnable() {
				public void run() {
					//发送返现红包和短信
					for(AnnualMettingRecord amr : finalAnnualMettingRecords){
						try{
							if("1".equals(amr.getRealFlag())){
								activityService.grantActivityCashbackForAnnualMeetingRedPacket(amr);
							}
						}catch (Exception e){
							logger.error("发送返现红包和短信失败："+amr.getName()+"|"+amr.getPhone()+"|"+amr.getPrizeCode()+"|"+amr.getType()+"|"+amr.getRealFlag(),e);
						}
					}
				}
			});

		}catch (Exception e){
			e.printStackTrace();
		}
		Map map = new HashMap();
		Collections.shuffle(finalAnnualMettingRecords);
		map.put("items",finalAnnualMettingRecords);
		response.setData(map);
		return response;
	}


	/**
	 * 年会抽奖—获取中奖记录
	 * @param paramForm
	 * @param prizeType 1.红米；2.返现红包；3.iphone7
	 * @return
	 */
	@RequestMapping(value = "records", method = RequestMethod.POST)
	HttpResponse records(@ModelAttribute BaseRequestForm paramForm,HttpServletRequest request,
					   Integer prizeType) {
		//校验授权
		this.validate(request);
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<AnnualMettingRecord> items= activityService.getAnnualMeetingRecords(prizeType);
		Map map=new HashMap();
		map.put("items",items);
		response.setData(map);
		return response;
	}

	/**
	 * 年会抽奖—弹幕数据
	 * @param paramForm
	 * @param prizeType 1.红米；2.返现红包；3.iphone7
	 * @return
	 */
	@RequestMapping(value = "participants", method = RequestMethod.POST)
	HttpResponse participants(@ModelAttribute BaseRequestForm paramForm,HttpServletRequest request,
						 Integer prizeType, Integer qty) {
		//校验授权
		this.validate(request);
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || prizeType==null
				|| qty==null ) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<AnnualMettingParticipant> items= activityService.getAnnualMeetingParticipants(prizeType,qty);
		Map map=new HashMap();
		map.put("items",items);
		response.setData(map);
		return response;
	}
}