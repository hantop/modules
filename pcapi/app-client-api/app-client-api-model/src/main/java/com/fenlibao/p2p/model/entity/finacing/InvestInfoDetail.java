package com.fenlibao.p2p.model.entity.finacing;

import java.util.Date;

/**
 * Created by laubrence on 2016/3/28.
 */
public class InvestInfoDetail extends InvestInfo{

    /*借款描述*/
    String remark;

    /*投资时间*/
    Date investTime;

    /*计息时间*/
    Date interestTime;

    /*到期时间*/
    Date expireTime;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }
}
