package com.example.oto01.model;

import java.io.Serializable;
import java.util.List;

public class Client implements Serializable {

	private String res;
	private String msg;
	private int totalnum;// 总页数
	private String countnum;// 总条数
	private int p;// 当前页

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public String getCountnum() {
		return countnum;
	}

	public void setCountnum(String countnum) {
		this.countnum = countnum;
	}

	private List<clientList> data;

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<clientList> getData() {
		return data;
	}

	public void setData(List<clientList> data) {
		this.data = data;
	}

	public class clientList {

		private boolean down = false;
		private boolean up = false;

		public boolean isDown() {
			return down;
		}

		public void setDown(boolean down) {
			this.down = down;
		}

		public boolean isUp() {
			return up;
		}

		public void setUp(boolean up) {
			this.up = up;
		}

		private int id;
		private String shopsid;
		private String uid;
		/**
		 * 姓名
		 */
		private String nickname;
		/**
		 * 备注
		 */
		private String remark_name;
		private String userphone;
		/**
		 * 用户头像
		 */
		private String headimage;
		private String total;
		private String ordernums;

		// private String addtime;
		/**
		 * 最近订单完成时间
		 */
		private String overtime_order;
		/**
		 * 是否是外卖订单
		 */
		private String is_wai;
		/**
		 * 是否是到店支付订单
		 */
		private String is_pay;
		/**
		 * 是否交易
		 */
		private String is_trade;
		/**
		 * 可打电话 是否可打电话：1、是 2、不是
		 */
		private String is_call;

		public String getIs_call() {
			return is_call;
		}

		public void setIs_call(String is_call) {
			this.is_call = is_call;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getUserphone() {
			return userphone;
		}

		public void setUserphone(String userphone) {
			this.userphone = userphone;
		}

		public String getHeadimage() {
			return headimage;
		}

		public void setHeadimage(String headimage) {
			this.headimage = headimage;
		}

		public String getIs_pay() {
			return is_pay;
		}

		public void setIs_pay(String is_pay) {
			this.is_pay = is_pay;
		}

		public int getId() {
			return id;
		}

		public String getIs_wai() {
			return is_wai;
		}

		public void setIs_wai(String is_wai) {
			this.is_wai = is_wai;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getShopsid() {
			return shopsid;
		}

		public void setShopsid(String shopsid) {
			this.shopsid = shopsid;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getRemark_name() {
			return remark_name;
		}

		public void setRemark_name(String remark_name) {
			this.remark_name = remark_name;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getOrdernums() {
			return ordernums;
		}

		public void setOrdernums(String ordernums) {
			this.ordernums = ordernums;
		}

		public String getIs_trade() {
			return is_trade;
		}

		public void setIs_trade(String is_trade) {
			this.is_trade = is_trade;
		}

		public String getOvertime_order() {
			return overtime_order;
		}

		public void setOvertime_order(String overtime_order) {
			this.overtime_order = overtime_order;
		}

	}

}
