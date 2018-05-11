package com.fenlibao.p2p.service.sms;

import com.dimeng.framework.message.sms.entity.SmsTask;

public interface SmsExtracterService {

	public SmsTask[] extract(int maxCount, int expiresMinutes) throws Throwable;

	public void mark(long id, boolean success, String extra) throws Throwable;

	/**
	 * 验证码校验
	 * @param phoneNum
	 * @param captcha
	 * @param type
	 * @throws Exception
     */
	void captchaValidate(String phoneNum, String captcha, String type) throws Exception;

	/**
	 * 发短信
	 * @param mobile
	 * @param content
	 * @param type
	 * @throws Exception
     */
	void sendMsg(String mobile, String content, int type) throws Exception;
}
