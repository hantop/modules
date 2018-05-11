package com.fenlibao.p2p.service.xinwang.bid.impl;

import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.SysBidManageDao;
import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.model.xinwang.consts.TradeConsts;
import com.fenlibao.p2p.model.xinwang.entity.bid.SysBidInfo;
import com.fenlibao.p2p.model.xinwang.entity.order.BidOrder;
import com.fenlibao.p2p.model.xinwang.entity.order.SysTenderOrder;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.order.XWTradeOrderType;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.bid.SysBidManageService;
import com.fenlibao.p2p.util.xinwang.UserRoleUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2017/6/1 9:39
 */
@Service
public class SysBidManageServiceImpl implements SysBidManageService {
    @Resource
    private SysBidManageDao bidManageDao;
    @Resource
    private SysOrderManageDao orderManageDao;

    @Override
    public SysTenderOrder getTenderOrder(int orderId) {
        return bidManageDao.getTenderOrder(orderId);
    }

    @Override
    public SysBidInfo process(int bidId, BigDecimal tenderAmount) {
        SysBidInfo bidInfo = bidManageDao.getBidById(bidId);
        validateTender(bidInfo, tenderAmount);
        bidInfo.F07 = bidInfo.F07.subtract(tenderAmount);
        Map<String, Object> bidParams = new HashMap<>(2);
        bidParams.put("F01", bidInfo.F01);
        //这里先扣减可投金额，如果用户去到华兴放弃投资或投资失败，确定最后状态后再加回来
        bidParams.put("F07", bidInfo.F07);
        bidManageDao.updateBid(bidParams);
        return bidInfo;
    }

    private void validateTender(SysBidInfo bidInfo, BigDecimal amount) {
        if (bidInfo == null) {
            throw new XWTradeException(XWResponseCode.BID_NOT_EXIST);
        }
        if (TradeConsts.T6230_F38_DM != bidInfo.F38) {
            throw new XWTradeException(XWResponseCode.BID_TYPE_NOT_DM);
        }
        if (PTProjectState.TBZ != bidInfo.F20) {
            throw new XWTradeException(XWResponseCode.BID_FULLED);
        }
        if (amount.compareTo(bidInfo.F07) > 0) {
            throw new XWTradeException(XWResponseCode.BID_INVEST_AMOUNT_TOO_MUCH);//投标金额大于可投金额
        }
        if (bidInfo.F07.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new XWTradeException(XWResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //剩余可投金额不能低于最低起投金额
        }
    }

    @Override
    public void updateBid(Map<String, Object> bidParams) {
        bidManageDao.updateBid(bidParams);
    }

    @Transactional
    @Override
    public int createTenderOrder(XWTenderBO tender) {
        Integer userId = UserRoleUtil.parseUserNo(tender.getInvestorPlatformUserNo()).getUserId();
        //创建平台还款订单
        SystemOrder systemOrder = new SystemOrder();
        systemOrder.setTypeCode(XWTradeOrderType.PROJECT_REPAY.orderType());
        systemOrder.setOrderStatus(XWOrderStatus.DTJ);
        systemOrder.setSource(Source.HT);
        systemOrder.setUserId(userId);
        orderManageDao.add(systemOrder);
        //添加投标订单
        BidOrder tenderOrder = new BidOrder();
        tenderOrder.setId(systemOrder.getId());
        tenderOrder.setUserId(userId);
        tenderOrder.setBidId(tender.getBidId());
        tenderOrder.setAmount(tender.getAmount());
        orderManageDao.addTenderOrder(tenderOrder);
        return systemOrder.getId();
    }
}
