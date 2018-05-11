package com.fenlibao.p2p.service.mp.impl;

import com.fenlibao.p2p.dao.mq.MemberPointsDao;
import com.fenlibao.p2p.dao.mq.PointsCleanerDao;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.mp.entity.MyPoint;
import com.fenlibao.p2p.model.mp.entity.PointsType;
import com.fenlibao.p2p.model.mp.enums.topup.ClearStatus;
import com.fenlibao.p2p.model.mp.enums.topup.PointsTypeEnum;
import com.fenlibao.p2p.model.mp.vo.PointsClearVO;
import com.fenlibao.p2p.service.mp.PointsCleanerService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.mp.PointChangeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Mingway.Xu
 * @date 2016/11/25 14:07
 */
@Service
public class PointsClearServiceImpl implements PointsCleanerService {
    @Resource
    private MemberPointsDao memberPointsDao;
    @Resource
    private PointsCleanerDao pointsCleanerDao;

    /**
     * 获取用户需要清除的积分信息
     * @return
     */
    public List<PointsClearVO> getUserPiontInfoForClear(Integer limit){
        return pointsCleanerDao.getUserPiontInfoForClear(limit);
    }

    @Transactional
    @Override
    public void clearUserPoints(PointsClearVO pointsClearVO, int needClearPoint) throws Exception {
        this.modifyUserAccountPointsNum(PointsTypeEnum.EXPIRE_POINTS.getTypeCode(),pointsClearVO.getUserId(),needClearPoint, InterfaceConst.PCT_OUTGOINGS,"系统自动清理过期积分");
        pointsCleanerDao.updateMemberPointsStatus(pointsClearVO.getUserId(), ClearStatus.YES);
        pointsCleanerDao.insertAutoClearPointsRecord(pointsClearVO.getUserId(), needClearPoint);
    }

    private void modifyUserAccountPointsNum(String pTypeCode, int userId, int pNum, int pointChangeType, String remark) throws Exception{
        //锁定用户账户积分数量
        MyPoint myPointAccount = memberPointsDao.getMyPointsNum(userId);
        int userPointNum = myPointAccount.getPointNum();

        //账户剩余积分
        int remainPointNum = 0;
        if(pointChangeType == PointChangeType.PCT_OUTGOINGS){
            remainPointNum = userPointNum - pNum ;
        }
        if(pointChangeType == PointChangeType.PCT_INCOME){
            remainPointNum = userPointNum + pNum ;
        }
        if(remainPointNum<0){
            throw new BusinessException(ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getCode(), ResponseCode.MP_MY_POINTS_ACCOUNT_REMAIN_LACK.getMessage());
        }

        PointsType pointsType = memberPointsDao.getPointsTypeInfo(pTypeCode);
        if (pointsType == null || pointsType.getId() == 0 ){
            throw new BusinessException(ResponseCode.MP_INTEGRAL_NOT_EXIST);
        }
        Date nowDatetime = DateUtil.nowDate();
        memberPointsDao.modifyUserAccountPoints(userId,remainPointNum);
        memberPointsDao.addPointsSheetRecord(pointsType.getId(), userId, pNum, pointChangeType,remark, nowDatetime);
    }

    @Override
    public void updateMemberPointsStatus(Integer userId, ClearStatus clearStatus) {
        pointsCleanerDao.updateMemberPointsStatus(userId, clearStatus);
    }

    @Override
    public int getUserHoldPoints(int userId, Date startTime) {
        return pointsCleanerDao.getUserHoldPoints(userId,startTime);
    }
}
