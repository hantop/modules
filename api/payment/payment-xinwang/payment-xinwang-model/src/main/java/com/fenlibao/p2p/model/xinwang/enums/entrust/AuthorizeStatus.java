package com.fenlibao.p2p.model.xinwang.enums.entrust;

/**
 * 委托支付授权审核状态
 *
 * @date 2017/7/13 16:15
 */
public enum AuthorizeStatus {
    AUDIT("AUDIT","待审核"),
    FAIL("FAIL","授权失败"),
    SUCCESS("SUCCESS","授权成功");

    protected final String code;
    protected final String name;

    AuthorizeStatus(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AuthorizeStatus parse(String code) {
        for (AuthorizeStatus status : AuthorizeStatus.values()) {
            if (status.name().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
