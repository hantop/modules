package com.fenlibao.p2p.model.xinwang.enums.common;

import org.apache.commons.lang3.StringUtils;

/** 
 * 对账状态
 */
public enum CheckStatus {


    /** 
     * 未对账
     */
    WDZ("未对账"),

    /** 
     * 已对账
     */
    YDZ("已对账");

    protected final String chineseName;

    private CheckStatus(String chineseName){
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
     * @return {@link CheckStatus}
     */
    public static final CheckStatus parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return CheckStatus.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
