package com.fenlibao.p2p.service.pay;

import java.util.List;
import java.util.Map;

import com.fenlibao.lianpay.v_1_0.vo.RefundCallBackVO;
import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;
import com.fenlibao.p2p.model.vo.pay.RefundVO;

/**
 * 连连快捷支付
 * @author zcai
 * @date 2016年4月25日
 */
public interface ILlpQuickPayService {

	/**
	 * 获取秘钥(快捷)
	 * （RSA私钥签名，公钥验签）
	 * @param sign_type 签名类型
	 * @param isAdd 添加签名  ? private : public
	 * @return
	 */
	String getSignKey(String sign_type, boolean isAdd);
	
	/**
	 * 只获取提交给连连的参数
	 * @param amount
	 * @param userId
	 * @return
	 */
	String getRequestData(PaymentOrderEntity order, String clientType) throws Throwable;
	
	/**
	 * 产生支付订单并返回连连支付参数和支付订单ID
	 */
	Map<String, String> getRequestData(String amount, Integer userId, String clientType) throws Throwable;
	
	/**
	 * 获取待退款的订单
	 * @return
	 */
	List<RefundVO> getWaitRefundOrder();
	
	/**
	 * 退款
	 * @param params 请求的报文
	 * @throws Exception
	 */
	void submitRefund(RefundVO vo, Map<String, String> params, String key, boolean isTest) throws Exception;
	
	/**
	 * 快捷支付退款回调确认
	 * @param vo
	 * @throws Exception
	 */
	void confirmRefund(RefundCallBackVO vo) throws Exception;
}
