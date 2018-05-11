package com.fenlibao.service.pms.da.cs;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.cs.Income;
import com.fenlibao.model.pms.da.cs.form.PageForm;

public interface IncomeService {
    List<Income> incomeList(PageForm pageForm,RowBounds bounds);
}
