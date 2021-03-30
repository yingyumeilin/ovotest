package com.example.oto01;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.listener.OrderListListener;
import com.example.oto01.listener.OrderListListener.DingDanListener;
import com.example.oto01.model.Login;
import com.example.oto01.model.Order;
import com.example.oto01.model.StorePay;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.OrderService;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.PullListViewUtils;
import com.example.oto01.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 订单列表界面
 * 
 * @author lqq
 * 
 */
@SuppressLint("ResourceAsColor")
public class OrdersActivity extends BaseActivity implements OnClickListener,
		DingDanListener {

	// 待接单
	private static final int WAIT_RECEIVE_ORDER = 1;
	// 待发货
	private static final int WAIT_SEND = 5;
	// 未付款
	private static final int WAIT_PAY = 2; // 变成了已发货
	// 已发货
	private static final int HAVE_SEND = -1; // 变成了 已完成。已关闭
	// 到店支付
	private static final int STORE_PAY = 102; // 变成了到店支付
	private static final int SHOP_CODE = 103;
	// 已关闭
	private static final int CLOSED = 104;
	// //售出记录
	// private static final int WORK_OFF=105;
	// 消费记录
	private static final int ORDER_NUM = 100;
	private static final int JIE_DAN = 101;
	private static final int REQUEST_WAIT_ORDER = 21;
	private static final int REQUEST_WAIT_SEND = 22;
	private static final int REQUEST_HAVE_SEND = 23;
	private static final int REQUEST_WAIT_PAY = 24;
	private static final int REQUEST_SHOP_PAY_DETAILS = 25;
	private static final int REQUEST_FINISH = 26;

	private int search_type = 0;
	private String keyword = "";
	private int paystate = 0;

	private int CURRENT_OPT = WAIT_RECEIVE_ORDER;
	private int current_page = 1;
	private int current_store_page = 0;
	private int total_page = 0;
	private TextView finishOrders, closedOrders;
	private TextView badgeView;
	// private BadgeView badgeView1;
	private TextView recentTextView, obligationTextView, overhangTextView,
			finishTextView, closedTextView;
	private TextView wait_receive_order, tv_wait_receive_order, tv_overhang,
			overhang_tv;
	private TextView tv_wait_pay_order, wait_pay_order, tv_obligation,
			obligation_tv;
	private LinearLayout iv_no;
	private RelativeLayout rl_on_the_way;
	private LinearLayout overhangOrders, obligationOrders,
			ll_wait_receive_order, ll_wait_pay_order, ll_on_the_way;
	private LinearLayout ll_listView1, ll_listView2, ll_listView3,
			ll_listView4, ll_listView5, ll_listView6;
	private PullToRefreshListView pullToRefreshListView1,
			pullToRefreshListView2, pullToRefreshListView3,
			pullToRefreshListView4, pullToRefreshListView5,
			pullToRefreshListView6;
	/**
	 * listView5 到店支付，listView6 会员卡
	 */
	private ListView listView1, listView2, listView3, listView4, listView5,
			listView6;
	/**
	 * 待接单的红线
	 */
	private static ImageView iv_wait_receive_red;
	/**
	 * 订单管理的adapter 待接单，待发货，已发货，已完成／已关闭
	 */
	private OrdersAdapter adapter1, adapter2, adapter3, adapter4;
	/**
	 * 到店支付adapter
	 */
	private StorePayAdapter storePayAdapter;
	/**
	 * 会员卡支付 售出记录adapter
	 */
	private MemberAdapter memberAdapter;
	/**
	 * 消费记录 adpater
	 */

	private Dialog proDialog;
	private GoodImgAdapter goodImgAdapter;
	private TextView tv_on_the_way;
	private String code1;
	private int shopsid = 1;
	private int waitnum = 0;
	private int havenum = 0;
	private boolean isFinished = true;
	private boolean isClosed = true;
	private boolean data = true;
	private String message;
	private ImageView red_dian;
	private ImageView search;
	private Intent it;
	private int jiedan = 0;
	private float startWidth = 0f;// 动画开始的宽度
	private float endWidth = 0f;// 动画结束时的宽度
	int imageOriginalHeight = 5;// 红线初始状态下的高度
	int imageOriginalWidth = 0;// 红线初始状态下的宽度

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == ORDER_NUM) {
				// 得到订单数量
				String res = (String) msg.obj;
				try {
					JSONObject jo;
					jo = new JSONObject(res);
					if (jo.optInt("res") == 0) {
						wait_receive_order.setText(jo
								.optInt("wait_receive_num") + "");
						tv_overhang.setText(jo.optInt("wait_send_num") + "");
						tv_wait_pay_order.setText(jo
								.optInt("complete_send_num") + "");
						tv_obligation.setText(jo.optInt("over_num") + "");

						if (jo.optInt("wait_receive_num") > 0) {
							red_dian.setVisibility(View.VISIBLE);
						} else {
							red_dian.setVisibility(View.INVISIBLE);
						}

					} else if (jo.optInt("res") == 1) {
						ToastUtil.show(getApplicationContext(), "获取订单数量失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			else if (msg.what == JIE_DAN) {
				// 接单功能
				try {
					Integer flag = (Integer) msg.obj;
					ToastUtil.show(getApplicationContext(), message);
					WAIT_RECEIVE_ORDER(false, true);
					initOrderNum();
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (msg.what == SHOP_CODE) {
				String res = (String) msg.obj;
				JSONObject jo;
				try {
					jo = new JSONObject(res);
					finish_onCLick();
					ToastUtil
							.show(getApplicationContext(), jo.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				getCurrentOrderList(msg);
			}

		};
	};

	/**
	 * 得到当前订单列表
	 * 
	 * @param msg
	 */
	private void getCurrentOrderList(Message msg) {
		if (!isClosed) {
			proDialog.dismiss();
			isClosed = true;
		}

		if (msg.what == STORE_PAY) {
			// 到店支付
			List<StorePay.Data> list = (List<StorePay.Data>) msg.obj;
			if (list != null && !list.isEmpty()) {
				if (list.size() == 0) {
					ToastUtil.show(getApplicationContext(), "没有数据");
					pullToRefreshListView5.onRefreshComplete();
					return;
				}
				int over = msg.arg2;
				if (over == 0) {
					ToastUtil.show(this, "已经到底部了");
					storePayAdapter.addAllDatas(list);
					storePayAdapter.notifyDataSetChanged();
					pullToRefreshListView5.onRefreshComplete();
					return;
				} else {
					storePayAdapter.addAllDatas(list);
					storePayAdapter.notifyDataSetChanged();
					pullToRefreshListView5.onRefreshComplete();
				}
			} else {
				ToastUtil.show(getApplicationContext(), "暂无数据");
				pullToRefreshListView5.onRefreshComplete();
			}

		} else if (msg.what == CLOSED) {
			// 会员卡支付订单
			List<StorePay.Data> list = (List<StorePay.Data>) msg.obj;
			if (list != null && !list.isEmpty()) {
				if (list.size() == 0) {
					ToastUtil.show(getApplicationContext(), "没有数据");
					pullToRefreshListView6.onRefreshComplete();
					return;
				}
				int over = msg.arg2;
				if (over == 0) {
					ToastUtil.show(this, "已经到底部了");
					memberAdapter.addAllDatas(list);
					memberAdapter.notifyDataSetChanged();
					pullToRefreshListView6.onRefreshComplete();
					return;
				} else {
					memberAdapter.addAllDatas(list);
					memberAdapter.notifyDataSetChanged();
					pullToRefreshListView6.onRefreshComplete();
				}
			} else {
				ToastUtil.show(getApplicationContext(), "暂无数据");
				pullToRefreshListView6.onRefreshComplete();
			}
		} else {
			List<Order> gds = (List<Order>) msg.obj;
			if (gds != null && !gds.isEmpty()) {

				if (gds.size() == 0) {
					ToastUtil.show(getApplicationContext(), "没有数据");
					pullToRefreshListView1.onRefreshComplete();
					pullToRefreshListView2.onRefreshComplete();
					pullToRefreshListView3.onRefreshComplete();
					pullToRefreshListView4.onRefreshComplete();
					return;
				}
				int over = msg.arg2;
				if (over == 0) {
					ToastUtil.show(this, "已经到底部了");
					pullToRefreshListView1.onRefreshComplete();
					pullToRefreshListView2.onRefreshComplete();
					pullToRefreshListView3.onRefreshComplete();
					pullToRefreshListView4.onRefreshComplete();
					return;
				} else {
					switch (msg.what) {
					case WAIT_RECEIVE_ORDER:
						// 近一月
						adapter1.addAllDatas(gds);
						pullToRefreshListView1.onRefreshComplete();
						// currentPullRefreshView = recentPullRefreshView;
						break;
					case WAIT_SEND:
						adapter2.addAllDatas(gds);
						adapter2.notifyDataSetChanged();
						pullToRefreshListView2.onRefreshComplete();
						// currentPullRefreshView = obligationPullRefreshView;
						break;
					case WAIT_PAY:
						adapter3.addAllDatas(gds);
						adapter3.notifyDataSetChanged();
						pullToRefreshListView3.onRefreshComplete();
						// currentPullRefreshView = overhangPullRefreshView;
						break;
					case HAVE_SEND:
						adapter4.addAllDatas(gds);
						adapter4.notifyDataSetChanged();
						pullToRefreshListView4.onRefreshComplete();
						break;
					// case CLOSED:
					// // 已撤销
					// adapter6.addAllDatas(gds);
					// adapter6.notifyDataSetChanged();
					// pullToRefreshListView6.onRefreshComplete();
					// // currentPullRefreshView = closedPullRefreshView;
					// break;
					}
				}

			} else {
				ToastUtil.show(getApplicationContext(), "暂无数据");
				iv_no.setVisibility(View.VISIBLE);
				ll_listView1.setVisibility(View.GONE);
				ll_listView2.setVisibility(View.GONE);
				ll_listView3.setVisibility(View.GONE);
				ll_listView4.setVisibility(View.GONE);
				// ll_listView5.setVisibility(View.GONE);
				// ll_listView6.setVisibility(View.GONE);
			}

			initOrderNum();

			switch (msg.what) {
			case HAVE_SEND:
				badgeView.setText("");
				if (havenum > 0) {
					badgeView.setText(havenum + "");
				}
				break;
			case WAIT_SEND:
				badgeView.setText("");
				if (waitnum > 0) {
					badgeView.setText(waitnum + "");
				}
				break;
			default:
				badgeView.setText("");
				break;
			}
		}

	}

	private void showError() {
		ToastUtil.show(this, "出错了");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		initViews(getLayoutInflater());
	}

	/**
	 * 选择已关闭
	 * 
	 * @param view
	 */
	public void closed_onCLick() {
		System.out.println("-----点击已关闭---->");
		isFinished = false;
		select3(isFinished);
		// CURRENT_OPT = FINISH;
		// otherListView.setAdapter(finishAdapter);
	}

	/**
	 * 选择已完成
	 * 
	 * @param view
	 */
	public void finish_onCLick() {
		System.out.println("-----点击已完成---->")  ;

		isFinished = true;
		select3(isFinished);
		// CURRENT_OPT = CLOSED;
		// otherListView.setAdapter(closedAdapter);
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
	 * 初始化数据
	 * 
	 * @param inflater
	 */
	@SuppressWarnings("deprecation")
	private void initViews(LayoutInflater inflater) {
		showDialog();
		LoginManager lm = LoginManager.getInstance(OrdersActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		CURRENT_OPT = WAIT_RECEIVE_ORDER;
		// 进行中
		rl_on_the_way = (RelativeLayout) findViewById(R.id.rl_on_the_way);
		// 待接单
		ll_wait_receive_order = (LinearLayout) findViewById(R.id.ll_wait_receive_order);
		// 待发货
		obligationOrders = (LinearLayout) findViewById(R.id.obligation);
		// 待付款
		ll_wait_pay_order = (LinearLayout) findViewById(R.id.ll_wait_pay_order);
		// 已发货
		overhangOrders = (LinearLayout) findViewById(R.id.overhang);
		// 进行中 引申出来的列表
		ll_on_the_way = (LinearLayout) findViewById(R.id.ll_on_the_way);
		iv_wait_receive_red = (ImageView) findViewById(R.id.iv_wait_receive_red);

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		iv_wait_receive_red.setLayoutParams(new LinearLayout.LayoutParams(
				(int) (width * 0.25), imageOriginalHeight));

		// ll_member_card_way = (LinearLayout)
		// findViewById(R.id.ll_member_card_way);
		// ll_work_off = (LinearLayout) findViewById(R.id.ll_work_off);
		// ll_xpense_tracker = (LinearLayout)
		// findViewById(R.id.ll_xpense_tracker);
		//
		// tv_work_off = (TextView) findViewById(R.id.tv_work_off);
		// tv_xpense_tracker = (TextView) findViewById(R.id.tv_xpense_tracker);
		iv_no = (LinearLayout) findViewById(R.id.iv_no);

		tv_on_the_way = (TextView) findViewById(R.id.tv_on_the_way);

		finishOrders = (TextView) findViewById(R.id.finish);
		closedOrders = (TextView) findViewById(R.id.closed);

		wait_receive_order = (TextView) findViewById(R.id.wait_receive_order);
		tv_wait_receive_order = (TextView) findViewById(R.id.tv_wait_receive_order);
		// 已发货
		tv_overhang = (TextView) findViewById(R.id.tv_overhang);
		overhang_tv = (TextView) findViewById(R.id.overhang_tv);

		tv_wait_pay_order = (TextView) findViewById(R.id.tv_wait_pay_order);
		wait_pay_order = (TextView) findViewById(R.id.wait_pay_order);
		search = (ImageView) findViewById(R.id.search);

		tv_obligation = (TextView) findViewById(R.id.tv_obligation);
		obligation_tv = (TextView) findViewById(R.id.obligation_tv);
		ll_listView1 = (LinearLayout) findViewById(R.id.ll_listView1);
		ll_listView2 = (LinearLayout) findViewById(R.id.ll_listView2);
		ll_listView3 = (LinearLayout) findViewById(R.id.ll_listView3);
		ll_listView4 = (LinearLayout) findViewById(R.id.ll_listView4);
		ll_listView5 = (LinearLayout) findViewById(R.id.ll_listView5);
		ll_listView6 = (LinearLayout) findViewById(R.id.ll_listView6);
		pullToRefreshListView1 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView1);
		pullToRefreshListView2 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView2);
		pullToRefreshListView3 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView3);
		pullToRefreshListView4 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView4);
		pullToRefreshListView5 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView5);
		pullToRefreshListView6 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView6);

		badgeView = (TextView) findViewById(R.id.badge_view);
		badgeView.setBackgroundColor(Color.parseColor("#2F5B90"));
		badgeView.setTextColor(Color.WHITE);

		red_dian = (ImageView) findViewById(R.id.red_dian);
		rl_on_the_way.setOnClickListener(this);
		ll_wait_receive_order.setOnClickListener(this);
		obligationOrders.setOnClickListener(this);
		ll_wait_pay_order.setOnClickListener(this);
		overhangOrders.setOnClickListener(this);
		finishOrders.setOnClickListener(this);
		closedOrders.setOnClickListener(this);
		search.setOnClickListener(this);
		// ll_work_off.setOnClickListener(this);
		// ll_xpense_tracker.setOnClickListener(this);

		tv_on_the_way.setTextColor(Color.parseColor("#ee5048"));
		finishOrders.setTextColor(Color.parseColor("#333333"));
		closedOrders.setTextColor(Color.parseColor("#333333"));
		red_dian.setVisibility(View.INVISIBLE);

		adapter1 = new OrdersAdapter();
		adapter2 = new OrdersAdapter();
		adapter3 = new OrdersAdapter();
		adapter4 = new OrdersAdapter();
		// 到店支付 adapter
		storePayAdapter = new StorePayAdapter();
		// 会员卡支付 adapter
		memberAdapter = new MemberAdapter();
		pullToRefreshListView1 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView1);
		pullToRefreshListView2 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView2);
		pullToRefreshListView3 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView3);
		pullToRefreshListView4 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView4);
		pullToRefreshListView5 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView5);
		pullToRefreshListView6 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView6);
		// pullToRefreshListView7 = PullListViewUtils
		// .setRefreshBothMode(pullToRefreshListView7);

		pullToRefreshListView1.setOnRefreshListener(new OrderListListener(
				OrderListListener.WAIT_RECEIVE_ORDER, this));
		pullToRefreshListView2.setOnRefreshListener(new OrderListListener(
				OrderListListener.WAIT_SEND, this));
		pullToRefreshListView3.setOnRefreshListener(new OrderListListener(
				OrderListListener.WAIT_PAY, this));
		pullToRefreshListView4.setOnRefreshListener(new OrderListListener(
				OrderListListener.HAVE_SEND, this));
		pullToRefreshListView5.setOnRefreshListener(new OrderListListener(
				OrderListListener.FINISH, this));
		pullToRefreshListView6.setOnRefreshListener(new OrderListListener(
				OrderListListener.CLOSED, this));
		// pullToRefreshListView7.setOnRefreshListener(new OrderListListener(
		// OrderListListener.XPENSE, this));

		listView1 = pullToRefreshListView1.getRefreshableView();
		listView2 = pullToRefreshListView2.getRefreshableView();
		listView3 = pullToRefreshListView3.getRefreshableView();
		listView4 = pullToRefreshListView4.getRefreshableView();
		listView5 = pullToRefreshListView5.getRefreshableView();
		listView6 = pullToRefreshListView6.getRefreshableView();
		// listView7 = pullToRefreshListView7.getRefreshableView();

		listView1.setAdapter(adapter1);
		listView2.setAdapter(adapter2);
		listView3.setAdapter(adapter3);
		listView4.setAdapter(adapter4);
		// 到店支付
		listView5.setAdapter(storePayAdapter);
		// 会员卡支付
		listView6.setAdapter(memberAdapter);

		ll_on_the_way.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化订单数量
	 */
	private void initOrderNum() {
		if (!NetConn.checkNetwork(OrdersActivity.this)) {
		} else {
			new OrderAsyc().execute();
		}
	}

	private class OrderAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(OrdersActivity.this);
			Message message = handler.obtainMessage(ORDER_NUM);
			// message.arg1 = CURRENT_OPT_STATUS;
			String res = orderService.getOrderNum(shopsid);
			try {
				message.obj = res;
				handler.sendMessage(message);

			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("super.onResume();");
		if (!NetConn.checkNetwork(OrdersActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			initOrderNum();
			if (jiedan == 0) {
				WAIT_RECEIVE_ORDER(false, true);
			} else if (jiedan == REQUEST_WAIT_SEND) {
				// jiedan = REQUEST_WAIT_SEND;
				// 待发货
				// WAIT_SEND(false, true);
			} else if (jiedan == REQUEST_HAVE_SEND) {
				// jiedan = REQUEST_HAVE_SEND;
				// 已发货
				// HAVE_SEND(false, true);
			} else if (jiedan == REQUEST_WAIT_PAY) {
				// jiedan = REQUEST_WAIT_PAY;
				// WAIT_PAY(false, true);
			} else if (jiedan == REQUEST_WAIT_ORDER) {

			}

			else {

			}

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
	}

	/**
	 * 获取数据
	 */
	private void getData(Boolean data1) {
		data = data1;
		if (!NetConn.checkNetwork(OrdersActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			if (isClosed) {
				proDialog.show();
				isClosed = false;
			}
			new MyAsync().execute();
		}
	}

	/**
	 * 到店支付 获取数据
	 */
	private void getStorePayData(Boolean data1) {
		data = data1;
		if (!NetConn.checkNetwork(OrdersActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			if (isClosed) {
				proDialog.show();
				isClosed = false;
			}
			new MyStorePayAsync().execute();
		}

	}

	/**
	 * 会员卡支付订单 获取数据
	 * 
	 * @author Administrator
	 * 
	 */
	private void getMemberPayData(Boolean data1) {
		data = data1;
		if (!NetConn.checkNetwork(OrdersActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			if (isClosed) {
				proDialog.show();
				isClosed = false;
			}
			new MyMemberPayAsync().execute();
		}

	}

	private class MyMemberPayAsync extends AsyncTask<Void, Void, Void> {
		private String res;

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(OrdersActivity.this);
			Message message = handler.obtainMessage(CURRENT_OPT);
			// message.arg1 = CURRENT_OPT_STATUS;
			if (data) {
				// 刷新
				message.arg2 = -2;
				res = orderService.getStorePayList(shopsid, 1, keyword);
			} else {
				// 加载
				res = orderService.getStorePayList(shopsid, current_store_page,
						keyword);

				if (current_store_page >= total_page) {
					message.arg2 = 0;
					current_store_page = total_page;
				} else if (current_store_page < total_page) {
					message.arg2 = -1;
				}
			}
			StorePay storePay = JsonUtils.fromJson(res, StorePay.class);
			total_page = Integer.parseInt(storePay.getTotal());
			message.obj = storePay.getData();
			handler.sendMessage(message);
			return null;
		}
	}

	private class MyStorePayAsync extends AsyncTask<Void, Void, Void> {
		private String res;

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(OrdersActivity.this);
			Message message = handler.obtainMessage(CURRENT_OPT);
			// message.arg1 = CURRENT_OPT_STATUS;
			if (data) {
				// 刷新
				message.arg2 = -2;
				res = orderService.getStorePayList(shopsid, 1, keyword);
			} else {
				// 加载
				res = orderService.getStorePayList(shopsid, current_store_page,
						keyword);

				if (current_store_page >= total_page) {
					message.arg2 = 0;
					current_store_page = total_page;
				} else if (current_store_page < total_page) {
					message.arg2 = -1;
				}
			}
			StorePay storePay = JsonUtils.fromJson(res, StorePay.class);
			total_page = Integer.parseInt(storePay.getTotal());
			message.obj = storePay.getData();
			handler.sendMessage(message);
			return null;
		}
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */

	private class MyAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(OrdersActivity.this);
			List<Order> orders = new ArrayList<Order>();
			Message message = handler.obtainMessage(CURRENT_OPT);
			// message.arg1 = CURRENT_OPT_STATUS;
			if (data) {
				message.arg2 = -2;
				orders = orderService.getOrdersList(shopsid, CURRENT_OPT, 1,
						keyword, paystate);
			} else {
				if (current_page >= total_page) {
					message.arg2 = 0;
					current_page = total_page;
				} else if (current_page < total_page) {
					message.arg2 = -1;
					current_page++;
				}
				orders = orderService.getOrdersList(shopsid, CURRENT_OPT,
						current_page, keyword, paystate);

			}
			total_page = orderService.total_page;
			current_page = orderService.p;
			waitnum = orderService.waitnum;
			havenum = orderService.havenum;
			message.obj = orders;
			handler.sendMessage(message);
			return null;
		}
	}

	/*
	 * public void four_onClick(View view){ select3(); }
	 */

	/**
	 * 单击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_on_the_way:
			// 进行中
			CURRENT_OPT = WAIT_RECEIVE_ORDER;
			iv_wait_receive_red.setVisibility(View.VISIBLE);
			endWidth = (float) v.getLeft();
			slideview(startWidth, endWidth);
			select0();

			startWidth = endWidth;
			break;
		case R.id.overhang:
			// 未发货
			CURRENT_OPT = WAIT_SEND;
			iv_wait_receive_red.setVisibility(View.VISIBLE);
			endWidth = (float) v.getLeft();
			slideview(startWidth, endWidth);
			select1();

			startWidth = endWidth;
			break;
		case R.id.obligation:
			// 已完成，已关闭的订单
			jiedan = 1;
			iv_wait_receive_red.setVisibility(View.VISIBLE);
			CURRENT_OPT = HAVE_SEND;
			endWidth = (float) v.getLeft();
			slideview(startWidth, endWidth);
			select2();

			startWidth = endWidth;
			break;
		case R.id.finish:
			// 到店支付
			jiedan = 1;

			iv_wait_receive_red.clearAnimation();
			iv_wait_receive_red.setVisibility(View.GONE);
			CURRENT_OPT = STORE_PAY;
			finish_onCLick();
			break;
		case R.id.closed:
			// 会员卡
			// 售出记录
			jiedan = 1;
			iv_wait_receive_red.setVisibility(View.GONE);
			CURRENT_OPT = CLOSED;
			closed_onCLick();
			break;
		case R.id.ll_wait_receive_order:
			// 待接单
			CURRENT_OPT = WAIT_RECEIVE_ORDER;
			endWidth = (float) v.getLeft();
			iv_wait_receive_red.setVisibility(View.VISIBLE);
			slideview(startWidth, endWidth);
			wait_receive();

			startWidth = endWidth;
			break;
		case R.id.ll_wait_pay_order:
			// 待付款 变成了现在的 已发货
			CURRENT_OPT = WAIT_PAY;
			jiedan = 1;
			endWidth = (float) v.getLeft();
			iv_wait_receive_red.setVisibility(View.VISIBLE);
			slideview(startWidth, endWidth);
			wait_pay();

			startWidth = endWidth;
			break;
		// case R.id.ll_work_off:
		// // 售出记录
		// CURRENT_OPT = CLOSED;
		// closed_onCLick();
		// break;
		// case R.id.ll_xpense_tracker:
		// // 消费记录
		// CURRENT_OPT = XPENSE_TRACKER;
		// xpense_tracker();
		// break;
		case R.id.search:
			// 查询订单
			Intent intent = new Intent();
			if (CURRENT_OPT == 102) {
				intent.putExtra("type", "shop");
			} else {
				intent.putExtra("type", "0");
			}

			if (CURRENT_OPT == WAIT_RECEIVE_ORDER) {
				// 待接单
				intent.setClass(getApplicationContext(),
						SearchOrderActivity.class);
				startActivityForResult(intent, REQUEST_WAIT_ORDER);
			} else if (CURRENT_OPT == WAIT_SEND) {
				// 未发货
				intent.setClass(getApplicationContext(),
						SearchOrderActivity.class);
				startActivityForResult(intent, REQUEST_WAIT_SEND);
			} else if (CURRENT_OPT == WAIT_PAY) {
				// 已发货
				intent.setClass(getApplicationContext(),
						SearchOrderActivity.class);
				startActivityForResult(intent, REQUEST_HAVE_SEND);
			} else if (CURRENT_OPT == HAVE_SEND) {
				// 已完成
				intent.setClass(getApplicationContext(),
						SearchOrderActivity.class);
				startActivityForResult(intent, REQUEST_FINISH);
			} else if (CURRENT_OPT == STORE_PAY) {
				// 到店支付
				intent.setClass(getApplicationContext(),
						SearchOrderActivity.class);
				startActivityForResult(intent, REQUEST_SHOP_PAY_DETAILS);
			}

			break;
		}
	}

	private void wait_pay() {
		paystate = 1;
		tv_wait_receive_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		wait_receive_order.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_overhang.setTextColor(getResources().getColor(
				R.color.content_color_black));
		overhang_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_wait_pay_order.setTextColor(getResources().getColor(
				R.color.text_content_red));
		wait_pay_order.setTextColor(getResources().getColor(
				R.color.text_content_red));

		tv_obligation.setTextColor(getResources().getColor(
				R.color.content_color_black));
		obligation_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));
		iv_no.setVisibility(View.GONE);
		ll_listView1.setVisibility(View.GONE);
		ll_listView2.setVisibility(View.GONE);
		ll_listView3.setVisibility(View.VISIBLE);
		ll_listView4.setVisibility(View.GONE);
		ll_listView5.setVisibility(View.GONE);
		ll_listView6.setVisibility(View.GONE);
		WAIT_PAY(false, true);
	}

	/**
	 * 待接单 功能:
	 * 
	 * @author: chenym
	 * @date:2015-9-15上午11:22:32
	 */
	@SuppressLint("ResourceAsColor")
	private void wait_receive() {
		// ll_wait_receive_order.setBackgroundResource(R.drawable.select_bg);
		tv_wait_receive_order.setTextColor(getResources().getColor(
				R.color.text_content_red));
		wait_receive_order.setTextColor(getResources().getColor(
				R.color.text_content_red));

		tv_overhang.setTextColor(getResources().getColor(
				R.color.content_color_black));
		overhang_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_obligation.setTextColor(getResources().getColor(
				R.color.content_color_black));
		obligation_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));
		iv_no.setVisibility(View.GONE);
		ll_listView1.setVisibility(View.VISIBLE);
		ll_listView2.setVisibility(View.GONE);
		ll_listView3.setVisibility(View.GONE);
		ll_listView4.setVisibility(View.GONE);
		ll_listView5.setVisibility(View.GONE);
		ll_listView6.setVisibility(View.GONE);
		WAIT_RECEIVE_ORDER(false, true);
	}

	/**
	 * 到店支付/会员卡
	 * 
	 * @param isFinished
	 */
	private void select3(boolean isFinished) {
		iv_wait_receive_red.setVisibility(View.GONE);
		if (isFinished) {
			// 到店支付
			CURRENT_OPT = STORE_PAY;
			tv_on_the_way.setTextColor(Color.parseColor("#333333"));
			finishOrders.setTextColor(Color.parseColor("#ee5048"));
			closedOrders.setTextColor(Color.parseColor("#333333"));
			iv_no.setVisibility(View.GONE);
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.VISIBLE);
			ll_listView6.setVisibility(View.GONE);
			listView5.setAdapter(storePayAdapter);
			finishOrders.setBackgroundResource(R.drawable.select_bg);
			closedOrders.setBackgroundResource(R.drawable.no_select_bg);
			FINISH(false, true);
		} else {
			// 会员卡支付
			// 售出记录
			tv_on_the_way.setTextColor(Color.parseColor("#333333"));
			finishOrders.setTextColor(Color.parseColor("#333333"));
			closedOrders.setTextColor(Color.parseColor("#ee5048"));
			CURRENT_OPT = CLOSED;
			iv_no.setVisibility(View.GONE);
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.VISIBLE);
			closedOrders.setBackgroundResource(R.drawable.select_bg);
			finishOrders.setBackgroundResource(R.drawable.no_select_bg);
			listView6.setAdapter(memberAdapter);
			CLOSED(false, true);
		}

		// overhangOrders.setBackgroundResource(R.drawable.no_select_bg);
		rl_on_the_way.setBackgroundResource(R.drawable.no_select_bg);
		// obligationOrders.setBackgroundResource(R.drawable.no_select_bg);
		ll_on_the_way.setVisibility(View.GONE);

	}

	/**
	 * 选择第二项
	 */
	private void select1() {
		// 未发货
		tv_wait_receive_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		wait_receive_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		tv_overhang.setTextColor(getResources().getColor(
				R.color.text_content_red));

		overhang_tv.setTextColor(getResources().getColor(
				R.color.text_content_red));
		tv_wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_obligation.setTextColor(getResources().getColor(
				R.color.content_color_black));
		obligation_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));
		iv_no.setVisibility(View.GONE);
		ll_listView1.setVisibility(View.GONE);
		ll_listView2.setVisibility(View.VISIBLE);
		ll_listView3.setVisibility(View.GONE);
		ll_listView4.setVisibility(View.GONE);
		ll_listView5.setVisibility(View.GONE);
		ll_listView6.setVisibility(View.GONE);
		WAIT_SEND(false, true);
	}

	/**
	 * 选择第三项
	 */
	private void select2() {
		// 已发货
		tv_wait_receive_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		wait_receive_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		tv_overhang.setTextColor(getResources().getColor(
				R.color.content_color_black));
		overhang_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_obligation.setTextColor(getResources().getColor(
				R.color.text_content_red));
		obligation_tv.setTextColor(getResources().getColor(
				R.color.text_content_red));
		iv_no.setVisibility(View.GONE);
		ll_listView1.setVisibility(View.GONE);
		ll_listView2.setVisibility(View.GONE);
		ll_listView3.setVisibility(View.GONE);
		ll_listView4.setVisibility(View.VISIBLE);
		ll_listView5.setVisibility(View.GONE);
		ll_listView6.setVisibility(View.GONE);
		HAVE_SEND(false, true);
	}

	/**
	 * 选择第一项，进行中的订单
	 */
	private void select0() {
		// titleTextView.setText("近一月");
		tv_on_the_way.setTextColor(Color.parseColor("#ee5048"));
		finishOrders.setTextColor(Color.parseColor("#626a7d"));
		closedOrders.setTextColor(Color.parseColor("#626a7d"));
		rl_on_the_way.setBackgroundResource(R.drawable.order_jinxing);
		// obligationOrders.setBackgroundResource(R.drawable.no_select_bg);
		// overhangOrders.setBackgroundResource(R.drawable.no_select_bg);
		// TextView textView=(TextView)otherOrders.getSelectedView();
		finishOrders.setBackgroundResource(R.drawable.no_select_bg);// 到店支付
		// closedOrders.setBackgroundResource(R.drawable.no_select_bg);
		ll_on_the_way.setVisibility(View.VISIBLE);
		tv_wait_receive_order.setTextColor(getResources().getColor(
				R.color.text_content_red));
		wait_receive_order.setTextColor(getResources().getColor(
				R.color.text_content_red));

		// overhangOrders.setBackgroundResource(R.drawable.no_select_bg);
		tv_overhang.setTextColor(getResources().getColor(
				R.color.content_color_black));
		overhang_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));
		wait_pay_order.setTextColor(getResources().getColor(
				R.color.content_color_black));

		tv_obligation.setTextColor(getResources().getColor(
				R.color.content_color_black));
		obligation_tv.setTextColor(getResources().getColor(
				R.color.content_color_black));
		iv_no.setVisibility(View.GONE);
		ll_listView1.setVisibility(View.VISIBLE);
		ll_listView2.setVisibility(View.GONE);
		ll_listView3.setVisibility(View.GONE);
		ll_listView4.setVisibility(View.GONE);
		ll_listView5.setVisibility(View.GONE);
		ll_listView6.setVisibility(View.GONE);
		WAIT_RECEIVE_ORDER(false, true);
	}

	/**
	 * 订单列表的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class OrdersAdapter extends BaseAdapter {
		private List<Order> orders = new ArrayList<Order>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<Order> list) {
			orders.addAll(list);
			notifyDataSetChanged();
		}

		// /**
		// * 移除某个数据
		// * @param position
		// */
		// public void removeDatas(int position){
		// datas.remove(position);
		// notifyDataSetChanged();
		// }
		public List<Order> getDatas() {
			return orders;
		}

		/**
		 * 移除某个数据
		 */
		public void removeAllDatas() {
			orders.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orders.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			// return datas.get(arg0);
			return orders.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(OrdersActivity.this).inflate(
						R.layout.order_list_item, parent, false);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, orders.get(position));
			holder.status.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							confirmOrder(orders.get(position).getStatus(),
									orders.get(position).getId());
						}
					}).start();

				}
			});

			cv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					jumpToDetails(orders.get(position));
				}
			});
			return cv;
		}

	}

	/**
	 * 根据类型提交订单
	 * 
	 * @param type
	 */

	private void confirmOrder(int status, int orderid) {
		OrderService orderService = new OrderService(OrdersActivity.this);
		int flag = -3;
		String res;
		res = orderService.updateOrderStatejiedan(orderid, 5);
		JSONObject jo = null;
		try {
			jo = new JSONObject(res);
			flag = jo.optInt("res");
			try {
				message = jo.getString("msg");
				System.out.println("------------flag-------------->" + flag);
				System.out.println("------------message-------------->"
						+ message);
				Message message = handler.obtainMessage(JIE_DAN);
				message.obj = flag;
				handler.sendMessage(message);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找视图控件
	 * 
	 * @param holder
	 * @param cv
	 */
	private void findViews(ViewHolder holder, View cv) {
		holder.consiginee = (TextView) cv.findViewById(R.id.order_consiginee);
		holder.disnum = (TextView) cv.findViewById(R.id.disnum);
		holder.goodsnum = (TextView) cv.findViewById(R.id.goodsnum);
		holder.disnum_title = (TextView) cv.findViewById(R.id.title_disnum);
		holder.goodsnum_title = (TextView) cv.findViewById(R.id.title_goodsnum);
		holder.line = (ImageView) cv.findViewById(R.id.line1);
		// holder.phone = (TextView) cv.findViewById(R.id.order_phone);
		holder.location = (TextView) cv.findViewById(R.id.order_location);
		holder.time = (TextView) cv.findViewById(R.id.order_time);
		holder.status = (TextView) cv.findViewById(R.id.order_status);
		holder.goodTitle = (TextView) cv.findViewById(R.id.good_title);
		holder.orderPic = (LinearLayout) cv.findViewById(R.id.order_pic);
		holder.ll_two = (LinearLayout) cv.findViewById(R.id.ll_two);
		holder.iv_mine1 = (ImageView) cv.findViewById(R.id.iv_mine1);
		holder.iv_mine3 = (ImageView) cv.findViewById(R.id.iv_mine3);
		holder.iv_mine2 = (ImageView) cv.findViewById(R.id.iv_mine2);
		holder.iv_mine4 = (ImageView) cv.findViewById(R.id.iv_mine4);
		holder.order_status1 = (TextView) cv.findViewById(R.id.order_status1);
		holder.rl_quan = (RelativeLayout) cv.findViewById(R.id.rl_quan);
	}

	/**
	 * 填充数据Item
	 * 
	 * @param holder
	 * @param order
	 */
	private void refreshItem(ViewHolder holder, Order order) {
		Log.d("TAG", "order.getStatus() : " + order.getStatus());
		Log.d("TAG", "order.getPaystate() : " + order.getPaystate());
		Log.d("TAG", "order.getPayment() : " + order.getPayment());
		switch (order.getState()) {
		case 1:
			// holder.status.setImageResource(R.drawable.dai_jie_dan);
			holder.status.setText("接单");
			holder.status.setBackgroundResource(R.drawable.dai_jie_dan);
		case 4:
			if (order.getPaystate() == 1 && order.getPayment() == 2) {
				// 未付款，电子支付
				holder.order_status1.setBackgroundResource(R.drawable.wait_pay);
			} else {
				holder.status.setText("接单");
				holder.status.setBackgroundResource(R.drawable.dai_jie_dan);
			}
			break;
		case 2:
			holder.order_status1.setText("已发货");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_yifa);
			break;
		case 3:
			holder.order_status1.setText("已完成");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_yifa);
			break;
		case -1:
			holder.order_status1.setText("已关闭");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_closed);
			break;
		case 5:
			// 待发货
			holder.order_status1.setText("待发货");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_yifa);
			break;
		}

		if (order.getIs_dis() == 3 && order.getState() != 1
				&& order.getState() != 4 && order.getState() != 5) {// 已找管家
			holder.disnum.setText(order.getDisnum() + "");
			holder.goodsnum.setText(order.getGoodsnum() + "");
			holder.disnum.setVisibility(View.VISIBLE);
			holder.goodsnum.setVisibility(View.VISIBLE);
			holder.line.setVisibility(View.VISIBLE);
			holder.goodsnum_title.setVisibility(View.VISIBLE);
			holder.disnum_title.setVisibility(View.VISIBLE);
		} else {
			holder.disnum.setVisibility(View.GONE);
			holder.goodsnum.setVisibility(View.GONE);
			holder.disnum_title.setVisibility(View.GONE);
			holder.goodsnum_title.setVisibility(View.GONE);
			holder.line.setVisibility(View.GONE);
		}
		// 1、普通(优惠券)订单 2、礼品券到店订单
		if (order.getOrder_type().equals("1")) {
			holder.rl_quan.setVisibility(View.GONE);
		} else {
			holder.rl_quan.setVisibility(View.VISIBLE);
		}
		holder.consiginee.setText(order.getLinkman());
		holder.location.setText(order.getAddress());
		// holder.price.setText("￥" + order.getShowTotal());
		holder.time.setText(DateUtil.getFormatedDate_1(order.getAddtime()));
		holder.goodTitle.setText(order.getOrdernum());
		// holder.paymentType.setText(order.getPayment() == 1 ? "货到付款" : "电子支付"
		// + (order.getPaystate() == 1 ? "(未付款)" : "(已付款)"));
		if (order.getOrder_type().equals("1")) {
			int size = order.getOrderGood().size();
			// ImgLoad loader = ImgLoad.getInstance();
			if (size > 4) {
				size = 4;
			}
			if (size == 1) {
				String imgPath = order.getOrderGood().get(0).getPicpath();
				if (imgPath != null) {
					ImageLoadTool.disPlay(imgPath, holder.iv_mine1,
							R.drawable.default_image);
					// holder.iv_mine1.setTag(imgPath);
					// loader.addTask(imgPath, holder.iv_mine1);
					// loader.doTask();
				}
				holder.orderPic.setVisibility(View.VISIBLE);
				holder.iv_mine1.setVisibility(View.VISIBLE);
				holder.ll_two.setVisibility(View.GONE);
				holder.iv_mine2.setVisibility(View.GONE);
				holder.iv_mine3.setVisibility(View.GONE);
				holder.iv_mine4.setVisibility(View.GONE);

				// holder.mlv.setVisibility(View.GONE);
			} else if (size == 2) {
				String imgPath = order.getOrderGood().get(0).getPicpath();
				String imgPath1 = order.getOrderGood().get(1).getPicpath();
				if (imgPath != null && imgPath1 != null) {
					ImageLoadTool.disPlay(imgPath, holder.iv_mine1,
							R.drawable.default_image);
					ImageLoadTool.disPlay(imgPath1, holder.iv_mine2,
							R.drawable.default_image);
					// holder.iv_mine1.setTag(imgPath);
					// holder.iv_mine2.setTag(imgPath1);
					// loader.addTask(imgPath, holder.iv_mine1);
					// loader.addTask(imgPath1, holder.iv_mine2);
					// loader.doTask();
				}
				holder.orderPic.setVisibility(View.VISIBLE);
				holder.iv_mine1.setVisibility(View.VISIBLE);
				holder.ll_two.setVisibility(View.VISIBLE);
				holder.iv_mine2.setVisibility(View.VISIBLE);
				holder.iv_mine3.setVisibility(View.GONE);
				holder.iv_mine4.setVisibility(View.GONE);
			} else if (size == 3) {
				String imgPath = order.getOrderGood().get(0).getPicpath();
				String imgPath1 = order.getOrderGood().get(1).getPicpath();
				String imgPath2 = order.getOrderGood().get(2).getPicpath();
				if (imgPath != null && imgPath1 != null && imgPath2 != null) {
					ImageLoadTool.disPlay(imgPath, holder.iv_mine1,
							R.drawable.default_image);
					ImageLoadTool.disPlay(imgPath1, holder.iv_mine2,
							R.drawable.default_image);
					ImageLoadTool.disPlay(imgPath2, holder.iv_mine3,
							R.drawable.default_image);
					// holder.iv_mine1.setTag(imgPath);
					// holder.iv_mine2.setTag(imgPath1);
					// holder.iv_mine3.setTag(imgPath2);
					// loader.addTask(imgPath, holder.iv_mine1);
					// loader.addTask(imgPath1, holder.iv_mine2);
					// loader.addTask(imgPath2, holder.iv_mine3);
					// loader.doTask();
				}
				holder.orderPic.setVisibility(View.VISIBLE);
				holder.iv_mine1.setVisibility(View.VISIBLE);
				holder.ll_two.setVisibility(View.VISIBLE);
				holder.iv_mine2.setVisibility(View.VISIBLE);
				holder.iv_mine3.setVisibility(View.VISIBLE);
				holder.iv_mine4.setVisibility(View.GONE);
			} else if (size == 4) {
				String imgPath = order.getOrderGood().get(0).getPicpath();
				String imgPath1 = order.getOrderGood().get(1).getPicpath();
				String imgPath2 = order.getOrderGood().get(2).getPicpath();
				String imgPath3 = order.getOrderGood().get(3).getPicpath();
				if (imgPath != null && imgPath1 != null && imgPath2 != null
						&& imgPath3 != null) {
					ImageLoadTool.disPlay(imgPath, holder.iv_mine1,
							R.drawable.default_image);
					ImageLoadTool.disPlay(imgPath1, holder.iv_mine2,
							R.drawable.default_image);
					ImageLoadTool.disPlay(imgPath2, holder.iv_mine3,
							R.drawable.default_image);
					ImageLoadTool.disPlay(imgPath3, holder.iv_mine4,
							R.drawable.default_image);
					// holder.iv_mine1.setTag(imgPath);
					// holder.iv_mine2.setTag(imgPath1);
					// holder.iv_mine3.setTag(imgPath2);
					// holder.iv_mine4.setTag(imgPath3);
					// loader.addTask(imgPath, holder.iv_mine1);
					// loader.addTask(imgPath1, holder.iv_mine2);
					// loader.addTask(imgPath2, holder.iv_mine3);
					// loader.addTask(imgPath3, holder.iv_mine4);
					// loader.doTask();
				}
				holder.orderPic.setVisibility(View.VISIBLE);
				holder.iv_mine1.setVisibility(View.VISIBLE);
				holder.ll_two.setVisibility(View.VISIBLE);
				holder.iv_mine2.setVisibility(View.VISIBLE);
				holder.iv_mine3.setVisibility(View.VISIBLE);
				holder.iv_mine4.setVisibility(View.VISIBLE);
			}
		} else {
			String imgPath = order.getGift_goods_pic();
			if (imgPath != null) {
				ImageLoadTool.disPlay(imgPath, holder.iv_mine1,
						R.drawable.default_image);
			}
			holder.orderPic.setVisibility(View.VISIBLE);
			holder.iv_mine1.setVisibility(View.VISIBLE);
			holder.ll_two.setVisibility(View.GONE);
			holder.iv_mine2.setVisibility(View.GONE);
			holder.iv_mine3.setVisibility(View.GONE);
			holder.iv_mine4.setVisibility(View.GONE);
		}
	}

	private class GoodImgAdapter extends ArrayAdapter<String> {
		public GoodImgAdapter(Context cx, int resId, List<String> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolderImage holder = null;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(
						R.layout.horizontal_list_item, parent, false);
				holder = new ViewHolderImage();
				holder.img = (ImageView) cv.findViewById(R.id.img);
				cv.setTag(holder);
			} else {
				holder = (ViewHolderImage) cv.getTag();
			}
			refreshItem(ImgLoad.getInstance(), getItem(position), holder);

			return cv;
		}
	}

	/**
	 * 刷新Item
	 * 
	 * @param loader
	 * @param img
	 * @param holder
	 */
	private void refreshItem(ImgLoad loader, String img, ViewHolderImage holder) {
		if (img != null) {
			holder.img.setTag(img);
			loader.addTask(img, holder.img);
			loader.doTask();
		}
	}

	private class ViewHolderImage {
		ImageView img;
	}

	private class ViewHolder {
		TextView consiginee;
		TextView disnum;
		TextView goodsnum;
		TextView disnum_title;
		TextView goodsnum_title;
		ImageView line;
		// TextView phone;
		TextView location;
		TextView price;
		TextView time;
		// TextView status;
		TextView status;
		TextView goodTitle;
		LinearLayout orderPic;
		LinearLayout ll_two;
		TextView paymentType;
		ImageView iv_mine1;
		ImageView iv_mine3;
		ImageView iv_mine2;
		ImageView iv_mine4;
		TextView order_status1;
		RelativeLayout rl_quan;

		// 到店支付
		TextView tv_code;
		TextView tv_already_verified;
		ImageView iv_store_pay_not_verified;
		TextView order_num;
		TextView tv_order_time;
		TextView order_money;
		TextView title_address;
		TextView tv1;

	}

	/**
	 * 跳转到订单详情
	 * 
	 * @param or
	 */
	private void jumpToDetails(Order or) {
		if (or != null) {
			it = new Intent(this, OrderDetailsActivity.class);
			it.putExtra("order_id", or.getId());
			it.putExtra("uid", or.getUid());
			it.putExtra("ordertype", or.getOrder_type());
			System.out
					.println("-------------or.getState()----------------------->"
							+ or.getState());
			System.out
					.println("-------------or.getPaystate()----------------------->"
							+ or.getPaystate());
			System.out
					.println("-------------getPayment()----------------------->"
							+ or.getPayment());
			if (or.getState() == 1) {
				// 待接单状态
				// jiedan = 0;
				startActivityForResult(it, REQUEST_WAIT_ORDER);
			} else if (or.getState() == 4) {
				if (or.getPaystate() == 1 && or.getPayment() == 2) {
					startActivityForResult(it, REQUEST_WAIT_PAY);
				} else {
					startActivityForResult(it, REQUEST_WAIT_ORDER);
				}

			} else if (or.getState() == 5) {
				// 待发货状态
				startActivityForResult(it, REQUEST_WAIT_SEND);
			} else if (or.getState() == 2) {
				// 已发货状态
				startActivityForResult(it, REQUEST_HAVE_SEND);
			} else {
				jiedan = 1;
				startActivity(it);
			}

		} else {
			System.out.println("------无法跳转------>");
		}
	}

	@Override
	public void WAIT_RECEIVE_ORDER(boolean isRefresh, boolean isHeadRefresh) {
		CURRENT_OPT = WAIT_RECEIVE_ORDER;
		if (isHeadRefresh) {// 刷新
			getData(true);
			adapter1.removeAllDatas();
		} else {// 加载
			getData(false);
		}

	}

	@Override
	public void WAIT_SEND(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = WAIT_SEND;
		if (isHeadRefresh) {// 刷新
			getData(true);
			adapter2.removeAllDatas();
		} else {// 加载
			getData(false);
		}
	}

	@Override
	public void WAIT_PAY(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = WAIT_PAY;
		if (isHeadRefresh) {// 刷新
			getData(true);
			adapter3.removeAllDatas();
		} else {// 加载
			getData(false);
		}
	}

	@Override
	public void HAVE_SEND(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = HAVE_SEND;
		if (isHeadRefresh) {// 刷新
			getData(true);
			adapter4.removeAllDatas();
		} else {// 加载
			getData(false);
		}
	}

	/**
	 * 到店支付订单
	 */
	@Override
	public void FINISH(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = STORE_PAY;
		if (isHeadRefresh) {// 刷新
			current_store_page = 0;
			getStorePayData(true);
			storePayAdapter.removeAllDatas();
		} else {// 加载
			getStorePayData(false);
		}
		current_store_page++;
	}

	@Override
	public void CLOSED(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = CLOSED;
		if (isHeadRefresh) {// 刷新
			getMemberPayData(true);
			memberAdapter.removeAllDatas();
		} else {// 加载
			getMemberPayData(false);
		}
	}

	// @Override
	// public void XPENSE(boolean isRefresh, boolean isHeadRefresh) {
	// CURRENT_OPT = XPENSE_TRACKER;
	// if (isHeadRefresh) {// 刷新
	// getMemberPayData(true);
	// xpenseAdater.removeAllDatas();
	// } else {// 加载
	// getMemberPayData(false);
	// }
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			slideview(startWidth, endWidth);
			if (requestCode == REQUEST_WAIT_ORDER) {
				jiedan = REQUEST_WAIT_ORDER;
				// 待接单
				WAIT_RECEIVE_ORDER(false, true);
			} else if (requestCode == REQUEST_WAIT_SEND) {
				jiedan = REQUEST_WAIT_SEND;
				// 待发货
				WAIT_SEND(false, true);
			} else if (requestCode == REQUEST_HAVE_SEND) {
				jiedan = REQUEST_HAVE_SEND;
				// 已发货
				WAIT_PAY(false, true);
				// HAVE_SEND(false, true);
			} else if (requestCode == REQUEST_WAIT_PAY) {
				jiedan = REQUEST_WAIT_PAY;
				// 待付款
				WAIT_PAY(false, true);
			} else if (requestCode == REQUEST_SHOP_PAY_DETAILS) {
				// 到店支付订单
				iv_wait_receive_red.clearAnimation();
				iv_wait_receive_red.setVisibility(View.GONE);
				FINISH(false, true);
			} else if (requestCode == REQUEST_FINISH) {
				// 已完成订单
				HAVE_SEND(false, true);
			}

		}
	}

	/**
	 * 到店支付列表的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class StorePayAdapter extends BaseAdapter {
		private List<StorePay.Data> storePay = new ArrayList<StorePay.Data>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<StorePay.Data> list) {
			storePay.addAll(list);
			notifyDataSetChanged();
		}

		// /**
		// * 移除某个数据
		// * @param position
		// */
		// public void removeDatas(int position){
		// datas.remove(position);
		// notifyDataSetChanged();
		// }
		public List<StorePay.Data> getDatas() {
			return storePay;
		}

		/**
		 * 移除某个数据
		 */
		public void removeAllDatas() {
			storePay.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return storePay.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			// return datas.get(arg0);
			return storePay.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@ViewInject(R.id.tv_code)
		private TextView tv_code;
		@ViewInject(R.id.tv_already_verified)
		private TextView tv_already_verified;
		@ViewInject(R.id.iv_store_pay_not_verified)
		private ImageView iv_store_pay_not_verified;
		@ViewInject(R.id.order_num)
		private TextView order_num;
		@ViewInject(R.id.tv_order_time)
		private TextView tv_order_time;
		@ViewInject(R.id.order_money)
		private TextView order_money;
		@ViewInject(R.id.rl_quan)
		private RelativeLayout rl_quan;
		@ViewInject(R.id.title_address)
		private TextView title_address;
		@ViewInject(R.id.tv1)
		private TextView tv1;

		@Override
		public View getView(final int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(OrdersActivity.this).inflate(
						R.layout.store_shop_item, parent, false);
				holder = new ViewHolder();
				ViewUtils.inject(this, cv);
				holder.tv_code = tv_code;
				holder.tv_already_verified = tv_already_verified;
				holder.iv_store_pay_not_verified = iv_store_pay_not_verified;
				holder.order_num = order_num;
				holder.tv_order_time = tv_order_time;
				holder.order_money = order_money;
				holder.rl_quan = rl_quan;
				holder.title_address = title_address;
				holder.tv1 = tv1;
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			final StorePay.Data list = storePay.get(position);
			if (list.getOrder_type().equals("1")) {
				// 普通优惠券订单
				holder.tv1.setText("验证码");
				holder.rl_quan.setVisibility(View.GONE);
				holder.title_address.setVisibility(View.VISIBLE);
				holder.order_money.setVisibility(View.VISIBLE);
				holder.order_money.setText(list.getReceive_total_price() + "元");

				// 是否确认 1：已确认 2：未确认
				if (list.getIs_confirm().equals("1")) {
					holder.iv_store_pay_not_verified.setVisibility(View.GONE);
					holder.tv_already_verified.setVisibility(View.VISIBLE);
				} else if (list.getIs_confirm().equals("2")) {
					holder.iv_store_pay_not_verified
							.setVisibility(View.VISIBLE);
					holder.tv_already_verified.setVisibility(View.GONE);
				}
			} else {
				// 礼品券订单
				holder.tv1.setText("礼品券码");
				holder.rl_quan.setVisibility(View.VISIBLE);
				holder.title_address.setVisibility(View.GONE);
				holder.order_money.setVisibility(View.GONE);
				holder.iv_store_pay_not_verified.setVisibility(View.GONE);
				holder.tv_already_verified.setVisibility(View.GONE);
			}
			holder.tv_code.setText(list.getAuthcode());
			holder.order_num.setText(list.getOrdernum());
			holder.tv_order_time.setText(DateUtil.getFormatedDate_1(list
					.getAddtime()));

			holder.iv_store_pay_not_verified
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							yanzheng(list.getAuthcode());
						}
					});

			cv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							ShopPayDetailsActivity.class);
					if (list.getOrder_type().equals("1")) {
						intent.putExtra("type", "order_details");
					} else {
						intent.putExtra("type", "quan");
					}
					intent.putExtra("id", list.getId());
					startActivityForResult(intent, REQUEST_SHOP_PAY_DETAILS);
				}
			});
			return cv;
		}
	}

	protected void yanzheng(String code) {
		code1 = code;
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				OrdersActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否设为已验证？");
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!NetConn.checkNetwork(OrdersActivity.this)) {
						} else {
							new CodeAsyc().execute();
						}
					}
				});
		dialog.setPositiveButton("取消", null);
		dialog.show();
	}

	private class CodeAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(OrdersActivity.this);
			Message message = handler.obtainMessage(SHOP_CODE);
			String res = orderService.getShopCode(shopsid, code1);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	/**
	 * 会员卡支付列表的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class MemberAdapter extends BaseAdapter {
		private List<StorePay.Data> storePay = new ArrayList<StorePay.Data>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<StorePay.Data> list) {
			storePay.addAll(list);
			notifyDataSetChanged();
		}

		// /**
		// * 移除某个数据
		// * @param position
		// */
		// public void removeDatas(int position){
		// datas.remove(position);
		// notifyDataSetChanged();
		// }
		public List<StorePay.Data> getDatas() {
			return storePay;
		}

		/**
		 * 移除某个数据
		 */
		public void removeAllDatas() {
			storePay.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return storePay.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			// return datas.get(arg0);
			return storePay.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(OrdersActivity.this).inflate(
						R.layout.member_order_item, parent, false);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}

			return cv;
		}

	}

	class XpenseAdapter extends BaseAdapter {
		private List<StorePay.Data> storePay = new ArrayList<StorePay.Data>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<StorePay.Data> list) {
			storePay.addAll(list);
			notifyDataSetChanged();
		}

		// /**
		// * 移除某个数据
		// * @param position
		// */
		// public void removeDatas(int position){
		// datas.remove(position);
		// notifyDataSetChanged();
		// }
		public List<StorePay.Data> getDatas() {
			return storePay;
		}

		/**
		 * 移除某个数据
		 */
		public void removeAllDatas() {
			storePay.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return storePay.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			// return datas.get(arg0);
			return storePay.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(OrdersActivity.this).inflate(
						R.layout.member_order_item, parent, false);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}

			return cv;
		}

	}

	public static void slideview(final float p1, final float p2) {
		Animation animation = new TranslateAnimation(p1, p2, 0, 0);
		// 添加了这行代码的作用时，view移动的时候 会有弹性效果
		// animation.setInterpolator(new OvershootInterpolator());
		// 设置动画持续时间，单位毫秒 ；
		animation.setDuration(500);
		animation.setFillAfter(true);
		// 设置动画执行之前等待时间；
		animation.setStartOffset(0);

		// animation.setAnimationListener(new Animation.AnimationListener() {
		// @Override
		// public void onAnimationStart(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// int left = (int) p2;
		// int top = iv_wait_receive_red.getTop();
		// int width = iv_wait_receive_red.getWidth();
		// int height = iv_wait_receive_red.getHeight();
		// iv_wait_receive_red.clearAnimation();
		// animation.setFillAfter(true);
		// iv_wait_receive_red.layout(left, top, left + width, top
		// + height);
		// System.out.println("left+" + left + "top+" + top + "width+"
		// + width + "height+" + height);
		// }
		// });

		iv_wait_receive_red.startAnimation(animation);
	}

}