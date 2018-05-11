package com.fenlibao.p2p.service.trade.pay.impl;

import com.fenlibao.p2p.model.user.vo.UserBankCardVO;
import com.fenlibao.p2p.service.trade.pay.BindCardService;
import com.fenlibao.p2p.service.user.UserService;
import com.fenlibao.p2p.util.api.StringHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class BindCardServiceImpl implements BindCardService {

    @Resource
    UserService userService;

    @Override
    public boolean bindCard(int userId, String cardNo, String acName, Integer bankId,String reservedPhone) throws Exception {
        boolean isChanged = false;
        UserBankCardVO userBankCard = userService.getBankCard(userId);
        if (userBankCard == null) {
            String cardNoEncrypt = StringHelper.encode(cardNo);
            cardNo = StringHelper.getBankCardNoAsterisk(cardNo);
            userBankCard = new UserBankCardVO().forUser(userId, cardNo, cardNoEncrypt, acName, bankId,reservedPhone);
            userService.addBankCard(userBankCard);
        } else{ 
        	UserBankCardVO param = new UserBankCardVO();
			if (!cardNo.equals(StringHelper.decode(userBankCard.getCardNoEncrypt()))) {
				String cardNoEncrypt = StringHelper.encode(cardNo);
				param.setCardNoEncrypt(cardNoEncrypt);
				cardNo = StringHelper.getBankCardNoAsterisk(cardNo);
				param.setCardNo(cardNo);
				param.setAuthStatus("WRZ");// 更换银行卡后都解绑，因为这个状态只针对连连，改成WRZ是为了预防以后接回连连
				isChanged = true;
			}
			param.setUserId(userId);
			param.setBankId(bankId);
			param.setQYStatus("QY");
			param.setReservedPhone(reservedPhone);
			userService.updateBankCard(param);
        }
        return isChanged;
    }

	@Override
	public void unBindCard(int userId) throws Exception {
		UserBankCardVO param = new UserBankCardVO();
		param.setUserId(userId);
		param.setAuthStatus("WRZ");
		userService.updateBankCard(param);
	}
}
