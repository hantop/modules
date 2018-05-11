package com.fenlibao.p2p.model.lottery.vo;

import java.math.BigDecimal;

public class UserInvestBoardVO {

    //用户手机尾号
    private String cellTailNumber;

    //用户累计投资金额
    private String totalInvestAmount;

    public String getCellTailNumber() {
        return cellTailNumber;
    }

    public void setCellTailNumber(String cellTailNumber) {
        this.cellTailNumber = cellTailNumber;
    }

    public String getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(String totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }
}