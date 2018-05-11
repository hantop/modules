package com.fenlibao.p2p.service.user.impl;

import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.form.BindIdCardForm;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.user.GuoXinIdCardAuthService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.thirdparty.guoxin.GxConst;
import com.fenlibao.thirdparty.guoxin.GxWebServices;
import com.fenlibao.thirdparty.guoxin.vo.xsd.IdentityResponseSingle;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 国信实名认证服务类
 *
 * @author Mingway.Xu
 * @date 2016/11/7 14:07
 */
@Service
public class GuoXinIdCardAuthServiceImpl implements GuoXinIdCardAuthService {

    @Resource
    private UserInfoService userInfoService;
    /**
     * 实名认证操作
     *
     * @param userInfo 系统用户信息
     * @param form     身份证信息
     * @return
     */
    @Override
    public Map<String, Object> authUserRealNameAndIdcard(UserInfo userInfo, BindIdCardForm form) throws Throwable {
        Map<String, Object> resultMap = null;
        Map<String, Object> temMap = null;
        String bankCarNum = form.getIdCardNum();
        String orderRules = "GX" + System.currentTimeMillis() + bankCarNum.substring(bankCarNum.length() - 4);

        GxWebServices gxWebServices = new GxWebServices();

        String key = Config.get("gx.key");

        String url = Config.get("gx.invoke.url");

        String username = Config.get("gx.username");
        String password = Config.get("gx.password");

        /******【身份】*********/
        String name = "";
        String identity = "";

        name = form.getIdCardFullName();
        identity = form.getIdCardNum();

        String xml = gxWebServices.gxRealNameAuthentication(name, identity, key, url, username, password);
        IdentityResponseSingle identityResponseSingle = gxWebServices.xmlElements(xml);

        if (identityResponseSingle!=null) {

            String authmessageStatus = identityResponseSingle.getAuthmessageStatus();

            resultMap = new HashMap<>();
            temMap = new HashMap<>();
            resultMap.put("authmessageStatus",authmessageStatus);
            if (GxConst.authmessageStatus_success.equals(authmessageStatus)) {

                String messageStatus = identityResponseSingle.getMessageStatus();
                resultMap.put("messageStatus", messageStatus);
                if(GxConst.messageStatus_success.equals(messageStatus)){
                    resultMap.put("status",identityResponseSingle.getAuthStatus());
                    resultMap.put("retMsg", identityResponseSingle.getAuthResult());
                    resultMap.put("retCode", identityResponseSingle.getAuthmessageStatus());

                    temMap.put("result", (Integer) resultMap.get("status")+1);
                    temMap.put("name_card", form.getIdCardFullName());
                    temMap.put("ret_code", resultMap.get("retCode"));
                    temMap.put("product_id", null);
                    temMap.put("sign_type", null);
                    temMap.put("ret_msg", resultMap.get("retMsg"));
                    temMap.put("outorder_no", orderRules);

                    temMap.put("id_card", StringHelper.encode(bankCarNum));

                    temMap.put("order_fee", GxConst.ORDER_FEE);
                    temMap.put("order_no", null);
                    temMap.put("userId", userInfo.getUserId());
                    temMap.put("useType", "1");

                    userInfoService.insertlianLianAuth(temMap);
                }
            } else {
                resultMap.put("retCode",identityResponseSingle.getAuthmessageStatus());
                resultMap.put("retMsg", identityResponseSingle.getAuthmessageValue());
            }

        } else {

            return resultMap;
        }

        return resultMap;
    }


}
