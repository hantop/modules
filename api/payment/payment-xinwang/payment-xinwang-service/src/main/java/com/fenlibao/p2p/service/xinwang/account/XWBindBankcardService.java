package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface XWBindBankcardService extends XWNotifyService {
    /**
     * 获取绑定银行卡参数B
     * @return
     */
    Map<String,Object> getBindcardParam(Integer userId, String uri, UserRole userRole) throws Exception;

    /**
     * 获取待确认订单
     * @param interfaceNames
     * @param requestTime
     * @return
     */
    List<String> getOrderNeedComfired(List<XinwangInterfaceName> interfaceNames, Date requestTime);

    /**
     * 确认绑卡
     * @param platformUserNo
     * @param bankcardNo
     * @param bankcode
     * @param requestNo
     */
    void comfiredOrder(String platformUserNo, String bankcardNo, String bankcode, String requestNo);
}
