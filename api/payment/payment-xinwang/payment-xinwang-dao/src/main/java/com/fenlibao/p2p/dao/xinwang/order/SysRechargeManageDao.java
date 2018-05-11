package com.fenlibao.p2p.dao.xinwang.order;

import com.fenlibao.p2p.model.xinwang.entity.order.RechargeOrderEntity;
import com.fenlibao.p2p.model.xinwang.entity.pay.SysPaymentLimitVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 该模块是公共的模块，且是后面建的，为了和别的模块冲突，加 manage......
 * Created by zcai on 2016/11/1.
 */
public interface SysRechargeManageDao {

    /**
     * 添加充值订单
     * @param order
     */
    void addOrder(RechargeOrderEntity order);

    /**
     * 获取充值订单
     * @param orderId
     * @return
     */
    RechargeOrderEntity getOrder(int orderId);

    void updateOrder(int orderId, String serialNum);
    
    /**
     * 获取待确认的订单
     * @param paymentInstitutionCode
     * @return
     */
    List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode);

    /**
     * 获取用户当天充值总金额
     * @param userId
     * @param code
     * @return
     */
    BigDecimal getCurdateTotalAmount(int userId, int code);

    /**
     * 获取充值限额
     * @return
     */
    List<SysPaymentLimitVO> getLimitList(String bankCode);
}
