package com.fenlibao.service.pms.da.reward.common;

import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.RewardRecordCondition;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface RewardService {
    /**
     * 查询发放记录
     *
     * @param bounds
     * @return
     */
    List<RewardRecord> getRewardRecords(RewardRecordCondition rewardRecordCondition, RowBounds bounds);

    RewardRecord getRewardRecordById(RewardRecord rewardRecord);

    /**
     * 查询当前是否有在发放的奖励
     *
     * @return
     */
    Integer getInServiceRewards();

    /**
     * 获取是否已发放
     *
     * @param id
     * @return
     */
    Byte getGrantedById(Integer id);

    void updateRewardRecord(RewardRecord rewardRecord);
}
