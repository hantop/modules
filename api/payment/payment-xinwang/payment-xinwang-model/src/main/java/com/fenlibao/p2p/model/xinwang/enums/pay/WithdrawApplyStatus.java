package com.fenlibao.p2p.model.xinwang.enums.pay;

import com.fenlibao.p2p.model.xinwang.enums.account.AuditStatus;

/**
 * t6130.F09
 */
public enum WithdrawApplyStatus {
    DSH ("DSH","待审核"),
    DFK("DFK","待放款"),
    TXSB("TXSB","提现失败"),
    FKZ("FKZ","放款中"),
    YFK("YFK","已放款"),
    ;

    protected final String code;
    protected final String name;

    WithdrawApplyStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static WithdrawApplyStatus parse(String code) throws Exception{
        WithdrawApplyStatus result=null;
        for(WithdrawApplyStatus item:WithdrawApplyStatus.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
