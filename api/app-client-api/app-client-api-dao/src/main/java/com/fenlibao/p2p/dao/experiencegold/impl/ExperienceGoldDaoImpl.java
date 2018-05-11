package com.fenlibao.p2p.dao.experiencegold.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.experiencegold.IExperienceGoldDao;
import com.fenlibao.p2p.model.entity.ExperienceGoldInfo;
import com.fenlibao.p2p.model.entity.experiencegold.ExperienceGoldEarningsEntity;
import com.fenlibao.p2p.model.form.experiencegold.ExperienceGoldEarningsForm;
import com.fenlibao.p2p.model.form.experiencegold.UserExperienceGoldForm;

@Repository
public class ExperienceGoldDaoImpl extends BaseDao implements IExperienceGoldDao {

	public ExperienceGoldDaoImpl() {
		super("ExperienceGoldMapper");
	}

	@Override
	public List<ExperienceGoldInfo> getActivityByType(Map<String, Object> paramMap) {
		return sqlSession.selectList(MAPPER + "getActivityByType", paramMap);
	}

	@Override
	public void addUserExperienceGold(Map<String, Object> paramMap) {
		sqlSession.insert(MAPPER + "addUserExperienceGold", paramMap);
	}

	@Override
	public BigDecimal getTotalEarnings(int userId) {
		return sqlSession.selectOne(MAPPER + "getTotalEarnings", userId);
	}

	@Override
	public List<ExperienceGoldEarningsForm> getEarningsList(int userId, String timestamp) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("timestamp", timestamp);
		return sqlSession.selectList(MAPPER + "getEarningsList", params);
	}

	@Override
	public BigDecimal getEarningsYesterday(int userId) {
		return sqlSession.selectOne(MAPPER + "getEarningsYesterday", userId);
	}

	@Override
	public int insertDayEarnings(ExperienceGoldEarningsEntity entity) throws Exception {
		return sqlSession.insert(MAPPER + "insertDayEarnings", entity);
	}

	@Override
	public List<UserExperienceGoldForm> getUserExperienceGoldList() {
		return sqlSession.selectList(MAPPER + "getUserExperienceGoldList");
	}

	@Override
	public int updateUserExperience(int expId, Integer status, String yieldStatus) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("id", expId);
		params.put("status", status);
		params.put("yieldStatus", yieldStatus);
		return sqlSession.update(MAPPER + "updateUserExperience", params);
	}

	@Override
	public void addExpGoldFunding(Map<String, Object> paramMap) {
		sqlSession.insert(MAPPER + "addExpGoldFunding", paramMap);
	}

	@Override
	public List<ExperienceGoldInfo> getExperienceGolds(Map<String, Object> paramMap) {
		return sqlSession.selectList(MAPPER + "getExperienceGolds",paramMap);
	}

	@Override
	public int updateExperienceEarningsStatus(int expId, int userId) throws Exception {
		Map<String, Integer> params = new HashMap<>();
		params.put("expId", expId);
		params.put("userId", userId);
		return sqlSession.update(MAPPER + "updateExperienceEarningsStatus", params);
	}

	@Override
	public int updateUserExperienceYieldStatus(int id, String yieldStatus) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("yieldStatus", yieldStatus);
		return sqlSession.update(MAPPER + "updateUserExperienceYieldStatus", params);
	}

	@Override
	public List<UserExperienceGoldForm> getExpireUserExperienceGold() {
		return sqlSession.selectList(MAPPER + "getExpireUserExperienceGold");
	}

}
