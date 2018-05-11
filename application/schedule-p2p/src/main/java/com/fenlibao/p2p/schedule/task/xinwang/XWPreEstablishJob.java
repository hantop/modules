package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.service.xinwang.project.XWEstablishProjectService;
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
 * 新网发布到时间的预发布标
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWPreEstablishJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWPreEstablishJob.class);

    @Resource
    XWProjectDao projectDao;

    @Resource
    XWEstablishProjectService establishProjectService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网发布到时间的预发布标开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            List<Integer> projectList = projectDao.getPreEstablishProject();
            for (Integer projectId : projectList) {
                try {
                    establishProjectService.establishProject(projectId);
                }catch(Exception e){
                    logger.error("预发布标"+projectId+"发布出错："+e.toString(), e);
                }
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
    }
}
