package com.fenlibao.p2p.model.trade.enums;

import org.apache.commons.lang3.StringUtils;

/** 
 * 状态
 */
public enum T6123_F05{


    /** 
     * 未读
     */
    WD("未读"),

    /** 
     * 已读
     */
    YD("已读"),

    /** 
     * 删除
     */
    SC("删除");

    protected final String chineseName;

    private T6123_F05(String chineseName){
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
     * @return {@link T6123_F05}
     */
    public static final T6123_F05 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6123_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
