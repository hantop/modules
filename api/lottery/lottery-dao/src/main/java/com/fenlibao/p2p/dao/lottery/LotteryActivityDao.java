package com.fenlibao.p2p.dao.lottery;

import com.fenlibao.p2p.model.lottery.entity.ActivityInfo;
import com.fenlibao.p2p.model.lottery.entity.LotteryActivityInfo;

public interface LotteryActivityDao {

    /**
     * 根据activityId或者activityCode获取抽奖活动信息
     * @param activityId
     * @param activityCode
     * @return
     */
    LotteryActivityInfo getLotteryActivityInfo(int activityId, String activityCode);

    /**
     * 从 flb.t_activity 表中获取活动信息
     * @param id
     * @param code
     * @return
     */
    ActivityInfo getActivityInfo(int id, String code);
}
