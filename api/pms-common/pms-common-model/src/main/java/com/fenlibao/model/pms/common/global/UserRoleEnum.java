package com.fenlibao.model.pms.common.global;

/**
 * Created by Administrator on 2017/6/22.
 * 用户角色枚举
 */
public enum UserRoleEnum {
    INVESTOR("投资人", "INVESTOR"),
    BORROWERS("借款人", "BORROWERS"),
    GUARANTEECORP("担保机构", "GUARANTEECORP"),
    INTERMEDIATOR("居间人", "INTERMEDIATOR"),
    COLLABORATOR("合作机构", "COLLABORATOR"),
    SUPPLIER("供应商", "SUPPLIER"),
    PLATFORM_COMPENSATORY("平台代偿账户", "PLATFORM_COMPENSATORY"),
    PLATFORM_MARKETING("平台营销款账户", "PLATFORM_MARKETING"),
    PLATFORM_PROFIT("平台分润账户", "PLATFORM_PROFIT"),
    PLATFORM_INCOME("平台收入账户", "PLATFORM_INCOME"),
    PLATFORM_INTEREST("平台派息账户", "PLATFORM_INTEREST"),
    PLATFORM_ALTERNATIVE_RECHARGE("平台代充值账户", "PLATFORM_ALTERNATIVE_RECHARGE"),
    PLATFORM_FUNDS_TRANSFER("平台总账户", "PLATFORM_FUNDS_TRANSFER");

    private String label;
    private String value;

    UserRoleEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static UserRoleEnum getByValue(String value) {
        UserRoleEnum s = null;
        UserRoleEnum[] types = UserRoleEnum.values();
        for (UserRoleEnum type : types) {
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

