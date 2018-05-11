package com.fenlibao.service.pms.da.cs.logInfo.impl;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.dao.pms.da.biz.loanmanage.LoanManageMapper;
import com.fenlibao.dao.pms.da.cs.account.UserDetailInfoMapper;
import com.fenlibao.dao.pms.da.cs.logInfo.LogInfoMapper;
import com.fenlibao.model.pms.common.global.LogInfoType;
import com.fenlibao.model.pms.da.biz.viewobject.BidVO;
import com.fenlibao.model.pms.da.cs.LogInfo;
import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.form.LogInfoForm;
import com.fenlibao.service.pms.da.cs.account.UserDetailInfoService;
import com.fenlibao.service.pms.da.cs.logInfo.LogInfoService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/12/12.
 */
@Service
public class LogInfoServiceImpl implements LogInfoService {
    @Resource
    private LogInfoMapper logInfoMapper;

    @Resource
    private UserDetailInfoService userDetailInfoService;
	
	@Resource
    private LoanManageMapper loanManageMapper;

    @Resource
    private UserDetailInfoMapper userDetailInfoMapper;

    @Override
    public List<LogInfo> getLogInfoList(LogInfoForm logInfoForm, RowBounds bounds) {
        List<Integer> userIds = new ArrayList<>();
        List<Integer> types = new ArrayList<>();// 日志分类
        // 根据电话/ 姓名/ 身份证查询用户id
        if((logInfoForm.getPhoneNum() != null && !logInfoForm.getPhoneNum().equals(""))
                || (logInfoForm.getIdCard() != null && !logInfoForm.getIdCard().equals(""))
                || (logInfoForm.getName() != null && !logInfoForm.getName().equals("")) ){
            List<UserDetail> userDetailList =  userDetailInfoService.getUserDetail(logInfoForm.getPhoneNum(), logInfoForm.getName(), logInfoForm.getIdCard(), bounds);
            if (userDetailList.size() == 0){
                return null;
            }else {
                for (UserDetail u: userDetailList) {
                    userIds.add(u.getUserId());
                }
            }
        }
        if (logInfoForm.getEndTime() != null) {
            Date endTime = logInfoForm.getEndTime();
            endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
            logInfoForm.setEndTime(endTime);
        }
        types = getLogInfoTypes(logInfoForm.getConductType());
        List<LogInfo> logInfoList = logInfoMapper.getLogInfoList(types, userIds, logInfoForm.getStartTime(), logInfoForm.getEndTime(), bounds);
        return logInfoList;
    }


    /**
     * 1: 登录/登出
     * 2: 充值/提现/投资/借款/债转申请/购买债权
     * 3: 绑定银行卡/解绑银行卡/修改登录密码
     */
    private List<Integer> getLogInfoTypes(int type){
        List<Integer> types = new ArrayList<>();
        if (type == 1){
            types.add(LogInfoType.LOGIN.getValue());
            types.add(LogInfoType.LOGOUT.getValue());
        }else if (type == 2){
            types.add(LogInfoType.RECHARGE.getValue());
            types.add(LogInfoType.WITHDRAW.getValue());
            types.add(LogInfoType.INVEST.getValue());
            types.add(LogInfoType.CREDITIN.getValue());
            types.add(LogInfoType.CREDITOUT.getValue());
            types.add(LogInfoType.LOAN.getValue());
        }else if (type == 3){
            types.add(LogInfoType.BIND.getValue());
            types.add(LogInfoType.UNBIND.getValue());
            types.add(LogInfoType.MODIFYPASSWORD.getValue());
        }
        return types;
    }

    @Override
    public void addUserLog(int loanId,int status) {
        LogInfo logInfo = new LogInfo();
        BidVO entrustPayBid = loanManageMapper.getBidInfoByLoanId(loanId);
        if(entrustPayBid!=null){
          UserDetailInfo userDetailInfo = userDetailInfoMapper.getLoanUserInfo(String.valueOf(entrustPayBid.getUserId()));
            logInfo.setConduct(5);
            logInfo.setIdcard(userDetailInfo.getIdcard());
            logInfo.setName(userDetailInfo.getName());
            logInfo.setPhoneNum(userDetailInfo.getPhoneNum());
            logInfo.setPrice(new BigDecimal(entrustPayBid.getBidAmount()));
            logInfo.setUserId(userDetailInfo.getUserId());
            logInfo.setRemarks(userDetailInfo.getBankcardNum());
            logInfo.setStatus(status);
            Map<String,String[]> map = new HashMap<>();
            map.put("amount",new String[]{entrustPayBid.getBidAmount()});
            if(map!=null){
                JSONObject json =(JSONObject) JSONObject.toJSON(map);
                logInfo.setRequestStr(json.toString());
            }
            this.logInfoMapper.addUserLog(logInfo);
        }

    }
}
