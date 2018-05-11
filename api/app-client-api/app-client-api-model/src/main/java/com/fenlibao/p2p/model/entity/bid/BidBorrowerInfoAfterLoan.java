package com.fenlibao.p2p.model.entity.bid;

import java.util.Date;

/**
 * @Description: 借款人贷后信息
 */
public class BidBorrowerInfoAfterLoan {

	private Integer bidId;  //借款标ID,参考s62.t6230.F01
	private String useDetail;  //借款资金运用情况
	private String financeDetail;  //借款人经营状况及财务状况
	private String repayAbility;  //借款人还款能力变化情况
	private String overdueDetail;  //借款人逾期情况
	private String lawsuitDetail;  //借款人涉诉情况
	private String punishDetail;  //借款人受行政处罚情况
	private Date createTime;  //创建时间
	private Date updateTime;  //更新时间
	private Integer publishTimes;  //发布期数

	public Integer getBidId() {
		return bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public String getUseDetail() {
		return useDetail;
	}

	public void setUseDetail(String useDetail) {
		this.useDetail = useDetail;
	}

	public String getFinanceDetail() {
		return financeDetail;
	}

	public void setFinanceDetail(String financeDetail) {
		this.financeDetail = financeDetail;
	}

	public String getRepayAbility() {
		return repayAbility;
	}

	public void setRepayAbility(String repayAbility) {
		this.repayAbility = repayAbility;
	}

	public String getOverdueDetail() {
		return overdueDetail;
	}

	public void setOverdueDetail(String overdueDetail) {
		this.overdueDetail = overdueDetail;
	}

	public String getLawsuitDetail() {
		return lawsuitDetail;
	}

	public void setLawsuitDetail(String lawsuitDetail) {
		this.lawsuitDetail = lawsuitDetail;
	}

	public String getPunishDetail() {
		return punishDetail;
	}

	public void setPunishDetail(String punishDetail) {
		this.punishDetail = punishDetail;
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

	public Integer getPublishTimes() {
		return publishTimes;
	}

	public void setPublishTimes(Integer publishTimes) {
		this.publishTimes = publishTimes;
	}
}
