package com.example.oto01;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.model.Constant;
import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.UpLoadFile;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class OnlineFenghuangbaoActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.title_font)
	private TextView title_font;
	@ViewInject(R.id.rl1)
	private RelativeLayout rl1;
	@ViewInject(R.id.iv_duihao)
	private ImageView iv_duihao;
	@ViewInject(R.id.tv_xieyi)
	private TextView tv_xieyi;
	@ViewInject(R.id.btn_refuse)
	private Button btn_refuse;
	@ViewInject(R.id.btn_use)
	private Button btn_use;
	private ProgressDialog dialog;
	private Boolean dui = true;
	private static final int SUBMIT_ALL = 101;
	private static final int ONLINE_CHECK = 107;
	protected static final int UPLOAD_PIC = 201;
	protected static final int UPLOAD_PIC_BEI = 202;
	protected static final int UPLOAD_PIC_SHOU = 203;
	private int shopsid;
	private String phone;
	private Map<Integer, String> CURRENT_IMG_PATH1 = new HashMap<Integer, String>();
	private Map<Integer, String> UPDATE_IMG_PATH = new HashMap<Integer, String>();

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SUBMIT_ALL:
				dialog.dismiss();
				String res = (String) msg.obj;
				JSONObject jo = null;

				if (msg.arg1 == 0) {
					try {
						jo = new JSONObject(res);
						int flag = jo.optInt("res");
						// String error = jo.getString("msg");
						int addtime = jo.optInt("addtime");
						int submit_time = jo.optInt("submit_time");// 审核提交时间
						int examine_time = jo.optInt("examine_time");// 审核时间
						int refuse_time = jo.optInt("refuse_time");// 拒绝时间
						int success_time = jo.optInt("success_time");// 开通成功时间

						if (getIntent().getStringExtra("type0").equals("")) {
							// 首页进来的
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									OnlineCheckActivity.class);
							intent.putExtra("type0", getIntent()
									.getStringExtra("type0"));
							intent.putExtra("time",
									DateUtil.getFormatedDate_1(addtime + ""));
							intent.putExtra("submit_time", DateUtil
									.getFormatedDate_1(submit_time + ""));
							intent.putExtra(
									"examine_time",
									DateUtil.getFormatedDate_1(examine_time
											+ ""));
							intent.putExtra("refuse_time", DateUtil
									.getFormatedDate_1(refuse_time + ""));
							intent.putExtra(
									"success_time",
									DateUtil.getFormatedDate_1(success_time
											+ ""));
							intent.putExtra("type", "online");
							startActivityForResult(intent, ONLINE_CHECK);
							// finish();

						} else if (getIntent().getStringExtra("type0").equals(
								"1")) {
							// 从注册页面直接过来的
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									OnlineCheckActivity.class);
							intent.putExtra("type0", getIntent()
									.getStringExtra("type0"));
							intent.putExtra("time",
									DateUtil.getFormatedDate_1(addtime + ""));
							intent.putExtra("submit_time", DateUtil
									.getFormatedDate_1(submit_time + ""));
							intent.putExtra(
									"examine_time",
									DateUtil.getFormatedDate_1(examine_time
											+ ""));
							intent.putExtra("refuse_time", DateUtil
									.getFormatedDate_1(refuse_time + ""));
							intent.putExtra(
									"success_time",
									DateUtil.getFormatedDate_1(success_time
											+ ""));
							intent.putExtra("type", "online");
							startActivity(intent);
							finish();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (msg.arg1 == 12) {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (msg.arg1 == 13) {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (msg.arg1 == 14) {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (msg.arg1 == 10) {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			case UPLOAD_PIC:
				if ((String) msg.obj != null) {
					// changePic();
					ImgLoad loader = ImgLoad.getInstance();
					String res1 = (String) msg.obj;

					JSONObject jo1 = null;
					try {
						jo1 = new JSONObject(res1);

						String message = jo1.getString("msg");
						String local_img = jo1.getString("local_img");
						String bank_img = jo1.getString("bank_img");
						CURRENT_IMG_PATH1.put(0, local_img);
						UPDATE_IMG_PATH.put(0, bank_img);
						picture1(getIntent().getStringExtra("picPath_bei"));
					} catch (Exception e) {

						try {
							ToastUtil.show(getApplicationContext(),
									jo1.getString("msg"));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

				} else {
				}
				break;

			case UPLOAD_PIC_BEI:
				if ((String) msg.obj != null) {
					// changePic();
					ImgLoad loader = ImgLoad.getInstance();
					String res2 = (String) msg.obj;

					JSONObject jo2 = null;
					try {
						jo2 = new JSONObject(res2);

						String message = jo2.getString("msg");
						String local_img = jo2.getString("local_img");
						String bank_img = jo2.getString("bank_img");
						CURRENT_IMG_PATH1.put(1, local_img);
						UPDATE_IMG_PATH.put(1, bank_img);
						picture2(getIntent().getStringExtra("picPath_shou"));
					} catch (Exception e) {
						try {
							ToastUtil.show(getApplicationContext(),
									jo2.getString("msg"));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				} else {
				}
				break;

			case UPLOAD_PIC_SHOU:
				if ((String) msg.obj != null) {
					// changePic();
					ImgLoad loader = ImgLoad.getInstance();
					String res3 = (String) msg.obj;

					JSONObject jo3 = null;
					try {
						jo3 = new JSONObject(res3);
						String message = jo3.getString("msg");
						String local_img = jo3.getString("local_img");
						String bank_img = jo3.getString("bank_img");
						CURRENT_IMG_PATH1.put(2, local_img);
						UPDATE_IMG_PATH.put(2, bank_img);
						send();
					} catch (Exception e) {
						try {
							ToastUtil.show(getApplicationContext(),
									jo3.getString("msg"));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				} else {
				}
				break;
			}
		};
	};
	private int autoFinance = 0;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fenghuang_bao);
		ViewUtils.inject(this);
		dialog = new ProgressDialog(this);
		title_font.setText("凤凰宝介绍");
		rl1.setOnClickListener(this);
		iv_duihao.setOnClickListener(this);
		tv_xieyi.setOnClickListener(this);
		btn_refuse.setOnClickListener(this);
		btn_use.setOnClickListener(this);

		LoginManager lm = LoginManager
				.getInstance(OnlineFenghuangbaoActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
			phone = login.getUserName();
		}

	}

	protected void send() {
		// TODO Auto-generated method stub
		if (!NetConn.checkNetwork(OnlineFenghuangbaoActivity.this)) {
		} else {
			dialog.setMessage("正在上传信息...");
			dialog.setCancelable(true);
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							OnlineFenghuangbaoActivity.this);
					Message message = handler.obtainMessage(SUBMIT_ALL);
					try {

						String res = service.getAllSubimt(shopsid, getIntent()
								.getStringExtra("tv_phone"), getIntent()
								.getStringExtra("et_email"), getIntent()
								.getStringExtra("et_tuijian"), getIntent()
								.getStringExtra("tv_online_name"), getIntent()
								.getStringExtra("tv_shengfenzhenghao"),
								CURRENT_IMG_PATH1.get(0), CURRENT_IMG_PATH1
										.get(1), CURRENT_IMG_PATH1.get(2),
								UPDATE_IMG_PATH.get(0), UPDATE_IMG_PATH.get(1),
								UPDATE_IMG_PATH.get(2), autoFinance);

						JSONObject jo = null;
						try {
							jo = new JSONObject(res);
							int flag = jo.optInt("res");
							String error = jo.getString("msg");
							int addtime = jo.optInt("addtime");
							message.arg1 = flag;
							message.arg2 = addtime;
							message.obj = res;
							handler.sendMessage(message);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
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
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl1:
			intent = new Intent(OnlineFenghuangbaoActivity.this,
					ReadAndAgreeEAccountActivity.class);
			intent.putExtra("tag", 4);
			startActivity(intent);
			break;
		case R.id.btn_refuse:
			// 残忍拒绝
			submit(2);
			break;
		case R.id.btn_use:
			// 马上使用
			submit(1);
			break;
		case R.id.iv_duihao:
			// 点击对勾
			if (dui) {
				iv_duihao.setImageResource(R.drawable.online_no);
				dui = false;
			} else {
				iv_duihao.setImageResource(R.drawable.online_yes);
				dui = true;
			}
			break;
		case R.id.tv_xieyi:
			intent = new Intent(OnlineFenghuangbaoActivity.this,
					ReadAndAgreeEAccountActivity.class);
			intent.putExtra("tag", 3);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void submit(int tag) {
		if (tag == 1) {
			// 马上使用
			autoFinance = 1;
			if (!dui) {
				// 没有点击用户协议
				ToastUtil.show(getApplicationContext(), "请勾选“阅读并同意协议”");
				return;
			}
		} else if (tag == 2) {
			// 拒绝
			autoFinance = 0;
		}

		picture(getIntent().getStringExtra("picPath"));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == ONLINE_CHECK) {
			Intent intent = new Intent();
			OnlineFenghuangbaoActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
	}

	public void picture(final String picPath1) {

		if (!NetConn.checkNetwork(OnlineFenghuangbaoActivity.this)) {
		} else {
			if (!"".equals(picPath1) && picPath1 != null) {
				dialog.setMessage("正在上传身份证照片...");
				dialog.setCancelable(true);
				dialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("shopsid", shopsid + "");
						params.put("phone", phone + "");
						// 图片类型
						params.put("imgtype", "IDIMG");
						params.put("planNo", "android");
						String res = UpLoadFile.uploadImage(
								Constant.UPDATA_ID_CARD, picPath1, params);
						Message message = handler.obtainMessage(UPLOAD_PIC);
						message.obj = res;
						handler.sendMessage(message);
					}
				}).start();
			}
		}

	}

	public void picture1(final String picPath1) {

		if (!NetConn.checkNetwork(OnlineFenghuangbaoActivity.this)) {
		} else {
			if (!"".equals(picPath1) && picPath1 != null) {
				// dialog.setMessage("正在上传身份证背面照片...");
				// dialog.setCancelable(true);
				// dialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("shopsid", shopsid + "");
						params.put("phone", phone + "");
						// 图片类型
						params.put("imgtype", "IDIMG");
						params.put("planNo", "android");
						String res = UpLoadFile.uploadImage(
								Constant.UPDATA_ID_CARD, picPath1, params);
						Message message = handler.obtainMessage(UPLOAD_PIC_BEI);
						message.obj = res;
						handler.sendMessage(message);
					}
				}).start();
			}
		}
	}

	public void picture2(final String picPath1) {

		if (!NetConn.checkNetwork(OnlineFenghuangbaoActivity.this)) {
		} else {
			if (!"".equals(picPath1) && picPath1 != null) {
				// dialog.setMessage("正在上传身份证手持照片...");
				// dialog.setCancelable(true);
				// dialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("shopsid", shopsid + "");
						params.put("phone", phone + "");
						// 图片类型
						params.put("imgtype", "IDIMG");
						params.put("planNo", "android");
						String res = UpLoadFile.uploadImage(
								Constant.UPDATA_ID_CARD, picPath1, params);
						Message message = handler
								.obtainMessage(UPLOAD_PIC_SHOU);
						message.obj = res;
						handler.sendMessage(message);
					}
				}).start();
			}
		}

	}

}
