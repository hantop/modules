package com.fenlibao.p2p.service.thirdparty.impl;

import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dimeng.framework.service.exception.LogicalException;
import com.fenlibao.p2p.dao.user.ThirdPartyUserDao;
import com.fenlibao.p2p.model.entity.bid.ConsumeBid;
import com.fenlibao.p2p.model.entity.bid.InvestRecords;
import com.fenlibao.p2p.model.entity.bid.Tripleagreement;
import com.fenlibao.p2p.model.entity.user.UnRegUser;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.p2p.model.enums.user.ThirdPartyEnum;
import com.fenlibao.p2p.service.thirdparty.BestSignService;
import com.fenlibao.p2p.service.thirdparty.ShangshangqianService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.StringHelper;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.fenlibao.thirdparty.shangshangqian.vo.CertificateApply;
import com.fenlibao.thirdparty.shangshangqian.vo.RegisterUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ShangshangqianServiceImpl extends BestSignService implements ShangshangqianService {
    private static final Logger logger = LogManager.getLogger(ShangshangqianServiceImpl.class);

    @Resource
    private ThirdPartyUserDao thirdPartyUserDao;

    @Override
    public List<ConsumeBid> getUnSignBidList() {
        return thirdPartyUserDao.getUnSignBidList();
    }

    @Transactional
    @Override
    public JSONObject regUserAndCa(Map regUserMap,Map CertificateApplyMap) throws Exception{
        RegisterUser ru=new RegisterUser(regUserMap);
        JSONObject result=null;
        String pfUid =null;

        Map pmap=new HashMap();
        pmap.put("uid",regUserMap.get("userId"));
        pmap.put("pf", ThirdPartyEnum.shangshangqian.getCode());
        try {
            result = ShangshangqianUtil.regUser(ru.getUserType(),ru.getEmail(),ru.getMobile(),ru.getName());
            pfUid = getRegUserId(result);
            pmap.put("pfUid",pfUid);
        }catch (Exception e){
            logger.error(String.format("[上上签注册异常],userId=%s,[%s]"),regUserMap.get("userId"), e);
            throw e;
        }
        if(pfUid==null){
            logger.warn(String.format("[上上签注册失败],userId=%s,result=%s"), regUserMap.get("userId"), result);
            throw new LogicalException("上上签注册失败");
        }
        //更新
//        thirdPartyUserDao.updateRegUser(pmap);
        //ca申请证书
        CertificateApply cf= new CertificateApply(CertificateApplyMap);
        try {
            result = ShangshangqianUtil.certificateApply(cf.getCaType(), cf.getName(), cf.getPassword(),
                    cf.getLinkMobile(), cf.getEmail(), cf.getAddress(), cf.getProvince(), cf.getCity(), cf.getLinkIdCode());
        }catch (Exception e){
            logger.error(String.format("[上上签ca异常],userId=%s,[%s]"), regUserMap.get("userId"), e);
            throw e;
        }

        boolean caResult = (boolean) result.get("isResult");
        if(!caResult){//失败
            logger.warn(String.format("[上上签ca失败],userId=%s,result=%s", regUserMap.get("userId"), result));
            throw new LogicalException("上上签ca失败");
        }else{//更新
            pmap.put("caResult","S");
            thirdPartyUserDao.updateRegUser(pmap);
        }
        return result;
    }

    @Transactional
    @Override
    public JSONObject regUserAndCa(UnRegUser unRegUser) throws Exception {
        JSONObject result = null;
        String pfUid = null;
        int uid=unRegUser.getUserId();
        Map pmap=new HashMap();
        pmap.put("uid",uid);
        pmap.put("pf", ThirdPartyEnum.shangshangqian.getCode());
        pmap.put("phone",unRegUser.getPhone());
        thirdPartyUserDao.addRegUser(pmap);//先新增
        try {
            RegisterUser ru=new RegisterUser();
            ru.setName(unRegUser.getUserName());
            ru.setMobile(unRegUser.getPhone());
            result = ShangshangqianUtil.regUser(ru.getUserType(),ru.getEmail(),ru.getMobile(),ru.getName());
            pfUid = getRegUserId(result);
            pmap.put("pfUid",pfUid);
        }catch (Exception e){
            logger.error(String.format("[上上签注册异常],userId=%s,[%s]", uid, e));
            throw e;
        }
        if(pfUid==null){
            logger.warn(String.format("[上上签注册失败],userId("+uid+")"));
            throw new LogicalException("上上签注册失败");
        }
        //更新
//        thirdPartyUserDao.updateRegUser(pmap);
        //ca申请证书
        CertificateApply cf= new CertificateApply();
        cf.setName(unRegUser.getUserName());
        cf.setLinkMobile(unRegUser.getPhone());
        cf.setLinkIdCode(unRegUser.getIdNum());
        cf.setAddress(unRegUser.getAddr());
        String pwd = CommonTool.randomNumber(8);
        cf.setPassword(pwd);
        pmap.put("pwd",pwd);
        boolean caResult =false;
        try {
            result = ShangshangqianUtil.certificateApply(cf.getCaType(), cf.getName(), cf.getPassword(),
                    cf.getLinkMobile(), cf.getEmail(), cf.getAddress(), cf.getProvince(), cf.getCity(), cf.getLinkIdCode());
            caResult = (boolean) result.get("isResult");
        }catch (Exception e){
            logger.error(String.format("[上上签ca异常],userId:%s,[%s]"), uid, e);
            throw e;
        }
        if(!caResult){//失败
            logger.warn(String.format("[上上签ca失败],userId("+uid+")"));
            throw new LogicalException("上上签ca失败");
        }else{//更新
            pmap.put("caResult","S");
            logger.info(String.format("[上上签ca成功],"+ JSON.toJSONString(cf)));
            thirdPartyUserDao.updateRegUser(pmap);
        }
        return result;
    }

    @Override
    public JSONObject regUser(RegisterUser ru) throws Exception{
        JSONObject result = ShangshangqianUtil.regUser(ru.getUserType(),ru.getEmail(),ru.getMobile(),ru.getName());
        return result;
    }
    @Override
    public JSONObject certificateApply(CertificateApply cf)throws Exception {
        JSONObject result = ShangshangqianUtil.certificateApply(cf.getCaType(), cf.getName(), cf.getPassword(),
                cf.getLinkMobile(), cf.getEmail(), cf.getAddress(), cf.getProvince(), cf.getCity(), cf.getLinkIdCode());
        return result;
    }

    @Override
    public JSONObject sjdsendcontract(Map map)throws Exception  {
        JSONObject result = ShangshangqianUtil.sjdsendcontract(map);
        return result;
    }

    @Override
    public JSONObject AutoSignbyCA(Map map)throws Exception  {
        JSONObject result = ShangshangqianUtil.AutoSignbyCA(map);
        return result;
    }

    @Override
    public List<ConsumeBid> getSignFailConsumeBids() {
        return thirdPartyUserDao.getSignFailConsumeBids();
    }
    @Transactional
    @Override
    public ConsumeBid getSignFailConsumeBid(int bid) {
        Map map = new HashMap();
        map.put("bid", bid);
        ConsumeBid consumeBid=thirdPartyUserDao.getSignFailConsumeBid(map);
        if(consumeBid!=null) {
            map.put("signStatus", AgreementSignStatusEnum.QMZ);
            int re = thirdPartyUserDao.updateUnSignBidInfo(map);
            if (re != 1) return null;
        }
        return consumeBid;
    }

    @Override
    public List<InvestRecords> investRecords(int bid) {
        return thirdPartyUserDao.getInvestRecords(bid);
    }

    @Override
    public List<UnRegUser> unRegUsers(int bid) {
        return thirdPartyUserDao.getUnRegUsers(bid);
    }

    @Transactional
    @Override
    public ConsumeBid lockConsumeBid(int bid) {
        Map map = new HashMap();
        map.put("bid", bid);
        ConsumeBid consumeBid=thirdPartyUserDao.lockConsumeBid(map);
        if(consumeBid!=null) {
            map.put("signStatus", AgreementSignStatusEnum.QMZ);
            int re = thirdPartyUserDao.updateUnSignBidInfo(map);
            if (re != 1) return null;
        }
        return consumeBid;
    }

    @Override
    public int updateConsumeBid(int bid,AgreementSignStatusEnum statusEnum) {
        Map map = new HashMap();
        map.put("bid", bid);
        map.put("signStatus", statusEnum);
        return thirdPartyUserDao.updateUnSignBidInfo(map);
    }

    @Override
    public int recordError(Map signmap) {
        return thirdPartyUserDao.recordError(signmap);
    }

    public String getRegUserId(JSONObject result){
        result =result.getJSONObject("response");
        result =result.getJSONObject("content");
        String pfUid = (String) result.get("uid");
        return pfUid;
    }

    @Override
    public List<Tripleagreement> getUnSignTripleagreements() {
        return thirdPartyUserDao.getUnSignTripleagreements();
    }

    @Transactional
    @Override
    public Tripleagreement getUnSignTripleagreement(int id) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("signStatus", AgreementSignStatusEnum.WQM);
        Tripleagreement tripleagreement = thirdPartyUserDao.getUnSignTripleagreement(map);
        if(tripleagreement!=null) {
            map.put("signStatus", AgreementSignStatusEnum.QMZ);
            int re = thirdPartyUserDao.updateTripleagreement(map);
            if (re != 1) return null;
        }
        return tripleagreement;
    }

    @Override
    public int updateTripleagreement(int id, AgreementSignStatusEnum statusEnum) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("signStatus", statusEnum);
        return thirdPartyUserDao.updateTripleagreement(map);
    }
    @Override
    public List<Tripleagreement> getSignFailTripleagreements() {
        return thirdPartyUserDao.getSignFailTripleagreements();
    }

    @Transactional
    @Override
    public Tripleagreement getSignFailTripleagreement(int id) {
        Map map = new HashMap();
        map.put("id", id);
        Tripleagreement tripleagreement= thirdPartyUserDao.getSignFailTripleagreement(map);
        if(tripleagreement!=null) {
            map.put("signStatus", AgreementSignStatusEnum.QMZ);
            int re = thirdPartyUserDao.updateTripleagreement(map);
            if (re != 1) return null;
        }
        return tripleagreement;
    }


    @Override
    public void sign(ConsumeBid consumeBid){
//      logger.info("上上签开始签,bid:="+consumeBid.getBid());
        //通过手机号码查，如果用户更改手机号码，上上签那边也要解绑，否则会出异常
        List<UnRegUser> unRegUsers=unRegUsers(consumeBid.getBid());
        try {
            //注册和ca
            register(unRegUsers);
        } catch (Exception e) {
            logger.info(String.format("上上签register异常(%s)", e.getMessage()));
            e.printStackTrace();
            return;
        }
        //更新记录
        consumeBid = lockConsumeBid(consumeBid.getBid());
        if(consumeBid == null)return;
        try {
            List<InvestRecords> investRecords=investRecords(consumeBid.getBid());
            //开始签名-投资用户
            autoSignbyCA(investRecords,consumeBid,false);
            //分利宝签名
            autoSignbyCA_flb(consumeBid,true);
            //更新记录
            updateConsumeBid(consumeBid.getBid(), AgreementSignStatusEnum.YQM);
        } catch (Exception e) {
            logger.info(String.format("上上签签名异常(%s)", e.toString()));
            //更新记录
            updateConsumeBid(consumeBid.getBid(), AgreementSignStatusEnum.QMSB);
            e.printStackTrace();
        }
    }



    //批量注册用户
    public void register(List<UnRegUser> unRegUsers) throws Exception{
        if(unRegUsers!=null && unRegUsers.size()>0) {
            for (UnRegUser unRegUser : unRegUsers) {
                String idcard = unRegUser.getIdNum();
                try {
                    idcard = StringHelper.decode(idcard);
                } catch (Throwable throwable) {
                    logger.info(String.format("上上签idcard转换异常(%s)", throwable.getMessage()));
                    throw new LogicalException("上上签idcard转换异常");
                }
                unRegUser.setIdNum(idcard);
//				String addr = IdcardLocation.get(idcard.substring(0, 6));
                String addr ="广州";//不用真实地址
                unRegUser.setAddr(addr);
                regUserAndCa(unRegUser);
            }
        }
    }

    private static final float X_INIT=0.15f;//x默认位置
    private static final float Y_INIT=0.15f;//y默认位置
    private static final int PAGE_SIGN_NUM=21;//一页默认可签数量
    private static final int LINE_SIGN_NUM=3;//一行默认可签数量
    private static final float SIGNX_=0.24f;//签名X偏移量
    private static final float SIGNY_=0.1f;//签名y偏移量
    //签名用户
    public void autoSignbyCA(List<InvestRecords> investRecords,
                             ConsumeBid consumeBid,boolean openflag)throws Exception{
        if(investRecords!=null && investRecords.size()>0) {
            String signid=consumeBid.getSignid();
            float signx = X_INIT;
            float signy = Y_INIT;
            int pagenum_init = consumeBid.getInvestorPageNum();
            int index=0;
            for (InvestRecords InvestRecord : investRecords) {
                signx =(index%PAGE_SIGN_NUM%LINE_SIGN_NUM)*SIGNX_+X_INIT;
                signy =(float)Math.round((Y_INIT+index%PAGE_SIGN_NUM/LINE_SIGN_NUM*SIGNY_)*100)/100;
                int a=(index+1)/PAGE_SIGN_NUM;
                int pagenum=a>0?(index+1)%PAGE_SIGN_NUM==0?pagenum_init+a-1:pagenum_init+a:pagenum_init;
                index++;
                Map signmap = new HashMap();
                signmap.put("signid",signid);
                signmap.put("phone", InvestRecord.getPhone());
                signmap.put("pagenum", pagenum);
                signmap.put("signx",signx);
                signmap.put("signy", signy);
                signmap.put("openflag",openflag);
                try{
                    ReceiveUser user = new ReceiveUser(null, InvestRecord.getUserName(),
                            InvestRecord.getPhone(), Constants.USER_TYPE.PERSONAL, Constants.CONTRACT_NEEDVIDEO.NONE, true);
                    ReceiveUser[] userlist = {user};
                    Map sjdsendcontractMap = new HashMap();
                    sjdsendcontractMap.put("userlist",userlist);
                    sjdsendcontractMap.put("signid",signid);
                    //添加签名记录
                    signmap.put("uid",InvestRecord.getUserId());
                    signmap.put("recordId",consumeBid.getId());
//                    logger.info("[上上签用户签名记录],开始追加签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
                    sjdsendcontract(sjdsendcontractMap);
//                    logger.info("[上上签用户签名记录],开始签名：signmap="+ JSON.toJSONString(signmap));
                    AutoSignbyCA(signmap);
                }catch (Exception e){
                    //记录
                    logger.info("[上上签用户签名记录],异常："+e.toString());
                    recordError(signmap);
                    throw e;
                }
            }
        }
    }

    //签名分利宝
    public void autoSignbyCA_flb(ConsumeBid consumeBid,boolean openflag)throws Exception{
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
        String signid=consumeBid.getSignid();
        Map signmap = new HashMap();
        signmap.put("signid",signid);
        signmap.put("phone",email);//企业账号用的是邮箱
        signmap.put("pagenum", consumeBid.getFlbPageNum());
        signmap.put("signx", consumeBid.getFlbSignX());
        signmap.put("signy", consumeBid.getFlbSignY());
        signmap.put("openflag",openflag);
        try{
            ReceiveUser user = new ReceiveUser(email, username,
                    phone, Constants.USER_TYPE.ENTERPRISE, Constants.CONTRACT_NEEDVIDEO.NONE, true);
            ReceiveUser[] userlist = {user};
            Map sjdsendcontractMap = new HashMap();
            sjdsendcontractMap.put("userlist",userlist);
            sjdsendcontractMap.put("signid",signid);
            sjdsendcontract(sjdsendcontractMap);
            AutoSignbyCA(signmap);
        }catch (Exception e){
            //记录
            signmap.put("uid",0);
            signmap.put("phone",0);//flb的phone改为0
            signmap.put("recordId",consumeBid.getId());
            recordError(signmap);
            throw e;
        }
    }

    public static void main(String[] args) {
        List list =new ArrayList<ConsumeBid>();

        for(int i=0;i<10;i++){
            list.add(new ConsumeBid());
        }
        ShangshangqianServiceImpl o=new ShangshangqianServiceImpl();
        o.batchExe(list,o);
    }



}
