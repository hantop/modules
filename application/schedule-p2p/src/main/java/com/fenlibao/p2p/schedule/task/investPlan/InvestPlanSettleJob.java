package com.fenlibao.p2p.schedule.task.investPlan;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.service.creditassignment.ZqzrManage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 投资计划 退出结清
 * @author: kris
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InvestPlanSettleJob extends QuartzJobBean{

	private static final Logger logger = LogManager.getLogger(InvestPlanSettleJob.class);
	@Resource
	private ZqzrManage zqzrManage;
//	private static final int USER_PLAN_NUM = 1000;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("(投资计划结清)处理开始 实例ID:"+context.getFireInstanceId());
		List<UserPlan> userPlans = zqzrManage.getUserPlansInQuit(null);
		if(userPlans!=null && userPlans.size()>0){
			for(UserPlan userPlan : userPlans){
				try {
					zqzrManage.doSettlementPlan(userPlan);
				}catch(Throwable e){
					logger.error("(投资计划结清)处理异常：userPlan:({}),异常信息:({})", JSON.toJSONString(userPlan),e.toString());
					e.printStackTrace();
					continue;
				}
			}
		}
		logger.info("(投资计划结清)结清处理结束({})",userPlans.size());
	}
}