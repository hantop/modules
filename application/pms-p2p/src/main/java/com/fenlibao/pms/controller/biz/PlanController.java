package com.fenlibao.pms.controller.biz;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.biz.Loan;
import com.fenlibao.model.pms.da.biz.Plan;
import com.fenlibao.model.pms.da.biz.form.LoansForm;
import com.fenlibao.model.pms.da.biz.form.PlanForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.biz.plan.PlanService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 计划
 * <p>
 * Created by chenzhixuan on 2017/2/7.
 */
@RestController
@RequestMapping("biz/plan")
public class PlanController {
    @Autowired
    private PlanService planService;

    @RequiresPermissions("plan:view")
    @RequestMapping
    public ModelAndView list(PlanForm planForm,
                             @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                             @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        PlanForm _planForm = new PlanForm();
        BeanUtils.copyProperties(planForm, _planForm);
        String releaseStartDateStr = _planForm.getReleaseStartDate();
        String releaseEndDateStr = _planForm.getReleaseEndDate();
        String tenderfullStartDateStr = _planForm.getTenderfullStartDate();
        String tenderfullEndDateStr = _planForm.getTenderfullEndDate();
        if(!StringUtils.isEmpty(releaseStartDateStr)) {
            _planForm.setReleaseStartDate(releaseStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(releaseEndDateStr)) {
            _planForm.setReleaseEndDate(releaseEndDateStr + " 23:59:59");
        }
        if(!StringUtils.isEmpty(tenderfullStartDateStr)) {
            _planForm.setTenderfullStartDate(tenderfullStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(tenderfullEndDateStr)) {
            _planForm.setTenderfullEndDate(tenderfullEndDateStr + " 23:59:59");
        }
        List<Plan> list = planService.getPlans(_planForm, bounds);
        for (Plan plan : list) {
            plan.setRateStr(plan.getRate() == null ? "0" : (plan.getRate().multiply(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            plan.setRaisedRateStr(plan.getRaisedRate() == null ? "0" : (plan.getRaisedRate().multiply(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        PageInfo<Plan> paginator = new PageInfo<>(list);
        return new ModelAndView("biz/plan/index")
                .addObject("plans", list)
                .addObject("paginator", paginator)
                .addObject("planForm", planForm);
    }

    @RequiresPermissions("plan:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void planExport(PlanForm planForm,
                            HttpServletResponse response) {
        RowBounds bounds = new RowBounds();
        PlanForm _planForm = new PlanForm();
        BeanUtils.copyProperties(planForm, _planForm);
        String releaseStartDateStr = _planForm.getReleaseStartDate();
        String releaseEndDateStr = _planForm.getReleaseEndDate();
        String tenderfullStartDateStr = _planForm.getTenderfullStartDate();
        String tenderfullEndDateStr = _planForm.getTenderfullEndDate();
        if(!StringUtils.isEmpty(releaseStartDateStr)) {
            _planForm.setReleaseStartDate(releaseStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(releaseEndDateStr)) {
            _planForm.setReleaseEndDate(releaseEndDateStr + " 23:59:59");
        }
        if(!StringUtils.isEmpty(tenderfullStartDateStr)) {
            _planForm.setTenderfullStartDate(tenderfullStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(tenderfullEndDateStr)) {
            _planForm.setTenderfullEndDate(tenderfullEndDateStr + " 23:59:59");
        }
        List<Plan> list = planService.getPlans(_planForm, bounds);
        List<ExportPlan> exportPlans = new ArrayList<>();
        ExportPlan e;
        for (Plan loan : list) {
            e = new ExportPlan();
            BeanUtils.copyProperties(loan, e);
            e.init();
            exportPlans.add(e);
        }
        String headers[] = {"名称", "金额", "期限", "投资利率", "加息利率", "投资人数", "标的数据(个)", "发布时间", "投满时间", "状态"};
        String fieldNames[] = {"title", "amount", "displayCycle", "displayRate", "displayRaisedRate", "investPeopleNum", "loanNum", "displayCreateTime", "displayTenderfullTime", "displayStatus"};
        POIUtil.export(response, headers, fieldNames, exportPlans, "计划");
    }

    private class ExportPlan {
        private String title;// 计划名称
        private BigDecimal amount;// 计划借款总金额
        private int cycle;// 借款期限
        private String cycleType;// 借款周期类型（按天/按月）
        private BigDecimal rate;// 发标利率
        private String displayRate;// 发标利率
        private BigDecimal raisedRate;// 加息利率
        private String displayRaisedRate;// 加息利率
        private int investPeopleNum;// 投资人数
        private int loanNum;// 标的个数
        private Date createTime;// 创建时间
        private String displayCreateTime;// 创建时间
        private Date tenderfullTime;// 投满时间
        private String displayTenderfullTime;// 投满时间
        private String status;// 状态
        private String displayCycle;
        private String displayStatus;

        public void init() {
            if (cycleType != null) {
                if (cycleType.equals("m"))
                    cycleType = "月";
                else
                    cycleType = "天";
                displayCycle = cycle + cycleType;
            }
            displayRate = rate + "%";
            displayRaisedRate = raisedRate + "%";
            if (status != null) {
                if (status.equals("TBZ")) {
                    displayStatus = "投资中";
                } else if (status.equals("DFK")) {
                    displayStatus = "已满额";
                } else if (status.equals("HKZ")) {
                    displayStatus = "还款中";
                } else if (status.equals("YJQ")) {
                    displayStatus = "已结清";
                }
            }
            if (createTime != null) {
                displayCreateTime = DateUtil.getDateTime(createTime);
            }
            if (tenderfullTime != null) {
                displayTenderfullTime = DateUtil.getDateTime(tenderfullTime);
            }
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public String getCycleType() {
            return cycleType;
        }

        public void setCycleType(String cycleType) {
            this.cycleType = cycleType;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        public String getDisplayRate() {
            return displayRate;
        }

        public void setDisplayRate(String displayRate) {
            this.displayRate = displayRate;
        }

        public BigDecimal getRaisedRate() {
            return raisedRate;
        }

        public void setRaisedRate(BigDecimal raisedRate) {
            this.raisedRate = raisedRate;
        }

        public String getDisplayRaisedRate() {
            return displayRaisedRate;
        }

        public void setDisplayRaisedRate(String displayRaisedRate) {
            this.displayRaisedRate = displayRaisedRate;
        }

        public int getInvestPeopleNum() {
            return investPeopleNum;
        }

        public void setInvestPeopleNum(int investPeopleNum) {
            this.investPeopleNum = investPeopleNum;
        }

        public int getLoanNum() {
            return loanNum;
        }

        public void setLoanNum(int loanNum) {
            this.loanNum = loanNum;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public String getDisplayCreateTime() {
            return displayCreateTime;
        }

        public void setDisplayCreateTime(String displayCreateTime) {
            this.displayCreateTime = displayCreateTime;
        }

        public Date getTenderfullTime() {
            return tenderfullTime;
        }

        public void setTenderfullTime(Date tenderfullTime) {
            this.tenderfullTime = tenderfullTime;
        }

        public String getDisplayTenderfullTime() {
            return displayTenderfullTime;
        }

        public void setDisplayTenderfullTime(String displayTenderfullTime) {
            this.displayTenderfullTime = displayTenderfullTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDisplayCycle() {
            return displayCycle;
        }

        public void setDisplayCycle(String displayCycle) {
            this.displayCycle = displayCycle;
        }

        public String getDisplayStatus() {
            return displayStatus;
        }

        public void setDisplayStatus(String displayStatus) {
            this.displayStatus = displayStatus;
        }
    }

    @RequiresPermissions("plan:view")
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ModelAndView viewByid(@PathVariable("id") final Integer id,
                                 @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                 @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        LoansForm loansForm = new LoansForm() {{
            setPlanId(id);
        }};
        return planLoans(loansForm, page, limit);
    }


    @RequiresPermissions("loans:view")
    @RequestMapping(value = "loans", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView planLoans(LoansForm loansForm,
                                  @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                  @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        ModelAndView mav = new ModelAndView("biz/plan/loans");
        LoansForm _loansForm = new LoansForm();
        BeanUtils.copyProperties(loansForm, _loansForm);
        String releaseStartDateStr = _loansForm.getReleaseStartDate();
        String releaseEndDateStr = _loansForm.getReleaseEndDate();
        String tenderfullStartDateStr = _loansForm.getTenderfullStartDate();
        String tenderfullEndDateStr = _loansForm.getTenderfullEndDate();
        if(!StringUtils.isEmpty(releaseStartDateStr)) {
            _loansForm.setReleaseStartDate(releaseStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(releaseEndDateStr)) {
            _loansForm.setReleaseEndDate(releaseEndDateStr + " 23:59:59");
        }
        if(!StringUtils.isEmpty(tenderfullStartDateStr)) {
            _loansForm.setTenderfullStartDate(tenderfullStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(tenderfullEndDateStr)) {
            _loansForm.setTenderfullEndDate(tenderfullEndDateStr + " 23:59:59");
        }
        List<Loan> loans = planService.getLoans(_loansForm, bounds);
        for (Loan loan : loans) {
            loan.setRateStr(loan.getRate() == null ? "0" : (loan.getRate().multiply(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            loan.setRaisedRateStr(loan.getRaisedRate() == null ? "0" : (loan.getRaisedRate().multiply(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        PageInfo<Loan> paginator = new PageInfo<>(loans);
        return mav
                .addObject("loans", loans)
                .addObject("paginator", paginator)
                .addObject("loansForm", loansForm);
    }

    @RequiresPermissions("loans:export")
    @RequestMapping(value = "loans/export", method = RequestMethod.POST)
    public void loansExport(LoansForm loansForm,
                            HttpServletResponse response) {
        RowBounds bounds = new RowBounds();
        LoansForm _loansForm = new LoansForm();
        BeanUtils.copyProperties(loansForm, _loansForm);
        String releaseStartDateStr = _loansForm.getReleaseStartDate();
        String releaseEndDateStr = _loansForm.getReleaseEndDate();
        String tenderfullStartDateStr = _loansForm.getTenderfullStartDate();
        String tenderfullEndDateStr = _loansForm.getTenderfullEndDate();
        if(!StringUtils.isEmpty(releaseStartDateStr)) {
            _loansForm.setReleaseStartDate(releaseStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(releaseEndDateStr)) {
            _loansForm.setReleaseEndDate(releaseEndDateStr + " 23:59:59");
        }
        if(!StringUtils.isEmpty(tenderfullStartDateStr)) {
            _loansForm.setTenderfullStartDate(tenderfullStartDateStr + " 00:00:00");
        }
        if(!StringUtils.isEmpty(tenderfullEndDateStr)) {
            _loansForm.setTenderfullEndDate(tenderfullEndDateStr + " 23:59:59");
        }
        List<Loan> loans = planService.getLoans(_loansForm, bounds);
        List<ExportLoan> exportLoans = new ArrayList<>();
        ExportLoan e;
        for (Loan loan : loans) {
            e = new ExportLoan();
            BeanUtils.copyProperties(loan, e);
            e.init();
            exportLoans.add(e);
        }
        String headers[] = {"计划名称", "标的名称", "标的金额（元）", "标的期限", "投资利率", "加息利率", "状态"};
        String fieldNames[] = {"planName", "loanName", "amount", "displayCycle", "displayRate", "displayRaisedRate", "displayStatus"};
        POIUtil.export(response, headers, fieldNames, exportLoans, "计划标的");
    }

    private class ExportLoan {
        private String planName;
        private String loanName;
        private BigDecimal amount;// 计划借款总金额
        private int cycle;// 借款期限
        private String cycleType;// 借款周期类型（按天/按月）
        private BigDecimal rate;// 发标利率
        private BigDecimal raisedRate;// 加息利率
        private String status;// 状态
        private String displayCycle;
        private String displayRate;
        private String displayRaisedRate;
        private String displayStatus;

        public void init() {
            getDisplayCycle();
            getDisplayRate();
            getDisplayRaisedRate();
            getDisplayStatus();
        }

        public String getDisplayCycle() {
            if (cycleType != null) {
                if (cycleType.equals("m"))
                    cycleType = "月";
                else
                    cycleType = "天";
                displayCycle = cycle + cycleType;
            }
            return displayCycle = cycle + cycleType;
        }

        public String getDisplayRate() {
            return displayRate = rate + "%";
        }

        public String getDisplayRaisedRate() {
            return displayRaisedRate = raisedRate + "%";
        }

        public String getDisplayStatus() {
            String displayStatus = status;
            Map<String, String> statusMap = planService.getStatusMap();
            if (statusMap.containsKey(displayStatus)) {
                displayStatus = statusMap.get(displayStatus);
            }
            if (displayStatus.equals("TBZ")) {
                displayStatus = "投标中";
            } else if (displayStatus.equals("DFK")) {
                displayStatus = "待放款";
            } else if (displayStatus.equals("YFK")){
                displayStatus = "已放款";
            }
            return this.displayStatus = displayStatus;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getLoanName() {
            return loanName;
        }

        public void setLoanName(String loanName) {
            this.loanName = loanName;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public String getCycleType() {
            return cycleType;
        }

        public void setCycleType(String cycleType) {
            this.cycleType = cycleType;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        public BigDecimal getRaisedRate() {
            return raisedRate;
        }

        public void setRaisedRate(BigDecimal raisedRate) {
            this.raisedRate = raisedRate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setDisplayCycle(String displayCycle) {
            this.displayCycle = displayCycle;
        }

        public void setDisplayRate(String displayRate) {
            this.displayRate = displayRate;
        }

        public void setDisplayRaisedRate(String displayRaisedRate) {
            this.displayRaisedRate = displayRaisedRate;
        }

        public void setDisplayStatus(String displayStatus) {
            this.displayStatus = displayStatus;
        }
    }

}
