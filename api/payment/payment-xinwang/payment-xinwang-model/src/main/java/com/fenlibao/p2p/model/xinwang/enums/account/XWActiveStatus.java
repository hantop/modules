package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 用户状态（可用/不可用）
 */
public enum XWActiveStatus {
    ACTIVATED("ACTIVATED","可用"),
    DEACTIVATED("DEACTIVATED","不可用"),
    ;

    protected final String code;
    protected final String name;

    XWActiveStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWActiveStatus parse(String code) throws Exception{
        XWActiveStatus result=null;
        for(XWActiveStatus item: XWActiveStatus.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
