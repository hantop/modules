package com.fenlibao.p2p.model.xinwang.enums.pay;

/**
 * 提现到账
 */
public enum WithdrawArrival {
    F ("F","否"),
    S("S","是"),
    ;

    protected final String code;
    protected final String name;

    WithdrawArrival(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static WithdrawArrival parse(String code) throws Exception{
        WithdrawArrival result=null;
        for(WithdrawArrival item: WithdrawArrival.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
