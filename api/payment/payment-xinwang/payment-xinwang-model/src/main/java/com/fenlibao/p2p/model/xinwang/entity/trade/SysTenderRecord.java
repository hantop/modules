package com.fenlibao.p2p.model.xinwang.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 投标记录 T6250
 *
 * @date 2017/6/14 18:02
 */
public class SysTenderRecord implements Serializable{
    /**
     * 自增ID
     */
    public int id;

    /**
     * 标ID,参考T6230.F01
     */
    public int bidId;

    /**
     * 投资人ID,参考T6110.F01
     */
    public int userId;

    /**
     * 购买价格
     */
    public BigDecimal tenderAmount;

    /**
     * 债权金额
     */
    public BigDecimal creditAmount;

    /**
     * 投标时间
     */
    public Timestamp investTime;

    /**
     * 是否取消,F:否;S:是;
     */
    public String cancelFlag;

    /**
     * 是否已放款,F:否;S:是;
     */
    public String loanFlag;

    /**
     * 是否自动投标
     */
    public String autoFlag;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTenderAmount() {
        return tenderAmount;
    }

    public void setTenderAmount(BigDecimal tenderAmount) {
        this.tenderAmount = tenderAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Timestamp getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Timestamp investTime) {
        this.investTime = investTime;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getLoanFlag() {
        return loanFlag;
    }

    public void setLoanFlag(String loanFlag) {
        this.loanFlag = loanFlag;
    }

    public String getAutoFlag() {
        return autoFlag;
    }

    public void setAutoFlag(String autoFlag) {
        this.autoFlag = autoFlag;
    }

}
