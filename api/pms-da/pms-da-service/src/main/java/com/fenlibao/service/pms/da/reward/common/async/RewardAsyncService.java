package com.fenlibao.service.pms.da.reward.common.async;

import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.form.UserRedpacketsForm;

/**
 * Created by Louis Wang on 2016/6/17.
 */

public interface RewardAsyncService {

    //异步发放红包
    void asyncGrantBackVoucherResult(RewardRecord rewardRecord, UserRedpacketsForm userRedpacketsForm);


    /**
     * 多线程发放返现券
     *
     * @param rewardRecord
     * @param userRedpacketsForm
     */
    void multiThreadGrantRedPacket(RewardRecord rewardRecord, UserRedpacketsForm userRedpacketsForm);

    /**
     * 发放现金红包
     * @param rewardRecord
     */
    void multiThreadGrantCashRedPacket(RewardRecord rewardRecord);

}
