package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lullaby on 2015/7/9.
 */
public class TEnum implements Serializable {

    private Integer id;// 主键

    private String enumTable;// 枚举表明

    private String enumColumn;// 枚举字段明

    private String enumKey;// 枚举值

    private String enumValue;// 枚举描述值

    private Date createTime;// 创建时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnumTable() {
        return enumTable;
    }

    public void setEnumTable(String enumTable) {
        this.enumTable = enumTable;
    }

    public String getEnumColumn() {
        return enumColumn;
    }

    public void setEnumColumn(String enumColumn) {
        this.enumColumn = enumColumn;
    }

    public String getEnumKey() {
        return enumKey;
    }

    public void setEnumKey(String enumKey) {
        this.enumKey = enumKey;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
