package com.fenlibao.model.pms.da.biz.form;

import java.math.BigDecimal;

public class InterestManagementForm {
    private int pid;// 当前启用利息管理费比例id
    private String currentPercentId;// 当前启用利息管理费比例id
    private BigDecimal currentPercent;// 当前启用利息管理费比例
    private String currentPercentStr;
    private BigDecimal updatePercent;// 修改后利息管理费比例
    private String updatePercentStr;// 修改后利息管理费比例

    public String getCurrentPercentId() {
        return currentPercentId;
    }

    public void setCurrentPercentId(String currentPercentId) {
        this.currentPercentId = currentPercentId;
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

    public String getCurrentPercentStr() {
        return currentPercentStr;
    }

    public void setCurrentPercentStr(String currentPercentStr) {
        this.currentPercentStr = currentPercentStr;
    }

    public BigDecimal getUpdatePercent() {
        return updatePercent;
    }

    public void setUpdatePercent(BigDecimal updatePercent) {
        this.updatePercent = updatePercent;
    }

    public String getUpdatePercentStr() {
        return updatePercentStr;
    }

    public void setUpdatePercentStr(String updatePercentStr) {
        this.updatePercentStr = updatePercentStr;
    }

}
