package com.fenlibao.dao.pms.common.message.sms;

import com.fenlibao.model.pms.common.message.sms.SendSmsRecord;

import java.util.Map;

public interface SendSmsRecordMapper {

	public int insertSendSmsRecord(SendSmsRecord record);
	
	public int userSendPhoneCount(Map map);
}
