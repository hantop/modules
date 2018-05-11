package com.fenlibao.p2p.schedule.task.investPlan;

import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 投资未满计划
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InvestPlanNotFullJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(InvestPlanNotFullJob.class);
    @Resource
    private SpecialUserService specialUserService;
    @Resource
    private PlanService planService;
    @Resource
    private UserInfoService userInfoService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info(String.format("%s(投资未满计划标)处理开始 实例ID:" + context.getFireInstanceId(), new Date()));
        List<String> userIds = null;
        Date date = null;
        try {
            userIds = specialUserService.getUserIds(SpecialUserType.INVEST_PLAN_NOT_FULL);
            String hourStr = Config.get("invest_plan_not_full_date");
            String minuteStr = Config.get("invest_plan_not_full_date_minute");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.nowDate());
            if (hourStr != null) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourStr));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minuteStr));
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 21);
                calendar.set(Calendar.MINUTE, 0);
            }
            calendar.set(Calendar.SECOND, 0);
            date = calendar.getTime();
            logger.info(String.format("查询%s之前的未满计划标", DateUtil.getDateTime(date)));
        } catch (Exception e) {
            logger.error("投资未满计划标获取userIds:%s失败$s", userIds, e);
        }
        //需要扫掉的计划
        List<Integer> plans = null;
        try {
            if (date != null) {
                plans = planService.getPlanInfoNotFull(date, 2);
                logger.info(String.format("(投资未满计划标id列表)({%s})", Arrays.asList(plans)));
                if (!plans.isEmpty()) {
                    Iterator it = plans.iterator();
                    InvestPlan investPlan = null;//计划标
                    BigDecimal surplusAmount = BigDecimal.ZERO;//可投金额
                    BigDecimal balance = null;//账户金额
                    while (it.hasNext()) {
                        //计划标的ID
                        Integer id = (Integer) it.next();
                        investPlan = planService.getInvestPlan(id);
                        if (investPlan != null && VersionTypeEnum.CG.equals(VersionTypeEnum.parse(investPlan.getIsCG()))) {
                            //如果是定向计划 需要验证是否符合条件, 因为是固定账户，不用再次判断用户条件①
                            //①planInfoService.checkCanInvest(investPlan.getId(),userId,VersionTypeEnum.CG);
                            //计划标可投金额
                            if (investPlan.getSurplusAmount().compareTo(BigDecimal.ZERO) == 1) {
                                surplusAmount = investPlan.getSurplusAmount();
                                logger.info(String.format("投资未满计划标id:%s可投金额:%s", id, surplusAmount));
                                Iterator userIt = userIds.iterator();
                                Integer userId = -1;
                                while(userIt.hasNext()){
                                    String uId = (String)userIt.next();
                                    userId = Integer.parseInt(uId);
                                    balance = getBalance(userId);
                                    //金额够不够投资
                                    if (balance.compareTo(BigDecimal.ZERO) == 1 && surplusAmount.compareTo(balance) == -1) {
                                        break;
                                    }else if(balance.compareTo(BigDecimal.ZERO)  < 1){//账户金额小于等于0，移除，防止下一次再次查询数据库
                                        userIt.remove();
                                        userId = -1;
                                    } else{
                                        userId = -1;
                                    }
                                }
                                if(userId > 0) {
                                    planService.doInvestPlanForCG(id, surplusAmount, userId, null, null, null);
                                }else {
                                    logger.error(String.format("投资未满计划标id:%s失败，可投金额:%s，账户余额:$s", id, surplusAmount, balance));
                                }
                            }
                        } else {
                            logger.info(String.format("查询未满计划标 id：%s不是存管标", id));
                        }
                    }
                } else {
                    logger.info(String.format("%s(查询未满计划标为空，不需要投资)", new Date()));
                }
            }
        } catch (Exception e) {
            logger.error(String.format("%s(投资未满计划标)出现异常，实例ID:" + context.getFireInstanceId() + "  msg:" + e.getMessage(), new Date()));
        } catch (Throwable t) {
            logger.error(String.format("%s(投资未满计划)出现异常，实例ID:" + context.getFireInstanceId() + "  msg:" + t.getMessage(), new Date()));
        }
        logger.info(String.format("%s(投资未满计划处理结束({})", new Date()));
    }

    /**
     * 获取账户余额
     * @param userId
     * @return 余额
     */
    private BigDecimal getBalance(Integer userId){
        String accountType = InterfaceConst.XW_INVESTOR_WLZH;
        UserAccount userAccount = userInfoService.getUserAccount(String.valueOf(userId), accountType);
        BigDecimal balance =  BigDecimal.ZERO;
        if (userAccount == null) {
            logger.error(String.format("投资未满计划标获取user id:%s失败,userInfo null", userId));

        } else {
            balance = userAccount.getBalance();
            logger.error(String.format("准备投资未满计划标 user id:%s,金额%s", userId, balance));
            if (balance.compareTo(BigDecimal.ZERO) < 1) {
                logger.error(String.format("投资未满计划标 user id:%s失败,金额不足%s", userId, balance));
            }
        }
        return balance;
    }
}