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
 * 店铺介绍界面
 * 
 * @author lqq
 * 
 */
public class ShopIntroduceActivity extends BaseActivity {

	private EditText inputContentEditText;
	private int shopsid;
	private Dialog proDialog;
	private static final int GET_CONTENT = 1;
	private static final int UPDATE_CONTENT = 2;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_CONTENT:
				String content = (String) msg.obj;
				if (content != null) {
					inputContentEditText.setText(content);
					inputContentEditText.setSelection(inputContentEditText
							.getText().toString().length());
				} else {
					inputContentEditText.setText("");
					inputContentEditText.setHint("如:经营范围、营业时间");
				}
				break;

			case UPDATE_CONTENT:
				if (msg.arg1 == 0) {
					ToastUtil.show(ShopIntroduceActivity.this, "保存成功");
					finish();
				} else {
					ToastUtil.show(ShopIntroduceActivity.this, msg.obj + "");
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_introduce);
		initView();
		initData();
	}

	private void initView() {
		LoginManager lm = LoginManager.getInstance(ShopIntroduceActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}

		inputContentEditText = (EditText) findViewById(R.id.content);
	}

	private void initData() {
		if (!NetConn.checkNetwork(ShopIntroduceActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopIntroduceActivity.this);
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
			ToastUtil.show(ShopIntroduceActivity.this, "请输入介绍");
			return;
		}
		if (!NetConn.checkNetwork(ShopIntroduceActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopIntroduceActivity.this);
					String res = service.updateShopContent(shopsid, getIntent()
							.getStringExtra("phone"), inputContentEditText
							.getText().toString().trim());
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
