package com.example.oto01;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.oto01.model.Login;
import com.example.oto01.model.LoginReturnMsg;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.services.GoodService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.wheelDialog.WheelDialogManager;
import com.example.wheelmanager.view.wheelitem.WheelView;

public class SettingSendInfoActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private Map<String, String> map;
	private String phone, code, passWord, recommendCode;
	private String name, shopName, shopInfo, location, identity_number;
	private String detail_address;
	private String logo, license;
	private TextView tv_distance;
	private double lng = 0;
	private double lat = 0;
	private int type_id = 0;
	private int shangquan_id = 0;
	private CheckBox cbIsSend, cbNoSend;
	private FrameLayout fLayout;
	private EditText etDiscance, etSendPrice, etDistributionPrice;
	private TextView tvStartTime, tvEndTime;
	private int isSend = 1;
	private AlertDialog dialog;
	private WheelView minute, hour;
	private int mHour = 0, mMinute = 0;
	private int fontSize = 24; // 字体大小
	private final static int REGISTER_RES = 1;
	private final static int UPDATE_RES = 2;
	private static final int CHECK_SHOP_INFO = 12;
	private ProgressDialog proDialog;
	private int is_send;
	private WheelDialogManager mWheeManager;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REGISTER_RES:
				proDialog.dismiss();
				LoginReturnMsg mLoginReturnMsg = (LoginReturnMsg) msg.obj;
				if (mLoginReturnMsg == null) {
					ToastUtil.show(SettingSendInfoActivity.this, "注册失败！");
				} else if (mLoginReturnMsg != null
						&& mLoginReturnMsg.getRes() == 0) {
					SysApplication.getInstance().removeActivity();
					Intent intent = new Intent(SettingSendInfoActivity.this,
							SelectOpenElectronicActivity.class);
					intent.putExtra("shopsName", shopName);
					startActivity(intent);
					finish();
				} else {
					ToastUtil.show(SettingSendInfoActivity.this,
							mLoginReturnMsg.getMsg());
				}
				break;
			case UPDATE_RES:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(SettingSendInfoActivity.this, msg.obj + "");
					finish();
				} else {
					ToastUtil.show(SettingSendInfoActivity.this, msg.obj + "");
				}
				break;
			case CHECK_SHOP_INFO:
				proDialog.dismiss();
				ShopInfo shopInfo = (ShopInfo) msg.obj;
				// 是否支持外送:1是2否
				if (shopInfo.getIs_send() == 1) {
					cbIsSend.setChecked(true);
					cbNoSend.setChecked(false);
					fLayout.setVisibility(View.GONE);
					setViewIs(true);
				} else if (shopInfo.getIs_send() == 2) {
					cbNoSend.setChecked(true);
					cbIsSend.setChecked(false);
					fLayout.setVisibility(View.VISIBLE);
					setViewIs(false);
				}

				try {
					if (shopInfo.getSend_distance().equals("null")) {
						etDiscance.setText("");
						tv_distance.setVisibility(View.VISIBLE);
					} else if (shopInfo.getSend_distance().equals("-1")) {
						etDiscance.setText("同城");
						etDiscance.setFocusable(false);
						tv_distance.setVisibility(View.INVISIBLE);

					} else if (shopInfo.getSend_distance().equals("-2")) {
						etDiscance.setText("全国");
						etDiscance.setFocusable(false);
						tv_distance.setVisibility(View.INVISIBLE);
					} else {
						tv_distance.setVisibility(View.VISIBLE);
						etDiscance.setText(shopInfo.getSend_distance() + "");
						etDiscance.setSelection(etDiscance.getText().length());
					}
				} catch (Exception e) {
				}

				etSendPrice.setText(shopInfo.getSendprice() + "");
				etDistributionPrice.setText(shopInfo.getFreight_price() + "");
				tvStartTime.setText(shopInfo.getBusiness_time().get(0)
						.getStart_time()
						+ "");
				tvEndTime.setText(shopInfo.getBusiness_time().get(0)
						.getEnd_time()
						+ "");

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_send_info);
		Bundle bundle = getIntent().getBundleExtra("data");
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		initView();
		try {
			if (getIntent().getStringExtra("type").equals("0")) {
				tv_title.setText("外卖设置");
				// 从首页外卖上面点进去 点进 外卖设置
				LoginManager lm = LoginManager
						.getInstance(SettingSendInfoActivity.this);
				Login login = lm.getLoginInstance();
				if (login != null && login.getAdminId() != -1) {
					shopsid = login.getAdminId();
				}
				chekShopInfo();
			}
		} catch (Exception e) {
			// TODO: handle exception
			if (bundle != null) {
				phone = bundle.getString("phone");
				passWord = bundle.getString("password");
				code = bundle.getString("code");
				recommendCode = bundle.getString("recode");
				name = bundle.getString("name");
				shopName = bundle.getString("shopsname");
				shopInfo = bundle.getString("content");
				location = bundle.getString("location");
				detail_address = bundle.getString("detail_address");
				identity_number = bundle.getString("identity_number");
				logo = bundle.getString("logo");
				license = bundle.getString("license");
				lng = bundle.getDouble("longitude");
				lat = bundle.getDouble("latitude");
				type_id = bundle.getInt("typeid");
				shangquan_id = bundle.getInt("shangquan_id");
				map = (Map<String, String>) bundle.getSerializable("info");
				System.out.println("-----bundle-------->" + bundle.toString());
				System.out.println("-----bundle--map------>" + map);
			}
		}

	}

	/**
	 * 进入这个页面的时候 判断 是否接单的状态
	 */
	private void chekShopInfo() {
		// TODO Auto-generated method stub
		if (!NetConn.checkNetwork(SettingSendInfoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new ChekShopInfoAsyc().execute();
		}
	}

	private class ChekShopInfoAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			SettingService settingService = new SettingService(
					SettingSendInfoActivity.this);
			Message message = handler.obtainMessage(CHECK_SHOP_INFO);
			ShopInfo res = settingService.checkShopInfo2(shopsid);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	private void initView() {
		cbIsSend = (CheckBox) findViewById(R.id.checkbox_yes);
		cbNoSend = (CheckBox) findViewById(R.id.checkbox_no);
		etDiscance = (EditText) findViewById(R.id.et_distance);
		etSendPrice = (EditText) findViewById(R.id.et_send_price);
		etDistributionPrice = (EditText) findViewById(R.id.et_distribution_price);
		tvStartTime = (TextView) findViewById(R.id.start_time_textview);
		tvEndTime = (TextView) findViewById(R.id.end_time_textview);
		fLayout = (FrameLayout) findViewById(R.id.fl_layout);
		cbIsSend.setOnCheckedChangeListener(this);
		cbNoSend.setOnCheckedChangeListener(this);
		proDialog = new ProgressDialog(this);
		proDialog.setMessage("正在注册......");
		proDialog.setCancelable(false);
		mWheeManager = WheelDialogManager.getInstanse();
	}

	// private void register(final int send_distance_m, final int sendprice,
	// final double freight_price, final String time_one) {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// /**
	// * 上传注册信息
	// */
	// RegisterService registerService = new RegisterService(
	// SettingSendInfoActivity.this);
	// LoginReturnMsg loginReturnMsg = registerService.registerNew(
	// phone, passWord, recommendCode, shopName, lng, lat,
	// location, detail_address, type_id, name,
	// identity_number, shopInfo, logo, license, isSend,
	// send_distance_m, sendprice, freight_price, time_one);
	// Message message = handler.obtainMessage(REGISTER_RES);
	// message.obj = loginReturnMsg;
	// handler.sendMessage(message);
	// }
	// }).start();
	// }

	private int shopsid = 0;

	private void goodsup(final int send_distance_m, final int sendprice,
			final double freight_price, final String time_one) {

		if (cbIsSend.isChecked()) {
			is_send = 1;
		} else {
			is_send = 2;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				/**
				 * 上传注册信息
				 */
				GoodService service = new GoodService(
						SettingSendInfoActivity.this);
				String res = service.goodSetting(shopsid, is_send,
						send_distance_m, sendprice, freight_price, time_one);
				Message message = handler.obtainMessage(UPDATE_RES);

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(res);
					message.obj = jsonObject.getString("msg");
					message.arg1 = jsonObject.optInt("res");
					handler.sendMessage(message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	public void back_onClick(View view) {
		finish();
	}

	/**
	 * 
	 * 功能:点击完成
	 * 
	 * @param view
	 * @author: liqq
	 * @date:2015-4-29下午5:46:36
	 */
	public void next_onClick(View view) {

		try {
			if (getIntent().getStringExtra("type").equals("0")) {
				// 新加的接口

				if (cbIsSend.isChecked()) {
					if (etDiscance.getText().toString().trim().length() == 0) {
						ToastUtil.show(this, "请填写配送距离");
						return;
					}
					if (etSendPrice.getText().toString().trim().length() == 0) {
						ToastUtil.show(this, "请填写起送价");
						return;
					}
					if (etDistributionPrice.getText().toString().trim()
							.length() == 0) {
						ToastUtil.show(this, "请填写配送费");
						return;
					}
					if ((tvStartTime.toString().trim().length() == 0 || tvEndTime
							.toString().trim().length() == 0)) {
						ToastUtil.show(this, "请选择营业时间");
						return;
					}
					int sendDistance = -1;
					if (etDiscance.getText().toString().trim().equals("同城")) {
						sendDistance = -1;
					} else if (etDiscance.getText().toString().trim()
							.equals("全国")) {
						sendDistance = -2;
					} else {
						sendDistance = Integer.parseInt(etDiscance.getText()
								.toString().trim());
					}

					int sendprice = Integer.parseInt(etSendPrice.getText()
							.toString().trim());
					double freight_price = Double
							.parseDouble(etDistributionPrice.getText()
									.toString().trim());
					String time_one = tvStartTime.getText().toString().trim()
							+ "_" + tvEndTime.getText().toString().trim();

					if (!NetConn.checkNetwork(SettingSendInfoActivity.this)) {
					} else {
						proDialog.setMessage("正在加载......");
						proDialog.show();
						goodsup(sendDistance, sendprice, freight_price,
								time_one);
					}
				} else {
					if (!NetConn.checkNetwork(SettingSendInfoActivity.this)) {
					} else {
						proDialog.setMessage("正在加载......");
						proDialog.show();
						goodsup(0, 0, 0, "0");
					}
				}

			}
		} catch (Exception e) {

			if (cbIsSend.isChecked()) {
				if (checkInfoIsSpace("send_distance_m")
						&& etDiscance.getText().toString().trim().length() == 0) {
					ToastUtil.show(this, "请填写配送距离");
					return;
				}
				if (checkInfoIsSpace("sendprice")
						&& etSendPrice.getText().toString().trim().length() == 0) {
					ToastUtil.show(this, "请填写起送价");
					return;
				}
				if (checkInfoIsSpace("freight_price")
						&& etDistributionPrice.getText().toString().trim()
								.length() == 0) {
					ToastUtil.show(this, "请填写配送费");
					return;
				}
				if (checkInfoIsSpace("time_one")
						&& (tvStartTime.toString().trim().length() == 0 || tvEndTime
								.toString().trim().length() == 0)) {
					ToastUtil.show(this, "请选择营业时间");
					return;
				}
				int sendDistance = Integer.parseInt(etDiscance.getText()
						.toString().trim());
				int sendprice = Integer.parseInt(etSendPrice.getText()
						.toString().trim());
				double freight_price = Double.parseDouble(etDistributionPrice
						.getText().toString().trim());
				String time_one = tvStartTime.getText().toString().trim() + "_"
						+ tvEndTime.getText().toString().trim();
				if (!NetConn.checkNetwork(SettingSendInfoActivity.this)) {
				} else {
					proDialog.show();
					// register(sendDistance, sendprice, freight_price,
					// time_one);
				}
			} else {
				if (!NetConn.checkNetwork(SettingSendInfoActivity.this)) {
				} else {
					proDialog.show();
					// register(0, 0, 0, "");
				}

			}
		}

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
	public void onCheckedChanged(CompoundButton view, boolean flag) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.checkbox_yes:
			if (flag) {
				cbIsSend.setChecked(true);
				cbNoSend.setChecked(false);
				fLayout.setVisibility(View.GONE);
				setViewIs(true);
				isSend = 1;
			} else {
				cbNoSend.setChecked(true);
				cbIsSend.setChecked(false);
				fLayout.setVisibility(View.VISIBLE);
				setViewIs(false);
				isSend = 2;
			}
			break;
		case R.id.checkbox_no:
			if (flag) {
				cbNoSend.setChecked(true);
				cbIsSend.setChecked(false);
				fLayout.setVisibility(View.VISIBLE);
				setViewIs(false);
				isSend = 2;
			} else {
				cbIsSend.setChecked(true);
				cbNoSend.setChecked(false);
				fLayout.setVisibility(View.GONE);
				setViewIs(true);
				isSend = 1;
			}
			break;
		}

	}

	private void setViewIs(boolean flag) {
		etDiscance.setEnabled(flag);
		etSendPrice.setEnabled(flag);
		etDistributionPrice.setEnabled(flag);
		tvStartTime.setEnabled(true);
		tvEndTime.setEnabled(true);
	}

	/**
	 * 选择时间
	 * 
	 * @param view
	 */
	public void select_time_onClick(View view) {
		mWheeManager.createDatePreciseVheel(SettingSendInfoActivity.this, tvStartTime);
	}
	
	// 选择右边的时间
	public void select_time_onClick1(View view) {
		mWheeManager.createDatePreciseVheel(SettingSendInfoActivity.this, tvEndTime);
	}

}
