package com.fenlibao.p2p.schedule.task.investPlan;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.enums.plan.UserPlanStatusEnum;
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
 * @Description: 投资计划 到期申请退出
 * @author: kris
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InvestPlanExpireJob extends QuartzJobBean{

	private static final Logger logger = LogManager.getLogger(InvestPlanExpireJob.class);
	@Resource
	private ZqzrManage zqzrManage;
	private static final int PLAN_NUM=2;//每次处理计划数量
	private static final Boolean isAuto = true;//true计划到期退出 false用户申请提前退出

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("(投资计划退出)到期转让处理开始 实例ID:"+context.getFireInstanceId());
		List<InvestPlanInfo> planList = zqzrManage.getExpirePlans(PLAN_NUM);
		if (planList != null && planList.size() > 0) {
			for(InvestPlanInfo plan:planList){
				List<UserPlan> userPlans = zqzrManage.getExpireUserPlans(plan.getPlanId(), UserPlanStatusEnum.CYZ.getCode());
				if (userPlans != null && userPlans.size() > 0) {
					for (UserPlan userPlan : userPlans) {
						try {
							//申请转让用户投资计划关联的债权，并生成还款计划
							zqzrManage.doTansferAndRepayPlan(userPlan,isAuto);
						}catch(Throwable e){
							logger.error("(投资计划退出)到期转让处理异常：userPlan:({}),异常信息:({})",
									JSON.toJSONString(userPlan),e.toString());
							e.printStackTrace();
							continue;
						}
					}
				}
			}
		}
		logger.info("(投资计划退出)到期转让处理结束({})",planList.size());
	}
}