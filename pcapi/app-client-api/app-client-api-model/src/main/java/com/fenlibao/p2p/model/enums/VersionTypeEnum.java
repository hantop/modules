package com.fenlibao.p2p.model.enums;

/**
 * 区分普通版本和存管版本的枚举
 * Created by xiao on 2017/5/23.
 */
public enum VersionTypeEnum {
    PT("PT", "普通版本", 1),
    CG("CG", "存管版本", 2),
    CGJK("CGJK", "存管借款版本", 3);

    private String code;
    private String name;
    private int index;

    VersionTypeEnum(String code, String name, int index) {
        this.code = code;
        this.name = name;
        this.index = index;
    }

    public static int length() {
        return VersionTypeEnum.values().length;
    }

    public static VersionTypeEnum parse(int index) {    //手写的从int到enum的转换函数
        for (VersionTypeEnum item : VersionTypeEnum.values()) {
            if (item.getIndex() == index) {
                return item;
            }
        }
        return null;
    }

    public static VersionTypeEnum parse(String code) {
        for (VersionTypeEnum item : VersionTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
