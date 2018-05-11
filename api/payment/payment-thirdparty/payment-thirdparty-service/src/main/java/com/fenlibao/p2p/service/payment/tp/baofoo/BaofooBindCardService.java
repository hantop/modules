package com.fenlibao.p2p.service.payment.tp.baofoo;

public interface BaofooBindCardService {
	/**
	 * 绑卡
	 * @param userId 用户id
	 * @param bankCardNum 银行卡号（解密）
	 * @param phoneNum 手机号（解密）
	 * @param bankCode 银行编码（t5020.F04）
	 * @throws Exception
	 */
	void bindCard(int userId,String bankCardNum,String phoneNum,String bankCode) throws Exception;
	
	/**
	 * 绑卡结果查询
	 * @param bankCardNum 银行卡号（解密）
	 * @throws Exception
	 */
	String bindCardResultQuery(String bankCardNum) throws Exception;

	/**
	 * 校验绑卡
	 * @param userId
	 * @throws Exception
     */
	boolean validateBind(int userId);
}
