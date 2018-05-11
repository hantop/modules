package com.fenlibao.model.pms.da.platformstatistics;

import java.math.BigDecimal;

/**
 * 平台累计数据
 * Created by chenzhixuan on 2015/12/1.
 */
public class PlatformstatisticsTotal {
    private int registerNum;// 注册人数
    private int investNum;// 投资人数(投资即计算。流标，债权转出不减去)
    private BigDecimal receivableMoney;// 待回款金额
    private BigDecimal turnoverFee;// 平台成交服务费
    private BigDecimal investMoeny;// 投资金额(投资即计算。流标，债权转出不减去)
    private BigDecimal profitMoneyForInvestor;// 为投资者赚取收益包含:普通标的购买的到期付息及逾期罚息、体验金的收益、激活的返现红包、所发送的现金奖励、债权转入吃的差价（标的1000，转出价950，这50元要算）提前还款的违约金

    public int getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(int registerNum) {
        this.registerNum = registerNum;
    }

    public int getInvestNum() {
        return investNum;
    }

    public void setInvestNum(int investNum) {
        this.investNum = investNum;
    }

    public BigDecimal getReceivableMoney() {
        return receivableMoney;
    }

    public void setReceivableMoney(BigDecimal receivableMoney) {
        this.receivableMoney = receivableMoney;
    }

    public BigDecimal getTurnoverFee() {
        return turnoverFee;
    }

    public void setTurnoverFee(BigDecimal turnoverFee) {
        this.turnoverFee = turnoverFee;
    }

    public BigDecimal getInvestMoeny() {
        return investMoeny;
    }

    public void setInvestMoeny(BigDecimal investMoeny) {
        this.investMoeny = investMoeny;
    }

    public BigDecimal getProfitMoneyForInvestor() {
        return profitMoneyForInvestor;
    }

    public void setProfitMoneyForInvestor(BigDecimal profitMoneyForInvestor) {
        this.profitMoneyForInvestor = profitMoneyForInvestor;
    }
}