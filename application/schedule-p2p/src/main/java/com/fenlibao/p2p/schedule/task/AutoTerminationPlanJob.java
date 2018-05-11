package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.service.bid.TerminationPlanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 自动封计划
 *
 * Created by chenzhixuan on 2017/4/11.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutoTerminationPlanJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(AutoTerminationPlanJob.class);
    @Autowired
    private TerminationPlanService terminationPlanService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("执行自动计划终止开始");
        try {
            terminationPlanService.terminationByLessThanSurplusAmount();
        } catch (Exception e) {
            logger.error("执行自动计划终止异常", e);
        }
        logger.info("执行自动计划终止结束");
    }
}
