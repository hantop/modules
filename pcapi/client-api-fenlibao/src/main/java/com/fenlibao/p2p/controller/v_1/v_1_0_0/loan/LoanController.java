package com.fenlibao.p2p.controller.v_1.v_1_0_0.loan;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.p2p.model.api.enums.SystemBoolean;
import com.fenlibao.p2p.model.enums.loan.AnnualIncomeRange;
import com.fenlibao.p2p.model.enums.loan.LoanAmountRange;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.loan.LoanApplicationForm;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.loan.ILoanService;
import com.fenlibao.p2p.util.Validator;

@RestController("v_1_0_0/LoanController")
@RequestMapping("loan")
public class LoanController {

	private static final Logger logger = LogManager.getLogger(LoanController.class);
	
	@Resource
	private ILoanService loanService;
	
	/**
	 * 借款申请
	 * @param form
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "application", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
	public HttpResponse application(BaseRequestForm form, LoanApplicationForm params) {
		HttpResponse response = new HttpResponse();
		if (!form.validate() || !params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			loanService.add(validate(params));
		} catch (BusinessException b) {
			response.setCodeMessage(b.getCode(), b.getMessage());
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error(e.getMessage(), e);
		}
		return response;
	}
	
	/**
	 * 校验、组装参数
	 * @param params
	 * @return
	 */
	private LoanApplicationForm validate(LoanApplicationForm params) throws Exception {
		if (loanService.getUntreatedQty(params.getUserId()) > 0) {
			throw new BusinessException(ResponseCode.LOAN_HAS_UNTREATED.getCode(),
					ResponseCode.LOAN_HAS_UNTREATED.getMessage());
		}
		if (!Validator.isMobile(params.getPhoneNum())) {
			throw new BusinessException(ResponseCode.ACTIVITY_PHONE_REG_NOT_RIGHT.getCode(),
					ResponseCode.ACTIVITY_PHONE_REG_NOT_RIGHT.getMessage());
		}
		LoanAmountRange amountRange = LoanAmountRange.get(params.getAmountRange());
		if (amountRange == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_ERROR.getCode(),
					ResponseCode.COMMON_PARAM_TYPE_ERROR.getMessage());
		}
		AnnualIncomeRange incomeRange = AnnualIncomeRange.get(params.getAnnualIncome());
		if (incomeRange == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_ERROR.getCode(),
					ResponseCode.COMMON_PARAM_TYPE_ERROR.getMessage());
		}
		SystemBoolean room = SystemBoolean.get(params.getRoom());
		if (room == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_ERROR.getCode(),
					ResponseCode.COMMON_PARAM_TYPE_ERROR.getMessage());
		}
		SystemBoolean car = SystemBoolean.get(params.getCar());
		if (car == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_ERROR.getCode(),
					ResponseCode.COMMON_PARAM_TYPE_ERROR.getMessage());
		}
		params.setAmountRange(amountRange.getValue());
		params.setAnnualIncome(incomeRange.getValue());
		return params;
	}
}
