package com.example.oto01;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oto01.adapter.NumericWheelAdapter;
import com.example.oto01.model.CityName;
import com.example.oto01.model.Constant;
import com.example.oto01.model.DeviceInfoResponse;
import com.example.oto01.model.LoginReturnMsg;
import com.example.oto01.model.ShopType;
import com.example.oto01.services.CommunityManager;
import com.example.oto01.services.RegisterService;
import com.example.oto01.task.CheckDownAsyncTask;
import com.example.oto01.task.CheckDownAsyncTask.OnCheckDownListener;
import com.example.oto01.utils.Engine;
import com.example.oto01.utils.IDCard;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.TelephoneUtil;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.UpLoadFile;
import com.example.oto01.wheelDialog.WheelDialogManager;
import com.example.oto01.wheelDialog.WheelDialogManager.OnWheelItemClickListener;

public class RegisterStepTwoActivity extends BaseActivity implements
		OnCheckDownListener {
	private EditText citySpinner;
	private int city_position, area_position, shangquan_position;
	private double lng = 0.0;
	private double lat = 0.0;
	private int shopsId;
	private static int city_id = -1;
	private static int area_id = -1;
	private static int shangquan_id = -1;
	private static int type_id = -1;
	private TextView shopTypeSpinner;
	private int type_position;
	private EditText nameEditView, shopNameEditText, shopInfoEditText,
			locationEditText, idNoEditText;
	private LinearLayout ll_dingwei;
	private String name, shopName, shopInfo, location;
	private ImageView shopImageView, shopLicenseImageView;
	private String picPath = "";// 图片位置
	private String picPathLicense = "";// 证件图片位置
	private String logo = "";
	private String license = "";
	private CommunityManager cm;
	private Dialog dialog, newDialog;
	private static final int CITY_DATA = 1;
	private static final int AREA_DATA = 2;
	private static final int SHANGQ_QUAN_DATA = 3;

	private static final int SHOPTYPE_DATA = 4;
	private static final int UPLOAD_SHOP_LOGO_DATA = 5;
	private static final int UPLOAD_SHOP_LICENSE_DATA = 9;
	protected static final int TO_SELECT_PHOTO_SHOP_IMG = 6;
	protected static final int TO_SELECT_PHOTO_SHOP_LICENSE = 10;
	protected static final int TO_SELECT_LOCATION = 7;
	protected static final int STEP_TWO = 8;
	protected static final int REGISTER = 11;
	private TextView tvStartTime, tvEndTime;
	private int fontSize = 24; // 字体大小
	private ArrayList<CityName> citys;
	private int mPostion = 0;// 默认位置

	private CheckDownAsyncTask cdTask;
	// private List<City> currentCity = new ArrayList<City>();
	// private List<City> currentArea = new ArrayList<City>();
	// private List<City> currentShangQuan = new ArrayList<City>();

	private String phone, code, passWord, recommendCode;

	private ProgressDialog proDialog;

	private Map<String, String> map;
	private int screenWidth;
	private int screenHeight;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CITY_DATA:
				// refeshCitySpinner((List<City>) msg.obj);
				break;
			case AREA_DATA:
				// refeshAreaSpinner((List<City>) msg.obj);
				break;
			case SHANGQ_QUAN_DATA:
				// refeshShangQuanSpinner((List<City>) msg.obj);
				break;
			case SHOPTYPE_DATA:
				dialog.dismiss();
				refeshShopTypeSpinner((List<ShopType>) msg.obj);
				break;
			case UPLOAD_SHOP_LOGO_DATA:
				// picPath = "";
				// 在这里 进行 上传的操作
				// FIXME
				if (msg.obj != null) {
					logo = (String) msg.obj;
					System.out.println("-------logo------>" + logo);
				} else {
					logo = "";
				}
				// uploadRegisterText();

				uploadLicense();
				break;
			case UPLOAD_SHOP_LICENSE_DATA:// 上传证件
				if (msg.obj != null) {
					license = (String) msg.obj;
					System.out.println("-------license------>" + license);
				} else {
					license = "";
				}
				// proDialog.dismiss();
				// 上传注册信息
				updateRegisterMessage();

				cm.setLocation(String.valueOf(lng), String.valueOf(lat));
				checkDown();// 获取设备信息
				// Bundle bundle = new Bundle();
				// bundle.putString("phone", phone);
				// bundle.putString("password", passWord);
				// bundle.putString("code", code);
				// bundle.putString("recode", recommendCode);
				// bundle.putString("name", nameEditView.getText().toString()
				// .trim());
				// bundle.putString("shopsname", shopNameEditText.getText()
				// .toString().trim());
				// bundle.putInt("typeid", type_id);
				//
				// // bundle.putInt("shangquan_id", shangquan_id); 已经去掉
				// bundle.putDouble("longitude", lng);
				// bundle.putDouble("latitude", lat);
				// bundle.putString("content", shopInfoEditText.getText()
				// .toString().trim());
				// // locationEditText 现在已经是 详细地址
				// // bundle.putString("location", locationEditText.getText()
				// // .toString().trim());
				// bundle.putString("location", locationEditText.getText()
				// .toString().trim());
				// bundle.putString("detail_address", citySpinner.getText()
				// .toString().trim());
				// bundle.putString("identity_number", idNoEditText.getText()
				// .toString().trim());
				// bundle.putString("logo", logo);
				// bundle.putString("license", license);
				// bundle.putSerializable("info", (Serializable) map);
				// nextIntent.putExtra("data", bundle);
				break;
			case STEP_TWO:
				LoginReturnMsg message = (LoginReturnMsg) msg.obj;
				proDialog.dismiss();
				try {
					if (message.getRes() == 0) {
						Intent nextIntent = new Intent(
								RegisterStepTwoActivity.this,
								SelectOpenElectronicActivity.class);
						startActivity(nextIntent);
						finish();
					} else {
						ToastUtil.show(getApplicationContext(),
								message.getMsg() + "");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				break;
			}
		};
	};

	private boolean checkInfoIsSpace(String key) {
		if (map != null && map.containsKey(key)) {
			System.out.println("------checkInfoIsSpace--key------>" + key);
			System.out.println("------checkInfoIsSpace-value----->"
					+ map.get(key));
			return (map.get(key) == "1" || ("1").equals(map.get(key))) ? true
					: false;
		}
		return false;
	}

	protected void checkDown() {
		// TODO Auto-generated method stub
		if (cdTask != null) {
			cdTask.cancel(true);
			cdTask = null;
		}
		cdTask = new CheckDownAsyncTask(RegisterStepTwoActivity.this);
		cdTask.setOnCheckDownListener(this);
		cdTask.execute();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_2);
		SysApplication.getInstance().addActivity(this);
		Bundle bundle = getIntent().getBundleExtra("data");
		if (bundle != null) {
			phone = bundle.getString("phone");
			passWord = bundle.getString("password");
			code = bundle.getString("code");
			recommendCode = bundle.getString("recode");
			map = (Map<String, String>) bundle.getSerializable("info");
			System.out.println("-----bundle--map------>" + map);
		}

		initView();
		cm = new CommunityManager(this);
		dialog = new Dialog(this, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.window_layout);
		dialog.show();
		newDialog = new Dialog(RegisterStepTwoActivity.this,
				R.style.theme_dialog_alert);
		proDialog = new ProgressDialog(this);
		proDialog.setMessage("正在上传店铺Logo...");
		proDialog.setCancelable(false);
		tvStartTime = (TextView) findViewById(R.id.start_time_textview);
		tvEndTime = (TextView) findViewById(R.id.end_time_textview);
		et_license_num = (EditText) findViewById(R.id.et_license_num);
		tvStartTime.setText("09:30");
		tvEndTime.setText("22:30");
		showInfoDialog();
		initData();
		getScreenSize();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (cdTask != null) {
			cdTask.cancel(true);
			cdTask = null;
		}
	}

	private void showInfoDialog() {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = null;
		layout = (LinearLayout) inflater.inflate(R.layout.info2_dialogview,
				null);
		layout.setMinimumWidth((int) (RegisterStepTwoActivity.this
				.getWindowManager().getDefaultDisplay().getWidth() * 0.7));// 设置dialog的宽度
		newDialog.setContentView(layout);
		newDialog.setCancelable(false);
		newDialog.setCanceledOnTouchOutside(false);
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				newDialog.dismiss();
			}
		});
		newDialog.show();// 显示AlertDialog
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// getCitieList(0, 0);
		getShopTypeList();
	}

	/**
	 * 刷新商店类型
	 * 
	 * @param obj
	 */
	private void refeshShopTypeSpinner(final List<ShopType> obj) {

		CityName city = null;
		for (int i = 0; i < obj.size(); i++) {

			city = new CityName();
			city.name = obj.get(i).getName();
			city.id = obj.get(i).getId();
			citys.add(city);
			// 当前商品的typeid
			shopTypeSpinner.setText(obj.get(0).getName());
			mPostion = 0;
		}
		type_id = obj.get(0).getId();

		// List<String> shoptypeList = new ArrayList<String>();
		// if (obj != null && !obj.isEmpty()) {
		// for (ShopType shopType : obj) {
		// shoptypeList.add(shopType.getName());
		// }
		// }
		// ArrayAdapter<String> shopTypeAdapter = new ArrayAdapter<String>(
		// RegisterStepTwoActivity.this,
		// android.R.layout.simple_spinner_item, shoptypeList);
		// shopTypeAdapter.setDropDownViewResource(R.layout.drop_down_item);
		// shopTypeSpinner.setAdapter(shopTypeAdapter);
		// shopTypeSpinner.setSelection(type_position);
		// if (shoptypeList.size() >= 1) {
		// type_id = obj.get(0).getId();
		// }
		// shopTypeSpinner.setOnItemSelectedListener(new
		// OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parentView, View view,
		// int position, long arg3) {
		// // FIXME 联动地区
		// type_id = obj.get(position).getId();
		// type_position = position;
		// TextView tv = (TextView) view;
		// tv.setTextSize(16);
		// tv.setTextColor(Color.GRAY);
		// tv.setGravity(android.view.Gravity.CENTER_VERTICAL
		// | Gravity.LEFT); // 设置居中
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		//
		// }
		// });
	}

	/**
	 * 打开相机
	 * 
	 * @param view
	 */
	public void open_camera(View view) {
		//  上传 icon
		Intent in = new Intent(this, SelectPic2Activity.class);
		in.putExtra("screenWidth", screenWidth);
		in.putExtra("screenHeight", screenHeight);
		startActivityForResult(in, TO_SELECT_PHOTO_SHOP_IMG);
	}

	private void getScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	/**
	 * 打开相机
	 * 
	 * @param view
	 */
	public void open_camera_license(View view) {
		System.out.println("--------open_camera_license---------->");
		// Intent in = new Intent(this, SelectPic1Activity.class);
		Intent in = new Intent(this, Fenbinglv300.class);
		in.putExtra("screenWidth", screenWidth);
		in.putExtra("screenHeight", screenHeight);
		startActivityForResult(in, TO_SELECT_PHOTO_SHOP_LICENSE);
	}

	/**
	 * 点击下一步
	 * 
	 * @param view
	 */
	private String identity_number;

	public void next_onClick(View view) {

		if (checkInfoIsSpace("shopsname")
				&& shopNameEditText.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请填写店铺名称");
			return;
		}

		if (et_license_num.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请填写营业执照号");
			return;
		}
		// if (checkInfoIsSpace("cid") && city_id == -1) {
		// ToastUtil.show(RegisterStepTwoActivity.this, "请选择城市");
		// return;
		// }
		// if (checkInfoIsSpace("cid") && area_id == -1) {
		// ToastUtil.show(RegisterStepTwoActivity.this, "该城市没有地区选项，请重新选择城市");
		// return;
		// }
		// if (checkInfoIsSpace("cid") && shangquan_id == -1) {
		// ToastUtil.show(RegisterStepTwoActivity.this, "该地区没有商圈选项，请重新选择地区");
		// return;
		// }

		if (checkInfoIsSpace("address")
				&& locationEditText.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请填写店铺地址");
			return;
		}
		if (checkInfoIsSpace("longitude") && lng == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请进行定位");
			return;
		}
		if (checkInfoIsSpace("latitude") && lat == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请进行定位");
			return;
		}

		if (checkInfoIsSpace("typeid")
				&& shopTypeSpinner.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请选择店铺分类");
			return;
		}
		if (checkInfoIsSpace("name")
				&& nameEditView.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请填写姓名");
			return;
		}
		if (checkInfoIsSpace("identity_number")
				&& idNoEditText.getText().toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请填写身份证号");
			return;
		}
		// if (checkInfoIsSpace("content")
		// && shopInfoEditText.getText().toString().trim().length() == 0) {
		// i = 0;
		// ToastUtil.show(RegisterStepTwoActivity.this, "请填写店铺介绍");
		// return;
		// }
		if (checkInfoIsSpace("logo") && picPath.toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请上传店铺Logo图片");
			return;
		}
		if (checkInfoIsSpace("license")
				&& picPathLicense.toString().trim().length() == 0) {
			ToastUtil.show(RegisterStepTwoActivity.this, "请上传店铺营业执照");
			return;
		}
		if (shopNameEditText.getText().toString().trim().length() > 20) {
			ToastUtil.show(RegisterStepTwoActivity.this, "店铺名称长度为20个字以内");
			return;
		}
		if (nameEditView.getText().toString().trim().length() < 2
				|| nameEditView.getText().toString().trim().length() > 6) {
			ToastUtil.show(RegisterStepTwoActivity.this, "姓名长度为2-6位");
			return;
		}
		// locationEditText 现在是 详细地址
		if (locationEditText.getText().toString().trim().length() > 40) {
			ToastUtil.show(RegisterStepTwoActivity.this, "店铺地址长度为40个字以内");
			return;
		}

		if (shopInfoEditText.getText().toString().trim().length() > 100) {
			ToastUtil.show(RegisterStepTwoActivity.this, "店铺介绍长度为100个字以内");
			return;
		}

		IDCard cc = new IDCard();
		try {
			String info = cc.IDCardValidate(idNoEditText.getText().toString()
					.trim());
			if (!"".equals(info)) {
				ToastUtil.show(RegisterStepTwoActivity.this, info);
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!NetConn.checkNetwork(RegisterStepTwoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {

			proDialog.show();
			// 1、上传LOGO；2、获取Logo名称，上传文本信息
			uploadLogo();
			// FIXME 验证第二步：；1、上传文本信息,返回商店id 2、先上传Logo图片，返回Logo名称；3、添加商铺Logo
			// uploadRegisterText();
		}
	}

	/**
	 * 上传注册信息
	 */
	protected void updateRegisterMessage() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				uploadRegisterInfo();

			}
		}).start();

	}

	/**
	 * 上传注册信息 registerString phone, String pass, String rid, String shopsname,
	 * double longitude, double latitude, String address, String detail_address,
	 * int typeid, String name, String identity_number, String content, String
	 * logo, String license, String license_number, String time_one
	 */
	private void uploadRegisterInfo() {
		RegisterService registerService = new RegisterService(
				RegisterStepTwoActivity.this);

		String time_one = tvStartTime.getText().toString().trim() + "_"
				+ tvEndTime.getText().toString().trim();
		LoginReturnMsg msg = registerService.registerNew(phone, passWord,
				recommendCode, shopNameEditText.getText().toString().trim(),
				lng, lat, locationEditText.getText().toString().trim(),
				citySpinner.getText().toString().trim(), type_id, nameEditView
						.getText().toString().trim(), idNoEditText.getText()
						.toString().trim(), shopInfoEditText.getText()
						.toString().trim(), logo, license, et_license_num
						.getText().toString().trim(), time_one);
		Message message = handler.obtainMessage(STEP_TWO);
		message.obj = msg;
		handler.sendMessage(message);
	}

	/**
	 * 选择时间 左边的
	 * 
	 * @param view
	 */
	public void select_time_onClick(View view) {
		mWheeManager.createDatePreciseVheel(RegisterStepTwoActivity.this,
				tvStartTime);
	}

	// 选择右边的时间
	public void select_time_onClick1(View view) {
		mWheeManager.createDatePreciseVheel(RegisterStepTwoActivity.this,
				tvEndTime);
	}

	/**
	 * 上传LOGO
	 */
	private void uploadLogo() {
		if (!NetConn.checkNetwork(RegisterStepTwoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Map<String, String> map = new HashMap<String, String>();
					// map.put("shopsid", shopsId+"");
					map.put("imgtype", "Logo");
					String string = UpLoadFile.uploadImage(
							Constant.uploadImgURL, picPath, map);
					Message message = handler
							.obtainMessage(UPLOAD_SHOP_LOGO_DATA);
					System.out.println("----return name---->" + string);
					message.obj = string;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 
	 * 功能: 上传证件
	 * 
	 * @author: liqq
	 * @date:2015-4-29下午4:46:07
	 */
	private void uploadLicense() {
		if (!NetConn.checkNetwork(RegisterStepTwoActivity.this)) {
		} else {
			proDialog.setMessage("正在上传店铺证件照片.....");
			proDialog.setCancelable(false);
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					Map<String, String> map = new HashMap<String, String>();
					// map.put("shopsid", shopsId+"");
					map.put("imgtype", "License");
					String string = UpLoadFile.uploadImage(
							Constant.uploadImgURL, picPathLicense, map);
					Message message = handler
							.obtainMessage(UPLOAD_SHOP_LICENSE_DATA);
					message.obj = string;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 下载商店类型列表数据
	 */
	private void getShopTypeList() {
		if (!NetConn.checkNetwork(RegisterStepTwoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					RegisterService registerService = new RegisterService(
							RegisterStepTwoActivity.this);
					List<ShopType> list = registerService.getShopTypeList();
					Message message = handler.obtainMessage(SHOPTYPE_DATA);
					message.obj = list;
					handler.sendMessage(message);

				}
			}).start();
		}
	}

	/**
	 * 打开地图
	 * 
	 * @param view
	 */
	public void mapOnclick(View view) {
		Intent intent = new Intent(RegisterStepTwoActivity.this,
				MyLocationActivity.class);
		startActivityForResult(intent, TO_SELECT_LOCATION);
	}

	private void initView() {
		nameEditView = (EditText) findViewById(R.id.real_name);
		shopNameEditText = (EditText) findViewById(R.id.shop_name);
		shopTypeSpinner = (TextView) findViewById(R.id.shop_cata_spinner);
		citySpinner = (EditText) findViewById(R.id.city_cata_spinner);
		shopInfoEditText = (EditText) findViewById(R.id.shop_info);
		locationEditText = (EditText) findViewById(R.id.location);
		idNoEditText = (EditText) findViewById(R.id.id_card_no);
		shopImageView = (ImageView) findViewById(R.id.shop_img);
		shopLicenseImageView = (ImageView) findViewById(R.id.shop_license_img);
		ll_dingwei = (LinearLayout) findViewById(R.id.ll_dingwei);
		ll_shop_jt = (LinearLayout) findViewById(R.id.ll_shop_jt);
		nameEditView.setText(name);
		shopNameEditText.setText(shopName);
		shopInfoEditText.setText(shopInfo);
		locationEditText.setText(location);
		mWheeManager = WheelDialogManager.getInstanse();
		citys = new ArrayList<CityName>();
		shopTypeSpinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWheeManager.createMyItemWheelShop_Type(
						RegisterStepTwoActivity.this, citys, mPostion,
						shopTypeSpinner, new OnWheelItemClickListener() {

							@Override
							public void onWheelItemClicl(int positon) {
								mPostion = positon;
								type_id = citys.get(positon).getId();
							}
						});

			}
		});

		ll_shop_jt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWheeManager.createMyItemWheelShop_Type(
						RegisterStepTwoActivity.this, citys, mPostion,
						shopTypeSpinner, new OnWheelItemClickListener() {

							@Override
							public void onWheelItemClicl(int positon) {
								mPostion = positon;
								type_id = citys.get(positon).getId();
							}
						});
			}
		});
	}

	private WheelDialogManager mWheeManager;// 滚轮

	/*
	 * @Override public void back_onClick(View view) { //
	 * super.back_onClick(view); Intent intent = new
	 * Intent(RegisterStepTwoActivity.this,RegisterStepOneActivity.class);
	 * Bundle bundle = new Bundle(); bundle.putString("phone", phone );
	 * bundle.putString("password", passWord); bundle.putString("code", code);
	 * bundle.putString("recode", recommendCode); intent.putExtra("data",
	 * bundle); startActivity(intent); }
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO_SHOP_IMG) {
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			// Bitmap bm = PictureUtil.getSmallBitmap(picPath);
			// int degree = PictureUtil.readPictureDegree(picPath);
			// if(degree!=0){//旋转照片角度
			// bm=PictureUtil.rotateBitmap(bm,degree);
			// }
			// shopImageView.setImageBitmap(bm);
			// shopImageView.setImageBitmap(PictureUtil.getSmallBitmap(picPath));
			// Bundle bundle = data.getBundleExtra("bundle");
			// System.out.println("------picPath---2->"+bundle);
			// bitmap = bundle.getParcelable("data");
			File file = new File(picPath);
			if (file.exists()) {
				addImageView();
			} else {
				// ToastUtil.show(RegisterStepTwoActivity.this, "该图片不存在");
			}

		}
		if (resultCode == RESULT_OK
				&& requestCode == TO_SELECT_PHOTO_SHOP_LICENSE) {
			picPathLicense = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			// Bundle bundle = data.getBundleExtra("bundle");
			System.out.println("------picPath---2->" + picPathLicense);
			// bitmap = bundle.getParcelable("data");
			// Bitmap bm = PictureUtil.getSmallBitmap(picPathLicense);
			// int degree = PictureUtil.readPictureDegree(picPathLicense);
			// if(degree!=0){//旋转照片角度
			// bm=PictureUtil.rotateBitmap(bm,degree);
			// }
			// shopLicenseImageView.setImageBitmap(bm);
			// shopLicenseImageView.setImageBitmap(PictureUtil.getSmallBitmap(picPathLicense));
			File file = new File(picPathLicense);
			if (file.exists()) {
				addImageView();
			} else {
				// ToastUtil.show(RegisterStepTwoActivity.this, "该图片不存在");
			}

		}
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_LOCATION) {
			Bundle bundle = data.getBundleExtra("bundle");
			lng = bundle.getDouble("LNG");
			lat = bundle.getDouble("LAT");
			location = bundle.getString("address");
			locationEditText.setText(location);
			if (lat != 0 && lng != 0) {
				ToastUtil.show(RegisterStepTwoActivity.this, "定位成功！");
				// locationResTextView.setText("定位成功!");
				// locationEditText.setTextColor(Color.RED);
				System.out.println("-----lng---" + lng + "-----lat--" + lat);
			} else {
				ToastUtil.show(RegisterStepTwoActivity.this, "定位失败！");
			}
			// addImageView();
		}

	}

	/**
	 * 添加图片视图
	 */
	private void addImageView() {
		if (picPath != "" && picPath != null
				&& picPath.toString().trim().length() > 0) {
			System.out.println("----old--picPath------->" + picPath);
			// ImgLoad loader = ImgLoad.getInstance();
			Bitmap bmp = ImageUtil.getBitmap(picPath, 4);
			Bitmap m = ImageUtil.changeDirectionBitmap(bmp, picPath, true);
			shopImageView.setImageBitmap(m);

			System.out.println("----new--------->" + picPath);
			// loader.doTask();
			System.out.println("------结束-->");
		} else {
			shopImageView.setImageResource(R.drawable.default_good_img);
		}
		if (picPathLicense != "" && picPathLicense != null
				&& picPathLicense.toString().trim().length() > 0) {
			System.out
					.println("----old--picPathLicense----->" + picPathLicense);
			/*
			 * Bitmap bmp = ImageUtil.getBitmap(picPathLicense, 4); Bitmap m =
			 * ImageUtil.changeDirectionBitmap(bmp, picPathLicense, true);
			 * shopLicenseImageView.setImageBitmap(m);
			 * System.out.println("----new--------->"+picPathLicense);
			 * System.out.println("------结束-->");
			 * System.out.println("----old--------->"+picPath);
			 */
			// ImgLoad loader = ImgLoad.getInstance();
			Bitmap bmp = ImageUtil.getBitmap(picPathLicense, 4);
			Bitmap m = ImageUtil.changeDirectionBitmap(bmp, picPathLicense,
					true);
			Bitmap bit = ImageUtil.loadBitmap(
					ImageUtil.getimage(picPathLicense), picPathLicense, true);
			ImageUtil.saveBitmap2file(bit,
					Environment.getExternalStorageDirectory()
							+ "/EServiceStore/uplad_image.jpg");
			shopLicenseImageView.setImageBitmap(bit);
			System.out.println("----new--------->" + picPathLicense);
			System.out.println("------结束-->");
		} else {
			shopLicenseImageView.setImageResource(R.drawable.shop_licences);
		}
		// if (lat != 0 && lng != 0) {
		// ToastUtil.show(RegisterStepTwoActivity.this, "定位成功！");
		// // locationResTextView.setText("定位成功!");
		// // locationEditText.setTextColor(Color.RED);
		// System.out.println("-----lng---" + lng + "-----lat--" + lat);
		// } else {
		// ToastUtil.show(RegisterStepTwoActivity.this, "定位失败！");
		// }
	}

	/**
	 * 
	 * 功能:验证身份证号
	 * 
	 * @param number
	 * @author: liqq
	 * @date:2015-5-6下午6:04:36
	 */
	private boolean checkIDNo(String number) {
		if (number.length() != 15 && number.length() != 18) {
			return false;
		}
		String areaNum = null;
		String dateNum = null;
		String sexNum = null;
		String endNum = null;
		// 是数值
		if (number.matches("[0-9]+")) {
			// 如果是15位身份证号
			if (number.length() == 15) {
				// 省市县（6位）
				areaNum = number.substring(0, 6);
				// 出生年月（6位）
				dateNum = number.substring(6, 12);
				// 性别（3位）
				sexNum = number.substring(12, 15);
			} else {
				// 如果是18位身份证号
				// 省市县（6位）
				areaNum = number.substring(0, 6);
				// 出生年月（8位）
				dateNum = number.substring(6, 14);
				// 性别（3位）
				sexNum = number.substring(14, 17);
				// 校验码（1位）
				endNum = number.substring(17, 18);
			}
		} else {
			// 不是数值
			if (number.length() == 15) {
				return false;
			} else {
				// 验证前17位为数值，且18位为字符x
				String check17 = number.substring(0, 17);
				if (!check17.matches("[0-9]+")) {
					return false;
				}
				// 省市县（6位）
				areaNum = number.substring(0, 6);
				// 出生年月（8位）
				dateNum = number.substring(6, 14);
				// 性别（3位）
				sexNum = number.substring(14, 17);
				// 校验码（1位）
				endNum = number.substring(17, 18);
				if (!"x".equals(endNum) && !"X".equals(endNum)) {
					return false;
				}
			}
		}
		if (areaNum != null) {
			if (!checkArea(areaNum)) {
				return false;
			}
			;
		}

		if (dateNum != null) {
			if (!checkDate(dateNum)) {
				return false;
			}
		}

		/*
		 * if( sexNum != null){ if(!checkSex(Integer.parseInt(sexNum))){ return
		 * false; } }
		 */

		/*
		 * if( endNum!=null ){ return checkEnd(endNum,number); }
		 */
		return true;
	}

	private boolean checkArea(String area) {
		String num1 = area.substring(0, 2);
		String num2 = area.substring(2, 4);
		String num3 = area.substring(4, 6);
		// 根据GB/T2260—999，省市代码11到65
		if (10 < Integer.parseInt(num1) && Integer.parseInt(num1) < 66) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkDate(String date) {
		System.out.println("-----checkDate------->" + date);
		int date1 = 0;
		int date2 = 0;
		int date3 = 0;
		int nowY = 0;
		boolean statusY = false;
		if (date.length() == 6) {
			date1 = Integer.parseInt(date.substring(0, 2));
			date2 = Integer.parseInt(date.substring(2, 4));
			date3 = Integer.parseInt(date.substring(4, 6));
			statusY = checkY(Integer.parseInt("19" + date1));
		} else {
			date1 = Integer.parseInt(date.substring(0, 4));
			date2 = Integer.parseInt(date.substring(4, 6));
			date3 = Integer.parseInt(date.substring(6, 8));
			nowY = new Date().getYear() + 1900;
			if (1900 < date1 && date1 <= nowY) {
				statusY = checkY(date1);
			} else {
				return false;
			}
		}
		System.out.println("-----checkDate--date1----->" + date1);
		System.out.println("-----checkDate--date2----->" + date2);
		System.out.println("-----checkDate--date3----->" + date3);
		if (0 < date2 && date2 < 13) {
			if (date2 == 2) {
				// 润年
				if (statusY) {
					if (0 < date3 && date3 <= 29) {
						return true;
					} else {
						return false;
					}
				} else {
					// 平年
					if (0 < date3 && date3 <= 28) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				int maxDateNum = getDateNum(date2);
				if (0 < date3 && date3 <= maxDateNum) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 功能:验证性别
	 * 
	 * @param sex
	 * @return
	 * @author: liqq
	 * @date:2015-5-6下午6:27:10
	 */
	private boolean checkSex(int sex) {
		if (sex % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * 功能:验证18位身份证最后一位
	 * 
	 * @return
	 * @author: liqq
	 * @date:2015-5-6下午6:27:25
	 */
	/*
	 * private boolean checkEnd(String endNum , String number){ $checkHou =
	 * array(1,0,'x',9,8,7,6,5,4,3,2); $checkGu =
	 * array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2); $sum = 0; for($i = 0;$i < 17;
	 * $i++){ $sum += (int)$checkGu[$i] * (int)$num[$i]; } $checkHouParameter=
	 * $sum % 11; if($checkHou[$checkHouParameter] != $num[17]){ return false;
	 * }else{ return true; } }
	 */

	/**
	 * 
	 * 功能:验证平年润年，参数年份,返回 true为润年 false为平年
	 * 
	 * @param y
	 * @return
	 * @author: liqq
	 * @date:2015-5-6下午6:21:51
	 */
	private boolean checkY(int Y) {
		/*
		 * if(getType($Y) == 'string'){ $Y = (int)$Y; }
		 */
		if (Y % 100 == 0) {
			if (Y % 400 == 0) {
				return true;
			} else {
				return false;
			}
		} else if (Y % 4 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 功能:当月天数 参数月份（不包括2月） 返回天数
	 * 
	 * @param month
	 * @return
	 * @author: liqq
	 * @date:2015-5-6下午6:22:57
	 */
	private int getDateNum(int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		} else if (month == 2) {
			return -1;
		} else {
			return 30;
		}
	}

	@Override
	public void onCheckDown(DeviceInfoResponse response) {
		// TODO Auto-generated method stub

		if (cdTask != null) {
			cdTask.cancel(true);
			cdTask = null;
		}
		if (response != null) {
			if ("0".equals(response.getRes())) {
				/** 保存本地的版本号 */

				TelephoneUtil.saveState(this, Engine.getVersionCode(this));
			} else {
			}
		}

	}

	// private WheelView minute, hour;
	private int mHour = 0, mMinute = 0;

	// public void showDateTimePicker(final View view) {
	// LayoutInflater inflater = LayoutInflater
	// .from(RegisterStepTwoActivity.this);
	// View timepickerview = inflater.inflate(R.layout.selectbirthday, null);
	// timepickerview.setMinimumWidth(getWindowManager().getDefaultDisplay()
	// .getWidth());
	// dialog = new AlertDialog.Builder(this).setView(timepickerview).show();
	// Window window = dialog.getWindow();
	// window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
	// window.setWindowAnimations(R.style.mystyle); // 添加动画
	// dialog.setCanceledOnTouchOutside(true);
	// dialog.show();
	// Button btn = (Button) timepickerview
	// .findViewById(R.id.btn_datetime_sure);
	// Calendar calendar = Calendar.getInstance();
	// mHour = calendar.get(Calendar.HOUR_OF_DAY);
	// mMinute = calendar.get(Calendar.MINUTE);
	// hour = (WheelView) timepickerview.findViewById(R.id.hour);
	// minute = (WheelView) timepickerview.findViewById(R.id.minute);
	// // int curMonth = calendar.get(Calendar.HOUR_OF_DAY);
	// hour.setViewAdapter(new DateNumericAdapter(this, 0, 23, mHour));
	// hour.setCurrentItem(mHour);
	// hour.addChangingListener(listener);
	// hour.setCyclic(true);
	// // int curYear = calendar.get(Calendar.MINUTE);
	// minute.setViewAdapter(new DateNumericAdapter(this, 0, 59, mMinute));
	// minute.setCurrentItem(mMinute);
	// minute.addChangingListener(listener);
	// minute.setCyclic(true);
	// updateDays(hour, minute, true);
	//
	// btn.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// if (view.getId() == R.id.start_time_textview) {
	// tvStartTime.setText((mHour < 10 ? "0" + mHour : mHour)
	// + ":" + (mMinute < 10 ? "0" + mMinute : mMinute));
	// } else if (view.getId() == R.id.end_time_textview) {
	// tvEndTime.setText((mHour < 10 ? "0" + mHour : mHour) + ":"
	// + (mMinute < 10 ? "0" + mMinute : mMinute));
	// }
	// dialog.dismiss();
	// }
	// });
	// }

	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(fontSize);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				// view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	// OnWheelChangedListener listener = new OnWheelChangedListener() {
	// public void onChanged(WheelView wheel, int oldValue, int newValue) {
	// updateDays(hour, minute, false);
	// }
	// };
	private EditText et_license_num;
	private LinearLayout ll_shop_jt;

	// void updateDays(WheelView hour, WheelView minute, boolean isF) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.HOUR, hour.getCurrentItem());
	// calendar.set(Calendar.MINUTE, minute.getCurrentItem());
	// mHour = hour.getCurrentItem();
	// mMinute = minute.getCurrentItem();
	// }
}
