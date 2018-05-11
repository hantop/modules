package com.fenlibao.p2p.model.xinwang.enums.common;

import java.io.Serializable;

/**
 * 新网通用返回码
 * @date 2017/5/25 17:11
 */
public enum GeneralResponseCode implements Serializable{
    SUCCESS(0),
    FAIL(1);

    protected final Integer code;

    GeneralResponseCode(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
