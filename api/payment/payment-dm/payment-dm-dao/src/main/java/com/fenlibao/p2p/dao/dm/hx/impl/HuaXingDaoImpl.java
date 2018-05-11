package com.fenlibao.p2p.dao.dm.hx.impl;

import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.entity.HXRepayOrder;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcai on 2016/10/10.
 */
@Repository
public class HuaXingDaoImpl implements HuaXingDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "HuaXingMapper.";


    @Override
    public void saveMessage(String tradeCode, String oldFlowNum, String flowNum, String message) {
        Map<String, String> params = new HashMap<>(3);
        params.put("tradeCode", tradeCode);
        params.put("flowNum", flowNum);
        params.put("oldFlowNum", oldFlowNum);
        params.put("message", message);
        sqlSession.insert(MAPPER + "saveMessage", params);
    }

    @Override
    public void createOrder(HXOrder order) {
        sqlSession.insert(MAPPER + "createOrder", order);
    }

    @Override
    public void addOrderAndClientRelation(int clientType, int orderId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("clientType", clientType);
        params.put("orderId", orderId);
        sqlSession.insert(MAPPER + "addOrderAndClientRelation", params);
    }

    @Override
    public HXOrder getOrderByFlowNum(String flowNum) {
        Map<String, String> param = new HashMap<>(1);
        param.put("flowNum", flowNum);
        return sqlSession.selectOne(MAPPER + "getOrderByFlowNum", param);
    }

    @Override
    public HXOrder getOrderById(int orderId) {
        return sqlSession.selectOne(MAPPER + "getOrderById", orderId);
    }

    @Override
    public void submitOrder(HXOrder order) {
        sqlSession.update(MAPPER + "submitOrder", order);
    }

    @Override
    public void completeOrder(HXOrder order) {
        sqlSession.update(MAPPER + "completeOrder", order);
    }

	@Override
	public Integer getClientType(int orderId) {
		return sqlSession.selectOne(MAPPER + "getClientType", orderId);
	}

    @Override
    public int addRewardsRecord(int userId, BigDecimal amount, String remark) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("userId", userId);
        params.put("amount", amount);
        params.put("remark", remark);
        sqlSession.insert(MAPPER + "addRewardsRecord", params);
        String id = String.valueOf(params.get("id"));
        return Integer.parseInt(id);
    }
    
	@Override
	public void createRepayOrder(HXRepayOrder order) {
		sqlSession.insert(MAPPER + "createRepayOrder", order);
	}

	@Override
	public HXRepayOrder getRepayOrderById(int orderId) {
		return sqlSession.selectOne(MAPPER + "getRepayOrderById", orderId);
	}

    @Override
    public void updateRewardsRecord(int id, int state, String result) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("id", id);
        params.put("state", state);
        params.put("result", result);
        sqlSession.update(MAPPER + "updateRewardsRecord", params);
    }

    @Override
	public HXOrder getOrder(Map<String, Object> param) {
        return sqlSession.selectOne(MAPPER + "getOrder", param);
	}
}
