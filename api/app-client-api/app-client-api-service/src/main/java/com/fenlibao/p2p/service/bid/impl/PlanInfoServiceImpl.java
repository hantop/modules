package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.model.entity.bid.DirectionalPlan;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.PlanInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Mingway.Xu
 * @date 2017/3/22 11:49
 */
@Service
public class PlanInfoServiceImpl implements PlanInfoService {
    private static final Logger logger = LogManager.getLogger(PlanInfoServiceImpl.class);
    @Resource
    private PlanDao planDao;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ITradeService tradeService;

    @Override
    public void checkCanInvest(int productId, int userId, VersionTypeEnum versionTypeEnum) {
        //如果是定向计划 需要验证是否符合条件　
        DirectionalPlan directionalPlan = this.getDirectionalPlan(productId);
        if (directionalPlan == null) {
            logger.info("定向计划 需要验证是否符合条件为空 productId："+productId +" userId:"+userId);
            return;
        }

        DueInAmount bidDueInAmount = null;
        DueInAmount planDueInAmount = null;
        Map<String, Object> userAssetsData = null;
        if(VersionTypeEnum.CG.equals(versionTypeEnum)){
            bidDueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.CG.getIndex());//标待收本息
            planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.CG.getIndex());//计划待收本息
            userAssetsData = userInfoService.getUserAssetsByXW(userId);
        }else{
            bidDueInAmount = tradeService.getNewDueInAmount(userId,VersionTypeEnum.PT.getIndex());//标待收本息
            planDueInAmount = tradeService.getPlanDueInAmount(userId,VersionTypeEnum.PT.getIndex());//计划待收本息
            userAssetsData = userInfoService.getUserAssets(userId);
        }

        //计算累计收益
        //待收本息
        DueInAmount dueInAmount = bidDueInAmount == null ? new DueInAmount() : bidDueInAmount;
        if(planDueInAmount != null){
            dueInAmount.setPrincipal(bidDueInAmount.getPrincipal().add(planDueInAmount.getPrincipal()));
            dueInAmount.setGains(bidDueInAmount.getGains().add(planDueInAmount.getGains()));
        }
        //用户资产总额
        BigDecimal totalAssets = new BigDecimal((userAssetsData.get("totalAssets")).toString());
        //已获收益
        BigDecimal YHGains = new BigDecimal((userAssetsData.get("historyGains")).toString());
        int whiteBoardContain = userInfoService.checkWhiteBoard(userId);

        boolean assestFlag = false;
        if (directionalPlan.getTotalUserAssets() != null) {
            assestFlag = directionalPlan.getTotalUserAssets().compareTo(totalAssets) <= 0;
        }
        boolean interestFlag = false;
        if (directionalPlan.getUserAccumulatedIncome() != null && dueInAmount != null) {
            interestFlag = directionalPlan.getUserAccumulatedIncome().compareTo(dueInAmount.getGains().add(YHGains)) <= 0;
        }
        boolean investFlag = false;
        if (directionalPlan.getUserInvestingAmount() != null && dueInAmount != null) {
            investFlag = directionalPlan.getUserInvestingAmount().compareTo(dueInAmount.getPrincipal()) <= 0;
        }
        boolean whiteFlag = false;
        if (directionalPlan.getTargetUser() == 1) {
            whiteFlag = whiteBoardContain >= 1;
        }
        //满足任一提交可投：1 在投金额 2 累计收益 3 白名单用户
        if (assestFlag || interestFlag || investFlag || whiteFlag) {
            return;
        }
        logger.info("定向计划 满足任一提交可出借：1 已出借额 2 累计利息 3 白名单用户 无法满足 productId："+productId +" userId:"+userId);
        throw new BusinessException(ResponseCode.BID_ANYTIME_QUIT_USER_INVEST);


    }

    @Override
    public DirectionalPlan getDirectionalPlan(int productId) {
        return planDao.getDirectionalPlan(productId);
    }

    @Override
    public PlanRecordInfo getPlanRecordInfo(int planRecordId, Integer userId) {
        return planDao.getPlanRecordInfo(planRecordId,userId);
    }

    @Override
    public List<UserPlanProduct> getUserPlanProducts(int userPlanId) {
        return planDao.getUserPlanProducts(userPlanId);
    }

    /**
     * 锁用户投资计划记录
     * @param userPlanId
     */
    @Override
    public void lockUserPlan(int userPlanId) {
        planDao.lockUserPlan(userPlanId);
    }
}
