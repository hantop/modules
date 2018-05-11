package com.fenlibao.p2p.dao.lottery.impl;

import com.fenlibao.p2p.dao.lottery.LotteryPrizeDao;
import com.fenlibao.p2p.model.lottery.entity.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LotteryPrizeDaoImpl implements LotteryPrizeDao {

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "LotteryPrizeMapper.";

    @Override
    public List<LotteryPrizeInfo> getLotteryPrizeInfos(int activityId) {
        Map<String,Object> map = new HashMap<>();
        if(activityId>0) {
            map.put("activityId", activityId);
        }
        return this.sqlSession.selectList(MAPPER+"getLotteryPrizeInfos",map);
    }

    @Override
    public List<LotteryPrizeInfo> getEffectivePrizeInfos(int activityId) {
        Map<String,Object> map = new HashMap<>();
        if(activityId>0) {
            map.put("activityId", activityId);
        }
        map.put("probability", 1);
        map.put("quantity", 1);
        return this.sqlSession.selectList(MAPPER+"getLotteryPrizeInfos",map);
    }

    @Override
    public int updateUserLotteryAvailTimes(int recordId) {
        Map<String,Object> map = new HashMap<>();
        map.put("recordId",recordId);

        return this.sqlSession.update(MAPPER+"updateUserLotteryAvailTimes",map);
    }

    @Override
    public int addLotteryDrawRecord(int activityId, int userId, String cellTailNumber, int prizeId) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("cellTailNumber", cellTailNumber);
        map.put("prizeId", prizeId);
        map.put("activityId",activityId);

        this.sqlSession.insert(MAPPER+"addLotteryDrawRecord",map);
        if(map.get("rId")!=null){
            return Integer.valueOf(String.valueOf(map.get("rId")));
        }
        return 0;
    }

    @Override
    public UserLotteryTimes getUserLotteryTimes(int activityId, int userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("userId", userId);

        return this.sqlSession.selectOne(MAPPER+"getUserLotteryTimes",map);
    }

    @Override
    public UserLotteryTimes lockUserLotteryTimes(int recordId) {
        Map<String,Object> map = new HashMap<>();
        map.put("recordId", recordId);
        return this.sqlSession.selectOne(MAPPER+"lockUserLotteryTimes",map);
    }

    @Override
    public UserLotteryRecord getUserLotteryOneRecord(int activityId, int recordId) {
        Map<String,Object> map = new HashMap<>();
        map.put("activityId", activityId);
        map.put("recordId", recordId);
        return this.sqlSession.selectOne(MAPPER+"getUserLotteryOneRecord",map);
    }

    /**
     * 获取一条用户在某活动中的抽奖次数
     * @param activityId
     * @return
     * @throws Exception
     */
    @Override
    public int getUserLotteryCount(int activityId, int userId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityId", activityId);
        map.put("userId", userId);
        return this.sqlSession.selectOne(MAPPER+"getUserLotteryCount",map);
    }

    @Override
    public List<UserInvestBoard> getBoardDayRichestList(Date currentDate, int limit, int userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("currentDate", currentDate);
        map.put("limit", limit);
        map.put("userId", userId);
        return this.sqlSession.selectList(MAPPER+"getBoardDayRichestList",map);
    }

    @Override
    public List<UserLotteryDrawPrizeRecord> getUserDrawPrizes(int userId, int activityId, Date drawDate) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("activityId", activityId);
        map.put("drawDate", drawDate);
        return this.sqlSession.selectList(MAPPER+"getUserDrawPrizes",map);
    }

    @Override
    public List<UserLotteryDrawPrizeRecord> getUserThisDayDrawPrizes(int userId, int activityId, Date drawDate) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("activityId", activityId);
        map.put("drawDate", drawDate);
        return this.sqlSession.selectList(MAPPER+"getUserThisDayDrawPrizes",map);
    }

    @Override
    public List<UserLotteryDrawPrizeRecord> getUserLatestDrawPrizes(int activityId, int size) {
        Map<String,Object> map = new HashMap<>();
        map.put("activityId", activityId);
        map.put("size", size);
        return this.sqlSession.selectList(MAPPER+"getUserLatestDrawPrizes",map);
    }

    /**
     * 修改奖品数量
     * @param activityId
     * @param prizeId
     * @param quantity
     * @return
     */
    public int updatePrizeQuantity(int activityId ,int prizeId, int quantity){
        Map<String,Object> map = new HashMap<>();
        map.put("activityId", activityId);
        map.put("prizeId", prizeId);
        map.put("quantity", quantity);
        return sqlSession.update(MAPPER + "updatePrizeQuantity", map);
    }

    /**
     * 修改奖品概率
     * @param activityId
     * @param prizeId
     * @return
     */
    public int updatePrizeProbability(int activityId ,int prizeId, int quantity){
        Map<String,Object> map = new HashMap<>();
        map.put("activityId", activityId);
        map.put("prizeId", prizeId);
        map.put("quantity", quantity);
        return sqlSession.update(MAPPER + "updatePrizeProbability", map);
    }
}
