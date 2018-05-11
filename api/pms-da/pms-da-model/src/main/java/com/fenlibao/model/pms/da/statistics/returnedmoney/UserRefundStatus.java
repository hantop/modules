package com.fenlibao.model.pms.da.statistics.returnedmoney;

/**
 * 还款状态
 *
 * Created by chenzhixuan on 2016/3/24.
 */
public class UserRefundStatus {
    private Integer userId;
    private String refundStatus;// 还款状态
    private Integer refundId;// 还款记录ID

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }
}
