package com.fenlibao.p2p.model.xinwang.enums.bid;

import org.apache.commons.lang3.StringUtils;

/** 
 * 是否有担保
 */
public enum IS_DB{


    /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private IS_DB(String chineseName){
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
     * @return {@link IS_DB}
     */
    public static final IS_DB parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return IS_DB.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
