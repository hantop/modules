package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.service.xinwang.checkfile.XWCheckfileDownloadService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 一次性下载所有对账文件并入库
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWCheckFileDownAllTemporaryJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWCheckFileDownAllTemporaryJob.class);
    @Resource
    XWCheckfileDownloadService checkfileDownloadService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("====【..一次性下载所有对账文件并入库开始.】实例ID:" + context.getFireInstanceId());
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(2017, 7, 8);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.add(Calendar.DAY_OF_MONTH, -1);
            long endTime = endCalendar.getTime().getTime();

            while (startCalendar.getTime().getTime() <= endTime) {
                Date startDate = startCalendar.getTime();
                String startString = format.format(startDate);
                checkfileDownloadService.postXinwang(startString);
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception ex) {
            logger.error("一次性下载所有对账文件并入库出现异常,实例ID:[{}],异常信息:[{}]", context.getFireInstanceId(), ex);
        }
    }
}