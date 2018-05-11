package com.fenlibao.p2p.model.entity.redenvelope;

import java.util.Date;

/**
 * 领取投资分享红包记录
 * Created by zcai on 2016/6/24.
 */
public class ReceiveTenderShareEntity {

    private Integer id;
    private String phoneNum;
    private Integer investmentShareId; //投资分享红包记录ID flb.t_investment_share_re_record.id
    private Integer redEnvelopeId; //红包ID(添加加息券 added on 20161108)
    private Date createTime;
    private Integer redEnvelopeQty; //红包数量（获取用户每种红包的数量）
    private Integer couponType;//1=返现券；2=加息券 added on 20161108
    private Integer rewardSetId;//t_tender_share_setting_reward.id

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getInvestmentShareId() {
        return investmentShareId;
    }

    public void setInvestmentShareId(Integer investmentShareId) {
        this.investmentShareId = investmentShareId;
    }

    public Integer getRedEnvelopeId() {
        return redEnvelopeId;
    }

    public void setRedEnvelopeId(Integer redEnvelopeId) {
        this.redEnvelopeId = redEnvelopeId;
    }

    public Integer getRedEnvelopeQty() {
        return redEnvelopeQty;
    }

    public void setRedEnvelopeQty(Integer redEnvelopeQty) {
        this.redEnvelopeQty = redEnvelopeQty;
    }

    public Integer getRewardSetId() {
        return rewardSetId;
    }

    public void setRewardSetId(Integer rewardSetId) {
        this.rewardSetId = rewardSetId;
    }
}
