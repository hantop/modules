package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.SendSmsRecord;

import java.util.Map;

public interface SendSmsRecordDao {

	public int insertSendSmsRecord(SendSmsRecord record);
	
	public int userSendPhoneCount(Map map);

	public int getSmsDateDiff(String content, String phoneNum);

	Integer insertT1040(Integer type, String content) throws Exception;

	void insertT1041(Integer msgId, String mobile) throws Exception;
}
