package com.fenlibao.p2p.dao.trade.order.impl;

import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.model.trade.entity.bid.T6504;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zcai on 2016/11/1.
 */
@Repository
public class OrderManageDaoImpl implements OrderManageDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "OrderManageMapper.";

    @Override
    public void add(T6501 order) {
        sqlSession.insert(MAPPER + "add", order);
    }

    @Override
    public void update(T6501 order) {
        sqlSession.update(MAPPER + "update", order);
    }

    @Override
    public T6501 get(int id) {
        return sqlSession.selectOne(MAPPER + "get", id);
    }

	@Override
	public void updateByFlowNo(T6501 order) {
		sqlSession.update(MAPPER + "updateByFlowNo", order);
	}

	@Override
	public List<T6501> getByFlowNo(String code) {
		return sqlSession.selectList(MAPPER + "getByFlowNo", code);
	}

    @Override
    public void addTenderOrder(T6504 tenderOrder) {
        sqlSession.insert(MAPPER + "addTenderOrder", tenderOrder);
    }

    @Override
    public T6501 getBySerialNum(String serialNum) {
        return sqlSession.selectOne(MAPPER + "getBySerialNum", serialNum);
    }
}
