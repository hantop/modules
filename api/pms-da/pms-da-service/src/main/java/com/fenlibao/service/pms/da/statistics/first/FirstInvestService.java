package com.fenlibao.service.pms.da.statistics.first;

import com.fenlibao.dao.pms.da.statistics.first.FirstInvestMapper;
import com.fenlibao.model.pms.da.statistics.invest.FirstInvest;
import com.fenlibao.model.pms.da.statistics.invest.form.FirstInvestForm;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bogle on 2016/3/11.
 */
@Service
public class FirstInvestService {

    @Autowired
    private FirstInvestMapper firstInvestMapper;

    public List<FirstInvest> findFirstInvest(FirstInvestForm firstInvestForm, RowBounds rowBounds) {
        return firstInvestMapper.findFirstInvest(firstInvestForm, rowBounds);
    }

    /**
     * 首投信息汇总
     *
     * @param firstInvestForm
     * @return
     */
    public FirstInvest getFirstInvestTotal(FirstInvestForm firstInvestForm) {
        return firstInvestMapper.getFirstInvestTotal(firstInvestForm);
    }
}
