package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.param.account.UnbindBankCardRequestParams;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface XWUnbindBankCardService extends XWNotifyService {

    /**
     * 预留手机号更新
     * @param params
     * @return
     */
    Map<String,Object> getUnbindBankCardRequestData(UnbindBankCardRequestParams params) throws Exception;

    /**
     * 获取需要对账的订单
     * @param unbindBankcard
     * @param dqr
     * @param requestTime
     * @return
     */
    List<XWRequest> getOrderNeedComfired(XinwangInterfaceName unbindBankcard, XWOrderStatus dqr, Date requestTime);

    void comfiredOrder(String platformUserNo, XWRequest requestParam);
}
