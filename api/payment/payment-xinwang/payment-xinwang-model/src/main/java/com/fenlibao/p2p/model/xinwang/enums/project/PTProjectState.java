package com.fenlibao.p2p.model.xinwang.enums.project;

import org.apache.commons.lang3.StringUtils;

/** 
 * 平台标状态
 */
public enum PTProjectState {


    /** 
     * 申请中
     */
    SQZ("申请中"),

    /** 
     * 待审核
     */
    DSH("待审核"),

    /** 
     * 待发布
     */
    DFB("待发布"),

    /** 
     * 预发布
     */
    YFB("预发布"),

    /** 
     * 投标中
     */
    TBZ("出借中"),

    /** 
     * 待放款
     */
    DFK("待放款"),

    /** 
     * 还款中
     */
    HKZ("还款中"),

    /** 
     * 已结清
     */
    YJQ("已结清"),

    /** 
     * 已流标
     */
    YLB("已流标"),

    /** 
     * 已作废
     */
    YZF("已作废"),

    /** 
     * 已垫付
     */
    YDF("已垫付"),
     /** 
     * 垫付中
     */
    DFZ("垫付中")
    ;

    protected final String chineseName;

    private PTProjectState(String chineseName){
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
     * @return {@link PTProjectState}
     */
    public static final PTProjectState parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return PTProjectState.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
