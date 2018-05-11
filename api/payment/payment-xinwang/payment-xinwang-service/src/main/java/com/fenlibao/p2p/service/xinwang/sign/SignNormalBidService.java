package com.fenlibao.p2p.service.xinwang.sign;

import com.fenlibao.p2p.model.xinwang.entity.sign.SignNormalBidInfo;
import com.fenlibao.p2p.model.xinwang.entity.sign.SignRegUserRecord;
import com.fenlibao.p2p.model.xinwang.entity.sign.SignXFXDBid;

import java.util.List;

/**
 * @author zeronx on 2017/12/13 16:34.
 * @version 1.0
 * 签章常规标
 */
public interface SignNormalBidService {
    /**
     * 根据 状态 获取常规标列表
     * @param status
     * @return
     */
    List<SignNormalBidInfo> getSignBidsByStatus(Integer status) throws Exception;

    /**
     * 处理记录状态为0：待上传协议导上上签的常规标
     * @param signBidInfo
     * @return
     * @throws Exception
     */
    boolean dealWithStatus0(SignNormalBidInfo signBidInfo) throws Exception;

    /**
     * 处理记录状态为1：待签章的常规标
     * @param signBidInfo
     * @return
     * @throws Exception
     */
    boolean dealWithStatus1(SignNormalBidInfo signBidInfo) throws Exception;

    /**
     * 处理记录状态为2：关闭合同和待下载 的常规标
     * @param signBidInfo
     * @return
     * @throws Exception
     */
    boolean dealWithStatus2(SignNormalBidInfo signBidInfo) throws Exception;

    /**
     * 获取需要下载签章协议 的消费信贷表列表
     * @return
     */
    List<SignXFXDBid> getXFXDBids();

    /**
     * 获取签章状态从 未签名/签名失败/签名中 -> 已签名 的消费信贷列表
     * @return
     */
    List<SignXFXDBid> getYqmXFXDBids();

    /**
     * 下载消费信贷文档
     * @param signXFXDBids
     * @param isSave true: 新下载文档保存到s62.sign_agreement_download , false : 更新 s62.sign_agreement_download
     */
    void downloadXFXDAgreement(List<SignXFXDBid> signXFXDBids, boolean isSave);

    /**
     * 准备签该标的所有企业/个人时判断企业/个人是否都已注册和ca和上传企业公章
     * @param bid
     * @return
     */
    boolean isAllSignUserRegAndCA(Integer bid);

    /**
     * 根据email 和 platform 获取 注册上上签的记录
     * @param email 不一定时ｅｍａｉｌ的格式可以是其他任何的字符串
     * @param platform 上上签平台时为：1
     * @return
     */
    SignRegUserRecord getRegUserRecord(String email, int platform);

    /**
     * 添加需要签名的标的
     * @param bid
     * @return
     */
    int addSignBid(Integer bid);
}
