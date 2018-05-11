package com.fenlibao.model.pms.idmt.group;

/**
 * Created by Louis Wang on 2016/1/27.
 */

public class PmsGroupForm {

    private Integer nodeId;

    private Integer deptParent;

    private String deptName;

    private Integer deptSort;

    private String deptParents;

    private String deptMark;

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getDeptParent() {
        return deptParent;
    }

    public void setDeptParent(Integer deptParent) {
        this.deptParent = deptParent;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getDeptSort() {
        return deptSort;
    }

    public void setDeptSort(Integer deptSort) {
        this.deptSort = deptSort;
    }

    public String getDeptParents() {
        return deptParents;
    }

    public void setDeptParents(String deptParents) {
        this.deptParents = deptParents;
    }

    public String getDeptMark() {
        return deptMark;
    }

    public void setDeptMark(String deptMark) {
        this.deptMark = deptMark;
    }
}
