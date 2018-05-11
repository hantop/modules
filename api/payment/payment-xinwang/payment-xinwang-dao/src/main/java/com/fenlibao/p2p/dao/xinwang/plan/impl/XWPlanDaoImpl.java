package com.fenlibao.p2p.dao.xinwang.plan.impl;

import com.fenlibao.p2p.dao.xinwang.plan.XWPlanDao;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlanCredit;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysPlanPayeeReceivePayment;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/6.
 */
@Repository
public class XWPlanDaoImpl implements XWPlanDao{
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWPlanMapper.";

    @Override
    public void fillCreditIdByTenderId(Map<String,Object> params) {
        sqlSession.update(MAPPER+"fillCreditIdByTenderId",params);
    }

    @Override
    public SysUserPlanCredit getUserPlanCreditByCreditId(Integer creditId) {
        return sqlSession.selectOne(MAPPER+"getUserPlanCreditByCreditId",creditId);
    }

    @Override
    public SysUserPlan getUserPlanById(Integer id) {
        return sqlSession.selectOne(MAPPER+"getUserPlanById",id);
    }

    @Override
    public SysPlan getPlanById(Integer id) {
        return sqlSession.selectOne(MAPPER+"getPlanById",id);
    }

    @Override
    public void updateUserPlanCreditReturnAmount(Map<String, Object> params) {
        sqlSession.update(MAPPER+"updateUserPlanCreditReturnAmount",params);
    }

    @Override
    public List<SysPlanPayeeReceivePayment> sumBidPlanData(Integer planId) {
        return sqlSession.selectList(MAPPER+"sumBidPlanData",planId);
    }
}
