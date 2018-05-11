package com.fenlibao.p2p.model.xinwang.enums.account;

import org.apache.commons.lang3.StringUtils;

/**
 * @date 2017/5/25 20:35
 */
public enum SysFundAccountType {
    /**
     * 往来账户
     */
    WLZH("往来账户"),

    /**
     * 风险保证金账户
     */
    FXBZJZH("风险保证金账户"),

    /**
     * 锁定账户
     */
    SDZH("锁定账户"),

    /**
     * 华兴往来账户（该账户的余额不做维护，最终的余额已华兴的为准）
     */
    HXWLZH("华兴往来账户"),
    /**
     * 华兴锁定账户账户（该账户的余额不做维护）
     */
    HXSDZH("华兴锁定账户"),

    XW_INVESTOR_WLZH("新网出借人往来账户"),

    XW_INVESTOR_SDZH("新网出借人锁定账户"),

    XW_BORROWERS_WLZH("新网借款人往来账户"),

    XW_BORROWERS_SDZH("新网借款人锁定账户"),

    XW_GUARANTEECORP_WLZH("新网担保机构往来账户"),

    XW_GUARANTEECORP_SDZH("新网担保机构锁定账户"),

    XW_PLATFORM_COMPENSATORY_WLZH("新网平台代偿往来账户"),

    XW_PLATFORM_COMPENSATORY_SDZH("新网平台代偿锁定账户"),

    XW_PLATFORM_MARKETING_WLZH("新网平台营销款往来账户"),

    XW_PLATFORM_MARKETING_SDZH("新网平台营销款锁定账户"),

    XW_PLATFORM_PROFIT_WLZH("新网平台分润往来账户"),

    XW_PLATFORM_PROFIT_SDZH("新网平台分润锁定账户"),

    XW_PLATFORM_INCOME_WLZH("新网平台收入往来账户"),

    XW_PLATFORM_INCOME_SDZH("新网平台收入锁定账户"),

    XW_PLATFORM_INTEREST_WLZH("新网平台派息往来账户"),

    XW_PLATFORM_INTEREST_SDZH("新网平台派息锁定账户"),

    XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH("新网平台代充值往来账户"),

    XW_PLATFORM_ALTERNATIVE_RECHARGE_SDZH("新网平台代充值锁定账户"),

    XW_PLATFORM_FUNDS_TRANSFER_WLZH("新网平台总账户往来账户"),

    XW_PLATFORM_FUNDS_TRANSFER_SDZH("新网平台总账户锁定账户"),

    XW_PLATFORM_URGENT_WLZH("新网平台垫资往来账户"),

    XW_PLATFORM_URGENT_SDZH("新网平台垫资锁定账户"),

    XW_ENTRUST_BORROWERS_WLZH("新网委托借款人往来账户"),

    XW_ENTRUST_BORROWERS_SDZH("新网委托借款人锁定账户"),
    ;
    protected final String chineseName;

    private SysFundAccountType(String chineseName){
        this.chineseName = chineseName;
    }
    /**
     * 获取中文名称.
     *
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
    /**
     * 解析字符串.
     *
     * @return {@link SysFundAccountType}
     */
    public static final SysFundAccountType parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return SysFundAccountType.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
