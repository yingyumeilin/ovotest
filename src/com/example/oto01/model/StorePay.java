package com.example.oto01.model;

import java.io.Serializable;
import java.util.List;

public class StorePay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String res;
	private String total;
	private String num;
	private String nowp;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getNowp() {
		return nowp;
	}

	public void setNowp(String nowp) {
		this.nowp = nowp;
	}

	private List<Data> data;

	public class Data {
		private String id;
		private String shopsid;
		private String ordernum;
		private String addtime;// 下单时间
		private String order_type;// 订单类型：1、普通(优惠券)订单 2、礼品券到店订单

		public String getOrder_type() {
			return order_type;
		}

		public void setOrder_type(String order_type) {
			this.order_type = order_type;
		}

		public String getReceive_total_price() {
			return receive_total_price;
		}

		public void setReceive_total_price(String receive_total_price) {
			this.receive_total_price = receive_total_price;
		}

		private String receive_total_price;
		private String authcode;// 验证码
		private String is_confirm;// 是否确认 1：已确认 2：未确认

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getShopsid() {
			return shopsid;
		}

		public void setShopsid(String shopsid) {
			this.shopsid = shopsid;
		}

		public String getOrdernum() {
			return ordernum;
		}

		public void setOrdernum(String ordernum) {
			this.ordernum = ordernum;
		}

		public String getAddtime() {
			return addtime;
		}

		public void setAddtime(String addtime) {
			this.addtime = addtime;
		}

		public String getAuthcode() {
			return authcode;
		}

		public void setAuthcode(String authcode) {
			this.authcode = authcode;
		}

		public String getIs_confirm() {
			return is_confirm;
		}

		public void setIs_confirm(String is_confirm) {
			this.is_confirm = is_confirm;
		}

	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

}
