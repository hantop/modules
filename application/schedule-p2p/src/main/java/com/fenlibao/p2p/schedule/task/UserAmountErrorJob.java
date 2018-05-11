package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.check.amount.XwAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.check.amount.CheckAmountService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zeronx on 2017/8/31.
 */

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class UserAmountErrorJob extends QuartzJobBean {

    private static final Logger LOGGER = LogManager.getLogger(UserAmountErrorJob.class);

    @Autowired
    private XWUserInfoService xwUserInfoService;

    @Autowired
    private CheckAmountService checkAmountService;
   //int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue
    private ExecutorService executorService = new ThreadPoolExecutor(0, 8, 90, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100), new MyRejectedExecutionHandler());

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        List<XwAccount> accountList = checkAmountService.getAllXwUsers();
        LOGGER.info("对比用户新网账户总余额是否相等总记录数：" + accountList.size());
//        int userId = 0;
        for (final XwAccount xwAccount : accountList) {
            try {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        int userId = xwAccount.getUserId();
                        XinwangUserInfo xwUserInfo = xwUserInfoService.queryUserInfo(UserRole.INVESTOR.getCode() + userId);
                        XWFundAccount xwFundAccountWL = xwUserInfoService.getFundAccount(userId, SysFundAccountType.XW_INVESTOR_WLZH);
                        XWFundAccount xwFundAccountSD = xwUserInfoService.getFundAccount(userId, SysFundAccountType.XW_INVESTOR_SDZH);
                        BigDecimal bigDecimal = xwFundAccountWL.getAmount().add(xwFundAccountSD.getAmount());
                        if (bigDecimal.compareTo(xwUserInfo.getBalance()) != 0) {
                            checkAmountService.saveUserAmountError(userId, xwAccount.getPlatformUserNo(), xwFundAccountWL.getAmount(), xwUserInfo.getAvailableAmount(), xwFundAccountSD.getAmount(), xwUserInfo.getFreezeAmount(), xwUserInfo.getBalance());
                        }
                        LOGGER.info("对比用户新网账户总余额完成时间:[{}]", new Date());
                    }
                });
//                Thread.sleep(300);
            } catch (Exception e) {
                LOGGER.error("对比用户新网账户总余额是否相等异常: {}", e);
            }
        }
    }
}
