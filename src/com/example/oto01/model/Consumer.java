package com.example.oto01.model;
/**
 * 用户
 * @author liqq
 *
 */
public class Consumer {
/**
 *   uid	int	用户id
  username	string	用户�?
  nickname	string	昵称
  content	string	评论内容
  addtime	int	评论时间
  avatarpath	string	用户头像
 */
	
	private int uid;
	private String username;
	private String nickname;
	private String content;
	private String addtime;
	private String avatarpath;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public String getAvatarpath() {
		return avatarpath;
	}
	public void setAvatarpath(String avatarpath) {
		this.avatarpath = avatarpath;
	}
	public Consumer(int uid, String username, String nickname, String content,
			String addtime, String avatarpath) {
		super();
		this.uid = uid;
		this.username = username;
		this.nickname = nickname;
		this.content = content;
		this.addtime = addtime;
		this.avatarpath = avatarpath;
	}
	public Consumer(int uid, String nickname, String content,
			String addtime, String avatarpath) {
		super();
		this.uid = uid;
		this.nickname = nickname;
		this.content = content;
		this.addtime = addtime;
		this.avatarpath = avatarpath;
	}
	
	
}
