package com.fenlibao.p2p.dao.financing;

import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.finacing.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.math.BigDecimal;
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
	 * @param bidType
	 * @param bidStatus
     * @param pageBounds
	 * @return
     */
	List<InvestInfo> getUserInvestList(int userId, String bidType, String[] bidStatus, PageBounds pageBounds,int cgNum);

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

	RepaymentInfo getLastRepaymentItem(int userId, int creditId, int[] tradeTypes);

	/**
     * 获取用户各投资状态数量
     * @param userId
     * @return
     */
    Map<String, Map<String, String>> getInvestmentQty(Integer userId, VersionTypeEnum versionTypeEnum);


	/**
	 * 获取用户最近的投资记录
	 * @param userId
	 * @param pageBounds
     * @return
     */
	List<InvestInfo> getNearInvestList(int userId,PageBounds pageBounds);

	/**
	 * 获取用户最近一次未还债权的还款计划记录
	 */
	List<RepaymentInfo> getNextRepaymentItem(int userId, int creditId, int[] tradeTypes);

	/**
	 * 获取用户投资债权的下一期还款收益
	 */
	Map getNextRepaymentItemProfit(int userId, int creditId, int[] tradeTypes);


	PlanFinacing getUserPlanDetail(int userId, Integer planRecordId);

	List<Double> getPlanCollectInterest(Integer planRecordId);

	PlanFinacing getUserNewPlanDetail(int userId, Integer planRecordId);

	/**
	 * 获取最近投资的债权
	 * @param userId
	 * @param num
	 * @param pageBounds
	 * @return
	 */
	List<InvestInfo> getNearCredit(int userId, int num, VersionTypeEnum vte, PageBounds pageBounds);
}
