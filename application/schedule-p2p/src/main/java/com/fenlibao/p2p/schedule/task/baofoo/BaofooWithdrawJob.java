package com.fenlibao.p2p.schedule.task.baofoo;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooWithdrawService;
import com.fenlibao.p2p.service.trade.order.WithdrawProcessService;

/**
 * 提现对账
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BaofooWithdrawJob extends QuartzJobBean {
	
	private static final Logger logger = LogManager.getLogger(BaofooWithdrawJob.class);
	
    @Resource
    private WithdrawProcessService withdrawProcessService;
    
    @Resource
    private BaofooWithdrawService baofooWithdrawService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("宝付提现对账开始。。。  实例ID： " + context.getFireInstanceId());

        Integer withdrawDelayRequestState = baofooWithdrawService.getTransactionState("withdrawDelayRequest");
        if(withdrawDelayRequestState == 1){
            logger.info("在事务状态表中withdrawDelayRequest处于开启状态，BaofooWithdrawJob需要关闭。当前时间：" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            return;
        }

		try {
            // 查询FKZ所有单
            List<Integer> orderList = withdrawProcessService.getOrderNeedConfirmed(PaymentInstitution.BF.getCode());
            logger.info(String.format("有【%s】张提现订单需要对账", orderList.size()));
            if (orderList != null && orderList.size() > 0) {
                for (Integer orderId : orderList) {
                    logger.debug("宝付提现订单号：" + orderId + "开始对账");
                    // 查询结果处理
                    try {
                        baofooWithdrawService.withdrawResultQuery(orderId);
                        Thread.sleep(1000);
                    } catch(Exception e){
                        logger.error(e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
	}
	
}
