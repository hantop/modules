package com.fenlibao.model.pms.da.riskcontrol;

import java.util.Date;

/**
 * 消费信贷风控模型数据-多头借贷
 * Created by Administrator on 2017/12/27.
 */
public class RiskMutiBorrow {
    /**
     *借款标ID,参考flb.t_consume_bidinfo.id
     */
    private String bidInfoId;
    /**
     *创建时间
     */
    private Date createTime;

    /**
     * 1天内设备使用过多的身份证或 手机号进行申请
     **/
    private int deviceUseMutiId1D;
    /**
     * 7天内设备使用过多的身份证 或手机号进行申请
     **/
    private int deviceUseMutiId7D;
    /**
     * 1天内身份证使用过多设备 进行申请
     **/
    private int idUseMutiDevice1D;
    /**
     * 7天内身份证使用过多设备 进行申请
     **/
    private int idUseMutiDevice7D;
    /**
     * 1个月内身份证使用过多设备 进行申请
     **/
    private int idUseMutiDevice1M;
    /**
     * 7天内申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform7D;
    /**
     * 1个月内申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform1M;
    /**
     * 3个月内申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform3M;
    /**
     * 6个月内申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform6M;
    /**
     * 12个月内申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform12M;
    /**
     * 18个月内申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform18M;
    /**
     * 24个月内申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform24M;
    /**
     * 近60个月以上申请人在多个平台 申请借款
     **/
    private int borrowMutiPlatform60M;
    /**
     * 3个月内申请人在多个平台被放款_ 不包含本合作方
     **/
    private int loanExclude3M;
    /**
     * 3个月内申请人被本合作方放款
     **/
    private int loanInclude3M;
    /**
     * 6个月内申请人在多个平台被放款
     **/
    private int loanMutiPlatform6M;
    /**
     * 12个月内申请人在多个平台被放款
     **/
    private int loanMutiPlatform12M;
    /**
     * 18个月内申请人在多个平台被放款
     **/
    private int loanMutiPlatform18M;
    /**
     * 24个月内申请人在多个平台被放款
     **/
    private int loanMutiPlatform24M;
    /**
     * 近60个月以上申请人在多个 平台被放款
     **/
    private int loanMutiPlatform60M;

    public String getBidInfoId() {
        return bidInfoId;
    }

    public void setBidInfoId(String bidInfoId) {
        this.bidInfoId = bidInfoId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDeviceUseMutiId1D() {
        return deviceUseMutiId1D;
    }

    public void setDeviceUseMutiId1D(int deviceUseMutiId1D) {
        this.deviceUseMutiId1D = deviceUseMutiId1D;
    }

    public int getDeviceUseMutiId7D() {
        return deviceUseMutiId7D;
    }

    public void setDeviceUseMutiId7D(int deviceUseMutiId7D) {
        this.deviceUseMutiId7D = deviceUseMutiId7D;
    }

    public int getIdUseMutiDevice1D() {
        return idUseMutiDevice1D;
    }

    public void setIdUseMutiDevice1D(int idUseMutiDevice1D) {
        this.idUseMutiDevice1D = idUseMutiDevice1D;
    }

    public int getIdUseMutiDevice7D() {
        return idUseMutiDevice7D;
    }

    public void setIdUseMutiDevice7D(int idUseMutiDevice7D) {
        this.idUseMutiDevice7D = idUseMutiDevice7D;
    }

    public int getIdUseMutiDevice1M() {
        return idUseMutiDevice1M;
    }

    public void setIdUseMutiDevice1M(int idUseMutiDevice1M) {
        this.idUseMutiDevice1M = idUseMutiDevice1M;
    }

    public int getBorrowMutiPlatform7D() {
        return borrowMutiPlatform7D;
    }

    public void setBorrowMutiPlatform7D(int borrowMutiPlatform7D) {
        this.borrowMutiPlatform7D = borrowMutiPlatform7D;
    }

    public int getBorrowMutiPlatform1M() {
        return borrowMutiPlatform1M;
    }

    public void setBorrowMutiPlatform1M(int borrowMutiPlatform1M) {
        this.borrowMutiPlatform1M = borrowMutiPlatform1M;
    }

    public int getBorrowMutiPlatform3M() {
        return borrowMutiPlatform3M;
    }

    public void setBorrowMutiPlatform3M(int borrowMutiPlatform3M) {
        this.borrowMutiPlatform3M = borrowMutiPlatform3M;
    }

    public int getBorrowMutiPlatform6M() {
        return borrowMutiPlatform6M;
    }

    public void setBorrowMutiPlatform6M(int borrowMutiPlatform6M) {
        this.borrowMutiPlatform6M = borrowMutiPlatform6M;
    }

    public int getBorrowMutiPlatform12M() {
        return borrowMutiPlatform12M;
    }

    public void setBorrowMutiPlatform12M(int borrowMutiPlatform12M) {
        this.borrowMutiPlatform12M = borrowMutiPlatform12M;
    }

    public int getBorrowMutiPlatform18M() {
        return borrowMutiPlatform18M;
    }

    public void setBorrowMutiPlatform18M(int borrowMutiPlatform18M) {
        this.borrowMutiPlatform18M = borrowMutiPlatform18M;
    }

    public int getBorrowMutiPlatform24M() {
        return borrowMutiPlatform24M;
    }

    public void setBorrowMutiPlatform24M(int borrowMutiPlatform24M) {
        this.borrowMutiPlatform24M = borrowMutiPlatform24M;
    }

    public int getBorrowMutiPlatform60M() {
        return borrowMutiPlatform60M;
    }

    public void setBorrowMutiPlatform60M(int borrowMutiPlatform60M) {
        this.borrowMutiPlatform60M = borrowMutiPlatform60M;
    }

    public int getLoanExclude3M() {
        return loanExclude3M;
    }

    public void setLoanExclude3M(int loanExclude3M) {
        this.loanExclude3M = loanExclude3M;
    }

    public int getLoanInclude3M() {
        return loanInclude3M;
    }

    public void setLoanInclude3M(int loanInclude3M) {
        this.loanInclude3M = loanInclude3M;
    }

    public int getLoanMutiPlatform6M() {
        return loanMutiPlatform6M;
    }

    public void setLoanMutiPlatform6M(int loanMutiPlatform6M) {
        this.loanMutiPlatform6M = loanMutiPlatform6M;
    }

    public int getLoanMutiPlatform12M() {
        return loanMutiPlatform12M;
    }

    public void setLoanMutiPlatform12M(int loanMutiPlatform12M) {
        this.loanMutiPlatform12M = loanMutiPlatform12M;
    }

    public int getLoanMutiPlatform18M() {
        return loanMutiPlatform18M;
    }

    public void setLoanMutiPlatform18M(int loanMutiPlatform18M) {
        this.loanMutiPlatform18M = loanMutiPlatform18M;
    }

    public int getLoanMutiPlatform24M() {
        return loanMutiPlatform24M;
    }

    public void setLoanMutiPlatform24M(int loanMutiPlatform24M) {
        this.loanMutiPlatform24M = loanMutiPlatform24M;
    }

    public int getLoanMutiPlatform60M() {
        return loanMutiPlatform60M;
    }

    public void setLoanMutiPlatform60M(int loanMutiPlatform60M) {
        this.loanMutiPlatform60M = loanMutiPlatform60M;
    }
}
