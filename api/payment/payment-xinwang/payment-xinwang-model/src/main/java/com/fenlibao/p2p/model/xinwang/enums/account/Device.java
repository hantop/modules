package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 终端类型
 * @author Mingway.Xu
 * @date 2017/5/6 13:58
 */
public enum  Device {
    PC("PC"),
    MOBILE("MOBILE");

    protected final String code;

    Device(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
