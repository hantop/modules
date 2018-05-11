package com.fenlibao.model.pms.common.global;

/**
 * Created by Administrator on 2017/5/31.
 */
public enum SystemType {
    CG("存管", 2),
    NORMAL("普通", 1),
    COMMON("通用", 3);

    private String label;
    private int value;

    SystemType(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public static SystemType getByValue(int value) {
        SystemType s = null;
        SystemType[] types = SystemType.values();
        for (SystemType type : types) {
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

