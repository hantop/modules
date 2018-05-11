package com.fenlibao.model.pms.da.cs.investUser;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权记录
 * Created by Administrator on 2017/12/14.
 */
public class RightsRecord extends  InvestRecord {

    /**
     * 卖出时间
     */
    private Date soldTime;

    /**
     * 卖出金额
     */
    private BigDecimal soldAmount;

    /**
     * 手续费
     */
    private BigDecimal soldFee;

    public Date getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(Date soldTime) {
        this.soldTime = soldTime;
    }

    public BigDecimal getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(BigDecimal soldAmount) {
        this.soldAmount = soldAmount;
    }

    public BigDecimal getSoldFee() {
        return soldFee;
    }

    public void setSoldFee(BigDecimal soldFee) {
        this.soldFee = soldFee;
    }
}


