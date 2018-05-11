package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.schedule.common.ThreadPoolHelper;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 检查标的放款是否结束。。。
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWMakeLoanFinishRerunJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWMakeLoanFinishRerunJob.class);

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWMakeLoanService makeLoanService;

    @Resource
    XWProjectDao projectDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("检查标的放款是否结束。。。  实例ID： " + context.getFireInstanceId());
        ThreadPoolExecutor executor = ThreadPoolHelper.INSTANCE.getInstance();
        //检查标的放款是否结束 500条
        List<Integer> bidIds = projectDao.getBidByStatus(PTProjectState.DFK);
        for (Integer bidId : bidIds) {
            try {
                //检查标是否放完款
                Runner runner = new XWMakeLoanFinishRerunJob.Runner(bidId);
                executor.submit(runner);
            } catch (Exception ex) {
                StringBuilder sb = new StringBuilder("检查标的放款是否结束异常：");
                sb.append(bidId);
                sb.append(ex.toString());
                logger.error(sb.toString(), ex);
            }
        }
    }

    public class Runner implements Runnable {
        protected Integer bidId;

        public Runner(Integer bidId) {
            this.bidId = bidId;
        }

        @Override
        public void run() {
            try {
                //检查标是否放完款
                makeLoanService.checkIfFinish(bidId);
            } catch (Exception e) {
                logger.error("检查标的放款是否结束异常出现异常：" + bidId, e);
            }
        }
    }
}
