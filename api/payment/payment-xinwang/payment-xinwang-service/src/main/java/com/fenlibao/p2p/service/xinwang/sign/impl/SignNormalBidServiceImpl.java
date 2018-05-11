package com.fenlibao.p2p.service.xinwang.sign.impl;

import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.domain.vo.params.SendUser;
import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.sign.SignNormalBidDao;
import com.fenlibao.p2p.model.api.enums.ConstEnum;
import com.fenlibao.p2p.model.api.vo.PdfPosition;
import com.fenlibao.p2p.model.xinwang.consts.SignConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysVariableConsts;
import com.fenlibao.p2p.model.xinwang.consts.ThirdPartyPlatformEnum;
import com.fenlibao.p2p.model.xinwang.entity.sign.*;
import com.fenlibao.p2p.model.xinwang.enums.common.PathEnum;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignEnterpriseCaVO;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignPersonCaVO;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignRegUserVO;
import com.fenlibao.p2p.service.xinwang.sign.ElectronicSignatureService;
import com.fenlibao.p2p.service.xinwang.sign.SignNormalBidService;
import com.fenlibao.p2p.service.xinwang.sign.handler.SignNormalBidHandler;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.api.load.ApiUtilConfig;
import com.fenlibao.p2p.util.api.pdf.PDFUtil;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zeronx on 2017/12/13 16:34.
 * @version 1.0
 * 说明：在上上签操作记录表的中因为一条记录存储了脱敏和没有脱敏两份文档，且文档的状态控制也在同一条记录中。
 * 为了便于控制状态，所以添加了记录状态 operate_status
 * 通过记录状态 operate_status 来控制操作，0上传 -> 1签名 -> 2下载 -> 3成功（所有操作都已完成） 流程。
 * 只有当上传文档到上上签都成功后 operate_status 状态才能改为1才能进行下一步签名，同理签名，下载也是，只有都成功才可以 改为 2 、3
 * 由于这样的表设计，也就造成了下来的代码，if (脱敏）{...} if (没有脱敏) {...} ， 如果可以优化，是不是可以优化成以某条记录(脱敏一条，不脱敏一条)的形式，而不是现在的
 * 这样将一条记录分为脱敏和没有脱敏。
 */
@Service
public class SignNormalBidServiceImpl implements SignNormalBidService {

    private static final Logger LOGGER = LogManager.getLogger(SignNormalBidServiceImpl.class);

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    @Autowired
    private SignNormalBidDao signNormalBidDao;
    @Autowired
    private PTCommonDao ptCommonDao;

    @Autowired
    private SignNormalBidHandler signNormalBidHandler;
    @Autowired
    private ElectronicSignatureService electronicSignatureService;

    @Override
    public List<SignNormalBidInfo> getSignBidsByStatus(Integer status) {
        return signNormalBidDao.getSignBidsByStatus(status);
    }

    @Transactional
    @Override
    public boolean dealWithStatus0(SignNormalBidInfo signBidInfo) throws Exception {
        SignNormalBidInfo lockSignBidInfo = signNormalBidDao.lockSignBidInfoById(signBidInfo.getBid());
        if (lockSignBidInfo.getOperateStatus() == 0) {
            lockSignBidInfo.setNoSensitiveAgreement(signBidInfo.getNoSensitiveAgreement());
            lockSignBidInfo.setSensitiveAgreement(signBidInfo.getSensitiveAgreement());
            Map<String, String> enterpriseMap = getFlbInfo();
            boolean isAllSuccess = uploadAgreementToShangShangQian(lockSignBidInfo, enterpriseMap);
            if (isAllSuccess) {
                lockSignBidInfo.setOperateStatus(1);
            }
            signNormalBidDao.updateSignBidInfo(lockSignBidInfo);
        }
        return true;
    }

    private boolean uploadAgreementToShangShangQian(SignNormalBidInfo lockSignBidInfo, Map<String, String> enterpriseMap) throws Exception {
        int noSensitive = lockSignBidInfo.getNoSensitiveIsUpload();
        int sensitive = lockSignBidInfo.getSensitiveIsUpload();
        SendUser senduser = new SendUser(enterpriseMap.get("email"), enterpriseMap.get("userName"), enterpriseMap.get("phone"), 30, true, Constants.USER_TYPE.ENTERPRISE, false, "常规标五方合同", "");
        if (lockSignBidInfo.getNoSensitiveIsUpload() == 0) {
            try {
                byte[] fileData = FileUtils.downloadFile(lockSignBidInfo.getNoSensitiveAgreement(), ApiUtilConfig.get("resources.server.path"));
                JSONObject result = ShangshangqianUtil.sjdsendcontractdocUpload(null, senduser, fileData);
                Map<String, String> resultMap = dealJsonResultToMap(result);
                if (resultMap != null) {
                    lockSignBidInfo.setNoSensitiveSignId(resultMap.get("signId"));
                    lockSignBidInfo.setNoSensitiveDocId(resultMap.get("docId"));
                    lockSignBidInfo.setNoSensitiveIsUpload(1);
                    noSensitive = 1;
                }
            } catch (Exception e) {
                LOGGER.error("上传常规标没有脱敏文档到上上签失败：", e);
            }
        }
        if (lockSignBidInfo.getSensitiveIsUpload() == 0) {
            try {
                byte[] fileData = FileUtils.downloadFile(lockSignBidInfo.getSensitiveAgreement(), ApiUtilConfig.get("resources.server.path"));
                JSONObject result = ShangshangqianUtil.sjdsendcontractdocUpload(null, senduser, fileData);
                Map<String, String> resultMap = dealJsonResultToMap(result);
                if (resultMap != null) {
                    lockSignBidInfo.setSensitiveSignId(resultMap.get("signId"));
                    lockSignBidInfo.setSensitiveDocId(resultMap.get("docId"));
                    lockSignBidInfo.setSensitiveIsUpload(1);
                    sensitive = 1;
                }
            } catch (Exception e) {
                LOGGER.error("上传常规标脱敏文档到上上签失败：", e);
            }
        }
        return (noSensitive == sensitive && sensitive == 1);
    }

    /**
     * @param result
     * @return
     */
    private Map<String,String> dealJsonResultToMap(JSONObject result) {
        Map<String, String> resultMap = new HashMap<>();
        if (result == null) {
            return null;
        }
        JSONObject response = result.getJSONObject("response");
        JSONObject info = response.getJSONObject("info");
        String code = info.getString("code");
        if ("100000".equals(code)) {
            JSONObject content = response.getJSONObject("content");
            JSONArray continfoList = content.getJSONArray("contlist");
            JSONObject contractInfo = continfoList.getJSONObject(0);
            contractInfo = contractInfo.getJSONObject("continfo");
            String signId = contractInfo.getString("signid");
            String docId = contractInfo.getString("docid");
            resultMap.put("signId", signId);
            resultMap.put("docId", docId);
            return resultMap;
        }
        return null;
    }

    /**
     * 获取分利宝信息
     * @return
     */
    private Map<String,String> getFlbInfo() {
        String email;
        String username;
        String phone;
        if(Boolean.parseBoolean(ApiUtilConfig.get("ssq.develop.mode"))) {
            email=ApiUtilConfig.get("ssq.sign.email_test");
            username=ApiUtilConfig.get("ssq.sign.name");
            phone=ApiUtilConfig.get("ssq.sign.phone_test");
        }else{
            email=ApiUtilConfig.get("ssq.sign.email");
            username=ApiUtilConfig.get("ssq.sign.name");
            phone=ApiUtilConfig.get("ssq.sign.phone");
        }
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("userName", username);
        params.put("phone", phone);
        return params;
    }

    /**
     * 获取担保品纳信息
     * @return
     */
    private Map<String,String> getPinNaInfo() {
        String email = ApiUtilConfig.get("ssq.sign.pinna.email");
        String username = ApiUtilConfig.get("ssq.sign.pinna.name");
        String phone = ApiUtilConfig.get("ssq.sign.pinna.phone");
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("userName", username);
        params.put("phone", phone);
        return params;
    }

    @Transactional
    @Override
    public boolean dealWithStatus1(SignNormalBidInfo signBidInfo) throws Exception {
        SignNormalBidInfo lockSignBidInfo = signNormalBidDao.lockSignBidInfoById(signBidInfo.getBid());
        // 用来存储需要更新数据库的字段，key（SignNormalBidInfo实体字段名）=value（新值） 形式
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", "" + lockSignBidInfo.getBid());
        if (lockSignBidInfo.getOperateStatus() == 1) {
            Map<String, String> enterpriseMap = getFlbInfo();
            // 签借款个人或企业担保 出借人 等
            boolean isAllSuccess = signBidAgreement(lockSignBidInfo, enterpriseMap, paramsMap);
            // 出借人团签
//            boolean isSuccess = autoTeamSignBidInvestors(lockSignBidInfo, paramsMap);
            if (isAllSuccess) {
                paramsMap.put("operateStatus", "2");
            }
            if (paramsMap.size() > 1) {
                signNormalBidDao.updateSignBidInfoByMap(paramsMap);
            }
        }
        return true;
    }

    public boolean autoTeamSignBidInvestors(SignNormalBidInfo lockSignBidInfo, Map<String, String> paramsMap) throws Exception {
        String fileContentBase64;
        String investorsPdf = lockSignBidInfo.getInvestorsPdf();
        String investorsPdfMd5 = lockSignBidInfo.getInvestorsPdfMd5();
        int noSensitive = lockSignBidInfo.getNoSensitiveInvestorSuc();
        int sensitive = lockSignBidInfo.getSensitiveInvestorSuc();
        try {
            String path = PathEnum.TEMP_PATH.getPath();
            // 获取需要签章的所有账户信息....
            List<Investors> investors = signNormalBidDao.getInvestorsByBid(lockSignBidInfo.getBid());
            if (CollectionUtils.isEmpty(investors)) {
                return true;
            }
            Map<String, String> representMap = getRepresentBy(investors.get(0));
            representMap.put("memberCount", "" + investors.size());
            FileInputStream fileInputStream = null;
            // 出借人签章/出借人签章
            if (StringUtils.isEmpty(investorsPdfMd5)) { // 为空 说明：没有生成出借人名单文件
                try {
                    investorsPdf = signNormalBidHandler.writeInvestorIntoPdf(investors, path);
                    fileInputStream = new FileInputStream(path.concat(investorsPdf));
                    investorsPdf = FileUtils.saveFile(new File(path.concat(investorsPdf)), ApiUtilConfig.get("resources.server.path"), ConstEnum.PDF.getCode());
                    byte[] bData = IOUtils.toByteArray(fileInputStream);
                    fileContentBase64 = Base64.encodeBase64String(bData);
                    investorsPdfMd5 = DigestUtils.md5Hex(bData);
                    paramsMap.put("investorsPdf", investorsPdf);
                    paramsMap.put("investorsPdfMd5", investorsPdfMd5);
                    noSensitive = autoSignInvestors(lockSignBidInfo.getNoSensitiveSignId(), investorsPdfMd5, "pdf", fileContentBase64, representMap);
                    sensitive = autoSignInvestors(lockSignBidInfo.getSensitiveSignId(), investorsPdfMd5, "pdf", fileContentBase64, representMap);
                    paramsMap.put("noSensitiveInvestorSuc", "" + noSensitive);
                    paramsMap.put("sensitiveInvestorSuc", "" + sensitive);
                } catch (Exception e) {
                    LOGGER.error("常规标团体签首次异常（同时生成出借人pdf文件）", e);
                } finally {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                }
            } else {
                try {
                    byte[] bData = FileUtils.downloadFile(investorsPdf, ApiUtilConfig.get("resources.server.path"));
                    fileContentBase64 = Base64.encodeBase64String(bData);
                    if (lockSignBidInfo.getNoSensitiveInvestorSuc() == 0) {
                        noSensitive = autoSignInvestors(lockSignBidInfo.getNoSensitiveSignId(), investorsPdfMd5, "pdf", fileContentBase64, representMap);
                        paramsMap.put("noSensitiveInvestorSuc", "" + noSensitive);
                    }
                    if (lockSignBidInfo.getSensitiveInvestorSuc() == 0) {
                        sensitive = autoSignInvestors(lockSignBidInfo.getSensitiveSignId(), investorsPdfMd5, "pdf", fileContentBase64, representMap);
                        paramsMap.put("sensitiveInvestorSuc", "" + sensitive);
                    }
                } catch (Exception e) {
                    LOGGER.error("常规标团体签次次异常", e);
                } finally {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                }
            }
//            signNormalBidHandler.updateSignBidInvestor(lockSignBidInfo.getBid(), investorsPdfMd5, investorsPdf, noSensitive, sensitive);
        } catch (Exception e) {
            LOGGER.error("获取常规表出借人时异常：", e);
        }
        return (noSensitive == sensitive && sensitive == 1);
    }

    private int autoSignInvestors(String signId, String filePdfMd5, String fileType, String fileContent, Map<String, String> representMap) {
        try {
            JSONObject jsonObject = ShangshangqianUtil.autoTeamSign(signId, filePdfMd5, fileType, fileContent, representMap);
            return processRequestSuc(jsonObject);
        } catch (Exception e) {
            LOGGER.error("调用上上签团体签异常：" , e);
        }
        return 0;
    }

    private int processRequestSuc(JSONObject result) {
        if (result == null) {
            return 0;
        }
        JSONObject response = result.getJSONObject("response");
        JSONObject info = response.getJSONObject("info");
        String code = info.getString("code");
        if ("100000".equals(code)) {
            return 1;
        }
        return 0;
    }

    private Map<String,String> getRepresentBy(Investors investor) throws Exception {
        Map<String, String> params = new HashMap<>();
//        String idCard;
//        try {
//            idCard = StringHelper.decode(investor.getIdCard());
//        } catch (Throwable throwable) {
//            LOGGER.error("解码身份证出错:" + throwable);
//            throw new Exception("解码身份证出错");
//        }
        params.put("representative", "{\"description\":\"代表\",\"value\":\""+investor.getName()+"\"}");
        params.put("representativeType", "{\"description\":\"团队代表类型\",\"value\":\"1\"}");
        params.put("representativeCredentials", investor.getIdCard());
        params.put("representativeCredentialsType", "0");
        return params;
    }

    private boolean signBidAgreement(SignNormalBidInfo lockSignBidInfo, Map<String, String> enterpriseMap, Map<String, String> paramsMap) throws Exception {
        Map noSensitiveConMap = getContractInfo(lockSignBidInfo.getNoSensitiveSignId());
        Map sensitiveConMap = getContractInfo(lockSignBidInfo.getSensitiveSignId());
        int noSensitive = 1;
        int sensitive = 1;
        List<Investors> investors = signNormalBidDao.getInvestorsByBid(lockSignBidInfo.getBid());
        // 获取签章借款人或企业
        SignUserInfo signUserInfo = signNormalBidDao.getSignUserInfoByBid(lockSignBidInfo.getBid());
        // 借款用户/企业信息
        Map<String, String> borrowerMap = signNormalBidHandler.convertBorrowerUserToMap(signUserInfo);
        // 连带担保用户/企业信息
        Map<String, String> liabilityMap = signNormalBidHandler.convertLiabilityUserToMap(signUserInfo);
        boolean hasLiability = signUserInfo.getLiabilityUserType() == 2 ? false : true;
        int positionVector = 3;
        int startPage; // 开始签出借人的页数
        // 获取签章对应的坐标
        String positionJsonStr;

        // 出借人需要签章的页数，包含原文档那页 那么新添加的页数 必定 会影响 原始 分利宝 借款人等签章的页数 下面签这些章时 需要处理一下
        int needAddPages = investors.size() % SignConsts.SIGN_INVESTOR_NUMS == 0 ? investors.size() / SignConsts.SIGN_INVESTOR_NUMS : investors.size() / SignConsts.SIGN_INVESTOR_NUMS + 1;

        if (hasLiability) { //四方合同
            positionJsonStr = ptCommonDao.getSystemVariable(SysVariableConsts.AGREEMENT_POSITION_FOR_FOURTH);
            startPage = SignConsts.DELETE_AGREEMENT4_PAGE_POSITION - 1;
        } else { // 三方合同
            positionJsonStr = ptCommonDao.getSystemVariable(SysVariableConsts.AGREEMENT_POSITION_FOR_THREE);
            startPage = SignConsts.DELETE_AGREEMENT3_PAGE_POSITION - 1;
        }
        // 处理position位置
        Map<String, PdfPosition> positionMap = dealWithPositionJsonStr(positionJsonStr, hasLiability, positionVector, needAddPages);
        // 签不敏感合同
        if ("WQM".equals(lockSignBidInfo.getNoSensitiveSignStatus()) || "QMSB".equals(lockSignBidInfo.getNoSensitiveSignStatus())) {
            noSensitive = 0;
            try {
                // 签出借人
                autoSignInvestors(noSensitiveConMap, lockSignBidInfo.getNoSensitiveSignId(), investors, startPage);
                if (signUserInfo.getBorrowerISPerson() == 1) { // 借款人为个人
                    // 签借款个人
                    autoSignPerson(noSensitiveConMap, lockSignBidInfo.getNoSensitiveSignId(), borrowerMap, positionMap.get("param46"));
                } else {
                    // 借款人为企业
                    autoSignEnterprise(noSensitiveConMap, lockSignBidInfo.getNoSensitiveSignId(), borrowerMap, positionMap.get("param46"));
                }
                // 签分利宝章
                autoSignEnterprise(noSensitiveConMap, lockSignBidInfo.getNoSensitiveSignId(), enterpriseMap, positionMap.get("param47"));
                if (hasLiability) {
                    // 签连带责任担保章
                    if (signUserInfo.getLiabilityUserType() != null && signUserInfo.getLiabilityUserType() == 1 && StringUtils.isNotEmpty(signUserInfo.getLiabilityIdCardNo())) { // 连带责任担为个人
                        // 签连带个人
                        autoSignPerson(noSensitiveConMap, lockSignBidInfo.getNoSensitiveSignId(), liabilityMap, positionMap.get("param48"));
                    } else {
                        // 连带责任担企业
                        if (signUserInfo.getLiabilityUserType() != null && signUserInfo.getLiabilityUserType() == 0 && StringUtils.isNotEmpty(signUserInfo.getLiabilityUnifiedCode())) {
                            autoSignEnterprise(noSensitiveConMap, lockSignBidInfo.getNoSensitiveSignId(), liabilityMap, positionMap.get("param48"));
                        }
                    }
                }
                // 签一般担保签章 modify by zeronx 不签品纳 2018-02-06 17：29
//                autoSignEnterprise(noSensitiveConMap, lockSignBidInfo.getNoSensitiveSignId(), getPinNaInfo(), positionMap.get("param39"));
                paramsMap.put("noSensitiveSignStatus", "YQM");
                noSensitive = 1;
            } catch (Exception e) {
                paramsMap.put("noSensitiveSignStatus", "QMSB");
                LOGGER.error("常规标签章异常，签个人或企业不敏感合同异常", e);
            }
        }
        // 签敏感合同
        if ("WQM".equals(lockSignBidInfo.getSensitiveSignStatus()) || "QMSB".equals(lockSignBidInfo.getSensitiveSignStatus())) {
            sensitive = 0;
            try {
                // 签出借人
                autoSignInvestors(sensitiveConMap, lockSignBidInfo.getSensitiveSignId(), investors, startPage);
                if (signUserInfo.getBorrowerISPerson() == 1) { // 借款人为个人
                    // 签借款个人
                    autoSignPerson(sensitiveConMap, lockSignBidInfo.getSensitiveSignId(), borrowerMap, positionMap.get("param46"));
                } else {
                    // 借款人为企业
                    autoSignEnterprise(sensitiveConMap, lockSignBidInfo.getSensitiveSignId(), borrowerMap, positionMap.get("param46"));
                }
                // 签分利宝章
                autoSignEnterprise(sensitiveConMap, lockSignBidInfo.getSensitiveSignId(), enterpriseMap, positionMap.get("param47"));
                if (hasLiability) {
                    // 签连带责任担保章
                    if (signUserInfo.getLiabilityUserType() != null && signUserInfo.getLiabilityUserType() == 1 && StringUtils.isNotEmpty(signUserInfo.getLiabilityIdCardNo())) { // 连带责任担为个人
                        // 签连带个人
                        autoSignPerson(sensitiveConMap, lockSignBidInfo.getSensitiveSignId(), liabilityMap, positionMap.get("param48"));
                    } else {
                        // 连带责任担企业
                        if (signUserInfo.getLiabilityUserType() != null && signUserInfo.getLiabilityUserType() == 0 && StringUtils.isNotEmpty(signUserInfo.getLiabilityUnifiedCode())) {
                            autoSignEnterprise(sensitiveConMap, lockSignBidInfo.getSensitiveSignId(), liabilityMap, positionMap.get("param48"));
                        }
                    }
                }
                // // 签一般担保签章 modify by zeronx 不签品纳 2018-02-06 17：29
//                autoSignEnterprise(sensitiveConMap, lockSignBidInfo.getSensitiveSignId(), getPinNaInfo(), positionMap.get("param39"));
                paramsMap.put("sensitiveSignStatus", "YQM");
                sensitive = 1;
            } catch (Exception e) {
                paramsMap.put("sensitiveSignStatus", "QMSB");
                LOGGER.error("常规标签章异常，签个人或企业敏感合同异常", e);
            }
        }
        return (noSensitive == sensitive && sensitive == 1);
    }

    private void autoSignInvestors(Map conMap, String signId, List<Investors> investors, int startPage) throws Exception {
        float X_INIT=0.15f;//x默认位置
        float Y_INIT=0.15f;//y默认位置
        int PAGE_SIGN_NUM=SignConsts.SIGN_INVESTOR_NUMS;//一页默认可签数量
        int LINE_SIGN_NUM=3;//一行默认可签数量
        float SIGNX_=0.24f;//签名X偏移量
        float SIGNY_=0.1f;//签名y偏移量
        float signX = X_INIT;
        float signY = Y_INIT;
        int pageNum_init = startPage + 1;
        Map<String, String> personMap = new HashMap<>();
        PdfPosition pdfPosition;
        int index=0;
        for (Investors investor : investors) {
            signX = (index % PAGE_SIGN_NUM % LINE_SIGN_NUM) * SIGNX_ + X_INIT;
            signY = (float)Math.round((Y_INIT + index % PAGE_SIGN_NUM / LINE_SIGN_NUM * SIGNY_) *100) / 100;
            int a = (index + 1) / PAGE_SIGN_NUM;
            int pageNum = a > 0 ? (index + 1) % PAGE_SIGN_NUM == 0 ? pageNum_init + a - 1 : pageNum_init + a : pageNum_init;
            index++;
            personMap.put("email", StringHelper.encode(investor.getIdCard()));
            personMap.put("userName", investor.getName());
            personMap.put("phone", investor.getPhone());
            pdfPosition = new PdfPosition();
            pdfPosition.setPage(pageNum);
            pdfPosition.setSignX(signX);
            pdfPosition.setSignY(signY);
            autoSignPerson(conMap, signId, personMap, pdfPosition);
        }
    }

    private Map<String, PdfPosition> dealWithPositionJsonStr(String positionJsonStr, boolean hasLiability, int positionVector, int investorsPageNum) {
        float pageSizeX = 595.50f;
        float pageSizeY = 842.25f;
        Map<String, PdfPosition> params = new HashMap<>();
        Map<String, JSONObject> map3 = JSONObject.parseObject(positionJsonStr, Map.class);
        for (String paramKey : map3.keySet()) {
            PdfPosition pdfPosition = JSON.parseObject(map3.get(paramKey).toString(), PdfPosition.class);
            pdfPosition.setPage(pdfPosition.getPage() + investorsPageNum - 1);
            pdfPosition.setSignX(pdfPosition.getSignX()/pageSizeX);
            pdfPosition.setSignY((pageSizeY - (pdfPosition.getSignY() + 10))/pageSizeY);
            if (hasLiability) {
                params.put(paramKey, pdfPosition);
            } else {
                int k = Integer.parseInt(paramKey.substring(5)) + positionVector;
                params.put("param" + k, pdfPosition);
            }
        }
        return params;
    }

    /**
     * 签个人
     * @param noSensitiveConMap
     * @param signId
     * @param personMap
     * @param pdfPosition
     * @throws Exception
     */
    private void autoSignPerson(Map noSensitiveConMap, String signId, Map<String, String> personMap, PdfPosition pdfPosition) throws Exception {
        Map signmap = new HashMap();
        signmap.put("signid",signId);
        signmap.put("phone",personMap.get("email")); //此时的phone字段实际为email传往上上签的email字段
        signmap.put("pagenum", pdfPosition.getPage());
        signmap.put("signx", pdfPosition.getSignX());
        signmap.put("signy", pdfPosition.getSignY());
        signmap.put("openflag", false);
        ReceiveUser user = new ReceiveUser(personMap.get("email"), personMap.get("userName"),
                personMap.get("phone"), Constants.USER_TYPE.PERSONAL, Constants.CONTRACT_NEEDVIDEO.NONE, true);
        ReceiveUser[] userlist = {user};
        Map sjdsendcontractMap = new HashMap();
        sjdsendcontractMap.put("userlist",userlist);
        sjdsendcontractMap.put("signid",signId);
        if(noSensitiveConMap.containsKey(personMap.get("email"))){//已存在
            if((long)noSensitiveConMap.get(personMap.get("email")) == 0) {//未签名
                LOGGER.info("常规标开始个人签名：signmap="+ JSON.toJSONString(signmap));
                ShangshangqianUtil.AutoSignbyCA(signmap);
            }
        }else{
            LOGGER.info("常规标开始追加个人签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
            ShangshangqianUtil.sjdsendcontract(sjdsendcontractMap);
            LOGGER.info("常规标开始个人签名：signmap="+ JSON.toJSONString(signmap));
            ShangshangqianUtil.AutoSignbyCA(signmap);
        }
    }

    /**
     * 签企业
     * @param noSensitiveConMap
     * @param signId
     * @param enterpriseMap
     * @param pdfPosition
     * @throws Exception
     */
    private void autoSignEnterprise(Map noSensitiveConMap, String signId, Map<String, String> enterpriseMap, PdfPosition pdfPosition) throws Exception {
        Map signmap = new HashMap();
        signmap.put("signid",signId);
        signmap.put("phone",enterpriseMap.get("email"));//企业账号用的是邮箱 此时的phone字段实际为email传往上上签的email字段
        signmap.put("pagenum", pdfPosition.getPage());
        signmap.put("signx", pdfPosition.getSignX());
        signmap.put("signy", pdfPosition.getSignY());
        signmap.put("openflag", false);
        ReceiveUser user = new ReceiveUser(enterpriseMap.get("email"), enterpriseMap.get("userName"),
                enterpriseMap.get("phone"), Constants.USER_TYPE.ENTERPRISE, Constants.CONTRACT_NEEDVIDEO.NONE, true);
        ReceiveUser[] userlist = {user};
        Map sjdsendcontractMap = new HashMap();
        sjdsendcontractMap.put("userlist",userlist);
        sjdsendcontractMap.put("signid",signId);
        if(noSensitiveConMap.containsKey(enterpriseMap.get("email"))){//已存在
            if((long)noSensitiveConMap.get(enterpriseMap.get("email")) == 0) {//未签名
                LOGGER.info("常规标开始企业签名：signmap="+ JSON.toJSONString(signmap));
                ShangshangqianUtil.AutoSignbyCA(signmap);
            }
        }else{
            LOGGER.info("常规标开始追加企业签名参数：sjdsendcontractMap="+ JSON.toJSONString(sjdsendcontractMap));
            ShangshangqianUtil.sjdsendcontract(sjdsendcontractMap);
            LOGGER.info("常规标开始企业签名：signmap="+ JSON.toJSONString(signmap));
            ShangshangqianUtil.AutoSignbyCA(signmap);
        }
    }

    //合同详情,返回签名用户集合
    public Map getContractInfo(String signid) throws Exception {
        Map userSignMap=new HashMap();
        JSONObject result = ShangshangqianUtil.sdk.contractInfo(signid);
        JSONObject response = result.getJSONObject("response");
        JSONObject content = response.getJSONObject("content");
        JSONArray  signUserlist = content.getJSONArray("userlist");
        for (int i = 0; i < signUserlist.size(); i++) {
            JSONObject userinfo = signUserlist.getJSONObject(i);
            JSONObject user = userinfo.getJSONObject("userinfo");
            String signtime1 = JSONObject.toJSONString(user.get("signtime"));
            JSONObject signtime =null;
            if(signtime1.length() > 2) {
                signtime =user.getJSONObject("signtime");
            }
            long time = (null == signtime ? 0l : (long) signtime.get("time"));
            userSignMap.put(user.get("email"), time);
        }
        return userSignMap;
    }

    //    @Transactional
    @Override
    public boolean dealWithStatus2(SignNormalBidInfo signBidInfo) throws Exception {
        SignNormalBidInfo lockSignBidInfo = signNormalBidDao.lockSignBidInfoById(signBidInfo.getBid());
        // 用来存储需要更新数据库的字段，key（SignNormalBidInfo实体字段名）=value（新值）
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", "" + lockSignBidInfo.getBid());
        if (lockSignBidInfo.getOperateStatus() == 2) {
            lockSignBidInfo.setNoSensitiveAgreement(signBidInfo.getNoSensitiveAgreement()); // 脱敏合同文件
            lockSignBidInfo.setSensitiveAgreement(signBidInfo.getSensitiveAgreement()); // 完整合同文件
            // 下载签名成功的协议（脱敏和不脱敏）
            boolean isAllSuccess = signNormalBidHandler.downloadSignBidAgreement(lockSignBidInfo, paramsMap);
            if (isAllSuccess) {
                paramsMap.put("operateStatus", "3");
            }
            if (paramsMap.size() > 1) {
                signNormalBidDao.updateSignBidInfoByMap(paramsMap);
            }
        }
        return true;
    }

    private int endContract(String signId) {
        try {
            JSONObject jsonObject = ShangshangqianUtil.endContract(signId);
            return processRequestSuc(jsonObject);
        } catch (Exception e) {
            LOGGER.error("调用上上签结束合同签接口异常", e);
        }
        return 0;
    }

    @Override
    public List<SignXFXDBid> getXFXDBids() {
        return signNormalBidDao.getXFXDBids();
    }

    @Override
    public List<SignXFXDBid> getYqmXFXDBids() {
        return signNormalBidDao.getYqmXFXDBids();
    }

    /**
     * 如果消费信贷很多很多的话，可以考虑用多线程下载，现在暂时不用
     * @param signXFXDBids
     * @param isSave true: 新下载文档保存到s62.sign_agreement_download , false : 更新 s62.sign_agreement_download
     */
    @Override
    public void downloadXFXDAgreement(List<SignXFXDBid> signXFXDBids, final boolean isSave) {
        List<SignXFXDBid> tempSigns = new ArrayList<>();

        String fileName;
        // 异步任务完成后放在其封装的阻塞队列中，调用 take()获取，当队列为空时会阻塞。
        CompletionService<SignXFXDBid> completionService = new ExecutorCompletionService<>(executor);
        try {
            for (final SignXFXDBid signXFXDBid : signXFXDBids) {
                try {
                    fileName = signXFXDBid.getSensitiveAgreement();
                    if (StringUtils.isEmpty(fileName)) { // 新增加
                        fileName = FileUtils.getSealCode(null, "pdf", ConstEnum.PDF.getCode());
                        signXFXDBid.setNoSensitiveAgreement(fileName);
                        signXFXDBid.setSensitiveAgreement(fileName);
                    }
                    signXFXDBid.setSign(0);
                    if ("YQM".equals(signXFXDBid.getSignStatus())) {
                        signXFXDBid.setSign(1);
                    }
                    completionService.submit(new Callable<SignXFXDBid>() {
                        @Override
                        public SignXFXDBid call() throws Exception {
                            try {
                                byte[] result = ShangshangqianUtil.dowloadPdf(signXFXDBid.getSignId());
                                String tempPath = PathEnum.TEMP_PATH.getPath();
                                byte[] newResult = PDFUtil.dealWithResultPdf(result, tempPath, 19);
//                                byte[] image = PDFUtil.convertPdfToImage(result);
                                boolean isSuc = FileUtils.saveFile(newResult, ApiUtilConfig.get("resources.server.path"), signXFXDBid.getNoSensitiveAgreement());
                                if (isSuc) {
                                    return signXFXDBid;
                                }
                                return null;
                            } catch (Exception e) {
                                LOGGER.error("从上上签下载消费信贷保存时异常", e);
                            }
                            return null;
                        }
                    });
                } catch (Exception e) {
                    LOGGER.error("从上上签下载文档时异常, bid:{}, signId{}, {}", signXFXDBid.getBid(), signXFXDBid.getSignId(), e);
                }
            }
            for (int i = 0, n = signXFXDBids.size(); i < n; i++) {
                try {
                    // 组塞获取，完成一个取一个
                    Future<SignXFXDBid> future = completionService.take();
                    SignXFXDBid signXFXDBidC = future.get();
                    if (signXFXDBidC != null) {
                        tempSigns.add(signXFXDBidC);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("执行下载的消费信贷的线程在运行时被强制中断", e);
//                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    LOGGER.error("执行下载的消费信贷的线程运行异常", e);
                }
            }
            if (tempSigns.size() > 0) {
                if (isSave) {
                    signNormalBidDao.saveSignAgreementDownloads(tempSigns);
                } else {
                    signNormalBidDao.updateSignAgreementDownloads(tempSigns);
                }
            }
        } catch (Exception e) {
            LOGGER.error("处理消费信贷下载文档时异常", e);
        }
    }

    @Override
    public SignRegUserRecord getRegUserRecord(String email, int platform) {
        return signNormalBidDao.getRegUserByEmail(email, platform);
    }

    @Override
    public boolean isAllSignUserRegAndCA(Integer bid) {
        try {
            // 获取签章借款人或企业
            SignUserInfo signUserInfo = signNormalBidDao.getSignUserInfoByBid(bid);
            SignRegUserRecord signRegUserRecord2 = null; // 连带担保
            boolean isRegAndCaSuc = true; //用于判断借款用户和连带担保注册和ca是否都成功
            boolean isCaSuc = true; // 用于判断借款用户和连带担保ca是否都成功
            boolean isUploadSuc = true;
            // 循环注册或ca 上上签
            List<SignRegUserRecord> notResInvestors = signNormalBidDao.getNotRegInvestorsByBid(bid);
            for (SignRegUserRecord notResInvestor : notResInvestors) {
                SignUserInfo signUserInfo1 = new SignUserInfo();
                signUserInfo1.setBorrowerIdCard(notResInvestor.getEmail());
                signUserInfo1.setEnterprisePhone(notResInvestor.getPhone());
                signUserInfo1.setEnterpriseUserName(notResInvestor.getMark());
                SignPersonCaVO signPersonCaVO = signNormalBidHandler.convetSignUserInfoToBorrowerPersonCaVO(signUserInfo1);
                if (notResInvestor.getCaResult() == null) {
                    SignRegUserVO signRegUserVo = new SignRegUserVO(Constants.USER_TYPE.PERSONAL.value(), notResInvestor.getEmail(), notResInvestor.getPhone(), notResInvestor.getMark());
                    // 注册和ca是否成功
                    isRegAndCaSuc = isRegAndCaSuc && regUserAndPersonCa(notResInvestor.getUserId(), signRegUserVo, signPersonCaVO);

                } else if (notResInvestor.getCaResult() == 1) {
                    JSONObject enCaJson = personCa(signPersonCaVO);
                    Boolean caResult = (boolean) enCaJson.get("isResult");
                    isCaSuc = isCaSuc && caResult;
                    if (caResult) {
                        LOGGER.info("用户email：{}, phone:{}(申请申请证书成功）", signPersonCaVO.getEmail(), signPersonCaVO.getLinkMobile());
                        signNormalBidHandler.updateCaResult(signPersonCaVO.getEmail(), 0, signPersonCaVO.getPassword());
                    }
                }
            }
            // 借款人是否已注册上上签
            SignRegUserRecord signRegUserRecord = getRegUserRecord(signUserInfo.getBorrowerIdCard(), ThirdPartyPlatformEnum.SHANGSHANGQIAN.getCode());
            if (signUserInfo.getBorrowerISPerson() == 1) {
                // 没有，就去注册和申请证书
                if (signRegUserRecord == null) { //个人
                    SignRegUserVO signRegUserVo = new SignRegUserVO(Constants.USER_TYPE.PERSONAL.value(), signUserInfo.getBorrowerIdCard(), signUserInfo.getEnterprisePhone(), signUserInfo.getEnterpriseUserName());
                    SignPersonCaVO signPersonCaVO = signNormalBidHandler.convetSignUserInfoToBorrowerPersonCaVO(signUserInfo);
                    // 注册和ca是否成功
                    isRegAndCaSuc = isRegAndCaSuc && regUserAndPersonCa(signUserInfo.getUserId(), signRegUserVo, signPersonCaVO);

                } else if (signRegUserRecord != null && signRegUserRecord.getCaResult() == 1) { // 如果已经注册 但 ca失败 那么去ca（申请证书）
                    SignPersonCaVO signPersonCaVO = signNormalBidHandler.convetSignUserInfoToBorrowerPersonCaVO(signUserInfo);
                    JSONObject enCaJson = personCa(signPersonCaVO);
                    Boolean caResult = (boolean) enCaJson.get("isResult");
                    isCaSuc = isCaSuc && caResult;
                    if (caResult) {
                        LOGGER.info("用户email：{}, phone:{}(申请申请证书成功）", signPersonCaVO.getEmail(), signPersonCaVO.getLinkMobile());
                        signNormalBidHandler.updateCaResult(signPersonCaVO.getEmail(), 0, signPersonCaVO.getPassword());
                    }
                }
            } else {
                if (signRegUserRecord == null) { // 企业
                    SignRegUserVO signRegUserVo = new SignRegUserVO(Constants.USER_TYPE.ENTERPRISE.value(), signUserInfo.getBorrowerIdCard(), signUserInfo.getEnterprisePhone(), signUserInfo.getEnterpriseUserName());
                    SignEnterpriseCaVO signEnterpriseCaVO = signNormalBidHandler.convetSignUserInfoToBorrowerEnterpriseCaVO(signUserInfo);
                    isRegAndCaSuc = isRegAndCaSuc && regUserAndEnterpriseCa(signUserInfo.getUserId(), signRegUserVo, signEnterpriseCaVO);
                } else if (signRegUserRecord != null && signRegUserRecord.getCaResult() == 1) { // 如果已经注册 但 ca失败 那么去ca（申请证书）
                    SignEnterpriseCaVO signEnterpriseCaVO = signNormalBidHandler.convetSignUserInfoToBorrowerEnterpriseCaVO(signUserInfo);
                    enterpriseCa(signEnterpriseCaVO);
                    JSONObject enCaJson = enterpriseCa(signEnterpriseCaVO);
                    Boolean caResult = (boolean) enCaJson.get("isResult");
                    isCaSuc = isCaSuc && caResult;
                    if (caResult) {
                        LOGGER.info("用户email：{}, phone:{}(申请申请证书成功）", signEnterpriseCaVO.getEmail(), signEnterpriseCaVO.getLinkMobile());
                        signNormalBidHandler.updateCaResult(signEnterpriseCaVO.getEmail(), 0, signEnterpriseCaVO.getPassword());
                    }
                }
            }

            //连带担保是否已注册上上签
            if (signUserInfo.getLiabilityUserType() != null && signUserInfo.getLiabilityUserType() == 1) { //个人连带担保
                if (StringUtils.isNotEmpty(signUserInfo.getLiabilityIdCardNo())) {
                    signRegUserRecord2 = getRegUserRecord(signUserInfo.getLiabilityIdCardNo(), ThirdPartyPlatformEnum.SHANGSHANGQIAN.getCode());
                    // 没有，就去注册和申请证书
                    if (signRegUserRecord2 == null) {
                        SignRegUserVO signRegUserVo = new SignRegUserVO(Constants.USER_TYPE.PERSONAL.value(), signUserInfo.getLiabilityIdCardNo(), signUserInfo.getLiabilityPhone(), signUserInfo.getLiabilityUserName());
                        SignPersonCaVO signPersonCaVO = signNormalBidHandler.convetSignUserInfoToLiabilityPersonCaVO(signUserInfo);
                        isRegAndCaSuc = isRegAndCaSuc && regUserAndPersonCa(null, signRegUserVo, signPersonCaVO);
                    }
                    // 如果已经注册 但 ca失败 那么去ca（申请证书）
                    if (signRegUserRecord2 != null && signRegUserRecord2.getCaResult() == 1) {
                        SignPersonCaVO signPersonCaVO = signNormalBidHandler.convetSignUserInfoToLiabilityPersonCaVO(signUserInfo);
                        JSONObject enCaJson = personCa(signPersonCaVO);
                        Boolean caResult = (boolean) enCaJson.get("isResult");
                        isCaSuc = isCaSuc && caResult;
                        if (caResult) {
                            LOGGER.info("用户email：{}, phone:{}(申请申请证书成功）", signPersonCaVO.getEmail(), signPersonCaVO.getLinkMobile());
                            signNormalBidHandler.updateCaResult(signPersonCaVO.getEmail(), 0, signPersonCaVO.getPassword());
                        }
                    }
                }
            } else { // 企业连带担保
                if (StringUtils.isNotEmpty(signUserInfo.getLiabilityUnifiedCode())) {
                    signRegUserRecord2 = getRegUserRecord(signUserInfo.getLiabilityUnifiedCode(), ThirdPartyPlatformEnum.SHANGSHANGQIAN.getCode());
                    if (signRegUserRecord2 == null) {
                        SignRegUserVO signRegUserVo = new SignRegUserVO(Constants.USER_TYPE.ENTERPRISE.value(), signUserInfo.getLiabilityUnifiedCode(), signUserInfo.getLiabilityPhone(), signUserInfo.getLiabilityUserName());
                        SignEnterpriseCaVO signEnterpriseCaVO = signNormalBidHandler.convetSignUserInfoToLiabilityEnterpriseCaVO(signUserInfo);
                        isRegAndCaSuc = isRegAndCaSuc && regUserAndEnterpriseCa(null, signRegUserVo, signEnterpriseCaVO);
                    }
                    // 如果已经注册 但 ca失败 那么去ca（申请证书）
                    if (signRegUserRecord2 != null && signRegUserRecord2.getCaResult() == 1) {
                        SignEnterpriseCaVO signEnterpriseCaVO = signNormalBidHandler.convetSignUserInfoToLiabilityEnterpriseCaVO(signUserInfo);
                        JSONObject enCaJson = enterpriseCa(signEnterpriseCaVO);
                        Boolean caResult = (boolean) enCaJson.get("isResult");
                        isCaSuc = isCaSuc && caResult;
                        if (caResult) {
                            LOGGER.info("用户email：{}, phone:{}(申请申请证书成功）", signEnterpriseCaVO.getEmail(), signEnterpriseCaVO.getLinkMobile());
                            signNormalBidHandler.updateCaResult(signEnterpriseCaVO.getEmail(), 0, signEnterpriseCaVO.getPassword());
                        }
                    }
                }
            }
            // 判断借款企业公章是否上传 否：就是去上传
            if(signUserInfo.getBorrowerISPerson() == 0 && !signUserInfo.getEnterpriseStatus()) {
                isUploadSuc = isUploadSuc && uploadEnterpriseImage(signUserInfo.getBorrowerIdCard(), signUserInfo.getEnterprisePhone(), signUserInfo.getEnterpriseUserName(), signUserInfo.getEnterpriseOfficialSealCode());
                if (isUploadSuc) { //  upload img shanghsangqian Success
                    electronicSignatureService.updateSealStatus(signUserInfo.getId(), "enterprise");
                }
            }
            // 判断连带担保企业公章是否上传 否：就是去上传
            if (signUserInfo.getLiabilityUserType() != null && signUserInfo.getLiabilityUserType() == 0 && !signUserInfo.getLiabilityStatus() && StringUtils.isNotEmpty(signUserInfo.getLiabilityUnifiedCode())) {
                boolean isSuc = uploadEnterpriseImage(signUserInfo.getLiabilityUnifiedCode(), signUserInfo.getLiabilityPhone(), signUserInfo.getLiabilityUserName(), signUserInfo.getLiabilityOfficialSealCode());
                isUploadSuc = isUploadSuc && isSuc;
                if (isSuc) { // upload img shanghsangqian Success
                    electronicSignatureService.updateSealStatus(signUserInfo.getId(), "liability");
                }
            }
            // 当注册 ca 上传公章都成功时 才能返回true ，才能进行下一步签名
            return isCaSuc && isRegAndCaSuc && isUploadSuc;
        } catch (Exception e) {
            LOGGER.error("判断bid：{}，的所有要签名用户或企业是否注册/申请证书异常：{}", bid, e);
        }
        return false;
    }

    /**
     * 申请企业证书
     */
    private JSONObject enterpriseCa(SignEnterpriseCaVO signEnterpriseCaVO) throws Exception {
        // 企业申请证书
        Constants.CA_TYPE caType = Constants.CA_TYPE.ZJCA;
        String name = signEnterpriseCaVO.getName(); // 广东品纳担保责任有限公司
        String password = signEnterpriseCaVO.getPassword();
        Constants.CERT_IDENT_TYPE identityType = Constants.CERT_IDENT_TYPE.PERSONAL_ID_CARD; // 身份证
        String icCode = signEnterpriseCaVO.getIcCode();  //注册号(企业用户必填)，如果为三证合一，填统一社会信用代码
        String linkMan = signEnterpriseCaVO.getLinkMan();
        String orgCode = ""; //组织机构代码(企业用户必填)
        String taxCode = ""; // 税务登记证号(企业用户必填)
        String province = signEnterpriseCaVO.getProvince();
        String city = signEnterpriseCaVO.getCity();
        String address = signEnterpriseCaVO.getAddress();
        String linkMobile = signEnterpriseCaVO.getLinkMobile(); //"130" + Utils.rand(10000000, 99999999); //
        String email = signEnterpriseCaVO.getEmail();//linkMobile + "@mobile.cn";
        String linkIdCode = signEnterpriseCaVO.getLinkIdCode(); // 518607195510227019
        JSONObject result = ShangshangqianUtil.certificateApplyEnterprise(caType, name, password, linkMan, linkMobile, email, address, province, city, linkIdCode, icCode, orgCode, taxCode);
        return result;
    }

    /**
     * 申请个人证书
     * @param signPersonCaVO
     */
    private JSONObject personCa(SignPersonCaVO signPersonCaVO) throws Exception {
        JSONObject caResultJson = ShangshangqianUtil.certificateApply(Constants.CA_TYPE.CFCA, signPersonCaVO.getName(), signPersonCaVO.getPassword(), signPersonCaVO.getLinkMobile(), signPersonCaVO.getEmail(), signPersonCaVO.getAddress(), signPersonCaVO.getProvince(), signPersonCaVO.getCity(), signPersonCaVO.getLinkIdCode());
        return caResultJson;
    }

    /**
     * 注册企业和申请企业证书
     * @param signRegUserVo
     * @param signEnterpriseCaVO
     */
    private boolean regUserAndEnterpriseCa(Integer userId, SignRegUserVO signRegUserVo, SignEnterpriseCaVO signEnterpriseCaVO) throws Exception {
        JSONObject regResultJson = ShangshangqianUtil.regUser(Constants.USER_TYPE.ENTERPRISE, signRegUserVo.getEmail(), signRegUserVo.getMobile(), signRegUserVo.getName());
        if (processRequestSuc(regResultJson) == 1) {
            LOGGER.info("用户email：{}, phone:{}(注册上上签成功）", signRegUserVo.getEmail(), signRegUserVo.getMobile());
            String pfUid = getRegUserId(regResultJson);
            signNormalBidHandler.saveRegThirdPartUser(userId, pfUid, signRegUserVo);
            JSONObject caResultJson = enterpriseCa(signEnterpriseCaVO);
            Boolean caResult = (boolean) caResultJson.get("isResult");
            if (caResult) {
                LOGGER.info("用户email：{}, phone:{}(申请申请证书成功）", signRegUserVo.getEmail(), signRegUserVo.getMobile());
                signNormalBidHandler.updateCaResult(signRegUserVo.getEmail(), 0, signEnterpriseCaVO.getPassword());
                return true;
            } else {
                LOGGER.error("企业申请证书失败 email:{}, 异常信息：{}", signRegUserVo.getEmail(), caResultJson.toJSONString());
            }
        } else {
            LOGGER.error("企业用户注册上上签失败参数：{} ：异常信息{}", JSON.toJSONString(signRegUserVo),  regResultJson.toJSONString());
        }
        return false;
    }

    /**
     * 注册个人和申请个人证书
     * @param signRegUserVo
     * @param signPersonCaVO
     */
    private boolean regUserAndPersonCa(Integer userId, SignRegUserVO signRegUserVo, SignPersonCaVO signPersonCaVO) throws Exception {
        JSONObject regResultJson = ShangshangqianUtil.regUser(Constants.USER_TYPE.PERSONAL, signRegUserVo.getEmail(), signRegUserVo.getMobile(), signRegUserVo.getName());
        if (processRequestSuc(regResultJson) == 1) {
            LOGGER.info("用户email：{}, phone:{}(注册上上签成功）", signRegUserVo.getEmail(), signRegUserVo.getMobile());
            String pfUid = getRegUserId(regResultJson);
            signNormalBidHandler.saveRegThirdPartUser(userId, pfUid, signRegUserVo);
            JSONObject caResultJson = personCa(signPersonCaVO);
            Boolean caResult = (boolean) caResultJson.get("isResult");
            if (caResult) {
                LOGGER.info("用户email：{}, phone:{}(申请申请证书成功）", signRegUserVo.getEmail(), signRegUserVo.getMobile());
                signNormalBidHandler.updateCaResult(signRegUserVo.getEmail(), 0, signPersonCaVO.getPassword());
                return true;
            } else {
                LOGGER.error("个人申请帧数异常email:{}, 异常信息：{}", signRegUserVo.getEmail(), caResultJson.toJSONString());
            }
        } else {
            LOGGER.error("个人用户注册上上签失败参数：{} ：异常信息{}", JSON.toJSONString(signRegUserVo),  regResultJson.toJSONString());
        }
        return false;
    }

    public String getRegUserId(JSONObject result){
        result =result.getJSONObject("response");
        result =result.getJSONObject("content");
        String pfUid = (String) result.get("uid");
        return pfUid;
    }

    private boolean uploadEnterpriseImage(String email, String phone, String name, String imageCode) throws Exception {
        SealImage sealImage = new SealImage();
        sealImage.setPhone(phone);
        sealImage.setUserAccount(email);
        sealImage.setSealCode(imageCode);
        sealImage.setUserName(name);
        JSONObject result = electronicSignatureService.uploadImage(sealImage);
        if (processRequestSuc(result) == 1) {
            return true;
        } else {
            LOGGER.error("上传企业公章失败email{} 异常信息：{}",email, result.toJSONString());
        }
        return false;
    }

    @Override
    public int addSignBid(Integer bid) {
        return signNormalBidDao.addSignBid(bid);
    }
}

