package com.fenlibao.p2p.model.enums;

/**
 * 用户操作行为日志记录
 * .
 */
public enum LogUrlEnum {
    LOGIN("/user/login", "登录", 0),
    CANCLE("/user/logout", "注销", 1),
    RECHARGE("/gateway/recharge", "充值", 2),
    WITHDRAWALS("/gateway/withdraw", "提现", 3),
    INVEST("/bid/doBid", "出借", 4),
    LOAN("CG", "借款", 5),
    CREDITASSIGNMENT("/transferOut/applyfor", "债转申请", 6),
    BUY_CREDIT("/transferIn/purchase", "购买债权", 7),
    BIND_CARD("/gateway/bindBankCard", "绑定银行卡", 8),
    CANCLE_CARD("/gateway/unbindBankCard", "解绑银行卡", 9),
    MODIFY_PASSWORD("/user/retrievePassword","修改登录密码",10);
            ;

    private String code;
    private String name;
    private int index;

    LogUrlEnum(String code, String name, int index) {
        this.code = code;
        this.name = name;
        this.index = index;
    }

    public static int length() {
        return LogUrlEnum.values().length;
    }

    public static LogUrlEnum parse(int index) {    //手写的从int到enum的转换函数
        for (LogUrlEnum item : LogUrlEnum.values()) {
            if (item.getIndex() == index) {
                return item;
            }
        }
        return null;
    }

    public static LogUrlEnum parse(String code) {
        for (LogUrlEnum item : LogUrlEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
