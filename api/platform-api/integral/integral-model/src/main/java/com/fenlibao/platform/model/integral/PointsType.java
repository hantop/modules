package com.fenlibao.platform.model.integral;

/**
 * 积分类型
 * Created by Lullaby on 2016/2/19.
 */
public class PointsType {

    private int id;

    private int parentId;

    private String typeName;

    private String typeCode;

    private String typeLogo;

    private byte changeType;

    private byte isRoot;

    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeLogo() {
        return typeLogo;
    }

    public void setTypeLogo(String typeLogo) {
        this.typeLogo = typeLogo;
    }

    public byte getChangeType() {
        return changeType;
    }

    public void setChangeType(byte changeType) {
        this.changeType = changeType;
    }

    public byte getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(byte isRoot) {
        this.isRoot = isRoot;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
