package com.fenlibao.p2p.service.xinwang.account;

import java.math.BigDecimal;

/**
 * 用户的资金账号service
 */
public interface XWFundService {
    /**
     *  资金冻结
     * @param userId
     * @param amount
     * @throws Exception
     */
    void doFreezeFund(Integer userId, BigDecimal amount) throws Exception;
    /**
     * 资金解冻
     * @param userId
     * @param amount
     * @param sourceNo 冻结资金的流水号
     * @throws Exception
     */
    void doUnFreezeFund(Integer userId, BigDecimal amount, String sourceNo) throws Exception;
}
