package com.example.oto01.model;
/**
 * 管家
 * @author liqq
 *
 */
public class Houser {


	/**
	 * name	string	管家姓名
phone	string	管家电话
disnum	string	配单�?
goodsnum	string	货号
	 */
	
	private String name;
	private String phone;
	private String disnum;
	private String goodsnum;
	private String errormsg;
	
	
	public Houser(String name, String phone, String disnum, String goodsnum) {
		super();
		this.name = name;
		this.phone = phone;
		this.disnum = disnum;
		this.goodsnum = goodsnum;
	}
	public Houser(String errormsg) {
		super();
		this.errormsg = errormsg;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDisnum() {
		return disnum;
	}
	public void setDisnum(String disnum) {
		this.disnum = disnum;
	}
	public String getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}
	
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
}
