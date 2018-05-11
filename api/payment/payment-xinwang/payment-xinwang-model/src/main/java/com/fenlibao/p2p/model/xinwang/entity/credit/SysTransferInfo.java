package com.fenlibao.p2p.model.xinwang.entity.credit;

import java.math.BigDecimal;

/**
 * 平台债权转让信息
 * @date 2017/6/1 14:46
 */
public class SysTransferInfo extends BaseCreditInfo{
    Integer applyId;//T6260.F01
    BigDecimal creditAmount;//转让价格
    String creditsaleNo;//出让流水
    String transactionNo;//预处理流水

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getCreditsaleNo() {
        return creditsaleNo;
    }

    public void setCreditsaleNo(String creditsaleNo) {
        this.creditsaleNo = creditsaleNo;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
}
