package com.example.oto01;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.model.Login;
import com.example.oto01.services.ClientService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ClientAddActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.rl_tongxunlu)
	private RelativeLayout rl_tongxunlu;
	@ViewInject(R.id.btn_client_add)
	private Button btn_client_add;
	@ViewInject(R.id.et_name)
	private EditText et_name;
	@ViewInject(R.id.et_phone)
	private EditText et_phone;
	@ViewInject(R.id.title_font)
	private TextView title_font;
	private Dialog proDialog;
	private int shopsid = 1;
	private static final int HEBING_STATUS = 1;// 删除商品分类

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				proDialog.dismiss();
				if (msg.arg1 == 4) {
					String error = (String) msg.obj;
					hebing(error);
				} else {
					String error = (String) msg.obj;
					ToastUtil.show(getApplicationContext(), error);
					et_name.setText("");
					et_phone.setText("");
				}

			} else if (msg.what == HEBING_STATUS) {
				String res = (String) msg.obj;

				try {
					JSONObject jo = new JSONObject(res);
					ToastUtil.show(getApplicationContext(), jo.getString("msg")
							+ "");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}

	};

	private void hebing(String error) {
		// 是否合并的提示框
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ClientAddActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage(error);
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						final Message message = handler
								.obtainMessage(HEBING_STATUS);
						if (!NetConn.checkNetwork(ClientAddActivity.this)) {
							// NetConn.setNetwork(OrdersActivity.this);
						} else {
							new Thread(new Runnable() {
								@Override
								public void run() {

									final Message message = handler
											.obtainMessage(HEBING_STATUS);
									new Thread(new Runnable() {
										@Override
										public void run() {
											ClientService service = new ClientService(
													ClientAddActivity.this);
											String res = service.ClientUPDATE(
													shopsid, et_name.getText()
															.toString().trim(),
													et_phone.getText()
															.toString().trim());

											message.obj = res;
											handler.sendMessage(message);
										}
									}).start();

								}
							}).start();
						}
					}
				});
		dialog.setPositiveButton("取消", null);
		dialog.show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_add);
		ViewUtils.inject(this);
		title_font.setText("添加客户");
		rl_tongxunlu.setOnClickListener(this);
		btn_client_add.setOnClickListener(this);
		LoginManager lm = LoginManager.getInstance(ClientAddActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		Intent intent = new Intent();
		ClientAddActivity.this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			ClientAddActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_tongxunlu:
			// 点击手机通讯录
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), TongXunLuActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_client_add:
			// 添加按钮
			if (et_name.getText().toString().trim().isEmpty()
					|| et_phone.getText().toString().trim().isEmpty()) {
				ToastUtil.show(getApplicationContext(), "请您完成信息的填写");
				return;
			}

			if (et_phone.getText().toString().trim().length() < 11) {
				ToastUtil.show(getApplicationContext(), "您输入的手机号小于11位");
				return;
			}

			if (!NetConn.checkNetwork(ClientAddActivity.this)) {
			} else {
				showDialog();
				new AddClientAsync().execute();
			}
		}
	}

	private class AddClientAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			ClientService clientService = new ClientService(
					ClientAddActivity.this);
			Message message = handler.obtainMessage(0);

			String res = clientService.AddClientList(shopsid, et_name.getText()
					.toString().trim(), et_phone.getText().toString().trim());

			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				int flag = jo.optInt("res");
				String error = jo.getString("msg");

				message.arg1 = flag;
				message.obj = error;
				handler.sendMessage(message);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
