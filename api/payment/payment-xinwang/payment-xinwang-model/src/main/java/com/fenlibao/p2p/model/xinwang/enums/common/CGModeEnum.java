package com.fenlibao.p2p.model.xinwang.enums.common;

/**
 * 区分存管和普通版
 *
 * @date 2017/6/24 15:15
 */
public enum CGModeEnum {
    PT("PT", 1),
    CG("CG", 2),;

    protected String name;
    protected int index;

    CGModeEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static CGModeEnum parse(int index) throws Exception {
        CGModeEnum result = null;
        for (CGModeEnum cgModeEnum : CGModeEnum.values()) {
            if (cgModeEnum.getIndex() == index) {
                result = cgModeEnum;
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
