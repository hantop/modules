package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * 业务类型
 * @date 2017/5/5 17:03
 */
public enum XWBizType {
    TENDER("TENDER","出借确认"),
    REPAYMENT("REPAYMENT","还款"),
    CREDIT_ASSIGNMENT("CREDIT_ASSIGNMENT","债权认购"),
    COMPENSATORY("COMPENSATORY","代偿"),
    COMPENSATORY_REPAYMENT("COMPENSATORY_REPAYMENT","还代偿款"),
    PLATFORM_INDEPENDENT_PROFIT("PLATFORM_INDEPENDENT_PROFIT","独立分润"),
    MARKETING("MARKETING","营销红包"),
    INTEREST("INTEREST","派息"),
    ALTERNATIVE_RECHARGE("ALTERNATIVE_RECHARGE","代充值"),
    INTEREST_REPAYMENT("INTEREST_REPAYMENT","还派息款"),
    COMMISSION("COMMISSION","佣金"),
    PROFIT("PROFIT","关联分润"),
    APPEND_FREEZE("APPEND_FREEZE","追加冻结"),
    DEDUCT("DEDUCT","平台服务费"),
    FUNDS_TRANSFER("FUNDS_TRANSFER","平台资金划拨"),

    ;

    protected final String code;
    protected final String name;

    XWBizType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWBizType parse(String code) throws Exception{
        XWBizType result=null;
        for(XWBizType item: XWBizType.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
