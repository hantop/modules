package com.fenlibao.p2p.schedule.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fenlibao.p2p.model.entity.experiencegold.ExperienceGoldEarningsEntity;
import com.fenlibao.p2p.model.form.experiencegold.UserExperienceGoldForm;
import com.fenlibao.p2p.service.experiencegold.IExperienceGoldService;

/**
 * 计算体验金收益
 * @author yangzengcai
 * @date 2015年11月19日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ExperienceEarningsJob extends QuartzJobBean {

	private static final Logger logger = LogManager.getLogger(ExperienceEarningsJob.class);

	@Resource
	private IExperienceGoldService experienceGoldService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("初始化体验金收益开始  实例ID： "+context.getFireInstanceId());
		try {
			int i = 1;
			List<UserExperienceGoldForm> goldList = this.experienceGoldService.getUserExperienceGoldList();
			ExperienceGoldEarningsEntity goldEarnings = null;
			int validDate = 0;
			Date earningsDate = null;
			for (UserExperienceGoldForm g : goldList) {
				try {
					experienceGoldService.initExperienceGoldEarnings(validDate, earningsDate, goldEarnings, g);
					if (i % 1000 == 0) {
						Thread.sleep(5000);
						logger.debug("rest 5s .......");
					}
				} catch (Exception e) {
					logger.error(String.format("expId[%s]体验金初始化失败", g.getExpId()), e);
				}
				i++;
			}
			logger.info("初始化体验金收益 完成。。。。");
		} catch (Exception e) {
			logger.info("初始化体验金收益失败。。。。");
			logger.error(e.toString(), e);
		}
	}

}
