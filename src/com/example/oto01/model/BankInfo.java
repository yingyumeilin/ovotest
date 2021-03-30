package com.example.oto01.model;
/**
 * 银行信息
 * @author sujian
 *
 */
public class BankInfo {

	
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankLogo() {
		return bankLogo;
	}

	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getIsAcNo() {
		return isAcNo;
	}

	public void setIsAcNo(String isAcNo) {
		this.isAcNo = isAcNo;
	}

	public String getIsName() {
		return isName;
	}

	public void setIsName(String isName) {
		this.isName = isName;
	}

	public String getIsExpireDate() {
		return isExpireDate;
	}

	public void setIsExpireDate(String isExpireDate) {
		this.isExpireDate = isExpireDate;
	}

	public String getIsCertType() {
		return isCertType;
	}

	public void setIsCertType(String isCertType) {
		this.isCertType = isCertType;
	}

	public String getIsCertNo() {
		return isCertNo;
	}

	public void setIsCertNo(String isCertNo) {
		this.isCertNo = isCertNo;
	}

	public String getIsCvv() {
		return isCvv;
	}

	public void setIsCvv(String isCvv) {
		this.isCvv = isCvv;
	}

	public String getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(String isMobile) {
		this.isMobile = isMobile;
	}

	private String bankId;
	private String bankName;
	private String bankLogo;
	private String openType;
	private String isAcNo;
	private String isName;
	private String isExpireDate;
	private String isCertType;
	private String isCertNo;
	private String isCvv;
	private String isMobile;
	
	public BankInfo(String bankId, String bankName, String bankLogo, String openType, String isAcNo,
			String isName, String isExpireDate, String isCertType, String isCertNo, String isCvv, String isMobile) {
		super();
		this.bankId = bankId;
		this.bankName = bankName;
		this.bankLogo = bankLogo;
		this.openType = openType;
		this.isAcNo = isAcNo;
		this.isName = isName;
		this.isExpireDate = isExpireDate;
		this.isCertType = isCertType;
		this.isCertNo = isCertNo;
		this.isCvv = isCvv;
		this.isMobile = isMobile;
	}
	public BankInfo(String bankId, String bankName) {
		super();
		this.bankId = bankId;
		this.bankName = bankName;
	}
	
//	@Override
//	public String toString() {
//		return "City [id=" + id + ", name=" + name + ", pid=" + pid + ", path="
//				+ path + ", longitude=" + longitude + ", latitude=" + latitude
//				+ ", level=" + level + "]";
//	}

	
	
	
}
