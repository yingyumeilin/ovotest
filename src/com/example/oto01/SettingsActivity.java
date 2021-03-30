package com.example.oto01;

import io.rong.imkit.RongIM;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.db.GesturePasswordDBService;
import com.example.oto01.gesture.CreateGesturePasswordActivity;
import com.example.oto01.model.Login;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.model.VersionInfo;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.ActivityManager;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.SystemUtil;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.VersionUtil;

/**
 * 设置界面
 * 
 * @author lqq
 * 
 */
public class SettingsActivity extends BaseActivity {
	private int shopsid = 1;
	// 滚动条
	private ProgressDialog proDialog;
	private AlertDialog myDialog = null;
	// 退出登录
	private static final int EXIT_LOGIN = 1;
	// app更新
	private static final int APP_UPDATE = 2;
	// 店铺信息
	private static final int SHOP_CHANGE = 3;
	// 店铺资料申请变更
	protected static final int GET_SHOP_TEXT_INFO = 6;
	// 默认更新为false
	public static boolean isUpdate = false;
	// 版本号
	private int mVersionCode;
	// 版本名称
	private String versionName;
	// 订单声音的sp
	private SharedPreferences sp;
	private SysApplication app;// application类的定义
	private GesturePasswordDBService dao;// 数据库的定义
	// 订单声音标识
	private boolean flag;
	// 订单提示是否有声音提示
	private ImageView voiceImageView;
	// 版本号的文本
	private TextView tvVersion;
	// 店铺资料变更状态
	private TextView tv_status;
	// 版本更新的图片
	private ImageView iv_new;
	// 修改手势密码的布局
	private RelativeLayout rl_changefinger;
	// 手势密码几个字
	private TextView tv_finger;
	private boolean update = false;
	// 关闭手势密码
	private boolean exit_gesture = false;
	// 修改手势密码
	private boolean change_gesture = false;
	// 手势密码的状态变更
	private boolean update_gesture = false;
	private SQLiteDatabase db;// 手势密码的数据库

	private Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case EXIT_LOGIN:
				// 退出登录
				if (exit_gesture) {
					// 关闭手势密码，修改手势密码要走的逻辑
					exit_gesture = false;
					Intent intent = new Intent(SettingsActivity.this,
							LoginActivity.class);
					intent.putExtra("exit", "exit");
					startActivity(intent);
				} else if (change_gesture) {
					change_gesture = false;
					Intent intent = new Intent(SettingsActivity.this,
							LoginActivity.class);
					intent.putExtra("exit", "change");
					startActivity(intent);
				} else {
					ActivityManager.getActivityManager(SettingsActivity.this)
							.exit();
					Intent intent = new Intent(SettingsActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}

				break;
			case APP_UPDATE:
				proDialog.dismiss();
				// 版本更新
				VersionInfo info = (VersionInfo) msg.obj;
				if (info != null) {
					regularUpdates(info.getVersionname(), info.getUpdateinfo(),
							info.getFilepath());
					iv_new.setVisibility(View.VISIBLE);
				} else {
					iv_new.setVisibility(View.INVISIBLE);
					if (update) {
						ToastUtil.show(SettingsActivity.this, "已经是最新版本");
						update = false;
					}

				}
				break;
			case GET_SHOP_TEXT_INFO:
				proDialog.dismiss();
				// 店铺信息变更
				ShopInfo shop = (ShopInfo) msg.obj;
				if (shop != null) {
					if (shop.getState().equals("3")
							|| shop.getState().equals("4")
							|| shop.getState().equals("6")) {
						tv_status.setText("重新提交");
					} else {
						tv_status.setText("申请变更");
					}
				}

			}
		};
	};
	private Button login_out;
	private RelativeLayout rl_setfinger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		LoginManager lm = LoginManager.getInstance(SettingsActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		proDialog = new ProgressDialog(this);
		iv_new = (ImageView) findViewById(R.id.iv_new);
		tv_finger = (TextView) findViewById(R.id.tv_finger);
		voiceImageView = (ImageView) findViewById(R.id.voice);
		rl_changefinger = (RelativeLayout) findViewById(R.id.rl_changefinger);
		tv_status = (TextView) findViewById(R.id.tv_status);
		login_out = (Button) findViewById(R.id.login_out);
		rl_setfinger = (RelativeLayout) findViewById(R.id.rl_setfinger);
		sp = this.getSharedPreferences("voiceprompt", Context.MODE_PRIVATE);
		flag = sp.getBoolean("voiceflag", true);
		// 初始化application
		app = (SysApplication) getApplication();
		// 初始化数据库
		dao = new GesturePasswordDBService(this);
		if (flag) {// 开
			voiceImageView.setImageResource(R.drawable.open_icon);
		} else {
			voiceImageView.setImageResource(R.drawable.close_icon);
		}
		tvVersion = (TextView) findViewById(R.id.versionCode);
		versionName = VersionUtil.getVersionName(this);
		tvVersion.setText("V" + versionName);
		myDialog = new AlertDialog.Builder(this).create();
		myDialog.setCancelable(false);
		myDialog.setCanceledOnTouchOutside(false);
		getShopInfo();
	}

	/**
	 * 获取店铺信息变更的接口
	 */
	private void getShopInfo() {
		if (!NetConn.checkNetwork(SettingsActivity.this)) {
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							SettingsActivity.this);
					ShopInfo shopInfo = service.checkShopInfo2(shopsid);
					Message message = handler.obtainMessage(GET_SHOP_TEXT_INFO);
					message.obj = shopInfo;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 更新版本
	 * 
	 * @param vName
	 *            版本名称
	 * @param info
	 * @param path
	 *            路径
	 */
	private void regularUpdates(String vName, String info, final String path) {

		myDialog.show();
		myDialog.getWindow().setContentView(R.layout.regular_updates_dialog);
		TextView versionTV = (TextView) myDialog.getWindow().findViewById(
				R.id.versiontextView);
		TextView infoTV = (TextView) myDialog.getWindow().findViewById(
				R.id.updateinfotextview);
		Button but = (Button) myDialog.getWindow().findViewById(
				R.id.cancelbutton);
		versionTV.setText(vName);
		infoTV.setText(info);
		but.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		myDialog.getWindow().findViewById(R.id.downloadbutton)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (android.os.Environment.getExternalStorageState()
								.equals(android.os.Environment.MEDIA_MOUNTED)) {
							Intent it = new Intent(SettingsActivity.this,
									NotificationUpdateActivity.class);
							it.putExtra("apkpath", path);
							SettingsActivity.isUpdate = true;
							startActivity(it);
						} else {
							ToastUtil.show(SettingsActivity.this, "请先挂载SD卡");
						}
						myDialog.dismiss();
					}
				});
		but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
	}

	/**
	 * 检查更新
	 */
	private void checkVersionInfo() {

		// if (!NetConn.checkNetwork(SettingsActivity.this)) {
		// } else {
		if (update) {
			proDialog.setMessage("正在检测......");
			proDialog.show();
		}

		mVersionCode = SystemUtil.getVersionCode(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				SettingService service = new SettingService(
						SettingsActivity.this);
				VersionInfo info = service.getUpdateInfo(mVersionCode);
				Message message = handler.obtainMessage(APP_UPDATE);
				message.obj = info;
				handler.sendMessage(message);
			}
		}).start();
	}

	// }

	/**
	 * 退出账号
	 * 
	 * @param view
	 */
	public void exit(View view) {
		// FIXME 清空登陆信息
		login_out.setClickable(false);
		LogOut();

	}

	private void LogOut() {
		LoginManager lm = LoginManager.getInstance(SettingsActivity.this);
		lm.delLogin();
		if (RongIM.getInstance() != null)
			RongIM.getInstance().disconnect(false);
		// proDialog.setMessage("正在退出...");
		// proDialog.show();
		System.out.println("-----shopsid---->" + lm.getLoginId());
		// ToastUtil.show(SettingsActivity.this, "退出成功！");
		if (!NetConn.checkNetwork(SettingsActivity.this)) {
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							SettingsActivity.this);
					boolean flag = service.exitLogin(shopsid);
					Message message = handler.obtainMessage(EXIT_LOGIN);
					message.arg1 = flag == true ? 0 : 1;
					handler.sendMessage(message);
				}
			}).start();
		}

	}

	/**
	 * 我的等级
	 * 
	 * @param view
	 */
	public void my_level_onClick(View view) {
		// startActivity(SettingsActivity.this, );
	}

	/**
	 * 店铺信息
	 * 
	 * @param view
	 */
	public void shop_info_onClick(View view) {
		Intent intent = new Intent(SettingsActivity.this,
				ShopInfoActivity.class);
		startActivity(intent);
	}

	/**
	 * 社区e管家
	 * 
	 * @param view
	 */
	public void guanjia_onClick(View view) {
		Intent intent = new Intent(SettingsActivity.this,
				GuanJiaResActivity.class);
		startActivity(intent);
	}

	/**
	 * 
	 * TODO<手势密码的开启与关闭>
	 * 
	 * @throw
	 * @return void
	 */
	public void fingerprint_onClick(View view) {

		if (update_gesture) {
			tv_finger.setText("打开手势密码");
			rl_changefinger.setVisibility(View.GONE);
			update_gesture = true;
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					CreateGesturePasswordActivity.class);
			startActivity(intent);
		} else {
			rl_setfinger.setClickable(false);
			tv_finger.setText("关闭手势密码");
			rl_changefinger.setVisibility(View.VISIBLE);
			exit_gesture = true;
			update_gesture = false;
			LogOut();
		}

	}

	/**
	 * 语音播报
	 * 
	 * @param view
	 */
	public void yuyin_onClick(View view) {
		// startActivity(SettingsActivity.this, );
		sp = this.getSharedPreferences("voiceprompt", Context.MODE_PRIVATE);
		boolean flag = sp.getBoolean("voiceflag", true);
		if (flag) {
			Editor ed = sp.edit();
			ed.putBoolean("voiceflag", false);
			ed.commit();
			voiceImageView.setImageResource(R.drawable.close_icon);
			// dataMid.add(item(R.drawable.setting_zhanghao,
			// "订单语音提示","关",R.drawable.more_arrow));
		} else {
			Editor ed = sp.edit();
			ed.putBoolean("voiceflag", true);
			ed.commit();
			voiceImageView.setImageResource(R.drawable.open_icon);
			// dataMid.add(item(R.drawable.setting_zhanghao,
			// "订单语音提示","开",R.drawable.more_arrow));
		}
	}

	/**
	 * 检测更新
	 * 
	 * @param view
	 */
	public void update_onClick(View view) {
		// startActivity(SettingsActivity.this, );
		update = true;
		checkVersionInfo();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 意见反馈
	 * 
	 * @param view
	 */
	public void feed_back_onClick(View view) {
		Intent intent = new Intent(SettingsActivity.this,
				FeedbackActivity.class);
		startActivity(intent);
	}

	/**
	 * 关于我们
	 * 
	 * @param view
	 */
	public void about_onClick(View view) {
		Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
		startActivity(intent);
	}

	/**
	 * 开店资质
	 * 
	 * @param view
	 */
	public void shop_change_onClick(View view) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ShopChangeActivity.class);
		intent.putExtra("type", tv_status.getText().toString());
		startActivity(intent);
	}

	/**
	 * 
	 * TODO<修改手势密码>
	 * 
	 * @throw
	 * @return void
	 */
	public void change_finger(View view) {
		change_gesture = true;
		rl_changefinger.setClickable(false);
		LogOut();
	}

	@Override
	public void onResume() {
		super.onResume();
		checkVersionInfo();
		rl_setfinger.setClickable(true);
		rl_changefinger.setClickable(true);
		try {
			// System.out.println(dao.getGestureNumberDataListBean()
			// + "dao.getGestureNumberDataListBean()");
			String nullString = dao.getAllSearchTrace(getApplicationContext(),
					shopsid + "");
			System.out.println(nullString);
			if (nullString.equals("")) {
				// 该账号不存在手势密码
				tv_finger.setText("打开手势密码");
				rl_changefinger.setVisibility(View.GONE);
				update_gesture = true;
			} else {
				// 该账号的手势密码已存在
				rl_changefinger.setVisibility(View.VISIBLE);
				tv_finger.setText("关闭手势密码");
				update_gesture = false;
			}
		} catch (Exception e) {
			// 该账号不存在手势密码
			tv_finger.setText("打开手势密码");
			rl_changefinger.setVisibility(View.GONE);
			update_gesture = true;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (db != null) {
			db.close();
		}

	}

}