package com.fenlibao.p2p.dao.coupon.impl;

import com.fenlibao.p2p.dao.coupon.CouponDao;
import com.fenlibao.p2p.model.entity.coupons.RateCoupon;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.coupons.UserCouponStatisticsInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junda.feng 20160819
 */
@Repository
public class CouponDaoImpl implements CouponDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "CouponMapper.";

    @Override
    public void addUserRateCoupons(List<UserCouponInfo> userCouponInfos) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userCouponInfos", userCouponInfos);
        sqlSession.insert(MAPPER + "addUserRateCoupons", paramMap);
    }

    @Override
    public List<RateCoupon> getRateCoupons(String activityCode) {
        return sqlSession.selectList(MAPPER + "getRateCoupons", activityCode);
    }

    @Override
    public List<UserCouponInfo> getCoupons(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getMyConpons",paramMap);
    }

    @Override
    public int getMyConponsCount(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "getMyConponsCount", paramMap);
    }

    @Override
    public List<UserCouponInfo> getAddinterestList(Map<String, Object> paramMap) {
        return sqlSession.selectList(MAPPER + "getAddinterestList",paramMap);
    }

    @Override
    public int getCouponsCount(Map<String, Object> paramMap) {
        if(paramMap.get("bidId")==null){
            return sqlSession.selectOne(MAPPER + "getUserCouponsCount", paramMap);
        }else{
            return sqlSession.selectOne(MAPPER + "getCouponsCount", paramMap);
        }
    }

    @Override
    public int updateUserCoupon(Map<String, Object> paramMap) {
        return sqlSession.update(MAPPER + "updateUserCoupon", paramMap);
    }

    @Override
    public int updateUserCouponForPlan(Map<String, Object> paramMap) {
        return sqlSession.update(MAPPER + "updateUserCouponForPlan", paramMap);
    }

    @Override
    public int insertUserCouponExt(Map<String, Object> paramMap) {
        return sqlSession.insert(MAPPER + "insertUserCouponExt", paramMap);
    }

    @Override
    public int checkCouponBeforeInvest(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "checkCouponBeforeInvest", paramMap);
    }

    @Override
    public UserCouponStatisticsInfo getUserCouponStatistics(Map<String, Object> paramMap) {
        return sqlSession.selectOne(MAPPER + "getUserCouponStatistics", paramMap);
    }

    /**
     * 获取加息券
     *
     * @param jxqId
     * @return
     */
    @Override
    public UserCouponInfo getCoupon(int jxqId, int userId) {
        Map param = new HashMap();
        param.put("jxqId", jxqId);
        param.put("userId", userId);
        return sqlSession.selectOne(MAPPER + "getCouponById", param);
    }
}
