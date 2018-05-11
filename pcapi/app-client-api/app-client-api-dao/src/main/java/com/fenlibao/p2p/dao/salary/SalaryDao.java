package com.fenlibao.p2p.dao.salary;

import com.fenlibao.p2p.model.entity.salary.BidInfo;
import com.fenlibao.p2p.model.entity.salary.SalaryInfo;
import com.fenlibao.p2p.model.entity.salary.UserXjbBidInfo;
import com.fenlibao.p2p.model.entity.salary.UserXjbJoinRecord;
import com.fenlibao.p2p.model.vo.SalaryDetailVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SalaryDao {
	/**
	 * 用户薪金宝指定日期的收益
	 * @param map
	 * @return
	 */
	BigDecimal getUserEarnByDate(Map<String, Object> map);

	/**
	 * 薪金宝投资总额
	 * @param userId
	 * @return
	 */
	BigDecimal getXjbInvestSum(String userId);

	/**
	 * 薪金宝投资冻结总额
	 * @param userId
	 * @return
	 */
	BigDecimal getXjbTenderFreezeSum(String userId);

	public SalaryInfo getSalaryInfo(Map<String,Object> map);
	
	public SalaryInfo getSalaryDetail(Map<String, Object> map);

	public List<BidInfo> getSalaryPlanHistoryList(Map<String, Object> map);
	
	public List<UserXjbBidInfo> getUserXjbBidList(Map<String, Object> map);
	
	public List<UserXjbBidInfo> getBuyBidList(Map<String, Object> map);
	
	//获取用户薪金宝记录
	public UserXjbJoinRecord getUserXjbJoinRecord(Map<String, Object> map);

	public BidInfo getContinueBuyBid(Map<String, Object> map);

	//获取标的期数
	public int getBidPeriodById(int loanId);
	
	//获取用户姓名
	public String getUserName(int userId);
	
	//获取薪金宝计划明细信息
	public SalaryDetailVO getSalaryDetailInfo(int salaryId);
	
	//判断用户是否已购买过当期该薪金宝计划
	public BidInfo isUserBided(Map map);
 
	//获取标的下一次投资日
	public BidInfo getBidNextInvestDay(Map map);
	
	//获取用户的下一次投资日
	public BidInfo getUserNextInvestDay(Map map);
	
	//根据userId investDay 获取用户第一次加入薪金宝计划的bid信息
	public BidInfo isUserHaveBid(Map map);

	/** 
	 * @Title: getUserXjbInvestDayList 
	 * @Description: 获取当前用户投资日的薪金宝计划列表
	 * @param map
	 * @return
	 * @return: List<UserXjbBidInfo>
	 */
	List<UserXjbBidInfo> getUserXjbInvestDayList(Map<String, Object> map);

}
