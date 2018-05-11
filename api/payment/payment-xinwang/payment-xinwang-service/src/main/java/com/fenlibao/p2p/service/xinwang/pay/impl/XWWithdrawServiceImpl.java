package com.fenlibao.p2p.service.xinwang.pay.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.order.SysWithdrawOrderDao;
import com.fenlibao.p2p.dao.xinwang.pay.XWWithdrawDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.enums.WithdrawType;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysUserStatus;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.pay.XinwangWithdrawStatus;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.pay.WithdrawParam;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.pay.SysPayUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Mingway.Xu
 * @date 2017/5/11 13:45
 */
@Service
public class XWWithdrawServiceImpl implements XWWithdrawService {

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

    @Resource
    XWWithdrawTransactionService withdrawTransactionService;

    @Override
    public Map<String, Object> withdrawApply(WithdrawParam params) throws Exception {
        String requestNo = XinWangUtil.createRequestNo();
        Date requestTime = new Date();
        String platformUserNo=params.getPlatformUserNo();
        BigDecimal totalAmount=new BigDecimal(params.getAmount());
        //校验
        validate(params.getUserId(),totalAmount,platformUserNo);
        //银行卡
        XinwangAccount xinwangAccount= accountDao.getXinwangAccount(platformUserNo);
        if (xinwangAccount.getBankcardNo()==null)
        {
            LOG.error("提现失败：platformUserNo=[{}]银行卡不存在",platformUserNo);
            throw new XWTradeException(XWResponseCode.PAYMENT_BANK_CARD_NOT_EXIST);
        }
        //手续费
        BigDecimal commossion=getCommission(params.getUserId());
        BigDecimal withdrawAmount=totalAmount.subtract(commossion);

        Map<String,Object> reqData=new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+params.getRedirectUrl());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("amount", params.getAmount());
        if(commossion.compareTo(BigDecimal.ZERO)>0){
            reqData.put("commission", commossion);
        }
        reqData.put("expired", params.getExpired());
        reqData.put("withdrawType", WithdrawType.NORMAL_URGENT.name());
        Map<String,Object> sendData=XinWangUtil.gatewayRequest(XinwangInterfaceName.WITHDRAW.getCode(),reqData);
        //创建订单，锁定提现金额
        withdrawTransactionService.pretreatment(xinwangAccount,requestNo,totalAmount,withdrawAmount,commossion,requestTime,reqData);

        return sendData;
    }

    private void validate(Integer userId,BigDecimal amount,String platformUserNo) throws Exception {
        //是否有逾期
        int overdueNum = accountDao.countOverdue(userId);
        if (overdueNum > 0) {
            throw new XWTradeException(XWResponseCode.PAYMENT_LOAN_OVERDUE);
        }
        //金额限制
        BigDecimal min = new BigDecimal(SysPayUtil.PAY_CONFIG.WITHDRAW_MIN_FUNDS());
        BigDecimal max = new BigDecimal(SysPayUtil.PAY_CONFIG.WITHDRAW_MAX_FUNDS());
        BigDecimal zero = new BigDecimal(0);
        if (amount.compareTo(min) <=0 || amount.compareTo(zero) <= 0)
        {
            throw new XWTradeException(XWResponseCode.PAYMENT_WITHDRAW_LIMIT_LOW);
        }
        if (amount.compareTo(max) > 0) {
            throw new XWTradeException(XWResponseCode.PAYMENT_WITHDRAW_LIMIT.getCode(),String.format(XWResponseCode.PAYMENT_WITHDRAW_LIMIT.getMessage(), min, max));
        }
        //用户状态
        PlatformAccount platformAccount=accountDao.getPlatformAccountInfoByPlatformUserNo(platformUserNo);
        if (SysUserStatus.HMD ==platformAccount.getUserStatus())
        {
            LOG.error("提现失败：userId=[{}]被拉入黑名单",userId);
            throw new XWTradeException(XWResponseCode.PAYMENT_BLACKLIST);
        }

    }

    /**
     * 首次提现免手续费
     * @param userId
     * @return
     */
    private BigDecimal getCommission(Integer userId) throws Exception{
        BigDecimal commission = BigDecimal.ZERO;
        if(SysCommonConsts.PLATFORM_USER_ID!=userId){
            String amount = SysPayUtil.PAY_CONFIG.WITHDRAW_POUNDAGE_1_RMB();
            Integer successApplyId = withdrawDao.getSuccessApplyId(userId);
            if (!(successApplyId == null || successApplyId < 1)) {
                commission = new BigDecimal(amount);
            }
        }
        return commission;
    }


    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String requestNo=(String)respMap.get("requestNo");
        XWRequest request=requestDao.getByRequestNo(requestNo);
        String withdrawStatusStr=(String)respMap.get("withdrawStatus");
        XinwangWithdrawStatus withdrawStatus=XinwangWithdrawStatus.parse(withdrawStatusStr);
        if(XinwangWithdrawStatus.ACCEPT==withdrawStatus){
            withdrawTransactionService.withdrawAccept(request);
            LOG.error("提现受理成功：requestNo=[{}]",request.getRequestNo());
        }
        else if(XinwangWithdrawStatus.ACCEPT_FAIL==withdrawStatus){
            withdrawTransactionService.withdrawAcceptFail(request);
            LOG.error("提现受理失败：requestNo=[{}]",request.getRequestNo());
        }
        else if(XinwangWithdrawStatus.SUCCESS==withdrawStatus){
            withdrawTransactionService.withdrawSuccess(request);
            LOG.error("提现成功：requestNo=[{}]",request.getRequestNo());
        }
        else if(XinwangWithdrawStatus.FAIL==withdrawStatus){
            withdrawTransactionService.withdrawFail(request);
            LOG.error("提现失败：requestNo=[{}]",request.getRequestNo());
        }
    }


}
