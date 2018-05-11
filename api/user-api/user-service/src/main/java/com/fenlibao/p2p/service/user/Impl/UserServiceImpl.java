package com.fenlibao.p2p.service.user.Impl;

import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.user.entity.T5020;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.entity.T6118;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;
import com.fenlibao.p2p.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zcai on 2016/10/10.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public UserInfoEntity get(Integer userId, String phoneNum) {
        return userDao.get(userId, phoneNum);
    }

	@Override
	public void addBankCard(UserBankCardVO bankCard) throws Exception {
        userDao.addBankCard(bankCard);
	}

    @Override
    public UserBankCardVO getBankCard(int userId) {
        return userDao.getBankCard(userId);
    }

    @Override
    public void updateBankCard(UserBankCardVO userBankCard) throws Exception {
    	userDao.updateBankCard(userBankCard);
    }

    @Override
    public void initFundAccount(List<AssetAccount> accounts) {
        userDao.initFundAccount(accounts);
    }

    @Override
    public AssetAccount getFundAccount(int userId, T6101_F03 type) {
        return userDao.getFundAccount(userId, type);
    }

    @Override
    public AssetAccount getFundAccountByF04(String account) {
        return userDao.getFundAccountByF04(account);
    }

	@Override
	public T6118 getAuthInfo(int userId) {
		return userDao.getAuthInfo(userId);
	}

	@Override
	public T5020 getBank(T5020 t5020) {
		return userDao.getBank(t5020);
	}

}
