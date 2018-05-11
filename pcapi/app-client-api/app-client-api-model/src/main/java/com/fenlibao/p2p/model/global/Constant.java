package com.fenlibao.p2p.model.global;

/**
 * 公共常量
 * 
 * @author chenzhixuan
 *
 */
public class Constant {
	/**
	 * 客户端类型:iphone端
	 */
	public static final int CLIENTTYPE_IPHONE = 1;
	/**
	 * 客户端类型:android端
	 */
	public static final int CLIENTTYPE_ANDROID = 2;
	/**
	 * 客户端类型:ipad客户端
	 */
	public static final int CLIENTTYPE_IPAD = 3;
	/**
	 * 客户端类型:wp端
	 */
	public static final int CLIENTTYPE_WP = 4;
	/**
	 * 客户端类型:wap端
	 */
	public static final int CLIENTTYPE_WAP = 5;
	/**
	 * 客户端类型:微信端
	 */
	public static final int CLIENTTYPE_WEIXIN = 6;
	/**
	 * 客户端类型:pc端
	 */
	public static final int CLIENTTYPE_PC = 7;

	/**
	 * 判断是否是wap端
	 * @param clientType
	 * @return
	 */
	public static boolean isWap(String clientType) {
		return (CLIENTTYPE_WAP + "").equals(clientType) || (CLIENTTYPE_WEIXIN + "").equals(clientType);
	}

}
