package com.fenlibao.service.pms.da.reward.common.async.impl;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserCashRedPacket;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import com.fenlibao.model.pms.da.reward.form.UserRedpacketsForm;
import com.fenlibao.service.pms.da.reward.cashRedPacket.RedPacketService;
import com.fenlibao.service.pms.da.reward.cashRedPacket.UserRedpacketsService;
import com.fenlibao.service.pms.da.reward.common.async.RewardAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 发放返现红包异步操作
 * Created by Louis Wang on 2016/6/17.
 */

@Service
public class RewardAsyncServiceImpl implements RewardAsyncService {

    private static final Logger logger = LoggerFactory.getLogger(RewardAsyncServiceImpl.class);

    @Autowired
    private RedPacketService redPacketService;

    @Autowired
    private UserRedpacketsService userRedpacketsService;

    @Override
    @Async
    public void asyncGrantBackVoucherResult(RewardRecord rewardRecord, UserRedpacketsForm userRedpacketsForm){

        logger.info("异步操作====================================================================================开始");
        try {
            List<UserRedpackets> userRedpacketsList = userRedpacketsService.findUserRedpacketsAll(rewardRecord, userRedpacketsForm);
                /* 返现卷发放的次数限制 add Jing 20160420 */
            int size = userRedpacketsList.size(); // 全部的数量
            int batchNum = Integer.valueOf(Config.get("batch.processing.amount")); // 每次默认执行500
            // 个
            int count = size % batchNum > 0 ? size / batchNum + 1 : size / batchNum; // 要发起请求的次数
            int fromIndex = 0;
            int toIndex = 0;
            boolean status = false;
            BigDecimal grantSum = new BigDecimal(0);
            int doBatch = 0; // 操作成功的条数
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
                try {
                        RewardRecord tmpRewardRecord = userRedpacketsService.grantBackVoucherResult(rewardRecord, userRedpacketsList.subList(fromIndex, toIndex));
                    if (tmpRewardRecord != null) {
                        // grantSum =
                        // grantSum.add(tmpRewardRecord.getGrantSum());
                        doBatch += batchNum;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }

            RewardRecord doRewardRecord = new RewardRecord();
            // 全部成功才更新表的状态
            if (size == doBatch) {
                doRewardRecord.setId(rewardRecord.getId());
                doRewardRecord.setGranted((byte) 1);
                doRewardRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
                doRewardRecord.setInService((byte)0);
                userRedpacketsService.updateRewardRecord(doRewardRecord);
                // 一次全部更新成功
                status = true;
            }
        }catch (Exception e){

        }

        logger.info("异步操作====================================================================================结束");
    }


    @Async
    @Override
    public void multiThreadGrantRedPacket(RewardRecord rewardRecord, UserRedpacketsForm userRedpacketsForm) {
        logger.info("异步操作====================================================================================开始");
        Date date = new Date();
        try {
            List<UserRedpackets> userRedpacketsList = userRedpacketsService.findUserRedpacketsAll(rewardRecord, userRedpacketsForm);
            int size = userRedpacketsList.size(); // 全部的数量
            int batchNum = Integer.valueOf(Config.get("batch.processing.amount")); // 每次默认执行500个
            int count = size % batchNum > 0 ? size / batchNum + 1 : size / batchNum; // 要发起请求的次数
            int fromIndex;
            int toIndex;
            boolean isError = false;
            // 线程数量
            int threadNum = 15;
            ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
            GrantRedpacketCallable callable;
            FutureTask futureTask;
            List<FutureTask<Boolean>> futureTasks = new ArrayList<>();
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
                callable = new GrantRedpacketCallable(rewardRecord, userRedpacketsList.subList(fromIndex, toIndex));
                futureTask = new FutureTask(callable);
                executorService.submit(futureTask);
                futureTasks.add(futureTask);
            }
            // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
            executorService.shutdown();
            // 一直循环到所有任务都已完成
            while (!executorService.isTerminated());
            for (FutureTask<Boolean> ft : futureTasks) {
                // 线程返回结果
                Boolean result = ft.get();
                if (!result) {
                    isError = true;
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
            logger.error("[AsyncGrantBackVoucherResult.multiThreadGrantRedPacket]" + e.getMessage(), e);
            e.printStackTrace();
        }
        Date end = new Date();
        logger.info(end.toString());
        logger.info("异步操作====================================================================================结束" );
    }

    private class GrantRedpacketCallable implements Callable<Boolean> {
        private RewardRecord rewardRecord;
        private List<UserRedpackets> userRedpackets;

        public GrantRedpacketCallable(RewardRecord rewardRecord, List<UserRedpackets> userRedpackets) {
            this.rewardRecord = rewardRecord;
            this.userRedpackets = userRedpackets;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                userRedpacketsService.grantBackVoucherResult(rewardRecord, userRedpackets);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    @Override
    public void multiThreadGrantCashRedPacket(RewardRecord rewardRecord) {
        logger.info("(现金红包存管版)异步操作====================================================================================开始");
        Date date = new Date();
        try {
            UserCashRedPacket userCashRedPacketCondition = new UserCashRedPacket();
            userCashRedPacketCondition.setGrantId(rewardRecord.getId());
            List<UserCashRedPacket> userCashRedPacketList = userRedpacketsService.getCustodyCashRedPacket(rewardRecord);
            int size = userCashRedPacketList.size(); // 全部的数量
            int batchNum = Integer.valueOf(Config.get("batch.processing.amount")); // 每次默认执行500个
            int count = size % batchNum > 0 ? size / batchNum + 1 : size / batchNum; // 要发起请求的次数
            int fromIndex;
            int toIndex;
            boolean isError = false;
            // 线程数量
            int threadNum = 15;
            ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
            GrantCsahRedpacketCallable callable;
            FutureTask futureTask;
            List<FutureTask<Boolean>> futureTasks = new ArrayList<>();
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
                callable = new GrantCsahRedpacketCallable(rewardRecord, userCashRedPacketList.subList(fromIndex, toIndex));
                futureTask = new FutureTask(callable);
                executorService.submit(futureTask);
                futureTasks.add(futureTask);
            }
            // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
            executorService.shutdown();
            // 一直循环到所有任务都已完成
            while (!executorService.isTerminated());
            for (FutureTask<Boolean> ft : futureTasks) {
                // 线程返回结果
                Boolean result = ft.get();
                if (!result) {
                    isError = true;
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
            logger.error("[AsyncGrantBackVoucherResult.multiThreadGrantCashRedPacket]" + e.getMessage(), e);
            e.printStackTrace();
        }
        Date end = new Date();
        logger.info(end.toString());
        logger.info("异步操作====================================================================================结束" );
    }

    private class GrantCsahRedpacketCallable implements Callable<Boolean> {
        private RewardRecord rewardRecord;
        private List<UserCashRedPacket> userCashRedPackets;

        public GrantCsahRedpacketCallable(RewardRecord rewardRecord, List<UserCashRedPacket> userCashRedPackets) {
            this.rewardRecord = rewardRecord;
            this.userCashRedPackets = userCashRedPackets;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                redPacketService.grantCustodyCashRedPacket(rewardRecord, userCashRedPackets);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
