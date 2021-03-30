package com.example.oto01;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ContactsActivity extends BaseActivity {
	private EditText phone1EditText, phone2EditText, phone3EditText;
	private int shopsid;
	private Dialog proDialog;
	private static final int GET_CONTACT_LIST = 1;
	private static final int UPDATE_CONTACT_LIST = 2;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_CONTACT_LIST:
				proDialog.dismiss();
				List<String> content = (List<String>) msg.obj;
				if (content != null) {
					switch (content.size()) {
					case 3:
						phone3EditText.setText(content.get(2));
					case 2:
						phone2EditText.setText(content.get(1));
					case 1:
						phone1EditText.setText(content.get(0));
						break;
					}
				} else {
					phone1EditText.setHint("联系电话1");
					phone2EditText.setHint("联系电话2");
					phone3EditText.setHint("联系电话3");
				}
				break;

			case UPDATE_CONTACT_LIST:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(ContactsActivity.this, "保存成功");
					finish();
				} else {
					ToastUtil.show(ContactsActivity.this, msg.obj + "");
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		initView();
		initData();
	}

	private void initView() {
		LoginManager lm = LoginManager.getInstance(ContactsActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		phone1EditText = (EditText) findViewById(R.id.phone1);
		phone2EditText = (EditText) findViewById(R.id.phone2);
		phone3EditText = (EditText) findViewById(R.id.phone3);
	}

	private void initData() {
		if (!NetConn.checkNetwork(ContactsActivity.this)) {
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
							ContactsActivity.this);
					List<String> content = service.getContactList(shopsid);
					Message message = handler.obtainMessage(GET_CONTACT_LIST);
					message.obj = content;
					handler.sendMessage(message);
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
		String phone1 = phone1EditText.getText().toString().trim();
		String phone2 = phone2EditText.getText().toString().trim();
		String phone3 = phone3EditText.getText().toString().trim();
		boolean phoneFlag1 = isTruePhone(phone1) || isTrueMobile(phone1)
				|| isTrue400Mobile(phone1) || isMobileNO(phone1)
				|| isFixedPhone(phone1) || isgan(phone1);
		boolean phoneFlag2 = isTruePhone(phone2) || isTrueMobile(phone2)
				|| isTrue400Mobile(phone2) || isMobileNO(phone2)
				|| isFixedPhone(phone2) || isgan(phone2);
		boolean phoneFlag3 = isTruePhone(phone3) || isTrueMobile(phone3)
				|| isTrue400Mobile(phone3) || isMobileNO(phone3)
				|| isFixedPhone(phone3) || isgan(phone3);

		if (!phoneFlag1 && phone1.length() != 0) {
			System.out.println("-----1------->" + isTruePhone(phone1) + "---"
					+ isTrueMobile(phone1) + "---" + isTrue400Mobile(phone1));
			ToastUtil.show(ContactsActivity.this, "联系方式格式错误");
		} else if (!phoneFlag2 && phone2.length() != 0) {
			System.out.println("-----2------->" + isTruePhone(phone2) + "---"
					+ isTrueMobile(phone2) + "---" + isTrue400Mobile(phone2));
			ToastUtil.show(ContactsActivity.this, "联系方式格式错误");
		} else if (!phoneFlag3 && phone3.length() != 0) {
			System.out.println("-----3------->" + isTruePhone(phone3) + "---"
					+ isTrueMobile(phone3) + "---" + isTrue400Mobile(phone3));
			ToastUtil.show(ContactsActivity.this, "联系方式格式错误");
		} else {
			if (phone1.length() == 13 && phone1.contains("-")) {
				phone1 = phone(phone1);
			}
			if (phone2.length() == 13 && phone2.contains("-")) {
				phone2 = phone(phone2);
			}
			if (phone3.length() == 13 && phone3.contains("-")) {
				phone3 = phone(phone3);
			}

			inputPhone(phone1, phone2, phone3);
		}
	}

	/**
	 * 含有－的手机号
	 */
	private static boolean isgan(String test1) {
		if (test1.length() == 13 && test1.contains("-")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 去除－号
	 */
	private static String phone(String test) {
		String[] messageSplit = test.split("-");
		String str1 = "";
		for (int i = 0; i < messageSplit.length; i++) {
			str1 = str1 + messageSplit[i];
		}
		return str1;
	}

	/**
	 * 判断是否为手机号码
	 * 
	 * @param test
	 * @return
	 */
	private static boolean isTruePhone(String test) {
		Pattern phonePattern = Pattern.compile("[1][34578][0-9]{9}");
		Matcher phoneMatcher = phonePattern.matcher(test);
		return phoneMatcher.matches();
	}

	/**
	 * 是否为 ＋86的格式
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles.contains("+") && mobiles.length() == 14) {
			String mobString = mobiles.substring(3);
			return isTruePhone(mobString);
		} else {
			return false;
		}

	}

	/**
	 * 区号+座机号码+分机号码
	 * 
	 * @param fixedPhone
	 * @return
	 */
	public static boolean isFixedPhone(String fixedPhone) {
		String reg = "^(0\\d{2,3}-\\d{7,8}(-\\d{3,5}){0,1})|(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})$";
		return Pattern.matches(reg, fixedPhone);
	}

	/**
	 * 
	 */
	public static boolean isEightNum(String test) {
		if (test.indexOf("+") == -1 && test.indexOf("-") == -1
				&& test.length() == 8) {
			// 既不包含＋，又不包含－
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断是否为电话号码
	 * 
	 * @param test
	 * @return
	 */
	private boolean isTrueMobile(String test) {
		Pattern phonePattern = Pattern
				.compile("([0-9]{2,5})??[0-9]{7,8}([0-9]{1,})?");
		Matcher phoneMatcher = phonePattern.matcher(test);
		return phoneMatcher.matches();
	}

	/**
	 * 判断是否为400热线
	 * 
	 * @param test
	 * @return
	 */
	private boolean isTrue400Mobile(String test) {
		Pattern phonePattern = Pattern.compile("^400[0-9]{7}$");
		Matcher phoneMatcher = phonePattern.matcher(test);
		return phoneMatcher.matches();
	}

	private void inputPhone(final String phone1, final String phone2,
			final String phone3) {

		if (!NetConn.checkNetwork(ContactsActivity.this)) {
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
							ContactsActivity.this);
					String res = service.updateContactList(shopsid, getIntent()
							.getStringExtra("phone"), phone1, phone2, phone3);
					Message message = handler
							.obtainMessage(UPDATE_CONTACT_LIST);
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

}
