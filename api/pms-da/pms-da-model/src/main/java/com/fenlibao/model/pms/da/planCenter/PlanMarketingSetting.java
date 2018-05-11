package com.fenlibao.model.pms.da.planCenter;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/21.
 */
public class PlanMarketingSetting extends InvestPlan{
    private int productType;//产品类型(1:计划 2:标的)
    private int productId;//产品ID

    private int monCycleShow;//月升计划期限
    private int preferenceCycleShow;//省心计划期限

    private BigDecimal totalUserAssets;//用户资产总额
    private BigDecimal userInvestingAmount;//用户在投金额
    private BigDecimal userAccumulatedIncome;//用户累计收益
    private boolean targetUser;//指定用户(1:指定 0:不指定)

    private String customLabel1;//标签
    private String customLabel2;
    private String label;
    private String comment;//条件说明

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getMonCycleShow() {
        return monCycleShow;
    }

    public void setMonCycleShow(int monCycleShow) {
        this.monCycleShow = monCycleShow;
    }

    public int getPreferenceCycleShow() {
        return preferenceCycleShow;
    }

    public void setPreferenceCycleShow(int preferenceCycleShow) {
        this.preferenceCycleShow = preferenceCycleShow;
    }

    public BigDecimal getTotalUserAssets() {
        return totalUserAssets;
    }

    public void setTotalUserAssets(BigDecimal totalUserAssets) {
        this.totalUserAssets = totalUserAssets;
    }

    public BigDecimal getUserInvestingAmount() {
        return userInvestingAmount;
    }

    public void setUserInvestingAmount(BigDecimal userInvestingAmount) {
        this.userInvestingAmount = userInvestingAmount;
    }

    public BigDecimal getUserAccumulatedIncome() {
        return userAccumulatedIncome;
    }

    public void setUserAccumulatedIncome(BigDecimal userAccumulatedIncome) {
        this.userAccumulatedIncome = userAccumulatedIncome;
    }

    public boolean isTargetUser() {
        return targetUser;
    }

    public void setTargetUser(boolean targetUser) {
        this.targetUser = targetUser;
    }

    public String getCustomLabel1() {
        return customLabel1;
    }

    public void setCustomLabel1(String customLabel1) {
        this.customLabel1 = customLabel1;
    }

    public String getCustomLabel2() {
        return customLabel2;
    }

    public void setCustomLabel2(String customLabel2) {
        this.customLabel2 = customLabel2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
