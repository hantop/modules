package com.fenlibao.p2p.dao.xinwang.sign.impl;


import com.fenlibao.p2p.dao.xinwang.sign.SignNormalBidDao;
import com.fenlibao.p2p.model.xinwang.entity.sign.*;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignRegUserVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/12/13 17:18.
 * @version 1.0
 */
@Repository
public class SignNormalBidDaoImpl implements SignNormalBidDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SignNormalBidMapper.";

    @Override
    public List<SignNormalBidInfo> getSignBidsByStatus(Integer status) {
        Map<String, String> params = new HashMap<>();
        params.put("status", "" + status);
        if (status == 0) {
            params.put("createAgreementStatus", "" + 1);
        }
        return sqlSession.selectList(MAPPER + "getSignBidsByStatus", params);
    }

    @Override
    public SignNormalBidInfo lockSignBidInfoById(Integer bid) {
        return sqlSession.selectOne(MAPPER + "lockSignBidInfoById", bid);
    }

    @Override
    public int updateSignBidInfo(SignNormalBidInfo lockSignBidInfo) {
        return sqlSession.update(MAPPER + "updateSignBidInfo", lockSignBidInfo);
    }

    @Override
    public int updateSignBidInfoByMap(Map<String, String> paramsMap) {
        return sqlSession.update(MAPPER + "updateSignBidInfoByMap", paramsMap);
    }

    @Override
    public List<Investors> getInvestorsByBid(Integer bid) {
        return sqlSession.selectList(MAPPER + "getInvestorsByBid", bid);
    }

    @Override
    public SignUserInfo getSignUserInfoByBid(Integer bid) {
        return sqlSession.selectOne(MAPPER + "getSignUserInfoByBid", bid);
    }

    @Override
    public List<SignXFXDBid> getXFXDBids() {
        return sqlSession.selectList(MAPPER + "getXFXDBids");
    }

    @Override
    public List<SignXFXDBid> getYqmXFXDBids() {
        return sqlSession.selectList(MAPPER + "getYqmXFXDBids");
    }

    @Override
    public int saveSignAgreementDownload(SignXFXDBid signXFXDBid) {
        return sqlSession.insert(MAPPER + "saveSignAgreementDownload", signXFXDBid);
    }

    @Override
    public int updateSignAgreementDownload(SignXFXDBid signXFXDBid) {
        return sqlSession.update(MAPPER + "updateSignAgreementDownload", signXFXDBid);
    }

    @Override
    public void saveSignAgreementDownloads(List<SignXFXDBid> tempSigns) {
        sqlSession.insert(MAPPER + "saveSignAgreementDownloads", tempSigns);
    }

    @Override
    public void updateSignAgreementDownloads(List<SignXFXDBid> tempSigns) {
        sqlSession.update(MAPPER + "updateSignAgreementDownloads", tempSigns);
    }

    @Override
    public SignRegUserRecord getRegUserByEmail(String email, int code) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("platform", code);
        return sqlSession.selectOne(MAPPER + "getRegUserByEmail", params);
    }

    @Override
    public int saveRegThirdPartUser(Integer userId, String pfUid, SignRegUserVO signRegUserVo) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("phone", signRegUserVo.getMobile());
        params.put("email", signRegUserVo.getEmail());
        params.put("platformUserId", pfUid);
        return sqlSession.insert(MAPPER + "saveRegThirdPartUser", params);
    }

    @Override
    public int updateCaResult(String email, int caStatus, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("caResult", caStatus);
        params.put("pwd", password);
        return sqlSession.update(MAPPER + "updateCaResult", params);
    }

    @Override
    public int addSignBid(Integer bid) {
        return sqlSession.insert(MAPPER + "addSignBid", bid);
    }

    @Override
    public List<SignRegUserRecord> getNotRegInvestorsByBid(Integer bid) {
        return sqlSession.selectList(MAPPER + "getNotRegInvestorsByBid", bid);
    }
}
