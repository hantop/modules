package com.fenlibao.p2p.model.entity.bid;

import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;

/**
 * Created by kris on 2016-11-08
 * 三方合同
 */
public class Tripleagreement {
    private int id;
    private String signid;
    private int flbPageNum;
    private float flbSignX;
    private float flbSignY;
    private AgreementSignStatusEnum signStatus;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public AgreementSignStatusEnum getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(AgreementSignStatusEnum signStatus) {
        this.signStatus = signStatus;
    }
}
