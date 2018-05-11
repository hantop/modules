package com.fenlibao.p2p.dao.mq.impl;

import com.fenlibao.p2p.dao.mq.PointsCleanerDao;
import com.fenlibao.p2p.model.mp.enums.topup.ClearStatus;
import com.fenlibao.p2p.model.mp.vo.PointsClearVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mingway.Xu
 * @date 2016/11/25 17:30
 */
@Repository
public class PointsCleanerDaoImpl implements PointsCleanerDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "PointsCleanerMapper.";
    /**
     * 插入一条用户积分清理记录
     * @param userId
     * @param realAccount
     */
    @Override
    public int insertAutoClearPointsRecord(int userId, int realAccount) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", userId);
        map.put("realAccount", realAccount);
        return sqlSession.insert(MAPPER + "insertAutoClearPointsRecord", map);

    }
    /**
     * 获取用户积分信息-用于清除
     * @return
     */
    @Override
    public List<PointsClearVO> getUserPiontInfoForClear(Integer limit){
        Map<String,Object> map = new HashMap<>(1);
        map.put("limit", limit);
        return sqlSession.selectList(MAPPER + "getUserPiontInfoForClear",map);
    }

    @Override
    public void updateMemberPointsStatus(int userId, ClearStatus clearStatus) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("userId", userId);
        map.put("clearStatus", clearStatus.getStatus());
        sqlSession.update(MAPPER + "updateMemberPointsStatus", map);
    }

    @Override
    public int getUserHoldPoints(int userId, Date startTime) {
        Map<String,Object> map = new HashMap<>(2);
        map.put("userId", userId);
        map.put("startTime", startTime);
        return sqlSession.selectOne(MAPPER + "getUserHoldPoints",map);
    }
}
