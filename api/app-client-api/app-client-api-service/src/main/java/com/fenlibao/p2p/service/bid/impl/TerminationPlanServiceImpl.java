package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.EnumDao;
import com.fenlibao.p2p.model.entity.TEnum;
import com.fenlibao.p2p.model.global.InvestPlanStatus;
import com.fenlibao.p2p.service.bid.PlanBidService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.bid.TerminationPlanService;
import com.fenlibao.p2p.service.pms.user.PmsUserService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
@Service
public class TerminationPlanServiceImpl implements TerminationPlanService {

    private static final Logger LOGGER = LogManager.getLogger(TerminationPlanServiceImpl.class);

    @Autowired
    private PlanService planService;
    @Autowired
    private PmsUserService pmsUserService;
    @Autowired
    private PlanBidService planBidService;
    @Autowired
    private EnumDao enumDao;

//    @Transactional
    @Override
    public void terminationByLessThanSurplusAmount() throws Exception {
        // PMS用户ID
        int pmsUserId = getPmsUserId();
        int lessThanSurplusAmount = _getLessThanSurplusAmount();
        // 扫描可投金额小于lessThanSurplusAmount或当前时间已到筹款期限的计划 modify by zeronx 2017-11-4 remove "for update"
        List<Integer> planIds = planService.findPlanIds(lessThanSurplusAmount, DateUtil.nowDate(), InvestPlanStatus.INVESTING.getKey());
        for (Integer planId : planIds) {
            try {
                planBidService.terminationPlan(planId, pmsUserId);
                // 计划终止操作用户为admin
//                planService.termination(planId, pmsUserId);
            } catch (Exception e) {
                LOGGER.error("自动封计划planId ：【{}】异常:{}", planId, e);
            }
        }
    }

    public int getPmsUserId() {
        String pmsUsername = "admin";
        TEnum enumOperator = enumDao.getEnum("t_plan_termination", "operater_id", "operater_username");
        if(enumOperator != null && StringUtils.isNotBlank(enumOperator.getEnumValue())) {
            pmsUsername = enumOperator.getEnumValue();
        }
        return pmsUserService.getUserId(pmsUsername);
    }

    private int _getLessThanSurplusAmount() {
        int lessThanSurplusAmount = 100;
        // 获取字典项
        TEnum enumLessThan = enumDao.getEnum("t_invest_plan", "surplus_amount", "lessthan");
        if (enumLessThan != null && StringUtils.isNotBlank(enumLessThan.getEnumValue())) {
            lessThanSurplusAmount = Integer.valueOf(enumLessThan.getEnumValue());
        }
        return lessThanSurplusAmount;
    }
}
