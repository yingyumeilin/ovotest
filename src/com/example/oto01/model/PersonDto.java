package com.example.oto01.model;

public class PersonDto {
	private Integer userId;// 用户ID
	private String name;// 姓名
	private String head;// 头像
	private String utype;// 用户类型
	private String phone;
	private String sortLetters; // 显示数据拼音的首字母
	private String suoxie;// 姓名缩写
	private String signature;// 个性签名
	// 是否存在 1、存在 2不存在
	public String is_have;

	public String getIs_have() {
		return is_have;
	}

	public void setIs_have(String is_have) {
		this.is_have = is_have;
	}

	public final Integer getUserId() {
		return userId;
	}

	public final void setUserId(Integer userId) {
		this.userId = userId;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getHead() {
		return head;
	}

	public final void setHead(String head) {
		this.head = head;
	}

	public final String getUtype() {
		return utype;
	}

	public final void setUtype(String utype) {
		this.utype = utype;
	}

	public final String getSortLetters() {
		return sortLetters;
	}

	public final void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public final String getSuoxie() {
		return suoxie;
	}

	public final void setSuoxie(String suoxie) {
		this.suoxie = suoxie;
	}

	public final String getSignature() {
		return signature;
	}

	public final void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}