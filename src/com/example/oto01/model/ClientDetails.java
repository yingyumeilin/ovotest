package com.example.oto01.model;

import java.io.Serializable;
import java.util.List;

public class ClientDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int res;
	public String msg;
	public int totalnum;
	public int countnum;
	public int pay_countnum;
	public int p;
	public List<Data> data;

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

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getCountnum() {
		return countnum;
	}

	public void setCountnum(int countnum) {
		this.countnum = countnum;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public class Data {
		public int id;
		public int uid;
		public String ordernum;
		public String linkman;
		public String phone;
		public String address;
		public String total;
		public String addtime;
		public String overtime;
		public int is_dis;
		public String disnum;
		public String goodsnum;
		public int payment;
		public double paystate;
		public String authcode;
		public String receive_total_price;
		public int type;
		private String order_type;// 订单类型：1、普通(优惠券)订单 2、礼品券到店订单
		private String gift_goods_pic;// 礼品券图片
		public List<Goods> goods;

		public String getGift_goods_pic() {
			return gift_goods_pic;
		}

		public void setGift_goods_pic(String gift_goods_pic) {
			this.gift_goods_pic = gift_goods_pic;
		}

		public String getOrder_type() {
			return order_type;
		}

		public void setOrder_type(String order_type) {
			this.order_type = order_type;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getReceive_total_price() {
			return receive_total_price;
		}

		public void setReceive_total_price(String receive_total_price) {
			this.receive_total_price = receive_total_price;
		}

		public String getAuthcode() {
			return authcode;
		}

		public void setAuthcode(String authcode) {
			this.authcode = authcode;
		}

		public String is_confirm;

		public String getIs_confirm() {
			return is_confirm;
		}

		public void setIs_confirm(String is_confirm) {
			this.is_confirm = is_confirm;
		}

		public String getAddtime() {
			return addtime;
		}

		public void setAddtime(String addtime) {
			this.addtime = addtime;
		}

		public String getOvertime() {
			return overtime;
		}

		public void setOvertime(String overtime) {
			this.overtime = overtime;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public String getOrdernum() {
			return ordernum;
		}

		public void setOrdernum(String ordernum) {
			this.ordernum = ordernum;
		}

		public String getLinkman() {
			return linkman;
		}

		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public int getIs_dis() {
			return is_dis;
		}

		public void setIs_dis(int is_dis) {
			this.is_dis = is_dis;
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

		public int getPayment() {
			return payment;
		}

		public void setPayment(int payment) {
			this.payment = payment;
		}

		public double getPaystate() {
			return paystate;
		}

		public void setPaystate(double paystate) {
			this.paystate = paystate;
		}

		public List<Goods> getGoods() {
			return goods;
		}

		public void setGoods(List<Goods> goods) {
			this.goods = goods;
		}

		public class Goods {
			public int id;
			public String picpath;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getPicpath() {
				return picpath;
			}

			public void setPicpath(String picpath) {
				this.picpath = picpath;
			}

		}

	}

}
