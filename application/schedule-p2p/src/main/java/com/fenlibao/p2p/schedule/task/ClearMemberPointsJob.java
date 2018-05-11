package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.mp.enums.topup.ClearStatus;
import com.fenlibao.p2p.model.mp.vo.PointsClearVO;
import com.fenlibao.p2p.schedule.common.ThreadPoolHelper;
import com.fenlibao.p2p.service.mp.PointsCleanerService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时清理积分
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ClearMemberPointsJob extends QuartzJobBean{
	private static final Logger logger = LogManager.getLogger(ClearMemberPointsJob.class);
    private static final String CLEAR_TIME = "2017-07-01";
	@Resource
	PointsCleanerService pointsCleanerService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("执行定时清理积分任务开始");

		List<PointsClearVO> memberPointList = pointsCleanerService.getUserPiontInfoForClear(100);
		ThreadPoolExecutor executor = ThreadPoolHelper.INSTANCE.getInstance();
		for(PointsClearVO memberPoint:memberPointList){
			try {
				Runnable runner = new ClearMemberPointsJob.Runner(memberPoint);
				executor.submit(runner);
			} catch (Throwable e) {
				logger.info("清除用户[" + memberPoint.getUserId() + "]的过期积分时出现异常",e);
			}
		}
	}

	public static Date getClearDate() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime;
		startTime = DateUtil.getLastTimeOfToday(dateFormat.parse(CLEAR_TIME));
		return startTime;
	}

	public class Runner implements Runnable {
		protected PointsClearVO memberPoint;

		public Runner(PointsClearVO memberPoint) {
			this.memberPoint = memberPoint;
		}

		@Override
		public void run() {
			try {
				Date startTime = getClearDate();
				int holdPoints = pointsCleanerService.getUserHoldPoints(memberPoint.getUserId(), startTime);
				int needClearPoint = memberPoint.getMemberPoints() - holdPoints;
				if (needClearPoint > 0) {
					pointsCleanerService.clearUserPoints(memberPoint, needClearPoint);
					logger.info("成功清除用户[" + memberPoint.getUserId() + "]的过期积分共[" + needClearPoint + "]");
				} else {
					pointsCleanerService.updateMemberPointsStatus(memberPoint.getUserId(), ClearStatus.YES);
				}

			} catch (Exception e) {
				logger.info("清除用户[" + memberPoint.getUserId() + "]的过期积分时出现异常",e);
			}
		}
	}
}

