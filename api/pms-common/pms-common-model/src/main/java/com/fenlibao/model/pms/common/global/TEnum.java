package com.fenlibao.model.pms.common.global;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lullaby on 2015/7/30.
 */
public class TEnum implements Serializable {

    private int id;

    private String enumTable;

    private String enumColumn;

    private String enumKey;

    private String enumValue;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
