package com.fenlibao.p2p.model.xinwang.enums.common;

/**
 * Created by Administrator on 2017/5/12.
 */
public enum XWCapitalFlowLevel {
    XT("XT","系统"),
    YH("YH","用户"),
    ;

    protected final String code;
    protected final String name;

    XWCapitalFlowLevel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWCapitalFlowLevel parse(String code) throws Exception{
        XWCapitalFlowLevel result=null;
        for(XWCapitalFlowLevel item: XWCapitalFlowLevel.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
