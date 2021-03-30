package com.example.oto01.model;
/**
 * 消息类型�?
 * @author lqq
 *
 */
public class MessageType {
	
	public MessageType(int infortype, String title, int num, String name,
			String addtime) {
		super();
		this.infortype = infortype;
		this.title = title;
		this.num = num;
		this.name = name;
		this.addtime = addtime;
	}
	public MessageType(int infortype, String title, int num,
			String addtime) {
		super();
		this.infortype = infortype;
		this.title = title;
		this.num = num;
		this.addtime = addtime;
	}
	@Override
	public String toString() {
		return "MessageType [infortype=" + infortype + ", title=" + title
				+ ", num=" + num + ", name=" + name + ", addtime=" + addtime
				+ "]";
	}
	public int getInfortype() {
		return infortype;
	}
	public void setInfortype(int infortype) {
		this.infortype = infortype;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	private int infortype;  
	private String title; 
	private int num; 
	private String name;
	private String addtime; 
	
}
