package com.example.oto01.services;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.oto01.model.Constant;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 登陆管理
 * 
 * @author lqq
 * 
 */
public class LoginService extends BaseHttpService {
	public String errorMsg;

	public LoginService(Context context) {
		super(context);
	}

	/**
	 * 登陆
	 * 
	 * @param usrName
	 * @param passwd
	 * @param isRememberPassword
	 * @return
	 */
	public int login(String phone, String passwd, boolean isRememberPassword) {
		Log.d("TAG", "login");
		try {
			JSONObject jo = new JSONObject();
			jo.put("phone", phone);
			jo.put("pass", passwd);
			System.out
					.println("---------开始登录----------->" + Constant.LOGIN_URI);
			String res = HttpUtil.doPost(Constant.LOGIN_URI, "parm",
					jo.toString());

			System.out.println("----------res------->" + res);
			JSONObject rj = new JSONObject(res);
			Log.d("TAG", "rj.getInt(res) : " + rj.getInt("res"));
			if (rj.getInt("res") == 0) {
				int adminId = rj.getInt("shopsid");

				LoginManager lm = LoginManager.getInstance(context);
				lm.delLogin();
				lm.saveLogin(phone, adminId, isRememberPassword);
				if (!rj.isNull("rongIM")) {
					JSONObject rongJO = rj.getJSONObject("rongIM");
					if (rongJO != null) {
						String userId = null;
						String token = null;
						if (!rongJO.isNull("userId")) {
							userId = rongJO.optString("userId");
						}
						if (!rongJO.isNull("token")) {
							token = rongJO.optString("token");
						}
						lm.saveRongInfo(userId, token);
					}
				}
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
	 * 忘记密码
	 * 
	 * @param phone
	 * @param newpass
	 * @param code
	 * @return
	 */
	public int forgetPass(String phone, String newpass, String code) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("phone", phone);
			jsonObject.put("newpass", newpass);
			jsonObject.put("code", code);
			System.out.println("------json----->" + jsonObject.toString());
			String res = HttpUtil.doPost(Constant.FIND_PASS_URI, "parm",
					jsonObject.toString());

			System.out.println("------forgetPass--------->" + res);
			JSONObject jObject = new JSONObject(res);
			if (jObject.optInt("res") == 0) {
				return 0;
			}
			return jObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 获取手机验证码
	 * 
	 * 
	 * @return
	 */
	public String getPhoneCode(String phone, String type, String signature,
			String timestamp, String random, Double longitude, Double latitude,
			String imei, String model_name) {
		try {
			String res = null;
			String string = "";
			string = "[[\"phone\",\"" + phone + "\"],[\"type\",\"" + type
					+ "\"],[\"signature\",\"" + signature
					+ "\"],[\"timestamp\",\"" + timestamp
					+ "\"],[\"random\",\"" + random + "\"],[\"longitude\",\""
					+ longitude + "\"]" + ",[\"latitude\",\"" + latitude
					+ "\"],[\"imei\",\"" + imei + "\"],[\"model_name\",\""
					+ model_name + "\"]]";
			res = HttpUtil.doPost(Constant.SEND_CODE, string);
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
	 * 
	 * TODO<获取服务器时间戳>
	 * 
	 * @throw
	 * @return String
	 */
	public String getTimeService() {
		JSONObject jsonObject = new JSONObject();
		System.out.println("------json----->" + jsonObject.toString());
		String res;
		try {
			res = HttpUtil.doPost(Constant.GET_TIME_SERVER, "parm",
					jsonObject.toString());
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int saveRongInfo(int shopsid) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			String res = HttpUtil.doPost(Constant.GET_RONG_TOKEN, "parm",
					jsonObject.toString());
			System.out.println("------saveRongInfo--------->" + res);
			JSONObject rj = new JSONObject(res);
			if (rj.getInt("res") == 0) {
				LoginManager lm = LoginManager.getInstance(context);
				if (!rj.isNull("rongIM")) {
					JSONObject rongJO = rj.getJSONObject("rongIM");
					if (rongJO != null) {
						String userId = null;
						String token = null;
						if (!rongJO.isNull("userId")) {
							userId = rongJO.optString("userId");
						}
						if (!rongJO.isNull("token")) {
							token = rongJO.optString("token");
						}
						lm.saveRongInfo(userId, token);
					}
				}
				return 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
}