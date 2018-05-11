package com.fenlibao.p2p.service.mp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.mp.entity.MyPoint;
import com.fenlibao.p2p.model.mp.entity.MyPointExchangeDetail;
import com.fenlibao.p2p.model.mp.entity.MyPointInfo;
import com.fenlibao.p2p.model.mp.entity.UserPointDetail;
import org.springframework.transaction.annotation.Transactional;


/**
 * @title  积分service
 * @author laubrence
 * @date   2016-2-6 00:21:25
 */
public interface MemberPointsService {
	
	 /**
     * 获取用户积分
     * @param userId
     * @return
     */
    MyPoint getMyPointsNum(int userId) throws Exception;

    /**
     * @title 用户最近30天积分使用记录查询
     * @param userId
     * @return
     */
    List<UserPointDetail> getPointRecordsThirty(int userId);
    
    
    /**
     * @title 积分使用记录查询(全部/收入/支出)
     * @param userId
     * @param changeType
     * @param createTime
     * @return
     */
	List<UserPointDetail> getPointRecords(int userId, int changeType,
			String createTime);

	/**
	 * @title 消费兑换积分数量
	 * @param pTypeCode
	 * @param amount 
	 * @return
	 * @throws Exception 
	 */
	int getConsumeExchangePointsNum(String pTypeCode, BigDecimal amount) throws Exception;

	/**
	 * @title 积分兑换金额操作
	 * @param userId
	 * @param pTypeCode
	 * @param pNum
	 * @return 
	 * @throws Exception 
	 */
	Map<String, Object> pointsExchangeCash(int userId, String pTypeCode, int pNum) throws Exception;
	
	/**
	 * @title 更新积分使用记录状态(1:已申请;2:兑换成功;3:兑换失败)
	 * @param id
	 * @param exStatus
	 * @param nowDatetime
	 * @return 大于0则表示更新成功
	 */
	int updatePointsUseRecordStatus(int id, int exStatus) throws Exception;

	/** 
	 * @Title: pointsCanExchangeCash 
	 * @Description: 积分类型使用限制
	 * @param userId
	 * @param pTypeCode
	 * @param pNum
	 * @return
	 * @throws Exception
	 * @return: boolean
	 */
	boolean pointsCanExchangeCash(int userId, String pTypeCode, int pNum) throws Exception;

	/** 
	 * @Title: minusUserAccountPoints 
	 * @Description: 减少用户账户积分
	 * @param userId
	 * @param pNum
	 * @return
	 * @throws Exception
	 * @return: int
	 */
	int minusUserAccountPoints(int userId, int pNum) throws Exception;

	/**
	 * 修改账户积分
	 * @param pTypeCode
	 * @param userId
	 * @param pNum
	 * @param pointChangeType
     * @throws Exception
     */
	void modifyUserAccountPointsNum(String pTypeCode, int userId, int pNum, int pointChangeType) throws Exception;

	@Transactional
	void modifyUserAccountPointsNum(String pTypeCode, int userId, int pNum, int pointChangeType, String remark) throws Exception;

	/**
	 * 获取用户积分(这里用于展示，不管是否冻结)
	 * @param userId
	 * @return
	 */
	MyPoint getPoints(int userId) throws Exception;

	/**
	 * @title 积分兑换现金
	 * @param pTypeCode
	 * @param pNum
	 * @return
	 * @throws Exception
	 */
	BigDecimal getPointsExchangeCashAmount(String pTypeCode, int pNum) throws Exception;


	/**
	 * 获取用户积分(这里用于展示，不管是否冻结)
	 * @param userId
	 * @return
	 */
	MyPointInfo getMyPoints(int userId) throws Exception;
	/**
	 * @title 积分兑换记录查询
	 * @param userId
	 * @return
	 */
	List<MyPointExchangeDetail> getExchangeRecords(int userId, Integer page,
												   Integer limit);
}

