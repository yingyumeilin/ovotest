package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.oto01.model.Constant;
import com.example.oto01.model.Good;
import com.example.oto01.model.GoodImage;
import com.example.oto01.model.GoodType;
import com.example.oto01.model.Login;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 商品管理
 * 
 * @author lqq
 * 
 */
public class GoodService extends BaseHttpService {
	private List<Good> goodList = new ArrayList<Good>();
	public int total_page = 0;
	public int p = 0;
	public String errorMsg;

	public GoodService(Context context) {
		super(context);
	}

	/**
	 * 浏览商品
	 * 
	 * shopsid int 商店id号 typeid int 商品类别id（搜索时提供） state int 商品状态，1上架，2下架 （搜索时提供）
	 * keyword string 搜索关键字(商品标题) （搜索时提供） sortattr string
	 * 排序属性_销量：buynum；库存：num；上架时间：groutime sortmode string 排序方式_升序：asc；降序：desc；
	 * p int 分页页数(第几页)
	 * 
	 * @return
	 */
	public List<Good> getGoodsList(int shopsid, int typeid, String keyword,
			String sortattr, String sortmode, int p, String jumpsort) {
		try {
			// &shopsid=1&sortattr=num&sortmode=desc
			String res = null;
			// if(isgroom == -1 && isupordown == -1){
			// res = HttpUtil.doGet(Constant.GOOD_LIST_URI + "&shopsid="
			// + shopsId+"&typeid="+typeid+ "&sortattr=" + sortattr
			// + "&sortmode=" + sortmode + "&p="+p);
			// }else if(isgroom != -1 && isupordown == -1){
			// res = HttpUtil.doGet(Constant.GOOD_LIST_URI + "&shopsid="
			// + shopsId+"&typeid="+typeid+ "&sortattr=" + sortattr
			// + "&sortmode=" + sortmode + "&isgroom="+isgroom + "&p="+p);
			// }else if(isgroom == -1 && isupordown != -1){
			// res = HttpUtil.doGet(Constant.GOOD_LIST_URI + "&shopsid="
			// + shopsId+"&typeid="+typeid+ "&sortattr=" + sortattr
			// + "&sortmode=" + sortmode + "&state="+isupordown + "&p="+p);
			//
			// }else if(isgroom != -1 && isupordown != -1){
			// res = HttpUtil.doGet(Constant.GOOD_LIST_URI + "&shopsid="
			// + shopsId+"&typeid="+typeid+ "&sortattr=" + sortattr
			// + "&sortmode=" + sortmode + "&isgroom="+isgroom +
			// "&state="+isupordown + "&p="+p);
			//
			// }
			if (jumpsort.trim().length() > 0) {
				res = HttpUtil.doGet(Constant.GOOD_LIST_URI + "&shopsid="
						+ shopsid + "&typeid=" + typeid + "&sortattr="
						+ sortattr + "&sortmode=" + sortmode + "&jumpsort="
						+ jumpsort + "&p=" + p);
			} else {
				res = HttpUtil.doGet(Constant.GOOD_LIST_URI + "&shopsid="
						+ shopsid + "&typeid=" + typeid + "&sortattr="
						+ sortattr + "&sortmode=" + sortmode + "&p=" + p);
			}
			System.out.println("------url----->" + Constant.GOOD_LIST_URI
					+ "&shopsid=" + shopsid + "&typeid=" + typeid
					+ "&sortattr=" + sortattr + "&sortmode=" + sortmode
					+ "&jumpsort=" + jumpsort + "&p=" + p);
			System.out.println("----jumpsort--->" + jumpsort);

			return handleGoodListRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return goodList;
	}

	private List<Good> handleGoodListRes(String res) {
		Log.d("TAG", "res handleGoodListRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				total_page = jo.optInt("totalnum", -1);
				p = jo.optInt("p", -1);
				JSONArray pa = (JSONArray) jo.get("goodslist");
				for (int i = 0; i < pa.length(); i++) {
					JSONObject po = (JSONObject) pa.get(i);
					// FIXME 未处理 imgList信息
					String editTime = null;
					if (po.isNull("edittime")) {
						editTime = po.getString("addtime");
					}
					Good good = new Good(po.getInt("id"), po.getInt("typeid"),
							po.getInt("buynum"), po.getInt("num"),
							po.getString("content"), po.getString("goods"),
							po.getString("addtime"), editTime,
							Float.parseFloat(po.getString("goodsprice")),
							po.getString("unit"), po.getString("picpath"),
							new ArrayList<GoodImage>(),
							po.getString("picname"), po.getInt("state"),
							po.getInt("isgroom"), po.getString("goodsprice"));
					goodList.add(good);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return goodList;
	}

	/**
	 * 获取商品类别列表
	 * 
	 * @return
	 */
	public List<GoodType> getGoodTypeList(int shopsid, String operate) {
		try {
			// ?m=RcbShops&a=goodstype&shopsid=1
			String res = "";
			if (operate.trim().length() > 0) {
				res = HttpUtil.doGet(Constant.GOOD_TYPES_URI + "&shopsid="
						+ shopsid + "&operate=" + operate);
				System.out.println("----url---->" + Constant.GOOD_TYPES_URI
						+ "&shopsid=" + shopsid + "&operate=" + operate);
			} else {
				res = HttpUtil.doGet(Constant.GOOD_TYPES_URI + "&shopsid="
						+ shopsid);
				System.out.println("----url---->" + Constant.GOOD_TYPES_URI
						+ "&shopsid=" + shopsid);
			}

			return handleGoodTypeListRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<GoodType> handleGoodTypeListRes(String res) {
		Log.d("TAG", "res handleGoodTypeListRes : " + res);
		List<GoodType> goodTypeList = new ArrayList<GoodType>();
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				JSONArray pa = (JSONArray) jo.get("typelist");
				for (int i = 0; i < pa.length(); i++) {
					JSONObject po = (JSONObject) pa.get(i);
					int pid = 0;
					if (!po.isNull("pid") && po.get("pid") != null
							&& po.opt("pid") != null) {
						System.out.println("pid-----------" + po.opt("pid"));
						pid = po.getInt("pid");
					}
					GoodType goodType = new GoodType(po.getInt("id"),
							po.getInt("shopsid"), po.getString("name"), pid,
							po.getString("path"));
					goodTypeList.add(goodType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodTypeList;
	}

	/**
	 * 添加商品类别接口：m=RcbShops&a=addtype&shopsid=1&typename=儿童套餐
	 * 
	 * @return
	 */
	public String addGoodType(String typename) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			int shopsId = 1;
			if (login != null && login.getAdminId() != -1) {
				shopsId = login.getAdminId();
			}
			// ?m=RcbShops&a=goodstype&shopsid=1
			String res = HttpUtil.doGet(Constant.ADD_GOOD_TYPE_URI
					+ "&shopsid=" + shopsId + "&typename=" + typename);

			// if (jo.optInt("res") == 0) {
			// return 0;
			// } else {
			// errorMsg = jo.getString("msg");
			// }
			// return jo.optInt("res");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 添加商品类别接口：m=RcbShops&a=addtype&shopsid=1&typename=儿童套餐
	 * 
	 * @return
	 */
	public int addGoodType1(String typename) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			int shopsId = 1;
			if (login != null && login.getAdminId() != -1) {
				shopsId = login.getAdminId();
			}
			String res = HttpUtil.doGet(Constant.ADD_GOOD_TYPE_URI
					+ "&shopsid=" + shopsId + "&typename=" + typename);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				return 0;
			} else {
				errorMsg = jo.getString("msg");
			}
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return 2;
	}

	/**
	 * 删除商品类别接口：?m=RcbShops&a=deltype&typeid=11
	 * 
	 * @param typeid
	 * @return
	 */
	public boolean deleteGoodType(int typeid) {
		try {
			String res = HttpUtil.doGet(Constant.DELETE_GOOD_TYPE_URI
					+ "&typeid=" + typeid);

			return handleSimpleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return false;
	}

	/**
	 * 商品详情接口：?m=RcbShops&a=goodsvo&shopsid=1&goodsid=198
	 * 
	 * @param goodsid
	 * @return
	 */
	// FIXME　商品详情有问题
	public Good getGoodDetails(int goodsid) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			int shopsId = 1;
			if (login != null && login.getAdminId() != -1) {
				shopsId = login.getAdminId();
			}
			String res = HttpUtil.doGet(Constant.GOOD_DETAILS_URI + "&shopsid="
					+ shopsId + "&goodsid=" + goodsid);

			return handleGoodDetailsRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;

	}

	private Good handleGoodDetailsRes(String res) {
		Log.d("TAG", "res handleGoodDetailsRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				JSONObject jsonObject = jo.optJSONArray("goodsvo")
						.getJSONObject(0);
				String edittime = "";
				if (!jsonObject.isNull("edittime")) {
					edittime = jsonObject.getString("edittime");
				}
				ArrayList<GoodImage> images = new ArrayList<GoodImage>();
				if (!jsonObject.isNull("imglist")) {
					for (int i = 0; i < jsonObject.getJSONArray("imglist")
							.length(); i++) {
						JSONObject jsObject = jsonObject
								.getJSONArray("imglist").getJSONObject(i);
						GoodImage goodImage = new GoodImage(
								jsObject.getInt("id"),
								jsObject.getInt("shopsid"),
								jsObject.getInt("goodsid"),
								jsObject.getString("picname"),
								jsObject.getString("picpath"));
						images.add(goodImage);
					}
				}
				int buynum = 0;
				if (!jsonObject.isNull("buynum")) {
					buynum = jsonObject.getInt("buynum");
				}
				String grouttime = null;
				if (!jsonObject.isNull("grouttime")) {
					grouttime = jsonObject.getString("groutime");
				}
				String unit = null;
				if (!jsonObject.isNull("unit")) {
					unit = jsonObject.getString("unit");
				}
				int isgroom = 0;
				if (!jsonObject.isNull("isgroom")) {
					isgroom = jsonObject.getInt("isgroom");
				}
				int state = 0;
				if (!jsonObject.isNull("state")) {
					state = jsonObject.getInt("state");
				}
				Good good = new Good(jsonObject.getInt("id"),
						jsonObject.getInt("typeid"), buynum,
						jsonObject.getInt("num"),
						jsonObject.getString("content"),
						jsonObject.getString("goods"), grouttime, edittime,
						Double.parseDouble(jsonObject.getString("goodsprice")),
						unit, null, images, null, state, isgroom,
						jsonObject.getString("goodsprice"));
				return good;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Good good = new Good();
			return good;
		}
		return null;
	}

	/**
	 * 添加商品
	 * 
	 * @param good
	 * 
	 *            { "id": "3", "shopsid": "1", "typeid": "1", "goodsprice":
	 *            "39.8", "num": "38", "unit": "个", "content": "我是商品描述4444444",
	 *            "piclist": [ "52a2ea32d094b.png", "52a2ea32d14bc.png" ], }
	 * 
	 * @return
	 */
	public boolean addGood(int typeid, double price, int num, String unit,
			String content, String goods, ArrayList<String> imgList) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			int shopsId = 1;
			if (login != null && login.getAdminId() != -1) {
				shopsId = login.getAdminId();
			}
			JSONObject jo = new JSONObject();
			jo.put("shopsid", shopsId);
			jo.put("typeid", typeid);
			jo.put("goodsprice", price);
			jo.put("num", num);
			jo.put("unit", unit);
			jo.put("content", content);
			jo.put("goods", goods);
			// String json = "";
			// if (imgList.size() == 0)
			// {
			// json ="[]";
			// jo.put("piclist", json);
			// } else{
			// final StringBuilder sb = new StringBuilder(imgList.size() << 4);
			// sb.append('[');
			// for (final String o : imgList)
			// {
			// sb.append(o);
			// sb.append(',');
			// }
			// sb.setCharAt(sb.length() - 1, ']');
			//
			// jo.put("piclist", sb.toString());
			// }
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < imgList.size(); i++) {
				jsonArray.put(imgList.get(i));
			}
			System.out.println("-------jsonArray----->" + jsonArray);
			System.out.println("-------imgList----->" + imgList);
			jo.put("piclist", jsonArray);

			Log.d("TAG", "handleAddGoodRes_params : " + jo.toString());
			System.out.println("--------url------->" + Constant.ADD_GOOD_URI);
			String res = HttpUtil.doPost(Constant.ADD_GOOD_URI, "parm",
					jo.toString());

			return handleSimpleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return false;
	}

	private boolean handleSimpleRes(String res) {
		Log.d("TAG", "res handleSimpleRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * "id": "3", "shopsid": "1", "typeid": "1", "goodsprice": "39.8", "num":
	 * "38", "unit": "个", "content": "我是商品描述4444444", "isgroom": "2", "state":
	 * "2",
	 * 
	 * @param good
	 * @return
	 */
	public boolean updateGoodInfo(int shopsid, int id, int typeid,
			double price, int num, String unit, String content, String goods,
			int isgroom, int state) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("shopsid", shopsid);
			jo.put("typeid", typeid);
			jo.put("goodsprice", price);
			jo.put("num", num);
			jo.put("unit", unit);
			jo.put("content", content);
			jo.put("goods", goods);
			jo.put("isgroom", isgroom);
			jo.put("state", state);
			System.out.println("------ss----->" + jo.toString());
			String res = HttpUtil.doPost(Constant.UPDATE_GOOD_URI, "parm",
					jo.toString());

			System.out.println("-----res----->" + res);
			return handleSimpleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return false;
	}

	/**
	 * 更新商品图片
	 * 
	 * @param shopsid
	 * @param goodsid
	 * @param oldpic
	 * @param newpic
	 * @return
	 */
	public boolean updateGoodImg(int shopsid, int goodsid, List<String> newpic) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("shopsid", shopsid);
			jo.put("goodsid", goodsid);
			// JSONArray oldArray=new JSONArray();
			// for (int i = 0; i < oldpic.size(); i++) {
			// oldArray.put(oldpic.get(i));
			// }
			JSONArray newArray = new JSONArray();
			for (int i = 0; i < newpic.size(); i++) {
				newArray.put(newpic.get(i));
			}
			// jo.put("oldpic", oldArray);
			jo.put("newpic", newArray);
			System.out.println("------updateGoodImg----->" + jo.toString());
			String res = HttpUtil.doPost(Constant.UPDATE_GOOD_IMG_URI, "parm",
					jo.toString());

			return handleSimpleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}

		return false;
	}

	// FIXME　新添加的的接口解析

	/**
	 * 店铺详情预览
	 * 
	 * @param shopsid
	 * @return
	 */
	public ShopInfo getShopInfoDetails(int shopsid) {
		try {
			System.out
					.println("-----getShopInfoDetails-url----->"
							+ Constant.UPDATE_SHOP_INFO_DETAILS + "&shopsid="
							+ shopsid);
			String res = HttpUtil.doGet(Constant.UPDATE_SHOP_INFO_DETAILS
					+ "&shopsid=" + shopsid);
			System.out.println("-----getShopInfoDetails-res--->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				JSONObject jo = jsonObject.getJSONObject("shopvo");
				ShopInfo shopInfo = new ShopInfo(jo.getString("shopsname"),
						jo.getString("address"), jo.getString("logo"),
						jo.getInt("level"), jo.getInt("is_send"),
						jo.getInt("sendprice"), jo.getString("logopath"),
						jo.getString("fixphone"), jo.getInt("connum"),
						jo.getInt("attnum"));
				return shopInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 店铺商品列表预览
	 * 
	 * @param shopsid
	 * @return
	 */
	public ShopInfo getShopGoodsList(int shopsid, int curP) {
		try {
			System.out.println("-----getShopGoodsList-url----->"
					+ Constant.UPDATE_SHOP_LIST + "&shopsid=" + shopsid + "&p="
					+ curP);
			String res = HttpUtil.doGet(Constant.UPDATE_SHOP_LIST + "&shopsid="
					+ shopsid + "&p=" + curP);
			System.out.println("-----getShopGoodsList-res--->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				total_page = jsonObject.optInt("totalnum", -1);
				p = jsonObject.optInt("p", -1);
				JSONObject jObject = jsonObject.getJSONObject("shopsvo");
				JSONArray pa = (JSONArray) jsonObject.get("goodslist");
				List<Good> goods = new ArrayList<Good>();
				for (int i = 0; i < pa.length(); i++) {
					JSONObject po = (JSONObject) pa.get(i);
					// FIXME
					Good good = new Good(po.getInt("id"), po.getInt("typeid"),
							po.getInt("buynum"), po.getInt("num"),
							po.getString("content"),
							po.getDouble("goodsprice"), po.getString("unit"),
							po.getString("picpath"), po.getString("picname"),
							po.getInt("state"), po.getInt("isgroom"),
							po.getString("goods"), po.getInt("is_hot"));

					goods.add(good);
				}
				ShopInfo shopInfo = new ShopInfo(shopsid,
						jObject.getString("shopsname"),
						jObject.getString("slogan"), goods);
				return shopInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}

		}
		return null;
	}

	/**
	 * 预览商品详情接口
	 * 
	 * @return
	 */
	public Good getGoodDetails(int shopsid, int goodsid) {
		try {
			String res = HttpUtil.doGet(Constant.UPDATE_GOOD_DETAILS);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				ArrayList<GoodImage> images = new ArrayList<GoodImage>();
				if (!jsonObject.isNull("imglist")) {
					for (int i = 0; i < jsonObject.getJSONArray("imglist")
							.length(); i++) {
						JSONObject jsObject = jsonObject
								.getJSONArray("imglist").getJSONObject(i);
						GoodImage goodImage = new GoodImage(
								jsObject.getInt("id"),
								jsObject.getInt("shopsid"),
								jsObject.getInt("goodsid"),
								jsObject.getString("picname"),
								jsObject.getString("picpath"),
								jsObject.getInt("state"));
						images.add(goodImage);
					}
					Good good = new Good(
							jsonObject.getInt("id"),
							jsonObject.getInt("typeid"),
							jsonObject.getInt("buynum"),
							jsonObject.getString("content"),
							Float.parseFloat(jsonObject.getString("goodsprice")),
							images, jsonObject.getInt("state"), jsonObject
									.getInt("isgroom"), jsonObject
									.getString("goods"), jsonObject
									.getInt("is_hot"), jsonObject
									.getDouble("marketprice"), jsonObject
									.getInt("revnum"), jsonObject
									.getInt("attnum"));

					return good;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 删除商品接口
	 * 
	 * @param goodsid
	 * @return
	 */
	public int deleteGood(int goodsid) {
		try {
			String res = HttpUtil.doGet(Constant.DELETE_GOOD_URI + "&goodsid="
					+ goodsid);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				errorMsg = jsonObject.getString("msg");
				return jsonObject.getInt("res");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 暂不接单 接口
	 * 
	 * @param shopsid
	 * @param is_business
	 * @return
	 */
	public String updateShopInfo(int shopsid, int is_business) {
		try {
			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"],"
					+ "[\"is_business\",\"" + is_business + "\"]]";

			System.out.println("----url------->"
					+ Constant.UPDATE_SHOP_INFO_URI2 + "----" + string);
			String res = HttpUtil
					.doPost(Constant.UPDATE_SHOP_INFO_URI2, string);
			System.out.println("------updateShopInfo------->" + res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 外卖设置
	 * 
	 * @param shopsid
	 * @param is_send
	 * @param send_distance_m
	 * @param sendprice
	 * @param freight_price
	 * @param time_one
	 * @return
	 */
	public String goodSetting(int shopsid, int is_send, int send_distance_m,
			int sendprice, double freight_price, String time_one) {
		String string = "";
		try {
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"is_send\",\""
					+ is_send + "\"],[\"send_distance_m\",\"" + send_distance_m
					+ "\"],[\"sendprice\",\"" + sendprice
					+ "\"],[\"freight_price\",\"" + freight_price
					+ "\"],[\"time_one\",\"" + time_one + "\"]]";

			String res = HttpUtil.doPost(Constant.GOOD_SETTING, string);
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 得到分类列表
	 * 
	 * @param shopsid
	 * @param operate_type
	 * @return
	 */
	public String getSortManagerList(int shopsid, int operate_type) {
		try {
			String res = HttpUtil.doGet(Constant.GOOD_SORT_LIST + "&shopsid="
					+ shopsid + "&operate_type=" + operate_type);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 修改商品类型
	 */

	public String updateSortList(int shopsid, int typeid, String typename) {
		try {
			String res = HttpUtil.doGet(Constant.UPDATE_SORT_LIST + "&shopsid="
					+ shopsid + "&typeid=" + typeid + "&typename=" + typename);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
