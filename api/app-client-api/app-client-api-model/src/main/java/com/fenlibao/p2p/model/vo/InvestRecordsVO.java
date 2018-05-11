package com.fenlibao.p2p.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 投标记录 T6250
 * Created by LouisWang on 2015/8/14.
 */
public class InvestRecordsVO implements Serializable {
    /**
     * 自增ID
     */
    public int id; //F01

    /**
     * 标ID,参考T6230.F01
     */
    public int bid; //F02

    /**
     * 投资人ID,参考T6110.F01
     */
    public int investId; //F03

    /**
     * 购买价格
     */
    public BigDecimal price = BigDecimal.ZERO;    //F04

    /**
     * 债权金额
     */
    public BigDecimal creditorAmount  = BigDecimal.ZERO;   //F05

    /**
     * 投标时间
     */
    public Timestamp timestamp;   //F06

    /**
     * 是否取消,F:否;S:是;
     */
    public boolean status;   //T6250_F07 F07

    /**
     * 是否已放款,F:否;S:是;
     */
    public boolean loanStatus;   //T6250_F08 F08

    /**
     * 预授权合同号
     */
    public String pactName;  // F10

    /**
     * 红包ID,参考T6351.F01
     */
    public int redPacketId; //F13

    /**
     * 红包金额,参考T6351.F04
     */
    public BigDecimal redPacket;    //F14

    /**
     * 标状态,参考T6230.F20
     */
    // public T6230_F20 F20;
    public String bidStatus;

    public long longTime ;

    private String phone;//投资人手机

    private int isCG; //1：普通标 2：存管标

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getInvestId() {
        return investId;
    }

    public void setInvestId(int investId) {
        this.investId = investId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCreditorAmount() {
        return creditorAmount;
    }

    public void setCreditorAmount(BigDecimal creditorAmount) {
        this.creditorAmount = creditorAmount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(boolean loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getPactName() {
        return pactName;
    }

    public void setPactName(String pactName) {
        this.pactName = pactName;
    }

    public int getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(int redPacketId) {
        this.redPacketId = redPacketId;
    }

    public BigDecimal getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(BigDecimal redPacket) {
        this.redPacket = redPacket;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public long getLongTime() {
        return longTime;
    }

    public void setLongTime(long longTime) {
        this.longTime = longTime;
    }

    public int getIsCG() {
        return isCG;
    }

    public void setIsCG(int isCG) {
        this.isCG = isCG;
    }
}
