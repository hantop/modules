package com.fenlibao.service.pms.idmt.user.impl;

import com.fenlibao.model.pms.idmt.user.PmsUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public class PasswordHelper {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public void encryptPassword(PmsUser user) {
        if(StringUtils.isEmpty(user.getUsername())) throw new RuntimeException("用户名不能为空");
        if(StringUtils.isEmpty(user.getPassword())) throw new RuntimeException("密码不能为空");
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    
    public void encryptPassword(PmsUser user,String salt) {
    	if(StringUtils.isEmpty(user.getUsername())) throw new RuntimeException("用户名不能为空");
    	if(StringUtils.isEmpty(user.getPassword())) throw new RuntimeException("密码不能为空");
    	user.setSalt(salt);
    	String newPassword = new SimpleHash(
    			algorithmName,
    			user.getPassword(),
    			ByteSource.Util.bytes(user.getCredentialsSalt()),
    			hashIterations).toHex();
    	
    	user.setPassword(newPassword);
    }
}
