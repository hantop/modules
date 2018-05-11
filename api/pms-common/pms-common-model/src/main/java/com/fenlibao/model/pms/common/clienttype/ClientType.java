package com.fenlibao.model.pms.common.clienttype;

/**
 * 客户端类型
 *
 * Created by chenzhixuan on 2016/5/17.
 */
public class ClientType {
    private Integer id;
    private String code;// 编码
    private String name;// 名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
