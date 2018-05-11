package com.fenlibao.dao.pms.da.biz.plan;

import com.fenlibao.model.pms.da.biz.Loan;
import com.fenlibao.model.pms.da.biz.Plan;
import com.fenlibao.model.pms.da.biz.PlanBidsStatus;
import com.fenlibao.model.pms.da.biz.PlanLoan;
import com.fenlibao.model.pms.da.biz.form.LoansForm;
import com.fenlibao.model.pms.da.biz.form.PlanForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 计划
 *
 * Created by chenzhixuan on 2017/2/7.
 */
public interface PlanMapper {

    List<PlanLoan> listPlanLoan(@Param("loanIds") List<Integer> loanIds);

    List<Plan> getPlans(PlanForm planForm, RowBounds bounds);

    Plan getPlan(Integer id);

    List<Loan> getLoans(LoansForm loansForm, RowBounds bounds);

    List<PlanBidsStatus> getPlanBidsStatus(int planId);
}
