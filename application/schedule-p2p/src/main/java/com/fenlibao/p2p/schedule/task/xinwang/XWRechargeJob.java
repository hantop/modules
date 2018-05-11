package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
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
 * 新网充值对账
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWRechargeJob extends QuartzJobBean{
	private static final Logger logger = LogManager.getLogger(XWRechargeJob.class);
	@Resource
	private XWRechargeService rechargeService;
	@Resource
	private RedisService redisService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("====【..新网充值对账开始.】实例ID:"+ context.getFireInstanceId());
		Date requestTime = DateUtil.minuteAdd(new Date(), -10);
		List<String> requestNoList = rechargeService.getOrderNeedComfired(XinwangInterfaceName.RECHARGE,requestTime);
		for (String requestNo : requestNoList) {
			try {
				if (!redisService.existsKey(requestNo)) {
					rechargeService.comfiredOrder(requestNo);
				}
			} catch (Exception ex) {
				logger.warn("新网充值对账出现异常，requestNo:[{}],实例ID:[{}],异常信息:[{}]", requestNo,context.getFireInstanceId(),ex);
			} finally {
				redisService.removeKey(requestNo);
			}
		}
	}
}