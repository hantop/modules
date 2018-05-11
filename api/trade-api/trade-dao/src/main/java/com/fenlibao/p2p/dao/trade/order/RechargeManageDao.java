package com.fenlibao.p2p.dao.trade.order;

import com.fenlibao.p2p.model.trade.entity.order.T6502;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.vo.payment.PaymentLimitVO_;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 该模块是公共的模块，且是后面建的，为了和别的模块冲突，加 manage......
 * Created by zcai on 2016/11/1.
 */
public interface RechargeManageDao {

    /**
     * 添加充值订单
     * @param order
     */
    void addOrder(T6502 order);

    /**
     * 获取充值订单
     * @param orderId
     * @return
     */
    T6502 getOrder(int orderId);

    void updateOrder(int orderId, String serialNum);
    
    /**
     * 获取待确认的订单
     * @param paymentInstitutionCode
     * @return
     */
    List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode, Date requestTime);

    /**
     * 获取用户当天充值总金额
     * @param userId
     * @return
     */
    BigDecimal getCurdateTotalAmount(int userId, PaymentInstitution channel);

    /**
     * 获取银行充值限额
     * @param bankCode
     * @param channelCode
     * @return
     */
    List<PaymentLimitVO_> getLimitList(String bankCode, Integer channelCode);
}
