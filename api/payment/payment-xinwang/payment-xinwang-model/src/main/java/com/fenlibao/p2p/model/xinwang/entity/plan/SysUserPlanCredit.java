package com.fenlibao.p2p.model.xinwang.entity.plan;

import java.math.BigDecimal;

/**
 * flb.t_user_plan_product
 */
public class SysUserPlanCredit {
    private Integer id;
    private Integer userPlanId;
    /**
     * product_id
     */
    private Integer creditId;
    private Integer tenderId;
    private BigDecimal amount;
    private BigDecimal returnAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(Integer userPlanId) {
        this.userPlanId = userPlanId;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }
}
