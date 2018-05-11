package com.fenlibao.p2p.service.xinwang.sign.handler;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.sign.SignNormalBidDao;
import com.fenlibao.p2p.model.xinwang.entity.sign.Investors;
import com.fenlibao.p2p.model.xinwang.entity.sign.SignNormalBidInfo;
import com.fenlibao.p2p.model.xinwang.entity.sign.SignUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.common.PathEnum;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignEnterpriseCaVO;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignPersonCaVO;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignRegUserVO;
import com.fenlibao.p2p.service.xinwang.sign.ElectronicSignatureService;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.api.load.ApiUtilConfig;
import com.fenlibao.p2p.util.api.pdf.PDFUtil;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author zeronx on 2017/12/14 9:41.
 * @version 1.0
 */

@Component
public class SignNormalBidHandler {

    private static final Logger LOGGER = LogManager.getLogger(SignNormalBidHandler.class);

    @Autowired
    private SignNormalBidDao signNormalBidDao;
    @Autowired
    private ElectronicSignatureService electronicSignatureService;

    public String writeInvestorIntoPdf(List<Investors> investors, String path) throws DocumentException, IOException {
        Document document = null;
        PdfWriter writer = null;
        try {
            String name = UUID.randomUUID().toString().replace("-", "").concat(".pdf");
            //创建文件
            document = new Document();
            //建立一个书写器
            writer = PdfWriter.getInstance(document, new FileOutputStream(path.concat(name)));
            //打开文件
            document.open();
            //中文字体,解决中文不能显示问题
            BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // title字体
            Font titleFont = new Font(bfChinese, 16, Font.BOLD);
            // title内容
            Paragraph titleParagraph = new Paragraph("出借人名单", titleFont);
            titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titleParagraph);
            // 添加分行
            document.add(new Paragraph("\n"));
            // 正文字体
            Font contentFont = new Font(bfChinese, 12, Font.NORMAL);
            // 正文
            // 8列的表.
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100); // 宽度100%填充
            table.setSpacingBefore(10f); // 前间距
            table.setSpacingAfter(10f); // 后间距
            List<PdfPRow> listRow = table.getRows();
            //设置每列宽
            float[] columnWidths = {0.5f, 0.5f, 1.0f};
            int cols = columnWidths.length;
            table.setWidths(columnWidths);
            int rows = investors.size();
//            int rows = (investors.size()%cols == 0 ? investors.size()/8 : investors.size()/cols + 1);
//            int lastRowCols = (investors.size()%cols == 0 ? cols : investors.size()%cols);
            addTableHeader(listRow, cols, contentFont);
            for (int i = 0; i < rows; i++) {
                PdfPCell cells[]= new PdfPCell[cols];
                PdfPRow row = new PdfPRow(cells);
//                for (int j = 0; j < cols; j++) {
                //单元格
                cells[0] = new PdfPCell(new Paragraph(investors.get(i).getName(), contentFont));//单元格内容
                cells[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中

                cells[1] = new PdfPCell(new Paragraph("身份证", contentFont));//单元格内容
                cells[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中

                cells[2] = new PdfPCell(new Paragraph(investors.get(i).getIdCard(), contentFont));//单元格内容
                cells[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
//                cells[j].setBorderColor(BaseColor.BLACK);//边框颜色
//                cells[j].setPaddingLeft(20);//左填充20
//                }
                //把第行添加到集合
                listRow.add(row);
            }
            //把表格添加到文件中
            document.add(table);
            return name;
        } finally {
            if (document != null) {
                //关闭文档
                document.close();
            }
            if (writer != null) {
                //关闭书写器
                writer.close();
            }
        }
    }

    private void addTableHeader(List<PdfPRow> listRow, int cols, Font contentFont) {
        PdfPCell cells[]= new PdfPCell[cols];
        PdfPRow row = new PdfPRow(cells);
//                for (int j = 0; j < cols; j++) {
        //单元格
        cells[0] = new PdfPCell(new Paragraph("姓名", contentFont));//单元格内容
        cells[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中

        cells[1] = new PdfPCell(new Paragraph("证件类型", contentFont));//单元格内容
        cells[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中

        cells[2] = new PdfPCell(new Paragraph("证件号码", contentFont));//单元格内容
        cells[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cells[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
//                cells[j].setBorderColor(BaseColor.BLACK);//边框颜色
//                cells[j].setPaddingLeft(20);//左填充20
//                }
        //把第行添加到集合
        listRow.add(row);
    }

    public Map<String,String> convertBorrowerUserToMap(final SignUserInfo signUserInfo) {
        Map<String, String> params = new HashMap<String, String>() {
            {
                put("email", signUserInfo.getBorrowerIdCard());
                put("userName", signUserInfo.getEnterpriseUserName());
                put("phone", signUserInfo.getEnterprisePhone());
            }
        };
        return params;
    }

    public Map<String,String> convertLiabilityUserToMap(final SignUserInfo signUserInfo) {
        Map<String, String> params = new HashMap<String, String>() {
            {
                if (signUserInfo.getLiabilityUserType() != null && signUserInfo.getLiabilityUserType() == 1) {
                    put("email", signUserInfo.getLiabilityIdCardNo());
                } else {
                    put("email", signUserInfo.getLiabilityUnifiedCode());
                }
                put("userName", signUserInfo.getLiabilityUserName());
                put("phone", signUserInfo.getLiabilityPhone());
            }
        };
        return params;
    }

    public SignPersonCaVO convetSignUserInfoToBorrowerPersonCaVO(SignUserInfo signUserInfo) {
        SignPersonCaVO signPersonCaVO = new SignPersonCaVO();
        signPersonCaVO.setName(signUserInfo.getEnterpriseUserName());
        signPersonCaVO.setPassword(getRandomPwd(8));
        signPersonCaVO.setLinkMobile(signUserInfo.getEnterprisePhone());
        signPersonCaVO.setEmail(signUserInfo.getBorrowerIdCard());
        signPersonCaVO.setProvince("广东省");
        signPersonCaVO.setCity("广州市");
        signPersonCaVO.setAddress("天河区");
        signPersonCaVO.setLinkIdCode(decodeIdCard(signUserInfo.getBorrowerIdCard()));
        return signPersonCaVO;
    }

    // 将 signUserInfo 转换证map 会不会好点 算了不转了
    public SignEnterpriseCaVO convetSignUserInfoToBorrowerEnterpriseCaVO(SignUserInfo signUserInfo) {
        SignEnterpriseCaVO signEnterpriseCaVO = new SignEnterpriseCaVO();
        signEnterpriseCaVO.setName(signUserInfo.getEnterpriseUserName());
        signEnterpriseCaVO.setPassword(getRandomPwd(8));
        signEnterpriseCaVO.setLinkMan(signUserInfo.getBorrowerLinkMan());
        signEnterpriseCaVO.setLinkMobile(signUserInfo.getEnterprisePhone());
        signEnterpriseCaVO.setEmail(signUserInfo.getBorrowerIdCard());
        signEnterpriseCaVO.setIcCode(signUserInfo.getBorrowerIdCard());
        signEnterpriseCaVO.setAddress("天河区");
        signEnterpriseCaVO.setProvince("广东省");
        signEnterpriseCaVO.setCity("广州市");
        signEnterpriseCaVO.setLinkIdCode(decodeIdCard(signUserInfo.getBorrowerLinkManIdCard()));
        signEnterpriseCaVO.setOrgCode("");
        signEnterpriseCaVO.setTaxCode("");
        return signEnterpriseCaVO;
    }

    public String decodeIdCard(String enIdCard) {
        String idCard = null;
        try {
            idCard = StringHelper.decode(enIdCard);
        } catch (Throwable throwable) {
            LOGGER.error("解码身份证{}出错:{}", enIdCard, throwable);
        }
        return idCard;
    }

    public String getRandomPwd(int length) {
        Random random = new Random();
        char[] codeSequence = "123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(String.valueOf(codeSequence[random.nextInt(codeSequence.length)]));
        }
        return stringBuffer.toString();
    }

    public SignPersonCaVO convetSignUserInfoToLiabilityPersonCaVO(SignUserInfo signUserInfo) {
        SignPersonCaVO signPersonCaVO = new SignPersonCaVO();
        signPersonCaVO.setName(signUserInfo.getLiabilityUserName());
        signPersonCaVO.setPassword(getRandomPwd(8));
        signPersonCaVO.setLinkMobile(signUserInfo.getLiabilityPhone());
        signPersonCaVO.setEmail(signUserInfo.getLiabilityIdCardNo());
        signPersonCaVO.setProvince("广东省");
        signPersonCaVO.setCity("广州市");
        signPersonCaVO.setAddress(signUserInfo.getLiabilityAddress());
        signPersonCaVO.setLinkIdCode(decodeIdCard(signUserInfo.getLiabilityIdCardNo()));
        return signPersonCaVO;
    }

    public SignEnterpriseCaVO convetSignUserInfoToLiabilityEnterpriseCaVO(SignUserInfo signUserInfo) {
        SignEnterpriseCaVO signEnterpriseCaVO = new SignEnterpriseCaVO();
        signEnterpriseCaVO.setName(signUserInfo.getLiabilityUserName());
        signEnterpriseCaVO.setPassword(getRandomPwd(8));
        signEnterpriseCaVO.setLinkMan(signUserInfo.getLiabilityJuridicalPerson());
        signEnterpriseCaVO.setLinkMobile(signUserInfo.getLiabilityPhone());
        signEnterpriseCaVO.setEmail(signUserInfo.getLiabilityUnifiedCode());
        signEnterpriseCaVO.setIcCode(signUserInfo.getLiabilityUnifiedCode());
        signEnterpriseCaVO.setAddress(signUserInfo.getLiabilityAddress());
        signEnterpriseCaVO.setProvince("广东省");
        signEnterpriseCaVO.setCity("广州市");
        signEnterpriseCaVO.setLinkIdCode(decodeIdCard(signUserInfo.getLiabilityIdCardNo()));
        signEnterpriseCaVO.setOrgCode("");
        signEnterpriseCaVO.setTaxCode("");
        return signEnterpriseCaVO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRegThirdPartUser(Integer userId, String pfUid, SignRegUserVO signRegUserVo) {
        signNormalBidDao.saveRegThirdPartUser(userId, pfUid, signRegUserVo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCaResult(String email, int caStatus, String password) {
        signNormalBidDao.updateCaResult(email, caStatus, password);
    }
    /**
     * 下载已经签名的协议文档
     * @param lockSignBidInfo
     * @param paramsMap
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean downloadSignBidAgreement(SignNormalBidInfo lockSignBidInfo, Map<String, String> paramsMap) throws Exception {
        int noSensitive = lockSignBidInfo.getNoSensitiveIsDownload();
        int sensitive = lockSignBidInfo.getSensitiveIsDownload();
        int noSensitiveClose = lockSignBidInfo.getNoSensitiveIsClose();
        int sensitiveClose = lockSignBidInfo.getSensitiveIsClose();
        SignUserInfo signUserInfo = signNormalBidDao.getSignUserInfoByBid(lockSignBidInfo.getBid());
        int positionPage = 18; // 需要将脱敏的文档有签章的那一页转为 图片 ， 三方 和 四方的页数不同
        boolean hasLiability = signUserInfo.getLiabilityUserType() == 2 ? false : true;
        if (hasLiability) {
            positionPage = 20;
        }
        String imageName = null;
        if (lockSignBidInfo.getNoSensitiveIsDownload() == 0) {
            try {
                if (noSensitiveClose == 0) {
                    noSensitiveClose = endContract(lockSignBidInfo.getNoSensitiveSignId());
                    paramsMap.put("noSensitiveIsClose", "" + noSensitiveClose);
                }
                if (noSensitiveClose == 1) {
                    byte result[] = ShangshangqianUtil.dowloadPdf(lockSignBidInfo.getNoSensitiveSignId());
                    boolean isSuc = FileUtils.saveFile(result, ApiUtilConfig.get("resources.server.path"), lockSignBidInfo.getNoSensitiveAgreement());
//                    byte[] result = ShangshangqianUtil.dowloadPdf(lockSignBidInfo.getNoSensitiveSignId());
//                    String tempPath = PathEnum.TEMP_PATH.getPath();
//                    byte[] newResult = PDFUtil.dealWithResultPdf(result, tempPath, positionPage);
////                    byte[] image = PDFUtil.convertPdfToImage(result);
////                    int index = lockSignBidInfo.getNoSensitiveAgreement().lastIndexOf(".");
////                    imageName = lockSignBidInfo.getNoSensitiveAgreement().substring(0, index).concat(".png");
//                    boolean isSuc = FileUtils.saveFile(newResult, ApiUtilConfig.get("resources.server.path"), lockSignBidInfo.getNoSensitiveAgreement());
                    if (isSuc) {
//                        electronicSignatureService.updateFileNameByBid(lockSignBidInfo.getBid(), imageName, null);
                        paramsMap.put("noSensitiveIsDownload", "1");
                        noSensitive = 1;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("下载签章脱敏文档失败：", e);
            }
        }
        if (lockSignBidInfo.getSensitiveIsDownload() == 0) {
            try {
                if (sensitiveClose == 0) {
                    sensitiveClose = endContract(lockSignBidInfo.getSensitiveSignId());
                    paramsMap.put("sensitiveIsClose", "" + sensitiveClose);
                }
                if (sensitiveClose == 1) {
                    byte result[] = ShangshangqianUtil.dowloadPdf(lockSignBidInfo.getSensitiveSignId());
                    boolean isSuc = FileUtils.saveFile(result, ApiUtilConfig.get("resources.server.path"), lockSignBidInfo.getSensitiveAgreement());
                    if (isSuc) {
                        paramsMap.put("sensitiveIsDownload", "1");
                        sensitive = 1;
                    }
                    // 转为图片
//                    byte result[] = ShangshangqianUtil.dowloadPdf(lockSignBidInfo.getSensitiveSignId());
//                    byte[] image = PDFUtil.convertPdfToImage(result);
//                    int index = lockSignBidInfo.getSensitiveAgreement().lastIndexOf(".");
//                    imageName = lockSignBidInfo.getSensitiveAgreement().substring(0, index).concat(".png");
//                    boolean isSuc = FileUtils.saveFile(image, ApiUtilConfig.get("resources.server.path"), imageName);
//                    if (isSuc) {
//                        electronicSignatureService.updateFileNameByBid(lockSignBidInfo.getBid(), null, imageName);
//                        paramsMap.put("sensitiveIsDownload", "1");
//                        sensitive = 1;
//                    }
                }
            } catch (Exception e) {
                LOGGER.error("下载签章已脱敏文档失败：", e);
            }
        }
        return (noSensitive == sensitive && sensitive == 1);
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
}
