package com.fenlibao.p2p.dao.entrust;

import com.fenlibao.p2p.model.entity.user.UnRegUser;
import com.fenlibao.p2p.model.vo.entrust.EntrustAgreementVo;
import com.fenlibao.p2p.model.vo.entrust.EntrustUserInfoVo;

import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/1.
 */
public interface EntrustDao {

    /**
     * 未签名委托协议书
     * @return
     */
    List<EntrustAgreementVo> getUnSignAgreementList();

    UnRegUser getUnRegUser(int userId);

    EntrustAgreementVo lockAgreement(Map map);

    EntrustUserInfoVo getEntruetUserInfoByUserId(int userId);

    int updateUnSignAgreementById(Map map);

    List<EntrustAgreementVo> getSignFailAgreementList();

    EntrustAgreementVo lockSignFailAgreement(Map map);
}
