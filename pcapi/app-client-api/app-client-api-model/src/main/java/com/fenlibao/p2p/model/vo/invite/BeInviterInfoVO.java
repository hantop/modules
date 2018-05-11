package com.fenlibao.p2p.model.vo.invite;

/**
 * 被邀请人信息
 * Created by chenzhixuan on 2016/7/26.
 */
public class BeInviterInfoVO {
    private String realname;// 姓名
    private String phonenum;// 手机号码
    private Long registerDate;// 注册时间戳
    private boolean hasInvest;// 是否投资(false:未投资 true:已投资)

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public Long getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Long registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isHasInvest() {
        return hasInvest;
    }

    public void setHasInvest(boolean hasInvest) {
        this.hasInvest = hasInvest;
    }
}
