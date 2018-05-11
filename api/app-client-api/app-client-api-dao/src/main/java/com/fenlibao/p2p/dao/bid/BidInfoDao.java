package com.fenlibao.p2p.dao.bid;

import com.fenlibao.p2p.model.entity.PfBidInfoVo;
import com.fenlibao.p2p.model.entity.bid.*;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;
import com.fenlibao.p2p.model.vo.share.InvestShareVO;
import com.fenlibao.p2p.util.api.annotations.RedisCache;

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
    
    
	/**
     * modify by zeronx 2017-10-24 11:03 添加List参数
	 * 更新借款标信息
	 */
    int updateBidInfo(List<PfBidInfoVo> updateRows);
	
	/**
	 * 插入借款标信息
	 */
    int addBidInfo();
    
    /**
	 * 插入投标记录信息
	 */
    int addBidInvestrecords();
    
	/** 
     * 标对应借款人详细信息
     */
    @RedisCache(type = BidBorrowerInfo.class)
    public BidBorrowerInfo getBidBorrowerInfo(int bidId);

    /**
     * 获取用户某个标最新投标记录ID
     * @param userId
     * @param bidId
     * @return
     */
    InvestShareVO getTenderIdLatest(int userId, int bidId);

    /**
     * 获取用户投资次数
     * @param userId
     * @return
     */
    Integer getUserInvestCount(int userId);
    /**
     * 获取投标中剩余可投金额小于100的标
     * @return
     */
    List<AutoTenderVO> getTBZ(VersionTypeEnum versionTypeEnum);
    /**
     * 获取定向标
     * @param bidId
     * @return
     */
    DirectionalBid getDirectionalBid(int bidId);

    /**
     * 获取投标中的信用贷
     * @return
     */
    List<AutoTenderVO> getCreditLoanTBZ(Integer type, VersionTypeEnum versionTypeEnum);

    /**
     * 获取需要跟新的记录
     * @return
     */
    List<PfBidInfoVo> getNeedUpdateRows();

    /**
     * 获取借款人贷后信息
     * @param bidId
     * @return
     */
    BidBorrowerInfoAfterLoan getBidBorrowerInfoAfterLoan(Integer bidId);

    /**
     * 该计划绑定标数量
     * @param planId
     * @return
     */
    Integer countPlanBindBid(Integer planId);

    /**
     * 该计划绑定标列表
     * @param planId
     * @return
     */
    List<Integer> getPlanBindBidList(Integer planId, Integer offset, Integer pageSize);


    /**
     * 获取上上签电子签章脱敏文件路径
     * @param bidId
     * @return
     */
    String getNoSensitiveAgreementPath(String bidId);

    /**
     * 获取上传文档到上上签成功回传的签名文档标识与签名文档存储编号
     * @param bidId
     * @return
     */
    Map getSignIdAndDocId(String bidId);
}


