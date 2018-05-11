package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.user.AutoBidDao;
import com.fenlibao.p2p.dao.user.AutoMatchBidDao;
import com.fenlibao.p2p.model.entity.bid.UserBidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserNewCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserPlanInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.bid.PlanBidVO;
import com.fenlibao.p2p.service.bid.AutoMatchBidExtService;
import com.fenlibao.p2p.service.bid.AutoMatchBidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiao on 2016/11/13.
 */
@Service
public class AutoMatchBidServiceImpl implements AutoMatchBidService {

    private static final Logger logger = LogManager.getLogger(AutoMatchBidServiceImpl.class);

    @Resource
    private AutoMatchBidDao autoMatchBidDao;
    @Resource
    private AutoMatchBidExtService autoMatchBidExtService;
    @Resource
    private AutoBidDao autoBidDao;

    @Override
    public UserPlanInfo getUserPlanInfo(Timestamp dbCurrentTime, Timestamp lastCreateTime , VersionTypeEnum versionTypeEnum) {
        return   getSuitableUserPlanInfo(dbCurrentTime, lastCreateTime, versionTypeEnum);
    }

    /**
     * 普通版计划配标
     *
     * @param userPlanInfo
     * @return
     */
    @Transactional
    @Override
    public UserPlanInfo autoMatchBidForNormalPlan( UserPlanInfo userPlanInfo) {
       // UserPlanInfo userPlanInfo = null;
        try {
           // userPlanInfo = getSuitableUserPlanInfo(dbCurrentTime, lastCreateTime, VersionTypeEnum.PT);
            if (userPlanInfo != null) {
                autoMatchBidDao.lockUserPlan(userPlanInfo.getId());
                logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]出借/复投开始");
                //离到期时间还剩3天的时候就不配标
                boolean creditFlag = (userPlanInfo.getExpireDateDiff() == 0 || userPlanInfo.getExpireDateDiff() > 3) ? true : false;

                //去除债权转让 2017.7.20
                //--购买计划债权
//                if (userPlanInfo.getBeinvestAmount().compareTo(new BigDecimal(5)) >= 0) {
//                    //查询从计划中退出的债权列表（可投金额大于50元，到期时间大于10天），按利率降序排列
//                    List<UserCreditInfoForPlan> userCreditInfoForPlanList = getPlanCreditList("plan", userPlanInfo.getUserId());
//                    //根据规则过滤出要买的债权
//                    List<UserCreditInfoForPlan> matchedCreditList = matchCredit(userPlanInfo, userCreditInfoForPlanList);
//
//                    logger.info("获取计划转让债权" + (userCreditInfoForPlanList == null ? 0 : userCreditInfoForPlanList.size()) + "个，为记录[" + userPlanInfo.getId() + "]匹配" + (matchedCreditList == null ? 0 : matchedCreditList.size()) + "个");
//
//                    //调用购买接口
//                    for (int i = 0; i < matchedCreditList.size(); i++) {
//                        try {
//                            logger.info("为计划出借记录[" + userPlanInfo.getId() + "]购买计划转让债权[" + matchedCreditList.get(i).getCreditCode() + "]开始");
//                            autoMatchBidExtService.doCreditForPlan(matchedCreditList.get(i), userPlanInfo);
//                            logger.info("为计划出借记录]" + userPlanInfo.getId() + "]购买计划转让债权[" + matchedCreditList.get(i).getCreditCode() + "]成功");
//                        } catch (Throwable t) {
//                            logger.error(t, t);
//                        }
//                    }
//                }

                //--购买加入债权库的散标
                if (userPlanInfo.getBeinvestAmount().compareTo(new BigDecimal(5)) >= 0 && creditFlag) {
                    //查询债权库中的可购买标（手动加入的和自动加入的消费信贷标）
                    List<UserBidInfoForPlan> userBidInfoForPlanList = getPlanBidList("nxfxd", userPlanInfo.getUserId(), userPlanInfo.getCgMode());
                    //根据规则过滤并匹配标
                    List<PlanBidVO> matchedBidList = matchBid(userPlanInfo, userBidInfoForPlanList);

                    logger.info("获取散标" + (userBidInfoForPlanList == null ? 0 : userBidInfoForPlanList.size()) + "个，为记录[" + userPlanInfo.getId() + "]匹配" + (matchedBidList == null ? 0 : matchedBidList.size()) + "个");

                    for (int i = 0; i < matchedBidList.size(); i++) {
                        //购买标，并回填至“用户出借计划产品”表（新增一条记录）
                        try {
                            logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]购买散标[" + matchedBidList.get(i).getBidId() + "]" + matchedBidList.get(i).getPurchaseAmount() + "元开始");
                            autoMatchBidExtService.doBidForPlan(matchedBidList.get(i), userPlanInfo);
                            logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]购买散标[" + matchedBidList.get(i).getBidId() + "]" + matchedBidList.get(i).getPurchaseAmount() + "元开始");
                        } catch (Throwable t) {
                            logger.error(t, t);
                        }
                    }
                }

                //--购买普通债权
//                if (userPlanInfo.getBeinvestAmount().compareTo(new BigDecimal(5)) >= 0) {
//                    //查询普通债权列表
//                    List<UserCreditInfoForPlan> userCreditInfoForPlanList = getPlanCreditList("whitOutPlan", userPlanInfo.getUserId());
//                    //根据规则过滤出要买的债权
//                    List<UserCreditInfoForPlan> matchedCreditList = matchCredit(userPlanInfo, userCreditInfoForPlanList);
//
//                    logger.info("获取普通债权" + (userCreditInfoForPlanList == null ? 0 : userCreditInfoForPlanList.size()) + "个，为记录[" + userPlanInfo.getId() + "]匹配" + (matchedCreditList == null ? 0 : matchedCreditList.size()) + "个");
//
//                    //调用购买接口
//                    for (int i = 0; i < matchedCreditList.size(); i++) {
//                        try {
//                            logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]购买普通债权[" + matchedCreditList.get(i).getCreditCode() + "]开始");
//                            autoMatchBidExtService.doCreditForPlan(matchedCreditList.get(i), userPlanInfo);
//                            logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]购买普通债权[" + matchedCreditList.get(i).getCreditCode() + "]成功");
//                        } catch (Throwable t) {
//                            logger.error(t, t);
//                        }
//                    }
//                }

                //--购买加入债权库的消费信贷标
                if (userPlanInfo.getBeinvestAmount().compareTo(new BigDecimal(5)) >= 0 && creditFlag) {
                    //查询债权库中的可购买标（手动加入的和自动加入的消费信贷标）
                    List<UserBidInfoForPlan> userBidInfoForPlanList = getPlanBidList("xfxd", userPlanInfo.getUserId(), userPlanInfo.getCgMode());
                    //根据规则过滤并匹配标
                    List<PlanBidVO> matchedBidList = matchBid(userPlanInfo, userBidInfoForPlanList);

                    logger.info("获取消费信贷标" + (userBidInfoForPlanList == null ? 0 : userBidInfoForPlanList.size()) + "个，为记录[" + userPlanInfo.getId() + "]匹配" + (matchedBidList == null ? 0 : matchedBidList.size()) + "个");

                    //购买标，并回填至“用户出借计划产品”表（新增一条记录）
                    for (int i = 0; i < matchedBidList.size(); i++) {
                        //购买标，并回填至“用户出借计划产品”表（新增一条记录）
                        try {
                            logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]购买消费信贷标[" + matchedBidList.get(i).getBidId() + "]" + matchedBidList.get(i).getPurchaseAmount() + "元开始");
                            autoMatchBidExtService.doBidForPlan(matchedBidList.get(i), userPlanInfo);
                            logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]购买消费信贷标[" + matchedBidList.get(i).getBidId() + "]" + matchedBidList.get(i).getPurchaseAmount() + "元成功");
                        } catch (Throwable t) {
                            logger.error(t, t);
                        }
                    }
                }
                logger.info("为普通版计划出借记录[" + userPlanInfo.getId() + "]出借/复投结束");
            }
        } catch (Throwable t) {
            logger.error(t, t);
        }
        return userPlanInfo;
    }

    /**
     * 存管版计划配标
     *
     * @param userPlanInfo
     * @return
     */
    @Transactional
    @Override
    public UserPlanInfo autoMatchBidForCGPlan(UserPlanInfo userPlanInfo) {
       // UserPlanInfo userPlanInfo = null;
        try {
            //userPlanInfo = getSuitableUserPlanInfo(dbCurrentTime, lastCreateTime, VersionTypeEnum.CG);
            if (userPlanInfo != null) {
                autoMatchBidDao.lockUserPlan(userPlanInfo.getId());
                logger.info("为存管版计划出借记录[" + userPlanInfo.getId() + "]出借开始");
                //离到期时间还剩3天的时候就不配标
                boolean creditFlag = (userPlanInfo.getExpireDateDiff() == 0 || userPlanInfo.getExpireDateDiff() > 3) ? true : false;

                //--购买加入债权库的消费信贷标
                //购买加入债权库的消费信贷标
                if (userPlanInfo.getBeinvestAmount().compareTo(new BigDecimal(5)) >= 0 && creditFlag) {
                    //查询债权库中的可购买标（手动加入的和自动加入的消费信贷标）
                    List<UserBidInfoForPlan> userBidInfoForPlanList = getPlanBidList("xfxd", userPlanInfo.getUserId(), userPlanInfo);
                    //根据规则过滤并匹配标
                    List<PlanBidVO> matchedBidList = matchBid(userPlanInfo, userBidInfoForPlanList);

                    logger.info("获取消费信贷标" + (userBidInfoForPlanList == null ? 0 : userBidInfoForPlanList.size()) + "个，为记录[" + userPlanInfo.getId() + "]匹配" + (matchedBidList == null ? 0 : matchedBidList.size()) + "个");

                    //购买标，并回填至“用户出借计划产品”表（新增一条记录）
                    for (int i = 0; i < matchedBidList.size(); i++) {
                        //购买标，并回填至“用户出借计划产品”表（新增一条记录）
                        try {
                            logger.info("为存管版计划出借记录[" + userPlanInfo.getId() + "]购买消费信贷标[" + matchedBidList.get(i).getBidId() + "]" + matchedBidList.get(i).getPurchaseAmount() + "元开始");
                            autoMatchBidExtService.doBidForPlan(matchedBidList.get(i), userPlanInfo);
                            logger.info("为存管版计划出借记录[" + userPlanInfo.getId() + "]购买消费信贷标[" + matchedBidList.get(i).getBidId() + "]" + matchedBidList.get(i).getPurchaseAmount() + "元成功");
                        } catch (Throwable t) {
                            logger.error(t, t);
                        }
                    }
                }
                logger.info("为存管版计划出借记录[" + userPlanInfo.getId() + "]出借结束");
            }
        } catch (Throwable t) {
            logger.error(t, t);
        }
        return userPlanInfo;
    }

    /**
     * 从用户出借计划表获取已经出借了的记录（可投金额大于5元，到期时间大于10天）
     *
     * @return
     */
    private UserPlanInfo getSuitableUserPlanInfo(Timestamp dbCurrentTime, Timestamp lastCreateTime, VersionTypeEnum versionTypeEnum) {
        UserPlanInfo userPlanInfo = autoMatchBidDao.getSuitableUserPlanInfo(dbCurrentTime, lastCreateTime, versionTypeEnum);
        return userPlanInfo;
    }

    /**
     * 查询从计划中退出的债权列表,排除自己发的和转的
     */
    private List<UserCreditInfoForPlan> getPlanCreditList(String type, int userId) {
        List<UserCreditInfoForPlan> planCreditList = autoMatchBidDao.getPlanCreditList(type, userId);
        return planCreditList;
    }

    /**
     * 查询加入债权库的标列表,排除自己发的和转的
     */
    private List<UserBidInfoForPlan> getPlanBidList(String type, int userId, int cgMode) {
        List<UserBidInfoForPlan> planBidList = autoMatchBidDao.getPlanBidList(type, userId, cgMode);
        return planBidList;
    }

    /**
     * 查询加入债权库的消费信贷标列表,排除自己发的和转的
     */
    private List<UserBidInfoForPlan> getPlanBidList(String type, int userId, UserPlanInfo userPlanInfo) {
        List<UserBidInfoForPlan> planBidList = autoMatchBidDao.getPlanBidList(type, userId, userPlanInfo);
        return planBidList;
    }

    /**
     * 匹配债权
     *
     * @param userPlanInfo
     * @param userCreditInfoForPlanList
     */
    private List<UserCreditInfoForPlan> matchCredit(UserPlanInfo userPlanInfo, List<UserCreditInfoForPlan> userCreditInfoForPlanList) {
        BigDecimal tempAmount = userPlanInfo.getBeinvestAmount();
        List<UserCreditInfoForPlan> tarCreditList = new ArrayList<UserCreditInfoForPlan>();
        if (userPlanInfo != null && userCreditInfoForPlanList != null && userCreditInfoForPlanList.size() > 0) {
            for (int i = 0; i < userCreditInfoForPlanList.size(); i++) {
                UserCreditInfoForPlan userCreditInfoForPlan = userCreditInfoForPlanList.get(i);
                if (userCreditInfoForPlan.getTransferOutPrice().compareTo(tempAmount) <= 0) {
                    tarCreditList.add(userCreditInfoForPlan);
                    tempAmount = tempAmount.subtract(userCreditInfoForPlan.getTransferOutPrice());
                    userCreditInfoForPlanList.remove(i);
                    i--;
                }
            }
        }
        return tarCreditList;
    }

    /**
     * 匹配标
     *
     * @param userPlanInfo
     * @param userBidInfoForPlanList
     */
    private List<PlanBidVO> matchBid(UserPlanInfo userPlanInfo, List<UserBidInfoForPlan> userBidInfoForPlanList) {
        //用户可投金额
        BigDecimal surplusAmount = userPlanInfo.getBeinvestAmount();
        List<PlanBidVO> tarPlanBidVOList = new ArrayList<PlanBidVO>();

        for (int i = 0; i < userBidInfoForPlanList.size(); i++) {
            UserBidInfoForPlan userBidInfoForPlan = userBidInfoForPlanList.get(i);

            //匹配的标
            PlanBidVO planBidVO = new PlanBidVO();
            planBidVO.setBidId(userBidInfoForPlan.getId());
            planBidVO.setBidName(userBidInfoForPlan.getName());
            //标可投金额和用户可投金额
            if (userBidInfoForPlan.getVoteAmount().compareTo(surplusAmount) <= 0) {
                planBidVO.setPurchaseAmount(userBidInfoForPlan.getVoteAmount());
            } else {
                planBidVO.setPurchaseAmount(surplusAmount);
            }


            //如果匹配的标金额小于100，调整购买金额
            if (planBidVO.getPurchaseAmount().compareTo(new BigDecimal(100)) < 0) {
                boolean balanceFlag = false;
                //100 减去 （ 标可投金额和用户可投金额 ）
                BigDecimal amountDiff = new BigDecimal(100).subtract(planBidVO.getPurchaseAmount());
                for (int j = tarPlanBidVOList.size() - 1; j >= 0; j++) {
                    PlanBidVO planBidVOTemp = tarPlanBidVOList.get(j);
                    if (planBidVOTemp.getPurchaseAmount().subtract(amountDiff).compareTo(new BigDecimal(100)) >= 0) {
                        surplusAmount = surplusAmount.add(amountDiff);
                        planBidVOTemp.setPurchaseAmount(planBidVOTemp.getPurchaseAmount().subtract(amountDiff));
                        tarPlanBidVOList.remove(j);
                        tarPlanBidVOList.add(j, planBidVOTemp);
                        balanceFlag = true;
                        break;
                    }
                }
                if (balanceFlag) {
                    i--;
                    continue;
                }
            }

            tarPlanBidVOList.add(planBidVO);

            surplusAmount = surplusAmount.subtract(planBidVO.getPurchaseAmount());
            if (surplusAmount.compareTo(BigDecimal.ZERO) == 1) {//还有余额
                continue;
            } else { //正好配完或余额小于标的
                break;
            }

        }
        return tarPlanBidVOList;
    }

    /**
     * 获取数据库时间
     *
     * @return
     */
    public Timestamp getDBCurrentTime() {
        Timestamp dbCurrentTime = autoBidDao.getDBCurrentTime();
        return dbCurrentTime;
    }

    /**
     * 获取购买的债权id并插入新纪录
     *
     * @param userNewCreditInfoForPlan
     * @param userPlanInfo
     * @return
     */
    @Override
    public void insertNewCredit(UserNewCreditInfoForPlan userNewCreditInfoForPlan, UserPlanInfo userPlanInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userPlanInfo.getUserId());
        map.put("userPlanId", userPlanInfo.getId());
        map.put("productType", 2);
        map.put("productId", userNewCreditInfoForPlan.getNewCreditId());
        map.put("amount", userNewCreditInfoForPlan.getAmount());
        map.put("tenderId", userNewCreditInfoForPlan.getBidRecordId());
        autoMatchBidDao.insertUserPlanProduct(map);
    }

    /**
     * 从用户出借计划表获取出借记录
     *
     * @param userPlanId
     * @return
     */
    @Override
    public UserPlanInfo getSuitableUserPlanInfoById(int userPlanId) {
        UserPlanInfo userPlanInfo = autoMatchBidDao.getSuitableUserPlanInfoById(userPlanId);
        return userPlanInfo;
    }

    /**
     * 获取债权转让结果
     *
     * @param applyforId
     * @return
     */
    @Override
    public UserNewCreditInfoForPlan getNewCreditInfo(int applyforId) {
        UserNewCreditInfoForPlan userNewCreditInfoForPlan = autoMatchBidDao.getNewCreditInfo(applyforId);
        return userNewCreditInfoForPlan;
    }
}
