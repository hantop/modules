package com.fenlibao.model.pms.da.platformstatistics;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 平台每日统计数据
 * Created by chenzhixuan on 2015/12/1.
 */
public class Platformstatistics {
    private int registerNum;// 注册人数
    private int tenderNum;// 投标人数
    private int transferinNum;// 债权转入人数
    private BigDecimal rechargeMoney;// 充值金额
    private BigDecimal tenderMoney;// 投标金额
    private BigDecimal transferinMoney;// 债权转入金额
    private BigDecimal withdrawMoney;// 提现金额
    private BigDecimal earningsMoney;// 回款金额
    private BigDecimal loanMoney;// 放款金额
    private BigDecimal receivableMoney;// 待回款金额
    private BigDecimal creditassigmentFee;// 债权转让服务费
    private BigDecimal bidFee;// 标的成交服务费
    private Timestamp statisticsDate;// 统计日期
    private BigDecimal turnoverFee;// 平台成交服务费
    private int firstInvestNum;// 首投人数

    public int getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(int registerNum) {
        this.registerNum = registerNum;
    }

    public int getTenderNum() {
        return tenderNum;
    }

    public void setTenderNum(int tenderNum) {
        this.tenderNum = tenderNum;
    }

    public int getTransferinNum() {
        return transferinNum;
    }

    public void setTransferinNum(int transferinNum) {
        this.transferinNum = transferinNum;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(BigDecimal rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public BigDecimal getTenderMoney() {
        return tenderMoney;
    }

    public void setTenderMoney(BigDecimal tenderMoney) {
        this.tenderMoney = tenderMoney;
    }

    public BigDecimal getTransferinMoney() {
        return transferinMoney;
    }

    public void setTransferinMoney(BigDecimal transferinMoney) {
        this.transferinMoney = transferinMoney;
    }

    public BigDecimal getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public BigDecimal getEarningsMoney() {
        return earningsMoney;
    }

    public void setEarningsMoney(BigDecimal earningsMoney) {
        this.earningsMoney = earningsMoney;
    }

    public BigDecimal getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }

    public BigDecimal getReceivableMoney() {
        return receivableMoney;
    }

    public void setReceivableMoney(BigDecimal receivableMoney) {
        this.receivableMoney = receivableMoney;
    }

    public BigDecimal getCreditassigmentFee() {
        return creditassigmentFee;
    }

    public void setCreditassigmentFee(BigDecimal creditassigmentFee) {
        this.creditassigmentFee = creditassigmentFee;
    }

    public BigDecimal getBidFee() {
        return bidFee;
    }

    public void setBidFee(BigDecimal bidFee) {
        this.bidFee = bidFee;
    }

    public Timestamp getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Timestamp statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public BigDecimal getTurnoverFee() {
        return turnoverFee;
    }

    public void setTurnoverFee(BigDecimal turnoverFee) {
        this.turnoverFee = turnoverFee;
    }

    public int getFirstInvestNum() {
        return firstInvestNum;
    }

    public void setFirstInvestNum(int firstInvestNum) {
        this.firstInvestNum = firstInvestNum;
    }
}
