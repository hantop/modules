package com.fenlibao.p2p.dao.mq.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.mp.entity.*;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.mq.MemberPointsDao;

@Repository
public class MemberPointsDaoImpl implements MemberPointsDao {
	
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "MemberPointsMapper.";

	@Override
	public MyPoint getMyPointsNum(int userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
        return sqlSession.selectOne(MAPPER + "getMyPointsNum", map);
	}

	@Override
	public List<UserPointDetail> getPointRecordsThirty(int userId, Date nowDatetime) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("nowDatetime", nowDatetime);
		return sqlSession.selectList(MAPPER + "getPointRecordsThirty", map);
	}

	@Override
	public List<UserPointDetail> getPointRecords(int userId, int changeType, Date createTime) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("changeType", changeType);
		map.put("createTime", createTime);
		return sqlSession.selectList(MAPPER + "getPointRecords", map);
	}

	@Override
	public PointsExchangeCashInfo getPointsExchangeCashConfigInfo(String pTypeCode, Date nowDatetime) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pTypeCode", pTypeCode);
		map.put("nowDatetime", nowDatetime);
		return sqlSession.selectOne(MAPPER + "getPointsExchangeCashConfigInfo", map);
	}
	
	@Override
	public PointsType getPointsExchangeCashParentInfo(String pTypeCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pTypeCode", pTypeCode);
		return sqlSession.selectOne(MAPPER + "getPointsExchangeCashParentInfo", map);
	}
	
	@Override
	public PointsType getPointsTypeInfo(String pTypeCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pTypeCode", pTypeCode);
		return sqlSession.selectOne(MAPPER + "getPointsTypeInfo", map);
	}

	@Override
	public ConsumeExchangePointsInfo getConsumeExchangePointsConfigInfo(
			String pTypeCode, Date nowDatetime) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pTypeCode", pTypeCode);
		map.put("nowDatetime", nowDatetime);
		return sqlSession.selectOne(MAPPER + "getConsumeExchangePointsConfigInfo", map);
	}

	@Override
	public int addPointsUseRecord(int typeId, int userId, int pNum, BigDecimal cashAmount, int exStatus, Date nowDatetime){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("typeId", typeId);
		map.put("userId", userId);
		map.put("pNum", pNum);
		map.put("cashAmount", cashAmount);
		map.put("exStatus", exStatus);
		map.put("nowDatetime", nowDatetime);
		sqlSession.insert(MAPPER + "addPointsUseRecord", map);
		System.out.println(map.get("rId"));
		if(map.get("rId")!=null){
			return Integer.valueOf(String.valueOf(map.get("rId")));
		}
		return 0 ;
	}
	
	@Override
	public int addPointsSheetRecord(int typeId, int userId, int pNum, int changeType, Date nowDatetime){
		return this.addPointsSheetRecord(typeId,userId,pNum,changeType,"",nowDatetime);
	}

	@Override
	public int addPointsSheetRecord(int typeId, int userId, int pNum, int changeType, String remark, Date nowDatetime){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("typeId", typeId);
		map.put("userId", userId);
		map.put("pNum", pNum);
		map.put("changeType", changeType);
		map.put("remark", remark);
		map.put("nowDatetime", nowDatetime);
		sqlSession.insert(MAPPER + "addPointsSheetRecord", map);
		if(map.get("sId")!=null){
			return Integer.valueOf(String.valueOf(map.get("sId")));
		}
		return 0 ;
	}

	/**
	 * @Title: updatePointsUseRecordStatus
	 * @Description: 更新积分记录使用状态
	 * @param id
	 * @param exStatus
	 * @param nowDatetime
	 * @return 
	 * @see com.fenlibao.p2p.dao.mq.MemberPointsDao#updatePointsUseRecordStatus(int, int, java.util.Date) 
	 */
	@Override
	public int updatePointsUseRecordStatus(int id, int exStatus, Date nowDatetime) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("exStatus", exStatus);
		map.put("nowDatetime", nowDatetime);
		return sqlSession.update(MAPPER + "updatePointsUseRecordStatus", map);
	}

	/**
	 * @Title: minusUserAccountPoints
	 * @Description: 积分账户积分减少
	 * @param userId
	 * @param remainPointNum
	 * @return 
	 * @see com.fenlibao.p2p.dao.mq.MemberPointsDao#minusUserAccountPoints(int, int) 
	 */
	@Override
	public int minusUserAccountPoints(int userId, int remainPointNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("remainPointNum", remainPointNum);
		return sqlSession.update(MAPPER + "minusUserAccountPoints", map);
	}
	
	/** 
	 * @Title: getUserPointsUseSucRecord 
	 * @Description: 获取积分类型及子类型成功使用记录
	 * @param userId
	 * @param pTypeCodeList
	 * @param isByYear 
	 * @param isByMonth
	 * @param isByDay
	 * @param nowDatetime
	 * @return
	 * @return: List<PointsUseRecord>
	 */
	@Override
	public List<PointsUseRecord> getUserPointsUseSucRecord(int userId, List<String> pTypeCodeList, int isByYear, int isByMonth, int isByDay, Date nowDatetime) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("pTypeCodeList", pTypeCodeList);
		map.put("isByYear", isByYear);
		map.put("isByMonth", isByMonth);
		map.put("isByDay", isByDay);
		map.put("nowDatetime", nowDatetime);
		return sqlSession.selectList(MAPPER + "getUserPointsUseSucRecord", map);
	}

	/**
	 * @Title: getTypeChildrenCodeList
	 * @Description: 获取当前积分类型的子积分类型列表
	 * @param pTypeCode
	 * @return 
	 * @see com.fenlibao.p2p.dao.mq.MemberPointsDao# getTypeChildrenCodeList(java.lang.String)
	 */
	@Override
	public List<PointsType> getPointsTypeChildrenList(String pTypeCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pTypeCode", pTypeCode);
		return sqlSession.selectList(MAPPER + "getPointsTypeChildrenList", map);
	}

	/**
	 * @Title: modifyUserAccountPoints
	 * @Description: 修改积分账户积分
	 * @param userId
	 * @param remainPointNum
	 * @return
	 */
	@Override
	public int modifyUserAccountPoints(int userId, int remainPointNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("remainPointNum", remainPointNum);
		return sqlSession.update(MAPPER + "modifyUserAccountPoints", map);
	}

	@Override
	public MyPointInfo getMyPoints(int userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
		return sqlSession.selectOne(MAPPER + "getMyPoints", map);
	}

	@Override
	public List<MyPointExchangeDetail> getExchangeRecords(int userId, Integer startPageIndex, Integer limit) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("startPageIndex", startPageIndex);
		map.put("limit", limit);
		return sqlSession.selectList(MAPPER + "getExchangeRecords", map);
	}



}
