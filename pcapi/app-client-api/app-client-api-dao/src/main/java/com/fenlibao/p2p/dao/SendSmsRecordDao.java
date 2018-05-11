package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.SendSmsRecord;

import java.util.Map;

public interface SendSmsRecordDao {

	public int insertSendSmsRecord(SendSmsRecord record);
	
	public int userSendPhoneCount(Map map);
}
