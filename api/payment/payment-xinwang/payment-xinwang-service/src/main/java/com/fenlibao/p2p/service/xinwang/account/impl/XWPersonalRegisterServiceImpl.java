package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.*;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.param.account.PersonalRegisterRequestParams;
import com.fenlibao.p2p.service.xinwang.account.XWPersonalRegisterService;import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
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
public class XWPersonalRegisterServiceImpl implements XWPersonalRegisterService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Transactional
    @Override
    public Map<String,Object> getPersonalRegisterRequestData(PersonalRegisterRequestParams params) throws Exception {
        String requestNo=XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        String platformUserNo=params.getUserRole() + params.getUserId();
        //package the request params
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("idCardType", IdCardType.PRC_ID.getCode());
        reqData.put("userRole", params.getUserRole());
        reqData.put("checkType", CheckType.LIMIT.getCode());
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+params.getUri());
        reqData.put("userLimitType", null);
        reqData.put("authList", params.getAuthList());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("realName", params.getRealName());
        reqData.put("idCardNo", params.getIdCardNo());
        reqData.put("bankcardNo", params.getBankcardNo());
        reqData.put("mobile", params.getMobile());
        reqData.put("failTime", new DateTime().plusYears(XinwangConsts.DEFAULT_USER_AUTH_YEAR).toString("yyyyMMdd"));
        reqData.put("amount", XinwangConsts.DEFAULT_USER_AUTH_MONEY);
        Map<String,Object> sendData=XinWangUtil.gatewayRequest(XinwangInterfaceName.PERSONAL_REGISTER_EXPAND.getCode(),reqData);
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.PERSONAL_REGISTER_EXPAND.getCode());
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
        return sendData;
    }

    @Transactional
    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String requestNo=(String)respMap.get("requestNo");
        String platformUserNo=(String)respMap.get("platformUserNo");
        String userRole=(String)respMap.get("userRole");
        String auditStatus=(String)respMap.get("auditStatus");
        String bankcardNo=(String)respMap.get("bankcardNo");
        String bankcode=(String)respMap.get("bankcode");
        String mobile=(String)respMap.get("mobile");
        Integer userId=Integer.parseInt(platformUserNo.replace(userRole,""));
        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);

        Integer count = accountDao.countXWAccount(platformUserNo);
        if(count > 0){
            LOG.error("用户已存在数据库，无需重复插入:{}" , respMap);
            return;
        }
        //save account info
        XinwangAccount account=new XinwangAccount();
        account.setPlatformUserNo(platformUserNo);
        account.setUserRole(UserRole.parse(userRole));
        account.setUserId(userId);
        account.setAuditStatus(AuditStatus.parse(auditStatus));
        account.setUserType(XWUserType.PERSONAL);
        account.setBankcardNo(StringHelper.encode(bankcardNo));
        account.setBankcode(bankcode);
        account.setMobile(mobile);
        account.setAuthlist("1");
        accountDao.createXWAccount(account);
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
        //如果对应的分利宝用户还没实名，要改成已实名
        if(!accountDao.getIdentityAuthState(userId)){
            accountDao.updatePTAccountIdentityState(userId);
            String realName=(String)respMap.get("realName");
            String idCardNo=(String)respMap.get("idCardNo");
            // 身份证号码,3-18位星号替换
            String idcardStar = StringHelper.getIdCardNoAsterisk(idCardNo);
            // 身份证号码AES加密
            String idcardEncrypt = StringHelper.encode(idCardNo);
            Date birthday = StringHelper.getBirthdayFromIdCard(idCardNo);
            String authState = "TG";
            Map<String,Object> accountInfoParams=new HashMap<>();
            accountInfoParams.put("userId",userId);
            accountInfoParams.put("realName",realName);
            accountInfoParams.put("authState",authState);
            accountInfoParams.put("idcardStar",idcardStar);
            accountInfoParams.put("idcardEncrypt",idcardEncrypt);
            accountInfoParams.put("birthday",birthday);
            accountDao.updatePTAccountInfo(accountInfoParams);
        }
    }

}
