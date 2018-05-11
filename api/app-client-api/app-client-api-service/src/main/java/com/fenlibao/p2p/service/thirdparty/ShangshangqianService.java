package com.fenlibao.p2p.service.thirdparty;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.entity.bid.ConsumeBid;
import com.fenlibao.p2p.model.entity.bid.InvestRecords;
import com.fenlibao.p2p.model.entity.bid.Tripleagreement;
import com.fenlibao.p2p.model.entity.user.UnRegUser;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.thirdparty.shangshangqian.vo.CertificateApply;
import com.fenlibao.thirdparty.shangshangqian.vo.RegisterUser;

import java.util.List;
import java.util.Map;

/**
 *上上签
 */
public interface ShangshangqianService extends Runnable{

	/**
	 * 未签名消费信贷标四方合同
	 * @return
     */
	List<ConsumeBid> getUnSignBidList();

	/**
	 * 消费信贷标投资记录-签名
	 * @param bid
	 * @return
     */
	List<InvestRecords> investRecords(int bid);

	/**
	 * 消费信贷标投资用户未注册第三方平台的
	 * @param bid
	 * @return
     */
	List<UnRegUser> unRegUsers(int bid);

	/**
	 * 获取消费信贷标四方合同
	 * @param bid
	 * @return
     */
	ConsumeBid lockConsumeBid(int bid);

	/**
	 * 更新消费信贷标四方合同
	 * @param bid
	 * @param statusEnum
     * @return
     */
	int updateConsumeBid(int bid,AgreementSignStatusEnum statusEnum);

	/**
	 * 记录异常的四方合同签名用户
	 * @param signmap
	 * @return
     */
	int recordError(Map signmap);

	/**
	 * 注册并ca
	 * @param regUserMap
	 * @param CertificateApplyMap
	 * @return
     * @throws Exception
     */
	JSONObject regUserAndCa(Map regUserMap,Map CertificateApplyMap) throws Exception;

	/**
	 * 注册并ca
	 * @param unRegUser
	 * @return
	 * @throws Exception
     */
	JSONObject regUserAndCa(UnRegUser unRegUser) throws Exception;

	/**
	 * 注册
	 * @param ru
	 * @return
	 * @throws Exception
	 */
	JSONObject regUser(RegisterUser ru) throws Exception;

	/**
	 * CFCA证书申请
	 * @param cf
	 * @return
	 * @throws Exception
     */
	JSONObject certificateApply(CertificateApply cf)throws Exception;

	/**
	 * 追加签署人
	 * @param map
	 * @return
	 * @throws Exception
     */
	JSONObject sjdsendcontract(Map map)throws Exception;

	/**
	 * 自动签
	 * @param map
	 * @return
	 * @throws Exception
     */
	JSONObject AutoSignbyCA(Map map)throws Exception;

	/**
	 * 获取签名失败的四方合同
	 * @return
     */
	List<ConsumeBid> getSignFailConsumeBids();

	/**
	 * 获取签名失败的四方合同并更新状态
	 * @param bid
	 * @return
     */
	ConsumeBid getSignFailConsumeBid(int bid);


	/**
	 * 获取待签三方合同列表
	 * @return
	 */
	List<Tripleagreement> getUnSignTripleagreements();

	/**
	 * 获取待签三方合同，并更新状态
	 * @param id
	 * @return
	 */
	Tripleagreement getUnSignTripleagreement(int id);

	/**
	 * 更新三方合同状态
	 * @param id
	 * @param statusEnum
	 * @return
	 */
	int updateTripleagreement(int id,AgreementSignStatusEnum statusEnum);


	/**
	 * 获取签名失败的三方合同列表
	 * @return
	 */
	List<Tripleagreement> getSignFailTripleagreements();

	/**
	 * 获取签名失败的三方合同，并更新状态
	 * @return
	 */
	Tripleagreement getSignFailTripleagreement(int id);


	/**
	 * 批量操作
	 */
	void batchExe(List<ConsumeBid> list, Runnable runner);


}