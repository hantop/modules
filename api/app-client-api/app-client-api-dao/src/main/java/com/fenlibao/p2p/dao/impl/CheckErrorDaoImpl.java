package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.CheckErrorDao;
import com.fenlibao.p2p.model.entity.CheckError;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class CheckErrorDaoImpl implements CheckErrorDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "CheckErrorMapper.";
	
	@Override
	public int matchVerifyCodeErrorCount(Map map) {
		return sqlSession.selectOne(MAPPER+"matchVerifyCodeErrorCount", map);
	}

	@Override
	public void insertMatchVerifyCodeError(CheckError checkError) {
		sqlSession.insert(MAPPER+"insertMatchVerifyCodeError", checkError);
	}

}
