package com.fenlibao.p2p.model.xinwang.enums;

/**
 * @author Mingway.Xu
 * @date 2017/5/11 11:42
 */
public enum  CallBackMode {
    DIRECT_CALLBACK("DIRECT_CALLBACK"),;

    protected final String code;

    CallBackMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
