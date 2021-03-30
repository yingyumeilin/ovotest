package com.example.oto01.model;
/**
 * 商店类别
 * @author lqq
 *
 */
public class ShopType {
/**
 *   id	int	分类id
  name	int	分类名称
  pid	int	父类id
  path	string	id路径
  picname	string	分类图片名称

 */
	private int id;
	private String name;
	private int pid;
	private String path;
	private String picname;
	
	public ShopType(int id, String name, int pid, String path, String picname) {
		super();
		this.id = id;
		this.name = name;
		this.pid = pid;
		this.path = path;
		this.picname = picname;
	}

	@Override
	public String toString() {
		return "ShopType [id=" + id + ", name=" + name + ", pid=" + pid
				+ ", path=" + path + ", picname=" + picname + "]";
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	
}
