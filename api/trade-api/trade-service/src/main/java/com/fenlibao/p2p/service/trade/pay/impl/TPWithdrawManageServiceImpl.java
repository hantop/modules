package com.fenlibao.p2p.service.trade.pay.impl;

import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.pay.WithdrawManageDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.trade.entity.CapitalFlow;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6503;
import com.fenlibao.p2p.model.trade.entity.pay.T6130;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.common.MsgSendType;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.pay.T6130_F09;
import com.fenlibao.p2p.model.trade.enums.pay.T6130_F16;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;
import com.fenlibao.p2p.service.trade.common.TradeCommonService;
import com.fenlibao.p2p.service.trade.order.OrderManageService;
import com.fenlibao.p2p.service.trade.order.WithdrawProcessService;
import com.fenlibao.p2p.service.trade.pay.TPWithdrawManageService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.trade.pay.PayUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TPWithdrawManageServiceImpl implements TPWithdrawManageService{
	
	@Resource
	UserDao userDao;
	
	@Resource
	WithdrawManageDao withdrawManageDao;
	
	@Resource
	TradeCommonDao tradeCommonDao;
	
	@Resource
	TradeCommonService tradeCommonService;
	
	@Resource
	WithdrawProcessService withdrawProcessService;
	
	@Resource
	OrderManageService orderManageService;
	
	@Transactional
	@Override
	public int withdrawApply(int userId,BigDecimal totalAmount,BigDecimal withdrawAmount,BigDecimal poundage, UserInfoEntity userInfo,UserBankCardVO bankCard,String flowNum)
            throws Exception{
        //当前时间 
        Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();
        //新建提现申请
        int withdrawApplyId=addWithdrawApply(userId,withdrawAmount,poundage,bankCard.getCardId(),currentTimestamp);
        //更新帐户金额（往来->锁定）
        updateAccountAmount(userId,totalAmount,withdrawAmount,poundage,currentTimestamp);
        //创建订单
		T6503 withdrawOrder = new T6503();
		withdrawOrder.F02 = userId;
		withdrawOrder.F03 = withdrawAmount;
		withdrawOrder.F04 = new BigDecimal(PayUtil.PAY_CONFIG.WITHDRAW_POUNDAGE_1_RMB());
		withdrawOrder.F05 = poundage;
		withdrawOrder.F06 = StringHelper.decode(bankCard.getCardNoEncrypt());
		withdrawOrder.F07 = PaymentInstitution.BF.getCode();
		withdrawOrder.F09 = withdrawApplyId;
        int orderId=withdrawProcessService.addOrder(withdrawOrder,flowNum);
        orderManageService.submit(orderId);
        //发送短信 
//     	String content = String.format("尊敬的用户：您好！您于%s提交的%s元提现申请我们正在处理，如您填写的账户信息正确无误，您的资金将会于2个工作日内到达您的银行账户。感谢您对我们的关注与支持！", 
//    			DateUtil.getYYYY_MM_DD_HH_MM_SS(currentTimestamp),withdrawAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//     	tradeCommonService.sendMsg(userInfo.getPhone(), content,MsgSendType.ZD.getValue());
	    return orderId;
	}
	
	private int addWithdrawApply(int userId,BigDecimal withdrawAmount,BigDecimal poundage,int cardId,Timestamp currentTimestamp)throws Exception{
        T6130 withdrawApply=new T6130();
        withdrawApply.F02=userId;
        withdrawApply.F03=cardId;
        withdrawApply.F04=withdrawAmount; 
        withdrawApply.F06=poundage;
        withdrawApply.F07=poundage;
        withdrawApply.F08=currentTimestamp;
        withdrawApply.F09=T6130_F09.FKZ;
        withdrawApply.F14=currentTimestamp;
        withdrawApply.F16=T6130_F16.F;
        withdrawManageDao.addWithdrawApply(withdrawApply);
        return withdrawApply.F01;
	}
	
	private void updateAccountAmount(int userId,BigDecimal totalAmount,BigDecimal withdrawAmount,BigDecimal poundage,Timestamp currentTimestamp) throws Exception{
		// 往来账户
		AssetAccount wlzh = userDao.getFundAccount(userId, T6101_F03.WLZH);
		if (wlzh == null) {
			throw new UserException(UserResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
		}
		if (totalAmount.compareTo(wlzh.F06) > 0) {
			throw new TradeException(TradeResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
		}
		BigDecimal offlineAmount = withdrawManageDao.getOfflineRechargeAmount(userId,
				Integer.valueOf(PayUtil.PAY_CONFIG.WITHDRAW_OFFLINE_INTERVAL())); // xx小时内线下充值的金额
		if (totalAmount.compareTo(wlzh.F06.subtract(offlineAmount)) > 0) {
			throw new TradeException(TradeResponseCode.PAYMENT_OFFLINE_CANNOT_WITHDRAW.getCode(),
					String.format(TradeResponseCode.PAYMENT_OFFLINE_CANNOT_WITHDRAW.getMessage(),
							PayUtil.PAY_CONFIG.WITHDRAW_OFFLINE_INTERVAL()));
		}
		// 锁定账户
		AssetAccount sdzh = userDao.getFundAccount(userId, T6101_F03.SDZH);
		if (sdzh == null) {
			throw new UserException(UserResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
		}
		List<CapitalFlow> flowList = new ArrayList<>();
		// 更新往来帐户余额
		AssetAccount updateWLZHParam = new AssetAccount();
		updateWLZHParam.F01 = wlzh.F01;
		updateWLZHParam.F06 = wlzh.F06.subtract(totalAmount);
		updateWLZHParam.F07 = currentTimestamp;
		userDao.updateAccount(updateWLZHParam);
		// 往来账户流水
		{
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = wlzh.F01;
			t6102.F03 = TradeFeeCode.TX;
			t6102.F04 = sdzh.F01;
			t6102.F07 = withdrawAmount;
			wlzh.F06 = wlzh.F06.subtract(withdrawAmount);
			t6102.F08 = wlzh.F06;
			t6102.F09 = "提现金额";
			flowList.add(t6102);
		}
		if (poundage.compareTo(BigDecimal.ZERO) > 0) {
			// 往来账户流水
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = wlzh.F01;
			t6102.F03 = TradeFeeCode.TX_SXF;
			t6102.F04 = sdzh.F01;
			t6102.F07 = poundage;
			wlzh.F06 = wlzh.F06.subtract(poundage);
			t6102.F08 = wlzh.F06;
			t6102.F09 = "提现手续费";
			flowList.add(t6102);
		}
		// 更新往来帐户余额
		AssetAccount updateSDZHParam = new AssetAccount();
		updateSDZHParam.F01 = sdzh.F01;
		updateSDZHParam.F06 = sdzh.F06.add(totalAmount);
		updateSDZHParam.F07 = currentTimestamp;
		userDao.updateAccount(updateSDZHParam);
		{
			// 锁定账户流水
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = sdzh.F01;
			t6102.F03 = TradeFeeCode.TX;
			t6102.F04 = wlzh.F01;
			t6102.F06 = withdrawAmount;
			sdzh.F06 = sdzh.F06.add(withdrawAmount);
			t6102.F08 = sdzh.F06;
			t6102.F09 = "提现金额";
			flowList.add(t6102);
		}
		if (poundage.compareTo(BigDecimal.ZERO) > 0) {
			// 锁定账户流水
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = sdzh.F01;
			t6102.F03 = TradeFeeCode.TX_SXF;
			t6102.F04 = wlzh.F01;
			t6102.F06 = poundage;
			sdzh.F06 = sdzh.F06.add(poundage);
			t6102.F08 = sdzh.F06;
			t6102.F09 = "提现手续费";
			flowList.add(t6102);
		}
		tradeCommonDao.insertT6102s(flowList);
	}

	@Transactional
	@Override
	public void withdrawSuccess(int orderId, String thirdpartyOrderId)
			throws Exception {
		//提现订单
		T6503 withdrawOrder= withdrawProcessService.getOrder(orderId);
		if(withdrawOrder==null){
			throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
		}
		//平台往来帐户
		AssetAccount platformWLZH= userDao.getPlatformFundAccount(T6101_F03.WLZH);
		if(platformWLZH==null){
			throw new UserException(UserResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST.getCode(),"平台往来账户不存在");
		}
		//用户锁定帐户
		AssetAccount userSDZH=userDao.getFundAccount(withdrawOrder.F02, T6101_F03.SDZH);
		if(userSDZH==null){
			throw new UserException(UserResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
		}
		if (userSDZH.F06.compareTo(withdrawOrder.F03.add(withdrawOrder.F05)) < 0){
			throw new UserException(UserResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK);
		}
		//更新帐户余额
		withdrawSuccessUpdateAccountAmount(withdrawOrder,platformWLZH,userSDZH);
		//更新提现申请
		withdrawSuccessUpdateWithdrawApply(withdrawOrder.F09);
		//保存第三方订单id
		if(thirdpartyOrderId!=null){
			withdrawOrder.F08=thirdpartyOrderId;
			withdrawProcessService.updateOrder(withdrawOrder);
		}
		//结束订单
		orderManageService.complete(orderId, T6501_F03.CG);
		//发信息
		T6501 order=orderManageService.getOrder(orderId);
		UserInfoEntity userInfo=userDao.get(withdrawOrder.F02, null);
        String content = String.format("尊敬的用户：您好！ 您于%s提交的提现申请已成功，其中实际到账%s元，手续费%s元，请您注意查收。 感谢您对我们的关注与支持", 
        		DateUtil.getYYYY_MM_DD_HH_MM_SS(order.F04), withdrawOrder.F03.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
        		withdrawOrder.F05.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        int days = DateUtil.daysOfTwo(order.F04, new Date());
        if (days < 10) { //10天前的不再发送短信
        	tradeCommonService.sendMsg(userInfo.getPhone(),content,MsgSendType.ZD.getValue());
        }
        tradeCommonService.sendLetter(withdrawOrder.F02, "提现成功", content);
	}
	
	private void withdrawSuccessUpdateAccountAmount(T6503 withdrawOrder,AssetAccount platformWLZH,AssetAccount userSDZH)throws Exception{
		List<CapitalFlow> flowList = new ArrayList<>();
		{
			//用户锁定帐户减去提现金额
			userSDZH.F06 = userSDZH.F06.subtract(withdrawOrder.F03);
            CapitalFlow t6102 = new CapitalFlow();
            t6102.F02 = userSDZH.F01;
            t6102.F03 = TradeFeeCode.TX;
            t6102.F04 = userSDZH.F01;
            t6102.F07 = withdrawOrder.F03;
            t6102.F08 = userSDZH.F06;
            t6102.F09 = "用户提现";
            flowList.add(t6102);
        }
        if (withdrawOrder.F05.compareTo(BigDecimal.ZERO) > 0)
        {
        	//用户支出手续费
        	userSDZH.F06 = userSDZH.F06.subtract(withdrawOrder.F05);
            CapitalFlow t6102 = new CapitalFlow();
            t6102.F02 = userSDZH.F01;
            t6102.F03 = TradeFeeCode.TX_SXF;
            t6102.F04 = platformWLZH.F01;
            t6102.F07 = withdrawOrder.F05;
            t6102.F08 = userSDZH.F06;
            t6102.F09 = "用户提现手续费";
            flowList.add(t6102);
        }
        userDao.updateAccount(userSDZH);
        if (withdrawOrder.F05.compareTo(BigDecimal.ZERO) > 0)
        {
        	//平台收入手续费
        	platformWLZH.F06 = platformWLZH.F06.add(withdrawOrder.F05);
        	userDao.updateAccount(platformWLZH);
            {
                CapitalFlow t6102 = new CapitalFlow();
                t6102.F02 = platformWLZH.F01;
                t6102.F03 = TradeFeeCode.TX_SXF;
                t6102.F04 = userSDZH.F01;
                t6102.F06 = withdrawOrder.F05;
                t6102.F08 = platformWLZH.F06;
                t6102.F09 = "用户提现手续费";
                flowList.add(t6102);
            }
        }
        tradeCommonDao.insertT6102s(flowList);
	}
	
	private void withdrawSuccessUpdateWithdrawApply(int withdrawApplyId)throws Exception{
        T6130 withdrawApply=new T6130();
        withdrawApply.F01=withdrawApplyId;
        withdrawApply.F09=T6130_F09.YFK;
        withdrawApply.F16=T6130_F16.S;
        withdrawManageDao.updateWithdrawApply(withdrawApply);
	}

	@Transactional
	@Override
	public void withdrawFail(int orderId, String thirdpartyOrderId) throws Exception {
		//提现订单
		T6503 withdrawOrder= withdrawProcessService.getOrder(orderId);
		if(withdrawOrder==null){
			throw new TradeException(TradeResponseCode.ORDER_NOT_EXIST);
		}
		//用户锁定帐户
		AssetAccount userSDZH=userDao.getFundAccount(withdrawOrder.F02, T6101_F03.SDZH);
		if(userSDZH==null){
			throw new UserException(UserResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
		}
		if (userSDZH.F06.compareTo(withdrawOrder.F03.add(withdrawOrder.F05)) < 0){
			throw new UserException(UserResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK);
		}
		//用户往来帐户
		AssetAccount userWLZH=userDao.getFundAccount(withdrawOrder.F02, T6101_F03.WLZH);
		if(userWLZH==null){
			throw new UserException(UserResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
		}
		//更新帐户余额
		withdrawFailUpdateAccountAmount(withdrawOrder,userSDZH,userWLZH);
		//更新放款申请
		withdrawFailUpdateWithdrawApply(withdrawOrder.F09);
		//保存第三方订单id
		if(thirdpartyOrderId!=null){
			withdrawOrder.F08=thirdpartyOrderId;
			withdrawProcessService.updateOrder(withdrawOrder);
		}
		//结束订单
		orderManageService.complete(orderId, T6501_F03.SB);
		//发信息
		T6501 order=orderManageService.getOrder(orderId);
		UserInfoEntity userInfo=userDao.get(withdrawOrder.F02, null);
		String content = String.format(
				"尊敬的用户：您好！您于%s提交的%s元提现申请失败，金额已经退回账户。感谢您对我们的关注与支持！",
				DateUtil.getYYYY_MM_DD_HH_MM_SS(order.F04),
				withdrawOrder.F03.setScale(2,
						BigDecimal.ROUND_HALF_UP).toString());
        int days = DateUtil.daysOfTwo(order.F04, new Date());
        if (days < 10) { //10天前的不再发送短信
        	tradeCommonService.sendMsg(userInfo.getPhone(),content,MsgSendType.ZD.getValue());
        }
        tradeCommonService.sendLetter(withdrawOrder.F02, "提现失败", content);
	}
	
	private void withdrawFailUpdateAccountAmount(T6503 withdrawOrder,AssetAccount userSDZH,AssetAccount userWLZH)throws Exception{
		List<CapitalFlow> flowList = new ArrayList<>();
		{
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = userSDZH.F01;
			t6102.F03 = TradeFeeCode.TX_SB;
			t6102.F04 = userWLZH.F01;
			t6102.F07 = withdrawOrder.F03;
			userSDZH.F06 = userSDZH.F06.subtract(withdrawOrder.F03);
			t6102.F08 = userSDZH.F06;
			t6102.F09 = "提现失败,本金返还";
			flowList.add(t6102);
		}
		if(withdrawOrder.F05.compareTo(BigDecimal.ZERO)>0)
		{
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = userSDZH.F01;
			t6102.F03 = TradeFeeCode.TX_SB_SXF;
			t6102.F04 = userWLZH.F01;
			t6102.F07 = withdrawOrder.F05;
			userSDZH.F06 = userSDZH.F06.subtract(withdrawOrder.F05);
			t6102.F08 = userSDZH.F06;
			t6102.F09 = "提现失败,手续费返还";
			flowList.add(t6102);
		}
		// 扣减锁定账户资金
		userDao.updateAccount(userSDZH);
		{
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = userWLZH.F01;
			t6102.F03 = TradeFeeCode.TX_SB;
			t6102.F04 = userSDZH.F01;
			t6102.F06 = withdrawOrder.F03;
			userWLZH.F06 = userWLZH.F06.add(withdrawOrder.F03);
			t6102.F08 = userWLZH.F06;
			t6102.F09 = "提现失败,本金返还";
			flowList.add(t6102);
		}
		if(withdrawOrder.F05.compareTo(BigDecimal.ZERO)>0)
		{
			CapitalFlow t6102 = new CapitalFlow();
			t6102.F02 = userWLZH.F01;
			t6102.F03 = TradeFeeCode.TX_SB_SXF;
			t6102.F04 = userSDZH.F01;
			t6102.F06 = withdrawOrder.F05;
			userWLZH.F06 = userWLZH.F06.add(withdrawOrder.F05);
			t6102.F08 = userWLZH.F06;
			t6102.F09 = "提现失败,手续费返还";
			flowList.add(t6102);
		}
		// 增加往来账户资金
		userDao.updateAccount(userWLZH);
		tradeCommonDao.insertT6102s(flowList);
	}
	
	private void withdrawFailUpdateWithdrawApply(int withdrawApplyId)throws Exception{
        T6130 withdrawApply=new T6130();
        withdrawApply.F01=withdrawApplyId;
        withdrawApply.F09=T6130_F09.TXSB;
        withdrawManageDao.updateWithdrawApply(withdrawApply);
	}
}
