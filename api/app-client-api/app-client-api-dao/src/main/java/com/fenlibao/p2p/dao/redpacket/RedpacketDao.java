package com.fenlibao.p2p.dao.redpacket;

import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.vo.redpacket._RedPacketVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/16.
 */
public interface RedpacketDao {

    /**
     * 获取资金账户ID
     *
     * @param paramMap
     * @return
     */
    Integer getAccountId(Map<String, Object> paramMap);

    /**
     * 新增红包发放异常日志
     * @param paramMap
     */
    void addRedpackExceptionLog(Map<String, Object> paramMap);

    /**
     * 根据交易类型编码获取交易类型
     * @param code
     * @return
     */
    FeeType getFeeType(int code);

    /**
     * 减少用户资金余额
     * @param paramMap
     * */
    int subtractUserAccountAmount(Map<String, Object> paramMap);

    /**
     * 增加用户资金余额
     * @param paramMap
     * */
    int increaseUserAccountAmount(Map<String, Object> paramMap);

    /**
     * 锁定资产账户
     * @param paramMap
     * @return
     */
    UserAccount lockUserAccount(Map<String, Object> paramMap);

    /**
     * 锁定发放工资资金账户
     *
     * @param accountId 资金账户ID
     * @return
     */
    UserAccount lockSalaryaccount(Integer accountId);

    /**
     * 新增资金流水(转入)
     * @param payoutAmount 收入金额
     * @param balance 余额
     * @param paramMap
     * @return
     */
    int addTruninFundsRecord(Map<String, Object> paramMap);

    /**
     * 新增资金流水(转出)
     * @param payinAmount 支出金额
     * @param balance 余额
     * @param paramMap
     * @return
     */
    int addTrunoutFundsRecord(Map<String, Object> paramMap);

    /**
     * 获取注册红包设置信息
     * @param paramMap
     * @return
     */
    List<UserRedPacketInfo> getActivityRedBagByType(Map<String, Object> paramMap);

    /**
     * 新增用户的红包
     * @param paramMap
     */
    int addUserRedpacket(Map<String, Object> paramMap);

    /**
     * 根据用户ID获取红包信息
     * @param paramMap
     * @return
     */
    List<UserRedPacketInfo> getRedpackets(Map<String, Object> paramMap);

    /**
     * 根据用户ID获取可使用红包数量
     * @param paramMap
     * @return
     */
    int getRedpacketCount(Map<String, Object> paramMap);

    /**
     * @author wangyunjing
     * @date 20151026
     * @todo 投标成功后获取的返现红包
     * @param paramMap
     */
    List<UserRedPacketInfo> getBidRedpacket(Map<String, Object> paramMap);

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
     * @date 20151028
     * @todo 获取订单详情
     * @param orderId
     */
    InverstBidTradeInfo getBidOrderDetail(Integer orderId);

    /**
     * 根据红包ID获取红包
     * @param id
     * @return
     */
    _RedPacketVO getById(int id);

    List<UserRedPacketInfo> getActivityRedBagBySetting(Map<String, Object> paramMap);

    List<UserRedPacketInfo> getActivityRedBagList(Map<String, Object> paramMap);

    List<UserRedPacketInfo> getUserRedBagByActivity(Map<String, Object> paramMap);
}
