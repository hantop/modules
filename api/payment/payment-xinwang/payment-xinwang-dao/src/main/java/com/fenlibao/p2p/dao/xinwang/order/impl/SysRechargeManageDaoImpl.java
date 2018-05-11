package com.fenlibao.p2p.dao.xinwang.order.impl;

import com.fenlibao.p2p.dao.xinwang.order.SysRechargeManageDao;
import com.fenlibao.p2p.model.xinwang.entity.order.RechargeOrderEntity;
import com.fenlibao.p2p.model.xinwang.entity.pay.SysPaymentLimitVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/11/1.
 */
@Repository
public class SysRechargeManageDaoImpl implements SysRechargeManageDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysRechargeManageMapper.";

    @Override
    public void addOrder(RechargeOrderEntity order) {
        sqlSession.insert(MAPPER + "addOrder", order);
    }

    @Override
    public RechargeOrderEntity getOrder(int orderId) {
        return sqlSession.selectOne(MAPPER + "getOrder", orderId);
    }

    @Override
    public void updateOrder(int orderId, String serialNum) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("orderId", orderId);
        params.put("serialNum", serialNum);
        sqlSession.update(MAPPER + "updateOrder", params);
    }
    
    /**
     * 获取待确认的订单
     * @param paymentInstitutionCode
     * @return
     */
    public List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode){
    	return sqlSession.selectList(MAPPER + "getOrderNeedConfirmed", paymentInstitutionCode);
    }

    @Override
    public BigDecimal getCurdateTotalAmount(int userId, int code) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("channelCode", code);
        return sqlSession.selectOne(MAPPER + "getCurdateTotalAmount", params);
    }

    @Override
    public List<SysPaymentLimitVO> getLimitList(String bankCode) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("bankCode", bankCode);
        return sqlSession.selectList(MAPPER + "getLimitList", params);
    }
}
