package com.fenlibao.p2p.model.vo.plan;

/**
 * @author zeronx on 2017/11/23 11:41.
 * @version 1.0
 */
public class SysOrderVO {

    private Integer orderId; // 订单Id
    private String status; // 订单状态
    private String level; // 订单级别 ,XT:系统;YH:用户;HT:后台;
    private Integer userId; // 用户ID
    private Integer adminId; // 后台用户ID

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}
