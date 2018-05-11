package com.fenlibao.p2p.model.xinwang.entity.trust;

import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托支付授权记录查询
 *
 * @date 2017/7/14 16:37
 */
public class AuthorizationEntrustRecord extends BaseResult implements Serializable {
    String authorizeStatus;//委托支付授权审核状态
    String borrowPlatformUserNo;//借款方平台会员编号
    String entrustedPlatformUserNo;//受托方平台用户编号
    String requestNo;
    String projectNo;
    String entrustedType;//受托方类型，枚举值 PERSONAL（个人），ENTERPRISE（企业）
    Date createTime;
    Date completedTime;

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getBorrowPlatformUserNo() {
        return borrowPlatformUserNo;
    }

    public void setBorrowPlatformUserNo(String borrowPlatformUserNo) {
        this.borrowPlatformUserNo = borrowPlatformUserNo;
    }

    public String getEntrustedPlatformUserNo() {
        return entrustedPlatformUserNo;
    }

    public void setEntrustedPlatformUserNo(String entrustedPlatformUserNo) {
        this.entrustedPlatformUserNo = entrustedPlatformUserNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getEntrustedType() {
        return entrustedType;
    }

    public void setEntrustedType(String entrustedType) {
        this.entrustedType = entrustedType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }
}
