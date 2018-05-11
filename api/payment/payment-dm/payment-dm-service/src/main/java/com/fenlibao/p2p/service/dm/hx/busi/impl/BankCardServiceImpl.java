package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.model.api.enums.SystemBoolean;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.dm.hx.busi.BankCardService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import org.aeonbits.owner.ConfigCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by zcai on 2016/10/25.
 */
@Service
public class BankCardServiceImpl extends HXOrderProcessImpl implements BankCardService {

    @Resource
    private HuaXingService huaXingService;
    @Resource
    private HXUserDao hxUserDao;

    @Transactional
    @Override
    public String bindCard(HXAccountInfo accountInfo, int clientType, String uri) throws Exception {
        int userId = accountInfo.getUserId();
        String tradeCode = HXTradeType.getTradeCode(HXTradeType.BK, clientType);
        HXOrder order = new HXOrder().fromUser(userId, HXTradeType.BK.getBusiCode(), null);
        huaXingService.createOrder(clientType, order);
        String flowNum = HXUtil.getChannelFlow(tradeCode, order.getId());
        super.submit(order.getId(), flowNum, null);//绑卡没有相应的业务订单，所以直接提交
        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        ReqBusinessParams params = new ReqBusinessParams();
        params.setTTRANS(String.valueOf(HXTradeType.BK.getCode()));
        params.setACNO(StringHelper.decode(accountInfo.getAcNo()));
        params.setRETURNURL(config.serverDomain() + uri);
        return MessageUtil.getMessageByBusi(params, tradeCode, flowNum, clientType);
    }

    @Override
    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
        HXAccountInfo info = new HXAccountInfo();
        info.setUserId(order.getUserId());
        info.setIsBindBankCard(SystemBoolean.TRUE.getCode());
        hxUserDao.updateAccountInfo(info);
    }
}
