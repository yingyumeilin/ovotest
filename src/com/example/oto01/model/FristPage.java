package com.example.oto01.model;

public class FristPage {
	private String shopsname;
	private String total_today;
	private String total_yesterday;
	private int waitorder;
	private int haveorder;
	private String location;
	private String picpath;
	public String getShopsname() {
		return shopsname;
	}
	public void setShopsname(String shopsname) {
		this.shopsname = shopsname;
	}
	public String getTotal_today() {
		return total_today;
	}
	public void setTotal_today(String total_today) {
		this.total_today = total_today;
	}
	public String getTotal_yesterday() {
		return total_yesterday;
	}
	public void setTotal_yesterday(String total_yesterday) {
		this.total_yesterday = total_yesterday;
	}
	public int getWaitorder() {
		return waitorder;
	}
	public void setWaitorder(int waitorder) {
		this.waitorder = waitorder;
	}
	public int getHaveorder() {
		return haveorder;
	}
	public void setHaveorder(int haveorder) {
		this.haveorder = haveorder;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}
	public FristPage(String shopsname, String total_today,
			String total_yesterday, int waitorder, int haveorder) {
		super();
		this.shopsname = shopsname;
		this.total_today = total_today;
		this.total_yesterday = total_yesterday;
		this.waitorder = waitorder;
		this.haveorder = haveorder;
	}
	
	
	public FristPage(String shopsname, String total_today,
			String total_yesterday, int waitorder, int haveorder,
			String location, String picpath) {
		super();
		this.shopsname = shopsname;
		this.total_today = total_today;
		this.total_yesterday = total_yesterday;
		this.waitorder = waitorder;
		this.haveorder = haveorder;
		this.location = location;
		this.picpath = picpath;
	}
	public FristPage() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
