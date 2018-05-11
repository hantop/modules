package com.fenlibao.model.pms.da.biz.viewobject;


import java.util.Date;

/**
 * 标的
 */
public class BidVO {
    private int bidId;// id
    private int userId;// 用户Id
    private String name;// 借款人
    private String title;// 标的名称
    private String contractNO;// 合同编号
    private String account;// 借款人账号
    private int bidTypeId;// 标的类型
    private String bidAmount;// 借款金额
    private String rate;// 年利率
    private String canInvestAmount;// 可投金额
    private String tenderAmount;// 投标金额(借款金额 - 可投金额)
    private int month;// 借款期限月
    private int day;// 借款期限天
    private String repayment;// 还款方式
    private String status;// 状态
    private Date applyTime;// 处理时间
    private Date displayTime;// 显示时间
    private Date bidTime;// 可投时间
    private int isRelease = 1;// 用于判断该标是否可以点击发布操作的标志(默认都可以操作)
    private String projectType;// 存管标类型  'STANDARDPOWDER','ENTRUST_PAY'

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContractNO() {
        return contractNO;
    }

    public void setContractNO(String contractNO) {
        this.contractNO = contractNO;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getBidTypeId() {
        return bidTypeId;
    }

    public void setBidTypeId(int bidTypeId) {
        this.bidTypeId = bidTypeId;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCanInvestAmount() {
        return canInvestAmount;
    }

    public void setCanInvestAmount(String canInvestAmount) {
        this.canInvestAmount = canInvestAmount;
    }

    public String getTenderAmount() {
        return tenderAmount;
    }

    public void setTenderAmount(String tenderAmount) {
        this.tenderAmount = tenderAmount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getRepayment() {
        return repayment;
    }

    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public int getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(int isRelease) {
        this.isRelease = isRelease;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}