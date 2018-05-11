package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by xiao on 2017/3/22.
 */
public class UserCreditInfoForPlan {

    private int applyforId;
    private int recordId;
    private String creditCode;
    private BigDecimal yearYield;
    private BigDecimal transferOutPrice;
    private Timestamp transferApplyforTime;
    private String repaymentMode;
    private BigDecimal discountRate;

    public UserCreditInfoForPlan(BigDecimal discountRate, int applyforId, int recordId, String creditCode, BigDecimal yearYield, BigDecimal transferOutPrice, Timestamp transferApplyforTime, String repaymentMode) {

        this.discountRate = discountRate;
        this.applyforId = applyforId;
        this.recordId = recordId;
        this.creditCode = creditCode;
        this.yearYield = yearYield;
        this.transferOutPrice = transferOutPrice;
        this.transferApplyforTime = transferApplyforTime;
        this.repaymentMode = repaymentMode;
    }

    public UserCreditInfoForPlan() {

    }

    public int getApplyforId() {
        return applyforId;
    }

    public void setApplyforId(int applyforId) {
        this.applyforId = applyforId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public BigDecimal getYearYield() {
        return yearYield;
    }

    public void setYearYield(BigDecimal yearYield) {
        this.yearYield = yearYield;
    }

    public BigDecimal getTransferOutPrice() {
        return transferOutPrice;
    }

    public void setTransferOutPrice(BigDecimal transferOutPrice) {
        this.transferOutPrice = transferOutPrice;
    }

    public Timestamp getTransferApplyforTime() {
        return transferApplyforTime;
    }

    public void setTransferApplyforTime(Timestamp transferApplyforTime) {
        this.transferApplyforTime = transferApplyforTime;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
}
