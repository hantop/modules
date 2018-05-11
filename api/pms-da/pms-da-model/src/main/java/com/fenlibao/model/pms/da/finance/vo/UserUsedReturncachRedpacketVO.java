package com.fenlibao.model.pms.da.finance.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/12.
 */
public class UserUsedReturncachRedpacketVO {
    private Integer redpacketId;// 返现券ID
    private Date activityTime;// 激活时间
    private String phoneNum;// 手机号
    private BigDecimal redMoney;// 返现券金额

    public Integer getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(Integer redpacketId) {
        this.redpacketId = redpacketId;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }
}
