package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chen on 2017/11/20.
 */
public class ProjectInfo {

    private String name;//借款标题

    private BigDecimal loanAmount;//借款金额

    private String loanTerm;//借款周期

    private double rate;//年化利率

    private String loanPurpose;//借款用途

    private String assetsType;//认证类型

    private String repaymentOrigin;//还款来源

    private String repaymentMode;//还款方式

    private String interestType;//计息方式

    private String interestDate;//计息时间

    private String isEarlyExit;//提前退出

    private String guarantee;//担保措施

    private String fwUrl;//服务协议


    public String getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    public String getFwUrl() {
        return fwUrl;
    }

    public void setFwUrl(String fwUrl) {
        this.fwUrl = fwUrl;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }


    public String getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(String interestDate) {
        this.interestDate = interestDate;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getIsEarlyExit() {
        return isEarlyExit;
    }

    public void setIsEarlyExit(String isEarlyExit) {
        this.isEarlyExit = isEarlyExit;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getRepaymentOrigin() {
        return repaymentOrigin;
    }

    public void setRepaymentOrigin(String repaymentOrigin) {
        this.repaymentOrigin = repaymentOrigin;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }
}
