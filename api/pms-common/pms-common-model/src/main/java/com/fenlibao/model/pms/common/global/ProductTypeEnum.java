package com.fenlibao.model.pms.common.global;

/**
 * 产品类型
 *
 * Created by Administrator on 2016/10/27.
 */
public enum ProductTypeEnum {
    // 消费信贷
    CONSUMER_LOAN("XFXD");
    private String code;

    ProductTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
