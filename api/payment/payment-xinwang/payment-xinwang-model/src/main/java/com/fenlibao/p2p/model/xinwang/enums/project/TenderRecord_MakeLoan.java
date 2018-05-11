package com.fenlibao.p2p.model.xinwang.enums.project;

import org.apache.commons.lang3.StringUtils;

/** 
 * 是否已放款
 */
public enum TenderRecord_MakeLoan {


    /** 
     * 否
     */
    F("否"),

    /** 
     * 是
     */
    S("是");

    protected final String chineseName;

    private TenderRecord_MakeLoan(String chineseName){
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
     * @return {@link TenderRecord_MakeLoan}
     */
    public static final TenderRecord_MakeLoan parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return TenderRecord_MakeLoan.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
