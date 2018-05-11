package com.fenlibao.p2p.util.xinwang;

import com.fenlibao.p2p.model.xinwang.bo.UserRoleBO;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import org.apache.commons.lang3.StringUtils;

public class UserRoleUtil {
    public static UserRoleBO parseUserNo(String platformUserNo) {
        if (StringUtils.isEmpty(platformUserNo)) {
            throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
        }
        UserRoleBO bo = new UserRoleBO();
        String[] strs = platformUserNo.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        bo.setUserRole(strs[0]);
        bo.setUserId(Integer.parseInt(strs[1]));
        return bo;
    }
}
