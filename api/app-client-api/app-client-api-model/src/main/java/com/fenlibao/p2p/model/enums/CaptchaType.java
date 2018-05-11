package com.fenlibao.p2p.model.enums;

/**
 * 验证码类型
 * Created by zcai on 2016/6/14.
 */
public enum CaptchaType {

    REGISTER(208, "注册", "sms.register.content"),
    RETRIEVE_PASSWORD(207, "找回密码", "sms.findLoginPwd.content"),
//    BIND_BANKCARD(209, "绑定银行卡", ""),
//    BIND_PHONE(201, "绑定手机", ""),
    RESET_TRADE_PASSWORD(202, "重置交易密码", "sms.resetTradePwd.content"),
    LOAN_APPCATION(203, "借款申请", "sms.default.content"),
    ;

    private Integer code;
    private String desc;
    private String templateKey;

    CaptchaType(Integer code, String desc, String templateKey) {
        this.code = code;
        this.desc = desc;
        this.templateKey = templateKey;
    }

    public static CaptchaType getByCode(Integer code) {
        CaptchaType[] types = CaptchaType.values();
        for (CaptchaType t : types) {
            if (t.getCode().equals(code)) {
                return t;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getTemplateKey() {
        return templateKey;
    }
}
