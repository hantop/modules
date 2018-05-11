package com.fenlibao.service.pms.da.cs.impl;

import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.dao.pms.da.cs.InviteMapper;
import com.fenlibao.model.pms.da.cs.Invite;
import com.fenlibao.model.pms.da.cs.form.PageForm;
import com.fenlibao.service.pms.da.cs.InviteService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InviteServiceImpl implements InviteService{
	@Resource
	private InviteMapper inviteMapper;

	@Override
	public List<Invite> inviteList(PageForm pageForm, RowBounds bounds) {
		String enddate=pageForm.getEndDate();
		if(!StringHelper.isNull(enddate)){
			pageForm.setEndDate(enddate + " 23:59:59");
		}
		List<Invite> list = inviteMapper.inviteList(pageForm, bounds);
		pageForm.setEndDate(enddate);
		List<Integer> beinvitedIds = new ArrayList<>();
 		if(list.size() > 0){
			for (Invite invite: list) {
				if(!beinvitedIds.contains(invite.getBeinvitedId())){
					beinvitedIds.add(invite.getBeinvitedId());
				}
			}
			List<Map<String, Object>> investMoneyList = inviteMapper.getInvestMoney(beinvitedIds);
			List<Map<String, Object>> holddebtList = inviteMapper.getHolddebt(beinvitedIds);
			List<Map<String, Object>> repaymentList = inviteMapper.getRepayment(beinvitedIds);
			for (Invite invite: list) {
				Map<String, Object> investMoneyMap = getBeinvitedUserDetail(investMoneyList, invite.getBeinvitedId().toString());
				Map<String, Object> holddebtMap = getBeinvitedUserDetail(holddebtList, invite.getBeinvitedId().toString());
				Map<String, Object> repaymentMap = getBeinvitedUserDetail(repaymentList, invite.getBeinvitedId().toString());
				String investMoney = null;
				String holddebt = null;
				String repayment = null;
				if(investMoneyMap != null){
					investMoney = investMoneyMap.get("investMoney").toString() == null ? null : investMoneyMap.get("investMoney").toString();
				}
				if(holddebtMap != null){
					holddebt = holddebtMap.get("holddebt").toString() == null ? null : holddebtMap.get("holddebt").toString();
				}
				if(repaymentMap != null){
					repayment = repaymentMap.get("repayment").toString() == null ? null : repaymentMap.get("repayment").toString();
				}
				if(investMoney != null){
					invite.setHasBeenInvest(new BigDecimal(investMoney));
					invite.setInvestingMoney(new BigDecimal(investMoney));
					if(holddebt != null){
						invite.setHasBeenInvest(invite.getHasBeenInvest().add(new BigDecimal(holddebt)));
						invite.setInvestingMoney(invite.getInvestingMoney().add(new BigDecimal(holddebt)));
						if (repayment != null) {
							invite.setHasBeenInvest(invite.getHasBeenInvest().add(new BigDecimal(repayment)));
						}
					}
				}else if(holddebt != null){
					invite.setHasBeenInvest(new BigDecimal(holddebt));
					invite.setInvestingMoney(new BigDecimal(holddebt));
					if (repayment != null) {
						invite.setHasBeenInvest(invite.getHasBeenInvest().add(new BigDecimal(repayment)));
					}
				}else if(repayment != null){
					invite.setHasBeenInvest(new BigDecimal(repayment));
				}

			}
		}
		return list;
	}

	private Map<String, Object> getBeinvitedUserDetail(List<Map<String, Object>> beinvitedUserDetail, String beinvitedId){
		for (Map<String, Object> beinvitedUserDetailTemp : beinvitedUserDetail) {
			if (beinvitedId.toString().equals(beinvitedUserDetailTemp.get("beinvitedId").toString())) {
				return beinvitedUserDetailTemp;
			}
		}
		return null;
	}
}
