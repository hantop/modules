package com.fenlibao.dao.pms.da.cs;

import com.fenlibao.model.pms.da.cs.ReplacePhoneInfo;
import com.fenlibao.model.pms.da.cs.UserAuthInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/12/23.
 */

public interface ReplacePhoneMapper {

    List<ReplacePhoneInfo> getReplacePhoneList(Map<String, Object> replacePhoneForm, RowBounds bounds);

    UserAuthInfo getUserInfoByPhone(Map<String, Object> param);

    UserAuthInfo getUserAuthByPhone(String phone);

    void updateUserPhone(Map<String, Object> param);

    void updateUserFundPhone(Map<String, Object> paramMap);

    void updateUserRankingPhone(Map<String, Object> paramMap);

    void updateUserSalaryPhone(Map<String, Object> paramMap);

    void updateUserByRankingPhone(Map<String, Object> paramMap);

    void saveReplacePhone(Map<String, Object> paramMap);

    void updateUserSalaryDetailPhone(Map<String, Object> paramMap);

    void updateRegUserCancel(Map<String, Object> param);
}
