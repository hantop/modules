package com.fenlibao.p2p.dao.user.impl;

import com.fenlibao.p2p.dao.user.AutoMatchBidDao;
import com.fenlibao.p2p.model.entity.bid.UserBidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserNewCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserPlanInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户自动投设置
 */
@Repository
public class AutoMatchBidDaoImpl implements AutoMatchBidDao {
    private static final String MAPPER = "UserAutoMatchBidMapper.";
    @Resource
    private SqlSession sqlSession;

    /**
     * 从用户投资计划表获取已经投资了的记录
     */
    @Override
    public UserPlanInfo getSuitableUserPlanInfo(Timestamp dbCurrentTime, Timestamp lastCreateTime, VersionTypeEnum versionTypeEnum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dbTime", dbCurrentTime);
        map.put("lastCreateTime", lastCreateTime);
        map.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
        return this.sqlSession.selectOne(MAPPER + "getSuitableUserPlanInfo", map);
    }

    /**
     * 记录加锁
     */
    @Override
    public int lockUserPlan(int userPlanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPlanId", userPlanId);
        return this.sqlSession.selectOne(MAPPER + "lockUserPlan", map);
    }

    /**
     * 从用户投资计划表获取投资记录
     */
    @Override
    public UserPlanInfo getSuitableUserPlanInfoById(int userPlanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPlanId", userPlanId);
        return this.sqlSession.selectOne(MAPPER + "getSuitableUserPlanInfoById", map);
    }

    /**
     * 查询从债权列表,排除自己发的和转的
     */
    @Override
    public List<UserCreditInfoForPlan> getPlanCreditList(String type, int userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("userId", userId);
        return this.sqlSession.selectList(MAPPER + "getPlanCreditList", map);
    }

    /**
     * 获取债权转让后的id
     */
    @Override
    public UserNewCreditInfoForPlan getNewCreditInfo(int applyforId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyforId", applyforId);
        return this.sqlSession.selectOne(MAPPER + "getNewCreditId", map);
    }

    /**
     * 保存将购买记录
     */
    @Override
    public void insertUserPlanProduct(Map<String, Object> map) {
        this.sqlSession.insert(MAPPER + "insertUserPlanProduct", map);
    }

    /**
     * 查询标列表
     *
     * @param type xfxd,nxfxd
     * @param cgMode
     * @return
     */
    @Override
    public List<UserBidInfoForPlan> getPlanBidList(String type, int userId, int cgMode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("userId", userId);
        map.put("cgMode", cgMode);
        return this.sqlSession.selectList(MAPPER + "getPlanBidList", map);
    }

    /**
     * 查询标列表
     *
     * @param type xfxd,nxfxd
     * @param userPlanInfo
     * @return
     */
    @Override
    public List<UserBidInfoForPlan> getPlanBidList(String type, int userId, UserPlanInfo userPlanInfo)  {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("userId", userId);
        map.put("cgMode", userPlanInfo.getCgMode());
        map.put("cycle", userPlanInfo.getCycle());
        map.put("cycleType", userPlanInfo.getCycleType());
        return this.sqlSession.selectList(MAPPER + "getPlanBidList", map);
    }
}
