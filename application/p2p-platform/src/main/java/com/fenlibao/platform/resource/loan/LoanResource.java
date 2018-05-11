package com.fenlibao.platform.resource.loan;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.util.AES;
import com.fenlibao.platform.common.util.AESForTP;
import com.fenlibao.platform.common.util.IPUtil;
import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.model.queqianme.*;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.CommonService;
import com.fenlibao.platform.service.queqianme.QueqianmeService;
import com.fenlibao.platform.util.file.HttpUpload;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("loan")
public class LoanResource extends ParentResource {

    @Inject
    private QueqianmeService queqianmeService;
    @Inject
    private CommonService commonService;

    private static Config config = ConfigFactory.create(Config.class);

    /**
     * 传输发标相关数据接口
     */
    @POST
    @Path("/info/upload")
    @Produces(MediaType.APPLICATION_JSON)
    public String addbidinfo(@Context HttpServletRequest request,
                             @FormParam("appid") String appid,
                             @FormParam("orderNum") String orderNum,
                             @FormParam("stageNum") String stageNum,
                             @FormParam("repaymentType") String repaymentType,
                             @FormParam("serialNum") String serialNum,
                             @FormParam("amount") String amount,
                             @FormParam("cycle") String cycle,
                             @FormParam("cycleType") String cycleType,
                             @FormParam("idNo") String idNo,
                             @FormParam("phoneNum") String phoneNum,
                             @FormParam("idName") String idName,
                             @FormParam("agreementSignId") String agreementSignId,
                             @FormParam("agreementDocId") String agreementDocId,
                             @FormParam("flbPageNum") String flbPageNum,
                             @FormParam("flbSignX") String flbSignX,
                             @FormParam("flbSignY") String flbSignY,
                             @FormParam("investorPageNum") String investorPageNum,
                             @FormParam("investorSignX") String investorSignX,
                             @FormParam("investorSignY") String investorSignY,
                             @FormParam("bankcardNo") String bankcardNo,
                             @FormParam("industry") String industry,
                             @FormParam("address") String address,
                             @FormParam("subjectNature") String subjectNature,
                             @FormParam("loanUsage") String loanUsage,
                             @FormParam("repaymentOrigin") String repaymentOrigin,
                             @FormParam("baseInfo") String baseInfo,
                             @FormParam("workInfo") String workInfo,
                             @FormParam("mutiplyBorrow") String mutiplyBorrow,
                             @FormParam("antiFraud") String antiFraud,
                             @FormParam("auditInfo") String auditInfo) throws Exception {
        Map<String, Object> response = success();
        Bidinfo bidinfo = null;
        BaseInfo baseEntity = null;//个人基础信息
        WorkInfo workEntity = null;//借款人工作信息
        MutiplyBorrow mutiplyEntity = null;//多头借贷
        AntiFraud antiFraudEntity = null;//反欺诈信息
        RiskMutiply riskMutiplyEntity = null;//审核信息
        Map<String,Object> userInfo = new HashMap<>();
        String key = commonService.getAesSecret(appid);
        try {

            bidinfo = new Bidinfo(orderNum, stageNum, serialNum, amount, cycle, cycleType, idNo, idName, phoneNum, repaymentType, agreementSignId, agreementDocId, flbPageNum, flbSignX, flbSignY, investorPageNum, investorSignX, investorSignY,bankcardNo,industry,address,subjectNature,loanUsage,repaymentOrigin);
            baseEntity = new BaseInfo(baseInfo);
            workEntity = new WorkInfo(workInfo);
            mutiplyEntity = new MutiplyBorrow(mutiplyBorrow);
            antiFraudEntity = new AntiFraud(antiFraud);
            riskMutiplyEntity = new RiskMutiply(auditInfo);
            //获取密钥redis,校验数据
             key = commonService.getAesSecret(appid);
            Response res = bidinfo.verifyBidinfo(key);
            if (res != Response.RESPONSE_SUCCESS) {
                //如果是暂不支持的还款类型，写日志
                if (res == Response.REPAYMENT_UNSUPPORT_ERROR) {
                    queqianmeService.writeLog(0, "自动发标数据采集", "拒绝暂不支持的还款方式[" + repaymentType + "],流水号[" + serialNum + "]", IPUtil.getIPAddress(request));
                }
                response = failure(res);
                logger.error("{}数据校验失败...", bidinfo.getSerialNum());
                return jackson(response);
            }

            res = baseEntity.verifyBaseInfo(key);
            if (res != Response.RESPONSE_SUCCESS) {
                response = failure(res);
                logger.error("{}基础信息有误...", bidinfo.getSerialNum());
                return jackson(response);
            }

            res = antiFraudEntity.verifyAntiFraud();
            if (res != Response.RESPONSE_SUCCESS) {
                response = failure(res);
                logger.error("{}反欺诈信息错误...", bidinfo.getSerialNum());
                return jackson(response);
            }
        } catch (Exception e) {
            response = failure(Response.SYSTEM_ERROR_PARAMETERS);
            logger.error("数据校验失败...");
            return jackson(response);
        }

        //校验是否开户
        try{
                userInfo.put("phoneNum",bidinfo.getPhoneNum());
                userInfo.put("userNo",bidinfo.getIdNo());
                userInfo.put("bankcardNo", bidinfo.getBankcardNo());
                userInfo.put("userName", bidinfo.getIdName());
                Integer businessUserInfo = commonService.getBusinessUser(userInfo);
                if(businessUserInfo<=0){
                    response = failure(Response.BUSINESS_NOTREGISTER_ERROR);
                    logger.error("{}用户还未委托开户...", bidinfo.getSerialNum());
                    return jackson(response);
            }
        }catch (Exception e){
            response = failure(Response.SYSTEM_ERROR_PARAMETERS);
            logger.error("开户校验失败...");
            return jackson(response);
        }

        //校验loan_id重复
        int checkSerialNum = queqianmeService.getSerialNumCount(bidinfo.getSerialNum());
        if (checkSerialNum > 0) {
            response = failure(Response.LOAN_SERIAL_NUM_EXIST);
            logger.error("serialNum[{}]重复...", bidinfo.getSerialNum());
            return jackson(response);
        }

        try {
            //插入数据表
            queqianmeService.addLoanInfo(bidinfo,workEntity,baseEntity,mutiplyEntity,antiFraudEntity,riskMutiplyEntity);
            logger.info("[{}]入库成功...", bidinfo.getSerialNum());
            return jackson(response);
        } catch (Exception e) {
            logger.info("[{}]入库失败...", bidinfo.getSerialNum());
            response = failure();
            return jackson(response);
        }
    }

    /**
     * 三方协议信息上传接口
     */
    @POST
    @Path("/tripleAgreement/upload")
    @Produces(MediaType.APPLICATION_JSON)
    public String addTripleAgreement(@Context HttpServletRequest request,
                                     @FormParam("serialNum") String serialNum,
                                     @FormParam("agreementSignId") String agreementSignId,
                                     @FormParam("agreementDocId") String agreementDocId,
                                     @FormParam("flbPageNum") String flbPageNum,
                                     @FormParam("flbSignX") String flbSignX,
                                     @FormParam("flbSignY") String flbSignY) throws Exception {

        Map<String, Object> response = success();
        TripleAgreementinfo tripleAgreementinfo = null;
        try {
            tripleAgreementinfo = new TripleAgreementinfo(serialNum, agreementSignId, agreementDocId, flbPageNum, flbSignX, flbSignY);
            Response res = tripleAgreementinfo.verifyTripleAgreementinfo();
            if (res != Response.RESPONSE_SUCCESS) {
                response = failure(res);
                logger.error("{}数据校验失败...", tripleAgreementinfo.getSerialNum());
                return jackson(response);
            }
        } catch (Exception e) {
            response = failure(Response.SYSTEM_ERROR_PARAMETERS);
            logger.error("数据校验失败...");
            return jackson(response);
        }

        //检查serialNum是否存在于主表
        int checkSerialNum = queqianmeService.getSerialNumCount(tripleAgreementinfo.getSerialNum());
        if (checkSerialNum <= 0) {
            response = failure(Response.LOAN_SERIAL_NUM_NOT_EXIST);
            logger.error("serialNum[{}]不存在于主表...", tripleAgreementinfo.getSerialNum());
            return jackson(response);
        }

        //检查serialNum是否已经上传过三方协议
        int checkSerialNumForTripleAgreement = queqianmeService.getSerialNumCountForTripleAgreement(tripleAgreementinfo.getSerialNum());
        if (checkSerialNumForTripleAgreement > 0) {
            response = failure(Response.LOAN_TRIPLE_AGREEMENT_EXIST);
            logger.error("serialNum[{}]对应第三方协议已存在...", tripleAgreementinfo.getSerialNum());
            return jackson(response);
        }

        try {
            //插入数据表
            queqianmeService.addTripleAgreementinfo(tripleAgreementinfo);
            logger.info("[{}]对应第三方协议入库成功...", tripleAgreementinfo.getSerialNum());
            return jackson(response);
        } catch (Exception e) {
            logger.info("[{}]对应第三方协议入库失败...", tripleAgreementinfo.getSerialNum());
            response = failure();
            return jackson(response);
        }
    }


    /**
     * 传输发标相关文件接口
     */
    @POST
    @Path("/agreement/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addextrainfo(@Context HttpServletRequest request) throws Exception {
        Map<String, Object> response = success();
        Bidextralinfo bidextralinfo = null;
        String loanSerialNum = "";
        String serialNum = "";
        String type = "";
        FileItem file = null;

        try {
            Map<String, Object> parmMap = getFeilds(request);
            loanSerialNum = (String) parmMap.get("loanSerialNum");
            serialNum = (String) parmMap.get("serialNum");
            type = (String) parmMap.get("type");
            file = (FileItem) parmMap.get("file");

            bidextralinfo = new Bidextralinfo(loanSerialNum, serialNum, type);
            //校验数据
            Response res = bidextralinfo.verify();
            if (res != Response.RESPONSE_SUCCESS) {
                response = failure(res);
                logger.error("{}数据校验失败...", bidextralinfo.getSerialNum());
                return jackson(response);
            }
        } catch (Exception e) {
            response = failure(Response.SYSTEM_ERROR_PARAMETERS);
            logger.error("数据校验失败...");
            return jackson(response);
        }


        //校验loanSerialNum是否存在
        int checkSerialNum = queqianmeService.getSerialNumCount(loanSerialNum);
        if (checkSerialNum <= 0) {
            response = failure(Response.LOAN_SERIAL_NUM_NOT_EXIST);
            logger.error("对应标数据loanSerialNum[{}]不存在...", loanSerialNum);
            return jackson(response);
        }

        //校验协议文件唯一流水号是否重复
        int checkFileSerialNum = queqianmeService.getFileSerialNum(serialNum);
        if (checkFileSerialNum > 0) {
            response = failure(Response.LOAN_FILE_SERIAL_NUM_EXIST);
            logger.error("SerialNum[{}]重复...", serialNum);
            return jackson(response);
        }

        String filePath = "";
        try {
            filePath = saveFile(loanSerialNum, type, file);
        } catch (Exception e) {
            if ("file size too big".equals(e.getMessage())) {
                //太大
                response = failure(Response.LOAN_FILE_SIZE_ERROR);
                return jackson(response);
            } else if ("file type forbidden".equals(e.getMessage())) {
                //类型不对
            }
        }

        try {
            if (StringUtils.isNotBlank(filePath)) {
                bidextralinfo.setFilePath(filePath);
                queqianmeService.addextrainfo(bidextralinfo);
                logger.info("附件[{}]入库成功...", serialNum);
                return jackson(response);
            } else {
                response = failure(Response.LOAN_FILE_UPLOAD_ERROR);
                logger.error("文件上传失败");
                return jackson(response);
            }
        } catch (Exception e) {
            response = failure();
            logger.error("附件[{}]入库失败...", serialNum);
            return jackson(response);
        }
    }

    /**
     * 获取参数
     *
     * @param request
     * @return
     * @throws Exception
     */
    private Map<String, Object> getFeilds(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ServletFileUpload.isMultipartContent(request)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = null;
            try {
                items = upload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            if (items != null) {
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        //解决普通输入项的数据的中文乱码问题
                        String value = item.getString("UTF-8");
                        map.put(name, value);
                    }
                    if (!item.isFormField() && item.getSize() > 0) {
                        String name = item.getFieldName();
                        map.put(name, item);
                    }
                }
            }
        }
        return map;
    }


    /**
     * 保存文件
     *
     * @param
     * @return
     * @throws Exception
     */
    private String saveFile(String loanSerialNum, String type, FileItem item) throws Exception {
        String fileUrl = "";

        if (!checkFileSize(item)) {
            throw new Exception("file size too big");
        }
        if (!checkFileType(item)) {
            throw new Exception("file type forbidden");
        }
        String fileName = processFileName(loanSerialNum, type, item.getName());
        String filePath = processFilePath();
        try {
            fileUrl = config.getLoanResourcesPath() + config.getLoanExtraInfoPath() + filePath + fileName;
            HttpUpload httpUpload = new HttpUpload(config.getLoanResourcesPath(), config.getLoanExtraInfoPath() + filePath + fileName);
            httpUpload.upladFile(item.getInputStream());
            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }


    /**
     * 生成文件名称
     *
     * @return
     */
    private String processFileName(String loanSerialNum, String type, String fileNameInput) {
        String suffix = fileNameInput.substring(fileNameInput.lastIndexOf("."), fileNameInput.length());
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String curDate = s.format(c.getTime());
        String fileNameOutput = loanSerialNum + "-" + type + "-" + curDate + suffix;
        return fileNameOutput;
    }

    /**
     * 生成文件路径
     *
     * @param
     * @return
     */
    private String processFilePath() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        int year = c.get(Calendar.YEAR);    //获取年
        int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        return "/" + year + "/" + month + "/" + day + "/";
    }

    /**
     * 检测文件大小
     *
     * @param item
     */
    private boolean checkFileSize(FileItem item) {
        long size = item.getSize();
        // 检测大小
        long maxFileSize = Integer.parseInt(config.getLoanExtraFileSize()) * 1024 * 1024;
        if (maxFileSize == 0 || size <= maxFileSize) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测文件类型
     *
     * @param item
     */
    private boolean checkFileType(FileItem item) {
        String size = item.getContentType();
        return true;
    }
}
