package com.fenlibao.p2p.model.xinwang.enums.bid;

import org.apache.commons.lang3.StringUtils;

/** 
 * 担保方案
 */
public enum ProductTypeForCG {


    /**
     * 本息全额担保
     */
    PLAN("PLAN"),

    /**
     * 本金全额担保
     */
    BID("BID");

    protected final String code;

    private ProductTypeForCG(String code){
        this.code = code;
    }
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getCode() {
        return code;
    }
    /**
     * 解析字符串.
     * 
     * @return {@link ProductTypeForCG}
     */
    public static final ProductTypeForCG parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return ProductTypeForCG.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
