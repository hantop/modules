package com.fenlibao.p2p.service.pay;

import java.util.Map;

import com.fenlibao.lianpay.v_1_0.vo.PayDataBean;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;

public interface ILianLianPayService {

	/**
	 * 通过银行卡号查询银行卡信息
	 * @param bankCode
	 * @return
	 * @throws Throwable
	 */
	String queryBankCardByNo(String bankCardNo) throws Throwable;
	
	/**
	 * 设置wap请求所需的参数
	 * @param param
	 * @throws Exception
	 */
	void setParamsForWap(Map<String, String> param) throws Exception;
	
	/**
	 * 如果为测试模式测设置测试参数(放最后)
	 * @param param
	 * @throws Exception
	 */
	void setParamsForTest(Map<String, String> param) throws Exception;
	
	/**
	 * 设置请求参数用户信息
	 * @param param
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	UserInfo setUserInfo(Map<String, String> param, Integer userId) throws Throwable;
	
	/**
	 * 连连快捷支付回调确认
	 * TODO 这里可定义成一个统一的接口，不同业务实现处理
	 * @param data
	 * @throws Exception
	 */
	PaymentOrderEntity callbackConfirm(PayDataBean payDataBean) throws Exception;
	
}
