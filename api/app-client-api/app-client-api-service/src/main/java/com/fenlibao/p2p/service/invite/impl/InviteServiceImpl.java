package com.fenlibao.p2p.service.invite.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.invite.InviteDao;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;
import com.fenlibao.p2p.service.invite.InviteService;

@Service
public class InviteServiceImpl implements InviteService {

    @Resource 
    InviteDao inviteDao;
    
    @Override
	public Map<String, Object> getMyInviteInfo(int userId){
		return inviteDao.getMyInviteInfo(userId);
	}
    
    @Override
    public List<UserInviteInfo> getUserInviteInfoList(int userId, int pageNum){
    	
    	return inviteDao.getUserInviteInfoList(userId,pageNum);
    }
    
    @Override
    public Map<String, Object>  countMyInviteInfo(int userId){
    	
    	return inviteDao.countMyInviteInfo(userId);
    }
    
    @Override
    public List<UserInviteInfo> getMyInviteInfoList(int userId, int pageNo, int pagesize){
    	
    	return inviteDao.getMyInviteInfoList(userId,pageNo,pagesize);
    }

	@Override
	public List<MyAwordInfo> getMyAwordInfoList(int userId, int pageNo,
			int pagesize) {
		return  inviteDao.getMyAwordInfoList(userId, pageNo, pagesize);
	}
    
}
