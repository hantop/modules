package com.fenlibao.service.pms.da.cs.impl;

import com.fenlibao.common.pms.util.loader.Message;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.dao.pms.da.cs.UnbindBankcardMapper;
import com.fenlibao.model.pms.da.cs.OrgAuthInfo;
import com.fenlibao.model.pms.da.cs.UnbindBankcardInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import com.fenlibao.model.pms.da.cs.UserBankcard;
import com.fenlibao.model.pms.da.cs.form.UnbindBankcardForm;
import com.fenlibao.service.pms.common.message.privatemessage.PrivateMessageService;
import com.fenlibao.service.pms.common.message.sms.SmsService;
import com.fenlibao.service.pms.da.cs.UnbindBankcardService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 解绑银行卡
 * Created by chenzhixuan on 2015/12/4.
 */
@Service
public class UnbindBankcardServiceImpl implements UnbindBankcardService {
    @Resource
    private UnbindBankcardMapper unbindBankcardMapper;
    @Resource
    private PrivateMessageService privateMessageService;
    @Resource
    private SmsService smsService;

    @Transactional
    @Override
    public void unbindBankcard(String userAccount, Integer userId, String bankcardNum, String operator, int unbindStatus) {
        // 修改用户银行卡认证状态
//        unbindBankcardMapper.updateUserBankcardStatus(userId);
        // 删除连连支付协议号
        //unbindBankcardMapper.deletePayExtend(userId);
        // 将连连支付协议号清空（目前没有分通道解绑，暂时解绑的时候将两个通道的都一起解绑，以后要分通道）
//        unbindBankcardMapper.setPayExtendNull(userId);
        // 新增解绑银行卡记录
        addUnbindBankcardRecord(userAccount, userId, bankcardNum, operator, unbindStatus);
        String phoneNum = unbindBankcardMapper.getPhoneNumByUserId(userId);
        // 发送站内信和短信
        _sendMessage(null, phoneNum, userId);
    }

    @Transactional
    @Override
    public void auditUnbindBankcard(String userAccount, Integer userId, String bankcardNum, String operator, int auditStatus, String userRole) {
        Date expiryTime = new Date();
        expiryTime = new Date(expiryTime.getTime() + 3 * 24 * 60 * 60 * 1000);
        // 新增解绑银行卡记录
        int unbindBankcardId = addUnbindBankcardRecord(userAccount, userId, bankcardNum, operator, 0);
        unbindBankcardMapper.addUnbindBankcardAuditRecord(unbindBankcardId, userId, userRole, auditStatus, expiryTime);
        String phoneNum = unbindBankcardMapper.getPhoneNumByUserId(userId);
        // 发送站内信和短信
        _sendMessage(userRole, phoneNum, userId);
    }

    @Override
    public void addUnbindBankcardAuditRecord(Integer auditId, Integer userId, String userRole, int auditStatus, Date expiryTime) {
        unbindBankcardMapper.addUnbindBankcardAuditRecord(auditId, userId, userRole, auditStatus, expiryTime);
    }

    private void _sendMessage(String userRole, String phoneNum, Integer userId) {
        StringBuilder smsContent = null;
        String privateMessageContent = null;
        if(userRole != null){//'INVESTOR','BORROWERS','GUARANTEECORP'
            if(userRole.equals("INVESTOR")){
                userRole = "投资账号";
            }else if(userRole.equals("BORROWERS")){
                userRole = "借款账号";
            }else if(userRole.equals("GUARANTEECORP")){
                userRole = "担保账号";
            }
            // 审核解绑银行卡模板
            smsContent = new StringBuilder(Message.get("sms.auditUnbindcard"));
//            smsContent.replace(smsContent.indexOf("{userRole}"), smsContent.indexOf("{userRole}") + "{userRole}".length(), "" + userRole);
        }else{
            // 短信模板
            smsContent = new StringBuilder(Message.get("sms.unbindcard"));
            // 站内信模板
            privateMessageContent = Message.get("privatemessage.unbindcard");
            // 发送站内信
            privateMessageService.sendLetter(String.valueOf(userId), "解绑银行卡成功", privateMessageContent);
        }
        // 发送短信
        smsService.sendMsg(phoneNum, smsContent.toString());
    }

    @Override
    public int addUnbindBankcardRecord(String userAccount, Integer userId, String bankcardNum, String operator, int unbindStatus) {
        UnbindBankcardInfo unbindBankcardInfo = new UnbindBankcardInfo();
        unbindBankcardInfo.setUserId(userId);
        unbindBankcardInfo.setUserAccount(userAccount);
        unbindBankcardInfo.setBankcardNo(bankcardNum);
        unbindBankcardInfo.setOperator(operator);
        unbindBankcardInfo.setUnbindTime(DateUtil.nowDate());
        unbindBankcardInfo.setUnbindStatus(unbindStatus);
        unbindBankcardMapper.addUnbindBankCard(unbindBankcardInfo);
       return unbindBankcardInfo.getId();
    }

    @Override
    public List<UserBankcard> getUserBankCard(Integer userId, RowBounds rowBounds) {
        List<UserBankcard> userBankcards = unbindBankcardMapper.getUserBankCard(userId, rowBounds);
        List<Integer> updateAuditStatusIds = new ArrayList<>();
        Date now = new Date();
        if(userBankcards.size() > 0){
            for (UserBankcard userBankcard : userBankcards) {
                // 只关联最近一次的审核记录
                if(userBankcard.getAuditStatus() != null && userBankcard.getUnbindStatus() != null && userBankcard.getExpiryTime() != null){
                    if(userBankcard.getAuditStatus().equals("1")){//已审核
                        if(userBankcard.getUnbindStatus().equals("1")){//已解绑
                            userBankcard.setAudit(true);
                        }else{
                            if(now.compareTo(userBankcard.getExpiryTime()) > 0){// 截止时间已到
                                userBankcard.setAudit(true);
                            }
                        }
                    }
                }else {
                    userBankcard.setAudit(true);
                }
                userBankcard.setBankNum(getNoiseBankNum(userBankcard.getBankNum()));
            }
        }
        return userBankcards;
    }

    private String getNoiseBankNum(String bankNum) {
        String noiseBankNum = null;
        if(bankNum != null && bankNum.length() > 4){
            String front = bankNum.substring(0,3);
            String back = bankNum.substring(bankNum.length() - 4, bankNum.length());
            String replaceMent = "*************";
            noiseBankNum = front + replaceMent + back;
            return noiseBankNum;
        }else{
            return null;
        }
    }

    @Override
    public List<UserAuthInfo> getUserAuthInfo(UnbindBankcardForm unbindBankcardForm) {
        return unbindBankcardMapper.getUserAuthInfo(unbindBankcardForm.getUserAccount(), unbindBankcardForm.getUid());
    }

    @Override
    public List<OrgAuthInfo> getOrgAuthInfo(UnbindBankcardForm unbindBankcardForm) {
        return unbindBankcardMapper.getOrgAuthInfo(unbindBankcardForm.getUserAccount(), unbindBankcardForm.getUid());
    }

    @Override
    public List<UnbindBankcardInfo> getUnbindBankcardInfos(String userAccount, String operator, Date unbindStartDate, Date unbindEndDate, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userAccount", userAccount);
        paramMap.put("operator", operator);
        paramMap.put("unbindStartTime", unbindStartDate);
        paramMap.put("unbindEndTime", unbindEndDate);
        return unbindBankcardMapper.getUnbindBankcardInfos(paramMap, bounds);
    }

}