package com.fenlibao.p2p.service.xinwang.pay.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.order.SysWithdrawOrderDao;
import com.fenlibao.p2p.dao.xinwang.pay.XWWithdrawDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.order.SysWithdrawOrder;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.pay.SysWithdrawApply;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWWithdrawRequest;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.common.CGModeEnum;
import com.fenlibao.p2p.model.xinwang.enums.common.SysMsgSendType;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.order.XWTradeOrderType;
import com.fenlibao.p2p.model.xinwang.enums.pay.WithdrawApplyStatus;
import com.fenlibao.p2p.model.xinwang.enums.pay.WithdrawArrival;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysPaymentInstitution;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.pay.SysPayUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/7/6.
 */
@Service
public class XWWithdrawTransactionServiceImpl implements XWWithdrawTransactionService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysOrderManageDao orderManageDao;

    @Resource
    XWWithdrawDao withdrawDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    PTCommonDao commonDao;

    @Resource
    SysOrderService orderService;

    @Resource
    SysWithdrawOrderDao withdrawOrderDao;

    @Resource
    PTCommonService commonService;

    @Override
    @Transactional
    public void pretreatment(XinwangAccount xinwangAccount, String requestNo, BigDecimal totalAmount, BigDecimal withdrawAmount, BigDecimal commossion, Date requestTime, Map<String,Object> reqData) throws Exception{
        BigDecimal commissionReceivable=new BigDecimal(SysPayUtil.PAY_CONFIG.WITHDRAW_POUNDAGE_1_RMB());
        //新建提现申请
        SysWithdrawApply withdrawApply=new SysWithdrawApply();
        withdrawApply.setUserId(xinwangAccount.getUserId());
        withdrawApply.setBankId(xinwangAccount.getBankId());
        withdrawApply.setAmount(withdrawAmount);
        withdrawApply.setCommissionReceivable(commissionReceivable);
        withdrawApply.setPaidInCommission(commossion);
        withdrawApply.setStatus(WithdrawApplyStatus.FKZ);
        withdrawApply.setArrival(WithdrawArrival.F);
        withdrawApply.setCgMode(CGModeEnum.CG);
        withdrawDao.createWithdrawApply(withdrawApply);

        //创建平台订单
        SystemOrder systemOrder = new SystemOrder();
        systemOrder.typeCode = XWTradeOrderType.WITHDRAW.orderType();
        systemOrder.orderStatus = XWOrderStatus.DTJ;
        systemOrder.source = Source.YH;
        systemOrder.userId = xinwangAccount.getUserId();
        systemOrder.flowNo = requestNo;
        orderManageDao.add(systemOrder);

        //更新帐户金额（往来->锁定）
        updateFundAccountAmount(systemOrder.getId(),xinwangAccount,totalAmount,withdrawAmount,commossion);
        //创建提现订单
        SysWithdrawOrder withdrawOrder=new SysWithdrawOrder();
        withdrawOrder.setId(systemOrder.getId());
        withdrawOrder.setUserId(xinwangAccount.getUserId());
        withdrawOrder.setAmount(withdrawAmount);
        withdrawOrder.setCommissionReceivable(commissionReceivable);
        withdrawOrder.setPaidInCommission(commossion);
        withdrawOrder.setBankcard(StringHelper.decode(xinwangAccount.getBankcardNo()));
        withdrawOrder.setThirdParty(SysPaymentInstitution.XW.getCode());
        withdrawOrder.setWithdrawApplyId(withdrawApply.getId());
        withdrawOrderDao.addOrder(withdrawOrder);
        //创建新网订单
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.WITHDRAW.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DTJ);
        req.setPlatformUserNo(xinwangAccount.getPlatformUserNo());
        req.setUserId(xinwangAccount.getUserId());
        req.setOrderId(systemOrder.getId());
        requestDao.createRequest(req);
        //创建新网提现信息
        XWWithdrawRequest withdrawRequest=new XWWithdrawRequest();
        withdrawRequest.setRequestNo(requestNo);
        withdrawRequest.setPlatformUserNo(xinwangAccount.getPlatformUserNo());
        withdrawRequest.setAmount(withdrawAmount);
        withdrawRequest.setCommission(commossion);
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date expiredTime = format.parse((String)reqData.get("expired"));
        withdrawRequest.setExpiredTime(expiredTime);
        withdrawDao.createWithdrawRequest(withdrawRequest);
        //保存请求参数
        XWResponseMessage message=new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);
    }


    private void updateFundAccountAmount(int orderId,XinwangAccount xinwangAccount,BigDecimal totalAmount,BigDecimal withdrawAmount,BigDecimal commossion) throws Exception{
        // 往来账户
        XWFundAccount wlzh = accountDao.getFundAccount(xinwangAccount.getUserId(), SysFundAccountType.parse("XW_"+xinwangAccount.getUserRole().getCode()+"_WLZH"));
        if (wlzh == null) {
            throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
        }
        if (totalAmount.compareTo(wlzh.getAmount()) > 0) {
            throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
        }

        // 锁定账户
        XWFundAccount sdzh = accountDao.getFundAccount(xinwangAccount.getUserId(), SysFundAccountType.parse("XW_"+xinwangAccount.getUserRole().getCode()+"_SDZH"));
        if (sdzh == null) {
            throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
        }
        List<XWCapitalFlow> flowList = new ArrayList<>();
        // 更新往来帐户余额
        Map<String,Object> wlzhParams=new HashMap<>();
        wlzhParams.put("id",wlzh.getId());
        wlzhParams.put("amount",wlzh.getAmount().subtract(totalAmount));
        accountDao.updateFundAccount(wlzhParams);
        // 往来账户流水
        {
            //提现金额流水
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(wlzh.getId());
            t6102.setTadeType(SysTradeFeeCode.TX);
            t6102.setOtherFundAccountId(sdzh.getId());
            t6102.setExpenditure(withdrawAmount);
            wlzh.setAmount(wlzh.getAmount().subtract(withdrawAmount));
            t6102.setBalance(wlzh.getAmount());
            t6102.setRemark("提现金额");
            t6102.setOrderId(orderId);
            flowList.add(t6102);
        }
        if (commossion.compareTo(BigDecimal.ZERO) > 0) {
            //提现手续费流水
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(wlzh.getId());
            t6102.setTadeType(SysTradeFeeCode.TX_SXF);
            t6102.setOtherFundAccountId(sdzh.getId());
            t6102.setExpenditure(commossion);
            wlzh.setAmount(wlzh.getAmount().subtract(commossion));
            t6102.setBalance(wlzh.getAmount());
            t6102.setRemark("提现手续费");
            t6102.setOrderId(orderId);
            flowList.add(t6102);
        }
        //更新锁定帐户余额
        Map<String,Object> sdzhParams=new HashMap<>();
        sdzhParams.put("id",sdzh.getId());
        sdzhParams.put("amount",sdzh.getAmount().add(totalAmount));
        accountDao.updateFundAccount(sdzhParams);
        // 锁定账户流水
        {
            //提现金额流水
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(sdzh.getId());
            t6102.setTadeType(SysTradeFeeCode.TX);
            t6102.setOtherFundAccountId(wlzh.getId());
            t6102.setIncome(withdrawAmount);
            sdzh.setAmount(sdzh.getAmount().add(withdrawAmount));
            t6102.setBalance(sdzh.getAmount());
            t6102.setRemark("提现金额");
            t6102.setOrderId(orderId);
            flowList.add(t6102);
        }
        if (commossion.compareTo(BigDecimal.ZERO) > 0) {
            //提现手续费流水
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(sdzh.getId());
            t6102.setTadeType(SysTradeFeeCode.TX_SXF);
            t6102.setOtherFundAccountId(wlzh.getId());
            t6102.setIncome(commossion);
            sdzh.setAmount(sdzh.getAmount().add(commossion));
            t6102.setBalance(sdzh.getAmount());
            t6102.setRemark("提现手续费");
            t6102.setOrderId(orderId);
            flowList.add(t6102);
        }
        commonDao.batchInsertT6102(flowList);
    }

    @Override
    @Transactional
    public void withdrawAccept(XWRequest request) throws Exception{
        request.setState(XWRequestState.DQR);
        requestDao.updateRequest(request);
        orderService.submit(request.getOrderId());
    }

    @Override
    @Transactional
    public void withdrawSuccess(XWRequest request) throws Exception{
        if(XWRequestState.DQR!=request.getState()){
            return;
        }
        SystemOrder order=orderManageDao.get(request.getOrderId(),true);
        SysWithdrawOrder withdrawOrder=withdrawOrderDao.get(request.getOrderId());
        //更新帐户余额
        withdrawSuccessUpdateAccountAmount(request.getPlatformUserNo(),withdrawOrder);
        //更新提现申请
        Map<String,Object> applyParams=new HashMap<>();
        applyParams.put("id",withdrawOrder.getWithdrawApplyId());
        applyParams.put("status",WithdrawApplyStatus.YFK);
        applyParams.put("arrival",WithdrawArrival.S);
        withdrawDao.updateWithdrawApply(applyParams);
        //结束新网订单
        request.setState(XWRequestState.CG);
        requestDao.updateRequest(request);
        //结束平台订单
        orderService.success(request.getOrderId());
        //发信息
        PlatformAccount platformAccount=accountDao.getPlatformAccountInfoByPlatformUserNo(request.getPlatformUserNo());
        String content = String.format("尊敬的用户：您好！ 您于%s提交的提现申请已到账，其中实际到账%s元，手续费%s元，请您注意查收，感谢您的支持",
                DateUtil.getYYYY_MM_DD_HH_MM_SS(order.getCreateTime()), withdrawOrder.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                withdrawOrder.getPaidInCommission().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        int days = DateUtil.daysOfTwo(order.getCreateTime(), new Date());
        if (days < 10) { //10天前的不再发送短信
            commonService.sendMsg(platformAccount.getMobile(),content, SysMsgSendType.ACTIVE.getValue());
        }
        commonService.sendLetter(withdrawOrder.getUserId(), "提现成功", content);
    }

    private void withdrawSuccessUpdateAccountAmount(String platformUserNo,SysWithdrawOrder withdrawOrder) throws Exception{
        // 新网平台收入账户
        XWFundAccount platformIncomeWLZH = accountDao.getFundAccount(SysCommonConsts.PLATFORM_USER_ID, SysFundAccountType.XW_PLATFORM_INCOME_WLZH);
        //用户锁定帐户
        XinwangAccount xinwangAccount=accountDao.getXinwangAccount(platformUserNo);
        String fundAccountType="XW_"+xinwangAccount.getUserRole().getCode()+"_SDZH";
        XWFundAccount userSDZH=accountDao.getFundAccount(withdrawOrder.getUserId(), SysFundAccountType.parse(fundAccountType));
        if(userSDZH==null){
            throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
        }
        List<XWCapitalFlow> flowList = new ArrayList<>();
        {
            //用户锁定帐户减去提现金额
            userSDZH.setAmount(userSDZH.getAmount().subtract(withdrawOrder.getAmount()));
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(userSDZH.getId());
            t6102.setTadeType(SysTradeFeeCode.TX);
            t6102.setOtherFundAccountId(userSDZH.getId());
            t6102.setExpenditure(withdrawOrder.getAmount());
            t6102.setBalance(userSDZH.getAmount());
            t6102.setRemark("用户提现");
            t6102.setOrderId(withdrawOrder.getId());
            flowList.add(t6102);
        }
        if (withdrawOrder.getPaidInCommission().compareTo(BigDecimal.ZERO) > 0)
        {
            //用户支出手续费
            userSDZH.setAmount(userSDZH.getAmount().subtract(withdrawOrder.getPaidInCommission()));
            {
                XWCapitalFlow t6102 = new XWCapitalFlow();
                t6102.setFundAccountId(userSDZH.getId());
                t6102.setTadeType(SysTradeFeeCode.TX_SXF);
                t6102.setOtherFundAccountId(platformIncomeWLZH.getId());
                t6102.setExpenditure(withdrawOrder.getPaidInCommission());
                t6102.setBalance(userSDZH.getAmount());
                t6102.setRemark("用户提现手续费");
                t6102.setOrderId(withdrawOrder.getId());
                flowList.add(t6102);
            }

            //平台收入手续费
            platformIncomeWLZH.setAmount(platformIncomeWLZH.getAmount().add(withdrawOrder.getPaidInCommission()));
            Map<String,Object>platformIncomeParams=new HashMap<>();
            platformIncomeParams.put("userId",platformIncomeWLZH.getUserId());
            platformIncomeParams.put("type",platformIncomeWLZH.getFundAccountType());
            platformIncomeParams.put("amount",platformIncomeWLZH.getAmount());
            accountDao.updateFundAccount(platformIncomeParams);
            {
                XWCapitalFlow t6102 = new XWCapitalFlow();
                t6102.setFundAccountId(platformIncomeWLZH.getId());
                t6102.setTadeType(SysTradeFeeCode.TX_SXF);
                t6102.setOtherFundAccountId(userSDZH.getId());
                t6102.setIncome(withdrawOrder.getPaidInCommission());
                t6102.setBalance(platformIncomeWLZH.getAmount());
                t6102.setRemark("用户提现手续费");
                t6102.setOrderId(withdrawOrder.getId());
                flowList.add(t6102);
            }
        }
        Map<String,Object>userSDZHParams=new HashMap<>();
        userSDZHParams.put("userId",userSDZH.getUserId());
        userSDZHParams.put("type",userSDZH.getFundAccountType());
        userSDZHParams.put("amount",userSDZH.getAmount());
        accountDao.updateFundAccount(userSDZHParams);
        commonDao.batchInsertT6102(flowList);
    }


    @Override
    @Transactional
    public void withdrawAcceptFail(XWRequest request) throws Exception{
        if(XWRequestState.DTJ!=request.getState()){
            return;
        }
        SystemOrder order=orderManageDao.get(request.getOrderId(),true);
        SysWithdrawOrder withdrawOrder=withdrawOrderDao.get(request.getOrderId());
        //更新帐户余额
        withdrawFailUpdateAccountAmount(request.getPlatformUserNo(),withdrawOrder);
        //更新提现申请
        Map<String,Object> applyParams=new HashMap<>();
        applyParams.put("id",withdrawOrder.getWithdrawApplyId());
        applyParams.put("status",WithdrawApplyStatus.TXSB);
        withdrawDao.updateWithdrawApply(applyParams);
        //结束新网订单
        request.setState(XWRequestState.SB);
        requestDao.updateRequest(request);
        //结束平台订单
        orderService.fail(request.getOrderId());
    }

    @Override
    @Transactional
    public void withdrawFail(XWRequest request) throws Exception{
        SystemOrder order=orderManageDao.get(request.getOrderId(),true);
        SysWithdrawOrder withdrawOrder=withdrawOrderDao.get(request.getOrderId());

        /*
        //更新帐户余额
        withdrawFailUpdateAccountAmount(request.getPlatformUserNo(),withdrawOrder);
        不需要更新账户余额，新网提现失败会有个回充的回调
        */

        //更新提现申请
        Map<String,Object> applyParams=new HashMap<>();
        applyParams.put("id",withdrawOrder.getWithdrawApplyId());
        applyParams.put("status",WithdrawApplyStatus.TXSB);
        withdrawDao.updateWithdrawApply(applyParams);
        //结束新网订单
        request.setState(XWRequestState.SB);
        requestDao.updateRequest(request);
        //结束平台订单
        orderService.fail(request.getOrderId());
    }

    @Override
    @Transactional
    public void backrollRecharge(Map<String, Object> respMap)throws Exception{
        String withdrawRequestNo=(String) respMap.get("withdrawRequestNo");
        String backrollRechargeRequestNo=(String) respMap.get("requestNo");
        XWRequest request = requestDao.getByRequestNo(withdrawRequestNo);
        SystemOrder order=orderManageDao.get(request.getOrderId(),true);
        SysWithdrawOrder withdrawOrder=withdrawOrderDao.get(request.getOrderId());
        //更新帐户余额
        withdrawFailUpdateAccountAmount(request.getPlatformUserNo(),withdrawOrder);
        //保存回充请求号
        Map<String,Object> updateWithdrawRequestParams=new HashMap<>();
        updateWithdrawRequestParams.put("requestNo",withdrawRequestNo);
        updateWithdrawRequestParams.put("backrollRechargeRequestNo",backrollRechargeRequestNo);
        withdrawDao.updateWithdrawRequest(updateWithdrawRequestParams);
        //发信息
        PlatformAccount platformAccount=accountDao.getPlatformAccountInfoByPlatformUserNo(request.getPlatformUserNo());
        String content = String.format("尊敬的用户：您好！您于%s提交的%s元提现申请失败，金额已经退回账户。感谢您对我们的关注与支持！",
                DateUtil.getYYYY_MM_DD_HH_MM_SS(order.getCreateTime()), withdrawOrder.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        int days = DateUtil.daysOfTwo(order.getCreateTime(), new Date());
        if (days < 10) { //10天前的不再发送短信
            commonService.sendMsg(platformAccount.getMobile(),content, SysMsgSendType.ACTIVE.getValue());
        }
        commonService.sendLetter(withdrawOrder.getUserId(), "提现失败", content);
    }

    private void withdrawFailUpdateAccountAmount(String platformUserNo,SysWithdrawOrder withdrawOrder) throws Exception{
        XinwangAccount xinwangAccount=accountDao.getXinwangAccount(platformUserNo);
        //用户锁定帐户
        String SDZHAccountType="XW_"+xinwangAccount.getUserRole().getCode()+"_SDZH";
        XWFundAccount userSDZH=accountDao.getFundAccount(withdrawOrder.getUserId(), SysFundAccountType.parse(SDZHAccountType));
        if(userSDZH==null){
            throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
        }
        //用户往来帐户
        String WLZHAccountType="XW_"+xinwangAccount.getUserRole().getCode()+"_WLZH";
        XWFundAccount userWLZH=accountDao.getFundAccount(withdrawOrder.getUserId(), SysFundAccountType.parse(WLZHAccountType));
        if(userWLZH==null){
            throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
        }
        //扣减锁定账户资金
        List<XWCapitalFlow> flowList = new ArrayList<>();
        {
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(userSDZH.getId());
            t6102.setTadeType(SysTradeFeeCode.TX_SB);
            t6102.setOtherFundAccountId(userWLZH.getId());
            t6102.setExpenditure(withdrawOrder.getAmount());
            userSDZH.setAmount(userSDZH.getAmount().subtract(withdrawOrder.getAmount()));
            t6102.setBalance(userSDZH.getAmount());
            t6102.setRemark("提现失败,本金返还");
            t6102.setOrderId(withdrawOrder.getId());
            flowList.add(t6102);
        }
        if(withdrawOrder.getPaidInCommission().compareTo(BigDecimal.ZERO)>0)
        {
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(userSDZH.getId());
            t6102.setTadeType(SysTradeFeeCode.TX_SB_SXF);
            t6102.setOtherFundAccountId(userWLZH.getId());
            t6102.setExpenditure(withdrawOrder.getPaidInCommission());
            userSDZH.setAmount(userSDZH.getAmount().subtract(withdrawOrder.getPaidInCommission()));
            t6102.setBalance(userSDZH.getAmount());
            t6102.setRemark("提现失败,手续费返还");
            t6102.setOrderId(withdrawOrder.getId());
            flowList.add(t6102);
        }
        Map<String,Object>userSDZHParams=new HashMap<>();
        userSDZHParams.put("userId",userSDZH.getUserId());
        userSDZHParams.put("type",userSDZH.getFundAccountType());
        userSDZHParams.put("amount",userSDZH.getAmount());
        accountDao.updateFundAccount(userSDZHParams);

        //增加往来账户资金
        {
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(userWLZH.getId());
            t6102.setTadeType(SysTradeFeeCode.TX_SB);
            t6102.setOtherFundAccountId(userSDZH.getId());
            t6102.setIncome(withdrawOrder.getAmount());
            userWLZH.setAmount(userWLZH.getAmount().add(withdrawOrder.getAmount()));
            t6102.setBalance(userWLZH.getAmount());
            t6102.setRemark("提现失败,本金返还");
            t6102.setOrderId(withdrawOrder.getId());
            flowList.add(t6102);
        }
        if(withdrawOrder.getPaidInCommission().compareTo(BigDecimal.ZERO)>0)
        {
            XWCapitalFlow t6102 = new XWCapitalFlow();
            t6102.setFundAccountId(userWLZH.getId());
            t6102.setTadeType(SysTradeFeeCode.TX_SB_SXF);
            t6102.setOtherFundAccountId(userSDZH.getId());
            t6102.setIncome(withdrawOrder.getPaidInCommission());
            userWLZH.setAmount(userWLZH.getAmount().add(withdrawOrder.getPaidInCommission()));
            t6102.setBalance(userWLZH.getAmount());
            t6102.setRemark("提现失败,手续费返还");
            t6102.setOrderId(withdrawOrder.getId());
            flowList.add(t6102);
        }
        Map<String,Object>userWLZHParams=new HashMap<>();
        userWLZHParams.put("userId",userWLZH.getUserId());
        userWLZHParams.put("type",userWLZH.getFundAccountType());
        userWLZHParams.put("amount",userWLZH.getAmount());
        accountDao.updateFundAccount(userWLZHParams);
        commonDao.batchInsertT6102(flowList);
    }
}
