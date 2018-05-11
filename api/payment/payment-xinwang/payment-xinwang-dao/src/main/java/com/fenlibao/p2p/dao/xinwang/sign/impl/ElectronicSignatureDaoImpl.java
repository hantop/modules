package com.fenlibao.p2p.dao.xinwang.sign.impl;

import com.fenlibao.p2p.dao.xinwang.sign.ElectronicSignatureDao;
import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;
import com.fenlibao.p2p.model.xinwang.entity.sign.UploadImage;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ElectronicSignatureDaoImpl implements ElectronicSignatureDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "ElectronicSignatrueMapper.";

    @Override
    public List<ElectronicSignature> getListByStatus(Integer agreementStage) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("agreementStage", agreementStage);
        return sqlSession.selectList(MAPPER + "getListByStatus", param);
    }

    @Override
    public int updateFileName(Integer id, String sensitive, String noSensitive) {
        Map<String, Object> param = new HashMap<>(4);
        param.put("id", id);
        param.put("noSensitiveAgreement", noSensitive);
        param.put("sensitiveAgreement", sensitive);
        return sqlSession.update(MAPPER + "updateFileName", param);
    }

    @Override
    public int updateAgreementStage(Integer id, int toStage, int oldStage) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("toStage", toStage);
        param.put("oldStage", oldStage);
        return sqlSession.update(MAPPER + "updateAgreementStage", param);
    }

    @Override
    public List<UploadImage> getImageList() {
        return sqlSession.selectList(MAPPER + "getImageList", null);
    }

    @Override
    public int updateSealStatus(Map map) {
        return sqlSession.update(MAPPER + "updateSealStatus",map);
    }

    @Override
    public int updateFileNameByBid(Map<String, Object> map) {
        return sqlSession.update(MAPPER + "updateFileNameByBid", map);
    }
}
