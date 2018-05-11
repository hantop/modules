package com.fenlibao.platform.dao.integral;

import com.fenlibao.platform.model.integral.MonthPoints;
import com.fenlibao.platform.model.integral.PointsConfig;
import com.fenlibao.platform.model.integral.PointsRecord;
import com.fenlibao.platform.model.integral.PointsType;
import com.fenlibao.platform.model.member.MemberConsumeRecord;
import com.fenlibao.platform.model.member.MemberPoints;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Lullaby on 2016/2/19.
 */
public interface IntegralMapper {

    PointsType getPointsType(@Param("typecode") String typecode);

    PointsConfig getPointsConfig(@Param("pointsTypeId") int pointsTypeId);

    int saveMemberPoints(MemberPoints points);

    PointsRecord getMemberPointsRecord(@Param("userId") int userId);

    MemberConsumeRecord getMemberConsumeRecord(@Param("pos_sn") String pos_sn);

    int updateMemberPointsRecord(PointsRecord record);

    /**
     * 获取积分账号ID
     * @param userId
     * @return
     */
    Integer getId(Integer userId);
    
    /**
     * 获取用户积分数量
     * @param userId
     * @return
     */
    Integer getPoints(String userId);

    PointsRecord getMemberPointsRecordByOpenid(String openid);

    MonthPoints getThisMonthPoints(@Param("typeCode") String typeCode, @Param("openId") String openId);

    int insertMonthPoints(MonthPoints monthPoints);

    int updateThisMonthPoints(@Param("id") int id, @Param("userId") int pfUserId, @Param("sumPoints") int sumPoints);
}
