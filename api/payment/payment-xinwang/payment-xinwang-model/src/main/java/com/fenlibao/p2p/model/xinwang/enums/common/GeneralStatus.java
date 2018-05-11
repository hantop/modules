package com.fenlibao.p2p.model.xinwang.enums.common;

import java.io.Serializable;

/**
 * 新网通用业务处理状态
 * @date 2017/5/25 17:24
 */
public enum GeneralStatus implements Serializable{
    INIT("INIT"),
    SUCCESS("SUCCESS"),;

    protected final String status;

    GeneralStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
