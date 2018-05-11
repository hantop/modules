package com.fenlibao.p2p.dao.spread;

import java.util.Map;

/**
 * 推广奖励统计
 * Created by chenzhixuan on 2015/8/25.
 */
public interface UserSpreadDao {
    /**
     * 新增推广奖励统计
     * @param map
     * @return
     */
    int addSpreadAwardStatistics(Map<String, Object> map);

    /**
     * 新增首次充值奖励记录
     * @param map
     * @return
     */
    int addFirstChargeAward(Map<String, Object> map);

    /**
     * 修改推荐人推广奖励统计次数+1
     * @param map
     * @return
     */
    int updateSpreadAwardStatistics(Map<String, Object> map);

    /**
     * 新增用户推广信息
     * @param map
     * @return
     */
    int addSpreadInfo(Map<String, Object> map);

}
