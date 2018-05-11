package com.fenlibao.p2p.dao.mq.topup.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpErrorRecord;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.mq.topup.ITopUpDao;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpOrderInchargeEntity;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;
import com.fenlibao.p2p.model.mp.entity.topup.ParvalueEntity;
import com.fenlibao.p2p.model.mp.enums.topup.ParvalueType;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpOrderRecord;
import com.fenlibao.p2p.model.mp.vo.topup.ParvalueVO;

@Repository
public class TopUpDaoImpl implements ITopUpDao {
	
	@Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "TopUpMapper.";

	@Override
	public List<ParvalueVO> getParvalue(ParvalueType type) {
		return sqlSession.selectList(MAPPER + "getParvalue", type.getCode());
	}

	@Override
	public ParvalueEntity getParvalueByCode(String code) {
		return sqlSession.selectOne(MAPPER + "getParvalueByCode", code);
	}

	@Override
	public void addOrder(MobileTopupOrderEntity order) throws Exception {
		sqlSession.insert(MAPPER + "addOrder", order);
	}

	@Override
	public MobileTopupOrderEntity getOrder(Map<String,Object> map) {
		return sqlSession.selectOne(MAPPER + "getOrder", map);
	}

	@Override
	public void updateOrder(MobileTopupOrderEntity order) {
		 this.sqlSession.update(MAPPER+"updateOrder", order);
	}

	@Override
	public List<ParvalueVO> getParvalue(Map<String, Object> map) {
		return sqlSession.selectList(MAPPER + "getParvalueIno", map);
	}

	@Override
	public int getFrequency(int userId, int days) {
		Map<String, Integer> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("days", days);
		return sqlSession.selectOne(MAPPER + "getFrequency", params);
	}

	@Override
	public int getFrequencyByMonth(int userId) {
		return sqlSession.selectOne(MAPPER + "getFrequencyByMonth", userId);
	}

	@Override
	public void addYishangOrder(MobileTopupOrderEntity order) throws Exception {
	   sqlSession.insert(MAPPER + "addYishangOrder", order);
	}

	@Override
	public List<MobileTopUpOrderRecord> getMobileTopupOrderList(int userId, int pageNum){
		Map<String,Object> map = new HashMap<String,Object>();
		int limit=20;
		map.put("userId", userId);
		map.put("startPageIndex", (pageNum-1)*limit);
		
		map.put("limit",limit);
		
		return sqlSession.selectList(MAPPER+"getOrderRecordList", map);
	}

	@Override
	public List<MobileTopUpOrderInchargeEntity> getOrderListInCharge(Integer page,Integer limit) {
		if(page==null||page<1)page=1;
		if(limit==null)limit=200;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("page", (page-1)*limit);
		map.put("limit", limit);
		return sqlSession.selectList(MAPPER+"getOrderListInCharge",map);
	}

	@Override
	public int addTopupErrorRecord(MobileTopupOrderEntity entity) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",entity.getUserId());
		map.put("orderId",entity.getId());
		map.put("orderNum",entity.getOrderNum());
		return sqlSession.insert(MAPPER + "addTopupErrorRecord", map);
	}
	@Override
	public List<MobileTopUpErrorRecord> yishangErrorRecords() throws Exception {
		return sqlSession.selectList(MAPPER+"yishangErrorRecords");
	}

	@Override
	public MobileTopUpErrorRecord getYishangErrorRecord(Map map) throws Exception {
		return this.sqlSession.selectOne(MAPPER+"getYishangErrorRecord", map);
	}


	@Override
	public int updateErrorRecords(MobileTopUpErrorRecord errorRecord) throws Exception {
		return this.sqlSession.update(MAPPER+"updateErrorRecords", errorRecord);
	}

	@Override
	public Integer getUserTopUpSameMonth(int userId) {
		return this.sqlSession.selectOne(MAPPER+"getUserTopUpSameMonth", userId);
	}


}
