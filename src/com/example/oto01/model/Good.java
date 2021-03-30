package com.example.oto01.model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 商品�?
 * @author lqq
 *
 */
public class Good implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 *    id	int	商品id
  typeid	int	商品类别id
  buynum	int	�?��
  num	int	库存
  content	int	描述
  groutime	int	上架时间
  edittime	int	更新时间
  goodsprice	float	现价
  unit      String     单位
  picpath	string	图片地址
  imglist	array	图集
  picname	string	图片�?
  state	tinyint	状�?�?上架�?下架
   int shopsid;
	String goods;
	 */
	private int id;
	private int typeid;
	private int buynum; //�?��
	private int num; //库存
	private String content; //描述
	private String addtime; //上架时间
	private String edittime;//更新时间
	private double goodsprice;//现价
	private String showGoodsprice;
	private String unit; //单位
	private String picpath; //图片地址
	private ArrayList<GoodImage> imgList; //图集
	private String picname; //图片�?
	private int state; //状�? 
	private int isgroom; //是否为推荐商�?
	private int shopsid;
	private String goods;
	
	
	/**
	 * regoods	int	推荐商品�?
soldgoods	int	在售商品�?
yesrevenue	int	昨日营收

	 */
	
	private int regoods;
	private int soldgoods;
	private String yesrevenue;
	
	/**
	 *   id	int	商品id
  typeid	int	商品类别id
  buynum	int	销量
  num	int	库存
  unit	string	单位
  goods	string	商品名称
  content	string	描述
  goodsprice	double	现价
  picpath	string	图片地址
  picname	string	图片名
  isgroom	int	是否推荐_1否2是
  state	int	状态：1上架，2下架
  is_hot	int	是否热卖：1否2是
shopsvo	object	店铺信息对象
  shopsname 	string	店铺名称
  slogan	string	广告语
	 * @param regoods
	 * @param soldgoods
	 * @param yesrevenue
	 */
	private int is_hot;
	
	private double marketprice;//原价
	
	
	/**
	 * 
	  id	int	商品id
  shopsid	int	商店id
  typeid	int	类别id
  marketprice	double	原价
  goodsprice	double	现价
  goods	string	商品名称
  content	string	商品描述
  buynum	int	销量
  revnum	int	评论数
  attnum	int	收藏数
  isgroom	int	推荐状态
  is_hot	int	是否热卖：1否2是
  state	int	状态1上架2下架
  imglist	array	图集
	 */
	
	
	private int revnum;
	public Good(int id, int typeid, int buynum, String content,
			double goodsprice, ArrayList<GoodImage> imgList, int state,
			int isgroom, String goods, int is_hot, double marketprice,
			int revnum, int attnum) {
		super();
		this.id = id;
		this.typeid = typeid;
		this.buynum = buynum;
		this.content = content;
		this.goodsprice = goodsprice;
		this.imgList = imgList;
		this.state = state;
		this.isgroom = isgroom;
		this.goods = goods;
		this.is_hot = is_hot;
		this.marketprice = marketprice;
		this.revnum = revnum;
		this.attnum = attnum;
	}
	private int attnum;
		
	
	
	public Good(int id, int typeid, int buynum, int num, String content,
			double goodsprice, String unit, String picpath, String picname,
			int state, int isgroom, String goods, int is_hot) {
		super();
		this.id = id;
		this.typeid = typeid;
		this.buynum = buynum;
		this.num = num;
		this.content = content;
		this.goodsprice = goodsprice;
		this.unit = unit;
		this.picpath = picpath;
		this.picname = picname;
		this.state = state;
		this.isgroom = isgroom;
		this.goods = goods;
		this.is_hot = is_hot;
	}
	private ShopInfo shopInfo;
	
	
	
	public Good(int regoods, int soldgoods, String yesrevenue) {
		super();
		this.regoods = regoods;
		this.soldgoods = soldgoods;
		this.yesrevenue = yesrevenue;
	}
	public Good(){
		super();
	}
	public Good(int id, int shopsid, String picname,
			String picpath, String content) {
		super();
		this.id = id;
		this.shopsid = shopsid;
		this.picname = picname;
		this.picpath = picpath;
		this.content = content;
	}
	public Good(int id, int typeid, int buynum, int num, String content,
			String addtime, String edittime, double goodsprice, String unit,String picpath,
			ArrayList<GoodImage> imgList, String picname, int state) {
		super();
		this.id = id;
		this.typeid = typeid;
		this.buynum = buynum;
		this.num = num;
		this.content = content;
		this.addtime = addtime;
		this.edittime = edittime;
		this.goodsprice = goodsprice;
		this.unit = unit;
		this.picpath = picpath;
		this.imgList = imgList;
		this.picname = picname;
		this.state = state;
	}
	public Good(int id, int typeid, int buynum, int num, String content,String goods,
			String addtime, String edittime, double goodsprice, String unit,String picpath,
			ArrayList<GoodImage> imgList, String picname, int state,int isgroom ,String showGoodsPrice) {
		super();
		this.id = id;
		this.typeid = typeid;
		this.buynum = buynum;
		this.num = num;
		this.content = content;
		this.goods = goods;
		this.addtime = addtime;
		this.edittime = edittime;
		this.goodsprice = goodsprice;
		this.unit = unit;
		this.picpath = picpath;
		this.imgList = imgList;
		this.picname = picname;
		this.state = state;
		this.isgroom = isgroom;
		this.showGoodsprice = showGoodsPrice;
	}

	
	@Override
	public String toString() {
		return "Good [id=" + id + ", typeid=" + typeid + ", buynum=" + buynum
				+ ", num=" + num + ", content=" + content + ", addtime="
				+ addtime + ", edittime=" + edittime + ", goodsprice="
				+ goodsprice + ", showGoodsprice=" + showGoodsprice + ", unit="
				+ unit + ", picpath=" + picpath + ", imgList=" + imgList
				+ ", picname=" + picname + ", state=" + state + ", isgroom="
				+ isgroom + ", shopsid=" + shopsid + ", goods=" + goods
				+ ", regoods=" + regoods + ", soldgoods=" + soldgoods
				+ ", yesrevenue=" + yesrevenue + ", is_hot=" + is_hot
				+ ", marketprice=" + marketprice + ", revnum=" + revnum
				+ ", attnum=" + attnum + ", shopInfo=" + shopInfo + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public int getBuynum() {
		return buynum;
	}
	public void setBuynum(int buynum) {
		this.buynum = buynum;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEdittime() {
		return edittime;
	}
	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	public double getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(double goodsprice) {
		this.goodsprice = goodsprice;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}
	public ArrayList<GoodImage> getImgList() {
		return imgList;
	}
	public void setImgList(ArrayList<GoodImage> imgList) {
		this.imgList = imgList;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getShopsid() {
		return shopsid;
	}
	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public int getIsgroom() {
		return isgroom;
	}
	public void setIsgroom(int isgroom) {
		this.isgroom = isgroom;
	}
	public int getRegoods() {
		return regoods;
	}
	public void setRegoods(int regoods) {
		this.regoods = regoods;
	}
	public int getSoldgoods() {
		return soldgoods;
	}
	public void setSoldgoods(int soldgoods) {
		this.soldgoods = soldgoods;
	}
	public String getYesrevenue() {
		return yesrevenue;
	}
	public void setYesrevenue(String yesrevenue) {
		this.yesrevenue = yesrevenue;
	}
	public String getShowGoodsprice() {
		return showGoodsprice;
	}
	public void setShowGoodsprice(String showGoodsprice) {
		this.showGoodsprice = showGoodsprice;
	}
	public int getIs_hot() {
		return is_hot;
	}
	public void setIs_hot(int is_hot) {
		this.is_hot = is_hot;
	}
	public ShopInfo getShopInfo() {
		return shopInfo;
	}
	public void setShopInfo(ShopInfo shopInfo) {
		this.shopInfo = shopInfo;
	}
	public double getMarketprice() {
		return marketprice;
	}
	public void setMarketprice(double marketprice) {
		this.marketprice = marketprice;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public int getRevnum() {
		return revnum;
	}
	public void setRevnum(int revnum) {
		this.revnum = revnum;
	}
	public int getAttnum() {
		return attnum;
	}
	public void setAttnum(int attnum) {
		this.attnum = attnum;
	}
	
	
}