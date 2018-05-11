package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.SendEmailDao;
import com.fenlibao.p2p.model.entity.SendEmail;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class SendEmailDaoImpl implements SendEmailDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "SendEmailMapper.";
	
	@Override
	public void addSendEmail(SendEmail sendEmail) {
		sqlSession.insert(MAPPER+"insertEmail", sendEmail);
	}

	@Override
	public List<SendEmail> getList(Map map) {
		return sqlSession.selectList(MAPPER+"getSendEmail", map);
	}
}
