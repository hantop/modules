package com.fenlibao.p2p.model.xinwang.entity.trade;

/**
 * flb.t_xw_confirm_tender
 */
public class SysProjectConfirmTenderInfo {
    private Integer id;
    private Integer orderId;
    private Integer projectId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
