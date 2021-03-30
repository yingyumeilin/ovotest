package com.example.oto01.model;

import java.io.Serializable;

/**
 * 管家端的交互信息
 * @author user
 *
 */
@SuppressWarnings("serial")
public class DISInfo implements Serializable {

	/**
	 * status	int	返回状态码
1：请求失败
2：尚未审核开通
3：审核已通过
4：审核中
5：审核未通过
6：请求e管家失败
msg	string	显示文案
address	string	详细地址
state_result	string	失败原因(status=5时用)
	 */
	private int status;
	private String msg;
	private String address;
	private String state_result;
	
	
	@Override
	public String toString() {
		return "DISInfo [status=" + status + ", msg=" + msg + ", address="
				+ address + ", state_result=" + state_result + "]";
	}
	public DISInfo(int status, String msg, String address, String state_result) {
		super();
		this.status = status;
		this.msg = msg;
		this.address = address;
		this.state_result = state_result;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState_result() {
		return state_result;
	}
	public void setState_result(String state_result) {
		this.state_result = state_result;
	}
	
	
	
}
