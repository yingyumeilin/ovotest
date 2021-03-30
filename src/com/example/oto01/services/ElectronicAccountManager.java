package com.example.oto01.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.oto01.model.Login;

public class ElectronicAccountManager {
	private static int adminId;
	private static Context context;
	private static ElectronicAccountManager lm;
	private Login login;

	public static ElectronicAccountManager getInstance(Context cx) {
		context = cx.getApplicationContext();
		if (lm == null) {
			lm = new ElectronicAccountManager();
		}
		return lm;
	}

//	public void saveLogin(String uanme, int adminId, boolean isRememberPassword) {
//		SharedPreferences sp = context.getSharedPreferences("login",
//				Context.MODE_PRIVATE);
//		System.out.println("-------保存登陆信息------->"+adminId);
//		Editor ed = sp.edit();
//		ed.putString("username", uanme);
//		ed.putInt("shopsid", adminId);
//		ed.putBoolean("is_remember_password", isRememberPassword);
//		ed.commit();
//	}
//	public void saveContact(String contact) {
//		SharedPreferences sp = context.getSharedPreferences("login",
//				Context.MODE_PRIVATE);
//		System.out.println("-------保存联系人信息------->"+contact);
//		Editor ed = sp.edit();
//		ed.putString("contact", contact);
//		ed.commit();
//	}
	public void setElectronicAccountStatus(int tag) {
		SharedPreferences sp = context.getSharedPreferences("electronicAccountStatus",
				Context.MODE_PRIVATE);
		System.out.println("-------保存开启应用信息------->"+tag);
		Editor ed = sp.edit();
		ed.putInt("status", tag);
		ed.commit();
	}
//
//	public void delLogin() {
//		SharedPreferences sp = context.getSharedPreferences("login",
//				Context.MODE_PRIVATE);
//		System.out.println("-------删除登陆信息------->"+adminId);
//		Editor ed = sp.edit();
////		ed.remove("username");
//		ed.remove("shopsid");
//		ed.remove("contact");
//		ed.remove("tag");
//		ed.remove("is_remember_password");
////		ed.clear();
//		ed.commit();
//	}
//	public void delTag() {
//		SharedPreferences sp = context.getSharedPreferences("login",
//				Context.MODE_PRIVATE);
//		System.out.println("-------删除Tag信息------->");
//		Editor ed = sp.edit();
//		ed.remove("tag");
//		ed.commit();
//	}
//
//	public Login getLoginInstance() {
//		SharedPreferences sp = context.getSharedPreferences("login",
//				Context.MODE_PRIVATE);
//		String uname = sp.getString("username", null);
//		if (uname != null && uname.trim().length() != 0) {
//			boolean isRememberPassword = sp.getBoolean("is_remember_password",
//					false);
//			int adminId = sp.getInt("shopsid", -1);
//			login = new Login(uname, adminId, isRememberPassword);
//		}
//		return login;
//	}
//
//	public int getLoginId() {
//		SharedPreferences sp = context.getSharedPreferences("login",
//				Context.MODE_PRIVATE);
//		String uname = sp.getString("username", null);
//		if (uname != null && uname.trim().length() != 0) {
//			adminId = sp.getInt("shopsid", -1);
//		}
//		return adminId;
//	}
//	public String getContact() {
//		SharedPreferences sp = context.getSharedPreferences("login",
//				Context.MODE_PRIVATE);
//		String contact = sp.getString("contact", null);
//		if (contact != null && contact.trim().length() != 0) {
//			return contact;
//		}
//		return null;
//	}
	public int getElectronicAccountStatus() {
		SharedPreferences sp = context.getSharedPreferences("electronicAccountStatus",
				Context.MODE_PRIVATE);
		int tag = sp.getInt("status", -2);
		if (tag != -2) {
			return tag;
		}
		return -2;
	}
//
//	public boolean isLogined() {
//		ElectronicAccountManager lm = getInstance(context);
//		Login login = lm.getLoginInstance();
//		if (login != null && login.getAdminId() != -1) {
//			return true;
//		} else {
//			return false;
//		}
//	}
}