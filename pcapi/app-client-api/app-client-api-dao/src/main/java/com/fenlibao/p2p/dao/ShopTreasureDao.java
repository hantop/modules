package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.ShopInformation;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

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

    List<ShopTreasureInfo> getBidInfoListByMap(Map<String, Object> map);

    /**
     * 开店宝计划列表
     */
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

	List<ShopTreasureInfo> getBidInfoList(Map<String, Object> map);

    /**
     * 开店宝计划列表，投标中排序
     */
    public List<ShopTreasureInfo> getCloseShopTreasureOderByStatus(Map<String, Object> map);
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
     * 获取计划列表
     * @param map
     * @return
     */
    List<ShopTreasureInfo> getBidPlansList(Map<String, Object> map);

    /**
     * 计划里面标对应的状态
     * @param map
     * @return
     */
    List<PlanBidsStatus> getPlanBidsStatus(Map<String, Object> map);


    /**
     * 计划详情
     */
    public ShopTreasureInfo getPlanInfo(Map<String, Object> map);

    /**
     * 定向标(投标中)
     * by:kris
     */
    List<ShopTreasureInfo> getdirectionalBid(Map<String, Object> map);

    /**
     * 判断是否已经购买计划
     * @param map
     * @return
     */
    int getpurchasedPlan(Map<String, Object> map);

    /**
     * 获取置顶标列表
     * @param map
     * @return
     */
    public List<ShopTreasureInfo> getStickShopTreasure(Map<String, Object> map);

    List<ShopTreasureInfo> getPlansList(Map<String, Object> map);

    /**
     * 计划详情(3.2.0版本)
     */
    public ShopTreasureInfo getPlanDetail(Map<String, Object> map);

    /**
     * 最近投资标
     * @param userId
     * @param num
     * @param pageBounds
     * @return
     */
    List<InvestInfo> getNearBid(int userId, int num, VersionTypeEnum vte, PageBounds pageBounds);

    /**
     * 判断是否已经购买计划
     * @param map
     * @return
     */
    int getNewPurchasedPlan(Map<String, Object> map);

    /**
     * 判断是否是存管标
     * @param map
     * @return
     */
    int isCGBid(Map<String, Object> map);

    /**
     * 统计可以投产品
     * @param type
     * @return
     */
    int countIvestProduct(String type,String versionType);
}
