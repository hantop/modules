package com.fenlibao.p2p.model.xinwang.entity.trade;

import com.fenlibao.p2p.model.xinwang.enums.trade.CreditToRepay_RepayState;

import java.math.BigDecimal;

/**
 * 封装要还款的债权的信息 flb.t_xw_credit_repay
 */
public class SysCreditToRepay {

    private Integer id;

    /**
     * 平台整标还款订单id
     */
    private Integer orderId;

    /**
     * 债权id
     */
    private Integer creditId;

    /**
     * 存管还款请求编号
     */
    private String repayRequestNo;
    /**
     * 存管营销（加息等）请求编号
     */
    private String marketingRequestNo;

    /**
     * 关联的计划id
     */
    private Integer planId;
    /**
     * 投资人平台用户id
     */
    private Integer investorId;
    /**
     * 投资人存管账号
     */
    private String investorPlatformUserNo;

    /**
     * 本金
     */
    private BigDecimal principal=BigDecimal.ZERO;
    /**
     * 利息
     */
    private BigDecimal interest=BigDecimal.ZERO;

    /**
     * 利息管理费
     */
    private BigDecimal interestServiceFee = BigDecimal.ZERO;

    /**
     * 成交服务费（实际上是平台服务费，下面的平台服务费不知道是什么鬼。。）
     */
    private BigDecimal dealFee = BigDecimal.ZERO;

    /**
     * 逾期罚息
     */
    private BigDecimal overduePenalty=BigDecimal.ZERO;
    /**
     * 提前还款罚息
     */
    private BigDecimal prepayPenalty=BigDecimal.ZERO;

    /**
     * 提前还款罚息平台分成
     */
    private BigDecimal penaltyDivide = BigDecimal.ZERO;

    /**
     * 逾期手续费
     */
    private BigDecimal overdueCommission=BigDecimal.ZERO;
    /**
     * 加息券加息
     */
    private BigDecimal tenderIncreaseInterest=BigDecimal.ZERO;
    /**
     * 标加息
     */
    private BigDecimal projectIncreaseInterest=BigDecimal.ZERO;
    /**
     * 平台服务费
     */
    private BigDecimal serviceCharge=BigDecimal.ZERO;

    /**
     * 借款人还该债权已完成
     */
    private CreditToRepay_RepayState repayState;

    /**
     * 平台还该债权加息已完成
     */
    private CreditToRepay_RepayState marketingState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getOverduePenalty() {
        return overduePenalty;
    }

    public void setOverduePenalty(BigDecimal overduePenalty) {
        this.overduePenalty = overduePenalty;
    }

    public BigDecimal getPrepayPenalty() {
        return prepayPenalty;
    }

    public void setPrepayPenalty(BigDecimal prepayPenalty) {
        this.prepayPenalty = prepayPenalty;
    }

    public BigDecimal getOverdueCommission() {
        return overdueCommission;
    }

    public void setOverdueCommission(BigDecimal overdueCommission) {
        this.overdueCommission = overdueCommission;
    }

    public BigDecimal getTenderIncreaseInterest() {
        return tenderIncreaseInterest;
    }

    public void setTenderIncreaseInterest(BigDecimal tenderIncreaseInterest) {
        this.tenderIncreaseInterest = tenderIncreaseInterest;
    }

    public BigDecimal getProjectIncreaseInterest() {
        return projectIncreaseInterest;
    }

    public void setProjectIncreaseInterest(BigDecimal projectIncreaseInterest) {
        this.projectIncreaseInterest = projectIncreaseInterest;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getRepayRequestNo() {
        return repayRequestNo;
    }

    public void setRepayRequestNo(String repayRequestNo) {
        this.repayRequestNo = repayRequestNo;
    }

    public Integer getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Integer investorId) {
        this.investorId = investorId;
    }

    public String getInvestorPlatformUserNo() {
        return investorPlatformUserNo;
    }

    public void setInvestorPlatformUserNo(String investorPlatformUserNo) {
        this.investorPlatformUserNo = investorPlatformUserNo;
    }

    public String getMarketingRequestNo() {
        return marketingRequestNo;
    }

    public void setMarketingRequestNo(String marketingRequestNo) {
        this.marketingRequestNo = marketingRequestNo;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public CreditToRepay_RepayState getRepayState() {
        return repayState;
    }

    public void setRepayState(CreditToRepay_RepayState repayState) {
        this.repayState = repayState;
    }

    public CreditToRepay_RepayState getMarketingState() {
        return marketingState;
    }

    public void setMarketingState(CreditToRepay_RepayState marketingState) {
        this.marketingState = marketingState;
    }

    public BigDecimal getInterestServiceFee() {
        return interestServiceFee;
    }

    public void setInterestServiceFee(BigDecimal interestServiceFee) {
        this.interestServiceFee = interestServiceFee;
    }

    public BigDecimal getPenaltyDivide() {
        return penaltyDivide;
    }

    public void setPenaltyDivide(BigDecimal penaltyDivide) {
        this.penaltyDivide = penaltyDivide;
    }

    public BigDecimal getDealFee() {
        return dealFee;
    }

    public void setDealFee(BigDecimal dealFee) {
        this.dealFee = dealFee;
    }
}
