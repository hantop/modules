package com.fenlibao.p2p.dao.mq;

import com.fenlibao.p2p.model.mp.enums.topup.ClearStatus;
import com.fenlibao.p2p.model.mp.vo.PointsClearVO;

import java.util.Date;
import java.util.List;

/**
 * @author Mingway.Xu
 * @date 2016/11/25 17:30
 */
public interface PointsCleanerDao {
    /**
     * 插入一条用户积分清理记录
     * @param userId
     * @param realAccount
     */
    int insertAutoClearPointsRecord(int userId, int realAccount);

    /**
     * 获取用户积分信息-用于清除
     * @return
     */
    List<PointsClearVO> getUserPiontInfoForClear(Integer limit);

    /**
     * 更新用户积分账户清除状态
     * @param userId
     * @param clearStatus
     */
    void updateMemberPointsStatus(int userId, ClearStatus clearStatus);

    /**
     * 获取用户需要保留的积分
     * @param userId
     * @param startTime
     * @return
     */
    int getUserHoldPoints(int userId, Date startTime);
}
