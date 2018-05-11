package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.bid.BidService;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
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

/**
 * 对来自缺钱么的信用贷进行自动投标，
 * 条件：发布时间小于当天还没满的，当天进度大于50%的
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutotenderForCreditLoanJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(AutotenderForCreditLoanJob.class);
    @Resource
    ActivityService activityService;
    @Resource
    private BidService bidService;
    @Resource
    private SpecialUserService specialUserService;
    @Resource
    private XWUserInfoService xwUserInfoService;
    @Resource
    private BidManageService bidManageService;
    @Resource
    private XWBidService xwBidService;

    /*
    TMD需求改了N次了，刚开发完正在测试，丫的那边需求又改，艹
	需求:
	8-23点，每隔2h扫一次
	每次扫掉12h前及12h内先发标的50%
	 */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时器自动投标(来自缺钱么的信用贷)开始  实例ID： " + context.getFireInstanceId());
        List<String> userIds = null;
        try {
            userIds = specialUserService.getUserIds(SpecialUserType.DYYBKT);
        } catch (Exception e) {
            logger.error("消费贷自动投标(来自缺钱么的信用贷-普通标)失败", e);
        }
        try {
            //先处理12h前的
            tender(getCreditLoanTBZ(1, VersionTypeEnum.PT, 100), userIds);
            //处理12h内的5%
            tender(getCreditLoanTBZ(null, VersionTypeEnum.PT, 5), userIds);
        } catch (Exception e) {
            logger.error("消费贷自动投标(来自缺钱么的信用贷-普通标)失败", e);
        }
        //2017-9-21 去除存管版本的自动投标
//        try {
//            //先处理12h前的
//            tenderCG(getCreditLoanTBZ(1, VersionTypeEnum.CG, 100), userIds);
//            //处理12h内的5%
//            tenderCG(getCreditLoanTBZ(null, VersionTypeEnum.CG, 5), userIds);
//        } catch (Exception e) {
//            logger.error("消费贷自动投标(来自缺钱么的信用贷-存管标)失败", e);
//        }
    }

    private List<AutoTenderVO> getCreditLoanTBZ(Integer type, VersionTypeEnum versionTypeEnum, int percent) {
        List<AutoTenderVO> in12h = bidService.getCreditLoanTBZ(type, versionTypeEnum);
        int in12hSize = in12h.size();
        if (in12hSize > 10) {
            int toIndex = in12hSize * percent / 100;
            in12h = in12h.subList(0, toIndex);
        }
        String msg = type == null ? "12h内的" + percent + "%" : "12h前的" + percent + "%";
        logger.info("待封标({})(来自缺钱么的信用贷-{})数量：【{}】", msg, versionTypeEnum.getName(), in12hSize);
        return in12h;
    }

    private void tender(List<AutoTenderVO> list, List<String> userIds) {
        for (AutoTenderVO vo : list) {
            try {
                bidService.doBid(vo.getBidId(), vo.getSurplusAmount(), Integer.valueOf(userIds.get(0)), "", "");
                logger.info("定时器自动投标(来自缺钱么的信用贷-普通标)完成,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
            } catch (Throwable e) {
                if (e instanceof BusinessException) {
                    if ("311115".equals(((BusinessException) e).getCode())) {//余额不足后不再执行
                        logger.warn("-----------------------------------------------自动投标账户余额不足");
                        // 2017/3/18 发短信 18620157471
                        String smsPattern = "自动扫标(来自缺钱么的信用贷-普通标)账户余额不足,请及时处理。（USERID："+userIds.get(0)+"）";
                        String phone = "18620157471";
                        activityService.sendSms(smsPattern, phone);
                        break;
                    }
                }
                logger.error("定时器自动投标(来自缺钱么的信用贷-普通标)失败AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                logger.error(e, e);
            }
        }
    }

    private void tenderCG(List<AutoTenderVO> list, List<String> userIds) {
        try {
            logger.info("定时器自动投标(来自缺钱么的信用贷-存管标)开始");
            XWFundAccount xwUserInfo = null;
            for (String userId : userIds) {
                xwUserInfo = xwUserInfoService.getFundAccount(Integer.valueOf(userId), SysFundAccountType.XW_INVESTOR_WLZH);
                if (xwUserInfo != null) {
                    break;
                }
            }
            for (AutoTenderVO vo : list) {
                try {
                    xwUserInfoService.validateAmount(xwUserInfo, vo.getSurplusAmount());
                    int orderId = bidManageService.tender(vo.getBidId(), vo.getSurplusAmount(), xwUserInfo.getUserId());
                    String[] red = null;
                    xwBidService.doBid(orderId,  null, red);
                    logger.info("定时器自动投标(来自缺钱么的信用贷-存管标)完成,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                } catch (Throwable e) {
                    logger.error("定时器自动投标(来自缺钱么的信用贷-存管标)失败,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                    logger.error(e, e);
                }
            }
            logger.info("定时器自动投标(来自缺钱么的信用贷-存管标)结束");
        } catch (Throwable e) {
            logger.error("定时器自动投标(来自缺钱么的信用贷-存管标)失败");
            logger.error(e, e);
        }
    }
}
