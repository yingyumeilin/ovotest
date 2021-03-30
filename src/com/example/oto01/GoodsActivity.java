package com.example.oto01;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.model.Good;
import com.example.oto01.model.GoodType;
import com.example.oto01.model.Login;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.services.GoodService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.DensityUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.views.PullRefreshView;
import com.example.oto01.views.PullRefreshView.OnFooterRefreshListener;
import com.example.oto01.views.PullRefreshView.OnHeaderRefreshListener;

/**
 * 商品列表界面
 * 
 * @author lqq
 * 
 */
public class GoodsActivity extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnItemClickListener,
		OnClickListener {
	// 0表示升序，1表示降序
	private static final int SX = 0;
	private static final int JX = 1;
	private static int BUYNUM_STATUS = SX;
	private static int NUM_STATUS = SX;
	private static int GROUTIME_STATUS = SX;
	private static final int HEAD = -1;
	private static final int FOOT = -2;
	private static final int NORMAL = -3;

	private static final int BUYNUM = 2;// 销量
	private static final int NUM = 3;// 库存
	private static final int GROUTIME = 4;// 上架时间

	private static int LOAD_STATUS = NORMAL;// 加载数据的状态
	private static int CURRENT_OPT = BUYNUM;// 当前选项
	private static int CURRENT_OPT_STATUS = SX;// 当前选项的状态
	private static int CURRENT_OPT_TYPEID = 0;// 当前选择的类别的id

	private static final int UPDATE_GOOD_DETAILS = 5;// 更新商品详情信息
	private static final int UPDATE_GOOD_TYPES = 6;// 更新商品类别
	private static final int REMOVE_STATUS = 7;// 删除商品分类
	private static final int REMOVE_GOOD_STATUS = 8;// 删除商品
	private static final int ADD_GOOD_TYPE = 9;// 添加商品分类
	private static final int ZAN_NO_JIE_DAN = 11;
	private static final int CHECK_SHOP_INFO = 12;// 是否接单
	private int current_page = 1;
	private int total_page = 1;
	private int shopsid = 0;
	private int current_good_num = 0;
	private String tag = "";
	private ImageView iv_noselect;
	private ImageView iv_select;

	private ViewPager viewPager;
	private List<View> list = new ArrayList<View>();
	private Dialog proDialog;
	private TextView titleTextView;
	private PullRefreshView saleNumPullRefreshView, repertoryPullRefreshView,
			putawayTimePullRefreshView;
	private TextView saleNumInfoTextView, repertoryInfoTextView,
			putawayTimeInfoTextView;
	private ListView saleListView, repertoryListView, putawayListView;
	private TextView saleNumTextView, repertoryTextView, putawayTimeTextView;
	private ImageView saleStatusTextView, repertoryStatusTextView,
			putawayStatusTextView;
	private LinearLayout linearLayout1, linearLayout2, linearLayout3;
	private List<Good> saleNumGoods, repertoryGoods, putawayTimeGoods;
	private MyGoodsAdapter saleAdapter, repertoryAdapter, putawayAdapter;
	private LinearLayout fragmentButton;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList = null;
	private List<GoodType> goodTypeList = new ArrayList<GoodType>();
	private List<GoodType> currentGoodTypes = new ArrayList<GoodType>();
	private GoodType newGoodType = new GoodType(-2, -1, "新增品类", -1, null);
	private LeftAdapter leftAdapter;
	private Dialog newDialog;
	private int selected_good_isgroom = 1;
	private ImageView iv_add;
	private boolean isClosed = true;
	private PopupWindow pp_mune;
	private int is_business;
	private static final int REQUEST_DETAILS = 101;
	private static final int REQUEST_ADD = 102;
	private Handler handler = new Handler() {

		@SuppressLint("ResourceAsColor")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_GOOD_DETAILS:
				showUpdateRes(msg);
				break;
			case UPDATE_GOOD_TYPES:
				initListView();
				break;
			case REMOVE_STATUS:
				removeType(msg);
			case ADD_GOOD_TYPE:
				if (msg.arg1 == 0) {
					ToastUtil.show(GoodsActivity.this, "添加分类成功！");
					if (!NetConn.checkNetwork(GoodsActivity.this)) {
						// NetConn.setNetwork(GoodsActivity.this);
					} else {
						getGoodTypeData();
						CURRENT_OPT_TYPEID = 0;
						getData();
						initData();
					}
				} else if (msg.arg1 == 1) {
					ToastUtil.show(GoodsActivity.this, "添加分类失败！");
				} else if (msg.arg1 == 2) {
					ToastUtil.show(GoodsActivity.this, "分类名称已存在！");
				} else if (msg.arg1 == -1) {
					ToastUtil.show(GoodsActivity.this, "添加分类失败！");
				}
				break;

			case ZAN_NO_JIE_DAN:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					if (type == 1) {
						iv_noselect.setVisibility(View.VISIBLE);
						iv_select.setVisibility(View.GONE);
						ToastUtil.show(getApplicationContext(), "开始接单啦!");
					} else if (type == 2) {
						iv_noselect.setVisibility(View.GONE);
						iv_select.setVisibility(View.VISIBLE);
						ToastUtil.show(getApplicationContext(), "休息中停止接单了！");
					}
					if (type == 1) {
						type = 2;
					} else if (type == 2) {
						type = 1;
					}
				} else {
					ToastUtil.show(GoodsActivity.this, msg.obj + "");
				}
				break;
			case CHECK_SHOP_INFO:
				ShopInfo shopInfo = (ShopInfo) msg.obj;
				is_business = shopInfo.getIs_business();
				// 1、营业2、不营业
				if (is_business == 1) {
					iv_noselect.setVisibility(View.VISIBLE);
					iv_select.setVisibility(View.GONE);
					type = 2;
				} else if (is_business == 2) {
					iv_noselect.setVisibility(View.GONE);
					iv_select.setVisibility(View.VISIBLE);
					type = 1;
				}
				break;
			default:
				if (!isClosed) {
					proDialog.dismiss();
					isClosed = true;
				}
				getCurrentGoodList(msg);
				break;
			}
		}
	};
	private RelativeLayout title;
	private TextView tv_jiedan;
	private int type;
	private LinearLayout ll_add;

	/**
	 * 显示更新结果
	 * 
	 * @param message
	 */
	protected void showUpdateRes(Message message) {
		boolean flag = (Boolean) message.obj;
		if (flag) {
			LOAD_STATUS = NORMAL;
			initData();
		} else {
			ToastUtil.show(GoodsActivity.this, "更新失败！");
		}
	}

	/**
	 * 删除分类
	 * 
	 * @param msg
	 */
	protected void removeType(Message msg) {
		if (msg.arg1 == 0) {
			ToastUtil.show(GoodsActivity.this, "删除分类成功！");
			if (!NetConn.checkNetwork(GoodsActivity.this)) {
				// NetConn.setNetwork(GoodsActivity.this);
			} else {
				getGoodTypeData();
				CURRENT_OPT_TYPEID = 0;
				getData();
				initData();
				leftAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtil.show(GoodsActivity.this, "删除分类失败！");
		}
	}

	/*
	 * private void updateGoodStateUI() { getData( ); }
	 */

	/**
	 * 获取当前商品列表
	 * 
	 * @param msg
	 */
	private void getCurrentGoodList(Message msg) {
		List<Good> gds = (List<Good>) msg.obj;
		List<Good> currentGoods = new ArrayList<Good>();
		MyGoodsAdapter adapter = new MyGoodsAdapter(currentGoods,
				GoodsActivity.this);
		PullRefreshView currentPullRefreshView = new PullRefreshView(
				GoodsActivity.this);
		switch (msg.what) {
		case BUYNUM:
			currentGoods = saleNumGoods;
			adapter = saleAdapter;
			currentPullRefreshView = saleNumPullRefreshView;
			break;
		case NUM:
			currentGoods = repertoryGoods;
			adapter = repertoryAdapter;
			currentPullRefreshView = repertoryPullRefreshView;
			break;
		case GROUTIME:
			currentGoods = putawayTimeGoods;
			adapter = putawayAdapter;
			currentPullRefreshView = putawayTimePullRefreshView;
			break;
		}

		if (gds != null && !gds.isEmpty()) {
			putawayTimeInfoTextView.setVisibility(View.GONE);
			repertoryInfoTextView.setVisibility(View.GONE);
			saleNumInfoTextView.setVisibility(View.GONE);
			if (msg.arg1 == HEAD) {
				currentGoods.clear();
				currentGoods.addAll(gds);
			} else if (msg.arg1 == FOOT) {
				int over = msg.arg2;
				if (over == -1) {
					ToastUtil.show(this, "已经到底部了");
				} else if (over == -2) {
					currentGoods.addAll(gds);
					System.out.println("-----正在加载下页数据---" + gds);
				}
			} else {
				currentGoods.clear();
				currentGoods.addAll(gds);
			}
			adapter.notifyDataSetChanged();
			if (msg.arg1 == HEAD) {
				currentPullRefreshView.onHeaderRefreshComplete();
			} else if (msg.arg1 == FOOT) {
				currentPullRefreshView.onFooterRefreshComplete();
			}
			current_good_num = currentGoods.size();
		} else {
			if (msg.arg1 == NORMAL) {
				System.out.println("----当前类无数据----->" + msg.what);
				switch (msg.what) {
				case BUYNUM:
					putawayTimeInfoTextView.setVisibility(View.GONE);
					repertoryInfoTextView.setVisibility(View.GONE);
					saleNumInfoTextView.setVisibility(View.VISIBLE);
					break;
				case NUM:
					putawayTimeInfoTextView.setVisibility(View.GONE);
					repertoryInfoTextView.setVisibility(View.VISIBLE);
					saleNumInfoTextView.setVisibility(View.GONE);
					break;
				case GROUTIME:
					putawayTimeInfoTextView.setVisibility(View.VISIBLE);
					repertoryInfoTextView.setVisibility(View.GONE);
					saleNumInfoTextView.setVisibility(View.GONE);
					break;
				}
			} else {
				currentGoods.clear();
				adapter.notifyDataSetChanged();
				ToastUtil.show(this, "没有新数据");
			}
			current_good_num = 0;
		}
		switch (msg.what) {
		case BUYNUM:
			saleNumGoods = currentGoods;
			break;
		case NUM:
			repertoryGoods = currentGoods;
			break;
		case GROUTIME:
			putawayTimeGoods = currentGoods;
			break;
		}
		System.out.println("----current_good_num---->" + current_good_num);
		if (!isClosed) {
			proDialog.dismiss();
			isClosed = true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		showDialog();
		iv_noselect = (ImageView) findViewById(R.id.iv_noselect);
		iv_select = (ImageView) findViewById(R.id.iv_select);
		tv_jiedan = (TextView) findViewById(R.id.tv_jiedan);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		title = (RelativeLayout) findViewById(R.id.title);
		saleNumGoods = new ArrayList<Good>();
		repertoryGoods = new ArrayList<Good>();
		putawayTimeGoods = new ArrayList<Good>();
		CURRENT_OPT = BUYNUM;
		BUYNUM_STATUS = SX;
		NUM_STATUS = SX;
		GROUTIME_STATUS = SX;
		initpop();
		// 添加按钮
		iv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] location = new int[2];
				tv_jiedan.getLocationOnScreen(location);
				int x = location[0];
				pp_mune.showAsDropDown(titleTextView, x - 50, 10);
			}
		});
		ll_add = (LinearLayout) findViewById(R.id.ll_add);
		initView(getLayoutInflater());
		if (!NetConn.checkNetwork(GoodsActivity.this)) {
		} else {
			tag = getIntent().getStringExtra("tag");
			System.out.println("------tag------->" + tag);
			getGoodTypeData();
			CURRENT_OPT_TYPEID = 0;
			select1();
			getData();
			chekShopInfo();
		}

		ll_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_onClick();
			}
		});
	}

	/**
	 * 进入这个页面的时候 判断 是否接单的状态
	 */
	private void chekShopInfo() {
		// if (!NetConn.checkNetwork(GoodsActivity.this)) {
		// } else {
		new ChekShopInfoAsyc().execute();
		// }
	}

	private class ChekShopInfoAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			SettingService settingService = new SettingService(
					GoodsActivity.this);
			Message message = handler.obtainMessage(CHECK_SHOP_INFO);
			ShopInfo res = settingService.checkShopInfo2(shopsid);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	protected void initpop() {
		View need_mune_select_layout = View.inflate(this, R.layout.pop_menu,
				null);
		need_mune_select_layout.findViewById(R.id.ll_add_good)
				.setOnClickListener(this);
		need_mune_select_layout.findViewById(R.id.ll_type).setOnClickListener(
				this);
		need_mune_select_layout.findViewById(R.id.ll_setting)
				.setOnClickListener(this);
		pp_mune = new PopupWindow(need_mune_select_layout, DensityUtils.dp2px(
				GoodsActivity.this, 150),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		pp_mune.setFocusable(true);
		pp_mune.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	public void onResume() {
		super.onResume();
		if (!NetConn.checkNetwork(GoodsActivity.this)) {
			// // NetConn.setNetwork(OrdersActivity.this);
		} else {
			getData();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		proDialog.dismiss();
		super.onDestroy();
	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
		isClosed = false;
		newDialog = showNewDialog();
	}

	/**
	 * 初始化视图
	 * 
	 * @param inflater
	 */
	private void initView(LayoutInflater inflater) {
		LoginManager lm = LoginManager.getInstance(GoodsActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		fragmentButton = (LinearLayout) findViewById(R.id.btn);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		titleTextView = (TextView) findViewById(R.id.title_font);
		viewPager = (ViewPager) findViewById(R.id.vPager);
		saleNumTextView = (TextView) findViewById(R.id.sale_num);
		repertoryTextView = (TextView) findViewById(R.id.kucun_num);
		putawayTimeTextView = (TextView) findViewById(R.id.putaway_time);
		linearLayout1 = (LinearLayout) findViewById(R.id.linear1);
		linearLayout2 = (LinearLayout) findViewById(R.id.linear2);
		linearLayout3 = (LinearLayout) findViewById(R.id.linear3);
		saleStatusTextView = (ImageView) findViewById(R.id.sale_status);
		repertoryStatusTextView = (ImageView) findViewById(R.id.kucun_status);
		putawayStatusTextView = (ImageView) findViewById(R.id.putaway_status);

		// 显示Fragment
		fragmentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("-------被点击了-------->");
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});

		linearLayout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select1();
				if (BUYNUM_STATUS == 0) {
					saleStatusTextView
							.setImageResource(R.drawable.jiangxu_icon);
					BUYNUM_STATUS = 1;
				} else {
					saleStatusTextView
							.setImageResource(R.drawable.shengxu_icon);
					BUYNUM_STATUS = 0;
				}
				CURRENT_OPT = BUYNUM;
				CURRENT_OPT_STATUS = BUYNUM_STATUS;
				LOAD_STATUS = NORMAL;
				getData();
			}
		});
		linearLayout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select2();
				if (NUM_STATUS == 0) {
					repertoryStatusTextView
							.setImageResource(R.drawable.jiangxu_icon);
					NUM_STATUS = 1;
				} else {
					repertoryStatusTextView
							.setImageResource(R.drawable.shengxu_icon);
					NUM_STATUS = 0;
				}
				CURRENT_OPT = NUM;
				CURRENT_OPT_STATUS = NUM_STATUS;
				LOAD_STATUS = NORMAL;
				getData();
			}
		});
		linearLayout3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select3();
				if (GROUTIME_STATUS == 0) {
					putawayStatusTextView
							.setImageResource(R.drawable.jiangxu_icon);
					GROUTIME_STATUS = 1;
				} else {
					putawayStatusTextView
							.setImageResource(R.drawable.shengxu_icon);
					GROUTIME_STATUS = 0;
				}
				CURRENT_OPT = GROUTIME;
				CURRENT_OPT_STATUS = GROUTIME_STATUS;
				LOAD_STATUS = NORMAL;
				getData();
			}
		});

		list.add(inflater.inflate(R.layout.history_listview, null));
		list.add(inflater.inflate(R.layout.history_listview, null));
		list.add(inflater.inflate(R.layout.history_listview, null));
		viewPager.setAdapter(new MyViewPagerAdapter(list));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new VPageChangeListener());

		saleAdapter = new MyGoodsAdapter(saleNumGoods, GoodsActivity.this);
		saleListView = (ListView) list.get(0).findViewById(
				R.id.history_listview);
		saleListView.setAdapter(saleAdapter);
		saleAdapter.notifyDataSetChanged();
		saleListView.setOnItemClickListener(this);

		repertoryAdapter = new MyGoodsAdapter(repertoryGoods,
				GoodsActivity.this);
		repertoryListView = (ListView) list.get(1).findViewById(
				R.id.history_listview);
		repertoryListView.setAdapter(repertoryAdapter);
		repertoryListView.setOnItemClickListener(this);
		repertoryAdapter.notifyDataSetChanged();

		putawayAdapter = new MyGoodsAdapter(putawayTimeGoods,
				GoodsActivity.this);
		putawayListView = (ListView) list.get(2).findViewById(
				R.id.history_listview);
		putawayListView.setAdapter(putawayAdapter);
		putawayListView.setOnItemClickListener(this);
		putawayAdapter.notifyDataSetChanged();

		saleNumInfoTextView = (TextView) list.get(0).findViewById(R.id.info);
		repertoryInfoTextView = (TextView) list.get(1).findViewById(R.id.info);
		putawayTimeInfoTextView = (TextView) list.get(2)
				.findViewById(R.id.info);

		saleNumPullRefreshView = (PullRefreshView) list.get(0).findViewById(
				R.id.his_pull_refresh_view);
		saleNumPullRefreshView.setOnHeaderRefreshListener(this);
		saleNumPullRefreshView.setOnFooterRefreshListener(this);

		repertoryPullRefreshView = (PullRefreshView) list.get(1).findViewById(
				R.id.his_pull_refresh_view);
		repertoryPullRefreshView.setOnHeaderRefreshListener(this);
		repertoryPullRefreshView.setOnFooterRefreshListener(this);

		putawayTimePullRefreshView = (PullRefreshView) list.get(2)
				.findViewById(R.id.his_pull_refresh_view);
		putawayTimePullRefreshView.setOnHeaderRefreshListener(this);
		putawayTimePullRefreshView.setOnFooterRefreshListener(this);

	}

	/**
	 * 点击菜单
	 * 
	 * @param view
	 */
	public void open_onClick(View view) {
		System.out.println("-------被点击了-open_onClick------->");
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	/**
	 * 获取商品类别数据
	 */
	private void getGoodTypeData() {
		// if (!NetConn.checkNetwork(GoodsActivity.this)) {
		// // NetConn.setNetwork(OrdersActivity.this);
		// } else {
		new Thread(new Runnable() {

			@Override
			public void run() {
				goodTypeList = new ArrayList<GoodType>();
				GoodService goodService = new GoodService(GoodsActivity.this);
				goodTypeList = goodService.getGoodTypeList(shopsid, "");
				System.out.println("----net---goodTypeList----->"
						+ goodTypeList);
				Message message = handler.obtainMessage(UPDATE_GOOD_TYPES);
				handler.sendMessage(message);
			}
		}).start();
		// }
	}

	/**
	 * 左部的ViewHolder
	 * 
	 * @author lqq
	 * 
	 */
	private class ViewHolderLeft {
		TextView item;
	}

	/**
	 * 左部的Listview的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class LeftAdapter extends ArrayAdapter<GoodType> {
		private List<GoodType> list = new ArrayList<GoodType>();

		public LeftAdapter(Context cx, int resId, List<GoodType> objs) {
			super(cx, resId, objs);
			this.list.clear();
			if (objs != null) {
				this.list = objs;
			}
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolderLeft holder;
			if (cv == null) {
				// FIXME
				cv = LayoutInflater.from(getContext()).inflate(
						R.layout.good_type_list_item, null);
				holder = new ViewHolderLeft();
				holder.item = (TextView) cv.findViewById(R.id.item);
				cv.setTag(holder);
			} else {
				holder = (ViewHolderLeft) cv.getTag();
			}
			GoodType goodType = list.get(position);
			holder.item.setText(goodType.getName());
			if (goodType.getId() == -2) {
				// holder.item.setBackgroundResource(R.drawable.back);
				holder.item.setTextColor(R.color.content_color_black);
			}
			if (goodType.getId() == 0 && CURRENT_OPT_TYPEID == 0) {
				// holder.item.setBackgroundResource(R.drawable.new_add_type);
				holder.item.setBackgroundColor(Color.parseColor("#00C1FF"));
				holder.item.setTextColor(Color.WHITE);
			}
			return cv;
		}
	}

	/**
	 * 初始化左侧商品分类列表
	 */
	private void initListView() {

		// id = 0 全部
		// id = -1 其他
		// id = -2 新增
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// if(currentGoodTypes.size() > 0){
		// currentGoodTypes.clear();
		// }
		currentGoodTypes = goodTypeList;
		System.out.println("---goodTypeList--->" + goodTypeList);
		if (!currentGoodTypes.contains(newGoodType)) {
			currentGoodTypes.add(newGoodType);
		}
		System.out.println("---current--list--->" + currentGoodTypes);
		leftAdapter = new LeftAdapter(GoodsActivity.this,
				R.layout.good_type_list_item, currentGoodTypes);
		mDrawerList.setAdapter(leftAdapter);
		// FIXME
		// mDrawerList.setSelection(0);
		// mDrawerList.getSelectedView().setBackgroundResource(R.drawable.new_add_type);
		mDrawerList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long arg3) {
				LOAD_STATUS = NORMAL;
				mDrawerLayout.closeDrawer(mDrawerList);
				GoodType goodType = currentGoodTypes.get(position);
				CURRENT_OPT_TYPEID = goodType.getId();

				for (int i = 0; i < parent.getChildCount(); i++) {
					int currentId = currentGoodTypes.get(i).getId();
					if (currentId == -2) { // 最后一条
						// parent.getChildAt(i).setBackgroundResource(R.drawable.back);
						((TextView) parent.getChildAt(i))
								.setTextColor(R.color.content_color_black);
						// ((TextView)parent.getChildAt(i)).setTextColor(Color.GRAY);
					} else {
						// parent.getChildAt(i).setBackgroundColor(Color.parseColor("#c6c6c6"));
						parent.getChildAt(i).setBackgroundColor(Color.WHITE);
						((TextView) parent.getChildAt(i))
								.setTextColor(R.color.content_color_black);
					}
				}

				if (CURRENT_OPT_TYPEID != -2) {
					/*
					 * ((TextView)view).setBackgroundResource(R.drawable.
					 * new_add_type);
					 * ((TextView)view).setTextColor(Color.WHITE);
					 */
					((TextView) view).setBackgroundColor(Color
							.parseColor("#00C1FF"));
					((TextView) view).setTextColor(Color.WHITE);

				}
				if (CURRENT_OPT_TYPEID == -2) {
					// ((TextView)view).setBackgroundResource(R.drawable.back);
					((TextView) view)
							.setBackgroundResource(R.color.content_color_black);
					((TextView) view).setTextColor(Color.WHITE);
					return false;
				}
				if (CURRENT_OPT_TYPEID == -1 || CURRENT_OPT_TYPEID == 0) {
					ToastUtil.show(GoodsActivity.this, "该分类不能删除！");
					return false;
				}
				// FIXME　点击其他条目
				// 提示是否要删除该商品分类
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						GoodsActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage("是否要删除该商品分类(分类中的商品移到“其他”分类中)?");
				dialog.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 　删除该商品分类
								final Message message = handler
										.obtainMessage(REMOVE_STATUS);
								if (!NetConn.checkNetwork(GoodsActivity.this)) {
									// NetConn.setNetwork(OrdersActivity.this);
								} else {
									new Thread(new Runnable() {
										@Override
										public void run() {
											// message.arg1=1 删除失败
											// message.arg1=0 删除成功
											GoodService goodService = new GoodService(
													GoodsActivity.this);
											boolean flag = goodService
													.deleteGoodType(CURRENT_OPT_TYPEID);
											if (flag) {
												message.arg1 = 0;
											} else {
												message.arg1 = 1;
											}
											handler.sendMessage(message);
										}
									}).start();
								}
							}
						});
				dialog.setNegativeButton("取消", null);
				dialog.show();
				// ToastUtil.show(GoodsActivity.this,
				// "暂不删除--"+goodType.getName());
				return false;
			}
		});
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoodType goodType = currentGoodTypes.get(position);
				int rember = CURRENT_OPT_TYPEID;
				CURRENT_OPT_TYPEID = goodType.getId();

				System.out.println("-------CURRENT_OPT_TYPEID---->"
						+ CURRENT_OPT_TYPEID);

				for (int i = 0; i < parent.getChildCount(); i++) {
					int currentId = currentGoodTypes.get(i).getId();
					System.out.println("----currentid----->" + currentId);
					if (currentId == -2) { // 最后一条
						// parent.getChildAt(i).setBackgroundResource(R.drawable.back);
						((TextView) parent.getChildAt(i))
								.setTextColor(R.color.content_color_black);
					} else {
						// parent.getChildAt(i).setBackgroundColor(Color.parseColor("#c6c6c6"));
						parent.getChildAt(i).setBackgroundColor(Color.WHITE);
						((TextView) parent.getChildAt(i))
								.setTextColor(R.color.content_color_black);
					}
				}
				if (CURRENT_OPT_TYPEID != -2) {
					// ((TextView)view).setBackgroundResource(R.drawable.new_add_type);
					((TextView) view).setBackgroundColor(Color
							.parseColor("#00C1FF"));
					((TextView) view).setTextColor(Color.WHITE);

				}
				if (CURRENT_OPT_TYPEID == -2) {
					// FIXME
					CURRENT_OPT_TYPEID = rember;
					addType();
				} else {
					LOAD_STATUS = NORMAL;
					initData();
				}
				leftAdapter.notifyDataSetChanged();
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
	}

	/**
	 * 显示新的Dialog
	 * 
	 * @return
	 */
	private Dialog showNewDialog() {
		Dialog dialog = new Dialog(GoodsActivity.this,
				R.style.theme_dialog_alert);
		return dialog;
	}

	/**
	 * 添加商品分类
	 */
	private void addType() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_type_dialogview, null);
		layout.setMinimumWidth((int) (GoodsActivity.this.getWindowManager()
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
					System.out.println("-------length-->"
							+ name.getBytes().length);
					if (name.length() > 6) {
						ToastUtil.show(GoodsActivity.this, "分类名称不能超过6个字");
					} else {
						if (!NetConn.checkNetwork(GoodsActivity.this)) {
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
					ToastUtil.show(GoodsActivity.this, "请填写名称");
				}
			}
		});
		newDialog.show();// 显示AlertDialog
	}

	/**
	 * 添加类型名称
	 * 
	 * @param typename
	 */
	private void addTypeName(String typename) {
		GoodService goodService = new GoodService(GoodsActivity.this);
		int flag = goodService.addGoodType1(typename);
		Message message = handler.obtainMessage(ADD_GOOD_TYPE);
		message.arg1 = flag;
		handler.sendMessage(message);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		getData();
	}

	/**
	 * 获取当前选项当前状态下 的商品列表并发送消息
	 */
	private void getData() {
		// if (!NetConn.checkNetwork(GoodsActivity.this)) {
		// // NetConn.setNetwork(OrdersActivity.this);
		// } else {
		if (isClosed) {
			proDialog.show();
			isClosed = false;
		}
		new MyAsync().execute();
		// }
	}

	/**
	 * 获取商品列表的异步任务
	 * 
	 * @author lqq
	 * 
	 */
	private class MyAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			System.out.println("----total_page---" + total_page
					+ "----current_page-----" + current_page);
			GoodService goodService = new GoodService(GoodsActivity.this);
			List<Good> goods = new ArrayList<Good>();
			Message message = handler.obtainMessage(CURRENT_OPT);
			message.arg1 = LOAD_STATUS;
			if (LOAD_STATUS == HEAD || LOAD_STATUS == NORMAL) {
				goods = goodService.getGoodsList(shopsid, CURRENT_OPT_TYPEID,
						null, getCurrentOpt(), getCurrentOptStatus(), 1, tag);
			} else if (LOAD_STATUS == FOOT) {
				if (current_page >= total_page) {
					message.arg2 = -1;
					current_page = total_page;
				} else if (current_page < total_page) {
					message.arg2 = -2;
					current_page++;
				}
				goods = goodService.getGoodsList(shopsid, CURRENT_OPT_TYPEID,
						null, getCurrentOpt(), getCurrentOptStatus(),
						current_page, tag);
			}
			total_page = goodService.total_page;
			current_page = goodService.p;
			System.out.println("----total_page---" + total_page
					+ "----current_page-----" + current_page);
			message.obj = goods;
			handler.sendMessage(message);
			return null;
		}

	}

	/**
	 * 获取当前选项
	 * 
	 * @return
	 */
	private String getCurrentOpt() {
		String currentOpt = null;
		switch (CURRENT_OPT) {
		case BUYNUM:
			currentOpt = "buynum";
			break;
		case NUM:
			currentOpt = "num";
			break;
		case GROUTIME:
			currentOpt = "addtime";
			break;
		}
		return currentOpt;
	}

	/**
	 * 获取当前选项排序
	 * 
	 * @return
	 */
	private String getCurrentOptStatus() {
		String currentOptStatus = null;
		switch (CURRENT_OPT_STATUS) {
		case SX:
			currentOptStatus = "asc";
			break;
		case JX:
			currentOptStatus = "desc";
			break;
		}
		return currentOptStatus;
	}

	/**
	 * 商品项的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class MyGoodsAdapter extends BaseAdapter {
		private List<Good> list;
		private LayoutInflater inflater;

		public MyGoodsAdapter(List<Good> list, Context context) {
			this.list = list;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View cv, ViewGroup arg2) {
			ViewHolder holder;
			if (cv == null) {
				cv = inflater.inflate(R.layout.good_list_item, arg2, false);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			Good good = (Good) getItem(position);
			cv.setId(good.getId());
			refreshItem(holder, good);
			return cv;
		}

	}

	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	private void refreshItem(ViewHolder holder, Good good) {
		// FIXME　差一个分享
		String imgPath = good.getPicpath();

		if (null != imgPath && imgPath.contains(";")) {
			String[] splitImage = imgPath.split(";");
			ImageLoadTool.disPlay(splitImage[0], holder.goodPicImageView,
					R.drawable.default_good_img);
		} else {
			ImageLoadTool.disPlay(imgPath, holder.goodPicImageView,
					R.drawable.default_good_img);
		}

		String name = "";
		if (good.getGoods().toString().length() >= 11) {
			name = good.getGoods().substring(0, 11) + "...";
		} else {
			name = good.getGoods().toString();
		}
		holder.nameTextView.setText((name == null || name == "null"
				|| name.equals(null) || name.equals("null")) ? "" : name);
		// holder.nameTextView.setSelected(true);
		String price = "";
		if (String.valueOf(good.getGoodsprice()).length() > 12) {
			price = String.valueOf(good.getGoodsprice()).substring(0, 12)
					+ "..";
		} else {
			price = String.valueOf(good.getGoodsprice());
		}
		holder.priceTextView.setText("￥" + good.getShowGoodsprice());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String date = formatter
					.format(Long.parseLong(good.getAddtime()) * 1000);
			holder.putawayTimeTextView.setText(date + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.kucunNumTextView.setText(good.getNum() == -1 ? "不限" : good
				.getNum() + "");
		holder.saleNumTextView.setText(good.getBuynum() + "");
		holder.unitTextView2.setText(good.getUnit() == null ? "" : good
				.getUnit());
		holder.unitTextView1.setText(good.getUnit() == null ? "" : good
				.getUnit());
		if (good.getState() == 1) {// 上架商品
			holder.statusTextView.setText("在售");
			// holder.statusTextView.setBackgroundResource(R.drawable.status_no_select);
			holder.statusTextView.setBackgroundResource(R.drawable.orangeshape);
			if (good.getIsgroom() == 2) {// 推荐商品
				// holder.recommendTextView.setBackgroundResource(R.drawable.status_no_select);
				holder.recommendTextView.setText("已推荐");
				holder.recommendTextView
						.setBackgroundResource(R.drawable.orangeshape);
			} else {
				// holder.recommendTextView.setBackgroundResource(R.drawable.status_select);
				holder.recommendTextView.setText("未推荐");
				holder.recommendTextView
						.setBackgroundResource(R.drawable.goods_gray_shape);
			}
		} else if (good.getState() == 2) {
			holder.statusTextView.setText("停售");
			// holder.statusTextView.setBackgroundResource(R.drawable.status_select);
			// holder.recommendTextView.setBackgroundResource(R.drawable.status_no_select);
			holder.statusTextView
					.setBackgroundResource(R.drawable.goods_gray_shape);
			holder.recommendTextView
					.setBackgroundResource(R.drawable.goods_gray_shape);
		}
		if (good.getIsgroom() == 2) {// 推荐商品
			holder.recommendTextView.setText("已推荐");
		} else {
			holder.recommendTextView.setText("未推荐");
		}
		holder.recommendTextView.setTag(good);
		holder.recommendTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				int isgroom = ((Good) v.getTag()).getIsgroom();
				System.out.println("-----isgroom----->" + isgroom);
				if (isgroom == 1) {
					if (((Good) v.getTag()).getState() == 2) {
						ToastUtil.show(GoodsActivity.this, "该商品已下架！");
					} else {
						selected_good_isgroom = 2;
						if (!NetConn.checkNetwork(GoodsActivity.this)) {
							// NetConn.setNetwork(OrdersActivity.this);
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									updateGoodIsgroom((Good) v.getTag(),
											selected_good_isgroom);
								}
							}).start();
						}
					}
				} else if (isgroom == 2) {

					if (((Good) v.getTag()).getState() == 2) {
						ToastUtil.show(GoodsActivity.this, "该商品已下架！");
					} else if (!NetConn.checkNetwork(GoodsActivity.this)) {
						// NetConn.setNetwork(OrdersActivity.this);
					} else {
						selected_good_isgroom = 1;
						new Thread(new Runnable() {
							@Override
							public void run() {
								updateGoodIsgroom((Good) v.getTag(),
										selected_good_isgroom);
							}
						}).start();
					}
				}
			}
		});

		holder.statusTextView.setTag(good);
		holder.statusTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {

				if (!NetConn.checkNetwork(GoodsActivity.this)) {
					// NetConn.setNetwork(OrdersActivity.this);
				} else {
					new Thread(new Runnable() {

						@Override
						public void run() {
							int state = ((Good) v.getTag()).getState();
							if (state == 1) {
								state = 2;
							} else if (state == 2) {
								state = 1;
							}
							updateGoodState((Good) v.getTag(), state);
						}
					}).start();
				}
			}
		});
	}

	/**
	 * 更新商品的推荐状态
	 * 
	 * @param good
	 * @param isgroom
	 */
	private void updateGoodIsgroom(Good good, int isgroom) {
		GoodService goodService = new GoodService(GoodsActivity.this);
		boolean flag = goodService.updateGoodInfo(shopsid, good.getId(),
				good.getTypeid(), good.getGoodsprice(), good.getNum(),
				good.getUnit(), good.getContent(), good.getGoods(), isgroom,
				good.getState());
		Message message = handler.obtainMessage(UPDATE_GOOD_DETAILS);
		message.obj = flag;
		handler.sendMessage(message);
	}

	/**
	 * 更新商品的上下架状态
	 * 
	 * @param good
	 * @param state
	 */
	private void updateGoodState(Good good, int state) {
		GoodService goodService = new GoodService(GoodsActivity.this);
		boolean flag = goodService.updateGoodInfo(shopsid, good.getId(),
				good.getTypeid(), good.getGoodsprice(), good.getNum(),
				good.getUnit(), good.getContent(), good.getGoods(),
				good.getIsgroom(), state);
		Message message = handler.obtainMessage(UPDATE_GOOD_DETAILS);
		message.obj = flag;
		handler.sendMessage(message);
	}

	/**
	 * 查找视图UI
	 * 
	 * @param holder
	 * @param cv
	 */
	private void findViews(ViewHolder holder, View cv) {
		holder.nameTextView = (TextView) cv.findViewById(R.id.name);
		holder.priceTextView = (TextView) cv.findViewById(R.id.price);
		holder.saleNumTextView = (TextView) cv.findViewById(R.id.sale_num);
		holder.putawayTimeTextView = (TextView) cv
				.findViewById(R.id.putaway_time);
		holder.statusTextView = (TextView) cv.findViewById(R.id.status);
		holder.kucunNumTextView = (TextView) cv.findViewById(R.id.kucun_num);
		holder.goodPicImageView = (ImageView) cv.findViewById(R.id.icon);
		holder.unitTextView2 = (TextView) cv.findViewById(R.id.danwei2);
		holder.unitTextView1 = (TextView) cv.findViewById(R.id.danwei);
		holder.recommendTextView = (TextView) cv.findViewById(R.id.recommend);
	}

	/**
	 * 商品列表的ViewHolder
	 * 
	 * @author lqq
	 * 
	 */
	private class ViewHolder {
		TextView nameTextView;
		TextView priceTextView;
		TextView saleNumTextView;
		TextView putawayTimeTextView;
		TextView statusTextView;
		TextView kucunNumTextView;
		TextView unitTextView1;
		TextView unitTextView2;
		TextView recommendTextView;
		ImageView goodPicImageView;
	}

	/**
	 * 选中第一项
	 */
	private void select1() {
		CURRENT_OPT = BUYNUM;
		CURRENT_OPT_STATUS = BUYNUM_STATUS;
		LOAD_STATUS = NORMAL;
		linearLayout1.setBackgroundResource(R.drawable.select_bg);
		// saleNumTextView.setTextColor(Color.WHITE);
		linearLayout2.setBackgroundResource(R.drawable.no_select_bg);
		// repertoryTextView.setTextColor(Color.parseColor("#424242"));
		linearLayout3.setBackgroundResource(R.drawable.no_select_bg);
		// putawayTimeTextView.setTextColor(Color.parseColor("#424242"));
		// titleTextView.setText("销量");
		viewPager.setCurrentItem(0);
	}

	/**
	 * 选中第二项
	 */
	private void select2() {
		CURRENT_OPT = NUM;
		CURRENT_OPT_STATUS = NUM_STATUS;
		LOAD_STATUS = NORMAL;
		linearLayout2.setBackgroundResource(R.drawable.select_bg);
		// titleTextView.setText("库存");
		// repertoryTextView.setTextColor(Color.WHITE);
		linearLayout1.setBackgroundResource(R.drawable.no_select_bg);
		// saleNumTextView.setTextColor(Color.parseColor("#424242"));
		linearLayout3.setBackgroundResource(R.drawable.no_select_bg);
		// putawayTimeTextView.setTextColor(Color.parseColor("#424242"));
		viewPager.setCurrentItem(1);
	}

	/**
	 * 选中第三项
	 */
	private void select3() {
		CURRENT_OPT = GROUTIME;
		CURRENT_OPT_STATUS = GROUTIME_STATUS;
		LOAD_STATUS = NORMAL;
		// titleTextView.setText("上架时间");
		linearLayout3.setBackgroundResource(R.drawable.select_bg);
		// putawayTimeTextView.setTextColor(Color.WHITE);
		linearLayout2.setBackgroundResource(R.drawable.no_select_bg);
		// repertoryTextView.setTextColor(Color.parseColor("#424242"));
		linearLayout1.setBackgroundResource(R.drawable.no_select_bg);
		// saleNumTextView.setTextColor(Color.parseColor("#424242"));
		viewPager.setCurrentItem(2);
	}

	/**
	 * 商品图片的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class MyViewPagerAdapter extends PagerAdapter {
		private List<View> listviews;

		public MyViewPagerAdapter(List<View> listviews) {
			this.listviews = listviews;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(listviews.get(arg1), 0);
			return listviews.get(arg1);
		}

		@Override
		public int getCount() {
			return listviews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewGroup) container).removeView((View) object);
			object = null;
		}
	}

	/**
	 * ViewPager 的监听
	 * 
	 * @author lqq
	 * 
	 */
	private class VPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageSelected(int arg0) {
			setCurrentTab(arg0);
		}
	}

	/**
	 * 设置当前的选项参数
	 * 
	 * @param index
	 */
	private void setCurrentTab(int index) {
		switch (index) {
		case 0:
			select1();
			break;
		case 1:
			select2();
			break;
		case 2:
			select3();
			break;
		}
		getData();
	}

	@Override
	public void onFooterRefresh(PullRefreshView view) {
		LOAD_STATUS = FOOT;
		getData();
	}

	@Override
	public void onHeaderRefresh(PullRefreshView view) {
		LOAD_STATUS = HEAD;
		getData();
	}

	/**
	 * 单项点击
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Intent intent = new Intent(GoodsActivity.this,
				GoodDeatilsActivity.class);
		intent.putExtra("id", view.getId());
		startActivityForResult(intent, REQUEST_DETAILS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_DETAILS) {
			initData();
		} else if (resultCode == RESULT_OK && requestCode == REQUEST_ADD) {
			initData();
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

	/**
	 * 暂不接单
	 * 
	 * @param view
	 */
	public void add_onClick() {

		if (!NetConn.checkNetwork(GoodsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					GoodService service = new GoodService(GoodsActivity.this);
					String res = service.updateShopInfo(shopsid, type);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(res);
						Message message = handler.obtainMessage(ZAN_NO_JIE_DAN);
						message.arg1 = jsonObject.getInt("res");
						message.obj = jsonObject.getString("msg");
						handler.sendMessage(message);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
		}

	}

	/**
	 * 刷新单击
	 */
	public void refresh_onClick() {
		LOAD_STATUS = NORMAL;
		getData();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ll_setting:
			pp_mune.dismiss();
			intent.setClass(getApplicationContext(),
					SettingSendInfoActivity.class);
			intent.putExtra("type", "0");
			startActivity(intent);
			break;

		case R.id.ll_add_good:
			pp_mune.dismiss();
			intent = new Intent(GoodsActivity.this, GoodAddActivity.class);
			startActivityForResult(intent, REQUEST_ADD);
			break;
		case R.id.ll_type:
			pp_mune.dismiss();
			intent = new Intent(GoodsActivity.this,
					SortManagementActivity.class);
			startActivity(intent);
		}
	}
}
