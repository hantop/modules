package com.fenlibao.p2p.service.xinwang.checkfile.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.checkfile.XWCheckfileDownloadDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.checkfile.*;
import com.fenlibao.p2p.model.xinwang.config.XinWangConfig;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.checkfile.XWCheckfileDownloadService;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.query.XWQueryTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.HttpUtil;
import com.fenlibao.p2p.util.xinwang.SignatureAlgorithm;
import com.fenlibao.p2p.util.xinwang.SignatureUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/5/18.
 */
@Service
public class XWCheckfileDownloadServiceImpl implements XWCheckfileDownloadService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());
    public static XinWangConfig CONFIG;
    static {
        CONFIG = ConfigFactory.create(XinWangConfig.class);
    }
    /**
     * 网关接口后缀
     */
    private final static String DOWNLOAD = "/download";
    private static final DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");


    @Resource
    XWProjectDao projectDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    PTCommonDao ptCommonDao;

    @Resource
    PTCommonService ptCommonService;

    @Resource
    XWCheckfileDownloadDao checkfileDownloadDao;




    @Override
    public void insertCheckfile(String tempString, String fileName) throws Exception {
        if("RECHARGE.txt".equals(fileName)){
            //用逗号隔开
            String[] splitString = tempString.split(",", 15);
            String[] insert = new String[]{"-", "-", " ", ":", ":"};
            Date time = DateUtil.StrToDate(StringHelper.insertInString(splitString[0], insert, 4,7,10,13,16));
            CheckfileRecharge cr = new CheckfileRecharge();
            cr.setRechargeTime(time);
            cr.setPlatformNo(splitString[1]);
            cr.setPayCompanyCode(splitString[2]);
            cr.setRequestNo(splitString[3]);
            cr.setRechargeOrderNo(splitString[4]);
            cr.setBusinessType(splitString[5]);
            cr.setAmount(new BigDecimal(splitString[6]));
            cr.setCurrencyType(splitString[7]);
            cr.setUserId(splitString[8]);
            cr.setPlatformUserNo(splitString[9]);
            cr.setBankcardNo(splitString[10]);
            cr.setBankcode(splitString[11]);
            cr.setPayType(splitString[12]);
            cr.setReceiveAmount(new BigDecimal(splitString[13]));
            cr.setRemark(splitString[14]);

            checkfileDownloadDao.insertCheckfileRecharge(cr);
        }

        if("WITHDRAW.txt".equals(fileName)){
            //用逗号隔开
            String[] splitString = tempString.split(",", 18);
            String[] insert = new String[]{"-", "-", " ", ":", ":"};
            Date time = DateUtil.StrToDate(StringHelper.insertInString(splitString[0], insert, 4,7,10,13,16));
            CheckfileWithdraw cw = new CheckfileWithdraw();
            cw.setWithdrawTime(time);
            cw.setPlatformNo(splitString[1]);
            cw.setRequestNo(splitString[2]);
            cw.setOrderNo(splitString[3]);
            cw.setBusinessType(splitString[4]);
            cw.setAmount(new BigDecimal(splitString[5]));
            cw.setCurrencyType(splitString[6]);
            cw.setUserId(splitString[7]);
            cw.setPlatformUserNo(splitString[8]);
            cw.setBankcardNo(splitString[9]);
            cw.setBankcode(splitString[10]);
            cw.setReceiveAmount(new BigDecimal(splitString[11]));
            cw.setRemark(splitString[12]);
            cw.setWithdrawStatus(splitString[13]);
            cw.setWithdrawWay(splitString[14]);
            cw.setOtherpayAmount(new BigDecimal(splitString[15]));
            cw.setRepaymentType(splitString[16]);
            cw.setWithdrawType(splitString[17]);

            checkfileDownloadDao.insertCheckfileWithdraw(cw);
        }

        if("COMMISSION.txt".equals(fileName)){
            //用逗号隔开
            String[] splitString = tempString.split(",", 11);
            String[] insert = new String[]{"-", "-", " ", ":", ":"};
            Date time = DateUtil.StrToDate(StringHelper.insertInString(splitString[0], insert, 4,7,10,13,16));
            CheckfileCommission cc = new CheckfileCommission();
            cc.setDeductMoneyTime(time);
            cc.setPlatformNo(splitString[1]);
            cc.setOrderNo(splitString[2]);
            cc.setBusinessType(splitString[3]);
            cc.setSponsorPlatformUserNo(splitString[4]);
            cc.setReceiverPlatformUserNo(splitString[5]);
            cc.setAmount(new BigDecimal(splitString[6]));
            cc.setCurrencyType(splitString[7]);
            if(!StringUtils.isEmpty(splitString[8])){
                cc.setLoadId(splitString[8]);
            }
            cc.setRemark(splitString[9]);
            cc.setRequestNo(splitString[10]);

            checkfileDownloadDao.insertCheckfileCommission(cc);
        }

        if("USER.txt".equals(fileName)){
            //用逗号隔开
            String[] splitString = tempString.split(",", 23);
            String[] insert = new String[]{"-", "-", " ", ":", ":"};
            CheckfileUser cu = new CheckfileUser();
            cu.setRecordType(splitString[0]);
            Date time1 = DateUtil.StrToDate(StringHelper.insertInString(splitString[1], insert, 4,7,10,13,16));
            cu.setRegisterTime(time1);
            Date time2 = DateUtil.StrToDate(StringHelper.insertInString(splitString[2], insert, 4,7,10,13,16));
            cu.setModifyTime(time2);
            cu.setUserId(splitString[3]);
            cu.setPlatformNo(splitString[4]);
            cu.setBusinessType(splitString[5]);
            cu.setPlatformUserNo(splitString[6]);
            cu.setMemberType(splitString[7]);
            cu.setMemberRole(splitString[8]);
            cu.setAccountName(splitString[9]);
            cu.setPaperType(splitString[10]);
            cu.setPaperNumber(splitString[11]);
            cu.setMobile(splitString[12]);
            cu.setBankcardNo(splitString[13]);
            cu.setLegalPersonName(splitString[14]);
            cu.setBankcode(splitString[15]);
            cu.setBusinessLicenceNumber(splitString[16]);
            cu.setOrganizationCode(splitString[17]);
            cu.setTaxRegisterNumber(splitString[18]);
            cu.setBankLicence(splitString[19]);
            cu.setOrganizationCreditCode(splitString[20]);
            cu.setUnifySocietyCreditCode(splitString[21]);
            cu.setRemark(splitString[22]);

            checkfileDownloadDao.insertCheckfileUser(cu);
        }

        if("BACKROLL_RECHARGE.txt".equals(fileName)){
            //用逗号隔开
            String[] splitString = tempString.split(",", 15);
            String[] insert = new String[]{"-", "-", " ", ":", ":"};
            Date time = DateUtil.StrToDate(StringHelper.insertInString(splitString[0], insert, 4,7,10,13,16));
            CheckfileBackrollRecharge cbr = new CheckfileBackrollRecharge();
            cbr.setRechargeTime(time);
            cbr.setPlatformNo(splitString[1]);
            cbr.setPayCompanyCode(splitString[2]);
            cbr.setRequestNo(splitString[3]);
            cbr.setRechargeOrderNo(splitString[4]);
            cbr.setBusinessType(splitString[5]);
            cbr.setAmount(new BigDecimal(splitString[6]));
            cbr.setCurrencyType(splitString[7]);
            cbr.setUserId(splitString[8]);
            cbr.setPlatformUserNo(splitString[9]);
            cbr.setPayType(splitString[10]);
            cbr.setReceiveAmount(new BigDecimal(splitString[11]));
            cbr.setWithdrawCommission(new BigDecimal(splitString[12]));
            cbr.setRemark(splitString[13]);
            cbr.setWithdrawRequestNo(splitString[14]);

            checkfileDownloadDao.insertCheckfileBackrollRecharge(cbr);
        }


        if("TRANSACTION.txt".equals(fileName)){
            //用逗号隔开
            String[] splitString = tempString.split(",", 17);
            String[] insert = new String[]{"-", "-", " ", ":", ":"};
            Date time = DateUtil.StrToDate(StringHelper.insertInString(splitString[0], insert, 4,7,10,13,16));
            CheckfileTransaction ct = new CheckfileTransaction();
            ct.setTransactionTime(time);
            ct.setPlatformNo(splitString[1]);
            ct.setRequestNo(splitString[2]);
            ct.setOrderNo(splitString[3]);
            ct.setBusinessType(splitString[4]);
            ct.setAmount(new BigDecimal(splitString[5]));
            ct.setInterestAmount(new BigDecimal(splitString[6]));
            ct.setCurrencyType(splitString[7]);
            ct.setSponsorUserId(splitString[8]);
            ct.setSponsorPlatformUserNo(splitString[9]);
            ct.setReceiverUserId(splitString[10]);
            ct.setReceiverPlatformUserNo(splitString[11]);
            ct.setLoadId(splitString[12]);
            ct.setOldOrderNo(splitString[13]);
            ct.setRemark(splitString[14]);
            ct.setShareRights(new BigDecimal(splitString[15]));
            ct.setCustomDefinedParam(splitString[16]);

            checkfileDownloadDao.insertCheckfileTransaction(ct);
        }
    }

    @Override
    public void downloadCheckFiles(String fileDate)throws Exception{
        // 定义reqData参数集合
        Map<String, Object> reqData = new HashMap<String, Object>();
        reqData.put("fileDate", fileDate);
        // 必须添加的参数
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        String url = XinWangUtil.CONFIG.url() + DOWNLOAD;

        CloseableHttpResponse response = null;
        try {
            List<BasicNameValuePair> formparams = new ArrayList<>();
            BasicNameValuePair bn1 = new BasicNameValuePair("serviceName", "DOWNLOAD_CHECKFILE");
            BasicNameValuePair bn2 = new BasicNameValuePair("platformNo", XinWangUtil.CONFIG.platformNo());
            BasicNameValuePair bn3 = new BasicNameValuePair("reqData", JSON.toJSONString(reqData));
            BasicNameValuePair bn4 = new BasicNameValuePair("keySerial", XinWangUtil.CONFIG.keySerial());
            BasicNameValuePair bn5 = new BasicNameValuePair("sign", sign(JSON.toJSONString(reqData)));
            formparams.add(bn1);
            formparams.add(bn2);
            formparams.add(bn3);
            formparams.add(bn4);
            formparams.add(bn5);

            response = HttpUtil.post(url, formparams);
            InputStream in=response.getEntity().getContent();
            XinWangUtil.downloadCheckFile(in);
        }
        catch(Exception e){
            LOG.error("下载对账文件出错", e);
        }

        try {
            readfile( XinWangUtil.CONFIG.checkFileSavePath() + "/" + fileDate);
        }catch (Exception e){
            LOG.error("对账文件入库出错", e);
        }
    }

    private static String sign(String reqData) throws Exception{
        String privateStr = XinWangUtil.CONFIG.privateKey();
        PrivateKey privateKey = SignatureUtil.getRsaPkcs8PrivateKey(Base64.decodeBase64(privateStr));
        byte[] sign = SignatureUtil.sign(SignatureAlgorithm.SHA1WithRSA,privateKey, reqData);
        String signBase64=Base64.encodeBase64String(sign);
        return signBase64;
    }

    /**
     * 读取某个文件夹下的所有文件
     */
    private void readfile(String filepath) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + File.separator + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath="
                                + readfile.getAbsolutePath());
                        System.out.println("name=" + readfile.getName());
                        readFileByLines(readfile);
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + File.separator + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    @Transactional
    private void readFileByLines(File file) {
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                if(line >1) {
                    insertCheckfile(tempString, file.getName());
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
            LOG.error("对账文件,插入数据出错");
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    @Override
    public void confirmCheckFile(String fileDate)throws Exception{
        //组装请求
        String requestNo= XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("requestNo", requestNo);
        reqData.put("fileDate", fileDate);
        List<Map<String,String>> detail=new ArrayList<>();
        Map<String,String> map1=new HashMap<>();
        map1.put("fileType","RECHARGE");
        detail.add(map1);
        Map<String,String> map2=new HashMap<>();
        map2.put("fileType","WITHDRAW");
        detail.add(map2);
        Map<String,String> map3=new HashMap<>();
        map3.put("fileType","COMMISSION");
        detail.add(map3);
        Map<String,String> map4=new HashMap<>();
        map4.put("fileType","TRANSACTION");
        detail.add(map4);
        Map<String,String> map5=new HashMap<>();
        map5.put("fileType","BACKROLL_RECHARGE");
        detail.add(map5);
        reqData.put("detail",detail);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.CONFIRM_CHECKFILE.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        requestDao.createRequest(req);
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        //发送求
        String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.CONFIRM_CHECKFILE.getCode(),reqData);
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(resultJson);
        requestDao.saveResponseMessage(responseParams);
        //处理结果
        String code = (String) respMap.get("code");
        String status = (String) respMap.get("status");
        String errorMessage = (String) respMap.get("errorMessage");
        if (("0").equals(code)&& "SUCCESS".equals(status)) {
            XWRequest param=new XWRequest();
            param.setRequestNo(requestNo);
            param.setState(XWRequestState.CG);
            requestDao.updateRequest(param);
            LOG.info(fileDate+"新网对账确认成功");
        }
        else{
            XWRequest param=new XWRequest();
            param.setRequestNo(requestNo);
            param.setState(XWRequestState.SB);
            requestDao.updateRequest(param);
            LOG.error(fileDate+"新网对账确认失败："+errorMessage);
        }
    }

    @Override
    public void postXinwang(String fileDate) throws Exception {
        // 定义reqData参数集合
        Map<String, Object> reqData = new HashMap<String, Object>();
        reqData.put("fileDate", fileDate);
        // 必须添加的参数
        reqData.put("timestamp", format.format(new Date()));
        String url = CONFIG.url() + XWCheckfileDownloadServiceImpl.DOWNLOAD;

        CloseableHttpResponse response = null;
        try {
            List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
            BasicNameValuePair bn1 = new BasicNameValuePair("serviceName", "DOWNLOAD_CHECKFILE");
            BasicNameValuePair bn2 = new BasicNameValuePair("platformNo", CONFIG.platformNo());
            BasicNameValuePair bn3 = new BasicNameValuePair("reqData", JSON.toJSONString(reqData));
            BasicNameValuePair bn4 = new BasicNameValuePair("keySerial", CONFIG.keySerial());
            BasicNameValuePair bn5 = new BasicNameValuePair("sign", sign(JSON.toJSONString(reqData)));
            formparams.add(bn1);
            formparams.add(bn2);
            formparams.add(bn3);
            formparams.add(bn4);
            formparams.add(bn5);

            response = HttpUtil.post(url, formparams);
            InputStream in=response.getEntity().getContent();
            XinWangUtil.downloadCheckFile(in);
        }
        catch(Exception e){
            LOG.error("下载对账文件出错", e);
        }

        try {
            readfile(CONFIG.checkFileSavePath() + "/" + fileDate);
        }catch (Exception e){
            LOG.error("对账文件入库出错", e);
        }
    }

    @Override
    public void insertCheckFileStatus(CheckfileDateStatus cfs) {

        checkfileDownloadDao.insertCheckFileStatus(cfs);
    }

    @Override
    public void updateCheckFileStatus(CheckfileDateStatus cfs) {
        checkfileDownloadDao.updateCheckFileStatus(cfs);

    }

    @Override
    public CheckfileDateStatus getCheckFileStatus(CheckfileDateStatus cfs) {

        return checkfileDownloadDao.getCheckFileStatus(cfs);
    }

    @Override
    public List<Map<String, Object>> getCheckWithDrawDiffData(String dateString) {
        return checkfileDownloadDao.getCheckDiffData(dateString);
    }

    @Override
    public List<Map<String, Object>> getCheckRechargeDiffData(String dateString) {
        return checkfileDownloadDao.getCheckRechargeDiffData(dateString);
    }
}
