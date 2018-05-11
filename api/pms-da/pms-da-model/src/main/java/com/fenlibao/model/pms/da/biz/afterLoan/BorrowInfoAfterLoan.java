package com.fenlibao.model.pms.da.biz.afterLoan;

import java.util.Date;

/**
 * 借款人贷后信息表
 * Created by Administrator on 2017/11/22.
 */
public class BorrowInfoAfterLoan {
    /**
     * 标id
     */
    private int bidId;
    /**
     *借款资金运用情况
     */
    private String useDetail;
    /**
     *借款人经营状况及财务状况
     */
    private String financeDetail;

    /**
     *借款人还款能力变化情况
     */
    private String repayAbility;
    /**
     *借款人逾期情况
     */
    private String overdueDetail;
    /**
     *借款人涉诉情况
     */
    private String lawsuitDetail;
    /**
     *借款人受行政处罚情况
     */
    private String punishDetail;
    /**
     *创建时间
     */
    private Date createTime;
    /**
     *更新时间
     */
    private Date updateTime;

    /**
     *发布时间
     */
    private int publishTimes;

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
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

    public int getPublishTimes() {
        return publishTimes;
    }

    public void setPublishTimes(int publishTimes) {
        this.publishTimes = publishTimes;
    }
}
