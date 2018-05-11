package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.model.entity.bid.DirectionalBid;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.PreDoBidService;
import com.fenlibao.p2p.service.trade.ITradeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Mingway.Xu
 * @date 2017/3/1 11:11
 */
@Service
public class PreDoBidServiceImpl implements PreDoBidService {
    private static final Logger logger = LogManager.getLogger(PreDoBidService.class);

    @Resource
    private BidInfoService bidInfoService;

    @Resource
    private ITradeService tradeService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 投标前校验用户是否可投
     * @param loanId
     * @param userId
     * @return
     */
    @Override
    public boolean checkCanInvestBid(int loanId, int userId, VersionTypeEnum versionTypeEnum) {
        /*默认普通标可投*/
        boolean canInvest = true;
        //如果是定向标 需要验证是否符合条件
        DirectionalBid directionalBid=bidInfoService.getDirectionalBid(loanId);
        if(directionalBid!=null) {
            //用户资产总额
           // BigDecimal totalAssets = BigDecimal.ZERO;
            //计算累计收益
           /* DueInAmount dueInAmount = tradeService.getDueInAmount(userId); //待收本息
            BigDecimal YHGains = tradeService.getYHGains(String.valueOf(userId)); //已获收益
            int whiteBoardContain = userInfoService.checkWhiteBoard(userId);

            BigDecimal dueInPrincipal = BigDecimal.ZERO;
            BigDecimal dueInterest = BigDecimal.ZERO;
            BigDecimal dueOthers = BigDecimal.ZERO;
            if (dueInAmount != null) {
                dueInPrincipal = dueInAmount.getPrincipal();
                dueInterest = dueInAmount.getInterest();
                dueOthers = dueInAmount.getOthers();
            }*/
            int whiteBoardContain = userInfoService.checkWhiteBoard(userId);
            DueInAmount bidDueInAmount = null;
            DueInAmount planDueInAmount = null;
            Map<String, Object> userAssetsData = null;
            if(VersionTypeEnum.CG.equals(versionTypeEnum)){
                bidDueInAmount = tradeService.getNewDueInAmount(userId,versionTypeEnum);//标待收本息
                planDueInAmount = tradeService.getPlanDueInAmount(userId,versionTypeEnum);//计划待收本息
                userAssetsData = userInfoService.getUserAssetsByXW(userId);
            }else{
                bidDueInAmount = tradeService.getNewDueInAmount(userId,versionTypeEnum);//标待收本息
                planDueInAmount = tradeService.getPlanDueInAmount(userId,versionTypeEnum);//计划待收本息
                userAssetsData = userInfoService.getUserAssets(userId);
            }

            //计算累计收益
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
            //用户资产总额
            BigDecimal totalAssets = new BigDecimal((userAssetsData.get("totalAssets")).toString());
            //已获收益
            BigDecimal YHGains = new BigDecimal((userAssetsData.get("historyGains")).toString());

            boolean assestFlag = false;
            if (directionalBid.getTotalUserAssets() != null) {
                totalAssets = userInfoService.getUserTotalAssets(userId);
                assestFlag = directionalBid.getTotalUserAssets().compareTo(totalAssets) <= 0;
            }

            boolean interestFlag = false;
            if (directionalBid.getUserAccumulatedIncome() != null) {
                interestFlag = directionalBid.getUserAccumulatedIncome().compareTo(dueInterest.add(dueOthers).add(YHGains)) <= 0;
            }

            boolean investFlag = false;
            if (directionalBid.getUserInvestAmount() != null) {
                investFlag = directionalBid.getUserInvestAmount().compareTo(dueInPrincipal) <= 0;
            }

            boolean whiteFlag = false;
            if (directionalBid.getTargetUser() == 1) {
                whiteFlag = whiteBoardContain >= 1;
            }
            //满足任一提交可投：1 在投金额 2 累计收益 3 白名单用户 4 用户总资产
            canInvest = assestFlag || interestFlag || investFlag || whiteFlag;
        }
        return canInvest;
    }
}
