package com.fenlibao.p2p.controller.v_1.v_1_0_0.trade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.pay.BranchInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestFormExtend;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.withdraw.IAipgManageService;
import com.fenlibao.p2p.service.withdraw.IAipgWithdrawService;
import com.fenlibao.p2p.service.withdraw.IUserWithdrawalsService;
import com.fenlibao.p2p.service.withdraw.IWithdrawManageService;
import com.fenlibao.p2p.service.withdraw.IWithdrawService;
import com.fenlibao.p2p.util.loader.Payment;

/**
 * 提现控制器
 * @author yangzengcai
 * @date 2015年8月17日
 */
@RestController("v_1_0_0/WithdrawController")
@RequestMapping("lianlianPay")
public class WithdrawController {

	protected static final Logger logger = LogManager.getLogger(WithdrawController.class);

    @Resource
    private IWithdrawManageService withdrawManageService;
    @Resource
    private IUserWithdrawalsService userWithdrawalsService;
    @Resource
    private IAipgWithdrawService aipgWithdrawService;
    @Resource
    private IWithdrawService withdrawService;
    @Resource
    private BankService bankService;
    @Resource
    private IAipgManageService aipgManageService;
	
	/**
	 * 提现
	 * @return
	 */
	@RequestMapping(value = "withdraw", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
	public HttpResponse withdraw(@ModelAttribute BaseRequestFormExtend params,
			String amount, String bankCardId, String tradePassword, String cityCode, String branchName) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || StringUtils.isBlank(amount) || StringUtils.isBlank(bankCardId) || StringUtils.isBlank(tradePassword)) {
			response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
			return response;
		}
		try {
			int cardId = IntegerParser.parse(bankCardId);
			int userId = params.getUserId();
			int accountId = InterfaceConst.BACK_AUTO_OPERATION_ACCOUNT_ID;
			int[] orderIds = null;
	        BigDecimal withdrawAmount = new BigDecimal(amount);
			boolean txkcfs = Boolean.parseBoolean(Payment.get(Payment.TXSXF_KCFS));
			Map<String,Object> data = new HashMap<String, Object>();
			Map<String, String> param = new HashMap<>();
			
			tradePassword = AES.getInstace().decrypt(tradePassword);
			
			param.put("clientType", params.getClientType());
			data.put("amount", amount);
			data.put("poundage", "");
			response.setData(data);
/////////////////////////////////把相关的定义和参数的设置都放在上面，避免在后面发生异常/////////////////////////////////////	        
			
	        int withdrawOrderId = withdrawManageService.withdraw(withdrawAmount, tradePassword, cardId, T6101_F03.WLZH,txkcfs, userId);//添加提现订单申请
        	
//			if (new BigDecimal(Payment.get(Payment.WITHDRAW_LIMIT_FUNDS)).compareTo(withdrawAmount) > 0) { //超过10万需要后台审核
				try {
			        if (StringUtils.isNoneBlank(cityCode, branchName)) { //添加支行信息
			        	saveBranchInfo(cityCode, branchName, userId, withdrawOrderId);
			        }
					//模拟审核
					userWithdrawalsService.check(true, "后台自动审核", accountId, withdrawOrderId);
					//模拟放款
					orderIds = userWithdrawalsService.fk(true, "后台自动放款", accountId, withdrawOrderId);
					if (orderIds == null) {
						throw new BusinessException(ResponseCode.TRADE_WITHDRAW_FAIL.getCode(),
								ResponseCode.TRADE_WITHDRAW_FAIL.getMessage());
					}
				} catch (Exception e) {
					aipgManageService.withdrawRollback(withdrawOrderId, e.getMessage());
					logger.error("withdraw fail", e);
					throw e;
				}
				for (int orderId : orderIds) {
					aipgWithdrawService.submit(orderId, param); //提交订单(只有一个)
				}
//			}
		} catch (LogicalException logical) {
			logical.printStackTrace();
			response.setCodeMessage(ResponseEnum.WITHDRAW_FAILURE.getCode(), logical.getMessage());
		} catch (BusinessException b) {
			response.setCodeMessage(b.getCode(), b.getMessage());
		} catch (Throwable e) {
			response.setCodeMessage(ResponseEnum.WITHDRAW_FAILURE.getCode(), ResponseEnum.WITHDRAW_FAILURE.getMessage());
			logger.error(String.format("withdraw failrue,userId[%s]", params.getUserId()), e);
		}
		return response;
	}
	
	/**
	 * 保存支行信息
	 * @param cityCode
	 * @param branchName
	 * @param userId
	 * @param withdrawOrderId
	 * 
	 * @throws Exception
	 */
	private void saveBranchInfo(String cityCode, String branchName, 
			int userId, int withdrawOrderId) throws Exception {
		int result = bankService.updateBranchInfo(cityCode, branchName, userId); //这里往银行卡表t6114中添加，后面提现需要用到
    	if (result == -1) {
    		throw new LogicalException(ResponseEnum.NOT_BIND_BANK_CARD.getMessage());
    	} else if (result > 0) {
    		BranchInfo info = new BranchInfo(withdrawOrderId, cityCode, branchName);
    		result = withdrawService.saveBranchInfo(info); //这里保存支行信息和提现订单的关系，最后以提现成功的订单的支行信息为准
    		if (result < 1) {
    			throw new LogicalException(ResponseEnum.ADD_BRANCH_INFO_FAIL.getMessage());
    		}
    	}
	}
	
}
