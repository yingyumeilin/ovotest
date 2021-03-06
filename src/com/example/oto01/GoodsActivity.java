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
 * ??????????????????
 * 
 * @author lqq
 * 
 */
public class GoodsActivity extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnItemClickListener,
		OnClickListener {
	// 0???????????????1????????????
	private static final int SX = 0;
	private static final int JX = 1;
	private static int BUYNUM_STATUS = SX;
	private static int NUM_STATUS = SX;
	private static int GROUTIME_STATUS = SX;
	private static final int HEAD = -1;
	private static final int FOOT = -2;
	private static final int NORMAL = -3;

	private static final int BUYNUM = 2;// ??????
	private static final int NUM = 3;// ??????
	private static final int GROUTIME = 4;// ????????????

	private static int LOAD_STATUS = NORMAL;// ?????????????????????
	private static int CURRENT_OPT = BUYNUM;// ????????????
	private static int CURRENT_OPT_STATUS = SX;// ?????????????????????
	private static int CURRENT_OPT_TYPEID = 0;// ????????????????????????id

	private static final int UPDATE_GOOD_DETAILS = 5;// ????????????????????????
	private static final int UPDATE_GOOD_TYPES = 6;// ??????????????????
	private static final int REMOVE_STATUS = 7;// ??????????????????
	private static final int REMOVE_GOOD_STATUS = 8;// ????????????
	private static final int ADD_GOOD_TYPE = 9;// ??????????????????
	private static final int ZAN_NO_JIE_DAN = 11;
	private static final int CHECK_SHOP_INFO = 12;// ????????????
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
	private GoodType newGoodType = new GoodType(-2, -1, "????????????", -1, null);
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
					ToastUtil.show(GoodsActivity.this, "?????????????????????");
					if (!NetConn.checkNetwork(GoodsActivity.this)) {
						// NetConn.setNetwork(GoodsActivity.this);
					} else {
						getGoodTypeData();
						CURRENT_OPT_TYPEID = 0;
						getData();
						initData();
					}
				} else if (msg.arg1 == 1) {
					ToastUtil.show(GoodsActivity.this, "?????????????????????");
				} else if (msg.arg1 == 2) {
					ToastUtil.show(GoodsActivity.this, "????????????????????????");
				} else if (msg.arg1 == -1) {
					ToastUtil.show(GoodsActivity.this, "?????????????????????");
				}
				break;

			case ZAN_NO_JIE_DAN:
				proDialog.dismiss();
				if (msg.arg1 == 0) {
					if (type == 1) {
						iv_noselect.setVisibility(View.VISIBLE);
						iv_select.setVisibility(View.GONE);
						ToastUtil.show(getApplicationContext(), "???????????????!");
					} else if (type == 2) {
						iv_noselect.setVisibility(View.GONE);
						iv_select.setVisibility(View.VISIBLE);
						ToastUtil.show(getApplicationContext(), "???????????????????????????");
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
				// 1?????????2????????????
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
	 * ??????????????????
	 * 
	 * @param message
	 */
	protected void showUpdateRes(Message message) {
		boolean flag = (Boolean) message.obj;
		if (flag) {
			LOAD_STATUS = NORMAL;
			initData();
		} else {
			ToastUtil.show(GoodsActivity.this, "???????????????");
		}
	}

	/**
	 * ????????????
	 * 
	 * @param msg
	 */
	protected void removeType(Message msg) {
		if (msg.arg1 == 0) {
			ToastUtil.show(GoodsActivity.this, "?????????????????????");
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
			ToastUtil.show(GoodsActivity.this, "?????????????????????");
		}
	}

	/*
	 * private void updateGoodStateUI() { getData( ); }
	 */

	/**
	 * ????????????????????????
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
					ToastUtil.show(this, "??????????????????");
				} else if (over == -2) {
					currentGoods.addAll(gds);
					System.out.println("-----????????????????????????---" + gds);
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
				System.out.println("----??????????????????----->" + msg.what);
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
				ToastUtil.show(this, "???????????????");
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
		// ????????????
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
	 * ??????????????????????????? ?????? ?????????????????????
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
	 * ??????Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
		isClosed = false;
		newDialog = showNewDialog();
	}

	/**
	 * ???????????????
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

		// ??????Fragment
		fragmentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("-------????????????-------->");
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
	 * ????????????
	 * 
	 * @param view
	 */
	public void open_onClick(View view) {
		System.out.println("-------????????????-open_onClick------->");
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	/**
	 * ????????????????????????
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
	 * ?????????ViewHolder
	 * 
	 * @author lqq
	 * 
	 */
	private class ViewHolderLeft {
		TextView item;
	}

	/**
	 * ?????????Listview????????????
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
	 * ?????????????????????????????????
	 */
	private void initListView() {

		// id = 0 ??????
		// id = -1 ??????
		// id = -2 ??????
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
					if (currentId == -2) { // ????????????
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
					ToastUtil.show(GoodsActivity.this, "????????????????????????");
					return false;
				}
				// FIXME?????????????????????
				// ????????????????????????????????????
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						GoodsActivity.this);
				dialog.setTitle("??????");
				dialog.setMessage("??????????????????????????????(?????????????????????????????????????????????)?");
				dialog.setPositiveButton("??????",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ????????????????????????
								final Message message = handler
										.obtainMessage(REMOVE_STATUS);
								if (!NetConn.checkNetwork(GoodsActivity.this)) {
									// NetConn.setNetwork(OrdersActivity.this);
								} else {
									new Thread(new Runnable() {
										@Override
										public void run() {
											// message.arg1=1 ????????????
											// message.arg1=0 ????????????
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
				dialog.setNegativeButton("??????", null);
				dialog.show();
				// ToastUtil.show(GoodsActivity.this,
				// "????????????--"+goodType.getName());
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
					if (currentId == -2) { // ????????????
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
	 * ????????????Dialog
	 * 
	 * @return
	 */
	private Dialog showNewDialog() {
		Dialog dialog = new Dialog(GoodsActivity.this,
				R.style.theme_dialog_alert);
		return dialog;
	}

	/**
	 * ??????????????????
	 */
	private void addType() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_type_dialogview, null);
		layout.setMinimumWidth((int) (GoodsActivity.this.getWindowManager()
				.getDefaultDisplay().getWidth() * 0.8));// ??????dialog?????????
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
				// ?????????????????????
				if (editText.getText().toString() != null
						&& !editText.getText().toString().equals("")
						&& editText.getText().toString() != "") {
					String name = editText.getText().toString().trim();
					System.out.println("-------length-->"
							+ name.getBytes().length);
					if (name.length() > 6) {
						ToastUtil.show(GoodsActivity.this, "????????????????????????6??????");
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
					ToastUtil.show(GoodsActivity.this, "???????????????");
				}
			}
		});
		newDialog.show();// ??????AlertDialog
	}

	/**
	 * ??????????????????
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
	 * ???????????????
	 */
	private void initData() {
		getData();
	}

	/**
	 * ????????????????????????????????? ??????????????????????????????
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
	 * ?????????????????????????????????
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
	 * ??????????????????
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
	 * ????????????????????????
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
	 * ?????????????????????
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
		// FIXME??????????????????
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
		holder.priceTextView.setText("???" + good.getShowGoodsprice());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String date = formatter
					.format(Long.parseLong(good.getAddtime()) * 1000);
			holder.putawayTimeTextView.setText(date + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.kucunNumTextView.setText(good.getNum() == -1 ? "??????" : good
				.getNum() + "");
		holder.saleNumTextView.setText(good.getBuynum() + "");
		holder.unitTextView2.setText(good.getUnit() == null ? "" : good
				.getUnit());
		holder.unitTextView1.setText(good.getUnit() == null ? "" : good
				.getUnit());
		if (good.getState() == 1) {// ????????????
			holder.statusTextView.setText("??????");
			// holder.statusTextView.setBackgroundResource(R.drawable.status_no_select);
			holder.statusTextView.setBackgroundResource(R.drawable.orangeshape);
			if (good.getIsgroom() == 2) {// ????????????
				// holder.recommendTextView.setBackgroundResource(R.drawable.status_no_select);
				holder.recommendTextView.setText("?????????");
				holder.recommendTextView
						.setBackgroundResource(R.drawable.orangeshape);
			} else {
				// holder.recommendTextView.setBackgroundResource(R.drawable.status_select);
				holder.recommendTextView.setText("?????????");
				holder.recommendTextView
						.setBackgroundResource(R.drawable.goods_gray_shape);
			}
		} else if (good.getState() == 2) {
			holder.statusTextView.setText("??????");
			// holder.statusTextView.setBackgroundResource(R.drawable.status_select);
			// holder.recommendTextView.setBackgroundResource(R.drawable.status_no_select);
			holder.statusTextView
					.setBackgroundResource(R.drawable.goods_gray_shape);
			holder.recommendTextView
					.setBackgroundResource(R.drawable.goods_gray_shape);
		}
		if (good.getIsgroom() == 2) {// ????????????
			holder.recommendTextView.setText("?????????");
		} else {
			holder.recommendTextView.setText("?????????");
		}
		holder.recommendTextView.setTag(good);
		holder.recommendTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				int isgroom = ((Good) v.getTag()).getIsgroom();
				System.out.println("-----isgroom----->" + isgroom);
				if (isgroom == 1) {
					if (((Good) v.getTag()).getState() == 2) {
						ToastUtil.show(GoodsActivity.this, "?????????????????????");
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
						ToastUtil.show(GoodsActivity.this, "?????????????????????");
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
	 * ???????????????????????????
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
	 * ??????????????????????????????
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
	 * ????????????UI
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
	 * ???????????????ViewHolder
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
	 * ???????????????
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
		// titleTextView.setText("??????");
		viewPager.setCurrentItem(0);
	}

	/**
	 * ???????????????
	 */
	private void select2() {
		CURRENT_OPT = NUM;
		CURRENT_OPT_STATUS = NUM_STATUS;
		LOAD_STATUS = NORMAL;
		linearLayout2.setBackgroundResource(R.drawable.select_bg);
		// titleTextView.setText("??????");
		// repertoryTextView.setTextColor(Color.WHITE);
		linearLayout1.setBackgroundResource(R.drawable.no_select_bg);
		// saleNumTextView.setTextColor(Color.parseColor("#424242"));
		linearLayout3.setBackgroundResource(R.drawable.no_select_bg);
		// putawayTimeTextView.setTextColor(Color.parseColor("#424242"));
		viewPager.setCurrentItem(1);
	}

	/**
	 * ???????????????
	 */
	private void select3() {
		CURRENT_OPT = GROUTIME;
		CURRENT_OPT_STATUS = GROUTIME_STATUS;
		LOAD_STATUS = NORMAL;
		// titleTextView.setText("????????????");
		linearLayout3.setBackgroundResource(R.drawable.select_bg);
		// putawayTimeTextView.setTextColor(Color.WHITE);
		linearLayout2.setBackgroundResource(R.drawable.no_select_bg);
		// repertoryTextView.setTextColor(Color.parseColor("#424242"));
		linearLayout1.setBackgroundResource(R.drawable.no_select_bg);
		// saleNumTextView.setTextColor(Color.parseColor("#424242"));
		viewPager.setCurrentItem(2);
	}

	/**
	 * ????????????????????????
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
	 * ViewPager ?????????
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
	 * ???????????????????????????
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
	 * ????????????
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
	 * ??????
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}

	/**
	 * ????????????
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
	 * ????????????
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
