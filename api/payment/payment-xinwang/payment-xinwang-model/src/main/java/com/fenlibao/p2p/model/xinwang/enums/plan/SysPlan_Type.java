package com.fenlibao.p2p.model.xinwang.enums.plan;

/**
 * t_invest_plan.type
 */
public enum SysPlan_Type {
    YUE_SHENG(1,"月升计划"),
    SHENG_XIN(2,"省心计划"),
    ;

    protected final Integer code;
    protected final String name;

    SysPlan_Type(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SysPlan_Type parse(Integer code) throws Exception{
        SysPlan_Type result=null;
        for(SysPlan_Type item: SysPlan_Type.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
