package com.fenlibao.service.pms.da.finance.returngold;

import com.fenlibao.model.pms.da.finance.form.ReturnGoldForm;
import com.fenlibao.model.pms.da.finance.vo.ReturnExperienceGoldVO;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Louis Wang on 2016/1/12.
 */
public interface ReturnGoldService {
    /**
     * 获取体验金列表
     */
    List<ReturnExperienceGoldVO> getExperienceGoldCostList(ReturnGoldForm returnGoldForm, RowBounds bounds);

    /**
     * 获取体验金详情
     */
    List<ReturnExperienceGoldVO> getExperienceGolddetails(Integer id, String startTime, String endTime, String telphone, RowBounds bounds);

    /**
     * 获取体验金总收益
     */
    BigDecimal getExperienceGoldTotal(ReturnGoldForm returnGoldForm);
}
