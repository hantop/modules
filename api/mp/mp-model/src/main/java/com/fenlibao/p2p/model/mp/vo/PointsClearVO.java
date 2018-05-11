package com.fenlibao.p2p.model.mp.vo;

/**
 * 会员积分清除信息
 * @date 2016/11/25 15:21
 */
public class PointsClearVO {
    private int id;
    private int userId;
    private int memberPoints; //积分

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

    public int getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(int memberPoints) {
        this.memberPoints = memberPoints;
    }
}
