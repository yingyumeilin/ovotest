package com.example.oto01.model;
/**
 * 订单跟踪
 * @author lqq
 *
 */
public class OrderTrackDetail {
	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "OrderTrackDetail [orderid=" + orderid + ", content=" + content
				+ ", poeople=" + people + ", addtime=" + addtime + "]";
	}

	public OrderTrackDetail() {

	}

	public OrderTrackDetail(int orderid, String content, String poeople,
			String addtime) {
		super();
		this.orderid = orderid;
		this.content = content;
		this.people = poeople;
		this.addtime = addtime;
	}

	private int orderid; //订单id�?
	private String content;  //处理内容
	private String people; //操作�?
	private String addtime; //操作时间

}