package com.fenlibao.p2p.service.thirdparty;

import com.fenlibao.p2p.model.entity.user.UnRegUser;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.p2p.model.vo.entrust.EntrustAgreementVo;
import com.fenlibao.p2p.model.vo.entrust.EntrustUserInfoVo;

import java.util.List;

/**
 * Created by zeronx on 2017/7/31.
 * 委托开户
 */
public interface EntrustAccountService {

    /**
     * 未签名的委托开户合同
     * @return
     */
    List<EntrustAgreementVo> getUnSignAgreementList();

    /**
     * 通过手机号判断用户手否已注册上上签
     * @param userId
     * @return
     */
    UnRegUser getUnRegUser(int userId);

    /**
     * 锁记录
     * @param id
     * @return
     */
    EntrustAgreementVo lockAgreement(int id);

    /**
     * 获取委托开户人的信息
     * @param userId
     * @return
     */
    EntrustUserInfoVo getEntruetUserInfoByUserId(int userId);

    /**
     * 更新签名状态
     * @param id
     * @param statusEnum
     */
    int updateUnSignAgreementById(int id, AgreementSignStatusEnum statusEnum);
    /**
     * 签名失败或长时间处于签名中的委托开户合同
     * @return
     */
    List<EntrustAgreementVo> getSignFailAgreementList();

    /**
     * 锁记录
     * @param id
     * @return
     */
    EntrustAgreementVo lockSignFailAgreement(int id);
}
