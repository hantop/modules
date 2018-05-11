package com.fenlibao.p2p.schedule.task.sign;

import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.p2p.model.vo.entrust.EntrustAgreementVo;
import com.fenlibao.p2p.schedule.task.sign.BestSignJob;
import com.fenlibao.p2p.service.thirdparty.EntrustAccountService;
import com.fenlibao.p2p.service.thirdparty.ShangshangqianService;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/7/31.
 * 委托开户协议签名
 */

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class EntrustAccountJob extends QuartzJobBean{

    private static final Logger LOGGER = LogManager.getLogger(BestSignJob.class);

    @Autowired
    EntrustAccountService entrustAccountService;

    @Resource
    ShangshangqianService shangshangqianService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("[委托开户协议书][上上签]开始签名");
        List<EntrustAgreementVo> agreementVos = entrustAccountService.getUnSignAgreementList();
        if(agreementVos != null && agreementVos.size() > 0){
            for(EntrustAgreementVo agreementVo : agreementVos){
                LOGGER.info("上上签委托开户协议书开始签,t_business_agreement.id:=" + agreementVo.getId());
                //更新记录
                agreementVo = entrustAccountService.lockAgreement(agreementVo.getId());
                if(agreementVo == null) {
                    continue;
                }
                try {
                    //分利宝签名
                    autoSignbyCA_flb(agreementVo, true);
                    //更新记录
                    entrustAccountService.updateUnSignAgreementById(agreementVo.getId(), AgreementSignStatusEnum.YQM);
                } catch (Exception e) {
                    LOGGER.info(String.format("上上签委托开户协议书签名异常(%s)", e.toString()));
                    //更新记录
                    entrustAccountService.updateUnSignAgreementById(agreementVo.getId(), AgreementSignStatusEnum.QMSB);
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info(String.format("[上上签]委托开户协议书签名结束(记录数：%s)",agreementVos.size()));
    }

    //签名分利宝
    public void autoSignbyCA_flb(EntrustAgreementVo agreementVo, boolean openflag)throws Exception{
        String email =null;
        String username=null;
        String phone=null;
        if(Boolean.parseBoolean(Config.get("ssq.develop.mode"))){
            email=Config.get("ssq.sign.email_test");
            username=Config.get("ssq.sign.name");
            phone=Config.get("ssq.sign.phone_test");
        }else{
            email=Config.get("ssq.sign.email");
            username=Config.get("ssq.sign.name");
            phone=Config.get("ssq.sign.phone");
        }
        String signid=agreementVo.getSignId();
        Map signmap = new HashMap();
        signmap.put("signid",signid);
        signmap.put("phone",email);//企业账号用的是邮箱
        signmap.put("pagenum", agreementVo.getFlbPageNum());
        signmap.put("signx", agreementVo.getFlbSignX());
        signmap.put("signy", agreementVo.getFlbSignY());
        signmap.put("openflag",openflag);
        try{
            ReceiveUser user = new ReceiveUser(email, username,
                    phone, Constants.USER_TYPE.ENTERPRISE, Constants.CONTRACT_NEEDVIDEO.NONE, true);
            ReceiveUser[] userList = {user};
            Map sjdsendcontractMap = new HashMap();
            sjdsendcontractMap.put("userlist",userList);
            sjdsendcontractMap.put("signid",signid);
            LOGGER.info("[上上签],开始委托开户添加分利宝签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
            shangshangqianService.sjdsendcontract(sjdsendcontractMap);
            LOGGER.info("[上上签],开始委托开户分利宝签名：signmap="+ JSON.toJSONString(signmap));
            shangshangqianService.AutoSignbyCA(signmap);
        }catch (Exception e){
            //记录
            signmap.put("uid",0);
            signmap.put("phone",0);//flb的phone改为0
            signmap.put("recordId",agreementVo.getId());
            shangshangqianService.recordError(signmap);
            throw e;
        }
    }
}
