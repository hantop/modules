package com.fenlibao.p2p.dao.bid.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.bid.BidExtendDao;
import com.fenlibao.p2p.model.entity.bid.BidExtendGroupInfo;
import com.fenlibao.p2p.model.entity.bid.BidExtendGroupItemInfo;
import com.fenlibao.p2p.model.form.user.AccountAssetsForm;

/**
 * Created by LouisWang on 2015/8/14.
 */
@Repository
public class BidExtendDaoImpl implements BidExtendDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "BidExtendMapper.";

    @Override
    public BigDecimal getHqbUserEarnByDate(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "getHqbUserEarnByDate", map);
    }

    @Override
    public BigDecimal getHqbInvestSum(String userId) {
        return sqlSession.selectOne(MAPPER + "getHqbInvestSum", userId);
    }

    @Override
    public BigDecimal getKdbInvestSum(String userId) {
        return sqlSession.selectOne(MAPPER + "getKdbInvestSum", userId);
    }

    @Override
    public BigDecimal getTenderFreezeSum(String userId) {
        return sqlSession.selectOne(MAPPER + "getTenderFreezeSum", userId);
    }

    @Override
    public BigDecimal getKdbTenderFreezeSum(String userId) {
        return sqlSession.selectOne(MAPPER + "getKdbTenderFreezeSum", userId);
    }

    @Override
    public BigDecimal getHqbTenderFreezeSum(String userId) {
        return sqlSession.selectOne(MAPPER + "getHqbTenderFreezeSum", userId);
    }

    @Override
    public List getBidInvestRecords(Map map) {
            return sqlSession.selectList(MAPPER + "getBidInvestRecords",map);
    }

    @Override
    public List getBidInvestAllRecords(Map map) {
        return sqlSession.selectList(MAPPER + "getBidInvestAllRecords",map);
    }

    @Override
    public List getRedPackets(Map map) throws Exception {
        return sqlSession.selectList(MAPPER + "getRedPackets",map);
    }
    
    @Override
    public List<BidExtendGroupInfo> getBidExtendGroupInfo(String groupCode, int bidId ) throws Exception {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("groupCode", groupCode);
    	map.put("bidId", bidId);
        return sqlSession.selectList(MAPPER + "getBidExtendGroupInfo",map);
    }
    
    @Override
    public List<BidExtendGroupItemInfo> getBidExtendGroupItemInfo(int bidId, int groupId, String extCode) throws Exception {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("bidId", bidId);
    	map.put("groupId", groupId);
    	map.put("extCode", extCode);
    	return getBidExtendGroupItemInfo(map);
    }
    
    @Override
    public List<BidExtendGroupItemInfo> getBidExtendGroupItemInfo(int bidId, int groupId) throws Exception {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("bidId", bidId);
    	map.put("groupId", groupId);
    	return getBidExtendGroupItemInfo(map);
    }
    @Override
    public List<BidExtendGroupItemInfo> getBidExtendGroupItemInfo(int bidId, String extCode) throws Exception {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("bidId", bidId);
    	map.put("extCode", extCode);
    	return getBidExtendGroupItemInfo(map);
    }
    
    public List<BidExtendGroupItemInfo> getBidExtendGroupItemInfo(Map<String,Object> map) throws Exception {
        return sqlSession.selectList(MAPPER + "getBidExtendGroupItemInfo",map);
    }

	@Override
	public List<AccountAssetsForm> getInvestmentAssets(Integer userId) {
		 return sqlSession.selectList(MAPPER + "getInvestmentAssets", userId);
	}
}
