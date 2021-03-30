package com.example.oto01;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.model.Constant;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.services.RegisterService;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.UpLoadFile;

public class RegisterStepThreeActivity extends BaseActivity {
	private ImageView shopLicenseImageView, backImageView;
	private ProgressDialog proDialog;
	private int shopsId = 1;
	private String shopName;
	private String picPath = "";// 图片位置
	private static final int UPLOAD_SHOP_LICENSE_DATA = 1;
	private static final int STEP_THREE = 2;
	protected static final int TO_SELECT_PHOTO = 3;
	protected static final int UPDATE_LICENSE = 4;
	private static final int GET_SHOP_TEXT_INFO = 5;
	private int tag;
	private String license;
	private Dialog dialog;
	private int screenWidth;
	private int screenHeight;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_SHOP_LICENSE_DATA:
				if (msg.obj != null) {
					final String license = (String) msg.obj;
					System.out.println("-------zuihou--license---->" + license);
					if (tag != -1) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								SettingService service = new SettingService(
										RegisterStepThreeActivity.this);
								int flag = service.updateShopInfo(shopsId,
										license);
								Message message = handler
										.obtainMessage(UPDATE_LICENSE);
								message.arg1 = flag;
								message.obj = service.errorMsg;
								handler.sendMessage(message);
							}
						}).start();
					} else {

						new Thread(new Runnable() {
							@Override
							public void run() {
								RegisterService registerService = new RegisterService(
										RegisterStepThreeActivity.this);
								// FIXME 完成定位
								boolean flag = registerService.uploadLicense(
										shopsId, license);
								Message message = handler
										.obtainMessage(STEP_THREE);
								message.arg1 = flag == true ? 0 : 1;
								handler.sendMessage(message);
							}
						}).start();
					}
				}
				break;
			case STEP_THREE:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(RegisterStepThreeActivity.this, "注册成功,进入首页");
					Intent intent = new Intent(RegisterStepThreeActivity.this,
							SelectOpenElectronicActivity.class);
					intent.putExtra("shopsName", shopName);
					startActivity(intent);
					finish();
				} else {
					ToastUtil.show(RegisterStepThreeActivity.this,
							"上传证件失败，请重新上传");
				}
				break;
			case UPDATE_LICENSE:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(RegisterStepThreeActivity.this, "修改成功");
					finish();
				} else {
					ToastUtil
							.show(RegisterStepThreeActivity.this, msg.obj + "");
					if (license != null && license.length() > 0) {
						ImgLoad loader = ImgLoad.getInstance();
						shopLicenseImageView.setTag(license);
						loader.addTask(license, shopLicenseImageView);
						loader.doTask();
					} else {
						shopLicenseImageView
								.setImageResource(R.drawable.shop_licences);
					}

				}
				break;
			case GET_SHOP_TEXT_INFO:
				dialog.dismiss();
				ShopInfo shop = (ShopInfo) msg.obj;
				if (shop != null) {
					license = shop.getLicense();
				}
				// FIXME 下载当前的证件
				backImageView.setVisibility(View.VISIBLE);
				ImgLoad loader = ImgLoad.getInstance();
				System.out.println("-----license------->" + license);
				if (license != null && license.length() > 0) {
					shopLicenseImageView.setTag(license);
					loader.addTask(license, shopLicenseImageView);
					loader.doTask();
				} else {
					shopLicenseImageView
							.setImageResource(R.drawable.shop_licences);
				}
				break;
			}

		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_3);
		proDialog = new ProgressDialog(this);
		shopName = getIntent().getStringExtra("shopsName");
		shopsId = getIntent().getIntExtra("shopsId", -1);
		tag = getIntent().getIntExtra("tag", -1);
		shopLicenseImageView = (ImageView) findViewById(R.id.shop_license_img);
		backImageView = (ImageView) findViewById(R.id.back);

		if (tag != -1) {
			dialog = new Dialog(this, R.style.theme_dialog_alert);
			dialog.setContentView(R.layout.window_layout);
			dialog.setCancelable(false);
			dialog.show();
			getShopLicense();
		}

		getScreenSize();
	}

	private void getScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	private void getShopLicense() {
		if (!NetConn.checkNetwork(RegisterStepThreeActivity.this)) {
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					SettingService service = new SettingService(
							RegisterStepThreeActivity.this);
					ShopInfo shopInfo = service.checkShopInfo2(shopsId);
					Message message = handler.obtainMessage(GET_SHOP_TEXT_INFO);
					message.obj = shopInfo;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			// Bundle bundle = data.getBundleExtra("bundle");
			// System.out.println("------picPath---2->"+bundle);
			// bitmap = bundle.getParcelable("data");
			// Bitmap bm = PictureUtil.getSmallBitmap(picPath);
			// int degree = PictureUtil.readPictureDegree(picPath);
			// if (degree != 0) {// 旋转照片角度
			// bm = PictureUtil.rotateBitmap(bm, degree);
			// }
			// shopLicenseImageView.setImageBitmap(bm);
			// shopLicenseImageView.setImageBitmap(PictureUtil.getSmallBitmap(picPath));
			// File file = new File(picPath);
			// if (file.exists()) {
			addImageView();
			// } else {
			// // ToastUtil.show(RegisterActivity.this, "该图片不存在");
			// }

		}

	}

	/**
	 * 打开相机
	 * 
	 * @param view
	 */
	public void open_camera(View view) {
		// Intent in = new Intent(this, SelectPicActivity.class);
		Intent in = new Intent(this, Fenbinglv300.class);
		in.putExtra("screenWidth", screenWidth);
		in.putExtra("screenHeight", screenHeight);
		startActivityForResult(in, TO_SELECT_PHOTO);
	}

	/**
	 * 添加图片视图
	 */
	private void addImageView() {
		if (picPath != "" && picPath != null
				&& picPath.toString().trim().length() > 0) {
			System.out.println("----old--------->" + picPath);
			// ImgLoad loader = ImgLoad.getInstance();
			Bitmap bmp = ImageUtil.getBitmap(picPath, 4);
			Bitmap m = ImageUtil.changeDirectionBitmap(bmp, picPath, true);
			Bitmap bit = ImageUtil.loadBitmap(ImageUtil.getimage(picPath),
					picPath, true);
			ImageUtil.saveBitmap2file(bit,
					Environment.getExternalStorageDirectory()
							+ "/EServiceStore/uplad_image.jpg");
			shopLicenseImageView.setImageBitmap(bit);
			System.out.println("----new--------->" + picPath);
			// loader.doTask();
			System.out.println("------结束-->");
		} else {
			shopLicenseImageView.setImageResource(R.drawable.shop_img);
		}

	}

	/**
	 * 暂不上传
	 * 
	 * @param view
	 */
	public void no_upload(View view) {
		if (tag != -1) {
			finish();
		} else {
			Intent intent = new Intent(RegisterStepThreeActivity.this,
					SelectOpenElectronicActivity.class);
			intent.putExtra("shopsName", shopName);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 上传
	 * 
	 * @param view
	 */
	public void upload(View view) {

		if (picPath.toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepThreeActivity.this, "请上传店铺证件图片");
			return;
		}
		if (!NetConn.checkNetwork(RegisterStepThreeActivity.this)) {
		} else {
			proDialog.setMessage("正在上传店铺证件照片.....");
			proDialog.setCancelable(false);
			proDialog.show();
			// FIXME 验证第三步：上传或者不上传证件照片
			new Thread(new Runnable() {

				@Override
				public void run() {
					Map<String, String> map = new HashMap<String, String>();
					map.put("shopsid", shopsId + "");
					map.put("imgtype", "License");
					String string = UpLoadFile.uploadImage(
							Constant.uploadImgURL, picPath, map);
					Message message = handler
							.obtainMessage(UPLOAD_SHOP_LICENSE_DATA);
					message.obj = string;
					handler.sendMessage(message);
				}
			}).start();
		}
	}
}
