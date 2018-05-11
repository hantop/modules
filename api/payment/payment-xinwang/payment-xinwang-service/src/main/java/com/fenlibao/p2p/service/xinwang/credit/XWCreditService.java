package com.fenlibao.p2p.service.xinwang.credit;

import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;

import java.math.BigDecimal;

/**
 * 债权相关
 * @date 2017/5/31 15:37
 */
public interface XWCreditService {
    /**
     * 存管债权转让
     * @param userId
     * @param creditId
     * @param requestNo
     */
    void doDebentureSale(int userId, int creditId, String requestNo) throws Exception;

    /**
     *存管取消债权转让
     * @param creditId
     */
    void doCancelDebentureSale( Integer creditId) throws Exception;

    /**
     * 存管购买债权
     * @param orderId 债权购买订单
     * @param zrCommission 转让人佣金
     * @param srCommission 受让人佣金
     */
    void doCreditAssignment(int orderId, BigDecimal zrCommission, BigDecimal srCommission) ;

    /**
     * 取消购买债权
     * @param orderId
     */
    void doCancelCreditAssignment(int orderId);

    /**
     * 发起取消债权转让请求
     * @param sourceRequestNo
     * @return
     * @throws Exception
     */
    BaseResult cancelTransfer(String sourceRequestNo) throws Exception;
}
