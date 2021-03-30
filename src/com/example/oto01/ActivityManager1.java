package com.example.oto01;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;

public class ActivityManager1 {

	private Context context;

	private static ActivityManager1 activityManager;

	public static ActivityManager1 getActivityManager(Context context) {
		if (activityManager == null) {
			activityManager = new ActivityManager1(context);
		}
		return activityManager;
	}

	private ActivityManager1(Context context) {
		this.context = context;
	}

	/**
	 * task map，用于记录activity栈，方便�?��程序（这里为了不影响系统回收activity，所以用软引用）
	 */
	private final HashMap<String, SoftReference<Activity>> taskMap = new HashMap<String, SoftReference<Activity>>();

	/**
	 * �?��用task map加入activity
	 */
	public final void putActivity(Activity atv) {
		taskMap.put(atv.toString(), new SoftReference<Activity>(atv));
	}

	/**
	 * �?��用task map加入activity
	 */
	public final void removeActivity(Activity atv) {
		taskMap.remove(atv.toString());
	}

	/**
	 * 清除应用的task栈，如果程序正常运行这会导致应用�?��到桌�?
	 */
	public final void exit() {
		for (Iterator<Entry<String, SoftReference<Activity>>> iterator = taskMap
				.entrySet().iterator(); iterator.hasNext();) {
			SoftReference<Activity> activityReference = iterator.next()
					.getValue();
			Activity activity = activityReference.get();
			if (activity != null) {
				activity.finish();
			}
		}
		taskMap.clear();
	}

	/**
	 * 清除应用的task栈，除了本页面
	 */
	public final void exit(Activity at) {
		for (Iterator<Entry<String, SoftReference<Activity>>> iterator = taskMap
				.entrySet().iterator(); iterator.hasNext();) {
			SoftReference<Activity> activityReference = iterator.next()
					.getValue();
			Activity activity = activityReference.get();
			if (activity != at) {
				activity.finish();
			}
		}
		taskMap.clear();
	}

}
