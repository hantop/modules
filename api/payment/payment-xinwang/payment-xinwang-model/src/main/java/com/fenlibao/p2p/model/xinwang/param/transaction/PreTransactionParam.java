package com.fenlibao.p2p.model.xinwang.param.transaction;

import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;

import java.math.BigDecimal;

/**
 * 单笔交易请求参数
 * @date 2017/6/7 14:41
 */
public class PreTransactionParam {
    XWBizType bizType;
    String projectNo;
    String platformUserNo;
    BigDecimal amount;
    BigDecimal share;
    String creditsaleRequestNo;
    String requestNo;

    public XWBizType getBizType() {
        return bizType;
    }

    public void setBizType(XWBizType bizType) {
        this.bizType = bizType;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }

    public String getCreditsaleRequestNo() {
        return creditsaleRequestNo;
    }

    public void setCreditsaleRequestNo(String creditsaleRequestNo) {
        this.creditsaleRequestNo = creditsaleRequestNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }
}
