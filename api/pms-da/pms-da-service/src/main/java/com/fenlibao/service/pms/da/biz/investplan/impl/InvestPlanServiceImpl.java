package com.fenlibao.service.pms.da.biz.investplan.impl;

import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.dao.pms.da.biz.investplan.InvestPlanMapper;
import com.fenlibao.dao.pms.da.biz.plan.PlanInfoMapper;
import com.fenlibao.model.pms.da.biz.plan.UserPlan;
import com.fenlibao.model.pms.da.global.InvestPlanStatus;
import com.fenlibao.model.pms.da.planCenter.InvestPlan;
import com.fenlibao.service.pms.da.biz.investplan.InvestPlanService;
import com.fenlibao.service.pms.da.biz.plan.UserPlanRepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InvestPlanServiceImpl implements InvestPlanService {
    @Autowired
    private InvestPlanMapper investPlanMapper;
    @Autowired
    private UserPlanRepayService userPlanRepayService;
    @Autowired
    private PlanInfoMapper planInfoMapper;

    @Override
    public void cancelRecommend(int planId) {
        // 修改计划置顶时间
        investPlanMapper.updateRecommendTime(planId, false);
    }

    @Override
    public void recommend(int planId) {
        // 修改计划推荐置顶时间
        investPlanMapper.updateRecommendTime(planId, true);
    }

    @Transactional
    @Override
    public void audit(int planId, boolean isPass) {
        int status = isPass ? InvestPlanStatus.AWAIT_RELEASE.getKey() : InvestPlanStatus.AWAIT_SUBMIT.getKey();
        investPlanMapper.updatePlanAuditStatus(planId, status);
    }

    @Transactional
    @Override
    public void invalid(int planId) {
        investPlanMapper.updatePlanInvalidStatus(planId, InvestPlanStatus.INVALID.getKey());
    }

    @Transactional
    @Override
    public void release(int planId) {
        investPlanMapper.updatePlanReleaseStatus(planId, InvestPlanStatus.INVESTING.getKey());
    }

    /**
     * 起息日期
     *
     **/
    private Date buildBearrateDate() {
        Calendar calendar = Calendar.getInstance();
        // 起息日期
        calendar.setTime(DateUtil.nowDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    @Transactional
    @Override
    public void timingRelease(int planId, Date relaseTime, Date displayTime) {
        investPlanMapper.updatePlanTimingRelease(planId, relaseTime, displayTime, InvestPlanStatus.ADVANCE_RELEASE.getKey());
    }

    @Transactional
    @Override
    public void termination(int planId, int operatorId) throws Exception {
        // 计算到期时间
        Date expireTime = buildExpireTime(planId);
        // 新增计划_终止记录
        investPlanMapper.insertPlanTermination(planId, operatorId);
        //用户投资计划记录
        List<UserPlan> userPlanList= planInfoMapper.getUserPlansByPlanId(planId);
        if(userPlanList==null||userPlanList.isEmpty()){
            // 没有用户投资改为已结清
            investPlanMapper.updatePlanTermination(planId, expireTime, null, null, InvestPlanStatus.FLOWED.getKey());
        } else {
            // 起息日期
            Date bearrateDate = buildBearrateDate();
            // 有用户投资改为还款中
            investPlanMapper.updatePlanTermination(planId, expireTime, bearrateDate, DateUtil.nowDate(), InvestPlanStatus.REFUNDING.getKey());
            // 创建计划预期还款计划,计划改状态为还款中时调用
            userPlanRepayService.createExpectedRepayPlan(planId);
        }
    }

    @Transactional
    @Override
    public void sticktop(int planId) {
        // 修改计划置顶时间
        investPlanMapper.updateSticktopTime(planId, true);
    }

    @Override
    public void cancelSticktop(int planId) {
        // 修改计划置顶时间
        investPlanMapper.updateSticktopTime(planId, false);
    }

    @Override
    public void cancelTimingRelease(int id, int status) {
        investPlanMapper.cancelTimingRelease(id, status);
    }

    /**
     * 计算到期时间
     * @return
     * @param planId
     */
    private Date buildExpireTime(int planId) {
        InvestPlan investPlan = investPlanMapper.getInvestPlan(planId);
        final Date interestDate = DateUtil.nowDate();
        final Date endDate;
        if(investPlan.getCycleType().equals("d")) {
            endDate = DateUtil.dateAdd(interestDate, investPlan.getCycle());
        } else {
            endDate = DateUtil.monthAdd(interestDate, investPlan.getCycle());
        }
        // 转换为无时间的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
