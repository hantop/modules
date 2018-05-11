package com.fenlibao.p2p.dao.xinwang.order.impl;

import com.fenlibao.p2p.dao.xinwang.order.SysWithdrawOrderDao;
import com.fenlibao.p2p.model.xinwang.entity.order.SysWithdrawOrder;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * s65.t6503
 */
@Repository
public class SysWithdrawOrderDaoImpl implements SysWithdrawOrderDao{
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysWithdrawOrderMapper.";

    @Override
    public void addOrder(SysWithdrawOrder order) {
        sqlSession.insert(MAPPER+"addOrder",order);
    }

    @Override
    public SysWithdrawOrder get(Integer orderId) {
        return sqlSession.selectOne(MAPPER+"get",orderId);
    }
}
