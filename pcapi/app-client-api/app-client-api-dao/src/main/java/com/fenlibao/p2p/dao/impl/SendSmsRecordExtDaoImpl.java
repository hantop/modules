package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.SendSmsRecordExtDao;
import com.fenlibao.p2p.model.entity.SendSmsRecordExt;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class SendSmsRecordExtDaoImpl implements SendSmsRecordExtDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "SendSmsRecordExtMapper.";
	
	@Override
	public void insertSendSmsRecordExt(SendSmsRecordExt recordExt) {

		this.sqlSession.insert(MAPPER+"insertSendSmsRecordExt", recordExt);
	}

}
