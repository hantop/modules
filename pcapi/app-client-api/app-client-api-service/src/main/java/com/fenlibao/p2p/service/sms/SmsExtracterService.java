package com.fenlibao.p2p.service.sms;

import com.dimeng.framework.message.sms.entity.SmsTask;

public interface SmsExtracterService {

	public SmsTask[] extract(int maxCount, int expiresMinutes) throws Throwable;
	
	public void mark(long id, boolean success, String extra) throws Throwable;
}
