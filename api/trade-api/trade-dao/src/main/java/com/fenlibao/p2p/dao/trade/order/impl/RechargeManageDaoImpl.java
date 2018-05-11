package com.fenlibao.p2p.dao.trade.order.impl;

import com.fenlibao.p2p.dao.trade.order.RechargeManageDao;
import com.fenlibao.p2p.model.trade.entity.order.T6502;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.vo.payment.PaymentLimitVO_;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/11/1.
 */
@Repository
public class RechargeManageDaoImpl implements RechargeManageDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "RechargeManageMapper.";

    @Override
    public void addOrder(T6502 order) {
        sqlSession.insert(MAPPER + "addOrder", order);
    }

    @Override
    public T6502 getOrder(int orderId) {
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
    public List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode, Date requestTime){
        Map<String, Object> params = new HashMap<>(2);
        params.put("paymentInstitutionCode", paymentInstitutionCode);
        params.put("requestTime", requestTime);
        return sqlSession.selectList(MAPPER + "getOrderNeedConfirmed", params);
    }

    @Override
    public BigDecimal getCurdateTotalAmount(int userId, PaymentInstitution channel) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("channelCode", channel.getCode());
        return sqlSession.selectOne(MAPPER + "getCurdateTotalAmount", params);
    }

    @Override
    public List<PaymentLimitVO_> getLimitList(String bankCode, Integer channelCode) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("bankCode", bankCode);
        params.put("channelCode", channelCode);
        return sqlSession.selectList(MAPPER + "getLimitList", params);
    }


}
