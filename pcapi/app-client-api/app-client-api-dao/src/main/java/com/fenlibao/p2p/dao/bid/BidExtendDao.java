package com.fenlibao.p2p.dao.bid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.bid.BidExtendGroupInfo;
import com.fenlibao.p2p.model.entity.bid.BidExtendGroupItemInfo;
import com.fenlibao.p2p.model.form.user.AccountAssetsForm;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

/**
 * Created by LouisWang on 2015/8/14.
 */
public interface BidExtendDao  {

    /**
     * 获取用户活期宝指定日期的收益
     * @param map
     * @return
     */
    BigDecimal getHqbUserEarnByDate(Map<String, Object> map);

    /**
     * 获取用户活期宝投资总额
     * @param userId
     * @return
     */
    BigDecimal getHqbInvestSum(String userId);

    /**
     * 获取用户开店宝投资总额
     * @param userId
     * @return
     */
    BigDecimal getKdbInvestSum(String userId);

    /**
     * 根据用户ID获取标的状态为投标中和待放款的投标记录总额。
     * @param userId
     * @return
     */
    BigDecimal getTenderFreezeSum(String userId);

	/**
	 * 区分普通账户与存管账户
	 * 根据用户ID获取标的状态为投标中和待放款的投标记录总额。
	 * @param userId
	 * @return
	 */
	BigDecimal getTenderFreezeSumByDepository(String userId,String depository);


    /**
     * 根据用户ID获取标的状态为投标中和待放款的开店宝投标记录总额。
     * @param userId
     * @return
     */
    BigDecimal getKdbTenderFreezeSum(String userId);
    /**
     * 根据用户ID获取标的状态为投标中和待放款的活期宝投标记录总额。
     * @param userId
     * @return
     */
    BigDecimal getHqbTenderFreezeSum(String userId);




    /**
     * 获取我的红包列表
     * @param
     * @return
     */
    List getRedPackets(Map map) throws Exception;

	/**
	 * @title 获取标的扩展组信息
	 * @param groupCode
	 * @param bidId
	 * @return
	 * @throws Exception
	 */
	List<BidExtendGroupInfo> getBidExtendGroupInfo(String groupCode, int bidId) throws Exception;



	/** 
	 * @Title: getBidExtendGroupItemInfo 
	 * @Description: 获取标的扩展组每项信息(groupId,extCode 只需一个即可)
	 * @param bidId
	 * @param groupId
	 * @param extCode
	 * @return
	 * @throws Exception
	 * @return: List<BidExtendGroupItemInfo>
	 */
	List<BidExtendGroupItemInfo> getBidExtendGroupItemInfo(int bidId, int groupId, String extCode) throws Exception;

	/** 
	 * @Title: getBidExtendGroupItemInfo 
	 * @Description: 获取标的扩展组每项信息( 根据extCode)
	 * @param bidId
	 * @param extCode
	 * @return
	 * @return: List<BidExtendGroupItemInfo>
	 * @throws Exception 
	 */
	List<BidExtendGroupItemInfo> getBidExtendGroupItemInfo(int bidId,String extCode) throws Exception;
	
	/** 
	 * @Title: getBidExtendGroupItemInfo 
	 * @Description: 获取标的扩展组每项信息( 根据extCode)
	 * @param bidId
	 * @param extCode
	 * @return
	 * @return: List<BidExtendGroupItemInfo>
	 * @throws Exception 
	 */
	List<BidExtendGroupItemInfo> getBidExtendGroupItemInfo(int bidId,int groupId) throws Exception;
	
	/**
	 * 获取用户投资资产
	 * @param userId
	 * @return
	 */
	List<AccountAssetsForm> getInvestmentAssets(Integer userId);

	/**
	 * 获取投资记录列表
	 * @param
	 * @return
	 */
	List<InvestRecordsVO> getBidInvestRecords(String bidId, PageBounds pageBounds);
}
