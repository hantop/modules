package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;

/**
 * @author Mingway.Xu
 * @date 2017/3/22 13:46
 */
public class DirectionalPlan {
    private int productId;//id

    private int productType;//产品类型 ：1 计划 ；  2 标

    private BigDecimal totalUserAssets; //用户资产总额

    private BigDecimal userInvestingAmount; //用户在投金额

    private int targetUser;//是否指定用户可投的标

    private BigDecimal userAccumulatedIncome;//用户累计收益

    private int anytimeQuit;//随时退出计划信息 1：是 ； 0：否

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
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

    public int getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(int targetUser) {
        this.targetUser = targetUser;
    }

    public BigDecimal getUserAccumulatedIncome() {
        return userAccumulatedIncome;
    }

    public void setUserAccumulatedIncome(BigDecimal userAccumulatedIncome) {
        this.userAccumulatedIncome = userAccumulatedIncome;
    }

    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }
}
