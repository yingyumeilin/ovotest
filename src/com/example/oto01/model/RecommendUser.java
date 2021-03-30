package com.example.oto01.model;

/**
 * 推荐商家或�?用户
 * @author lqq
 *
 */
public class RecommendUser {

	/**
	 *  id	int	商家id（用户id�?
	  phone	int	电话
	  nickname	string	昵称（tag=1�?
	  name	string	姓名（tag=2�?
	  addtime	int	时间�?
	  level	int	奖励�?
	 */
	 
	  private int id;
	  private String phone;
	  private String nickname;
	  private String name;
	  private String addtime;
	  private int level;
	  
	  private String typename;
	  
	  
	  
	  
	public RecommendUser(int id, String phone, String name,
			String addtime, String typename) {
		super();
		this.id = id;
		this.phone = phone;
		this.name = name;
		this.addtime = addtime;
		this.typename = typename;
	}
	public RecommendUser(int id, String phone, String nickname,
			String addtime) {
		super();
		this.id = id;
		this.phone = phone;
		this.nickname = nickname;
		this.addtime = addtime;
	}
	public RecommendUser(int id, String phone, String nickname, String name,
			String addtime, int level) {
		super();
		this.id = id;
		this.phone = phone;
		this.nickname = nickname;
		this.name = name;
		this.addtime = addtime;
		this.level = level;
	}
	@Override
	public String toString() {
		return "RecommendUser [id=" + id + ", phone=" + phone + ", nickname="
				+ nickname + ", name=" + name + ", addtime=" + addtime
				+ ", level=" + level + "]";
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	  
}
