package com.fenlibao.model.pms.da.statistics.authentication;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Louis Wang on 2015/12/30.
 */

public class AuthenticationInfo {
    private Date createDate;
    private Integer authCount;  //次数
    private BigDecimal authMoney = new BigDecimal(0);  //次数

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getAuthCount() {
        return authCount;
    }

    public void setAuthCount(Integer authCount) {
        this.authCount = authCount;
    }

    public BigDecimal getAuthMoney() {
        return authMoney;
    }

    public void setAuthMoney(BigDecimal authMoney) {
        this.authMoney = authMoney;
    }
}
