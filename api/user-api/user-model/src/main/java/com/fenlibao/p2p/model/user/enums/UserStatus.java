package com.fenlibao.p2p.model.user.enums;

public enum UserStatus {
    QY("启用"),
    SD("锁定"),
    HMD("黑名单"),
    ;

    protected final String chineseName;

    private UserStatus(String chineseName){
        this.chineseName = chineseName;
    }
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
}
