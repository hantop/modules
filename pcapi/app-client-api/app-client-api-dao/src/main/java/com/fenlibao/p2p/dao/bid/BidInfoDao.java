package com.fenlibao.p2p.dao.bid;

import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.util.List;
import java.util.Map;

/** 
 * @ClassName: BidInfoDao 
 * @Description: 标基本信息查询dao
 * @author: laubrence
 * @date: 2016-3-3 下午4:33:42  
 */
public interface BidInfoDao {
	
    /** 
     * @Title: getBorrowerInfo 
     * @Description: 获取标的借款人信息
     * @param bidId
     * @return
     * @return: BorrowerInfo
     */
    public BorrowerInfo getBorrowerInfo(int bidId);
    
	/** 
     * 标对应借款人详细信息
     */
    public BidBorrowerInfo getBidBorrowerInfo(int bidId);
    
    /** 
     * @Title: getBidBaseInfoByUser 
     * @Description: 查询用户投资的标的基本信息
     * @param userId
     * @param bidId
     * @return
     * @return: BidBaseInfo
     */
    public BidBaseInfo getBidBaseInfoByUser(int userId, int bidId );

    BidBaseInfo getBidBaseInfo(int bidId);

    BidExtendInfo getBidExtendInfo(int bidId);

    BidExtendInfo getBidAllInfo(int bidId);

    List<ShopTreasureInfo> getBidInfoList(String bidType, String[] bidStatus, int minDays, int maxDays, PageBounds pageBounds);

    List<ShopTreasureInfo> getBidInfoOrderByList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds);

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
     */
    List<ShopTreasureInfo> getBidInfoAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds);

    /**
     * 获取用户投资次数
     * @param userId
     * @return
     */
    Integer getUserInvestCount(int userId);

    /**
     * 获取定向标
     * @param bidId
     * @return
     */
    DirectionalBid getDirectionalBid(int bidId);

    /**
     * 获取可投标（计划）的和历史标的
     * @param bidType
     * @param bidStatuses
     * @param minDays
     * @param maxDays
     * @param sortType
     * @param sortBy
     * @param pageBounds
     * @param cgNum 区分存管
     * @return
     */
    List<ShopTreasureInfo> getBidInfoAndPlanAndHisBidList(String bidType, String[] bidStatuses, int minDays, int maxDays, String sortType, String sortBy, PageBounds pageBounds,String productType,int cgNum,int isNeedHis,int isNovice);

    /**
     * 获取公章文件编码
     * @param bidId
     * @return
     */
    String getenterpriseSealCode(Integer bidId);


    /**
     * 获取标的电子签章字段
     * @param bidId
     * @return
     */
    BidAgreement getBidAgreement(Integer bidId);
}
