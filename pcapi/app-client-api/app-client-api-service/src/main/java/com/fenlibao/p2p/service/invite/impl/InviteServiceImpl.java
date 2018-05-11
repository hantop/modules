package com.fenlibao.p2p.service.invite.impl;

import com.fenlibao.p2p.dao.invite.InviteDao;
import com.fenlibao.p2p.model.entity.invite.BeInviterInfo;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;
import com.fenlibao.p2p.model.vo.invite.MyInviteInfoVO;
import com.fenlibao.p2p.service.invite.InviteService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InviteServiceImpl implements InviteService {

    @Resource 
    private InviteDao inviteDao;

    @Override
    public List<BeInviterInfo> getBeinviterInfoList(Integer userId, String inviteStartTime, String inviteEndTime, String beInviterPhonenum, PageBounds pageBounds) {
        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isNotEmpty(inviteStartTime)) {
            startDate =  DateUtil.timestampToDateBySec(Long.valueOf(inviteStartTime));
        }
        if (StringUtils.isNotEmpty(inviteEndTime)) {
            endDate =  DateUtil.timestampToDateBySec(Long.valueOf(inviteEndTime));
        }
        return inviteDao.getBeinviterInfoList(userId, startDate, endDate, beInviterPhonenum, pageBounds);
    }

    @Override
    public List<MyAwordInfo> getMyAwordInfoList(int userId, String inviteStartTime, String inviteEndTime, String beInviterPhonenum, PageBounds pageBounds) throws Exception {
        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isNotEmpty(inviteStartTime)) {
            startDate =  DateUtil.timestampToDateBySec(Long.valueOf(inviteStartTime));
        }
        if (StringUtils.isNotEmpty(inviteEndTime)) {
            endDate =  DateUtil.timestampToDateBySec(Long.valueOf(inviteEndTime));
        }
        return inviteDao.getMyAwordInfoList(userId, startDate, endDate, beInviterPhonenum, pageBounds);
    }

    @Override
	public MyInviteInfoVO getMyInviteInfo(int userId) throws Exception {
        MyInviteInfoVO myInviteInfoVO = new MyInviteInfoVO();
        Map<String, Object> myInviteInfo = inviteDao.getMyInviteInfo(userId);
        myInviteInfoVO.setInviteUserRegNum(Integer.valueOf(myInviteInfo.get("inviteUserRegNum").toString()));
        myInviteInfoVO.setInviteUserInvNum(Integer.valueOf(myInviteInfo.get("inviteUserInvNum").toString()));
        myInviteInfoVO.setInviteAwardSum(inviteDao.getInviteAwardSum(userId));
        return myInviteInfoVO;
	}
    
    @Override
    public List<UserInviteInfo> getUserInviteInfoList(int userId, int pageNum){
    	
    	return inviteDao.getUserInviteInfoList(userId,pageNum);
    }
    
}
