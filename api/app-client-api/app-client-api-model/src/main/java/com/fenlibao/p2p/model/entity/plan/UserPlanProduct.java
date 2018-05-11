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

    private BigDecimal returnedAmount;//回款金额

    private Integer userId;//持有用户id

    private Integer applyforId;//债权申请id

    private Integer bidBorrower;//标借款人

    public Integer getBidBorrower() {
        return bidBorrower;
    }

    public void setBidBorrower(Integer bidBorrower) {
        this.bidBorrower = bidBorrower;
    }

    public Integer getApplyforId() {
        return applyforId;
    }

    public void setApplyforId(Integer applyforId) {
        this.applyforId = applyforId;
    }

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

    public BigDecimal getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(BigDecimal returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
