package com.fenlibao.p2p.dao.lottery;

import com.fenlibao.p2p.model.lottery.entity.*;

import java.util.Date;
import java.util.List;

public interface LotteryPrizeDao {

    /**
     * 根据activityId获取奖品信息
     * @param activityId
     * @return
     */
    List<LotteryPrizeInfo> getLotteryPrizeInfos(int activityId);

    /**
     * 据activityId获取有效奖品信息
     * @param activityId
     * @return
     */
    List<LotteryPrizeInfo> getEffectivePrizeInfos(int activityId);

    /**
     * 更新用户的可抽奖次数
     * @param activityId
     * @param userId
     * @return
     */
    int updateUserLotteryAvailTimes(int recordId);

    /**
     * 添加用户中奖纪录
     * @param userId
     * @param cellTailNumber
     * @param prizeId
     * @param activityId
     */
    int addLotteryDrawRecord(int activityId, int userId, String cellTailNumber, int prizeId);

    /**
     * 获取用户抽奖次数信息
     * @param userId
     * @param activityId
     * @return
     */
    UserLotteryTimes getUserLotteryTimes(int activityId, int userId);

    /**
     * 锁定用户抽奖次数记录
     * @param recordId
     * @return
     */
    UserLotteryTimes lockUserLotteryTimes(int recordId);

    /**
     * 获取一条用户获奖信息
     * @param activityId
     * @param recordId
     * @return
     */
    UserLotteryRecord getUserLotteryOneRecord(int activityId, int recordId);

    /**
     * 获取一条用户在某活动中的抽奖次数
     * @param activityId
     * @return
     * @throws Exception
     */
    int getUserLotteryCount(int activityId, int userId);

    /**
     * 获取每日投资排行榜
     * @param currentDate
     * @param limit
     * @param userId
     * @return
     */
    List<UserInvestBoard> getBoardDayRichestList(Date currentDate, int limit, int userId);

    List<UserLotteryDrawPrizeRecord> getUserDrawPrizes(int currentDate, int userId, Date activityId);

    List<UserLotteryDrawPrizeRecord> getUserThisDayDrawPrizes(int userId, int activityId, Date drawDate);

    List<UserLotteryDrawPrizeRecord> getUserLatestDrawPrizes(int activityId, int size);

    /**
     * 修改奖品数量
     * @param activityId
     * @param prizeId
     * @param quantity
     * @return
     */
    int updatePrizeQuantity(int activityId ,int prizeId, int quantity);
    /**
     * 修改奖品概率
     * @param activityId
     * @param prizeId
     * @return
     */
    int updatePrizeProbability(int activityId ,int prizeId, int quantity);
}
