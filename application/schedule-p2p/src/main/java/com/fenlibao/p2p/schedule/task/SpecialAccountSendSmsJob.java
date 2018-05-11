package com.fenlibao.p2p.schedule.task;


import com.fenlibao.p2p.model.entity.SpecialAccount;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.service.SpecialAccountService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * j8tao 2018/01/10
 */

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SpecialAccountSendSmsJob extends QuartzJobBean {

    private static final Logger LOGGER = LogManager.getLogger(SpecialAccountSendSmsJob.class);

    @Autowired
    private SpecialAccountService specialAccountService;

    @Autowired
    private UserInfoService userInfoService;







    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        List<SpecialAccount> specialAccountList = this.specialAccountService.getSpecialPhone("5");
        if(specialAccountList!=null&&specialAccountList.size()>0){
            for(SpecialAccount specialAccount : specialAccountList){
                //获取新网往来账户
                UserAccount xwwlzhBalance = userInfoService.getUserAccount(String.valueOf(specialAccount.getUserId()), "XW_INVESTOR_WLZH");
                int smsType = 0;//发送短信类型 0：不发送 1：发送500000元短信  2：发送1000000元短信
                String content = null;//短信模板
                if(xwwlzhBalance!=null){
                   if(xwwlzhBalance.getBalance().compareTo(new BigDecimal(100000))<0){
                       smsType = 1;
                       content = Sender.get("sms.special.account.warning").replace("#{time}",DateUtil.getYYYY_MM_DD_HH_MM_SS(new Date())).replace("#{phone}",specialAccount.getPhoneNum())
                                 .replace("#{name}",specialAccount.getName()).replace("#{amount}","100,000");
                   }/*else if(xwwlzhBalance.getBalance().compareTo(new BigDecimal(1000000))<0&&xwwlzhBalance.getBalance().compareTo(new BigDecimal(500000))>=0){
                       smsType = 2;
                       content = Sender.get("sms.special.account.warning").replace("#{time}",DateUtil.getYYYY_MM_DD_HH_MM_SS(new Date())).replace("#{phone}",specialAccount.getPhoneNum())
                               .replace("#{name}",specialAccount.getName()).replace("#{amount}","1,000,000");
                   }*/
               }
                if(smsType!=0){//准备发送短信
                    SpecialAccount innerSms = new SpecialAccount();
                    //当天第一次校验
                    if(specialAccount.getCheckTime()==null||(specialAccount.getCheckTime()!=null&&!DateUtil.isToday(specialAccount.getCheckTime()))){
                        String phonenumStrings = Sender.get("sms.special.account.receiver");
                        String[] phone = phonenumStrings.split(",");
                        for (String s: phone) {
                            //发短信
                            try {
                                this.specialAccountService.sendMessage(s, content);
                            }catch (Exception e){
                                LOGGER.error("发送超级账户余额预警短信失败", e);
                            }
                        }

                        innerSms.setSmsCount(1);
                        innerSms.setBalance(xwwlzhBalance.getBalance());
                        innerSms.setPhoneNum(specialAccount.getPhoneNum());
                        this.specialAccountService.updateSpecialAccountSendCount(innerSms);
                    }
                    //当天的非首次校验，且短信发送次数少于3
                    else if(DateUtil.isToday(specialAccount.getCheckTime())&&specialAccount.getSmsCount()<3){
                        String phonenumStrings = Sender.get("sms.special.account.receiver");
                        String[] phone = phonenumStrings.split(",");
                        for (String s: phone) {
                            //发短信
                            try {
                                this.specialAccountService.sendMessage(s, content);
                            }catch (Exception e){
                                LOGGER.error("发送超级账户余额预警短信失败", e);
                            }
                        }
                        innerSms.setSmsCount(specialAccount.getSmsCount()+1);
                        innerSms.setBalance(xwwlzhBalance.getBalance());
                        innerSms.setCheckTime(new Date());
                        innerSms.setPhoneNum(specialAccount.getPhoneNum());
                        this.specialAccountService.updateSpecialAccountSendCount(innerSms);
                    }

                }


            }
        }

    }
}
