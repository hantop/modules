package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.service.recharge.IRechargeService;
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
 *
 * @author yangzengcai
 * @date 2015年9月1日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RechargeJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(RechargeJob.class);

    @Resource
    private IRechargeService rechargeService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("充值对账开始。。。  实例ID： " + context.getFireInstanceId());
		try {
            Date requestTime = DateUtil.minuteAdd(new Date(), -10);
            List<RechargeOrder> orderList = rechargeService.getDQROrder(requestTime);
            if (orderList != null) {
            	logger.info(String.format("有【%s】张状态为“待确认”的充值订单将进行连连支付结果查询检验", orderList.size()));
            	rechargeService.queryResult(orderList);
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
	}

}
