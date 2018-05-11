package com.fenlibao.p2p.service;

import com.fenlibao.p2p.model.entity.finacing.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.FinacingDetailVo;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.vo.FinacingDetailVO_131;
import com.fenlibao.p2p.model.vo.FinacingResultVo;
import com.fenlibao.p2p.model.vo.ShopFinacingVo;
import com.fenlibao.p2p.model.vo.ShopInformationVO;
import com.fenlibao.p2p.model.vo.ShopProductVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FinacingService {

	/**
	 * 根据用户ID和日期获取开店宝收益
	 * @param userId
	 * @param earnDate
	 * @return
	 */
	public BigDecimal getKdbEarning(String userId, Date earnDate);

	FinacingDetailVo getFinacingDetail(String userId, String zqId);

	/**
	 * 开店宝投资详情
	 * @param zpId
	 * @return
	 */
	ShopFinacingVo getShopFinacing(int zpId);
	
	/**
	 * 开店宝产品明细
	 * @param kdbPlantId
	 * @return
	 */
	ShopProductVo getShopProduct(int kdbPlantId);
	
	/**
	 * 用户投资记录
	 * @param userId
	 * @param isUp
	 * @param timestamp
	 * @return
	 */
	FinacingResultVo getInvestRecordList(String userId, int isUp, String timestamp,String proType);
	
	/**
	 * 获取标的相关店铺信息
	 * @param bidId
	 * @return
	 */
	ShopInformationVO getShopInformation(int bidId);
	
	/**
	 * 获取债权信息
	 * @param userId
	 * @param bidId
	 * @param creditId
	 * @return
	 */
	List<Finacing> getFinacing(String userId,String bidId,String creditId);

	/**
	 * 获取债权信息
	 * @param userId
	 * @param bidId
	 * @param creditId
	 * @return
	 */
	List<Finacing> getFinacing(int userId,int bidId,int creditId);
	
	/**
	 * 获取债权转让昨天收益
	 * @param userId
	 * @return
	 */
	BigDecimal getZqzrEarning(String userId);
	
	/**
	 * 获取债权投资资产
	 * @param userId
	 * @return
	 */
	BigDecimal getZqzrAssets(String userId);
	
	int isUserInvest(int userId);

	FinacingDetailVO_131 getFinacingDetail_131(String userId, String zqId) throws Exception;

	/** 
	 * @Title: getFinacingList 
	 * @Description: 获取用户投资债权列表
	 * @param userId
	 * @param isUp
	 * @param timestamp
	 * @return
	 * @return: FinacingResultVo
	 */
	FinacingResultVo getFinacingList(String userId, int isUp, String timestamp);

	/** 
	 * @Title: getUserInfo 
	 * @Description: 获取用户信息
	 * @param userId
	 * @return
	 * @return: UserInfo
	 */
	UserInfo getUserInfo(int userId);

	/**
	 * 获取用户投资列表
	 * @param userId
	 * @param isUp
	 * @param timestamp
	 * @return
     * @throws Exception
     */
	List<InvestInfo> getUserInvestList(int userId, int isUp, String timestamp) throws Exception;

	/**
	 * 获取用户投资债权详情
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
     */
	InvestInfoDetail getUserFinacingDetail(String userId, String creditId) throws Exception;

	/**
	 * 获取用户投资债权的代收本息
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
     */
	BigDecimal getUserCollectInterest(int userId, int creditId) throws Exception;

	/**
	 * 获取用户债权信息
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	CreditInfo getUserCreditInfo(int creditId) throws Exception;

	/**
	 * 获取用户投资债权的还款计划
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
     */
	List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId) throws Exception;

	/**
	 * 获取用户投资债权的还款计划
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId,int[] tradeTypes);


	/**
	 * 获取用户投资债权的下一期还款计划
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	List<RepaymentInfo> getNextRepaymentItem(int userId, int creditId,int[] tradeTypes);

	/**
	 * 获取用户投资债权的下一期还款收益
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	Map getNextRepaymentItemProfit(int userId, int creditId, int[] tradeTypes);


	/**
	 * 获取用户投资债权的上一期还款计划
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	RepaymentInfo getLastRepaymentItem(int userId, int creditId,int[] tradeTypes);


	/**
	 * 获取用户所有的还款计划
	 * @param userId
	 * @param type
	 * @param tradeTypes
     * @return
     */
	public List<RepaymentInfoExt> getAllUserRepaymentItem(int userId, int type, Integer pageNo, Integer pagesize);

	/**
	 * 获取用户所有的还款计划(2017.6.3) mode为1：顺序取30条，2：顺序取剩余当天的
	 * @param userId
	 * @param type
	 * @param tradeTypes
     * @return
     */
	public List<RepaymentInfoExt> getAllUserRepaymentItem(int userId, int type, VersionTypeEnum versionTypeEnum, Date timestamp, int mode);
	
	/**
	 * 回款总额
	 * @param userId
     * @return
     */
	public List<UserRepaymentAmout> userRepaymentAmout(Integer userId);
	/**
	 * 回款总额
	 * @param userId
	 * @return
	 */
	public List<UserRepaymentAmout> userRepaymentAmout(Integer userId,int versionTypeEnum);


	/**
	 * 计划回款详情
	 * @param planRecordId
	 * @return
	 */
	public List<PlanRepaymentDetail> getPlanRepaymentDetail(Integer planRecordId);
}


