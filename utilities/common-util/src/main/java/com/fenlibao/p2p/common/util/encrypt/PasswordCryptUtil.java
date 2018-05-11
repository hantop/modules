package com.fenlibao.p2p.common.util.encrypt;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户密码加密(不可逆)
 * 
 * @author chenzhixuan
 *
 */
public class PasswordCryptUtil {

	/**
	 * 加密
	 * @param password AES和base64编码后的用户密码
	 * @return
	 */
	public static String cryptAESPassword(String password) throws Exception{
		String pwd = null;
		if (StringUtils.isNotBlank(password)) {
			// 密码进行AES解码
			pwd = AES.getInstace().decrypt(password);
			// 加密
			pwd = UnixCrypt.crypt(pwd, DigestUtils.sha256Hex(pwd));
		}
		return pwd;
	}
	
	/**
	 * 加密
	 * @param password 明文密码
	 */
	public static String crypt(String password) {
		String pwd = null;
		if (StringUtils.isNotBlank(password)) {
			pwd = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
		}
		return pwd;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(AES.getInstace().encrypt("13751763759"));
		System.out.println(AES.getInstace().decrypt(""));
	}
}
