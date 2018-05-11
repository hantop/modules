package com.fenlibao.p2p.model.entity.trade;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/2/15.
 */
public class PlanBidProfit {

    private int recordId;//投资记录id

    private BigDecimal profit;//收益

    private BigDecimal raiseProfit;//加息收益

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getRaiseProfit() {
        return raiseProfit;
    }

    public void setRaiseProfit(BigDecimal raiseProfit) {
        this.raiseProfit = raiseProfit;
    }
}
