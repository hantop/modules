package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;

/**
 * Created by xiao on 2017/2/7.
 */
public class BidInfoForPlan {
    int bidId;//标ID

    String bidTitle;//借款标题

    int bidType;//标的类型

    int loanMonths;//借款月数

    int loanDays; //借款天数

    double yearYield;//年化利率

    String repaymentMode; //还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清)

    String bidStatus; //标的状态

    String assetTypes;//资产类型

    String isNoviceBid;//是否是新手标 (S:是新手标;F:普通标)

    BigDecimal surplusAmount; //可投金额

    public BidInfoForPlan() {
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getBidTitle() {
        return bidTitle;
    }

    public void setBidTitle(String bidTitle) {
        this.bidTitle = bidTitle;
    }

    public int getBidType() {
        return bidType;
    }

    public void setBidType(int bidType) {
        this.bidType = bidType;
    }

    public int getLoanMonths() {
        return loanMonths;
    }

    public void setLoanMonths(int loanMonths) {
        this.loanMonths = loanMonths;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getAssetTypes() {
        return assetTypes;
    }

    public void setAssetTypes(String assetTypes) {
        this.assetTypes = assetTypes;
    }

    public String getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(String isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }
}
