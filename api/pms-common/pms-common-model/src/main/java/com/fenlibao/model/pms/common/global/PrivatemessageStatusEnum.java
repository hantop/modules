package com.fenlibao.model.pms.common.global;

/**
 * 站内信状态
 * Created by chenzhixuan on 2015/12/15.
 */
public enum PrivatemessageStatusEnum {
    WD("未读"),
    YD("已读"),
    SC("删除");

    protected final String chineseName;

    private PrivatemessageStatusEnum(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return this.chineseName;
    }
}
