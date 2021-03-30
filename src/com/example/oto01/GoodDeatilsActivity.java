package com.example.oto01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.model.CityName;
import com.example.oto01.model.Constant;
import com.example.oto01.model.Good;
import com.example.oto01.model.GoodImage;
import com.example.oto01.model.GoodType;
import com.example.oto01.model.Login;
import com.example.oto01.services.GoodService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.CallPhoneUtil;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.UpLoadFile;
import com.example.oto01.wheelDialog.WheelDialogManager;
import com.example.oto01.wheelDialog.WheelDialogManager.OnWheelItemClickListener;

/**
 * 商品详情界面
 * 
 * @author lqq
 * 
 */
public class GoodDeatilsActivity extends BaseActivity implements
		OnClickListener {
	private ViewPager viewPager;
	private LinearLayout mDotsLayout;
	private List<View> goodPicList;
	private TextView rukuTextView, numTextView, priceTextView,
			updatePriceTextView, statusTextView, pandianTextView;
	private EditText titleEditText, contentEditText;
	private int mPostion = 0;// 默认位置
	private TextView product_type;// 分类控件
	private ImageView img1, img2, img3, img4;
	private int id = 0;
	private Good good = new Good();
	private Dialog proDialog, newDialog;
	private ProgressDialog progressDialog;
	private static final int INIT_GOOD_DETAILS = 1;// 初始化商品详情信息
	private static final int UPDATE_GOOD_DETAILS = 2;// 更新商品详情信息
	private static final int DELETE_IMG = 3;// 删除该图片
	private static final int ADD_IMG = 4;// 添加新图片
	private static final int UPDATE_IMG = 5;// 修改该图片
	private static final int TO_SELECT_PHOTO = 6;// 选择图片
	private static final int UPLOAD_IN_PIC = 7;// 上传图片
	private static final int UPLOAD_IN_CONTENT = 8;// 上传内容
	private static final int GOOD_TYPE_LIST = 9;// 获取商品类型列表
	private static final int UPDATE_TYPEID = 10;// 修改品类
	private static final int DELETE_GOOD = 11;// 删除商品
	private int screenWidth;
	private int screenHeight;
	private int shopsid = 0;
	private int current_img_size = 0;// 0 张图片
	private Map<Integer, String> CURRENT_IMG_PATH = new TreeMap<Integer, String>();
	private Map<Integer, String> CURRENT_IMG_NAME = new TreeMap<Integer, String>();
	private int current_tag = 0;// 当前点击的图片
	private String picPath = "";
	private static String LOCAL_IMG = "EServiceStore/";
	// private Spinner detailCataSpinner;
	// private SpinnerAdapter spinnerAdapter;
	private String phoneNumber;
	private ArrayList<CityName> citys;
	// private List<GoodType> typeList = new ArrayList<GoodType>();
	private static int typeid = 0;
	private static int old_typeid = 0;
	private static int ruku_num = 0;
	private static int pandian_num = 0;
	private int current_num = 0;// 当前库存
	private double current_price = 0.00;// 当前价格
	private WheelDialogManager mWheeManager;// 滚轮
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_GOOD_DETAILS:
				initGoodDetailsView();
				break;
			case UPDATE_GOOD_DETAILS:
				showUpdateRes(msg);
				break;
			case UPLOAD_IN_PIC:// 上传图片
				uploadContent((ArrayList<String>) msg.obj);
				break;
			case UPLOAD_IN_CONTENT:// 上传内容
				/*
				 * if(msg.arg2==1){ updateTypeId(); }else{
				 */
				if (msg.arg1 == 0) {
					ToastUtil.show(GoodDeatilsActivity.this, "商品图片更新成功！");
					// finish();
					// initData();
					updateTypeId();
				} else {
					ToastUtil.show(GoodDeatilsActivity.this, "商品图片更新失败！");
				}

				progressDialog.dismiss();
				// }
				break;
			case UPDATE_TYPEID:// 修改文本信息
				progressDialog.dismiss();
				if (msg.arg1 == 0) {
					ToastUtil.show(GoodDeatilsActivity.this, "商品信息更新成功！");
					finish();
				} else {
					ToastUtil.show(GoodDeatilsActivity.this, "商品信息更新失败！");
				}
				break;
			case GOOD_TYPE_LIST:
				initSpinnerData((ArrayList<GoodType>) msg.obj);
				break;
			case DELETE_GOOD:
				if (msg.arg1 == 0) {
					ToastUtil.show(GoodDeatilsActivity.this, "删除成功!");
					Intent data = new Intent();
					setResult(RESULT_OK, data);
					finish();
				} else {
					ToastUtil.show(GoodDeatilsActivity.this,
							"删除失败," + msg.obj.toString());
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_details);
		initView();
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}

	/**
	 * 显示对话框
	 * 
	 * @return
	 */
	private Dialog showNewDialog() {
		Dialog dialog = new Dialog(GoodDeatilsActivity.this,
				R.style.theme_dialog_alert);
		return dialog;
	}

	/**
	 * 修改商品分类
	 */
	private void updateTypeId() {
		if (!NetConn.checkNetwork(GoodDeatilsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			if (current_price > 9999999.99) {
				current_price = 9999999.99;
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message message = handler.obtainMessage(UPDATE_TYPEID);
					GoodService goodService = new GoodService(
							GoodDeatilsActivity.this);
					boolean flag = goodService.updateGoodInfo(shopsid, id,
							typeid, current_price, current_num, good.getUnit(),
							contentEditText.getText().toString().trim(),
							titleEditText.getText().toString().trim(),
							good.getIsgroom(), good.getState());
					message.arg1 = flag == true ? 0 : 1;
					handler.sendMessage(message);
				}
			}).start();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			picPath = "";
			finish();
			System.out.println("-----back------>");
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 获取商品类型列表
	 */
	private void initSpinner() {
		if (!NetConn.checkNetwork(GoodDeatilsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					GoodService goodService = new GoodService(
							GoodDeatilsActivity.this);
					List<GoodType> list = goodService.getGoodTypeList(shopsid,
							"addgoods");
					Message message = handler.obtainMessage(GOOD_TYPE_LIST);
					message.obj = list;
					handler.sendMessage(message);

				}
			}).start();
		}
	}

	/**
	 * 初始化类型的数据
	 * 
	 * @param typeList
	 */

	private void initSpinnerData(final List<GoodType> typeList) {
		CityName city = null;
		for (int i = 0; i < typeList.size(); i++) {

			city = new CityName();
			city.name = typeList.get(i).getName();
			city.id = typeList.get(i).getId();
			citys.add(city);

			if (typeList.get(i).getId() == typeid) {
				// 当前商品的typeid
				product_type.setText(typeList.get(i).getName());
				mPostion = i;
			}
		}

	}

	/**
	 * 上传修改的内容
	 * 
	 * @param obj
	 */
	protected void uploadContent(final ArrayList<String> obj) {
		if (!NetConn.checkNetwork(GoodDeatilsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			// tag==0 只更新图片
			new Thread(new Runnable() {

				@Override
				public void run() {
					GoodService goodService = new GoodService(
							GoodDeatilsActivity.this);
					boolean flag = goodService.updateGoodImg(shopsid, id, obj);
					Message message = handler.obtainMessage(UPLOAD_IN_CONTENT);
					message.arg1 = flag == true ? 0 : 1;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 调价
	 */
	public void update_price_onClick(double price) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_num_dialogview1, null);
		layout.setMinimumWidth((int) (GoodDeatilsActivity.this
				.getWindowManager().getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
		newDialog.setContentView(layout);
		newDialog.setCancelable(true);
		final EditText editText = (EditText) layout.findViewById(R.id.ruku_num);
		TextView curNumTextView = (TextView) layout.findViewById(R.id.num);
		TextView titleTextView = (TextView) layout.findViewById(R.id.title);
		titleTextView.setText("调价");
		curNumTextView.setText("当前价格：" + current_price);
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
				if (editText.getText().toString() != null
						&& !editText.getText().toString().equals("")
						&& editText.getText().toString() != "") {
					newDialog.dismiss();
					final double newprice = Double.parseDouble(editText
							.getText().toString());

					if (newprice > 9999999.99) {
						ToastUtil.show(GoodDeatilsActivity.this,
								"商品价格不能大于9999999.99");
						current_price = 9999999.99;
					}
					current_price = newprice;
					priceTextView.setText("￥" + current_price);
					/*
					 * if(!NetConn.checkNetwork(GoodDeatilsActivity.this)){ //
					 * NetConn.setNetwork(OrdersActivity.this); }else{ new
					 * Thread(new Runnable() {
					 * 
					 * @Override public void run() { updatePrice(newprice); }
					 * 
					 * 
					 * }).start(); }
					 */

				} else {
					ToastUtil.show(GoodDeatilsActivity.this, "请填写价格");
				}
			}
		});
		newDialog.show();// 显示AlertDialog
	}

	/**
	 * 更新价格
	 * 
	 * @param price
	 */
	private void updatePrice(double price) {
		if (price > 9999999.99) {
			price = 9999999.99;
		}
		GoodService goodService = new GoodService(GoodDeatilsActivity.this);
		System.out.println("----updatePrice----->" + good.getIsgroom());
		System.out.println("----updatePrice----->" + price);
		boolean flag = goodService.updateGoodInfo(shopsid, good.getId(),
				good.getTypeid(), price, good.getNum(), good.getUnit(),
				good.getContent(), good.getGoods(), good.getIsgroom(),
				good.getState());
		Message message = handler.obtainMessage(UPDATE_GOOD_DETAILS);
		message.obj = flag;
		handler.sendMessage(message);
	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog.show();

	}

	/**
	 * 显示更新结果
	 * 
	 * @param message
	 */
	protected void showUpdateRes(Message message) {
		boolean flag = (Boolean) message.obj;
		if (flag) {
			ToastUtil.show(GoodDeatilsActivity.this, "更新成功！");
			// updateUI();
		} else {
			ToastUtil.show(GoodDeatilsActivity.this, "更新失败！");
		}
		initData();
	}

	/**
	 * 初始化商品详情UI
	 */
	private void initGoodDetailsView() {
		if (old_typeid == 0) {
			ToastUtil.show(getApplicationContext(), "您的商品已被删除");
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} else {
			typeid = good.getTypeid();
			current_num = good.getNum();
			current_price = good.getGoodsprice();
			System.out.println("------old typeid-------->" + typeid);
		}

		if (proDialog != null) {
			proDialog.dismiss();
		}
	}

	/**
	 * 点击”入库“按钮事件
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	public void addKu_onClick(final int now_num) {
		System.out.println("------入库------->");
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_num_dialogview, null);
		layout.setMinimumWidth((int) (GoodDeatilsActivity.this
				.getWindowManager().getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
		newDialog.setContentView(layout);
		newDialog.setCancelable(true);
		final EditText editText = (EditText) layout.findViewById(R.id.ruku_num);
		TextView curNumTextView = (TextView) layout.findViewById(R.id.num);
		curNumTextView.setText("当前库存："
				+ (current_num == -1 ? "无限库存" : current_num + ""));
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
				// 添加入库的数量
				String goodsnum = editText.getText().toString();
				if (goodsnum != null && !goodsnum.equals("") && goodsnum != "") {
					int num = 0;
					newDialog.dismiss();
					if (goodsnum.trim() == "" || goodsnum.trim().equals("")) {
						num = -1;
					} else if (goodsnum.toString().trim().length() >= 5) {
						num = 9999;
					} else {
						num = Integer.parseInt(goodsnum);
					}
					final int totalNum = num + current_num;
					if (num == -1) {
						ToastUtil.show(GoodDeatilsActivity.this, "该商品库存为无限！");
					} else {
						current_num = totalNum;
						if (current_num > 9999) {
							current_num = 9999;
							ToastUtil.show(GoodDeatilsActivity.this,
									"商品库存不能大于9999！");
						}
						numTextView.setText("" + current_num);
					}
					/*
					 * if(!NetConn.checkNetwork(GoodDeatilsActivity.this)){ //
					 * NetConn.setNetwork(OrdersActivity.this); }else{ if(num ==
					 * -1){ ToastUtil.show(GoodDeatilsActivity.this,
					 * "该商品库存为无限！"); }else{ proDialog.show(); new Thread(new
					 * Runnable() {
					 * 
					 * @Override public void run() { updateNum(totalNum);
					 * 
					 * } }).start(); }
					 * 
					 * }
					 */

				} else {
					ToastUtil.show(GoodDeatilsActivity.this, "请填写数字");
				}
			}
		});
		newDialog.show();// 显示AlertDialog

	}

	/**
	 * 点击”盘点“按钮事件
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	public void pandian_onClick(int now_num) {
		System.out.println("------盘点------->");
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_num_dialogview, null);
		layout.setMinimumWidth((int) (GoodDeatilsActivity.this
				.getWindowManager().getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
		newDialog.setContentView(layout);
		newDialog.setCancelable(true);
		TextView textView = (TextView) layout.findViewById(R.id.title);
		textView.setText("盘点");
		final EditText editText = (EditText) layout.findViewById(R.id.ruku_num);
		TextView curNumTextView = (TextView) layout.findViewById(R.id.num);
		curNumTextView.setText("当前库存："
				+ (current_num == -1 ? "无限库存" : current_num + ""));
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
				// 盘点的数量
				String goodsnum = editText.getText().toString();
				if (goodsnum != null && !goodsnum.equals("") && goodsnum != "") {
					int num = 0;
					if (goodsnum.trim() == "" || goodsnum.trim().equals("")) {
						num = -1;
					} else if (goodsnum.toString().trim().length() >= 5) {
						num = 9999;
					} else {
						num = Integer.parseInt(goodsnum);
					}
					final int totalNum = num;
					current_num = totalNum;
					if (current_num > 9999) {
						ToastUtil.show(GoodDeatilsActivity.this,
								"商品库存不能大于9999！");
						current_num = 9999;
					}
					numTextView.setText("" + current_num);
					/*
					 * if(!NetConn.checkNetwork(GoodDeatilsActivity.this)){ //
					 * NetConn.setNetwork(OrdersActivity.this); }else{
					 * proDialog.show(); new Thread(new Runnable() {
					 * 
					 * @Override public void run() { updateNum(totalNum); }
					 * }).start(); }
					 */
					newDialog.dismiss();
				} else {
					ToastUtil.show(GoodDeatilsActivity.this, "请填写数字");
				}
			}
		});
		newDialog.show();// 显示AlertDialog

	}

	/**
	 * 盘点
	 * 
	 * @param num
	 */
	private void updateNum(int num) {
		if (num > 9999) {
			num = 9999;
		}
		GoodService goodService = new GoodService(GoodDeatilsActivity.this);
		boolean flag = goodService.updateGoodInfo(shopsid, good.getId(),
				good.getTypeid(), good.getGoodsprice(), num, good.getUnit(),
				good.getContent(), good.getGoods(), good.getIsgroom(),
				good.getState());
		Message message = handler.obtainMessage(UPDATE_GOOD_DETAILS);
		message.obj = flag;
		handler.sendMessage(message);
	}

	/**
	 * 初始化商品详情数据
	 */
	private void initData() {
		new MyAsync().execute();
	}

	/**
	 * 获取商品详情的异步任务
	 * 
	 * @author lqq
	 * 
	 */
	private class MyAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			proDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			GoodService goodService = new GoodService(GoodDeatilsActivity.this);
			good = goodService.getGoodDetails(id);
			try {
				old_typeid = good.getTypeid();
			} catch (Exception e) {
				old_typeid = 0;
			}

			Message message = handler.obtainMessage(INIT_GOOD_DETAILS);
			handler.sendMessage(message);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// initViewPager();
			if (old_typeid == 0) {

			} else {
				if (good.getImgList() != null && good.getImgList().size() > 0) {
					for (int i = 0; i < good.getImgList().size(); i++) {
						CURRENT_IMG_PATH.put(i, good.getImgList().get(i)
								.getPicpath());
						CURRENT_IMG_NAME.put(i, good.getImgList().get(i)
								.getPicname());
					}
					current_img_size = CURRENT_IMG_PATH.size();
				}
				initImages(CURRENT_IMG_PATH);
				updateUI();
				if (!NetConn.checkNetwork(GoodDeatilsActivity.this)) {
					// NetConn.setNetwork(OrdersActivity.this);
				} else {
					initSpinner();
				}
			}

			super.onPostExecute(result);
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		id = getIntent().getIntExtra("id", -1);
		LoginManager lm = LoginManager.getInstance(GoodDeatilsActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.setCancelable(false);
		proDialog.setCanceledOnTouchOutside(false);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		newDialog = showNewDialog();
		img1 = (ImageView) findViewById(R.id.img1);
		img1.setId(0);
		img2 = (ImageView) findViewById(R.id.img2);
		img2.setId(1);
		img3 = (ImageView) findViewById(R.id.img3);
		img3.setId(2);
		img4 = (ImageView) findViewById(R.id.img4);
		img4.setId(3);
		ImageViewOnClickListener onClickListener = new ImageViewOnClickListener();
		ImageViewOnIongClickListener onIongClickListener = new ImageViewOnIongClickListener();

		img1.setOnClickListener(onClickListener);
		img1.setOnLongClickListener(onIongClickListener);
		img2.setOnClickListener(onClickListener);
		img2.setOnLongClickListener(onIongClickListener);
		img3.setOnClickListener(onClickListener);
		img3.setOnLongClickListener(onIongClickListener);
		img4.setOnClickListener(onClickListener);
		img4.setOnLongClickListener(onIongClickListener);
		viewPager = (ViewPager) findViewById(R.id.guide_viewpager_head);
		mDotsLayout = (LinearLayout) findViewById(R.id.guide_dots_head);
		rukuTextView = (TextView) findViewById(R.id.ruku);
		numTextView = (TextView) findViewById(R.id.kucun_num);
		priceTextView = (TextView) findViewById(R.id.price);
		titleEditText = (EditText) findViewById(R.id.good_name);
		contentEditText = (EditText) findViewById(R.id.content);
		statusTextView = (TextView) findViewById(R.id.good_status);
		pandianTextView = (TextView) findViewById(R.id.pandian);
		product_type = (TextView) findViewById(R.id.product_type);
		product_type.setOnClickListener(this);
		updatePriceTextView = (TextView) findViewById(R.id.update_price);
		mWheeManager = WheelDialogManager.getInstanse();
		citys = new ArrayList<CityName>();
		updatePriceTextView
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						update_price_onClick(good.getGoodsprice());
					}
				});
		pandianTextView
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						int now_num = good.getNum();
						pandian_onClick(now_num);
					}

				});

		rukuTextView
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						int now_num = good.getNum();
						addKu_onClick(now_num);
					}

				});
		if (!NetConn.checkNetwork(GoodDeatilsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			initData();
		}
		getScreenSize();
	}

	/**
	 * 更新UI
	 */
	private void updateUI() {
		numTextView.setText(good.getNum() == -1 ? "充足" : good.getNum() + "");
		priceTextView.setText("￥" + good.getShowGoodsprice() + "");
		titleEditText
				.setText((good.getGoods() == "null"
						|| good.getGoods().equals("null") || good.getGoods() == "null") ? ""
						: good.getGoods());
		contentEditText
				.setText((good.getContent() == "null"
						|| good.getContent().equals("null") || good
						.getContent() == "null") ? "" : good.getContent());

		if (good.getState() == 1) {// 已上架
			statusTextView.setText("在售");
		} else if (good.getState() == 2) {
			statusTextView.setText("停售");
		}

	}

	/**
	 * 初始化图片
	 */
	private void initImages(Map<Integer, String> imgs) {
		ImgLoad loader = ImgLoad.getInstance();
		System.out.println("------initImages------->" + imgs);
		if (imgs != null) {
			// List<GoodImage> imagePathList = imgs;
			int len = imgs.size();
			switch (len) {
			case 0:
				System.out.println("--------0张图片--------->");
				img1.setVisibility(View.VISIBLE);
				img2.setVisibility(View.INVISIBLE);
				img3.setVisibility(View.INVISIBLE);
				img4.setVisibility(View.INVISIBLE);
				img1.setImageResource(R.drawable.add_pic);
				img1.setScaleType(ScaleType.FIT_XY);
				break;
			case 1:
				System.out.println("--------1张图片--------->");
				String path = picPath;
				for (int i = 0; i < imgs.size(); i++) {
					if (imgs.get(i) != null && !imgs.get(i).equals(null)) {
						path = imgs.get(i);
					}
				}

				if ((!path.contains(LOCAL_IMG))
						&& (!path.contains("mnt/sdcard"))) {
					loader.addTask(path, img1);
					loader.doTask();
					if (path != null) {
						ImageLoadTool.disPlay(path, img1,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(path, 4);
					img1.setImageBitmap(bmp);
				}

				img1.setVisibility(View.VISIBLE);
				img1.setScaleType(ScaleType.FIT_XY);

				img2.setVisibility(View.VISIBLE);
				img2.setImageResource(R.drawable.add_pic);
				img2.setScaleType(ScaleType.FIT_XY);
				img3.setVisibility(View.INVISIBLE);
				img4.setVisibility(View.INVISIBLE);
				break;
			case 2:
				System.out.println("--------2张图片--------->");
				img1.setTag(imgs.get(0));
				img2.setTag(imgs.get(1));

				if ((!imgs.get(0).contains(LOCAL_IMG))
						&& (!imgs.get(0).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(0), img1);
					if (imgs.get(0) != null) {
						ImageLoadTool.disPlay(imgs.get(0), img1,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(0), 4);
					img1.setImageBitmap(bmp);
				}
				if ((!imgs.get(1).contains(LOCAL_IMG))
						&& (!imgs.get(1).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(1), img2);
					if (imgs.get(1) != null) {
						ImageLoadTool.disPlay(imgs.get(1), img2,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(1), 4);
					img2.setImageBitmap(bmp);
				}
				loader.doTask();
				img1.setVisibility(View.VISIBLE);
				img1.setScaleType(ScaleType.FIT_XY);
				img2.setVisibility(View.VISIBLE);
				img2.setScaleType(ScaleType.FIT_XY);
				img3.setImageResource(R.drawable.add_pic);
				img3.setVisibility(View.VISIBLE);
				img3.setScaleType(ScaleType.FIT_XY);
				img4.setVisibility(View.INVISIBLE);
				break;
			case 3:
				System.out.println("--------3张图片--------->");

				img1.setTag(imgs.get(0));
				img2.setTag(imgs.get(1));
				img3.setTag(imgs.get(2));
				if ((!imgs.get(0).contains(LOCAL_IMG))
						&& (!imgs.get(0).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(0), img1);
					if (imgs.get(0) != null) {
						ImageLoadTool.disPlay(imgs.get(0), img1,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(0), 4);
					img1.setImageBitmap(bmp);
				}
				if ((!imgs.get(1).contains(LOCAL_IMG))
						&& (!imgs.get(1).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(1), img2);
					if (imgs.get(1) != null) {
						ImageLoadTool.disPlay(imgs.get(1), img2,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(1), 4);
					img2.setImageBitmap(bmp);
				}
				System.out.println("------imgs.get(2)--------->" + imgs.get(2));
				if ((!imgs.get(2).contains(LOCAL_IMG))
						&& (!imgs.get(2).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(2), img3);
					if (imgs.get(2) != null) {
						ImageLoadTool.disPlay(imgs.get(2), img3,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(2), 4);
					img3.setImageBitmap(bmp);
				}

				loader.doTask();
				img1.setVisibility(View.VISIBLE);
				img1.setScaleType(ScaleType.FIT_XY);
				img2.setVisibility(View.VISIBLE);
				img2.setScaleType(ScaleType.FIT_XY);
				img3.setVisibility(View.VISIBLE);
				img3.setScaleType(ScaleType.FIT_XY);
				img4.setImageResource(R.drawable.add_pic);
				img4.setScaleType(ScaleType.FIT_XY);
				img4.setVisibility(View.VISIBLE);
				break;
			case 4:
				System.out.println("--------4张图片--------->");

				img1.setTag(imgs.get(0));
				img2.setTag(imgs.get(1));
				img3.setTag(imgs.get(2));
				img4.setTag(imgs.get(3));
				if ((!imgs.get(0).contains(LOCAL_IMG))
						&& (!imgs.get(0).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(0), img1);
					if (imgs.get(0) != null) {
						ImageLoadTool.disPlay(imgs.get(0), img1,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(0), 4);
					img1.setImageBitmap(bmp);
				}
				if ((!imgs.get(1).contains(LOCAL_IMG))
						&& (!imgs.get(1).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(1), img2);
					if (imgs.get(1) != null) {
						ImageLoadTool.disPlay(imgs.get(1), img2,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(1), 4);
					img2.setImageBitmap(bmp);
				}
				if ((!imgs.get(2).contains(LOCAL_IMG))
						&& (!imgs.get(2).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(2), img3);
					if (imgs.get(2) != null) {
						ImageLoadTool.disPlay(imgs.get(2), img3,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(2), 4);
					img3.setImageBitmap(bmp);
				}
				if ((!imgs.get(3).contains(LOCAL_IMG))
						&& (!imgs.get(3).contains("mnt/sdcard"))) {
					loader.addTask(imgs.get(3), img4);
					if (imgs.get(1) != null) {
						ImageLoadTool.disPlay(imgs.get(3), img4,
								R.drawable.default_image);
					}
				} else {
					Bitmap bmp = ImageUtil.getBitmap(imgs.get(3), 4);
					img4.setImageBitmap(bmp);
				}
				loader.doTask();
				img1.setVisibility(View.VISIBLE);
				img1.setScaleType(ScaleType.FIT_XY);
				img2.setVisibility(View.VISIBLE);
				img2.setScaleType(ScaleType.FIT_XY);
				img3.setVisibility(View.VISIBLE);
				img3.setScaleType(ScaleType.FIT_XY);
				img4.setVisibility(View.VISIBLE);
				img4.setScaleType(ScaleType.FIT_XY);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (picPath.length() > 0) {
			CURRENT_IMG_PATH.remove(current_tag);
			System.out.println("------current_tag-----" + current_tag
					+ "--picPath--->" + picPath);
			CURRENT_IMG_PATH.put(current_tag, picPath);
			System.out
					.println("------CURRENT_IMG_PATH-----" + CURRENT_IMG_PATH);
			initImages(CURRENT_IMG_PATH);
		}
	}

	/**
	 * 图片点击
	 * 
	 * @author lqq
	 * 
	 */
	private class ImageViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			System.out.println("------点击了------->" + v.getId());
			int tag = v.getId();
			current_tag = tag;
			open_camera();
		}

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
	public void open_camera() {
		Intent in = new Intent(this, SelectPic2Activity.class);
		in.putExtra("screenWidth", screenWidth);
		in.putExtra("screenHeight", screenHeight);
		startActivityForResult(in, TO_SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			// Bundle bundle = data.getBundleExtra("bundle");
			// System.out.println("------picPath---2->"+bundle);
			// bitmap = bundle.getParcelable("data");
		}
	}

	/**
	 * 图片长点击：删除该图片
	 * 
	 * @author lqq
	 * 
	 */
	private class ImageViewOnIongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			final int tag = v.getId();
			System.out.println("------点击了------->" + tag);
			// 提示是否要删除该商品分类
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					GoodDeatilsActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("是否要删除该图片?");
			dialog.setPositiveButton("确定",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 　删除该商品分类
							// final Message
							// message=handler.obtainMessage(DELETE_IMG);
							// current_img_pathList.remove(tag);
							// current_img_nameList.remove(tag);
							System.out.println("-----CURRENT_IMG_PATH----->"
									+ CURRENT_IMG_PATH);
							CURRENT_IMG_NAME.remove(tag);
							CURRENT_IMG_PATH.remove(tag);
							System.out.println("-----CURRENT_IMG_PATH----->"
									+ CURRENT_IMG_PATH);
							Map<Integer, String> temp = new TreeMap<Integer, String>();
							List<String> list = new ArrayList<String>();

							// CURRENT_IMG_PATH.keySet(0)

							for (Entry<Integer, String> entry : CURRENT_IMG_PATH
									.entrySet()) {
								list.add(entry.getValue());
							}
							System.out.println("-----list----->" + list);
							for (int i = 0; i < list.size(); i++) {
								temp.put(i, list.get(i));
							}
							System.out.println("-----temp----->" + temp);
							CURRENT_IMG_PATH.clear();
							CURRENT_IMG_PATH = temp;
							initImages(CURRENT_IMG_PATH);
						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.show();
			return false;
		}

	}

	/**
	 * 保存修改
	 * 
	 * @param view
	 */
	public void save_onClick(View view) {
		int i = 0;
		for (Entry<Integer, String> entry : CURRENT_IMG_PATH.entrySet()) {
			String imgPath = entry.getValue();
			/*
			 * if((!imgPath.contains(LOCAL_IMG))&&(!imgPath.contains("mnt/sdcard"
			 * ))){ i++; }
			 */
			if ((imgPath.contains(LOCAL_IMG))
					|| (imgPath.contains("mnt/sdcard"))) {
				i++;
			}
		}
		System.out.println("--------i------>" + i);
		System.out.println("-------current_img_size------>" + current_img_size);
		System.out.println("-------CURRENT_IMG_PATH------>"
				+ CURRENT_IMG_PATH.size());
		System.out.println("-----------length---------->"
				+ titleEditText.getText().toString().trim().length());
		if (titleEditText.getText().toString().trim().length() == 0) {
			ToastUtil.show(GoodDeatilsActivity.this, "商品名称不能为空！");
		} else if (CURRENT_IMG_PATH.size() == 0) {
			ToastUtil.show(GoodDeatilsActivity.this, "商品图片不能为空！");
		} else {
			if (i == 0 && current_img_size == CURRENT_IMG_PATH.size()) {
				System.out.println("-------正在更新商品信息----->" + old_typeid);
				progressDialog.setMessage("正在更新商品信息");
				progressDialog.setCancelable(false);
				progressDialog.show();
				updateTypeId();
			} else {
				progressDialog.setMessage("正在更新商品图片");
				progressDialog.setCancelable(false);
				progressDialog.show();
				updateImg();
			}
		}
		/*
		 * if(i == CURRENT_IMG_PATH.size()&&typeid==old_typeid ){ //
		 * ToastUtil.show(GoodDeatilsActivity.this, "请选择修改图片或者品类，否则无法更新");
		 * ToastUtil.show(GoodDeatilsActivity.this, "已保存"); finish(); return ; }
		 * if(i == CURRENT_IMG_PATH.size()&&typeid!=old_typeid){
		 * System.out.println("---------old_typeid----->"+old_typeid);
		 * System.out.println("---------typeid----->"+typeid);
		 * progressDialog.setMessage("正在更新商品信息");
		 * progressDialog.setCancelable(false); progressDialog.show();
		 * updateTypeId(); return ; } if(i !=
		 * CURRENT_IMG_PATH.size()&&typeid==old_typeid){
		 * progressDialog.setMessage("正在更新商品图片");
		 * progressDialog.setCancelable(false); progressDialog.show();
		 * updateImg(0); return ; } if(i != CURRENT_IMG_PATH.size()
		 * &&typeid!=old_typeid){ progressDialog.setMessage("正在更新商品图片和信息");
		 * progressDialog.setCancelable(false); progressDialog.show();
		 * updateImg(1); return ; }
		 */

	}

	/**
	 * 更新商品图片
	 */
	private void updateImg() {
		if (!NetConn.checkNetwork(GoodDeatilsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					ArrayList<String> picNames = new ArrayList<String>();
					Map<String, String> map = new HashMap<String, String>();
					map.put("shopsid", shopsid + "");
					map.put("imgtype", "Goods");
					for (Entry<Integer, String> entry : CURRENT_IMG_PATH
							.entrySet()) {
						String imgPath = entry.getValue();
						if ((imgPath.contains(LOCAL_IMG))
								|| (imgPath.contains("mnt/sdcard"))) {
							String string = UpLoadFile.uploadImage(
									Constant.uploadImgURL, imgPath, map);
							picNames.add(string);
							System.out.println("--------返回的String-------->"
									+ string);
						} else {
							for (GoodImage goodImage : good.getImgList()) {
								if (goodImage.getPicpath() == imgPath) {
									picNames.add(goodImage.getPicname());
								}
							}
						}

					}
					Message message = handler.obtainMessage(UPLOAD_IN_PIC);
					message.obj = picNames;
					// message.arg1= tag;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 打电话
	 * 
	 * @param view
	 */
	public void call_onClick(View view) {
		CallPhoneUtil.callPhone(this, phoneNumber);
	}

	/**
	 * 删除该商品
	 * 
	 * @param view
	 */
	public void delete_onClick(View view) {
		showInfo();
	}

	/**
	 * 显示提醒取消删除对话框
	 */
	private void showInfo() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(
				R.layout.cancel_delete__goods_dialogview, null);
		final Dialog dialog = new Dialog(GoodDeatilsActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				if (!NetConn.checkNetwork(GoodDeatilsActivity.this)) {
				} else {
					new Thread(new Runnable() {

						@Override
						public void run() {
							GoodService goodService = new GoodService(
									GoodDeatilsActivity.this);
							int flag = goodService.deleteGood(good.getId());
							Message message = handler
									.obtainMessage(DELETE_GOOD);
							message.arg1 = flag;
							message.obj = goodService.errorMsg;
							handler.sendMessage(message);

						}
					}).start();
				}
			}
		});
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.product_type:
			// 添加分类
			mWheeManager.createMyItemWheel(GoodDeatilsActivity.this, citys,
					mPostion, product_type, new OnWheelItemClickListener() {

						@Override
						public void onWheelItemClicl(int positon) {
							mPostion = positon;
							typeid = citys.get(mPostion).getId();
						}
					});
			break;

		default:
			break;
		}

	}
}
