package com.fenlibao.service.pms.da.reward.common.async.impl;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserRateCoupon;
import com.fenlibao.model.pms.da.reward.form.RateCouponDetailForm;
import com.fenlibao.service.pms.da.reward.cashRedPacket.UserRedpacketsService;
import com.fenlibao.service.pms.da.reward.common.async.GrantRateCouponAsyncService;
import com.fenlibao.service.pms.da.reward.rateCoupon.UserRateCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/9/12.
 */
@Service
public class GrantRateCouponAsyncServiceImpl implements GrantRateCouponAsyncService {
    private static final Logger logger = LoggerFactory.getLogger(GrantRateCouponAsyncServiceImpl.class);

    @Autowired
    private UserRedpacketsService userRedpacketsService;
    @Autowired
    private UserRateCouponService userRateCouponService;

    @Async
    @Override
    public void multiThreadGrantRateCoupon(RewardRecord rewardRecord, RateCouponDetailForm rateCouponDetailForm) {
        logger.info("异步操作====================================================================================开始");
        try {
            List<UserRateCoupon> userRateCoupons = userRateCouponService.findUserRateCouponAll(rewardRecord, rateCouponDetailForm);
            int size = userRateCoupons.size(); // 全部的数量
            int batchNum = Integer.valueOf(Config.get("batch.processing.amount")); // 每次默认执行500个
            int count = size % batchNum > 0 ? size / batchNum + 1 : size / batchNum; // 要发起请求的次数
            int fromIndex;
            int toIndex;
            boolean isError = false;
            // 线程数量
            int threadNum = 15;
            ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
            GrantRateCouponCallable callable;
            List<Future<Boolean>> futures = new ArrayList<>();
            Future<Boolean> future;
            for (int i = 0; i < count; i++) {
                fromIndex = i * batchNum;
                if (i == count - 1 && (size % batchNum != 0)) {
                    batchNum = size % batchNum;
                    toIndex = size;

                } else {
                    toIndex = (i + 1) * batchNum;
                }
                logger.info("fromIndex: " + fromIndex + " toIndex: " + toIndex + " batchNum: " + batchNum + " i: "
                        + (i + 1));
                callable = new GrantRateCouponCallable(rewardRecord, userRateCoupons.subList(fromIndex, toIndex));
                future = executorService.submit(callable);
                futures.add(future);
            }
            // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
            executorService.shutdown();
            // 一直循环到所有任务都已完成
            while (!executorService.isTerminated());
            // 如果关闭后所有任务都已完成，则返回 true。
            if (executorService.isTerminated()) {
                for (Future<Boolean> fs : futures) {
                    // 一直循环到线程执行完
                    while (!fs.isDone());
                    // 线程返回结果
                    Boolean result = fs.get();
                    if (!result) {
                        isError = true;
                    }
                }
            }
            RewardRecord doRewardRecord = new RewardRecord();
            // 全部成功才更新表的状态
            if (!isError) {
                doRewardRecord.setId(rewardRecord.getId());
                doRewardRecord.setGranted((byte) 1);
                doRewardRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
                doRewardRecord.setInService((byte)0);
                userRedpacketsService.updateRewardRecord(doRewardRecord);
            }
        } catch (Exception e){
            logger.error("[GrantRateCouponAsyncServiceImpl.multiThreadGrantRateCoupon]" + e.getMessage(), e);
            e.printStackTrace();
        }
        logger.info("异步操作====================================================================================结束");
    }

    private class GrantRateCouponCallable implements Callable<Boolean> {
        private RewardRecord rewardRecord;
        private List<UserRateCoupon> userRateCoupons;

        public GrantRateCouponCallable(RewardRecord rewardRecord, List<UserRateCoupon> userRateCoupons) {
            this.rewardRecord = rewardRecord;
            this.userRateCoupons = userRateCoupons;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                userRateCouponService.grantRateCoupons(rewardRecord, userRateCoupons);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
