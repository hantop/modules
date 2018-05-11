package com.fenlibao.p2p.model.xinwang.enums.plan;

/**
 * Created by Administrator on 2017/5/12.
 */
public enum SysPlan_RepayMode {
    DEBX("DEBX","等额本息"),
    MYFX("MYFX","每月付息"),
    YCFQ("YCFQ","本息到期一次付清"),
    ;

    protected final String code;
    protected final String name;

    SysPlan_RepayMode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SysPlan_RepayMode parse(String code) throws Exception{
        SysPlan_RepayMode result=null;
        for(SysPlan_RepayMode item: SysPlan_RepayMode.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
