package com.fenlibao.model.pms.common.global;

/**
 * Created by Administrator on 2017/5/31.
 */
public enum AccountSystemType {
    WLZH("普通", 1),
    XW_INVESTOR_WLZH("存管", 2);

    private String label;
    private int value;

    AccountSystemType(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public static AccountSystemType getByValue(int value) {
        AccountSystemType s = null;
        AccountSystemType[] types = AccountSystemType.values();
        for (AccountSystemType type : types) {
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

