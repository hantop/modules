package com.fenlibao.p2p.service.trade.order.impl;

import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.trade.order.RechargeManageDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6502;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F07;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.trade.vo.payment.PaymentLimitVO_;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;
import com.fenlibao.p2p.service.trade.order.RechargeProcessService;
import com.fenlibao.p2p.util.trade.order.OrderUtil;
import com.fenlibao.p2p.util.trade.pay.PayUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zcai on 2016/11/2.
 */
@Service
public class RechargeProcessServiceImpl implements RechargeProcessService {

    @Resource
    private OrderManageDao orderManageDao;
    @Resource
    private RechargeManageDao rechargeManageDao;
    @Resource
    private UserDao userDao;


    @Transactional
    @Override
    public int addOrder(int userId, BigDecimal amount, PaymentInstitution institution) throws Exception {
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new TradeException(TradeResponseCode.COMMON_PARAM_TYPE_WRONG); //金额或支付类型错误
        }
        //充值限额校验
        //validateCurdateTotalAmount(userId, amount, institution);
        //不收取手续费
        /*
        添加订单
         */
        T6501 t6501 = new T6501();
        t6501.F02 = TradeOrderType.CHARGE.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = userId;
        t6501.F10 = OrderUtil.getOrderNo(institution.name()); //系统流水号（非第三方）
        orderManageDao.add(t6501);
        /*
        添加充值订单
         */
        T6502 t6502 = new T6502();
        t6502.F01 = t6501.F01;
        t6502.F02 = userId;
        t6502.F03 = amount;
        //f04,f05 手续费，不收
        t6502.F07 = institution.getCode();
        rechargeManageDao.addOrder(t6502);
        return t6501.F01;
    }

	@Override
	public List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode, Date requestTime) throws Exception {
		return rechargeManageDao.getOrderNeedConfirmed(paymentInstitutionCode,requestTime);
	}

    @Override
    public void validateCurdateTotalAmount(int userId, BigDecimal amount, PaymentInstitution channel) throws Exception {
        BigDecimal minAmount = PayUtil.PAY_CONFIG.CHARGE_MIN_AMOUNT();//最低充值金额
        if (minAmount.compareTo(amount) > 0) {
            throw new TradeException(TradeResponseCode.PAYMENT_RECHARGE_LIMIT_LOW.getCode(),
                    String.format(TradeResponseCode.PAYMENT_RECHARGE_LIMIT_LOW.getMessage(), minAmount.toBigInteger()));
        }
        UserBankCardVO userBankCard = userDao.getBankCard(userId);
        //如果银行卡为空不需要校验，因为用户可能是通过充值绑卡(如连连)
        if (null != userBankCard) {
            List<PaymentLimitVO_> paymentLimits = rechargeManageDao.getLimitList(userBankCard.getBankCode(), channel.getChannelCode());
            if (null != paymentLimits && paymentLimits.size() > 0) {
                BigDecimal singleLimit = paymentLimits.get(0).getSingleLimit();//单位：万元
                BigDecimal dailyLimit = paymentLimits.get(0).getDailyLimit();//单位：万元
                BigDecimal curdateTotalAmount = rechargeManageDao.getCurdateTotalAmount(userId, channel);
                if (amount.compareTo(singleLimit.multiply(new BigDecimal(10000))) > 0) {
                    throw new TradeException(TradeResponseCode.PAYMENT_RECHARGE_LIMIT_EXCEED);
                }
                if (curdateTotalAmount.compareTo(dailyLimit.multiply(new BigDecimal(10000))) > 0) {
                    throw new TradeException(TradeResponseCode.PAYMENT_RECHARGE_LIMIT_EXCEED_CURDATE);
                }
            }
        }
    }
}
