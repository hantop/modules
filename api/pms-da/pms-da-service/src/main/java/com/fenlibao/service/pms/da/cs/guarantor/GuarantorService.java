package com.fenlibao.service.pms.da.cs.guarantor;

import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.guarantor.BankCodeInfo;
import com.fenlibao.model.pms.da.cs.guarantor.Guarantor;
import com.fenlibao.model.pms.da.cs.guarantor.form.GuarantorForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 担保人
 *
 * Created by chenzhixuan on 2017/6/7.
 */
public interface GuarantorService {

    /**
     * 获取账户管理列表
     * @param guarantorForm
     * @param bounds
     * @return
     */
    List<Guarantor> getGuarantorList(GuarantorForm guarantorForm, RowBounds bounds);

    /**
     * 根据用户id获取对应的企业信息
     * @param userId
     * @return
     */
    BussinessInfo getBussinessInfoByUserId(Integer userId, String accountType, String auditStatus);

    /**
     * 根据用户账号判断该账号是否已注册分利宝
     * @param accountType
     * @param account
     * @return
     */
    UserDetailInfo validAccount(String accountType, String account);

    /**
     * 保存企业信息
     * @param bussinessInfo
     */
    Map<String,Object>  addEnterprise(BussinessInfo bussinessInfo, String redirectUrl) throws Exception;

    /**
     * 构造新网注册用户的参数
     * @param userId
     * @return
     */
    Map<String,Object> submitPersonal(String userId, String redirectUrl) throws Exception;

    /**
     * 获取新网支持的银行编码
     * @return
     */
    List<BankCodeInfo> getBankCodes();

    /**
     * 获取更新新网企业信息的请求信息
     * @param userId
     * @return
     */
    Map<String,Object> getXwUpdateEnterpriseRequestData(String userId, String redirectUrl) throws Exception;

    /**
     * 修改企业信息时处理浏览器返回的信息
     * @param platformUserNo
     */
    void updateAuditStatusToAudit(String platformUserNo);

    /**
     * 校验账号开通新网存管账户的状态（null 即为未开通）(担保账户)
     * @param accountType
     * @param userId
     * @return
     */
    String validAccountIsRegXw(String accountType, Integer userId);
}
