package com.fenlibao.p2p.service.pay;

import java.util.Map;

import com.fenlibao.p2p.model.entity.pay.RechargeOrder;

public interface LlpAuthPayService {

	/**
	 * 设置银行卡信息
	 * @param userId
	 * @param bankCardNo
	 * @throws Exception
	 * @return 是否是首次绑定
	 */
	boolean putBankCardNo(Map<String, String> params, Integer userId, String bankCardNo) throws Throwable;
	
	/**
	 * 设置请求数据
	 * @param params
	 * @param order
	 * @throws Exception
	 */
	void setRequestData(Map<String, String> params, String returnUrl, RechargeOrder order) throws Throwable;
	
	/**
	 * 获取秘钥(快捷)
	 * （RSA私钥签名，公钥验签）
	 * @param sign_type 签名类型
	 * @param isAdd 添加签名  ? private : public
	 * @return
	 */
	String getSignKey(String sign_type, boolean isAdd);
	
}
