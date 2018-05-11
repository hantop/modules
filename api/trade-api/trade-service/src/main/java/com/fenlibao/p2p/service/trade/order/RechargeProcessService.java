package com.fenlibao.p2p.service.trade.order;

import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 为了避免冲突加个 Process
 * Created by zcai on 2016/11/2.
 */
public interface RechargeProcessService {

    /**
     * 添加充值订单
     * @param userId
     * @param amount
     * @param institution 支付机构
     */
    int addOrder(int userId, BigDecimal amount, PaymentInstitution institution) throws Exception;
    /**
     * 获取待确认的订单
     * @param paymentInstitutionCode
     * @param requestTime
     * @return
     */
    List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode, Date requestTime) throws Exception;

    /**
     * 校验用户当天充值总金额
     * @param userId
     * @return
     */
    void validateCurdateTotalAmount(int userId, BigDecimal amount, PaymentInstitution channel) throws Exception;
}
