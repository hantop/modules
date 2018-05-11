package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.model.api.enums.GlobalStatus;
import com.fenlibao.p2p.model.dm.DMException;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.consts.HXConst;
import com.fenlibao.p2p.model.dm.entity.FundAccount;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.DMResponseCode;
import com.fenlibao.p2p.model.dm.enums.FundAccountType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.service.dm.hx.busi.OpenAccountService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import org.aeonbits.owner.ConfigCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcai on 2016/10/12.
 */
@Service
public class OpenAccountServiceImpl extends HXOrderProcessImpl implements OpenAccountService{

    @Transactional
    @Override
    public String openAccount(int userId, int clientType, String uri) throws Exception {
        HXAccountInfo accountInfo = hxUserService.getAccountInfo(userId);
        if (accountInfo != null && StringUtils.isNotBlank(accountInfo.getAcNo())) {
            throw new DMException(DMResponseCode.DM_HAS_BEEN_OPEN_ACCOUNT);//TODO 统一存管的响应信息
        }
        UserInfoEntity user = userService.get(userId, null);
        if (user == null) {
            throw new UserException(UserResponseCode.USER_NOT_EXIST);
        }
        if (!GlobalStatus.TG.name().equals(user.getAuthStatus())) {
            throw new UserException(UserResponseCode.USER_IDENTITY_UNAUTH);
        }
        String tradeCode = HXTradeType.getTradeCode(HXTradeType.KH, clientType);
        HXOrder order = new HXOrder().fromUser(userId, HXTradeType.KH.getBusiCode(), null);
        huaXingService.createOrder(clientType, order);
        String flowNum = HXUtil.getChannelFlow(tradeCode, order.getId());
        super.submit(order.getId(), flowNum, null);//开户没有相应的业务订单，所以直接提交
        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        ReqBusinessParams params = new ReqBusinessParams();
        params.setTTRANS(String.valueOf(HXTradeType.KH.getCode()));
        params.setACNAME(user.getFullName());
        params.setIDNO(StringHelper.decode(user.getIdCardEncrypt()));//需解密身份证
        params.setIDTYPE("1010");
        params.setMOBILE(user.getPhone());
        params.setRETURNURL(config.serverDomain() + uri);
        params.setEMAIL("");
        params.setCUSTMNGRNO("");
        return MessageUtil.getMessageByBusi(params, tradeCode, flowNum, clientType);
    }

    @Override
    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
        String returnState = busiParams.getRETURN_STATUS();
        if (null != returnState) {//结果查询处理
            if (HXConst.RETURN_STATUS_F.equals(returnState)) {
                order.setStatus(OrderStatus.SB.getCode());
                return;
            } else if (HXConst.ERRORCODE_FLOWNO_NOT_EXIST.equals(returnState)) {
                order.setStatus(OrderStatus.MJL.getCode());
                return;
            } else if (HXConst.ORDER_STATE_TIMEOUT.equals(returnState)) {
                order.setStatus(OrderStatus.CS.getCode());
                return;
            }
        }
        int userId = order.getUserId();
        UserInfoEntity user = userService.get(userId, null);
        String acNo = StringHelper.encode(busiParams.getACNO());
        //姓名校验，身份证、手机号格式化校验 (如果华兴那边异步回调/结果查询都是明文传回这三个信息都应校验)
        if (user.getFullName().equals(busiParams.getACNAME())
                ) {//&& StringHelper.decode(user.getIdCardEncrypt()).equals(busiParams.getIDNO())
            hxUserService.saveEAccount(userId, acNo);
            //初始化华兴资金账户
            //该账户余额不做维护，因为用户资金操作的时候只能以华兴那边的为准,该账户的余额多少不能影响用户的操作
            List<AssetAccount> fundAccounts = new ArrayList<>(2);
            AssetAccount HXWLZH = new AssetAccount(); //华兴往来账户
            HXWLZH.F02 = userId;
            HXWLZH.F03 = T6101_F03.HXWLZH;
            HXWLZH.F04 = getAccount("HW", userId);
            HXWLZH.F05 = user.getPhone();
            fundAccounts.add(HXWLZH);
            AssetAccount HXSDZH = new AssetAccount(); //华兴锁定账户
            HXSDZH.F02 = userId;
            HXSDZH.F03 = T6101_F03.HXSDZH;
            HXSDZH.F04 = getAccount("HS", userId);
            HXSDZH.F05 = user.getPhone();
            fundAccounts.add(HXSDZH);
            userService.initFundAccount(fundAccounts);
        } else {
            logger.warn("开户信息不一致，userId=[{}],ACNAME=[{}],ACNO=[{}]", userId, busiParams.getACNAME(),busiParams.getACNO());
            throw new TradeException("开户信息不一致");
        }
    }

    /**
     * 生成资金账户 账号
     * @param type
     * @param userId
     * @return
     */
    private String  getAccount(String type, int userId) {
        DecimalFormat df = new DecimalFormat("00000000000");
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append(df.format(userId));
        return sb.toString();
    }

    /**
     * 不再初始化这些账户，只在t6101再添加一个资金账户类别 华兴账户
     * @param userId
     */
    @Deprecated
    private void initFundAccount(int userId) {
        List<FundAccount> accounts = new ArrayList<>(2);
        FundAccount ac;
        for (FundAccountType acType : FundAccountType.values()) {
            ac = new FundAccount(userId, acType.getCode());
            accounts.add(ac);
        }
        hxUserService.createFundAcount(accounts);
    }

}
