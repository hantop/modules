package com.fenlibao.p2p.model.vo.plan;

import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/23 9:36.
 * @version 1.0
 */
public class PlanProductVO {

    private Integer userId; // 用户id
    private Integer userPlanId; // 用户投资计划记录Id
    private Integer productType; // '产品类型(1:标的 2:债权)',
    private Integer productId; //  债权id t6251.F01
    private BigDecimal amount; // 投资金额
    private Integer tenderId; // 投资记录Id s62.t6250.F01

    public PlanProductVO() {
    }

    public PlanProductVO(Integer userId, Integer userPlanId, Integer productType, Integer productId, BigDecimal amount, Integer tenderId) {
        this.userId = userId;
        this.userPlanId = userPlanId;
        this.productType = productType;
        this.productId = productId;
        this.amount = amount;
        this.tenderId = tenderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(Integer userPlanId) {
        this.userPlanId = userPlanId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }
}
