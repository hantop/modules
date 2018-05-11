package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by laubrence on 2016/3/29.
 */
public class CreditInfo {

    int creditId; /*债权id*/

    String creditCode; /**债权编码*/

    int bidId; /*标Id*/

    int userId;/*投资人用户id*/

    BigDecimal purchaseAmount;/*投资金额*/

    BigDecimal originalAmount;/*原始债权金额*/

    BigDecimal holdAmount;/*持有债权金额*/

    Date investDate; /*债权创建日期*/

    Date interestDate; /*计息日期*/

    String isTransfer;/*是否正在转让*/

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public Date getInvestDate() {
        return investDate;
    }

    public void setInvestDate(Date investDate) {
        this.investDate = investDate;
    }

    public String getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(String isTransfer) {
        this.isTransfer = isTransfer;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public Date getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(Date interestDate) {
        this.interestDate = interestDate;
    }
}
