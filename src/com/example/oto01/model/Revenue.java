package com.example.oto01.model;

/**
 * 营收
 * 
 * @author lqq
 * 
 */
public class Revenue {

	/**
	 * id int id�? shopsid int 商店id statidate string 日期 total float 营收
	 */
//	private int id;
//	private int shopsid;
//	private String statidate;
//	private String total;
//
//	private String ordernum;// 订单�?
//
	private String days;
	private String shops_receive_total;

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getShops_receive_total() {
		return shops_receive_total;
	}

	public void setShops_receive_total(String shops_receive_total) {
		this.shops_receive_total = shops_receive_total;
	}

//	public Revenue(int id, int shopsid, String statidate, String total) {
//		super();
//		this.id = id;
//		this.shopsid = shopsid;
//		this.statidate = statidate;
//		this.total = total;
//	}

	public Revenue(String days, String shops_receive_total) {
		super();
		this.days = days;
		this.shops_receive_total = shops_receive_total;
	}

	// public Revenue(String total, String ordernum) {
	// super();
	// this.total = total;
	// this.ordernum = ordernum;
	// }
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public int getShopsid() {
//		return shopsid;
//	}
//
//	public void setShopsid(int shopsid) {
//		this.shopsid = shopsid;
//	}
//
//	public String getStatidate() {
//		return statidate;
//	}
//
//	public void setStatidate(String statidate) {
//		this.statidate = statidate;
//	}
//
//	public String getTotal() {
//		return total;
//	}
//
//	public void setTotal(String total) {
//		this.total = total;
//	}
//
//	public String getOrdernum() {
//		return ordernum;
//	}
//
//	public void setOrdernum(String ordernum) {
//		this.ordernum = ordernum;
//	}

}
