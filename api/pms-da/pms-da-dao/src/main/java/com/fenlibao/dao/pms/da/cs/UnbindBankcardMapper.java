package com.fenlibao.dao.pms.da.cs;

import com.fenlibao.model.pms.da.cs.OrgAuthInfo;
import com.fenlibao.model.pms.da.cs.UnbindBankcardInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import com.fenlibao.model.pms.da.cs.UserBankcard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/4.
 */
public interface UnbindBankcardMapper {
    /**
     * 新增解绑银行卡记录
     *
     * @param unbindBankcardInfo
     */
    int addUnbindBankCard(UnbindBankcardInfo unbindBankcardInfo);

    /**
     * 审核解绑银行卡记录
     * @param unbindBankcardId 解绑银行卡记录Id
     * @param userId
     * @param userRole
     * @param auditStatus
     */
    void addUnbindBankcardAuditRecord(@Param(value = "unbindBankcardId")Integer unbindBankcardId,
                                      @Param(value = "userId")Integer userId,
                                      @Param(value = "userRole")String userRole,
                                      @Param(value = "auditStatus")int auditStatus,
                                      @Param(value = "expiryTime")Date expiry_time);

    /**
     * 删除连连支付协议号
     *
     * @param userId
     */
    void deletePayExtend(Integer userId);


    /**
     * 将连连支付协议号以及宝付支付协议号清空
     *
     * @param userId
     */
    int setPayExtendNull(Integer userId);


    /**
     * 修改用户银行卡认证状态
     *
     * @param userId
     * @return
     */
    int updateUserBankcardStatus(Integer userId);

    /**
     * 根据用户ID获取用户银行卡
     *
     * @param userId
     * @return
     */
    List<UserBankcard> getUserBankCard(Integer userId, RowBounds rowBounds);

    /**
     * 查询用户实名认证信息(普通/存管)
     *
     * @param userAccount
     * @param uid
     * @return
     */
    List<UserAuthInfo> getUserAuthInfo(@Param(value = "userAccount") String userAccount,
                                 @Param(value = "uid") String uid);

    /**
     * 查询企业相关认证信息(普通/存管)
     * @param userAccount
     * @param uid
     * @return
     */
    List<OrgAuthInfo> getOrgAuthInfo(@Param(value = "userAccount") String userAccount,
                                     @Param(value = "uid") String uid);

    /**
     * 查询解绑银行卡
     *
     * @param paramMap
     * @param bounds
     * @return
     */
    List<UnbindBankcardInfo> getUnbindBankcardInfos(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取用户手机号码
     * @param userId
     * @return
     */
    String getPhoneNumByUserId(Integer userId);
}
