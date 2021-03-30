package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.oto01.model.City;
import com.example.oto01.model.Login;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.model.ShopType;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.RegisterService;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;

/**
 * 店铺资料界面
 * 
 * @author lqq
 * 
 */
public class ShopDataActivity extends BaseActivity {
	private int shopsid = 1;
	private Dialog proDialog;
	private String address;
	private double lng = 0.0;
	private double lat = 0.0;
	private static final int TO_SELECT_LOCATION = 1;
	private static final int CITY_DATA = 2;
	private static final int AREA_DATA = 3;
	private static final int SHANGQ_QUAN_DATA = 4;
	private static final int UPDATE_SHOP_INFO = 6;

	private static final int SHOPTYPE_DATA = 5;
	private TextView phoneTextView;
	private EditText et_details_address;
	private EditText shopNameEditText, nameEditText, addressEditText;
	private Spinner shopTypeSpinner;

	private static int city_id = -1;
	private static int area_id = -1;
	private static int shangquan_id = -1;
	private int type_id = 1;
	// private int city_position,area_position ,shangquan_position
	// ,type_position;
	// private List<City> currentCity = new ArrayList<City>();
	private String mShopTypeName;

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
				proDialog.dismiss();
				// refeshShangQuanSpinner((List<City>) msg.obj);
				break;
			case SHOPTYPE_DATA:
				proDialog.dismiss();
				refeshShopTypeSpinner((List<ShopType>) msg.obj);
				break;
			case UPDATE_SHOP_INFO:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(ShopDataActivity.this, "保存成功！");
					finish();
				} else {
					ToastUtil.show(ShopDataActivity.this, msg.obj.toString());
				}
				break;
			default:
				refeshUI((ShopInfo) msg.obj);
				break;
			}
		};
	};

	/**
	 * 刷新商店类型
	 * 
	 * @param obj
	 */
	private void refeshShopTypeSpinner(final List<ShopType> obj) {

		List<String> shoptypeList = new ArrayList<String>();
		if (obj != null && !obj.isEmpty()) {
			for (ShopType shopType : obj) {
				shoptypeList.add(shopType.getName());
			}
		}
		ArrayAdapter<String> shopTypeAdapter = new ArrayAdapter<String>(
				ShopDataActivity.this, android.R.layout.simple_spinner_item,
				shoptypeList);
		shopTypeAdapter.setDropDownViewResource(R.layout.drop_down_item);
		shopTypeSpinner.setAdapter(shopTypeAdapter);
		// shopTypeSpinner.setSelection(type_position);
		/*
		 * if(shoptypeList.size()>=1){ type_id = obj.get(0).getId(); }
		 */

		// if (type_id != -1) {
		// for (int i = 0; i < obj.size(); i++) {
		// if (obj.get(i).getId() == type_id) {
		// System.out.println("------selection-------->" + i);
		// shopTypeSpinner.setSelection(i);
		// }
		// }
		// }else{
		for (int i = 0; i < obj.size(); i++) {
			if (obj.get(i).getName().equals(mShopTypeName)) {
				System.out.println("------selection-------->" + i);
				shopTypeSpinner.setSelection(i);
			}
		}
		// }

		shopTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View view,
					int position, long arg3) {
				// FIXME 联动地区
				type_id = obj.get(position).getId();
				// type_position = position;
				TextView tv = (TextView) view;
				tv.setTextSize(16);
				tv.setTextColor(Color.GRAY);
				tv.setGravity(android.view.Gravity.CENTER_VERTICAL
						| Gravity.LEFT); // 设置居中
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/**
	 * 刷新城市
	 * 
	 * @param obj
	 */
	// private void refeshCitySpinner(final List<City> obj) {
	// System.out.println("-----第一步----->" + obj);
	// // currentCity = obj;
	// List<String> cityList = new ArrayList<String>();
	// if (obj != null && !obj.isEmpty()) {
	// for (City city : obj) {
	// cityList.add(city.getName());
	// }
	// }
	// ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
	// ShopDataActivity.this, android.R.layout.simple_spinner_item,
	// cityList);
	// cityAdapter.setDropDownViewResource(R.layout.drop_down_item);
	// citySpinner.setAdapter(cityAdapter);
	// for (int i = 0; i < obj.size(); i++) {
	// if (obj.get(i).getId() == city_id) {
	// System.out.println("------selection-------->" + i);
	// citySpinner.setSelection(i);
	// }
	// }
	// citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> parentView, View view,
	// int position, long arg3) {
	// // FIXME 联动地区
	// city_id = obj.get(position).getId();
	// // city_position = position;
	// getCitieList(city_id, 0);
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
	// }

	/**
	 * 刷新地区
	 * 
	 * @param obj
	 */
	// private void refeshAreaSpinner(final List<City> obj) {
	// System.out.println("-----第二步----->" + obj);
	// List<String> areaList = new ArrayList<String>();
	// if (obj != null && !obj.isEmpty()) {
	// for (City city : obj) {
	// areaList.add(city.getName());
	// }
	// }
	// ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(
	// ShopDataActivity.this, android.R.layout.simple_spinner_item,
	// areaList);
	// areaAdapter.setDropDownViewResource(R.layout.drop_down_item);
	// areaSpinner.setAdapter(areaAdapter);
	// for (int i = 0; i < obj.size(); i++) {
	// if (obj.get(i).getId() == area_id) {
	// System.out.println("------selection-------->" + i);
	// areaSpinner.setSelection(i);
	// }
	// }
	// areaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> parentView, View view,
	// int position, long arg3) {
	// // FIXME 联动地区
	// // area_position = position;
	// area_id = obj.get(position).getId();
	// getCitieList(area_id, 1);
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
	//
	// }

	/**
	 * 刷新商圈
	 * 
	 * @param obj
	 */
	// private void refeshShangQuanSpinner(final List<City> obj) {
	// System.out.println("-----第三步----->" + obj);
	// List<String> shangquanList = new ArrayList<String>();
	// if (obj != null && !obj.isEmpty()) {
	// for (City city : obj) {
	// shangquanList.add(city.getName());
	// }
	// }
	// ArrayAdapter<String> shangquanAdapter = new ArrayAdapter<String>(
	// ShopDataActivity.this, android.R.layout.simple_spinner_item,
	// shangquanList);
	// shangquanAdapter.setDropDownViewResource(R.layout.drop_down_item);
	// shangquanSpinner.setAdapter(shangquanAdapter);
	// for (int i = 0; i < obj.size(); i++) {
	// if (obj.get(i).getId() == shangquan_id) {
	// System.out.println("------selection-------->" + i);
	// shangquanSpinner.setSelection(i);
	// }
	// }
	// shangquanSpinner
	// .setOnItemSelectedListener(new OnItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> parentView,
	// View view, int position, long arg3) {
	// // FIXME 联动地区
	// // shangquan_position = position;
	// shangquan_id = obj.get(position).getId();
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
	//
	// }

	/**
	 * 下载城市列表数据
	 * 
	 * @param parentid
	 */
	private void getCitieList(final int parentid, final int tag) {
		if (!NetConn.checkNetwork(ShopDataActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					RegisterService registerService = new RegisterService(
							ShopDataActivity.this);
					List<City> list = registerService.getCitieList(parentid);
					Message message = null;
					if (parentid == 0) {
						message = handler.obtainMessage(CITY_DATA);
					} else {
						if (tag == 0) {
							message = handler.obtainMessage(AREA_DATA);
						} else {
							message = handler.obtainMessage(SHANGQ_QUAN_DATA);
						}

					}
					message.obj = list;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_data);
		showDialog();
		initView();
		initData();
	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	/***
	 * 刷新UI
	 * 
	 * @param shopInfo
	 */
	private void refeshUI(ShopInfo shopInfo) {
		if (shopInfo != null) {
			shopNameEditText.setText(shopInfo.getShopsname());
			nameEditText.setText(shopInfo.getName());
			findViewById(R.id.phone_hint).setVisibility(View.VISIBLE);
			phoneTextView.setText(shopInfo.getPhone());
			addressEditText
					.setText((shopInfo.getAddress() == "null" || shopInfo
							.getAddress().equals("null")) ? "" : shopInfo
							.getAddress());
			city_id = shopInfo.getCity();
			area_id = shopInfo.getArea();
			shangquan_id = shopInfo.getCountry();
			mShopTypeName = shopInfo.getTypename();
			et_details_address.setText(shopInfo.getDetail_address());
			lng = shopInfo.getLng();
			lat = shopInfo.getLat();
		}
		initSpinnerData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		LoginManager lm = LoginManager.getInstance(ShopDataActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		shopNameEditText = (EditText) findViewById(R.id.shopName);
		nameEditText = (EditText) findViewById(R.id.name);
		phoneTextView = (TextView) findViewById(R.id.phone);
		addressEditText = (EditText) findViewById(R.id.address);
		shopTypeSpinner = (Spinner) findViewById(R.id.shop_cata_spinner);
		// 商铺的详细地址
		et_details_address = (EditText) findViewById(R.id.tv_details_address);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (!NetConn.checkNetwork(ShopDataActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopDataActivity.this);
					ShopInfo shopInfo = service.checkShopData(shopsid);
					Message message = handler.obtainMessage();
					message.obj = shopInfo;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 初始化数据
	 */
	private void initSpinnerData() {
		// getCitieList(0, 0);
		getShopTypeList();
	}

	/**
	 * 下载商店类型列表数据
	 */
	private void getShopTypeList() {
		if (!NetConn.checkNetwork(ShopDataActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					RegisterService registerService = new RegisterService(
							ShopDataActivity.this);
					List<ShopType> list = registerService.getShopTypeList();
					Message message = handler.obtainMessage(SHOPTYPE_DATA);
					message.obj = list;
					handler.sendMessage(message);

				}
			}).start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_LOCATION) {
			Bundle bundle = data.getBundleExtra("bundle");
			lng = bundle.getDouble("LNG");
			lat = bundle.getDouble("LAT");
			// ToastUtil.show(ShopDataActivity.this, lng+"==="+lat);
			address = bundle.getString("address");
			addressEditText.setText(address);
			if (lat != 0 && lng != 0) {
				ToastUtil.show(ShopDataActivity.this, "定位成功！");
			} else {
				ToastUtil.show(ShopDataActivity.this, "定位失败！");
			}
			System.out.println("------lat-->" + lat + "------lng-->" + lng);
		}
	}

	/**
	 * 点击地图定位
	 * 
	 * @param view
	 */
	public void mapOnclick(View view) {
		Intent in = new Intent(this, MyLocationActivity.class);
		startActivityForResult(in, TO_SELECT_LOCATION);
	}

	/**
	 * 保存
	 * 
	 * @param view
	 */
	public void save_onClick(View view) {
		if (!NetConn.checkNetwork(ShopDataActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					// SettingService service = new SettingService(
					// ShopDataActivity.this);
					// // int flag = service.updateShopInfo(shopsid,
					// // shopNameEditText
					// // .getText().toString().trim(), nameEditText
					// // .getText().toString().trim(), type_id,
					// // shangquan_id, addressEditText.getText().toString()
					// // .trim(), lng, lat, et_details_address
					// // .getText().toString().trim());
					// Message message =
					// handler.obtainMessage(UPDATE_SHOP_INFO);
					// message.arg1 = flag;
					// message.obj = service.errorMsg;
					// handler.sendMessage(message);
				}
			}).start();
		}
	}
}
