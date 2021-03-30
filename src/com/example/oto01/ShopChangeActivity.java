package com.example.oto01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.model.CityName;
import com.example.oto01.model.Constant;
import com.example.oto01.model.Login;
import com.example.oto01.model.ShopType;
import com.example.oto01.model.ShopsInfo;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.RegisterService;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.ActivityManager;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.UpLoadFile;
import com.example.oto01.wheelDialog.WheelDialogManager;
import com.example.oto01.wheelDialog.WheelDialogManager.OnWheelItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ShopChangeActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.title_font)
	private TextView title_font;

	/**
	 * 店铺名称
	 */
	@ViewInject(R.id.shop_name)
	private EditText shop_name;
	/**
	 * 店主名称
	 */
	@ViewInject(R.id.real_name)
	private EditText real_name;
	/**
	 * 身份证号
	 */
	@ViewInject(R.id.id_card_no)
	private EditText id_card_no;
	/**
	 * 电话
	 */
	@ViewInject(R.id.et_phone)
	private EditText et_phone;

	/**
	 * 位置
	 */
	@ViewInject(R.id.location)
	private EditText location;

	/**
	 * 详细地址
	 */
	@ViewInject(R.id.city_cata_spinner)
	private EditText city_cata_spinner;

	/**
	 * 店铺类型
	 */
	@ViewInject(R.id.shop_cata_spinner)
	private TextView shop_cata_spinner;

	/**
	 * 营业执照号
	 */
	@ViewInject(R.id.id_license_num)
	private EditText id_license_num;

	@ViewInject(R.id.rl_one)
	private RelativeLayout rl_one;

	@ViewInject(R.id.shop_license_img)
	private ImageView shop_license_img;

	@ViewInject(R.id.next)
	private Button next;

	@ViewInject(R.id.shopImg)
	private ImageView shopImg;
	private int shopsid;
	private int screenWidth;
	private int screenHeight;
	protected static final int GET_SHOP_TEXT_INFO = 6;
	protected static final int TO_SELECT_PHOTO = 1;
	protected static final int TO_SELECT_LICENCE = 2;
	protected static final int UPLOAD_PIC = 3;
	protected static final int UPDATE_PIC = 5;
	private static final int UPLOAD_SHOP_LICENSE_DATA = 4;
	private static final int UPDATE_LICENSE = 7;
	private static final int SHOPTYPE_DATA = 8;
	protected static final int TO_SELECT_LOCATION = 9;
	protected static final int UPDATE_ALL_MESSAGE = 10;
	private JSONObject jObject;
	private static final int DISTABLE = 105;
	private static final int EXIT_LOGIN = 106;
	private String error;
	private String picPath = "";
	private String picPathLicence = "";
	private Dialog proDialog;
	private String newPicName = "";
	private String oldPicName = "";
	private double lng = 0.0;
	private double lat = 0.0;
	private String phone;
	private String id_card;
	public String card;
	private String typename;
	private ShopsInfo shopInfo;
	private int pictureChange = 0;
	private int mPostion = 0;// 默认位置
	private ArrayList<CityName> citys;
	private WheelDialogManager mWheeManager;// 滚轮
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_SHOP_TEXT_INFO:
				JSONObject jsonObject = (JSONObject) msg.obj;
				shopInfo = new ShopsInfo();
				try {
					if (jsonObject != null) {

						try {
							if (jsonObject.getString("shopsname").length() >= 17) {
								shop_name.setTextSize(11f);
							}

							shop_name.setText(jsonObject.getString("shopsname")
									+ "");
							shopInfo.setShopsname(jsonObject
									.getString("shopsname") + "");
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							phone = jsonObject.getString("phone");
							et_phone.setText(jsonObject.getString("phone") + "");
							shopInfo.setPhone(jsonObject.getString("phone")
									+ "");
						} catch (Exception e) {
						}
						try {
							real_name
									.setText(jsonObject.getString("name") + "");
							shopInfo.setName(jsonObject.getString("name") + "");

						} catch (Exception e) {
						}
						try {
							id_card = jsonObject.getString("identity_number");
							shopInfo.setIdentity_number(jsonObject
									.getString("identity_number"));
						} catch (Exception e) {
						}
						try {
							id_card_no.setText(jsonObject
									.getString("identity_number_star") + "");
							card = jsonObject.getString("identity_number_star");
							shopInfo.setIdentity_number_star(card);
						} catch (Exception e) {
						}
						try {
							location.setText(jsonObject.getString("address")
									+ "");
							shopInfo.setAddress(jsonObject.getString("address")
									+ "");
						} catch (Exception e) {
						}
						try {
							city_cata_spinner.setText(jsonObject
									.getString("detail_address") + "");
							shopInfo.setDetail_address(jsonObject
									.getString("detail_address") + "");
						} catch (Exception e) {
						}

						try {
							id_license_num.setText(jsonObject
									.getString("license_number") + "");
							shopInfo.setLicense_number(jsonObject
									.getString("license_number") + "");
						} catch (Exception e) {
						}

						try {
							oldPicName = jsonObject.getString("logo");
							if (null != oldPicName && oldPicName.contains(";")) {
								String[] splitImage = oldPicName.split(";");
								ImageLoadTool.disPlay(splitImage[0], shopImg,
										R.drawable.default_good_img);
							} else {
								ImageLoadTool.disPlay(oldPicName, shopImg,
										R.drawable.default_good_img);
							}
						} catch (Exception e) {
						}
						try {
							picPathLicence = jsonObject.getString("license");

							if (null != picPathLicence
									&& picPathLicence.contains(";")) {
								String[] splitImage = picPathLicence.split(";");
								ImageLoadTool.disPlay(splitImage[0],
										shop_license_img,
										R.drawable.shop_licences);
							} else {
								ImageLoadTool.disPlay(picPathLicence,
										shop_license_img,
										R.drawable.shop_licences);
							}
						} catch (Exception e) {
						}
						try {
							typename = jsonObject.getString("typename");
							shopInfo.setTypename(typename);
						} catch (Exception e) {
						}
						try {
							if (jsonObject.getString("audit_state").equals("1")) {
								title_font.setText("开店资质" + "(" + "审核中" + ")");
								next.setText("重新提交");
							} else if (jsonObject.getString("audit_state")
									.equals("3")) {
								next.setText("重新提交");
							} else {
								next.setText("提交变更");
							}
						} catch (Exception e) {
						}
						try {
							lng = jsonObject.getDouble("longitude");
							lat = jsonObject.getDouble("latitude");
							shopInfo.setLongitude(jsonObject
									.getDouble("longitude"));
							shopInfo.setLatitude(jsonObject
									.getDouble("latitude"));
						} catch (Exception e) {
						}

					}
					getShopTypeList();
				} catch (Exception e) {
				}

				break;

			case UPLOAD_PIC:
				proDialog.dismiss();
				pictureChange++;
				if ((String) msg.obj != null) {
					newPicName = (String) msg.obj;
					// changePic();
				}
				break;
			case UPDATE_PIC:

				break;
			case UPLOAD_SHOP_LICENSE_DATA:
				pictureChange++;
				proDialog.dismiss();
				if (msg.obj != null) {
					picPathLicence = (String) msg.obj;

				}
				break;
			case UPDATE_LICENSE:
				break;

			case SHOPTYPE_DATA:
				refeshShopTypeSpinner((List<ShopType>) msg.obj);
				break;
			case EXIT_LOGIN:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ActivityManager.getActivityManager(ShopChangeActivity.this)
						.exit();
				Intent intent = new Intent(ShopChangeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			case DISTABLE:
				// 禁止
				// 进行弹框

				if (msg.arg2 == 0) {
					if (msg.arg1 == 2) {
						// 禁用的状态
						disableUser();
					}
				} else {
					ToastUtil.show(getApplicationContext(), error);
				}

				break;
			case UPDATE_ALL_MESSAGE:
				proDialog.dismiss();
				if (msg.arg1 == 0) {

					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.dialog_shop_change,
							null);
					final Dialog dialog = new Dialog(ShopChangeActivity.this,
							R.style.theme_dialog_alert);
					dialog.setContentView(layout);
					layout.findViewById(R.id.ok).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									dialog.dismiss();
									finish();
								}

							});
					dialog.show();

				} else {
					ToastUtil.show(ShopChangeActivity.this, msg.obj + "");
				}

			}

		}

	};

	private static int type_id = -1;
	private static String type_id1 = "";
	private int type_position;

	/**
	 * 刷新商店类型
	 * 
	 * @param obj
	 */
	@SuppressLint("NewApi")
	private void refeshShopTypeSpinner(List<ShopType> obj) {

		CityName city = null;
		if (obj != null && !obj.isEmpty()) {
			for (int i = 0; i < obj.size(); i++) {
				city = new CityName();
				city.name = obj.get(i).getName();
				city.id = obj.get(i).getId();
				citys.add(city);
				if (typename == null || typename.isEmpty()) {
					mPostion = 0;
					// shop_cata_spinner.setText(typename + "");
				} else {
					if (typename.equals(obj.get(i).getName())) {
						mPostion = i;
						type_id = obj.get(i).getId();
						shop_cata_spinner.setText(typename + "");
					}
				}

			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_change);
		ViewUtils.inject(this);
		title_font.setText("开店资质");
		LoginManager lm = LoginManager.getInstance(ShopChangeActivity.this);
		Login login = lm.getLoginInstance();
		shopsid = login.getAdminId();
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.setCancelable(false);
		shop_cata_spinner.setOnClickListener(this);
		citys = new ArrayList<CityName>();
		mWheeManager = WheelDialogManager.getInstanse();
		getScreenSize();
		if (!NetConn.checkNetwork(ShopChangeActivity.this)) {
		} else {
			initData();
			initDisable();
		}
	}

	protected void disableUser() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.disable_user, null);
		final Dialog dialog = new Dialog(ShopChangeActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		dialog.show();
		exitOvo();
	}

	protected void exitOvo() {
		// FIXME 清空登陆信息
		LoginManager lm = LoginManager.getInstance(ShopChangeActivity.this);
		lm.delLogin();
		// proDialog.show();
		System.out.println("-----shopsid---->" + lm.getLoginId());
		if (!NetConn.checkNetwork(ShopChangeActivity.this)) {
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopChangeActivity.this);
					boolean flag = service.exitLogin(shopsid);
					Message message = handler.obtainMessage(EXIT_LOGIN);
					message.arg1 = flag == true ? 0 : 1;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	private void initDisable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SettingService service = new SettingService(
						ShopChangeActivity.this);
				String res = service.getDisableStatus(shopsid);
				Message message = handler.obtainMessage(DISTABLE);
				try {
					jObject = new JSONObject(res);
					try {
						if (jObject.optInt("res") == 0) {
							int flag = jObject.optInt("state");
							message.arg1 = flag;
							message.arg2 = 0;
							handler.sendMessage(message);
						} else {
							error = jObject.getString("msg");
							message.arg2 = 1;
							message.obj = error;
							handler.sendMessage(message);
						}
					} catch (Exception e) {
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * 获取店铺类型列表
	 */
	private void getShopTypeList() {
		if (!NetConn.checkNetwork(ShopChangeActivity.this)) {
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					RegisterService registerService = new RegisterService(
							ShopChangeActivity.this);
					List<ShopType> list = registerService.getShopTypeList();
					Message message = handler.obtainMessage(SHOPTYPE_DATA);
					message.obj = list;
					handler.sendMessage(message);

				}
			}).start();
		}
	}

	private void getScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	private void initData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				SettingService service = new SettingService(
						ShopChangeActivity.this);
				// ShopsInfo shopInfo =
				// service.getShopCheckMessage(shopsid);
				JSONObject res = service.getShopCheckMessage(shopsid);
				Message message = handler.obtainMessage(GET_SHOP_TEXT_INFO);
				message.obj = res;
				handler.sendMessage(message);
			}
		}).start();
	}

	/**
	 * 完成店铺资质的修改 updataShopContent(int shopsid, String phone, String logo,
	 * String shopsname, String name, String identity_number, double longitude,
	 * double latitude, String address, String detail_address, int typeid,
	 * String license, String license_number)
	 * 
	 * @param view
	 */
	public void next_onClick(View view) {
		final String id_card_num;
		final String logo;
		if (id_card_no.getText().toString().trim().equals(card)) {
			id_card_num = id_card;
		} else {
			id_card_num = id_card_no.getText().toString().trim();
		}

		if (newPicName.equals("")) {
			logo = oldPicName;
		} else {
			logo = newPicName;
		}

		if (id_license_num.equals("") || id_license_num.length() == 0) {
			ToastUtil.show(getApplicationContext(), "请填写营业执照号");
			return;
		}
		System.out.println();

		if (shop_name.getText().toString().trim()
				.equals(shopInfo.getShopsname())
				&& real_name.getText().toString().trim()
						.equals(shopInfo.getName())
				&& et_phone.getText().toString().trim()
						.equals(shopInfo.getPhone())
				&& location.getText().toString().trim()
						.equals(shopInfo.getAddress())
				&& city_cata_spinner.getText().toString().trim()
						.equals(shopInfo.getDetail_address())
				&& id_license_num.getText().toString().trim()
						.equals(shopInfo.getLicense_number())
				&& id_card_no.getText().toString().trim().equals(card)
				&& pictureChange == 0 && type_id1.equals(typename)) {
			ToastUtil.show(getApplicationContext(), "您没做任何修改！");
			return;
		}

		if (shop_name.getText().toString().trim() != ""
				&& real_name.getText().toString().trim() != ""
				&& et_phone.getText().toString().trim() != ""
				&& location.getText().toString().trim() != ""
				&& city_cata_spinner.getText().toString().trim() != ""
				&& id_license_num.getText().toString().trim() != "") {
			if (!NetConn.checkNetwork(ShopChangeActivity.this)) {
			} else {
				proDialog.show();
				new Thread(new Runnable() {
					//
					@Override
					public void run() {
						SettingService service = new SettingService(
								ShopChangeActivity.this);
						String res = service.updataShopContent(shopsid, phone,
								logo, shop_name.getText().toString().trim(),
								real_name.getText().toString().trim(),
								id_card_num, lng, lat, location.getText()
										.toString().trim(), city_cata_spinner
										.getText().toString().trim(), type_id,
								picPathLicence, id_license_num.getText()
										.toString().trim());
						Message message = handler
								.obtainMessage(UPDATE_ALL_MESSAGE);
						try {
							JSONObject jo = new JSONObject(res);
							String msg = jo.getString("msg");
							message.arg1 = jo.getInt("res");
							message.obj = msg;
							handler.sendMessage(message);
						} catch (JSONException e) {
							e.printStackTrace();
							message.obj = "";
							handler.sendMessage(message);
						}

					}
				}).start();
			}
		} else {
			ToastUtil.show(getApplicationContext(), "请补充完整信息");
		}

	}

	/**
	 * 修改店铺照片
	 */
	public void change_onClick(View view) {
		Intent in = new Intent(this, SelectPic2Activity.class);
		in.putExtra("screenWidth", screenWidth);
		in.putExtra("screenHeight", screenHeight);
		startActivityForResult(in, TO_SELECT_PHOTO);
	}

	/**
	 * 修改营业执照
	 */
	public void open_camera_license(View view) {
		Intent in = new Intent(this, Fenbinglv300.class);
		in.putExtra("screenWidth", screenWidth);
		in.putExtra("screenHeight", screenHeight);
		startActivityForResult(in, TO_SELECT_LICENCE);
	}

	/**
	 * 打开地图
	 * 
	 * @param view
	 */
	public void mapOnclick(View view) {
		Intent intent = new Intent(ShopChangeActivity.this,
				MyLocationActivity.class);
		startActivityForResult(intent, TO_SELECT_LOCATION);
	}

	// @ViewInject(R.id.shop_license_img)
	// private ImageView shop_license_img;
	//
	// @ViewInject(R.id.shopImg)
	// private ImageView shopImg;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			// 修改店铺图片
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			if (picPath != "" && picPath != null
					&& picPath.toString().trim().length() > 0) {
				System.out.println("----old--picPath------->" + picPath);
				// ImgLoad loader = ImgLoad.getInstance();
				Bitmap bmp = ImageUtil.getBitmap(picPath, 4);
				Bitmap m = ImageUtil.changeDirectionBitmap(bmp, picPath, true);
				shopImg.setImageBitmap(m);

			} else {
				shopImg.setImageResource(R.drawable.default_good_img);
			}
			if (!NetConn.checkNetwork(ShopChangeActivity.this)) {
			} else {
				if (!"".equals(picPath) && picPath != null) {

					proDialog.show();
					new Thread(new Runnable() {

						@Override
						public void run() {
							Map<String, String> params = new HashMap<String, String>();
							params.put("shopsid", shopsid + "");
							params.put("imgtype", "Logo");
							String newPicPath = UpLoadFile.uploadImage(
									Constant.uploadImgURL, picPath, params);
							Message message = handler.obtainMessage(UPLOAD_PIC);
							message.obj = newPicPath;
							handler.sendMessage(message);
						}
					}).start();
				}
			}
		} else if (resultCode == RESULT_OK && requestCode == TO_SELECT_LICENCE) {
			// 修改营业执照
			picPathLicence = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			if (picPathLicence != "" && picPathLicence != null
					&& picPathLicence.toString().trim().length() > 0) {
				// ImgLoad loader = ImgLoad.getInstance();
				Bitmap bmp = ImageUtil.getBitmap(picPathLicence, 4);
				Bitmap m = ImageUtil.changeDirectionBitmap(bmp, picPathLicence,
						true);
				shop_license_img.setImageBitmap(m);

			} else {
				shop_license_img.setImageResource(R.drawable.default_good_img);
			}
			if (!NetConn.checkNetwork(ShopChangeActivity.this)) {
			} else {
				if (!"".equals(picPathLicence) && picPathLicence != null) {
					proDialog = new Dialog(this, R.style.theme_dialog_alert);
					proDialog.setContentView(R.layout.window_layout);
					proDialog.setCancelable(false);
					proDialog.show();
					new Thread(new Runnable() {

						@Override
						public void run() {
							Map<String, String> map = new HashMap<String, String>();
							map.put("shopsid", shopsid + "");
							map.put("imgtype", "License");
							String string = UpLoadFile.uploadImage(
									Constant.uploadImgURL, picPathLicence, map);
							Message message = handler
									.obtainMessage(UPLOAD_SHOP_LICENSE_DATA);
							message.obj = string;
							handler.sendMessage(message);
						}
					}).start();
				}
			}
		}

		if (resultCode == RESULT_OK && requestCode == TO_SELECT_LOCATION) {
			Bundle bundle = data.getBundleExtra("bundle");
			lng = bundle.getDouble("LNG");
			lat = bundle.getDouble("LAT");
			location.setText(bundle.getString("address") + "");
			if (lat != 0 && lng != 0) {
				ToastUtil.show(ShopChangeActivity.this, "定位成功！");
				System.out.println("-----lng---" + lng + "-----lat--" + lat);
			} else {
				ToastUtil.show(ShopChangeActivity.this, "定位失败！");
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shop_cata_spinner:
			// 添加分类
			if (citys.size() == 0) {

			} else {
				mWheeManager.createMyItemWheelShop_Type(
						ShopChangeActivity.this, citys, mPostion,
						shop_cata_spinner, new OnWheelItemClickListener() {

							@Override
							public void onWheelItemClicl(int positon) {
								mPostion = positon;
								type_id = citys.get(positon).getId();
							}
						});
			}

			break;

		default:
			break;
		}

	}

}
