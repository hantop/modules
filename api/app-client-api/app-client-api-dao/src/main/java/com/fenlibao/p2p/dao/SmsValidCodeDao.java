package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.SmsValidcode;

import java.util.List;
import java.util.Map;

public interface SmsValidCodeDao {

	public void addSmsCode(SmsValidcode code) ;
	
	public List<SmsValidcode> getCode(Map<String, String> map);
	
	public int getSendSmsCount(Map<String, Object> map);
	
}
