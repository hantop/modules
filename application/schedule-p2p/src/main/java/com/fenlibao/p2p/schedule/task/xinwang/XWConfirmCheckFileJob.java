package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.service.xinwang.checkfile.XWCheckfileDownloadService;
import com.fenlibao.p2p.util.api.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 新网对账确认
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWConfirmCheckFileJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWConfirmCheckFileJob.class);

    @Resource
    XWCheckfileDownloadService checkfileDownloadService;

    @Resource
    XWRequestDao requestDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网对账确认开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            Date yesterday=DateUtil.timeAddOrSub(new Date(), Calendar.DATE,-1);
            String fileDate = DateUtil.getYYYYMMDD(yesterday);
            //下载对账文件
            //checkfileDownloadService.downloadCheckFiles(fileDate);
            //需要的话进行对账
            //对账确认
            checkfileDownloadService.confirmCheckFile(fileDate);
        } catch (Throwable e) {
            logger.error("新网对账确认出错：" + e.toString(), e);
        }
    }
}
