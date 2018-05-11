package com.fenlibao.pms.shiro.listener;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.fenlibao.model.pms.idmt.log.PmsLog;
import com.fenlibao.service.pms.idmt.log.PmsLogService;
import com.fenlibao.service.pms.idmt.permit.PmsPermitService;
import com.fenlibao.service.pms.idmt.permit.impl.PmsPermitServiceImpl;

/**
 * Created by Bogle on 2016/2/2.
 */
public class CustomAuthenticationListener implements AuthenticationListener {

	PmsLogService pmsLogService; 

	public void setPmsLogService (PmsLogService pmsLogService) {
		 this.pmsLogService= pmsLogService;
    }

    public PmsLogService getPmsLogService () {
		 return pmsLogService;
    }

    @Autowired
    private UserDetailsService pmsUserService;


    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        System.out.println("onSuccess");
        PmsLog log=new PmsLog();
        UsernamePasswordToken t=(UsernamePasswordToken)token;
        log.setIp(t.getHost());
        log.setUserName(t.getUsername());
        log.setOperation("login");
        pmsLogService.saveLog(log);
        //登陆成功，密码错误次数将被清零
        String username = token.getPrincipal().toString();
        PmsUser user = pmsUserService.findByUsername(username);
        user.setErrorNumber(0);
        pmsUserService.updateErrorNumber(user);
    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {
        String username = token.getPrincipal().toString();
        PmsUser user = pmsUserService.findByUsername(username);
        Class message = ae.getClass();
        if (IncorrectCredentialsException.class == message) {
            if(user != null && user.getErrorNumber()<5){
                user.setErrorNumber(user.getErrorNumber()+1);
                pmsUserService.updateErrorNumber(user);
                if(user.getErrorNumber()==5)
                {
                    user.setStatus(0);
                    pmsUserService.updateStatus(user);
                }
            }

        }
        System.out.println("onFailure");
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        System.out.println("onLogout");
        Subject currentUser=SecurityUtils.getSubject();
        PmsLog log=new PmsLog();
        log.setIp(currentUser.getSession().getHost());
        log.setUserName(principals.getPrimaryPrincipal().toString());
        log.setOperation("logout");
        pmsLogService.saveLog(log);
    }
}
