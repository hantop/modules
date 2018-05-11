package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * t6110.F06
 */
public enum PlatformAccount_UserType {
    ZRR ("PERSONAL","个人用户"),
    FZRR("ENTERPRISE","企业用户"),
            ;

    protected final String code;
    protected final String name;

    PlatformAccount_UserType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWUserType parse(String code) throws Exception{
        XWUserType result=null;
        for(XWUserType item: XWUserType.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
