package com.example.oto01.model;

import java.util.List;

/**
 * 店铺信息
 * 
 * @author lqq
 * 
 */
public class ShopInfo {

	/**
	 * id int 商店id shopsname string 商店名称 name string 店主姓名 phone string 注册手机�?
	 * address string 店铺地址 typeid int 分类id typename string 分类名称
	 */
	private int id;
	private String shopsname;
	private String name;
	private String phone;
	private String address;
	private int typeid;
	private String typename;
	private String detail_address;
	private String state;
	private String send_distance;

	public String getSend_distance() {
		return send_distance;
	}

	public void setSend_distance(String send_distance) {
		this.send_distance = send_distance;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDetail_address() {
		return detail_address;
	}

	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}

	/**
	 * picname string logo图片�? picpath string logo图片地址
	 */
	private String picname;

	@Override
	public String toString() {
		return "ShopInfo [id=" + id + ", shopsname=" + shopsname + ", name="
				+ name + ", phone=" + phone + ", address=" + address
				+ ", typeid=" + typeid + ", typename=" + typename
				+ ", detail_address=" + detail_address + ", picname=" + picname
				+ ", picpath=" + picpath + ", logo=" + logo + ", level="
				+ level + ", lng=" + lng + ", lat=" + lat + ", location="
				+ location + ", slogan=" + slogan + ", is_send=" + is_send
				+ ", sendprice=" + sendprice + ", is_business=" + is_business
				+ ", business_time=" + business_time + ", freight_price="
				+ freight_price + ", is_goods_payment=" + is_goods_payment
				+ ", logopath=" + logopath + ", fixphone=" + fixphone
				+ ", connum=" + connum + ", attnum=" + attnum + ", goodsList="
				+ goodsList + ", onlinetime=" + onlinetime + ", license="
				+ license + ", cid=" + cid + ", city=" + city + ", area="
				+ area + ", country=" + country + ", typeName=" + typeName
				+ "]";
	}

	private String picpath;

	/**
	 * logo string logo图片�? level int 星级 picpath string logo图片路径
	 */
	private String logo;
	private int level;

	private double lng;
	private double lat;

	private String location;// 商圈位置
	private String slogan;// string 广告�?
	private int is_send;// int 是否支持外�?:1�?�?
	private int sendprice;// int 起�?�?
	private int is_business;
	private List<ShopBusinessTime> business_time;
	private int freight_price;
	private int is_goods_payment;
	private String license_number;
	private String business_time_show;
	private String identity_number;

	public String getLicense_number() {
		return license_number;
	}

	public void setLicense_number(String license_number) {
		this.license_number = license_number;
	}

	public String getBusiness_time_show() {
		return business_time_show;
	}

	public void setBusiness_time_show(String business_time_show) {
		this.business_time_show = business_time_show;
	}

	public String getIdentity_number() {
		return identity_number;
	}

	public void setIdentity_number(String identity_number) {
		this.identity_number = identity_number;
	}

	/**
	 * logo string logo图片 logopath string logo图片地址 level float 商家等级 fixphone
	 * string 联系方式 address string 地址 connum int 评论数 is_send int 是否支持外送:1是2否
	 * sendprice int 起送价 attnum int 收藏数 license string 证件地址 shopsname string
	 * 店铺名称 name string 店铺姓名 typeid int 分类id cid int 商圈id(第三级) city int 城市 area
	 * Int 区 country Int 县
	 */
	private String logopath;
	private String fixphone;
	private int connum;
	private int attnum;
	private List<Good> goodsList;
	private String onlinetime;// 营业时间
	private String license;
	private int cid;
	private int city;
	private int area;
	private int country;
	private String typeName;

	public ShopInfo(String shopsname, String address, String logo, int level,
			int is_send, int sendprice, String logopath, String fixphone,
			int connum, int attnum) {
		super();
		this.shopsname = shopsname;
		this.address = address;
		this.logo = logo;
		this.level = level;
		this.is_send = is_send;
		this.sendprice = sendprice;
		this.logopath = logopath;
		this.fixphone = fixphone;
		this.connum = connum;
		this.attnum = attnum;
	}

	public ShopInfo() {
		super();
	}

	public ShopInfo(int id, String shopsname, String name, String phone,
			String address, double lng, double lat) {
		super();
		this.id = id;
		this.shopsname = shopsname;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
	}

	public ShopInfo(String shopsname, String picpath, String logo, int level) {
		super();
		this.shopsname = shopsname;
		this.picpath = picpath;
		this.logo = logo;
		this.level = level;
	}

	public ShopInfo(String shopsname, String picpath, String logo,
			String location) {
		super();
		this.shopsname = shopsname;
		this.picpath = picpath;
		this.logo = logo;
		this.location = location;
	}

	public ShopInfo(String picname, String picpath) {
		super();
		this.picname = picname;
		this.picpath = picpath;
	}

	public ShopInfo(int id, String shopsname, String name, String phone,
			String address, String detail_address, int typeid, int city,
			int area, int country, String typename, double longitude,
			double latitude) {
		super();
		this.id = id;
		this.shopsname = shopsname;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.typeid = typeid;
		this.city = city;
		this.area = area;
		this.country = country;
		this.typename = typename;
		this.lng = longitude;
		this.lat = latitude;
		this.detail_address = detail_address;

	}

	public ShopInfo(int id, String address, double lng, double lat,
			String slogan, int is_send, int freight_price, int is_business,
			int sendprice, String onlinetime, String license, int typeid,
			List<ShopBusinessTime> business_time, int is_goods_payment,
			String state, String send_distance, String shopsname, String name,
			String phone, String logo, String license_number,
			String business_time_show, String identity_number, String typename,
			String detail_address) {
		super();
		this.id = id;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.slogan = slogan;
		this.is_send = is_send;
		this.freight_price = freight_price;
		this.is_business = is_business;
		this.sendprice = sendprice;
		this.onlinetime = onlinetime;
		this.license = license;
		this.typeid = typeid;
		this.business_time = business_time;
		this.is_goods_payment = is_goods_payment;
		this.state = state;
		this.send_distance = send_distance;
		this.shopsname = shopsname;
		this.name = name;
		this.phone = phone;
		this.logo = logo;
		this.license_number = license_number;
		this.business_time_show = business_time_show;
		this.identity_number = identity_number;
		this.detail_address = detail_address;
		this.typename = typename;
	}

//	public ShopInfo(int id, String shopsname, String name, String address,
//			String detail_address, double longitude, double latitude,
//			String license, int typeid, String license_number,
//			String identity_number, String logo, String phone) {
//		super();
//		this.id = id;
//		this.shopsname = shopsname;
//		this.name = name;
//		this.address = address;
//		this.detail_address = detail_address;
//		this.lng = longitude;
//		this.lat = latitude;
//		this.license = license;
//		this.typeid = typeid;
//		this.license_number = license_number;
//		this.identity_number = identity_number;
//		this.logo = logo;
//		this.phone = phone;
//	}

	/**
	 * shopsname string 店铺名称 slogan string 广告语
	 * 
	 * @return
	 */

	public int getId() {
		return id;
	}

	public ShopInfo(int shopsid, String shopsname, String slogan) {
		super();
		this.id = shopsid;
		this.shopsname = shopsname;
		this.slogan = slogan;
	}

	public ShopInfo(int shopsid, String shopsname, String slogan,
			List<Good> list) {
		super();
		this.id = shopsid;
		this.shopsname = shopsname;
		this.slogan = slogan;
		this.goodsList = list;
	}

	public int getIs_business() {
		return is_business;
	}

	public List<ShopBusinessTime> getBusiness_time() {
		return business_time;
	}

	public void setBusiness_time(List<ShopBusinessTime> business_time) {
		this.business_time = business_time;
	}

	public void setIs_business(int is_business) {
		this.is_business = is_business;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShopsname() {
		return shopsname;
	}

	public void setShopsname(String shopsname) {
		this.shopsname = shopsname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLogopath() {
		return logopath;
	}

	public void setLogopath(String logopath) {
		this.logopath = logopath;
	}

	public String getFixphone() {
		return fixphone;
	}

	public void setFixphone(String fixphone) {
		this.fixphone = fixphone;
	}

	public int getConnum() {
		return connum;
	}

	public void setConnum(int connum) {
		this.connum = connum;
	}

	public int getAttnum() {
		return attnum;
	}

	public void setAttnum(int attnum) {
		this.attnum = attnum;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public int getIs_send() {
		return is_send;
	}

	public void setIs_send(int is_send) {
		this.is_send = is_send;
	}

	public int getSendprice() {
		return sendprice;
	}

	public void setSendprice(int sendprice) {
		this.sendprice = sendprice;
	}

	public List<Good> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Good> goodsList) {
		this.goodsList = goodsList;
	}

	public String getOnlinetime() {
		return onlinetime;
	}

	public void setOnlinetime(String onlinetime) {
		this.onlinetime = onlinetime;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getIs_goods_payment() {
		return is_goods_payment;
	}

	public void setIs_goods_payment(int is_goods_payment) {
		this.is_goods_payment = is_goods_payment;
	}

	public int getFreight_price() {
		return freight_price;
	}

	public void setFreight_price(int freight_price) {
		this.freight_price = freight_price;
	}
}
