package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * 交易类型
 * @date 2017/5/5 17:03
 */
public enum XWTradeType {
    TENDER("TENDER","出借"),
    REPAYMENT("REPAYMENT","还款"),
    CREDIT_ASSIGNMENT("CREDIT_ASSIGNMENT","债权认购"),
    COMPENSATORY("COMPENSATORY","直接代偿"),
    INDIRECT_COMPENSATORY("INDIRECT_COMPENSATORY","间接代偿"),
    PLATFORM_INDEPENDENT_PROFIT("PLATFORM_INDEPENDENT_PROFIT","独立分润"),
    MARKETING("MARKETING","平台营销款"),
    PLATFORM_SERVICE_DEDUCT("PLATFORM_SERVICE_DEDUCT","收费"),
    FUNDS_TRANSFER("FUNDS_TRANSFER","平台资金划拨"),

    ;

    protected final String code;
    protected final String name;

    XWTradeType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWTradeType parse(String code) throws Exception{
        XWTradeType result=null;
        for(XWTradeType item: XWTradeType.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
