package com.fenlibao.p2p.service.redpacket;

import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.entity.coupons.UserCouponStatisticsInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.redpacket._RedPacketVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 红包service
 * Created by chenzhixuan on 2015/10/16.
 */
public interface RedpacketService {
    /**
     * 首次登陆发放注册返现券
     *
     * @param phoneNum
     * @param userId
     * @return 返现券总额
     * @throws Exception
     */
    BigDecimal grantRedpacketFirstLogin(String phoneNum, String userId) throws Exception;

    /**
     * 发放注册返现券
     *
     * @param phoneNum
     * @param userId
     * @return 返现券总额
     * @throws Exception
     */
    BigDecimal grantRedpacketRegister(String phoneNum, String userId) throws Exception;

    /**
     * 记录红包发放异常日志
     * @param paramsMap
     */
    void recordRedpackExceptionLog(Map<String, Object> paramsMap);


    /**
     * 新增资金流水(转入)
     * @param truninAccountId 资金账号ID
     * @param trunoutAccountId 对方资金账号ID
     * @param tradeTypeId 交易类型ID
     * @param payinAmount 收入金额
     * @param balance 余额
     * @param remark
     * @return
     * */
    int addTruninFundsRecord(int truninAccountId, int trunoutAccountId, int tradeTypeId, BigDecimal payinAmount, BigDecimal balance, String remark);

    /**
     * 新增资金流水(转出)
     * @param truninAccountId 资金账号ID
     * @param trunoutAccountId 对方资金账号ID
     * @param tradeTypeId 交易类型ID
     * @param payoutAmount 支出金额
     * @param balance 余额
     * @param remark
     * @return
     * */
    int addTrunoutFundsRecord(int truninAccountId, int trunoutAccountId, int tradeTypeId, BigDecimal payoutAmount, BigDecimal balance, String remark);


    /**
     * 发放红包
     * @param userId 用户ID
     * @param amount 发放金额
     * @param feeType
     */
    void grantRedPacket(String userId, BigDecimal amount, FeeType feeType) throws Exception;

    /**
     * 根据红包类型查询红包设置信息并发放，发送站内信和短信
     * @param orderId
     * @param userRedPacketInfos
     * @param userId
     * @param phoneNum
     * @param tradeType
     */
    void grantRedPackets(Integer orderId, List<UserRedPacketInfo> userRedPacketInfos, String userId, String phoneNum, int tradeType, VersionTypeEnum versionTypeEnum) throws Exception;


    /**
     * 根据用户ID获取可使用红包数量
     *
     * @param paramMap
     * @return
     */
    int getRedpacketCount(Map<String, Object> paramMap);

    /**
     * 根据用户ID和红包状态获取红包信息
     *
     * @param paramMap
     * @return
     */
    List<UserRedPacketInfo> getRedpackets(Map<String, Object> paramMap);

    /**
     * 获取注册红包设置信息
     * @param redpacketType 红包类型
     * @return
     */
    List<UserRedPacketInfo> getActivityRedBagByType(int redpacketType);

    /**
     * 增加用户的红包(返现红包)
     * @param redPacketId 红包设置信息ID
     * @param amount
     * @param userId      用户ID
     * @param validTime
     */
    void addUserRedpacket(int redPacketId, BigDecimal amount, String userId, String validTime);

    /**
     * 根据红包类型查询红包设置信息并增加用户的红包
     * @param userRedPacketInfos
     * @param userId
     * @param phoneNum
     * @param smsTemplate 短信模板
     * @param letterSuffix 站内信后缀
     */
    void addUserRedpackets(List<UserRedPacketInfo> userRedPacketInfos, String userId, String phoneNum,
                           String smsTemplate, String letterSuffix) throws Exception;

    /**
     * @author wangyunjing
     * @date 20151026
     * @todo 投标成功后获取的返现红包
     * @param paramMap
     */
    List<UserRedPacketInfo> getBidRedpacket(Map<String,Object> paramMap) throws Exception;

    /**
     * @author wangyunjing
     * @date 20151026
     * @todo 投标成功后使用返现红包 修改状态
     * @param paramMap
     */
    void updateRedpacketsRelation(Map<String, Object> paramMap);

    /**
     * 投资计划成功后使用返现红包 修改状态
     * @param paramMap
     */
    void updateRedpacketsRelationForPlan(Map<String, Object> paramMap);

    /**
     * @author wangyunjing
     * @date 20151026
     * @todo 获取订单详情
     * @param orderId
     */
    InverstBidTradeInfo getBidOrderDetail(Integer orderId);

    /**
     * 返现券统计
      * @param paramMap
     * @return
     */
    UserCouponStatisticsInfo getRedpacketsStatistics(Map<String, Object> paramMap);

    /**
     * 根据红包ID获取红包
     * @param id
     * @return
     */
    _RedPacketVO getById(int id);


    /**
     * 获取活动红包列表
     * @param paramMap
     * @return
     */
    List<UserRedPacketInfo> getActivityRedBagList(Map<String, Object> paramMap);

    /**
     * 获取用户活动红包
     * @param paramMap
     * @return
     */
    List<UserRedPacketInfo> getUserRedBagByActivity(Map<String, Object> paramMap);
}