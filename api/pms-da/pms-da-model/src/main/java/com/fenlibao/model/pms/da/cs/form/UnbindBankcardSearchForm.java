package com.fenlibao.model.pms.da.cs.form;

/**
 * 解绑银行卡记录
 * Created by chenzhixuan on 2015/12/4.
 */
public class UnbindBankcardSearchForm {
    private String userAccount;// 用户账号
    private String operator;// 解绑人
    private String unbindStartTime;// 解绑开始时间
    private String unbindEndTime;// 解绑结束时间

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getUnbindStartTime() {
        return unbindStartTime;
    }

    public void setUnbindStartTime(String unbindStartTime) {
        this.unbindStartTime = unbindStartTime;
    }

    public String getUnbindEndTime() {
        return unbindEndTime;
    }

    public void setUnbindEndTime(String unbindEndTime) {
        this.unbindEndTime = unbindEndTime;
    }
}
