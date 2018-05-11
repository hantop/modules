package com.fenlibao.p2p.controller.v_3.v_3_2_0.trade;

import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.model.entity.pay.BranchInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.withdraw.*;
import com.fenlibao.p2p.util.loader.Payment;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 提现控制器
 * @author yangzengcai
 * @date 2015年8月17日
 */
@RestController("v_3_2_0/WithdrawController")
@RequestMapping(value = "lianlianPay", headers = APIVersion.v_3_2_0)
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
	@Resource
	private RedisService redisService;
	
	/**
	 * 提现
	 * @return
	 */
	@RequestMapping(value = "withdraw", method = RequestMethod.POST)
	public HttpResponse withdraw(@ModelAttribute BaseRequestFormExtend params,
			String amount, String bankCardId, String tradePassword, String cityCode, String branchName) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || StringUtils.isBlank(amount)
				|| StringUtils.isBlank(bankCardId) || StringUtils.isBlank(tradePassword)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(params.getUserId().toString());
		if (redisService.existsKey(requestCacheKey)) {
			response.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
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
			
			param.put("clientType", params.getClientType());
			data.put("amount", amount);
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
						throw new BusinessException(ResponseCode.PAYMENT_WITHDRAW_FAIL);
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
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
		} catch (Throwable e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error(String.format("withdraw failrue,userId[%s]", params.getUserId()), e);
		} finally {
			redisService.removeKey(requestCacheKey);
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
    		throw new BusinessException(ResponseCode.PAYMENT_UNBOUND_BANK_CARD);
    	} else if (result > 0) {
    		BranchInfo info = new BranchInfo(withdrawOrderId, cityCode, branchName);
    		result = withdrawService.saveBranchInfo(info); //这里保存支行信息和提现订单的关系，最后以提现成功的订单的支行信息为准
    		if (result < 1) {
    			throw new BusinessException(ResponseCode.FAILURE);
    		}
    	}
	}
	
}
