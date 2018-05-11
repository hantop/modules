package com.fenlibao.p2p.service.pay.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.fenlibao.lianpay.v_1_0.enums.SignTypeEnum;
import com.fenlibao.lianpay.v_1_0.utils.YinTongUtil;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.bank.BankCardDmService;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.pay.ILianLianPayService;
import com.fenlibao.p2p.service.pay.LlpAuthPayService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.pay.OrderUtil;

@Service
public class LlpAuthPayServiceImpl implements LlpAuthPayService {

	private static final Logger logger = LogManager.getLogger(LlpAuthPayServiceImpl.class);
	
	@Resource
	protected BankService bankService;
	@Resource
	private BankCardDmService bankCardDmService;
	@Resource
	protected ITradeService tradeService;
	@Resource
	protected ILianLianPayService llpPayService;
	
	private static final String BANK_CARD_NO_REG = "^[0-9]{16,19}$";
	
	@Override
	public String getSignKey(String sign_type, boolean isAdd) {
		String isTest = Payment.get(Payment.IS_PAY_TEST);// 是否为测试
		String key = "";
		if (SignTypeEnum.MD5.getCode().equals(sign_type)) {
			key = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY));
		} else if (SignTypeEnum.RSA.getCode().equals(sign_type)) {
			if (isAdd) {
				key = AES.getInstace().decrypt(Payment.get(Payment.RSA_P_KEY));
			} else {
				key = AES.getInstace().decrypt(Payment.get(Payment.RSA_PB_KEY));
			}
		}
		if ("true".equals(isTest)) {
			key = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY_TEST));
		}
		return key;
	}

	@Override
	public boolean putBankCardNo(Map<String, String> params, Integer userId, String bankCardNo) throws Throwable {
		String bindStatus = bankService.getBankCardBindStatus(userId);
		if (InterfaceConst.BANK_CARD_STATUS_WRZ.equals(bindStatus)) { //是否绑定银行卡
			if (StringUtils.isBlank(bankCardNo) || !bankCardNo.matches(BANK_CARD_NO_REG)) {
				throw new BusinessException(ResponseCode.BANK_CARD_NO_FORMAT_ERROR);
			}
			if (!bankService.isSupportBank(bankCardNo)) {
				throw new BusinessException(ResponseCode.TRADE_NOT_SUPPORT_BANK);
			}
			int isSuccess = bankCardDmService.bindBankcard(userId, bankCardNo); //首次充值进行绑定银行卡
			if (isSuccess < 1) {
				logger.warn("用户[{}]绑定银行卡[{}]失败", userId, bankCardNo);
				throw new BusinessException(ResponseCode.BIND_BANK_CARD_FAILURE);
			}
			params.put("card_no", bankCardNo);
			return true;
		}
		setPayFormPropertiesByUserId(params, userId);
		return false;
	}
	
	private void setPayFormPropertiesByUserId(Map<String, String> params, int userId) throws Throwable {
		String cardNo = bankService.getCardNo(userId);
		if (StringUtils.isNotBlank(cardNo)) {
			params.put("card_no", cardNo); //银行卡号
		} else {
			throw new BusinessException(ResponseCode.BANK_CARD_NO_EMPTY);
		}
		String noAgree = tradeService.getNoAgree(userId);
		if (StringUtils.isNotBlank(noAgree)) {
			params.put("no_agree", noAgree); //协议号
		}
	}

	@Override
	public void setRequestData(Map<String, String> params, String returnUrl, RechargeOrder order) throws Throwable {
		int userId = order.getUserId();
		Date createTime = order.getCreateTime();
		params.put("user_id", OrderUtil.genLlpUserId(userId));
		params.put("no_order", OrderUtil.genLlpRechargeOrderId(order.getId(), createTime));
		params.put("dt_order", DateParser.format(createTime, "yyyyMMddHHmmss"));
		params.put("money_order", order.getAmount().toString());
		UserInfo userInfo = llpPayService.setUserInfo(params, userId);
		setRiskItem(params, userInfo);
		setPaymentConfig(params, returnUrl);
		llpPayService.setParamsForTest(params);
		YinTongUtil.putSign(params, getSignKey(Payment.get(Payment.SIGN_TYPE), true));
	}

	/**
	 * 设置支付信息
	 * @param params
	 * @throws Exception
	 */
	private void setPaymentConfig(Map<String, String> params, String returnUrl) throws Exception {
		params.put("version", "1.0");
		params.put("name_goods", "充值");
		params.put("oid_partner", Payment.get(Payment.OID_PARTNER));
		params.put("sign_type", Payment.get(Payment.SIGN_TYPE));
		params.put("busi_partner", Payment.get(Payment.BUSI_PARTNER));
		params.put("notify_url", Config.get(Payment.NOTIFY_URL_RECHARGE));
		params.put("url_return", Config.get(Payment.URL_RETURN) + returnUrl);
		params.put("timestamp", DateParser.format(new Date(), "yyyyMMddHHmmss"));
//		params.put("valid_order", ""); //订单有效时间。分钟为单位，默认为 10080 分钟（7 天），
	}
	
	/**
	 * 设置风控参数
	 * @param params
	 * @param userInfo
	 * @throws Throwable 
	 */
	private void setRiskItem(Map<String, String> params, UserInfo userInfo) throws Throwable {
		Map<String,String> riskItem = new HashMap<String, String>(10);
		riskItem.put("frms_ware_category","2009");
		riskItem.put("user_info_mercht_userno", userInfo.getUserId());//用户在商户系统中的标识
		riskItem.put("user_info_mercht_userlogin", userInfo.getUsername());//用户在商户系统中的登陆名（手机号、邮箱等标识）
		riskItem.put("user_info_bind_phone", userInfo.getPhone());//绑定手机号
		riskItem.put("user_info_dt_register", DateParser.format(userInfo.getRegisterTime(),"yyyyMMddHHmmss"));//注册时间
		riskItem.put("user_info_id_type", "0");
		//用户的身份证信息是否是自然人已经在获取userInfo的时候判断，如果是非自然人已经从t6161获取法人信息
		riskItem.put("user_info_full_name", userInfo.getFullName());//用户注册姓名
		riskItem.put("user_info_id_no", StringHelper.decode(userInfo.getIdCardEncrypt()));//用户注册证件号码
		
		riskItem.put("user_info_identify_state", "1");//是否实名认证
		riskItem.put("user_info_identify_type", "4");//实名认证方式
		params.put("risk_item", JSON.toJSONString(riskItem));
	}
}
