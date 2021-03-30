package com.example.oto01.model;

/**
 * 订单详情
 * @author lqq
 *
 */
public class OrderDetail {
	
	@Override
	public String toString() {
		return "OrderDetail [id=" + id + ", goodsid=" + goodsid + ", orderid="
				+ orderid + ", goodsnum=" + goodsnum + ", name=" + name
				+ ", price=" + price + ", showPrice=" + showPrice + ", num="
				+ num + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public int getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(int goodsnum) {
		this.goodsnum = goodsnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
public OrderDetail(int id, int goodsid, int orderid, int goodsnum,
			String name, double price, int num,String showPrice) {
		super();
		this.id = id;
		this.goodsid = goodsid;
		this.orderid = orderid;
		this.goodsnum = goodsnum;
		this.name = name;
		this.price = price;
		this.num = num;
		this.showPrice = showPrice;
	}



/**
 *  id	int	详情id
  orderid	int	订单id
  goodsid	int	商品id
  goodsnum	int	商品编号
  name	string	商品名称
  price	float	单价
  num	int	数量
 */
	private int id;
	private int goodsid;
	private int orderid;
	private int goodsnum;//商品编号
	private String name;
	private double price;
	private String showPrice;
	private int num;//数量
	
	public String getShowPrice() {
		return showPrice;
	}
	public void setShowPrice(String showPrice) {
		this.showPrice = showPrice;
	}

}