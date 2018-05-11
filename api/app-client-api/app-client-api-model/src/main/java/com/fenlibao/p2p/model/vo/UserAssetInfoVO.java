package com.fenlibao.p2p.model.vo;

import java.math.BigDecimal;

/**
 * 用户资产信息VO
 *
 * @author chenzhixuan
 *
 */
public class UserAssetInfoVO {
	// 用户余额(联动优势的余额)，往来账户余额
	private BigDecimal balance;
    // 用户总资产(活期宝总额+开店宝总额+薪金宝总额+现金资产总额)
	private BigDecimal totalSum;
    // 活期宝总额
    private BigDecimal hqbSum;
    // 开店宝总额
    private BigDecimal kdbSum;
    // 薪金宝总额
    private BigDecimal xjbSum;
    // 投资总额(活期宝总额+开店宝总额+薪金宝总额)
    private BigDecimal investSum;
    // 昨日总收益(活期宝昨日收益+开店宝昨日收益+薪金宝昨日收益)
    private BigDecimal yesterdayEarningSum;
    // 活期宝昨日收益
    private BigDecimal hqbEarning;
    // 开店宝昨日收益
    private BigDecimal kdbEarning;
    // 薪金宝昨日收益
    private BigDecimal xjbEarning;
    // 累计总收益
    private BigDecimal earningSum;
    // 现金资产总额(用户余额+投资冻结金额+提现冻结金额)
    private BigDecimal cashSum;
    // 投资冻结金额
    private BigDecimal tzdjSum;
    // 提现冻结金额
    private BigDecimal txdjSum;
    //债权转让
    private BigDecimal zqzrEarnings;
    //债权转让投资资产
    private BigDecimal zqzrAssets;


//	// 用户锁定账户余额
//	private BigDecimal freezeBalance;
//	public BigDecimal getFreezeBalance() {
//		return freezeBalance;
//	}
//
//	public void setFreezeBalance(BigDecimal freezeBalance) {
//		this.freezeBalance = freezeBalance;
//	}


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public BigDecimal getHqbSum() {
        return hqbSum;
    }

    public void setHqbSum(BigDecimal hqbSum) {
        this.hqbSum = hqbSum;
    }

    public BigDecimal getKdbSum() {
        return kdbSum;
    }

    public void setKdbSum(BigDecimal kdbSum) {
        this.kdbSum = kdbSum;
    }

    public BigDecimal getXjbSum() {
        return xjbSum;
    }

    public void setXjbSum(BigDecimal xjbSum) {
        this.xjbSum = xjbSum;
    }

    public BigDecimal getInvestSum() {
        return investSum;
    }

    public void setInvestSum(BigDecimal investSum) {
        this.investSum = investSum;
    }

    public BigDecimal getYesterdayEarningSum() {
        return yesterdayEarningSum;
    }

    public void setYesterdayEarningSum(BigDecimal yesterdayEarningSum) {
        this.yesterdayEarningSum = yesterdayEarningSum;
    }

    public BigDecimal getHqbEarning() {
        return hqbEarning;
    }

    public void setHqbEarning(BigDecimal hqbEarning) {
        this.hqbEarning = hqbEarning;
    }

    public BigDecimal getKdbEarning() {
        return kdbEarning;
    }

    public void setKdbEarning(BigDecimal kdbEarning) {
        this.kdbEarning = kdbEarning;
    }

    public BigDecimal getXjbEarning() {
        return xjbEarning;
    }

    public void setXjbEarning(BigDecimal xjbEarning) {
        this.xjbEarning = xjbEarning;
    }

    public BigDecimal getEarningSum() {
        return earningSum;
    }

    public void setEarningSum(BigDecimal earningSum) {
        this.earningSum = earningSum;
    }

    public BigDecimal getCashSum() {
        return cashSum;
    }

    public void setCashSum(BigDecimal cashSum) {
        this.cashSum = cashSum;
    }

    public BigDecimal getTzdjSum() {
        return tzdjSum;
    }

    public void setTzdjSum(BigDecimal tzdjSum) {
        this.tzdjSum = tzdjSum;
    }

    public BigDecimal getTxdjSum() {
        return txdjSum;
    }

    public void setTxdjSum(BigDecimal txdjSum) {
        this.txdjSum = txdjSum;
    }

	public BigDecimal getZqzrEarnings() {
		return zqzrEarnings;
	}

	public void setZqzrEarnings(BigDecimal zqzrEarnings) {
		this.zqzrEarnings = zqzrEarnings;
	}

	public BigDecimal getZqzrAssets() {
		return zqzrAssets;
	}

	public void setZqzrAssets(BigDecimal zqzrAssets) {
		this.zqzrAssets = zqzrAssets;
	}
}
