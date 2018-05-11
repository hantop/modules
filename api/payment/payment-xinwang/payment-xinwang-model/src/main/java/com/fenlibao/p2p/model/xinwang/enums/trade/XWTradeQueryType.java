package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * 新网交易查询类型
 */
public enum XWTradeQueryType {
    /**
     * 充值
     */
    RECHARGE,
    /**
     * 提现
     */
    WITHDRAW,
    /**
     * 交易预处理
     */
    PRETRANSACTION,
    /**
     * 交易确认
     */
    TRANSACTION,
    /**
     * 冻结
     */
    FREEZE,
    /**
     * 债权出让
     */
    DEBENTURE_SALE,
    /**
     * 取消预处理
     */
    CANCEL_PRETRANSACTION,
    /**
     * 解冻
     */
    UNFREEZE,
    /**
     * 提现拦截
     */
    INTERCEPT_WITHDRAW,
    /**
     * 通用冻结
     */
    GENERAL_FREEZE,
    ;
}
