package com.fenlibao.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

/**
 * 债权
 */
public class FinacingDetailVO_131 implements Serializable{

	private static final long serialVersionUID = 1L;

	private int zqId;//债权ID
	
	private int bidId;//标
	
	private String zqTitle;//债权名称
	
	private double zqSum;//投资金额
	
	private double originalMoney;//原始债权金额
	
	private String zqYield;//收益率
	
	private int zqTime;//债权期限
	
	private double zqEarning;//所有收益（预计）
	
	private int zqStatus;//债权状态(0:收益中  1:转让中)
	
	private long buyTimestamp;//购买时间  申购时间戳
		
	private long interestTimestamp;//计息时间戳
	
	private long endTimestamp;//到期时间戳
	
	private int isNoviceBid;//是否是新手标 (1:是新手标;0:普通开店宝标)
	
	private int loanDays;//借款周期（单位：天）
	
	public String serviceArgeementUrl; //服务协议
	
	private double passedEarning;//过去天数的收益
	
	private int surplusDays;//债权剩余天数
	
	private String borrowerUrl;//借款人信息url
	
	private String[] lawFiles;//法律文书页面链接
	
	private String lawFileUrl;//法律文书url
	
	private String remark;//项目描述
	
	private List<BidExtendGroupVO> groupInfoList;
	
	public double getOriginalMoney() {
		return originalMoney;
	}

	public void setOriginalMoney(double originalMoney) {
		this.originalMoney = originalMoney;
	}

	public String getZqTitle() {
		return zqTitle;
	}

	public void setZqTitle(String zqTitle) {
		this.zqTitle = zqTitle;
	}

	public double getZqSum() {
		return zqSum;
	}

	public void setZqSum(double zqSum) {
		this.zqSum = zqSum;
	}

	public String getZqYield() {
		return zqYield;
	}

	public void setZqYield(String zqYield) {
		this.zqYield = zqYield;
	}

	public int getZqTime() {
		return zqTime;
	}

	public void setZqTime(int zqTime) {
		this.zqTime = zqTime;
	}

	public int getZqStatus() {
		return zqStatus;
	}

	public void setZqStatus(int zqStatus) {
		this.zqStatus = zqStatus;
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

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public double getZqEarning() {
		return zqEarning;
	}

	public void setZqEarning(double zqEarning) {
		this.zqEarning = zqEarning;
	}

	/**
	 * @return the isNoviceBid
	 */
	public int getIsNoviceBid() {
		return isNoviceBid;
	}

	/**
	 * @param isNoviceBid the isNoviceBid to set
	 */
	public void setIsNoviceBid(int isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	/**
	 * @return the loanDays
	 */
	public int getLoanDays() {
		return loanDays;
	}

	/**
	 * @param loanDays the loanDays to set
	 */
	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public int getZqId() {
		return zqId;
	}

	public void setZqId(int zqId) {
		this.zqId = zqId;
	}

	public String getBorrowerUrl() {
		return borrowerUrl;
	}

	public void setBorrowerUrl(String borrowerUrl) {
		this.borrowerUrl = borrowerUrl;
	}

	public String[] getLawFiles() {
		return lawFiles;
	}

	public void setLawFiles(String[] lawFiles) {
		this.lawFiles = lawFiles;
	}

	public String getLawFileUrl() {
		return lawFileUrl;
	}

	public void setLawFileUrl(String lawFileUrl) {
		this.lawFileUrl = lawFileUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<BidExtendGroupVO> getGroupInfoList() {
		return groupInfoList;
	}

	public void setGroupInfoList(List<BidExtendGroupVO> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public String getServiceArgeementUrl() {
		return serviceArgeementUrl;
	}

	public void setServiceArgeementUrl(String serviceArgeementUrl) {
		this.serviceArgeementUrl = serviceArgeementUrl;
	}

	public double getPassedEarning() {
		return passedEarning;
	}

	public void setPassedEarning(double passedEarning) {
		this.passedEarning = passedEarning;
	}

	public int getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(int surplusDays) {
		this.surplusDays = surplusDays;
	}
	
}
