package com.fenlibao.service.pms.da.biz.plan;

import com.fenlibao.model.pms.da.biz.Loan;
import com.fenlibao.model.pms.da.biz.Plan;
import com.fenlibao.model.pms.da.biz.form.LoansForm;
import com.fenlibao.model.pms.da.biz.form.PlanForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 计划
 *
 * Created by chenzhixuan on 2017/2/7.
 */
public interface PlanService {

    Map<String, String> getStatusMap();

    /**
     * 列表
     * @param planForm
     * @param bounds
     * @return
     */
    List<Plan> getPlans(PlanForm planForm, RowBounds bounds);

    /**
     * 根据ID获取计划
     * @param id
     * @return
     */
    Plan getPlan(Integer id);

    List<Loan> getLoans(LoansForm loansForm, RowBounds bounds);
}
