package com.fenlibao.service.pms.da.cs;

import com.fenlibao.model.pms.da.cs.OrgAuthInfo;
import com.fenlibao.model.pms.da.cs.UnbindBankcardInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import com.fenlibao.model.pms.da.cs.UserBankcard;
import com.fenlibao.model.pms.da.cs.form.UnbindBankcardForm;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * 解绑银行卡
 * Created by chenzhixuan on 2015/12/4.
 */
public interface UnbindBankcardService {
    /**
     * 解绑用户银行卡
     * @param userAccount
     * @param userId
     * @param bankcardNum
     * @param operator
     * @param unbindStatus
     */
    void unbindBankcard(String userAccount, Integer userId, String bankcardNum, String operator, int unbindStatus);

    /**
     * 存管审核解绑银行卡
     * @param userAccount
     * @param userId
     * @param bankcardNum
     * @param operator
     * @param auditStatus
     * @param userRole
     */
    void auditUnbindBankcard(String userAccount, Integer userId, String bankcardNum, String operator, int auditStatus, String userRole);

    /**
     * 新增解绑银行卡记录(普通版直接解绑,存管版审核后交给用户操作)
     * (存管版本之前记录的是用户的手机号,只支持对个人银行卡进行解绑)
     * @param userAccount
     * @param userId
     * @param bankcardNum
     * @param operator
     * @param unbindStatus
     */
    int addUnbindBankcardRecord(String userAccount, Integer userId, String bankcardNum, String operator, int unbindStatus);

    /**
     * 存管审核解绑银行卡记录
     * @param auditId
     * @param userId
     * @param userRole
     * @param auditStatus
     * @param expiryTime
     * @return
     */
    void addUnbindBankcardAuditRecord(Integer auditId, Integer userId, String userRole, int auditStatus, Date expiryTime);

    /**
     * 根据用户ID获取用户银行卡
     *
     * @param userId
     * @return
     */
    List<UserBankcard> getUserBankCard(Integer userId, RowBounds rowBounds);

    /**
     * 查询用户实名认证信息
     *
     * @param unbindBankcardForm
     * @return
     */
    List<UserAuthInfo> getUserAuthInfo(UnbindBankcardForm unbindBankcardForm);

    /**
     * 查询企业相关认证信息
     * @param unbindBankcardForm
     * @return
     */
    List<OrgAuthInfo> getOrgAuthInfo(UnbindBankcardForm unbindBankcardForm);

    /**
     * 查询解绑银行卡
     *
     * @param phoneNum
     * @param operator
     * @param unbindStartDate
     * @param unbindEndDate
     * @param bounds
     * @return
     */
    List<UnbindBankcardInfo> getUnbindBankcardInfos(String phoneNum, String operator, Date unbindStartDate, Date unbindEndDate, RowBounds bounds);

}
