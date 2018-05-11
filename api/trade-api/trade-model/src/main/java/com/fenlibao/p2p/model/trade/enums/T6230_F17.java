package com.fenlibao.p2p.model.trade.enums;

import org.apache.commons.lang3.StringUtils;

/** 
 * 付息方式
 */
public enum T6230_F17{


    /** 
     * 自然月
     */
    ZRY("自然月"),

    /** 
     * 固定日
     */
    GDR("固定日");

    protected final String chineseName;

    private T6230_F17(String chineseName){
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
     * @return {@link T6230_F17}
     */
    public static final T6230_F17 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6230_F17.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
