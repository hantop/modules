package com.fenlibao.model.pms.common.producttype;

/**
 * 产品类型
 *
 * Created by Administrator on 2016/10/27.
 */
public class ProductType {
    private int id;
    private String name;// 类型名称
    private String code;// 类型编码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
