package com.fenlibao.p2p.controller.noversion;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketActivityVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.trade.TenderRecords;
import com.fenlibao.p2p.model.vo.TenderRecordResult;
import com.fenlibao.p2p.model.vo.TenderRecordVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.Validator;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Message;

/**
 * 临时活动相关的接口
 */
@RestController
@RequestMapping("activity")
public class ActivityController {

	private static final Logger logger= LogManager.getLogger(ActivityController.class);
	
	@Resource
	private ITradeService tradeService;
	
	@Resource
    private UserTokenService userTokenService;
	
	@Resource
	private ActivityService activityService;
	
	@Resource
	private UserInfoService userInfoService;
	
	/**
	 * 用户投标金额统计 前十名
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param startDate  开始时间戳
	 * @param endDate    截止时间戳
	 * @return
	 */
	@RequestMapping(value = "tender/records", method = RequestMethod.GET)
    HttpResponse tenderRecords(
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = false, value = "token") String token,
    		@RequestParam(required = false, value = "userId") String userId,
    		@RequestParam(required = false, value = "startDate") String startDate,
    		@RequestParam(required = false, value = "endDate") String endDate){
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()||StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate)){
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
				return response;
			}
			
			if (StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(token)){
					if(userTokenService.isInvalidToken(token, userId, paramForm.getClientType())){
						response.setCodeMessage(Message.STATUS_1029, Message.get(Message.STATUS_1029));
			            return response;
					}
	        }
			
			Date start = DateUtil.getDate(Long.parseLong(startDate));
			Date end = DateUtil.getDate(Long.parseLong(endDate));
			List<TenderRecords> list = tradeService.getUserTenderRecords(start, end, 10);
			
			List<TenderRecordVO> voList = new ArrayList<TenderRecordVO>();//用户投标记录统计结果
			
			for(TenderRecords tender:list){
				TenderRecordVO vo = new TenderRecordVO();
				vo.setUserName(StringHelper.replace(3, 7, tender.getPhone(), "****"));
				vo.setTotalInvest(tender.getTotalInvest());
				voList.add(vo);
			}
			
			//读取文件
			File file = new File(Config.get("winners.url"));
			List<String> strList = FileUtils.txt2String(file);
			for(String str:strList){
				String[] arr = str.split(",");
				TenderRecordVO vo = new TenderRecordVO();
				vo.setUserName(StringHelper.replace(3, 7, arr[0], "****"));
				vo.setTotalInvest(Double.parseDouble(arr[1]));
				voList.add(vo);
			}
			
			//排序
			Collections.sort(voList, new Comparator<TenderRecordVO>() {
				@Override
				public int compare(TenderRecordVO o1, TenderRecordVO o2) {
					if(o1.getTotalInvest() < o2.getTotalInvest()){   
	                    return 1;   
	                }
					if(o1.getTotalInvest() == o2.getTotalInvest()){   
	                    return 0;   
	                }
					return -1;
				}
			});
			
			voList = voList.subList(0, 10);
			
			TenderRecordResult result = new TenderRecordResult();
			result.setItems(voList);
			
			if(StringUtils.isNotEmpty(userId)){
				//用户投标总额
				double totalInvest = this.tradeService.getUserTenderTotal(start, end, Integer.parseInt(userId));
				result.setUserTotalInvest(totalInvest);
			}
			response.setData(CommonTool.toMap(result));
		}catch(Exception ex){
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
			logger.error("[ActivityController.tenderRecords]"+ex.getMessage(), ex);
		}
		return response;
	}
	
	@RequestMapping(value = "join", method = RequestMethod.POST)
    HttpResponse JoinActivity(
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = false, value = "phone") String phone,
    		@RequestParam(required = false, value = "activityCode") String activityCode){
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()||StringUtils.isEmpty(phone)||StringUtils.isEmpty(activityCode)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}
			
			if(!Validator.isMobile(phone)){
				response.setCodeMessage(ResponseCode.ACTIVITY_PHONE_REG_NOT_RIGHT.getCode(),ResponseCode.ACTIVITY_PHONE_REG_NOT_RIGHT.getMessage());
				return response;
			};
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("phoneNum", phone);
			UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(map);
			int isNew = 1;
			if(userInfo!=null && Integer.valueOf(userInfo.getUserId()) > 0){
				isNew = 0;
			}
			
			Map<String,Object> isNewMap = new HashMap<String,Object>();
			isNewMap.put("isNew",isNew);
			response.setData(isNewMap);
			
			int activityId = activityService.validIsRegistActivity(activityCode, phone);
			if(activityId > 0){
				response.setCodeMessage(ResponseCode.ACTIVITY_RECORD_EXIST.getCode(), ResponseCode.ACTIVITY_RECORD_EXIST.getMessage());
				return response;
			}
			int insertFlag = activityService.insertActivity(activityCode, phone,isNew);
			if(insertFlag > 0){
				response.setCodeMessage(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
				return response;
			}
		}catch(Exception ex){
			throw ex;
		}
		return response;
	}

	/**
	 * 获取相应编码活动的红包列表
	 * （以后类似这样的活动可以直接使用，直接在数据库配置相应的红包即可）
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "redpacket/list", method = RequestMethod.GET)
	HttpResponse getRedPacketList(BaseRequestForm params, Integer userId, String activityCode) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>();
		if (!params.validate() || StringUtils.isBlank(activityCode)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<RedPacketActivityVO> redPacketList = activityService.getRedPacketList(userId, activityCode);
		Map<String, List<RedPacketActivityVO>> redPacketMap = new HashMap<>(5);
		List<RedPacketActivityVO> itemList;
		String investDeadline;
		for (RedPacketActivityVO vo : redPacketList) {
			investDeadline = vo.getInvestDeadline();
			if (redPacketMap.containsKey(investDeadline))
				redPacketMap.get(investDeadline).add(vo);
			else {
				itemList = new ArrayList<>();
				itemList.add(vo);
				redPacketMap.put(investDeadline, itemList);
			}
		}
		data.put("redPacketList", redPacketMap);
		response.setData(data);
		return response;
	}

	/**
	 * 领取相应编码活动的红包
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "redpacket/receive", method = RequestMethod.POST)
	HttpResponse receiveRedPacket(BaseRequestFormExtend params, int redPacketId, String activityCode) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || redPacketId < 1 || StringUtils.isBlank(activityCode)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			activityService.receiveRedPacket(params.getUserId(), redPacketId, activityCode);
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error(String.format("领[%s]红包失败：userId[%s],redPacketId[%s]", activityCode, params.getUserId(), redPacketId), e);
		}
		return response;
	}
}
