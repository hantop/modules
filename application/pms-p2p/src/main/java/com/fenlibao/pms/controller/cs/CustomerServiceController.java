/*
 * Copyright (c) 2015 by FENLIBAO NETWORK TECHNOLOGY CO.
 *             All rights reserved
 */
package com.fenlibao.pms.controller.cs;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.model.pms.da.cs.OrgAuthInfo;
import com.fenlibao.model.pms.da.cs.UnbindBankcardInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import com.fenlibao.model.pms.da.cs.UserBankcard;
import com.fenlibao.model.pms.da.cs.form.UnbindBankcardForm;
import com.fenlibao.model.pms.da.cs.form.UnbindBankcardSearchForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooUnBindCardService;
import com.fenlibao.service.pms.da.cs.UnbindBankcardService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author <a href="mailto:toby.xiong2@qq.com">Toby</a>
 * @Data 2015年11月30日
 * @Version 1.0.0
 */

@RestController
@RequestMapping("cs")
public class CustomerServiceController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceController.class);
    @Resource
    private UnbindBankcardService unbindBankcardService;
    @Resource
    private BaofooUnBindCardService baofooUnBindCardService;

    /**
     * 介绍页
     * @return
     */
    @RequestMapping("index")
    public ModelAndView csIndex() {
        ModelAndView view = new ModelAndView("cs/index");
        return view;
    }

    @RequestMapping(value = "export", method = {RequestMethod.GET, RequestMethod.POST})
    public void export(HttpServletResponse response,
                       UnbindBankcardSearchForm unbindBankcardSearchForm) {
        RowBounds bounds = new RowBounds();
        Date unbindStartDate = null;
        Date unbindEndDate = null;
        String userAccount = unbindBankcardSearchForm.getUserAccount();
        String operator = unbindBankcardSearchForm.getOperator();
        String unbindStartTimeStr = unbindBankcardSearchForm.getUnbindStartTime();
        String unbindEndTimeStr = unbindBankcardSearchForm.getUnbindEndTime();
        if(!StringUtils.isEmpty(unbindStartTimeStr)){
            unbindStartDate = DateUtil.StringToDate(unbindStartTimeStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(!StringUtils.isEmpty(unbindEndTimeStr)){
            unbindEndDate = DateUtil.StringToDate(unbindEndTimeStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        List<UnbindBankcardInfo> unbindBankcardInfos = unbindBankcardService.getUnbindBankcardInfos(userAccount, operator, unbindStartDate, unbindEndDate, bounds);
        List<UnbindBankcardInfoExportObject> exportList = new ArrayList<>();
        UnbindBankcardInfoExportObject exportObject = null;
        for(UnbindBankcardInfo o : unbindBankcardInfos) {
            exportObject = new UnbindBankcardInfoExportObject();
            exportObject.userId = o.getUserId();
            exportObject.userAccount = o.getUserAccount();
            exportObject.bankcardNo = o.getBankcardNo();
            exportObject.operator = o.getOperator();
            exportObject.description = o.getDescription();
            exportObject.unbindTime = DateUtil.getDateTime(o.getUnbindTime());
            exportList.add(exportObject);
        }
        String headers[] = {"解绑日期", "操作人", "解绑用户账号", "解绑银行卡"};
        String fieldNames[] = {"unbindTime", "operator", "userAccount", "bankcardNo"};
        POIUtil.export(response, headers, fieldNames, exportList);
    }

    @RequestMapping(value = "unbindBankCard", method = {RequestMethod.POST})
    public String unbindBankCard(String userId, String userAccount, String userRole, String baofooBindId, String bankNum) {
        String resultCode = "0000";
        // 当前用户名
        String operator = (String) SecurityUtils.getSubject().getPrincipal();
        try {
            if(userRole != null && userRole != "0"){
                //银行存管审核
                unbindBankcardService.auditUnbindBankcard(userAccount, Integer.valueOf(userId), bankNum, operator, 1, userRole);
            }else{
                //宝付解绑
               if(baofooBindId != null){
                   baofooUnBindCardService.unBindCard(Integer.valueOf(userId));
                   // 解绑银行卡记录
                   unbindBankcardService.unbindBankcard(userAccount, Integer.valueOf(userId), bankNum, operator, 1);
               } else{
                   resultCode = "2000";//宝付协议号为空
               }
            }
        } catch (Exception e) {
            if (e instanceof UserException){
                resultCode = ((UserException) e).getCode();//111113
                logger.error("[用户不存在]", e);
            }else if (e instanceof TradeException){
                resultCode = ((TradeException) e).getCode();
                if(resultCode.equals("250130")){
                    logger.error("[用户没有绑卡：]", e.getLocalizedMessage());
                }else if(resultCode.equals("250131")){
                    logger.error("[解绑失败：]", e.getLocalizedMessage());
                }
            }else{
                resultCode = "1000";
                logger.error("[审核/解绑银行卡异常：]", e);
            }
        }
        return resultCode;
    }

    @RequestMapping(value = "userUnbindBankCards", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView userUnbindBankCards(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            UnbindBankcardForm unbindBankcardForm) {
        ModelAndView view = new ModelAndView("cs/userUnbindBankCards");
        RowBounds bounds = new RowBounds(page, limit);
        // 查询用户/企业实名认证信息(存管/普通)
        Integer userId;
        List<UserAuthInfo> userAuthInfo = new ArrayList<>();
        List<OrgAuthInfo> orgAuthInfo = new ArrayList<>();
        String msg = null;
        try{
            String uid = StringHelper.encode(unbindBankcardForm.getUid());
            if(unbindBankcardForm.getUserType() != null && unbindBankcardForm.getUserAccount() != null && unbindBankcardForm.getUid() != null){
                if(unbindBankcardForm.getUserType().equals("PERSONAL")){
                    unbindBankcardForm.setUid(uid);
                    userAuthInfo = unbindBankcardService.getUserAuthInfo(unbindBankcardForm);
                    if(userAuthInfo.size() > 0){
                        userId = userAuthInfo.get(0).getUserId();
                        unbindBankcardForm.setUserName(userAuthInfo.get(0).getUserName());
                        unbindBankcardForm.setUid(StringHelper.decode(unbindBankcardForm.getUid()));
                    }else{
                        unbindBankcardForm.setUid(StringHelper.decode(unbindBankcardForm.getUid()));
                        msg = "账号/唯一识别码有误";
                        return new ModelAndView("cs/unbindBankCards").addObject("msg", msg).addObject("unbindBankcardForm", unbindBankcardForm);
                    }
                }else{
                    orgAuthInfo = unbindBankcardService.getOrgAuthInfo(unbindBankcardForm);
                    if(orgAuthInfo.size() > 0){
                        userId = orgAuthInfo.get(0).getUserId();
                        unbindBankcardForm.setUserName(orgAuthInfo.get(0).getUserName());
                    }else{
                        msg = "账号/唯一识别码有误";
                        return new ModelAndView("cs/unbindBankCards").addObject("msg", msg).addObject("unbindBankcardForm", unbindBankcardForm);
                    }
                }
            }else{
                return new ModelAndView("cs/unbindBankCards");
            }
            List<UserBankcard> userBankcardInfos = unbindBankcardService.getUserBankCard(userId, bounds);
            PageInfo<UserBankcard> paginator = new PageInfo<>(userBankcardInfos);
            view.addObject("list", userBankcardInfos);
            view.addObject("unbindBankcardForm", unbindBankcardForm);
            view.addObject("paginator", paginator);
        }catch (Throwable e) {
            logger.error("[解绑银行卡->检查用户账号/唯一识别码异常：]", e);
        }
        return view;
    }

    /*@RequestMapping(value = "checkPhoneNumIdcard", method = RequestMethod.POST)
    public Map<String, Object> checkPhoneNumIdcard(UnbindBankcardForm unbindBankcardForm) {
        Map<String, Object> resultMap = new HashMap<>();
        String resultCode = "0000";
        String phoneNum = unbindBankcardForm.getPhoneNum();
        String idcard = unbindBankcardForm.getIdcard();
        Integer userId;
        String bankNum;
        try {
            // 加密身份证号
            String idCardEncrypt = StringHelper.encode(idcard);
            // 查询用户实名认证信息
            UserAuthInfo userAuthInfo = unbindBankcardService.getUserAuthInfo(phoneNum, idCardEncrypt);
            if(userAuthInfo == null) {
                resultCode = "2000";
            } else {
                userAuthInfo.setIdcard(idcard);
                // 查询用户银行卡
                userId = userAuthInfo.getUserId();
                // 根据用户ID获取用户银行卡
                UserBankcard userBankCard = unbindBankcardService.getUserBankCard(userId);
                if(userBankCard != null){
                    //宝付协议号为空的时候没有绑定宝付
                    if(userBankCard.getBaofooBindId() == null || userBankCard.getBaofooBindId().equals("")) {
                        resultCode = "3000";
                    } else {
                        bankNum = userBankCard.getBankNum();
                        resultMap.put("userId", userId);
                        resultMap.put("phoneNum", phoneNum);
                        resultMap.put("idcard", idcard);
                        resultMap.put("bankcardNum", bankNum);
                    }
                }else{
                    resultCode = "1000";
                }
            }
        } catch (Throwable e) {
            resultCode = "1000";
            logger.error("[解绑银行卡->检查手机号身份证异常：]", e);
        }
        resultMap.put("resultCode", resultCode);
        return resultMap;
    }*/

    @RequestMapping(value = "unbindBankCards", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView unbindBankCards(
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
            UnbindBankcardSearchForm unbindBankcardSearchForm) {
        ModelAndView view = new ModelAndView("cs/unbindBankCards");
        RowBounds bounds = new RowBounds(page, limit);
        Date unbindStartDate;
        Date unbindEndDate;
        String userAccount = unbindBankcardSearchForm.getUserAccount();
        String operator = unbindBankcardSearchForm.getOperator();
        String unbindStartTimeStr = unbindBankcardSearchForm.getUnbindStartTime();
        String unbindEndTimeStr = unbindBankcardSearchForm.getUnbindEndTime();
        if(StringUtils.isEmpty(unbindStartTimeStr)){
            String todayDateStr = DateUtil.getDate(DateUtil.nowDate());
            unbindBankcardSearchForm.setUnbindStartTime(todayDateStr);
            unbindStartDate = DateUtil.StringToDate(todayDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } else {
            unbindStartDate = DateUtil.StringToDate(unbindStartTimeStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(StringUtils.isEmpty(unbindEndTimeStr)){
            String todayDateStr = DateUtil.getDate(DateUtil.nowDate());
            unbindBankcardSearchForm.setUnbindEndTime(todayDateStr);
            unbindEndDate = DateUtil.StringToDate(todayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else {
            unbindEndDate = DateUtil.StringToDate(unbindEndTimeStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        List<UnbindBankcardInfo> unbindBankcardInfos = unbindBankcardService.getUnbindBankcardInfos(userAccount, operator, unbindStartDate, unbindEndDate, bounds);
        PageInfo<UnbindBankcardInfo> paginator = new PageInfo<>(unbindBankcardInfos);
        view.addObject("list", unbindBankcardInfos);
        view.addObject("unbindBankcardSearchForm", unbindBankcardSearchForm);
        view.addObject("paginator", paginator);
        return view;
    }

    private class UnbindBankcardInfoExportObject {
        public Integer userId;// 用户ID
        public String userAccount;// 用户账号
        public String bankcardNo;// 银行卡号
        public String operator;// 解绑人
        public String description;// 备注
        public String unbindTime;// 解绑时间
    }
}
