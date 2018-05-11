package com.fenlibao.platform.model.queqianme;

/**
 * Created by xiao on 2016/10/27.
 */
public class LogInfo {
    private int accountId;//后台帐号ID,参考T7110.F01
    private String operateType;//操作类型
    private String operateDescription;//操作描述
    private String remoteIP;//访问IP

    public LogInfo() {
    }

    public LogInfo(int accountId, String operateType, String operateDescription, String remoteIP) {
        this.accountId = accountId;
        this.operateType = operateType;
        this.operateDescription = operateDescription;
        this.remoteIP = remoteIP;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateDescription() {
        return operateDescription;
    }

    public void setOperateDescription(String operateDescription) {
        this.operateDescription = operateDescription;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }
}
