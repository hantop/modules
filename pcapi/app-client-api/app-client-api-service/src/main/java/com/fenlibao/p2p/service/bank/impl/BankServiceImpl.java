package com.fenlibao.p2p.service.bank.impl;

import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.dao.bank.BankDao;
import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.BankCardVO;
import com.fenlibao.p2p.model.vo.BankVO;
import com.fenlibao.p2p.service.bank.BankService;
import com.fenlibao.p2p.service.pay.ILianLianPayService;
import com.fenlibao.p2p.util.formater.certification.BankCardFormater;
import com.fenlibao.p2p.util.loader.Payment;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LouisWang on 2015/8/15.
 */
@Service
public class BankServiceImpl implements BankService {
	
	private static final Logger logger = LogManager.getLogger(BankService.class);

    @Resource
    private BankDao bankDao;
    @Resource
    private ILianLianPayService lianLianPayService;

	@Override
	public List<Map<String, Object>> buildBankCardsResult(List<BankCard> bankCards, boolean containBaofoo) {
		List<Map<String, Object>> banks = new ArrayList<>();
		Map<String, Object> bankMap;
		for(BankCard card : bankCards) {
			bankMap = new HashMap<>();
			// 格式化身份证
//			String bankNumFormat = IDCardFormater.format(card.getBankNum());
			bankMap.put("bankName", card.getBankName());
//			bankMap.put("bankNum", bankNumFormat);
			
			String temp = card.getBankNumEncrypt();
			try {
				// 格式化銀行卡號 junda.feng 2016-5-20
				temp=StringHelper.decode(temp);
				String bankCard= BankCardFormater.format(temp);
	  			bankMap.put("bankNum", bankCard);
				if (!containBaofoo) {
					bankMap.put("bankRealNum", temp);
				}

			} catch (Throwable e) {
				e.printStackTrace();
			}
  			
			bankMap.put("bankCode", card.getBankCode());
			bankMap.put("bankCardId", card.getBankCardId());
			// 银行卡信息是否完整,比如是否设置了开户行(0:没完善  1:已完善)
			int bankInfoCompleteFlag = 1;
			if(card.getDistrictId() <= 0) {
				bankInfoCompleteFlag = 0;
			}
			bankMap.put("bankInfoCompleteFlag", bankInfoCompleteFlag);
			// 银行卡认证状态
			int bankAuthStatusResult = getBankAuthStatusResult(card.getBankAuthStatus());
			bankMap.put("bankAuthStatus", bankAuthStatusResult);
			banks.add(bankMap);
			//连连通道的标记
			bankMap.put("bankType", 0);
		}
		return banks;
	}

	/**
	 * 银行卡认证状态(1:未认证,2:已认证,3:可提现)
	 * @param bankAuthStatus
	 * @return
	 */
	private int getBankAuthStatusResult(String bankAuthStatus) {
		int bankAuthStatusResult = 0;
		if(StringUtils.isBlank(bankAuthStatus)
				|| bankAuthStatus.equals(Status.WRZ.name())) {
			bankAuthStatusResult = 1;
		} else if(bankAuthStatus.equals(Status.YRZ.name())) {
			bankAuthStatusResult = 2;
		} else if(bankAuthStatus.equals(Status.KTX.name())) {
			bankAuthStatusResult = 3;
		}
		return bankAuthStatusResult;
	}

	@Override
	public List<Map<String, Object>> getBankCardsByUserId(int userId) {
		List<BankCard> bankCards = bankDao.getBankCardsByUserId(userId);
		List<Map<String, Object>> resultList = buildBankCardsResult(bankCards, true);
		return resultList;
	}

	@Override
    public List<BankCardVO> getBankCars(String userId, String status)  throws Exception {
        int acount = 0;
        if(!StringUtils.isEmpty(userId)){
            acount = Integer.parseInt(userId);
        }

        if (StringUtils.isEmpty(status) || acount <= 0) {
            throw new Exception("参数错误");
        }
        /*Map<String,Object> params = new HashMap<String,Object();
        params.put("acount",acount);
        params.put("status",)
        return nubankDao.getBankCars(acount,status);*/
        return null;
    }

    /**
     * S10.1010 SYSTEM.WITHDRAW 提现手续费 先默认都是2 元
     * T6118 交易密码
     * T6101 账户余额
     * @param userId
     * @return
     */
    @Override
    public Map getUserDealStatus(int userId) throws Exception {

        return bankDao.getUserDealStatus(userId);
    }

    /**
     * 查询银行卡是否信息完整
     * @param userId
     * @return
     */
    @Override
    public BankCardVO checkBankCardInfo(int userId) throws Exception {

        return bankDao.checkBankCardInfo(userId);
    }

    @Override
    public Map<String,Object> getBankCardById(int id) throws Exception {
        return bankDao.getBankCardById(id);
    }

    /**
     * 通过bankId 获取银行卡信息
     * @return
     * @throws Exception
     */
    @Override
    public BankVO getBank(int bankId) {
        return bankDao.getBank(bankId);
    }

    /**
     * 通过 bankNum 通过银行卡获取银行所有详细信息
     * @return
     * @throws Exception
     */
    @Override
    public Map getBankCardMsg(int userId,String bankNum) throws Throwable {
        Map retMap = null;
        if (!StringUtils.isBlank(bankNum) || userId > 0){
            String eBankNum = StringHelper.encode(bankNum);
            Map queryMap = new HashMap();
            queryMap.put("userId",userId);
            queryMap.put("bankNum",bankNum);

            retMap = bankDao.getBankCardMsg(queryMap);
        }

        return retMap;
    }

	@Override
	public String getCardNo(int userId) throws Throwable {
		String cardNo = null;
		BankCardVO bankCardVO = this.checkBankCardInfo(userId);
		if (bankCardVO != null) {
			if (BankCardStatus.QY.name().equals(bankCardVO.getStatus())
					&& !InterfaceConst.BANK_CARD_STATUS_WRZ.equals(bankCardVO.getBindStatus())) {
				cardNo = bankCardVO.getBankNumber();
				if (StringUtils.isNotBlank(cardNo))
					cardNo = StringHelper.decode(cardNo);
			}
		}
		return cardNo;
	}

	@Override
	public int updateBankCardInfo(BankCardVO bankInfo) throws Exception {
		return bankDao.updateBankCardInfo(bankInfo);
	}

	@Override
	public int getIdByCode(String code) {
		return bankDao.getIdByCode(code);
	}

	@Override
	public boolean isBindBankCard(int userId, String bankCardNo)
			throws Throwable {
		BankCardVO vo = this.checkBankCardInfo(userId);
		if (vo != null) {
			if (bankCardNo.equals(StringHelper.decode(vo.getBankNumber())) 
					&& !InterfaceConst.BANK_CARD_STATUS_WRZ.equals(vo.getBindStatus())) { 
					return true;
				}
		}
		return false;
	}

	@Override
	public String getBankCardBindStatus(int userId) {
		String status = this.bankDao.getBankCardBindStatus(userId);
		if (StringUtils.isBlank(status)) { //如果没有状态也是未绑定状态
			status = InterfaceConst.BANK_CARD_STATUS_WRZ;
		}
		return status;
	}

	@Override
	public int updateBranchInfo(String cityCode, String branchName, int userId) throws Exception {
		String status = this.getBankCardBindStatus(userId);
		if (InterfaceConst.BANK_CARD_STATUS_WRZ.equals(status)) {
			return -1;
		} 
		if (!InterfaceConst.BANK_CARD_STATUS_KTX.equals(status)) {
			BankCardVO bankInfo = new BankCardVO();
			bankInfo.setAcount(userId);
			bankInfo.setCity(cityCode);
			bankInfo.setBankKhhName(branchName);
			return this.updateBankCardInfo(bankInfo);
		} else {
			return -2;
		}
	}

	@Override
	public boolean isSupportBank(String bankCardNo) throws Throwable {
		String bankCodes = Payment.get(Payment.BANK_CODES); //支持的银行编码
		Map<String, String>  retMap = queryBankCardInfo(bankCardNo);
		String bankCode = retMap.get("bank_code"); // 银行编码
		if (bankCodes.indexOf(bankCode) >= 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryBankCardInfo(String bankCardNo) throws Throwable {
		String returnMsg = lianLianPayService.queryBankCardByNo(bankCardNo);
		Map<String, String>  retMap = new Gson().fromJson(returnMsg, Map.class);
		if (retMap == null || retMap.size() < 1 || !"0000".equals(retMap.get("ret_code"))) {
			logger.error(String.format("[%s]查询银行卡信息失败，原因：%s", bankCardNo,retMap.get("ret_msg")));
			throw new BusinessException(ResponseCode.TRADE_QUERY_CARD_INFO_FAIL);
		}
		return retMap;
	}

	@Override
	public String getXWBankcode(String code) {
		return bankDao.getXWBankcode(code);
	}
}
