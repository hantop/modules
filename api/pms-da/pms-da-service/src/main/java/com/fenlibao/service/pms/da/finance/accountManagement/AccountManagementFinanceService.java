package com.fenlibao.service.pms.da.finance.accountManagement;

import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.cs.form.RechargeForm;
import com.fenlibao.model.pms.da.finance.T6101Extend;
import com.fenlibao.model.pms.da.finance.enums.PlatformRole;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.enums.PaymentMode;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/26.
 */

public interface AccountManagementFinanceService {

    public List<T6101Extend> findList();

    public List<Transaction> findTradeHistory(int userId, String accountType,Date startDate,Date endDate,RowBounds bounds);


    /**
     * 客户端充值
     * @param userId 充值账户的id t6110.F01
     * @param platformRole 存管用户编号
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
    Map<String,Object> doRecharge(int userId, PlatformRole platformRole, String uri, int orderId, String requestNo, PaymentMode paymode, String bankcode) throws APIException;

    /**
     * 绑定银行卡(企业)
     * @param bindInfo
     * @param redirectUrl
     * @return
     */
    Map<String,Object> bindBank(RechargeForm bindInfo,String platformRole, String redirectUrl) ;
}
