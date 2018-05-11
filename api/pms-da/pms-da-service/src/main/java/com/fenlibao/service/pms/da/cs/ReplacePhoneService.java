package com.fenlibao.service.pms.da.cs;

import com.fenlibao.model.pms.da.cs.ReplacePhoneInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import com.fenlibao.model.pms.da.cs.form.ReplacePhoneForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/12/23.
 */

public interface ReplacePhoneService {

    /**
     * 查询更换手机的记录信息
     *
     * @param replacePhoneForm
     * @param bounds
     * @return
     */
    List<ReplacePhoneInfo> getReplacePhoneList(ReplacePhoneForm replacePhoneForm, RowBounds bounds);

    /**
     * 查询用户指信息
     *
     * @param param
     * @return
     */
    UserAuthInfo getUserInfoByPhone(Map<String,Object> param);

    /**
     * 查询用户是否绑定身份证
     *
     * @param phone
     * @return
     */
    UserAuthInfo getUserAuthByPhone(String phone);

    /**
     * 更换手机号码
     * @param userId
     * @param rePhone
     * @param userInfo
     * @return
     */
    void replacePhoneLogic(Integer userId, String rePhone, String oldPhone, UserAuthInfo userInfo);

    /**
     * 保存更换手机号码
     * @param userId
     * @param rePhone
     * @return
     */
    void saveReplacePhone(int userId, String rePhone, String oldPhone);
}
