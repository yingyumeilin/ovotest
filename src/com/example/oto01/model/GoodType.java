package com.example.oto01.model;

import java.io.Serializable;
/**
 * 商品类别
 * @author lqq
 *
 */
public class GoodType  implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 *   id	int	类别id
  shopsid	int	商店id
  name	string	类别名称
  pid	int	父类id
  path	string	Id路径

	 */
	
	private int id;
	private int shopsid;
	private String name;
	private int pid;
	private String path;
	
	public GoodType(int id, int shopsid, String name, int pid, String path) {
		super();
		this.id = id;
		this.shopsid = shopsid;
		this.name = name;
		this.pid = pid;
		this.path = path;
	}
	@Override
	public String toString() {
		return "GoodType [id=" + id + ", shopsid=" + shopsid + ", name=" + name
				+ ", pid=" + pid + ", path=" + path + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShopsid() {
		return shopsid;
	}
	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
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
	
	
}
