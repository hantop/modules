package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.SendEmail;

import java.util.List;
import java.util.Map;

public interface SendEmailDao {

	public void addSendEmail(SendEmail sendEmail);
	
	public List<SendEmail> getList(Map map);
}
