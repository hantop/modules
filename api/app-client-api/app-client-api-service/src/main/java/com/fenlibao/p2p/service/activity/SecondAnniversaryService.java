package com.fenlibao.p2p.service.activity;

import com.fenlibao.p2p.model.lottery.entity.LotteryPrizeInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */
public interface SecondAnniversaryService {

    public List<LotteryPrizeInfo> getLotteryPrizeInfos(int activityId) throws Exception;

    public LotteryPrizeInfo lotteryAndUpdatePrizeQuantity(int activityId, int userId, String cellTailNumber) throws Exception;

    public int updatePrizeQuantity(int activityId, int prizeId, int quantity);

    public int updatePrizeProbability(int activityId, int prizeId, int quantity);

}
