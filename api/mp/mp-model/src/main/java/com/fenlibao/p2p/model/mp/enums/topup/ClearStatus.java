package com.fenlibao.p2p.model.mp.enums.topup;

/**
 * 积分清除状态
 * @date 2017/6/23 10:01
 */
public enum ClearStatus {
    YES(1),
    NO(0);

    protected int status;

    ClearStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
