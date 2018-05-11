package com.fenlibao.model.pms.idmt.permit;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class PermitTreeNode {
    private Integer id;
    @JSONField(name="pId")
    private Integer pid;
    private List<PermitTreeNode> children;
    private String name;
    private Integer sort;
    private String type;
    private String description;
    private String code;
    private String isParent;
    private boolean checked;// 是否选中
    private String permitUrl;

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public List<PermitTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<PermitTreeNode> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

	public String getPermitUrl() {
		return permitUrl;
	}

	public void setPermitUrl(String permitUrl) {
		this.permitUrl = permitUrl;
	}
}
