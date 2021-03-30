package com.example.oto01;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.ClientsUtil;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;

/**
 * 选择是否开通电子账户
 * 
 * @author user
 * 
 */
public class SelectOpenElectronicActivity extends Activity {
	private String phone;
	private int shopsid = 1;
	private String shopsName;
	private static final int SELECT_BANK = 101;
	private ProgressDialog proDialog;
	private static final int GET_ACCOUNT_STATUS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_shop_res);
		this.shopsName = getIntent().getStringExtra("shopsName");
		proDialog = new ProgressDialog(this);
		LoginManager lm = LoginManager
				.getInstance(SelectOpenElectronicActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
			phone = login.getUserName();
		}
		if (ClientsUtil.getFirstLoginSettingKuang(getApplicationContext())) {

		} else {
			ClientsUtil.setFirstSecondSettingKuang(getApplicationContext(),
					true);
			LoginActivity.secondtime++;
		}
	}

	/**
	 * 开通收银台
	 * 
	 * @param view
	 */
	public void open_onClick(View view) {

		if (!NetConn.checkNetwork(SelectOpenElectronicActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			proDialog.setMessage("正在加载......");
			proDialog.show();
		}

		new ShouAsync().execute();

	}

	private String phone1;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			proDialog.dismiss();
			switch (msg.what) {

			case GET_ACCOUNT_STATUS:
				String res = (String) msg.obj;
				try {
					JSONObject jObject = new JSONObject(res);
					if (jObject.optInt("res") == 0) {
						JSONObject array = jObject.getJSONObject("data");
						phone1 = array.getString("phone");
					} else {
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				new OnLineAsync().execute();
				break;

			case SELECT_BANK:
				// 在线收银台 查询
				proDialog.dismiss();
				if (msg.arg1 == 0) {

					String res1 = (String) msg.obj;

					JSONObject jo = null;
					try {
						jo = new JSONObject(res1);
						// 店主姓名
						String userIdName = jo.getString("userIdName");
						// 身份证号码
						String userIdNo = jo.getString("userIdNo");
						// 电子邮箱
						String Email = jo.getString("Email");
						// 推荐码
						String ReferrerCode = jo.getString("ReferrerCode");
						Intent intent1 = new Intent();
						intent1.setClass(getApplicationContext(),
								OnLineElecActivity.class);
						intent1.putExtra("type0", "1");
						intent1.putExtra("Mobile", phone1);
						intent1.putExtra("userIdName", userIdName);
						intent1.putExtra("userIdNo", userIdNo);
						intent1.putExtra("Email", Email);
						intent1.putExtra("ReferrerCode", ReferrerCode);
						startActivity(intent1);
					} catch (Exception e) {

					}
				} else {

					if (msg.arg1 == 10 || msg.arg1 == 11 || msg.arg1 == 12) {
						System.out
								.println("--------------msg.arg2------------->"
										+ msg.arg1);
						JSONObject jo = null;
						try {
							String res2 = (String) msg.obj;
							jo = new JSONObject(res2);
							int addtime = jo.optInt("addtime");
							int submit_time = jo.optInt("submit_time");// 审核提交时间
							int examine_time = jo.optInt("examine_time");// 审核时间
							int refuse_time = jo.optInt("refuse_time");// 拒绝时间
							int success_time = jo.optInt("success_time");// 开通成功时间
							String error = jo.optString("msg");

							Intent intent1 = new Intent();
							intent1.setClass(getApplicationContext(),
									OnlineCheckActivity.class);
							intent1.putExtra("type0", "1");
							intent1.putExtra("type", msg.arg1 + "");
							intent1.putExtra("msg", error + "");
							intent1.putExtra("time",
									DateUtil.getFormatedDate_1(addtime + ""));
							intent1.putExtra("submit_time", DateUtil
									.getFormatedDate_1(submit_time + ""));
							intent1.putExtra(
									"examine_time",
									DateUtil.getFormatedDate_1(examine_time
											+ ""));
							intent1.putExtra("refuse_time", DateUtil
									.getFormatedDate_1(refuse_time + ""));
							intent1.putExtra(
									"success_time",
									DateUtil.getFormatedDate_1(success_time
											+ ""));

							try {
								if (!jo.isNull("Mobile")) {
									String phone1 = jo.getString("Mobile");
									intent1.putExtra("Mobile", phone1);
								}
							} catch (Exception e) {
							}
							startActivity(intent1);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					} else {
						JSONObject jo = null;
						try {
							String res3 = (String) msg.obj;
							jo = new JSONObject(res3);
							String error = jo.optString("msg");
							ToastUtil.show(getApplicationContext(), error + "");
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}

				break;
			}
		};
	};
	private JSONObject jObject;

	private class ShouAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			SettingService service = new SettingService(
					SelectOpenElectronicActivity.this);
			String res = service.getAccountStatus(shopsid);
			Message message = handler.obtainMessage(GET_ACCOUNT_STATUS);
			try {
				jObject = new JSONObject(res);
				if (jObject.optInt("res") == 0) {
					jObject = jObject.getJSONObject("data");
					if (!jObject.isNull("audit")) {
						String audit = jObject.getString("audit");
					}
					if (!jObject.isNull("returnmsg")) {
						String returnmsg = jObject.getString("returnmsg");
					}
					int flag = jObject.optInt("accountstate");
					message.arg1 = flag;
					message.obj = res;
					handler.sendMessage(message);
				} else {
					String error = jObject.getString("msg");
					message.obj = res;
					handler.sendMessage(message);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class OnLineAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			SettingService settingService = new SettingService(
					SelectOpenElectronicActivity.this);
			Message message = handler.obtainMessage(SELECT_BANK);
			// message.arg1 = CURRENT_OPT_STATUS;
			String res = settingService.getSelectBank(shopsid, phone1);

			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				int flag = jo.optInt("res");
				int addtime = jo.optInt("addtime");
				if (flag == 0) {
					message.arg1 = flag;
					message.arg2 = addtime;
					message.obj = res;
					handler.sendMessage(message);
				} else {
					String msg = jo.optString("msg");
					message.arg1 = flag;
					message.arg2 = addtime;
					message.obj = res;
					handler.sendMessage(message);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 进入主界面
	 * 
	 * @param view
	 */
	public void enter_onClick(View view) {
		Intent intent = new Intent(SelectOpenElectronicActivity.this,
				MainActivity.class);
		intent.putExtra("shopsName", shopsName);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(SelectOpenElectronicActivity.this,
					MainActivity.class);
			intent.putExtra("shopsName", shopsName);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
