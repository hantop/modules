package com.fenlibao.p2p.dao.lottery.impl;

import com.fenlibao.p2p.dao.lottery.LotteryActivityDao;
import com.fenlibao.p2p.model.lottery.entity.ActivityInfo;
import com.fenlibao.p2p.model.lottery.entity.LotteryActivityInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class LotteryActivityDaoImpl implements LotteryActivityDao {

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "LotteryActivityMapper.";

    @Override
    public LotteryActivityInfo getLotteryActivityInfo(int activityId, String activityCode){
        Map<String,Object> map = new HashMap<>();
        if(activityId>0) {
            map.put("activityId", activityId);
        }
        if(StringUtils.isNotEmpty(activityCode)){
            map.put("activityCode",activityCode);
        }
        return this.sqlSession.selectOne(MAPPER+"getLotteryActivityInfo",map);
    }


    /**
     * 从 flb.t_activity 表中获取活动信息
     * @param id
     * @param code
     * @return
     */
    @Override
    public ActivityInfo getActivityInfo(int id, String code){
        Map<String,Object> map = new HashMap<>();
        if(id > 0) {
            map.put("id", id);
        }
        if(StringUtils.isNotEmpty(code)){
            map.put("code",code);
        }
        return this.sqlSession.selectOne(MAPPER+"getActivityInfo",map);
    }
}
