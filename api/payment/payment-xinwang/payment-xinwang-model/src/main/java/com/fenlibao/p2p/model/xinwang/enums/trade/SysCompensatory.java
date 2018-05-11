package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * 是否代偿
 */
public enum SysCompensatory {
    TRUE,
    FALSE,
    ;

    public static SysCompensatory parse(Boolean value){
        SysCompensatory name=SysCompensatory.FALSE;
        if(value){
            name=SysCompensatory.TRUE;
        }
        return name;
    }
}
