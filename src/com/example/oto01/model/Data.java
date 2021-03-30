package com.example.oto01.model;

import java.io.Serializable;

public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String ordernum;
	private String goods_total_price;
	private String discount_total_price;
	private String total;
	private String reward_price;
	private String receive_total_price;
	private String authcode;
	// 是否确认 1：已确认 2：未确认
	private String is_confirm;
	// 支付方式：1、电子支付 2、微信支付
	private String payment;
	private String addtime;
	// private String shop_subsidy_price;

	private String shops_receive_total;// 商户实收金额
	private String coupon_name;// 券名称
	private String actual_coupon_total;// 代金券价格
	private String order_type;// 订单类型：1、到店电子支付 2、礼品券到店订单
	private String shops_settle_price;// 商户结算价格

	// public String getShop_subsidy_price() {
	// return shop_subsidy_price;
	// }
	//
	// public void setShop_subsidy_price(String shop_subsidy_price) {
	// this.shop_subsidy_price = shop_subsidy_price;
	// }

	public String getShops_receive_total() {
		return shops_receive_total;
	}

	public void setShops_receive_total(String shops_receive_total) {
		this.shops_receive_total = shops_receive_total;
	}

	public String getCoupon_name() {
		return coupon_name;
	}

	public void setCoupon_name(String coupon_name) {
		this.coupon_name = coupon_name;
	}

	public String getActual_coupon_total() {
		return actual_coupon_total;
	}

	public void setActual_coupon_total(String actual_coupon_total) {
		this.actual_coupon_total = actual_coupon_total;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getShops_settle_price() {
		return shops_settle_price;
	}

	public void setShops_settle_price(String shops_settle_price) {
		this.shops_settle_price = shops_settle_price;
	}

	public Data(String id, String ordernum, String goods_total_price,
			String discount_total_price, String total, String reward_price,
			String receive_total_price, String authcode, String is_confirm,
			String payment, String addtime, String shops_receive_total,
			String coupon_name, String actual_coupon_total, String order_type,
			String shops_settle_price) {
		super();
		// private String coupon_name;// 券名称
		// private String actual_coupon_total;// 代金券价格
		// private String order_type;// 订单类型：1、到店电子支付 2、礼品券到店订单
		// private String shops_settle_price;// 商户结算价格
		this.id = id;
		this.ordernum = ordernum;
		this.goods_total_price = goods_total_price;
		this.discount_total_price = discount_total_price;
		this.total = total;
		this.reward_price = reward_price;
		this.receive_total_price = receive_total_price;
		this.authcode = authcode;
		this.is_confirm = is_confirm;
		this.payment = payment;
		this.addtime = addtime;
		this.shops_receive_total = shops_receive_total;
		this.coupon_name = coupon_name;
		this.actual_coupon_total = actual_coupon_total;
		this.order_type = order_type;
		this.shops_settle_price = shops_settle_price;
	}

	public String getGoods_total_price() {
		return goods_total_price;
	}

	public void setGoods_total_price(String goods_total_price) {
		this.goods_total_price = goods_total_price;
	}

	public String getDiscount_total_price() {
		return discount_total_price;
	}

	public void setDiscount_total_price(String discount_total_price) {
		this.discount_total_price = discount_total_price;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getReward_price() {
		return reward_price;
	}

	public void setReward_price(String reward_price) {
		this.reward_price = reward_price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
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

	public String getIs_confirm() {
		return is_confirm;
	}

	public void setIs_confirm(String is_confirm) {
		this.is_confirm = is_confirm;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

}
