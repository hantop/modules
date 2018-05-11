package com.fenlibao.service.pms.da.finance.withdrawLimit.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.dao.pms.da.finance.withdrawLimit.WithdrawLimitMapper;
import com.fenlibao.model.pms.da.finance.WithdrawLimit;
import com.fenlibao.model.pms.da.finance.form.WithdrawLimitForm;
import com.fenlibao.service.pms.da.finance.withdrawLimit.WithdrawLimitService;

@Service
public class WithdrawLimitServiceImpl implements WithdrawLimitService {

    @Autowired
    private WithdrawLimitMapper withdrawLimitMapper;

	@Override
	public List<WithdrawLimit> getWithdrawLimitList(WithdrawLimitForm withdrawLimitForm, RowBounds bounds) {

        List<WithdrawLimit> withdrawLimits = null;
        if(!StringUtils.isEmpty(withdrawLimitForm)){
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("phoneNum", withdrawLimitForm.getPhoneNum());
            paramMap.put("operator", withdrawLimitForm.getOperator());

            String beginDate = withdrawLimitForm.getStartTime();
            String endDate = withdrawLimitForm.getEndTime();
            Date startTime;
            Date endTime;
            // 默认昨天到今天
            if(!StringUtils.isEmpty(beginDate)){
            	startTime = DateUtil.StringToDate(beginDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("startTime",startTime);
            }else {
                Date yesterdayDate = DateUtil.dateAdd(DateUtil.nowDate(), -1);
                String yesterdayDateStr = DateUtil.getDate(yesterdayDate);
                startTime = DateUtil.StringToDate(yesterdayDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("startTime",startTime);
                withdrawLimitForm.setStartTime(yesterdayDateStr);
            }
            if(!StringUtils.isEmpty(endDate)){
            	endTime = DateUtil.StringToDate(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("endTime",endTime);
            }else {
                String yesterdayDateStr = DateUtil.getDate(DateUtil.nowDate());
                endTime = DateUtil.StringToDate(yesterdayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("endTime",endTime);
                withdrawLimitForm.setEndTime(yesterdayDateStr);
            }
            //获取用户提现金额限制记录
            withdrawLimits = withdrawLimitMapper.getWithdrawLimitList(paramMap, bounds);
        }

        return withdrawLimits;
    
	}

	@Override
	public int insertIntoUserWithdrawLimit(int userId, BigDecimal limitMoney, String remark) {
		   // 当前用户名
        String operator = (String) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        paramMap.put("limitMoney",limitMoney);
        paramMap.put("remark",remark);
        paramMap.put("operator",operator);

        return withdrawLimitMapper.insertIntoUserWithdrawLimit(paramMap);
	}
}
