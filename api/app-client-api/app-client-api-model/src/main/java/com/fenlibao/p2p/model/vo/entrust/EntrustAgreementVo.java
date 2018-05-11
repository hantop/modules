package com.fenlibao.p2p.model.vo.entrust;

import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;

/**
 * Created by zeronx on 2017/8/1.
 */
public class EntrustAgreementVo {

    private int id;
    private int userId;
    private int businessId; //
    private String signId; // 签名文档id
    private int flbPageNum;
    private float flbSignX;
    private float flbSignY;
    private int loanPageNum;
    private float loanSignX;
    private float loanSignY;
    private AgreementSignStatusEnum signStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
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

    public int getLoanPageNum() {
        return loanPageNum;
    }

    public void setLoanPageNum(int loanPageNum) {
        this.loanPageNum = loanPageNum;
    }

    public float getLoanSignX() {
        return loanSignX;
    }

    public void setLoanSignX(float loanSignX) {
        this.loanSignX = loanSignX;
    }

    public float getLoanSignY() {
        return loanSignY;
    }

    public void setLoanSignY(float loanSignY) {
        this.loanSignY = loanSignY;
    }

    public AgreementSignStatusEnum getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(AgreementSignStatusEnum signStatus) {
        this.signStatus = signStatus;
    }
}
