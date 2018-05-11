package com.fenlibao.p2p.dao.bid.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.bid.TenderShareSettingDao;
import com.fenlibao.p2p.model.entity.redenvelope.ShareRewardEntity;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资分享红包设置dao
 * @author Mingway.Xu
 * @date 2017/1/23 17:54
 */
@Repository
public class TenderShareSettingDaoImpl extends BaseDao implements TenderShareSettingDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "TenderShareSettingMapper.";

    @Override
    public Map getTenderShareSettingId(BigDecimal buyAmount) {
        return sqlSession.selectOne(MAPPER + "getTenderShareSettingId", buyAmount);
    }

    @Override
    public List<ShareRewardEntity> getTenderShareEntityList(int settingId,int isNovice) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("settingId", settingId);
        paramMap.put("isNovice", isNovice);
        return sqlSession.selectList(MAPPER + "getTenderShareEntityList", paramMap);
    }

    @Override
    public int getRestNumByInvestRecord(Map<String, Object> param) {
        return sqlSession.selectOne(MAPPER + "getRestNumByInvestRecord", param);
    }
}
