package com.fenlibao.p2p.dao.user.impl;


import com.fenlibao.p2p.dao.user.RiskTestDao;
import com.fenlibao.p2p.model.entity.user.RiskTestOption;
import com.fenlibao.p2p.model.entity.user.RiskTestQuestion;
import com.fenlibao.p2p.model.entity.user.RiskTestResult;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RiskTestDaoImpl implements RiskTestDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "RiskTestMapper.";
	
	@Override
	public List<RiskTestQuestion> getQuestionList() {
		return sqlSession.selectList(MAPPER+"getQuestionList");
	}

	@Override
	public List<RiskTestOption> getOptionByQid(int qid) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qid", qid);
		return sqlSession.selectList(MAPPER+"getOptionByQid", map);
	}

	@Override
	public  RiskTestResult getResultByScore(int score) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("score", score);
		return sqlSession.selectOne(MAPPER+"getResultByScore", map);
	}

	@Override
	public int addTestResult(int resultId, int userId,
			int score) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("resultId", resultId);
		map.put("userId", userId);
		map.put("score", score);
		return sqlSession.insert(MAPPER+"addTestResult", map);
	}

	@Override
	public Map getTestResultByUid(int userId) {
		return sqlSession.selectOne(MAPPER+"getTestResultByUid", userId);
	}
	
}