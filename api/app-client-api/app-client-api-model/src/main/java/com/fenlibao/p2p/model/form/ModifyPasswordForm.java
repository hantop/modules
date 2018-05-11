package com.fenlibao.p2p.model.form;

/**
 * 修改密码请求参数
 * 
 * @author chenzhixuan
 *
 */
public class ModifyPasswordForm {
	private String userId;
	private String oldPassword;
	private String newPassword;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}