package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.util.List;

/** 
 * @ClassName: BidInfoService 
 * @Description: 标相关业务service
 * @author: laubrence
 * @date: 2016-3-2 下午3:11:59  
 */
public interface BidInfoService {
	
	/** 
     * @Title: getBorrowerInfo 
     * @Description: 根据bidId查询借款人信息
     * @param bidId
     * @return
     * @return: BorrowerInfo
     */
    public BorrowerInfo getBorrowerInfo(int bidId);
    
	/** 
     * @param bidId 标id
     * @return: BidBorrowerInfo
     */
    public BidBorrowerInfo getBidBorrowerInfo(int bidId);


	/**
	 * 取标的信息列表
	 * @param bidStatus   标的状态
	 * @param timestamp   排序 (按筹款到期时间)
	 * @param limit       获取条数(0:不分页)
	 * @param isNoviceBid      是否新手标(S/F)
	 * @param isFull      是否满额(满额：传非空的字符串 )
	 * @param sortType    排序类型
	 * @return
	 */
	List<ShopTreasureInfo> getBidList(String[] bidStatus,String timestamp,int limit,String isNoviceBid,String isFull);
	
	/**
	 * 获取标的详情
	 * @param bidId  标的ID
	 * @return
	 */
	ShopTreasureInfo getBidInfo(int bidId);
	/** 
	 * @Title: getGroupInfoList 
	 * @Description: 获取标组信息返回列表VO
	 * @param bidId
	 * @return
	 * @throws Exception
	 * @return: List<BidExtendGroupVO>
	 */
	public List<BidExtendGroupVO> getGroupInfoList(int bidId) throws Exception;
	
	/**
	 * 获取标的投资记录
	 * @param bid
	 * @param timestamp
	 * @param isUp
	 * @return
	 */
	List<InvestRecordsVO> getBidInvestRecords(String bidId, PageBounds pageBounds);

	/**
	 * 获取标的列表(专用排序)
	 * @param bidStatus
	 * @param sortType
	 * @param sortBy
	 * @param limit
	 * @return
	 */
	List<ShopTreasureInfo> getBidList(String[] bidStatus, String sortType, String sortBy, int limit,String isNoviceBid,int minDays, int maxDays,String bidType,int cgNum);
	
    /** 
     * @Title: getGroupItemValue 
     * @Description: 根据extCode获取标扩展信息某项值
     * @param bidId
     * @param extCode
     * @return
     * @throws Exception
     * @return: String
     */
    public String getGroupItemValue(int bidId, String extCode) throws Exception;

	/** 
	 * @Title: getBidBaseInfoByUser 
	 * @Description: 查询用户投资的标的基本信息
	 * @param userId
	 * @param bidId
	 * @return
	 * @return: BidBaseInfo
	 */
	BidBaseInfo getBidBaseInfoByUser(int userId, int bidId);

	/**
	 * 获取标的合同图片地址数组
	 * @param bidId
	 * @return
     */
	String[] getBidPublicAccessoryFiles(int bidId);

	/**
	 * 获取标的合同静态访问url
	 * @param bidId
	 * @return
	 * @throws Exception
     */
	String getBidPublicAccessoryFileUrl(int bidId) throws Exception;

	BidBaseInfo getBidBaseInfo(int bidId);

	BidExtendInfo getBidExtendInfo(int bidId);

	BidExtendInfo getBidAllInfo(int bidId);

	List<ShopTreasureInfo> getBidInfoList(String bidType, String[] bidStatus, int minDays, int maxDays, PageBounds pageBounds) throws Exception;

	List<ShopTreasureInfo> getBidInfoList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds) throws Exception;

	/**
	 * 获取可投标的和历史标的
	 * @param bidType
	 * @param bidStatuses
	 * @param minDays
	 * @param maxDays
	 * @param sortType
	 * @param sortBy
	 * @param pageBounds
	 * @return
	 * @throws Exception
	 */
	List<ShopTreasureInfo> getBidInfoAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds) throws Exception;

	/**
	 * 判断用户是否是新手
	 * @param userId
	 * @return
	 */
	boolean isNovice(int userId);
	
	/**
	 * 获取标的合同图片地址数组
	 * @param bidId
	 * @return
     */
	public List<BidFiles> getBidPublicAccessoryFilesList(int bidId);

	/**
	 * 获取定向标
	 * by:kris
	 */
	DirectionalBid getDirectionalBid(int bidId);

	/**
	 * 获取列表以标状态‘TBZ’排序，再以发布时间倒序排
	 * @param bidStatus
	 * @param timestamp
	 * @param limit
	 * @param isNoviceBid
	 * @param isFull
	 * @return
	 */
	List<ShopTreasureInfo> getBidListOrderByStatus(String[] bidStatus,String timestamp,int limit,String isNoviceBid,String isFull,String bidType,int minDays, int maxDays);
	/**
	 * 抢购标(计时中)
	 * by:kris
	 */
	List<ShopTreasureInfo> getPreSaleBids(int limit,String bidType,int minDays, int maxDays);

	/**
	 * 抢购标(投标中)
	 * by:kris
	 */
	List<ShopTreasureInfo> getTimingBids(int limit,String bidType,int minDays, int maxDays);

	/**
	 *获取计划列表
	 * @param status
	 * @param timestamp
	 * @param minDaysInteger
	 * @param maxDaysInteger
	 * @param sortType
	 * @param sortBy
	 * @param pageNo
	 * @param pagesize
	 * @param limit
     * @param isNoviceBid
     * @return
     */
	List<ShopTreasureInfo> getBidPlansList(String[] status,String timestamp,int minDaysInteger,int maxDaysInteger,String sortType,String sortBy,Integer pageNo,Integer pagesize,int limit,String isNoviceBid);

	/**
	 * 计划里面标对应的状态
	 * @param planId
	 * @return
	 */
	List<PlanBidsStatus> getPlanBidsStatus(int planId);

    /**
	 * 获取计划信息
	 * @param planId
	 * @return
     */
	ShopTreasureInfo getPlanInfo(int planId);

	/**
	 * 定向标
	 * by:kris
	 */
	List<ShopTreasureInfo> getdirectionalBid(int limit,String bidType,int minDays, int maxDays);

	/**
	 * 是否已经购买此计划
	 * @param userId
	 * @param planId
     * @return
     */
	int getpurchasedPlan(int userId,int planId);

	/**
	 * 获取可投标(计划)的和历史标的
	 * @param bidType
	 * @param bidStatuses
	 * @param minDays
	 * @param maxDays
	 * @param sortType
	 * @param sortBy
	 * @param pageBounds
	 * @return
	 * @throws Exception
	 */
	List<ShopTreasureInfo> getBidInfoAndPlanAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds,String productType,int cgNum,int isNeedHis,int isNovice) throws Exception;

	/**
	 * 取置顶标列表
	 *
	 * @param bidStatus   标的状态
	 * @param limit       获取条数(0:不分页)
	 * @param isNoviceBid 是否新手标(S/F)
	 * @return
	 */
	List<ShopTreasureInfo> getStickBidList(String[] bidStatus, int limit, String isNoviceBid);

	/**
	 * 获取取计划列表3.2版本
	 * @param status
	 * @param timestamp
	 * @param minDaysInteger 最小借款期限
	 * @param maxDaysInteger 最大借款期限
	 * @param sortType
	 * @param sortBy
	 * @param pageNo   分页码数
	 * @param pagesize
	 * @param cgNum 区分存管
	 * @return
	 */
	List<ShopTreasureInfo> getPlansList(String[] status,String timestamp,int minDaysInteger,int maxDaysInteger,String sortType,String sortBy,Integer pageNo,Integer pagesize,int limit,String isNoviceBid,int type,String timeSort,int timeBid,int cgNum);

	/**
	 * 计划详情(3.2.0版本以上)
	 * @param planId
	 * @return
     */
	ShopTreasureInfo getPlanDetail(int planId);

	/**
	 * 是否已经购买此计划(3.2.0版本适用)
	 * @param userId
	 * @param planId
	 * @return
	 */
	int getNewPurchasedPlan(int userId,int planId);

	/**
	 * 判断是否为存管标
	 *
	 * @param bidId
	 * @return
	 */
	int isCGBid(int bidId);

	/**
	 * 统计可以投产品
	 * @param type
	 * @return
     */
	int countIvestProduct(String type,String versionType);

	/**
	 * 获取公章文件编码
	 * @param bidId
	 * @return
     */
	String getenterpriseSealCode(Integer bidId);
}

