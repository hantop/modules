package com.fenlibao.service.pms.da.biz.loanOverview.impl;

import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.dao.pms.da.biz.loanOverview.LoanOverviewMapper;
import com.fenlibao.model.pms.common.KV;
import com.fenlibao.model.pms.common.global.ProductTypeEnum;
import com.fenlibao.model.pms.common.producttype.ProductType;
import com.fenlibao.model.pms.da.biz.LoanOverview;
import com.fenlibao.model.pms.da.biz.form.LoanOverviewForm;
import com.fenlibao.service.pms.common.producttype.ProductTypeService;
import com.fenlibao.service.pms.da.biz.loanOverview.LoanOverviewService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class LoanOverviewServiceImpl implements LoanOverviewService{

	@Autowired
	LoanOverviewMapper loanOverviewMapper;
	@Autowired
	private ProductTypeService productTypeService;

	@Override
	public List<LoanOverview> getLoanList(LoanOverviewForm loanOverviewForm, RowBounds bounds) {
		return loanOverviewMapper.getLoanList(_buildPropties(loanOverviewForm), bounds);
	}

	@Override
	public Map<String, String> getLoanTotal(LoanOverviewForm loanOverviewForm) {
		return loanOverviewMapper.getLoanTotal(_buildPropties(loanOverviewForm));
	}

	/**
	 * 产品类型
	 * @return
	 * @param productType
	 */
	private List<Integer> buildProductTypes(String productType) {
		List<KV<String, String>> tempProductTypes = productTypeService.getProductTypes();
		// 消费金融产品类型id
		ProductType consumerLoanPT = productTypeService.getProductType(ProductTypeEnum.CONSUMER_LOAN.getCode());
		int consumerLoanId = 0;
		if (consumerLoanPT != null) {
			consumerLoanId = consumerLoanPT.getId();
		}
		List<Integer> productTypes = new LinkedList<>();
		// 如果是消费金融就不添加
        if (StringUtils.isBlank(productType)) {
            for (KV kv : tempProductTypes) {
				if (!kv.getKey().toString().equals(String.valueOf(consumerLoanId))) {
					productTypes.add(Integer.valueOf(kv.getKey().toString()));
				}
			}
        } else {
			productTypes.add(Integer.valueOf(productType));
        }
		return productTypes;
	}

	private LoanOverviewForm _buildPropties(LoanOverviewForm loanOverviewForm) {
		LoanOverviewForm formForQuery = new LoanOverviewForm();
		formForQuery.setTenderStartDate(StringHelper.isEmpty(loanOverviewForm.getTenderStartDate())?null:loanOverviewForm.getTenderStartDate()+" 00:00:00");
		formForQuery.setTenderEndDate(StringHelper.isEmpty(loanOverviewForm.getTenderEndDate())?null:loanOverviewForm.getTenderEndDate()+" 23:99:99");
		formForQuery.setLoanStartDate(StringHelper.isEmpty(loanOverviewForm.getLoanStartDate())?null:loanOverviewForm.getLoanStartDate()+" 00:00:00");
		formForQuery.setLoanEndDate(StringHelper.isEmpty(loanOverviewForm.getLoanEndDate())?null:loanOverviewForm.getLoanEndDate()+" 23:99:99");
		formForQuery.setRepayStartDate(StringHelper.isEmpty(loanOverviewForm.getRepayStartDate())?null:loanOverviewForm.getRepayStartDate()+" 00:00:00");
		formForQuery.setRepayEndDate(StringHelper.isEmpty(loanOverviewForm.getRepayEndDate())?null:loanOverviewForm.getRepayEndDate()+" 23:99:99");
		formForQuery.setBidStatus(loanOverviewForm.getBidStatus());
		formForQuery.setBidTitle(loanOverviewForm.getBidTitle());
		formForQuery.setBorrowerAccount(loanOverviewForm.getBorrowerAccount());
		formForQuery.setBorrowerName(loanOverviewForm.getBorrowerName());
		formForQuery.setSort(loanOverviewForm.getSort());
		// 产品类型
		formForQuery.setProductTypes(buildProductTypes(loanOverviewForm.getProductType()));
		formForQuery.setSysType(loanOverviewForm.getSysType());
		return formForQuery;
	}

}
