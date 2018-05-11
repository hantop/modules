package com.fenlibao.p2p.dao.bid;

import com.fenlibao.p2p.model.entity.redenvelope.ShareRewardEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Mingway.Xu
 * @date 2017/1/23 17:53
 */
public interface TenderShareSettingDao {
    /**
     * 获取奖励设置对应的id
     * @param buyAmount
     * @return
     */
    Map getTenderShareSettingId(BigDecimal buyAmount);

    /**
     * 获取固定设置对应的奖励列表
     * @param settingId
     * @return
     */
    List<ShareRewardEntity> getTenderShareEntityList(int settingId,int isNovice);

    /**
     * 获取本次分项记录中的类型奖励剩余分享次数
     * @param param
     * @return
     */
    int getRestNumByInvestRecord(Map<String, Object> param);
}
