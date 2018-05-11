package com.fenlibao.service.pms.da.cs.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.dao.pms.da.cs.IncomeMapper;
import com.fenlibao.model.pms.da.cs.Income;
import com.fenlibao.model.pms.da.cs.form.PageForm;
import com.fenlibao.service.pms.da.cs.IncomeService;

@Service
public class IncomeServiceImpl implements IncomeService {

	@Resource
	private IncomeMapper incomeMapper;

	@Override
	public List<Income> incomeList(PageForm pageForm, RowBounds bounds) {
		String enddate=pageForm.getEndDate();
		if(!StringHelper.isNull(enddate)){
			pageForm.setEndDate(enddate + " 23:59:59");
		}
		List<Income> list=incomeMapper.incomeList(pageForm, bounds);
		pageForm.setEndDate(enddate);
		return list;
	}

}
