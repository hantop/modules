package com.fenlibao.p2p.service.xinwang.sign.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.project.SysBidManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.api.enums.ConstEnum;
import com.fenlibao.p2p.model.api.vo.PdfPosition;
import com.fenlibao.p2p.model.xinwang.bo.InvestorBO;
import com.fenlibao.p2p.model.xinwang.bo.PdfParamBO;
import com.fenlibao.p2p.model.xinwang.consts.SignConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysVariableConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.OrganizationBaseInfo;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.bid.TBidExtUser;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;
import com.fenlibao.p2p.model.xinwang.enums.account.XWUserType;
import com.fenlibao.p2p.model.xinwang.enums.common.PathEnum;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentWay;
import com.fenlibao.p2p.model.xinwang.enums.sign.AgreementStage;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnterpriseService;
import com.fenlibao.p2p.service.xinwang.sign.AgreementService;
import com.fenlibao.p2p.service.xinwang.sign.ElectronicSignatureService;
import com.fenlibao.p2p.service.xinwang.sign.SignNormalBidService;
import com.fenlibao.p2p.util.api.Amount2RMB;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.load.ApiUtilConfig;
import com.fenlibao.p2p.util.api.pdf.EditPDFDetails;
import com.fenlibao.p2p.util.api.pdf.PDFUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 签章合同文件
 */
@Service
public class AgreementServiceImpl implements AgreementService {
    private static final Logger logger = LogManager.getLogger(AgreementService.class);
    @Resource
    ElectronicSignatureService signatureService;
    @Resource
    XWEnterpriseService enterpriseService;
    @Resource
    SignNormalBidService signNormalBidService;
    @Resource
    PTCommonDao ptCommonDao;
    @Resource
    XWProjectDao projectDao;
    @Resource
    XWAccountDao accountDao;
    @Resource
    SysBidManageDao bidManageDao;

    @Override
    public void createAgreement(ElectronicSignature electronicSignature) throws Exception {
        // 组装Param参数
        PdfParamBO paramBO = this.buildParam(electronicSignature);
        // 用是否有担保人来选择 三方合同 或者 四方合同 add 2018-03-06 18:00
        boolean hasLiability = electronicSignature.getLiabilityUserType() == 2 ? false : true;
        // 组装map
        Map<Integer, List<EditPDFDetails>> writeMap1 = this.buildWriteMap(paramBO,false, hasLiability);
        Map<Integer, List<EditPDFDetails>> writeMap2 = this.buildWriteMap(paramBO,true, hasLiability);
        // 获取签名文件的模板
        byte[] byteArray;
        if (hasLiability) { // 四方
            byteArray = com.fenlibao.p2p.util.api.file.FileUtils.downloadFile(ApiUtilConfig.get("resources.server.path") + "/Agreement4.pdf");
        } else {  // 三方
            byteArray = com.fenlibao.p2p.util.api.file.FileUtils.downloadFile(ApiUtilConfig.get("resources.server.path") + "/Agreement3.pdf");
        }
        // 写入模板合同
        String fileName1 = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName2 = UUID.randomUUID().toString().replaceAll("-", "");
        String sealCode1 = com.fenlibao.p2p.util.api.file.FileUtils.getSealCode(fileName1, "pdf", ConstEnum.PDF.getCode());
        String sealCode2 = com.fenlibao.p2p.util.api.file.FileUtils.getSealCode(fileName2, "pdf", ConstEnum.PDF.getCode());
        byte[] bytes1 = PDFUtil.editPDF(byteArray, writeMap1);
        byte[] bytes2 = PDFUtil.editPDF(byteArray, writeMap2);
        // 上传文件服务器
        boolean result1 = com.fenlibao.p2p.util.api.file.FileUtils.saveFile(bytes1, ApiUtilConfig.get("resources.server.path"), sealCode1);
        boolean result2 = com.fenlibao.p2p.util.api.file.FileUtils.saveFile(bytes2, ApiUtilConfig.get("resources.server.path"), sealCode2);
        if (result1 && result2) {
            electronicSignature.setNoSensitiveAgreement(sealCode1);
            electronicSignature.setSensitiveAgreement(sealCode2);
            signatureService.makeAgreement(electronicSignature, AgreementStage.KQM);
        }
    }

    private PdfParamBO buildParam(ElectronicSignature electronicSignature) throws Exception {
        // 生成文件
        /** 一共需要填写10页 */
        XWProjectExtraInfo extraInfo = projectDao.getProjectExtraInfo(electronicSignature.getBid());
        XWProjectInfo projectInfo = projectDao.getProjectInfoById(electronicSignature.getBid());
        XinwangAccount userAccount = accountDao.getXinwangAccount(projectInfo.getBorrowerPlatformUserNo());
        PlatformAccount platformAccount = accountDao.getPlatformAccountInfoByPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
        TBidExtUser bidExtUser = bidManageDao.getTBidExtUser(electronicSignature.getBid());
        Map<String, BigDecimal> bidOtherRateMap = bidManageDao.getBidOtherRateByBid(electronicSignature.getBid());
        PdfParamBO paramBO = new PdfParamBO();
        /** 第1页 */
        paramBO.setParam0(extraInfo.getAgreementNo());
        paramBO.setParam1(bidExtUser.getRealName());
        paramBO.setParam2("身份证号");
        paramBO.setParam3(StringUtils.isEmpty(bidExtUser.getRealCreditCardNum()) ? "" : StringHelper.decode(bidExtUser.getRealCreditCardNum()));//身份证
        paramBO.setParam4(platformAccount.getUserName());
        if (userAccount.getUserType().equals(XWUserType.ORGANIZATION)) {
            //企业借款人
            OrganizationBaseInfo organizationBaseInfo = enterpriseService.getOrganizationBaseInfo(projectInfo.getBorrowerUserId());
            paramBO.setParam1(organizationBaseInfo.getOrganizationName());
            paramBO.setParam2("注册号/统一信用代码");
            paramBO.setParam3(StringUtils.isEmpty(organizationBaseInfo.getRegisterNo()) ? "" : organizationBaseInfo.getRegisterNo());//身份证
            paramBO.setParam4(platformAccount.getUserName());
        }
        /* 分利宝信息*/
        paramBO.setParam5("莫劲云");
        paramBO.setParam6("注册号/统一信用代码");
        paramBO.setParam7("91440101340114469W");

        if (electronicSignature.getLiabilityUserType() != 2) {
            /* 连带担保人信息*/
            paramBO.setParam8(electronicSignature.getLiabilityUserName());
            paramBO.setParam9(StringUtils.isEmpty(electronicSignature.getLiabilityUserName()) ? null : "注册号/统一信用代码");
            paramBO.setParam10(electronicSignature.getLiabilityUnifiedCode());
            if (Integer.valueOf(1).equals(electronicSignature.getLiabilityUserType())) {
                //个人连带担保人
                paramBO.setParam9("身份证号");
                paramBO.setParam10(StringHelper.decode(electronicSignature.getLiabilityIdCardNo()));
            }
        }
        /* 一般担保人 */
        //paramBO.setParam8(electronicSignature.getNormalUserName());
        //paramBO.setParam9("注册号/统一信用代码");
        //paramBO.setParam10(electronicSignature.getNormalUnifiedCode());
        /* 第4页 */
        // 项目名称 和 项目编号 为 projectInfo.getProjectName() 的文字 和 数字部分
        int subPosition = getSubPosition(projectInfo.getProjectName());
        paramBO.setParam11(projectInfo.getProjectName().substring(0, subPosition)); //
        paramBO.setParam12(projectInfo.getProjectName().substring(subPosition)); // extraInfo.getAgreementNo()
        paramBO.setParam13(projectInfo.getProjectAmount().toString());
        paramBO.setParam14(Amount2RMB.convert(projectInfo.getProjectAmount().toString()));
        paramBO.setParam15(projectInfo.getAnnnualInterestRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP).toString());
        // 服务费率
        BigDecimal param16 = bidOtherRateMap.get("orderRate").divide(new BigDecimal(projectInfo.getMonthProjectPeriod()), 8, RoundingMode.HALF_UP).multiply(new BigDecimal(12)).multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP);
        paramBO.setParam16(param16.toString()); // 服务费率
        // // 借款人综合资金成本利率
        BigDecimal param17 = bidOtherRateMap.get("borrowerRate").multiply(new BigDecimal(100)).setScale(2,RoundingMode.HALF_UP);
        paramBO.setParam17(param17.toString());
        // 借款本息总金额 = 本金 + 本金 * (借款利率/12) * 借款期数
        BigDecimal param18 = projectInfo.getProjectAmount().add(projectInfo.getProjectAmount().multiply(projectInfo.getAnnnualInterestRate().divide(new BigDecimal(12), 8, RoundingMode.HALF_UP)).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod())).setScale(2,RoundingMode.HALF_UP));
        paramBO.setParam18(param18.toString());
        // 服务费总金额 = 本金 * (服务费率/12) * 借款期数
        String param19 = projectInfo.getProjectAmount().multiply(bidOtherRateMap.get("orderRate").divide(new BigDecimal(12), 9, RoundingMode.HALF_UP)).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod())).setScale(2,RoundingMode.HALF_UP).toString();
        paramBO.setParam19(param19); // 服务费总金额
        paramBO.setParam30(projectInfo.getRepaymentWay().getName());
        paramBO.setParam31(projectInfo.getMonthProjectPeriod().toString());
        paramBO.setParam33(extraInfo.getUseFor());
        paramBO.setParam34(platformAccount.getUserName());
        /* 还款方式决定填写内容 */
        if (RepaymentWay.DEBX.equals(projectInfo.getRepaymentWay())) {
            BigDecimal monthPayTotal = this.debx(projectInfo.getProjectAmount(), projectInfo.getAnnnualInterestRate().setScale(SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                    SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP), projectInfo.getMonthProjectPeriod());
            paramBO.setParam35(monthPayTotal.setScale(2, RoundingMode.HALF_UP).toString());
            // 每月应还服务费 = 借款本金总额 × 服务费率/12
            BigDecimal param36 =  projectInfo.getProjectAmount().multiply(param16.divide(new BigDecimal(100))).divide(new BigDecimal(12), 8, RoundingMode.HALF_UP).setScale(2,RoundingMode.HALF_UP);
            paramBO.setParam36(param36.toString());
        } else if (RepaymentWay.MYFX.equals(projectInfo.getRepaymentWay())) {
            BigDecimal monthPayTotal = projectInfo.getProjectAmount().multiply(projectInfo.getAnnnualInterestRate()).divide(new BigDecimal(12), 2, BigDecimal.ROUND_DOWN);
            paramBO.setParam37(monthPayTotal.toString());
            // 每月应还服务费 = 借款本金总额 × 服务费率/12
            BigDecimal param38 =  projectInfo.getProjectAmount().multiply(param16.divide(new BigDecimal(100))).divide(new BigDecimal(12), 8, RoundingMode.HALF_UP).setScale(2,RoundingMode.HALF_UP);
            paramBO.setParam38(param38.toString());
        } else if (RepaymentWay.YCFQ.equals(projectInfo.getRepaymentWay())) {
            BigDecimal totalPay = projectInfo.getProjectAmount().add(projectInfo.getProjectAmount().multiply(projectInfo.getAnnnualInterestRate())
                    .divide(new BigDecimal(12), 2, BigDecimal.ROUND_DOWN)
                    .multiply(new BigDecimal(projectInfo.getMonthProjectPeriod())).setScale(2, RoundingMode.HALF_UP));
            paramBO.setParam39(totalPay.toString());
            //到期应还服务费 = 借款本金总额 × 服务费率 /12 × 借款期数
            BigDecimal param40 =  projectInfo.getProjectAmount().multiply(param16.divide(new BigDecimal(100))).divide(new BigDecimal(12), 8, RoundingMode.HALF_UP).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod())).setScale(2,RoundingMode.HALF_UP);
            paramBO.setParam40(param40.toString());
        }
        // 借款本金 + 借款本金 * （24% / 12） * 借款期数
        String param41 = projectInfo.getProjectAmount().add(projectInfo.getProjectAmount().multiply(new BigDecimal(0.24).divide(new BigDecimal(12), 8, RoundingMode.HALF_UP)).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod())).setScale(2,RoundingMode.HALF_UP)).toString();
        paramBO.setParam41(param41);
        //邮寄地址 电话号码
        paramBO.setParam42(bidExtUser.getAddress());
        paramBO.setParam44(bidExtUser.getRealPhone());

        return paramBO;
    }

    private int getSubPosition(String projectName) {
        int count = 0;
        for (char c : projectName.toCharArray()) {
            int kk = 0;
            try {
                kk = String.valueOf(c).getBytes("GBK").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (kk >= 2) {
                count ++;
            } else {
                break;
            }
        }
        return count;
    }

    private BigDecimal debx(BigDecimal total, BigDecimal monthRate, int terms) {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
        return total.multiply(monthRate).multiply(tmp).divide(tmp.subtract(new BigDecimal(1)), 2,
                BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取需要拼写的map
     * @param paramBO
     * @return
     * @throws Exception
     */
    private Map<Integer, List<EditPDFDetails>> buildWriteMap(PdfParamBO paramBO,boolean atStar, boolean hasLiability) throws Exception {
        String str;
        if (hasLiability) { // 获取四方合同 json
            str = ptCommonDao.getSystemVariable(SysVariableConsts.AGREEMENT_POSITION_FOR_FOURTH);
        } else { // 获取三方合同坐标 json
            str = ptCommonDao.getSystemVariable(SysVariableConsts.AGREEMENT_POSITION_FOR_THREE);
        }
        Map<String, JSONObject> params = JSON.parseObject(str, Map.class);
        //2 填写文件两份
        Map<Integer, List<EditPDFDetails>> sensitiveMap = getAgreementEditPage(hasLiability); // new HashMap<>();
        Class clazz = PdfParamBO.class;
        Method method ;
        int k = 0;
        for (int i = 0; i < 52; i++) {
            /** 填写内容 */
            method = clazz.getMethod("getParam" + i);
            String content = (String) method.invoke(paramBO);
            if (StringUtils.isEmpty(content)) {
                continue;
            }
            if (hasLiability) { // 四方合同
                this.write2Map(i, content, params, sensitiveMap,atStar);
            } else { // 三方合同
                k = i;
                // 说明：因为在三个合同和四方合同中唯一不同的就是 三方合同缺少连带担保人，也就是缺乏三个字段信息,
                // 为了能直接重用 buildParam()方法的代码，将其大于等于 8（param8 --> param11） 的位置字段信息都往后移了3，所以在这里要减去3
                if (i >= 8) {
                    k = i - 3;
                }
                this.write2Map(k, content, params, sensitiveMap,atStar);
            }
        }
        return sensitiveMap;
    }

    private Map<Integer,List<EditPDFDetails>> getAgreementEditPage(boolean hasLiability) {
        Map<Integer,List<EditPDFDetails>> sensitiveMap = new HashMap<>();
        // 默认认为 总共协议有 40页,每页都可能需要填写, 如过超过继续增大数字即可
        for (int i = 0; i <= 40; i++) {
            sensitiveMap.put(i, new ArrayList<EditPDFDetails>());
        }
        return sensitiveMap;
    }

    /**
     * 将每个元素填写到map
     * @param i
     * @param content
     * @param params
     * @param sensitiveMap
     * @throws Exception
     */
    private void write2Map(int i, String content, Map<String, JSONObject> params, Map<Integer, List<EditPDFDetails>> sensitiveMap,boolean atStar) throws Exception {
        JSONObject jsonObject = params.get("param" + i);
        PdfPosition pdfPosition = JSONObject.parseObject(jsonObject.toJSONString(), PdfPosition.class);

        EditPDFDetails editPDFDetails = new EditPDFDetails();
        editPDFDetails.setX(pdfPosition.getSignX());
        editPDFDetails.setY(pdfPosition.getSignY());

        if (atStar) {
            if ((i == 1 && content.length() < 6) || (i == 8 && content.length() < 4)) {
                pdfPosition.setStarType(1);
            }
            content = this.addStar(content, pdfPosition.getStarType());
        }
        editPDFDetails.setContents(content);
        List<EditPDFDetails> list = sensitiveMap.get(pdfPosition.getPage());
        list.add(editPDFDetails);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public File[] fillAgreement(ElectronicSignature electronicSignature) throws Exception {
        //合同文件保存的文件服务器路径
        String servicePath = ApiUtilConfig.get("resources.server.path");
        // 用是否有担保人来选择 三方合同 或者 四方合同 add 2018-03-06 18:00
        boolean hasLiability = electronicSignature.getLiabilityUserType() == 2 ? false : true;
        //修改合同文件状态
        int result = signatureService.updateAgreementStage(electronicSignature.getId(), AgreementStage.YQM, AgreementStage.KQM);
        if (result == 1) {
            //填写放款时间
            String str;
            if (hasLiability) { // 获取四方合同 json
                str = ptCommonDao.getSystemVariable(SysVariableConsts.AGREEMENT_POSITION_FOR_FOURTH);
            } else { // 获取三方合同坐标 json
                str = ptCommonDao.getSystemVariable(SysVariableConsts.AGREEMENT_POSITION_FOR_THREE);
            }
            XWProjectExtraInfo extraInfo = projectDao.getProjectExtraInfo(electronicSignature.getBid());
            PdfParamBO paramBO = new PdfParamBO();
            if (extraInfo.getBearInterestDate() != null && extraInfo.getEndDate() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(extraInfo.getBearInterestDate());
                paramBO.setParam20(calendar.get(Calendar.YEAR) + "");
                paramBO.setParam21(calendar.get(Calendar.MONTH) + 1 + "");
                paramBO.setParam22(calendar.get(Calendar.DATE) + "");
                paramBO.setParam23(DateUtil.daysOfTwo(extraInfo.getBearInterestDate(),extraInfo.getEndDate())+"");
                paramBO.setParam24(calendar.get(Calendar.YEAR) + "");
                paramBO.setParam25(calendar.get(Calendar.MONTH) + 1 + "");
                paramBO.setParam26(calendar.get(Calendar.DATE) + "");
                calendar.setTime(extraInfo.getEndDate());
                paramBO.setParam27(calendar.get(Calendar.YEAR) + "");
                paramBO.setParam28(calendar.get(Calendar.MONTH) + 1 + "");
                paramBO.setParam29(calendar.get(Calendar.DATE) + "");
                paramBO.setParam32(calendar.get(Calendar.DATE) + "");
            }

            //填写文件
            Map<Integer, List<EditPDFDetails>> sensitiveMap = new HashMap<>();
            List<EditPDFDetails> list4 = new ArrayList<>();
            sensitiveMap.put(4, list4);
            Map<String, JSONObject> params = JSON.parseObject(str, Map.class);
            Class clazz = PdfParamBO.class;
            Method method;
            int k ;
            for (int i = 0; i < 52; i++) {
                /** 填写内容 */
                method = clazz.getMethod("getParam" + i);
                String content = (String) method.invoke(paramBO);
                if (StringUtils.isEmpty(content)) {
                    continue;
                }
                if (hasLiability) { // 四方合同
                    this.write2Map(i, content, params, sensitiveMap,false);
                } else { // 三方合同
                    k = i;
                    // 说明：因为在三个合同和四方合同中唯一不同的就是 三方合同缺少连带担保人，也就是缺乏三个字段信息,
                    // 为了能直接重用 buildParam()方法的代码，将其大于等于 8（param8 --> param11） 的位置字段信息都往后移了3，所以在这里要减去3
                    if (i >= 8) {
                        k = i - 3;
                    }
                    this.write2Map(k, content, params, sensitiveMap,false);
                }
            }
            //完整
            byte[] sourceBytes1 = com.fenlibao.p2p.util.api.file.FileUtils.downloadFile(electronicSignature.getSensitiveAgreement(), servicePath);
            //脱敏
            byte[] sourceBytes2 = com.fenlibao.p2p.util.api.file.FileUtils.downloadFile(electronicSignature.getNoSensitiveAgreement(), servicePath);
            byte[] bytes1 = PDFUtil.editPDF(sourceBytes1, sensitiveMap);
            byte[] bytes2 = PDFUtil.editPDF(sourceBytes2, sensitiveMap);
            //添加投资人列表
            //投资人账号 t6110.F02;真实姓名;身份证;投资金额
            Map<String, Object> tenderRecordParams = new HashMap<>(3);
            tenderRecordParams.put("projectNo", electronicSignature.getBid());
            List<XWTenderRecord> tenderRecordList = projectDao.getTenderRecord(tenderRecordParams);
            Map<Integer, InvestorBO> investorBOMap1 = new HashMap<>();
            Map<Integer, InvestorBO> investorBOMap2 = new HashMap<>();
            for (XWTenderRecord tenderRecord : tenderRecordList) {
                if (!investorBOMap1.containsKey(tenderRecord.getInvestorId())) {
                    //未脱敏
                    InvestorBO bo1 = projectDao.getInvestorBO(tenderRecord.getInvestorId());
                    String cardNo = StringHelper.decode(bo1.getIdCardNo());
                    bo1.setIdCardNo(cardNo);
                    bo1.setAmount(tenderRecord.getAmount());
                    investorBOMap1.put(tenderRecord.getInvestorId(), bo1);
                    //脱敏
                    InvestorBO bo2 = new InvestorBO(bo1.getUserId(), StringHelper.recurseReplace(4,7,bo1.getUserName(),"*"), StringHelper.getNoSensitiveName(bo1.getRealName()), StringHelper.recurseReplace(3,16,bo1.getIdCardNo(),"*"), bo1.getAmount());
                    investorBOMap2.put(tenderRecord.getInvestorId(), bo2);
                    continue;
                }
                InvestorBO bo1 = investorBOMap1.get(tenderRecord.getInvestorId());
                bo1.setAmount(tenderRecord.getAmount().add(bo1.getAmount()));
                InvestorBO bo2 = investorBOMap2.get(tenderRecord.getInvestorId());
                bo2.setAmount(tenderRecord.getAmount().add(bo2.getAmount()));
            }
            //创建投资人列表和借款信息
            //会产生临时文件，放在/var/tmp路径下
            File tail1 = new File(PathEnum.TEMP_PATH.getPath() + UUID.randomUUID().toString());
            File tail2 = new File(PathEnum.TEMP_PATH.getPath() + UUID.randomUUID().toString());
            signatureService.createInvestorsTable(investorBOMap1, tail1);
            signatureService.createInvestorsTable(investorBOMap2, tail2);
            File dest1 = new File(UUID.randomUUID().toString().replaceAll("-", "") + ".pdf");
            File dest2 = new File(UUID.randomUUID().toString().replaceAll("-", "") + ".pdf");
            // 需要投资人签名的空白页面（包含原始文档空白那页在内）
            int needAddPages = investorBOMap1.size() % SignConsts.SIGN_INVESTOR_NUMS == 0 ? investorBOMap1.size() / SignConsts.SIGN_INVESTOR_NUMS : investorBOMap1.size() / SignConsts.SIGN_INVESTOR_NUMS + 1;
            int removePageNum; // 需要移除的该页之后的页的开始位置（包含）
            if (hasLiability) {
                removePageNum = SignConsts.DELETE_AGREEMENT4_PAGE_POSITION ; // 需要移除的该页之后的页的开始位置（包含）
            } else {
                removePageNum = SignConsts.DELETE_AGREEMENT3_PAGE_POSITION ; // 需要移除的该页之后的页的开始位置（包含）
            }

            byte[] newBytes1 = PDFUtil.addInvestorsSignPage(bytes1, needAddPages - 1, removePageNum);
            byte[] newBytes2 = PDFUtil.addInvestorsSignPage(bytes2, needAddPages - 1, removePageNum);
            //合并文件
            if (hasLiability) { // 四方
                byte[] byteArray = com.fenlibao.p2p.util.api.file.FileUtils.downloadFile(ApiUtilConfig.get("resources.server.path") + "/SignPage4.pdf");
                PDFUtil.mergePdfFiles(dest1, newBytes1, removePageNum + needAddPages - 1, byteArray, FileUtils.readFileToByteArray(tail1));
                PDFUtil.mergePdfFiles(dest2, newBytes2, removePageNum + needAddPages - 1, byteArray, FileUtils.readFileToByteArray(tail2));
            } else { // 三方
                byte[] byteArray = com.fenlibao.p2p.util.api.file.FileUtils.downloadFile(ApiUtilConfig.get("resources.server.path") + "/SignPage3.pdf");
                PDFUtil.mergePdfFiles(dest1, newBytes1, removePageNum + needAddPages - 1, byteArray, FileUtils.readFileToByteArray(tail1));
                PDFUtil.mergePdfFiles(dest2, newBytes2, removePageNum + needAddPages - 1, byteArray, FileUtils.readFileToByteArray(tail2));
            }

            String sealCode1 = com.fenlibao.p2p.util.api.file.FileUtils.saveFile(dest1, servicePath, ConstEnum.PDF.getCode());
            String sealCode2 = com.fenlibao.p2p.util.api.file.FileUtils.saveFile(dest2, servicePath, ConstEnum.PDF.getCode());
            int res1 = signatureService.updateFileName(electronicSignature.getId(), sealCode1, sealCode2);
            int res2 = signNormalBidService.addSignBid(electronicSignature.getBid());
            if (res1 != 1 || res2 != 1) {
                throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
            }
            return new File[]{tail1, tail2, dest1, dest2};
        }
        return null;
    }

    /**
     * 替换星星，含业务逻辑
     * @param content
     * @param starType
     * @return
     */
    private String addStar(String content, int starType) {
        int end = content.length();
        switch (starType) {
            case 0:
                return content;
            case 1:
                return StringHelper.recurseReplace(1,end,content,"*");
            case 2:
                return StringHelper.recurseReplace(2, end - 2, content, "*");
            case 3:
                return StringHelper.recurseReplace(3, end - 4, content, "*");
            case 4:
                return StringHelper.recurseReplace(4, end - 4, content, "*");
            case 9:
                return StringHelper.recurseReplace(9, end, content, "*");
            default:
                return null;
        }
    }
}
