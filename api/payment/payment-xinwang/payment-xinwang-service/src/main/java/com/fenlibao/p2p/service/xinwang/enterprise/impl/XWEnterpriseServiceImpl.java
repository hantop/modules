package com.fenlibao.p2p.service.xinwang.enterprise.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.enterprise.XWEnterpriseDao;
import com.fenlibao.p2p.model.xinwang.entity.account.OrganizationBaseInfo;
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
import com.fenlibao.p2p.model.xinwang.param.account.EnterpriseRegisterRequestParams;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnterpriseService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2017/6/19 11:09
 */
@Service
public class XWEnterpriseServiceImpl implements XWEnterpriseService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());
    @Resource
    private XWRequestDao requestDao;
    @Resource
    private PTCommonDao ptCommonDao;
    @Resource
    private XWAccountDao accountDao;
    @Resource
    XWEnterpriseDao enterpriseDao;
    @Override
    public Map<String, Object> getModifyInfo(Integer enpId, UserRole userRole, String uri){
        String requestNo = XinWangUtil.createRequestNo();
        Date requestTime = new Date();
        String platformUserNo = userRole.getCode() + enpId;
        //特殊账户编码
        int specialId = ptCommonDao.getSpecialUserId();
        if (specialId == enpId) {
            XinwangAccount account = accountDao.getXWRoleAccount(enpId, userRole);
            platformUserNo = account.getPlatformUserNo();
        }
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.ENTERPRISE_REGISTER.getCode());
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
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP() + uri);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        Map<String, Object> sendData = null;
        try {
            sendData = XinWangUtil.gatewayRequest(XinwangInterfaceName.ENTERPRISE_INFORMATION_UPDATE.getCode(), reqData);
        } catch (Exception e) {
            LOG.warn("用户新网存管修改企业信息参数异常：enpId:[{}],requestNo:[{}]", enpId, requestNo);
            throw new XWTradeException(XWResponseCode.XW_ASSEMBLE_REQUEST_PARAM_WRONG);
        }
        //save request params
        XWResponseMessage message = new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);
        return sendData;
    }

    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String code = (String) respMap.get("code");
        String status = (String) respMap.get("status");
        String requestNo = (String) respMap.get("requestNo");
        String errorCode = (String) respMap.get("errorCode");
        String errorMessage = (String) respMap.get("errorMessage");
        String reviewStatus = (String) respMap.get("reviewStatus");
        String reviewDescription = (String) respMap.get("reviewDescription");
        String qualificationModify = (String) respMap.get("qualificationModify");
        String platformUserNo = (String) respMap.get("platformUserNo");
        XWRequest request = new XWRequest(requestNo);
        if (code.equals(GeneralResponseCode.FAIL.getCode()) || status.equals(GeneralStatus.INIT.getStatus())) {
            LOG.warn(String.format("修改企业信息失败,requestNo:[{}],errorCode:[{}],errorMessage:[{}]",requestNo,errorCode,errorMessage));
            doFail(request);
            return;
        }
        /**
         * 审核状态为必传字段
         */
        accountDao.updateAuditStatus(reviewStatus, platformUserNo);
        if ("REFUSED".equals(reviewStatus)) {
            LOG.warn(String.format("修改企业信息审核不通过失败,requestNo:[{}],reviewDescription:[{}]",requestNo, reviewDescription));
            doFail(request);
            return;
        }
        if ("NO".equals(qualificationModify)) {
            LOG.warn(String.format("修改企业信息无变化,requestNo", requestNo));
            doFail(request);
            return;
        }
        doSuccess(request);
        PlatformAccount ptAccountInfo = accountDao.getPlatformAccountInfoByPlatformUserNo(platformUserNo);
        EnterpriseRegisterRequestParams params = new EnterpriseRegisterRequestParams();
        params.setUserId(ptAccountInfo.getUserId());
        params.setEnterpriseName((String) respMap.get("enterpriseName"));
        params.setBankLicense((String) respMap.get("bankLicense"));
        params.setLegal((String) respMap.get("legal"));  //法人姓名

        params.setLegalIdCardNo((String) respMap.get("legalIdCardNo"));  //法人件号
        params.setLegalIdCardNoWithStar(StringHelper.getIdCardReplace9To16((String) respMap.get("legalIdCardNo")));
        params.setLegalIdCardNoEncrypt(StringHelper.encode((String) respMap.get("legalIdCardNo")));

        params.setContact((String) respMap.get("contact"));  //企业联系人
        params.setContactPhone((String) respMap.get("contactPhone")); //联系人手号
        params.setUnifiedCode((String) respMap.get("unifiedCode"));  //统一社会用代码
        params.setOrgNo((String) respMap.get("orgNo")); //组织机构代码
        params.setBusinessLicense((String) respMap.get("businessLicense")); //营执照编号
        params.setTaxNo((String) respMap.get("taxNo")); //税务登记号
        params.setAuditStatus((String) respMap.get("reviewStatus"));
        enterpriseDao.updateEnterpriseInfoT6161(params);
        enterpriseDao.updateEnterprideInfoT6164(params);
    }

    private void doFail(XWRequest request) {
        request.setState(XWRequestState.SB);
        requestDao.updateRequest(request);
    }

    private void doSuccess(XWRequest request) {
        request.setState(XWRequestState.CG);
        requestDao.updateRequest(request);
    }

    @Override
    public OrganizationBaseInfo getOrganizationBaseInfo(Integer orgId) {
        return enterpriseDao.getOrganizationBaseInfo(orgId);
    }
}
