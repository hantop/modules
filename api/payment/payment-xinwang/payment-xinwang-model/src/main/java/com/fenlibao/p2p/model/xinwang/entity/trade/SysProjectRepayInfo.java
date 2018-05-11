package com.fenlibao.p2p.model.xinwang.entity.trade;

import com.fenlibao.p2p.model.xinwang.enums.trade.SysCompensatory;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWRepayProgress;

import java.math.BigDecimal;

/**
 * flb.t_xw_repay
 */
public class SysProjectRepayInfo {
    private Integer id;
    /**
     * 标id
     */
    private Integer projectId;
    /**
     * 期号
     */
    private Integer term;
    /**
     * 整标还款id
     */
    private Integer orderId;
    /**
     * 到期还款/提前还款
     */
    private SysRepayOperationType repayType;
    /**
     * 借款人还款总额
     */
    private BigDecimal borrowerRepayAmount;

    /**
     * 还款进度
     */
    private XWRepayProgress repayProgress;

    /**
     * 是否代偿
     */
    private SysCompensatory compensatory;

    /**
     * 还款预处理请求号
     */
    private String preTreatRequestNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public SysRepayOperationType getRepayType() {
        return repayType;
    }

    public void setRepayType(SysRepayOperationType repayType) {
        this.repayType = repayType;
    }

    public BigDecimal getBorrowerRepayAmount() {
        return borrowerRepayAmount;
    }

    public void setBorrowerRepayAmount(BigDecimal borrowerRepayAmount) {
        this.borrowerRepayAmount = borrowerRepayAmount;
    }

    public XWRepayProgress getRepayProgress() {
        return repayProgress;
    }

    public void setRepayProgress(XWRepayProgress repayProgress) {
        this.repayProgress = repayProgress;
    }

    public SysCompensatory getCompensatory() {
        return compensatory;
    }

    public void setCompensatory(SysCompensatory compensatory) {
        this.compensatory = compensatory;
    }

    public String getPreTreatRequestNo() {
        return preTreatRequestNo;
    }

    public void setPreTreatRequestNo(String preTreatRequestNo) {
        this.preTreatRequestNo = preTreatRequestNo;
    }
}
