package com.fenlibao.p2p.dao.xinwang.sign;

import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;
import com.fenlibao.p2p.model.xinwang.entity.sign.UploadImage;

import java.util.List;
import java.util.Map;

public interface ElectronicSignatureDao {
    List<ElectronicSignature> getListByStatus(Integer agreementStage);

    int updateFileName(Integer id, String sensitive, String noSensitive);

    int updateAgreementStage(Integer id, int toStage, int oldStage);

    /**
     * 获取需要上传至上上签的公章图片列表
     */
    List<UploadImage> getImageList();

    /**
     * 更新上上签上传状态
     */
    int updateSealStatus(Map map);

    int updateFileNameByBid(Map<String, Object> map);
}
