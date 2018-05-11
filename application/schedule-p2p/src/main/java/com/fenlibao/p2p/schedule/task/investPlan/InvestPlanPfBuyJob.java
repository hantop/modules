package com.fenlibao.p2p.schedule.task.investPlan;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.xinwang.entity.credit.BaseCreditInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import com.fenlibao.p2p.service.creditassignment.ZqzrManage;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.service.xinwang.credit.SysCreditService;
import com.fenlibao.p2p.util.loader.Config;
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
 * @Description: 平台回购
 * 购买人和债权转出人或标借款人不可以是同一个人
 * @author: kris
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InvestPlanPfBuyJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(InvestPlanPfBuyJob.class);
    private static final int PF_BUY_NUM = 1000;//平台每次回购数量
    @Resource
    ActivityService activityService;
    @Resource
    private SpecialUserService specialUserService;
    @Resource
    private ZqzrManage zqzrManage;
    @Resource
    private TransferInService transferInService;

    @Resource
    private SysCreditService sysCreditService;
    @Resource
    private XWProjectDao xwProjectDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("(投资计划退出)平台回购处理开始 实例ID:{}", context.getFireInstanceId());
        List<String> userIds = specialUserService.getUserIds(SpecialUserType.ZQZR);
        if (userIds == null || userIds.size() == 0) {
            logger.warn("(投资计划退出)平台回购处理异常,回购账号不存在：userIds:({})", JSON.toJSONString(userIds));
            return;
        }
        purchasePT(userIds);
        // modify by zeronx 2017-09-11 20:30
        // 因为存管版的投资计划功能改变：计划匹配标的期限要与计划期限相同（但因有些标的回款日比计划到期回款日迟，那么这部分债权就需要做债转，为了不做债转，所以直到这部分债权都回款后，
        // 计划才能由申请退出 -->已退出）(其实就相当于推迟计划的到期还款日)，
        // 所以在这里将平台回购存管版的计划债权屏蔽
        // purchaseCG(userIds);
    }

    /**
     * 投资计划退出-非存管
     *
     * @param userIds
     */
    private void purchasePT(List<String> userIds) {
        try {
            logger.info("(投资计划退出-非存管)平台回购退出处理开始");
            List<UserPlanProduct> userPlanProducts = zqzrManage.getUserPlanProductsNeedPfBuy(PF_BUY_NUM, VersionTypeEnum.PT);
            if (userPlanProducts != null && userPlanProducts.size() > 0) {
                Integer userId = 0;
                for (UserPlanProduct UserPlanProduct : userPlanProducts) {
                    List<String> temp = userIds;
                    try {
                        if (temp.contains(String.valueOf(UserPlanProduct.getUserId())))
                            temp.remove(String.valueOf(UserPlanProduct.getUserId()));
                        if (temp.contains(String.valueOf(UserPlanProduct.getBidBorrower())))
                            temp.remove(String.valueOf(UserPlanProduct.getBidBorrower()));
                        if (temp.size() > 0) {
                            userId = Integer.valueOf(temp.get(0));
                            transferInService.purchase(UserPlanProduct.getApplyforId(), userId);
                        } else {
                            logger.warn("(投资计划退出-非存管)平台回购处理异常,回购账号不满足购买条件：userIds:({}),userPlanProducts:({})",
                                    JSON.toJSONString(userIds), JSON.toJSONString(UserPlanProduct));
                        }
                    } catch (Throwable e) {
                        if (e instanceof BusinessException) {
                            if (ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT.getCode()
                                    .equals(((BusinessException) e).getCode())) {
                                userIds.remove(String.valueOf(userId));
                                //删掉缓存中的userId
                                specialUserService.lremSpecialUser(String.valueOf(userId), SpecialUserType.ZQZR);
                                //短信通知
                                String smsPattern = String.format("(投资计划退出-非存管)平台回购账户余额不足,请及时处理(userId:%s)", userId);
                                logger.warn(smsPattern);
                                String phone = Config.get("special.account.notify");
                                activityService.sendSms(smsPattern, phone);
                            }
                        } else {
                            logger.error("(投资计划退出-非存管)平台回购处理异常：userPlan:({}),异常信息:({})", JSON.toJSONString(UserPlanProduct), e.toString());
                            continue;
                        }
                    }
                }
            }
            logger.info("(投资计划退出-非存管)平台回购退出处理结束({})", userPlanProducts.size());
        } catch (Throwable e) {
            logger.error("(投资计划退出-非存管)平台回购退出处理失败", e);
        }
    }

    /**
     * 投资计划退出-存管
     *
     * @param userIds
     */
    private void purchaseCG(List<String> userIds) {
        try {
            logger.info("(投资计划退出-存管)平台回购退出处理开始");
            List<UserPlanProduct> userPlanProducts = zqzrManage.getUserPlanProductsNeedPfBuy(PF_BUY_NUM, VersionTypeEnum.CG);
            if (userPlanProducts != null && userPlanProducts.size() > 0) {
                Integer userId = 0;
                for (UserPlanProduct UserPlanProduct : userPlanProducts) {
                    List<String> temp = userIds;
                    try {
                        // add by zeronx 2017-09-15 9:37
                        // 校验这个债权对应的标是否正在还款中,是 则不作处理
                        BaseCreditInfo baseCreditInfo = sysCreditService.getBaseCreditInfo(UserPlanProduct.getProductId());
                        XWProjectInfo xwProjectInfo = xwProjectDao.getProjectInfoById(baseCreditInfo.getBidId());
                        if(xwProjectInfo.getInProgressOfRepay()) {
                            logger.warn(String.format("(投资计划退出-存管)平台回购处理债权,该标:%s正在还款,该债权:%s不作处理..", baseCreditInfo.getBidId(), UserPlanProduct.getProductId()));
                            continue;
                        }
                        if (temp.contains(String.valueOf(UserPlanProduct.getUserId())))
                            temp.remove(String.valueOf(UserPlanProduct.getUserId()));
                        if (temp.contains(String.valueOf(UserPlanProduct.getBidBorrower())))
                            temp.remove(String.valueOf(UserPlanProduct.getBidBorrower()));
                        if (temp.size() > 0) {
                            userId = Integer.valueOf(temp.get(0));
                            transferInService.purchase(UserPlanProduct.getApplyforId(), userId);
                        } else {
                            logger.warn("(投资计划退出-存管)平台回购处理异常,回购账号不满足购买条件：userIds:({}),userPlanProducts:({})", JSON.toJSONString(userIds), JSON.toJSONString(UserPlanProduct));
                        }
                    } catch (Throwable e) {
                        if (e instanceof BusinessException) {
                            if (ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT.getCode()
                                    .equals(((BusinessException) e).getCode())) {
                                userIds.remove(String.valueOf(userId));
                                //删掉缓存中的userId
                                specialUserService.lremSpecialUser(String.valueOf(userId), SpecialUserType.ZQZR);
                                //短信通知
                                String smsPattern = String.format("(投资计划退出-存管)平台回购账户余额不足,请及时处理(userId:%s)", userId);
                                logger.warn(smsPattern);
                                String phone = Config.get("special.account.notify");
                                activityService.sendSms(smsPattern, phone);
                            }
                        } else {
                            logger.error("(投资计划退出-存管)平台回购处理异常：userPlan:({}),异常信息:({})", JSON.toJSONString(UserPlanProduct), e.toString());
                            continue;
                        }
                    }
                }
            }
            logger.info("(投资计划退出-存管)平台回购退出处理结束({})", userPlanProducts.size());
        } catch (Throwable e) {
            logger.error("(投资计划退出-存管)平台回购退出处理失败", e);
        }
    }
}