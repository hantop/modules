package com.fenlibao.p2p.controller.v_4.v_4_2_4.user;

import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.model.entity.*;
import com.fenlibao.p2p.model.enums.CaptchaType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.*;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.PrivateMessageVo;
import com.fenlibao.p2p.model.vo.ShopTreasureInfoVo;
import com.fenlibao.p2p.model.vo.ShopTreasureVo;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.TenderShareService;
import com.fenlibao.p2p.service.bid.impl.NciicDmServiceImpl;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooBindCardService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.sms.SmsExtracterService;
import com.fenlibao.p2p.service.user.AutoBidService;
import com.fenlibao.p2p.service.user.IdCardAuthService;
import com.fenlibao.p2p.service.user.LoginStateService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.email.EmailParam;
import com.fenlibao.p2p.util.email.GenerateLinkUtils;
import com.fenlibao.p2p.util.loader.Sender;
import com.fenlibao.p2p.util.verify.IDCardVerify;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController("v_4_2_4/UserApiController")
@RequestMapping(value = "user", headers = APIVersion.v_4_2_4)
public class UserApiController {

	private static final Logger logger=LogManager.getLogger(UserApiController.class);

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private NciicDmServiceImpl nciicDmService;

	@Resource
	private RedpacketService redpacketService;

	@Resource
	private LoginStateService loginStateService;

	@Resource
	private TenderShareService tenderShareService;
	@Resource
	private SmsExtracterService smsService;
	@Resource
	private AutoBidService autoBidService;
	@Resource
	private IdCardAuthService idCardAuthService;
	@Resource
	private BaofooBindCardService baofooBindCardService;

	/**
	 * 手机号正则表达式
	 */
	private final String phonePattern = "^(13|14|15|16|17|18)[0-9]{9}$";
	/**
	 * 用户密码正则表达式
	 */
	private final String pwdPattern = "[a-zA-Z0-9]{6,20}";

	@RequestMapping(value = "userAssetInfo", method = RequestMethod.GET)
	HttpResponse userAssetInfo(@ModelAttribute BaseRequestFormExtend formExtend,String versionType) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>();
		if (!formExtend.validate()||StringUtils.isEmpty(versionType)) {
			logger.debug("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else {
			try {
				if(versionType.equals(VersionTypeEnum.CG.getCode())){
					data = userInfoService.getUserAssetsByXW(formExtend.getUserId());
				}else {
					data = userInfoService.getUserAssets(formExtend.getUserId());
				}
				response.setData(data);
			} catch (Exception e) {
				response.setCodeMessage(ResponseCode.FAILURE);
				logger.error(e.getMessage());
			}
		}
		return response;
	}

	/**
	 * @what 实名认证
	 * @param formExtend 基础验证信息
	 * @param idCardFullName	姓名
	 * @param idCardNum	身份证信息
	 * @param bindIdCardForm
	 * @return
	 */
	@RequestMapping(value = "idCard", method = RequestMethod.POST)
	HttpResponse bindIdCard(
			@ModelAttribute BaseRequestFormExtend formExtend,
			@RequestParam(required = true, value = "idCardFullName") String idCardFullName,
			@RequestParam(required = true, value = "idCardNum") String idCardNum,
			BindIdCardForm bindIdCardForm) {

		HttpResponse response = new HttpResponse();
		if (!formExtend.validate()) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);

		} else {

			UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(formExtend.getUserId()));
			if (com.fenlibao.p2p.util.api.DateUtil.StrToDate("2017-12-19", com.fenlibao.p2p.util.api.DateUtil.yyyy_MM_dd).getTime() <= userAccountInfoVO.getRegisterTime().getTime()){
				logger.info("goto_CG");
				response.setCodeMessage(ResponseCode.COMMON_GOTO_CG);
				return response;
			}

			try {

				// AES解密
				String fullName = AES.getInstace().decrypt(idCardFullName);
				if(!StringUtils.isBlank(fullName)){
					bindIdCardForm.setIdCardFullName(StringHelper.trim(fullName));
				}
				String idCard = AES.getInstace().decrypt(idCardNum);

				if(!StringUtils.isBlank(idCard)){
					bindIdCardForm.setIdCardNum(idCard.toUpperCase());
				}

				String mtest = "^[\\u4E00-\\u9FA5]{2,5}(?:·[\\u4E00-\\u9FA5]{2,5})*$";
				if (!bindIdCardForm.getIdCardFullName().matches(mtest))
				{
					response.setCodeMessage("30811", "请输入合法的姓名");
					return response;
				}
				// 验证身份证格式并返回信息
				String idCardValidateResult = IDCardVerify.idCardValidate(bindIdCardForm.getIdCardNum());
				if(idCardValidateResult.equals("YES")) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					int year = calendar.get(Calendar.YEAR);
					int born = Integer.parseInt(bindIdCardForm.getIdCardNum().substring(6, 10));
					if ((year - born) < 16)
					{
						response.setCodeMessage("33001","必须年满16周岁");
						return response;
					}

					UserInfo userInfo = userInfoService.getUser(null, null, String.valueOf(formExtend.getUserId()));
					logger.info("[********UserInfo********]-->userId:" + userInfo.getUserId() + " userName:" + userInfo.getFullName() + " AuthStauts:" + userInfo.getAuthStatus() + " carId:" + userInfo.getIdCardEncrypt());
					if(userInfo != null){
						if("TG".equals(userInfo.getAuthStatus())){
							response.setCodeMessage("11307","用户已经实名认证过！");
							return response;
						}

						//同过用户名到S10_1037 获取保存登录错误次数
						int count = userInfoService.getUserAuthError(formExtend.getUserId());
						if(InterfaceConst.ALLWO_LOGIN_ERROR_TIMES  != count){	//最大校验12次
							if(!nciicDmService.isIdcard(bindIdCardForm.getIdCardNum())){

								boolean keyStatus = Boolean.valueOf(Sender.get("real_name_auth_key"));
								if (keyStatus){
									response = idCardAuthService.realNameAuth(userInfo, bindIdCardForm, response);
								} else {
									idCardAuthService.saveRealNameAuthentication(bindIdCardForm);
								}

							}else {
								response.setCodeMessage("30811","该身份证已认证过！");
							}
						}else {
							response.setCodeMessage("30810","超过实名验证最大错误限制数,请联系客服" );
							return response;
						}
					}else{
						response.setCodeMessage(
								ResponseCode.USER_NOT_EXIST);
					}

				} else {
					response.setCodeMessage(
							ResponseCode.USER_IDCARD_FORMAT_ERROR);
				}
			} catch (Throwable e) {
				response.setCodeMessage(ResponseCode.FAILURE);
			}
		}
		return response;

	}

	@RequestMapping(value = "retrievePassword", method = RequestMethod.PUT)
	HttpResponse retrievePassword(
			@ModelAttribute BaseRequestForm paramForm,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "phoneNum") String phoneNum,
			@RequestParam(required = false, value = "verifyCode") String verifyCode,
			@RequestParam(required = false, value = "platformType") String platformType,
			@RequestParam(required = false, value = "openId") String openId,
			@RequestParam(required = false, value = "newPassword") String newPassword,
			RetrievePasswordForm retrievePasswordForm) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()
				|| StringUtils.isBlank(type)
				|| StringUtils.isBlank(newPassword)) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else {
			// 手机找回密码
			if(type.equals("0")) {
				// AES解码
				phoneNum = AES.getInstace().decrypt(phoneNum);
				if(StringUtils.isBlank(phoneNum) || StringUtils.isBlank(verifyCode)) {
					logger.info("request param is empty");
					response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				} else {
					//校验验证码
					ResponseCode status = this.userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.RETRIEVE_PASSWORD_TYPE), verifyCode);
					if (status == null) {
						// 找回密码
						response = userInfoService.retrievePassword(retrievePasswordForm);
					} else {
						response.setCodeMessage(status.getCode(),
								status.getMessage());
					}
				}
			} else if(type.equals("1")){
				response.setCodeMessage(ResponseCode.USER_NOT_SUPPORT_TP_FIND_PWD);
			}
		}
		return response;
	}


	@RequestMapping(value = "userAccountInfo", method = RequestMethod.GET)
	HttpResponse getUserAccountInfo(@ModelAttribute BaseRequestFormExtend formExtend) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else {
			UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(formExtend.getUserId()));
			if (userAccountInfoVO == null) {
				response.setMessage("用户不存在!");
				response.setCodeMessage(ResponseCode.USER_NOT_EXIST);
			} else {
				// 设置用户token
				userAccountInfoVO.setToken(formExtend.getToken());
				// 获取用户账户信息字段Map
				Map<String, Object> data = new HashMap<>();

				boolean containBaofoo = baofooBindCardService.validateBind(formExtend.getUserId());
				data = userInfoService.getUserAccountInfoDataMap(userAccountInfoVO,1);
				List<Map<String,Object>> bankList = (List<Map<String,Object>>)data.get("bankList");
				if (containBaofoo&&bankList.size()!=0) {
					List<Map<String, Object>> newBankList = checkBaofooCardInfo(bankList);
					data.put("bankList", newBankList);
				}else if(containBaofoo&&bankList.size()==0){//连连未认证，宝付已认证
					List<Map<String, Object>> newBankList = userInfoService.getUserBaofooCardInfo(formExtend.getUserId());
					Map<String, Object> param = newBankList.get(0);
					String bankNum="";
					String bankPhone = "";
					try {
						bankNum = StringHelper.decode((String) param.get("bankNum"));
						bankNum = AES.getInstace().encrypt(bankNum);
						bankPhone =StringUtils.isBlank((String) param.get("bankPhone")) ? null :  AES.getInstace().encrypt((String) param.get("bankPhone"));
					} catch (Throwable throwable) {
						throwable.printStackTrace();
					}
					param.put("bankNum", bankNum);
					param.put("bankPhone", bankPhone);
					newBankList = new ArrayList<>();
					newBankList.add(param);
					data.put("bankList",newBankList);
				}

				data.put("autoBidding", autoBidService.checkUserAutoBid(formExtend.getUserId()));
				int limitPT = 0;
				if (com.fenlibao.p2p.util.api.DateUtil.StrToDate("2017-12-19", com.fenlibao.p2p.util.api.DateUtil.yyyy_MM_dd).getTime() <= userAccountInfoVO.getRegisterTime().getTime()){
					limitPT = 1;  //注册大于等于2017-12-19
				}
				data.put("limitPT", limitPT); //0不限制， 1限制
				response.setData(data);
			}
		}

		return response;
	}

	private List<Map<String,Object>> checkBaofooCardInfo(List<Map<String,Object>> bankList) {
		Map<String, Object> card = bankList.get(0);
		Map<String, Object> bankParam = new HashMap<>();

		bankParam.put("bankName", card.get("bankName"));

		bankParam.put("bankNum",card.get("bankNum"));

		bankParam.put("bankCode", card.get("bankCode"));

		bankParam.put("bankType", 1);

		bankParam.put("bankCardId", card.get("bankCardId"));

		bankParam.put("bankInfoCompleteFlag", card.get("bankInfoCompleteFlag"));

		bankParam.put("bankAuthStatus", card.get("bankAuthStatus"));

		bankParam.put("bankPhone", card.get("bankPhone"));

		bankList.add(bankParam);

		return bankList;
	}

	/**
	 * 设置用户名
	 */
	@RequestMapping(value = "setUsername", method = RequestMethod.PUT)
	HttpResponse setUsername(@ModelAttribute BaseRequestFormExtend params,
							 @RequestParam(required = false, value = "username") String username) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || StringUtils.isBlank(username)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		if (username.length() > 30) {
			response.setCodeMessage(ResponseCode.COMMON_TEXT_LENGTH_TOO_LONG);
			return response;
		}
		try {
			response = userInfoService.setUserName(String.valueOf(params.getUserId()), username);
		} catch (Exception e) {
			e.printStackTrace();
			response.setCodeMessage(ResponseCode.FAILURE);
		}
		return response;
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
	 * 解密和设置登陆参数
	 * @param loginForm
	 * @throws BadPaddingException
	 */
	private void _decryptAndSetLoginParams(LoginForm loginForm) throws BadPaddingException {
		AES aesInstance = AES.getInstace();
		String username = loginForm.getUsername();
		String phoneNum = loginForm.getPhoneNum();
		String password = loginForm.getPassword();
		// AES解码
		if (StringUtils.isNotBlank(username)) {
			loginForm.setUsername(aesInstance.decrypt2(username));
		} else {
			loginForm.setPhoneNum(aesInstance.decrypt2(phoneNum));
		}
		loginForm.setPassword(aesInstance.decrypt2(password));
	}

	/**
	 * 解密和设置注册参数
	 * @param registerForm
	 * @throws BadPaddingException
	 */
	private void _decryptAndSetRegisterParams(RegisterForm registerForm) throws BadPaddingException{
		AES aesInstance = AES.getInstace();
		// 手机号解密
		String phoneNum = registerForm.getPhoneNum();
		registerForm.setPhoneNum(aesInstance.decrypt2(phoneNum));
		// 密码解密
		String password = registerForm.getPassword();
		registerForm.setPassword(aesInstance.decrypt2(password));
		// 推荐人手机号
		String spreadPhoneNum = registerForm.getSpreadPhoneNum();
		// 如果有推荐人
		if(StringUtils.isNotBlank(spreadPhoneNum)) {
			registerForm.setSpreadPhoneNum(aesInstance.decrypt2(spreadPhoneNum));
		}
	}

	/**
	 * 普通注册
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	HttpResponse register(@ModelAttribute BaseRequestForm paramForm,
						  @RequestParam(required = false, value = "verifyCode") String verifyCode,
						  RegisterForm registerForm) {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate()
				|| StringUtils.isBlank(registerForm.getPhoneNum())
				|| StringUtils.isBlank(registerForm.getPassword())
				|| StringUtils.isBlank(verifyCode)) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else {
			try {
				_decryptAndSetRegisterParams(registerForm);
			} catch (BadPaddingException bpe) {
				logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
				response.setCodeMessage(ResponseCode.COMMON_PARAM_TYPE_WRONG);
				return response;
			}

			// 如果注册渠道大于25个字符，则截取到第25个
			String channelCode = registerForm.getChannelCode();
			if(StringUtils.isNotBlank(channelCode) && channelCode.trim().length() > 25) {
				channelCode = channelCode.trim().substring(0, 25);
				registerForm.setChannelCode(channelCode);
			}

			String phoneNum = registerForm.getPhoneNum();
			String password = registerForm.getPassword();

			//校验验证码
			ResponseCode status = this.userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.REGISTER_TYPE), verifyCode);
			if (status == null) {
				// 推荐人手机号
				String spreadPhoneNum = registerForm.getSpreadPhoneNum();
				if(StringUtils.isNotBlank(spreadPhoneNum)) {
					// 判断手机号与推荐人手机号是否一致
					if(phoneNum.equals(spreadPhoneNum)) {
						response.setCodeMessage(ResponseCode.USER_REFERRAL_PHONENUM_SAME);
						return response;
					}
				}
				// 验证密码格式
				if (password.matches(pwdPattern)) {
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
								userInfo = userInfoService.register(paramForm, registerForm);
							} catch (Exception e) {
								response.setCodeMessage(ResponseCode.FAILURE);
								logger.error("[UserApiController.register]" + e.getMessage(), e);
								isException = true;
							}
							if(!isException) {
								String userId = userInfo.getUserId();
								// 发放注册奖励
								userInfoService.grantAwardRegister(phoneNum, userId);
								// 发放现金红包
//								grantRedPackets(phoneNum, userId);
								// 发放体验金
//								grantExperienceGold(phoneNum,userId);
								tenderShareService.grantRedEnvelopeForRegister(userId, phoneNum); //发放未注册前领取的红包

								try {
									// 查询用户账户信息
									String clientType = paramForm.getClientType();
									UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(userId);
									// 登陆创建token
									String token = loginStateService.saveLoginToken(clientType,userId);

									if (StringUtils.isEmpty(token)) {
										response.setCodeMessage(
												ResponseCode.USER_REGISTER_SUCCESS_RELOGIN
										);
									} else {
										userAccountInfoVO.setToken(token);
										userInfoService.updateUserFirstLoginState(Integer.valueOf(userAccountInfoVO.getUserId()));
										// 返回信息
										Map<String, Object> resultMap = userInfoService.getUserBaseAccountInfoDataMap(userAccountInfoVO,1);
										response.setData(resultMap);
									}
								} catch (Exception e) {
									// 注册成功，但redis写入token失败。
									response.setCodeMessage(
											ResponseCode.USER_REGISTER_SUCCESS_RELOGIN
									);
									logger.error("[UserApiController.register.redistoken]", e);
								}
							}
						} else {
							// 手机号码已注册
							response.setCodeMessage(ResponseCode.USER_PHONE_REGISTERED);
						}
					} else {
						// 手机号码格式不正确
						response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
					}
				} else {
					// 密码格式错误
					response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
				}
			} else {
				response.setCodeMessage(status.getCode(),
						status.getMessage());
			}
		}
		return response;
	}

//	/** 发放体验金
//	 * @param phoneNum
//	 * @param userId
//	 */
//	private void grantExperienceGold(String phoneNum, String userId) {
//		// 体验金类型：注册体验金
//		int expType = InterfaceConst.REGISTER_EXPERIENCEGOLD;
//		List<ExperienceGoldInfo> experienceGoldInfos = null;
//		// 增加用户的体验金
//		try {
//			// 获取注册体验金设置信息
//			experienceGoldInfos = experienceGoldService.getActivityByType(expType);
//			if(experienceGoldInfos != null && experienceGoldInfos.size() > 0) {
//				//发放体验金
//				experienceGoldService.addUserExperienceGold(experienceGoldInfos, userId, phoneNum);
//			}
//		} catch (Exception e) {
//			String message = "[注册体验金异常：]" + e.getMessage();
//			logger.error(message, e);
//			StringBuffer stringBuff = new StringBuffer();
//			for (int i = 0; i < experienceGoldInfos.size() ; i++) {
//				ExperienceGoldInfo ee = experienceGoldInfos.get(i);
//				stringBuff.append(ee.getExpId()).append("|");
//			}
//			HashMap<String, Object> errMap = new HashMap<>();
//			errMap.put("userId", userId);
//			errMap.put("expId", stringBuff.toString());
//			errMap.put("userExpId", null);
//			errMap.put("bidId",null);
//			errMap.put("message", message);
//			errMap.put("redType", FeeCode.CACH_EXPERIENCEGOLD);
//			logger.error("[注册体验金插入参数:]" + errMap.toString(), e);
//		}
//	}

	private void addRedpacketReturncach(String phoneNum, String userId) {
		// 红包类型：注册返现红包
		int redpacketTypeReturncach = InterfaceConst.REDPACKET_REGISTERRETURNCACH;
		List<UserRedPacketInfo> userRedPacketInfos = null;
		// 增加用户的返现红包
		try {
			// 获取注册红包设置信息
			userRedPacketInfos = redpacketService.getActivityRedBagByType(redpacketTypeReturncach);
			if(userRedPacketInfos != null && userRedPacketInfos.size() > 0) {
				String smsTemplate = Sender.get("sms.register.fxhb.content");
				String letterSuffix = Sender.get("znx.suffix.content");
				redpacketService.addUserRedpackets(userRedPacketInfos, userId, phoneNum, smsTemplate, letterSuffix, true);
			}
		} catch (Exception e) {
			String message = "[注册返现红包异常：]" + e.getMessage();
			logger.error(message, e);
			StringBuffer stringBuff = new StringBuffer();
			for (int i = 0; i < userRedPacketInfos.size() ; i++) {
				stringBuff.append(userRedPacketInfos.get(i).getHbId()).append("|");
			}
			HashMap<String, Object> errMap = new HashMap<>();
			errMap.put("userId", userId);
			errMap.put("redpacket_id", stringBuff.toString());
			errMap.put("userRedpacketId", null);
			errMap.put("bidId",null);
			errMap.put("message", message);
			errMap.put("redType", FeeCode.CACH_REDPACKET);
			try {
				redpacketService.recordRedpackExceptionLog(errMap);
			} catch (Exception e1) {
				logger.error("[注册返现红包插入参数:]" + errMap.toString(), e1);
			}
		}
	}

//	private void grantRedPackets(String phoneNum, String userId) {
//		// 红包类型：注册现金红包
//		int redpacketTypeCach = InterfaceConst.REDPACKET_REGISTERCACH;
//		List<UserRedPacketInfo> userRedPacketInfos = null;
//		try {
//			// 根据红包类型查询现金红包并发放
//			userRedPacketInfos = redpacketService.getActivityRedBagByType(redpacketTypeCach);
//
//			if(userRedPacketInfos != null && userRedPacketInfos.size() > 0) {
//				redpacketService.grantRedPackets(userRedPacketInfos, userId, phoneNum, FeeCode.CACH_REDPACKET);
//			}
//		} catch (Exception e) {
//			String message = "[注册现金红包异常：]" + e.getMessage();
//			logger.error(message, e);
//			HashMap<String, Object> errMap = new HashMap<>();
//			errMap.put("userId", userId);
//			errMap.put("userRedpacketId", null);
//			errMap.put("bidId",null);
//			errMap.put("message", message);
//			errMap.put("redType", FeeCode.REGISTERRETURNCACH_REDPACKET);
//			try {
//				redpacketService.recordRedpackExceptionLog(errMap);
//			} catch (Exception e1) {
//				logger.error("[注册现金红包插入参数:]" + errMap.toString(), e1);
//			}
//		}
//	}

	/**
	 * 修改登陆密码
	 */
	@RequestMapping(value = "modifyPassword", method = RequestMethod.PUT)
	HttpResponse modifyPassword(@ModelAttribute BaseRequestFormExtend formExtend,
								@RequestParam(required = false, value = "oldPassword") String oldPassword,
								@RequestParam(required = false, value = "newPassword") String newPassword,
								ModifyPasswordForm modifyPasswordForm) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()
				|| StringUtils.isBlank(oldPassword)
				|| StringUtils.isBlank(newPassword)) {
			logger.info("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else {
			response = userInfoService.modifyPassword(modifyPasswordForm);
		}

		return response;
	}

	/**
	 * 修改手机号
	 * @return
	 */
	@RequestMapping(value = "phone")
	HttpResponse bindPhoneNum(HttpServletRequest request, @ModelAttribute BaseRequestForm  paramForm,
							  @RequestParam(required = false, value = "token") String token,
							  @RequestParam(required = false, value = "userId") String userId,
							  @RequestParam(required = false, value = "phoneNum") String phoneNum,
							  @RequestParam(required = false, value = "verifyCode") String verifyCode){
		logger.info("request paramter[userId:{},phoneNum:{},verifyCode:{}]",new Object[]{userId,phoneNum,verifyCode});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				logger.info("request param is empty");
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(verifyCode)||StringUtils.isEmpty(phoneNum)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			String phone=AES.getInstace().decrypt(phoneNum);//解密后的手机号

			int count=Integer.parseInt(Sender.get("phone.varifycode.maxerror.count"));//最大验证错误次数
			int ecount=userInfoService.matchVerifyCodeErrorCount(1, phone);//当日该手机与验证码匹配错误次数
			if(ecount>=count){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
				return response;
			}

			//校验手机号是否已存在
			if(userInfoService.checkPhone(phone)){
				response.setCodeMessage(ResponseCode.USER_PHONENUM_IS_EXIST);
				return response;
			}

			//校验验证码
			ResponseCode status = this.userInfoService.verifySmsCode(phone, String.valueOf(InterfaceConst.BIND_PHONE_TYPE), verifyCode);
			if (status == null) {
				//更新手机号
				userInfoService.updatePhoneSecurity(phone, userId, Status.TG.toString());
			} else {
				response.setCodeMessage(status.getCode(),
						status.getMessage());
			}
		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.bindPhoneNum]"+ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 手机号解除绑定
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "unbindPhoneNum",method=RequestMethod.POST)
	HttpResponse unbindPhoneNum(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
								@RequestParam(required = false, value = "token") String token,
								@RequestParam(required = false, value = "userId") String userId){
		logger.info("request paramter[userId:{}]",new Object[]{userId});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				logger.info("request param is empty");
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}
			if(StringUtils.isEmpty(userId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}


			userInfoService.updatePhoneSecurity("", userId, Status.BTG.toString());

		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.unbindPhoneNum]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 绑定邮箱（发送验证邮件）
	 * @return
	 */
	@RequestMapping(value = "bindEmail",method=RequestMethod.POST)
	HttpResponse bindEmail(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
						   @RequestParam(required = false, value = "token") String token,
						   @RequestParam(required = false, value = "userId") String userId,
						   @RequestParam(required = false, value = "email") String email){
		logger.info("request paramter[userId:{},email:{}]",new Object[]{userId,email});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				logger.info("request param is empty");
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(email)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			int count=Integer.parseInt(Sender.get("phone.varifycode.maxerror.count"));//最大验证错误次数
			int ecount=userInfoService.matchVerifyCodeErrorCount(1, email);//当日该邮箱与验证码匹配错误次数
			if(ecount>=count){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
				return response;
			}

			if(userInfoService.checkEmail(email)){
				response.setCodeMessage(ResponseCode.USER_EMAIL_IS_EXIST);
				return response;
			}

			//邮件发送
			this.userInfoService.insertSendEmail(userId, email, CommonTool.getDomain(request));

			String loginUrl=EmailSite.get(email.substring(email.lastIndexOf("@")+1));
			response.getData().put("url", loginUrl);
		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.bindEmail]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 绑定邮箱（验证邮件链接）
	 * @return
	 */
	@RequestMapping(value = "checkEmailAuth")
	public ModelAndView checkEmailAuth(HttpServletRequest request,ModelMap model,
									   @RequestParam(required = false, value = "userId") String userId,
									   @RequestParam(required = false, value = "type") String type,
									   @RequestParam(required = false, value = "email") String email,
									   @RequestParam(required = false, value = "checkCode") String checkCode){
		logger.info("request paramter[userId:{},type:{},email:{},checkCode:{}]",new Object[]{userId,type,email,checkCode});
		try{
			//是否已通过邮箱验证
			UserSecurityAuthentication authentication=this.userInfoService.getUserSecurity(userId);
			if(authentication.getEmailAuth().equals(Status.TG.name())){
				logger.info("已通过邮箱认证,无须再认证");
				model.put("msg", "您已通过认证,无须再认证!");
				return new ModelAndView("emailCheckResult",model);
			}

			SendEmail sendEmail=this.userInfoService.getLastEmail(type, userId);//最近一条邮箱验证邮件
			if(null==sendEmail){
				logger.info("没有发送该邮件");
				model.put("msg", "该验证链接异常，请重进行新邮箱认证！");
				return new ModelAndView("emailCheckResult",model);
			}

			long exceedTime = sendEmail.getOutDate().getTime();
			if (exceedTime <= System.currentTimeMillis()) {
				logger.info("该验证链接已过期");
				model.put("msg", "该验证链接已过期,请重进行新邮箱认证！");
				return new ModelAndView("emailCheckResult",model);
			}

			EmailParam param=new EmailParam();
			param.setType(sendEmail.getType());
			param.setUserId(String.valueOf(sendEmail.getSender()));
			param.setExceedTime(sendEmail.getOutDate());
			param.setEmail(email);
			if (!GenerateLinkUtils.verifyCheckcode(param, checkCode)) {
				logger.info("该验证链接为非法请求");
				model.put("msg", "该验证链接异常,请重进行新邮箱认证！");
				return new ModelAndView("emailCheckResult",model);
			}

			//绑定邮箱
			this.userInfoService.updateEmailSecurity(email, userId, Status.TG.name());
			model.put("msg", "邮箱认证成功！");
		}catch(Exception ex) {
			logger.error("[UserApiController.bindEmail]" + ex.getMessage(), ex);
			model.put("msg", email + "邮箱认证失败！");
		}
		return new ModelAndView("emailCheckResult",model);
	}

	/**
	 * 解绑邮箱认证
	 * @param token
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "unbindEmail",method=RequestMethod.POST)
	HttpResponse unbindEmail(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
							 @RequestParam(required = false, value = "token") String token,
							 @RequestParam(required = false, value = "userId") String userId){
		logger.info("request paramter[userId:{}]",new Object[]{userId});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				logger.info("request param is empty");
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			if(StringUtils.isEmpty(userId)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}


			userInfoService.updateEmailSecurity("", userId, Status.BTG.toString());

		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.unbindEmail]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 上传用户头像
	 * @param request
	 * @param userId
	 * @param userPicUrl
	 * @return
	 */
	@RequestMapping(value = "avatar",method=RequestMethod.POST)
	HttpResponse avatar(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
						@RequestParam(required = false, value = "token") String token,
						@RequestParam(required = false, value = "userId") String userId,
						@RequestParam(required = false, value = "type") String type,
						@RequestParam(required = false, value = "userPicUrl") String userPicUrl,
						@RequestParam(required = false, value = "file") MultipartFile file){
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(type)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			String fileUrl=userInfoService.uploadUserPic(Integer.parseInt(userId), file, Integer.parseInt(type), userPicUrl);
			response.getData().put("userUrl", fileUrl);

		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.userPicUpload]"+ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 获取手机验证码
	 * @return
	 */
	@RequestMapping(value = "getVerifyCode",method=RequestMethod.POST)
	HttpResponse getVerifyCode(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
							   @RequestParam(required = false, value = "token") String token,
							   @RequestParam(required = false, value = "phoneNum") String phoneNum,
							   @RequestParam(required = false, value = "type") String type,
							   @RequestParam(required = false, value = "userIp") String userIp){
		logger.info("request paramter[phoneNum:{},userIp:{},clientType:{}]",new Object[]{phoneNum,userIp,paramForm.getClientType()});

		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
				return response;
			}

			//暂时先不校验token，token校验要和userId一起
//			if(StringUtils.isNotEmpty(token)){
//				if(!userTokenService.checkToken(token)){
//					response.setCodeMessage(ResponseCode.NOT_VALID_TOKEN.getCode(),ResponseCode.NOT_VALID_TOKEN.getMessage());
//					return response;
//				}
//			}

			if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(type)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			String phone=AES.getInstace().decrypt(phoneNum);//解密后的手机号

			int codeType = 0;

			if(StringUtils.isNumeric(replaceBlank(type))){
				codeType = Integer.valueOf(replaceBlank(type));
			}

			/**
			 * 注册：手机号不能已注册
			 * 找回密码：手机号要已注册
			 */
			boolean isExist = userInfoService.checkPhone(phone);
			if(codeType == InterfaceConst.REGISTER_TYPE){
				if(isExist){
					response.setCodeMessage(ResponseCode.USER_PHONE_REGISTERED);
					return response;
				}
			}
			if(codeType == InterfaceConst.RETRIEVE_PASSWORD_TYPE){
				if(!isExist){
					response.setCodeMessage(ResponseCode.USER_NOT_EXIST.getCode(),ResponseCode.USER_NOT_EXIST.getMessage());
					return response;
				}
			}

			if(!paramForm.getClientType().equals(String.valueOf(Constant.CLIENTTYPE_WAP))&&!paramForm.getClientType().equals(String.valueOf(Constant.CLIENTTYPE_WEIXIN))){
				userIp = CommonTool.getIpAddr(request);
			}

			/**
			 * 手机号：半小时 3次       一天5次
			 *   IP：半小时5次        一天20次
			 */
			Date halfhour = DateUtil.minuteAdd(new Date(), -30);
			Date hour = DateUtil.minuteAdd(new Date(), -60);
			Date curDate = DateUtil.getDateFromDate(DateUtil.nowDate());
			int phoneSendCountByHour = this.userInfoService.getSendSmsCount(null, phone, hour, null);
			int ipSendCountByHalfour = this.userInfoService.getSendSmsCount(userIp, null, halfhour, null);
			int phoneSendCountByDay = this.userInfoService.getSendSmsCount(null, phone, null, curDate);
			int ipSendCountByDay = this.userInfoService.getSendSmsCount(userIp, null,null,curDate);

			if(phoneSendCountByDay >= Integer.parseInt(Sender.get("phone.send.day.maxcount"))||
					ipSendCountByDay >= Integer.parseInt(Sender.get("ip.send.day.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
				return response;
			}

			if(phoneSendCountByHour >= Integer.parseInt(Sender.get("phone.send.hour.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_HOUR_MAXTIME);
				return response;
			}

			if(ipSendCountByHalfour >= Integer.parseInt(Sender.get("ip.send.halfhour.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_HALFOUR_MAXTIME);
				return response;
			}


			int count=userInfoService.getSendPhoneCount(phone, codeType);
			if(count>Integer.parseInt(Sender.get("user.send.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
				return response;
			}

			//所有验证码发送间隔时间60秒，否则容易造成短信通道被封
			SmsValidcode smsValidCode = userInfoService.getLastSmsCode(phone,String.valueOf(codeType));
			if(smsValidCode!= null){
				long curTime = System.currentTimeMillis();
				if(curTime < (smsValidCode.getOutTime().getTime()-(30*60*1000)+60000)){
					response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_FREQUENTLY);
					return response;
				}
			}

			UserInfo userInfo=this.userInfoService.getUser(phone, null, null);

			if(userInfo!=null){
				userInfoService.sendVerifySms(phone, codeType,userInfo.getUserId(),userIp);
			}else{
				userInfoService.sendVerifySms(phone, codeType,null,userIp);
			}

//			UserInfo userInfo=this.userInfoService.getUser(phone, null, null);
//			
//			if(null!=userInfo){
//				//发送手机认证短信的次数
//				int count=userInfoService.getSendPhoneCount(Integer.parseInt(userInfo.getUserId()), Integer.parseInt(type));
//				if(count>Integer.parseInt(Sender.get("user.send.maxcount"))){
//					response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
//                    return response;
//				}
//				userInfoService.sendVerifySms(phone, Integer.parseInt(type),userInfo.getUserId());
//			}else{
//				userInfoService.sendVerifySms(phone, Integer.parseInt(type),null);
//			}
		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.getVerifyCode]"+ex.getMessage(), ex);
		}
		return response;
	}

	public String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 登陆验证接口
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public HttpResponse login(
			@ModelAttribute BaseRequestForm baseForm,
			@ModelAttribute LoginForm loginForm,
			@RequestParam(required = false, value = "username") String username,
			@RequestParam(required = false, value = "password") String password,
			@RequestParam(required = false, value = "phoneNum") String phoneNum) {
		HttpResponse response = new HttpResponse();
		if (!baseForm.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else if(StringUtils.isBlank(username) && StringUtils.isBlank(phoneNum)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		} else if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(phoneNum)) {
			response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
		} else if (StringUtils.isEmpty(password)) {
			response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
		} else {
			try {
				// 解密和设置登陆参数
				decryptAndSetLoginParams(loginForm);
				// 获取用户账户信息
				UserAccountInfoVO userAccountInfoVO = userInfoService.
						getUserAccountInfoByPhoneNumOrUsername(loginForm.getUsername(), loginForm.getPhoneNum());
				if (userAccountInfoVO == null) {
					response.setCodeMessage(ResponseCode.USER_ACCOUNT_NOT_EXIST);
				} else {
					String pwd =userAccountInfoVO.getPassword();
					// 密码进行AES解码进行加密后进行对比
					if (PasswordCryptUtil.crypt(loginForm.getPassword()).equals(pwd)) {
//                        String userId = userAccountInfoVO.getUserId();
						// 进行登录，创建token
//                        UserToken token = userInfoService.login(paramForm.getDeviceId(), baseForm.getClientType(), userId);
//                        userAccountInfoVO.setToken(token.getToken());

						/********************使用redis存储登录态********************/
						String result = loginStateService.saveLoginToken(baseForm.getClientType(), userAccountInfoVO.getUserId());
						if (StringUtils.isNotBlank(result)) {
							userAccountInfoVO.setToken(result);

							//判断用户是否第一次登陆  add by laubrence  2016-4-13 16:04:41
							boolean isFirstLoginState = userInfoService.isUserFirstLogin(Integer.valueOf(userAccountInfoVO.getUserId()));
							if(isFirstLoginState){
								userInfoService.updateUserFirstLoginState(Integer.valueOf(userAccountInfoVO.getUserId()));
							}
							// 获取用户账户信息字段Map
							Map<String, Object> data = userInfoService.getUserBaseAccountInfoDataMap(userAccountInfoVO,1);
							response.setData(data);
						} else {
							response.setCodeMessage(ResponseCode.FAILURE);
						}
					} else {
						response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR_);
					}
				}
			} catch (Exception e) {
				logger.error("[UserApiController.login]", e);
				response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
			}
		}
		return response;
	}

	/**
	 * 解密和设置登陆参数
	 * @param loginForm
	 * @throws BadPaddingException
	 */
	private void decryptAndSetLoginParams(LoginForm loginForm) throws BadPaddingException {
		AES aesInstance = AES.getInstace();
		String username = loginForm.getUsername();
		String phoneNum = loginForm.getPhoneNum();
		String password = loginForm.getPassword();
		// AES解码
		if (StringUtils.isNotEmpty(username)) {
			loginForm.setUsername(aesInstance.decrypt2(username));
		} else {
			loginForm.setPhoneNum(aesInstance.decrypt2(phoneNum));
		}
		loginForm.setPassword(aesInstance.decrypt2(password));
	}

	/**
	 * 修改昵称接口
	 * @param paramForm
	 * @param nickname
	 * @return
	 */
	@RequestMapping(value = "modifyNickname", method = RequestMethod.PUT)
	public HttpResponse modifyNickName(@ModelAttribute BaseRequestFormExtend  paramForm,
									   @RequestParam(required = false, value="nickname") String nickname) {
		logger.debug(paramForm + "nickname=" + nickname);
		HttpResponse respone = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isBlank(nickname)) {
			respone.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return respone;
		}
		if (nickname.length() > 30) {
			respone.setCodeMessage(ResponseCode.COMMON_TEXT_LENGTH_TOO_LONG);
			return respone;
		}
		return userInfoService.modifyNickName(paramForm.getUserId(), nickname);
	}

	/**
	 * 验证接口(银行卡删除,手机邮箱第三方账号解绑验证)
	 * @param paramForm
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "verify", method = RequestMethod.PUT)
	public HttpResponse verify(@ModelAttribute BaseRequestFormExtend  paramForm,
							   @RequestParam(required = false, value="password") String password) {
		if (!paramForm.validate()) {
			HttpResponse respone = new HttpResponse();
			respone.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return respone;
		}

		HttpResponse response = new HttpResponse();;
		try {
			boolean result = userInfoService.verify(paramForm.getUserId(), password);
			if (!result) {
				response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR_);
			}
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
		}

		return response;
	}

	/**
	 * 获取用户消息接口
	 * @param paramForm
	 * @param userId
	 * @param timestamp
	 * @param isUp
	 * @return
	 */
	@RequestMapping(value = "message/list", method = RequestMethod.GET)
	public HttpResponse messageList(@ModelAttribute BaseRequestForm  paramForm,
									@RequestParam(required = false, value="token") String token,
									@RequestParam(required = false, value="userId") String userId,
									@RequestParam(required = false, value="timestamp") String timestamp,
									@RequestParam(required = false, value="isUp") String isUp,
									@RequestParam(required = false, value="versionType") String versionType){
		logger.info("request paramter[userId:{},timestamp:{},isUp:{}]",new Object[]{userId,timestamp,isUp});
		HttpResponse response = new HttpResponse();
		try{
			if (!paramForm.validate()) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(isUp)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			VersionTypeEnum vte = VersionTypeEnum.PT;
			if(VersionTypeEnum.CG.getCode().equals(versionType)){
				vte = VersionTypeEnum.CG;
			}
			List<PrivateMessage> list=this.userInfoService.getUserMessage(userId, timestamp, isUp, vte);

 			List<PrivateMessageVo> resultList = new ArrayList<PrivateMessageVo>();

			for (PrivateMessage mes : list) {
				PrivateMessageVo vo = new PrivateMessageVo();
				vo.setMessageId(mes.getId());
				vo.setTitle(mes.getTitle());
				vo.setContent(mes.getContent());
				vo.setTimestamp(mes.getSendTime().getTime());
				if(mes.getStatus().equals(Status.WD.name())){
					vo.setStatus(1);
				}else{
					vo.setStatus(0);
				}
				resultList.add(vo);
			}

			response.getData().put("messageList", resultList);
		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.messageList]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 用户未读消息数量
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "message/unread/count", method = RequestMethod.GET)
	public HttpResponse messageUnderCount(@ModelAttribute BaseRequestForm  paramForm,
										  @RequestParam(required = false, value="token") String token,
										  @RequestParam(required = false, value="userId") String userId,
										  @RequestParam(required = false, value="versionType") String versionType){
		logger.info("request paramter[userId:{}]",new Object[]{userId});
		HttpResponse response = new HttpResponse();
		try{
			if (!paramForm.validate()||StringUtils.isEmpty(userId)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			VersionTypeEnum vte = VersionTypeEnum.PT;
			if(VersionTypeEnum.CG.getCode().equals(versionType)){
				vte = VersionTypeEnum.CG;
			}
			//获取用户未读消息数量
			int unreadCount = this.userInfoService.getUserMessageCount(userId, Status.WD.name(), vte);
			response.getData().put("unreadCount", unreadCount);
		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.messageUnderCount]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 删除用户消息
	 * @param paramForm
	 * @param token
	 * @param userId     用户ID（需要）
	 * @param messageIds 用户消息ID，多个ID用户英文逗号拼接(不传参则删除用户所有消息)
	 * @return
	 */
	@RequestMapping(value = "message", method = RequestMethod.DELETE)
	public HttpResponse deleteUserMessage(@ModelAttribute BaseRequestForm  paramForm,
										  @RequestParam(required = false, value="token") String token,
										  @RequestParam(required = false, value="userId") String userId,
										  @RequestParam(required = false, value="messageIds") String messageIds){
		logger.info("request paramter[userId:{}]",new Object[]{userId});
		HttpResponse response = new HttpResponse();
		try{
			if (!paramForm.validate()||StringUtils.isEmpty(userId)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			this.userInfoService.updateUserMessageStatus(messageIds, Integer.parseInt(userId), Status.SC.name());
		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.deleteUserMessage]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 开店宝计划列表
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param timestamp
	 * @param isUp 获取数据方式 0:获取最新数据  , 1: 查询历史记录
	 * @return
	 */
	@RequestMapping(value = "kdb/list", method = RequestMethod.GET)
	public HttpResponse getBid(@ModelAttribute BaseRequestForm  paramForm,
							   @RequestParam(required = false, value="token") String token,
							   @RequestParam(required = false, value="userId") String userId,
							   @RequestParam(required = false, value="timestamp") String timestamp,
							   @RequestParam(required = false, value="isUp") String isUp){
		HttpResponse response = new HttpResponse();
		try {
			if (!paramForm.validate()) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			List<ShopTreasureVo> voList=this.userInfoService.getShopTreasureList(timestamp, userId, isUp);
			response.getData().put("kdbPlantList", voList);
		} catch (Exception ex) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.getBid]"+ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 获取开店宝详情
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "kdb/detail", method = RequestMethod.GET)
	public HttpResponse getBidDetail(@ModelAttribute BaseRequestForm  paramForm,
									 @RequestParam(required = false, value="token") String token,
									 @RequestParam(required = false, value="userId") String userId,
									 @RequestParam(required = false, value="kdbPlantId") String kdbPlantId){
		HttpResponse response = new HttpResponse();
		try {
			if (!paramForm.validate()||StringUtils.isEmpty(kdbPlantId)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}

			ShopTreasureInfoVo vo=this.userInfoService.getShopTreasureInfo(kdbPlantId, userId);
			if(null==vo){
				response.setCodeMessage(ResponseCode.BID_DETAILS_EMPTY);
				return response;
			}

			response.setData(CommonTool.toMap(vo));
		} catch (Exception ex) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[UserApiController.getBidDetail]"+ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 校验用户真实姓名
	 * @param params
	 * @param identityName
	 * @param identityNo
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = "realname/auth", method = RequestMethod.GET)
	public HttpResponse validateRealName(BaseRequestFormExtend params, String identityName, String identityNo,
										 String captcha, String phoneNum) {
		HttpResponse response = new HttpResponse();
		try {
			if (!params.validate() || !StringUtils.isNoneBlank(phoneNum, captcha, identityName, identityNo)) {
				throw new BusinessException(ResponseCode.EMPTY_PARAM);
			}
			identityNo = AES.getInstace().decrypt(identityNo);
			identityName = AES.getInstace().decrypt(identityName);
			phoneNum = AES.getInstace().decrypt(phoneNum);
			smsService.captchaValidate(phoneNum, captcha, CaptchaType.RESET_TRADE_PASSWORD.getCode().toString());
			boolean isTrue = userInfoService.verifyIdentity(identityNo, identityName, params.getUserId());
			if (!isTrue) {
				throw new BusinessException(ResponseCode.USER_IDENTITY_VERIFY_FAIL);
			}
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
			logger.debug("用户实名校验失败,userId=[{}],identityName=[{}],identityNo=[{}]", params.getUserId(), identityName, identityNo);
		} catch (Throwable e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("用户实名校验失败,userId=[{}],identityName=[{}],identityNo=[{}]", params.getUserId(), identityName, identityNo);
			logger.error("用户实名校验失败", e);
		}
		return response;
	}

	@RequestMapping(value = "cgBankCard", method = RequestMethod.GET)
	public HttpResponse getCgBankCard(BaseRequestFormExtend params) {
		HttpResponse response = new HttpResponse();
		try {
			if (!params.validate()) {
				throw new BusinessException(ResponseCode.EMPTY_PARAM);
			}
			Map<String, Object> xwcgInfo = userInfoService.getXWCardInfo(params.getUserId());
			response.getData().put("xwcgInfo", xwcgInfo);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("获取用户存管银行卡信息失败,userId=[{}]", params.getUserId());
			logger.error("获取用户存管银行卡信息失败", e);
		}
		return response;
	}


}

