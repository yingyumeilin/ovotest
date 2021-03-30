package com.example.oto01;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.example.oto01.model.Constant;
import com.example.oto01.model.DeviceInfoResponse;
import com.example.oto01.services.LoginManager;
import com.example.oto01.task.CheckDownAsyncTask;
import com.example.oto01.task.CheckDownAsyncTask.OnCheckDownListener;
import com.example.oto01.utils.SystemUtil;
import com.example.oto01.utils.TelephoneUtil;

/**
 * 启动页面
 * 
 * @author lqq
 * 
 */
public class LoadingActivity extends BaseActivity implements
		OnCheckDownListener {
	private Dialog proDialog;
	private int mVersionCode;
	private String mVersionName;
	private TextView versionTextView;
	private LoginManager lm;
	private ApplicationInfo appInfo;
	private CheckDownAsyncTask checkDownTask;
	/* public static boolean isHTC = true; */
	private AlertDialog myHintVersionDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		// MobclickAgent.setDebugMode(true);
		// showDialog();

		String webUrl = Constant.webHost;
		String text = null;
		if (webUrl.contains("dev")) { // 开发版
			text = "Hello，everyone，i am 开发版！";
		} else if (webUrl.contains("moni")) { // 模拟版
			text = "Hello，everyone，i am 模拟版！";
		} else if (webUrl.contains("efuwu.me")) { // 线上版

		} else if (webUrl.contains("test")) {
			text = "Hello，everyone，i am 测试版！";
		}
		if (text != null) {
			versionDialog(text);
		}
		if (TelephoneUtil.getState(this) == 0) {
			// 联网---保存此时的code
			if (checkDownTask != null) {
				checkDownTask.cancel(true);
				checkDownTask = null;
			}

			checkDownTask = new CheckDownAsyncTask(this);
			checkDownTask.setOnCheckDownListener(this);
			checkDownTask.execute();
		}

		mVersionCode = SystemUtil.getVersionCode(this);
		mVersionName = SystemUtil.getVersionName(this);
		versionTextView = (TextView) findViewById(R.id.version);
		versionTextView.setText(mVersionName);
		lm = LoginManager.getInstance(getApplicationContext());

		System.out.println("-------loading------>" + lm.getLoginId());
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				Intent intent = new Intent();
				System.out.println("-----tag------>" + lm.getTag());
				if (lm.getTag() != -1) {
					if (lm.isLogined()) {
						intent.setClass(getApplicationContext(),
								MainActivity.class);
						intent.putExtra("login", "1");
					} else {
						intent.setClass(getApplicationContext(),
								LoginActivity.class);
					}
					// intent.setClass(getApplicationContext(),
					// LoadingTwoActivity.class);
				} else {
					intent.setClass(getApplicationContext(),
							NavigationActivity.class);
				}
				startActivity(intent);
				// proDialog.dismiss();
				// 设置切换动画，从右边进入，左边退出
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
				finish();
			}
		}, 1000);

	}

	/**
	 * 提示--对话框
	 */
	protected void versionDialog(String text) {
		myHintVersionDialog = new AlertDialog.Builder(this).create();
		myHintVersionDialog.show();
		myHintVersionDialog.getWindow().setContentView(
				R.layout.hint_version_dialog);
		TextView title = (TextView) myHintVersionDialog.getWindow()
				.findViewById(R.id.hint_dialog_title);
		try {
			title.setText(text);
		} catch (Exception e) {
			// TODO: handle exception
		}

		myHintVersionDialog.setCancelable(true);
	}

	/**
	 * 展示Dialog
	 */
	private void showDialog() {
		if (proDialog == null) {
			proDialog = new Dialog(LoadingActivity.this,
					R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
		}
		proDialog.show();
		/*
		 * if (!proDialog.isShowing()) { proDialog.show(); }
		 */
	}

	@Override
	public void onCheckDown(DeviceInfoResponse response) {
		// TODO Auto-generated method stub
		if (checkDownTask != null) {
			checkDownTask.cancel(true);
			checkDownTask = null;
		}

		if (response != null) {
			if ("0".equals(response.getRes())) {
				/** 保存本地的版本号 */
				TelephoneUtil.saveState(this, 1);
			} else if ("1".equals(response.getRes())) {

			}
		}
	}
}
