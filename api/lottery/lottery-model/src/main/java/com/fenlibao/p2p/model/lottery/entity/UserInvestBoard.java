package com.fenlibao.p2p.model.lottery.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserInvestBoard implements Serializable {

    //用户手机尾号
    private String cellTailNumber;

    //奖项名称
    private BigDecimal totalInvestAmount;

    public String getCellTailNumber() {
        return cellTailNumber;
    }

    public void setCellTailNumber(String cellTailNumber) {
        this.cellTailNumber = cellTailNumber;
    }

    public BigDecimal getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(BigDecimal totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }
}