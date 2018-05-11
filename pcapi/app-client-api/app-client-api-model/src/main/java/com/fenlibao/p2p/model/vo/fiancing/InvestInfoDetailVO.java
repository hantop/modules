package com.fenlibao.p2p.model.vo.fiancing;

import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

import java.util.Date;
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

    private int planRecordId;//计划记录id

    private List<BidExtendGroupVO> groupInfoList; //借款自定义信息

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
}
