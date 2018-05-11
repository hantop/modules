package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopInformation;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.util.api.annotations.RedisCache;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShopTreasureDao {

    /**
     * 根据用户ID获取用户累计收益
     * @param map
     * @return
     */
    BigDecimal getUserEarnStatistics(Map<String, Object> map);

    public ShopTreasureInfo getShopTreasure(Map<String, Object> map);


    /**
     * 获取用户开店宝指定日期的收益
     * @param map
     * @return
     */
    BigDecimal getUserEarnByDate(Map<String, Object> map);

    /**
     * 开店宝计划列表
     */
    @RedisCache(type = ShopTreasureInfo.class, cacheFlag = "YJQ")
    public List<ShopTreasureInfo> getCloseShopTreasure(Map<String, Object> map);

    /**
     * 开店宝计划详情
     */
    public ShopTreasureInfo getShopTreasureInfo(Map<String, Object> map);

    /**
     * 获取企业投资总额
     */
    public BigDecimal getInvestStatistics(Map<String, Object> map);

    /**
     * 用户累计收益
     */
    public BigDecimal getEarnStatistics(Map<String, Object> map);
    
    /**
     * 根据债权获取标的信息
     * @param zqId
     * @return
     */
    public ShopTreasureInfo getShopTreasureByZpid(int zqId);

    /**
     * 获取标的相关店铺信息
     * @param bidId
     * @return
     */
    public ShopInformation getShopInfomationByBid(int bidId);

	public int isUserInvest(int userId);

    @RedisCache(type = ShopTreasureInfo.class, cacheFlag = "YJQ")
	List<ShopTreasureInfo> getBidInfoList(Map<String, Object> map);

    /**
     * 抢购标(投标中)
     * by:kris
     */
    List<ShopTreasureInfo> getTimingBids(Map<String, Object> map);

    /**
     * 抢购标(计时中)
     * by:kris
     */
    List<ShopTreasureInfo> getPreSaleBids(Map<String, Object> map);

    /**
     * 获取投资理财标数据 by:kris
     */
    List<ShopTreasureInfo> getInvestmentBids(Map<String, Object> map);

    /**
     * 首页取消费信贷标的信息-2.1.1以上版本使用
     * 选择1~15天，16~30天的信贷标
     */
    List<ShopTreasureInfo> getXfxdBidList(Map map);

    /**
     *获取自动投标需要的标信息
     * @param map
     * @return
     */
    List<ShopTreasureInfo> getAutoBidShopTreasure(Map<String, Object> map);

    List<ShopTreasureInfo> getCloseShopTreasureRecommend(Map<String, Object> map);

    List<ShopTreasureInfo> getXfxdShortBidList(Map<String, Object> xfxdParam);

    List<ShopTreasureInfo> getBidInfoByTimeSort(Map<String, Object> map);

    /**
     * 判断是否是存管标
     * @param map
     * @return
     */
    int isCGBid(Map<String, Object> map);

    List<ShopTreasureInfo> getNoviceBidList(Map<String, Object> map);



    List<ShopTreasureInfo> getBidPlansList(Map<String, Object> map);

    /**
     * 计划详情
     */
    public ShopTreasureInfo getPlanInfo(Map<String, Object> map);

    /**
     * 计划里面标对应的状态
     * @param map
     * @return
     */
    List<PlanBidsStatus> getPlanBidsStatus(Map<String, Object> map);

    /**
     * 判断是否是计划标
     * @param map
     * @return
     */
    int isPlanBid(Map<String, Object> map);

    /**
     * 获取投资理财标加上计划数据 by:kris
     */
    List<ShopTreasureInfo> getInvestmentBidsAndPlan(Map<String, Object> map);

    /**
     * 获取置顶标列表
     * @param map
     * @return
     */
    public List<ShopTreasureInfo> getStickShopTreasure(Map<String, Object> map);

    List<ShopTreasureInfo> getPlansList(Map<String, Object> map);

    /**
     * 获取投资理财标加上计划数据 3.2版本
     */
    List<ShopTreasureInfo> getBidListAndPlanList(Map<String, Object> map);

    /**
     * 获取投资理财标加上计划数据 3.2版本
     */
    List<ShopTreasureInfo> getBidListAndPlanListOld(Map<String, Object> map);

    /**
     * 其他类型的标
     * @param map
     * @return
     */
    public List<ShopTreasureInfo> getOtherCloseShopTreasure(Map<String, Object> map);

    /**
     * 计划详情
     */
    public ShopTreasureInfo getPlanDetail(Map<String, Object> map);

    List<ShopTreasureInfo> getPlansListOderBy(Map<String, Object> map);

    public List<ShopTreasureInfo> getStickBidAndPlanTreasure(Map<String, Object> map);

    public List<ShopTreasureInfo> getStickBidAndPlanTreasureOld(Map<String, Object> map);

    /**
     * 判断是否是存管计划
     * @param map
     * @return
     */
    int isCGPlan(Map<String, Object> map);

}
