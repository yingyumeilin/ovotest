package com.example.oto01.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.oto01.model.City;
import com.example.oto01.model.Constant;
import com.example.oto01.model.LoginReturnMsg;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.model.ShopType;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.TelephoneUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 注册管理
 * 
 * @author lqq
 * 
 */
public class RegisterService extends BaseHttpService {
	public String errorMsg;
	private Context context;

	public RegisterService(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 获取城市列表
	 * 
	 * @param parentid
	 * @return
	 */
	public List<City> getCitieList(int parentid) {
		try {
			String res = null;
			if (parentid == 0) {
				res = HttpUtil.doGet(Constant.CITY_LIST_URI);
			} else {
				res = HttpUtil.doGet(Constant.CITY_LIST_URI + "&parentid="
						+ parentid);
			}

			return handleCitieListRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}

		return null;
	}

	private List<City> handleCitieListRes(String res) {
		Log.d("TAG", "res handleCitieListRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				List<City> list = new ArrayList<City>();
				JSONArray ja = jo.getJSONArray("city");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jsonObject = ja.getJSONObject(i);
					int usetype = -1;
					if (!jsonObject.isNull("usetype")
							&& !jsonObject.get("usetype").equals(null)) {
						usetype = jsonObject.getInt("usetype");
					}
					int pid = 0;
					if (!jsonObject.isNull("pid")
							&& !jsonObject.get("pid").equals(null)) {
						pid = jsonObject.getInt("pid");
					}
					int level = 0;
					if (!jsonObject.isNull("level")
							&& !jsonObject.get("level").equals(null)) {
						level = jsonObject.getInt("level");
					}
					City city = new City(jsonObject.getInt("id"),
							jsonObject.getString("name"), pid,
							jsonObject.getString("path"), 0.0f, 0.0f, level);
					// City city = new City(,
					// jsonObject.getInt("level"),usetype, parentid,
					// displayorder);
					list.add(city);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int checkCode(String code, String phone, int type, String recode) {
		try {
			System.out.println("-----url---->" + Constant.CHECK_PHONE_CODE_URI
					+ "&code=" + code + "&phone=" + phone + "&recode=" + recode
					+ "&type=" + type);
			String res = HttpUtil.doGet(Constant.CHECK_PHONE_CODE_URI
					+ "&code=" + code + "&phone=" + phone + "&recode=" + recode
					+ "&type=" + type);
			System.out.println("---------验证手机验证码----->" + res);

			JSONObject rj = new JSONObject(res);
			if (rj.getInt("res") == 0) {
				// lm.saveLogin(usrName, shopsId, isRememberPassword);
				return 0;
			}
			errorMsg = rj.getString("msg");
			return rj.getInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	// /**
	// * 验证注册接口
	// *
	// * @return
	// */
	// public int registerNew(String phone, String pass, String rid,
	// String shopsname, double longitude, double latitude,
	// String address, String detail_address, int typeid, String name,
	// String identity_number, String content, String logo,
	// String license, String time_one) {
	// Log.d("TAG", "register");
	// try {
	// 11111
	// JSONObject jo = new JSONObject();
	// // jo.put("cid", gbcityid);
	// jo.put("phone", phone);
	// jo.put("pass", pass);
	// jo.put("rid", rid);
	// jo.put("shopsname", shopsname);
	// jo.put("longitude", longitude);
	// jo.put("latitude", latitude);
	// jo.put("address", address);
	// jo.put("detail_address", detail_address);
	// jo.put("typeid", typeid);
	// jo.put("name", name);
	// jo.put("identity_number", identity_number);
	// jo.put("content", content);
	// jo.put("logo", logo);
	// jo.put("license", license);
	// jo.put("time_one", time_one);
	// jo.put("imei", TelephoneUtil.getImei(context));
	// System.out.println("-----json---->" + jo.toString());
	// String res = HttpUtil.doPost(Constant.REGISTER_UPDATE,
	// jo.toString());
	// System.out.println("------register---res------->" + res);
	// Log.d("TAG", "rj.getInt(res) : " + res);
	// JSONObject jsonObject = new JSONObject(res);
	// if (jsonObject.optInt("res") == 0) {
	// LoginManager lm = LoginManager.getInstance(context);
	// lm.delLogin();
	// lm.saveLogin(phone, jsonObject.optInt("id"), false);
	// JSONObject rongJO = jsonObject.getJSONObject("rongIM");
	// if (rongJO != null)
	// lm.saveRongInfo(rongJO.optString("userId"),
	// rongJO.optString("token"));
	// return jsonObject.optInt("id");
	// } else {
	// System.out.println("-------error code------->"
	// + jsonObject.optInt("res"));
	// return jsonObject.optInt("res");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// if (HttpUtil.TIME_OUT) {
	// ToastUtil.show(context, "当前网络状况不好！");
	// }
	//
	// }
	// return -1;
	// }

	public void ceshi(double lat, double lng) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("lat", lat);
			jsonObject.put("lng", lng);
			String res = HttpUtil.doPost(Constant.ceshi, "parm",
					jsonObject.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
	}

	/**
	 * 添加店铺LOGO
	 * 
	 * @param shopsid
	 * @param logo
	 * @return
	 */
	public boolean uploadLogo(int shopsid, String logo) {
		try {
			// post 方式
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", shopsid);
			jsonObject.put("logo", logo);
			System.out.println("---logo------>" + logo);
			System.out.println("------logo-url------->"
					+ Constant.uploadLogoURL + "-->shopsid" + shopsid);
			System.out.println("------json----->" + jsonObject.toString());
			String res = HttpUtil.doPost(Constant.uploadLogoURL, "parm",
					jsonObject.toString());

			// get 方式
			// String res =
			// HttpUtil.doGet(Constant.uploadLogoURL+"&id="+shopsid+"&logo="+logo);
			System.out.println("------上传Logo--------->" + res);

			JSONObject jObject = new JSONObject(res);
			if (jObject.optInt("res") == 0) {
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
	 * 商店类别列表
	 * 
	 * @return
	 */
	public List<ShopType> getShopTypeList() {
		try {
			String res = HttpUtil.doGet(Constant.SHOP_TYPE_LIST_URI);
			System.out.println("-------getShopTypeList res-------->" + res);
			return handleShopTypeListRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<ShopType> handleShopTypeListRes(String res) {
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				List<ShopType> list = new ArrayList<ShopType>();
				JSONArray ja = jo.getJSONArray("shopstype");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jsonObject = ja.getJSONObject(i);
					ShopType shopType = new ShopType(jsonObject.getInt("id"),
							jsonObject.getString("name"),
							jsonObject.getInt("pid"),
							jsonObject.getString("path"),
							jsonObject.getString("picname"));
					list.add(shopType);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean getSimpleRes(String res) {
		try {
			JSONObject jsonObject = new JSONObject(res);
			System.out.println("---res 0--->" + jsonObject.optInt("res"));

			if (jsonObject.optInt("res") == 0) {
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
	 * 上传营业执照
	 * 
	 * @return
	 */
	public boolean uploadLicense(int shopsid, String license) {
		Log.d("TAG", "uploadLicense");
		try {
			System.out.println("-----uploadLicense-url-->"
					+ Constant.UPLOAD_LICENSE_URI + "&id=" + shopsid
					+ "&license=" + license);
			// String newLis = license.replaceAll("/", "\\/");
			// System.out.println("------license------->"+newLis);
			String res = HttpUtil.doGet(Constant.UPLOAD_LICENSE_URI + "&id="
					+ shopsid + "&license=" + license);
			// String res =
			// HttpUtil.doGet(Constant.UPLOAD_LICENSE_URI+"&id="+shopsid+"&license="+"/Uploads/License/1405/21/15/537c5a959a60b.jpg");
			System.out.println("----uploadLicense--res-->" + res);

			return getSimpleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return false;
	}

	public List<ShopInfo> searchShopInfos(double lng, double lat) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("lng", lng);
			jsonObject.put("lat", lat);
			// jsonObject.put("uid", uid);
			String res = HttpUtil.doPost(Constant.RANGE_SHOP_LIST_URI, "parm",
					jsonObject.toString());

			System.out.println("--------->" + res);
			JSONObject jObject = new JSONObject(res);
			if (jObject.optInt("res") == 0) {
				List<ShopInfo> list = new ArrayList<ShopInfo>();
				JSONArray jsonArray = jObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = jsonArray.getJSONObject(i);
					ShopInfo shopInfo = new ShopInfo(jo.getInt("id"),
							jo.getString("shopsname"), "name",
							jo.getString("phone"), jo.getString("address"),
							jo.getDouble("longitude"), jo.getDouble("latitude"));
					list.add(shopInfo);
				}
				return list;
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
	 * 获取注册参数是否为必填
	 * 
	 * @return
	 */
	public Map<String, String> checkInputInfoIsSpace() {
		Log.d("TAG", "checkInputInfoIsSpace");
		try {
			System.out.println("-----checkInputInfoIsSpace-url-->"
					+ Constant.CHECK_INPUT_INFO_SPACE_URI);
			String res = HttpUtil.doGet(Constant.CHECK_INPUT_INFO_SPACE_URI);
			System.out.println("----checkInputInfoIsSpace--res-->" + res);
			JSONObject jObject = new JSONObject(res);
			if (jObject.optInt("res") == 0) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject object = jObject.getJSONObject("shopsinfo");
				map.put("phone", object.getString("phone"));
				map.put("pass", object.getString("pass"));
				map.put("rid", object.getString("rid"));
				map.put("shopsname", object.getString("shopsname"));
				map.put("cid", object.getString("cid"));
				map.put("longitude", object.getString("longitude"));
				map.put("latitude", object.getString("latitude"));
				map.put("address", object.getString("address"));
				map.put("typeid", object.getString("typeid"));
				map.put("name", object.getString("name"));
				map.put("identity_number", object.getString("identity_number"));
				map.put("content", object.getString("content"));
				map.put("logo", object.getString("logo"));
				map.put("license", object.getString("license"));
				map.put("is_send", object.getString("is_send"));
				map.put("send_distance_m", object.getString("send_distance_m"));
				map.put("sendprice", object.getString("sendprice"));
				map.put("freight_price", object.getString("freight_price"));
				map.put("time_one", object.getString("time_one"));
				return map;
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
	 * 新版注册接口
	 * 
	 * @return
	 */
	public LoginReturnMsg registerNew(String phone, String pass, String rid,
			String shopsname, double longitude, double latitude,
			String address, String detail_address, int typeid, String name,
			String identity_number, String content, String logo,
			String license, String license_number, String time_one) {
		Log.d("TAG", "register");
		String string = "";
		LoginReturnMsg loginReturnMsg = null;
		try {
			string = "[[\"phone\",\"" + phone + "\"],[\"pass\",\"" + pass
					+ "\"],[\"rid\",\"" + rid + "\"],[\"shopsname\",\""
					+ shopsname + "\"],[\"longitude\",\"" + longitude + "\"],"
					+ "[\"latitude\",\"" + latitude + "\"],"
					+ "[\"address\",\"" + address + "\"],"
					+ "[\"detail_address\",\"" + detail_address + "\"],"
					+ "[\"typeid\",\"" + typeid + "\"]," + "[\"name\",\""
					+ name + "\"]," + "[\"identity_number\",\""
					+ identity_number + "\"]," + "[\"content\",\"" + content
					+ "\"]," + "[\"logo\",\"" + logo + "\"],"
					+ "[\"license\",\"" + license + "\"],"
					+ "[\"license_number\",\"" + license_number + "\"],"
					+ "[\"time_one\",\"" + time_one + "\"]," + "[\"imei\",\""
					+ TelephoneUtil.getImei(context) + "\"]]";
			System.out.println("----register-url------->"
					+ Constant.REGISTER_UPDATE);
			System.out.println("-----json-string-->" + string);
			String res = HttpUtil.doPost(Constant.REGISTER_UPDATE, string);
			System.out.println("------register---res------->" + res);
			Log.d("TAG", "rj.getInt(res) : " + res);
			JSONObject jsonObject = new JSONObject(res);
			if (jsonObject.optInt("res") == 0) {
				LoginManager lm = LoginManager.getInstance(context);
				lm.delLogin();
				lm.saveLogin(phone, jsonObject.optInt("id"), false);
				JSONObject rongJO = jsonObject.getJSONObject("rongIM");
				if (rongJO != null)
					lm.saveRongInfo(rongJO.optString("userId"),
							rongJO.optString("token"));
				// return jsonObject.optInt("id");
				loginReturnMsg = new LoginReturnMsg(jsonObject.optInt("res"),
						jsonObject.getString("msg"), jsonObject.optInt("id"));
			} else {
				System.out.println("-------error code------->"
						+ jsonObject.optInt("res"));
				errorMsg = jsonObject.getString("msg");
				// return jsonObject.optInt("res");
				loginReturnMsg = new LoginReturnMsg(jsonObject.optInt("res"),
						jsonObject.getString("msg"), jsonObject.optInt("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}

		}
		return loginReturnMsg;
	}
}
