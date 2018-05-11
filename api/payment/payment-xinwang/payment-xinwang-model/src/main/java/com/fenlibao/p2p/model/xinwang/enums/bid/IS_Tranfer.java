package com.fenlibao.p2p.model.xinwang.enums.bid;

import org.apache.commons.lang3.StringUtils;

/** 
 * 是否线下债权转让
 */
public enum IS_Tranfer{


    /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private IS_Tranfer(String chineseName){
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
     * @return {@link IS_Tranfer}
     */
    public static final IS_Tranfer parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return IS_Tranfer.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
