package com.fenlibao.p2p.model.xinwang.entity.pay;

import java.math.BigDecimal;
import java.util.Date;

/**
 * t_xw_withdraw
 */
public class XWWithdrawRequest {
    private Integer id;
    private String requestNo;
    private String platformUserNo;
    private BigDecimal amount;
    private BigDecimal commission;
    private Date expiredTime;
    private String backrollRechargeRequestNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
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

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getBackrollRechargeRequestNo() {
        return backrollRechargeRequestNo;
    }

    public void setBackrollRechargeRequestNo(String backrollRechargeRequestNo) {
        this.backrollRechargeRequestNo = backrollRechargeRequestNo;
    }
}
