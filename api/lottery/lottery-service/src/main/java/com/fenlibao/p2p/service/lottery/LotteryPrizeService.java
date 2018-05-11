package com.fenlibao.p2p.service.lottery;

import com.fenlibao.p2p.model.lottery.entity.*;

import java.util.Date;
import java.util.List;

/**
 * @title  抽奖service
 * @author laubrence
 * @date   2016-4-26 20:54:08
 */
public interface LotteryPrizeService {

    /**
     * 根据活动id获取活动的奖项信息
     * @param activityId
     * @return
     * @throws Exception
     */
    List<LotteryPrizeInfo> getLotteryPrizeInfos(int activityId) throws Exception;

    /**
     * 获取抽奖有效奖项信息
     * @param activityId
     * @return
     * @throws Exception
     */
    List<LotteryPrizeInfo> getEffectivePrizeInfos(int activityId) throws Exception;

    /**
     * 抽奖
     * @param activityId
     * @param userId
     * @param cellTailNumber
     * @return
     * @throws Exception
     */
    LotteryPrizeInfo lottery(int activityId, int userId, String cellTailNumber) throws Exception;

    /**
     * 抽奖并减少奖品数量
     * @param activityId
     * @param userId
     * @param cellTailNumber
     * @return
     * @throws Exception
     */
    LotteryPrizeInfo lotteryAndUpdatePrizeQuantity(int activityId, int userId, String cellTailNumber) throws Exception;

    /**
     * 中指定奖品
     * @param activityId
     * @param userId
     * @param cellTailNumber
     * @param prizeId
     * @return
     * @throws Exception
     */
    int addLotteryDrawRecord(int activityId, int userId, String cellTailNumber, int prizeId) throws Exception ;

    /**
     * 获取用户抽奖次数信息
     * @param userId
     * @param activityId
     * @return
     * @throws Exception
     */
    UserLotteryTimes getUserLotteryTimes(int userId, int activityId) throws Exception;

    /**
     * 获取一条用户中奖信息
     * @param activityId
     * @param recordId
     * @return
     * @throws Exception
     */
    UserLotteryRecord getUserLotteryOneRecord(int activityId, int recordId) throws Exception;

    /**
     * 获取一条用户在某活动中的抽奖次数
     * @param activityId
     * @return
     * @throws Exception
     */
    int getUserLotteryCount(int activityId, int userId) throws Exception;

    /**
     * 获取每日投资排行榜
     * @param currentDate
     * @param limit
     * @param userId
     * @return
     */
    List<UserInvestBoard> getBoardDayRichestList(Date currentDate, int limit, int userId);

    /**
     * 获取中奖奖品列表
     * @param userId
     * @param activityId
     * @param drawDate
     */
    List<UserLotteryDrawPrizeRecord>  getUserDrawPrizes(int userId, int activityId, Date drawDate);


    /**
     * 获取当天中奖奖品列表
     * @param userId
     * @param activityId
     */
    List<UserLotteryDrawPrizeRecord>  getUserThisDayDrawPrizes(int userId, int activityId, Date drawDate);

    /**
     * 获取最新中奖纪录（一周年活动）
     * @param activityId
     * @param num
     * @return
     */
    List<UserLotteryDrawPrizeRecord> getUserLatestDrawPrizes(int activityId, int num);

    /**
     * 模拟抽奖(只获取数据，不插入数据库，用于模拟虚拟数据)
     * @param activityId
     * @param num
     * @return
     * @throws Exception
     */
    List<UserLotteryDrawPrizeRecord> simulateLottery(int activityId, int num) throws Exception;

    /**
     * 获取中奖奖品信息
     * @param activityId
     * @return
     */
    LotteryPrizeInfo getLotteryDrawPrize(int activityId);

    /**
     * 减少奖品数量
     * @param activityId
     * @param prizeId
     * @param quantity
     * @return
     */
    int updatePrizeQuantity(int activityId, int prizeId, int quantity);
}

