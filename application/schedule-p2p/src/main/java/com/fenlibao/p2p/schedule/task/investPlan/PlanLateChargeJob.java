package com.fenlibao.p2p.schedule.task.investPlan;

import com.fenlibao.p2p.service.plan.PlanLateChargeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * 计算计划逾期罚息
 * modify by zeronx 2017-09-12 10:21 修改存管版的计划没有逾期一说，所以在sql过滤，添加下面sql语句
 * AND c.is_cg = 1
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PlanLateChargeJob extends QuartzJobBean {
	
	private static final Logger logger = LogManager.getLogger(PlanLateChargeJob.class);
	
    @Resource
    private PlanLateChargeService planLateChargeService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			planLateChargeService.planLateCharge();
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
	}
	
}
