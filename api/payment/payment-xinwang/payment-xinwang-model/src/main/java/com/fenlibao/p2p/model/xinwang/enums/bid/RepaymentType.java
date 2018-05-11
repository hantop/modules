package com.fenlibao.p2p.model.xinwang.enums.bid;

import org.apache.commons.lang3.StringUtils;

/** 
 * 还款方式
 */
public enum RepaymentType{


    /** 
     * 等额本息
     */
    DEBX("等额本息"),

    /** 
     * 每月付息,到期还本
     */
    MYFX("每月付息,到期还本"),

    /** 
     * 本息到期一次付清
     */
    YCFQ("本息到期一次付清"),

    /** 
     * 等额本金
     */
    DEBJ("等额本金");

    protected final String chineseName;

    private RepaymentType(String chineseName){
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
     * @return {@link RepaymentType}
     */
    public static final RepaymentType parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return RepaymentType.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
