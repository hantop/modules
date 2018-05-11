package com.fenlibao.p2p.service.xinwang.sign.impl;


import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.sign.ElectronicSignatureDao;
import com.fenlibao.p2p.model.xinwang.bo.InvestorBO;
import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;
import com.fenlibao.p2p.model.xinwang.entity.sign.SealImage;
import com.fenlibao.p2p.model.xinwang.entity.sign.UploadImage;
import com.fenlibao.p2p.model.xinwang.enums.sign.AgreementStage;
import com.fenlibao.p2p.service.xinwang.sign.ElectronicSignatureService;
import com.fenlibao.p2p.util.api.file.FileUtils;
import com.fenlibao.p2p.util.api.load.ApiUtilConfig;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 针对存管版的电子签章
 */
@Service
public class ElectronicSignatureServiceImpl implements ElectronicSignatureService {
    private static final Logger logger = LogManager.getLogger(ElectronicSignatureServiceImpl.class);
    @Resource
    ElectronicSignatureDao electronicSignatureDao;

    @Override
    public List<ElectronicSignature> getListByStatus(AgreementStage agreementStage) {
        return electronicSignatureDao.getListByStatus(agreementStage.getCode());
    }

    @Transactional
    @Override
    public void makeAgreement(ElectronicSignature electronicSignature, AgreementStage agreementStage) {
        int result = this.updateAgreementStage(electronicSignature.getId(), AgreementStage.KQM, AgreementStage.parse(electronicSignature.getAgreementStage()));
        if (result == 1) {
            this.updateFileName(electronicSignature.getId(), electronicSignature.getNoSensitiveAgreement(), electronicSignature.getSensitiveAgreement());
        }
    }

    @Override
    public int updateFileName(Integer id, String sensitive, String noSensitive) {
        return electronicSignatureDao.updateFileName(id, sensitive, noSensitive);
    }

    @Override
    public int updateAgreementStage(Integer id, AgreementStage toStage, AgreementStage oldStage) {
        return electronicSignatureDao.updateAgreementStage(id, toStage.getCode(), oldStage.getCode());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void createInvestorsTable(Map<Integer, InvestorBO> insvestors, File destination) throws Exception {
        //创建文件
        Document document = new Document();
        //建立一个书写器
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
        //打开文件
        document.open();

        try {
            //中文字体,解决中文不能显示问题
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            //添加内容
            Paragraph titleParagraph = new Paragraph("附件1：", new Font(bfChinese, 16, Font.BOLD));
            document.add(titleParagraph);
            //添加内容
            Paragraph secondParagraph = new Paragraph("出借人名录及借款详情", new Font(bfChinese, 14, Font.NORMAL));
            document.add(secondParagraph);

            // 5列的表.
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100); // 宽度100%填充
            table.setSpacingBefore(10f); // 前间距
            table.setSpacingAfter(10f); // 后间距

            List<PdfPRow> listRow = table.getRows();
            //设置列宽
            float[] columnWidths = {2f, 1.5f,1.5f, 2f, 1.5f};
            table.setWidths(columnWidths);

            //行1
            PdfPCell cells1[] = new PdfPCell[5];
            PdfPRow row1 = new PdfPRow(cells1);

            //单元格
            Font cellFont = new Font(bfChinese, 14, Font.NORMAL);
            cells1[0] = new PdfPCell(new Paragraph("出借人\n" +
                    "分利宝用户名\n", cellFont));//单元格内容
            cells1[0].setBorderColor(BaseColor.BLACK);//边框验证
            cells1[0].setPaddingLeft(20);//左填充20
            cells1[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells1[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

            cells1[1] = new PdfPCell(new Paragraph("出借人\n" +
                    "真实姓名（或名称）\n", cellFont));
            cells1[1].setBorderColor(BaseColor.BLACK);//边框验证
            cells1[1].setPaddingLeft(20);//左填充20
            cells1[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells1[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells1[2] = new PdfPCell(new Paragraph("证件类型", cellFont));
            cells1[2].setBorderColor(BaseColor.BLACK);//边框验证
            cells1[2].setPaddingLeft(20);//左填充20
            cells1[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells1[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

            cells1[3] = new PdfPCell(new Paragraph("证件号码", cellFont));
            cells1[3].setBorderColor(BaseColor.BLACK);//边框验证
            cells1[3].setPaddingLeft(20);//左填充20
            cells1[3].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells1[3].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells1[4] = new PdfPCell(new Paragraph("借款金额\n" +
                    "（人民币\\元）\n", cellFont));
            cells1[4].setBorderColor(BaseColor.BLACK);//边框验证
            cells1[4].setPaddingLeft(20);//左填充20
            cells1[4].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells1[4].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            //把第一行添加到集合
            listRow.add(row1);
            for (Integer id : insvestors.keySet()) {
                InvestorBO bo = insvestors.get(id);
                //行2
                PdfPCell cells2[] = new PdfPCell[5];
                PdfPRow row2 = new PdfPRow(cells2);
                cells2[0] = new PdfPCell(new Paragraph(bo.getUserName()));
                cells2[1] = new PdfPCell(new Paragraph(bo.getRealName(), new Font(bfChinese)));
                cells2[2] = new PdfPCell(new Paragraph("身份证", new Font(bfChinese)));
                cells2[3] = new PdfPCell(new Paragraph(bo.getIdCardNo()));
                cells2[4] = new PdfPCell(new Paragraph(bo.getAmount().toString()));
                listRow.add(row2);
            }
            //把表格添加到文件中
            document.add(table);
        } finally {
            //关闭文档
            document.close();
            //关闭书写器
            writer.close();
        }
    }

    @Override
    public List<UploadImage> getImageList() {
        return electronicSignatureDao.getImageList();
    }

    @Override
    public JSONObject uploadImage(SealImage uploadImage) throws Exception {
        JSONObject result = null;
        String file = FileUtils.getPicURL(uploadImage.getSealCode(), ApiUtilConfig.get("contact.url"));
        try {
            result = ShangshangqianUtil.uploaduserimage(uploadImage.getUserAccount(), uploadImage.getPhone(), null, file, null, uploadImage.getUserName(), Constants.USER_TYPE.ENTERPRISE);
        } catch (Exception e) {
//            logger.error(String.format("[上上签上传图片异常],bidId=%s,[%s]"),"11", e);
            throw e;
        }
        return result;
    }

    public void updateSealStatus(int id, String sealType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("sealType", sealType);
        electronicSignatureDao.updateSealStatus(map);
    }

    @Override
    public void updateFileNameByBid(Integer bid, String noSensitive, String sensitive) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bid", bid);
        map.put("noSensitive", noSensitive);
        map.put("sensitive", sensitive);
        electronicSignatureDao.updateFileNameByBid(map);
    }
}
