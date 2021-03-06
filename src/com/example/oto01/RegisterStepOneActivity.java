package com.example.oto01;

import java.io.Serializable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.oto01.model.Data;
import com.example.oto01.services.LoginService;
import com.example.oto01.services.RegisterService;
import com.example.oto01.utils.MD5Utils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.TelephoneUtil;
import com.example.oto01.utils.ToastUtil;

public class RegisterStepOneActivity extends Activity implements
		LocationSource, AMapLocationListener {
	private EditText phonetTextView, checkCodeTextView, passWordTextView,
			recommendTextView;
	private TextView timeTextView;
	private String phone, checkcode, password, recommendCode;
	// private Timer timer;
	protected static final int PHONE_CODE_TIME = 1;
	protected static final int GET_PHONE_CODE = 2;
	protected static final int NEXT = 0;
	protected static final int CHECK_INPUT_INFO = 3;
	protected static final int GET_TIME_SERVICE = 4;
	private Dialog dialog, newDialog;
	private ProgressDialog proDialog;
	private ImageView checkBox;
	private int i = 1;
	private Timer timer;
	private MapView mapView;// ?????????view
	private AMap aMap;// ??????
	private OnLocationChangedListener mListener;// ????????????????????????
	private LocationManagerProxy mAMapLocationManager;// Location??????????????????
	public static Double geoLat;// ??????
	public static Double geoLng;// ??????

	private Map<String, String> map;
	/**
	 * ?????????????????????
	 */
	private String timestamp;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NEXT:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					// initStep2();
					// ?????????????????????????????????????????????????????????????????????
					Intent intent = new Intent(RegisterStepOneActivity.this,
							RegisterStepTwoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("phone", phonetTextView.getText()
							.toString());
					bundle.putString("password", passWordTextView.getText()
							.toString());
					bundle.putString("code", checkCodeTextView.getText()
							.toString());
					bundle.putString("recode", recommendTextView.getText()
							.toString());
					bundle.putSerializable("info", (Serializable) map);
					intent.putExtra("data", bundle);
					startActivity(intent);

				} else {
					ToastUtil.show(RegisterStepOneActivity.this,
							msg.obj.toString());
				}
				break;
			case PHONE_CODE_TIME:
				if (msg.arg1 > 0) {
					timeTextView.setText(msg.arg1 + "????????????");
					timeTextView.setClickable(false);
				} else {
					timeTextView.setText("???????????????");
					timeTextView.setClickable(true);
					// // ??????Timer?????????
					if (timer != null) {
						timer.cancel();
					}
				}
				break;
			case GET_PHONE_CODE:
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
						// ????????????TimerTask
						// TimerTask???????????????,?????????Runnable???????????????TimerTask?????????????????????
						TimerTask timerTask = new TimerTask() {
							// ??????90???
							int i = jObject.optInt("second");

							@Override
							public void run() {
								// ???????????????????????????
								Message msg = handler
										.obtainMessage(PHONE_CODE_TIME);
								msg.arg1 = i--;
								handler.sendMessage(msg);
							}
						};
						timer.schedule(timerTask, 1000, 1000);// 3??????????????????????????????????????????1???
					} else if (jObject.optInt("res") == 9) {
						// ?????????????????????????????????????????????????????????????????????????????????
						ToastUtil.show(getApplicationContext(),
								jObject.getString("msg"));
						timeTextView.setText("???????????????");
						timeTextView
								.setBackgroundResource(R.drawable.huidianshape);
						timeTextView.setClickable(false);
					} else {
						ToastUtil.show(getApplicationContext(),
								jObject.getString("msg"));
						timeTextView.setText("???????????????");
						timeTextView
								.setBackgroundResource(R.drawable.pandianshape);
						timeTextView.setClickable(true);
						// if (timer != null) {
						// timer.cancel();
						// }
					}

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			case CHECK_INPUT_INFO:
				// ??????????????? ???????????? ??????????????????
				break;
			case GET_TIME_SERVICE:
				// ????????????????????????
				String res_time = (String) msg.obj;
				try {
					JSONObject jo = new JSONObject(res_time);
					if (jo.getInt("res") == 0) {
						JSONObject jsonObject = jo.getJSONObject("data");
						timestamp = jsonObject.getString("timestamp");
						// ????????????????????????
						getCode();
					} else {
						ToastUtil.show(getApplicationContext(),
								jo.getString("msg"));
					}
				} catch (Exception e) {
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
						RegisterStepOneActivity.this);

				String res = service.getPhoneCode(phonetTextView.getText()
						.toString(), "1", signature, timestamp, random, geoLat,
						geoLng, TelephoneUtil
								.getImei(RegisterStepOneActivity.this),
						TelephoneUtil.getModel());
				Message message = handler.obtainMessage(GET_PHONE_CODE);
				message.obj = res;
				handler.sendMessage(message);
			}
		}).start();
	}

	private void initInputInfoIsSpace() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				RegisterService registerService = new RegisterService(
						RegisterStepOneActivity.this);
				map = registerService.checkInputInfoIsSpace();
				Message message = handler.obtainMessage(CHECK_INPUT_INFO);
				message.obj = map;
				System.out.println("-----map------->" + map);
				handler.sendMessage(message);
			}
		}).start();
	}

	private boolean checkInfoIsSpace(String key) {
		if (map != null && map.containsKey(key)) {
			System.out.println("------checkInfoIsSpace--key------>" + key);
			System.out.println("------checkInfoIsSpace-value----->"
					+ map.get(key));
			return (map.get(key) == "1" || ("1").equals(map.get(key))) ? true
					: false;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_1);
		SysApplication.getInstance().addActivity(this);
		dialog = new Dialog(this, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.window_layout);
		RelativeLayout title_font1 = (RelativeLayout) findViewById(R.id.title);
		newDialog = new Dialog(RegisterStepOneActivity.this,
				R.style.theme_dialog_alert);
		proDialog = new ProgressDialog(this);
		initMap();
		// showInfoDialog();
		initInputInfoIsSpace();
		initView();
		title_font1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getIntent().getStringExtra("type").equals("loading")) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					finish();
				}

			}
		});
	}

	/**
	 * 
	 * TODO ???????????????
	 * 
	 * @throw
	 * @return void
	 */
	private void initMap() {
		mapView = (MapView) findViewById(R.id.map_view);
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_blue));// ????????????????????????
		myLocationStyle.strokeColor(Color.TRANSPARENT);// ???????????????????????????
		myLocationStyle.strokeWidth(0f);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);// ???????????????????????????
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);// ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
		mAMapLocationManager = LocationManagerProxy
				.getInstance(RegisterStepOneActivity.this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationEnabled(true);// ?????????true????????????????????????????????????????????????false????????????????????????false
		aMap.getUiSettings().setZoomGesturesEnabled(false);
	}

	/**
	 * ???????????????
	 * 
	 * @param view
	 */
	public void read_onClick(View view) {
		Intent intent = new Intent(RegisterStepOneActivity.this,
				ReadAndAgreeEAccountActivity.class);
		intent.putExtra("tag", 1);
		startActivity(intent);
	}

	private void showInfoDialog() {
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = null;
		layout = (LinearLayout) inflater.inflate(R.layout.info1_dialogview,
				null);
		layout.setMinimumWidth((int) (RegisterStepOneActivity.this
				.getWindowManager().getDefaultDisplay().getWidth() * 0.7));// ??????dialog?????????
		newDialog.setContentView(layout);
		newDialog.setCancelable(false);
		newDialog.setCanceledOnTouchOutside(false);
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				newDialog.dismiss();
			}
		});
		newDialog.show();// ??????AlertDialog
	}

	private void initView() {
		phonetTextView = (EditText) findViewById(R.id.phone);
		checkCodeTextView = (EditText) findViewById(R.id.yanzheng_code);
		passWordTextView = (EditText) findViewById(R.id.password);
		recommendTextView = (EditText) findViewById(R.id.recommendCode);
		timeTextView = (TextView) findViewById(R.id.timer);
		checkBox = (ImageView) findViewById(R.id.checkbox);
		phonetTextView.setText(phone);
		passWordTextView.setText(password);
		checkCodeTextView.setText(checkcode);
		recommendTextView.setText(recommendCode);

		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (i == 1) {
					checkBox.setImageResource(R.drawable.checkbox_no_selected);
					i = 2;
				} else if (i == 2) {
					checkBox.setImageResource(R.drawable.checkbox_selected);
					i = 1;
				}
			}
		});

		phonetTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				timeTextView.setBackgroundResource(R.drawable.pandianshape);
				timeTextView.setClickable(true);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * ???????????????
	 * 
	 * @param view
	 */
	public void getcode_onClick(View view) {
		// FIXME ?????????????????????
		if (phonetTextView.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepOneActivity.this, "??????????????????");
			return;
		}
		if (phonetTextView.getText().toString().trim().length() != 11) {
			ToastUtil.show(RegisterStepOneActivity.this, "????????????????????????11???");
			return;
		}
		// Pattern phonePattern =
		// Pattern.compile("(\\+86|86|0086)?((13[0-9]|15[0-35-9]|14[57]|18[0-9])\\d{8})");
		// Matcher phoneMatcher =
		// phonePattern.matcher(phonetTextView.getText().toString().trim());
		// boolean phoneFlag = phoneMatcher.matches();
		// if(!phoneFlag){
		// ToastUtil.show(RegisterStepOneActivity.this, "???????????????????????????");
		// return ;
		// }
		if (!NetConn.checkNetwork(RegisterStepOneActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					LoginService loginService = new LoginService(
							RegisterStepOneActivity.this);
					String res = loginService.getTimeService();
					// int code = loginService.getPhoneCode(1, phonetTextView
					// .getText().toString().trim());
					Message message = handler.obtainMessage(GET_TIME_SERVICE);
					// message.arg1 = code;
					message.obj = res;
					handler.sendMessage(message);
				}
			}).start();
		}

	}

	public void next_onClick(View view) {
		if (i == 2) {
			ToastUtil.show(RegisterStepOneActivity.this, "??????????????????????????????");
			return;
		}
		if (map == null) {
			if (!NetConn.checkNetwork(RegisterStepOneActivity.this)) {
			} else {
				initInputInfoIsSpace();
			}
			return;
		}
		if (checkInfoIsSpace("phone")
				&& phonetTextView.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepOneActivity.this, "??????????????????");
			return;
		}
		if (phonetTextView.getText().toString().trim().length() != 11) {
			ToastUtil.show(RegisterStepOneActivity.this, "????????????????????????11???");
			return;
		}
		// Pattern phonePattern =
		// Pattern.compile("(\\+86|86|0086)?((13[0-9]|15[0-35-9]|14[57]|18[0-9])\\d{8})");
		// Matcher phoneMatcher =
		// phonePattern.matcher(phonetTextView.getText().toString().trim());
		// boolean phoneFlag = phoneMatcher.matches();
		// if(!phoneFlag){
		// ToastUtil.show(RegisterStepOneActivity.this, "???????????????????????????");
		// return ;
		// }
		if (checkCodeTextView.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepOneActivity.this, "??????????????????");
			return;
		}
		if (checkInfoIsSpace("pass")
				&& passWordTextView.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepOneActivity.this, "???????????????");
			return;
		}
		if (passWordTextView.getText().toString().trim().length() < 6
				|| passWordTextView.getText().toString().trim().length() > 16) {
			ToastUtil.show(RegisterStepOneActivity.this, "????????????6-16???");
			return;
		}
		String code = recommendTextView.getText().toString().trim();
		System.out.println("-------code length-------->"
				+ code.getBytes().length);
		if (checkInfoIsSpace("rid") && code.length() == 0) {
			ToastUtil.show(RegisterStepOneActivity.this, "??????????????????");
		}
		/*
		 * if(code.length()>0){
		 * if((!code.contains("b"))&&(!code.contains("B"))&&
		 * code.getBytes().length != 8){
		 * ToastUtil.show(RegisterStepOneActivity.this, "???????????????????????????"); return ; }
		 * if((code.contains("b")||code.contains("B"))&&code.getBytes().length
		 * != 9){ ToastUtil.show(RegisterStepOneActivity.this, "???????????????????????????");
		 * return ; }
		 * if((code.contains("b")||code.contains("B"))&&(code.charAt(0)!= 'b'&&
		 * code.charAt(0)!= 'B')){ ToastUtil.show(RegisterStepOneActivity.this,
		 * "???????????????????????????"); return ; } }
		 */
		if (!NetConn.checkNetwork(RegisterStepOneActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			// FIXME ?????????????????????????????????,???????????????????????????
			proDialog.setMessage("????????????????????????...");
			proDialog.setCancelable(false);
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					RegisterService registerService = new RegisterService(
							RegisterStepOneActivity.this);
					int flag = registerService.checkCode(checkCodeTextView
							.getText().toString().trim(), phonetTextView
							.getText().toString().trim(), 1, recommendTextView
							.getText().toString().trim());
					Message message = handler.obtainMessage(NEXT);
					message.arg1 = flag;
					message.obj = registerService.errorMsg;
					System.out.println("-----flag------->" + flag);
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * ??????????????????
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
	 * ????????????
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
	 * ??????????????????
	 * 
	 * @param Location
	 *            location ???????????
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	/**
	 * ????????????
	 * 
	 * @param provider
	 *            ?????????
	 * @param status
	 *            ??????
	 * @param extras
	 *            ???????????????
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * ????????????
	 * 
	 * @param provider
	 *            ?????????
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * ????????????
	 * 
	 * @param provider
	 *            ?????????
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * ????????????
	 * 
	 * @param AMapLocation
	 *            amapLocation ???????????
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null && mListener != null) {
			geoLng = amapLocation.getLatitude();
			geoLat = amapLocation.getLongitude();
			deactivate();
		}
	}
}
