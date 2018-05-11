package com.fenlibao.platform.model.thirdparty.entity.bid;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fenlibao.platform.model.thirdparty.enums.RepaymentType;


/**
 * 提供给第三方平台的借款标信息--v_bid_info
 * @author junda.feng
 * @date 2016年5月28日
 * 
 */
public class BidInfoEntity implements Serializable{

    private int projectId;//项目主键,t6230.F01
    private String title;//借款标题,t6230.F03
    private BigDecimal amount;//借款金额,t6230,F05
    private BigDecimal voteAmount;//可投金额,t6230,F07
    private BigDecimal interestRate;//年化利率,t6230.F06
    private String typeName;//借款标类型,参考t6230.F33
    private RepaymentType repaymentType;//还款方式,t6230.F10
    private String province;//借款人所在省份，参考t5019.F06
    private String city;//借款人所在城市，参考t5019.F07
    private int userID;//发标人ID,t6230.F02
    private String amountUsedDesc;//借款用途,t6231.F08
    private Date successTime;//满标的时间,,t6231.F11
    private Date publishTime;//发标时间,t6230.F22
    private int deadline_m;//借款周期（月）,t6230.F09
    private int deadline_d;//借款周期（天）,t6230.F08
    private String state;//TBZ:投标中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:已流标;YDF:已垫付;DFZ:垫付中'
    
    
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getVoteAmount() {
		return voteAmount;
	}
	public void setVoteAmount(BigDecimal voteAmount) {
		this.voteAmount = voteAmount;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public RepaymentType getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(RepaymentType repaymentType) {
		this.repaymentType = repaymentType;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getAmountUsedDesc() {
		return amountUsedDesc;
	}
	public void setAmountUsedDesc(String amountUsedDesc) {
		this.amountUsedDesc = amountUsedDesc;
	}
	public Date getSuccessTime() {
		return successTime;
	}
	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public int getDeadline_m() {
		return deadline_m;
	}
	public void setDeadline_m(int deadline_m) {
		this.deadline_m = deadline_m;
	}
	public int getDeadline_d() {
		return deadline_d;
	}
	public void setDeadline_d(int deadline_d) {
		this.deadline_d = deadline_d;
	}
    
    
    

}
