package com.fenlibao.dao.pms.da.finance.returngold;

import com.fenlibao.model.pms.da.finance.vo.ReturnExperienceGoldVO;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2016/1/13.
 */

public interface ReturnGoldMapper {

    List<ReturnExperienceGoldVO> getExperienceGoldCostList(Map<String, Object> paramMap, RowBounds bounds);

    List<ReturnExperienceGoldVO> getExperienceGolddetails(Map<String, Object> paramMap,RowBounds bounds);

    BigDecimal getExperienceGoldTotal(Map<String, Object> paramMap);
}
