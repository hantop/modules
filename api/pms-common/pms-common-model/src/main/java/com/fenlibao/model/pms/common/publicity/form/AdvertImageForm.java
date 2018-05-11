package com.fenlibao.model.pms.common.publicity.form;

/**
 * 广告图
 *
 * Created by chenzhixuan on 2016/5/11.
 */
public class AdvertImageForm {
    private String name;
    private String advertType;
    private Integer systemType;// 系统类型

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
        this.advertType = advertType;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }
}
