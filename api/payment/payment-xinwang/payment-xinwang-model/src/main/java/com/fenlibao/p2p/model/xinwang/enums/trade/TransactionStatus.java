package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * 交易状态
 * @date 2017/5/26 11:37
 */
public enum TransactionStatus {
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

    protected final String status;

    TransactionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
