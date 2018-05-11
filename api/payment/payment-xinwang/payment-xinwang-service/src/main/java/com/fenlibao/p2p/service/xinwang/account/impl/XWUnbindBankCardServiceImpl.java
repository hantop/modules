package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.param.account.UnbindBankCardRequestParams;
import com.fenlibao.p2p.service.xinwang.account.XWUnbindBankCardService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
@Service
public class XWUnbindBankCardServiceImpl implements XWUnbindBankCardService {

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    PTCommonDao ptCommonDao;

    @Transactional
    @Override
    public Map<String, Object> getUnbindBankCardRequestData(UnbindBankCardRequestParams params) throws Exception {
        Map<String, Object> reqData = new HashMap<>();

        String requestNo=XinWangUtil.createRequestNo();
        Date createTime = new Date();
        String platformUserNo = params.getUserRole() + params.getUserId();
        //特殊账户编码
        int specialId = ptCommonDao.getSpecialUserId();
        if (specialId == params.getUserId()) {
            XinwangAccount account = accountDao.getXWRoleAccount(params.getUserId(), UserRole.parse(params.getUserRole()));
            platformUserNo = account.getPlatformUserNo();
        }
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+params.getUri());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(createTime));
        Map<String, Object> sendData = XinWangUtil.gatewayRequest(XinwangInterfaceName.UNBIND_BANKCARD.getCode(),reqData);
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.UNBIND_BANKCARD.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(createTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        requestDao.createRequest(req);
        //save request params
        XWResponseMessage message=new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);
        return sendData;
    }

    @Transactional
    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String requestNo=(String)respMap.get("requestNo");
        String platformUserNo=(String)respMap.get("platformUserNo");
        String status = (String) respMap.get("status");
        UserRole userRole ;
        Integer userId;
        if (StringUtils.isNotBlank(platformUserNo)&&!platformUserNo.startsWith("SYS")) {
            userRole = UserRole.parse( platformUserNo.replaceAll("[0-9]",""));
            userId = Integer.parseInt( platformUserNo.replaceAll("[A-Z]",""));
            Integer id = accountDao.getPMSAudit(userId, userRole);
            if (id != null) {
                accountDao.updatePMSAuditStatus(id);
            }
        }
        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        if (GeneralStatus.SUCCESS.getStatus().equals(status)) {
            accountDao.clearBankInfo(platformUserNo);
            requestParams.setState(XWRequestState.CG);
        } else {
            requestParams.setState(XWRequestState.SB);
        }
        requestDao.updateRequest(requestParams);
    }

    @Override
    public List<XWRequest> getOrderNeedComfired(XinwangInterfaceName unbindBankcard, XWOrderStatus dqr, Date requestTime) {
        return requestDao.getUnbindOrder(unbindBankcard,dqr,requestTime);
    }

    @Transactional
    @Override
    public void comfiredOrder(String platformUserNo, XWRequest requestParam) {
        accountDao.clearBankInfo(platformUserNo);
        requestDao.updateRequest(requestParam);
    }
}
