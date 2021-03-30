package com.example.oto01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.model.CityName;
import com.example.oto01.model.Constant;
import com.example.oto01.model.Good;
import com.example.oto01.model.GoodType;
import com.example.oto01.model.Login;
import com.example.oto01.services.GoodService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.UpLoadFile;
import com.example.oto01.views.HorizontalListView;
import com.example.oto01.wheelDialog.WheelDialogManager;
import com.example.oto01.wheelDialog.WheelDialogManager.OnWheelItemClickListener;

/**
 * 添加商品界面
 * 
 * @author lqq
 * 
 */
public class GoodAddActivity extends BaseActivity implements OnClickListener {
	protected static final int REFRESH_UI = 0;
	protected static final int REFRESH_UI_ON_SALE = 1;
	protected static final int REFRESH_UI_SAVE_OK = 2;
	protected static final int REFRESH_UI_DEL = 3;
	protected static final int TO_UPLOAD_FILE = 4;
	protected static final int UPLOAD_FILE_DONE = 5;
	protected static final int TO_SELECT_PHOTO = 6;
	protected static final int REFRESH_UI_SAVE_FAIL = 9;
	protected static final int UPLOAD_IN_PIC = 10;
	protected static final int GOOD_TYPE_LIST = 11;
	protected static final int ADD_GOOD_TYPE = 12;
	private int screenWidth;
	private int screenHeight;

	private ImageView img1, img2, img3, img4;
	private int current_tag = 0;// 当前点击的图片
	private Map<Integer, String> CURRENT_IMG_PATH = new HashMap<Integer, String>();
	private EditText detailPrice;
	private EditText detailContext;
	private EditText detailName;
	private EditText detailUnit;
	private EditText detailNum;
	private Good good;
	private GoodImgAdapter goodImgAdapter;
	private HorizontalListView goodImgList;
	private ProgressDialog proDialog;
	private String picPath = "";
	private ArrayList<String> imgList = new ArrayList<String>();
	// private Spinner detailCataSpinner;
	// private SpinnerAdapter spinnerAdapter;
	private List<String> types = new ArrayList<String>();
	private List<GoodType> typeList = new ArrayList<GoodType>();
	private static int shopsid = 0;
	private static int typeid = 0;
	private int image_num = 0;
	private Dialog newDialog;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_UI:
				if (good != null)
					refreshUI();
				break;
			case REFRESH_UI_SAVE_OK:
				proDialog.dismiss();
				showToast("保存成功！");
				Intent data = new Intent();
				setResult(RESULT_OK, data);
				finish();
				break;
			case REFRESH_UI_SAVE_FAIL:
				proDialog.dismiss();
				showToast("保存失败！");
				break;
			case REFRESH_UI_DEL:
				showToast("删除成功！");
				break;
			case UPLOAD_IN_PIC:
				uploadContent((ArrayList<String>) msg.obj);
				break;
			case GOOD_TYPE_LIST:
				initSpinnerData((List<GoodType>) msg.obj);
				break;
			case ADD_GOOD_TYPE:
				if (msg.arg1 == 0) {
					ToastUtil.show(GoodAddActivity.this, "添加分类成功！");
					initSpinner();
				} else if (msg.arg1 == 1) {
					ToastUtil.show(GoodAddActivity.this, "添加分类失败！");
				} else if (msg.arg1 == 2) {
					ToastUtil.show(GoodAddActivity.this, "分类名称已存在！");
				} else if (msg.arg1 == -1) {
					ToastUtil.show(GoodAddActivity.this, "添加分类失败！");
				}
				break;
			}
		}
	};

	/**
	 * 上传文本
	 * 
	 * @param picnames
	 */
	protected void uploadContent(ArrayList<String> picnames) {
		saveGood(picnames);
	}

	/**
	 * 刷新UI
	 */
	private void refreshUI() {
		imgList.clear();
		goodImgAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_add);
		LoginManager lm = LoginManager.getInstance(GoodAddActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		mWheeManager = WheelDialogManager.getInstanse();
		citys = new ArrayList<CityName>();
		// y = 0;
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
		initGoodDetailViews();
		initGoodImgView();
		getScreenSize();

	}

	private void getScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		newDialog = showNewDialog();
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
				break;
			case 1:
				System.out.println("--------1张图片--------->");
				String path = picPath;
				for (int i = 0; i < imgs.size(); i++) {
					if (imgs.get(i) != null && !imgs.get(i).equals(null)) {
						path = imgs.get(i);
					}
				}
				img1.setTag(path);
				Bitmap bmp = ImageUtil.getBitmap(path, 4);
				img1.setImageBitmap(bmp);
				img1.setVisibility(View.VISIBLE);

				img2.setVisibility(View.VISIBLE);
				img2.setImageResource(R.drawable.add_pic);
				img3.setVisibility(View.INVISIBLE);
				img4.setVisibility(View.INVISIBLE);
				break;
			case 2:
				System.out.println("--------2张图片--------->");
				img1.setTag(imgs.get(0));
				img2.setTag(imgs.get(1));

				Bitmap bmp1 = ImageUtil.getBitmap(imgs.get(0), 4);
				img1.setImageBitmap(bmp1);
				Bitmap bmp2 = ImageUtil.getBitmap(imgs.get(1), 4);
				img2.setImageBitmap(bmp2);
				img1.setVisibility(View.VISIBLE);
				img2.setVisibility(View.VISIBLE);
				img3.setImageResource(R.drawable.add_pic);
				img3.setVisibility(View.VISIBLE);
				img4.setVisibility(View.INVISIBLE);
				break;
			case 3:
				System.out.println("--------3张图片--------->");

				img1.setTag(imgs.get(0));
				img2.setTag(imgs.get(1));
				img3.setTag(imgs.get(2));
				Bitmap bmp3 = ImageUtil.getBitmap(imgs.get(0), 4);
				img1.setImageBitmap(bmp3);
				Bitmap bmp4 = ImageUtil.getBitmap(imgs.get(1), 4);
				img2.setImageBitmap(bmp4);
				System.out.println("------imgs.get(2)--------->" + imgs.get(2));
				Bitmap bmp5 = ImageUtil.getBitmap(imgs.get(2), 4);
				img3.setImageBitmap(bmp5);
				img1.setVisibility(View.VISIBLE);
				img2.setVisibility(View.VISIBLE);
				img3.setVisibility(View.VISIBLE);
				img4.setImageResource(R.drawable.add_pic);
				img4.setVisibility(View.VISIBLE);
				break;
			case 4:
				System.out.println("--------4张图片--------->");

				img1.setTag(imgs.get(0));
				img2.setTag(imgs.get(1));
				img3.setTag(imgs.get(2));
				img4.setTag(imgs.get(3));
				Bitmap bmp6 = ImageUtil.getBitmap(imgs.get(0), 4);
				img1.setImageBitmap(bmp6);
				Bitmap bmp7 = ImageUtil.getBitmap(imgs.get(1), 4);
				img2.setImageBitmap(bmp7);
				Bitmap bmp8 = ImageUtil.getBitmap(imgs.get(2), 4);
				img3.setImageBitmap(bmp8);
				Bitmap bmp9 = ImageUtil.getBitmap(imgs.get(3), 4);
				img4.setImageBitmap(bmp9);
				img1.setVisibility(View.VISIBLE);
				img2.setVisibility(View.VISIBLE);
				img3.setVisibility(View.VISIBLE);
				img4.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
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
			// 提示是否要删除该商品图片
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					GoodAddActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("是否要删除该图片?");
			dialog.setPositiveButton("确定",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 　删除该商品分类
							CURRENT_IMG_PATH.remove(tag);
							System.out.println("-----CURRENT_IMG_PATH----->"
									+ CURRENT_IMG_PATH);
							Map<Integer, String> temp = new HashMap<Integer, String>();
							List<String> list = new ArrayList<String>();

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

	private Dialog showNewDialog() {
		Dialog dialog = new Dialog(GoodAddActivity.this,
				R.style.theme_dialog_alert);
		return dialog;
	}

	private ArrayList<CityName> citys;
	private int mPostion = 0;// 默认位置
	private TextView product_type;// 分类控件

	/**
	 * 初始化下拉选项的数据
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
			// if (typeList.get(i).getId() == typeid) {
			// 当前商品的typeid
			product_type.setText(typeList.get(0).getName());
			mPostion = 0;
			typeid = typeList.get(0).getId();
			// }
		}
		// types.clear();
		// // if (typeList.size() > 1) {
		// // typeList.remove(0);
		// // typeList.remove(typeList.size() - 1);
		// // }
		// for (GoodType type : typeList) {
		// types.add(type.getName());
		// }
		//
		// y++;
		//
		// System.out
		// .println("------------GoodAddActivity------y---------------->"
		// + y);
		// this.typeList.clear();
		// this.typeList = typeList;
		// spinnerAdapter = new SpinnerAdapter(this,
		// android.R.layout.simple_spinner_item, types);
		// System.out.println("----types----->" + types);
		// spinnerAdapter.setDropDownViewResource(R.layout.drop_down_item);
		// spinnerAdapter.notifyDataSetChanged();
		// detailCataSpinner.setAdapter(spinnerAdapter);
		//
		// if (y == 1) {
		// detailCataSpinner.setSelection(typeList.size() - 1);
		// } else {
		// detailCataSpinner.setSelection(0);
		// }
		//
		// detailCataSpinner
		// .setOnItemSelectedListener(new OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// if (arg2 < typeList.size()) {
		// typeid = typeList.get(arg2).getId();
		// } else {
		// typeid = typeList.size() - 1;
		// }
		// TextView tv = (TextView) arg1;
		// tv.setTextSize(16);
		// // tv.setTextColor(Color.parseColor("#626a7d"));
		// tv.setGravity(android.view.Gravity.CENTER_VERTICAL
		// | Gravity.LEFT); // 设置居中
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// }
		// });
	}

	/**
	 * 获取商品类型列表
	 */
	private void initSpinner() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				GoodService goodService = new GoodService(GoodAddActivity.this);
				List<GoodType> list = goodService.getGoodTypeList(shopsid,
						"addgoods");
				Message message = handler.obtainMessage(GOOD_TYPE_LIST);
				message.obj = list;
				handler.sendMessage(message);
			}
		}).start();
	}

	// private class SpinnerAdapter extends ArrayAdapter<String> {
	// public SpinnerAdapter(Context context, int textViewResourceId,
	// List<String> objects) {
	// super(context, textViewResourceId, objects);
	// }
	// }

	private void initGoodDetailViews() {
		proDialog = new ProgressDialog(this);
		// detailName = (TextView) findViewById(R.id.product_detail_name);
		detailPrice = (EditText) findViewById(R.id.product_detail_price);
		// 失去焦点事件
		detailPrice.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				InputMethodManager imm = (InputMethodManager) GoodAddActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (detailPrice.hasFocus() == false) {
					String temp = detailPrice.getText().toString();
					double price = TextUtils.isEmpty(temp) ? 0 : Double
							.parseDouble(temp);
					if (price > 9999999.99) {
						detailPrice.setText(9999999.99 + "");
						// det ailPrice.requestFocus();
						// detailPrice.selectAll();
						// detailPrice.setFocusable(true);
						// detailPrice.setFocusableInTouchMode(true);
						ToastUtil.show(GoodAddActivity.this,
								"商品价格大于9999999.99，默认为9999999.99");
					}
				} else {
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});

		detailContext = (EditText) findViewById(R.id.good_details_content);
		detailNum = (EditText) findViewById(R.id.product_repertory_num);
		detailUnit = (EditText) findViewById(R.id.product_detail_danwei);
		detailName = (EditText) findViewById(R.id.goods_name);
		product_type = (TextView) findViewById(R.id.product_type);
		product_type.setOnClickListener(this);
		if (!NetConn.checkNetwork(GoodAddActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			initSpinner();
		}

	}

	/**
	 * 初始化商品图片视图
	 */
	public void initGoodImgView() {
		goodImgAdapter = new GoodImgAdapter(this,
				R.layout.horizontal_list_item, imgList);
		goodImgList = (HorizontalListView) findViewById(R.id.img_listview);
		goodImgList.setAdapter(goodImgAdapter);
		goodImgList.setOnItemClickListener(imgClickListener);
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
	 * 拍照
	 * 
	 * @param view
	 */
	public void camera_onClick(View view) {
		if (image_num >= 4) {
			ToastUtil.show(GoodAddActivity.this, "只能添加4张图片");
		} else {
			Intent in = new Intent(this, SelectPic2Activity.class);
			in.putExtra("screenWidth", screenWidth);
			in.putExtra("screenHeight", screenHeight);
			startActivityForResult(in, TO_SELECT_PHOTO);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resCode, Intent data) {
		// super.onActivityResult(requestCode, resCode, data);
		if (resCode == RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			image_num++;
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			// System.out.println("------picPath---->"+picPath);
			// imgList.add(picPath);
			// goodImgAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 添加商品
	 * 
	 * @param view
	 */
	public void save_onClick(View view) {
		if (!NetConn.checkNetwork(GoodAddActivity.this)) {
		} else {
			if (CURRENT_IMG_PATH.size() == 0) {
				showToast("请选择图片！");
				return;
			}
			String content = detailName.getText().toString();
			String price = detailPrice.getText().toString();
			String detailNum1 = detailNum.getText().toString();
			if (content.trim().length() == 0) {
				ToastUtil.show(GoodAddActivity.this, "商品名称不能为空！");
			} else if (content.trim().length() > 20) {
				ToastUtil.show(GoodAddActivity.this, "商品名称不能超过20个字！");
			} else if (price.trim().length() == 0) {
				ToastUtil.show(GoodAddActivity.this, "商品价格不能为空！");
			} else if (Double.parseDouble(price) <= 0) {
				ToastUtil.show(GoodAddActivity.this, "商品价格必须大于0！");
			} else if (detailNum1.trim().length() > 4) {
				ToastUtil.show(GoodAddActivity.this, "库存最大为9999！");
			} else if (price.contains(".")) {
				String s[] = price.split("[.]");
				if (s[1].length() > 2) {
					ToastUtil.show(GoodAddActivity.this, "商品价格格式不对,小数点后精确到2位!");
				} else {
					addGood();
				}
			} else {
				addGood();
			}
		}

	}

	private void addGood() {
		proDialog.setMessage("正在更新...");
		proDialog.setCancelable(false);
		proDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {

				ArrayList<String> picNames = new ArrayList<String>();

				Map<String, String> map = new HashMap<String, String>();
				map.put("shopsid", shopsid + "");
				map.put("imgtype", "Goods");
				for (Entry<Integer, String> entry : CURRENT_IMG_PATH.entrySet()) {
					String imgPath = entry.getValue();
					String string = UpLoadFile.uploadImage(
							Constant.uploadImgURL, imgPath, map);
					System.out.println("--------返回的String-------->" + string);
					picNames.add(string);
				}
				Message message = handler.obtainMessage(UPLOAD_IN_PIC);
				message.obj = picNames;
				handler.sendMessage(message);
			}
		}).start();
	}

	/**
	 * 商品图片的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class GoodImgAdapter extends ArrayAdapter<String> {
		public GoodImgAdapter(Context cx, int resId, List<String> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder = null;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(
						R.layout.horizontal_list_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) cv.findViewById(R.id.img);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, getItem(position));
			return cv;
		}
	}

	/**
	 * 填充图片
	 * 
	 * @param holder
	 * @param path
	 */
	private void refreshItem(ViewHolder holder, String path) {
		Bitmap bmp = ImageUtil.getBitmap(path, 4);
		holder.img.setImageBitmap(bmp);

	}

	private class ViewHolder {
		ImageView img;
	}

	/**
	 * 单击事件
	 */
	private OnItemClickListener imgClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			startPagerActivity(position);
		}
	};
	private int y;

	private void startPagerActivity(int position) {
		/*
		 * Intent in = new Intent(this, ImgPagerActivity.class); String[] str =
		 * imgList.toArray(new String[imgList.size()]); in.putExtra("IMAGES",
		 * str); in.putExtra("IMAGE_TYPE", "LOCAL");
		 * in.putExtra("IMAGE_POSITION", position); startActivity(in);
		 */
	}

	/**
	 * 添加商品
	 * 
	 * @param imageList
	 */
	@SuppressLint("NewApi")
	private void saveGood(final ArrayList<String> imageList) {
		final String temp = detailPrice.getText().toString();
		double price = TextUtils.isEmpty(temp) ? 0 : Double.parseDouble(temp);
		if (price > 9999999.99) {
			detailPrice.setText(9999999.99 + "");
			price = 9999999.99;
		}
		final double myPrice = price;
		final String goodsnum = detailNum.getText().toString();
		final String unitq = detailUnit.getText().toString();
		final String unit = unitq.replaceAll("[\\t\\n\\r]", "");

		final String content = detailContext.getText().toString();
		final String documentTxt = content.replaceAll("[\\t\\n\\r]", "");
		final String goods1 = detailName.getText().toString();
		final String goods = goods1.replaceAll("[\\t\\n\\r]", "");
		if (!NetConn.checkNetwork(GoodAddActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {

			new Thread(new Runnable() {
				@Override
				public void run() {
					save(imageList, shopsid, typeid, documentTxt, myPrice,
							goodsnum, unit, goods);
				}
			}).start();
		}
	}

	/**
	 * 保存数据
	 * 
	 * @param image
	 * @param shopsid
	 * @param typeid
	 * @param content
	 * @param price
	 * @param goodsnum
	 * @param unit
	 */
	private void save(ArrayList<String> image, int shopsid, int typeid,
			String content, double price, String goodsnum, String unit,
			String goods) {
		GoodService goodService = new GoodService(GoodAddActivity.this);

		int num = 0;

		if (goodsnum.trim() == "" || goodsnum.trim().equals("")) {
			num = -1;
		} else if (goodsnum.toString().trim().length() >= 5) {
			num = 9999;
		} else {
			num = Integer.parseInt(goodsnum);
		}
		if (goodService
				.addGood(typeid, price, num, unit, content, goods, image)) {
			handler.obtainMessage(REFRESH_UI_SAVE_OK).sendToTarget();
		} else {
			handler.obtainMessage(REFRESH_UI_SAVE_FAIL).sendToTarget();
		}
	}

	/**
	 * 添加分类
	 * 
	 * @param view
	 */
	public void add_type_onClick(View view) {
		addType();
	}

	/**
	 * 添加商品分类
	 */
	private void addType() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_type_dialogview, null);
		layout.setMinimumWidth((int) (GoodAddActivity.this.getWindowManager()
				.getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
		newDialog.setContentView(layout);
		newDialog.setCancelable(true);
		final EditText editText = (EditText) layout
				.findViewById(R.id.type_name);
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
				// 添加品类的名称
				if (editText.getText().toString() != null
						&& !editText.getText().toString().equals("")
						&& editText.getText().toString() != "") {
					String name = editText.getText().toString().trim();
					if (name.length() > 6) {
						ToastUtil.show(GoodAddActivity.this, "分类名称不能超过6个字");
					} else {
						if (!NetConn.checkNetwork(GoodAddActivity.this)) {
							// NetConn.setNetwork(OrdersActivity.this);
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									addTypeName(editText.getText().toString()
											.trim());
								}
							}).start();
						}
						newDialog.dismiss();
					}
				} else {
					ToastUtil.show(GoodAddActivity.this, "请填写名称");
				}
			}
		});
		newDialog.show();// 显示AlertDialog
	}

	/**
	 * 添加分类名称
	 * 
	 * @param typename
	 */
	private void addTypeName(String typename) {
		GoodService goodService = new GoodService(GoodAddActivity.this);
		int flag = goodService.addGoodType1(typename);
		Message message = handler.obtainMessage(ADD_GOOD_TYPE);
		message.arg1 = flag;
		handler.sendMessage(message);
	}

	private WheelDialogManager mWheeManager;// 滚轮

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.product_type:
			// 添加分类
			if (citys.size() == 0) {

			} else {
				mWheeManager.createMyItemWheel(GoodAddActivity.this, citys,
						mPostion, product_type, new OnWheelItemClickListener() {
							@Override
							public void onWheelItemClicl(int positon) {
								mPostion = positon;
								typeid = citys.get(mPostion).getId();
							}
						});
			}
			break;

		default:
			break;
		}

	}
}