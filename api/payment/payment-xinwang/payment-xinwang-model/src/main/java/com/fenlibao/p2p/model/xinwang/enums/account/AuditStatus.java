package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 用户注册审核状态
 * @date 2017/5/5 17:03
 */
public enum AuditStatus {
    AUDIT ("AUDIT","审核中"),
    PASSED("PASSED","审核通过"),
    BACK("BACK","审核回退"),
    REFUSED("REFUSED","审核拒绝"),
    ;

    protected final String code;
    protected final String name;

    AuditStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AuditStatus parse(String code) throws Exception{
        AuditStatus result=null;
        for(AuditStatus item:AuditStatus.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
