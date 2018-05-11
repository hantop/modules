package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * t6110.F07 用户状态
 * @date 2017/5/5 17:03
 */
public enum SysUserStatus {
    QY ("QY","启用"),
    SD("SD","锁定"),
    HMD("HMD","黑名单"),
    ;

    protected final String code;
    protected final String name;

    SysUserStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SysUserStatus parse(String code) throws Exception{
        SysUserStatus result=null;
        for(SysUserStatus item: SysUserStatus.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
