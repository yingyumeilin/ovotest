package com.example.oto01.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtil {
	public static long DURATION_TIME = 1000 * 60 * 60 * 60 * 24;// �?��更新的周�?�?��;

	/**
	 * 获取当前程序版本,用于更新版本
	 * 
	 * @return
	 */
	public static double getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名�?代表是获取版本信�?
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			double code = packInfo.versionCode;
			return code;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;

	}
	/**
	 * 获取当前程序版本,用于更新版本
	 * 
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名�?代表是获取版本信�?
		String name=null;
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			name = packInfo.versionName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;

	}
	/**
	 * 保存当前时间戳，用于更新版本
	 */
	public static void saveCurrentTime(Context context) {
		SharedPreferences sp;
		long lastUpdateTime;
		sp = context.getSharedPreferences("version", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		lastUpdateTime = System.currentTimeMillis();
		editor.putLong("lasttime", lastUpdateTime);
		editor.commit();
	}

//	/**
//	 * �?��更新
//	 * 
//	 * @param context
//	 */
//	public static void checkNewVersion(Context context) {
//		// 如果嫌检查太频繁可以放开下面的方�?
//		SharedPreferences sp = context.getSharedPreferences("version",
//				Context.MODE_PRIVATE);
//		long lastUpdateTime = sp.getLong("lasttime", 0);
//		// lastUpdateTime=0; //测试
//		if (lastUpdateTime == 0) {
//			saveCurrentTime(context);
//			new CheckVersionTask(context, 0).execute(); // 0为自动检�?1为手动检�?
//		} else {
//			long current = System.currentTimeMillis();
//			long period = current - lastUpdateTime;
//			// 上次更新时间到现在的时间大于�?��,则更�?
//
//			if (period > DURATION_TIME) {
//				saveCurrentTime(context);
//				new CheckVersionTask(context, 0).execute();
//			}
//		}
//		// update();
//
//	}
}
