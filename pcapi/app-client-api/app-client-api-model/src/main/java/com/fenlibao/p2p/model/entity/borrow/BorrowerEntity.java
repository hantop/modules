package com.fenlibao.p2p.model.entity.borrow;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/11/20.
 */
public class BorrowerEntity {

    private Integer bidId;

    private String bidTitle;//项目名称

    private String loanName;//借款人姓名

    private BigDecimal loanAmount;//借款金额

    public String getBidTitle() {
        return bidTitle;
    }

    public void setBidTitle(String bidTitle) {
        this.bidTitle = bidTitle;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }
}
