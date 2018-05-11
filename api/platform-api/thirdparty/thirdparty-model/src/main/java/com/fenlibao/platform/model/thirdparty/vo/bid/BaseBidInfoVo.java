package com.fenlibao.platform.model.thirdparty.vo.bid;

import java.math.BigDecimal;

import org.aeonbits.owner.ConfigFactory;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;


/**
 * 借款标信息--通用字段
 * @author junda.feng
 * @date 2016年5月30日
 * 
 */
public class BaseBidInfoVo<M> {
	//消费信贷标用户id特殊处理 消费信贷标201611080633
	public static final String XFXDB="消费信贷标";

    private String projectId;//项目主键
    private String title;//借款标题
    private String loanUrl;//标的详细页面地址链接
    private String userName;//发标人ID
    private Double amount;//借款金额
    private String schedule;//进度
    private String interestRate;//年化利率
    private Integer deadline;//借款期限
    private String deadlineUnit;//期限单位
    private Double reward;//奖励 0
    private String type;//借款标类型
    
    private static Config config = ConfigFactory.create(Config.class);
    
    @SuppressWarnings("hiding")
	protected  <M> BaseBidInfoVo(BidInfoEntity bid) throws Throwable{
    	this.projectId=String.valueOf(bid.getProjectId());
    	this.title=bid.getTitle();
		//消费信贷 uid 随机数录入
//		if(title.length()>16 && title.substring(0,5).equals(BaseBidInfoVo.XFXDB)){
//			String uid=title.substring(7,title.length());
//			this.userName=StringHelper.encode(uid);
//		}else{
		//造成很多用户借款过多，直接用标id作为用户
		//this.userName=StringHelper.encode(StringHelper.deviation(bid.getUserID()));
		this.userName=StringHelper.encode(StringHelper.deviation(bid.getProjectId()));
//		}
    	this.loanUrl=config.getBidDetail()+bid.getProjectId();
    	this.amount=bid.getAmount().doubleValue();
    	BigDecimal amount=bid.getAmount();
    	BigDecimal voteAmount=bid.getVoteAmount();
    	BigDecimal oh=new BigDecimal(100);
		this.schedule=voteAmount.compareTo(oh)<0?"100":String.valueOf(amount.subtract(voteAmount).divide(bid.getAmount(),10,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).intValue());
		this.interestRate=String.valueOf(bid.getInterestRate().multiply(new BigDecimal(100)).floatValue());
    	this.deadline=bid.getDeadline_d()==0?bid.getDeadline_m():bid.getDeadline_d();
    	this.deadlineUnit=bid.getDeadline_d()==0?"月":"天";
    	this.reward=0d;
    	this.type=bid.getTypeName()==null?"":bid.getTypeName().replace(",", "+");
    }


	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLoanUrl() {
		return loanUrl;
	}
	public void setLoanUrl(String loanUrl) {
		this.loanUrl = loanUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	public int getDeadline() {
		return deadline;
	}
	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}
	public String getDeadlineUnit() {
		return deadlineUnit;
	}
	public void setDeadlineUnit(String deadlineUnit) {
		this.deadlineUnit = deadlineUnit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}

	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}
    

}
