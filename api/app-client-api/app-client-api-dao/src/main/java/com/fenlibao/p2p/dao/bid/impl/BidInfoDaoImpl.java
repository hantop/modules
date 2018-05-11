package com.fenlibao.p2p.dao.bid.impl;

import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.model.entity.PfBidInfoVo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: BidInfoDaoImpl 
 * @Description: 标基本信息查询dao
 * @author: laubrence
 * @date: 2016-3-3 下午4:33:42  
 */
@Repository
public class BidInfoDaoImpl implements BidInfoDao {
	
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "BidInfoMapper.";
    
    public BorrowerInfo getBorrowerInfo(int bidId) {
        return sqlSession.selectOne(MAPPER + "getBorrowerInfo", bidId);
	}

	@Override
	public BidBaseInfo getBidBaseInfoByUser(int userId, int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidBaseInfoByUser", map);	
	}

	@Override
	public BidBaseInfo getBidBaseInfo(int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidBaseInfo", map);
	}

	@Override
	public BidExtendInfo getBidExtendInfo(int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidExtendInfo", map);
	}

	@Override
	public BidExtendInfo getBidAllInfo(int bidId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getBidAllInfo", map);
	}

	@Override
	public int updateBidInfo(List<PfBidInfoVo> updateRows) {
		return sqlSession.update(MAPPER + "updateBidInfo", updateRows);
		
	}

	@Override
	public int addBidInfo() {
		return sqlSession.insert(MAPPER + "addBidInfo");
	}

	@Override
	public int addBidInvestrecords() {
		return sqlSession.insert(MAPPER + "addBidInvestrecords");
	}

	@Override
	public BidBorrowerInfo getBidBorrowerInfo(int bidId) {
		return sqlSession.selectOne(MAPPER + "getBidBorrowerInfo", bidId);
	}

	@Override
	public InvestShareVO getTenderIdLatest(int userId, int bidId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getTenderIdLatest", params);
	}

	@Override
	public Integer getUserInvestCount(int userId) {
		return sqlSession.selectOne(MAPPER + "getUserInvestCount", userId);
	}

	@Override
	public List<AutoTenderVO> getTBZ(VersionTypeEnum versionTypeEnum) {
		int versionType = versionTypeEnum == null ? versionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex();
		return sqlSession.selectList(MAPPER + "getTBZ", versionType);
	}
	@Override
	public DirectionalBid getDirectionalBid(int bidId) {
		Map<String, Integer> params = new HashMap<>(1);
		params.put("bidId", bidId);
		return sqlSession.selectOne(MAPPER + "getDirectionalBid", params);
	}

	@Override
	public List<AutoTenderVO> getCreditLoanTBZ(Integer type, VersionTypeEnum versionTypeEnum) {
		Map<String, Integer> params = new HashMap<>(1);
		params.put("type", type);
		params.put("versionType", versionTypeEnum == null ? versionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());

		return sqlSession.selectList(MAPPER + "getCreditLoanTBZ", params);
	}

	@Override
	public List<PfBidInfoVo> getNeedUpdateRows() {
		return sqlSession.selectList(MAPPER + "getNeedUpdateRows");
	}

	@Override
	public BidBorrowerInfoAfterLoan getBidBorrowerInfoAfterLoan(Integer bidId) {
		return sqlSession.selectOne(MAPPER + "getBidBorrowerInfoAfterLoan", bidId);
	}

	@Override
	public Integer countPlanBindBid(Integer planId) {
		return sqlSession.selectOne(MAPPER + "countPlanBindBid", planId);
	}

	@Override
	public List<Integer> getPlanBindBidList(Integer planId, Integer offset, Integer pageSize) {
    	Map map = new HashMap();
    	map.put("planId", planId);
    	map.put("offset", offset);
    	map.put("pageSize", pageSize);
		return sqlSession.selectList(MAPPER + "getPlanBindBidList", map);
	}

	@Override
	public String getNoSensitiveAgreementPath(String bidId) {
		Map map = new HashMap();
		map.put("bidId", bidId);
    	return sqlSession.selectOne(MAPPER + "getNoSensitiveAgreementPath", map);
	}

	@Override
	public Map getSignIdAndDocId(String bidId) {
		Map map = new HashMap();
		map.put("bidId", bidId);
    	return sqlSession.selectOne(MAPPER + "getSignIdAndDocId", map);
	}

}
