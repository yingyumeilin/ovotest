package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.oto01.model.Constant;
import com.example.oto01.model.Login;
import com.example.oto01.model.RecommendUser;
import com.example.oto01.model.ShopBusinessTime;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.model.VersionInfo;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 设置管理
 * 
 * @author lqq
 * 
 */
public class SettingService extends BaseHttpService {
	public int total_page = 0;
	public int p = 0;
	public String shopsName;
	public String audit;
	public String returnmsg;
	public String jumpurl;
	public String errorMsg;

	public SettingService(Context context) {
		super(context);
	}

	/**
	 * 问题反馈
	 * 
	 * @param uid
	 * @param contway
	 * @param content
	 * @return
	 */
	public boolean feedBack(String contway, String content) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			int shopsId = 1;
			if (login != null && login.getAdminId() != -1) {
				shopsId = login.getAdminId();
			}
			JSONObject jo = new JSONObject();
			jo.put("shopsid", shopsId);
			jo.put("contway", contway);
			jo.put("content", content);
			System.out.println("---feedBack---->" + jo.toString());
			String res = HttpUtil.doPost(Constant.FEED_BACK_URI, "parm",
					jo.toString());

			System.out.println("----feedBack res------>" + res);
			return handleFeedBackRes(res);
		} catch (Exception e) {
			System.out.println("-----chucuo ----->");
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return false;
	}

	private boolean handleFeedBackRes(String res) {
		Log.d("TAG", "res handleFeedBackRes : " + res);
		System.out.println("----handleFeedBackRes------>" + res);
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
	 * 查看店铺信息
	 * 
	 * @return
	 */
	public ShopInfo checkShopInfo(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.SEARCH_SHOP_INFO_URI
					+ "&shopsid=" + shopsid);

			return handleCheckShopInfoRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 查看店铺信息
	 * 
	 * @return
	 */
	public ShopInfo checkShopInfo2(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.NEW_GET_SHOP_INFO_URI
					+ "&shopsid=" + shopsid);
			System.out.println("-----------checkShopInfo2----->" + res);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				/**
				 * license string 证件地址 shopsname string 店铺名称 name string 店铺姓名
				 * typeid int 分类id cid int 商圈id(第三级)
				 */
				List<ShopBusinessTime> duitangs = new ArrayList<ShopBusinessTime>();
				JSONObject jsonObject = jo.getJSONObject("shopvo");
				String business_time = jsonObject.isNull("business_time") ? ""
						: jsonObject.getString("business_time");
				// String business_time="";
				if (!"".equals(business_time)) {
					JSONArray showJson = jsonObject
							.getJSONArray("business_time");
					for (int i = 0; i < showJson.length(); i++) {
						JSONObject object = showJson.getJSONObject(i);
						ShopBusinessTime sbt = new ShopBusinessTime();
						sbt.setStart_time(object.isNull("start_time") ? ""
								: object.getString("start_time"));
						sbt.setEnd_time(object.isNull("end_time") ? "" : object
								.getString("end_time"));
						duitangs.add(sbt);
					}
				}
				// String shopsname, String name,
				// String phone, String logo, String license_number,
				// String business_time_show, String identity_number
				ShopInfo shopInfo = new ShopInfo(jsonObject.getInt("id"),
						jsonObject.getString("address"),
						jsonObject.getDouble("longitude"),
						jsonObject.getDouble("latitude"),
						jsonObject.getString("slogan"),
						jsonObject.getInt("is_send"),
						jsonObject.getInt("freight_price"),
						jsonObject.getInt("is_business"),
						jsonObject.isNull("sendprice") ? 0 : jsonObject
								.getInt("sendprice"),
						jsonObject.getString("onlinetime"),
						jsonObject.getString("license"),
						jsonObject.getInt("typeid"), duitangs,
						jsonObject.getInt("is_goods_payment"),
						jsonObject.getString("state"),
						jsonObject.getString("send_distance"),
						jsonObject.getString("shopsname"),
						jsonObject.getString("name"),
						jsonObject.getString("phone"),
						jsonObject.getString("logo"),
						jsonObject.getString("license_number"),
						jsonObject.getString("business_time_show"),
						jsonObject.getString("identity_number"),
						jsonObject.getString("typename"),
						jsonObject.getString("detail_address"));
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
	 * 查看店铺资质信息
	 * 
	 * @return
	 */
	public JSONObject getShopCheckMessage(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.LOOK_SHOP_ZIZHI + "&shopsid="
					+ shopsid);
			System.out.println("-----------checkShopInfo2----->" + res);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				JSONObject jsonObject = jo.getJSONObject("shopvo");
				// ShopsInfo shopInfo = new ShopsInfo(jsonObject.getInt("id"),
				// jsonObject.getString("shopsname"),
				// jsonObject.getString("name"),
				// jsonObject.getString("address"),
				// jsonObject.getString("detail_address"),
				// jsonObject.getDouble("longitude"),
				// jsonObject.getDouble("latitude"),
				// jsonObject.getString("license"),
				// jsonObject.getInt("typeid"),
				// jsonObject.getString("license_number"),
				// jsonObject.getString("identity_number"),
				// jsonObject.getString("logo"),
				// jsonObject.getString("phone"),
				// jsonObject.getString("identity_number_star"),
				// jsonObject.getString("audit_state"),
				// jsonObject.getString("typename"));
				return jsonObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private ShopInfo handleCheckShopInfoRes(String res) {
		try {
			System.out.println("--------handleCheckShopInfoRes---------->"
					+ res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				ShopInfo shopInfo = new ShopInfo(
						jsonObject.getString("picname"),
						jsonObject.getString("picpath"));
				return shopInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// /**
	// * shopsid int 商铺id slogan string 广告语 is_send int 是否支持外送:1是2否 sendprice
	// int
	// * 起送价 address string 地址 longitude float 经度 latitude float 纬度
	// *
	// * @return
	// */
	public int updateShopInfo(int shopsid, String phone, String time_one) {
		try {
			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\"" + phone
					+ "\"],[\"time_one\",\"" + time_one + "\"]]";

			String res = HttpUtil.doPost(Constant.SHOP_UPDATE, string);
			System.out.println("------updateShopInfo------->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return 0;
			}
			errorMsg = jsonObject.getString("msg");
			return jsonObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	public int updateShopBusinessInfo(int shopsid, String openDay,
			String time1, String time2) {
		try {
			String string = "";
			if ("".equals(time2)) {
				string = "[[\"shopsid\",\"" + shopsid
						+ "\"],[\"onlinetime\",\"" + openDay
						+ "\"],[\"time_one\",\"" + time1 + "\"]]";
			} else {
				string = "[[\"shopsid\",\"" + shopsid
						+ "\"],[\"onlinetime\",\"" + openDay
						+ "\"],[\"time_one\",\"" + time1
						+ "\"],[\"time_two\",\"" + time2 + "\"]]";
			}

			System.out.println("----url------->"
					+ Constant.UPDATE_SHOP_INFO_URI2 + "----" + string);
			String res = HttpUtil
					.doPost(Constant.UPDATE_SHOP_INFO_URI2, string);
			// HttpUtil.doPost(Constant.upda, json)

			System.out.println("------updateShopInfo------->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return 0;
			}
			errorMsg = jsonObject.getString("msg");
			return jsonObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	// public int updateShopInfo(int shopsid, String shopsname, String name,
	// int typeid, int cid, String address, double lng, double lat,
	// String detail_address) {
	// try {
	// String string = "[[\"shopsid\",\"" + shopsid
	// + "\"],[\"shopsname\",\"" + shopsname + "\"],[\"name\",\""
	// + name + "\"],[\"typeid\",\"" + typeid + "\"],[\"cid\",\""
	// + cid + "\"],[\"address\",\"" + address
	// + "\"],[\"detail_address\",\"" + detail_address
	// + "\"],[\"longitude\",\"" + lng + "\"],[\"latitude\",\""
	// + lat + "\"]]";
	// System.out.println("----url------->"
	// + Constant.UPDATE_SHOP_INFO_URI2 + "----" + string);
	// String res = HttpUtil
	// .doPost(Constant.UPDATE_SHOP_INFO_URI2, string);
	// // HttpUtil.doPost(Constant.upda, json)
	//
	// System.out.println("------updateShopInfo------->" + res);
	// JSONObject jsonObject = new JSONObject(res);
	// if (jsonObject.optInt("res") == 0) {
	// return 0;
	// }
	// errorMsg = jsonObject.getString("msg");
	// return jsonObject.optInt("res");
	// } catch (Exception e) {
	// e.printStackTrace();
	// if (HttpUtil.TIME_OUT) {
	// ToastUtil.show(context, "当前网络状况不好！");
	// }
	// }
	// return -1;
	// }
	public int updateShopInfo1(int shopsid, String phone, String time_one) {
		try {
			String string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\""
					+ phone + "\"],[\"time_one\",\"" + time_one + "\"]]";
			System.out.println("----url------->" + Constant.SHOP_UPDATE
					+ "----" + string);
			String res = HttpUtil.doPost(Constant.SHOP_UPDATE, string);

			System.out.println("------updateShopInfo------->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return 0;
			}
			errorMsg = jsonObject.getString("msg");
			return jsonObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	public int updateShopInfo(int shopsid, String license) {
		try {
			String string = "[[\"shopsid\",\"" + shopsid
					+ "\"],[\"license\",\"" + license + "\"]]";
			System.out.println("----url------->"
					+ Constant.UPDATE_SHOP_INFO_URI2 + "----" + string);
			String res = HttpUtil
					.doPost(Constant.UPDATE_SHOP_INFO_URI2, string);
			// HttpUtil.doPost(Constant.upda, json)

			System.out.println("------updateShopInfo------->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return 0;
			}
			errorMsg = jsonObject.getString("msg");
			return jsonObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 更新店铺LOGO
	 * 
	 * @param oldpic
	 * @param newpic
	 * @return
	 */
	public String updateShopLogo(int shopsid, String oldpic, String newpic) {
		try {
			String res = HttpUtil.doGet(Constant.UPDATE_SHOP_INFO_URI
					+ "&shopsid=" + shopsid + "&oldpic=" + oldpic + "&newpic="
					+ newpic);

			return handleUpdateShopInfoRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private String handleUpdateShopInfoRes(String res) {
		System.out.println("-------handleUpdateShopInfoRes---------->" + res);
		try {
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return jsonObject.getString("picpath");
			} else {
				errorMsg = jsonObject.getString("msg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查看店铺资料接口
	 * 
	 * @return
	 */
	public ShopInfo checkShopData(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.SEARCH_SHOP_DATA_URI
					+ "&shopsid=" + shopsid);

			return handleCheckShopDataRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * id shopsname name phone address typeid typename comname
	 * 
	 * @param res
	 * @return
	 */
	private ShopInfo handleCheckShopDataRes(String res) {
		try {
			System.out.println("----checkShopData--res---->" + res);

			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				JSONObject jo = jsonObject.getJSONObject("shopvo");
				ShopInfo shopInfo = new ShopInfo(jo.getInt("id"),
						jo.getString("shopsname"), jo.getString("name"),
						jo.getString("phone"), jo.getString("address"),
						jo.getString("detail_address"), jo.getInt("typeid"),
						jo.getInt("city"), jo.getInt("area"),
						jo.getInt("country"), jo.getString("typename"),
						jo.getDouble("longitude"), jo.getDouble("latitude"));
				return shopInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改密码 nowpass string 新密码 rnowpass string 重复新密码
	 * 
	 * @param usrName
	 * @param passwd
	 * @param isRememberPassword
	 * @return
	 */
	public int changePass(int shopsid, String nowpass, String rnowpass) {
		Log.d("TAG", "login");
		try {

			JSONObject jo = new JSONObject();
			jo.put("shopsid", shopsid);
			jo.put("nowpass", nowpass);
			jo.put("rnowpass", rnowpass);
			String res = HttpUtil.doPost(Constant.UPDATE_PASSWORD_URI,
					"[[\"parm\"," + jo.toString() + "]]");
			Log.d("TAG", "rj.getInt(res) : " + res);
			JSONObject rj = new JSONObject(res);
			Log.d("TAG", "rj.getInt(res) : " + rj.getInt("res"));
			if (rj.getInt("res") == 0) {
				// lm.saveLogin(usrName, shopsId, isRememberPassword);
				return 0;
			}
			return rj.getInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("TAG", "" + e.toString());
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 修改密码 nowpass string 新密码 rnowpass string 重复新密码
	 * 
	 * @param usrName
	 * @param passwd
	 * @param isRememberPassword
	 * @return
	 */
	public int changePass2(int shopsid, String pass, String newpass) {
		Log.d("TAG", "changePass2");
		try {

			JSONObject jo = new JSONObject();
			jo.put("shopsid", shopsid);
			jo.put("pass", pass);
			jo.put("newpass", newpass);
			String res = HttpUtil.doPost(Constant.UPDATE_PASSWORD_URI2,
					"[[\"parm\"," + jo.toString() + "]]");
			Log.d("TAG", "rj.getInt(res) : " + res);
			JSONObject rj = new JSONObject(res);
			Log.d("TAG", "rj.getInt(res) : " + rj.getInt("res"));
			if (rj.getInt("res") == 0) {
				// lm.saveLogin(usrName, shopsId, isRememberPassword);
				return 0;
			}
			errorMsg = rj.getString("msg");
			return rj.getInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("TAG", "" + e.toString());
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 退出登陆
	 * 
	 * @param shopsid
	 * @return
	 */
	public boolean exitLogin(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.EXIT_LOGIN_URI + "&shopsid="
					+ shopsid);

			System.out.println("-----exitlogin--url---->"
					+ Constant.EXIT_LOGIN_URI + "&shopsid=" + shopsid);
			System.out.println("-----exitlogin--res---->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				LoginManager lm = LoginManager.getInstance(context);
				lm.delLogin();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return false;
	}

	/**
	 * 获取我的推荐码
	 * 
	 * @param id
	 * @return
	 */
	public String getMyCode(int id) {
		try {
			System.out.println("--url--->" + Constant.MY_RECOMMEND_CODE_URI
					+ "&id=" + id);
			String res = HttpUtil.doGet(Constant.MY_RECOMMEND_CODE_URI + "&id="
					+ id);

			System.out.println("-----getMyCode----->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return jsonObject.getString("recode");
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
	 * 获取他人推荐码
	 * 
	 * @param id
	 * @param recode
	 *            获取他人推荐码的验证码
	 * @return
	 */
	public String getOtherCode(int id) {
		try {
			System.out.println("--url--->" + Constant.OTHER_RECOMMEND_CODE_URI
					+ "&id=" + id);
			String res = HttpUtil.doGet(Constant.OTHER_RECOMMEND_CODE_URI
					+ "&id=" + id);

			System.out.println("-----getOtherCode----->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return jsonObject.getString("recode");
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
	 * 添加他人推荐码
	 * 
	 * @param id
	 * @param recode
	 * @return
	 */
	public String addOtherRecode(int id, String recode) {
		try {
			System.out.println("---url------>"
					+ Constant.ADD_OTHER_RECOMMEND_CODE_URI + "&id=" + id
					+ "&recode=" + recode);
			String res = HttpUtil.doGet(Constant.ADD_OTHER_RECOMMEND_CODE_URI
					+ "&id=" + id + "&recode=" + recode);

			System.out.println("-----addOtherRecode----->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return jsonObject.getString("recode");
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
	 * 得到推荐商家或者用户的列表
	 * 
	 * @return
	 */
	public List<RecommendUser> getRecommendUsers(int tag, String recode, int p) {
		// FIXME
		try {
			System.out.println("---url->" + Constant.RECOMMEND_LIST_URI
					+ "&tag=" + tag + "&recode=" + recode + "&p=" + p);
			String res = HttpUtil.doGet(Constant.RECOMMEND_LIST_URI + "&tag="
					+ tag + "&recode=" + recode + "&p=" + p);

			return handleRecommendUsersRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 得到推荐商家或者用户的列表
	 * 
	 * @return
	 */
	public List<RecommendUser> getRecommendUsers2(int tag, String recode, int p) {
		// FIXME
		try {
			System.out.println("---url->" + Constant.RECOMMEND_LIST_URI2
					+ "&tag=" + tag + "&recode=" + recode + "&p=" + p);
			String res = HttpUtil.doGet(Constant.RECOMMEND_LIST_URI2 + "&tag="
					+ tag + "&recode=" + recode + "&p=" + p);

			return handleRecommendUsersRes2(res, tag);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 获取奖励规则
	 * 
	 * @param type
	 * @return
	 */
	public String getRule(int prep) {
		try {
			System.out.println("------getRule------>" + Constant.RULE_URI
					+ "&prep=" + prep + "&type=1");
			String res = HttpUtil.doGet(Constant.RULE_URI + "&prep=" + prep
					+ "&type=1");

			JSONObject jsonObject = new JSONObject(res);
			System.out.println("-----res----->" + res);
			if (jsonObject.optInt("res") == 0) {
				/*
				 * JSONArray jsonArray = jsonObject.getJSONArray("data"); //
				 * StringBuffer stringBuffer = new StringBuffer(); List<String>
				 * list = new ArrayList<String>(); for(int i =0;i
				 * <jsonArray.length();i++){ JSONObject jo =
				 * jsonArray.getJSONObject(i); //
				 * stringBuffer.append(jo.getString("content"));
				 * list.add(jo.getString("content")); }
				 */
				return jsonObject.getString("data");
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<RecommendUser> handleRecommendUsersRes(String res) {
		try {
			System.out.println("----handleRecommendUsersRes-------->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				total_page = jsonObject.optInt("totalnum", -1);
				p = jsonObject.optInt("p", -1);
				List<RecommendUser> list = new ArrayList<RecommendUser>();
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.getJSONObject(i);
					int id = -1;
					if (!jObject.isNull("id")) {
						id = jObject.getInt("id");
					}
					String phone = "";
					if (!jObject.isNull("phone")) {
						phone = jObject.getString("phone");
					}
					String nickname = "";
					if (!jObject.isNull("nickname")) {
						nickname = jObject.getString("nickname");
					}
					String name = "";
					if (!jObject.isNull("name")) {
						name = jObject.getString("name");
					}
					String addtime = "";
					if (!jObject.isNull("addtime")) {
						addtime = jObject.getString("addtime");
					}
					int level = -1;
					if (!jObject.isNull("level")) {
						level = jObject.getInt("level");
					}
					RecommendUser recommendUser = new RecommendUser(id, phone,
							nickname, name, addtime, level);
					list.add(recommendUser);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<RecommendUser> handleRecommendUsersRes2(String res, int tag) {
		try {
			System.out.println("----handleRecommendUsersRes2-------->" + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				total_page = jsonObject.optInt("totalnum", -1);
				p = jsonObject.optInt("p", -1);
				List<RecommendUser> list = new ArrayList<RecommendUser>();
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.getJSONObject(i);
					int id = -1;
					if (!jObject.isNull("id")) {
						id = jObject.getInt("id");
					}
					String phone = "";
					if (!jObject.isNull("phone")) {
						phone = jObject.getString("phone");
					}
					String addtime = "";
					if (!jObject.isNull("addtime")) {
						addtime = jObject.getString("addtime");
					}
					if (tag == 1) {// 用户
						String nickname = "";
						if (!jObject.isNull("nickname")) {
							nickname = jObject.getString("nickname");
						}
						RecommendUser recommendUser = new RecommendUser(id,
								phone, nickname, addtime);
						list.add(recommendUser);
					} else {
						String shopsname = "";
						if (!jObject.isNull("shopsname")) {
							shopsname = jObject.getString("shopsname");
						}

						String typename = "";
						if (!jObject.isNull("typename")) {
							typename = jObject.getString("typename");
						}
						RecommendUser recommendUser = new RecommendUser(id,
								phone, shopsname, addtime, typename);
						list.add(recommendUser);
					}

				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查更新版本信息
	 * 
	 * @param id
	 * @param recode
	 * @return
	 */
	public VersionInfo getUpdateInfo(int v) {
		try {
			System.out.println("---url--getUpdateInfo---->"
					+ Constant.APP_UPDATE_URI + "&code=" + v);
			String res = HttpUtil.doGet(Constant.APP_UPDATE_URI + "&code=" + v);

			System.out.println("-----getUpdateInfo----->" + res);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("result") == 0) {
				String filepath = "";
				if (!jo.isNull("filepath")) {
					filepath = jo.getString("filepath");
				}
				String updateinfo = "";
				if (!jo.isNull("updateinfo")) {
					updateinfo = jo.getString("updateinfo");
				}
				String versioncode = "";
				if (!jo.isNull("versioncode")) {
					versioncode = jo.getString("versioncode");
				}
				String versionname = "";
				if (!jo.isNull("versionname")) {
					versionname = jo.getString("versionname");
				}
				String is_update = "";
				if (!jo.isNull("is_update")) {
					is_update = jo.getString("is_update");
				}
				VersionInfo info = new VersionInfo(filepath, updateinfo,
						versioncode, versionname, is_update);
				return info;
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
	 * 获取联系方式
	 * 
	 * @return
	 */
	public List<String> getContactList(int shopsid) {

		try {
			String res = HttpUtil.doGet(Constant.GET_CONTACT_LIST_URI + "&id="
					+ shopsid);

			return handleGetContactListList(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<String> handleGetContactListList(String res) {
		System.out.println("---handleGetContactListList----->" + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				JSONArray jsonArray = jo.getJSONArray("data");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject j = jsonArray.getJSONObject(i);
					list.add(j.getString("phone1"));
					list.add(j.getString("phone2"));
					list.add(j.getString("phone3"));
				}
				return list;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新联系人列表
	 * 
	 * @param shopsid
	 * @param phone1
	 * @param phone2
	 * @param phone3
	 * @return
	 */
	public String updateContactList(int shopsid, String phone, String phone1,
			String phone2, String phone3) {
		try {

			// try {
			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\"" + phone
					+ "\"],[\"fixphone_arr\",\"" + phone1 + "," + phone2 + ","
					+ phone3 + "\"]]";
			String res = HttpUtil.doPost(Constant.SHOP_UPDATE, string);
			return res;
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// return null;
			// JSONObject jsonObject = new JSONObject();
			// jsonObject.put("shopsid", shopsid);
			// jsonObject.put("phone", phone);
			// jsonObject.put("phone1", phone1);
			// jsonObject.put("phone2", phone2);
			// jsonObject.put("phone3", phone3);

			// String res = HttpUtil.doPost(Constant.SHOP_UPDATE, "parm",
			// jsonObject.toString());
			// System.out.println("-----updateContactList------>" + res);
			// System.out
			// .println("-----jsonObject------>" + jsonObject.toString());
			// JSONObject j = new JSONObject(res);
			// return j.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean handleUpdateContactList(String res) {
		try {
			JSONObject j = new JSONObject(res);
			if (j.optInt("res") == 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查看店铺介绍
	 * 
	 * @param shopsid
	 * @return
	 */
	public String lookShopContent(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.GET_SHOP_CONTENT_URI + "&id="
					+ shopsid);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				return jsonObject.getString("content");
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新店铺介绍
	 * 
	 * @param shopsid
	 * @param content
	 * @return
	 */
	public String updateShopContent(int shopsid, String phone, String content) {
		try {
			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\"" + phone
					+ "\"],[\"content\",\"" + content + "\"]]";
			String res = HttpUtil.doPost(Constant.SHOP_UPDATE, string);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新店铺广告语
	 * 
	 * @param shopsid
	 * @param content
	 * @return
	 */
	public String updateShopAdvertisingSlogan(int shopsid, String phone,
			String content) {
		String string = "";
		string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\"" + phone
				+ "\"],[\"slogan\",\"" + content + "\"]]";
		try {
			String res = HttpUtil.doPost(Constant.SHOP_UPDATE, string);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String analysisContact(String res) throws JSONException {
		JSONObject j = new JSONObject(res);
		if (j.optInt("res") == 0) {
			return "0";
		} else {
			return j.optString("msg");
		}
	}

	/**
	 * 返回开通电子账户的状态
	 * 
	 * @param shopsid
	 * @return
	 */
	public String getAccountStatus(int shopsid) {
		try {
			System.out.println("-------url------>"
					+ Constant.SETTING_STATUS_URI + "&shopsid=" + shopsid);
			String res = HttpUtil.doGet(Constant.SETTING_STATUS_URI
					+ "&shopsid=" + shopsid);
			System.out.println("-----erer------->" + res);

			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getSelectBank(int shopsid, String phone) {

		String string = "";
		string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\"" + phone
				+ "\"]]";
		try {
			String res = HttpUtil.doPost(Constant.SELECT_ONLINE, string);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 开通电子收银台提交审核接口
	 * 
	 */

	// shopsid int 商店id
	// Mobile string 手机号
	// Email string 邮箱
	// ReferrerCode string 推荐码
	// userIdName string 户名
	// userIdNo string 身份证号
	// local_idCardPath1 string 本地身份证正面
	// local_idCardPath2 string 本地身份证反面
	// local_idCardHandPath string 本地手持身份证
	// bank_idCardPath1 string 上传银行身份证正面
	// bank_idCardPath2 string 上传银行身份证反面
	// bank_idCardHandPath string 上传银行手持身份证

	public String getAllSubimt(int shopsid, String Mobile, String Email,
			String ReferrerCode, String userIdName, String userIdNo,
			String local_idCardPath1, String local_idCardPath2,
			String local_idCardHandPath, String bank_idCardPath1,
			String bank_idCardPath2, String bank_idCardHandPath, int autoFinance)
			throws Exception {

		String string = "";
		string = "[[\"shopsid\",\"" + shopsid + "\"],[\"Mobile\",\"" + Mobile
				+ "\"],[\"Email\",\"" + Email + "\"],[\"ReferrerCode\",\""
				+ ReferrerCode + "\"],[\"userIdName\",\"" + userIdName + "\"],"
				+ "[\"userIdNo\",\"" + userIdNo + "\"],"
				+ "[\"local_idCardPath1\",\"" + local_idCardPath1 + "\"],"
				+ "[\"local_idCardPath2\",\"" + local_idCardPath2 + "\"],"
				+ "[\"local_idCardHandPath\",\"" + local_idCardHandPath
				+ "\"]," + "[\"bank_idCardPath1\",\"" + bank_idCardPath1
				+ "\"]," + "[\"bank_idCardPath2\",\"" + bank_idCardPath2
				+ "\"]," + "[\"bank_idCardHandPath\",\"" + bank_idCardHandPath
				+ "\"]," + "[\"autoFinance\",\"" + autoFinance + "\"]]";
		String res = HttpUtil.doPost(Constant.SUBMIT_ALL, string);
		return res;

	}

	public String updataShopContent(int shopsid, String phone, String logo,
			String shopsname, String name, String identity_number,
			double longitude, double latitude, String address,
			String detail_address, int typeid, String license,
			String license_number) {
		String res;
		String string = "";
		if (identity_number.equals("")) {
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\"" + phone
					+ "\"],[\"logo\",\"" + logo + "\"],[\"shopsname\",\""
					+ shopsname + "\"],[\"name\",\"" + name + "\"],"
					+ "[\"longitude\",\"" + longitude + "\"],"
					+ "[\"latitude\",\"" + latitude + "\"],"
					+ "[\"address\",\"" + address + "\"],"
					+ "[\"detail_address\",\"" + detail_address + "\"],"
					+ "[\"typeid\",\"" + typeid + "\"]," + "[\"license\",\""
					+ license + "\"], " + "[\"license_number\",\""
					+ license_number + "\"]]";
		} else {
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"phone\",\"" + phone
					+ "\"],[\"logo\",\"" + logo + "\"],[\"shopsname\",\""
					+ shopsname + "\"],[\"name\",\"" + name + "\"],"
					+ "[\"identity_number\",\"" + identity_number + "\"],"
					+ "[\"longitude\",\"" + longitude + "\"],"
					+ "[\"latitude\",\"" + latitude + "\"],"
					+ "[\"address\",\"" + address + "\"],"
					+ "[\"detail_address\",\"" + detail_address + "\"],"
					+ "[\"typeid\",\"" + typeid + "\"]," + "[\"license\",\""
					+ license + "\"], " + "[\"license_number\",\""
					+ license_number + "\"]]";
		}
		try {
			res = HttpUtil.doPost(Constant.UPDATE_SHOP_ZIZHI, string);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getDisableStatus(int shopsid) {
		try {
			System.out.println("-------url------>" + Constant.SHOP_STATUS
					+ "&shopsid=" + shopsid);
			String res = HttpUtil.doGet(Constant.SHOP_STATUS + "&shopsid="
					+ shopsid);
			System.out.println("-----erer------->" + res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
