package com.fenlibao.p2p.dao.financing;

import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.finacing.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FinacingDao {

	/**
	 * 可转让的债权
	 */
	public List<Finacing> getMaySettleFinacing(Map<String, Object> map);
	
	/**
	 * 获取债权
	 */
	List<Finacing> getFinacingList(Map<String, Object> map);	
	/**
	 * 债权详情
	 */
	public Finacing getFinacingDetail(Map<String, Object> map);
	
	/**
	 * 标的债权
	 * @param map
	 * @return
	 */
	public List<Finacing> getFinacingByBid(Map<String, Object> map);
	
	/**
	 * 投资记录
	 * @param map
	 * @return
	 */
	public List<Finacing> getInvestRecord(Map<String, Object> map);
	
	
	/**
	 * 获取债权投资资产
	 * @param userId
	 * @return
	 */
	BigDecimal getZqzrAssets(String userId);

	/**
	 * 获取用户投资债权信息
	 * @param creditId
	 * @return
     */
	CreditInfo getUserCreditInfo(int creditId);

	/**
	 * 用户投资列表
	 * @param userId
	 * @param isUp
	 * @param time
     * @param bidStatus
	 * @return
     */
	List<InvestInfo> getUserInvestList(int userId, int isUp, Date time, String[] bidStatus);

	/**
	 * 用户投资详情
	 * @param userId
	 * @param creditId
     * @return
     */
	InvestInfoDetail getUserInvestDetail(String userId, String creditId);

	/**
	 * 计算用户债权的待收本息
	 * @param userId
	 * @param creditId
     * @param repaymentStatus
	 * @return
     */
	double getUserCollectInterest(int userId, int creditId, String[] repaymentStatus);

	/**
	 * 获取用户投资的债权的还款计划
	 * @param userId
	 * @param creditId
	 * @param tradeTypes
     * @return
     */
	List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId, int[] tradeTypes);

	/**
	 * 获取用户最近一次未还债权的还款计划记录
	 */
	List<RepaymentInfo> getNextRepaymentItem(int userId, int creditId, int[] tradeTypes);

	/**
	 * 获取用户投资债权的下一期还款收益
	 */
	Map getNextRepaymentItemProfit(int userId, int creditId, int[] tradeTypes);


	RepaymentInfo getLastRepaymentItem(int userId, int creditId, int[] tradeTypes);

	/**
	 * 获取用户所有的还款计划
	 * @param userId
	 * @param type
     * @return
     */
	public List<RepaymentInfoExt> getAllUserRepaymentItem(int userId, int type, Integer pageNo, Integer pagesize);

	/**
	 * 获取用户所有的还款计划 mode为1：顺序取30条，2：顺序取剩余当天的
	 * @param userId
	 * @param type
     * @return
     */
	public List<RepaymentInfoExt> getAllUserRepaymentItem(int userId, int type, VersionTypeEnum versionTypeEnum, Date timestamp, int mode);
	
	/**
	 * 回款总额
	 * @param userId
     * @return
     */
	public List<UserRepaymentAmout> userRepaymentAmout(int userId);
	/**
	 * 回款总额
	 * @param userId
	 * @return
	 */
	public List<UserRepaymentAmout> userRepaymentAmout(int userId,int versionTypeEnum);

	/**
	 * 计划回款详情
	 * @param planRecordId
	 * @return
	 */
	public List<PlanRepaymentDetail> getPlanRepaymentDetail(Integer planRecordId);
}
