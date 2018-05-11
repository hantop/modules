package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.dao.trade.order.RechargeManageDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.consts.HXConst;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.entity.CapitalFlow;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6502;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.service.dm.hx.busi.HXRechargeService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import org.aeonbits.owner.ConfigCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcai on 2016/11/3.
 */
@Service
public class HXRechargeServiceImpl extends HXOrderProcessImpl implements HXRechargeService {

    @Resource
    private RechargeManageDao rechargeManageDao;

    @Transactional
    @Override
    public String recharge(int orderId, int userId, int clientType, String uri) throws Exception {
        HXTradeType tradeType = HXTradeType.CZ;//华兴交易类型
        String tradeCode = HXTradeType.getTradeCode(tradeType, clientType);
        HXAccountInfo userInfo = hxUserService.getAccountInfo(userId);
        T6502 t6502 = rechargeManageDao.getOrder(orderId);//获取充值订单
        if (t6502 == null) {
            logger.warn("充值订单不存在，userId=[{}],orderId=[{}]", userId, orderId);
            throw new TradeException(TradeResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        //创建华兴订单
        HXOrder order = new HXOrder().fromUser(userId, tradeType.getBusiCode(), orderId);
        huaXingService.createOrder(clientType, order);
        String flowNum = HXUtil.getChannelFlow(tradeCode, order.getId());
        super.submit(order.getId(), flowNum, null);//没有相应的业务订单，所以直接提交
        orderManageService.submit(orderId);//提交系统订单

        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        //封装请求报文
        ReqBusinessParams params = new ReqBusinessParams();
        params.setTTRANS(String.valueOf(tradeType.getCode()));
        params.setACNO(StringHelper.decode(userInfo.getAcNo()));
        params.setACNAME(userInfo.getAcName());
        params.setAMOUNT(t6502.F03.toString());
        params.setREMARK("");
        params.setRETURNURL(config.serverDomain() + uri);
        return MessageUtil.getMessageByBusi(params, tradeCode, flowNum, clientType);
    }

    @Override
    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
        String returnSate = busiParams.getRETURN_STATUS();
        int busiOrderId = order.getBusinessId();
        int userId = order.getUserId();
        if (null != busiParams.getORDERSTATUS() && !HXConst.ORDERSTATUS_COMPLETED.equals(busiParams.getORDERSTATUS())) {//异步回调
            logger.info("华兴充值非实时到账，userId=[{}],busiOrderId=[{}],state=[{}]", userId, busiOrderId, busiParams.getORDERSTATUS());
            order.setStatus(OrderStatus.N.getCode());
            return;
        }
        T6501 t6501 = orderManageService.getOrder(busiOrderId);//获取系统业务订单
        if (t6501 != null && T6501_F03.DQR == t6501.F03) {
            if (null != returnSate) {//结果查询处理
                if (HXConst.RETURN_STATUS_F.equals(returnSate)) {
                    orderManageService.complete(busiOrderId, T6501_F03.SB);
                    order.setStatus(OrderStatus.SB.getCode());
                    return;
                } else if (HXConst.ERRORCODE_FLOWNO_NOT_EXIST.equals(returnSate)) {
                    orderManageService.complete(busiOrderId, T6501_F03.MJL);
                    order.setStatus(OrderStatus.MJL.getCode());
                    return;
                } else if (HXConst.ORDER_STATE_TIMEOUT.equals(returnSate)) {
                    orderManageService.complete(busiOrderId, T6501_F03.SB);
                    order.setStatus(OrderStatus.CS.getCode());
                    return;
                }
            }
            T6502 rechargeOrder = rechargeManageDao.getOrder(busiOrderId);
            if (rechargeOrder == null) {
                logger.warn("充值订单不存在，t6501.f01=[{}],userId=[{}]", busiOrderId, t6501.F08);
                throw new TradeException(TradeResponseCode.COMMON_RECORD_NOT_EXIST);
            }
            //如果回调有金额，需要校验金额是否一致（华兴的木有也不需要，第三方支付需要）
            //在投资的时候不能因为我们资金账户的余额来
            //用户资金的操作不能以平台的资金账户为准，所以平台的资金账户余额是没有意义的，在平台只记录相应流水
            AssetAccount fundAccount = userService.getFundAccount(userId, T6101_F03.HXWLZH);//获取华兴资金账户
            if (fundAccount == null) {
                throw new TradeException(TradeResponseCode.FUND_ACCOUNT_NOT_EXIST);
            }
            List<CapitalFlow> flows = new ArrayList<>(1);
            CapitalFlow flow = new CapitalFlow();
            flow.F02 = fundAccount.F01;
            flow.F03 = TradeFeeCode.CZ;
            flow.F04 = fundAccount.F01;
            flow.F06 = rechargeOrder.F03;
            flow.F08 = fundAccount.F06;
            flow.F09 = "华兴充值";
            flows.add(flow);
            tradeCommonService.addTransactionFlows(flows);
            orderManageService.complete(busiOrderId, T6501_F03.CG);
        }
    }

}
