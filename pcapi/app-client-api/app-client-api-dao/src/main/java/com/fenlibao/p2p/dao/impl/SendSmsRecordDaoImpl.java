package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.model.entity.SendSmsRecord;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class SendSmsRecordDaoImpl implements SendSmsRecordDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "SendSmsRecordMapper.";
	
	@Override
	public int insertSendSmsRecord(SendSmsRecord record) {
		return sqlSession.insert(MAPPER+"insertSendSmsRecord", record);
	}

	@Override
	public int userSendPhoneCount(Map map) {
		return sqlSession.selectOne(MAPPER+"userSendPhoneCount",map);
	}

}
