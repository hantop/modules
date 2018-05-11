package com.fenlibao.p2p.model.trade.enums;

import org.apache.commons.lang3.StringUtils;

/** 
 * 是否逾期
 */
public enum T6231_F19{


    /** 
     * 严重逾期
     */
    YZYQ("严重逾期"),

    /** 
     * 是(逾期)
     */
    S("是(逾期)"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private T6231_F19(String chineseName){
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
     * @return {@link T6231_F19}
     */
    public static final T6231_F19 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6231_F19.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
