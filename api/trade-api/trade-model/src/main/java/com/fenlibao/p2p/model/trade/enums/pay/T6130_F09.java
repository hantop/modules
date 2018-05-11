package com.fenlibao.p2p.model.trade.enums.pay;

import org.apache.commons.lang3.StringUtils;

/** 
 * 状态
 */
public enum T6130_F09{


    /** 
     * 待审核
     */
    DSH("待审核"),

    /** 
     * 待放款
     */
    DFK("待放款"),
    
    /** 
     * 放款中
     */
    FKZ("放款中"),

    /** 
     * 已放款
     */
    YFK("已放款"),

    /** 
     * 提现失败
     */
    TXSB("提现失败");
    

    protected final String chineseName;

    private T6130_F09(String chineseName){
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
     * @return {@link T6130_F09}
     */
    public static final T6130_F09 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6130_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
