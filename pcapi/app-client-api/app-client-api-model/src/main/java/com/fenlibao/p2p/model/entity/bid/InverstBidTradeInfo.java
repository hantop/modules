package com.fenlibao.p2p.model.entity.bid;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 对应数据库表 t6504 投标订单
 * Created by Louis Wang on 2015/10/28.
 */

public class InverstBidTradeInfo implements Serializable {

    public Integer orderId; //F01   订单ID
    public Integer userId;  //F02   投资用户ID
    public Integer bidId;   //F03   标ID
    public BigDecimal money = BigDecimal.ZERO;    //F04 投标金额
    public Integer bidRecordId;     //投标记录ID,参考T6250.F01,投标成功时记录


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getBidRecordId() {
        return bidRecordId;
    }

    public void setBidRecordId(Integer bidRecordId) {
        this.bidRecordId = bidRecordId;
    }
}
