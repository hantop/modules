package com.fenlibao.p2p.controller.v_1.v_1_0_0.user;

import com.alibaba.fastjson.JSONObject;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.model.entity.SendEmail;
import com.fenlibao.p2p.model.entity.SmsValidcode;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserSecurityAuthentication;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.*;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.trade.config.PayConfig;
import com.fenlibao.p2p.model.vo.PlatformInfoVo;
import com.fenlibao.p2p.model.vo.ShopTreasureInfoVo;
import com.fenlibao.p2p.model.vo.ShopTreasureVo;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.bid.impl.NciicDmServiceImpl;
import com.fenlibao.p2p.service.channel.UserOriginService;
import com.fenlibao.p2p.service.experiencegold.IExperienceGoldService;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooBindCardService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.salary.SalaryService;
import com.fenlibao.p2p.service.user.IdCardAuthService;
import com.fenlibao.p2p.service.user.LoginStateService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.email.EmailParam;
import com.fenlibao.p2p.util.email.GenerateLinkUtils;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Message;
import com.fenlibao.p2p.util.loader.Sender;
import com.fenlibao.p2p.util.verify.IDCardVerify;
import com.fenlibao.ud.config.DemoConfig;
import com.fenlibao.ud.pojo.AntifraudRequest;
import com.fenlibao.ud.util.HttpRequestSimple;
import com.fenlibao.ud.util.ParameterFactory;
import com.fenlibao.ud.util.SignatureHelper;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController("v_1_0_0/UserApiController")
@RequestMapping("user")
public class UserApiController {
	private static final Logger logger=LogManager.getLogger(UserApiController.class);

	@Resource
	private UserInfoService userInfoService;
	@Resource
	private UserOriginService userOriginService;
	@Resource
	private UserTokenService userTokenService;

	@Resource
	private NciicDmServiceImpl nciicDmService;

	@Resource
	private FinacingService finacingService;

	@Resource
	private SalaryService salaryService;

	@Resource
	private RedpacketService redpacketService;

    @Resource
    private LoginStateService loginStateService;

	@Resource
	private IExperienceGoldService experienceGoldService;

	@Resource
	private BaofooBindCardService baofooBindCardService;

	@Resource
	private IdCardAuthService idCardAuthService;

	//支付配置信息
	private PayConfig payConfig = ConfigFactory.create(PayConfig.class);

	/**
	 * 手机号正则表达式
	 */
	private final String phonePattern = "^(13|14|15|16|17|18)[0-9]{9}$";
	/**
	 * 用户密码正则表达式
	 */
	private final String pwdPattern = "[a-zA-Z0-9]{6,20}";

	@RequestMapping(value = "userAssetInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	HttpResponse userAssetInfo(@ModelAttribute BaseRequestFormExtend formExtend, VersionTypeEnum versionType) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>();
		if (!formExtend.validate()||versionType == null) {
			logger.debug("request param is empty");
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
		} else {
			try {
				if(versionType == VersionTypeEnum.CG){
					data = userInfoService.getUserAssetsByXW(formExtend.getUserId());
				}else {
					data = userInfoService.getUserAssets(formExtend.getUserId());
				}
				//Map<String, Object> data = userInfoService.getUserAssets(formExtend.getUserId());

				/*int isDepository = userInfoService.isDepository(formExtend.getUserId());
				HXBalanceVO balanceVO =null;
				if (isDepository > 0){//开通了存管的账户
                    //资产概况包含存管部分
                    try {
                        balanceVO = hxCommonService.getBalance(formExtend.getUserId());
                    }catch (Exception e){
                        logger.error("[UserApiController.userAssetInfo]"+e.getMessage(), e);
                    }
                    if(balanceVO!=null) {
                        //加上存管余额
                        BigDecimal balance = new BigDecimal("" + data.get("balance"));
                        data.put("balance",(balance.add(balanceVO.getAvailable())).toString());
                        //存管冻结资金
                        data.put("frozenDepository",(balanceVO.getFrozen()).toString());
                    }
                }*/
				response.setData(data);
			} catch (Exception e) {
				response.setCodeMessage(
						ResponseEnum.RESPONSE_USERASSET_FAILED.getCode(),
						ResponseEnum.RESPONSE_USERASSET_FAILED.getMessage());
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
	@RequestMapping(value = "idCard", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
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

	//保存实名认证信息
	private String saveRealNameAuthentication(BindIdCardForm bindIdCardForm) throws Throwable{
		String codeMsg = "";
		String idCardNum = bindIdCardForm.getIdCardNum().toUpperCase();
		boolean state = nciicDmService.check(idCardNum, bindIdCardForm.getIdCardFullName(),true);
		if(state){

			boolean result = userInfoService.bindIdCard(bindIdCardForm);
		}else {
			codeMsg = "30813";//response.setCodeMessage("30813","实名认证失败！");
		}
		return codeMsg;
	}

	//有盾实名认证
	private Map<String,Object> edRealNameAuthentication(UserInfo userInfo,BindIdCardForm form){

		Map<String,Object> resultMap = null;
		String bankCarNum = form.getIdCardNum();
		String orderRules = "UD" + System.currentTimeMillis() + bankCarNum.substring(bankCarNum.length() - 4);
		Map<String, String> param = ParameterFactory.getShimingParameterMap(form.getIdCardFullName(), form.getIdCardNum(), orderRules);
		AntifraudRequest request = new AntifraudRequest();

		request.setPartnerCode(DemoConfig.PARTNER_CODE);
		request.setNonceStr(RandomStringUtils.randomNumeric(32));
		request.setStrategyCode(DemoConfig.SHIMING_STRATEGY_CODE);
		request.setScenarioCode(DemoConfig.SHIMING_SENARIO_CODE);
		request.setPackageStr(SignatureHelper.generateSignedPackageStr(param));
		request.setSignature(SignatureHelper.generateSignatureFromRequest(DemoConfig.SECRET_KEY, request));
		HttpRequestSimple httpRequestSimple = new HttpRequestSimple();
		String outbuffer = httpRequestSimple.postSendHttp(DemoConfig.SERVICE_URL,request.toString());
		logger.info(outbuffer);
		Map authMap = JSONObject.parseObject(outbuffer);

		if (!authMap.isEmpty() && authMap.size() > 0){
			resultMap = new HashMap();
			String retCode = (String) authMap.get("retCode");
			if(!StringUtils.isBlank(retCode)){
				if(InterfaceConst.SMRZ_CORRECT_RETCODE.equals(retCode)){	//返回正确码

					String score = authMap.get("score").toString();
					if(!StringUtils.isBlank(score)){	//返回验证的结果
						resultMap.put("retCode",retCode);
						resultMap.put("score",score);
						if(!InterfaceConst.SMRZ_ZERO_SCORE.equals(score)){
							try {
								resultMap.put("retMsg",authMap.get("retMsg"));

								Map<String,Object> temMap = new HashMap<>();	//保存操作记录
								temMap.put("result",authMap.get("score"));
								temMap.put("name_card",form.getIdCardFullName());
								temMap.put("ret_code",retCode);
								temMap.put("product_id",null);
								temMap.put("sign_type",null);
								temMap.put("ret_msg",authMap.get("retMsg"));
								temMap.put("outorder_no",orderRules);
								temMap.put("id_card",StringHelper.encode(bankCarNum));
								temMap.put("order_fee",InterfaceConst.SMRZ_ORDER_FEE);
								temMap.put("order_no",null);
								temMap.put("userId",userInfo.getUserId());
								temMap.put("useType", "1"); //用于统计实名认证次数
								userInfoService.insertlianLianAuth(temMap);

							}catch (Throwable throwable){
								throwable.printStackTrace();
							}
						}else{
							resultMap.put("errorMsg",authMap.get("errorMsg"));
						}
					}
				}
			}
		}
		return resultMap;
	}

	//连连实名认证 不使用了
	/*private boolean realNameAuthentication(UserInfo userInfo,BindIdCardForm form) throws Exception{
		boolean flag = false;
		*//**
		 交易发起
		**//*
		try {
			JSONObject json = new JSONObject();
			json.put("merch_id", TrustUtil.MERCH_ID);//您的商户号
			json.put("product_id", TrustUtil.PRODUCT_ID);//业务类型，固定P01
			json.put("sign_type", TrustUtil.SIGN_TYPE);		    //加密类型，MD5或者RSA，根据您商户站的相关设置为主
			String bankCarNum = form.getIdCardNum();
			json.put("outorder_no","FLB" + System.currentTimeMillis() + bankCarNum.substring(bankCarNum.length() - 4));	//订单号，由商户生成的数字或者字母或者数字字母组合的订单号 + 身份证后面4位
			json.put("name_card",form.getIdCardFullName());			//认证人姓名
			json.put("id_card", form.getIdCardNum());		  		//认证人身份证件号
			json.put("sign", TrustUtil.addSign(TrustUtil.genSignData(json), "MD5", "", TrustUtil.SIGN));  //加密串

			HttpRequestSimple http = new HttpRequestSimple();
			String outbuffer = http.postSendJson("https://yintong.com.cn/tradeauthapi/v1/auth/get_auth", json.toString());
			Map authMap = (Map) JSONObject.parseObject(outbuffer);
 			if (!authMap.isEmpty()){

				authMap.put("id_card", StringHelper.encode(form.getIdCardNum()));
				if ("0000".equals(authMap.get("ret_code"))){
					String status = (String) authMap.get("result");
					if(!StringUtils.isBlank(status)){
						authMap.put("userId",Integer.parseInt(userInfo.getUserId()));
						if("1".equals(status.trim())){
							flag = true;
							userInfoService.insertlianLianAuth(authMap);
						}else {
							userInfoService.insertlianLianAuth(authMap);
							//	throw new Throwable("30777");	不是合法的身份证
						}
					}
				}else{
					userInfoService.insertlianLianAuth(authMap);
				}
			}
		}catch (Throwable throwable){
			throwable.printStackTrace();
		}
		return flag;
	}*/

	/**
	 * 2.3.1.	验证手机验证码
	 * @param paramForm
	 * @param phoneNum
	 * @param verifyCode
	 * @param type		1:忘记密码2交易密码
	 * @return
	 */
	@RequestMapping(value = "verifyCode", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
    HttpResponse verifyCode(
			@ModelAttribute BaseRequestForm paramForm,
			@RequestParam(required = false, value = "phoneNum") String phoneNum,
			@RequestParam(required = false, value = "type") Integer type,
			@RequestParam(required = false, value = "verifyCode") String verifyCode) {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			logger.info("request param is empty");
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
        } else {
			// 手机找回密码
        	// AES解码
			phoneNum = AES.getInstace().decrypt(phoneNum);
			if(StringUtils.isBlank(phoneNum) || StringUtils.isBlank(verifyCode)|| type==null) {
				logger.info("request param is empty");
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
            } else {
				//校验验证码
            	ResponseEnum status=null;
            	if(type==1){//找回密码
            		 status = this.userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.RETRIEVE_PASSWORD_TYPE), verifyCode);
            	}else{//重置交易密码
            		 status = this.userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.RESET_TRADE_PASSWORD), verifyCode);
            	}
				if (status != null) {
					response.setCodeMessage(status.getCode(),
							status.getMessage());
				}
			}
        }
		return response;
	}



	@RequestMapping(value = "retrievePassword", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
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
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
        } else {
			// 手机找回密码
			if(type.equals("0")) {
				// AES解码
				phoneNum = AES.getInstace().decrypt(phoneNum);
				if(StringUtils.isBlank(phoneNum) || StringUtils.isBlank(verifyCode)) {
					logger.info("request param is empty");
					response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                } else {
					//校验验证码
					ResponseEnum status = this.userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.RETRIEVE_PASSWORD_TYPE), verifyCode);
					if (status == null) {
						// 找回密码
						response = userInfoService.retrievePassword(retrievePasswordForm);
					} else {
						response.setCodeMessage(status.getCode(),
								status.getMessage());
					}
				}
			} else if(type.equals("1")){
				response.setCodeMessage(Message.STATUS_1028, "暂不支持第三方找回密码");
            }
        }
		return response;
	}


	@RequestMapping(value = "userAccountInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse getUserAccountInfo(@ModelAttribute BaseRequestFormExtend formExtend,VersionTypeEnum versionType,String userType) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()||versionType == null||userType == null) {
			logger.info("request param is empty");
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
		} else {
			UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(formExtend.getUserId()), userType);
			if (userAccountInfoVO == null) {
				response.setMessage("用户不存在!");
				response.setCodeMessage(Message.STATUS_1014, Message.get(Message.STATUS_1014));
			} else {
				// 设置用户token
				userAccountInfoVO.setToken(formExtend.getToken());


				String channelConfig = payConfig.TPPAYMENT_CHANNEL_CODE();
				if (StringUtils.isBlank(channelConfig)) {
					logger.info("[UserApiController.userAccountInfo] : 请检查是否配置 paymentConfig.TPPAYMENT_CHANNEL_CODE");
				}
				//查询Baofoo绑卡
				boolean containBaofoo = baofooBindCardService.validateBind(Integer.valueOf(userAccountInfoVO.getUserId()));
				// 获取用户账户信息字段Map
				Map<String, Object> data = userInfoService.getUserAccountInfoDataMap(userAccountInfoVO,channelConfig,containBaofoo,versionType);

				response.setData(data);
			}
		}

		return response;
	}
	/**
	 * 设置用户名
	 */
	@RequestMapping(value = "setUsername", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
    HttpResponse setUsername(@ModelAttribute BaseRequestFormExtend params,
			@RequestParam(required = false, value = "username") String username) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || StringUtils.isBlank(username)) {
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
            return response;
		}
		if (username.length() > 30) {
			response.setCodeMessage(ResponseEnum.TEXT_LENGTH_TOO_LONG.getCode(),
					ResponseEnum.TEXT_LENGTH_TOO_LONG.getMessage());
            return response;
		}
		try {
			response = userInfoService.setUserName(String.valueOf(params.getUserId()), username);
		} catch (Exception e) {
			e.printStackTrace();
			response.setCodeMessage(Message.STATUS_1031, Message.get(Message.STATUS_1031));
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
	 * 判断用户名是否存在
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "checkPhone", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse checkPhone(@ModelAttribute BaseRequestForm paramForm,
		    @RequestParam(required = false, value = "phoneNum") String phoneNum) {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || StringUtils.isBlank(phoneNum)) {
			logger.info("request param is empty");
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
			return  response;
		}

		try {
			AES aesInstance = AES.getInstace();
			phoneNum = aesInstance.decrypt2(phoneNum);// 手机号解密.
		} catch (BadPaddingException bpe) {
			logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
			response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
			return response;
		}

		// 验证手机号格式
		if (checkPhoneFormat(phoneNum)) {
			// 判断手机号是否存在
			@SuppressWarnings("rawtypes")
			Map paramMap = new HashMap();
			paramMap.put("phoneNum", phoneNum);
			int count = userInfoService.getUserCount(paramMap);
			if(count>0){
				// 手机号码已注册
				response.setCodeMessage(Message.STATUS_1018, Message.get(Message.STATUS_1018));
			}
		} else {
				// 手机号码格式不正确
				response.setCodeMessage(Message.STATUS_1019, Message.get(Message.STATUS_1019));
		}
		return  response;
	}

	/**
	 * 普通注册
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse register(@ModelAttribute BaseRequestForm paramForm,
		    @RequestParam(required = false, value = "verifyCode") String verifyCode,
			RegisterForm registerForm,
			VersionTypeEnum versionType) {
		if(T6110_F06.FZRR.name().equalsIgnoreCase(registerForm.getUserType())){
			return registerForCompany(paramForm, verifyCode, registerForm, versionType);
		}else{
			return registerForPerson(paramForm, verifyCode, registerForm, versionType);
		}
	}

	/**
	 * 个人注册
	 */
	@RequestMapping(value = "registerForPerson", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse registerForPerson(@ModelAttribute BaseRequestForm paramForm,
		    @RequestParam(required = false, value = "verifyCode") String verifyCode,
			RegisterForm registerForm,
			VersionTypeEnum versionType) {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate()
				|| StringUtils.isBlank(registerForm.getPhoneNum())
				|| StringUtils.isBlank(registerForm.getPassword())
				|| StringUtils.isBlank(verifyCode)) {
			logger.info("request param is empty");
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
		} else {
			try {
				_decryptAndSetRegisterParams(registerForm);
			} catch (BadPaddingException bpe) {
				logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
				response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
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
			ResponseEnum status = this.userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.REGISTER_TYPE), verifyCode);
			if (status == null) {
				// 推荐人手机号
				String spreadPhoneNum = registerForm.getSpreadPhoneNum();
				if(StringUtils.isNotBlank(spreadPhoneNum)) {
					// 判断手机号与推荐人手机号是否一致
					if(phoneNum.equals(spreadPhoneNum)) {
						response.setCodeMessage(ResponseEnum.RESPONSE_SPREADPHONENUM_ERROR.getCode(), "手机号不能与推荐人手机号一致");
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
								response.setCodeMessage(
										ResponseEnum.RESPONSE_REGISTER_ERROR.getCode(),
										ResponseEnum.RESPONSE_REGISTER_ERROR.getMessage());
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

								try {
									// 查询用户账户信息
									String clientType = paramForm.getClientType();
									UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(userId, T6110_F06.ZRR.name());
									// 登陆创建token
									String token = loginStateService.saveLoginToken(clientType,userId);

									if (StringUtils.isEmpty(token)) {
										response.setCodeMessage(
												ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getCode(),
												ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getMessage());
									} else {
										userAccountInfoVO.setToken(token);
										userInfoService.updateUserFirstLoginState(Integer.valueOf(userAccountInfoVO.getUserId()));

										String channelConfig = payConfig.TPPAYMENT_CHANNEL_CODE();
										if (StringUtils.isBlank(channelConfig)) {
											logger.info("[UserApiController.registerForPerson] : 请检查是否配置 paymentConfig.TPPAYMENT_CHANNEL_CODE");
										}
										//查询Baofoo绑卡
										boolean containBaofoo = baofooBindCardService.validateBind(Integer.valueOf(userAccountInfoVO.getUserId()));
										// 获取用户账户信息字段Map
										Map<String, Object> resultMap = userInfoService.getUserAccountInfoDataMap(userAccountInfoVO,channelConfig,containBaofoo,versionType);

										response.setData(resultMap);
									}
								} catch (Exception e) {
									// 注册成功，但redis写入token失败。
									response.setCodeMessage(
											ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getCode(),
											ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getMessage());
									logger.error("[UserApiController.register.redistoken]", e);
								}
							}
						} else {
							// 手机号码已注册
							response.setCodeMessage(Message.STATUS_1018, Message.get(Message.STATUS_1018));
						}
					} else {
						// 手机号码格式不正确
						response.setCodeMessage(Message.STATUS_1019, Message.get(Message.STATUS_1019));
					}
				} else {
					// 密码格式错误
					response.setCodeMessage(Message.STATUS_1016, Message.get(Message.STATUS_1016));
				}
			} else {
				response.setCodeMessage(status.getCode(),
						status.getMessage());
			}
        }
		return response;
	}

	/**
	 * 企业注册
	 */
	@RequestMapping(value = "registerForCompany", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
	HttpResponse registerForCompany(@ModelAttribute BaseRequestForm paramForm,
						  @RequestParam(required = false, value = "verifyCode") String verifyCode,
						  RegisterForm registerForm,
						  VersionTypeEnum versionType) {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate()
				|| StringUtils.isBlank(registerForm.getPhoneNum())
				|| StringUtils.isBlank(registerForm.getPassword())
				|| StringUtils.isBlank(verifyCode)) {
			logger.info("request param is empty");
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
		} else {
			try {
				_decryptAndSetRegisterParams(registerForm);
			} catch (BadPaddingException bpe) {
				logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
				response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
				return response;
			}
			// 如果注册渠道大于25个字符，则截取到第25个
			String channelCode = registerForm.getChannelCode();
			if(StringUtils.isNotBlank(channelCode) && channelCode.trim().length() > 25) {
				channelCode = channelCode.trim().substring(0, 25);
				registerForm.setChannelCode(channelCode);
			}

			try {
				if(
					(
						StringUtils.isNotBlank(registerForm.getUnifiedSocialCreditIdentifier())
						&&
						(
							StringUtils.isNotBlank(registerForm.getBusinessLicenseNumber())
							|| StringUtils.isNotBlank(registerForm.getTaxRegistrationId())
							|| StringUtils.isNotBlank(registerForm.getOrganizingInstitutionBarCode())
						)
					) ||
					(
						StringUtils.isBlank(registerForm.getUnifiedSocialCreditIdentifier())
						&&
						(
							StringUtils.isBlank(registerForm.getBusinessLicenseNumber())
							|| StringUtils.isBlank(registerForm.getTaxRegistrationId())
							|| StringUtils.isBlank(registerForm.getOrganizingInstitutionBarCode())
						)
					)
				){
					response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
					return response;
				}

				//检查三证或社会信用统一代码
				response = checkEnterpriseCertificate(registerForm);
				if(!ResponseCode.SUCCESS.getCode().equals(response.getCode())){
					return response;
				}
			} catch (Exception bpe) {
				logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
				response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
				return response;
			}

			String phoneNum = registerForm.getPhoneNum();
			String password = registerForm.getPassword();

			//校验验证码
			ResponseEnum status = this.userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.REGISTER_TYPE), verifyCode);
			if (status == null) {
				// 推荐人手机号
				String spreadPhoneNum = registerForm.getSpreadPhoneNum();
				if(StringUtils.isNotBlank(spreadPhoneNum)) {
					// 判断手机号与推荐人手机号是否一致
					if(phoneNum.equals(spreadPhoneNum)) {
						response.setCodeMessage(ResponseEnum.RESPONSE_SPREADPHONENUM_ERROR.getCode(), "手机号不能与推荐人手机号一致");
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
						paramMap.put("userType", T6110_F06.FZRR);

						int count = userInfoService.getUserCount(paramMap);

						UserInfo userInfo = null;
						boolean isException = false;
						// 手机号不存在
						if (count == 0) {
							// 企业注册
							try {
								userInfo = userInfoService.registerForCompany(paramForm, registerForm);
							} catch (Exception e) {
								response.setCodeMessage(
										ResponseEnum.RESPONSE_REGISTER_ERROR.getCode(),
										ResponseEnum.RESPONSE_REGISTER_ERROR.getMessage());
								logger.error("[UserApiController.register]" + e.getMessage(), e);
								isException = true;
							}
							if(!isException) {
								String userId = userInfo.getUserId();
								// 发放注册奖励
//								userInfoService.grantAwardRegister(phoneNum, userId);
								// 发放现金红包
//								grantRedPackets(phoneNum, userId);
								// 发放体验金
//								grantExperienceGold(phoneNum,userId);

								try {
									// 查询用户账户信息
									String clientType = paramForm.getClientType();
									UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(userId, T6110_F06.FZRR.name());
									// 登陆创建token
									String token = loginStateService.saveLoginToken(clientType,userId);

									if (StringUtils.isEmpty(token)) {
										response.setCodeMessage(
												ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getCode(),
												ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getMessage());
									} else {
										userAccountInfoVO.setToken(token);
										userInfoService.updateUserFirstLoginState(Integer.valueOf(userAccountInfoVO.getUserId()));

										String channelConfig = payConfig.TPPAYMENT_CHANNEL_CODE();
										if (StringUtils.isBlank(channelConfig)) {
											logger.info("[UserApiController.registerForCompany] : 请检查是否配置 paymentConfig.TPPAYMENT_CHANNEL_CODE");
										}
										//查询Baofoo绑卡
										boolean containBaofoo = baofooBindCardService.validateBind(Integer.valueOf(userAccountInfoVO.getUserId()));
										// 获取用户账户信息字段Map
										Map<String, Object> resultMap = userInfoService.getUserAccountInfoDataMap(userAccountInfoVO,channelConfig,containBaofoo,versionType);

										response.setData(resultMap);
									}
								} catch (Exception e) {
									// 注册成功，但redis写入token失败。
									response.setCodeMessage(
											ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getCode(),
											ResponseEnum.RESPONSE_REGSUCCESS_REDISERROR.getMessage());
									logger.error("[UserApiController.register.redistoken]", e);
								}
							}
						} else {
							// 手机号码已注册
							response.setCodeMessage(Message.STATUS_1018, Message.get(Message.STATUS_1018));
						}
					} else {
						// 手机号码格式不正确
						response.setCodeMessage(Message.STATUS_1019, Message.get(Message.STATUS_1019));
					}
				} else {
					// 密码格式错误
					response.setCodeMessage(Message.STATUS_1016, Message.get(Message.STATUS_1016));
				}
			} else {
				response.setCodeMessage(status.getCode(),
						status.getMessage());
			}
		}
		return response;
	}

	/**
	 * 检查企业证书是否重复
	 */
	@RequestMapping(value = "checkEnterpriseCertificate", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	HttpResponse checkEnterpriseCertificate(RegisterForm registerForm) {
		HttpResponse response = new HttpResponse();
		try {
			//检查三证或社会信用统一代码
			if(!userInfoService.checkEnterpriseCertificate(registerForm)){
				response.setCodeMessage(ResponseCode.COMPANY_CODE_EXISTS);
			}
		} catch (Exception bpe) {
			logger.error("[UserApiController.register]" + bpe.getMessage(), bpe);
			response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
		}
		return response;
	}

//	/**
//	 * @todo 发放体验金
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

//	private void grantRedPackets(String phoneNum, String userId) {
//		// 红包类型：注册现金红包
//		int redpacketTypeCach = InterfaceConst.REDPACKET_REGISTERCACH;
//		List<UserRedPacketInfo> userRedPacketInfos = null;
//		try {
//            // 根据红包类型查询现金红包并发放
//            userRedPacketInfos = redpacketService.getActivityRedBagByType(redpacketTypeCach);
//
//            if(userRedPacketInfos != null && userRedPacketInfos.size() > 0) {
//                redpacketService.grantRedPackets(userRedPacketInfos, userId, phoneNum, FeeCode.CACH_REDPACKET);
//            }
//        } catch (Exception e) {
//			String message = "[注册现金红包异常：]" + e.getMessage();
//            logger.error(message, e);
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
//        }
//	}

	/**
	 * 修改登陆密码
	 */
	@RequestMapping(value = "modifyPassword", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
    HttpResponse modifyPassword(@ModelAttribute BaseRequestFormExtend formExtend,
			@RequestParam(required = false, value = "oldPassword") String oldPassword,
			@RequestParam(required = false, value = "newPassword") String newPassword,
			ModifyPasswordForm modifyPasswordForm) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()
				|| StringUtils.isBlank(oldPassword)
				|| StringUtils.isBlank(newPassword)) {
			logger.info("request param is empty");
			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
		} else {
			response = userInfoService.modifyPassword(modifyPasswordForm);
		}

		return response;
	}

	/**
	 * 修改手机号
	 * @return
	 */
	@RequestMapping(value = "phone", headers = APIVersion.V_1_0_0)
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
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}

			if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(verifyCode)||StringUtils.isEmpty(phoneNum)){
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}

			String phone=AES.getInstace().decrypt(phoneNum);//解密后的手机号

			int count=Integer.parseInt(Sender.get("phone.varifycode.maxerror.count"));//最大验证错误次数
			int ecount=userInfoService.matchVerifyCodeErrorCount(1, phone);//当日该手机与验证码匹配错误次数
			if(ecount>=count){
				response.setCodeMessage(Message.STATUS_1005, Message.get(Message.STATUS_1005));
                return response;
			}

			//校验手机号是否已存在
			if(userInfoService.checkPhone(phone)){
				response.setCodeMessage(Message.STATUS_1006, Message.get(Message.STATUS_1006));
                return response;
			}

			//校验验证码
			ResponseEnum status = this.userInfoService.verifySmsCode(phone, String.valueOf(InterfaceConst.BIND_PHONE_TYPE), verifyCode);
			if (status == null) {
				//更新手机号
				userInfoService.updatePhoneSecurity(phone, userId, Status.TG.toString());
			} else {
				response.setCodeMessage(status.getCode(),
						status.getMessage());
			}
		}catch(Exception ex){
			response.setCodeMessage(Message.STATUS_12138, Message.get(Message.STATUS_12138));
			logger.error("[UserApiController.bindPhoneNum]"+ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 手机号解除绑定
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "unbindPhoneNum",method=RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse unbindPhoneNum(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value = "token") String token,
			@RequestParam(required = false, value = "userId") String userId){
		logger.info("request paramter[userId:{}]",new Object[]{userId});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				logger.info("request param is empty");
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}
			if(StringUtils.isEmpty(userId)){
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}


			userInfoService.updatePhoneSecurity("", userId, Status.BTG.toString());

		}catch(Exception ex){
			response.setCodeMessage(Message.STATUS_12138, Message.get(Message.STATUS_12138));
            logger.error("[UserApiController.unbindPhoneNum]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 绑定邮箱（发送验证邮件）
	 * @return
	 */
	@RequestMapping(value = "bindEmail",method=RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse bindEmail(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value = "token") String token,
			@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "email") String email){
		logger.info("request paramter[userId:{},email:{}]",new Object[]{userId,email});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				logger.info("request param is empty");
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}

			if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(email)){
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}

			int count=Integer.parseInt(Sender.get("phone.varifycode.maxerror.count"));//最大验证错误次数
			int ecount=userInfoService.matchVerifyCodeErrorCount(1, email);//当日该邮箱与验证码匹配错误次数
			if(ecount>=count){
				response.setCodeMessage(Message.STATUS_1005, Message.get(Message.STATUS_1005));
                return response;
			}

			if(userInfoService.checkEmail(email)){
				response.setCodeMessage(Message.STATUS_1007, Message.get(Message.STATUS_1007));
                return response;
			}

			//邮件发送
			this.userInfoService.insertSendEmail(userId, email, CommonTool.getDomain(request));

			String loginUrl=EmailSite.get(email.substring(email.lastIndexOf("@")+1));
			response.getData().put("url", loginUrl);
		}catch(Exception ex){
			response.setCodeMessage(Message.STATUS_12138, Message.get(Message.STATUS_12138));
			logger.error("[UserApiController.bindEmail]" + ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 绑定邮箱（验证邮件链接）
	 * @return
	 */
	@RequestMapping(value = "checkEmailAuth", headers = APIVersion.V_1_0_0)
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
	@RequestMapping(value = "unbindEmail",method=RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse unbindEmail(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value = "token") String token,
			@RequestParam(required = false, value = "userId") String userId){
		logger.info("request paramter[userId:{}]",new Object[]{userId});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				logger.info("request param is empty");
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}

			if(StringUtils.isEmpty(userId)){
				response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
                return response;
			}


			userInfoService.updateEmailSecurity("", userId, Status.BTG.toString());

		}catch(Exception ex){
			response.setCodeMessage(Message.STATUS_12138, Message.get(Message.STATUS_12138));
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
	@RequestMapping(value = "avatar",method=RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse avatar(HttpServletRequest request,@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value = "token") String token,
			@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "userPicUrl") String userPicUrl,
			@RequestParam(required = false, value = "file") MultipartFile file){
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
			}

			if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(type)){
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
			}

			String fileUrl=userInfoService.uploadUserPic(Integer.parseInt(userId), file, Integer.parseInt(type), userPicUrl);
			response.getData().put("userUrl", fileUrl);

		}catch(Exception ex){
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(),ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[UserApiController.userPicUpload]"+ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 获取手机验证码
	 * @return
	 */
	@RequestMapping(value = "getVerifyCode",method=RequestMethod.POST, headers = APIVersion.V_1_0_0)
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
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
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
					response.setCodeMessage(ResponseEnum.RESPONSE_PHONE_REGISTERED.getCode(),ResponseEnum.RESPONSE_PHONE_REGISTERED.getMessage());
					return response;
				}
			}
			if(codeType == InterfaceConst.RETRIEVE_PASSWORD_TYPE){
				if(!isExist){
					response.setCodeMessage(ResponseCode.USER_NOT_EXIST.getCode(),ResponseCode.USER_NOT_EXIST.getMessage());
					return response;
				}
			}

//			if(!paramForm.getClientType().equals(String.valueOf(Constant.CLIENTTYPE_WAP))&&!paramForm.getClientType().equals(String.valueOf(Constant.CLIENTTYPE_WEIXIN))){
//				userIp = CommonTool.getIpAddr(request);
//			}

			/**
			 * 手机号：半小时 3次       一天5次
			 *   IP：半小时5次        一天20次
			 */
			Date halfhour = DateUtil.minuteAdd(new Date(), -30);
			Date hour = DateUtil.minuteAdd(new Date(), -60);
			Date curDate = DateUtil.getDateFromDate(DateUtil.nowDate());
			int phoneSendCountByHour = this.userInfoService.getSendSmsCount(null, phone, halfhour, null);
			int ipSendCountByHalfour = this.userInfoService.getSendSmsCount(userIp, null, halfhour, null);
			int phoneSendCountByDay = this.userInfoService.getSendSmsCount(null, phone, null, curDate);
			int ipSendCountByDay = this.userInfoService.getSendSmsCount(userIp, null,null,curDate);

			if(phoneSendCountByDay >= Integer.parseInt(Sender.get("phone.send.day.maxcount"))||
				      ipSendCountByDay >= Integer.parseInt(Sender.get("ip.send.day.maxcount"))){
				response.setCodeMessage(ResponseEnum.RESPONSE_PHONE_CAPTCHA_MAXTIME.getCode(),ResponseEnum.RESPONSE_PHONE_CAPTCHA_MAXTIME.getMessage());
				return response;
			}

			if(phoneSendCountByHour >= Integer.parseInt(Sender.get("phone.send.hour.maxcount"))){
				response.setCodeMessage(ResponseEnum.RESPONSE_PHONE_HOUR_MAXTIME.getCode(),ResponseEnum.RESPONSE_PHONE_HOUR_MAXTIME.getMessage());
				return response;
			}

			if(ipSendCountByHalfour >= Integer.parseInt(Sender.get("ip.send.halfhour.maxcount"))){
				response.setCodeMessage(ResponseEnum.RESPONSE_PHONE_HALFOUR_MAXTIME.getCode(),ResponseEnum.RESPONSE_PHONE_HALFOUR_MAXTIME.getMessage());
				return response;
			}


			int count=userInfoService.getSendPhoneCount(phone, codeType);
			if(count>Integer.parseInt(Sender.get("user.send.maxcount"))){
				response.setCodeMessage(ResponseEnum.RESPONSE_PHONE_CAPTCHA_MAXTIME.getCode(),ResponseEnum.RESPONSE_PHONE_CAPTCHA_MAXTIME.getMessage());
				return response;
			}

			//所有验证码发送间隔时间60秒，否则容易造成短信通道被封
			SmsValidcode smsValidCode = userInfoService.getLastSmsCode(phone,String.valueOf(codeType));
			if(smsValidCode!= null){
				long curTime = System.currentTimeMillis();
				if(curTime < (smsValidCode.getOutTime().getTime()-(30*60*1000)+60000)){
					response.setCodeMessage(ResponseEnum.RESPONSE_PHONE_CAPTCHA_FREQUENTLY.getCode(),ResponseEnum.RESPONSE_PHONE_CAPTCHA_FREQUENTLY.getMessage());
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
//					response.setCodeMessage(ResponseEnum.RESPONSE_PHONE_CAPTCHA_MAXTIME.getCode(),ResponseEnum.RESPONSE_PHONE_CAPTCHA_MAXTIME.getMessage());
//                    return response;
//				}
//				userInfoService.sendVerifySms(phone, Integer.parseInt(type),userInfo.getUserId());
//			}else{
//				userInfoService.sendVerifySms(phone, Integer.parseInt(type),null);
//			}
		}catch(Exception ex){
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(),ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
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
    @RequestMapping(value = "login", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    public HttpResponse login(
            @ModelAttribute BaseRequestForm baseForm,
            @ModelAttribute LoginForm loginForm,
			VersionTypeEnum versionType) {
        HttpResponse response = new HttpResponse();
        if (!baseForm.validate()||versionType == null) {
            response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
        } else if(StringUtils.isBlank(loginForm.getUsername()) && StringUtils.isBlank(loginForm.getPhoneNum())){
            response.setCodeMessage(Message.STATUS_1013, "用户名和手机号不能同时为空");
        } else if(StringUtils.isNotBlank(loginForm.getUsername()) && StringUtils.isNotBlank(loginForm.getPhoneNum())) {
            response.setCodeMessage(Message.STATUS_213, "用户名和手机号不能同时存在");
        } else if (StringUtils.isEmpty(loginForm.getPassword())) {
            response.setCodeMessage(ResponseEnum.RESPONSE_WRONG_PASSWORD.getCode(), ResponseEnum.RESPONSE_WRONG_PASSWORD.getMessage());
        } else {
            try {
                // 解密和设置登陆参数
                decryptAndSetLoginParams(loginForm);
                // 获取用户账户信息
                UserAccountInfoVO userAccountInfoVO = userInfoService.
                        getUserAccountInfoByPhoneNumOrUsername(loginForm.getUsername(), loginForm.getPhoneNum(), loginForm.getUserType());
                if (userAccountInfoVO == null) {
                    response.setCodeMessage(Message.STATUS_1014, Message.get(Message.STATUS_1014));
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
								// 判断是否名创会员
								boolean flag = userOriginService.validUserOrigin(userAccountInfoVO.getUserId(), Config.get("mcMember.channelcode"));
								if (flag) {
									// 发放注册奖励
									userInfoService.grantAwardFirstLogin(loginForm.getPhoneNum(), userAccountInfoVO.getUserId());
								}
								userInfoService.updateUserFirstLoginState(Integer.valueOf(userAccountInfoVO.getUserId()));
							}

							String channelConfig = payConfig.TPPAYMENT_CHANNEL_CODE();
							if (StringUtils.isBlank(channelConfig)) {
								logger.info("[UserApiController.login] : 请检查是否配置 paymentConfig.TPPAYMENT_CHANNEL_CODE");
							}
							//查询Baofoo绑卡
							boolean containBaofoo = baofooBindCardService.validateBind(Integer.valueOf(userAccountInfoVO.getUserId()));
							// 获取用户账户信息字段Map
							Map<String, Object> data = userInfoService.getUserAccountInfoDataMap(userAccountInfoVO,channelConfig,containBaofoo,versionType);

							response.setData(data);
                        } else {
                            response.setCodeMessage(ResponseEnum.REDIS_INVALID_SESSION.getCode(), ResponseEnum.REDIS_INVALID_SESSION.getMessage());
                        }
                    } else {
                        response.setCodeMessage(Message.STATUS_1015, Message.get(Message.STATUS_1015));
                    }
                }
            } catch (Exception e) {
                logger.error("[UserApiController.login]", e);
                response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
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
	 * 修改用户基础信息   2016-5-11 junda.feng
	 * @param paramForm
	 * @param nickname 昵称
	 * @param schoole 毕业学校
	 * @param companyIndustry 公司行业
	 * @param companySize 公司规模
	 * @param position 职位
	 * @param income 月收入
	 * @return
	 */
	@RequestMapping(value = "info/modify", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
	public HttpResponse modify(@ModelAttribute BaseRequestFormExtend  paramForm, String nickname
			, String schoole, String companyIndustry, String companySize, String position, String income) {
//		logger.debug(paramForm + "nickname=" + nickname + ",schoole=" + schoole + ",companyIndustry=" + companyIndustry
//				 + ",companySize=" + companySize + ",position=" + position + ",income=" + income);
//		try {
//			if(StringUtils.isNoneBlank(nickname))
//				nickname=new String(nickname.getBytes("iso-8859-1"),"utf-8");
//			if(StringUtils.isNoneBlank(schoole))
//				schoole=new String(schoole.getBytes("iso-8859-1"),"utf-8");
//			if(StringUtils.isNoneBlank(companyIndustry))
//				companyIndustry=new String(companyIndustry.getBytes("iso-8859-1"),"utf-8");
//			if(StringUtils.isNoneBlank(companySize))
//				companySize=new String(companySize.getBytes("iso-8859-1"),"utf-8");
//			if(StringUtils.isNoneBlank(position))
//				position=new String(position.getBytes("iso-8859-1"),"utf-8");
//			if(StringUtils.isNoneBlank(income))
//				income=new String(income.getBytes("iso-8859-1"),"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		HttpResponse respone = new HttpResponse();
		if (!paramForm.validate() ) {//|| StringUtils.isBlank(nickname)
			respone.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
            return respone;
		}
		if (StringUtils.isNotEmpty(nickname) && nickname.length() > 30) {
			respone.setCodeMessage(ResponseEnum.NAME_LENGTH_TOO_LONG.getCode(),	ResponseEnum.NAME_LENGTH_TOO_LONG.getMessage());
            return respone;
		}
		if (StringUtils.isNotEmpty(schoole) && schoole.length() > 100) {
			respone.setCodeMessage(ResponseEnum.SCHOOLE_LENGTH_TOO_LONG.getCode(), ResponseEnum.SCHOOLE_LENGTH_TOO_LONG.getMessage());
            return respone;
		}
		if (StringUtils.isNotEmpty(position) && position.length() > 100) {
			respone.setCodeMessage(ResponseEnum.POSITION_LENGTH_TOO_LONG.getCode(), ResponseEnum.POSITION_LENGTH_TOO_LONG.getMessage());
            return respone;
		}
		if (StringUtils.isNotEmpty(companyIndustry) && companyIndustry.length() > 30) {
			respone.setCodeMessage(ResponseEnum.TEXT_LENGTH_TOO_LONG.getCode(), ResponseEnum.TEXT_LENGTH_TOO_LONG.getMessage());
            return respone;
		}
		if (StringUtils.isNotEmpty(companySize) && companySize.length() > 30) {
			respone.setCodeMessage(ResponseEnum.TEXT_LENGTH_TOO_LONG.getCode(),	ResponseEnum.TEXT_LENGTH_TOO_LONG.getMessage());
            return respone;
		}
		if (StringUtils.isNotEmpty(income) && income.length() > 30) {
			respone.setCodeMessage(ResponseEnum.TEXT_LENGTH_TOO_LONG.getCode(), ResponseEnum.TEXT_LENGTH_TOO_LONG.getMessage());
            return respone;
		}
//		return respone;
		return userInfoService.modifyInfoById(paramForm.getUserId(), nickname, schoole, companyIndustry, companySize, position, income);
	}

	/**
	 * 修改昵称接口
	 * @param paramForm
	 * @param nickname
	 * @return
	 */
	@RequestMapping(value = "modifyNickname", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
	public HttpResponse modifyNickName(@ModelAttribute BaseRequestFormExtend  paramForm,
			@RequestParam(required = false, value="nickname") String nickname) {
		logger.debug(paramForm + "nickname=" + nickname);
		HttpResponse respone = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isBlank(nickname)) {
			respone.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
            return respone;
		}
		if (nickname.length() > 30) {
			respone.setCodeMessage(ResponseEnum.TEXT_LENGTH_TOO_LONG.getCode(),
					ResponseEnum.TEXT_LENGTH_TOO_LONG.getMessage());
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
	@RequestMapping(value = "verify", method = RequestMethod.PUT, headers = APIVersion.V_1_0_0)
	public HttpResponse verify(@ModelAttribute BaseRequestFormExtend  paramForm,
			@RequestParam(required = false, value="password") String password) {
		if (!paramForm.validate()) {
			HttpResponse respone = new HttpResponse();
			respone.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
            return respone;
		}

		HttpResponse response = new HttpResponse();;
		try {
			boolean result = userInfoService.verify(paramForm.getUserId(), password);
			if (!result) {
				response.setCodeMessage(Message.STATUS_1015, Message.get(Message.STATUS_1015));
			}
		} catch (Exception e) {
			response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), "密码解密失败");
		}

		return response;
	}

	/**
	 * 获取平台信息(产品信息、总投资、总收益)
	 * @param paramForm
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "platform/information", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse getPlatformInfo(@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value="token") String token,
			@RequestParam(required = false, value="userId") String userId){
		HttpResponse response = new HttpResponse();
		try{
			if (!paramForm.validate()) {
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
				return response;
			}
			boolean userInvalid = this.userTokenService.isInvalidToken(token==null?"":token, userId==null?"":userId, paramForm.getClientType());
			PlatformInfoVo vo=this.userInfoService.getPlatformInfo(userId,userInvalid);
			response.setData(CommonTool.toMap(vo));
		}catch(Exception ex){
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(),ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
			logger.error("[UserApiController.getPlatformInfo]"+ex.getMessage(), ex);
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
	@RequestMapping(value = "kdb/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse getBid(@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value="token") String token,
			@RequestParam(required = false, value="userId") String userId,
			@RequestParam(required = false, value="timestamp") String timestamp,
			@RequestParam(required = false, value="isUp") String isUp){
		HttpResponse response = new HttpResponse();
		try {
			if (!paramForm.validate()) {
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
				return response;
			}

			List<ShopTreasureVo> voList=this.userInfoService.getShopTreasureList(timestamp, userId, isUp);
			response.getData().put("kdbPlantList", voList);
		} catch (Exception ex) {
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(),ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
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
	@RequestMapping(value = "kdb/detail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse getBidDetail(@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value="token") String token,
			@RequestParam(required = false, value="userId") String userId,
			@RequestParam(required = false, value="kdbPlantId") String kdbPlantId){
		HttpResponse response = new HttpResponse();
		try {
			if (!paramForm.validate()||StringUtils.isEmpty(kdbPlantId)) {
				response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
				return response;
			}

			ShopTreasureInfoVo vo=this.userInfoService.getShopTreasureInfo(kdbPlantId, userId);
			if(null==vo){
				response.setCodeMessage(ResponseEnum.RESPONSE_BID_EMPTY.getCode(), ResponseEnum.RESPONSE_BID_EMPTY.getMessage());
				return response;
			}

			response.setData(CommonTool.toMap(vo));
		} catch (Exception ex) {
			response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(),ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
			logger.error("[UserApiController.getBidDetail]"+ex.getMessage(), ex);
		}
		return response;
	}

	/**
	 * 判断当前用户是否新用户
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "isNewUser", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse getBidDetail(@ModelAttribute BaseRequestForm  paramForm,
									 @RequestParam(required = false, value="token") String token,
									 @RequestParam(required = false, value="userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
			return response;
		}
		int newUserFlag = userInfoService.isBidNewUser(Integer.valueOf(userId));
		response.getData().put("isNewUser",newUserFlag>0?0:1);
		return response;
	}


	/**
	 * 是否是存管账户
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "isDepository", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
	public HttpResponse isDepository(@ModelAttribute BaseRequestForm  paramForm,
									 @RequestParam(required = false, value="token") String token,
									 @RequestParam(required = false, value="userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate()) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
			return response;
		}
		int isDepository = userInfoService.isDepository(Integer.valueOf(userId));
		response.getData().put("isDepository",isDepository);
		return response;
	}

//	/**
//	 * 用户资产区分存管账户与普通账户
//	 * @param formExtend
//	 * @param depository
//     * @return
//     */
//	@RequestMapping(value = "userAssetByDepository", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
//	HttpResponse userAssetByDepository(@ModelAttribute BaseRequestFormExtend formExtend,
//									   @RequestParam(required = false, value="depository") String depository) {
//		HttpResponse response = new HttpResponse();
//		HXBalanceVO balanceVO = null;
//		Map<String, Object> data = null;
//        double balance = 0.0;
//		if (!formExtend.validate()) {
//			logger.debug("request param is empty");
//			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
//		} else {
//			try {
//				if(!StringUtils.isBlank(depository)&&depository.equals("2")){
//					try {
//						balanceVO = hxCommonService.getBalance(formExtend.getUserId());
//					}catch (Exception e){
//						logger.error("[UserApiController.userAssetByDepository]"+e.getMessage(), e);
//					}
//					if(balanceVO!=null) {
//						data = userInfoService.getUserAssetsByDepository(formExtend.getUserId(), depository, balanceVO.getAvailable());
//					}else{
//						data = userInfoService.getUserAssetsByDepository(formExtend.getUserId(),depository, BigDecimal.ZERO);
//					}
//				}
//				else{
//					data = userInfoService.getUserAssetsByDepository(formExtend.getUserId(),depository, BigDecimal.ZERO);
//				}
//
//
//				response.setData(data);
//			} catch (Exception e) {
//				response.setCodeMessage(
//						ResponseEnum.RESPONSE_USERASSET_FAILED.getCode(),
//						ResponseEnum.RESPONSE_USERASSET_FAILED.getMessage());
//				logger.error(e.getMessage());
//			}
//
//		}
//		return response;
//	}
//
///	@RequestMapping(value = "accountNoByE", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
//	HttpResponse accountNoByE(@ModelAttribute BaseRequestFormExtend formExtend) {
//		HttpResponse response = new HttpResponse();
//		HXBalanceVO balanceVO = null;
//		AccountNoVO accountNoVO = null;
//		double balance = 0.0;
//		if (!formExtend.validate()) {
//			logger.debug("request param is empty");
//			response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
//		} else {
//			try {
//				accountNoVO = userInfoService.getAccountNo(formExtend.getUserId());
//				balanceVO = hxCommonService.getBalance(formExtend.getUserId());
//				if (balanceVO != null) {
//					accountNoVO.setAvailable(balanceVO.getAvailable());
//					accountNoVO.setFrozen(balanceVO.getFrozen());
//				}else{
//					accountNoVO.setFrozen(BigDecimal.ZERO);
//					accountNoVO.setAvailable(BigDecimal.ZERO);
//				}
//			} catch (Exception e) {
//				logger.error("[UserApiController.accountNoByE]" + e.getMessage(), e);
//			}
//		}
//		response.getData().put("accountNo", accountNoVO.getAccountNo());
//		response.getData().put("available", accountNoVO.getAvailable());
//		response.getData().put("frozen", accountNoVO.getFrozen());
//		return response;
//	}

	/**
	 * 参数INVESTOR ，BORROWERS
	 * 获取存管投资用户银行卡信息
	 * @param params
	 * @return
     */
	@RequestMapping(value = "cgBankCard", method = RequestMethod.GET)
	public HttpResponse getCgBankCard(BaseRequestFormExtend params,String role) {
		HttpResponse response = new HttpResponse();
		try {
			if (!params.validate()||StringUtils.isEmpty(role)) {
				throw new BusinessException(ResponseCode.EMPTY_PARAM);
			}
			Map<String, Object> xwcgInfo = userInfoService.getXWCardInfo(params.getUserId(),role);
			response.getData().put("xwcgInfo", xwcgInfo);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("获取用户存管银行卡信息失败,userId=[{}]", params.getUserId());
			logger.error("获取用户存管银行卡信息失败", e);
		}
		return response;
	}



	/**
	 * 参数INVESTOR ，BORROWERS
	 * 用户授权状态列表
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getAuthStatus", method = RequestMethod.GET)
	public HttpResponse getAuthStatus(BaseRequestFormExtend params,@ModelAttribute BaseRequestFormExtend formExtend,String role) {
		HttpResponse response = new HttpResponse();
		try {
			if (!params.validate()||!formExtend.validate()||StringUtils.isEmpty(role)) {
				throw new BusinessException(ResponseCode.EMPTY_PARAM);
			}
			String authStatus = userInfoService.getAuthStatusList(params.getUserId(),role);
			if(StringUtils.isEmpty(authStatus)){
				authStatus="0";
			}
			response.getData().put("authStatus", authStatus);
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("获取用户授权状态,userId=[{}]", params.getUserId());
		}
		response.setCodeMessage(ResponseCode.SUCCESS);
		return response;
	}


	@RequestMapping(value = "updateAuthStatus", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
	public HttpResponse updateAuthStatus(
			@ModelAttribute BaseRequestForm baseForm,
			@ModelAttribute BaseRequestFormExtend formExtend,String role,
			String status, String uri) {
		HttpResponse response = new HttpResponse();
		if (!baseForm.validate()||!formExtend.validate()||StringUtils.isEmpty(role)||StringUtils.isEmpty(status)) {
			throw new BusinessException(ResponseCode.EMPTY_PARAM);
		}
		try{
			Map<String, Object> params = userInfoService.updateAuthStatus(formExtend.getUserId(), role, status, uri);
			response.setData(params);
		}catch(Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("更新用户授权状态失败");
			return response;
		}
		response.setCodeMessage(ResponseCode.SUCCESS);
		return response;
	}

}

