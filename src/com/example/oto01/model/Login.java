package com.example.oto01.model;
/**
 * 商户
 * @author liqq
 *
 */
public class Login {
	private int adminId = -1;
	private boolean rememberPassword;
	private String userName;

	public String getUserName() {
		if (userName == null)
			return "";
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public boolean isRememberPassword() {
		return rememberPassword;
	}

	public void setRememberPassword(boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}

	public Login(String userName, int adminId, boolean isRememberPassword) {
		super();
		this.userName = userName;
		this.adminId = adminId;
		this.rememberPassword = isRememberPassword;
	}
}