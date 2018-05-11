package com.fenlibao.p2p.controller.v_3.v_3_2_0.loan;

import com.fenlibao.p2p.model.api.enums.SystemBoolean;
import com.fenlibao.p2p.model.enums.CaptchaType;
import com.fenlibao.p2p.model.enums.loan.AnnualIncomeRange;
import com.fenlibao.p2p.model.enums.loan.LoanAmountRange;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.loan.LoanApplicationForm;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.loan.ILoanService;
import com.fenlibao.p2p.service.sms.SmsExtracterService;
import com.fenlibao.p2p.util.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("v_3_2_0/LoanController")
@RequestMapping(value = "loan", headers = APIVersion.v_3_2_0)
public class LoanController {

	private static final Logger logger = LogManager.getLogger(LoanController.class);
	
	@Resource
	private ILoanService loanService;
	@Resource
	private SmsExtracterService smsService;

	/**
	 * 借款申请
	 * @param form
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "application", method = RequestMethod.POST)
	public HttpResponse application(BaseRequestForm form, LoanApplicationForm params) {
		HttpResponse response = new HttpResponse();
		if (!form.validate() || !params.validate()) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			loanService.add(validate(params));
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
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
		String phoneNum = params.getPhoneNum();
		String captcha = params.getCaptcha();
		smsService.captchaValidate(phoneNum, captcha, CaptchaType.LOAN_APPCATION.getCode().toString());
		if (!Validator.isMobile(phoneNum)) {
			throw new BusinessException(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
		}
		if (loanService.getUntreatedQty(phoneNum) > 0) {
			throw new BusinessException(ResponseCode.LOAN_HAS_UNTREATED.getCode(),
					ResponseCode.LOAN_HAS_UNTREATED.getMessage());
		}
		LoanAmountRange amountRange = LoanAmountRange.get(params.getAmountRange());
		if (amountRange == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_WRONG);
		}
		AnnualIncomeRange incomeRange = AnnualIncomeRange.get(params.getAnnualIncome());
		if (incomeRange == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_WRONG);
		}
		SystemBoolean room = SystemBoolean.get(params.getRoom());
		if (room == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_WRONG);
		}
		SystemBoolean car = SystemBoolean.get(params.getCar());
		if (car == null) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_TYPE_WRONG);
		}
		params.setAmountRange(amountRange.getValue());
		params.setAnnualIncome(incomeRange.getValue());
		return params;
	}

	/**
	 * 获取用户是否还存在未审核的借款
	 * @param form
	 * @param phoneNum
     * @return
     */
	@RequestMapping(value = "application/isexist", method = RequestMethod.GET)
	public HttpResponse application(BaseRequestForm form, String phoneNum) {
		HttpResponse response = new HttpResponse();
		if (!form.validate() || StringUtils.isBlank(phoneNum)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			Integer count = loanService.getUntreatedQty(phoneNum);
			if (count != null && count > 0) {
				response.setCodeMessage(ResponseCode.LOAN_HAS_UNTREATED);
			}
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error(e.getMessage(), e);
		}
		return response;
	}
}
