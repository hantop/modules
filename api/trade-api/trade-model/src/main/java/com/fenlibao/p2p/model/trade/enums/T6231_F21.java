package com.fenlibao.p2p.model.trade.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 是否为按天借款,S:是;F:否
 */
public enum T6231_F21 {
	 /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private T6231_F21(String chineseName){
        this.chineseName = chineseName;
    }
    public String getChineseName() {
        return chineseName;
    }
    public static final T6231_F21 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6231_F21.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
