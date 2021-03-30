package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.listener.CLientSearchListenerBig;
import com.example.oto01.listener.CLientSearchListenerBig.CLientSearchListener;
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

public class SearchOrderActivity extends BaseActivity implements
		OnClickListener, CLientSearchListener, TextWatcher {

	@ViewInject(R.id.title_font)
	private TextView title_font;
	@ViewInject(R.id.school_friend)
	private EditText school_friend;
	@ViewInject(R.id.tv_cancle)
	private TextView tv_cancle;
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(R.id.pullToRefreshListView1)
	private PullToRefreshListView pullToRefreshListView1;
	@ViewInject(R.id.pullToRefreshListView2)
	private PullToRefreshListView pullToRefreshListView2;
	@ViewInject(R.id.ll_listView2)
	private LinearLayout ll_listView2;
	@ViewInject(R.id.ll_listView1)
	private LinearLayout ll_listView1;
	private ListView listView2;
	private ListView listView1;
	private SearchAdapter adapter;
	private Dialog proDialog;
	private boolean data2;
	private static final int SEARCH_ORDER = 1;
	private static final int SHOP_ORDER = 2;
	private int shopsid = 1;
	private int search_type = 1;
	private static final int JIE_DAN = 101;
	private static final int SHOP_CODE = 103;
	private static final int REQUEST_SHOP_PAY_DETAILS = 104;
	private static final int REQUEST_ORDER = 105;
	private int current_page = 1;
	private int total_page = 0;
	private String keyword = "";
	private int paystate = 0;
	/**
	 * 到店支付adapter
	 */
	private StorePayAdapter storePayAdapter;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == JIE_DAN) {
				// 接单功能
				Integer flag = (Integer) msg.obj;
				ToastUtil.show(getApplicationContext(), message);
				search(false, true);
			} else if (msg.what == SHOP_CODE) {
				String res = (String) msg.obj;
				JSONObject jo;
				try {
					jo = new JSONObject(res);
					if (jo.optInt("res") == 0) {
						try {
							storePayAdapter.removeAllDatas();
							storePayAdapter.notifyDataSetChanged();
							getData(true);
						} catch (Exception e) {
						}
					}
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
	private String message;
	private int i = 0;
	private int y = 0;

	private void getCurrentOrderList(Message msg) {

		if (msg.arg2 == -1) {
			try {
				if (getIntent().getStringExtra("type").equals("shop")) {
					// storePayAdapter.removeAllDatas();
					storePayAdapter.notifyDataSetChanged();
				} else {
					// adapter.removeAllDatas();
					adapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
			}
		}

		try {
			if (getIntent().getStringExtra("type").equals("shop")
					&& msg.what == SHOP_ORDER) {
				// 到店支付
				List<StorePay.Data> list = (List<StorePay.Data>) msg.obj;
				if (list != null && !list.isEmpty()) {
					if (list.size() == 0) {
						ToastUtil.show(getApplicationContext(), "没有数据");
						pullToRefreshListView2.onRefreshComplete();
						return;
					}
					// if (i != y) {
					// storePayAdapter.removeAllDatas();
					// storePayAdapter.notifyDataSetChanged();
					// }
					int over = msg.arg2;
					if (over == 0) {
						ToastUtil.show(this, "已经到底部了");
						storePayAdapter.addAllDatas(list);
						storePayAdapter.notifyDataSetChanged();
						pullToRefreshListView2.onRefreshComplete();
						return;
					} else if (over == 1) {
						storePayAdapter.notifyDataSetChanged();
						pullToRefreshListView2.onRefreshComplete();
					} else {
						storePayAdapter.addAllDatas(list);
						storePayAdapter.notifyDataSetChanged();
						pullToRefreshListView2.onRefreshComplete();
					}
				} else {
					ToastUtil.show(getApplicationContext(), "暂无数据");
					storePayAdapter.notifyDataSetChanged();
					pullToRefreshListView2.onRefreshComplete();
				}

			} else if (msg.what == SEARCH_ORDER) {

				List<Order> gds = (List<Order>) msg.obj;
				if (gds != null && !gds.isEmpty()) {
					if (gds.size() == 0) {
						adapter.removeAllDatas();
						adapter.notifyDataSetChanged();
						// ToastUtil.show(getApplicationContext(), "没有数据");
						pullToRefreshListView1.onRefreshComplete();
						return;
					}
					switch (msg.what) {
					case SEARCH_ORDER:
						// if (i!=y) {
						// adapter.removeAllDatas();
						// adapter.notifyDataSetChanged();
						// }
						int over = msg.arg2;
						if (over == 0) {
							ToastUtil.show(this, "已经到底部了");
							adapter.addAllDatas(gds);
							adapter.notifyDataSetChanged();
							pullToRefreshListView1.onRefreshComplete();
							return;
						} else if (over == 1) {
							adapter.notifyDataSetChanged();
							pullToRefreshListView1.onRefreshComplete();
						} else {
							adapter.addAllDatas(gds);
							adapter.notifyDataSetChanged();
							pullToRefreshListView1.onRefreshComplete();
						}
						break;
					}
				} else {
					// adapter.removeAllDatas();
					adapter.notifyDataSetChanged();
					pullToRefreshListView1.onRefreshComplete();
				}

			}
		} catch (Exception e) {
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			SearchOrderActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_search);
		ViewUtils.inject(this);
		LoginManager lm = LoginManager.getInstance(SearchOrderActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		try {
			if (getIntent().getStringExtra("type").equals("shop")) {
				ll_listView1.setVisibility(View.GONE);
				ll_listView2.setVisibility(View.VISIBLE);
				iv_back.setVisibility(View.VISIBLE);
				title_font.setVisibility(View.GONE);
				school_friend.setHint("输入订单号、验证码或金额");
				school_friend.setVisibility(View.VISIBLE);
				storePayAdapter = new StorePayAdapter();
				pullToRefreshListView2 = PullListViewUtils
						.setRefreshBothMode(pullToRefreshListView2);
				listView2 = pullToRefreshListView2.getRefreshableView();
				pullToRefreshListView2
						.setOnRefreshListener(new CLientSearchListenerBig(
								CLientSearchListenerBig.CLIENT_SEARCH, this));
				listView2.setAdapter(storePayAdapter);
				school_friend.addTextChangedListener(this);
			} else {
				ll_listView1.setVisibility(View.VISIBLE);
				ll_listView2.setVisibility(View.GONE);
				iv_back.setVisibility(View.VISIBLE);
				title_font.setVisibility(View.GONE);
				school_friend.setHint("输入电话、订单号、收货人或地址");
				school_friend.setVisibility(View.VISIBLE);

				adapter = new SearchAdapter();
				pullToRefreshListView1 = PullListViewUtils
						.setRefreshBothMode(pullToRefreshListView1);
				listView1 = pullToRefreshListView1.getRefreshableView();
				pullToRefreshListView1
						.setOnRefreshListener(new CLientSearchListenerBig(
								CLientSearchListenerBig.CLIENT_SEARCH, this));
				listView1.setAdapter(adapter);
				school_friend.addTextChangedListener(this);
			}
		} catch (Exception e) {

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		Intent intent = new Intent();
		SearchOrderActivity.this.setResult(RESULT_OK, intent);
		finish();
	}

	private class SearchAdapter extends BaseAdapter {
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
				cv = LayoutInflater.from(SearchOrderActivity.this).inflate(
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
			holder.status.setVisibility(View.VISIBLE);
			holder.status.setText("接单");
			holder.status.setBackgroundResource(R.drawable.dai_jie_dan);
			holder.order_status1.setVisibility(View.INVISIBLE);
		case 4:
			if (order.getPaystate() == 1 && order.getPayment() == 2) {
				// 未付款，电子支付
				holder.order_status1.setVisibility(View.VISIBLE);
				holder.order_status1.setBackgroundResource(R.drawable.wait_pay);
				holder.status.setVisibility(View.INVISIBLE);
			} else {
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setText("接单");
				holder.status.setBackgroundResource(R.drawable.dai_jie_dan);
				holder.order_status1.setVisibility(View.INVISIBLE);
			}
			break;
		case 2:
			holder.order_status1.setVisibility(View.VISIBLE);
			holder.order_status1.setText("已发货");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_yifa);
			holder.status.setVisibility(View.INVISIBLE);
			break;
		case 3:
			holder.order_status1.setVisibility(View.VISIBLE);
			holder.order_status1.setText("已完成");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_yifa);
			holder.status.setVisibility(View.INVISIBLE);
			break;
		case -1:
			holder.order_status1.setVisibility(View.VISIBLE);
			holder.order_status1.setText("已关闭");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_closed);
			holder.status.setVisibility(View.INVISIBLE);
			break;
		case 5:
			// 待发货
			holder.order_status1.setVisibility(View.VISIBLE);
			holder.order_status1.setText("待发货");
			holder.order_status1
					.setBackgroundResource(R.drawable.order_status_yifa);
			holder.status.setVisibility(View.INVISIBLE);
			break;
		}
		// if (order.getPaystate() == 1 && order.getPayment() == 1
		// && (order.getState() == 1 || order.getState() == 4)) {
		//
		// }
		// 1、普通(优惠券)订单 2、礼品券到店订单
		if (order.getOrder_type().equals("1")) {
			holder.rl_quan.setVisibility(View.GONE);
		} else {
			holder.rl_quan.setVisibility(View.VISIBLE);
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
		holder.consiginee.setText(order.getLinkman());
		holder.location.setText(order.getAddress());
		holder.time.setText(DateUtil.getFormatedDate_1(order.getAddtime()));
		holder.goodTitle.setText(order.getOrdernum());

		if (order.getOrder_type().equals("1")) {
			int size = order.getOrderGood().size();
			if (size > 4) {
				size = 4;
			}
			if (size == 1) {
				// holder.goodTitle.setText(order.getOrderGood().get(0).getContent());
				String imgPath = order.getOrderGood().get(0).getPicpath();
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

				// holder.mlv.setVisibility(View.GONE);
			} else if (size == 2) {
				String imgPath = order.getOrderGood().get(0).getPicpath();
				String imgPath1 = order.getOrderGood().get(1).getPicpath();
				if (imgPath != null && imgPath1 != null) {
					ImageLoadTool.disPlay(imgPath, holder.iv_mine1,
							R.drawable.default_image);
					ImageLoadTool.disPlay(imgPath1, holder.iv_mine2,
							R.drawable.default_image);
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
	 * 根据类型提交订单
	 * 
	 * @param type
	 */

	private void confirmOrder(int status, int orderid) {
		OrderService orderService = new OrderService(SearchOrderActivity.this);
		int flag = -3;
		String res;
		res = orderService.updateOrderStatejiedan(orderid, 5);
		JSONObject jo = null;
		try {
			jo = new JSONObject(res);
			flag = jo.optInt("res");
			message = jo.getString("msg");
			System.out.println("------------flag-------------->" + flag);
			System.out.println("------------message-------------->" + message);
			Message message = handler.obtainMessage(JIE_DAN);
			message.obj = flag;
			handler.sendMessage(message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private int pageIndex = 1;

	@Override
	public void search(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		if (isHeadRefresh) {// 刷新
			pageIndex = 1;
			getData(true);
			if (getIntent().getStringExtra("type").equals("shop")) {
				storePayAdapter.removeAllDatas();
			} else {
				adapter.removeAllDatas();
			}
		} else {// 加载
			getData(false);
		}
		pageIndex++;

	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	/**
	 * 请求数据
	 * 
	 * @param b
	 */
	@SuppressLint("NewApi")
	private void getData(Boolean data1) {
		data2 = data1;
		if (!NetConn.checkNetwork(SearchOrderActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {

			if (school_friend.getText().toString().trim().isEmpty()) {

			} else {
				try {
					if (getIntent().getStringExtra("type").equals("shop")) {
						// 到店支付页面
						new MyStorePayAsync().execute();
					} else {
						new SearchOrderAsync().execute();
					}
				} catch (Exception e) {

				}

			}

		}
	}

	private class MyStorePayAsync extends AsyncTask<Void, Void, Void> {
		private String res;

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(
					SearchOrderActivity.this);
			Message message = handler.obtainMessage(SHOP_ORDER);
			// message.arg1 = CURRENT_OPT_STATUS;
			if (data2) {
				// 刷新
				message.arg2 = -2;
				res = orderService.getStorePayList(shopsid, 1, keyword);
			} else {
				// 加载
				res = orderService.getStorePayList(shopsid, pageIndex, keyword);
				if (pageIndex == total_page) {
					message.arg2 = 0;
					// pageIndex = total_page;
				} else if (pageIndex < total_page) {
					message.arg2 = -1;
				} else if (pageIndex > total_page) {
					message.arg2 = 1;
				}
			}
			StorePay storePay = JsonUtils.fromJson(res, StorePay.class);
			total_page = Integer.parseInt(storePay.getTotal());
			message.obj = storePay.getData();
			handler.sendMessage(message);
			return null;
		}
	}

	private class SearchOrderAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(
					SearchOrderActivity.this);
			List<Order> orders = new ArrayList<Order>();
			Message message = handler.obtainMessage(SEARCH_ORDER);
			// message.arg1 = CURRENT_OPT_STATUS;
			if (data2) {
				message.arg2 = -2;
				orders = orderService.getOrdersList(shopsid, 0, 1, keyword,
						paystate);
			} else {
				if (current_page == total_page) {
					message.arg2 = 0;
					current_page++;
				} else if (current_page < total_page) {
					message.arg2 = -1;
					current_page++;
				} else if (current_page > total_page) {
					message.arg2 = 1;
				}
				orders = orderService.getOrdersList(shopsid, 0, current_page,
						keyword, paystate);
			}
			total_page = orderService.total_page;
			current_page = orderService.p;
			message.obj = orders;
			handler.sendMessage(message);
			return null;

		}
	}

	/**
	 * 跳转到订单详情
	 * 
	 * @param or
	 */
	private void jumpToDetails(Order or) {
		if (or != null) {
			adapter.removeAllDatas();
			adapter.notifyDataSetChanged();
			Intent it = new Intent(this, OrderDetailsActivity.class);
			it.putExtra("order_id", or.getId());
			it.putExtra("uid", or.getUid());
			it.putExtra("ordertype", or.getOrder_type());
			startActivityForResult(it, REQUEST_ORDER);
		} else {
			System.out.println("------无法跳转------>");
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		y = count;
		System.out.println(y + "---------beforeTextChanged----------->");

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		keyword = s.toString().trim();

		i = count;
		System.out.println(i + "---------onTextChanged----------->");
		System.out.println(keyword + "------------------>");
		if (keyword.equals("")) {
			System.out.println(keyword + "------if------------>");
			if (getIntent().getStringExtra("type").equals("shop")) {
				storePayAdapter.removeAllDatas();
				storePayAdapter.notifyDataSetChanged();
			} else {
				adapter.removeAllDatas();
				adapter.notifyDataSetChanged();
			}

		} else {
			System.out.println(keyword + "------else------------>");
			if (i != y) {
				if (getIntent().getStringExtra("type").equals("shop")) {
					storePayAdapter.removeAllDatas();
					storePayAdapter.notifyDataSetChanged();
				} else {
					adapter.removeAllDatas();
					adapter.notifyDataSetChanged();
				}
			}

			try {
				getData(true);
			} catch (Exception e) {
				e.printStackTrace();
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
				cv = LayoutInflater.from(SearchOrderActivity.this).inflate(
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
			holder.order_money.setText(list.getReceive_total_price() + "元");

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
					storePayAdapter.removeAllDatas();
					storePayAdapter.notifyDataSetChanged();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_ORDER) {
				// 外卖订单
				data2 = true;
				new SearchOrderAsync().execute();
			} else if (requestCode == REQUEST_SHOP_PAY_DETAILS) {
				// 到店支付订单
				data2 = true;
				new MyStorePayAsync().execute();
			}
		}
	}

	private String code1;

	protected void yanzheng(String code) {
		code1 = code;
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				SearchOrderActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否设为已验证？");
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!NetConn.checkNetwork(SearchOrderActivity.this)) {
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

			OrderService orderService = new OrderService(
					SearchOrderActivity.this);
			Message message = handler.obtainMessage(SHOP_CODE);
			String res = orderService.getShopCode(shopsid, code1);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	@Override
	public void shop_pay(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub

	}

	@Override
	public void member_card(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub

	}

}
