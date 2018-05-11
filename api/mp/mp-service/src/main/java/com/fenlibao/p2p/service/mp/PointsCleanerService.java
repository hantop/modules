package com.fenlibao.p2p.service.mp;

import com.fenlibao.p2p.model.mp.enums.topup.ClearStatus;
import com.fenlibao.p2p.model.mp.vo.PointsClearVO;

import java.util.Date;
import java.util.List;

/**
 * 定时清理积分服务
 *
 * @author Mingway.Xu
 * @date 2016/11/25 13:52
 */
public interface PointsCleanerService {
    /**
     * 获取用户需要清除的积分信息
     * @return
     */
    List<PointsClearVO> getUserPiontInfoForClear(Integer limit);

    /**
     * 清除用户的积分
     * @param pointsClearVO
     * @param needClearPoint
     */
    void clearUserPoints(PointsClearVO pointsClearVO, int needClearPoint) throws Exception;

    /**
     * 更新积分账号状态
     */
    void updateMemberPointsStatus(Integer userId, ClearStatus clearStatus);

    /**
     * 获取用户需要保留的积分
     * @param userId
     * @param startTime
     * @return
     */
    int getUserHoldPoints(int userId, Date startTime);
}
