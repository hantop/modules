package com.fenlibao.platform.dao.member;

import org.apache.ibatis.annotations.Param;

import com.fenlibao.platform.model.vo._SMSVO;

public interface MessageMapper {

	/**
	 * 保存待发送短信内容
	 * @param type
	 * @param content
	 * @return 待发送短信id
	 * @throws Exception
	 */
	void saveSMSContent(_SMSVO sms) throws Exception;
	
	/**
	 * 保存待发送短信手机号码
	 * @param SMSId
	 * @param phoneNum
	 * @throws Exception
	 */
	void saveSMSPhoneNum(@Param("SMSId")Integer SMSId, @Param("phoneNum")String phoneNum) throws Exception;
	
}
