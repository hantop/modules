package com.fenlibao.model.pms.common.global;

public enum LogInfoType {
    LOGIN("登录", 0),
    LOGOUT("退出登录", 1),
    RECHARGE("充值", 2),
    WITHDRAW("提现", 3),
    INVEST("投资", 4),
    LOAN("借款", 5),
    CREDITOUT("债转申请", 6),
    CREDITIN("购买债权", 7),
    BIND("绑定银行卡", 8),
    UNBIND("解绑银行卡", 9),
    MODIFYPASSWORD("解绑银行卡", 10);

    private String label;
    private int value;

    LogInfoType(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public static LogInfoType getByValue(int value) {
        LogInfoType s = null;
        LogInfoType[] types = LogInfoType.values();
        for (LogInfoType type : types) {
            if (value == type.value) {
                s = type;
                break;
            }
        }
        return s;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

