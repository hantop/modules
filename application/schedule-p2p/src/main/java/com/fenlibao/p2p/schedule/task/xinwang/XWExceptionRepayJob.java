package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.service.xinwang.trade.XWExceptionRepay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/9/20.
 * 用户扣费定时任务
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWExceptionRepayJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWExceptionRepayJob.class);

    @Resource
    XWExceptionRepay exceptionRepay;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("====【..用户扣费开始.】实例ID:"+ jobExecutionContext.getFireInstanceId());
        try {
            exceptionRepay.exceptionRepay();
        } catch (Throwable ex) {
            logger.error("====【..用户扣费出现异常.】实例ID:"+ jobExecutionContext.getFireInstanceId());
        }
    }
}
