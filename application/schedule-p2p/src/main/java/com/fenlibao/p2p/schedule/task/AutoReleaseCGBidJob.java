package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.service.bid.AutoReleaseCGBidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * 将录入的存管消费信贷标调用存管接口进行发布
 * Created by xiao on 2017/7/8.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutoReleaseCGBidJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(AutoReleaseCGBidJob.class);

    @Resource
    AutoReleaseCGBidService autoReleaseCGBidService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("执行存管消费信贷标自动发布任务开始");
        try {
            autoReleaseCGBidService.startAutoReleaseCGBid();
        } catch (Throwable e) {
            logger.info("执行存管消费信贷标自动发布任务出错");
            e.printStackTrace();
        }
        logger.info("执行存管消费信贷标自动发布任务结束");
    }
}
