package com.fenlibao.p2p.model.trade.enums;

import org.apache.commons.lang3.StringUtils;

/** 
 * 是否有抵押
 */
public enum T6230_F13{


    /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private T6230_F13(String chineseName){
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
     * @return {@link T6230_F13}
     */
    public static final T6230_F13 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6230_F13.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
