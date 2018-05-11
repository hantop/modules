package com.fenlibao.p2p.schedule.task.investPlan;

import com.fenlibao.p2p.service.bid.PlanBidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/16 14:19.
 * @version 1.0
 * 自动发布计划
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReleasePlanJob extends QuartzJobBean {

    private static final Logger LOGGER = LogManager.getLogger(ReleasePlanJob.class);

    @Autowired
    private PlanBidService planBidService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            LOGGER.info("自动发布计划定时器开始执行.......");
            // 查询数据库看设置是否允许自动发布计划
            boolean enableRelease = planBidService.enabledReleasePlan();
            if (enableRelease) {
                planBidService.preparedReleasePlan();
                // 测试购买计划
//                planBidService.doInvestPlan(594, new BigDecimal(298), 9217, null, null);
                LOGGER.info("自动发布计划（发布成功）.......");
            } else {
                LOGGER.info("自动发布计划已被关闭.......");
            }
            LOGGER.info("自动发布计划定时器执行结束.......");
        } catch (Exception e) {
            LOGGER.info("自动发布计划异常：" + e);
        }

    }
}
