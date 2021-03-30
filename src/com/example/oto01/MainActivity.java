package com.example.oto01;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.oto01.db.GesturePasswordDBService;
import com.example.oto01.db.MessageDBService;
import com.example.oto01.gesture.CreateGesturePasswordActivity;
import com.example.oto01.gesture.HomeListener;
import com.example.oto01.gesture.UnlockGesturePasswordActivity;
import com.example.oto01.model.FristPage;
import com.example.oto01.model.MessageList;
import com.example.oto01.model.Messages;
import com.example.oto01.model.RongIMBean;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.model.VersionInfo;
import com.example.oto01.services.ElectronicAccountManager;
import com.example.oto01.services.FristPageService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.MessageService;
import com.example.oto01.services.OrderService;
import com.example.oto01.services.SettingService;
import com.example.oto01.task.GetTokenTask;
import com.example.oto01.task.GetTokenTask.OnGetTokenLisener;
import com.example.oto01.utils.ActivityManager;
import com.example.oto01.utils.ClientsUtil;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.HttpRequestUtil;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.SystemUtil;
import com.example.oto01.utils.TelephoneUtil;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.views.CoverView;
import com.example.oto01.views.ImageViewClickListener;
import com.igexin.slavesdk.MessageManager;

/**
 * 
 * @类描述:首页
 * @项目名称: 社区e商户
 * @类名称: MainActivity
 * @包名称: com.example.oto01
 * @author: cym
 * @date: 2016-9-26上午11:07:30
 */

@SuppressLint("InflateParams")
public class MainActivity extends BaseActivity implements OnGetTokenLisener,
		LocationSource, AMapLocationListener, OnClickListener,
		PlatformActionListener {
	private long exitTime;
	private ImageView settingImageView, shareImageView, messaheImageView;// 设置，分享，消息
	private int shopsid;// 店铺id
	private Dialog proDialog;
	private TextView titleTextView, tv_order_num;// 标题，订单数量的文本
	protected String logoLocalPath = "";// 店铺本地图片
	protected String logoNetPath = "";// 店铺要上传到网上的图片
	protected String shopLocation = "";// 店铺所在位置 分享的时候用
	protected String shopName = "";// 初始化店铺名称
	protected static final int APP_UPDATE = 1;// 检查版本更新
	private static final int GET_ACCOUNT_STATUS = 3;// 在线收银台的状态
	private int mVersionCode;// 版本号
	private AlertDialog myDialog = null;
	private static final int ORDER_NUM = 100;// 订单管理的订单数量
	private static final int CHECK_SHOP_INFO = 101;// 店铺基本信息的显示
	private static final int SELECT_BANK = 102;// 在线收银台查询
	private static final int REVIEW_ONE = 103;// 在线收银台填写资料页面
	private static final int REVIEW_TWO = 104; // 在线收银台审核页面
	private static final int DISTABLE = 105;// 店铺是否禁用
	private static final int EXIT_LOGIN = 106;// 退出登录
	private static final int MESSAGE_NUM = 107;// 消息中心是否有未读／已读消息
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // 需要自己定义标志
	private Context context;
	public static boolean isConnect;// 融云连接
	private static final int NOTIFY_ID = 0;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder mBuilder;
	private String actionRong = "connect_rong";
	private FristPage obj = new FristPage();
	private String shopstate = "101";// 店铺状态
	private JSONObject jObject;// json解析
	private String jumpurl;// 跳转页面的url
	private int status;// 状态
	private String phone1;// 手机号
	private String shopsName;// 店铺名称
	private String error;// 错误消息
	private TextView main_gridview_item_layout_text2;
	private TextView tv_message_num;// 消息的未读 红点
	private ImageView iv_online;// 在线收银台的 审核状态的图片
	private CoverView conVerView;// 首次登录 在线收银台的布局
	private String shopstate1;// 店铺状态
	private RelativeLayout rl_check;// 首次登录 要出来的布局 店铺审核不通过的布局
	private ImageView imageView_twoCode;// 二维码
	private MapView mapView;// 地图的view
	private AMap aMap;// 地图
	private OnLocationChangedListener mListener;// 经纬度改变的回调
	private LocationManagerProxy mAMapLocationManager;// Location服务的提供者
	public static Double geoLat;// 经度
	public static Double geoLng;// 纬度
	private AlertDialog shareDailog;// 分享的弹框
	private ShareParams sp;// 分享需要的配置
	private Dialog newDialog;
	@SuppressLint("NewApi")
	private Handler handler = new Handler() {

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@SuppressLint("NewApi")
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case APP_UPDATE:
				// FIXME 提示更新
				VersionInfo info = (VersionInfo) msg.obj;
				if (info != null) {
					regularUpdates(info.getVersionname(), info.getUpdateinfo(),
							info.getFilepath(), info.getIs_update());
				}
				break;

			case EXIT_LOGIN:
				// 退出登录
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ActivityManager.getActivityManager(MainActivity.this).exit();
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
				// }
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
			case ORDER_NUM:
				// 得到订单数量
				String res = (String) msg.obj;
				try {
					if (res.isEmpty() || res == null) {
						return;
					}
				} catch (Exception e) {
					return;
				}

				try {
					JSONObject jo;
					jo = new JSONObject(res);
					if (jo.optInt("res") == 0) {
						try {
							if (jo.optInt("wait_receive_num") == 0) {
								tv_order_num.setVisibility(View.GONE);
							}
						} catch (Exception e) {
						}

						try {
							if (jo.optInt("wait_receive_num") > 0
									&& jo.optInt("wait_receive_num") < 10) {
								tv_order_num.setVisibility(View.VISIBLE);
								tv_order_num.setBackground(getResources()
										.getDrawable(R.drawable.red_dian));
								tv_order_num.setText(jo
										.optInt("wait_receive_num") + "");
							} else if (jo.optInt("wait_receive_num") >= 10
									&& jo.optInt("wait_receive_num") <= 99) {
								tv_order_num.setVisibility(View.VISIBLE);
								tv_order_num.setBackground(getResources()
										.getDrawable(R.drawable.red_dian1));
								tv_order_num.setText(jo
										.optInt("wait_receive_num") + "");
							} else if (jo.optInt("wait_receive_num") > 99) {
								tv_order_num.setVisibility(View.VISIBLE);
								tv_order_num.setBackground(getResources()
										.getDrawable(R.drawable.red_dian2));
								tv_order_num.setText("99+");
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

					} else if (jo.optInt("res") == 1) {
						ToastUtil.show(getApplicationContext(), "获取订单数量失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case CHECK_SHOP_INFO:
				// 店铺名称等的显示
				ShopInfo shopInfo = (ShopInfo) msg.obj;
				try {
					shopstate1 = shopInfo.getState();
					if (shopstate1.isEmpty()) {
						shopstate = "";
					} else if (shopstate1.equals("1")) {
						// shopstate = "启用";
						shopstate = "";
					} else if (shopstate1.equals("2")) {
						shopstate = "禁用";
					} else if (shopstate1.equals("3")) {
						shopstate = "审核中";
					} else if (shopstate1.equals("4")) {
						shopstate = "审核不通过";
					} else if (shopstate1.equals("6")) {
						shopstate = "审核中";
					}
				} catch (Exception e) {
					// TODO: handle exception
					shopstate = "";
				}

				if (obj.getShopsname().trim().length() <= 0) {

				} else {
					if (obj.getShopsname().trim().length() >= 13) {
						// 店铺名称大于等于13个字的时候
						titleTextView.setTextSize(12f);
					} else {
						titleTextView.setTextSize(18f);
					}

					if (shopstate.equals("101")) {

					} else {
						if (shopstate.isEmpty()) {
							titleTextView.setText(Html.fromHtml(obj
									.getShopsname()
									+ "<font color='#FF0000'>"
									+ " </font>"));
						} else {
							titleTextView.setText(Html.fromHtml(obj
									.getShopsname()
									+ "<font color='#FF0000'>"
									+ "(" + shopstate + ")" + " </font>"));
						}
					}
				}

				initAccountStatus();

				break;

			case GET_ACCOUNT_STATUS:
				// 在线收银台的状态
				proDialog.dismiss();
				status = msg.arg1;
				System.out.println("----------SettingActivity------------->"
						+ status);
				ElectronicAccountManager eam = ElectronicAccountManager
						.getInstance(MainActivity.this);
				eam.setElectronicAccountStatus(status);

				if (msg.arg1 == 4) {
					// 已通过
					// statusTextView.setText("已通过");
					iv_online.setVisibility(View.GONE);
				} else if (msg.arg1 == 8) {
					// 其他情况
					// statusTextView.setText("审核中");
					iv_online.setVisibility(View.VISIBLE);
					iv_online.setImageResource(R.drawable.online_checking);
				} else if (msg.arg1 == 9) {
					// statusTextView.setText("未通过");// 版本 >=3.00 8,9
					iv_online.setVisibility(View.VISIBLE);
					iv_online.setImageResource(R.drawable.online_check_notguo);
				} else if (msg.arg1 == 6) {
					// statusTextView.setText("审核中");// 版本 <3。00 6,7
					iv_online.setVisibility(View.VISIBLE);
					iv_online.setImageResource(R.drawable.online_checking);
				} else if (msg.arg1 == 7) {
					// statusTextView.setText("审核不通过");
					iv_online.setVisibility(View.VISIBLE);
					iv_online.setImageResource(R.drawable.online_check_notguo);
				} else if (msg.arg1 == 1 || msg.arg1 == 5) {
					// statusTextView.setText("未开通");
					iv_online.setVisibility(View.VISIBLE);
					iv_online.setImageResource(R.drawable.online_no_check);
				}
				try {
					if (shopstate1.equals("4") && msg.arg1 == 1) {
						if (ClientsUtil.getFirstLogin(getApplicationContext())) {
							ClientsUtil.setFirstLogin(getApplicationContext(),
									false);
							// 首次登陆 审核不通过
							rl_check.setVisibility(View.VISIBLE);
						} else {
							// 不是首次登陆
							rl_check.setVisibility(View.GONE);
						}
					} else if (shopstate1.equals("4") && msg.arg1 == 5) {
						if (ClientsUtil.getFirstLogin(getApplicationContext())) {
							ClientsUtil.setFirstLoginShopCheck(context, false);
							// 首次登陆
							rl_check.setVisibility(View.VISIBLE);
						} else {
							// 不是首次登陆
							rl_check.setVisibility(View.GONE);
						}
					} else if (shopstate1.equals("4") && msg.arg1 != 1
							&& msg.arg1 != 5) {
						// 审核不通过
						if (ClientsUtil.getFirstLogin(getApplicationContext())) {
							ClientsUtil.setFirstLogin(getApplicationContext(),
									false);
							// 首次登陆
							rl_check.setVisibility(View.VISIBLE);
						} else {
							// 不是首次登陆
							rl_check.setVisibility(View.GONE);
						}
					} else if (!shopstate1.equals("4")) {
						if (msg.arg1 == 1 || msg.arg1 == 5) {
							if (ClientsUtil
									.geFirstLoginShopChec(getApplicationContext())) {
								ClientsUtil.setFirstLoginShopCheck(
										getApplicationContext(), false);
								conVerView.setVisibility(View.VISIBLE);
							} else {
								iv_online
										.setImageResource(R.drawable.online_no_check);
								conVerView.setVisibility(View.GONE);
							}
						}

					}
				} catch (Exception e) {
				}

				String res0 = (String) msg.obj;
				try {
					JSONObject jObject = new JSONObject(res0);
					if (jObject.optInt("res") == 0) {
						JSONObject array = jObject.getJSONObject("data");
						phone1 = array.getString("phone");
						shopsName = array.getString("shopsName");
					} else {
						shopsName = jObject.getString("shopsName");
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				System.out.println("----------shopsName------>" + shopsName);
				break;
			case SELECT_BANK:
				// 在线收银台 查询
				proDialog.dismiss();

				if (msg.arg1 == 0) {

					String res3 = (String) msg.obj;

					JSONObject j = null;
					try {
						j = new JSONObject(res3);
						// 店主姓名
						String userIdName = j.getString("userIdName");
						// 身份证号码
						String userIdNo = j.getString("userIdNo");
						// 电子邮箱
						String Email = j.getString("Email");
						// 推荐码
						String ReferrerCode = j.getString("ReferrerCode");
						Intent intent1 = new Intent();
						intent1.setClass(getApplicationContext(),
								OnLineElecActivity.class);
						intent1.putExtra("type0", "");
						intent1.putExtra("Mobile", phone1);
						intent1.putExtra("userIdName", userIdName);
						intent1.putExtra("userIdNo", userIdNo);
						intent1.putExtra("Email", Email);
						intent1.putExtra("ReferrerCode", ReferrerCode);
						startActivityForResult(intent1, REVIEW_ONE);
					} catch (Exception e) {

					}
				} else {

					if (msg.arg1 == 10 || msg.arg1 == 11 || msg.arg1 == 12) {
						System.out
								.println("--------------msg.arg2------------->"
										+ msg.arg1);
						JSONObject j1 = null;
						try {
							String res2 = (String) msg.obj;
							j1 = new JSONObject(res2);
							int addtime = j1.optInt("addtime");
							int submit_time = j1.optInt("submit_time");// 审核提交时间
							int examine_time = j1.optInt("examine_time");// 审核时间
							int refuse_time = j1.optInt("refuse_time");// 拒绝时间
							int success_time = j1.optInt("success_time");// 开通成功时间
							String error = j1.optString("msg");

							Intent intent1 = new Intent();
							intent1.setClass(getApplicationContext(),
									OnlineCheckActivity.class);
							intent1.putExtra("type0", "");
							intent1.putExtra("type", msg.arg1 + "");
							intent1.putExtra("msg", error + "");
							intent1.putExtra("time",
									DateUtil.getFormatedDate_1(addtime + ""));
							if (submit_time == 0) {
								intent1.putExtra("submit_time", "");
							} else {
								intent1.putExtra(
										"submit_time",
										DateUtil.getFormatedDate_1(submit_time
												+ ""));
							}

							if (examine_time == 0) {
								intent1.putExtra("examine_time", "");
							} else {
								intent1.putExtra(
										"examine_time",
										DateUtil.getFormatedDate_1(examine_time
												+ ""));
							}

							if (refuse_time == 0) {
								intent1.putExtra("refuse_time", "");
							} else {
								intent1.putExtra(
										"refuse_time",
										DateUtil.getFormatedDate_1(refuse_time
												+ ""));
							}

							if (success_time == 0) {
								intent1.putExtra("success_time", "");
							} else {
								intent1.putExtra(
										"success_time",
										DateUtil.getFormatedDate_1(success_time
												+ ""));
							}

							try {
								if (!j1.isNull("Mobile")) {
									String phone1 = j1.getString("Mobile");
									intent1.putExtra("Mobile", phone1);
								}
							} catch (Exception e) {
							}

							startActivityForResult(intent1, REVIEW_TWO);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					} else {
						JSONObject j2 = null;
						try {
							String res1 = (String) msg.obj;
							j2 = new JSONObject(res1);
							String error = j2.optString("msg");
							ToastUtil.show(getApplicationContext(), error + "");
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}
				break;
			case MESSAGE_NUM:
				//  消息列表的已读未读
				List<Messages> list = (List<Messages>) msg.obj;
				statusFilter(list, msg);
				break;
			default:
				obj = (FristPage) msg.obj;
				// proDialog.dismiss();
				if (obj != null) {
					if (obj.getShopsname().trim().length() >= 13
							&& obj.getShopsname().trim().length() <= 15) {
						titleTextView.setTextSize(14f);
					} else if (obj.getShopsname().trim().length() > 15) {
						titleTextView.setTextSize(14f);
					} else {
						titleTextView.setTextSize(18f);
					}

					if (shopstate.equals("101")) {

					} else {
						if (shopstate.isEmpty()) {
							titleTextView.setText(Html.fromHtml(obj
									.getShopsname()
									+ "<font color='#FF0000'>"
									+ " </font>"));
						} else {
							titleTextView.setText(Html.fromHtml(obj
									.getShopsname()
									+ "<font color='#FF0000'>"
									+ "(" + shopstate + ")" + " </font>"));
						}
					}

					// titleTextView.setText(obj.getShopsname());

					shopName = obj.getShopsname();
					shopLocation = obj.getLocation();
					final String imgPath = obj.getPicpath();
					System.out.println("-----imgpath------->" + imgPath);
					logoNetPath = imgPath;
					if (imgPath != null && imgPath.length() > 0) {
						// logoLocalPath = getsaveimage(imgPath);
						new Thread(new Runnable() {

							@Override
							public void run() {
								InputStream stream = HttpRequestUtil
										.getStreamFromURL(imgPath);
								Bitmap bitmap = null;
								try {
									bitmap = ImageUtil
											.inputStreamToBitmap(stream);
									bitmap = compressImage(bitmap);
									String saveDir = Environment
											.getExternalStorageDirectory()
											+ "/EServiceStore";
									File dir = new File(saveDir);
									if (!dir.exists()) {
										dir.mkdir();
									}
									ImageUtil.deletePicture(saveDir,
											"eshopLogo.jpg");
									if (ImageUtil.savePictureToSDCard(bitmap,
											saveDir, "eshopLogo.jpg")) {
										logoLocalPath = saveDir
												+ "/eshopLogo.jpg";
									}

								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}).start();

						chekShopInfo();
					}

				}
				break;
			}

		}
	};

	/**
	 * 检查更新
	 * 
	 * @param vName
	 *            名称
	 * @param info
	 *            版本号
	 * @param path
	 *            app路径
	 * @param isUpdate
	 *            是否更新
	 */
	private void regularUpdates(String vName, String info, final String path,
			String isUpdate) {

		myDialog.show();
		myDialog.getWindow().setContentView(R.layout.regular_updates_dialog);
		TextView versionTV = (TextView) myDialog.getWindow().findViewById(
				R.id.versiontextView);
		TextView infoTV = (TextView) myDialog.getWindow().findViewById(
				R.id.updateinfotextview);
		Button but = (Button) myDialog.getWindow().findViewById(
				R.id.cancelbutton);
		System.out.println("-------isUpdate-------->"
				+ Integer.parseInt(isUpdate));
		if (Integer.parseInt(isUpdate) == 0) {// 不强制更新
			but.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		} else {
			but.setVisibility(View.GONE);

			myDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if ((System.currentTimeMillis() - exitTime) > 2000) {
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.exit_app_prompt),
									Toast.LENGTH_SHORT).show();
							exitTime = System.currentTimeMillis();
						} else {
							android.os.Process.killProcess(android.os.Process
									.myPid());
							System.exit(0);
						}

					}
					return false;
				}
			});
		}

		versionTV.setText(vName);
		infoTV.setText(info);
		myDialog.getWindow().findViewById(R.id.downloadbutton)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (android.os.Environment.getExternalStorageState()
								.equals(android.os.Environment.MEDIA_MOUNTED)) {
							Intent it = new Intent(MainActivity.this,
									NotificationUpdateActivity.class);
							it.putExtra("apkpath", path);
							SettingsActivity.isUpdate = true;
							startActivity(it);
						} else {
							ToastUtil.show(MainActivity.this, "请先挂载SD卡");
						}
						myDialog.dismiss();
					}
				});
		but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
	}

	// 禁用状态的弹框
	protected void disableUser() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.disable_user, null);
		final Dialog dialog = new Dialog(MainActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		dialog.show();
		exit();
	}

	/**
	 * 退出账号
	 * 
	 * @param view
	 *            控件
	 */
	public void exit() {
		// FIXME 清空登陆信息
		LoginManager lm = LoginManager.getInstance(MainActivity.this);
		lm.delLogin();
		// if (RongIM.getInstance() != null)
		// RongIM.getInstance().disconnect(false);
		// proDialog.show();
		System.out.println("-----shopsid---->" + lm.getLoginId());
		// ToastUtil.show(SettingsActivity.this, "退出成功！");
		// if (!NetConn.checkNetwork(MainActivity.this)) {
		// } else {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SettingService service = new SettingService(MainActivity.this);
				boolean flag = service.exitLogin(shopsid);
				Message message = handler.obtainMessage(EXIT_LOGIN);
				message.arg1 = flag == true ? 0 : 1;
				handler.sendMessage(message);
			}
		}).start();
		// }
		// loginOutDialog();
	}

	/**
	 * 检查版本更新
	 */
	private void checkVersionInfo() {

		// if (!NetConn.checkNetwork(MainActivity.this)) {
		// } else {
		mVersionCode = SystemUtil.getVersionCode(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				SettingService service = new SettingService(MainActivity.this);
				VersionInfo info = service.getUpdateInfo(mVersionCode);
				Message message = handler.obtainMessage(APP_UPDATE);
				message.obj = info;
				handler.sendMessage(message);
			}
		}).start();
		// }

	}

	/**
	 * 图片压缩
	 * 
	 * @param image
	 *            图片
	 * @return
	 */
	public Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	// 销毁
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	// 暂停
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LoginActivity.loginString = "";
		this.getApplicationContext().unregisterReceiver(rongReceiver);
	}

	private HomeListener mHomeWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_two);

		context = this;
		mNotificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		MessageManager.getInstance().initialize(
				MainActivity.this.getApplicationContext());
		ShareSDK.initSDK(this);
		LoginManager lm = LoginManager.getInstance(MainActivity.this);
		shopsid = lm.getLoginId();
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		iv_online = (ImageView) findViewById(R.id.iv_online);
		tv_message_num = (TextView) findViewById(R.id.tv_message_num);
		tv_message_num.setVisibility(View.INVISIBLE);
		newDialog = showNewDialog();
		initView();
		System.out.println("TelephoneUtil.getImei(context)"
				+ TelephoneUtil.getImei(context));
		if (LoginManager.getInstance(context).getToken() != null) {
			connectRong();
		}
		checkVersionInfo();// 检查更新
		initFinger();// 在这添加是因为 在退出这个应用的时候，再进来的时候 要走这个方法
		if (ClientsUtil.getFirstLoginSettingKuang(getApplicationContext())) {
			// 第一次登录成功之后，设置成false
			ClientsUtil.setFirstLoginSettingKuang(getApplicationContext(),
					false);
		}
		if (ClientsUtil.getFirstSecondSettingKuang(getApplicationContext())) {
			// 第二次登录
			if (LoginActivity.secondtime == 1) {
				try {
					ClientsUtil.setFirstSecondSettingKuang(
							getApplicationContext(), false);
					GesturePasswordDBService dao = new GesturePasswordDBService(
							this);
					String nullString = dao.getAllSearchTrace(
							getApplicationContext(), shopsid + "");
					if (nullString.equals(null)) {
						showSettingGestureDialog();
					}

				} catch (Exception e) {
					// 没有手势密码的情况
					showSettingGestureDialog();
				}
			} else {

			}
		}

	}

	/**
	 * 显示对话框
	 * 
	 * @return
	 */
	private Dialog showNewDialog() {
		Dialog dialog = new Dialog(MainActivity.this,
				R.style.theme_dialog_alert);
		return dialog;
	}

	@SuppressWarnings("deprecation")
	private void showSettingGestureDialog() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_gestrue_dialogview, null);
		layout.setMinimumWidth((int) (MainActivity.this.getWindowManager()
				.getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
		newDialog.setContentView(layout);
		newDialog.setCancelable(true);
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						newDialog.dismiss();
					}
				});
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newDialog.dismiss();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						CreateGesturePasswordActivity.class);
				startActivity(intent);
			}
		});
		newDialog.show();// 显示AlertDialog
	}

	/**
	 * 店铺信息的显示，例如店铺名称，店铺状态，等
	 */
	private void chekShopInfo() {
		// if (!NetConn.checkNetwork(MainActivity.this)) {
		// } else {
		new ChekShopInfoAsyc().execute();
		// }
	}

	/**
	 * 初始化订单数量
	 */
	private void initOrderNum() {
		new OrderAsyc().execute();
	}

	/**
	 * 订单数量的接口
	 * 
	 * @author chenyamei
	 * 
	 */
	private class OrderAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(MainActivity.this);
			Message message = handler.obtainMessage(ORDER_NUM);
			// message.arg1 = CURRENT_OPT_STATUS;
			String res = orderService.getOrderNum(shopsid);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	/**
	 * 店铺基本信息的显示
	 * 
	 * @author chenyamei
	 * 
	 */
	private class ChekShopInfoAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			SettingService settingService = new SettingService(
					MainActivity.this);
			Message message = handler.obtainMessage(CHECK_SHOP_INFO);
			// message.arg1 = CURRENT_OPT_STATUS;
			ShopInfo res = settingService.checkShopInfo2(shopsid);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		/** 注册广播 */
		IntentFilter intentFilterRong = new IntentFilter(actionRong);
		this.getApplicationContext().registerReceiver(rongReceiver,
				intentFilterRong);
		//  得到首页的基本信息
		if (!NetConn.checkNetwork(MainActivity.this)) {
		} else {
			initData();
			// 初始化订单数量
			initOrderNum();
			// 店铺禁用
			initDisable();
			// 调用当前位置
			initMap();
		}
	}

	/**
	 * 
	 * TODO<判断是否有手势密码>
	 * 
	 * @throw
	 * @return void
	 */
	private void initFinger() {

		try {
			if (getIntent().getStringExtra("login").equals("")) {
				// 登录进来的解锁不需要手势密码
			} else if (UnlockGesturePasswordActivity.unLockString
					.equals("lock")) {
			} else {

				GesturePasswordDBService dao = new GesturePasswordDBService(
						this);
				String nullString = dao.getAllSearchTrace(
						getApplicationContext(), shopsid + "");
				if (nullString.equals("")) {
					// 关闭手势密码走的流程
				} else {
					if (ClientsUtil.geFirstLoginGesture(context)) {
						ClientsUtil.setFirstLoginGesture(context, false);
					} else {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								UnlockGesturePasswordActivity.class);
						startActivity(intent);
					}
				}
			}

		} catch (Exception e) {
			ClientsUtil.setFirstLoginGesture(context, false);
		}

	}

	/**
	 * 
	 * TODO 初始化地图
	 * 
	 * @throw
	 * @return void
	 */
	private void initMap() {
		mapView = (MapView) findViewById(R.id.map_view);
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_blue));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
		myLocationStyle.strokeWidth(0f);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mAMapLocationManager = LocationManagerProxy
				.getInstance(MainActivity.this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationEnabled(true);// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
		aMap.getUiSettings().setZoomGesturesEnabled(false);
	}

	/**
	 * 
	 * TODO 判断消息列表是否有消息
	 * 
	 * @throw
	 * @return void
	 */
	private void initMessageNum() {
		// if (!NetConn.checkNetwork(MainActivity.this)) {
		// } else {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getCurMsg();
			}
		}).start();
		// }
	}

	/**
	 * 
	 * TODO 获取消息
	 * 
	 * @throw
	 * @return void
	 */
	private void getCurMsg() {
		MessageService messageService = new MessageService(MainActivity.this);
		Message msg = handler.obtainMessage(MESSAGE_NUM);
		Bundle bundle = new Bundle();
		MessageList list = new MessageList();
		String res = messageService.getMessageList(shopsid, 2, 15, geoLng,
				geoLat);
		list = JsonUtils.fromJson(res, MessageList.class);
		msg.obj = list.data;
		msg.setData(bundle);
		msg.sendToTarget();
	}

	/**
	 * 通过数据类型展示数据
	 * 
	 * @param newest
	 *            承载消息数据的列表
	 * @param type
	 *            类型
	 * @param msg
	 *            message的数据
	 */
	private void statusFilter(List<Messages> newest, Message msg) {
		if (newest == null || newest.size() == 0) {
			newest = searchAllData();
			if (newest == null || newest.size() == 0) {
				tv_message_num.setVisibility(View.INVISIBLE);
			}
			for (int i = 0; i < newest.size(); i++) {
				if (newest.get(i).getIs_readmessage().equals("1")) {
					// 存在未读状态
					tv_message_num.setVisibility(View.VISIBLE);
				} else {
					tv_message_num.setVisibility(View.INVISIBLE);
				}
			}
		} else {
			if (newest != null && !newest.isEmpty()) {
				saveData(newest);
				newest = searchAllData();
				for (int i = 0; i < newest.size(); i++) {
					if (newest.get(i).getIs_readmessage().equals("1")) {
						// 存在未读状态
						tv_message_num.setVisibility(View.VISIBLE);
					}
					// else {
					// tv_message_num.setVisibility(View.INVISIBLE);
					// }
				}
			}
		}

	}

	/**
	 * 
	 * TODO 店铺是否被禁用
	 * 
	 * @throw
	 * @return void
	 */
	private void initDisable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SettingService service = new SettingService(MainActivity.this);
				String res = service.getDisableStatus(shopsid);
				Message message = handler.obtainMessage(DISTABLE);
				try {
					jObject = new JSONObject(res);
					if (jObject.optInt("res") == 0) {
						int flag = jObject.optInt("state");
						try {
							message.arg1 = flag;
							message.arg2 = 0;
						} catch (Exception e) {
							// TODO: handle exception
						}

						handler.sendMessage(message);
					} else {
						error = jObject.getString("msg");
						try {
							message.arg2 = 1;
							message.obj = error;
						} catch (Exception e) {
							// TODO: handle exception
						}

						handler.sendMessage(message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * 
	 * TODO 初始化在线收银台
	 * 
	 * @throw
	 * @return void
	 */
	private void initAccountStatus() {
		System.out.println("--------initAccountStatus-------->");
		new Thread(new Runnable() {

			@Override
			public void run() {
				SettingService service = new SettingService(MainActivity.this);
				String res = service.getAccountStatus(shopsid);
				Message message = handler.obtainMessage(GET_ACCOUNT_STATUS);
				try {
					jObject = new JSONObject(res);
					if (jObject.optInt("res") == 0) {
						jObject = jObject.getJSONObject("data");
						if (!jObject.isNull("audit")) {
							String audit = jObject.getString("audit");
						}
						if (!jObject.isNull("returnmsg")) {
							String returnmsg = jObject.getString("returnmsg");
						}
						if (!jObject.isNull("jumpurl")) {
							jumpurl = jObject.getString("jumpurl");
						}

						int flag = jObject.optInt("accountstate");
						message.arg1 = flag;
						message.obj = res;
						handler.sendMessage(message);
					} else {
						error = jObject.getString("msg");
						message.obj = res;
						handler.sendMessage(message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * 
	 * TODO 得到首页的基本信息
	 * 
	 * @throw
	 * @return void
	 */
	private void initData() {
		if (!proDialog.isShowing()) {
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				FristPageService fristPageService = new FristPageService(
						MainActivity.this);
				FristPage fristPage = fristPageService.getFristPage(shopsid);
				Message message = handler.obtainMessage();
				message.obj = fristPage;
				handler.sendMessage(message);
			}
		}).start();
	}

	@SuppressLint("NewApi")
	/**
	 * 
	 * TODO 初始化布局
	 * @throw 
	 * @return void
	 */
	private void initView() {
		myDialog = new AlertDialog.Builder(this).create();
		myDialog.setCancelable(false);
		myDialog.setCanceledOnTouchOutside(false);

		titleTextView = (TextView) findViewById(R.id.title_font);
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		tv_order_num.setTextColor(Color.WHITE);

		// 扫码 结账
		main_gridview_item_layout_text2 = (TextView) findViewById(R.id.main_gridview_item_layout_text2);
		main_gridview_item_layout_text2.setText("扫码结账");
		rl_check = (RelativeLayout) findViewById(R.id.rl_check);
		View view2 = findViewById(R.id.main_gridview_item_layout2);
		View view3 = findViewById(R.id.main_gridview_item_layout3);
		View view4 = findViewById(R.id.main_gridview_item_layout4);
		View view5 = findViewById(R.id.main_gridview_item_layout5);
		View view6 = findViewById(R.id.main_gridview_item_layout6);
		View view7 = findViewById(R.id.main_gridview_item_layout7);
		conVerView = (CoverView) findViewById(R.id.conVerView);

		MyClickListener myClickListener = new MyClickListener();
		view2.setOnClickListener(myClickListener);
		view3.setOnClickListener(myClickListener);
		view4.setOnClickListener(myClickListener);
		view5.setOnClickListener(myClickListener);
		view6.setOnClickListener(myClickListener);
		view7.setOnClickListener(myClickListener);

		view2.getBackground().setAlpha(170);
		view3.getBackground().setAlpha(170);
		view4.getBackground().setAlpha(170);
		view5.getBackground().setAlpha(170);
		view6.getBackground().setAlpha(170);
		view7.getBackground().setAlpha(170);

		settingImageView = (ImageView) findViewById(R.id.imageView_setting);
		shareImageView = (ImageView) findViewById(R.id.imageView_share);
		messaheImageView = (ImageView) findViewById(R.id.imageView_message);
		imageView_twoCode = (ImageView) findViewById(R.id.imageView_twoCode);
		ImageViewClickListener listener = new ImageViewClickListener(this);
		settingImageView.setOnClickListener(listener);
		messaheImageView.setOnClickListener(listener);
		imageView_twoCode.setOnClickListener(listener);
		shareImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 分享
				// showShare(true, null);
				showShareDailog();
			}
		});

		tv_order_num.setVisibility(View.INVISIBLE);

		// 点击店铺名称，进入资质变更页面
		titleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						ShopChangeActivity.class);
				startActivity(intent);
			}
		});

		conVerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				conVerView.setVisibility(View.GONE);
				iv_online.setImageResource(R.drawable.online_no_check);
			}
		});

		rl_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (shopstate1.equals("4") && status == 1) {
					// 未开通 并且 审核未通过
					rl_check.setVisibility(View.GONE);
					if (ClientsUtil.geFirstLoginShopChec(context)) {
						conVerView.setVisibility(View.VISIBLE);
						ClientsUtil.setFirstLoginShopCheck(context, false);
					}
					iv_online.setImageResource(R.drawable.online_no_check);
				} else if (shopstate1.equals("4") && status == 5) {
					// 未开通 并且 审核未通过
					rl_check.setVisibility(View.GONE);
					if (ClientsUtil.geFirstLoginShopChec(context)) {
						conVerView.setVisibility(View.VISIBLE);
						ClientsUtil.setFirstLoginShopCheck(context, false);
					}
					iv_online.setImageResource(R.drawable.online_no_check);
				} else if (shopstate1.equals("4") && status != 1 && status != 5) {
					// 审核不通过 并不是 未开通的状态
					rl_check.setVisibility(View.GONE);
					conVerView.setVisibility(View.GONE);
				}
			}
		});
	}

	String shopstate2;

	/**
	 * 
	 * TODO 跳转到推荐页面
	 * 
	 * @throw
	 * @return void
	 * @View view 控件
	 */
	public void recommend_onClick(View view) {
		Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	/**
	 * 点击修改店铺门面
	 * 
	 * @param view
	 *            控件
	 */
	public void change_onClick(View view) {
		Intent intent = new Intent(this, ShopInfoActivity.class);
		intent.putExtra("shopsid", shopsid);
		startActivity(intent);
	}

	/**
	 * 分享对话框
	 */
	public void showShareDailog() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.mine_or_found_share, null);

		LinearLayout ll_share_wxf = (LinearLayout) view
				.findViewById(R.id.ll_share_wxf);
		LinearLayout ll_share_wxq = (LinearLayout) view
				.findViewById(R.id.ll_share_wxq);
		LinearLayout ll_share_sina = (LinearLayout) view
				.findViewById(R.id.ll_share_sina);
		LinearLayout ll_share_qq = (LinearLayout) view
				.findViewById(R.id.ll_share_qq);
		LinearLayout ll_share_qzen = (LinearLayout) view
				.findViewById(R.id.ll_share_qzen);

		ll_share_wxf.setOnClickListener(this);
		ll_share_wxq.setOnClickListener(this);
		ll_share_sina.setOnClickListener(this);
		ll_share_qq.setOnClickListener(this);
		ll_share_qzen.setOnClickListener(this);

		shareDailog = new AlertDialog.Builder(this).create();
		shareDailog.getWindow().setGravity(Gravity.BOTTOM);
		shareDailog.show();
		shareDailog.getWindow().setContentView(view);
		shareDailog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		shareDailog.getWindow().findViewById(R.id.tv_cancle)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						shareDailog.dismiss();
					}
				});
		shareDailog.setCanceledOnTouchOutside(true);
	}

	private class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (arg0.getId()) {
			case R.id.main_gridview_item_layout2:
				// 扫码结账
				intent.setClass(MainActivity.this, ScanActivity.class);

				break;
			case R.id.main_gridview_item_layout3:
				// 外卖/上门
				intent.setClass(MainActivity.this, GoodsActivity.class);
				intent.putExtra("tag", "state");
				break;
			case R.id.main_gridview_item_layout4:
				// 订单管理
				intent.setClass(MainActivity.this, OrdersActivity.class);
				intent.putExtra("tag", 0);
				break;
			case R.id.main_gridview_item_layout5:
				// 在线收银台
				if (status == 4) {
					intent.setClass(getApplicationContext(),
							ElectronicAccountActivity.class);
					intent.putExtra("url", jumpurl);
				} else {
					Select_bank();
				}
				break;
			case R.id.main_gridview_item_layout6:
				// 我的客户
				intent.setClass(MainActivity.this, ClientsActivity.class);
				break;
			case R.id.main_gridview_item_layout7:
				intent.setClass(MainActivity.this, LineChartActivity.class);
				break;
			default:
				break;
			}
			if (intent.getComponent() != null) {
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}

		}
	}

	/**
	 * 电子商务查询接口
	 */
	private void Select_bank() {

		if (!NetConn.checkNetwork(MainActivity.this)) {
		} else {
			new OnLineAsync().execute();
		}

	}

	/**
	 * 在线收银台接口
	 * 
	 * @author chenyamei
	 * 
	 */
	private class OnLineAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			SettingService settingService = new SettingService(
					MainActivity.this);
			Message message = handler.obtainMessage(SELECT_BANK);
			String res = settingService.getSelectBank(shopsid, phone1);
			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				int flag = jo.optInt("res");
				int addtime = jo.optInt("addtime");
				int submit_time = jo.optInt("submit_time");// 审核提交时间
				int examine_time = jo.optInt("examine_time");// 审核时间
				int refuse_time = jo.optInt("refuse_time");// 拒绝时间
				int success_time = jo.optInt("success_time");// 开通成功时间

				if (flag == 0) {
					message.arg1 = flag;
					message.arg2 = addtime;
					message.obj = res;
					handler.sendMessage(message);
				} else {
					String msg = jo.optString("msg");
					message.arg1 = flag;
					message.arg2 = addtime;
					message.obj = res;
					handler.sendMessage(message);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private int getKeyTime = 0; // 获取key的次数 减少服务器压力

	/**
	 * 
	 * TODO 融云的连接
	 * 
	 * @throw
	 * @return void
	 */
	private void connectRong() {
		RongIM.connect(LoginManager.getInstance(context).getToken(),
				new RongIMClient.ConnectCallback() {

					@Override
					public void onSuccess(String s) {
						// 此处处理连接成功。
						Log.d("Connect:", "Login successfully.");
						isConnect = true;
						if (RongIM.getInstance() != null) {
							RongIM.getInstance().setReceiveMessageListener(
									new OnReceiveMessageListener() {

										@Override
										public void onReceived(
												io.rong.imlib.RongIMClient.Message arg0,
												int arg1) {
											// TODO Auto-generated method stub
											String userId = arg0
													.getSenderUserId();
											String title = "";
											setNewMessageNotification(context,
													"", userId, title);
										}

									});
						}
					}

					@Override
					public void onError(ErrorCode errorCode) {
						// 此处处理连接错误。
						Log.d("Connect:", "Login failed.");
						if (errorCode.getValue() == ErrorCode.TOKEN_INCORRECT
								.getValue()) {
							if (getKeyTime < 5) {

								Looper.prepare();
								GetTokenTask tokenTask = new GetTokenTask(
										context, String.valueOf(LoginManager
												.getInstance(context)
												.getLoginId()));
								tokenTask
										.setOnGetTokenLisener(MainActivity.this);
								tokenTask.execute();
								Looper.loop();
								getKeyTime++;
							}
						}
					}
				});
	}

	/**
	 * 连接融云
	 */
	private BroadcastReceiver rongReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			System.out.println("----action----->" + intent.getAction());
			if (intent.getAction().equals(actionRong)) {
				connectRong();
			}
		}
	};

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				// 重写物理返回键
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出应用!",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					// unregisterReceiver(receiver);
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(0);
				}
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 初始化通知栏
	 * 
	 * @param context
	 *            全局变量
	 * @param targetUserId
	 *            id
	 * @param title
	 *            名称
	 */
	private void initNotify(Context context, String targetUserId, String title) {
		Uri uri = Uri
				.parse("rong://" + context.getApplicationInfo().packageName)
				.buildUpon()
				.appendPath("conversation")
				.appendPath(
						RongIMClient.ConversationType.PRIVATE.getName()
								.toLowerCase())
				.appendQueryParameter("targetId", targetUserId)
				.appendQueryParameter("title", title).build();
		Intent intent = new Intent("android.intent.action.VIEW", uri);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle("来新消息了").setContentIntent(contentIntent)
				.setTicker("来新消息了")// 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				.setSmallIcon(R.drawable.ic_launcher);
	}

	/**
	 * 新建新的消息
	 * 
	 * @param context
	 *            全局变量
	 * @param content
	 *            消息内容
	 * @param targetUserId
	 *            id
	 * @param title
	 *            名称
	 */
	private void setNewMessageNotification(Context context, String content,
			String targetUserId, String title) {
		initNotify(context, targetUserId, title);
		mBuilder.setContentTitle("聊天消息").setContentText("新的聊天消息")
				.setAutoCancel(true);// 通知首次出现在通知栏，带上升动画效果的
		mNotificationManager.notify(NOTIFY_ID, mBuilder.getNotification());
	}

	/**
	 * 获取到 token
	 */
	@Override
	public void OnGetToken(RongIMBean rongBean) {
		// TODO Auto-generated method stub
		if (rongBean != null) {
			LoginManager.getInstance(context).saveRongInfo(
					rongBean.getUserId(), rongBean.getToken());
			connectRong();
		}
	}

	/**
	 * startActivityforresult 对应的返回方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			initAccountStatus();
		}
	}

	/**
	 * 查找出全部的message
	 * 
	 * @return 返回所有的message列表
	 */
	private List<Messages> searchAllData() {
		MessageDBService service = new MessageDBService(getApplicationContext());
		return service.getAllMsgs();
	}

	/**
	 * 更新message所有的已读未读
	 * 
	 * @param list
	 *            message列表
	 * @param is_read
	 *            已读 2，未读 1
	 */
	public void updateData(List<Messages> list, String is_read) {
		MessageDBService service = new MessageDBService(getApplicationContext());
		service.updateIsRead(list, is_read);
	}

	/**
	 * 删除 某个message
	 * 
	 * @param list
	 *            message列表
	 */
	private void removeData(List<Messages> list, String id) {
		MessageDBService service = new MessageDBService(getApplicationContext());
		service.deleteMsgById(id);
	}

	/**
	 * 存储messagelist到本地
	 * 
	 * @param list
	 */
	private void saveData(List<Messages> list) {
		if (list != null) {
			MessageDBService service = new MessageDBService(
					getApplicationContext());
			service.insertData(list);
		}
	}

	/**
	 * 更新某一条消息的已读未读
	 * 
	 * @param id
	 *            list中的id
	 * @param is_read
	 *            已读未读状态
	 */
	private void updateDataById(String id, String is_read) {
		MessageDBService service = new MessageDBService(getApplicationContext());
		service.updateIsReadById(id, is_read);
	}

	/**
	 * 地图活跃状态
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 地图关闭
	 */
	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	/**
	 * 地图改变位置
	 * 
	 * @param Location
	 *            location  经纬度
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	/**
	 * 状态改变
	 * 
	 * @param provider
	 *            提供者
	 * @param status
	 *            状态
	 * @param extras
	 *            返回的参数
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * 提供服务
	 * 
	 * @param provider
	 *            提供者
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * 关闭服务
	 * 
	 * @param provider
	 *            提供者
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * 地图改变
	 * 
	 * @param AMapLocation
	 *            amapLocation  经纬度
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null && mListener != null) {
			geoLat = amapLocation.getLatitude();
			geoLng = amapLocation.getLongitude();
			deactivate();
			// 判断消息列表是否有消息
			initMessageNum();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_share_wxf:// 分享微信好友
			sp = new ShareParams();
			setShareInfo();
			Platform wechat = ShareSDK.getPlatform(MainActivity.this,
					Wechat.NAME);
			wechat.setPlatformActionListener(MainActivity.this); // 设置分享事件回调
			sp.setShareType(Platform.SHARE_WEBPAGE);
			wechat.share(sp);
			break;
		case R.id.ll_share_wxq:// 分享微信朋友圈
			sp = new ShareParams();
			setShareInfo();
			Platform wechatMoments = ShareSDK.getPlatform(MainActivity.this,
					WechatMoments.NAME);
			wechatMoments.setPlatformActionListener(MainActivity.this); // 设置分享事件回调
			sp.setShareType(Platform.SHARE_WEBPAGE);
			wechatMoments.share(sp);
			break;
		case R.id.ll_share_sina:// 分享新浪微博
			sp = new ShareParams();
			// setShareInfo();
			setShareInfoSina();
			Platform weibo = ShareSDK.getPlatform(MainActivity.this,
					SinaWeibo.NAME);
			if (weibo.isClientValid()) {
				System.out.println("安装了新浪微博");
			} else {
				System.out.println("没有安装了新浪微博");
			}
			if (weibo.isAuthValid()) {
				weibo.removeAccount(true);
				ShareSDK.removeCookieOnAuthorize(true);
			}
			sp.setShareType(Platform.SHARE_WEBPAGE);
			weibo.setPlatformActionListener(MainActivity.this); // 设置分享事件回调
			weibo.share(sp);
			break;
		case R.id.ll_share_qq:// 分享qq好友
			sp = new ShareParams();
			setShareInfo();
			sp.setShareType(Platform.SHARE_WEBPAGE);
			Platform qq = ShareSDK.getPlatform(MainActivity.this, QQ.NAME);
			qq.setPlatformActionListener(MainActivity.this); // 设置分享事件回调
			qq.share(sp);
			break;
		case R.id.ll_share_qzen:// 分享qq空间
			sp = new ShareParams();
			setShareInfo();
			sp.setShareType(Platform.SHARE_WEBPAGE);
			Platform qzone = ShareSDK
					.getPlatform(MainActivity.this, QZone.NAME);
			qzone.setPlatformActionListener(MainActivity.this); // 设置分享事件回调
			qzone.share(sp);
			break;
		}
	}

	// /**
	// * 分享
	// *
	// * @param silent
	// * 是否分享
	// * @param platform
	// * 分享的平台
	// */
	// private void showShare(boolean silent, String platform) {
	// String content = "我在" + shopLocation + "开店，店名：" + shopName
	// + ",赶紧下载客户端http://1fuwu.com.cn/";
	// final OnekeyShare oks = new OnekeyShare(shopsid, content);
	// oks.setTitle("社区e商户分享");
	// oks.setTitleUrl("http://1fuwu.com.cn/");
	// oks.setText(content);
	// System.out.println("------本地图片路径------>" + logoLocalPath);
	// if (logoLocalPath != null && logoLocalPath != "") {
	// oks.setImagePath(logoLocalPath);
	// }
	// if (logoNetPath != null && logoNetPath != "") {
	// oks.setImageUrl(logoNetPath);
	// }
	//
	// oks.setUrl("http://1fuwu.com.cn/");
	// oks.setSite(getBaseContext().getString(R.string.app_name));
	// oks.setSiteUrl("http://1fuwu.com.cn/");
	// oks.setSilent(silent);
	// if (platform != null) {
	// oks.setPlatform(platform);
	// }
	//
	// oks.show(MainActivity.this);
	// }
	/**
	 * 分享内容
	 */
	private void setShareInfo() {

		if (logoNetPath != null && logoNetPath != "") {
			sp.setImageUrl(logoNetPath);
		}
		String content = "我在" + shopLocation + "开店，店名：" + shopName
				+ ",赶紧下载客户端http://1fuwu.com.cn/";
		sp.setTitle("社区e商户");
		sp.setTitleUrl("http://1fuwu.com.cn/");
		sp.setUrl("http://1fuwu.com.cn/");
		sp.setSite("社区e商户");
		sp.setSiteUrl("http://1fuwu.com.cn/");
		sp.setText(content);
	}

	/**
	 * 微博分享内容
	 */
	private void setShareInfoSina() {

		// if (logoNetPath != null && logoNetPath != "") {
		// sp.setImageUrl(logoNetPath);
		// }
		String content = "我在" + shopLocation + "开店，店名：" + shopName
				+ ",赶紧下载客户端http://1fuwu.com.cn/";
		sp.setTitle("社区e商户");
		sp.setTitleUrl("http://1fuwu.com.cn/");
		sp.setUrl("http://1fuwu.com.cn/");
		sp.setSite("社区e商户");
		sp.setSiteUrl("http://1fuwu.com.cn/");
		sp.setText(content);

	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		Message message = handler1.obtainMessage();
		message.obj = "您取消了分享";
		handler1.sendMessage(message);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		Message message = handler1.obtainMessage();
		message.obj = "分享成功";
		handler1.sendMessage(message);
		shareDailog.dismiss();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		Message message = handler1.obtainMessage();
		message.obj = "分享失败";
		handler1.sendMessage(message);
	}

	/**
	 * 线程解决分享监听时的堵塞问题
	 */
	private Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ToastUtil.show(getApplicationContext(), msg.obj.toString());
		};
	};

}
