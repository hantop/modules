package com.fenlibao.p2p.model.vo.fiancing;

/**
 * Created by laubrence on 2016/3/29.
 */
public class RepaymentItemVO {

    String repaymentAmount;//还款金额

    int tradeType;//金额类型(7001:本金;7002:利息;7004:逾期罚息;7005:提前还

    String tradeTypeName;//金额类型名称

    long repaymentDate;//本期还款日期

    String repaymentStatus;//还款状态（YH:已还款;WH:未还款 ）

    public String getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(String repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public long getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(long repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(String repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }
}
