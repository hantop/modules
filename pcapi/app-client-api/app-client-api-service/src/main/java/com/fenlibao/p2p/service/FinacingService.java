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
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FinacingService {

    /**
     * 根据用户ID和日期获取开店宝收益
     *
     * @param userId
     * @param earnDate
     * @return
     */
    public BigDecimal getKdbEarning(String userId, Date earnDate);

    FinacingDetailVo getFinacingDetail(String userId, String zqId);

    /**
     * 开店宝投资详情
     *
     * @param zpId
     * @return
     */
    ShopFinacingVo getShopFinacing(int zpId);

    /**
     * 开店宝产品明细
     *
     * @param kdbPlantId
     * @return
     */
    ShopProductVo getShopProduct(int kdbPlantId);

    /**
     * 用户投资记录
     *
     * @param userId
     * @param isUp
     * @param timestamp
     * @return
     */
    FinacingResultVo getInvestRecordList(String userId, int isUp, String timestamp, String proType);

    /**
     * 获取标的相关店铺信息
     *
     * @param bidId
     * @return
     */
    ShopInformationVO getShopInformation(int bidId);

    /**
     * 获取债权信息
     *
     * @param userId
     * @param bidId
     * @param creditId
     * @return
     */
    List<Finacing> getFinacing(String userId, String bidId, String creditId);

    /**
     * 获取债权转让昨天收益
     *
     * @param userId
     * @return
     */
    BigDecimal getZqzrEarning(String userId);

    /**
     * 获取债权投资资产
     *
     * @param userId
     * @return
     */
    BigDecimal getZqzrAssets(String userId);

    int isUserInvest(int userId);

    FinacingDetailVO_131 getFinacingDetail_131(String userId, String zqId) throws Exception;

    /**
     * @param userId
     * @param isUp
     * @param timestamp
     * @return
     * @Title: getFinacingList
     * @Description: 获取用户投资债权列表
     * @return: FinacingResultVo
     */
    FinacingResultVo getFinacingList(String userId, int isUp, String timestamp);


    /**
     * @param userId
     * @return
     * @Title: getUserInfo
     * @Description: 获取用户信息
     * @return: UserInfo
     */
    UserInfo getUserInfo(int userId);

    /**
     * 获取用户投资列表
     *
     * @param userId
     * @param cgNum 区分存管字段
     * @return
     * @throws Exception
     */
    List<InvestInfo> getUserInvestList(int userId, String bidType, String[] bidStatusStr, PageBounds pageBounds,int cgNum) throws Exception;

    /**
     * 获取用户投资债权详情
     *
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    InvestInfoDetail getUserFinacingDetail(String userId, String creditId) throws Exception;

    /**
     * 获取用户投资债权的代收本息
     *
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    BigDecimal getUserCollectInterest(int userId, int creditId) throws Exception;

    /**
     * 获取用户债权信息
     *
     * @param creditId
     * @return
     * @throws Exception
     */
    CreditInfo getUserCreditInfo(int creditId) throws Exception;

    /**
     * 获取用户投资债权的还款计划
     *
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId) throws Exception;

    /**
     * 获取用户投资债权的还款计划
     *
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    List<RepaymentInfo> getUserRepaymentItem(int userId, int creditId, int[] tradeTypes);

    /**
     * 获取用户投资债权的下一期还款计划
     *
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    List<RepaymentInfo> getNextRepaymentItem(int userId, int creditId, int[] tradeTypes);

    /**
     * 获取用户投资债权的下一期还款收益
     *
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    Map getNextRepaymentItemProfit(int userId, int creditId, int[] tradeTypes);


    /**
     * 获取用户投资债权的上一期还款计划
     *
     * @param userId
     * @param creditId
     * @return
     * @throws Exception
     */
    RepaymentInfo getLastRepaymentItem(int userId, int creditId, int[] tradeTypes);

    /**
     * 获取用户各投资状态数量
     *
     * @param userId
     * @return
     */
    Map<String, Map<String, String>> getInvestmentQty(Integer userId, VersionTypeEnum versionTypeEnum);


    /**
     * 获取用户最近的投资记录（包含债权转让）
     *
     * @param userId
     * @param pageBounds
     * @return
     * @throws Exception
     */
    List<InvestInfo> getNearInvestList(int userId, VersionTypeEnum vte, PageBounds pageBounds) throws Exception;

    /**
     * 获取用户计划详情
     *
     * @param userId
     * @param planRecordId
     * @return
     */
    PlanFinacing getUserPlanDetail(int userId, Integer planRecordId);

    /**
     * 获取用户投资计划债权的待收本息
     *
     * @return
     * @throws Exception
     */
    List<Double> getPlanCollectInterest(Integer planRecordId) throws Exception;

    /**
     * 获取用户计划详情(3.2.0版本)
     * @param userId
     * @param planRecordId
     * @return
     */
    PlanFinacing getUserNewPlanDetail(int userId, Integer planRecordId);

    /**
     * @param ownsStatus   持有状态 (1:持有中 2:申请退出 3:已退出)
     * @param interestTime 投资时间
     * @param exitTime     退出时间
     * @return
     */
    Integer getPlanMonthNum(int ownsStatus, long interestTime, long exitTime);
}
