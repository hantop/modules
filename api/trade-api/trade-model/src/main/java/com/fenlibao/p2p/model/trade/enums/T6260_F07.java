package com.fenlibao.p2p.model.trade.enums;


import org.apache.commons.lang3.StringUtils;

/** 
 * 状态
 */
public enum T6260_F07{


    /** 
     * 待审核
     */
    DSH("待审核"),

    /** 
     * 转让中
     */
    ZRZ("转让中"),

    /** 
     * 已结束
     */
    YJS("已结束");

    protected final String chineseName;

    private T6260_F07(String chineseName){
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
     * @return {@link T6260_F07}
     */
    public static final T6260_F07 parse(String value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        try{
            return T6260_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
