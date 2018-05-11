package com.fenlibao.model.pms.da.planCenter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/20.
 */
public class PlanType {

    private int id;
    private String title;
    private BigDecimal rate;// 利率
    private BigDecimal raisedRate;// 加息利率
    private Integer amount;// 计划借款总金额
    private int cycle;// 借款周期
    private String cycleType;// 借款周期类型（按天'd'/按月'm'）
    private String repaymentType;//还款方式('DEBX','MYFX','YCFQ','DEBJ')
    private int stageNum;// 分期期数
    private String description; // 计划描述
    private String isNoviceBid;// 是否新手标
    private int priority; // 优先级（在同时筹满的相应标的情况下，优先级高的先发）
    private int waitTime;// 如果超过该时间（分钟）未能筹满，并且超过最小金额，则按已筹集的金额发出计划；-1：则直至筹集到满足条件的金额才发布
    private BigDecimal minAmount; // 最小发布计划金额
    private int isCg;// 1：普通计划，2：存管计划
    private Integer totalUserAssets;//用户资产总额
    private Integer userInvestingAmount;//用户在投金额
    private Integer userAccumulatedIncome;//用户累计收益
    private boolean targetUser;//指定用户(1:指定 0:不指定)
    private String label;// '标签'
    private String customLabel1;// '标签1'
    private String customLabel2;// '标签2'
    private String comment;// '条件说明'
    private Date createTime;// 创建时间
    private Date updateTime;// 修改时间
    private String status;// 状态(停用/启用)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRaisedRate() {
        return raisedRate;
    }

    public void setRaisedRate(BigDecimal raisedRate) {
        this.raisedRate = raisedRate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(String isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public int getIsCg() {
        return isCg;
    }

    public void setIsCg(int isCg) {
        this.isCg = isCg;
    }

    public Integer getTotalUserAssets() {
        return totalUserAssets;
    }

    public void setTotalUserAssets(Integer totalUserAssets) {
        this.totalUserAssets = totalUserAssets;
    }

    public Integer getUserInvestingAmount() {
        return userInvestingAmount;
    }

    public void setUserInvestingAmount(Integer userInvestingAmount) {
        this.userInvestingAmount = userInvestingAmount;
    }

    public Integer getUserAccumulatedIncome() {
        return userAccumulatedIncome;
    }

    public void setUserAccumulatedIncome(Integer userAccumulatedIncome) {
        this.userAccumulatedIncome = userAccumulatedIncome;
    }

    public boolean isTargetUser() {
        return targetUser;
    }

    public void setTargetUser(boolean targetUser) {
        this.targetUser = targetUser;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomLabel2() {
        return customLabel2;
    }

    public void setCustomLabel2(String customLabel2) {
        this.customLabel2 = customLabel2;
    }

    public String getCustomLabel1() {

        return customLabel1;
    }

    public void setCustomLabel1(String customLabel1) {
        this.customLabel1 = customLabel1;
    }

}





