package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 用户类型（个人/企业）
 * @date 2017/5/5 17:03
 */
public enum XWUserType {
    PERSONAL ("PERSONAL","个人用户"),
    ORGANIZATION("ORGANIZATION","企业用户"),
    ;

    protected final String code;
    protected final String name;

    XWUserType(String code, String name) {
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
