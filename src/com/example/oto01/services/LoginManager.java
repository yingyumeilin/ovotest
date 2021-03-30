package com.example.oto01.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.oto01.model.Login;

public class LoginManager {
	private static int adminId;
	private static Context context;
	private static LoginManager lm;
	private Login login;

	public static LoginManager getInstance(Context cx) {
		context = cx.getApplicationContext();
		if (lm == null) {
			lm = new LoginManager();
		}
		return lm;
	}

	public void saveLogin(String uanme, int adminId, boolean isRememberPassword) {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		System.out.println("-------保存登陆信息------->" + adminId);
		Editor ed = sp.edit();
		ed.putString("username", uanme);
		ed.putInt("shopsid", adminId);
		ed.putBoolean("is_remember_password", isRememberPassword);
		ed.commit();
	}

	public void savePhone(String phone) {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		System.out.println("-------保存登陆信息------->" + adminId);
		Editor ed = sp.edit();
		ed.putString("phone", phone);
		ed.commit();
	}

	public void saveRongInfo(String rongId, String token) {
		if (rongId == null)
			return;
		if (token == null)
			return;
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		System.out.println("-------保存融云信息------->" + adminId);
		Editor ed = sp.edit();
		ed.putString("rong_id", rongId);
		ed.putString("rong_token", token);
		ed.commit();
	}

	public void saveContact(String contact) {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		System.out.println("-------保存联系人信息------->" + contact);
		Editor ed = sp.edit();
		ed.putString("contact", contact);
		ed.commit();
	}

	public void saveTag(int falg) {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		System.out.println("-------保存开启应用信息------->" + falg);
		Editor ed = sp.edit();
		// ed.putInt("tag", tag);
		ed.putInt("tag", falg);
		ed.commit();
	}

	public void delLogin() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		System.out.println("-------删除登陆信息------->" + adminId);
		Editor ed = sp.edit();
		// ed.remove("username");
		ed.remove("shopsid");
		ed.remove("contact");
		ed.remove("is_remember_password");
		ed.remove("rong_token");
		// ed.clear();
		ed.commit();
	}

	/*
	 * public void delTag() { SharedPreferences sp =
	 * context.getSharedPreferences("login", Context.MODE_PRIVATE);
	 * System.out.println("-------删除Tag信息------->"); Ed itor ed = sp.edit();
	 * ed.remove("tag"); ed.commit(); }
	 */

	public Login getLoginInstance() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		String uname = sp.getString("username", null);
		if (uname != null && uname.trim().length() != 0) {
			boolean isRememberPassword = sp.getBoolean("is_remember_password",
					false);
			int adminId = sp.getInt("shopsid", -1);
			login = new Login(uname, adminId, isRememberPassword);
		}
		return login;
	}

	public int getLoginId() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		String uname = sp.getString("username", null);
		if (uname != null && uname.trim().length() != 0) {
			adminId = sp.getInt("shopsid", -1);
		}
		return adminId;
	}

	public String getContact() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		String contact = sp.getString("contact", null);
		if (contact != null && contact.trim().length() != 0) {
			return contact;
		}
		return null;
	}

	public int getTag() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		int tag = sp.getInt("tag", -1);
		return tag;
	}

	public boolean isLogined() {
		LoginManager lm = getInstance(context);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * get token
	 * 
	 * @return
	 */
	public String getToken() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		String token = sp.getString("rong_token", null);
		if (token != null && token.trim().length() != 0) {
			return token;
		}
		return null;
	}
}