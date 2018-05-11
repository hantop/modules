package com.fenlibao.model.pms.common.global;

/**
 * Created by Administrator on 2017/6/22.
 * 用户类型枚举
 */
public enum UserTypeEnum {
    PERSONAL("个人", "PERSONAL"),
    ORGANIZATION("企业", "ORGANIZATION");

    private String label;
    private String value;

    UserTypeEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static UserTypeEnum getByValue(String value) {
        UserTypeEnum s = null;
        UserTypeEnum[] types = UserTypeEnum.values();
        for (UserTypeEnum type : types) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

