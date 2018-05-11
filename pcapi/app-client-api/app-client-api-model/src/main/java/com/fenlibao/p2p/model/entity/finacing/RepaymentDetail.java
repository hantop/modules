package com.fenlibao.p2p.model.entity.finacing;

/**
 * Created by Administrator on 2016/12/27.
 */
public class RepaymentDetail {

    int pendingRepayment;//待还款期数

    int alreadyRepayment;//已还款期数

    String pendingPrincipal;//待还本金

    String alreadyPrincipal;//已还本金

    String alreadyBreak;//已还违约金

    String pendingAccrual;//待还利息

    String alreadyAccrual;//已还利息

    String alreadyOverdue;//已还逾期利息

    public String getAlreadyAccrual() {
        return alreadyAccrual;
    }

    public void setAlreadyAccrual(String alreadyAccrual) {
        this.alreadyAccrual = alreadyAccrual;
    }

    public String getAlreadyBreak() {
        return alreadyBreak;
    }

    public void setAlreadyBreak(String alreadyBreak) {
        this.alreadyBreak = alreadyBreak;
    }

    public String getAlreadyOverdue() {
        return alreadyOverdue;
    }

    public void setAlreadyOverdue(String alreadyOverdue) {
        this.alreadyOverdue = alreadyOverdue;
    }

    public String getAlreadyPrincipal() {
        return alreadyPrincipal;
    }

    public void setAlreadyPrincipal(String alreadyPrincipal) {
        this.alreadyPrincipal = alreadyPrincipal;
    }



    public String getPendingAccrual() {
        return pendingAccrual;
    }

    public void setPendingAccrual(String pendingAccrual) {
        this.pendingAccrual = pendingAccrual;
    }

    public String getPendingPrincipal() {
        return pendingPrincipal;
    }

    public void setPendingPrincipal(String pendingPrincipal) {
        this.pendingPrincipal = pendingPrincipal;
    }

    public int getAlreadyRepayment() {
        return alreadyRepayment;
    }

    public void setAlreadyRepayment(int alreadyRepayment) {
        this.alreadyRepayment = alreadyRepayment;
    }

    public int getPendingRepayment() {
        return pendingRepayment;
    }

    public void setPendingRepayment(int pendingRepayment) {
        this.pendingRepayment = pendingRepayment;
    }
}
