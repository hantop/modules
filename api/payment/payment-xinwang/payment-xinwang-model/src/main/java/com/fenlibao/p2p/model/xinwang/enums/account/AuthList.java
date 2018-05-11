package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 用户授权列表
 * @date 2017/5/5 17:03
 */
public enum AuthList {
    TENDER ("TENDER","自动出借"),
    REPAYMENT("REPAYMENT","自动还款"),
    CREDIT_ASSIGNMENT("CREDIT_ASSIGNMENT","自动债权认购"),
    COMPENSATORY("COMPENSATORY","自动代偿"),
    WITHDRAW("WITHDRAW","自动提现"),
    RECHARGE("RECHARGE","自动充值");

    protected final String code;
    protected final String name;

    AuthList(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getAllCodes() throws Exception{
        StringBuilder sb=new StringBuilder();
        for(AuthList item:AuthList.values()){
            sb.append(item.getCode());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public static String getAllCodesExcludeRecharge() throws Exception{
        StringBuilder sb=new StringBuilder();
        for(AuthList item:AuthList.values()){
            if(!"RECHARGE".equals(item.getCode())){
                sb.append(item.getCode());
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
