package com.fenlibao.platform.service.member.impl;

import javax.inject.Inject;

import com.fenlibao.platform.dao.member.MessageMapper;
import com.fenlibao.platform.model.vo._SMSVO;
import com.fenlibao.platform.service.member.MessageService;

public class MessageServiceImpl implements MessageService {

	@Inject
	private MessageMapper messageMapper;
	
	@Override
	public void sendSMS(Integer type, String phoneNum, String content) throws Exception {
		_SMSVO sms = new _SMSVO();
		sms.setContent(content);
		sms.setType(type);
		messageMapper.saveSMSContent(sms);
		if (sms.getId() == null) {
			throw new IllegalArgumentException("SMSId is null");
		}
		messageMapper.saveSMSPhoneNum(sms.getId(), phoneNum);
	}

}
