package com.example.oto01;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.oto01.db.GesturePasswordDBService;
import com.example.oto01.gesture.UnlockGesturePasswordActivity;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.ActivityManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类
 * 
 * @author Neusoft
 * 
 */
public class BaseActivity extends Activity {

	protected static BaseActivity mBaseActivity;
	private ActivityManager manager = ActivityManager.getActivityManager(this);
	protected int shopsid;// 店铺id

	public static BaseActivity getActivity() {
		return mBaseActivity;
	}

	protected boolean mHandleBackKey;
	private ProgressDialog mWaitingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mBaseActivity = this;
		mHandleBackKey = false;
		LoginManager lm = LoginManager.getInstance(BaseActivity.this);
		shopsid = lm.getLoginId();
		// NetUtil.isWifiConnected = NetUtil.isWiFiActive( this );

	}

	boolean isLoad = true;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		final String SYSTEM_DIALOG_REASON_KEY = "reason";
		final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
					String reason = intent
							.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
					if (reason != null) {
						if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
							// 短按home键
							// LogInfo.log("wwn", "短按home键");
							isLoad = false;
							UnlockGesturePasswordActivity.unLockString = "";
							LoginActivity.loginString = "";
							unregisterReceiver(receiver);
							System.out.println("短按home键");
							// initFinger();
						} else if (reason
								.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
							// 长按home键
							// LogInfo.log("wwn", "长按home键");
							// initFinger();
							// isLoad = false;
							// unregisterReceiver(receiver);
						}
					}
				}
				if (action.equals(Intent.ACTION_SCREEN_ON)) {// 开屏
					// LogInfo.log("wwn", "长按home键");
					System.out.println("开屏");
				} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {// 锁屏
					// initFinger();
					System.out.println("锁屏");
					isLoad = false;
					UnlockGesturePasswordActivity.unLockString = "";
					LoginActivity.loginString = "";
					unregisterReceiver(receiver);
				} else {// 解锁
					// isLoad = false;
					System.out.println("解锁");
					// initFinger();
				}

			}
		}
	};
	/**
	 * 退出应用
	 * 
	 * @KeyEvent event  返回事件
	 */
	private long exitTime;

	/**
	 * 
	 * TODO<判断是否有手势密码>
	 * 
	 * @throw
	 * @return void
	 */
	private void initFinger() {
		try {
			if (LoginActivity.loginString.equals("login")) {
				// 正常登录到首页，不进行手势密码的弹出
				// 
			} else {
				// if (getIntent().getStringExtra("login").equals("")) {
				// // 登录进来的解锁不需要手势密码
				// } else {
				GesturePasswordDBService dao = new GesturePasswordDBService(
						this);
				String nullString = dao.getAllSearchTrace(
						getApplicationContext(), shopsid + "");
				if (nullString.equals("")) {
					// 关闭手势密码走的流程
				} else if (UnlockGesturePasswordActivity.unLockString
						.equals("lock")) {

				} else {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							UnlockGesturePasswordActivity.class);
					startActivity(intent);
				}
				// }
			}

		} catch (Exception e) {
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("BaseActivity.onDestroy()");
		manager.removeActivity(this);
	}

	/**
	 * 退出
	 */
	public void exit() {
		manager.exit();
	}

	public void onResume() {
		super.onResume();
		System.out.println("BaseActivity.onResume()");
		MobclickAgent.onResume(this);
		manager.putActivity(this);
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);// home键
		mFilter.addAction(Intent.ACTION_SCREEN_ON); // 开屏
		mFilter.addAction(Intent.ACTION_SCREEN_OFF);// 锁屏
		mFilter.addAction(Intent.ACTION_USER_PRESENT);// 解锁

		registerReceiver(receiver, mFilter);

		if (isLoad) {

		} else {
			initFinger();
			isLoad = true;
		}

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		System.out.println("BaseActivity.onPause()");
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	//
	// StatService.onResume( this );
	// }
	//
	// @Override
	// protected void onPause() {
	// super.onPause();
	//
	// StatService.onPause( this );
	// }

	/**
	 * 对点击返回键进行处理
	 */
	// @Override
	// public boolean onKeyDown( int keyCode, KeyEvent event ) {
	// if ( keyCode == KeyEvent.KEYCODE_BACK ) {
	// if ( mHandleBackKey ) {
	// exitApp();
	// return false;
	// }
	// else {
	// return super.onKeyDown( keyCode, event );
	// }
	// }
	// else {
	// return super.onKeyDown( keyCode, event );
	// }
	// }

	// protected void startActivity( Class<?> aTargetClass, Bundle aBundle ) {
	// Intent i = new Intent( this, aTargetClass );
	// if ( aBundle != null ) {
	// i.putExtras( aBundle );
	// }
	// startActivity( i );
	// }

	/**
	 * 显示对话框
	 * 
	 * @param aMessage
	 */
	protected void showWaitingDialog(String aMessage) {
		if (isFinishing()) {
			return;
		}

		if (mWaitingDialog == null) {
			mWaitingDialog = new ProgressDialog(this);
			mWaitingDialog.setTitle(null);
			mWaitingDialog.setCancelable(false);
			mWaitingDialog.setMessage(aMessage);
			mWaitingDialog.show();
		}

	}

	protected void showWaitingDialog(int resId) {
		showWaitingDialog(getString(resId));
	}

	/**
	 * 取消对话框
	 */
	protected void dismissWaitingDialog() {
		try {
			if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
				mWaitingDialog.dismiss();
				mWaitingDialog = null;
			}
		} catch (IllegalArgumentException e) {
		}
	}

	public void showToast(String aMessage) {
		Toast.makeText(this, aMessage, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}

	// private void exitApp(){
	// OnClickListener cancelHandler = new OnClickListener() {
	// @Override
	// public void onClick( DialogInterface dialog, int which ) {
	// // no implementation
	// }
	// } ;
	//
	// OnClickListener okHandler = new OnClickListener() {
	// @Override
	// public void onClick( DialogInterface dialog, int which ) {
	// finish();
	// }
	// } ;
	//
	// AlertDialog dialog = new AlertDialog.Builder( this )
	// .setPositiveButton( R.string.dlg_ok, okHandler )
	// .setNeutralButton( R.string.dlg_cancel, cancelHandler )
	// .setTitle( R.string.app_name )
	// .setMessage( R.string.app_exit )
	// .create();
	// dialog.setCancelable( true );
	// dialog.show();
	// }

	/**
	 * 隐藏键盘
	 */
	// protected void hideInputMethod(){
	// ( ( InputMethodManager ) getSystemService( INPUT_METHOD_SERVICE ) )
	// .hideSoftInputFromWindow( getCurrentFocus().getWindowToken(),
	// InputMethodManager.HIDE_NOT_ALWAYS );
	// }

	// protected boolean checkBadResponse( Response aResponse ) {
	// boolean ret = false;
	// if ( aResponse == null ) {
	// showToast( R.string.networkerr_message );
	// ret = true;
	// }
	// else {
	// int errorCode = aResponse.mErrNo;
	// if ( errorCode != ServerError.NO_ERROR ) {
	// showToast( aResponse.mErrMsgCn );
	// ret = true;
	// if ( errorCode == ServerError.ERR_NOT_LOGIN ) {
	//
	// }
	// }
	// }
	//
	// return ret;
	// }
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			// if (isShouldHideInput(v, ev)) {
			// hideSoftInput(v.getWindowToken());
			// }
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	// private boolean isShouldHideInput(View v, MotionEvent event) {
	// if (v != null && (v instanceof EditText)) {
	// int[] l = { 0, 0 };
	// v.getLocationInWindow(l);
	// int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
	// + v.getWidth();
	// if (event.getX() > left && event.getX() < right
	// && event.getY() > top && event.getY() < bottom) {
	// // 点击EditText的事件，忽略它。
	// return false;
	// } else {
	// return true;
	// }
	// }
	// // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
	// return false;
	// }

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void back_onClick(View view) {
		finish();
	}
}
