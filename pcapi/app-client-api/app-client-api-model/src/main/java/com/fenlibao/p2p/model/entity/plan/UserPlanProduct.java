package com.fenlibao.p2p.model.entity.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户投资计划产品
 *
 * @author Mingway.Xu
 * @date 2017/3/23 16:59
 */
public class UserPlanProduct {
    private Integer userPlanProductId;//用户投资计划产品id

    private Integer userPlanId;//用户投资计划id flb.t_user_plan.id

    private Integer productType;//1 标 ； 2 债权

    private Integer productId;//产品id s62.t6251.F01 标债权记录

    private Integer tenderId;//投标记录id s62.t6250.F01

    private Date endTimestamp; //到期时间戳

    private BigDecimal investAmount;//投资金额

    private String yq;//逾期

    private String bidStatus;//标的状态

    private BigDecimal zqValue;//债权待收金额 by ：kris

    public BigDecimal getZqValue() {
        return zqValue;
    }
    public void setZqValue(BigDecimal zqValue) {
        this.zqValue = zqValue;
    }
    public Integer getUserPlanProductId() {
        return userPlanProductId;
    }

    public void setUserPlanProductId(Integer userPlanProductId) {
        this.userPlanProductId = userPlanProductId;
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

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public String getYq() {
        return yq;
    }

    public void setYq(String yq) {
        this.yq = yq;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }
}
