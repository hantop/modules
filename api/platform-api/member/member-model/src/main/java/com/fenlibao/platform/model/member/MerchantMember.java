package com.fenlibao.platform.model.member;

import java.util.Date;

/**
 * 商户会员信息
 * Created by Lullaby on 2016/2/18.
 */
public class MerchantMember {

    private int id;

    private int mId;

    private String openid;

    private int pfUserId;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getPfUserId() {
        return pfUserId;
    }

    public void setPfUserId(int pfUserId) {
        this.pfUserId = pfUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
