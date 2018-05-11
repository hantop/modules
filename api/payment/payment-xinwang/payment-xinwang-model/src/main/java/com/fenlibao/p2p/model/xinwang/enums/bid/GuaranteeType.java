package com.fenlibao.p2p.model.xinwang.enums.bid;

import org.apache.commons.lang3.StringUtils;

/** 
 * 担保方案
 */
public enum GuaranteeType{


    /** 
     * 本息全额担保
     */
    BXQEDB("本息全额担保"),

    /** 
     * 本金全额担保
     */
    BJQEDB("本金全额担保");

    protected final String chineseName;

    private GuaranteeType(String chineseName){
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
     * @return {@link GuaranteeType}
     */
    public static final GuaranteeType parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return GuaranteeType.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
