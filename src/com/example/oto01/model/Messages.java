package com.example.oto01.model;

/**
 * 消息�?
 * 
 * @author lqq
 * 
 */
public class Messages {

	public int id;
	public String orderid;
	public String shopsid;
	public String cid;
	public String uid;
	public String infortype;
	public String title;
	public String introduction;
	public String addtime;
	public String endtime;
	public String is_mark;
	public String ordernum;
	public String total;
	public String num;
	private String is_read;// 订单消息是否已读：1、已读 2、未读
	private String is_readmessage;// 官方消息 是否已读；
	private String username;
	private String content;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIs_readmessage() {
		return is_readmessage;
	}

	public void setIs_readmessage(String is_readmessage) {
		this.is_readmessage = is_readmessage;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
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

	public String getIs_read() {
		return is_read;
	}

	public void setIs_read(String is_read) {
		this.is_read = is_read;
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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getInfortype() {
		return infortype;
	}

	public void setInfortype(String infortype) {
		this.infortype = infortype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getIs_mark() {
		return is_mark;
	}

	public void setIs_mark(String is_mark) {
		this.is_mark = is_mark;
	}

	public Messages() {
		super();
	}

	public Messages(String infortype, String title, String content,
			String addtime) {
		super();
		this.infortype = infortype;
		this.title = title;
		this.content = content;
		this.addtime = addtime;
	}

}
