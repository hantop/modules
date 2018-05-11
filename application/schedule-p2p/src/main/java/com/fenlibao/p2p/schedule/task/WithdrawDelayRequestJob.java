package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooWithdrawService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * 提现延迟请求第三方支付处理
 */

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class WithdrawDelayRequestJob extends QuartzJobBean {

    private static final Logger LOGGER = LogManager.getLogger(WithdrawDelayRequestJob.class);

    @Resource
    BaofooWithdrawService baofooWithdrawService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("定时器WithdrawDelayRequestJob开始提现延迟请求第三方支付处理。当前时间：" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));

        Integer withdrawDelayRequestState = baofooWithdrawService.getTransactionState("withdrawDelayRequestJob");
        if (withdrawDelayRequestState != 1) {
            LOGGER.info("定时器WithdrawDelayRequestJob在事务状态表中处于关闭状态。当前时间：" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            return;
        }

        try {
            baofooWithdrawService.withdrawApplyAfter(100);
        } catch (Exception e) {
            LOGGER.info("定时器开始提现延迟请求第三方支付处异常：" + e.getMessage().toString());
            e.printStackTrace();
        }
    }
}
