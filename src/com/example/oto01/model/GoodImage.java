package com.example.oto01.model;

import java.io.Serializable;

/**
 * 商品图片
 * @author lqq
 *
 */
public class GoodImage  implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 *     id	int	图片id
    shopsid	int	商店id
    goodsid	int	商品id
    picname	string	图片�?
    picpath	string	图片地址

	 */
	private int id;
	private int shopsid;
	private int goodsid;
	private String picname;
	private String picpath;
	private int state;//图片状态：1使用2部使用
	
	
	
	public GoodImage(int id, int shopsid, int goodsid, String picname,
			String picpath) {
		super();
		this.id = id;
		this.shopsid = shopsid;
		this.goodsid = goodsid;
		this.picname = picname;
		this.picpath = picpath;
	}
	public GoodImage(int id, int shopsid, int goodsid, String picname,
			String picpath,int state) {
		super();
		this.id = id;
		this.shopsid = shopsid;
		this.goodsid = goodsid;
		this.picname = picname;
		this.picpath = picpath;
		this.state = state;
	}
	@Override
	public String toString() {
		return "GoodImage [id=" + id + ", shopsid=" + shopsid + ", goodsid="
				+ goodsid + ", picname=" + picname + ", picpath=" + picpath
				+ "]";
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
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}
	
	
}
