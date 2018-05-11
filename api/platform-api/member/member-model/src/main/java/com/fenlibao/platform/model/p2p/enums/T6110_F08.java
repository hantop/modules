package com.fenlibao.platform.model.p2p.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Lullaby on 2016/2/16.
 */
public enum T6110_F08 {

    ZC("注册"),
    HTTJ("后台添加");

    protected final String chineseName;

    private T6110_F08(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return this.chineseName;
    }

    public static final T6110_F08 parse(String value) {
        if(StringUtils.isBlank(value)) {
            return null;
        } else {
            try {
                return valueOf(value);
            } catch (Throwable var2) {
                return null;
            }
        }
    }

}
