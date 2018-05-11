package com.fenlibao.p2p.dao.salary.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.salary.TJoinRecordDao;
import com.fenlibao.p2p.model.entity.TJoinRecord;

@Repository
public class TJoinRecordDaoImpl implements TJoinRecordDao {

	@Resource
	private SqlSession sqlSession;

	private static final String MAPPER = "JoinRecordMapper.";
	
	@Override
	public List<TJoinRecord> getJoinRecord(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getJoinRecord", map);
	}

}
