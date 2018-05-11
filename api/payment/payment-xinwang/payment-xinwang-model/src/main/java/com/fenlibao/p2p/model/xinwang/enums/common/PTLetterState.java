package com.fenlibao.p2p.model.xinwang.enums.common;

import org.apache.commons.lang3.StringUtils;

/** 
 * T6123_F05
 */
public enum PTLetterState {


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

    private PTLetterState(String chineseName){
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
     * @return {@link PTLetterState}
     */
    public static final PTLetterState parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return PTLetterState.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
