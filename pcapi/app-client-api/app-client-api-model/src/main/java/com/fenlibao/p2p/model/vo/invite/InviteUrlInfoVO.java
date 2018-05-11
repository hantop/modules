/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: InviteUrlInfo.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.invite 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-10 下午9:23:05 
 * @version: V1.1   
 */
package com.fenlibao.p2p.model.vo.invite;

/** 
 * @ClassName: InviteUrlInfo 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:23:05  
 */
public class InviteUrlInfoVO {
	
	public String inviteUrl;  //web邀请url
	
	public String inviteInfoMsg; //邀请信息
	
	private String inviteTitle;//邀请信息标题
	
	private String invitePicUrl;//邀请信息中的小图标

	/**
	 * @return the inviteInfoMsg
	 */
	public String getInviteInfoMsg() {
		return inviteInfoMsg;
	}

	/**
	 * @param inviteInfoMsg the inviteInfoMsg to set
	 */
	public void setInviteInfoMsg(String inviteInfoMsg) {
		this.inviteInfoMsg = inviteInfoMsg;
	}

	/**
	 * @return the inviteUrl
	 */
	public String getInviteUrl() {
		return inviteUrl;
	}

	/**
	 * @param inviteUrl the inviteUrl to set
	 */
	public void setInviteUrl(String inviteUrl) {
		this.inviteUrl = inviteUrl;
	}

	public String getInviteTitle() {
		return inviteTitle;
	}

	public void setInviteTitle(String inviteTitle) {
		this.inviteTitle = inviteTitle;
	}

	public String getInvitePicUrl() {
		return invitePicUrl;
	}

	public void setInvitePicUrl(String invitePicUrl) {
		this.invitePicUrl = invitePicUrl;
	}

}
