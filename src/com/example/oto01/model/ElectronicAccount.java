package com.example.oto01.model;
/**
 * 电子账户
 * @author user
 *
 */
public class ElectronicAccount {
	private int shopsid;
	private String MerchName;//商户名称
	private String Name;//姓名
	private int frombank;//归属银行1
	private String PAN;//银行卡号
	
	
	
	
	public ElectronicAccount(int shopsid, String merchName, String name,
			int frombank, String pAN) {
		super();
		this.shopsid = shopsid;
		MerchName = merchName;
		Name = name;
		this.frombank = frombank;
		PAN = pAN;
	}
	
	
	public ElectronicAccount(int shopsid, String merchName, String name,
			String cardNo, String loginId, String mobile, String email,
			String identityImgX, String identityImgY) {
		super();
		this.shopsid = shopsid;
		MerchName = merchName;
		Name = name;
		CardNo = cardNo;
		LoginId = loginId;
		Mobile = mobile;
		Email = email;
		IdentityImgX = identityImgX;
		IdentityImgY = identityImgY;
	}


	private String CardNo;//证件�?
	private String LoginId;//登录�?
	private String Mobile;//手机�?
	private String Email;//电子邮箱
	private String IdentityImgX;//身份证正面（仅限jpg�?
	private String IdentityImgY;//身份证反面（仅限jpg�?
	public int getShopsid() {
		return shopsid;
	}
	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}
	public String getMerchName() {
		return MerchName;
	}
	public void setMerchName(String merchName) {
		MerchName = merchName;
	}
	public String getName() {
		return Name; 
	}
	public void setName(String name) {
		Name = name;
	}
	public int getFrombank() {
		return frombank;
	}
	public void setFrombank(int frombank) {
		this.frombank = frombank;
	}
	public String getPAN() {
		return PAN;
	}
	public void setPAN(String pAN) {
		PAN = pAN;
	}
	public String getCardNo() {
		return CardNo;
	}
	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}
	public String getLoginId() {
		return LoginId;
	}
	public void setLoginId(String loginId) {
		LoginId = loginId;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getIdentityImgX() {
		return IdentityImgX;
	}
	public void setIdentityImgX(String identityImgX) {
		IdentityImgX = identityImgX;
	}
	public String getIdentityImgY() {
		return IdentityImgY;
	}
	public void setIdentityImgY(String identityImgY) {
		IdentityImgY = identityImgY;
	}
	
	
	
}
