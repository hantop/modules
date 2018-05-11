package com.fenlibao.p2p.model.entity.activity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 查询满足条件的注册用户的实体
 * Created by xiao on 2017/4/26.
 */
public class MoneyTreeRegisterCheckEntity {
    private int inviteeId;//被邀请人id
    private String inviteePhoneNum;//被邀请人phone
    private int inviterId;//邀请人id
    private String inviterPhoneNum;//邀请人phone
    private BigDecimal investAmount;//首投金额
    private Timestamp investTime;//投资时间
    private int invitedNumToday;//该邀请人今天已经获得的分利果数量

    public MoneyTreeRegisterCheckEntity(int inviteeId, String inviteePhoneNum, int inviterId, String inviterPhoneNum, BigDecimal investAmount, Timestamp investTime, int invitedNumToday) {

        this.inviteeId = inviteeId;
        this.inviteePhoneNum = inviteePhoneNum;
        this.inviterId = inviterId;
        this.inviterPhoneNum = inviterPhoneNum;
        this.investAmount = investAmount;
        this.investTime = investTime;
        this.invitedNumToday = invitedNumToday;
    }

    public MoneyTreeRegisterCheckEntity() {

    }

    public int getInvitedNumToday() {
        return invitedNumToday;
    }

    public void setInvitedNumToday(int invitedNumToday) {
        this.invitedNumToday = invitedNumToday;
    }

    public int getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(int inviteeId) {
        this.inviteeId = inviteeId;
    }

    public String getInviteePhoneNum() {
        return inviteePhoneNum;
    }

    public void setInviteePhoneNum(String inviteePhoneNum) {
        this.inviteePhoneNum = inviteePhoneNum;
    }

    public int getInviterId() {
        return inviterId;
    }

    public void setInviterId(int inviterId) {
        this.inviterId = inviterId;
    }

    public String getInviterPhoneNum() {
        return inviterPhoneNum;
    }

    public void setInviterPhoneNum(String inviterPhoneNum) {
        this.inviterPhoneNum = inviterPhoneNum;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Timestamp getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Timestamp investTime) {
        this.investTime = investTime;
    }
}
