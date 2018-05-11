package com.fenlibao.model.pms.da.consts;

/**
 * 区分普通版本和存管版本的枚举
 * @author zeronx on 2018/1/15 17:57.
 * @version 1.0
 */
public enum VersionTypeEnum {
    PT("PT", "普通版本", 1),
    CG("CG", "存管版本", 2);

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
