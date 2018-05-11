package com.fenlibao.service.pms.da.cs;

import com.fenlibao.model.pms.da.cs.GuaranteeInfo;
import com.fenlibao.model.pms.da.cs.RechargePreInfo;
import com.fenlibao.model.pms.da.cs.form.GuaranteeForm;
import com.fenlibao.model.pms.da.cs.form.RechargeForm;
import com.fenlibao.model.pms.da.cs.form.XWWithdrawForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/6/26.
 */
public interface GuaranteeService {
    /**
     * 获取担保用户列表信息
     * @param guaranteeForm
     * @param bounds
     * @return
     */
    List<GuaranteeInfo> getGuaranteeInfoList(GuaranteeForm guaranteeForm, RowBounds bounds);

    /**
     * 担保用户绑定银行卡
     * @param bindInfo
     * @param guaranteeXwRedirectUrl
     * @return
     */
    Map<String,Object> bindBank(RechargeForm bindInfo, String guaranteeXwRedirectUrl) throws Exception;

    /**
     * 担保用户解绑银行卡
     * @param userId
     * @param guaranteeXwRedirectUrl
     * @return
     */
    Map<String,Object> unbindBank(String userId, String guaranteeXwRedirectUrl) throws Exception;


    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    RechargePreInfo getRechargePreInfoByUserId(String userId);

    /**
     * 获取请求新网充值参数
     * @param rechargeForm
     * @return
     */
    Map<String,Object> getRechargeRequestParam(RechargeForm rechargeForm, String redirectUrl) throws Exception;

    /**
     * 获取请求新网提现参数
     * @param withdrawForm
     * @param guaranteeXwRedirectUrl
     * @return
     */
    Map<String,Object> getWithdrawRequestParam(XWWithdrawForm withdrawForm, String guaranteeXwRedirectUrl) throws Exception;
}
