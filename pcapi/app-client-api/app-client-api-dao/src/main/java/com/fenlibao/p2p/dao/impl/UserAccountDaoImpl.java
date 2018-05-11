package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.UserAccountDao;
import com.fenlibao.p2p.model.entity.UserAccount;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserAccountDaoImpl implements UserAccountDao {

	@Resource
	private SqlSession sqlSession;

	private static final String MAPPER = "UserAccountMapper.";
	
	@Override
	public UserAccount getUserAccount(Map map) {
		return this.sqlSession.selectOne(MAPPER+"getUserAccount", map);
	}

	@Override
	public int addUserAccount(Map<String, Object> map) {
		return sqlSession.insert("addUserAccount", map);
	}

	@Override
	public BigDecimal getWLZHBalance(Integer userId) {
		return this.sqlSession.selectOne(MAPPER + "getWLZHBalance", userId);
	}

	@Override
	public BigDecimal getSdUserFreezeSum(Integer userId) {
		return this.sqlSession.selectOne(MAPPER + "getSdUserFreezeSum", userId);
	}

	@Override
	public BigDecimal getNewTenderFreezeSum(Integer userId,int cgNum) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("cgNum", cgNum);
		return this.sqlSession.selectOne(MAPPER + "getNewTenderFreezeSum", params);
	}

	@Override
	public BigDecimal getPlanFreezeSum(Integer userId) {
		return this.sqlSession.selectOne(MAPPER + "getPlanFreezeSum", userId);
	}

	@Override
	public BigDecimal getCgBalance(Integer userId) {
		return this.sqlSession.selectOne(MAPPER + "getCgBalance", userId);
	}
}
