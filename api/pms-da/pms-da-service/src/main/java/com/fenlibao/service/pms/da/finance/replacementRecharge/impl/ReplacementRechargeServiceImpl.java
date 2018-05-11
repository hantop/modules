package com.fenlibao.service.pms.da.finance.replacementRecharge.impl;

import com.fenlibao.common.pms.util.loader.XINWANG_PRO;
import com.fenlibao.dao.pms.da.finance.replacementRecharge.ReplacementRechargeMapper;
import com.fenlibao.model.pms.common.global.UserTypeEnum;
import com.fenlibao.model.pms.da.finance.ReplacementRecharge;
import com.fenlibao.model.pms.da.finance.form.ReplacementRechargeForm;
import com.fenlibao.model.pms.da.finance.vo.UserRechargeAuthVO;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.service.pms.da.finance.replacementRecharge.ReplacementRechargeService;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplacementRechargeServiceImpl implements ReplacementRechargeService {

    private static final Logger logger = LogManager.getLogger(ReplacementRechargeServiceImpl.class);

    @Autowired
    private ReplacementRechargeMapper replacementRechargeMapper;

    @Autowired
    private XWRechargeService xwRechargeService;

    @Autowired
    private XWUserInfoService xwUserInfoService;

    @Override
    public BigDecimal getReplacementRechargeAccountBalance() {
        XinwangUserInfo xinwangUserInfo = xwUserInfoService.queryUserInfo(XINWANG_PRO.get("replacementRechargeAccount"));
        if(xinwangUserInfo != null){
            return xinwangUserInfo.getAvailableAmount();
        }
        return null;
//        return replacementRechargeMapper.getReplacementRechargeAccountBalance();
    }

    @Override
    public List<ReplacementRecharge> getReplacementRechargeList(ReplacementRechargeForm replacementRechargeForm, RowBounds bounds) {
        List<ReplacementRecharge> replacementRechargeList = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        List<Map<String, Object>> userBaseMap = new ArrayList<>();
        replacementRechargeList = replacementRechargeMapper.getReplacementRechargeList(replacementRechargeForm, bounds);
        for (ReplacementRecharge re : replacementRechargeList) {
            int userId = re.getUserId();
            if(!userIds.contains(userId)){
                userIds.add(userId);
            }
        }
        if(userIds.size() > 0){
            userBaseMap = replacementRechargeMapper.getUserBaseInfo(userIds);
            for (ReplacementRecharge re : replacementRechargeList) {
                Map<String, Object> userBaseInfoMap = new HashMap<>();
                userBaseInfoMap = getUserBaseInfo(userBaseMap, re.getUserId());
                if(userBaseInfoMap != null){
                    re.setUserName(
                            (userBaseInfoMap.get("userName").toString() == null ? null : userBaseInfoMap.get("userName").toString()));
                }
            }
        }
        return replacementRechargeList;
    }

    private Map<String, Object> getUserBaseInfo(List<Map<String, Object>> userBaseMap, Integer userId){
        for (Map<String, Object> userBaseMapDetail : userBaseMap) {
            if (userId.toString().equals(userBaseMapDetail.get("userId").toString())) {
                return userBaseMapDetail;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserRechargeAuthVO(String userType, String account, String userRole) {
        UserRechargeAuthVO userRechargeAuthVO = null;
        String code = null;
        Map<String, Object> resultMap = new HashMap<>();
        //用户id
        Integer userId;
        if(account != null && userType != null && userRole != null){
            userId = replacementRechargeMapper.getUserIdByAccount(account);
            if (userId != null){
                String platformUserNo = userRole + userId;
                if(userType.equals(UserTypeEnum.PERSONAL.getValue())){
                    userRechargeAuthVO = replacementRechargeMapper.getUserRechargeAuthVO(userId, platformUserNo);
                }else{
                    userRechargeAuthVO = replacementRechargeMapper.getCompanyRechargeAuthVO(userId, platformUserNo);
                }
                if(userRechargeAuthVO != null){
                    // userRole:入口的参数
                    if(!userRechargeAuthVO.getUserRole().equals(userRole)){
                        code = "0010";// 用户角色不对应入口参数
                    }
                    if(userRechargeAuthVO.getBankcardNo() == null){
                        code = "0100";// 用户银行卡号为空,没有对应的账号
                    }
                }else{
                    code = "0010";// 没有相应新网角色
                }
            }else {
                code = "0000";// 查无本人
            }
        }
        resultMap.put("code", code);
        resultMap.put("userRechargeAuthVO", userRechargeAuthVO);
        return resultMap;
    }

    //获取特定角色用户
    private UserRechargeAuthVO getUserRechargeAuthVOByUserRole(List<UserRechargeAuthVO> userRechargeAuthVOList, String userRole){
        if(userRechargeAuthVOList.size() > 0){
            for (UserRechargeAuthVO u : userRechargeAuthVOList) {
                if(u.getUserRole().equals(userRole)){
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public int saveReplacementRecharge(ReplacementRecharge replacementRecharge) {
        if(replacementRecharge != null){
            replacementRecharge.setStatus(2);// 待审核状态
            return replacementRechargeMapper.saveReplacementRecharge(replacementRecharge);
        }
        return 0;
    }

    @Override
    public boolean checkAuditAndRechargeUser(int id, String operator) {
        if(id > 0){
            String rechargeUserName = replacementRechargeMapper.getRechargeUserName(id);
            if(rechargeUserName != null){
                if(!rechargeUserName.equals(operator)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String audit(int flag, ReplacementRecharge replacementRecharge) throws Exception{
        String code = null;
        if(replacementRecharge != null){
            if(getReplacementRechargeAccountBalance().compareTo(replacementRecharge.getRechargeMoney()) < 0){
                code = "2222";
                return code;
            }
            try{
                if(flag == 1){
                    replacementRecharge.setStatus(1);// 审核通过
                    //交易类型
                    BusinessType businessType = new BusinessType();
                    businessType.setCode(SysTradeFeeCode.CZ_XX);// 原线下充值交易类型
                    businessType.setName("代充值");
                    businessType.setStatus("QY");
                    int rechargeId = replacementRecharge.getId();
                    int userId = replacementRecharge.getUserId();
                    String userRole = replacementRecharge.getUserRole();
                    String plantFormNum = userRole + userId;
                    BigDecimal rechargeMoney = replacementRecharge.getRechargeMoney();
                    int xwRequestId;
                    xwRequestId = xwRechargeService.doAlternativeRecharge(userId, plantFormNum, rechargeMoney, businessType);
                    //查询当笔交易状态
                    if(xwRequestId > 0){
                        if(judgeReplacementRechargeState(rechargeId, xwRequestId)){
                            code = "success";// 审核通过充值成功
                        }else {
                            code = "fail";// 充值失败
                        }
                        replacementRecharge.setXwRequestId(xwRequestId);
                        //更新pms代充值记录表的状态
                        replacementRechargeMapper.updateReplacementRecharge(replacementRecharge);
                    }
                }else{
                    replacementRecharge.setStatus(0);// 审核不通过
                    replacementRechargeMapper.updateReplacementRecharge(replacementRecharge);
                    code = "0000";//审核不通过成功
                }
            } catch (XWTradeException e){
                logger.error("[调用存管代充值产生的异常:]" + e.getMessage(), e);
                throw e;
            }
        }
        return code;
    }

    private boolean judgeReplacementRechargeState(int rechargeId, int xwRequestId){
        String state = replacementRechargeMapper.getReplacementRechargeState(rechargeId, xwRequestId);
        if(state != null){
            if(state.equals("CG")){
                return true;
            }
        }
        return false;
    }
}
