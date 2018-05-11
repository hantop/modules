package com.fenlibao.p2p.dao.trade.common.impl;

import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.model.trade.entity.CapitalFlow;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TradeCommonDaoImpl implements TradeCommonDao{
	
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "TradeCommonMapper.";

	@Override
	public Timestamp getCurrentTimestamp() throws Exception {
		return sqlSession.selectOne(MAPPER + "getCurrentTimestamp");
	}

	@Override
	public Date getCurrentDate() throws Exception {
		return sqlSession.selectOne(MAPPER+"getCurrentDate");
	}

	@Override
	public long insertT6123(Integer userId, String title, String state) throws Exception {
        Map<String, Object> params = new HashMap<>(3);
        params.put("userId", userId);
        params.put("title", title);
        params.put("state", state);
		sqlSession.insert(MAPPER+"insertT6123", params);
		return (long)(params.get("id")==null?0:params.get("id"));
	}

	@Override
	public void insertT6124(long letterId, String content) throws Exception {
        Map<String, Object> params = new HashMap<>(2);
        params.put("letterId", letterId);
        params.put("content", content);
		sqlSession.insert(MAPPER+"insertT6124", params);
	}

	@Override
	public String getSystemVariable(String id) throws Exception {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
		return sqlSession.selectOne(MAPPER+"getSystemVariable", params);
	}

	@Override
	public Integer insertT1040(Integer type, String content) throws Exception {
        Map<String, Object> params = new HashMap<>(2);
        params.put("type", type);
        params.put("content", content);
        sqlSession.insert(MAPPER+"insertT1040", params);
		return Integer.parseInt(String.valueOf(params.get("id")));
	}

	@Override
	public void insertT1041(Integer msgId, String mobile) throws Exception {
        Map<String, Object> params = new HashMap<>(2);
        params.put("msgId", msgId);
        params.put("mobile", mobile);
		sqlSession.insert(MAPPER+"insertT1041", params);
	}

	@Override
	public void insertT6102s(List<CapitalFlow> list) throws Exception {
		sqlSession.insert(MAPPER+"insertT6102s", list);
	}

}
