package com.example.oto01;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oto01.db.GesturePasswordDBService;
import com.example.oto01.gesture.CreateGesturePasswordActivity;
import com.example.oto01.model.Login;
import com.example.oto01.model.VersionInfo;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.LoginService;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.ActivityManager;
import com.example.oto01.utils.ClientsUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.SystemUtil;
import com.example.oto01.utils.ToastUtil;
import com.igexin.slavesdk.MessageManager;

/**
 * 登录界面
 * 
 * @author lqq
 * 
 */
public class LoginActivity extends Activity {
	private EditText usernameEditText;
	private EditText passwordEditText;
	private TextView forgetTextView;
	private ProgressDialog proDialog;
	protected static final int APP_UPDATE = 1;
	private int mVersionCode;
	private AlertDialog myDialog = null;
	private long exitTime;
	private String username = "";// 本页面保存的用户名
	private SysApplication app;// application类的定义
	private GesturePasswordDBService dao;// 数据库的定义
	private int shopsid;
	public static String loginString;
	public static int secondtime = 0;
	private Handler handler = new Handler() {

		@SuppressWarnings("static-access")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case APP_UPDATE:
				// FIXME 提示更新
				VersionInfo info = (VersionInfo) msg.obj;
				if (info != null) {
					regularUpdates(info.getVersionname(), info.getUpdateinfo(),
							info.getFilepath(), info.getIs_update());
				}
				break;

			default:
				proDialog.dismiss();

				System.out.println("-----arg1---->" + msg.arg1);
				if (msg.arg1 == 1) {
					ActivityManager.getActivityManager(LoginActivity.this)
							.exit(LoginActivity.this);
					ToastUtil.show(LoginActivity.this,
							"您的商铺已被禁用，如有疑问请拨打电话：4000789000");
					// ActivityManager.getActivityManager(LoginActivity.this)
					// .exit();
				}
				if ((msg.arg1 == 0)) {

					LoginManager lm = LoginManager
							.getInstance(LoginActivity.this);
					shopsid = lm.getLoginId();
					System.out.println("----token--->" + lm.getToken());
					if (lm.getToken() == null) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								LoginManager lm = LoginManager
										.getInstance(LoginActivity.this);
								LoginService loginService = new LoginService(
										LoginActivity.this);
								loginService.saveRongInfo(lm.getLoginId());
							}
						}).start();
					}
					MessageManager.getInstance().initialize(
							LoginActivity.this.getApplicationContext());
					Intent intent = LoginActivity.this.getIntent();

					try {
						Login login = lm.getLoginInstance();
						if (login != null && login.getUserName() != null) {
							if (login.getUserName().equals(username)) {
								// 当关闭手势密码以及 修改手势密码 再次登录到之前的账号的时候

								if (intent.getStringExtra("exit")
										.equals("exit")) {
									// 关闭手势密码
									// 把手势密码 删除
									dao.removeOne(LoginActivity.this, shopsid
											+ "");
									if (ClientsUtil
											.getFirstLoginSettingKuang(getApplicationContext())) {

									} else {
										ClientsUtil.setFirstSecondSettingKuang(
												getApplicationContext(), true);
										secondtime++;
									}
								} else if (intent.getStringExtra("exit")
										.equals("change")) {
									if (ClientsUtil
											.getFirstLoginSettingKuang(getApplicationContext())) {

									} else {
										ClientsUtil.setFirstSecondSettingKuang(
												getApplicationContext(), true);
										secondtime++;
									}
									// 修改密码
									loginString = "login";
									intent = new Intent(LoginActivity.this,
											CreateGesturePasswordActivity.class);
									intent.putExtra("exit", "change");
									startActivity(intent);

								} else if (intent.getStringExtra("exit")
										.equals("unlock")) {
									// 解锁页面的用账号登录的设置
									loginString = "login";
									if (ClientsUtil
											.getFirstLoginSettingKuang(getApplicationContext())) {

									} else {
										ClientsUtil.setFirstSecondSettingKuang(
												getApplicationContext(), true);
										secondtime++;
									}
								}
							} else {
								// 正常的情况下 或者是 关闭或者修改手势密码之后 登录另一个账号的时候
								// 第一次登录设置成true
								// 当最一开始装这个app的时候，走的这个方法
								if (ClientsUtil
										.getFirstLoginSettingKuang(getApplicationContext())) {

								} else {
									ClientsUtil.setFirstSecondSettingKuang(
											getApplicationContext(), true);
									secondtime++;
								}

								ActivityManager.getActivityManager(
										LoginActivity.this).exit();
								ToastUtil.show(LoginActivity.this,
										"登录成功，进入主界面！");
								intent = new Intent(LoginActivity.this,
										MainActivity.class);
								intent.putExtra("login", "");
								startActivity(intent);
								overridePendingTransition(R.anim.in_from_right,
										R.anim.out_to_left);

							}
						}

					} catch (Exception e) {
						// 退出再登录的时候，没有手势密码的时候 走的这个方法
						if (ClientsUtil
								.getFirstLoginSettingKuang(getApplicationContext())) {

						} else {
							ClientsUtil.setFirstSecondSettingKuang(
									getApplicationContext(), true);
							secondtime++;
						}
						ToastUtil.show(LoginActivity.this, "登录成功，进入主界面！");
						intent = new Intent(LoginActivity.this,
								MainActivity.class);
						// sendBroadcast(new
						// Intent().setAction("connect_rong"));
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_right,
								R.anim.out_to_left);
					}

					finish();
				} else if (msg.arg1 == 2) {
					ToastUtil.show(LoginActivity.this, "登录失败，账号不存在或密码错误");
					LoginManager lm = LoginManager
							.getInstance(LoginActivity.this);
					Intent intent = LoginActivity.this.getIntent();
					Login login = lm.getLoginInstance();
					try {
						if (login.getUserName().equals(username)) {
							// 当关闭手势密码以及 修改手势密码 再次登录到之前的账号的时候
							if (intent.getStringExtra("exit").equals("exit")) {
								// 关闭手势密码
								// 把手势密码 删除
								// dao.removeOne(LoginActivity.this, shopsid +
								// "");
							} else if (intent.getStringExtra("exit").equals(
									"change")) {
								// 修改密码
							} else if (intent.getStringExtra("exit").equals(
									"unlock")) {
								// 解锁页面的用账号登录的设置
							} else {
								ActivityManager.getActivityManager(
										LoginActivity.this).exit(
										LoginActivity.this);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else if (msg.arg1 == -1) {
					ToastUtil.show(LoginActivity.this, "网络连接不给力！");
					LoginManager lm = LoginManager
							.getInstance(LoginActivity.this);
					Intent intent = LoginActivity.this.getIntent();
					Login login = lm.getLoginInstance();

					try {
						if (login.getUserName().equals(username)) {
							// 当关闭手势密码以及 修改手势密码 再次登录到之前的账号的时候
							if (intent.getStringExtra("exit").equals("exit")) {
								// 关闭手势密码
								// 把手势密码 删除
								// dao.removeOne(LoginActivity.this, shopsid +
								// "");
							} else if (intent.getStringExtra("exit").equals(
									"change")) {
								// 修改密码
							} else if (intent.getStringExtra("exit").equals(
									"unlock")) {
								// 解锁页面的用账号登录的设置
							} else {
								ActivityManager.getActivityManager(
										LoginActivity.this).exit(
										LoginActivity.this);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SysApplication.getInstance().addActivity(this);
		// overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		initView();
		// 初始化application
		app = (SysApplication) getApplication();
		// 初始化数据库
		dao = new GesturePasswordDBService(this);

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		myDialog = new AlertDialog.Builder(this).create();
		myDialog.setCancelable(false);
		myDialog.setCanceledOnTouchOutside(false);
		proDialog = new ProgressDialog(this);
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		forgetTextView = (TextView) findViewById(R.id.forgetPass);
		// forgetTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		LoginManager lm = LoginManager.getInstance(LoginActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getUserName() != null) {
			username = login.getUserName();
			usernameEditText.setText(login.getUserName());
		}
		checkVersionInfo();
	}

	/**
	 * 新开店铺
	 * 
	 * @param view
	 */
	public void open_shop(View view) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RegisterStepOneActivity.class);
		intent.putExtra("type", "login");
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	/**
	 * 忘记密码
	 * 
	 * @param view
	 */
	public void forgetPass(View view) {
		Intent intent = new Intent(LoginActivity.this,
				FindPasswordActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!NetConn.checkNetwork(LoginActivity.this)) {

		} else {
			final String username = usernameEditText.getText().toString()
					.trim();
			final String password = passwordEditText.getText().toString()
					.trim();
			if (username.length() == 0) {
				ToastUtil.show(LoginActivity.this, "请输入手机号");
				return;
			}
			if (password.length() == 0) {
				ToastUtil.show(LoginActivity.this, "请输入密码");
				return;
			}
			if (username.length() != 11) {
				ToastUtil.show(LoginActivity.this, "手机号长度只能为11位");
				return;
			}
			if (password.length() > 16 || password.length() < 6) {
				ToastUtil.show(LoginActivity.this, "密码长度只能在6-16位之间");
				return;
			}
			proDialog.setMessage("正在登陆......");
			proDialog.setCancelable(false);
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					LoginManager lm = LoginManager
							.getInstance(LoginActivity.this);
					lm.delLogin();
					LoginService loginService = new LoginService(
							LoginActivity.this);
					int res = loginService.login(username, password, true);
					Message message = handler.obtainMessage();
					message.arg1 = res;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	private void checkVersionInfo() {

		if (!NetConn.checkNetwork(LoginActivity.this)) {
		} else {
			mVersionCode = SystemUtil.getVersionCode(this);
			new Thread(new Runnable() {
				@Override
				public void run() {
					SettingService service = new SettingService(
							LoginActivity.this);
					VersionInfo info = service.getUpdateInfo(mVersionCode);
					Message message = handler.obtainMessage(APP_UPDATE);
					message.obj = info;
					handler.sendMessage(message);
				}
			}).start();
		}

	}

	/**
	 * 检查更新
	 * 
	 * @param vName
	 * @param info
	 * @param path
	 * @param isUpdate
	 */
	private void regularUpdates(String vName, String info, final String path,
			String isUpdate) {

		myDialog.show();
		myDialog.getWindow().setContentView(R.layout.regular_updates_dialog);
		TextView versionTV = (TextView) myDialog.getWindow().findViewById(
				R.id.versiontextView);
		TextView infoTV = (TextView) myDialog.getWindow().findViewById(
				R.id.updateinfotextview);
		Button but = (Button) myDialog.getWindow().findViewById(
				R.id.cancelbutton);
		System.out.println("-------isUpdate-------->"
				+ Integer.parseInt(isUpdate));
		if (Integer.parseInt(isUpdate) == 0) {// 不强制更新
			but.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		} else {
			but.setVisibility(View.GONE);

			myDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if ((System.currentTimeMillis() - exitTime) > 2000) {
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.exit_app_prompt),
									Toast.LENGTH_SHORT).show();
							exitTime = System.currentTimeMillis();
						} else {
							android.os.Process.killProcess(android.os.Process
									.myPid());
							System.exit(0);
						}

					}
					return false;
				}
			});
		}

		versionTV.setText(vName);
		infoTV.setText(info);
		myDialog.getWindow().findViewById(R.id.downloadbutton)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (android.os.Environment.getExternalStorageState()
								.equals(android.os.Environment.MEDIA_MOUNTED)) {
							Intent it = new Intent(LoginActivity.this,
									NotificationUpdateActivity.class);
							it.putExtra("apkpath", path);
							SettingsActivity.isUpdate = true;
							startActivity(it);
						} else {
							ToastUtil.show(LoginActivity.this, "请先挂载SD卡");
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

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				// 重写物理返回键
				ActivityManager.getActivityManager(LoginActivity.this).exit();
				this.finish();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

}
