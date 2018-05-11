package com.fenlibao.service.pms.da.cs.impl;

import com.fenlibao.common.pms.util.loader.Message;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.dao.pms.da.cs.ReplacePhoneMapper;
import com.fenlibao.model.pms.da.cs.ReplacePhoneInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import com.fenlibao.model.pms.da.cs.form.ReplacePhoneForm;
import com.fenlibao.service.pms.common.message.privatemessage.PrivateMessageService;
import com.fenlibao.service.pms.common.message.sms.SmsService;
import com.fenlibao.service.pms.da.cs.ReplacePhoneService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/12/23.
 */
@Service
public class ReplacePhoneServiceImpl implements ReplacePhoneService{

    @Resource
    private ReplacePhoneMapper replacePhoneMapper;
    @Resource
    private PrivateMessageService privateMessageService;
    @Resource
    private SmsService smsService;

    @Override
    public List<ReplacePhoneInfo> getReplacePhoneList(ReplacePhoneForm replacePhoneForm, RowBounds bounds) {
        List<ReplacePhoneInfo> replacePhoneInfos = null;
        if(!StringUtils.isEmpty(replacePhoneForm)){
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("phoneNum", replacePhoneForm.getPhoneNum());
            paramMap.put("operator", replacePhoneForm.getOperator());

            String beginDate = replacePhoneForm.getUnbindStartTime();
            String endDate = replacePhoneForm.getUnbindEndTime();
            Date unbindStartDate;
            Date unbindEndDate;
            // 默认昨天到今天
            if(!StringUtils.isEmpty(beginDate)){
                unbindStartDate = DateUtil.StringToDate(beginDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("unbindStartTime",unbindStartDate);
            }else {
                Date yesterdayDate = DateUtil.dateAdd(DateUtil.nowDate(), -1);
                String yesterdayDateStr = DateUtil.getDate(yesterdayDate);
                unbindStartDate = DateUtil.StringToDate(yesterdayDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("unbindStartTime",unbindStartDate);
                replacePhoneForm.setUnbindStartTime(yesterdayDateStr);
            }
            if(!StringUtils.isEmpty(endDate)){
                unbindEndDate = DateUtil.StringToDate(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("unbindEndTime",unbindEndDate);
            }else {
            //    Date yesterdayDate = DateUtil.dateAdd(DateUtil.nowDate(), -1);
                String yesterdayDateStr = DateUtil.getDate(DateUtil.nowDate());
                unbindEndDate = DateUtil.StringToDate(yesterdayDateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("unbindEndTime",unbindEndDate);
                replacePhoneForm.setUnbindEndTime(yesterdayDateStr);
            }

            //获取更换手机号码记录
            replacePhoneInfos = replacePhoneMapper.getReplacePhoneList(paramMap, bounds);
        }

        return replacePhoneInfos;
    }

    @Override
    public UserAuthInfo getUserInfoByPhone(Map<String,Object> param) {
        return replacePhoneMapper.getUserInfoByPhone(param);
    }

    @Override
    public UserAuthInfo getUserAuthByPhone(String phone) {
        return replacePhoneMapper.getUserAuthByPhone(phone);
    }

    /**
     * 更换手机号码 整体事务 一个失败则全部回滚
     * 用户表(t6101),资金账号(t6101),用户推广信息(t6111),历史站内信(t6123)，发放工资用户信息(t6193),电信流量(flb.t_flow)
     * @param userId
     * @param rePhone
     * @param userInfo
     */
    @Transactional
    @Override
    public void replacePhoneLogic(Integer userId, String rePhone, String oldPhone, UserAuthInfo userInfo) {
        // TODO: 2016/7/6 如果注册但是没有实名的账号 先取消掉
        if(userInfo != null){
            Map<String, Object> param = new HashMap<>();

            String returnStr = null;
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            returnStr = f.format(date);

            param.put("userId",userInfo.getUserId());
            param.put("account",userInfo.getPhoneNum() + "#" + returnStr);
            param.put("phone",userInfo.getPhoneNum() + "#" + returnStr);
            param.put("status","SD");
            replacePhoneMapper.updateRegUserCancel(param);
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        paramMap.put("rePhone",rePhone);
        paramMap.put("oldPhone",oldPhone);
        //1.更新用户表(t6110)
        replacePhoneMapper.updateUserPhone(paramMap);
        //2.资金账号(t6101)
        replacePhoneMapper.updateUserFundPhone(paramMap);
        //3.用户推广信息(t6111)
        replacePhoneMapper.updateUserByRankingPhone(paramMap);  //被别人邀请
        replacePhoneMapper.updateUserRankingPhone(paramMap);    //邀请的人
        //5.发放工资用户信息(t6193)
        replacePhoneMapper.updateUserSalaryPhone(paramMap);
        //6.发放工资用户明细信息(t6195)
        replacePhoneMapper.updateUserSalaryDetailPhone(paramMap);

        //插入更换手机号码操作记录
        saveReplacePhone(userId,rePhone,oldPhone);

        // 短信模板
        StringBuilder smsContent = new StringBuilder(Message.get("sms.replacePhone"));
        // 站内信模板
        StringBuilder privateContent = new StringBuilder(Message.get("privatemessage.replacePhone"));
        // 发送站内信
        privateContent.replace(privateContent.indexOf("{newphone}"), privateContent.indexOf("{newphone}") + "{newphone}".length(), "" + rePhone);
        privateMessageService.sendLetter(String.valueOf(userId), "更换手机号码成功", privateContent.toString());

        // 发送短信
        smsContent.replace(smsContent.indexOf("{newphone}"), smsContent.indexOf("{newphone}") + "{newphone}".length(), "" + rePhone);
        smsService.sendMsg(rePhone, smsContent.toString());
    }

    @Override
    public void saveReplacePhone(int userId, String rePhone, String oldPhone) {
        // 当前用户名
        String operator = (String) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        paramMap.put("rePhone",rePhone);
        paramMap.put("oldPhone",oldPhone);
        paramMap.put("operator",operator);

        replacePhoneMapper.saveReplacePhone(paramMap);
    }
}
