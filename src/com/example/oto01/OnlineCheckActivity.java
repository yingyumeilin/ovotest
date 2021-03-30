package com.example.oto01;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class OnlineCheckActivity extends BaseActivity {

	@ViewInject(R.id.title_font)
	private TextView title_font;
	@ViewInject(R.id.iv_line)
	private ImageView iv_line;
	@ViewInject(R.id.tv_14)
	private TextView tv_14;
	@ViewInject(R.id.tv_time_4)
	private TextView tv_time_4;
	@ViewInject(R.id.tv_time_1)
	private TextView tv_time_1;
	@ViewInject(R.id.tv_time_2)
	private TextView tv_time_2;
	@ViewInject(R.id.tv_time_3)
	private TextView tv_time_3;
	@ViewInject(R.id.tv_24)
	private TextView tv_24;
	@ViewInject(R.id.iv_check)
	private ImageView iv_check;
	@ViewInject(R.id.submit)
	private TextView submit;
	@ViewInject(R.id.submitno)
	private TextView submitno;
	private int shopsid = 1;
	private ProgressDialog dialog;
	private static final int SUBMIT_ALL = 101;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_ACCOUNT_STATUS:
				break;
			case SUBMIT_ALL:
				dialog.dismiss();
				String res = (String) msg.obj;
				JSONObject jo = null;
				try {
					jo = new JSONObject(res);
					int flag = jo.optInt("res");
					String error = jo.getString("msg");

					if (flag == 11) {
						Intent intent1 = new Intent();
						intent1.setClass(getApplicationContext(),
								OnLineElecActivity.class);
						intent1.putExtra("type0", "");
						intent1.putExtra("userIdName",
								jo.getString("userIdName"));
						intent1.putExtra("userIdNo", jo.getString("userIdNo"));
						intent1.putExtra("Mobile", jo.getString("Mobile"));
						intent1.putExtra("Email", jo.getString("Email"));
						intent1.putExtra("ReferrerCode",
								jo.getString("ReferrerCode"));
						intent1.setClass(getApplicationContext(),
								OnLineElecActivity.class);
						startActivity(intent1);
						finish();
					} else {
						ToastUtil.show(getApplicationContext(), error);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_check);
		ViewUtils.inject(this);
		title_font.setText("审核状态");
		dialog = new ProgressDialog(this);
		LoginManager lm = LoginManager.getInstance(OnlineCheckActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}

		if (getIntent().getStringExtra("type").equals("online")) {
			tv_time_1.setText(getIntent().getStringExtra("submit_time"));
			tv_time_2.setText(getIntent().getStringExtra("submit_time"));
			tv_time_3.setText(getIntent().getStringExtra("submit_time"));
			iv_check.setImageResource(R.drawable.check_4_1);
			submitno.setVisibility(View.VISIBLE);
			submitno.setFocusable(false);
			submit.setVisibility(View.GONE);
		} else if (getIntent().getStringExtra("type").equals("12")) {
			// 审核中
			tv_time_1.setText(getIntent().getStringExtra("submit_time"));
			tv_time_2.setText(getIntent().getStringExtra("submit_time"));
			tv_time_3.setText(getIntent().getStringExtra("examine_time"));
			iv_check.setImageResource(R.drawable.check_4_1);
			submitno.setVisibility(View.VISIBLE);
			submitno.setFocusable(false);
			submit.setVisibility(View.GONE);
		} else if (getIntent().getStringExtra("type").equals("10")) {
			// 开通成功
			tv_time_1.setText(getIntent().getStringExtra("submit_time"));
			tv_time_2.setText(getIntent().getStringExtra("submit_time"));
			tv_time_3.setText(getIntent().getStringExtra("examine_time"));
			tv_time_4.setText(getIntent().getStringExtra("success_time"));
			iv_check.setImageResource(R.drawable.check_4_3);
			submitno.setVisibility(View.GONE);
			submit.setVisibility(View.VISIBLE);
		} else if (getIntent().getStringExtra("type").equals("11")) {
			// 拒绝原因
			tv_time_1.setText(getIntent().getStringExtra("submit_time"));
			tv_time_2.setText(getIntent().getStringExtra("submit_time"));
			tv_time_3.setText(getIntent().getStringExtra("examine_time"));
			tv_time_4.setText(getIntent().getStringExtra("refuse_time"));
			tv_14.setText("开通失败！");
			tv_24.setText(getIntent().getStringExtra("msg"));
			iv_check.setImageResource(R.drawable.check_4_2);
			submitno.setVisibility(View.GONE);
			submit.setText("重新提交");
			submit.setVisibility(View.VISIBLE);
		}
	}

	private static final int GET_ACCOUNT_STATUS = 3;
	/**
	 * 初始化账户状态
	 */

	private String jumpurl;
	private JSONObject jObject;

	private void initAccountStatus() {
		System.out.println("--------initAccountStatus-------->");
		if (!NetConn.checkNetwork(OnlineCheckActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							OnlineCheckActivity.this);
					String res = service.getAccountStatus(shopsid);
					try {
						jObject = new JSONObject(res);
						if (jObject.optInt("res") == 0) {
							jObject = jObject.getJSONObject("data");
							if (!jObject.isNull("audit")) {
								String audit = jObject.getString("audit");
							}
							if (!jObject.isNull("returnmsg")) {
								String returnmsg = jObject
										.getString("returnmsg");
							}
							if (!jObject.isNull("jumpurl")) {
								jumpurl = jObject.getString("jumpurl");
							}

							Intent in = new Intent(OnlineCheckActivity.this,
									ElectronicAccountActivity.class);
							in.putExtra("url", jumpurl);
							startActivity(in);
						} else {
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
		}
	}

	/**
	 * 提交
	 * 
	 * @param view
	 */
	public void send_onClick(View view) {

		if (getIntent().getStringExtra("type0").equals("")) {

			if (getIntent().getStringExtra("type").equals("10")) {
				initAccountStatus();
			} else if (getIntent().getStringExtra("type").equals("11")) {

				resendSubimt();

			} else {
				// Intent intent = new Intent();
				// OnlineCheckActivity.this.setResult(RESULT_OK, intent);
				finish();
			}

		} else {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MainActivity.class);
			intent.putExtra("login", "");
			startActivity(intent);
			finish();
		}

	}

	private void resendSubimt() {
		// TODO Auto-generated method stub

		if (!NetConn.checkNetwork(OnlineCheckActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			dialog.setMessage("正在重新提交信息...");
			dialog.setCancelable(true);
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							OnlineCheckActivity.this);
					Message message = handler.obtainMessage(SUBMIT_ALL);
					String res = service.getSelectBank(shopsid, getIntent()
							.getStringExtra("Mobile"));

					JSONObject jo = null;
					try {
						jo = new JSONObject(res);
						int flag = jo.optInt("res");
						String error = jo.getString("msg");
						message.arg1 = flag;
						message.obj = res;
						handler.sendMessage(message);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		Intent intent = new Intent();
		OnlineCheckActivity.this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			OnlineCheckActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
