package com.fenlibao.platform.service.thirdparty.impl;

import com.alibaba.fastjson.JSON;

import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.*;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import com.fenlibao.platform.dao.thirdparty.XWEntrusImportUserMapper;
import com.fenlibao.platform.service.thirdparty.XWEntrustImportUserService;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 */
public class XWEntrustImportUserServiceImpl implements XWEntrustImportUserService{

    @Inject
    private XWEntrusImportUserMapper xwEntrusImportUserMapper;

    @Override
    public void entrustImportUser(Integer userId, String realName, String idCardType, String idCardNo, String mobile, String bankcardNo, String userRole) throws Exception {
        //组装请求
        String requestNo=XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        String platformUserNo = userRole + userId;
        reqData.put("requestNo", requestNo);
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("realName", realName);
        reqData.put("idCardType", idCardType);
        reqData.put("userRole" ,userRole);
        reqData.put("idCardNo", idCardNo);
        reqData.put("mobile", mobile);
        reqData.put("bankcardNo", bankcardNo);
        reqData.put("checkType", CheckType.LIMIT.getCode());
        reqData.put("authList", "REPAYMENT,WITHDRAW,RECHARGE");

        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.ENTRUST_IMPORT_USER.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(userId);
        xwEntrusImportUserMapper.createRequest(req);
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        xwEntrusImportUserMapper.saveRequestMessage(requestParams);
        //发送请求
        String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.ENTRUST_IMPORT_USER.getCode(),reqData);
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(resultJson);
        xwEntrusImportUserMapper.saveResponseMessage(responseParams);
        //处理结果
        String code = (String) respMap.get("code");
        String errorCode = (String) respMap.get("errorCode");
        String errorMessage = (String) respMap.get("errorMessage");
        if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
            doSuccess(respMap);
        } else{
            doFail(requestNo, errorCode ,errorMessage);
        }
    }

    private void doSuccess(Map<String, Object> respMap) throws Exception{
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
        xwEntrusImportUserMapper.updateRequest(requestParams);

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
        account.setImportUserActivate(false);
        xwEntrusImportUserMapper.createXWAccount(account);
        //create asset account
        if(UserRole.parse(userRole)==UserRole.ENTRUST_BORROWERS){
            Map<String,Object> wlzhParams=new HashMap<>();
            wlzhParams.put("userId",userId);
            wlzhParams.put("ptAssetAccountType", PTAssetAccountType.XW_ENTRUST_BORROWERS_WLZH);
            wlzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XEBW", userId));
            xwEntrusImportUserMapper.createPTAssetAccount(wlzhParams);
            Map<String,Object> sdzhParams=new HashMap<>();
            sdzhParams.put("userId",userId);
            sdzhParams.put("ptAssetAccountType",PTAssetAccountType.XW_ENTRUST_BORROWERS_SDZH);
            sdzhParams.put("ptAssetAccountName",StringHelper.getAssetAccountName("XEBS", userId));
            xwEntrusImportUserMapper.createPTAssetAccount(sdzhParams);
        }
        //如果对应的分利宝用户还没实名，要改成已实名
        if(!xwEntrusImportUserMapper.getIdentityAuthState(userId)){
            xwEntrusImportUserMapper.updatePTAccountIdentityState(userId);
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
            xwEntrusImportUserMapper.updatePTAccountInfo(accountInfoParams);
        }
    }

    private void doFail(String requestNo, String errorCode, String errorMessage) throws Exception{
        //请求失败或处理失败
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.SB);
        xwEntrusImportUserMapper.updateRequest(param);
        String errorLog=String.format(XWResponseCode.XW_ENTRUST_IMPORT_USER_FAIL.getMessage(),errorMessage);
        //LOG.error(errorLog);
        throw new XWTradeException(errorCode, errorLog);
    }
}
