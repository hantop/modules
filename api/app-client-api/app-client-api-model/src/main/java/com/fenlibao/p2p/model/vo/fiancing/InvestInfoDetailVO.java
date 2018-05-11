package com.fenlibao.p2p.model.vo.fiancing;

import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

import java.util.List;

/**
 * 投资信息详情VO
 * Created by laubrence on 2016-3-28 .
 */
public class InvestInfoDetailVO extends InvestInfoVO{

    Long investTime;  /*投资时间*/

    Long interestTime;/*计息时间*/

    Long expireTime; /*到期时间*/

    private String borrowerUrl;//借款人信息url

    private String[] lawFiles;//法律文书页面链接

    private String lawFileUrl;//法律文书url

    private String remark;//项目描述

    private List<BidExtendGroupVO> groupInfoList; //借款自定义信息
    
    //2016-06-28 junda.feng
    private String repaymentMode;//还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清) 
    
	private String interestPaymentType;// 付息方式,ZRY:自然月;GDR:固定日;
	
	private Long applyTime;//债权转让申请时间
	
	private Long successTime;//转让成功日
	
	private Long actualRepaymentDate;//到期回款日
    private String interestRiseAmount;//加息奖励

    private String fwxyUrl;//服务协议

    private String guaranteeFileUrl;//担保借款合同

    private int planRecordId;//计划记录id

    private double interestRise;//加息券加息利率
    private double bidInterestRise;//加息标加息利率
    private int anytimeQuit;//随时退出标：1是、0否

    private int holdDays;//持有天数

    private double presentRate;//当前月利率

    private String lowRate;//最低年化利率

    private String highRate;//最高年化利率

    private int currentMonth;//当前月份

    private String bonusRate;//月增幅利率
    private String agreementUrl;  //上上签担保合同


    public String getLowRate() {
        return lowRate;
    }

    public void setLowRate(String lowRate) {
        this.lowRate = lowRate;
    }

    public String getHighRate() {
        return highRate;
    }

    public void setHighRate(String highRate) {
        this.highRate = highRate;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(String bonusRate) {
        this.bonusRate = bonusRate;
    }

    public String getGuaranteeFileUrl() {
        return guaranteeFileUrl;
    }

    public void setGuaranteeFileUrl(String guaranteeFileUrl) {
        this.guaranteeFileUrl = guaranteeFileUrl;
    }

    public String getFwxyUrl() {
        return fwxyUrl;
    }

    public void setFwxyUrl(String fwxyUrl) {
        this.fwxyUrl = fwxyUrl;
    }

    public String getInterestRiseAmount() {
        return interestRiseAmount;
    }

    public void setInterestRiseAmount(String interestRiseAmount) {
        this.interestRiseAmount = interestRiseAmount;
    }

    public Long getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Long successTime) {
		this.successTime = successTime;
	}

	public Long getActualRepaymentDate() {
		return actualRepaymentDate;
	}

	public void setActualRepaymentDate(Long actualRepaymentDate) {
		this.actualRepaymentDate = actualRepaymentDate;
	}

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public String getInterestPaymentType() {
		return interestPaymentType;
	}

	public void setInterestPaymentType(String interestPaymentType) {
		this.interestPaymentType = interestPaymentType;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Long interestTime) {
        this.interestTime = interestTime;
    }

    public Long getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Long investTime) {
        this.investTime = investTime;
    }

    public String getBorrowerUrl() {
        return borrowerUrl;
    }

    public void setBorrowerUrl(String borrowerUrl) {
        this.borrowerUrl = borrowerUrl;
    }

    public List<BidExtendGroupVO> getGroupInfoList() {
        return groupInfoList;
    }

    public void setGroupInfoList(List<BidExtendGroupVO> groupInfoList) {
        this.groupInfoList = groupInfoList;
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

    public int getPlanRecordId() {
        return planRecordId;
    }

    public void setPlanRecordId(int planRecordId) {
        this.planRecordId = planRecordId;
    }

    @Override
    public double getInterestRise() {
        return interestRise;
    }

    @Override
    public void setInterestRise(double interestRise) {
        this.interestRise = interestRise;
    }

    @Override
    public double getBidInterestRise() {
        return bidInterestRise;
    }

    @Override
    public void setBidInterestRise(double bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    @Override
    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    @Override
    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public int getHoldDays() {
        return holdDays;
    }

    public void setHoldDays(int holdDays) {
        this.holdDays = holdDays;
    }

    public double getPresentRate() {
        return presentRate;
    }

    public void setPresentRate(double presentRate) {
        this.presentRate = presentRate;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }
}
