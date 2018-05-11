package com.fenlibao.service.pms.da.cs.account.impl;

import com.fenlibao.dao.pms.da.cs.account.UserDetailInfoMapper;
import com.fenlibao.model.pms.da.consts.VersionTypeEnum;
import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.investUser.DueInAmount;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.service.pms.da.cs.account.UserDetailInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/25.
 */
@Service
public class UserDetailInfoServiceImpl implements UserDetailInfoService {
    @Resource
    private UserDetailInfoMapper userDetailInfoMapper;

    @Resource
    private XWAccountDao accountDao;

    @Override
    public Map<String, Object> getUserByAccount(String account) {
        return userDetailInfoMapper.getUserByAccount(account);
    }

    @Override
    public UserDetailInfo getUserDetailInfo(String phoneNum) {
        return userDetailInfoMapper.getUserDetailInfo(phoneNum);
    }
    
    @Override
    public UserDetailInfo getFullUserDetailInfo(String userId) {

        UserDetailInfo userDetailInfo = userDetailInfoMapper.getFullUserDetailInfo(userId);

        if (userDetailInfo != null) {
            XWFundAccount xwFundAccount = accountDao.getFundAccount(Integer.parseInt(userId), SysFundAccountType.XW_INVESTOR_WLZH);
            if (xwFundAccount == null) {
                userDetailInfo.setName("未实名");
                userDetailInfo.setBankcardNum("未绑卡");
                userDetailInfo.setIdcard("未开户");
                userDetailInfo.setInvestingMoney(new BigDecimal(-1414));
                userDetailInfo.setBalance(new BigDecimal(-1414));
                userDetailInfo.setInvestorBlockAmount(new BigDecimal(-1414)); // 投资冻结金额
                userDetailInfo.setInterestReceivedAmount(new BigDecimal(-1414)); // 已收利息
                userDetailInfo.setWithdrawBlockAmount(new BigDecimal(-1414)); // 提现冻结
                userDetailInfo.setInterestingAmount(new BigDecimal(-1414)); // 待收利息
                return userDetailInfo;
            }
            // 银行卡号加星号
            String bankCard = userDetailInfo.getBankcardNum();
            if (StringUtils.isNotEmpty(bankCard)) {
                userDetailInfo.setBankcardNum(bankCard.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", "*"));
            } else {
                userDetailInfo.setBankcardNum("未绑卡");
            }
            BigDecimal investingAmount = userDetailInfoMapper.getUserInvestingAmount(userId, VersionTypeEnum.CG.getIndex());
            if (investingAmount == null) {
                investingAmount = BigDecimal.ZERO;
            }
            userDetailInfo.setInvestingMoney(investingAmount);
            BigDecimal withdrawFreezeAmount = userDetailInfoMapper.getWithdrawFreezeSum(userId, VersionTypeEnum.CG.getCode()); //提现冻结金额
            BigDecimal userFreezeSum = userDetailInfoMapper.getNewTenderFreezeSum(userId, VersionTypeEnum.CG.getIndex()); // 投资冻结金额

            BigDecimal dueInPrincipal = BigDecimal.ZERO; // 待收本金
            BigDecimal dueInGains = BigDecimal.ZERO; // 待收利息

            DueInAmount dueInAmount = userDetailInfoMapper.getNewDueInAmount(userId, VersionTypeEnum.CG.getIndex());//标待收本息
            DueInAmount planDueInAmount = userDetailInfoMapper.getPlanDueInAmount(userId, VersionTypeEnum.CG.getIndex());//计划待收本息

            BigDecimal yHGains =  userDetailInfoMapper.getNewYHGains(userId, VersionTypeEnum.CG.getIndex());//标已获收益
            BigDecimal planYHGains =  userDetailInfoMapper.getPlanYHGains(userId, VersionTypeEnum.CG.getIndex());//计划已获收益

            if (dueInAmount != null && planDueInAmount != null) {
                dueInGains = dueInAmount.getGains().add(planDueInAmount.getGains());
            }else if(null == dueInAmount && planDueInAmount != null) {
                dueInGains = planDueInAmount.getGains();
            }else if(dueInAmount != null && null == planDueInAmount) {
                dueInGains = dueInAmount.getGains();
            }
            if(planYHGains != null) {
                yHGains = planYHGains.add(yHGains);
            }
            userDetailInfo.setInvestorBlockAmount(userFreezeSum); // 投资冻结金额
            userDetailInfo.setInterestReceivedAmount(yHGains); // 已收利息
            userDetailInfo.setWithdrawBlockAmount(withdrawFreezeAmount); // 提现冻结
            userDetailInfo.setInterestingAmount(dueInGains); // 待收利息
        }
    	return userDetailInfo;
    }

	@Override
	public List<UserDetail> getUserDetail(String phoneNum, String name, String idCard, RowBounds bounds) {
		return userDetailInfoMapper.getUserDetail(phoneNum, name, idCard, bounds);
	}

	@Override
	public List<UserDetailInfo> getUserDetailInfoByPhoneOrNameOrIdCard(String phoneNum, String name, String idCard) {
		return userDetailInfoMapper.getUserDetailInfoByPhoneOrNameOrIdCard(phoneNum, name, idCard);
	}
}
