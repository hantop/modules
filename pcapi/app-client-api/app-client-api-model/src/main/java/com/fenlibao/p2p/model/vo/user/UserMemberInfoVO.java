package com.fenlibao.p2p.model.vo.user;

import java.util.Date;

/**
 * @author zeronx on 2017/12/20 11:08.
 * @version 1.0
 */
public class UserMemberInfoVO {

    private String phoneNum;//会员手机号码
    private Integer memberId;//商户会员ID pf.pf_merchant_member.id
    private Integer pointsId;//会员积分账号ID mp.mp_member_points.id
    private Date createTime;//创建时间
    private String channelCode;//渠道编码

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getPointsId() {
        return pointsId;
    }

    public void setPointsId(Integer pointsId) {
        this.pointsId = pointsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

}
