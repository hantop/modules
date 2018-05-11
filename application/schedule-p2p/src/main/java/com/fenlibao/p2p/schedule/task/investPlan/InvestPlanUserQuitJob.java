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
 * @Description: 投资计划 用户申请退出操作
 * @author: kris
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InvestPlanUserQuitJob extends QuartzJobBean{

	private static final Logger logger = LogManager.getLogger(InvestPlanUserQuitJob.class);
	@Resource
	private ZqzrManage zqzrManage;

	private static final int USER_PLAN_NUM=10;//用户计划每次处理个数
	private static final Boolean isAuto = false;//true计划到期退出 false用户申请提前退出

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("(投资计划退出)用户申请退出处理开始 实例ID:"+context.getFireInstanceId());

		List<UserPlan> userPlans = zqzrManage.getUserQuitPlans(USER_PLAN_NUM);
		if(userPlans!=null && userPlans.size()>0){
			for (UserPlan userPlan : userPlans) {
				try {
					//申请转让用户投资计划关联的债权，并生成还款计划
					zqzrManage.doTansferAndRepayPlan(userPlan,isAuto);
				}catch(Throwable e){
					logger.error("(投资计划退出用户申请退出处理异常：userPlan:({}),异常信息:({})",
							JSON.toJSONString(userPlan),e.toString());
					e.printStackTrace();
					continue;
				}
			}
		}
		logger.info("(投资计划退出)用户申请退出处理结束({})",userPlans.size());
	}
}