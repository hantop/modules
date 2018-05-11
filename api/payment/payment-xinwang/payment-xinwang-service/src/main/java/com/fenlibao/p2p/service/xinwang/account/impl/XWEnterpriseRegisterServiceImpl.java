package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.enterprise.XWEnterpriseDao;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.*;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.param.account.EnterpriseRegisterRequestParams;
import com.fenlibao.p2p.model.xinwang.param.account.PersonalRegisterRequestParams;
import com.fenlibao.p2p.service.xinwang.account.XWEnterpriseRegisterService;
import com.fenlibao.p2p.service.xinwang.account.XWPersonalRegisterService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9.
 */
@Service
public class XWEnterpriseRegisterServiceImpl implements XWEnterpriseRegisterService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    XWEnterpriseDao enterpriseDao;

    @Transactional
    @Override
    public Map<String,Object> getEnterpriseRegisterRequestData(EnterpriseRegisterRequestParams params) throws Exception {
        String requestNo=XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        String userRole = UserRole.BORROWERS.getCode();
        if(StringUtils.isNotBlank(params.getUserRole())){
            userRole = params.getUserRole();
        }
        String platformUserNo = userRole + params.getUserId();
        //package the request params
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("enterpriseName", params.getEnterpriseName());
        reqData.put("bankLicense", params.getBankLicense());
        reqData.put("legal", params.getLegal());
        reqData.put("idCardType", params.getIdCardType());
        reqData.put("legalIdCardNo", params.getLegalIdCardNo());
        reqData.put("contact", params.getContact());
        reqData.put("contactPhone", params.getContactPhone());
        reqData.put("userRole", userRole);
        reqData.put("bankcardNo", params.getBankcardNo());
        reqData.put("bankcode", params.getBankcode());
        reqData.put("unifiedCode", params.getUnifiedCode());
        reqData.put("orgNo", params.getOrgNo());
        reqData.put("businessLicense", params.getBusinessLicense());
        reqData.put("taxNo", params.getTaxNo());
        reqData.put("creditCode", params.getCreditCode());
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+params.getUri());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        StringBuilder sb = new StringBuilder().append(AuthList.COMPENSATORY).append(",").append(AuthList.REPAYMENT).append(",").append(AuthList.WITHDRAW);
        reqData.put("authList", sb.toString());
        reqData.put("failTime", new DateTime().plusYears(XinwangConsts.DEFAULT_USER_AUTH_YEAR).toString("yyyyMMdd"));
        reqData.put("amount", XinwangConsts.DEFAULT_USER_AUTH_MONEY);
        Map<String,Object> sendData=XinWangUtil.gatewayRequest(XinwangInterfaceName.ENTERPRISE_REGISTER.getCode(),reqData);
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.ENTERPRISE_REGISTER.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(params.getUserId());
        requestDao.createRequest(req);
        //save request params
        XWResponseMessage message=new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);

        params.setLegalIdCardNoWithStar(StringHelper.getIdCardReplace9To16(params.getLegalIdCardNo()));
        params.setLegalIdCardNoEncrypt(StringHelper.encode(params.getLegalIdCardNo()));
        params.setBankcardNo(StringHelper.encode(params.getBankcardNo()));
        if(enterpriseDao.getCountEnterpriseUser(params.getUserId()) == 0){
            //插入企业信息
            params.setEnterpriseNo(StringHelper.getAssetAccountName("QYZH", params.getUserId()));
            enterpriseDao.insertEnterpriseInfoToT6161(params);
        }else {
            //throw new APIException("企业用户已存在");
            //修改信息的时候改变状态为审核中
            params.setAuditStatus("AUDIT");
            enterpriseDao.updateEnterpriseInfoT6161(params);
        }

        if(enterpriseDao.getCountEnterpriseUser6164(params.getUserId()) == 0){
            enterpriseDao.insertEnterpriseInfoToT6164(params);
        }else {
            enterpriseDao.updateEnterprideInfoT6164(params);
        }
        return sendData;
    }

    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String requestNo=(String)respMap.get("requestNo");
        String platformUserNo=(String)respMap.get("platformUserNo");
        String userRole=(String)respMap.get("userRole");
        String auditStatus=(String)respMap.get("auditStatus");
        String bankcardNo=(String)respMap.get("bankcardNo");
        String bankcode=(String)respMap.get("bankcode");
        Integer userId=Integer.parseInt(platformUserNo.replace(userRole,""));
        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);

        //判断是否t_xw_account是否已存在数据
        XinwangAccount account=new XinwangAccount();
        //用户信息表是否存在对应数据
        Integer count = accountDao.countXWAccount(platformUserNo);
        if(count > 0){
            //update account info
            account.setPlatformUserNo(platformUserNo);
            account.setAuditStatus(AuditStatus.parse(auditStatus));
            accountDao.updateXWAccountAuditStatus(account);
        }else {
            //save account info
            account.setPlatformUserNo(platformUserNo);
            account.setUserRole(UserRole.parse(userRole));
            account.setUserId(userId);
            account.setAuditStatus(AuditStatus.parse(auditStatus));
            account.setUserType(XWUserType.ORGANIZATION);
            account.setBankcardNo(StringHelper.encode(bankcardNo));
            account.setBankcode(bankcode);
            account.setAuthlist("1");
            accountDao.createXWAccount(account);
        }

        //更新t6161状态
        if(UserRole.parse(userRole) == UserRole.GUARANTEECORP) {
            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            param.put("auditStatus", auditStatus);
            accountDao.updateAuditStatusToT6161(param);
        }

        if(count == 0){
            //create asset account
            if(UserRole.parse(userRole)==UserRole.INVESTOR){
                Map<String,Object> wlzhParams=new HashMap<>();
                wlzhParams.put("userId",userId);
                wlzhParams.put("ptAssetAccountType",PTAssetAccountType.XW_INVESTOR_WLZH);
                wlzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XIW", userId));
                accountDao.createPTAssetAccount(wlzhParams);
                Map<String,Object> sdzhParams=new HashMap<>();
                sdzhParams.put("userId",userId);
                sdzhParams.put("ptAssetAccountType",PTAssetAccountType.XW_INVESTOR_SDZH);
                sdzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XIS", userId));
                accountDao.createPTAssetAccount(sdzhParams);
            }
            else if(UserRole.parse(userRole)==UserRole.BORROWERS){
                Map<String,Object> wlzhParams=new HashMap<>();
                wlzhParams.put("userId",userId);
                wlzhParams.put("ptAssetAccountType",PTAssetAccountType.XW_BORROWERS_WLZH);
                wlzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XBW", userId));
                accountDao.createPTAssetAccount(wlzhParams);
                Map<String,Object> sdzhParams=new HashMap<>();
                sdzhParams.put("userId",userId);
                sdzhParams.put("ptAssetAccountType",PTAssetAccountType.XW_BORROWERS_SDZH);
                sdzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XBS", userId));
                accountDao.createPTAssetAccount(sdzhParams);
            }else if(UserRole.parse(userRole) == UserRole.GUARANTEECORP){
                Map<String,Object> wlzhParams=new HashMap<>();
                wlzhParams.put("userId",userId);
                wlzhParams.put("ptAssetAccountType",PTAssetAccountType.XW_GUARANTEECORP_WLZH);
                wlzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XGW", userId));
                accountDao.createPTAssetAccount(wlzhParams);
                Map<String,Object> sdzhParams=new HashMap<>();
                sdzhParams.put("userId",userId);
                sdzhParams.put("ptAssetAccountType",PTAssetAccountType.XW_GUARANTEECORP_SDZH);
                sdzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XGS", userId));
                accountDao.createPTAssetAccount(sdzhParams);
            }
        }
        //如果对应的分利宝用户还没实名，要改成已实名
        if(!accountDao.getIdentityAuthState(userId)){
            accountDao.updatePTAccountIdentityState(userId);
        }

    }

}
