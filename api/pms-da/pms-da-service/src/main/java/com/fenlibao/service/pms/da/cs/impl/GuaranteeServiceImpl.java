package com.fenlibao.service.pms.da.cs.impl;

import com.fenlibao.dao.pms.da.cs.guarantee.GuaranteeMapper;
import com.fenlibao.model.pms.da.cs.GuaranteeInfo;
import com.fenlibao.model.pms.da.cs.RechargePreInfo;
import com.fenlibao.model.pms.da.cs.form.GuaranteeForm;
import com.fenlibao.model.pms.da.cs.form.RechargeForm;
import com.fenlibao.model.pms.da.cs.form.XWWithdrawForm;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.PaymentMode;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.param.account.UnbindBankCardRequestParams;
import com.fenlibao.p2p.model.xinwang.param.pay.WithdrawParam;
import com.fenlibao.p2p.service.xinwang.account.XWBindBankcardService;
import com.fenlibao.p2p.service.xinwang.account.XWUnbindBankCardService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnpBindcardService;
import com.fenlibao.p2p.service.xinwang.pay.XWRechargeProcessService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import com.fenlibao.service.pms.da.cs.GuaranteeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/6/26.
 */
@Service
public class GuaranteeServiceImpl implements GuaranteeService {

    private static final Logger LOGGER = LogManager.getLogger(GuaranteeServiceImpl.class);

    @Autowired
    private GuaranteeMapper guaranteeMapper;
    @Autowired
    private XWBindBankcardService xwBindBankcardService;
    @Autowired
    private XWEnpBindcardService xwEnpBindcardService;
    @Autowired
    private XWUnbindBankCardService xwUnbindBankCardService;
    @Autowired
    private XWRechargeProcessService xwRechargeProcessService;
    @Autowired
    private XWRechargeService xwRechargeService;
    @Autowired
    private XWWithdrawService xwWithdrawService;
    @Autowired
    private XWUserInfoService xwUserInfoService;

    private static final String XW_USER_TYPE_ORGANIZATION = "ORGANIZATION";
    private static final String XW_USER_TYPE_PERSONAL = "PERSONAL";

    @Override
    public List<GuaranteeInfo> getGuaranteeInfoList(GuaranteeForm guaranteeForm, RowBounds bounds) {
        List<GuaranteeInfo> list = guaranteeMapper.getGuaranteeInfoList(guaranteeForm, bounds);
        // 处理是否可以解绑卡
        for (GuaranteeInfo guaranteeInfo : list) {
            guaranteeInfo.setAllowUnbind(isAllowUnbindBank(guaranteeInfo.getUserId(), guaranteeInfo.getIsBindBank()));
        }
        return list;
    }

    private Integer isAllowUnbindBank(Integer userId, Integer isBindBank) {
       // 如果没有绑定银行卡则不能解绑状态設置为0
        if (isBindBank == 0) {
            return 0;
        }
        Integer isUnbind = guaranteeMapper.isAllowUnbindBank(userId, UserRole.GUARANTEECORP.getCode());
        // 如果没有申请解绑，或已解绑的 则设置为不能解绑0
        if (isUnbind == null || isUnbind == 1) {
            return 0;
        }
        // 否则设置为 可解绑 1
        return 1;
    }

    @Override
    public Map<String, Object> bindBank(RechargeForm bindInfo, String redirectUrl) throws Exception {
        Map<String, Object> data = null;
        if (XW_USER_TYPE_ORGANIZATION.equals(bindInfo.getUserType())) {
            data = xwEnpBindcardService.getBindcardInfo(bindInfo.getUserId(), UserRole.GUARANTEECORP, bindInfo.getBankCode(), bindInfo.getBankcardNo(), redirectUrl);
        } else {
            data = xwBindBankcardService.getBindcardParam(bindInfo.getUserId(), redirectUrl, UserRole.GUARANTEECORP);
        }
        return data;
    }

    @Override
    public Map<String, Object> unbindBank(String userId, String redirectUrl) throws Exception {
        Map<String, Object> data = new HashMap<>();
        UnbindBankCardRequestParams params = new UnbindBankCardRequestParams();
        Integer intId = Integer.valueOf(userId);
        params.setUserId(intId);
        params.setUri(redirectUrl);
        params.setUserRole(UserRole.GUARANTEECORP.getCode());
        data = xwUnbindBankCardService.getUnbindBankCardRequestData(params);
        return data;
    }

    @Override
    public RechargePreInfo getRechargePreInfoByUserId(String userId) {
        return guaranteeMapper.getRechargePreInfoByUserId(userId);
    }

    @Override
    public Map<String, Object> getRechargeRequestParam(RechargeForm rechargeForm, String redirectUrl) throws Exception {
        int orderId;
        Map<String, Object> data;
        String requestNo;
        validateUserHasBindcard(rechargeForm.getUserId()); // 充值：校验是否已绑卡，直连新网校验，防止当用户在解绑后，新网未能及时返回等异常情况
        BigDecimal amount = new BigDecimal(rechargeForm.getAmount());
        requestNo = XinWangUtil.createRequestNo();
        orderId = xwRechargeProcessService.addBackstageOrder(rechargeForm.getUserId(), amount, requestNo, Source.HT);
        int mode = "WEB".equals(rechargeForm.getRechargeWay()) ? 0 : 1;
        requestNo = XinWangUtil.createRequestNo();
        data = xwRechargeService.doRecharge(rechargeForm.getUserId(), UserRole.GUARANTEECORP, redirectUrl, orderId, requestNo, PaymentMode.parse(mode), rechargeForm.getBankCode());
        return data;
    }

    @Override
    public Map<String, Object> getWithdrawRequestParam(XWWithdrawForm withdrawForm, String redirectUrl) throws Exception {
        Map<String, Object> data;
        validateUserHasBindcard(withdrawForm.getUserId()); // 提现：校验是否已绑卡，直连新网校验，防止当用户在解绑后，新网未能及时返回等异常情况
        WithdrawParam params = initWithdrawField(withdrawForm);
        params.setRedirectUrl(redirectUrl);
        data = xwWithdrawService.withdrawApply(params);
        return data;
    }

    private void validateUserHasBindcard(Integer userId) throws Exception {
        try {
            XinwangUserInfo xWUserInfo = xwUserInfoService.queryUserInfo(UserRole.GUARANTEECORP.getCode() + userId);
            if (xWUserInfo == null
                    || !"PASSED".equals(xWUserInfo.getAuditStatus())
                    || !"ACTIVATED".equals(xWUserInfo.getActiveStatus())
                    || StringUtils.isEmpty(xWUserInfo.getBankcardNo())) {
                throw new APIException(XWResponseCode.PAYMENT_UNBOUND_BANK_CARD);
            }
        } catch (APIException ae) {
            LOGGER.error(String.format("校验用户【%s】是否绑卡失败", UserRole.GUARANTEECORP.getCode() + userId));
            throw new APIException(XWResponseCode.PAYMENT_UNBOUND_BANK_CARD);
        } catch (Exception e) {
            LOGGER.error(String.format("校验用户【%s】是否绑卡失败", UserRole.GUARANTEECORP.getCode() + userId));
            throw new Exception("校验用户是否绑卡异常");
        }
    }

    private WithdrawParam initWithdrawField(XWWithdrawForm withdrawForm) {
        WithdrawParam params = new WithdrawParam();
        params.setUserId(withdrawForm.getUserId());
        params.setAmount(withdrawForm.getAmount());
        params.setUserRole(UserRole.GUARANTEECORP.getCode());
        params.setPlatformUserNo(UserRole.GUARANTEECORP.getCode() + params.getUserId());
        params.setWithdrawType(withdrawForm.getWithdrawType());
        // 计算页面过期时间 当前时间增加30分钟
        DateTime dateTime = new DateTime();
        params.setExpired(dateTime.plusMinutes(30).toString("yyyyMMddHHmmss"));
        return params;
    }
}
