package com.fenlibao.p2p.dao.xinwang.common;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */
public interface XWRequestDao {
    void createRequest(XWRequest request);
    void saveRequestMessage(XWResponseMessage message);
    void saveResponseMessage(XWResponseMessage responseMessage);
    void updateRequest(XWRequest request);
    XWRequest getByRequestNo(String requestNo);
    List<String> getOrderNeedComfired(XinwangInterfaceName interfaceName, Date requestTime);
    List<XWRequest> getUnbindOrder(XinwangInterfaceName interfaceName, XWOrderStatus orderStatus, Date requestTime);
    int updateRequestStatus(Map<String,Object> params);
}
