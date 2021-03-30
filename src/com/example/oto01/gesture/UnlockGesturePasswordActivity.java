package com.example.oto01.gesture;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oto01.BaseUnlockActivity;
import com.example.oto01.LoginActivity;
import com.example.oto01.R;
import com.example.oto01.SysApplication;
import com.example.oto01.db.GesturePasswordDBService;
import com.example.oto01.gesture.LockPatternView.Cell;
import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.model.FristPage;
import com.example.oto01.services.FristPageService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.ActivityManager;
import com.example.oto01.utils.NetConn;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 解锁手势密码界面
 * 
 * @类描述:
 * @项目名称: 社区e商户
 * @类名称: UnlockGesturePasswordActivity
 * @包名称: com.example.oto01.changeMessage
 * @author: cym
 * @date: 2016-11-16下午2:42:33
 */
@SuppressLint("ResourceAsColor")
public class UnlockGesturePasswordActivity extends BaseUnlockActivity {
	private LockPatternView mLockPatternView;// 九宫格
	// private int mFailedPatternAttemptsSinceLastTimeout = 0;
	// private CountDownTimer mCountdownTimer = null;
	// private Animation mShakeAnim;
	// 九宫格上面文字
	@ViewInject(R.id.gesturepwd_unlock_text)
	private TextView mHeaderText;
	// 基本布局
	@ViewInject(R.id.rootView)
	private LinearLayout rootView;
	// 忘记密码文字
	@ViewInject(R.id.forget_password)
	private TextView forgetPassword;
	// 修改密码文字
	@ViewInject(R.id.change_password)
	private TextView changePassword;
	// 切换用户、清除手势锁
	@ViewInject(R.id.changeUser)
	private TextView changeUser;
	// 清除手势动画
	private Animation mShakeAnim;
	// 首页信息的显示
	private FristPage obj = new FristPage();
	// 退出时间
	private long exitTime;
	// application类的定义
	private SysApplication app;
	// // 进行保存的手势密码的数据库
	private GesturePasswordDBService dao;
	// 九宫格保存的文字
	private String gustureMode;
	// 电话
	private TextView tv_phone;
	// 商户头像
	private ImageView iv_head;
	// 退出登录
	private static final int EXIT_LOGIN = 1;
	// 获得图片
	protected static final int GET_SHOP_TEXT_INFO = 2;
	// 解锁标识
	public static String unLockString = "";
	// 九宫格列表
	private List<Cell> cells = new ArrayList<LockPatternView.Cell>();

	private Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case EXIT_LOGIN:
				// 退出登录
				Intent intent = new Intent(UnlockGesturePasswordActivity.this,
						LoginActivity.class);
				intent.putExtra("exit", "unlock");
				startActivity(intent);
				finish();
				break;
			case GET_SHOP_TEXT_INFO:
				// 得到店铺图片
				obj = (FristPage) msg.obj;
				if (obj != null) {
					String phoString = phone.replaceAll(
							"(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					tv_phone.setText(phoString);
					String imgPath = obj.getPicpath();
					if (null != imgPath && imgPath.contains(";")) {
						String[] splitImage = imgPath.split(";");
						ImageLoadTool.disPlay(splitImage[0], iv_head,
								R.drawable.client_head);
					} else {
						ImageLoadTool.disPlay(imgPath, iv_head,
								R.drawable.client_head);
					}
				}
				break;

			default:

				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_unlock);
		// 初始化IOC注解（完全注解）,不需要再写findViewById的代码，其中R.id.txt_test是控件ID
		ViewUtils.inject(this);
		mLockPatternView = (LockPatternView) findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		iv_head = (ImageView) findViewById(R.id.iv_head);

		mLockPatternView.setTactileFeedbackEnabled(false); // 去掉手势密码的震动效果
		// mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		dao = new GesturePasswordDBService(this);
		// usrNameGu = AllUserName.getAllUsrName(this);
		gustureMode = dao.getAllSearchTrace(getApplicationContext(), shopsid
				+ "");
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		changeUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 清除 手势文件
				changeUser.setFocusable(false);
				changeUser.setClickable(false);
				try {
					app.getLockPatternUtils().clearLock();
				} catch (Exception e) {
					// TODO: handle exception
				}
				// 退出程序
				LogOut();
			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getShopInfo();
	}

	private void getShopInfo() {
		if (!NetConn.checkNetwork(UnlockGesturePasswordActivity.this)) {
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					FristPageService fristPageService = new FristPageService(
							UnlockGesturePasswordActivity.this);
					FristPage fristPage = fristPageService
							.getFristPage(shopsid);
					Message message = handler.obtainMessage(GET_SHOP_TEXT_INFO);
					message.obj = fristPage;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// // 从未创建过手势时，开始创建---没有记住密码
	// // if (!app.getLockPatternUtils().savedPatternExists()) {
	// // toLoginActivity();
	// // }
	// }
	/**
	 * 
	 * TODO<退出程序>
	 * 
	 * @throw
	 * @return void
	 */
	private void LogOut() {

		LoginManager lm = LoginManager
				.getInstance(UnlockGesturePasswordActivity.this);
		lm.delLogin();

		if (!NetConn.checkNetwork(UnlockGesturePasswordActivity.this)) {
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							UnlockGesturePasswordActivity.this);
					boolean flag = service.exitLogin(shopsid);
					Message message = handler.obtainMessage(EXIT_LOGIN);
					message.arg1 = flag == true ? 0 : 1;
					handler.sendMessage(message);
				}
			}).start();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if (mCountdownTimer != null)
		// mCountdownTimer.cancel();
	}

	/**
	 * 清除手势密码
	 */
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	/**
	 * 
	 * TODO<清除九宫格数据>
	 * 
	 * @throw
	 * @return void
	 */
	private void postClearPatternRunnable() {
		// 清空九宫格中画的线
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
		// 间隔两秒
		mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
	}

	/**
	 * 
	 * TODO<判断两次绘制是否一致>
	 * 
	 * @throw
	 * @return boolean
	 */
	private boolean isPatten(List<Cell> pattern, List<Cell> cells) {

		if (pattern.size() != cells.size()) {
			return false;
		}
		for (int i = 0; i < pattern.size(); i++) {
			if (pattern.get(i).equals(cells.get(i))) {
				if (i == (pattern.size() - 1)) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 新进行绘制的进行监听
	 */
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}

		public void onPatternDetected(List<Cell> pattern) {
			System.out.println(unLockString.toString());
			cells = LockPatternUtils.stringToPattern(gustureMode);
			if (pattern == null)
				return;
			if (isPatten(pattern, cells)) {
				// return;
				// }
				// if (app.getLockPatternUtils().checkPattern(pattern)) {// 解锁成功
				// cells = LockPatternUtils.stringToPattern(patternString);
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);

				// 解锁成功返回需要用户信息的页面----
				UnlockGesturePasswordActivity.this.finish();
				unLockString = "lock";
			} else {// 解锁失败
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);

				mHeaderText.setText("密码错误，请重试！");
				mHeaderText.setTextColor(Color.parseColor("#ff0000"));
				mHeaderText.startAnimation(mShakeAnim);
				postClearPatternRunnable();
				// if (pattern.size() >=
				// LockPatternUtils.MIN_PATTERN_REGISTER_FAIL)
				// {
				//
				// // mFailedPatternAttemptsSinceLastTimeout++;
				// int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
				// - mFailedPatternAttemptsSinceLastTimeout;
				// if (retry > 0) {
				// // // changeUser.setVisibility(View.VISIBLE);
				// // if (retry == 0)
				// // Toast.makeText(UnlockGesturePasswordActivity.this,
				// // "您已3次输错密码，再试1次到登录界面", 0).show();
				// // mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
				// // mHeadTextView.setTextColor(Color.RED);
				// // // mHeadTextView.startAnimation(mShakeAnim);
				// } else {
				// // 打开新的Activity
				// // 清除 手势文件
				// // AppClass.isOut = true;
				// // IsOut.setIsOut(UnlockGesturePasswordActivity.this,
				// // true);
				// // app.getLockPatternUtils().clearLock();
				// // toLoginActivity();
				// }
				// } else {
				// Toast.makeText(UnlockGesturePasswordActivity.this,
				// "输入长度不够，请重试", 0).show();
				// }
				// mLockPatternView.clearPattern();
			}
		}

		@Override
		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		private void patternInProgress() {
			mHeaderText.setText(R.string.lockpattern_recording_inprogress);
			mHeaderText.setTextColor(Color.parseColor("#333333"));
		}

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {

		}

	};

	// Runnable attemptLockout = new Runnable() {
	//
	// @Override
	// public void run() {
	// mLockPatternView.clearPattern();
	// mLockPatternView.setEnabled(false);
	// mCountdownTimer = new CountDownTimer(
	// LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {
	//
	// @Override
	// public void onTick(long millisUntilFinished) {
	// int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
	// if (secondsRemaining > 0) {
	// mHeadTextView.setText(secondsRemaining + " 秒后重试");
	// } else {
	// mHeadTextView
	// .setText(UnlockGesturePasswordActivity.this
	// .getString(R.string.gesture_drawPwd));
	// mHeadTextView.setTextColor(Color.WHITE);
	// }
	//
	// }
	//
	// @Override
	// public void onFinish() {
	// mLockPatternView.setEnabled(true);
	// mFailedPatternAttemptsSinceLastTimeout = 0;
	// }
	// }.start();
	// }
	// };

	/**
	 * 登录成功
	 */
	private void loginSuccessToMainAcrtivity() {
		finish();
		// exit();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				ActivityManager.getActivityManager(
						UnlockGesturePasswordActivity.this).exit();
				this.finish();
				// 重写物理返回键
				// if ((System.currentTimeMillis() - exitTime) > 2000) {
				// // Toast.makeText(getApplicationContext(), "再按一次退出应用!",
				// // Toast.LENGTH_SHORT).show();
				// exitTime = System.currentTimeMillis();
				// } else {
				// android.os.Process.killProcess(android.os.Process.myPid());
				// System.exit(0);
				// // ActivityManager.getActivityManager(
				// // UnlockGesturePasswordActivity.this).exit();
				// }

			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

}
