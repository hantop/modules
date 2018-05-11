package com.fenlibao.service.pms.da.biz.plan.impl;

import com.fenlibao.dao.pms.da.biz.plan.PlanMapper;
import com.fenlibao.model.pms.da.biz.Loan;
import com.fenlibao.model.pms.da.biz.Plan;
import com.fenlibao.model.pms.da.biz.PlanBidsStatus;
import com.fenlibao.model.pms.da.biz.PlanLoan;
import com.fenlibao.model.pms.da.biz.form.LoansForm;
import com.fenlibao.model.pms.da.biz.form.PlanForm;
import com.fenlibao.service.pms.da.biz.plan.PlanService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanMapper planMapper;

    private Map<String, String> statusMap = new HashMap<>();

    @Override
    public Map<String, String> getStatusMap() {
        if (statusMap.size() == 0) {
            statusMap.put("HKZ", "YFK");
            statusMap.put("YJQ", "YFK");
        }
        return statusMap;
    }

    @Override
    public List<Plan> getPlans(PlanForm planForm, RowBounds bounds) {
        List<Plan> plans = planMapper.getPlans(planForm, bounds);
        List<Integer> loanIds = new ArrayList<>();
        for (Plan plan : plans) {
            plan.setStatus(planStatus(plan.getId()));
            loanIds.add(plan.getId());
        }
        return plans;
    }

    private String planStatus(int PlanId) {
        List<PlanBidsStatus> planBidsList = planMapper.getPlanBidsStatus(PlanId);
        String status = "";
        for(PlanBidsStatus planBidsStatus : planBidsList){
            if(planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())>=100){
                status = "TBZ";
                break;
            }else if (planBidsStatus.getStatus().equals("DFK")||(planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())<100)){
                status = "DFK";
            }else if(planBidsStatus.getStatus().equals("HKZ")){
                status = "HKZ";
                break;
            }else {
                status = "YJQ";
            }
        }
        return status;
    }

    @Override
    public Plan getPlan(Integer id) {
        return planMapper.getPlan(id);
    }

    @Override
    public List<Loan> getLoans(LoansForm loansForm, RowBounds bounds) {
        String status = loansForm.getStatus();
        if (StringUtils.isNotBlank(status)) {
            Set<String> statuses = new TreeSet<>();
            if (status.equals("YFK")) {
                statuses.add("HKZ");
                statuses.add("YJQ");
            } else {
                statuses.add(status);
            }
            loansForm.setStatuses(statuses);
        }
        List<Loan> loans = planMapper.getLoans(loansForm, bounds);
        buildStatus(loans);
        return loans;
    }

    /**
     * 设置状态
     *
     * @param loans
     */
    private void buildStatus(List<Loan> loans) {
        String status;
        Map<String, String> statusMap = getStatusMap();
        for (Loan loan : loans) {
            status = loan.getStatus();
            if (statusMap.containsKey(status)) {
                loan.setStatus(statusMap.get(status));
            }
        }
    }
}
