package com.fenlibao.p2p.controller.v_4.v_4_2_0.trade;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.form.trade.TradeRecordForm;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易控制器
 * @author yangzengcai
 * @date 2015年8月15日
 */
@RestController("v_4_2_0/TradeController")
@RequestMapping(value = "trade", headers = APIVersion.v_4_2_0)
public class TradeController {

	private static final Logger logger = LogManager.getLogger(TradeController.class);
	
	@Resource
	private ITradeService tradeService;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private XWRechargeService xwRechargeService;
	@Resource
	private XWUserInfoService xwUserInfoService;
	@Resource
	private RedisService redisService;
	
	private final String TRADE_PASSWORD_REG = "^\\d{6}$"; //交易密码正则

	/**
	 * 交易记录
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "record/list", method = RequestMethod.GET)
	public HttpResponse recordList(@ModelAttribute BaseRequestFormExtend params, Integer pageNo, Integer pagesize, String versionType) {
		HttpResponse response = new HttpResponse();
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		VersionTypeEnum vte = VersionTypeEnum.CG.getCode().equals(versionType) ? VersionTypeEnum.CG : VersionTypeEnum.PT;
		List<TradeRecordForm> list = tradeService.getRecordList(params.getUserId(), pageNo, pagesize, vte);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", list);
		response.setData(data);
		return response;
	}
	
	/**
	 * 收益记录
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "earnings/list", method = RequestMethod.GET)
	public HttpResponse earningsList(@ModelAttribute BaseRequestFormExtend params) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		//List<EarningsRecordForm> list = tradeService.getEarningsList(params.getUserId());
		Map<String, Object> list = tradeService.getEarningsRecordList(params.getUserId());
		data.put("list", list);
		response.setData(data);
		return response; 
	}
	
	/**
	 * 重置交易密码
	 * @param params
	 * @param phoneNum
	 * @param verifyCode
	 * @param newPassword
	 * @param identityNo
	 * @param
	 * @return
	 */
	@RequestMapping(value = "password", method = RequestMethod.PUT)
	public HttpResponse resetPassword(@ModelAttribute BaseRequestFormExtend params,
                                      String phoneNum, String verifyCode, String newPassword,
                                      String identityNo, String identityName) {
		HttpResponse response = new HttpResponse();

		if(identityName == null || "".equals(identityName)){
			UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(params.getUserId()));
			if (DateUtil.StrToDate("2017-12-19", DateUtil.yyyy_MM_dd).getTime() <= userAccountInfoVO.getRegisterTime().getTime()){
				logger.info("goto_CG");
				response.setCodeMessage(ResponseCode.COMMON_GOTO_CG);
				return response;
			}
		}

		if (!params.validate() || !StringUtils.isNoneBlank(phoneNum, verifyCode, newPassword)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		int userId = params.getUserId();
		try { 
			ResponseCode responseCode = this.verifyIdentity(identityNo, identityName, userId);
			if (responseCode != null) {
				response.setCodeMessage(responseCode.getCode(), responseCode.getMessage());
				return response;
			}
			phoneNum = AES.getInstace().decrypt(phoneNum);
			newPassword = AES.getInstace().decrypt(newPassword);
			if (!newPassword.matches(TRADE_PASSWORD_REG)) { //校验交易密码类型
				response.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);
			} else {
				newPassword = PasswordCryptUtil.crypt(newPassword);
				//校验验证码
				ResponseCode status = userInfoService.verifySmsCode(phoneNum, String.valueOf(InterfaceConst.RESET_TRADE_PASSWORD), verifyCode);
				if (status == null) {
					int result = tradeService.resetPassword(userId, newPassword); //重置交易密码
					if (result < 1) {
						response.setCodeMessage(ResponseCode.FAILURE);
					}
				} else {
					response.setCodeMessage(status.getCode(),
							status.getMessage());
				}
			}
		} catch (Throwable e) {
			logger.error(e.toString(), e);
			response.setCodeMessage(ResponseCode.FAILURE
					);
		}
		return response;
	}
	
	private ResponseCode verifyIdentity(String identityNo, String identityName, int userId) throws Throwable {
		boolean hasPwd = this.tradeService.hasSetPwd(userId);
		if (hasPwd) {
			if (!StringUtils.isNoneBlank(identityNo, identityName)) {
				return ResponseCode.EMPTY_PARAM;
			}
			identityNo = AES.getInstace().decrypt(identityNo);
			identityName = AES.getInstace().decrypt(identityName);
			//校验身份
			boolean isTrue = this.userInfoService.verifyIdentity(identityNo, identityName, userId);
			if (!isTrue) {
				return ResponseCode.USER_IDENTITY_VERIFY_FAIL;
			}
		}
		return null;	
	}
	
	/**
	 * 开通/关闭免交易密码接口
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "password/status", method = RequestMethod.PUT)
	public HttpResponse switchNoPassword(@ModelAttribute BaseRequestFormExtend params,
                                         Integer isOpen, String tradePassword) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		if (!params.validate() || isOpen == null || StringUtils.isBlank(tradePassword)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		try {
			tradePassword = PasswordCryptUtil.cryptAESPassword(tradePassword);
			if (!tradeService.getTradePassword(params.getUserId()).equals(tradePassword)) {
				response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
				return response;
			}
			int result = tradeService.switchNoPassword(params.getUserId(), isOpen);
			if (result < 1) {
				response.setCodeMessage(ResponseCode.FAILURE
						);
				return response;
			}
			data.put("isOpen", isOpen); // app说要
			response.setData(data);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			response.setCodeMessage(ResponseCode.FAILURE
					);
		}
		return response;
	}
	
	/**
	 * 待收本息
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "duein/amount", method = RequestMethod.GET)
	public HttpResponse dueInPrincipalAndInterest(@ModelAttribute BaseRequestFormExtend params) {
		HttpResponse response = new HttpResponse();
		if (!params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
					);
			return response;
		}
		DueInAmount dueInAmount = tradeService.getDueInAmount(params.getUserId());
		BigDecimal principal = dueInAmount.getPrincipal();
		BigDecimal gains = dueInAmount.getGains();
		Map<String, Object> data = new HashMap<String, Object>();
		BigDecimal totalDueInAmount = principal.add(gains);
		data.put("totalDueInAmount", totalDueInAmount.toString());
		data.put("principal", principal.toString());
		data.put("interest", gains.toString());
		response.setData(data);
		return response;
	}


	@RequestMapping(value = "amount/move", method = RequestMethod.POST)
	public HttpResponse tradeMove(@ModelAttribute BaseRequestFormExtend params, String  amount, String dealPassword) throws Throwable{
		HttpResponse response = new HttpResponse();
		/**
		 * 资金迁移功能暂停使用
		 */
		/*if(true){
			response.setCodeMessage(ResponseCode.MOVE_AMOUNT_UNABLE);
			return response;
		}*/
		if (!params.validate()||StringUtils.isEmpty(amount)||StringUtils.isEmpty(dealPassword)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM
			);
			return response;
		}
        if(Double.valueOf(amount)<0){//迁移资金必须大于零
			response.setCodeMessage(ResponseCode.MOVE_AMOUNT_FIND);
			return response;
		}

		if (!tradeService.isValidUserPwd(params.getUserId(), dealPassword)) {//密码校验
			//apiResponse.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
			response.setCodeMessage(ResponseCode.MOVE_AMOUNT_FIND);
			return response;
		}
		if (StringUtils.isEmpty(userInfoService.isXWaccount(params.getUserId()))) {//判断是否开通新网投资用户
			response.setCodeMessage(ResponseCode.USER_NOT_XW_ACCOUNT);
			return response;
		}

		String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(params.getUserId().toString());
		if (redisService.existsKey(requestCacheKey)) {
			response.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
			return response;
		}
		//更新账户往来资金，新增资金流水
        try {
			tradeService.updateUserAccountInfo(params.getUserId(),new BigDecimal(amount),1);
		}catch (BusinessException b){
			response.setCode(b.getCode());
			response.setMessage(b.getMessage());
			return response;
		}
       //新网代客充值
		BusinessType businessType = new BusinessType();
		businessType.setCode(SysTradeFeeCode.HBJE);// 原线下充值交易类型
		businessType.setName("资金迁移");
		businessType.setStatus("QY");
		String platformNo= null;
		try {
			platformNo = tradeService.getPlatformNo(params.getUserId(),"INVESTOR");
		}catch (Exception e){
			response.setCodeMessage(ResponseCode.USER_NOT_XW_ACCOUNT);
			return response;
		}

		//新网平台代充值往来账户
		XWFundAccount ptFundAccount = xwUserInfoService.getFundAccount(1, SysFundAccountType.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH);
		// 新网平台代充值往来账户余额不足
		if(ptFundAccount != null && ptFundAccount.getAmount().compareTo(new BigDecimal(amount)) < 0){
			// 插入一条处理中的记录,实际上还没有发起新网代充值请求
			tradeService.addTransferApplication(params.getUserId(), platformNo, new BigDecimal(amount), "2");
			tradeService.sendMessage();
			response.setCodeMessage(ResponseCode.XW_RECHARGEACCOUNT_BALANCE_FIND);
			return response;
		}

		int transferApplicationId = 0;
		int xwRequestId;
		//发起代客充值
		try {
			xwRequestId = xwRechargeService.doAlternativeRecharge(params.getUserId(), platformNo, new BigDecimal(amount),businessType);
		}catch (Exception e){
			//失败订单记录
			transferApplicationId = tradeService.addTransferApplication(params.getUserId(), platformNo, new BigDecimal(amount), "0");
			//代客充值失败，将锁定账户资金回滚到往来账户资金
			try {
				tradeService.updateUserAccountInfo(params.getUserId(),new BigDecimal(amount),2);
			}catch (BusinessException ex){
				response.setCode(ex.getCode());
				response.setMessage(ex.getMessage());
				return response;
			}
			logger.error("资金迁移代充值异常" + e);
			response.setCodeMessage(ResponseCode.MOVE_RECHARGE_FIND);
			return response;
		}finally {
			redisService.removeKey(requestCacheKey);
		}
		// 查询是否代充值成功,如果成功,添加一条成功的记录,并将用户平台锁定账户里面的钱扣掉,生成一条由普通锁定账号到存管往来账号的流水;

		if(xwRequestId > 0){
			Boolean flag = tradeService.getResultOfXWRequest(xwRequestId);
			try{
				if(flag){
					transferApplicationId = tradeService.addTransferApplication(params.getUserId(), platformNo, new BigDecimal(amount), "1");
					tradeService.updateUserAccountInfo(params.getUserId(), new BigDecimal(amount), 3);
				}else {
					transferApplicationId = tradeService.addTransferApplication(params.getUserId(), platformNo, new BigDecimal(amount), "0");
					// 失败,将锁定账号资金回滚到往来账户资金
					try {
						tradeService.updateUserAccountInfo(params.getUserId(),new BigDecimal(amount),2);
					}catch (BusinessException ex){
						response.setCode(ex.getCode());
						response.setMessage(ex.getMessage());
						return response;
					}

					response.setCodeMessage(ResponseCode.MOVE_RECHARGE_FIND);
					return response;
				}
			}catch (Exception e){
				if(transferApplicationId > 0){
					tradeService.updateTransferApplication(transferApplicationId, "0");
				}else{
					transferApplicationId = tradeService.addTransferApplication(params.getUserId(), platformNo, new BigDecimal(amount), "0");
				}
				// 失败,将锁定账号资金回滚到往来账户资金
				try {
					tradeService.updateUserAccountInfo(params.getUserId(),new BigDecimal(amount),2);
				}catch (BusinessException ex){
					response.setCode(ex.getCode());
					response.setMessage(ex.getMessage());
					return response;
				}

				response.setCodeMessage(ResponseCode.MOVE_RECHARGE_FIND);
				return response;
			}

		}
		response.setCodeMessage(ResponseCode.SUCCESS);
		return response;
	}


}
