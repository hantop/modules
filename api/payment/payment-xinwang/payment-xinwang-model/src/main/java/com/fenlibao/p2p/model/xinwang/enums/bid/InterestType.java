package com.fenlibao.p2p.model.xinwang.enums.bid;

import org.apache.commons.lang3.StringUtils;

/** 
 * 付息方式
 */
public enum InterestType{


    /** 
     * 自然月
     */
    ZRY("自然月"),

    /** 
     * 固定日
     */
    GDR("固定日");

    protected final String chineseName;

    private InterestType(String chineseName){
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
     * @return {@link InterestType}
     */
    public static final InterestType parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return InterestType.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
