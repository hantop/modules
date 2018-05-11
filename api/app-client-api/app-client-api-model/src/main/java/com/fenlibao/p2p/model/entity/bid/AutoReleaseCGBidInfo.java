package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by xiao on 2017/7/10.
 */
public class AutoReleaseCGBidInfo implements Comparable {
    private int id;
    private String serialNum;//借款流水号
    private int orderNum;// 排序号
    private String repaymentType;//还款类型
    private int stageNum;//分期期数
    private BigDecimal amount;//借款金额（元）
    private int cycle;//借款周期
    private String cycleType;//DEFAULT d  借款周期类型（按天/按月）
    private String idNo;//借款用户身份证
    private String idName;//借款用户姓名
    private String phoneNum;//借款用户电话号码
    private String loanStatus;//DEFAULT DCL  状态（DCL:待处理，CLZ:处理中，CLCG:处理成功，CLSB:处理失败, SDZ:计划锁定中, YCL:预处理，等待存管发布处理）
    private Timestamp createTime;//创建时间
    private Timestamp updateTime;//更新时间
    private int dealCount;//迪蒙发布处理次数
    private int cgDealCount;//存管处理次数
    private int bidId;//对应标id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public int getStageNum() {
        return stageNum;
    }

    public void setStageNum(int stageNum) {
        this.stageNum = stageNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getDealCount() {
        return dealCount;
    }

    public void setDealCount(int dealCount) {
        this.dealCount = dealCount;
    }

    public int getCgDealCount() {
        return cgDealCount;
    }

    public void setCgDealCount(int cgDealCount) {
        this.cgDealCount = cgDealCount;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    @Override
    public int compareTo(Object o) {
        return amount.compareTo(((AutoReleaseCGBidInfo) o).amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        AutoReleaseCGBidInfo autoReleaseCGBidInfo = (AutoReleaseCGBidInfo) obj;
        return autoReleaseCGBidInfo.id == this.id;
    }
}
