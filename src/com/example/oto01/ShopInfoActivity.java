package com.example.oto01;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.model.Constant;
import com.example.oto01.model.Login;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.UpLoadFile;
import com.example.oto01.wheelDialog.WheelDialogManager;
import com.example.wheelmanager.view.wheelitem.WheelView;

/**
 * 店铺信息界面
 * 
 * @author lqq
 * 
 */
public class ShopInfoActivity extends BaseActivity {
	private ImageView shopImageView, cashOnDeliveryImageView;
	private TextView timeEditText, guanggaoEditText;;
	private int shopsid;
	protected static final int TO_SELECT_PHOTO = 1;
	protected static final int DOWNLOAD_PIC = 2;
	protected static final int UPLOAD_PIC = 3;
	protected static final int GET_SHOP_LOGO_INFO = 4;
	protected static final int GET_SHOP_TEXT_INFO = 6;
	protected static final int UPDATE_PIC = 5;
	protected static final int UPDATE_SHOP_INFO = 8;
	protected static final int NEED_UPDATE_SHOP_INFO = 7;
	private String picPath = "";
	private String oldPicName = "";
	private String newPicName = "";
	private Dialog proDialog;
	private ProgressDialog dialog;
	private AlertDialog dialog1;
	private WheelDialogManager mWheeManager;
	private int sendStatus = 1;// 是否支持外送:1是2否
	private int businessStatus = 1;// 是否营业:1是2否
	private int cashOnDeliveryStatus = 1;// 是否支持货到付款:1是2否
	private int sendPrice;// 起送价
	private int deliveryFee;// 配送费
	private String license;// 证件地址
	// private TextView addTextView,delTextView;//加、减
	private String mAdvSlogan;// 广告语
	private String errorMsg;
	private int screenWidth;
	private int screenHeight;
	private String phone;
	private TextView tvStartTime, tvEndTime;
	private WheelView minute, hour;
	private int mHour = 0, mMinute = 0;
	private int fontSize = 24; // 字体大小

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_SHOP_LOGO_INFO:
				ShopInfo shopInfo = (ShopInfo) msg.obj;
				if (shopInfo != null) {
					String imgPath = shopInfo.getPicpath();
					oldPicName = shopInfo.getPicname();
					ImgLoad loader = ImgLoad.getInstance();
					System.out.println("-----imgpath------->" + imgPath);
					if (imgPath != null && imgPath.length() > 0) {
						shopImageView.setTag(imgPath);
						loader.addTask(imgPath, shopImageView);
						loader.doTask();
					} else {
						shopImageView
								.setImageResource(R.drawable.default_good_img);
					}
				}
				break;
			case UPLOAD_PIC:
				if ((String) msg.obj != null) {
					newPicName = (String) msg.obj;
					changePic();
				}
				break;
			case UPDATE_PIC:
				dialog.dismiss();
				String newPicPath = (String) msg.obj;
				ImgLoad loader = ImgLoad.getInstance();
				System.out.println("-----newPicPath------->" + newPicPath);
				if (newPicPath != null && newPicPath.length() > 0) {
					ToastUtil.show(ShopInfoActivity.this, "更新成功");
					shopImageView.setTag(newPicPath);
					loader.addTask(newPicPath, shopImageView);
					loader.doTask();
				} else {
					ToastUtil.show(ShopInfoActivity.this, "" + errorMsg);
					// shopImageView.setImageResource(R.drawable.default_good_img);
				}
				break;
			case DOWNLOAD_PIC:
				break;
			case GET_SHOP_TEXT_INFO:
				proDialog.dismiss();
				ShopInfo shop = (ShopInfo) msg.obj;
				if (shop != null) {
					mAdvSlogan = (shop.getSlogan() == null || shop.getSlogan() == "null") ? ""
							: shop.getSlogan();
					guanggaoEditText.setText((shop.getSlogan() == null || shop
							.getSlogan() == "null") ? "" : shop.getSlogan());
					// guanggaoEditText.setSelection(guanggaoEditText.getText().toString().length());
					String onlineTime = (shop.getOnlinetime() == null || shop
							.getOnlinetime() == "null") ? "" : shop
							.getOnlinetime();

					// if (!onlineTime.contains("week_")) {
					// onlineTime = "每天";
					// } else {
					// String str[] = onlineTime.split("_");
					// if ("1,2,3,4,5".equals(str[1])) {
					// onlineTime = "工作日";
					// } else if ("6,0".equals(str[1])) {
					// onlineTime = "周末";
					// } else if ("1,2,3,4,5,6,0".equals(str[1])) {
					// onlineTime = "每天";
					// } else {
					// onlineTime = "";
					// String str1[] = str[1].split(",");
					// for (int i = 0; i < str1.length; i++) {
					// switch (Integer.parseInt(str1[i])) {
					// case 1:
					// onlineTime += "一,  ";
					// break;
					// case 2:
					// onlineTime += "二,  ";
					// break;
					// case 3:
					// onlineTime += "三,  ";
					// break;
					// case 4:
					// onlineTime += "四,  ";
					// break;
					// case 5:
					// onlineTime += "五,  ";
					// break;
					// case 6:
					// onlineTime += "六,  ";
					// break;
					// case 0:
					// onlineTime += "日,  ";
					// break;
					// }
					// }
					// onlineTime = "周" + onlineTime;
					// onlineTime = onlineTime.substring(0,
					// onlineTime.length() - 3);
					// }
					// }
					String str1 = null;
					String str2 = null;
					if (shop.getBusiness_time_show().equals("")) {
						tvStartTime.setText("00:00");
						tvEndTime.setText("23:59");
					} else {
						String[] messageSplit = shop.getBusiness_time_show()
								.split("-");
						for (int i = 0; i < messageSplit.length; i++) {
							str1 = messageSplit[0];
							str2 = messageSplit[1];
						}
						tvStartTime.setText(str1 + "");
						tvEndTime.setText(str2 + "");
					}

					// List<ShopBusinessTime> businessTime = shop
					// .getBusiness_time();
					// if (businessTime.size() == 0) {
					// onlineTime += "00:00  -  23:59";

					// } else {
					// // onlineTime += "\n";
					// onlineTime += businessTime.get(0).getStart_time() + "-";
					// onlineTime += businessTime.get(0).getEnd_time();
					//
					// tvStartTime
					// .setText(businessTime.get(0).getStart_time());
					// tvEndTime.setText(businessTime.get(0).getEnd_time());
					// if (businessTime.size() == 2) {
					// if (!"".equals(businessTime.get(1).getStart_time())
					// && !"".equals(businessTime.get(1)
					// .getEnd_time())) {
					// onlineTime += "   "
					// + businessTime.get(1).getStart_time()
					// + "-";
					// onlineTime += businessTime.get(1).getEnd_time();
					// tvStartTime.setText(businessTime.get(0)
					// .getStart_time());
					// tvEndTime.setText(businessTime.get(0)
					// .getEnd_time());
					// }
					// }
					//
					// }
					tv_name.setText(shop.getShopsname());

					phone = shop.getPhone();
					sendStatus = shop.getIs_send();
					businessStatus = shop.getIs_business();
					cashOnDeliveryStatus = shop.getIs_goods_payment();
					sendPrice = shop.getSendprice();
					deliveryFee = shop.getFreight_price();
					license = shop.getLicense();
					// mSendPriceTextView.setText(String.valueOf(sendPrice)+".00");
					// mDistributionCostsPriceTextView.setText(String
					// .valueOf(deliveryFee) + ".00");
					if (sendStatus == 1) {
						// sendImageView.setImageResource(R.drawable.open_icon);
						// delTextView.setEnabled(true);
						// addTextView.setEnabled(true);

					} else {
						// sendImageView.setImageResource(R.drawable.close_icon);
						// delTextView.setEnabled(false);
						// addTextView.setEnabled(false);
					}
					if (cashOnDeliveryStatus == 1) {
						cashOnDeliveryImageView
								.setImageResource(R.drawable.open_icon);
					} else {
						cashOnDeliveryImageView
								.setImageResource(R.drawable.close_icon);
					}
					if (businessStatus == 1) {
						// businessImageView
						// .setImageResource(R.drawable.open_icon);
					} else {
						// businessImageView
						// .setImageResource(R.drawable.close_icon);
					}
				}
				break;
			case UPDATE_SHOP_INFO:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(ShopInfoActivity.this, "保存成功！");
					finish();
				} else {
					ToastUtil.show(ShopInfoActivity.this, msg.obj.toString());
				}
				break;
			}

		};
	};
	private TextView tv_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_info);
		LoginManager lm = LoginManager.getInstance(ShopInfoActivity.this);
		Login login = lm.getLoginInstance();
		shopsid = login.getAdminId();
		shopImageView = (ImageView) findViewById(R.id.shopImg);
		guanggaoEditText = (TextView) findViewById(R.id.guanggao);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tvStartTime = (TextView) findViewById(R.id.start_time_textview);
		tvEndTime = (TextView) findViewById(R.id.end_time_textview);
		mWheeManager = WheelDialogManager.getInstanse();
		// mSendPriceTextView = (TextView) findViewById(R.id.send_the_price);
		// mDistributionCostsPriceTextView = (TextView)
		// findViewById(R.id.distribution_costs_price);
		// sendImageView = (ImageView) findViewById(R.id.send);
		cashOnDeliveryImageView = (ImageView) findViewById(R.id.cashOnDeliveryImageView);
		// businessImageView = (ImageView)
		// findViewById(R.id.is_business_imageview);
		// addTextView = (TextView) findViewById(R.id.add);
		// delTextView = (TextView) findViewById(R.id.del);
		getScreenSize();
		if (!NetConn.checkNetwork(ShopInfoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在更新店铺照片...");
			dialog.setCancelable(true);
			proDialog = new Dialog(this, R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
			proDialog.setCancelable(false);
			proDialog.show();
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// SettingService service = new SettingService(
			// ShopInfoActivity.this);
			// ShopInfo shopInfo = service.checkShopInfo(shopsid);
			// Message message = handler.obtainMessage(GET_SHOP_LOGO_INFO);
			// message.obj = shopInfo;
			// handler.sendMessage(message);
			// }
			// }).start();
			getShopInfo();
		}
	}

	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	//
	// }

	private void getShopInfo() {
		if (!NetConn.checkNetwork(ShopInfoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopInfoActivity.this);
					ShopInfo shopInfo = service.checkShopInfo2(shopsid);
					Message message = handler.obtainMessage(GET_SHOP_TEXT_INFO);
					message.obj = shopInfo;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 点击修改店铺门面
	 * 
	 * @param view
	 */
	public void change_onClick(View view) {
		Intent in = new Intent(this, SelectPicActivity.class);
		in.putExtra("screenWidth", screenWidth);
		in.putExtra("screenHeight", screenHeight);
		startActivityForResult(in, TO_SELECT_PHOTO);
	}

	private void getScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	/**
	 * 修改LOGO
	 */
	private void changePic() {
		if (!NetConn.checkNetwork(ShopInfoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopInfoActivity.this);
					String newPicPath = service.updateShopLogo(shopsid,
							oldPicName, newPicName);
					Message message = handler.obtainMessage(UPDATE_PIC);
					message.obj = newPicPath;
					errorMsg = service.errorMsg;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			if (!NetConn.checkNetwork(ShopInfoActivity.this)) {
			} else {
				if (!"".equals(picPath) && picPath != null) {
					dialog.show();
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
		} else if (resultCode == RESULT_OK
				&& requestCode == NEED_UPDATE_SHOP_INFO) {
			getShopInfo();
		}
	}

	/**
	 * 点击查看店铺资料
	 * 
	 * @param view
	 */
	public void showInfo_onClick(View view) {
		startActivity(new Intent(ShopInfoActivity.this, ShopDataActivity.class));
	}

	/**
	 * 点击修改密码
	 * 
	 * @param view
	 */
	public void updatePass_onClick(View view) {
		startActivity(new Intent(ShopInfoActivity.this,
				ChangePasswordStepOneActivity.class));
	}

	/**
	 * 点击联系方式
	 * 
	 * @param view
	 */
	public void contact_onClick(View view) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ContactsActivity.class);
		intent.putExtra("shopsid", shopsid);
		intent.putExtra("phone", phone);
		startActivity(intent);
	}

	/**
	 * 点击店铺介绍
	 * 
	 * @param view
	 */
	public void shopIntroduce_onClick(View view) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ShopIntroduceActivity.class);
		intent.putExtra("shopsid", shopsid);
		intent.putExtra("phone", phone);
		startActivity(intent);
	}

	/**
	 * 修改店铺证件
	 * 
	 * @param view
	 */
	public void shopLicense_onClick(View view) {
		Intent intent = new Intent(ShopInfoActivity.this,
				RegisterStepThreeActivity.class);
		intent.putExtra("tag", 2);
		intent.putExtra("shopsId", shopsid);
		intent.putExtra("license", license);
		startActivity(intent);
	}

	/**
	 * 修改广告语
	 * 
	 * @param view
	 */
	public void advertising_slogan_onClick(View view) {
		Intent intent = new Intent(ShopInfoActivity.this,
				ShopAdvertisingSloganActivity.class);
		intent.putExtra("advSlogan", mAdvSlogan);
		intent.putExtra("phone", phone);
		startActivityForResult(intent, NEED_UPDATE_SHOP_INFO);
	}

	// /**
	// * 修改营业时间
	// *
	// * @param view
	// */
	// public void business_time_onClick(View view) {
	// Intent intent = new Intent(ShopInfoActivity.this,
	// BusinessHoursActivity.class);
	// startActivityForResult(intent, NEED_UPDATE_SHOP_INFO);
	// }

	// /**
	// * 是否营业
	// *
	// * @param view
	// */
	// public void is_business_onClick(View view) {
	//
	// if (businessStatus == 1) {
	// businessImageView.setImageResource(R.drawable.close_icon);
	// businessStatus = 2;
	// } else {
	// businessImageView.setImageResource(R.drawable.open_icon);
	// businessStatus = 1;
	// }
	// }

	// /**
	// * 是否支持外送
	// *
	// * @param view
	// */
	// public void is_outside_order_onClick(View view) {
	// if (sendStatus == 1) {
	// sendImageView.setImageResource(R.drawable.close_icon);
	// sendStatus = 2;
	// // delTextView.setEnabled(false);
	// // addTextView.setEnabled(false);
	// } else {
	// sendImageView.setImageResource(R.drawable.open_icon);
	// sendStatus = 1;
	// // delTextView.setEnabled(true);
	// // addTextView.setEnabled(true);
	// }
	// }

	/**
	 * 是否货到付款
	 * 
	 * @param view
	 */
	public void is_cash_on_delivery_onClick(View view) {
		if (cashOnDeliveryStatus == 1) {
			cashOnDeliveryImageView.setImageResource(R.drawable.close_icon);
			cashOnDeliveryStatus = 2;
		} else {
			cashOnDeliveryImageView.setImageResource(R.drawable.open_icon);
			cashOnDeliveryStatus = 1;
		}
	}

	// /**
	// * 起送价
	// *
	// * @param view
	// */
	// public void send_the_price_onClick(View view) {
	// new PriceChangesDialog(this,
	// (int) Double.parseDouble(mSendPriceTextView.getText()
	// .toString().trim()),
	// new PriceChangesDialog.OnPriceChangesDialogListener() {
	//
	// @Override
	// public void onPriceChangesDialogListener(String content) {
	// /** 调用添加接口 */
	// sendPrice = Integer.parseInt(content);
	// mSendPriceTextView.setText(String.valueOf(sendPrice)
	// + ".00");
	// }
	// }).show();
	// }

	// /**
	// * 配送费
	// *
	// * @param view
	// */
	// public void distribution_costs_onClick(View view) {
	// new PriceChangesDialog(this,
	// (int) Double.parseDouble(mDistributionCostsPriceTextView
	// .getText().toString().trim()),
	// new PriceChangesDialog.OnPriceChangesDialogListener() {
	// @Override
	// public void onPriceChangesDialogListener(String content) {
	// /** 调用添加接口 */
	// deliveryFee = Integer.parseInt(content);
	// mDistributionCostsPriceTextView.setText(String
	// .valueOf(deliveryFee) + ".00");
	// }
	// }).show();
	//
	// }

	// /**
	// * 减价
	// * @param view
	// */
	// public void del_onClick(View view){
	// if(currentPrice>=5){
	// currentPrice -= 5;
	// priceEditText.setText("￥"+currentPrice);
	// }
	//
	// }

	// /**
	// * 加价
	// * @param view
	// */
	// public void add_onClick(View view){
	// if(currentPrice>=0){
	// currentPrice += 5;
	// priceEditText.setText("￥"+currentPrice);
	// }
	// }

	/**
	 * 保存
	 * 
	 * @param view
	 */
	public void save_onClick(View view) {
		if (!NetConn.checkNetwork(ShopInfoActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			proDialog.show();
			final String time_one = tvStartTime.getText().toString().trim()
					+ "_" + tvEndTime.getText().toString().trim();
			new Thread(new Runnable() {

				@Override
				public void run() {
					SettingService service = new SettingService(
							ShopInfoActivity.this);
					int flag = service
							.updateShopInfo1(shopsid, phone, time_one);
					Message message = handler.obtainMessage(UPDATE_SHOP_INFO);
					message.arg1 = flag;
					message.obj = service.errorMsg;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 选择时间 左边的选择时间
	 * 
	 * @param view
	 */
	public void select_time_onClick(View view) {
		mWheeManager.createDatePreciseVheel(ShopInfoActivity.this, tvStartTime);
	}

	// 选择右边的时间
	public void select_time_onClick1(View view) {
		mWheeManager.createDatePreciseVheel(ShopInfoActivity.this, tvEndTime);
	}

}
