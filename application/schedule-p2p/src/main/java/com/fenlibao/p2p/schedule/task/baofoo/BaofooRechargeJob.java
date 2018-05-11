package com.fenlibao.p2p.schedule.task.baofoo;

import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooRechargeService;
import com.fenlibao.p2p.service.trade.order.RechargeProcessService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 充值对账
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BaofooRechargeJob extends QuartzJobBean {
	
	private static final Logger logger = LogManager.getLogger(BaofooRechargeJob.class);
	
    @Resource
    private RechargeProcessService rechargeProcessService;
    
    @Resource
    private BaofooRechargeService baofooRechargeService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("宝付充值对账开始。。。  实例ID： " + context.getFireInstanceId());
		try {
            // 查询FKZ所有单
            Date requestTime = DateUtil.minuteAdd(new Date(), -10);
            List<Integer> orderList = rechargeProcessService.getOrderNeedConfirmed(PaymentInstitution.BF.getCode(),requestTime);            logger.info(String.format("有【%s】张充值订单需要对账", orderList.size()));
            if (orderList != null && orderList.size() > 0) {
                for (Integer orderId : orderList) {
                    logger.debug("宝付充值订单号：" + orderId + "开始对账");
                    // 查询结果处理
                    baofooRechargeService.queryResult(orderId);
                    Thread.sleep(1000);
                }
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
	}
	
}
