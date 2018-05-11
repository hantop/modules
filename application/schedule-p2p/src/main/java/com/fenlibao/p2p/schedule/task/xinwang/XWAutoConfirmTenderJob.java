package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
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
 * 新网消费信贷标自动放款
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWAutoConfirmTenderJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWAutoConfirmTenderJob.class);

    @Resource
    SysMakeLoanDao makeLoanDao;

    @Resource
    XWMakeLoanService makeLoanService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网消费信贷标自动放款开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            ThreadPoolExecutor executor = ThreadPoolHelper.INSTANCE.getInstance();
            List<Integer> projectList = makeLoanDao.getAutoConfirmTenderProjectList();
            logger.info("自动放款标数："+projectList.size());
            for (Integer projectId : projectList) {
                try {
                    Runnable runner = new Runner(projectId);
                    executor.submit(runner);
                } catch (Exception e){
                    logger.error("标"+projectId+"自动放款出错："+e.toString(), e);
                }
            }
        } catch (Throwable e) {
            logger.error("自动放款出错", e);
        }
    }

    public class Runner implements Runnable{
        protected Integer projectId;

        public Runner(Integer projectId) {
            this.projectId = projectId;
        }

        @Override
        public void run() {
            try {
                makeLoanService.makeLoanApply(projectId);
            } catch (Exception e) {
                logger.error("标"+projectId+"自动放款出错："+e.toString(), e);
            }
        }
    }

}
