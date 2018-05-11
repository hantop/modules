package com.fenlibao.p2p.model.xinwang.entity.account;

import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @date 2017/5/25 20:33
 */
public class XWFundAccount {
    private static final long serialVersionUID = 1L;

    /**
     * 账户ID,自增
     */
    private Integer id;

    /**
     * 用户ID,参考T6110.F01
     */
    private Integer userId;

    /**
     * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;SDZH:锁定账户;
     */
    private SysFundAccountType fundAccountType;

    /**
     * 资金账号
     */
    private String fundAccountCode;

    /**
     * 账户名称
     */
    private String userName;

    /**
     * 余额
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 最后更新时间
     */
    private Timestamp updateTime;

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

    public SysFundAccountType getFundAccountType() {
        return fundAccountType;
    }

    public void setFundAccountType(SysFundAccountType fundAccountType) {
        this.fundAccountType = fundAccountType;
    }

    public String getFundAccountCode() {
        return fundAccountCode;
    }

    public void setFundAccountCode(String fundAccountCode) {
        this.fundAccountCode = fundAccountCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
