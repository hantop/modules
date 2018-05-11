package com.fenlibao.p2p.model.trade.enums.pay;

import org.apache.commons.lang3.StringUtils;

/** 
 * 是否已到账
 */
public enum T6130_F16{


    /** 
     * 否
     */
    F("否"),

    /** 
     * 是
     */
    S("是");

    protected final String chineseName;

    private T6130_F16(String chineseName){
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
     * @return {@link T6130_F16}
     */
    public static final T6130_F16 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6130_F16.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
