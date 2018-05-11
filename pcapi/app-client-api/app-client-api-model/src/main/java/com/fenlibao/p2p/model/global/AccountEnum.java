package com.fenlibao.p2p.model.global;

/**
 * 资金账户
 * Created by chenzhixuan on 2015/8/24.
 */
public enum AccountEnum {

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
    SDZH("锁定账户");

    protected final String chineseName;

    private AccountEnum(String chineseName){
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
}
