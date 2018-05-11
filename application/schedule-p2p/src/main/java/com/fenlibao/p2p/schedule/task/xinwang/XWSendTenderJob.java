package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.schedule.common.ThreadPoolHelper;
import com.fenlibao.p2p.service.xinwang.bid.SysBidManageService;
import com.fenlibao.p2p.service.xinwang.bid.XWBidService;
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
 * 新网异步投资消费信贷标
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWSendTenderJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWSendTenderJob.class);

    @Resource
    XWBidService bidService;

    @Resource
    SysBidManageService bidManageService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网异步投资消费信贷标。。。  实例ID： " + context.getFireInstanceId());
        ThreadPoolExecutor executor = ThreadPoolHelper.INSTANCE.getInstance();
        List<XWTenderBO> tenderList = bidService.getSendTender();
        logger.info("发送投资个数：" + tenderList.size());
        for (XWTenderBO tender : tenderList) {
            try {
                Runnable runner = new Runner(tender);
                executor.submit(runner);
            } catch (Exception e) {
                logger.error("标" + tender.getBidId() + "发送投资出错：" + e.toString(), e);
            }
        }

    }

    public class Runner implements Runnable {
        protected XWTenderBO tender;

        public Runner(XWTenderBO tender) {
            this.tender = tender;
        }

        @Override
        public void run() {
            try {
                int orderId = bidManageService.createTenderOrder(this.tender);
                bidService.doBidForPlan(orderId, this.tender);
            } catch (Exception e) {
                logger.error("标" + tender.getBidId() + "发送投资出错：" + e.toString(), e);
            }
        }
    }

}
