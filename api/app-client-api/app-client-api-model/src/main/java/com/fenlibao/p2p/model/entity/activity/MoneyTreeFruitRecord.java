package com.fenlibao.p2p.model.entity.activity;

import java.math.BigDecimal;

/**
 * 保存分利果的实体
 * Created by xiao on 2017/4/27.
 */
public class MoneyTreeFruitRecord {
    private int userId;//果实所属用户
    private int inviterId;//邀请用户ID
    private int inviteeId;//被邀请用户ID
    private BigDecimal inviteAmount;//被邀请人首投金额
//    private String status;//果实状态（未掉落，已掉落，已摘取）

    public MoneyTreeFruitRecord() {

    }

    public MoneyTreeFruitRecord(int userId, int inviterId, int inviteeId, BigDecimal inviteAmount) {

        this.userId = userId;
        this.inviterId = inviterId;
        this.inviteeId = inviteeId;
        this.inviteAmount = inviteAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInviterId() {
        return inviterId;
    }

    public void setInviterId(int inviterId) {
        this.inviterId = inviterId;
    }

    public int getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(int inviteeId) {
        this.inviteeId = inviteeId;
    }

    public BigDecimal getInviteAmount() {
        return inviteAmount;
    }

    public void setInviteAmount(BigDecimal inviteAmount) {
        this.inviteAmount = inviteAmount;
    }
}
