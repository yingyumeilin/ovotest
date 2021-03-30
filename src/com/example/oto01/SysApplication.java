package com.example.oto01;

import io.rong.imkit.RongIM;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.example.oto01.gesture.LockPatternUtils;
import com.example.oto01.model.Constant;
import com.example.oto01.utils.ImageUtil;
import com.tencent.bugly.crashreport.CrashReport;

public class SysApplication extends Application {

	private LockPatternUtils mLockPatternUtils;// 手势

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();
		// mRequestCache = new RequestCache();//初始化缓存
		// HttpsUtil.setRequestCache(mRequestCache);//给请求设置缓存
		mLockPatternUtils = new LockPatternUtils(this);
		Constant.InitValue(this);
		ImageUtil.initImageLoader(this);
		// 融云初始化。
		// RongIM.init(this, "25wehl3uw6jww", R.drawable.ic_launcher); //测试
		RongIM.init(this, "qd46yzrf4vu4f", R.drawable.ic_launcher); // 正式
		CrashReport.initCrashReport(this, String.valueOf(900014909), true);
	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}

	private List<Activity> mList = new LinkedList<Activity>();
	private static SysApplication instance;

	public SysApplication() {
	}

	public synchronized static SysApplication getInstance() {
		if (null == instance) {
			instance = new SysApplication();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void removeActivity() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

}
