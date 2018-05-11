package com.fenlibao.p2p.dao.xinwang.sign;

import com.fenlibao.p2p.model.xinwang.entity.sign.*;
import com.fenlibao.p2p.model.xinwang.vo.sign.SignRegUserVO;

import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/12/13 17:17.
 * @version 1.0
 */
public interface SignNormalBidDao {

    List<SignNormalBidInfo> getSignBidsByStatus(Integer status);

    SignNormalBidInfo lockSignBidInfoById(Integer bid);

    int updateSignBidInfo(SignNormalBidInfo lockSignBidInfo);

    int updateSignBidInfoByMap(Map<String, String> paramsMap);

    List<Investors> getInvestorsByBid(Integer bid);

    SignUserInfo getSignUserInfoByBid(Integer bid);

    List<SignXFXDBid> getXFXDBids();

    List<SignXFXDBid> getYqmXFXDBids();

    int saveSignAgreementDownload(SignXFXDBid signXFXDBid);

    int updateSignAgreementDownload(SignXFXDBid signXFXDBid);

    void saveSignAgreementDownloads(List<SignXFXDBid> tempSigns);

    void updateSignAgreementDownloads(List<SignXFXDBid> tempSigns);

    SignRegUserRecord getRegUserByEmail(String email, int code);

    int saveRegThirdPartUser(Integer userId, String pfUid, SignRegUserVO signRegUserVo);

    int updateCaResult(String email, int caStatus, String password);

    int addSignBid(Integer bid);

    List<SignRegUserRecord> getNotRegInvestorsByBid(Integer bid);
}
