package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.PlanDetailVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidDetailVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: BidInfoService 
 * @Description: 标相关业务service
 * @author: laubrence
 * @date: 2016-3-2 下午3:11:59  
 */
public interface BidInfoService {

	/**
	 * @param bidId
	 * @return
	 * @Title: getBorrowerInfo
	 * @Description: 根据bidId查询借款人信息
	 * @return: BorrowerInfo
	 */
	public BorrowerInfo getBorrowerInfo(int bidId);

	/**
	 * 取标的信息列表
	 *
	 * @param bidStatus   标的状态
	 * @param timestamp   排序 (按筹款到期时间)
	 * @param limit       获取条数(0:不分页)
	 * @param isNoviceBid 是否新手标(S/F)
	 * @param isFull      是否满额(满额：传非空的字符串 )
	 * @return
	 */
	List<ShopTreasureInfo> getBidList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull,String bidOrigin,int cgNum);

	/**
	 * 取推荐标的信息列表
	 *
	 * @param bidStatus   标的状态
	 * @param limit       获取条数(0:不分页)
	 * @param isNoviceBid 是否新手标(S/F)
	 * @return
	 */
	List<ShopTreasureInfo> getBidRecommedList(String[] bidStatus, int limit, String isNoviceBid);

	/**
	 * 取标的信息列表
	 *
	 * @param bidStatus   标的状态
	 * @param timestamp   排序 (按筹款到期时间)
	 * @param limit       获取条数(0:不分页)
	 * @param isNoviceBid 是否新手标(S/F)
	 * @param isFull      是否满额(满额：传非空的字符串 )
	 * @return
	 */
	List<ShopTreasureInfo> getBidRecommendList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull);

	/**
	 * 获取标的详情
	 *
	 * @param bidId 标的ID
	 * @return
	 */
	ShopTreasureInfo getBidInfo(int bidId);

	/**
	 * @param bidId
	 * @return
	 * @throws Exception
	 * @Title: getGroupInfoList
	 * @Description: 获取标组信息返回列表VO
	 * @return: List<BidExtendGroupVO>
	 */
	public List<BidExtendGroupVO> getGroupInfoList(int bidId) throws Exception;

	/**
	 * 获取标的投资记录
	 *
	 * @param bid
	 * @param timestamp
	 * @param isUp
	 * @return
	 */
	List<InvestRecordsVO> getBidInvestRecords(String bid, String timestamp, String isUp);

	/**
	 * 获取标所有的投资记录
	 *
	 * @param bid
	 * @return
	 */
	List<InvestRecordsVO> getBidInvestAllRecords(String bid);

	/**
	 * 获取标的列表(专用排序)
	 *
	 * @param bidStatus
	 * @param sortType
	 * @param sortBy
	 * @param limit
	 * @return
	 */
	List<ShopTreasureInfo> getBidList(String[] bidStatus, String sortType, String sortBy, int limit, String isNoviceBid);

	/**
	 * @param bidId
	 * @param extCode
	 * @return
	 * @throws Exception
	 * @Title: getGroupItemValue
	 * @Description: 根据extCode获取标扩展信息某项值
	 * @return: String
	 */
	public String getGroupItemValue(int bidId, String extCode) throws Exception;

	/**
	 * @param userId
	 * @param bidId
	 * @return
	 * @Title: getBidBaseInfoByUser
	 * @Description: 查询用户投资的标的基本信息
	 * @return: BidBaseInfo
	 */
	BidBaseInfo getBidBaseInfoByUser(int userId, int bidId);

	/**
	 * 获取标的合同图片地址数组
	 *
	 * @param bidId
	 * @return
	 */
	public List<BidFiles> getBidPublicAccessoryFilesList(int bidId);

	/**
	 * 获取标的合同静态访问url
	 *
	 * @param bidId
	 * @return
	 * @throws Exception
	 */
	String getBidPublicAccessoryFileUrl(int bidId) throws Exception;

	BidBaseInfo getBidBaseInfo(int bidId);

	BidExtendInfo getBidExtendInfo(int bidId);

	BidExtendInfo getBidAllInfo(int bidId);

	/**
	 * 标借款人信息
	 */
	int updateBidInfo();

	/**
	 * @param bidId 标id
	 * @return: BorrowerInfo
	 */
	public BidBorrowerInfo getBidBorrowerInfo(int bidId);

	/**
	 * 获取标的合同图片地址数组
	 *
	 * @param bidId
	 * @return
	 */
	String[] getBidPublicAccessoryFiles(int bidId);

	/**
	 * 判断用户是否是新手
	 *
	 * @param userId
	 * @return
	 */
	boolean isNovice(int userId);

	/**
	 * 获取定向标
	 * by:kris
	 */
	DirectionalBid getDirectionalBid(int bidId);

	/**
	 * 抢购标(计时中)
	 * by:kris
	 */
	List<ShopTreasureInfo> getPreSaleBids(int limit);

	/**
	 * 抢购标(投标中)
	 * by:kris
	 */
	List<ShopTreasureInfo> getTimingBids(int limit);


	/**
	 * 获取投资理财标数据-2.1.0版本使用
	 * by:kris
	 *
	 * @param bidStatus
	 * @param sortType
	 * @param sortBy
	 * @param limit
	 */
	List<ShopTreasureInfo> getInvestmentBids(String[] bidStatus, String sortType, String sortBy, int limit, int preSaleBidlimit);

	/**
	 * 获取投资理财标数据-2.1.1以上版本使用
	 * by:kris
	 */
	List<ShopTreasureInfo> getInvestmentBids(Map map);


	/**
	 * 标的信息列表-2.1.1以上版本使用
	 */
	List<ShopTreasureInfo> getBidList(Map map);

	/**
	 * 首页取消费信贷标的信息-2.1.1以上版本使用
	 * 选择1~15天，16~30天的信贷标
	 */
	List<ShopTreasureInfo> getXfxdBidList(Map map);

	/**
	 * 短期标消费信贷1个
	 *
	 * @param xfxdParam
	 * @return
	 */
	List<ShopTreasureInfo> getXfxdShortBidList(Map<String, Object> xfxdParam);

	/**
	 * 根据时间查询
	 *
	 * @param strings
	 * @param timeSort
	 * @return
	 */
	List<ShopTreasureInfo> getBidInfoByTimeSort(String[] strings, int timeSort);

	/**
	 * 判断是否为存管标
	 *
	 * @param bidId
	 * @return
	 */
	int isCGBid(int bidId);

	/**
	 * 取标的信息列表
	 *
	 * @param bidStatus   标的状态
	 * @param timestamp   排序 (按筹款到期时间)
	 * @param limit       获取条数(0:不分页)
	 * @param isNoviceBid 是否新手标(S/F)
	 * @param isFull      是否满额(满额：传非空的字符串 )
	 * @param cgNum
	 * @return
	 */
	List<ShopTreasureInfo> getBidList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull, int cgNum);

	List<ShopTreasureInfo> getPreSaleBids(int limit,int cgNum);

	List<ShopTreasureInfo> getTimingBids(int limit,int cgNum);

	List<ShopTreasureInfo> getBidList(String[] bidStatus,String sortType,String sortBy,int limit,String isNoviceBid,int cgNum);

	ShopTreasureInfo getBidInfo(int bidId,Integer cgNum);

	List<ShopTreasureInfo> getInvestmentBids(String[] bidStatus,String sortType,String sortBy,int limit,int preSaleBidlimit,int cgNum);

	/**
	 * 首页获取新手标
	 * @param bidStatus
	 * @return
	 */
    List<ShopTreasureInfo> getNoviceBidList(String[] bidStatus,int limit);

	/**
	 * 获取取计划列表
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
	 * @param timeSort 以更新时间逆序排列
	 * @return
	 */
	List<ShopTreasureInfo> getBidPlansList(String[] status, String timestamp, int minDaysInteger, int maxDaysInteger, String sortType, String sortBy, Integer pageNo,Integer pagesize,int limit,String isNoviceBid,String timeSort);

	/**
	 * 获取取计划列表
	 * @param status
	 * @param timestamp
	 * @param minDaysInteger 最小借款期限
	 * @param maxDaysInteger 最大借款期限
	 * @param sortType
	 * @param sortBy
	 * @param pageNo   分页码数
	 * @param pagesize
	 * @return
	 */
	List<ShopTreasureInfo> getBidPlansList(String[] status,String timestamp,int minDaysInteger,int maxDaysInteger,String sortType,String sortBy,Integer pageNo,Integer pagesize,int limit,String isNoviceBid);


	ShopTreasureInfo getPlanInfo(int planId);

	/**
	 * 计划里面标对应的状态
	 * @param planId
	 * @return
     */
	List<PlanBidsStatus> getPlanBidsStatus(int planId,int limit);

	/**
	 * 判断是否计划标
	 *
	 * @param bidId
	 * @return
	 */
	int isPlanBid(int bidId);

	/**
	 * 获取投资理财标加上计划数据-2.1.1以上版本使用
	 * by:kris
	 */
	List<ShopTreasureInfo> getInvestmentBidsAndPlan(Map map);

	/**
	 * 校验用户是否可投
	 * @param bidId
	 * @param userId
	 * @return
	 */
    void checkCanInvestBid(int bidId, int userId,String versionType);

	/**
	 *	获取标列表
	 * @param bidStatus
	 * @param timestamp
	 * @param limit
	 * @param isNoviceBid
	 * @param isFull
	 * @param anytimeQuit 随时退出标
	 * @param bidInterest 是否是加息标
	 * @return
	 */
    List<ShopTreasureInfo> getBidList(String[] bidStatus, String timestamp, int limit,String isNoviceBid,String isFull, int anytimeQuit, BigDecimal bidInterest);

	/**
	 * 获取标推荐列表
	 * @param bidStatus
	 * @param limit
	 * @param isNoviceBid
	 * @param anytimeQuit
	 * @param bidInterest
	 * @return
	 */
	List<ShopTreasureInfo> getBidRecommedList(String[] bidStatus, int limit, String isNoviceBid, int anytimeQuit, BigDecimal bidInterest);

	/**
	 * 获取新手标（推荐标栏位）
	 * @param bidStatus
	 * @param limit
	 * @param anytimeQuit
	 * @param bidInterest
	 * @return
	 */
	List<ShopTreasureInfo> getNoviceBidList(String[] bidStatus,int limit, int anytimeQuit, BigDecimal bidInterest);

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
	 * @return
	 */
	List<ShopTreasureInfo> getPlansList(String[] status,String timestamp,int minDaysInteger,int maxDaysInteger,String sortType,String sortBy,Integer pageNo,Integer pagesize,int limit,String isNoviceBid,int type,String timeSort,int timeBid,int cgNum);

	/**
	 * 获取投资理财标加上计划数据-3.2.1以上版本使用
	 * by:kris
	 */
	List<ShopTreasureInfo> getBidListAndPlanList(Map map);

	/**
	 * 获取投资理财标加上计划数据-3.2.0版本使用
	 * by:kris
	 */
	List<ShopTreasureInfo> getBidListAndPlanListOld(Map map);

	/**
	 * 取其他类型标的信息列表
	 *
	 * @param bidStatus   标的状态
	 * @param timestamp   排序 (按筹款到期时间)
	 * @param limit       获取条数(0:不分页)
	 * @param isNoviceBid 是否新手标(S/F)
	 * @param isFull      是否满额(满额：传非空的字符串 )
	 * @return
	 */
	List<ShopTreasureInfo> getOtherBidList(String[] bidStatus, String timestamp, int limit, String isNoviceBid, String isFull,String bidOrigin,int cgNum);

	/**
	 * 计划详情
	 * @param planId
	 * @return
     */
	ShopTreasureInfo getPlanDetail(int planId);

	ShopTreasureInfo getPlanDetail(int planId,int versionType);

	/**
	 * 标与计划列表整合
	 * @param status
	 * @param limit
	 * @param isNoviceBid
     * @return
     */
	List<ShopTreasureInfo> getPlansListOrderBy(String[] status,int limit,String isNoviceBid,String orderType,int cgNum,int minDay,int maxDay,String statusOrder);


	/**
	 * 获取置顶计划与标列表（区分普通版和存管版）
	 * @param bidStatus
	 * @param limit
	 * @param isNoviceBid
	 * @param productType
     * @return
     */
	List<ShopTreasureInfo> getStickBidAndPlanList(String[] bidStatus, int limit, String isNoviceBid,String productType,int cgNum,int recommend);

	/**获取置顶计划与标列表
	 *
	 * @param bidStatus
	 * @param limit
	 * @param isNoviceBid
     * @return
     */
	List<ShopTreasureInfo> getStickBidAndPlanList(String[] bidStatus, int limit, String isNoviceBid);

	/**
	 * 是否是存管标/计划
	 * @param id
	 * @param productType 1：标 2：计划
     * @return  1：普通类型  2：存管类型
     */
	int isCGProduct(int id,String productType);


	/**
	 * 获取借款人贷后信息
	 * @param bidId
	 * @return
	 */
	BidBorrowerInfoAfterLoan getBidBorrowerInfoAfterLoan(Integer bidId);

	/**
	 * 组装借款人信息
	 * @param bbInfo
	 * @param bidBorrowerInfoAfterLoan
	 * @return
	 */
	List<Map<String,Object>> pieceTogetherBorrowerInfo(BidBorrowerInfo bbInfo, BidBorrowerInfoAfterLoan bidBorrowerInfoAfterLoan, ShopTreasureInfo info);

	/**
	 * 组装标信息
	 * @param info
	 * @return
	 */
	List<Map<String,Object>> pieceTogetherProjectInfo(ShopTreasureInfo info);

	/**
	 * 组装计划信息
	 * @param info
	 * @return
	 */
	List<Map<String,Object>> pieceTogetherPlanInfo(ShopTreasureInfo info);

	/**
	 * 计划中的借款人项目列表
	 * @param planId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	Map<String,Object> projectCreditRightList(Integer planId, Integer pageIndex, Integer pageSize);

	/**
	 * 拼凑标信息详情
	 * @param info
	 * @param bbInfo
	 * @param bidId
	 *@param userId
	 * @param id @return
	 */
    BidDetailVO pieceBidDetailVO(ShopTreasureInfo info, BidBorrowerInfo bbInfo, String bidId, String userId, String id) throws Exception;

	/**
	 * 拼凑计划详情
	 * @param info
	 * @param planId
	 * @param userId
	 * @param id
	 * @param versionType
	 * @return
	 */
	PlanDetailVO piecePlanDetailVO(ShopTreasureInfo info, String planId, String userId, String id, VersionTypeEnum versionType) throws Exception;



	/**
	 * 插入借款标信息--填充数据
	 */
	int addBidInfoFromHistory(List<Integer> ids);

	/**
	 * 插入投标记录信息--填充数据
	 */
	int addBidInvestrecordsFromHistory(List<Integer> ids);

	/*
	获取上上签电子签章脱敏文件路径
	 */
    String getNoSensitiveAgreementPath(String bidId);

	/**
	 * 获取上传文档到上上签成功回传的签名文档标识与签名文档存储编号
	 * @param bidId
	 * @return
	 */
	Map getSignIdAndDocId(String bidId);
}
