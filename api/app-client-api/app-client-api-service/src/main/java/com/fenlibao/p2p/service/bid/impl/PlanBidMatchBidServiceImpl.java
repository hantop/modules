package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.plan.PlanBidDao;
import com.fenlibao.p2p.model.entity.plan.PlanBid;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.plan.BidForPlanVO;
import com.fenlibao.p2p.model.vo.plan.PlanProductVO;
import com.fenlibao.p2p.model.vo.plan.XwTenderVO;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.bid.PlanBidMatchBidService;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/11/21 18:17.
 * @version 1.0
 */
@Service
public class PlanBidMatchBidServiceImpl implements PlanBidMatchBidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanBidMatchBidServiceImpl.class);

    @Resource
    private PlanBidDao planBidDao;

    @Override
    public BigDecimal investBidForPlan(int planId, int userPlanId, int accountId, BigDecimal amount, List<BidForPlanVO> bidForPlanVOs) {
        BigDecimal surplusAmount = amount;
        List<PlanBid> updateBids = new ArrayList<>();
        List<Integer> updateBidFullTimes = new ArrayList<>();
        List<XwTenderVO> addXwTenders = new ArrayList<>();
        List<PlanProductVO> addPlanProducts = new ArrayList<>();
        for (BidForPlanVO bidForPlanVO : bidForPlanVOs) {
            // 锁定标
            PlanBid planBid = planBidDao.lockBidById(bidForPlanVO.getBidId());
            // 查找该标的借款新网编号
            String borrowerUserNo = planBidDao.getBidBorrowerUserNoBy(bidForPlanVO.getBidId());
            if (StringUtils.isEmpty(borrowerUserNo)) {
                LOGGER.error("查找该标的新网借款用户编号异常，新网没有该借款标{}", bidForPlanVO.getBidId());
                throw new BusinessException(ResponseCode.BID_NOT_EXIST);
            }
            // 投资金额
            BigDecimal investBidAmount = preInvestBidAmount(planBid, accountId, surplusAmount);
            // 添加投标记录
            Integer investBidRecordId = doBidForPlan(accountId, investBidAmount, planBid);
            // 投资后标剩余可投金额
            BigDecimal voteAmount = planBid.getVoteAmount().subtract(investBidAmount);
            // 投资后 标的状态
            String status = voteAmount.compareTo(BigDecimal.ZERO) == 0 ? "DFK" : "TBZ";
            // 需更新标可投金额或状态的列表
            PlanBid tempBid = new PlanBid(planBid.getBidId(), status, null, voteAmount);
            updateBids.add(tempBid);
            if ("DFK".equals(status)) {
                // 需要更新t6231 F11 满标时间
                updateBidFullTimes.add(planBid.getBidId()); // 添加满标时间
            }
            // 需更插入flb.t_xw_tender 记录
            XwTenderVO xwTenderVO = new XwTenderVO(planBid.getBidId(), investBidRecordId, XinWangUtil.createRequestNo(), UserRole.INVESTOR.getCode() + accountId, borrowerUserNo);
            addXwTenders.add(xwTenderVO);
            // 需更回填计划投资标记录 flb.t_user_plan_product
            PlanProductVO planProductVO = new PlanProductVO(accountId, userPlanId, 1, 0 , investBidAmount, investBidRecordId);
            addPlanProducts.add(planProductVO);
            // 用户投资计划剩余可匹配金额
            surplusAmount = surplusAmount.subtract(investBidAmount);
            if (surplusAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }
        if (!updateBids.isEmpty()) {
            planBidDao.updateBids(updateBids);
            if (!updateBidFullTimes.isEmpty()) {
                planBidDao.updateBidFullTimes(updateBidFullTimes);
            }
            planBidDao.addXwTenders(addXwTenders);
            planBidDao.addPlanProducts(addPlanProducts);
        }

        return surplusAmount;
    }

    private Integer doBidForPlan(int accountId, BigDecimal investBidAmount, PlanBid planBid) {
        // 插入购买标的记录 s62.t6250
        Map<String, Object> investBidRecordMap = new HashMap<String, Object>();
        investBidRecordMap.put("bidId", planBid.getBidId());
        investBidRecordMap.put("investUser", accountId);
        investBidRecordMap.put("investAmount", investBidAmount);
        investBidRecordMap.put("creditAmount", investBidAmount);
        Integer investBidRecordId = planBidDao.addInvestBidRecord(investBidRecordMap);
        return investBidRecordId;
    }

    /**
     * 投资该标的金额
     * @param planBid
     * @param accountId
     * @param amount
     * @return
     */
    private BigDecimal preInvestBidAmount(PlanBid planBid, int accountId, BigDecimal amount) {
        if (planBid == null) {
            throw new BusinessException(ResponseCode.BID_NOT_EXIST);     //指定的标记录不存在
        }
        BigDecimal _a = planBid.getVoteAmount().stripTrailingZeros();
        if (_a.toPlainString().contains(".")) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_INTEGER); //投标金额必须为整数
        }
        boolean zjb = "true".equals(InterfaceConst.BID_SFZJKT); //PS:这里应该是动态获取资源
        if (zjb && accountId == planBid.getUserId()) {
            throw new BusinessException(ResponseCode.BID_BORROWER_CANNOT_INVESTMENT);     //您是该标的借款人，不能投标
        }
        if (!"TBZ".equals(planBid.getStatus())) {
            LOGGER.error("计划配标时[{}]标不是投标中状态[{}],不能投标,accountId=[{}]", planBid.getBidId(), planBid.getStatus(), accountId);
            throw new BusinessException(ResponseCode.BID_FULLED);     //指定的标不是投标中状态,不能投标  //160617按产品要求提示 该项目已满标，请投资其他项目
        }
        if (planBid.getVoteAmount().compareTo(new BigDecimal(0)) <= 0 ) {
            LOGGER.error("计划配标时标的可出借额为：{}", planBid.getVoteAmount());
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH);
        }
        return (planBid.getVoteAmount().compareTo(amount) <= 0 ? planBid.getVoteAmount() : amount);
    }

}
