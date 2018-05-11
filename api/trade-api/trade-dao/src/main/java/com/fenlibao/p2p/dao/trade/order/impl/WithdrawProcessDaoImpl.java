package com.fenlibao.p2p.dao.trade.order.impl;

import com.fenlibao.p2p.dao.trade.order.WithdrawProcessDao;
import com.fenlibao.p2p.model.trade.entity.order.T6503;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zcai on 2016/11/29.
 */
@Repository
public class WithdrawProcessDaoImpl implements WithdrawProcessDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "WithdrawProcessMapper.";

    @Override
    public void addOrder(T6503 order) throws Exception {
        sqlSession.insert(MAPPER + "addOrder", order);
    }

    @Override
    public T6503 getOrder(int orderId) {
        return sqlSession.selectOne(MAPPER + "getOrder", orderId);
    }

	@Override
	public void updateOrder(T6503 order) {
		sqlSession.update(MAPPER + "updateOrder", order);
	}

	@Override
	public List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode) {
		return sqlSession.selectList(MAPPER + "getOrderNeedConfirmed", paymentInstitutionCode);
	}
}
