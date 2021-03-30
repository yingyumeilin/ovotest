package com.example.oto01;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.LoginService;
import com.example.oto01.services.RegisterService;
import com.example.oto01.utils.MD5Utils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.TelephoneUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 修改密码界面
 * 
 * @author lqq
 * 
 */
public class ChangePasswordStepOneActivity extends BaseActivity implements
		LocationSource, AMapLocationListener {
	private EditText codeEditText;
	protected static final int PHONE_CODE_TIME = 1;
	protected static final int GET_PHONE_CODE = 2;
	protected static final int GET_TIME_SERVICE = 4;
	protected static final int NEXT = 3;
	private int shopsid;
	private Timer timer;
	private TextView timeTextView, phoneNumberTextView;
	private Dialog dialog;
	private String phoneNumber;
	private String timestamp;
	private MapView mapView;// 地图的view
	private AMap aMap;// 地图
	private OnLocationChangedListener mListener;// 经纬度改变的回调
	private LocationManagerProxy mAMapLocationManager;// Location服务的提供者
	public static Double geoLat;// 经度
	public static Double geoLng;// 纬度
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PHONE_CODE_TIME:
				if (msg.arg1 > 0) {
					timeTextView.setText(msg.arg1 + "秒后重试");
					timeTextView.setClickable(false);
				} else {
					timeTextView.setText("获取验证码");
					timeTextView.setClickable(true);
					// 结束Timer计时器
					timer.cancel();
				}
				break;
			case GET_PHONE_CODE:
				dialog.dismiss();
				dialog.dismiss();
				String res = (String) msg.obj;
				final JSONObject jObject;
				try {
					jObject = new JSONObject(res);

					if (jObject.optInt("res") == 0
							|| jObject.optInt("res") == 8) {
						ToastUtil.show(getApplicationContext(),
								jObject.getString("msg"));
						timer = new Timer();
						// 创建一个TimerTask
						// TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程
						TimerTask timerTask = new TimerTask() {
							// 倒数90秒
							int i = jObject.optInt("second");

							@Override
							public void run() {
								// 定义一个消息传过去
								Message msg = handler
										.obtainMessage(PHONE_CODE_TIME);
								msg.arg1 = i--;
								handler.sendMessage(msg);
							}
						};
						timer.schedule(timerTask, 1000, 1000);// 3秒后开始倒计时，倒计时间隔为1秒
					} else if (jObject.optInt("res") == 9) {
						// 这个手机号的操作次数超过上限，请明天再试或更换手机号！
						if (timer != null) {
							timer.cancel();
						}
						ToastUtil.show(getApplicationContext(),
								jObject.getString("msg"));
						timeTextView.setText("获取验证码");
						timeTextView
								.setBackgroundResource(R.drawable.huidianshape);
						timeTextView.setClickable(false);
					} else {
						ToastUtil.show(getApplicationContext(),
								jObject.getString("msg"));
						timeTextView.setText("获取验证码");
						timeTextView
								.setBackgroundResource(R.drawable.pandianshape);
						timeTextView.setClickable(true);
						if (timer != null) {
							timer.cancel();
						}
					}

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case NEXT:
				dialog.dismiss();
				if (msg.arg1 == 0) {
					Intent intent = new Intent(
							ChangePasswordStepOneActivity.this,
							ChangePasswordStepTwoActivity.class);
					startActivity(intent);
					finish();
				} else if (msg.arg1 == 1) {
					ToastUtil.show(ChangePasswordStepOneActivity.this,
							"验证码或手机号码不能为空");
				} else if (msg.arg1 == 2) {
					ToastUtil.show(ChangePasswordStepOneActivity.this,
							"该手机号码已被注册");
				} else if (msg.arg1 == 3) {
					ToastUtil.show(ChangePasswordStepOneActivity.this, "验证码错误");
				} else if (msg.arg1 == 4) {
					ToastUtil.show(ChangePasswordStepOneActivity.this, "验证码过期");
				}
				break;
			case GET_TIME_SERVICE:
				// 得到服务器时间戳
				String res_time = (String) msg.obj;
				try {
					JSONObject jo = new JSONObject(res_time);
					if (jo.getInt("res") == 0) {
						JSONObject jsonObject = jo.getJSONObject("data");
						timestamp = jsonObject.getString("timestamp");
						// 获取验证码的接口
						getCode();
					} else {
						ToastUtil.show(getApplicationContext(),
								jo.getString("msg"));
					}
				} catch (Exception e) {
				}

				break;

			default:
				if (msg.arg1 == 0) {
					ToastUtil.show(ChangePasswordStepOneActivity.this,
							"修改密码成功！");
					LoginManager lm = LoginManager
							.getInstance(ChangePasswordStepOneActivity.this);
					lm.delLogin();
					Intent intent = new Intent(
							ChangePasswordStepOneActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				} else if (msg.arg1 == 1) {
					ToastUtil.show(ChangePasswordStepOneActivity.this,
							"密码格式错误！");
				} else if (msg.arg1 == 2) {
					ToastUtil.show(ChangePasswordStepOneActivity.this,
							"两次密码不一致！");
				} else if (msg.arg1 == 3) {
					ToastUtil.show(ChangePasswordStepOneActivity.this,
							"修改密码失败！");
				} else if (msg.arg1 == -1) {
					ToastUtil.show(ChangePasswordStepOneActivity.this,
							"网络连接不给力！");
				}
				break;
			}

		};
	};

	private void getCode() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String random = String.valueOf((int) (Math.random() * 899999 + 100000));
				String signature = MD5Utils.getMD5Str("Strongunion_seller"
						+ timestamp + random);
				LoginService service = new LoginService(
						ChangePasswordStepOneActivity.this);

				String res = service.getPhoneCode(phoneNumber, "2", signature,
						timestamp, random, geoLat, geoLng, TelephoneUtil
								.getImei(ChangePasswordStepOneActivity.this),
						TelephoneUtil.getModel());
				Message message = handler.obtainMessage(GET_PHONE_CODE);
				message.obj = res;
				handler.sendMessage(message);
			}
		}).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password_one);
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		LoginManager lm = LoginManager
				.getInstance(ChangePasswordStepOneActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
			phoneNumber = login.getUserName();
		}
		dialog = new Dialog(this, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.window_layout);
		codeEditText = (EditText) findViewById(R.id.code);
		phoneNumberTextView = (TextView) findViewById(R.id.number);
		timeTextView = (TextView) findViewById(R.id.timer);
		if (phoneNumber.toString().trim().length() == 11) {
			String num = phoneNumber;
			num = num.substring(0, 3) + "****" + phoneNumber.substring(7);
			phoneNumberTextView.setText(num);
		}

		initMap();

		/*
		 * newPassEditText = (EditText)findViewById(R.id.pass); resPassEditText
		 * = (EditText)findViewById(R.id.resPass);
		 */
	}

	private void initMap() {
		// TODO Auto-generated method stub
		mapView = (MapView) findViewById(R.id.map_view);
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_blue));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
		myLocationStyle.strokeWidth(0f);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mAMapLocationManager = LocationManagerProxy
				.getInstance(ChangePasswordStepOneActivity.this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationEnabled(true);// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
		aMap.getUiSettings().setZoomGesturesEnabled(false);
	}

	/**
	 * 获取验证码
	 * 
	 * @param view
	 */
	public void getcode_onClick(View view) {
		// FIXME 短信获取验证码

		/*
		 * Pattern phonePattern =
		 * Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		 * Matcher phoneMatcher = phonePattern.matcher(phoneNumber); boolean
		 * phoneFlag = phoneMatcher.matches(); if(!phoneFlag){
		 * ToastUtil.show(ChangePasswordActivity.this, "手机号格式不正确！"); return ; }
		 */

		if (!NetConn.checkNetwork(ChangePasswordStepOneActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					LoginService loginService = new LoginService(
							ChangePasswordStepOneActivity.this);
					String res = loginService.getTimeService();
					// int code = loginService.getPhoneCode(1, phonetTextView
					// .getText().toString().trim());
					Message message = handler.obtainMessage(GET_TIME_SERVICE);
					// message.arg1 = code;
					message.obj = res;
					handler.sendMessage(message);
					// LoginService loginService = new
					// LoginService(ChangePasswordStepOneActivity.this);
					// int code=loginService.getPhoneCode(2, phoneNumber);
					// Message message = handler.obtainMessage(GET_PHONE_CODE);
					// message.arg1 = code;
					// handler.sendMessage(message);
				}
			}).start();
		}
		// 按钮按下时创建一个Timer定时器
		timer = new Timer();
		// 创建一个TimerTask
		// TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程
		TimerTask timerTask = new TimerTask() {
			// 倒数90秒
			int i = 90;

			@Override
			public void run() {
				// 定义一个消息传过去
				Message msg = handler.obtainMessage(PHONE_CODE_TIME);
				msg.arg1 = i--;
				handler.sendMessage(msg);
			}
		};
		timer.schedule(timerTask, 1000, 1000);// 3秒后开始倒计时，倒计时间隔为1秒
	}

	/**
	 * 点击下一步
	 * 
	 * @param view
	 */
	public void next_onClick(View view) {
		if (codeEditText.getText().toString().trim().length() == 0) {
			ToastUtil.show(ChangePasswordStepOneActivity.this, "请输入验证码！");
			return;
		}
		if (!NetConn.checkNetwork(ChangePasswordStepOneActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			// FIXME 验证第一步：推荐码为空,手机验证码学要验证
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					RegisterService registerService = new RegisterService(
							ChangePasswordStepOneActivity.this);
					int flag = registerService.checkCode(codeEditText.getText()
							.toString().trim(), phoneNumber, 2, null);
					Message message = handler.obtainMessage(NEXT);
					message.arg1 = flag;
					System.out.println("-----flag------->" + flag);
					handler.sendMessage(message);
					// uploadAllInfo(phonetTextView.getText().toString().trim(),
					// checkCodeTextView.getText().toString().trim(),passWordTextView.getText().toString().trim(),null);
				}
			}).start();
		}
	}

	/**
	 * 地图活跃状态
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 地图关闭
	 */
	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	/**
	 * 地图改变位置
	 * 
	 * @param Location
	 *            location  经纬度
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	/**
	 * 状态改变
	 * 
	 * @param provider
	 *            提供者
	 * @param status
	 *            状态
	 * @param extras
	 *            返回的参数
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * 提供服务
	 * 
	 * @param provider
	 *            提供者
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * 关闭服务
	 * 
	 * @param provider
	 *            提供者
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * 地图改变
	 * 
	 * @param AMapLocation
	 *            amapLocation  经纬度
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null && mListener != null) {
			geoLng = amapLocation.getLatitude();
			geoLat = amapLocation.getLongitude();
			// geoLat = amapLocation.getLatitude();
			// geoLng = amapLocation.getLongitude();
			deactivate();
		}
	}
}
