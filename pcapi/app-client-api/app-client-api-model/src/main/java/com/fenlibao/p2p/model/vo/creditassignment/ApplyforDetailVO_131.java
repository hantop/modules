package com.fenlibao.p2p.model.vo.creditassignment;

import java.io.Serializable;
import java.util.List;

import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

/**
 * 债权转让申请详情
 */
public class ApplyforDetailVO_131 implements Serializable {

	private static final long serialVersionUID = 1L;

	private int applyforId;//申请债权转让ID
	
	private int bidId;//标id
	
	private String zqTitle;//债权名称
	
	private double transferValue;//购买的价格(转出时的价格)
	
	private String zqYield;//收益率
	
	private int zqTime;//债权剩余天数(单位:天)
	
	private double zqSum;//债权金额(原本的价值)
	
	private double expectEarning;//预期收益
	
	private long buyTimestamp;//购买时间戳
	
	private long interestTimestamp;//计息时间戳
	
	private long endTimestamp;//到期时间戳
	
	private String[] assetTypes;//资产类型
	
	private String borrowerUrl;//借款人信息url
	
	private String[] lawFiles;//法律文书页面链接
	
	private String lawFileUrl;//法律文书url
	
	private String assignmentAgreement;//债权转让及受让协议url

	private String remark;//项目描述
	
	private List<BidExtendGroupVO> groupInfoList;

	public int getApplyforId() {
		return applyforId;
	}

	public void setApplyforId(int applyforId) {
		this.applyforId = applyforId;
	}

	public String getZqTitle() {
		return zqTitle;
	}

	public void setZqTitle(String zqTitle) {
		this.zqTitle = zqTitle;
	}

	public double getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(double transferValue) {
		this.transferValue = transferValue;
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

	public double getZqSum() {
		return zqSum;
	}

	public void setZqSum(double zqSum) {
		this.zqSum = zqSum;
	}

	public double getExpectEarning() {
		return expectEarning;
	}

	public void setExpectEarning(double expectEarning) {
		this.expectEarning = expectEarning;
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

	public String getAssignmentAgreement() {
		return assignmentAgreement;
	}

	public void setAssignmentAgreement(String assignmentAgreement) {
		this.assignmentAgreement = assignmentAgreement;
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
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

	public String getBorrowerUrl() {
		return borrowerUrl;
	}

	public void setBorrowerUrl(String borrowerUrl) {
		this.borrowerUrl = borrowerUrl;
	}

	public String[] getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(String[] assetTypes) {
		this.assetTypes = assetTypes;
	}

	
}
