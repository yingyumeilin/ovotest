package com.example.oto01.model;

import java.io.Serializable;
import java.util.List;

public class TongXunList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<PersonDto> user_arr;
	public int res;
	public String msg;

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

	public List<PersonDto> getUser_arr() {
		return user_arr;
	}

	public void setUser_arr(List<PersonDto> user_arr) {
		this.user_arr = user_arr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
