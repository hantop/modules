package com.fenlibao.model.pms.da.cs.guarantor;

/**
 * Created by zeronx on 2017/6/28.
 */
public class BankCodeInfo {
    private Integer id; // t5020 F01 主键
    private String name; // 银行名称
    private String code; // 银行编码

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
