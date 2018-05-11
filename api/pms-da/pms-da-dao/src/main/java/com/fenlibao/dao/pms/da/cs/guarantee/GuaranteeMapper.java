package com.fenlibao.dao.pms.da.cs.guarantee;

import com.fenlibao.model.pms.da.cs.GuaranteeInfo;
import com.fenlibao.model.pms.da.cs.RechargePreInfo;
import com.fenlibao.model.pms.da.cs.form.GuaranteeForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by zeronx on 2017/6/29.
 */
public interface GuaranteeMapper {
    /**
     * 获取担保人管理列表
     * @param guaranteeForm
     * @param bounds
     * @return
     */
    List<GuaranteeInfo> getGuaranteeInfoList(GuaranteeForm guaranteeForm, RowBounds bounds);

    Integer isAllowUnbindBank(@Param("userId") Integer userId, @Param("userRole") String guaranteecorp);

    RechargePreInfo getRechargePreInfoByUserId(String userId);
}
