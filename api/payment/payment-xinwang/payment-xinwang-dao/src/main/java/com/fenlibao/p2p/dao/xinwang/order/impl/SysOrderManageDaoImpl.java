package com.fenlibao.p2p.dao.xinwang.order.impl;

import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.model.xinwang.entity.order.BidOrder;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/11/1.
 */
@Repository
public class SysOrderManageDaoImpl implements SysOrderManageDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysOrderManageMapper.";

    @Override
    public void add(SystemOrder order) {
        sqlSession.insert(MAPPER + "add", order);
    }

    @Override
    public void update(SystemOrder order) {
        Map<String, Object> map = new HashMap<>();
        map.put("commitTime", order.getCommitTime());
        map.put("id", order.getId());
        map.put("completeTime", order.getCompleteTime());
        map.put("orderStatus", order.getOrderStatus());
        map.put("flowNo", order.getFlowNo());
        sqlSession.update(MAPPER + "update", map);
    }

    @Override
    public SystemOrder get(int id, boolean lock) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", id);
        params.put("lock", lock ? "lock" : null);
        return sqlSession.selectOne(MAPPER + "get", params);
    }

	@Override
	public void updateByFlowNo(SystemOrder order) {
		sqlSession.update(MAPPER + "updateByFlowNo", order);
	}

	@Override
	public List<SystemOrder> getByFlowNo(String code) {
		return sqlSession.selectList(MAPPER + "getByFlowNo", code);
	}

    @Override
    public void addTenderOrder(BidOrder tenderOrder) {
        sqlSession.insert(MAPPER + "addTenderOrder", tenderOrder);
    }

    @Override
    public SystemOrder getBySerialNum(String serialNum) {
        return sqlSession.selectOne(MAPPER + "getBySerialNum", serialNum);
    }

    @Override
    public void insertOrderExceptionLog(Integer orderId, String log) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("orderId", orderId);
        params.put("log", log);
        sqlSession.insert(MAPPER + "insertOrderExceptionLog", params);
    }
}
