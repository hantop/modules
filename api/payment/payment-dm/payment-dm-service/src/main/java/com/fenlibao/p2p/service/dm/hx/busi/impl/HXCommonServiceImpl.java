package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.vo.HXBalanceVO;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.service.dm.hx.busi.HXCommonService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.http.HXHttpUtil;
import org.aeonbits.owner.ConfigCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by zcai on 2016/11/30.
 */
@Service
public class HXCommonServiceImpl extends HXOrderProcessImpl implements HXCommonService {

    @Resource
    private HXUserDao hxUserDao;

    @Override
    public HXBalanceVO getBalance(int userId) throws Exception {
        String tradeCode = "OGW00049";
        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        HXAccountInfo accountInfo = hxUserDao.getAccountInfo(userId);
        if (accountInfo == null) {
            throw new TradeException(UserResponseCode.USER_E_ACCOUNT_NOT_EXIST);
        }
        ReqBusinessParams businessParams = new ReqBusinessParams();
        businessParams.setACNO(StringHelper.decode(accountInfo.getAcNo()));
        businessParams.setACNAME(accountInfo.getAcName());
        String message = MessageUtil.getMessageForQuery(businessParams, tradeCode);
        String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
        RespBusinessParams respBusiParams = MessageUtil.getRespBusiParams(result);
        return new HXBalanceVO(respBusiParams.getACCTBAL(), respBusiParams.getAVAILABLEBAL(), respBusiParams.getFROZBL());
    }

    @Override
    public void validateBalance(int userId, BigDecimal amount) throws Exception {
        HXBalanceVO balance = this.getBalance(userId);
        if (amount.compareTo(balance.getAvailable()) > 0) {//先判断华兴那边的可用余额是否足够
            throw new TradeException(TradeResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
        }
    }

    @Transactional
    @Override
    public boolean singleRewards(int userId, BigDecimal amount, String remark) throws Exception {
        boolean isSuccess = false;
        int rewardsRecordId = huaXingDao.addRewardsRecord(userId, amount, remark);
        HXTradeType tradeType = HXTradeType.DBJL;//华兴交易类型
        HXAccountInfo userInfo = hxUserDao.getAccountInfo(userId);
        //创建华兴订单
        int clientType = APPType.PC.getCode();
        HXOrder order = new HXOrder().fromUser(userId, tradeType.getBusiCode(), null);
        huaXingService.createOrder(clientType, order);
        String flowNum = HXUtil.getChannelFlow(tradeType.getPcCode(), order.getId());
        super.submit(order.getId(), flowNum, null);//没有相应的业务订单，所以直接提交
        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        //封装请求报文
        ReqBusinessParams params = new ReqBusinessParams();
        params.setACNO(StringHelper.decode(userInfo.getAcNo()));
        params.setACNAME(userInfo.getAcName());
        params.setAMOUNT(amount.toString());
        params.setREMARK(remark);
        String message = MessageUtil.getMessageByBusi(params, tradeType.getPcCode(), flowNum, clientType);
        String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
        ResponseDocument respdecument = MessageUtil.getXMLDocument(ResponseDocument.class, result);
        String errorCode = respdecument.getHeader().getErrorCode();
        if ("0".equals(errorCode)) {
            String enXmlParam = respdecument.getBody().getXMLPARA();
            RespBusinessParams respBusiParams = MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
            order.setThirdPartyFlowNum(respBusiParams.getRESJNLNO());
            huaXingDao.completeOrder(order);
            huaXingDao.updateRewardsRecord(rewardsRecordId, 2, null);//2=成功
            isSuccess = true;
        } else {
            logger.error("华兴单笔奖励发放失败,userId=[{}], amount=[{}], remark=[{}]", userId, amount, remark);
            huaXingDao.updateRewardsRecord(rewardsRecordId, 3, respdecument.getHeader().getErrorMsg());//3=失败
//            throw new TradeException("奖励发放失败");
        }
        return isSuccess;
    }
}
