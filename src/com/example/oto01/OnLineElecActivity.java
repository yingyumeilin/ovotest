package com.example.oto01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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

/**
 * 在线收银台
 * 
 * @author Administrator
 * 
 */
public class OnLineElecActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.title_font)
	private TextView title_font;
	@ViewInject(R.id.iv_duihao)
	private ImageView iv_duihao;
	@ViewInject(R.id.tv_online_name)
	private EditText tv_online_name;
	@ViewInject(R.id.tv_shengfenzhenghao)
	private EditText tv_shengfenzhenghao;
	@ViewInject(R.id.et_email)
	private EditText et_email;
	@ViewInject(R.id.et_tuijian)
	private EditText et_tuijian;
	// @ViewInject(R.id.iv_identificationcard_zhengmian)
	// private ImageView iv_identificationcard_zhengmian;
	// @ViewInject(R.id.iv_identificationcard_beimian)
	// private ImageView iv_identificationcard_beimian;
	// @ViewInject(R.id.iv_identificationcard_shouchi)
	// private ImageView iv_identificationcard_shouchi;
	@ViewInject(R.id.tv_xieyi)
	private TextView tv_xieyi;
	@ViewInject(R.id.tv_phone)
	private EditText tv_phone;
	@ViewInject(R.id.rl_identificationcard_zhengmian)
	private RelativeLayout rl_identificationcard_zhengmian;
	@ViewInject(R.id.rl_identificationcard_beimian)
	private RelativeLayout rl_identificationcard_beimian;
	@ViewInject(R.id.rl_identificationcard_shouchi)
	private RelativeLayout rl_identificationcard_shouchi;
	@ViewInject(R.id.tv_tixing)
	private TextView tv_tixing;
	@ViewInject(R.id.tv_tixing1)
	private TextView tv_tixing1;
	@ViewInject(R.id.tv_tixing4)
	private TextView tv_tixing4;
	@ViewInject(R.id.tv_tixingyou)
	private TextView tv_tixingyou;

	protected static final int TO_SELECT_PHOTO = 1;
	protected static final int TO_SELECT_PHOTO_BEI = 2;
	protected static final int TO_SELECT_PHOTO_SHOU = 3;
	private static final int ONLINE_CHECK = 107;
	private static final int ONLINE_FENGHUANGBAO = 108;
	private static final int SUBMIT_ALL = 101;
	protected static final int UPDATE_PIC = 5;
	private Map<Integer, String> CURRENT_IMG_PATH = new HashMap<Integer, String>();
	private Map<Integer, String> CURRENT_IMG_PATH1 = new HashMap<Integer, String>();
	private Map<Integer, String> UPDATE_IMG_PATH = new HashMap<Integer, String>();
	private Boolean dui = true;
	private String newPicName = "";
	protected static final int UPLOAD_PIC = 201;
	protected static final int UPLOAD_PIC_BEI = 202;
	protected static final int UPLOAD_PIC_SHOU = 203;

	private String picturename = "";
	private String picPath = "";
	private String picPath_bei = "";
	private String picPath_shou = "";
	private ProgressDialog dialog;
	private int shopsid;
	private String phone;
	private int y = 0;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_PIC:
				if ((String) msg.obj != null) {
					// changePic();
					ImgLoad loader = ImgLoad.getInstance();
					String res = (String) msg.obj;

					JSONObject jo = null;
					try {
						jo = new JSONObject(res);

						String message = jo.getString("msg");
						String local_img = jo.getString("local_img");
						String bank_img = jo.getString("bank_img");
						System.out.println("-----picPath------->" + picPath);
						CURRENT_IMG_PATH1.put(0, local_img);
						UPDATE_IMG_PATH.put(0, bank_img);
						picture1(picPath_bei);
					} catch (Exception e) {

						try {
							ToastUtil.show(getApplicationContext(),
									jo.getString("msg"));
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
					String res = (String) msg.obj;

					JSONObject jo = null;
					try {
						jo = new JSONObject(res);

						String message = jo.getString("msg");
						String local_img = jo.getString("local_img");
						String bank_img = jo.getString("bank_img");
						CURRENT_IMG_PATH1.put(1, local_img);
						UPDATE_IMG_PATH.put(1, bank_img);
						picture2(picPath_shou);
					} catch (Exception e) {
						try {
							ToastUtil.show(getApplicationContext(),
									jo.getString("msg"));
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
					String res = (String) msg.obj;

					JSONObject jo = null;
					try {
						jo = new JSONObject(res);
						String message = jo.getString("msg");
						String local_img = jo.getString("local_img");
						String bank_img = jo.getString("bank_img");
						CURRENT_IMG_PATH1.put(2, local_img);
						UPDATE_IMG_PATH.put(2, bank_img);
						send();
					} catch (Exception e) {
						try {
							ToastUtil.show(getApplicationContext(),
									jo.getString("msg"));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				} else {
				}
				break;
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
						tv_tixing.setVisibility(View.VISIBLE);
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (msg.arg1 == 13) {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						tv_tixing1.setVisibility(View.VISIBLE);
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (msg.arg1 == 14) {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						tv_tixing4.setVisibility(View.VISIBLE);
						ToastUtil.show(getApplicationContext(), error + "");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (msg.arg1 == 10) {
					try {
						jo = new JSONObject(res);
						String error = jo.getString("msg");
						tv_tixingyou.setVisibility(View.VISIBLE);
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
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_elec);
		ViewUtils.inject(this);
		LoginManager lm = LoginManager.getInstance(OnLineElecActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
			phone = login.getUserName();
		}

		dialog = new ProgressDialog(this);
		title_font.setText("在线收银台");
		iv_duihao.setOnClickListener(this);
		rl_identificationcard_zhengmian.setOnClickListener(this);
		rl_identificationcard_beimian.setOnClickListener(this);
		rl_identificationcard_shouchi.setOnClickListener(this);
		tv_online_name.setText(getIntent().getStringExtra("userIdName"));
		tv_shengfenzhenghao.setText(getIntent().getStringExtra("userIdNo"));
		et_email.setText(getIntent().getStringExtra("Email"));
		et_tuijian.setText(getIntent().getStringExtra("ReferrerCode"));
		try {
			if (getIntent().getStringExtra("Mobile") != null) {
				tv_phone.setText(getIntent().getStringExtra("Mobile") + "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			// tv_phone.setText(phone);
		}

		picIvs = new ImageView[3];
		picIvs[0] = (ImageView) findViewById(R.id.iv_identificationcard_zhengmian);
		picIvs[1] = (ImageView) findViewById(R.id.iv_identificationcard_beimian);
		picIvs[2] = (ImageView) findViewById(R.id.iv_identificationcard_shouchi);
		getScreenSize();
	}

	/**
	 * 获取屏幕的宽度和高度
	 */
	private void getScreenSize() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	protected void send() {
		// dialog.dismiss();
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(),
				OnlineFenghuangbaoActivity.class);
		intent.putExtra("type0", getIntent().getStringExtra("type0"));
		intent.putExtra("tv_phone", tv_phone.getText().toString().trim());
		intent.putExtra("et_email", et_email.getText().toString().trim());
		intent.putExtra("et_tuijian", et_tuijian.getText().toString().trim());
		intent.putExtra("tv_online_name", tv_online_name.getText().toString()
				.trim());
		intent.putExtra("tv_shengfenzhenghao", tv_shengfenzhenghao.getText()
				.toString().trim());
		intent.putExtra("picPath", picPath);
		intent.putExtra("picPath_bei", picPath_bei);
		intent.putExtra("picPath_shou", picPath_shou);
		// intent.putExtra("CURRENT_IMG_PATH0", CURRENT_IMG_PATH1.get(0));
		// intent.putExtra("CURRENT_IMG_PATH1", CURRENT_IMG_PATH1.get(1));
		// intent.putExtra("CURRENT_IMG_PATH2", CURRENT_IMG_PATH1.get(2));
		// intent.putExtra("UPDATE_IMG_PATH0", UPDATE_IMG_PATH.get(0));
		// intent.putExtra("UPDATE_IMG_PATH1", UPDATE_IMG_PATH.get(1));
		// intent.putExtra("UPDATE_IMG_PATH2", UPDATE_IMG_PATH.get(2));
		startActivityForResult(intent, ONLINE_FENGHUANGBAO);

	}

	/** 照片文件的名称 */
	private static final String IMAGE_FILE_NAME = "b.jpg";
	private TextView[] picTvs; // 照片的标签
	private int screenWidth; // 屏幕宽度（使用缩放图片）
	private int screenHeight; // 屏幕高度（使用缩放图片）
	private String imagePath; // 照片的路径
	private String[] paths = new String[3];
	private int mIndex;
	private ImageView[] picIvs; // 填充照片

	protected void addImageView(Intent data) {

		File file = new File(data.getStringExtra(Constant.KEY_PHOTO_PATH));
		if (file != null) {

			Bitmap bitmap = scalePictureForSave(file.getPath());
			// deleteTempPicture(file.getPath());
			String pathFile = savaPhotoToLocal(bitmap);
			imagePath = pathFile;
			paths[mIndex] = pathFile;
			picIvs[mIndex].setImageBitmap(imageCropThumbnailToDisplay(mIndex,
					pathFile)); // 显示缩略图

		}

	}

	/**
	 * 将产生的图片保存到本地
	 * 
	 * @param btp
	 * @return
	 */
	public String savaPhotoToLocal(Bitmap btp) {

		File rootdir = Environment.getExternalStorageDirectory();
		String imagerDir = rootdir.getPath() + File.separator + "yobee"
				+ File.separator;
		File dir = new File(imagerDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File tempFile = new File(imagerDir, IMAGE_FILE_NAME);
		try {
			FileOutputStream fileOut = new FileOutputStream(tempFile);
			btp.compress(CompressFormat.JPEG, 50, fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return tempFile.getPath();
	}

	/**
	 * 删除临时的拍照文件
	 * 
	 * @param path
	 * @return
	 */
	private boolean deleteTempPicture(String path) {
		File file = new File(path);
		return file.delete();
	}

	/**
	 * 缩放图片进行本地保存
	 * 
	 * @return
	 */
	public Bitmap scalePictureForSave(String path) {

		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int height = opts.outHeight;
		int width = opts.outWidth;

		// 获取宽度的比例
		int scaleWidth = width / screenWidth;
		int scaleHeight = height / screenHeight;

		// 比较比例使用大的比例进行缩放
		int scale = scaleHeight > scaleWidth ? scaleHeight : scaleWidth;
		opts.inSampleSize = scale;
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
		return bitmap;
	}

	/**
	 * 按正方形显示缩略图
	 */
	public Bitmap imageCropThumbnailToDisplay(int index, String path) {

		Bitmap bitmap = BitmapFactory.decodeFile(path);

		int ivWidth = picIvs[index].getMeasuredWidth();
		int ivHeight = picIvs[index].getMeasuredHeight();

		// 创建缩略图
		Bitmap thumBnail = ThumbnailUtils.extractThumbnail(bitmap, ivWidth,
				ivHeight);
		return bitmap;
	}

	/**
	 * 阅读并同意
	 * 
	 * @param view
	 */
	public void read_onClick(View view) {
		Intent intent = new Intent(OnLineElecActivity.this,
				ReadAndAgreeEAccountActivity.class);
		intent.putExtra("tag", 2);
		startActivity(intent);
	}

	protected void changePic() {

		if (!NetConn.checkNetwork(OnLineElecActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							OnLineElecActivity.this);
					Message message = handler.obtainMessage(UPDATE_PIC);
					handler.sendMessage(message);
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
		OnLineElecActivity.this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			OnLineElecActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 提交
	 * 
	 * @param view
	 */
	public void send_onClick(View view) {

		if (!dui) {
			ToastUtil.show(getApplicationContext(), "请您同意凤凰e账户用户协议");
			return;
		}
		
		if (tv_online_name.getText().toString().trim().length()==0) {
			ToastUtil.show(getApplicationContext(), "请填写店主姓名");
			return;
		}
		if (tv_shengfenzhenghao.getText().toString().trim().length()==0) {
			ToastUtil.show(getApplicationContext(), "请填写身份证号");
			return;
		}
		
		if (tv_phone.getText().toString().trim().length()==0) {
			ToastUtil.show(getApplicationContext(), "请填写手机号");
			return;
		}

		if (et_email.getText().toString().trim().equals("")) {
			ToastUtil.show(getApplicationContext(), "请填写邮箱");
			return;
		}
		if (picPath.equals("")) {
			ToastUtil.show(getApplicationContext(), "请拍身份证正面照");
			return;
		}
		if (picPath_bei.equals("")) {
			ToastUtil.show(getApplicationContext(), "请拍身份证背面照");
			return;
		}
		if (picPath_shou.equals("")) {
			ToastUtil.show(getApplicationContext(), "请拍身份证手持照");
			return;
		}
		
		
		send();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			// 点击进入协议页面
			break;
		case R.id.rl_identificationcard_zhengmian:
			Intent in = new Intent(this, Fenbinglv300.class);
			in.putExtra("screenWidth", screenWidth);
			in.putExtra("screenHeight", screenHeight);
			mIndex = 0;
			startActivityForResult(in, TO_SELECT_PHOTO);
			break;
		case R.id.rl_identificationcard_beimian:
			Intent in1 = new Intent(this, Fenbinglv300.class);
			in1.putExtra("screenWidth", screenWidth);
			in1.putExtra("screenHeight", screenHeight);
			mIndex = 1;
			startActivityForResult(in1, TO_SELECT_PHOTO_BEI);
			break;
		case R.id.rl_identificationcard_shouchi:
			Intent in2 = new Intent(this, Fenbinglv300.class);
			in2.putExtra("screenWidth", screenWidth);
			in2.putExtra("screenHeight", screenHeight);
			mIndex = 2;
			startActivityForResult(in2, TO_SELECT_PHOTO_SHOU);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picturename = "zheng";
			CURRENT_IMG_PATH.put(0,
					data.getStringExtra(Constant.KEY_PHOTO_PATH));
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);

			File file = new File(picPath);
			if (file.exists()) {
				addImageView(data);
			} else {
				// ToastUtil.show(RegisterStepTwoActivity.this,
				// "该图片不存在");
			}
			// picture(data.getStringExtra(Constant.KEY_PHOTO_PATH));
		} else if (resultCode == RESULT_OK
				&& requestCode == TO_SELECT_PHOTO_BEI) {
			picturename = "fan";
			CURRENT_IMG_PATH.put(1,
					data.getStringExtra(Constant.KEY_PHOTO_PATH));
			picPath_bei = data.getStringExtra(Constant.KEY_PHOTO_PATH);

			File file = new File(picPath_bei);
			if (file.exists()) {
				addImageView(data);
			} else {
				// ToastUtil.show(RegisterStepTwoActivity.this,
				// "该图片不存在");
			}
			// picture(data.getStringExtra(Constant.KEY_PHOTO_PATH));
		} else if (resultCode == RESULT_OK
				&& requestCode == TO_SELECT_PHOTO_SHOU) {

			picturename = "shou";
			CURRENT_IMG_PATH.put(2,
					data.getStringExtra(Constant.KEY_PHOTO_PATH));
			picPath_shou = data.getStringExtra(Constant.KEY_PHOTO_PATH);

			File file = new File(picPath_shou);
			if (file.exists()) {
				addImageView(data);
			} else {
				// ToastUtil.show(RegisterStepTwoActivity.this,
				// "该图片不存在");
			}
			// picture(data.getStringExtra(Constant.KEY_PHOTO_PATH));
		}
		if (resultCode == RESULT_OK && requestCode == ONLINE_CHECK) {
			Intent intent = new Intent();
			OnLineElecActivity.this.setResult(RESULT_OK, intent);
			finish();
		}

		if (resultCode == RESULT_OK && requestCode == ONLINE_FENGHUANGBAO) {
			Intent intent = new Intent();
			OnLineElecActivity.this.setResult(RESULT_OK, intent);
			finish();

		}

	}

	public void picture(final String picPath1) {

		if (!NetConn.checkNetwork(OnLineElecActivity.this)) {
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

		if (!NetConn.checkNetwork(OnLineElecActivity.this)) {
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

		if (!NetConn.checkNetwork(OnLineElecActivity.this)) {
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
