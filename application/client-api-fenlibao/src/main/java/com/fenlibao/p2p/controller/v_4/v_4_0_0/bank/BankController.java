package com.fenlibao.p2p.controller.v_4.v_4_0_0.bank;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6114_F10;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.lianpay.api.HttpRequestSimple;
import com.fenlibao.lianpay.share.config.PartnerConfig;
import com.fenlibao.lianpay.share.util.FuncUtils;
import com.fenlibao.lianpay.yintong.BankCardPayBean;
import com.fenlibao.p2p.model.entity.CheckError;
import com.fenlibao.p2p.model.entity.SmsValidcode;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.form.BankCardForm;
import com.fenlibao.p2p.model.form.BankCardQuery;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.BankCardVO;
import com.fenlibao.p2p.model.vo.BankVO;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.AuthList;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bank.BankCardDmService;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.bid.IUserDmService;
import com.fenlibao.p2p.service.withdraw.IWithdrawService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by LouisWang on 2015/8/15.
 */
@RestController("v_4_0_0/BankController")
@RequestMapping(value = "bank", headers = APIVersion.v_4_0_0)
public class BankController {
    private static final Logger logger= LogManager.getLogger(BankController.class);

    @Resource
    private IUserDmService userMgrSvc;
    @Resource
    private BankCardDmService bankCardDmService;
    @Resource
    private BankService bankService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private IWithdrawService withdrawService;
    @Resource
    private XWUserInfoService xwUserInfoService;

    /**
     * 添加银行卡
     *
     */
    @RequestMapping(value = "addBankCard", method = RequestMethod.POST)
    public HttpResponse addBankCard(
            @ModelAttribute BaseRequestFormExtend paramForm,
            @RequestParam(required = false, value = "bankKhhName") String bankKhhName,
            @RequestParam(required = true, value = "bankNumber") String bankNumber,
            @RequestParam(required = false, value = "bankPhone") String bankPhone,
            @RequestParam(required = false, value = "userName") String userName,
            @RequestParam(required = true, value = "bankId") String bankId,
            @RequestParam(required = false, value = "city") String city,
            @RequestParam(required = false, value = "subbranch") String subbranch,
            @RequestParam(required = true, value = "type") String type,
            @RequestParam(required = true, value = "phoneNum") String phoneNum, //电话号码
            @RequestParam(required = true, value = "verifyCode") String verifyCode,   //验证码
            @RequestParam(required = false, value = "isUp") String isUp     //0 是新增 1更新
    ) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate()
                || StringUtils.isBlank(bankNumber)
                || StringUtils.isBlank(bankId)
                || StringUtils.isBlank(type)
                || StringUtils.isBlank(phoneNum)
                || StringUtils.isBlank(verifyCode)){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }
        String banknumberNum = bankNumber.replaceAll("\\s", "");
        //校验验证码
        SmsValidcode smscode = userInfoService.getLastSmsCode(phoneNum, String.valueOf(InterfaceConst.BIND_BankCard_TYPE));
        String code=null==smscode?"":smscode.getValidCode();

        if(code.equals(verifyCode)){
            //校验过期时间
            long outTime = smscode.getOutTime().getTime();
            if (outTime <= System.currentTimeMillis()) {
                apiResponse.setCodeMessage(ResponseCode.COMMON_CAPTCHA_TIMEOUT);
                return apiResponse;
            }

            /**
             * 通过连连支付银行 Bin接口查询银行卡号和选择银行是否属于同个银行
             * 1.先从数据库查询对应的连连银行编码
             */
            BankVO bank = bankService.getBank(IntegerParser.parse(bankId));
            if(bank != null){
                BankCardPayBean bankCardBean = new BankCardPayBean();
                bankCardBean.setCard_no(banknumberNum);
                String retBankCard = lianLianTraderapi(bankCardBean,"bankcardquery.htm");
                Map retBankCardMsg = (Map) JSONObject.parseObject(retBankCard);

                String bank_code = (String) retBankCardMsg.get("bank_code");
                if(!StringUtils.isBlank(bank_code)){
                    if(!bank.getLlCode().equals(bank_code.trim())){
                        apiResponse.setCodeMessage("30702", "选择银行和填入卡号不是同属银行");
                        return apiResponse;
                    }
                }else{
                    apiResponse.setCodeMessage("30701", "银行卡号输入不正确");
                }
            }else{
                apiResponse.setCodeMessage(ResponseCode.PAYMENT_BANK_CARD_FORMAT_WRONG);
                return apiResponse;
            }


        } else {
            //添加验证错误记录
            CheckError checkError=new CheckError();
            checkError.setSendType(1);
            checkError.setSendTo(phoneNum);
            checkError.setVeryCode(verifyCode);
            checkError.setCrateTime(new Date());
            userInfoService.insertMatchVerifyCodeError(checkError);
            apiResponse.setCodeMessage(ResponseCode.COMMON_CAPTCHA_INVALID);
        }

        //开始添加银行卡信息
        try {
            T6110 user = userMgrSvc.selectT6110(paramForm.getUserId(), true);
            boolean validCard = StringHelper.isEmpty(banknumberNum);
            if (user != null && user.F06 == T6110_F06.ZRR)
            {
                String allowBankCards = InterfaceConst.ALLOW_BANKCARDS;
                if(!StringHelper.isEmpty(allowBankCards))
                {
                    if(allowBankCards.contains(banknumberNum))
                    {
                        validCard = false;
                    }
                    else
                    {
                        validCard = !checkBankCard(banknumberNum);
                    }
                }
                else
                {
                    validCard = !checkBankCard(banknumberNum);
                }
            }
            if (validCard)
            {
                apiResponse.setCodeMessage(ResponseCode.PAYMENT_BANK_CARD_FORMAT_WRONG);
                return apiResponse;
            }
            BankCardVO bcd = bankCardDmService.getBankCar(banknumberNum);

            //未实名认证
            if(!userMgrSvc.isSmrz(paramForm.getUserId())){
                apiResponse.setCodeMessage(ResponseCode.USER_IDENTITY_UNAUTH );
                return apiResponse;
            }

            // 封装银行卡BankCardQuery信息
            BankCardQuery query = new BankCardQuery();
            query.setBankId(IntegerParser.parse(bankId)); //3
            query.setBankNumber(banknumberNum);
            query.setCity(city);//130202
            query.setSubbranch(subbranch);   //广州农行支行
            query.setUserName(user.F14);     //房傲冬
            query.setType(IntegerParser.parse(type));     //1个人 2企业
            query.setStatus(BankCardStatus.QY.name());
            query.setAcount(paramForm.getUserId());

            if (bcd != null){
                //启用银行卡
                int id = bcd.id;
                if (bcd.acount != (paramForm.getUserId()) ){
                    //判断已经存在的银行卡状态，如果为停用，则其他人可以绑定，否则其他人不可以绑定
                    if(bcd.status.equalsIgnoreCase(T6114_F08.TY.name())){
                        bankCardDmService.updateTY(id, query, paramForm.getUserId());
                    }else
                    {
                        apiResponse.setCodeMessage("30703", "当前银行卡号已存在！");
                        return apiResponse;
                    }
                }else
                {
                    bankCardDmService.update(id, query);
                }

                if("1".equals(isUp)){  //更新卡的开户行相关信息
                    if(StringUtils.isEmpty(query.getCity())){
                        apiResponse.setCodeMessage(ResponseCode.PAYMENT_BIND_CARD_ONE);
                        return apiResponse;
                    }
                    if(StringUtils.isEmpty(query.getSubbranch())){
                        apiResponse.setCodeMessage(ResponseCode.PAYMENT_BIND_CARD_ONE);
                        return apiResponse;
                    }
                    userMgrSvc.updateT6114BankAdr(query, user.F01);
                }else {
                    //只能添加一张银行卡
                    apiResponse.setCodeMessage(ResponseCode.PAYMENT_BIND_CARD_ONE);
                }
            }else{
                int carId = bankCardDmService.AddBankCar(query);
                if(carId > 0){
                    apiResponse.setCodeMessage(ResponseCode.SUCCESS);
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            apiResponse.setCodeMessage(ResponseCode.FAILURE);
        }
        return apiResponse;
    }
        /**
         * 添加银行卡 以前的处理方法
         * User-Agent: Fiddler
         * Host: localhost
         * Content-Type: application/x-www-form-urlencoded;charset=utf-8
         */
         @RequestMapping(value = "addBankCard2", method = RequestMethod.POST)
            public HttpResponse addBankCard2(
                    @ModelAttribute BaseRequestForm paramForm,
                    @RequestParam(required = true, value = "token") String token,
                    @RequestParam(required = false, value = "bankKhhName") String bankKhhName,
                    @RequestParam(required = true, value = "bankNumber") String bankNumber,
                    @RequestParam(required = false, value = "bankPhone") String bankPhone,
                    @RequestParam(required = true, value = "userId") String userId,
                    @RequestParam(required = false, value = "userName") String userName,
                    @RequestParam(required = true, value = "bankId") String bankId,
                    @RequestParam(required = false, value = "city") String city,
                    @RequestParam(required = false, value = "subbranch") String subbranch,
                    @RequestParam(required = false, value = "type") String type,
                    @RequestParam(required = false, value = "isUp") String isUp //0 是新增 1更新
            ) {
             HttpResponse apiResponse = new HttpResponse();

         //    String bankCardNum = AES.getInstace().decrypt(bankNumber);
             // 获取绑卡请求参数 PC 加卡数据 PS:需要注意的是这里可能根据银行卡号去查询对应的银行
             String banknumber = bankNumber.replaceAll("\\s", "");
             if(StringUtils.isEmpty(bankId)){
                 apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
             }
             try {
                 T6110 user = userMgrSvc.selectT6110(Integer.parseInt(userId), true);
                 // 封装银行卡Form信息
                 BankCardForm bankCardForm = new BankCardForm();
                 bankCardForm.setBankId(bankId); //3
                 bankCardForm.setBanknumber(banknumber);
                 bankCardForm.setCity(city);//130202
                 bankCardForm.setSubbranch(subbranch);   //广州农行支行
                 bankCardForm.setUserName(user.F14);     //房傲冬
                 bankCardForm.setUserType("1");     //1

                 if(user != null){
                     // 查询第三方账号是否存在
             //        T6119 t6119 = userMgrSvc.selectT6119(user.F01);
             //        if (t6119 != null && StringUtils.isNoneBlank(t6119.F03)){
                         // 查询当前登陆用户已绑定银行卡数
                         int bankCardCount = userMgrSvc.selectCountT6114(Integer.parseInt(userId));
                         if(bankCardCount == 0){
                             if (StringUtils.isNotBlank(banknumber)){
                                 // 查询该卡号是否存在
                                 T6114 t6114 = userMgrSvc.selectT6114(banknumber);
                                 if (t6114 != null && t6114.F02 != user.F01)
                                 {
                                     //当前银行卡号已存在
                                     apiResponse.setCodeMessage(ResponseCode.FAILURE);
                                 }
                                 // 判断登陆用户是否自然人
                                 if (user.F06 == T6110_F06.ZRR){

                                     // 如果银行卡为空
                                     if (t6114 == null){
                                         // 先插入一张状态为停用且没实名认证通过的银行卡 T6114_F08.QY, T6114_F10.TG
                                 //        userMgrSvc.insertT6114(user.F01,bankCardForm, T6114_F08.TY, T6114_F10.BTG);
                                         userMgrSvc.insertT6114(user.F01,bankCardForm, T6114_F08.QY, T6114_F10.TG);
                                         apiResponse.setCode("0000");
//                                         MerBindCard merBindCard = BindCardFactory.createBindCard(user.F01, userMgrSvc, banknumber, userName);
//                                         Map<String, String> reqMap = Mer2PlatUtils.makeReqData(merBindCard);

//                                         String reqUrl = ConstantsVar.SERVICE_URL;
//                                         this.logger.info("发送请求地址为[" + reqUrl + "]参数为[" + reqMap + "]");
//                                         String formStr = PayUtils.createForm(reqMap, reqUrl);

//                                        response.setContentType("text/html");
//                                        response.setCharacterEncoding("UTF-8");
//                                        response.getWriter().write(formStr);
//                                          response.getWriter().close();
                                     }else if((T6114_F08.TY.equals(t6114.F08) && T6114_F10.BTG.equals(t6114.F10))){
//                                         MerBindCard merBindCard = BindCardFactory.createBindCard(user.F01,userMgrSvc, banknumber, userName);
//                                         Map<String, String> reqMap = Mer2PlatUtils.makeReqData(merBindCard);
//                                         String reqUrl = ConstantsVar.SERVICE_URL;
//                                         this.logger.info("发送请求地址为[" + reqUrl + "]参数为[" + reqMap + "]");
//                                         String formStr = PayUtils.createForm(reqMap, reqUrl);

//                                        response.setContentType("text/html");
//                                        response.setCharacterEncoding("UTF-8");
//                                        response.getWriter().write(formStr);
//                                          response.getWriter().close();
                                         apiResponse.setCode("0000");
                                     }else {
                                         //当前银行卡号已存在！
                                         apiResponse.setCodeMessage(ResponseCode.FAILURE);
                                     }
                                 }else { //// 登陆用户为非自然人，则添加一张正常的银行卡，因为在联动那边这个是线下行为，我们这边只有开户银行跟他们的一致即可
                                     if (t6114 == null)
                                     {
                                         userMgrSvc.insertT6114(user.F01,bankCardForm, T6114_F08.QY, T6114_F10.TG);
                                         //   referer = referer.concat("?close=close");
                                     }
                                     else if ((T6114_F08.TY.equals(t6114.F08) && T6114_F10.BTG.equals(t6114.F10)))
                                     {
                                         userMgrSvc.updateT6114Status(T6114_F08.QY, T6114_F10.TG,user.F01);
                                         // referer = referer.concat("?close=close");
                                     }
                                     else
                                     {
                                         //当前银行卡号已存在
                                         apiResponse.setCodeMessage(ResponseCode.PAYMENT_BANK_REPEAT);
                                     }
                                     //  sendRedirect(request, response, referer);
                                 }
                             }else {
                                 //银行卡错误
                                 apiResponse.setCodeMessage(ResponseCode.PAYMENT_BANK_CARD_FORMAT_WRONG);
                             }
                         }else{
                             if("1".equals(isUp)){  //更新卡的开户行相关信息
                                    if(StringUtils.isEmpty(bankCardForm.getCity())){
                                        apiResponse.setCodeMessage(ResponseCode.PAYMENT_BIND_CARD_ONE);
                                        return apiResponse;
                                     }
                                     if(StringUtils.isEmpty(bankCardForm.getSubbranch())){
                                         apiResponse.setCodeMessage(ResponseCode.PAYMENT_BIND_CARD_ONE);
                                         return apiResponse;
                                     }
                              //     userMgrSvc.updateT6114Bank(bankCardForm,user.F01);
                             }else {
                                 //只能添加一张银行卡
                                 apiResponse.setCodeMessage(ResponseCode.PAYMENT_BIND_CARD_ONE);
                             }
                         }
                     }else {
                         //请先完成第三方认证信息
                         apiResponse.setCodeMessage(ResponseCode.FAILURE);
                     }
                 /*}else {
                     //用户不存在
                     apiResponse.setCodeMessage(ResponseCode.USER_NOT_EXIST);
             }*/

             }catch (Throwable throwable){
                 apiResponse.setCodeMessage(ResponseCode.FAILURE);
                 throwable.printStackTrace();
             }
             return apiResponse;
         }

    /**
     *  用户交易状态信息
     *
     */
    @RequestMapping(value = "userDealStatus", method = RequestMethod.GET)
    public HttpResponse userDealStatus(@ModelAttribute BaseRequestFormExtend paramForm,
                                       @RequestParam(required = false, value="versionType") String versionType
    ) {
        HttpResponse apiResponse = new HttpResponse();
        if(!paramForm.validate()){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }
        if(VersionTypeEnum.CG.getCode().equals(versionType)){
            //存管
            try {
                //获取存管账户信息
                String platformUserNo = UserRole.INVESTOR.getCode() + paramForm.getUserId();
                XinwangUserInfo xinwangUserInfo = xwUserInfoService.queryUserInfo(platformUserNo);
                if(xinwangUserInfo == null){
                    apiResponse.setCodeMessage(ResponseCode.USER_NOT_EXIST);
                }else{
                    Map<String,Object> outMap = new HashMap<String,Object>();
                    Map<String,Object> data = userInfoService.getUserAssetsByXW(paramForm.getUserId());
                    outMap.put("balance",data.get("balance"));
                    String authlist = xinwangUserInfo.getAuthlist();
                    outMap.put("noDealpassword",StringUtils.isNotBlank(authlist) && authlist.contains(AuthList.TENDER.getCode()) ? "1" : "0");
                    BigDecimal poundage = withdrawService.getPoundage(paramForm.getUserId());
                    outMap.put("factorage", poundage.toString());
                    apiResponse.setData(outMap);
                }
            } catch (Exception e) {
                logger.error(e, e);
                apiResponse.setCodeMessage(ResponseCode.FAILURE);
            }
            return apiResponse;
        }else{
            //非存管
            try {
                Map<String,Object> retMap = bankService.getUserDealStatus(paramForm.getUserId());
                if(retMap != null && retMap.size() > 0){
                    Map<String,Object> outMap = new HashMap<String,Object>();
                    outMap.put("noDealpassword",(boolean) retMap.get("tradersPwdStatus") ? "1" : "0");
                    outMap.put("balance",(BigDecimal)(retMap.get("balance")));
                    BigDecimal poundage = withdrawService.getPoundage(paramForm.getUserId());
                    outMap.put("factorage", poundage.toString());
                    apiResponse.setData(outMap);
                }else {
                    apiResponse.setCodeMessage(ResponseCode.USER_NOT_EXIST);
                }
            } catch (Exception e) {
                logger.error(e, e);
                apiResponse.setCodeMessage(ResponseCode.FAILURE);
            }
        }
        return apiResponse;
    }

    /**
     *  支行模糊查询接口 查询连连支付 大额行号查询 CNAPSCodeQuery
     *
     *
     */
    @RequestMapping(value = "cnapsCode", method = RequestMethod.GET)
    public HttpResponse cnapsCode(@ModelAttribute BaseRequestForm paramForm,
                                  @RequestParam(required = false, value = "provinceId") String provinceId,
                                  @RequestParam(required = true, value = "cityid") String cityid,
                                  @RequestParam(required = true, value = "payType") String payType,
                                  @RequestParam(required = false, value = "bankNum") String bankNum,
                                  @RequestParam(required = false, value = "query") String query,
                                  @RequestParam(required = true, value = "bankCardId") String bankCardId
    ) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate()
                || StringUtils.isBlank(cityid)
                || StringUtils.isBlank(bankCardId)
                || StringUtils.isBlank(payType)){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }
        //20150911 Louis 根据银行id 去查银行卡号
        try {
        	String branchBankName = "银行";
        	if (StringUtils.isNotBlank(query)) {
        		branchBankName = new String(query.getBytes("iso-8859-1"),"utf-8");
        	}
            Map<String,Object> bandCarMap = bankService.getBankCardById(Integer.parseInt(bankCardId));
            if (bandCarMap != null){
                String bankNumberCode = (String) bandCarMap.get("bankNumber");
                if (!StringUtils.isBlank(bankNumberCode)){
                    bankNum = StringHelper.decode(bankNumberCode);
                }

                Map<String,Object> retMap = new HashMap<String,Object>();
                if("1".equals(payType.trim())){     //连连支行的查询方式
                    //1.卡号和银行编码二者选一传 连连可以根据卡号找到对应的哪家银行
                    BankCardPayBean bankCardPayBean = new BankCardPayBean();
                    //2.根据行政区号去查找对应点的市cityId
                    //CountyVO county = districtService.getCounty(payType, Integer.parseInt(cityid));
                    // int llCityCode = county.getLlCode();
                    bankCardPayBean.setCity_code(String.valueOf(cityid));
                    bankCardPayBean.setBrabank_name(branchBankName);
                    // bankCardPayBean.setBank_code("01020000");
                    bankCardPayBean.setCard_no(bankNum);
                    String retBankCard = lianLianTraderapi(bankCardPayBean, "CNAPSCodeQuery.htm");
                    Map retBankCardMsg = (Map) JSONObject.parseObject(retBankCard);
                    String retCode = (String) retBankCardMsg.get("ret_code");
                    //返回"0000" 则查询结果正确 不是则返回错误信息
                    if(InterfaceConst.RETCODE.equals(retCode)){
                        List cardList = (List) retBankCardMsg.get("card_list");
                        List cityAll = null;
                        if (cardList != null && cardList.size() > 0){
                            cityAll = new ArrayList();

                            for (int i = 0; i < cardList.size(); i++) {
                                JSONObject cityObj = (JSONObject) cardList.get(i);
                                String branchName = (String) cityObj.get("brabank_name");
                                String branchId = (String) cityObj.get("prcptcd");

                                JSONObject cityJson = new JSONObject();
                                cityJson.put("branchName", branchName);
                                cityJson.put("branchId", branchId);
                                cityAll.add(cityJson);
                            }
                            retMap.put("list",JSON.toJSON(cityAll));
                        }else{
                            apiResponse.setCodeMessage(retCode, (String) retBankCardMsg.get("ret_msg"));
                            return apiResponse;
                        }
                    }else{
                        apiResponse.setCodeMessage(retCode, (String) retBankCardMsg.get("ret_msg"));
                        return apiResponse;
                    }
                }

                apiResponse.setData(retMap);
            }else {
                apiResponse.setCodeMessage(ResponseCode.FAILURE);
                return apiResponse;
            }


        } catch (Throwable throwable) {
            throwable.printStackTrace();
            apiResponse.setCodeMessage(ResponseCode.FAILURE);
        }
        return apiResponse;
    }

    protected String lianLianTraderapi(BankCardPayBean bankCardBean,String url){
        //通用的验证数据
        bankCardBean.setOid_partner(PartnerConfig.OID_PARTNER);
        bankCardBean.setSign_type(PartnerConfig.SIGN_TYPE);
        bankCardBean.setSign(FuncUtils.genSign(JSON.parseObject(JSON.toJSONString(bankCardBean))));
        String reqJson = JSON.toJSONString(bankCardBean);
        HttpRequestSimple httpclent = new HttpRequestSimple();
        String resJson = httpclent.postSendHttp(InterfaceConst.SERVER + url, reqJson);
        BankCardPayBean bankCardMsg = JSON.parseObject(resJson,BankCardPayBean.class);
        return resJson;
    }

    /**
     * @what 校验银行卡卡号
     * @param cardId 银行卡id
     * @return 检验是否成功
     */
    protected boolean checkBankCard(String cardId)
    {
        if (cardId == null || cardId.trim().length() < 16)
        {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N')
        {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    protected char getBankCardCheckCode(String nonCheckCodeCardId)
    {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+"))
        {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++)
        {
            int k = chs[i] - '0';
            if (j % 2 == 0)
            {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
}
