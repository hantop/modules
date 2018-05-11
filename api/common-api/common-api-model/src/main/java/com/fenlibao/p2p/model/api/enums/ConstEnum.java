package com.fenlibao.p2p.model.api.enums;

/**
 * 文件类型
 */
public enum  ConstEnum {
    PIC(6),//图片
    PDF(12);//pdf

    protected int code;

    ConstEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
