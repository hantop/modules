package com.fenlibao.model.pms.da.cs.account;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 充值流水
 * Created by Bogle on 2015/12/22.
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 3603501231952744242L;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Timestamp tradeTime; // 对应表：s61.t6130.F08字段

    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Timestamp sealedTime;//封标时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Timestamp finishTime;//完成时间

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;//对应表：s65.t6502.F03字段

    private BigDecimal handleAmount;//手续费

    /**
     * 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;MJL:在连连没记录;WAITING等待支付(连连);REFUND退款(连连)
     */
    private String status;//对应表：s65.t6501.F03字段 改为 s61.t6130.F08

    private double rate;// 年化率
    private String rateStr;// 年化率

    private String noviceBid;//是否是新手标

    private int loanDays;//借款周期,单位:天

    private int month;//周期单位:月

    private String name;//产品名称

    private String tradeTypeCode;

    /**
     * 交易类型
     */
    private String tradeTypeName;//s51.T5122.F02

    public String getRateStr() {
		return rateStr;
	}

	public void setRateStr(String rateStr) {
		this.rateStr = rateStr;
	}

	private String bankName;// 银行名称

    private String bankShortNum;//银行卡号
    
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Timestamp fullTime; //到期日期: 标的的预计到期日期。
    
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Timestamp backTime; //回款时间: 借款人将钱打到公司对公账户上,财务将钱打到分利宝账户的时间。
    
    private BigDecimal originMoney;//债权本金：用户购买债权的本金价值

    private String interestSum;//加息利息：通过该债权预计到期日的加息利息收入

    private String origenRate;//债权利息：通过该债权预计到期日的利息收入
    
    private String acturalEarn;//实际收益：利息收益+购买债权金额与债权本金的差价+逾期罚息+提前还款违约金
    
    private String bidType;//标的类型：用户购买的标的类型

    private String paymentType;//还款方式

    //======================
    private BigDecimal saleMoney;//卖出金额
   
	private BigDecimal origrienMoneyTotal;//债权本金总计
    
    private BigDecimal investMoneyTotal;//投资金额总计

    private BigDecimal scope;// 优惠幅度

    private BigDecimal redPacketMoney;// 红包金额

    private BigDecimal balance;// 交易流水结余6102.F08

    private String remarks;// 交易流水备注6102.F09

    private String bidInterestSum;//标加息利息：通过该债权预计到期日的标加息利息收入

    private BigDecimal bidScope;//标加息

    public BigDecimal getSaleMoney() {
		return saleMoney;
	}

	public void setSaleMoney(BigDecimal saleMoney) {
		this.saleMoney = saleMoney;
	}
    
	public BigDecimal getOrigrienMoneyTotal() {
		return origrienMoneyTotal;
	}

	public void setOrigrienMoneyTotal(BigDecimal origrienMoneyTotal) {
		this.origrienMoneyTotal = origrienMoneyTotal;
	}

	public BigDecimal getInvestMoneyTotal() {
		return investMoneyTotal;
	}

	public void setInvestMoneyTotal(BigDecimal investMoneyTotal) {
		this.investMoneyTotal = investMoneyTotal;
	}

    public Timestamp getFullTime() {
		return fullTime;
	}

	public void setFullTime(Timestamp fullTime) {
		this.fullTime = fullTime;
	}

	public BigDecimal getOriginMoney() {
		return originMoney;
	}

	public void setOriginMoney(BigDecimal originMoney) {
		this.originMoney = originMoney;
	}

    public String getInterestSum() {
        return interestSum;
    }

    public void setInterestSum(String interestSum) {
        this.interestSum = interestSum;
    }

    public String getOrigenRate() {
        return origenRate;
    }

    public void setOrigenRate(String origenRate) {
        this.origenRate = origenRate;
    }

    public String getActuralEarn() {
        return acturalEarn;
    }

    public void setActuralEarn(String acturalEarn) {
        this.acturalEarn = acturalEarn;
    }

    public String getBidType() {
		return bidType;
	}

	public void setBidType(String bidType) {
		this.bidType = bidType;
	}

	public Timestamp getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Timestamp tradeTime) {
        this.tradeTime = tradeTime;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getHandleAmount() {
        return handleAmount;
    }

    public void setHandleAmount(BigDecimal handleAmount) {
        this.handleAmount = handleAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankShortNum() {
        return bankShortNum;
    }

    public void setBankShortNum(String bankShortNum) {
        this.bankShortNum = bankShortNum;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoviceBid() {
        return noviceBid;
    }

    public void setNoviceBid(String noviceBid) {
        this.noviceBid = noviceBid;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public Timestamp getSealedTime() {
        return sealedTime;
    }

    public void setSealedTime(Timestamp sealedTime) {
        this.sealedTime = sealedTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

	public Timestamp getBackTime() {
		return backTime;
	}

	public void setBackTime(Timestamp backTime) {
		this.backTime = backTime;
	}

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getScope() {
        return scope;
    }

    public void setScope(BigDecimal scope) {
        this.scope = scope;
    }

    public BigDecimal getRedPacketMoney() {
        return redPacketMoney;
    }

    public void setRedPacketMoney(BigDecimal redPacketMoney) {
        this.redPacketMoney = redPacketMoney;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBidInterestSum() {
        return bidInterestSum;
    }

    public void setBidInterestSum(String bidInterestSum) {
        this.bidInterestSum = bidInterestSum;
    }

    public BigDecimal getBidScope() {
        return bidScope;
    }

    public void setBidScope(BigDecimal bidScope) {
        this.bidScope = bidScope;
    }

    public String getTradeTypeCode() {
        return tradeTypeCode;
    }

    public void setTradeTypeCode(String tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }
}
