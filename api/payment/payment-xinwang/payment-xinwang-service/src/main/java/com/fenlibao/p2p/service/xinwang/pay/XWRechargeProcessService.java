package com.fenlibao.p2p.service.xinwang.pay;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;

import java.math.BigDecimal;

/**
 * @date 2017/5/23 18:02
 */
public interface XWRechargeProcessService {
    /**
     * 添加充值订单
     * @param userId
     * @param userRole
     *@param amount
     * @param requestNo
     * @param source 订单来源    @return
     */
    int addOrder(int userId, UserRole userRole, BigDecimal amount, String requestNo, Source source);

    /**
     * 添加充值订单 不校验金额
     * @param userId
     * @param amount
     * @param requestNo
     * @param source
     * @return
     */
    int addBackstageOrder(int userId, BigDecimal amount, String requestNo,Source source);

    void validateCurdateTotalAmount(int userId,UserRole userRole, BigDecimal amount);

    void rechargeAccept(XWRequest xwRequest);

    void rechargeFail(XWRequest xwRequest);

    void rechargeSuccess(XWRequest xwRequest, SystemOrder order, BusinessType businessType);

    void alternativeRechargeSuccess(XWRequest xwRequest,int orderId, BusinessType businessType);

    void fundsTransferSuccess(String sourceUserNo, String targetUserNo, XWRequest request, BusinessType businessType, BigDecimal amount);
}
