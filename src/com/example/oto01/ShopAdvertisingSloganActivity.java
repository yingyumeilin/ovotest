package com.example.oto01;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;

/**
 * 店铺广告语界面
 * 
 * @author lqq
 * 
 */
public class ShopAdvertisingSloganActivity extends BaseActivity {

	private EditText inputContentEditText;
	private int shopsid;
	private Dialog proDialog;
	private static final int GET_CONTENT = 1;
	private static final int UPDATE_CONTENT = 2;
	private String advSlogan;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_CONTENT:
				proDialog.dismiss();
				String content = (String) msg.obj;
				if (content != null) {
					inputContentEditText.setText(content);
				} else {
					inputContentEditText.setText("");
					inputContentEditText.setHint("如:经营范围、营业时间");
				}
				break;

			case UPDATE_CONTENT:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(ShopAdvertisingSloganActivity.this,
							(String) msg.obj);
					setResult(RESULT_OK);
					finish();
				} else {
					ToastUtil.show(ShopAdvertisingSloganActivity.this,
							(String) msg.obj);
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_advertising_slogan);
		advSlogan = getIntent().getStringExtra("advSlogan");
		initView();
		// initData();
	}

	private void initView() {
		LoginManager lm = LoginManager
				.getInstance(ShopAdvertisingSloganActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}

		inputContentEditText = (EditText) findViewById(R.id.content);
		inputContentEditText.setText(advSlogan);
		inputContentEditText.setSelection(inputContentEditText.getText()
				.toString().length());
	}

	private void initData() {
		if (!NetConn.checkNetwork(ShopAdvertisingSloganActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			proDialog = new Dialog(this, R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
			proDialog.setCancelable(false);
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopAdvertisingSloganActivity.this);
					String content = service.lookShopContent(shopsid);
					Message message = handler.obtainMessage(GET_CONTENT);
					message.obj = content;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	private void inputContent() {
		if (inputContentEditText.getText().toString().trim().length() == 0) {
			ToastUtil.show(ShopAdvertisingSloganActivity.this, "请输入广告语");
			return;
		}
		if (!NetConn.checkNetwork(ShopAdvertisingSloganActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			proDialog = new Dialog(this, R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
			proDialog.setCancelable(false);
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopAdvertisingSloganActivity.this);
					String res = service.updateShopAdvertisingSlogan(shopsid,
							getIntent().getStringExtra("phone"),
							inputContentEditText.getText().toString().trim());
					Message message = handler.obtainMessage(UPDATE_CONTENT);

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
				}
			}).start();

		}
	}

	/**
	 * 保存
	 * 
	 * @param view
	 */
	public void save_onClick(View view) {
		inputContent();
	}
}
