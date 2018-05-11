package com.fenlibao.p2p.dao.mq;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fenlibao.p2p.model.mp.entity.*;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;

public interface MemberPointsDao {

    /**
     * @title 获取用户积分
     * @param userId
     * @return
     */
    MyPoint getMyPointsNum(int userId);

	/**
	 * 获取用户积分(这里用于展示，不管是否冻结)
	 * @param userId
	 * @return
	 */
	MyPointInfo getMyPoints(int userId) throws Exception;
    
    /**
     * @title 用户最近30条积分使用记录查询
     * @param userId
     * @return
     */
    List<UserPointDetail> getPointRecordsThirty(int userId, Date nowDatetime);
    
    
    /**
     * @title 积分使用记录查询(全部/收入/支出)
     * @param userId
     * @param changeType
     * @param createTime
     * @return
     */
    List<UserPointDetail> getPointRecords(int userId, int changeType, Date createTime );
    
	/**
	 * @Title 积分兑换金额配置信息
	 * @param pTypeCode
	 * @param nowDatetime
	 * @return PointsExchangeCashInfo
	 */
    PointsExchangeCashInfo getPointsExchangeCashConfigInfo(String pTypeCode, Date nowDatetime);
    
	/** 
	 * @Title: 获取积分类型父节点 
	 * @Description: TODO
	 * @param pTypeCode
	 * @return
	 * @return: PointsType
	 */
    PointsType getPointsExchangeCashParentInfo(String pTypeCode);
    
    /** 
	 * @Title: getPointsTypeInfo 
	 * @Description: 获取积分类型信息
	 * @param pTypeCode
	 * @return
	 * @return: PointsType
	 */
	PointsType getPointsTypeInfo(String pTypeCode);
    
    /**
	 * @title 消费金额兑换现金配置信息
	 * @param pTypeCode
	 * @return
	 */
    ConsumeExchangePointsInfo getConsumeExchangePointsConfigInfo(String pTypeCode, Date nowDatetime);

	/**
	 * @title 添加积分使用记录
	 * @param typeId
	 * @param userId
	 * @param pNum
	 * @param cashAmount 
	 * @param exStatus
	 * @param nowDatetime
	 */
	int addPointsUseRecord(int typeId, int userId, int pNum, BigDecimal cashAmount, int exStatus, Date nowDatetime);

	int addPointsSheetRecord(int typeId, int userId, int pNum, int changeType, String remark, Date nowDatetime);

	/**
	 * @title 更新积分使用记录状态
	 * @param id
	 * @param exStatus
	 * @param nowDatetime
	 */
	int updatePointsUseRecordStatus(int id, int exStatus, Date nowDatetime);

	/**
	 * @title 减少用户账户积分
	 * @param userId
	 * @param remainPointNum
	 * @return
	 */
	int minusUserAccountPoints(int userId, int remainPointNum);
	
	/** 
	 * @Title: getUserPointsUseSucRecord 
	 * @Description: 获取某个时间段内用户积分使用记录
	 * @param userId
	 * @param pTypeCodeList
	 * @param isByYear
	 * @param isByMonth
	 * @param isByDay
	 * @param nowDatetime
	 * @return
	 * @return: List<PointsUseRecord>
	 */
	List<PointsUseRecord> getUserPointsUseSucRecord(int userId, List<String> pTypeCodeList, int isByYear,
			int isByMonth, int isByDay, Date nowDatetime);
	
	/**
	 * @title 添加积分使用明细记录
	 * @param typeId
	 * @param userId
	 * @param pNum
	 * @param changeType
	 * @param nowDatetime
	 */
	int addPointsSheetRecord(int typeId, int userId, int pNum, int changeType,Date nowDatetime);

	/** 
	 * @Title: getTypeChildrenCodeList 
	 * @Description: 获取当前积分类型的及子类型 
	 * @param pTypeCode
	 * @return
	 * @return: List<PointsType>
	 */
	List<PointsType> getPointsTypeChildrenList(String pTypeCode);

	int modifyUserAccountPoints(int userId, int remainPointNum);


	/**
	 * @title 积分兑换记录查询
	 * @param userId
	 * @return
	 */
	List<MyPointExchangeDetail> getExchangeRecords(int userId, Integer startPageIndex,
												   Integer limit);

}
