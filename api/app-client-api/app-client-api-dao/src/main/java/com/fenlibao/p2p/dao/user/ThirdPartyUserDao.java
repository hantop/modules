package com.fenlibao.p2p.dao.user;

import com.fenlibao.p2p.model.entity.bid.BidExtendRelatedGuaranteeInfo;
import com.fenlibao.p2p.model.entity.bid.ConsumeBid;
import com.fenlibao.p2p.model.entity.bid.InvestRecords;
import com.fenlibao.p2p.model.entity.bid.Tripleagreement;
import com.fenlibao.p2p.model.entity.user.UnRegUser;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;

import java.util.List;
import java.util.Map;

/**用户在第三方平台注册信息
 * by kris
 */
public interface ThirdPartyUserDao {
    /**
    *未签名消费信贷标
     */
    List<ConsumeBid> getUnSignBidList();
    /**
     *  消费信贷标投资记录
     * @param bidId
     * @return
     */
    List<InvestRecords> getInvestRecords(int bidId);

    /**
     *  消费信贷标投资用户未注册第三方平台的
     * @param bidId
     * @return
     */
    List<UnRegUser> getUnRegUsers(int bidId);

    /**
     * 添加第三方平台用户
     * @return
     */
    int addRegUser(Map map);

    /**
     * 更新第三方平台用户信息
     * @return
     */
    int updateRegUser(Map map);

    /**
     * 获取待签标记录
     * @return
     */
    ConsumeBid lockConsumeBid(Map map);
    /**
     * 更新代签标记录
     * @return
     */
    int updateUnSignBidInfo(Map map);

    /**
     * 更新代签标记录
     * @return
     */
    int recordError(Map map);

    //获取签名失败的标
    List<ConsumeBid> getSignFailConsumeBids();
    /**
     * 获取待签标记录
     * @return
     */
    ConsumeBid getSignFailConsumeBid(Map map);
    /**
     * 获取待签三方合同
     * @return
     */
    List<Tripleagreement> getUnSignTripleagreements();

    /**
     * 获取待签三方合同
     * @param map
     * @return
     */
    Tripleagreement getUnSignTripleagreement(Map map);

    /**
     * 更新三方合同
     * @param map
     * @return
     */
    int updateTripleagreement(Map map);

    /**
     * 获取签名失败的三方合同列表
     * @return
     */
    List<Tripleagreement> getSignFailTripleagreements();

    /**
     * 获取签名失败的三方合同，并更新状态
     * @return
     */
    Tripleagreement getSignFailTripleagreement(Map map);


    /**
     * 计算第三方平台注册数量
     * @param email
     * @param platform
     * @return
     */
    Integer countRegisterThirdParty(String email, int platform);

    /**
     * 获取一个标的的投资人用户列表
     * @param bidId
     * @return
     */
    List<Integer> getInvestorUserIdList(int bidId);

    /**
     * 上上签注册成功后加入数据库
     * @param map
     */
    void addSSQRegUser(Map map);
}
