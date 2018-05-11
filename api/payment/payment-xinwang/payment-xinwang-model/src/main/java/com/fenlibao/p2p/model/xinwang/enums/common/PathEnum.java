package com.fenlibao.p2p.model.xinwang.enums.common;

/**
 * 常用的路径
 */
public enum PathEnum {
    TEMP_PATH("/var/tmp/");

    private final String path;

    PathEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
