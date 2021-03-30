package com.example.oto01.model;

/**
 * 问题反馈
 * @author lqq
 *
 */
public class FeedBack {
	private int id;
	private String content;
	/**
	 *   addtime	int	问题时间
  phone	string	来自于（已处理）
  backcontent	string	反馈内容

	 */
	private String addtime ;
	private String phone;
	private String backcontent;
	
	public FeedBack() {
		super();
	}
	public FeedBack(int id, String content) {
		super();
		this.id = id;
		this.content = content;
	}
	public FeedBack(int id, String content, String addtime, String phone,
			String backcontent) {
		super();
		this.id = id;
		this.content = content;
		this.addtime = addtime;
		this.phone = phone;
		this.backcontent = backcontent;
	}

	public FeedBack(String addtime, String phone, String backcontent) {
		super();
		this.addtime = addtime;
		this.phone = phone;
		this.backcontent = backcontent;
	}

	@Override
	public String toString() {
		return "FeedBack [id=" + id + ", content=" + content + ", addtime="
				+ addtime + ", phone=" + phone + ", backcontent=" + backcontent
				+ "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBackcontent() {
		return backcontent;
	}
	public void setBackcontent(String backcontent) {
		this.backcontent = backcontent;
	}
	
}
