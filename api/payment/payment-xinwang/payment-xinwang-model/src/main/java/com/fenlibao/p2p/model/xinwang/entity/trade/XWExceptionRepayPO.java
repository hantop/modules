package com.fenlibao.p2p.model.xinwang.entity.trade;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/9/20.
 */
public class XWExceptionRepayPO {
    private Integer id;
    private Integer userId;
    private String userRole;
    private String exceptionRequestNo;
    private BigDecimal deductAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getExceptionRequestNo() {
        return exceptionRequestNo;
    }

    public void setExceptionRequestNo(String exceptionRequestNo) {
        this.exceptionRequestNo = exceptionRequestNo;
    }

    public BigDecimal getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(BigDecimal deductAmount) {
        this.deductAmount = deductAmount;
    }
}
