package com.fenlibao.p2p.model.entity.creditassignment;

import com.fenlibao.p2p.model.entity.bid.BidExtendInfo;

import java.util.Date;

/**
 * Created by laubrence on 2016/3/30.
 */
public class TransferOutInfo extends BidExtendInfo {

    int applyforId; //转让申请id

    double transferOutPrice;//转让价格

    String creditCode;//债权编号

    double originalCreditAmount;//原始债权金额

    double collectInterest;//待收本息

    int surplusDays;//债权剩余天数

    Date transferApplyforTime; //转让申请时间

    double discountRate;//转让折扣率

    String repaymentMode;//還款方式

    @Override
    public String getRepaymentMode() {
        return repaymentMode;
    }

    @Override
    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public int getApplyforId() {
        return applyforId;
    }

    public void setApplyforId(int applyforId) {
        this.applyforId = applyforId;
    }

    public double getCollectInterest() {
        return collectInterest;
    }

    public void setCollectInterest(double collectInterest) {
        this.collectInterest = collectInterest;
    }

    public double getOriginalCreditAmount() {
        return originalCreditAmount;
    }

    public void setOriginalCreditAmount(double originalCreditAmount) {
        this.originalCreditAmount = originalCreditAmount;
    }

    public int getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(int surplusDays) {
        this.surplusDays = surplusDays;
    }

    public Date getTransferApplyforTime() {
        return transferApplyforTime;
    }

    public void setTransferApplyforTime(Date transferApplyforTime) {
        this.transferApplyforTime = transferApplyforTime;
    }

    public double getTransferOutPrice() {
        return transferOutPrice;
    }

    public void setTransferOutPrice(double transferOutPrice) {
        this.transferOutPrice = transferOutPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}