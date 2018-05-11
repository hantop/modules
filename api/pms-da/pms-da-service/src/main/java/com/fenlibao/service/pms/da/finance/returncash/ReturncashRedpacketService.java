package com.fenlibao.service.pms.da.finance.returncash;

import com.fenlibao.model.pms.da.finance.vo.ReturncachRedpacketVO;
import com.fenlibao.model.pms.da.finance.vo.UserUsedReturncachRedpacketVO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public interface ReturncashRedpacketService {

	/**
	 * 获取已使用的返现红包金额
	 *
	 * @param startDate
	 * @param endDate
	 * @param activityCode
	 * @param systemgrantFlag
	 * @return
	 */
	BigDecimal findUsedTotalCost(Date startDate, Date endDate, String activityCode, boolean systemgrantFlag);

	/**
	 * 统计返现券的激活数量
	 * 
	 * @param startDate
	 * @param endDate
	 * @param activityCode
	 * @param systemgrantFlag
	 * @return
	 */
	Integer findTotlActiveCount(Date startDate, Date endDate, String activityCode, boolean systemgrantFlag);

	/**
	 * 获取已使用的返现红包详情
	 *
	 * @param startDate
	 * @param endDate
	 * @param redpacketId
	 * @param systemgrantFlag
	 * @param phoneNum
	 * @param bounds
	 * @return
	 */
	List<UserUsedReturncachRedpacketVO> findUserUsedReturncachRedpackets(Date startDate, Date endDate,
			Integer redpacketId, boolean systemgrantFlag, String phoneNum, RowBounds bounds);

	/**
	 * 获取已使用的返现红包
	 *
	 * @param startDate
	 * @param endDate
	 * @param activityCode
	 * @param systemgrantFlag
	 * @param bounds
	 * @return
	 */
	List<ReturncachRedpacketVO> findUsedReturncachRedpackets(Date startDate, Date endDate, String activityCode,
			boolean systemgrantFlag, RowBounds bounds);

	/**
	 * 统计返现券的总发送数量
	 * @param startDate
	 * @param endDate
	 * @param activityCode
	 * @param systemgrantFlag
	 * @return
	 */
	Integer findTotlRedNumber(Date startDate, Date endDate, String activityCode, boolean systemgrantFlag);

	/**
	 * 单独一种返现券的发送数量
	 * @param redpacketId
	 * @return
	 */
	Integer getEachRedPacketSendAmount(Date startDate,Date endDate,String activityCode,Integer redpacketId,boolean systemgrantFlag);

}
