package com.fenlibao.p2p.service.xinwang.enterprise.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnpBindcardService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2017/6/29 11:48
 */
@Service
public class XWEnpBindcardServieImpl implements XWEnpBindcardService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());
    @Resource
    private PTCommonDao ptCommonDao;
    @Resource
    private XWRequestDao requestDao;
    @Resource
    private XWAccountDao accountDao;

    @Override
    public Map<String, Object> getBindcardInfo(int enpId, UserRole userRole, String bankcode, String bankcardNo, String uri) {
        String platformUserNo = userRole.getCode() + enpId;
        //特殊账户编码
        int specialId = ptCommonDao.getSpecialUserId();
        if (specialId == enpId) {
            XinwangAccount account = accountDao.getXWRoleAccount(enpId, userRole);
            platformUserNo = account.getPlatformUserNo();
        }
        String requestNo = XinWangUtil.createRequestNo();
        //create order
        XWRequest req=new XWRequest();
        Date requestTime = new Date();
        req.setInterfaceName(XinwangInterfaceName.ENTERPRISE_BIND_BANKCARD.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(enpId);
        requestDao.createRequest(req);
        //package the request params
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("bankcode", bankcode);
        reqData.put("bankcardNo", bankcardNo);
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP() + uri);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        Map<String, Object> sendData;
        try {
            sendData = XinWangUtil.gatewayRequest(XinwangInterfaceName.ENTERPRISE_BIND_BANKCARD.getCode(), reqData);
        } catch (Exception e) {
            LOG.warn("用户新网存管企业绑卡参数异常：enpId:[{}],requestNo:[{}]", enpId, requestNo);
            throw new XWTradeException(XWResponseCode.XW_ASSEMBLE_REQUEST_PARAM_WRONG);
        }
        //save request params
        XWResponseMessage message = new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);
        return sendData;
    }

    @Transactional
    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String code = (String) respMap.get("code");
        String status = (String) respMap.get("status");
        String requestNo = (String) respMap.get("requestNo");
        if (code.equals(GeneralResponseCode.FAIL.getCode()) || status.equals(GeneralStatus.INIT.getStatus())) {
            String errorCode = (String) respMap.get("errorCode");
            String errorMessage = (String) respMap.get("errorMessage");
            LOG.warn(String.format("企业绑卡失败,requestNo:[{}],errorCode:[{}],errorMessage:[{}]",requestNo,errorCode,errorMessage));
            XWRequest request = new XWRequest();
            request.setRequestNo(requestNo);
            request.setState(XWRequestState.SB);
            requestDao.updateRequest(request);
            return;
        }
        //只保存成功的银行卡信息
        String platformUserNo = (String) respMap.get("platformUserNo");
        PlatformAccount ptAccountInfo = accountDao.getPlatformAccountInfoByPlatformUserNo(platformUserNo);
        if (ptAccountInfo != null) {
            Map<String, Object> params = new HashMap<>(3);
            params.put("platformUserNo", platformUserNo);
            String bankcardNo = (String) respMap.get("bankcardNo");
            params.put("bankcardNo", StringHelper.encode(bankcardNo));
            params.put("bankcode", respMap.get("bankcode"));
            accountDao.updateXWAccount(params);
            XWRequest request = new XWRequest();
            request.setRequestNo(requestNo);
            request.setState(XWRequestState.CG);
            requestDao.updateRequest(request);
        }
    }
}
