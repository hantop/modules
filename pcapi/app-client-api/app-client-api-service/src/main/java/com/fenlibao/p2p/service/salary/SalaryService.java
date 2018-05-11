package com.fenlibao.p2p.service.salary;

import com.fenlibao.p2p.model.entity.salary.BidInfo;
import com.fenlibao.p2p.model.vo.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 薪金宝相关
 */
public interface SalaryService {

	/**
	 * 获取薪金宝指定日期的收益
	 * @param userId
	 * @param earnDate
	 * @return
	 */
	BigDecimal getXjbEarning(String userId, Date earnDate);

	/**
	 * 获取当前开放的薪金宝计划
	 */
	SalaryVo getCurrentSalary(String userId);
	
	/**
	 * 获取薪金宝详情
	 */
	SalaryInfoVo getSalaryInfo(int salaryId);
	
	/**
	 * 获取薪金宝加入记录
	 * @param salaryId
	 * @param timestamp
	 * @return
	 */
	List<SalaryJoinRecordVo> getSalaryJoinRecord(int salaryId,String timestamp);
	
	/**
	 * 往期薪金宝列表
	 * @param timestamp
	 * @return
	 */
	List<SalaryPlanHistoryListVO> getXjbList(String timestamp);
	
	/**
	 * 薪金宝产品明细
	 * 
	 * @author zhaohongfeng 2015-12-2 13:46:43
	 */
	SalaryDetailVO getSalaryDetail(int salaryId);
	
	
	/**
	 * 判断用户是否已购买过当期该薪金宝计划
	 * 
	 * @author zhaohongfeng  2015-9-23 14:28:43
	 */
	BidInfo isUserCanBid(int bidId, int userId);
	
	
	/**
	 * 获取标的下次投资日
	 * @author zhaohongfeng 2015-8-28 09:05:55
	 */
	String getBidNextInvestDay(int bidId);
	
	/**
	 * 获取用户的下次投资日
	 * @author zhaohongfeng 2015-8-28 09:05:55
	 */
	
	String getUserNextInvestDay(int userXjbId);
	
	/**
	 * 判断该用户是否已经购买当月薪金宝
	 */
	public boolean isUserHaveBid(int userId);

	/** 
	 * @Title: getXjbBid 
	 * @Description: TODO
	 * @param userSalaryId 用户薪金宝计划id
	 * @return
	 * @return: Map<String,Object>
	 */
	Map<String, Object> getXjbBid(int userSalaryId);
	
	/** 
	 * @Title: getXjbBid 
	 * @Description: TODO
	 * @param userId
	 * @param investDay
	 * @return
	 * @return: Map<String,Object>
	 */
	Map<String, Object> getXjbBid(int userId, int investDay);

	/** 
	 * @Title: getUserXjbDayList 
	 * @Description: 获取当前用户投资日的未结清的薪金宝计划列表 
	 * @param userId
	 * @param investDay
	 * @return
	 * @return: List<SalaryUserListVO>
	 */
	List<SalaryUserListVO> getUserXjbInvestDayList(int userId, String investDay);

	/**
	 * 用户薪金宝计划列表
	 * @param userId
	 * @param timestamp
	 * @param investDay
	 * @return
	 * @author zhaohongfeng  2015-8-20 10:44:32
	 */
	List<SalaryUserListVO> getUserXjbList(int userId, String timestamp, String investDay);

}
