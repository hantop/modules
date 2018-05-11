package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.service.activity.ActivityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * 摇钱树活动生成分利果
 * Created by xiao on 2017/4/26.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MoneyTreeJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(MoneyTreeJob.class);

    @Resource
    private ActivityService activityService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("执行摇钱树活动生成分利果检查开始");
        try {
            activityService.startCheckAndCreateFruit();
        } catch (Exception e) {
            logger.info("执行摇钱树活动生成分利果检查出错");
            e.printStackTrace();
        }
        logger.info("执行摇钱树活动生成分利果检查结束");
    }
}
