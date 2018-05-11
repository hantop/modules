package com.fenlibao.dao.pms.da.statistics.first;

import com.fenlibao.model.pms.da.statistics.invest.FirstInvest;
import com.fenlibao.model.pms.da.statistics.invest.form.FirstInvestForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Bogle on 2016/3/11.
 */
public interface FirstInvestMapper {

    List<FirstInvest> findFirstInvest(FirstInvestForm firstInvestForm, RowBounds rowBounds);

    FirstInvest getFirstInvestTotal(FirstInvestForm firstInvestForm);
}
