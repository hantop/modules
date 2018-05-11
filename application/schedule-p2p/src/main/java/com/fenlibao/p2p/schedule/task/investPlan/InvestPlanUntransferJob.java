package com.fenlibao.p2p.schedule.task.investPlan;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.creditassignment.CreditAssigmentDao;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.service.creditassignment.ZqzrManage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 计划退出，上架债权
 * @author: kris
 * modify by zeronx 2017-09-11 22:02 添加下面这个 （功能只上架非存管的）
 * app-client-api\app-client-api-dao\src\main\resources\mapper\fenlibao\CreditassignmentMapper.xml
 * 在 CreditassignmentMapper.xml 添加
 * INNER JOIN flb.t_invest_plan ON  flb.t_invest_plan.id = t_user_plan.plan_id  AND t_invest_plan.is_cg = 1
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InvestPlanUntransferJob extends QuartzJobBean{

	private static final Logger logger = LogManager.getLogger(InvestPlanUntransferJob.class);
	@Autowired
	private CreditAssigmentDao creditAssigmentDao;
	@Resource
	private ZqzrManage zqzrManage;
	private static final int PRODUCT_NUM=500;//每次处理债权数量-为了方便维护直接在sql里面修改

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("(投资计划退出)未放款导致没上架的债权处理开始 实例ID:"+context.getFireInstanceId());
		List<UserPlanProduct> UserPlanProducts = creditAssigmentDao.getUserPlanProductsNeedTransfer(PRODUCT_NUM);
		if (UserPlanProducts != null && UserPlanProducts.size() > 0) {
			for(UserPlanProduct userPlanProduct:UserPlanProducts){
				try {
					zqzrManage.planProductTransfer(userPlanProduct.getUserId(),userPlanProduct.getProductId());
				} catch (Throwable e) {
					logger.error("(投资计划)未放款导致没上架的债权处理异常：userPlanProduct:({}),异常信息:({})",JSON.toJSONString(userPlanProduct),e.toString());
					e.printStackTrace();
					continue;
				}
			}
		}
		logger.info("(投资计划退出)未放款导致没上架的债权处理结束({})",UserPlanProducts.size());
	}
}