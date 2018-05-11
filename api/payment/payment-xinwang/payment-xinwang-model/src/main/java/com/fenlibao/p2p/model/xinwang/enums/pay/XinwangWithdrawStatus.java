package com.fenlibao.p2p.model.xinwang.enums.pay;

/**
 * 新网提现状态
 */
public enum XinwangWithdrawStatus {
    CONFIRMING("CONFIRMING","待确认"),
    ACCEPT("ACCEPT","已受理"),
    REMITING("REMITING","出款中"),
    SUCCESS("SUCCESS","提现成功"),
    FAIL("FAIL","提现失败"),
    ACCEPT_FAIL("ACCEPT_FAIL","受理失败"),

    ;

    protected final String code;
    protected final String name;

    XinwangWithdrawStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XinwangWithdrawStatus parse(String code) throws Exception{
        XinwangWithdrawStatus result=null;
        for(XinwangWithdrawStatus item: XinwangWithdrawStatus.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
