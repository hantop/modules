package com.fenlibao.p2p.model.trade.enums;

import org.apache.commons.lang3.StringUtils;

/** 
 * 否已放款
 */
public enum T6250_F08{


    /** 
     * 否
     */
    F("否"),

    /** 
     * 是
     */
    S("是");

    protected final String chineseName;

    private T6250_F08(String chineseName){
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
     * @return {@link T6250_F08}
     */
    public static final T6250_F08 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6250_F08.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
