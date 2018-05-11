package com.fenlibao.p2p.model.xinwang.entity.common
;


import com.fenlibao.p2p.model.xinwang.enums.common.XWCapitalFlowLevel;
import com.fenlibao.p2p.model.xinwang.enums.common.CheckStatus;

import java.math.BigDecimal;
import java.util.Date;

/** 
 * 资金流水
 */
public class XWCapitalFlow {

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    private Integer id;

    /** 
     * 资金账号ID,参考T6101.F01
     */
    private Integer fundAccountId;

    /** 
     * 交易类型ID,参考T5122.F01
     */
    private Integer tadeType;

    /** 
     * 对方账户ID,参考T6101.F01
     */
    private Integer otherFundAccountId;

    /** 
     * 创建时间
     */
    private Date createTime;

    /** 
     * 收入
     */
    private BigDecimal income = BigDecimal.ZERO;

    /** 
     * 支出
     */
    private BigDecimal expenditure = BigDecimal.ZERO;

    /** 
     * 余额
     */
    private BigDecimal balance = BigDecimal.ZERO;

    /** 
     * 备注
     */
    private String remark;

    /** 
     * 对账状态,WDZ:未对账;YDZ:已对账;
     */
    private CheckStatus checkStatus;

    /** 
     * 对账时间
     */
    private Date checkTime;
    
    /**
     * 标ID
     */
    private Integer projectId;

    private String requestNo;

    private Integer orderId;

    private XWCapitalFlowLevel Level=XWCapitalFlowLevel.YH;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFundAccountId() {
        return fundAccountId;
    }

    public void setFundAccountId(Integer fundAccountId) {
        this.fundAccountId = fundAccountId;
    }

    public Integer getTadeType() {
        return tadeType;
    }

    public void setTadeType(Integer tadeType) {
        this.tadeType = tadeType;
    }

    public Integer getOtherFundAccountId() {
        return otherFundAccountId;
    }

    public void setOtherFundAccountId(Integer otherFundAccountId) {
        this.otherFundAccountId = otherFundAccountId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(BigDecimal expenditure) {
        this.expenditure = expenditure;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public XWCapitalFlowLevel getLevel() {
        return Level;
    }

    public void setLevel(XWCapitalFlowLevel level) {
        Level = level;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "XWCapitalFlow{" +
                "id=" + id +
                ", fundAccountId=" + fundAccountId +
                ", tadeType=" + tadeType +
                ", otherFundAccountId=" + otherFundAccountId +
                ", createTime=" + createTime +
                ", income=" + income +
                ", expenditure=" + expenditure +
                ", balance=" + balance +
                ", remark='" + remark + '\'' +
                ", checkStatus=" + checkStatus +
                ", checkTime=" + checkTime +
                ", projectId=" + projectId +
                ", requestNo='" + requestNo + '\'' +
                ", orderId=" + orderId +
                ", Level=" + Level +
                '}';
    }
}
