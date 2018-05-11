package com.fenlibao.p2p.schedule.task;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.form.experiencegold.UserExperienceGoldForm;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.experiencegold.IExperienceGoldService;
import com.fenlibao.p2p.util.loader.Config;

/**
 * 体验金到期将收益转给用户
 * @author yangzengcai
 * @date 2015年11月20日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ExperienceEndJob extends QuartzJobBean {

	private static final Logger logger = LogManager.getLogger(ExperienceEndJob.class);

	@Resource
	private IExperienceGoldService experienceGoldService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("体验金到期将收益转给用户  实例ID： "+context.getFireInstanceId());
		try {
			int i = 1;
			List<UserExperienceGoldForm> goldList = this.experienceGoldService.getExpireUserExperienceGold();
			logger.info(String.format("正在处理用户体验金收益[%s]", goldList.size()));
			BigDecimal totalEarnings = BigDecimal.ZERO;
			FeeType feeType = new FeeType(7014, "体验金收益");
			String accountType = InterfaceConst.ACCOUNT_TYPE_WLZH;
			String platformAccount = Config.get("redpacket.grantAccount"); //和红包使用同一个账号
			String content = "尊敬的用户：您好！您的体验金收益%s元已转入账户余额，请查收。感谢您对我们的关注与支持";
			for (UserExperienceGoldForm g : goldList) {
				try {
					experienceGoldService.endExperience(feeType, accountType, platformAccount, g, totalEarnings, content);
					if (i % 200 == 0) {
						Thread.sleep(3000);
						logger.debug("rest 3s ....");
					}
				} catch (Exception e) {
					logger.error(String.format("expId[%s]体验金收益结算失败", g.getExpId()), e);
				}
				i++;
			}
			logger.info("处理用户体验金收益完成。。。");
		} catch (Exception e) {
			logger.info("体验金到期将收益转给用户失败。。。。");
			logger.error(e.toString(), e);
		}
	}

}
