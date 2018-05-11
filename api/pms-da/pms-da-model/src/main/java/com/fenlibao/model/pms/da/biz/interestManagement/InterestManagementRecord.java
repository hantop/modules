package com.fenlibao.model.pms.da.biz.interestManagement;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class InterestManagementRecord {
    private int recordId;// 操作记录id
    private int pid;// 当前启用利息管理费比例id
    private BigDecimal currentPercent;// 当前启用利息管理费比例
    private int sid;// 修改后利息管理费比例id
    private BigDecimal updatePercent;// 修改后利息管理费比例
    private int state;// 状态(修改后的状态)
    private String operator;// 操作人
    private Timestamp operateTime;// 操作时间
    private String auditor;// 审核人
    private Timestamp auditTime;// 审核时间时间
    private String currentPercentStr;// 当前启用利息管理费比例
    private String updatePercentStr;// 修改后利息管理费比例

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public BigDecimal getCurrentPercent() {
        return currentPercent;
    }

    public void setCurrentPercent(BigDecimal currentPercent) {
        this.currentPercent = currentPercent;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public BigDecimal getUpdatePercent() {
        return updatePercent;
    }

    public void setUpdatePercent(BigDecimal updatePercent) {
        this.updatePercent = updatePercent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Timestamp getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime) {
        this.auditTime = auditTime;
    }

    public String getCurrentPercentStr() {
        return currentPercentStr;
    }

    public void setCurrentPercentStr(String currentPercentStr) {
        this.currentPercentStr = currentPercentStr;
    }

    public String getUpdatePercentStr() {
        return updatePercentStr;
    }

    public void setUpdatePercentStr(String updatePercentStr) {
        this.updatePercentStr = updatePercentStr;
    }
}
