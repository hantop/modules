package com.fenlibao.p2p.service.payment.tp.baofoo;

import java.math.BigDecimal;

public interface BaofooRechargeService {

    /**
     * 预充值
     * @param orderId 充值订单ID(6501.f01)
     * @param serialNum 系统订单流水(6501.f10)
     * @throws Exception
     */
    String pre(int orderId, String serialNum, String userIp) throws Exception;

    /**
     * 确认充值
     * @param serialNum
     * @param captcha
     * @throws Exception
     */
    BigDecimal confirm(String serialNum, String captcha) throws Exception;
    
    /**
     * 充值结果查询
     * @param orderId
     * @throws Exception
     */
    void queryResult(int orderId) throws Exception;
}
