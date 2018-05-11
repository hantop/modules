package com.fenlibao.p2p.model.xinwang.param.transaction;



import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;

import java.math.BigDecimal;

/**
 * 单笔交易参数
 * @date 2017/6/7 15:32
 */
public class SyncTransactionParam {
    com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType tradeType;
    String projectNo;
    XWBizType bizType;
    BigDecimal amount;
    String freezeRequestNo;//预处理请求参数
    String sourcePlatformUserNo;
    String targetPlatformUserNo;
    BigDecimal share;
    String remark;
    String saleRequestNo;//债权转让参数

    public XWTradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(XWTradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public XWBizType getBizType() {
        return bizType;
    }

    public void setBizType(XWBizType bizType) {
        this.bizType = bizType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFreezeRequestNo() {
        return freezeRequestNo;
    }

    public void setFreezeRequestNo(String freezeRequestNo) {
        this.freezeRequestNo = freezeRequestNo;
    }

    public String getSourcePlatformUserNo() {
        return sourcePlatformUserNo;
    }

    public void setSourcePlatformUserNo(String sourcePlatformUserNo) {
        this.sourcePlatformUserNo = sourcePlatformUserNo;
    }

    public String getTargetPlatformUserNo() {
        return targetPlatformUserNo;
    }

    public void setTargetPlatformUserNo(String targetPlatformUserNo) {
        this.targetPlatformUserNo = targetPlatformUserNo;
    }

    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSaleRequestNo() {
        return saleRequestNo;
    }

    public void setSaleRequestNo(String saleRequestNo) {
        this.saleRequestNo = saleRequestNo;
    }
}
