package com.fenlibao.p2p.util.email;

import com.fenlibao.p2p.common.util.encrypt.Md5Coder;

public class GenerateLinkUtils {

	/**
	 * 生成邮箱认证的链接
	 * @param param
	 * @return
	 */
	public static String generateEmailCheckLink(EmailParam param) {
		StringBuffer sb=new StringBuffer();
		sb.append(param.getRedirecturl()).append("?userId=").append(param.getUserId())
		.append("&type=").append(param.getType()).append("&email=").append(param.getEmail()).append("&checkCode=").append(param.getSign());
        return sb.toString();
    }

	/**
     * 生成验证帐户的MD5校验码
     * @return 通过md5加密后的16进制格式的字符串
     */
	public static String generateCheckcode(EmailParam param) {
        String userId =String.valueOf(param.getUserId());
        int type = param.getType();
        long date=param.getExceedTime().getTime();
        String email=param.getEmail();
        return Md5Coder.md5(userId + "$" + date + "$" + type + "$" + email);
    }

	/**
     * 验证校验码是否和发送邮件时发送的验证码一致
     * @param checkCode 发送的校验码
     * @return 如果一致返回true，否则返回false
     */
	 public static boolean verifyCheckcode(EmailParam param,String checkCode) {
	        String genCheckcode=generateCheckcode(param);
	        return genCheckcode.equals(checkCode);  
	 }

}
