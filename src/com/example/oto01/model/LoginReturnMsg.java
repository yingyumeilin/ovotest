package com.example.oto01.model;

public class LoginReturnMsg {
	private int res;
	private String msg;
	private int id;
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "ReturnMsg [res=" + res + ", msg=" + msg + ", id=" + id + "]";
	}
	public LoginReturnMsg(int res, String msg, int id) {
		super();
		this.res = res;
		this.msg = msg;
		this.id = id;
	}
	
}
