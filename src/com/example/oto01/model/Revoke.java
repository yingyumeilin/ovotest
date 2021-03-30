package com.example.oto01.model;

public class Revoke {

	private int type;
	private String reason;
	
	
	public Revoke(int type, String reason) {
		super();
		this.type = type;
		this.reason = reason;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
