package com.fenlibao.dao.pms.da.cs.guarantor;

import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.guarantor.BankCodeInfo;
import com.fenlibao.model.pms.da.cs.guarantor.Guarantor;
import com.fenlibao.model.pms.da.cs.guarantor.form.GuarantorForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface GuarantorMapper {


    /**
     * 更新企业联系人信息
     *
     * @param bussinessInfo
     */
    void updateEnterpriseContactInfo(BussinessInfo bussinessInfo);

    /**
     * 更新企业基本信息
     *
     * @param bussinessInfo
     */
    void updateEnterpriseBaseInfo(BussinessInfo bussinessInfo);

    /**
     * 创建企业联系人信息
     *
     * @param bussinessInfo
     */
    void insertEnterpriseContactInfo(BussinessInfo bussinessInfo);

    /**
     * 创建企业基本信息
     *
     * @param bussinessInfo
     */
    void insertEnterpriseBaseInfo(BussinessInfo bussinessInfo);

    /**
     * 根据用户id判断该用户是否已存在对应的企业
     * @param userId
     * @return
     */
    Boolean isExitsEnterprise(Integer userId);

    /**
     * 获取账户管理列表
     * @param guarantorForm
     * @param bounds
     * @return
     */
    List<Guarantor> getGuarantorList(GuarantorForm guarantorForm, RowBounds bounds);

    /**
     * 获取企业信息
     * @param userId
     * @return
     */
    BussinessInfo getBussinessInfoByUserId(@Param("userId") Integer userId, @Param("accountType") String accountType, @Param("auditStatus") String auditStatus);

    /**
     * 校验用户是否在分利宝注册
     * @param accountType
     * @param account
     * @return
     */
    UserDetailInfo validAccount(@Param("accountType") String accountType, @Param("account") String account);

    /**
     * 获取新网银行支持的所有银行编码
     * @return
     */
    List<BankCodeInfo> getBankCodes();

    /**
     * 更新flb.t_xw_account 的企业审核状态为AUDIT 审核中
     * @param platformUserNo
     */
    void updateXWAuditStatusToAudit(String platformUserNo);

    /**
     * 更新s61.t6161 的企业审核状态为AUDIT 审核中
     * @param userId
     */
    void updateT61AuditStatusToAudit(String userId);

    /**
     * 校验账号开通新网存管账户的状态（null 即为未开通）(担保账户)
     * @param accountType
     * @param userId
     * @return
     */
    String validAccountIsRegXw(@Param("accountType") String accountType, @Param("userId") Integer userId);
}
