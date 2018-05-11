package com.fenlibao.platform.service.integral;

import com.fenlibao.platform.model.integral.PointsRecord;
import com.fenlibao.platform.model.integral.PointsType;
import com.fenlibao.platform.model.member.MemberConsumeRecord;
import com.fenlibao.platform.model.member.MemberPoints;

/**
 * 积分接口
 * Created by Lullaby on 2016/2/19.
 */
public interface IntegralService {

    PointsType getPointsType(String typecode);

    int amountToPoints(String amount, int pointsTypeId) throws Exception;

    int saveMemberPoints(MemberPoints points);

    MemberConsumeRecord getMemberConsumeRecord(String pos_sn);

    int updateMemberPointsRecord(PointsRecord record);

    PointsRecord getMemberPointsRecord(int userId);

    /**
     * 获取积分账号ID
     * @param userId
     * @return
     */
    Integer getId(Integer userId);
    
    /**
     * 获取用户积分
     * @param openid
     * @return
     */
    Integer getPoints(String appid, String openid) throws Exception;

    PointsRecord getMemberPointsRecordByOpenid(String openid);

    int validateThisMonthPoints(int gainPoints, String typecode, int pfUserId, String openid);
}
