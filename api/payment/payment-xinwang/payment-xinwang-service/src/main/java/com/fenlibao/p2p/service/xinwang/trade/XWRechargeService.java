package com.fenlibao.p2p.service.xinwang.trade;


import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWDirectRecharge;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.enums.PaymentMode;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 充值
 *
 * @date 2017/5/11 11:17
 */
public interface XWRechargeService extends XWNotifyService {
    /**
     * 客户端充值
     * @param userId 充值账户的id t6110.F01
     * @param userRole 充值用户角色
     * @param uri 返回地址
     * @param orderId 充值订单id
     * @param requestNo 生成的新网流水号
     * @param paymode 支付方式
            WEB("WEB", "网银" ,0),
            SWIFT("SWIFT", "快捷支付",1),
            BANK("BANK", "转账充值",2),
            BACKROLL("BACKROLL", "资金回退充值",3),
            PROXY("PROXY", "自动充值", 4),;
     * @param bankcode 银行编码 t5020.F07
     * @return
     * @throws APIException
     */
    Map<String,Object> doRecharge(int userId, UserRole userRole, String uri, int orderId, String requestNo, PaymentMode paymode, String bankcode) throws APIException;

    /**
     * 直连充值接口-支付方式默认使用PROXY("PROXY", "自动充值", 4),
     * @param userId 充值账户的id t6110.F01
     * @param userRole 充值账户的用户角色
     * @param orderId 充值订单id
     * @param requestNo 生成的新网流水号
     * @return
     */
    XWDirectRecharge doDirectRecharge(int userId, UserRole userRole, int orderId, String requestNo);

    /**
     * 代充值接口，默认使用SYS_GENERATE_006 代充值账户（系统自动生成平台用户编号）
     *
     * @param userId         充值用户的id
     * @param platformUserNo 新网用户编码
     * @param amount         充值金额
     * @param businessType   交易类型 t5122 参数要填齐
     * @return 返回 flb.t_xw_request.id
     */
    int doAlternativeRecharge(int userId, String platformUserNo, BigDecimal amount, BusinessType businessType);

    /**
     *  平台划拨
     * @param sourceUserNo 划出账户的新网编号
     * @param targetUserNo 划入账户的新网编号
     * @param amount 划拨金额
     * @param businessType 交易类型
     * @return 返回 flb.t_xw_request.id
     */
    int doFundsTransfer(String sourceUserNo, String targetUserNo, BigDecimal amount, BusinessType businessType);

    /**
     * 获取需要确认的充值订单
     *
     * @param interfaceName 接口名
     * @param requestTime
     * @return
     */
    List<String> getOrderNeedComfired(XinwangInterfaceName interfaceName, Date requestTime);

    /**
     * 网关充值订单确认
     * @param requestNo
     */
    void comfiredOrder(String requestNo);
}
