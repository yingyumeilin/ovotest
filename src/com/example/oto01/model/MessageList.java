package com.example.oto01.model;

import java.util.List;

public class MessageList {

	public int res;
	public int num;
	public int total;
	public int nowp;

	public List<Messages> data;

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNowp() {
		return nowp;
	}

	public void setNowp(int nowp) {
		this.nowp = nowp;
	}

	public List<Messages> getData() {
		return data;
	}

	public void setData(List<Messages> data) {
		this.data = data;
	}
	

}
