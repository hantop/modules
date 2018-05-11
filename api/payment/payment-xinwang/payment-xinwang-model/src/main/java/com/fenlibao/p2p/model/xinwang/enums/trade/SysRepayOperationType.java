package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * 还款操作类型
 */
public enum SysRepayOperationType {
    REPAY("REPAY","到期还款"),
    PREPAY("PREPAY","提前还款"),
    ;

    protected final String code;
    protected final String name;

    SysRepayOperationType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SysRepayOperationType parse(String code) throws Exception{
        SysRepayOperationType result=null;
        for(SysRepayOperationType item: SysRepayOperationType.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
