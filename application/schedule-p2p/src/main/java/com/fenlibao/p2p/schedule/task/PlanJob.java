package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.user.SpecialUserService;
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
 * （标）计划  自动封 定时器
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PlanJob extends QuartzJobBean {

    public static final Logger LOGGER = LogManager.getLogger(PlanJob.class);

    @Resource
    private PlanService planService;
    @Resource
    private SpecialUserService specialUserService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("(标)计划封标开始，实例 >> {}", context.getFireInstanceId());
        List<String> userIds = specialUserService.getUserIds(SpecialUserType.DYYBKT);
        if (userIds == null || userIds.size() < 1) {
            throw new BusinessException("找不到封标账户");
        }
        List<AutoTenderVO> plans = planService.getPlanIdList_TBZ();
        LOGGER.info("(标)计划待封标计划数量 >> {}", plans.size());
        doBid(plans, userIds);
    }

    private void doBid(List<AutoTenderVO> plans, List<String> userIds) {
        for (AutoTenderVO vo : plans) {
            try {
                planService.doPlan(vo.getBidId(), vo.getSurplusAmount(), Integer.valueOf(userIds.get(0)), "", "", "");
                LOGGER.info("(标)计划封标完成,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
            } catch (Throwable e) {
                if (e instanceof BusinessException) {
                    if ("311115".equals(((BusinessException) e).getCode())) {//余额不足后不再执行
                        LOGGER.warn("-----------------------------------------------自动投标账户余额不足");
                        break;
                    }
                }
                LOGGER.error("(标)计划封标失败AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
