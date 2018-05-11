package com.fenlibao.p2p.service.xinwang.sign;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.xinwang.bo.InvestorBO;
import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;
import com.fenlibao.p2p.model.xinwang.entity.sign.SealImage;
import com.fenlibao.p2p.model.xinwang.entity.sign.UploadImage;
import com.fenlibao.p2p.model.xinwang.enums.sign.AgreementStage;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ElectronicSignatureService {
    /**
     * 根据状态获取电子签章信息列表
     * @param agreementStage
     * @return
     */
    List<ElectronicSignature> getListByStatus(AgreementStage agreementStage);

    /**
     * 签合同
     * @param electronicSignature
     * @param agreementStage
     */
    void makeAgreement(ElectronicSignature electronicSignature, AgreementStage agreementStage);

    /**
     * 添加合同地址
     * @param id
     * @param sensitive
     * @param noSensitive
     */
    int updateFileName(Integer id, String sensitive, String noSensitive);

    /**
     * 修改合同签订状态
     *
     * @param id
     * @param toStage
     * @param oldStage
     * @return
     */
    int updateAgreementStage(Integer id, AgreementStage toStage, AgreementStage oldStage);

    /**
     * 生成投资用户列表
     *
     * @param insvestors
     * @param destination
     * @return
     */
    void createInvestorsTable(Map<Integer, InvestorBO> insvestors, File destination) throws Exception;


    /**
     * 获取需要上传的公章图片列表
     * @return
     */
    List<UploadImage> getImageList();

    /**
     * 上传图片
     */
    JSONObject uploadImage(SealImage uploadImage) throws  Exception;

    /**
     * 更新上上签上传状态
     */
    void updateSealStatus(int id ,String sealType);

    /**
     * 更新合同名称
     * @param bid
     * @param noSensitive
     * @param sensitive
     */
    void updateFileNameByBid(Integer bid, String noSensitive, String sensitive);
}
