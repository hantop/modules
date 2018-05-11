package com.fenlibao.p2p.schedule.task.sign;

import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.p2p.model.vo.entrust.EntrustAgreementVo;
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
 * Created by zeronx on 2017/8/2.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EntrustAccountPreparednessJob extends QuartzJobBean{

    private static final Logger LOGGER = LogManager.getLogger(EntrustAccountPreparednessJob.class);

    @Autowired
    EntrustAccountService entrustAccountService;

    @Resource
    ShangshangqianService shangshangqianService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("[上上签]开始处理委托开户签名失败的记录");
        List<EntrustAgreementVo> agreementVos = entrustAccountService.getSignFailAgreementList();
        if(agreementVos != null && agreementVos.size() > 0){
            for(EntrustAgreementVo agreementVo : agreementVos){
                //更新记录
                agreementVo = entrustAccountService.lockSignFailAgreement(agreementVo.getId());
                if(agreementVo == null) {
                    continue;
                }
                try {
                    Map userSignMap = getContractInfo(agreementVo.getSignId());
                    //签名分利宝
                    autoSignbyCA_flb(userSignMap, agreementVo, true);
                    //更新记录
                    entrustAccountService.updateUnSignAgreementById(agreementVo.getId(), AgreementSignStatusEnum.YQM);
                } catch (Exception e) {
                    LOGGER.info(String.format("[上上签]备灾委托开户异常(%s)", e.toString()));
                    //更新记录
                    entrustAccountService.updateUnSignAgreementById(agreementVo.getId(), AgreementSignStatusEnum.QMSB);
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info(String.format("[上上签]处理委托开户签名失败数据结束(%s)", agreementVos.size()));
    }

    //合同详情,返回签名用户集合
    public Map getContractInfo(String signid) throws Exception{
        Map userSignMap=new HashMap();
        JSONObject result = ShangshangqianUtil.sdk.contractInfo(signid);
        JSONObject response = result.getJSONObject("response");
        JSONObject content = response.getJSONObject("content");
        JSONArray signUserlist = content.getJSONArray("userlist");
        LOGGER.info("[上上签备灾],委托开户请求上上签返回文档签名信息 result = " + result.toJSONString() +" signId = " + signid);
        for (int i=0;i<signUserlist.size();i++){
            JSONObject userinfo = signUserlist.getJSONObject(i);
            JSONObject user = userinfo.getJSONObject("userinfo");
            String signtime1 = JSONObject.toJSONString(user.get("signtime"));
            JSONObject signtime =null;
            if(signtime1.length()>2){
                signtime =user.getJSONObject("signtime");
            }
            long time = (null==signtime?0l:(long) signtime.get("time"));
            userSignMap.put(user.get("mobile"),time);
        }
        return userSignMap;
    }

    //签名分利宝
    public void autoSignbyCA_flb(Map userSignMap, EntrustAgreementVo agreementVo, boolean openflag)throws Exception{
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
            ReceiveUser[] userlist = {user};
            Map sjdsendcontractMap = new HashMap();
            sjdsendcontractMap.put("userlist",userlist);
            sjdsendcontractMap.put("signid",signid);

            if(userSignMap.containsKey(phone)){//已存在
                if((long)userSignMap.get(phone)==0){//未签名
                    LOGGER.info("[上上签备灾],开始委托开户分利宝签名：signmap="+ JSON.toJSONString(signmap));
                    shangshangqianService.AutoSignbyCA(signmap);
                }
            }else{
                LOGGER.info("[上上签备灾],开始委托开户追加分利宝签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
                shangshangqianService.sjdsendcontract(sjdsendcontractMap);
                LOGGER.info("[上上签备灾],开始委托开户分利宝签名：signmap="+ JSON.toJSONString(signmap));
                shangshangqianService.AutoSignbyCA(signmap);
            }
        }catch (Exception e){
            //记录
            signmap.put("uid",0);
            signmap.put("recordId",agreementVo.getId());
            signmap.put("phone",0);//flb的phone改为0
            shangshangqianService.recordError(signmap);
            throw e;
        }
    }

}
