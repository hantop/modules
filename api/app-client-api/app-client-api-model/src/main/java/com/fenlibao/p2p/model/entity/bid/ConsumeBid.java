package com.fenlibao.p2p.model.entity.bid;

import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;

/**
 * Created by kris on 2016/10/28.
 * 消费信贷标投资记录
 */
public class ConsumeBid {
    private int id;
    private int bid;
    private String signid;
    private int flbPageNum;
    private float flbSignX;
    private float flbSignY;
    private int investorPageNum;
    private float investorSignX;
    private float investorSignY;
    private AgreementSignStatusEnum signStatus;

    public AgreementSignStatusEnum getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(AgreementSignStatusEnum signStatus) {
        this.signStatus = signStatus;
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

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public int getFlbPageNum() {
        return flbPageNum;
    }

    public void setFlbPageNum(int flbPageNum) {
        this.flbPageNum = flbPageNum;
    }

    public float getFlbSignX() {
        return flbSignX;
    }

    public void setFlbSignX(float flbSignX) {
        this.flbSignX = flbSignX;
    }

    public float getFlbSignY() {
        return flbSignY;
    }

    public void setFlbSignY(float flbSignY) {
        this.flbSignY = flbSignY;
    }

    public int getInvestorPageNum() {
        return investorPageNum;
    }

    public void setInvestorPageNum(int investorPageNum) {
        this.investorPageNum = investorPageNum;
    }

    public float getInvestorSignX() {
        return investorSignX;
    }

    public void setInvestorSignX(float investorSignX) {
        this.investorSignX = investorSignX;
    }

    public float getInvestorSignY() {
        return investorSignY;
    }

    public void setInvestorSignY(float investorSignY) {
        this.investorSignY = investorSignY;
    }
}
