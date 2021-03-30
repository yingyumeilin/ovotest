package com.example.oto01.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ClientsUtil {

	private static final String CLIENT = "client";
	private static final String FIRST_LOGIN = "FIRST_LOGIN";

	public static void saveClientState(Context context, int state) {
		SharedPreferences sp = context.getSharedPreferences(CLIENT,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("custom", state);
		editor.commit();
	}

	public static int getClientState(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CLIENT,
				Context.MODE_PRIVATE);
		return sp.getInt("custom", 0);
	}

	public static void saveMoneyState(Context context, int state) {
		SharedPreferences sp = context.getSharedPreferences(CLIENT,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("money", state);
		editor.commit();
	}

	public static int getMoneyState(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CLIENT,
				Context.MODE_PRIVATE);
		return sp.getInt("money", 0);
	}

	public static void setFirstLogin(Context context, Boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("firstLogin", flag);
		editor.commit();
	}

	public static boolean getFirstLogin(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		return sp.getBoolean("firstLogin", false);
	}

	public static void setFirstLoginShopCheck(Context context, Boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("firstLoginShopCheck", flag);
		editor.commit();
	}

	public static boolean geFirstLoginShopChec(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		return sp.getBoolean("firstLoginShopCheck", false);
	}

	/**
	 * 
	 * TODO<判断是不是首次登录，用于手势密码>
	 * 
	 * @throw
	 * @return void
	 */
	public static void setFirstLoginGesture(Context context, Boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("firstLoginGesture", flag);
		editor.commit();
	}

	public static boolean geFirstLoginGesture(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		return sp.getBoolean("firstLoginGesture", false);
	}

	/**
	 * 
	 * TODO<第二次登录进行手势密码设置的提醒的弹框中的第一次>
	 * 
	 * @throw
	 * @return void
	 */
	public static void setFirstLoginSettingKuang(Context context, Boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("firstLoginSettingKuang", flag);
		editor.commit();
	}

	public static boolean getFirstLoginSettingKuang(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		return sp.getBoolean("firstLoginSettingKuang", false);
	}

	/**
	 * 
	 * TODO<第二次登录进行手势密码设置的提醒的弹框中的第二次>
	 * 
	 * @throw
	 * @return void
	 */
	public static void setFirstSecondSettingKuang(Context context, Boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("firstSecondSettingKuang", flag);
		editor.commit();
	}

	public static boolean getFirstSecondSettingKuang(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FIRST_LOGIN,
				Context.MODE_PRIVATE);
		return sp.getBoolean("firstSecondSettingKuang", false);
	}

}
