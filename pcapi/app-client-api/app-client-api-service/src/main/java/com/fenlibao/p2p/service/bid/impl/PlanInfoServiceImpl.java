package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.model.entity.bid.DirectionalPlan;
import com.fenlibao.p2p.model.entity.borrow.BorrowerEntity;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.PlanInfoService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
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
    public boolean checkCanInvest(int productId, int userId, VersionTypeEnum versionType) {
        boolean canInvest = true;

        //如果是定向计划 需要验证是否符合条件　
        DirectionalPlan directionalPlan= this.getDirectionalPlan(productId);
        if(directionalPlan!=null) {
            //用户资产总额


            int whiteBoardContain = userInfoService.checkWhiteBoard(userId);

            DueInAmount bidDueInAmount = null;
            DueInAmount planDueInAmount = null;
            Map<String, Object> userAssetsData = null;
            if(VersionTypeEnum.CG.equals(versionType)){
                bidDueInAmount = tradeService.getNewDueInAmount(userId,versionType);//标待收本息
                planDueInAmount = tradeService.getPlanDueInAmount(userId,versionType);//计划待收本息
                userAssetsData = userInfoService.getUserAssetsByXW(userId);
            }else{
                bidDueInAmount = tradeService.getNewDueInAmount(userId,versionType);//标待收本息
                planDueInAmount = tradeService.getPlanDueInAmount(userId,versionType);//计划待收本息
                userAssetsData = userInfoService.getUserAssets(userId);
            }
            //待收本息
            DueInAmount dueInAmount = bidDueInAmount == null ? new DueInAmount() : bidDueInAmount;
            if(planDueInAmount != null){
                dueInAmount.setPrincipal(dueInAmount.getPrincipal().add(planDueInAmount.getPrincipal()));
                dueInAmount.setInterest(dueInAmount.getInterest().add(planDueInAmount.getInterest()));
                dueInAmount.setOthers(dueInAmount.getOthers().add(planDueInAmount.getOthers()));
            }


            BigDecimal dueInPrincipal = BigDecimal.ZERO;
            BigDecimal dueInterest = BigDecimal.ZERO;
            BigDecimal dueOthers = BigDecimal.ZERO;
            if (dueInAmount != null) {
                dueInPrincipal = dueInAmount.getPrincipal();
                dueInterest = dueInAmount.getInterest();
                dueOthers = dueInAmount.getOthers();
            }
           //已获收益
            BigDecimal YHGains = new BigDecimal((userAssetsData.get("historyGains")).toString());


            boolean assestFlag = false;
            if (directionalPlan.getTotalUserAssets() != null) {
                //用户资产总额
                BigDecimal totalAssets = new BigDecimal((userAssetsData.get("totalAssets")).toString());
                assestFlag = directionalPlan.getTotalUserAssets().compareTo(totalAssets) <= 0;
            }
            boolean interestFlag = false;
            if (directionalPlan.getUserAccumulatedIncome() != null ) {
                interestFlag = directionalPlan.getUserAccumulatedIncome().compareTo(dueInterest.add(dueOthers).add(YHGains)) <= 0;
            }
            boolean investFlag = false;
            if (directionalPlan.getUserInvestingAmount() != null ) {

                investFlag = directionalPlan.getUserInvestingAmount().compareTo(dueInPrincipal) <= 0;
            }
            boolean whiteFlag = false;
            if (directionalPlan.getTargetUser() == 1) {
                whiteFlag = whiteBoardContain >= 1;
            }
            //满足任一提交可投：1 在投金额 2 累计收益 3 白名单用户
            canInvest = assestFlag||interestFlag||investFlag||whiteFlag;
        }

        return canInvest;
    }

    @Override
    public DirectionalPlan getDirectionalPlan(int productId) {
        return planDao.getDirectionalPlan(productId);
    }

    @Override
    public PlanRecordInfo getPlanRecordInfo(int planRecordId, int userId) {
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



    @Override
    public List<BorrowerEntity> getPlanBidList(int planId, PageBounds pageBounds) {
        return planDao.getPlanBidList(planId,pageBounds);
    }
}
