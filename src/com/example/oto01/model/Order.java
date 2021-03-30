package com.example.oto01.model;

import java.util.List;

/**
 * 订单
 * 
 * @author lqq
 * 
 */
public class Order {

	public String getCoupon_total_price() {
		return coupon_total_price;
	}

	public void setCoupon_total_price(String coupon_total_price) {
		this.coupon_total_price = coupon_total_price;
	}

	public String getFreight_total_price() {
		return freight_total_price;
	}

	public void setFreight_total_price(String freight_total_price) {
		this.freight_total_price = freight_total_price;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public int getShopsid() {
		return shopsid;
	}

	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public int getStatus() {
		return state;
	}

	public void setStatus(int status) {
		this.state = status;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public List<Good> getOrderGood() {
		return orderGood;
	}

	public void setOrderGood(List<Good> orderGood) {
		this.orderGood = orderGood;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Order(int id, String ordernum, int shopsid, int uid, String linkman,
			String address, String phone, String total, int status,
			String addtime, List<Good> orderGood) {
		super();
		this.id = id;
		this.ordernum = ordernum;
		this.shopsid = shopsid;
		this.uid = uid;
		this.linkman = linkman;
		this.address = address;
		this.phone = phone;
		this.total = total;
		this.state = status;
		this.addtime = addtime;
		this.orderGood = orderGood;
	}

	public String gift_goods_pic;

	public String getGift_goods_pic() {
		return gift_goods_pic;
	}

	public void setGift_goods_pic(String gift_goods_pic) {
		this.gift_goods_pic = gift_goods_pic;
	}

	public Order(int id, String ordernum, int shopsid, int uid, String linkman,
			String address, String total, int state, String addtime,
			List<Good> orderGood, String showTotal, String goodsnum,
			String disnum, int is_dis, int payment, int paystate,
			String order_type, String gift_goods_pic) {
		super();
		this.id = id;
		this.ordernum = ordernum;
		this.shopsid = shopsid;
		this.uid = uid;
		this.linkman = linkman;
		this.address = address;
		this.total = total;
		this.state = state;
		this.addtime = addtime;
		this.orderGood = orderGood;
		this.showTotal = showTotal;
		this.is_dis = is_dis;
		this.disnum = disnum;
		this.goodsnum = goodsnum;
		this.payment = payment;
		this.paystate = paystate;
		this.order_type = order_type;
		this.gift_goods_pic = gift_goods_pic;
	}

	private String shop_subsidy_price;

	public String getShop_subsidy_price() {
		return shop_subsidy_price;
	}

	public void setShop_subsidy_price(String shop_subsidy_price) {
		this.shop_subsidy_price = shop_subsidy_price;
	}

	// private String order_type;// 订单类型：1、普通(优惠券)订单 2、礼品券到店订单
	// private String discount_total_price;// 平台补贴金额
	// private String shops_receive_total;// 商户实收金额
	// private String actual_coupon_total;// 实际优惠券价格
	// private String coupon_name;// 代金券名称
	// private String total_reward_price;// 平台结算金额
	public Order(int id, String ordernum, int payment, int shopsid,
			String linkman, String address, String phone, String total,
			int state, String addtime, String username, String userphone,
			String content, List<OrderDetail> orderDetails, int paystate,
			String showTotal, String goodsnum, String hphone, String hname,
			int is_dis, String disnum, String freight_total_price,
			String coupon_total_price, String goods_total_price,
			String reward_price, String receive_total_price,
			String shop_subsidy_price, String order_type,
			String discount_total_price, String shops_receive_total,
			String actual_coupon_total, String coupon_name,
			String total_reward_price) {
		super();
		this.id = id;
		this.ordernum = ordernum;
		this.payment = payment;
		this.shopsid = shopsid;
		this.linkman = linkman;
		this.address = address;
		this.phone = phone;
		this.total = total;
		this.state = state;
		this.addtime = addtime;
		this.username = username;
		this.userphone = userphone;
		this.content = content;
		this.orderDetails = orderDetails;
		this.paystate = paystate;
		this.showTotal = showTotal;
		this.goodsnum = goodsnum;
		this.hphone = hphone;
		this.hname = hname;
		this.is_dis = is_dis;
		this.disnum = disnum;
		this.freight_total_price = freight_total_price;
		this.coupon_total_price = coupon_total_price;
		this.goods_total_price = goods_total_price;
		this.reward_price = reward_price;
		this.receive_total_price = receive_total_price;
		this.shop_subsidy_price = shop_subsidy_price;
		this.order_type = order_type;
		this.discount_total_price = discount_total_price;
		this.shops_receive_total = shops_receive_total;
		this.actual_coupon_total = actual_coupon_total;
		this.coupon_name = coupon_name;
		this.total_reward_price = total_reward_price;

	}

	public Order() {
		super();
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", ordernum=" + ordernum + ", shopsid="
				+ shopsid + ", uid=" + uid + ", linkman=" + linkman
				+ ", address=" + address + ", phone=" + phone + ", total="
				+ total + ", status=" + state + ", addtime=" + addtime
				+ ", orderGood=" + orderGood + "]";
	}

	private int id;
	private String ordernum;// ordernum
	private int shopsid;
	private int uid;
	private String linkman;// 收货人姓�?
	private String address;
	private String phone;// 收货人手机号
	private String total;
	private String showTotal; // 价格显示
	private int state;
	private String addtime;// 下单时间
	private List<Good> orderGood;
	private String username;// 买家姓名
	private String userphone;// 买家手机�?
	private String content; // 订单附言
	private List<OrderDetail> orderDetails;
	private int payment;
	private String coupon_total_price;
	private String freight_total_price;// 运费总额

	private String goods_total_price;// 订单金额
	private String reward_price;// 平台奖励金额
	private String receive_total_price;
	private String order_type;// 订单类型：1、普通(优惠券)订单 2、礼品券到店订单
	private String discount_total_price;// 平台补贴金额
	private String shops_receive_total;// 商户实收金额
	private String actual_coupon_total;// 实际优惠券价格
	private String coupon_name;// 代金券名称
	private String total_reward_price;// 平台结算金额

	public String getTotal_reward_price() {
		return total_reward_price;
	}

	public void setTotal_reward_price(String total_reward_price) {
		this.total_reward_price = total_reward_price;
	}

	public String getDiscount_total_price() {
		return discount_total_price;
	}

	public void setDiscount_total_price(String discount_total_price) {
		this.discount_total_price = discount_total_price;
	}

	public String getShops_receive_total() {
		return shops_receive_total;
	}

	public void setShops_receive_total(String shops_receive_total) {
		this.shops_receive_total = shops_receive_total;
	}

	public String getActual_coupon_total() {
		return actual_coupon_total;
	}

	public void setActual_coupon_total(String actual_coupon_total) {
		this.actual_coupon_total = actual_coupon_total;
	}

	public String getCoupon_name() {
		return coupon_name;
	}

	public void setCoupon_name(String coupon_name) {
		this.coupon_name = coupon_name;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	/**
	 * todayorder int 今日订单�? waitorder int 待发货订单数 haveorder int 已发货订单数
	 */
	private int todayorder;

	public String getGoods_total_price() {
		return goods_total_price;
	}

	public void setGoods_total_price(String goods_total_price) {
		this.goods_total_price = goods_total_price;
	}

	public String getReward_price() {
		return reward_price;
	}

	public void setReward_price(String reward_price) {
		this.reward_price = reward_price;
	}

	public String getReceive_total_price() {
		return receive_total_price;
	}

	public void setReceive_total_price(String receive_total_price) {
		this.receive_total_price = receive_total_price;
	}

	private int waitorder;
	private int haveorder;
	private int paystate;// 1.未付款，2.已付�?
	private String todayrev;
	private String goodsnum;// 货号
	private String hname;
	private String hphone;
	private int is_dis;// 是否找管家派送
	private String disnum;// 配单号

	public Order(int todayorder, String todayrev, int waitorder, int haveorder) {
		super();
		this.todayorder = todayorder;
		this.waitorder = waitorder;
		this.haveorder = haveorder;
		this.todayrev = todayrev;
	}

	public String getDisnum() {
		return disnum;
	}

	public void setDisnum(String disnum) {
		this.disnum = disnum;
	}

	public int getIs_dis() {
		return is_dis;
	}

	public void setIs_dis(int is_dis) {
		this.is_dis = is_dis;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}

	public String getHphone() {
		return hphone;
	}

	public void setHphone(String hphone) {
		this.hphone = hphone;
	}

	public int getTodayorder() {
		return todayorder;
	}

	public void setTodayorder(int todayorder) {
		this.todayorder = todayorder;
	}

	public int getWaitorder() {
		return waitorder;
	}

	public void setWaitorder(int waitorder) {
		this.waitorder = waitorder;
	}

	public String getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}

	public int getHaveorder() {
		return haveorder;
	}

	public void setHaveorder(int haveorder) {
		this.haveorder = haveorder;
	}

	public int getPaystate() {
		return paystate;
	}

	public void setPaystate(int paystate) {
		this.paystate = paystate;
	}

	public String getShowTotal() {
		return showTotal;
	}

	public void setShowTotal(String showTotal) {
		this.showTotal = showTotal;
	}

	public String getTodayrev() {
		return todayrev;
	}

	public void setTodayrev(String todayrev) {
		this.todayrev = todayrev;
	}

}