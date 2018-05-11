package com.fenlibao.model.pms.da.riskcontrol;

import java.util.Date;

/**
 * 消费信贷风控模型数据-审核信息
 * Created by Administrator on 2017/12/27.
 */
public class RiskAuditInfo {

    private int bidInfoId;

    private Date cerateTime;

    private Date auditTime;

    private int creditScore;

    private int auditStatus;


    public int getBidInfoId() {
        return bidInfoId;
    }

    public void setBidInfoId(int bidInfoId) {
        this.bidInfoId = bidInfoId;
    }

    public Date getCerateTime() {
        return cerateTime;
    }

    public void setCerateTime(Date cerateTime) {
        this.cerateTime = cerateTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }
}
