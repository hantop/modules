package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/22.
 */
public class BorrowAccountInfo {

    private BigDecimal borrowTotals;//借款总额

    private BigDecimal borrowerFrozen;//冻结资金

    private BigDecimal balance ;//借款账户余额

    private BigDecimal factorage;//提现费用

    private Integer isBorrower;

    private Integer isBorrowBinkCards;

    private List<String> borrowBankList;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBorrowerFrozen() {
        return borrowerFrozen;
    }

    public void setBorrowerFrozen(BigDecimal borrowerFrozen) {
        this.borrowerFrozen = borrowerFrozen;
    }

    public BigDecimal getBorrowTotals() {
        return borrowTotals;
    }

    public void setBorrowTotals(BigDecimal borrowTotals) {
        this.borrowTotals = borrowTotals;
    }

    public BigDecimal getFactorage() {
        return factorage;
    }

    public void setFactorage(BigDecimal factorage) {
        this.factorage = factorage;
    }

    public List<String> getBorrowBankList() {
        return borrowBankList;
    }

    public void setBorrowBankList(List<String> borrowBankList) {
        this.borrowBankList = borrowBankList;
    }

    public Integer getIsBorrowBinkCards() {
        return isBorrowBinkCards;
    }

    public void setIsBorrowBinkCards(Integer isBorrowBinkCards) {
        this.isBorrowBinkCards = isBorrowBinkCards;
    }

    public Integer getIsBorrower() {
        return isBorrower;
    }

    public void setIsBorrower(Integer isBorrower) {
        this.isBorrower = isBorrower;
    }
}
