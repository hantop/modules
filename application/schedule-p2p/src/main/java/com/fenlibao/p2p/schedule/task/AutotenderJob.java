package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
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
 * 自动投标，可投金额小于100的(替补)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutotenderJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(AutotenderJob.class);

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

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时器自动投标开始  实例ID： " + context.getFireInstanceId());
        List<String> userIds = specialUserService.getUserIds(SpecialUserType.DYYBKT);

        //先封普通标
        try {
            logger.info("定时器自动投标(普通标)开始");
            List<AutoTenderVO> list = bidService.getTBZ(VersionTypeEnum.PT);
            logger.info("待封标数量（普通标）：【{}】", list.size());
            for (AutoTenderVO vo : list) {
                try {
                    bidService.doBid(vo.getBidId(), vo.getSurplusAmount(), Integer.valueOf(userIds.get(0)), "", "");
                    logger.info("定时器自动投标(普通标)完成,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                } catch (Throwable e) {
                    logger.error("定时器自动投标(普通标)失败,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                    logger.error(e, e);
                }
            }
            logger.info("定时器自动投标(普通标)结束");
        } catch (Throwable e) {
            logger.error("定时器自动投标(普通标)失败");
            logger.error(e, e);
        }

        //再封存管标
        try {
            logger.info("定时器自动投标(存管标)开始");

            List<AutoTenderVO> list = bidService.getTBZ(VersionTypeEnum.CG);
            logger.info("待封标数量（存管标）：【{}】", list.size());

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
                    xwBidService.doBid(orderId, null, red);
                    logger.info("定时器自动投标(存管标)完成,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                } catch (Throwable e) {
                    logger.error("定时器自动投标(存管标)失败,AutoTenderVO=[{}],userId=[{}]", vo.toString(), userIds.get(0));
                    logger.error(e, e);
                }
            }
            logger.info("定时器自动投标(存管标)结束");
        } catch (Throwable e) {
            logger.error("定时器自动投标(存管标)失败");
            logger.error(e);
        }
    }
}
