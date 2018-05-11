package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * 还款进度
 */
public enum XWRepayProgress {
    /**
     * 平台还款预处理（修改还款计划，冻结还款金额，保存债权还款明细）已完成
     */
    PLATFORM_PRETREATMENT_FINISH,
    /**
     * 新网资金已冻结
     */
    XINWANG_FUND_FROZEN,
    /**
     * 新网全部还款请求受理
     */
    XINWANG_REPAY_REQUEST_ACCEPTED,
    /**
     * 新网全部营销（加息）请求受理
     */
    XINWANG_MARKETING_REQUEST_ACCEPTED,
    ;

}
