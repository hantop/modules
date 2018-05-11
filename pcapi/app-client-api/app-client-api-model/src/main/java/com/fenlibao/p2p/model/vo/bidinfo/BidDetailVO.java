package com.fenlibao.p2p.model.vo.bidinfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 标的详情
 */
public class BidDetailVO extends BidInfoVO {

    private static final long serialVersionUID = 1L;

    private String repaymentMode;//还款方式

    private int purchasedStatus; //购买状态

    private String fwxyUrl; //服务协议

    private long buyTimestamp;//购买时间

    private long interestTimestamp;//计息时间

    private long endTimestamp;//到期时间

    private String remark;//借款描述

    private String lawFileUrl;//法律文件HTML页面URL

    private String[] lawFiles;//法律文件链接

//    private double purchaseTotal;//总申购额度
//
//	private double purchasedTotal;//已申购额度

    private String borrowerUrl;//借款人信息html页面url

    private List<BidExtendGroupVO> groupInfoList; //标扩展信息

    private long diffBidtimes;//满标期限

    private int totalInvestPers; //总投资人数

    //20161012
    private int bidClassify;//标类型 0:普通标 1：新手标 2定向标

    private Long panicBuyingTime;//抢购时间（抢购标）

    private Long timeLeft;//剩余时间

    private Integer countdown;//倒计时配置参数

    private String userTotalAssets;//用户资产总额

    private String userInvestAmount;//用户累计投资金额

    private double bidInterestRise;//加息利率

    private int anytimeQuit;//是否是随时退出标

    private double interestRise;//加息券加息

    private int bidNum;//投标次数

    private String collectInteres;

    private int rulesCount;//定向标和随时退出标规则个数

    private String bonusRate;//月增长利率

    private double presentRate;//当前月利率

    private long nextRaiseDate;//下一加息日

    private int holdDays;//持有天数

    private int month;//当前月数

    private int newPlan;//是否是3.2.0计划

    private int quitStatus;//退出状态 0：到期退出成功  1：申请退出中   2：提前退出成功

    private long expireTime;//到期时间

    private long applyTime;//申请退出时间

    private long exitTime;//退出成功时间

    private LinkedHashMap<String,Object> projectInfo;//项目描述

    private LinkedHashMap<String,Object> borrowerInfo;//借款人信息

    private LinkedHashMap<String,Object> remarkArray;//描述分组

    private int isShowFile;//是否展示贷后文件

    private String recruitmentPeriod; //募集期

    public int getBidClassify() {
        return bidClassify;
    }

    public void setBidClassify(int bidClassify) {
        this.bidClassify = bidClassify;
    }

    public Integer getCountdown() {
        return countdown;
    }

    public void setCountdown(Integer countdown) {
        this.countdown = countdown;
    }

    public Long getPanicBuyingTime() {
        return panicBuyingTime;
    }

    public void setPanicBuyingTime(Long panicBuyingTime) {
        this.panicBuyingTime = panicBuyingTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getUserInvestAmount() {
        return userInvestAmount;
    }

    public void setUserInvestAmount(String userInvestAmount) {
        this.userInvestAmount = userInvestAmount;
    }

    public String getUserTotalAssets() {
        return userTotalAssets;
    }

    public void setUserTotalAssets(String userTotalAssets) {
        this.userTotalAssets = userTotalAssets;
    }

    public int getPurchasedStatus() {
        return purchasedStatus;
    }

    public void setPurchasedStatus(int purchasedStatus) {
        this.purchasedStatus = purchasedStatus;
    }

    public String getFwxyUrl() {
        return fwxyUrl;
    }

    public void setFwxyUrl(String fwxyUrl) {
        this.fwxyUrl = fwxyUrl;
    }

    public long getBuyTimestamp() {
        return buyTimestamp;
    }

    public void setBuyTimestamp(long buyTimestamp) {
        this.buyTimestamp = buyTimestamp;
    }

    public long getInterestTimestamp() {
        return interestTimestamp;
    }

    public void setInterestTimestamp(long interestTimestamp) {
        this.interestTimestamp = interestTimestamp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLawFileUrl() {
        return lawFileUrl;
    }

    public void setLawFileUrl(String lawFileUrl) {
        this.lawFileUrl = lawFileUrl;
    }

    public List<BidExtendGroupVO> getGroupInfoList() {
        return groupInfoList;
    }

    public void setGroupInfoList(List<BidExtendGroupVO> groupInfoList) {
        this.groupInfoList = groupInfoList;
    }

//	public double getPurchaseTotal() {
//		return purchaseTotal;
//	}
//
//	public void setPurchaseTotal(double purchaseTotal) {
//		this.purchaseTotal = purchaseTotal;
//	}
//
//	public double getPurchasedTotal() {
//		return purchasedTotal;
//	}
//
//	public void setPurchasedTotal(double purchasedTotal) {
//		this.purchasedTotal = purchasedTotal;
//	}

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String[] getLawFiles() {
        return lawFiles;
    }

    public void setLawFiles(String[] lawFiles) {
        this.lawFiles = lawFiles;
    }

    public String getBorrowerUrl() {
        return borrowerUrl;
    }

    public void setBorrowerUrl(String borrowerUrl) {
        this.borrowerUrl = borrowerUrl;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public long getDiffBidtimes() {
        return diffBidtimes;
    }

    public void setDiffBidtimes(long diffBidtimes) {
        this.diffBidtimes = diffBidtimes;
    }

    public int getTotalInvestPers() {
        return totalInvestPers;
    }

    public void setTotalInvestPers(int totalInvestPers) {
        this.totalInvestPers = totalInvestPers;
    }

    public double getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(double bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public double getInterestRise() {
        return interestRise;
    }

    public void setInterestRise(double interestRise) {
        this.interestRise = interestRise;
    }

    public int getBidNum() {
        return bidNum;
    }

    public void setBidNum(int bidNum) {
        this.bidNum = bidNum;
    }

    public String getCollectInteres() {
        return collectInteres;
    }

    public void setCollectInteres(String collectInteres) {
        this.collectInteres = collectInteres;
    }

    public int getRulesCount() {
        return rulesCount;
    }

    public void setRulesCount(int rulesCount) {
        this.rulesCount = rulesCount;
    }

    public String getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(String bonusRate) {
        this.bonusRate = bonusRate;
    }

    public double getPresentRate() {
        return presentRate;
    }

    public void setPresentRate(double presentRate) {
        this.presentRate = presentRate;
    }

    public long getNextRaiseDate() {
        return nextRaiseDate;
    }

    public void setNextRaiseDate(long nextRaiseDate) {
        this.nextRaiseDate = nextRaiseDate;
    }

    public int getHoldDays() {
        return holdDays;
    }

    public void setHoldDays(int holdDays) {
        this.holdDays = holdDays;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public int getNewPlan() {
        return newPlan;
    }

    @Override
    public void setNewPlan(int newPlan) {
        this.newPlan = newPlan;
    }

    public int getQuitStatus() {
        return quitStatus;
    }

    public void setQuitStatus(int quitStatus) {
        this.quitStatus = quitStatus;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getExitTime() {
        return exitTime;
    }

    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }

    public LinkedHashMap<String, Object> getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(LinkedHashMap<String, Object> projectInfo) {
        this.projectInfo = projectInfo;
    }

    public LinkedHashMap<String, Object> getBorrowerInfo() {
        return borrowerInfo;
    }

    public void setBorrowerInfo(LinkedHashMap<String, Object> borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
    }

    public LinkedHashMap<String, Object> getRemarkArray() {
        return remarkArray;
    }

    public void setRemarkArray(LinkedHashMap<String, Object> remarkArray) {
        this.remarkArray = remarkArray;
    }

    public int getIsShowFile() {
        return isShowFile;
    }

    public void setIsShowFile(int isShowFile) {
        this.isShowFile = isShowFile;
    }

    public String getRecruitmentPeriod() {
        return recruitmentPeriod;
    }

    public void setRecruitmentPeriod(String recruitmentPeriod) {
        this.recruitmentPeriod = recruitmentPeriod;
    }
}
