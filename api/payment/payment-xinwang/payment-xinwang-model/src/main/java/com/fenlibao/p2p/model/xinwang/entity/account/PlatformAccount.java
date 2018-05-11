package com.fenlibao.p2p.model.xinwang.entity.account;

import com.fenlibao.p2p.model.xinwang.enums.account.PlatformAccount_UserType;
import com.fenlibao.p2p.model.xinwang.enums.account.SysUserStatus;

/**
 * Created by Administrator on 2017/5/22.
 */
public class PlatformAccount {
    /**
     * t6110.F01
     */
    private Integer userId;
    /**
     * t6110.F02 分利宝注册用户名
     */
    private String userName;
    /**
     * t6110.F04 不一定是银行预留手机号
     */
    private String mobile;

    /**
     * t6110.F06 个人/企业
     */
    private PlatformAccount_UserType userType;

    /**
     * t6110.F07
     */
    private SysUserStatus userStatus;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public SysUserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(SysUserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public PlatformAccount_UserType getUserType() {
        return userType;
    }

    public void setUserType(PlatformAccount_UserType userType) {
        this.userType = userType;
    }
}
