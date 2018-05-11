package com.fenlibao.p2p.controller.noversion;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.activity.AutoRegist;
import com.fenlibao.p2p.model.entity.activity.VirusSpreadFreinds;
import com.fenlibao.p2p.model.entity.trade.TenderRecords;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.BindIdCardForm;
import com.fenlibao.p2p.model.form.RegisterForm;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.TenderRecordResult;
import com.fenlibao.p2p.model.vo.TenderRecordVO;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketOlympicActivityVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.bid.TenderShareService;
import com.fenlibao.p2p.service.bid.impl.NciicDmServiceImpl;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.user.IdCardAuthService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.Validator;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.loader.Config;

import com.fenlibao.p2p.util.loader.Sender;
import com.fenlibao.p2p.util.verify.IDCardVerify;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;

import java.io.File;
import java.util.*;

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

	@Resource
	private TenderShareService tenderShareService;

	@Resource
	private IdCardAuthService idCardAuthService;

	@Resource
	private NciicDmServiceImpl nciicDmService;
	/**
	 * 用户密码正则表达式
	 */
	private final String pwdPattern = "[a-zA-Z0-9]{6,20}";


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
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			if (StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(token)){
					if(userTokenService.isInvalidToken(token, userId, paramForm.getClientType())){
						response.setCodeMessage(ResponseCode.COMMON_NOT_VALID_TOKEN);
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
			response.setCodeMessage(ResponseCode.FAILURE);
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
				response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
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
				response.setCodeMessage(ResponseCode.ACTIVITY_RECORD_EXIST);
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
	 * 获取8月份奥运红包列表
	 * @param params
	 * @return
     */
	@RequestMapping(value = "olympic/redpacket/list", method = RequestMethod.GET)
	HttpResponse getOlympicRedPacketList(BaseRequestForm params, Integer userId) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>();
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		List<RedPacketOlympicActivityVO> redPacketList = activityService.getOlympicRedPacketList(userId, "AUGUST_OLYMPIC");//AUGUST_OLYMPIC 8月奥运活动编码
		Map<String, List<RedPacketOlympicActivityVO>> redPacketMap = new HashMap<>(5);
		List<RedPacketOlympicActivityVO> itemList;
		String investDeadline;
		for (RedPacketOlympicActivityVO vo : redPacketList) {
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
	 * 领取8月奥运红包
	 * @param params
	 * @return
     */
	@RequestMapping(value = "olympic/redpacket/receive", method = RequestMethod.POST)
	HttpResponse receiveOlympicRedPacket(BaseRequestFormExtend params, int redPacketId) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || redPacketId < 1) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			activityService.receiveOlympicRedPacket(params.getUserId(), redPacketId, "AUGUST_OLYMPIC");
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error(String.format("领取奥运红包失败：userId[%s],redPacketId[%s]", params.getUserId(), redPacketId), e);
		}
		return response;
	}

	/**
	 * 获取系统奖励记录（8月邀请好友）
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "augustInvitation/awards",method = RequestMethod.POST)
	HttpResponse augustInvitationAwards(BaseRequestForm  paramForm){
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		if(isAugustActivityEnd()){//判断活动是否结束
			response.setCodeMessage(ResponseCode.ACTIVITY_END);
			return response;
		}

		Map<String, Object> data =getRandomRecodes(10);
		response.setData(data);
		return response;
	}

	public  Map<String, Object> getRandomRecodes(int len){
		List<Object> list=new ArrayList<Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Random rd=new Random();
		String str="赵钱孙李周吴郑王冯陈楮卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪邓";
		int a=50;
		int b=10;
		for (int i=0;i<10 ;i++) {
			int num=rd.nextInt(10)*a+rd.nextInt(20)*b;
			if(num==0)num=50;
			Map<String,Object> map=new HashMap<String,Object>();
			String record=str.charAt(rd.nextInt(str.length()))+"**,累计赚取"+num+"元现金";
			map.put("record",record);
			list.add(map);
		}
		data.put("items", list);
		return data;
	}


	/**
	 * 记录点击事件，判断手机号是否注册
	 * add by:junda.feng
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "augustInvitation/checkPhone", method = RequestMethod.POST, headers = APIVersion.V_2_0_0)
    HttpResponse checkPhone(@ModelAttribute BaseRequestForm paramForm,
		    String phoneNum,Integer eventType) {
		HttpResponse response = new HttpResponse();

		if(isAugustActivityEnd()){//判断活动是否结束
			response.setCodeMessage(ResponseCode.ACTIVITY_END);
			return response;
		}
		if (!paramForm.validate() || StringUtils.isBlank(phoneNum)|| eventType==null) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}

		try {
			AES aesInstance = AES.getInstace();
			phoneNum = aesInstance.decrypt2(phoneNum);// 手机号解密.
		} catch (BadPaddingException bpe) {
			logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
			response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
			return response;
		}
		Integer id = activityService.getUserActivityEvent(phoneNum,eventType);
		if(id==null){
			int insertFlag = activityService.insertUserActivityEvent(phoneNum, eventType);
			if(insertFlag != 1){
				response.setCodeMessage(ResponseCode.FAILURE);
				return response;
			}
		}
		//需求更改，两个按钮都要判断注册情况
//		if(eventType==2){// 2:转发赚钱 需要判断注册情况
			// 验证手机号格式
			if (checkPhoneFormat(phoneNum)) {
				// 判断手机号是否存在
				@SuppressWarnings("rawtypes")
				Map paramMap = new HashMap();
				paramMap.put("phoneNum", phoneNum);
				int count = userInfoService.getUserCount(paramMap);
				if(count>0){
					// 手机号码已注册
					response.setCodeMessage(ResponseCode.USER_PHONE_REGISTERED);
				}else{
					// 手机号码未注册
					response.setCodeMessage(ResponseCode.USER_ACCOUNT_NOT_EXIST);
				}
			} else {
					// 手机号码格式不正确
					response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
			}
//		}
		return  response;
	}

	public static boolean isAugustActivityEnd(){
		boolean flag=false;
		String deadDate=Config.get("augustInvitation.activity.endDate");
		Date endDate = DateUtil.StringToDate(deadDate, "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		if (now.after(endDate)) {
			flag=true;
		}
		return flag;
	}

	/**
	 * 验证手机号格式
	 * @param phoneNum
	 * @return
	 */
	private boolean checkPhoneFormat(String phoneNum) {
		return phoneNum.matches(phonePattern);
	}
	/**
	 * 手机号正则表达式
	 */
	private final String phonePattern = "^(13|14|15|17|18)[0-9]{9}$";



	/**
	 * 获取系统奖励记录（8月邀请好友）
	 * @param paramForm
	 * @return
	 */
	@RequestMapping(value = "augustInvitation/friends",method = RequestMethod.POST)
	HttpResponse augustInvitationFriends(BaseRequestForm  paramForm,
										 String phoneNum) {
		HttpResponse response = new HttpResponse();
		if(isAugustActivityEnd()){//判断活动是否结束
			response.setCodeMessage(ResponseCode.ACTIVITY_END);
			return response;
		}

		if (!paramForm.validate() || StringUtils.isBlank(phoneNum)) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}
		try {
			AES aesInstance = AES.getInstace();
			phoneNum = aesInstance.decrypt2(phoneNum);// 手机号解密
		} catch (BadPaddingException bpe) {
			logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
			response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
			return response;
		}
		List<VirusSpreadFreinds> list=activityService.getAugustInvitationFriends(phoneNum);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("items", list);
		response.setData(data);
		return response;
	}

	/**
	 * 判断活动是否结束
	 * @param deadLine//结束时间
     * @return
     */
	public static boolean isActivityEnd(String deadLine){
		if(deadLine == null) return true;
		boolean flag=false;
		Date endDate = DateUtil.StringToDate(deadLine, "yyyy-MM-dd");
		Date now = new Date();
		if (now.after(endDate)) {
			flag=true;
		}
		return flag;
	}
	/**
	 * 9月名创会员首投活动--检验手机号码是否符合活动条件
	 * @param paramForm//
	 * @param phoneNum//手机号(AES256加密)
	 * @return HttpResponse
	 */
	@RequestMapping(value = "miniso/member/check",method = RequestMethod.POST)
	HttpResponse minisoMemberCheck(BaseRequestForm  paramForm,String phoneNum,String activityCode) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isBlank(phoneNum)){
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return  response;
		}

		activityService.activityState(activityCode,null);//检查活动时间

		try {
			AES aesInstance = AES.getInstace();
			phoneNum = aesInstance.decrypt2(phoneNum);// 手机号解密
		} catch (BadPaddingException bpe){
			logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
			response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
			return response;
		}

		Map<String, Object> map = new HashMap<>();
		map.put("phoneNum", phoneNum);
		// 根据手机号或登陆名查询用户信息
		UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(map);
		if (userInfo == null) {
			response.setCodeMessage(ResponseCode.USER_NOT_EXIST);
			return response;
		}

		Map<String,Object> mcMember=activityService.minisoPhoneCheck(phoneNum,activityCode);

		Map<String,Object> result=new HashMap<>();
		result.put("mcMemberFlag",false);
		if(mcMember!=null){
//			if(1==(long)mcMember.get("activity")){//送现金
//				activityService.activityState("MC_ACTIVITY_CASH",null);
//			}else{
//				activityService.activityState("MC_ACTIVITY_PRIZE",null);
//			}
			if(mcMember.get("investAmout")==null){
				result.put("mcMemberFlag",true);
				result.put("firstLoginFlag",mcMember.get("firstLoginFlag"));
				result.put("activity",mcMember.get("activity"));
			}
		}
		response.setData(result);
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
		List<RedPacketOlympicActivityVO> redPacketList = activityService.getOlympicRedPacketList(userId, activityCode);
		Map<String, List<RedPacketOlympicActivityVO>> redPacketMap = new HashMap<>(5);
		List<RedPacketOlympicActivityVO> itemList;
		String investDeadline;
		for (RedPacketOlympicActivityVO vo : redPacketList) {
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
			activityService.receiveOlympicRedPacket(params.getUserId(), redPacketId, activityCode);
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error(String.format("领[%s]红包失败：userId[%s],redPacketId[%s]", activityCode, params.getUserId(), redPacketId), e);
		}
		return response;
	}


	/**
	 * 批量自动注册
	 */
	@RequestMapping(value = "autoRegister", method = RequestMethod.POST)
	HttpResponse register(@ModelAttribute BaseRequestForm paramForm,String limit
						  ) {
		HttpResponse response = new HttpResponse();
		RegisterForm registerForm = new RegisterForm();
		if (!paramForm.validate()) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else {

			List<AutoRegist> registList = this.activityService.getAutoRegistList("0", null,Integer.valueOf(limit));
			if (registList != null && registList.size() > 0) {
				for (AutoRegist autoRegist : registList) {
					registerForm.setChannelCode("pcgw");
					registerForm.setPhoneNum(autoRegist.getMobile());
					registerForm.setPassword("123456");
					String phoneNum = registerForm.getPhoneNum();
					String password = registerForm.getPassword();

						// 验证手机号格式
						if (checkPhoneFormat(phoneNum)) {
							// 判断手机号是否存在
							Map paramMap = new HashMap();
							paramMap.put("phoneNum", phoneNum);
							int count = userInfoService.getUserCount(paramMap);

							UserInfo userInfo = null;
							boolean isException = false;
							// 手机号不存在
							if (count == 0) {
								// 普通注册
								try {
									autoRegist.setRegistLog("用户注册成功");
									autoRegist.setRegistStatus(1);
									userInfo = userInfoService.autoRegister(paramForm, registerForm,autoRegist);

									isException =true;
								} catch (Exception e) {
									autoRegist.setRegistLog("用户注册，表插入失败");
									activityService.updateAutoRegist(autoRegist);
									isException = true;
								}
								if (!isException) {
									String userId = userInfo.getUserId();
									// 发放注册奖励
									userInfoService.grantAwardRegister(phoneNum, userId);
									tenderShareService.grantRedEnvelopeForRegister(userId, phoneNum); //发放未注册前领取的红包
								}
							} else {
								// 手机号码已注册
								Map<String,Object> map = new HashMap<>();
								map.put("phoneNum", phoneNum);
								// 新增用户信息
								UserInfo uInfo = this.userInfoService.getUserInfoByPhoneNumOrUsername(map);
								autoRegist.setRegistStatus(1);
								autoRegist.setUserId(Integer.valueOf(uInfo.getUserId()));
								autoRegist.setRegistLog(ResponseCode.USER_PHONE_REGISTERED.getMessage());
								activityService.updateAutoRegist(autoRegist);
							}
						} else {
							// 手机号码格式不正确
							autoRegist.setRegistLog(ResponseCode.COMMON_PHONE_FORMAT_WRONG.getMessage());
							activityService.updateAutoRegist(autoRegist);
						}


				}
			}
		}
			return response;


	}


	/**
	 * 批量自动实名
	 * @param paramForm
	 * @param limit
     * @return
     */
	@RequestMapping(value = "autoIdCard", method = RequestMethod.POST)
	HttpResponse bindIdCard(@ModelAttribute BaseRequestForm paramForm,String limit) {

		HttpResponse response = new HttpResponse();
		BindIdCardForm bindIdCardForm = new BindIdCardForm();
		List<AutoRegist> registList = this.activityService.getAutoRegistList("1", "0",Integer.valueOf(limit));
		if(registList!=null&&registList.size()>0) {
			for (AutoRegist autoRegist : registList) {
				UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(autoRegist.getUserId()));
				String idCardFullName = autoRegist.getName();

				try {
					bindIdCardForm.setUserId(autoRegist.getUserId());
					bindIdCardForm.setIdCardFullName(idCardFullName);
					String idCard =  autoRegist.getIdCard();


					String a = com.dimeng.util.StringHelper.encode("43048119711017475X");
					String d = com.dimeng.util.StringHelper.encode("510521199106021898");
					if (!StringUtils.isBlank(idCard)) {
						bindIdCardForm.setIdCardNum(idCard.toUpperCase());
					}
					String mtest = "^[\\u4E00-\\u9FA5]{2,5}(?:·[\\u4E00-\\u9FA5]{2,5})*$";
					if (!bindIdCardForm.getIdCardFullName().matches(mtest)) {
						autoRegist.setCardLog("请输入合法的姓名");
						activityService.updateAutoRegist(autoRegist);
						continue;
					}
					// 验证身份证格式并返回信息
					String idCardValidateResult = IDCardVerify.idCardValidate(bindIdCardForm.getIdCardNum());
					if (idCardValidateResult.equals("YES")) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(System.currentTimeMillis());
						int year = calendar.get(Calendar.YEAR);
						int born = Integer.parseInt(bindIdCardForm.getIdCardNum().substring(6, 10));
						if ((year - born) < 16) {
							autoRegist.setCardLog("必须年满16周岁");
							activityService.updateAutoRegist(autoRegist);
							continue;
						}

						UserInfo userInfo = userInfoService.getUser(null, null, String.valueOf(autoRegist.getUserId()));
						logger.info("[********UserInfo********]-->userId:" + userInfo.getUserId() + " userName:" + userInfo.getFullName() + " AuthStauts:" + userInfo.getAuthStatus() + " carId:" + userInfo.getIdCardEncrypt());
						if (userInfo != null) {
							if ("TG".equals(userInfo.getAuthStatus())) {
								autoRegist.setCardStatus(1);
								autoRegist.setCardLog("用户已经实名认证过");
								activityService.updateAutoRegist(autoRegist);
								continue;
							}

							//同过用户名到S10_1037 获取保存登录错误次数
							//int count = userInfoService.getUserAuthError(autoRegist.getUserId());
							//if (InterfaceConst.ALLWO_LOGIN_ERROR_TIMES != count) {    //最大校验12次
								if (!nciicDmService.isIdcard(bindIdCardForm.getIdCardNum())) {
										idCardAuthService.saveRealNameAuthentication(bindIdCardForm);
										autoRegist.setCardLog("实名成功");
										autoRegist.setCardStatus(1);
										activityService.updateAutoRegist(autoRegist);

								} else {
									autoRegist.setCardStatus(1);
									autoRegist.setCardLog("用户已经实名认证过");
									activityService.updateAutoRegist(autoRegist);
									continue;

								}
							/*} else {
								autoRegist.setCardLog("超过实名验证最大错误限制数,请联系客服");
								activityService.updateAutoRegist(autoRegist);
								continue;
							}*/
						} else {
							autoRegist.setCardLog(ResponseCode.USER_NOT_EXIST.getMessage());
							activityService.updateAutoRegist(autoRegist);
							continue;
						}

					} else {
						autoRegist.setCardLog(ResponseCode.USER_IDCARD_FORMAT_ERROR.getMessage());
						activityService.updateAutoRegist(autoRegist);
						continue;
					}
				} catch (Throwable e) {
					autoRegist.setCardLog(String.valueOf(e));
					activityService.updateAutoRegist(autoRegist);
					continue;

				}

			}
		}
		return response;

	}

}
