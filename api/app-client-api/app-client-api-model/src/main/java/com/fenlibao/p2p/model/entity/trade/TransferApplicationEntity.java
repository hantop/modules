package com.fenlibao.p2p.model.entity.trade;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/8/18.
 */
public class TransferApplicationEntity {

    private int id;//主键

    private int userId;//用户ID

    private String platformUserNo;//用户编号

    private BigDecimal amount;//金额

    private String status;//状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
