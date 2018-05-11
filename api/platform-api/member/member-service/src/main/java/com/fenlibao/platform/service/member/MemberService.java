package com.fenlibao.platform.service.member;

import com.fenlibao.platform.model.Restriction;
import com.fenlibao.platform.model.member.MerchantMember;

public interface MemberService {

	/**
	 * 第三方会员注册
	 * @param appid
	 * @param phoneNum
	 * @return openid
	 * @throws Exception
	 */
	String register(String appid, String phoneNum) throws Exception;

	MerchantMember getMerchantMember(String openid);

	void doConsumeRecord(String openid, String amount, String typecode, String pos_sn, MerchantMember member) throws Exception;

	/**
	 * 根据appid获取响应秘钥
	 * @param appid
	 * @return
	 */
	String getSecret(String appid);
	
	Restriction getAppidAndSecret(String appid, String secret);

	/**
	 * 商户自动注册
	 * @param phoneNum
	 * @return userId
	 * @throws Exception
     */
	Integer businessRegister(String phoneNum) throws  Exception;
	
}
