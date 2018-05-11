package com.fenlibao.platform.service.member;

public interface MessageService {

	/**
	 * 发送短信
	 * @param type
	 * @param phoneNum
	 * @param content
	 * @throws Exception
	 */
	void sendSMS(Integer type, String phoneNum, String content) throws Exception;
	
}
