package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.listener.ClientListListener;
import com.example.oto01.listener.ClientListListener.CLientListener;
import com.example.oto01.model.Client;
import com.example.oto01.model.Client.clientList;
import com.example.oto01.model.Login;
import com.example.oto01.services.ClientService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.CallPhoneUtil;
import com.example.oto01.utils.ClientsUtil;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.PullListViewUtils;
import com.example.oto01.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 
 * 项目名称:o2ov2<br/>
 * 类名称:ClientsActivity<br/>
 * 描述: 我的客户
 * 
 * @author:chenym
 * @Date:2015-9-14下午2:35:19
 */
public class ClientsActivity extends BaseActivity implements OnClickListener,
		CLientListener {

	@ViewInject(R.id.title_font)
	private TextView title_font;
	@ViewInject(R.id.search)
	private ImageView search;
	@ViewInject(R.id.iv_add)
	private ImageView iv_add;

	@ViewInject(R.id.iv_money_back)
	private ImageView iv_money_back;
	@ViewInject(R.id.view)
	private View view;
	@ViewInject(R.id.iv_client_back)
	private ImageView iv_client_back;

	@ViewInject(R.id.rl_all_client)
	private RelativeLayout rl_all_client;
	@ViewInject(R.id.rl_total_money)
	private RelativeLayout rl_total_money;
	@ViewInject(R.id.tv_all_client)
	private TextView tv_all_client;
	@ViewInject(R.id.tv_total_money)
	private TextView tv_total_money;
	@ViewInject(R.id.ll_listView1)
	private LinearLayout ll_listView1;
	@ViewInject(R.id.ll_listView2)
	private LinearLayout ll_listView2;
	@ViewInject(R.id.ll_listView3)
	private LinearLayout ll_listView3;
	@ViewInject(R.id.ll_listView4)
	private LinearLayout ll_listView4;
	@ViewInject(R.id.ll_listView5)
	private LinearLayout ll_listView5;
	@ViewInject(R.id.ll_listView6)
	private LinearLayout ll_listView6;
	@ViewInject(R.id.ll_listView7)
	private LinearLayout ll_listView7;
	@ViewInject(R.id.ll_listView8)
	private LinearLayout ll_listView8;
	@ViewInject(R.id.pullToRefreshListView1)
	private PullToRefreshListView pullToRefreshListView1;
	@ViewInject(R.id.pullToRefreshListView2)
	private PullToRefreshListView pullToRefreshListView2;
	@ViewInject(R.id.pullToRefreshListView3)
	private PullToRefreshListView pullToRefreshListView3;
	@ViewInject(R.id.pullToRefreshListView4)
	private PullToRefreshListView pullToRefreshListView4;
	@ViewInject(R.id.pullToRefreshListView5)
	private PullToRefreshListView pullToRefreshListView5;
	@ViewInject(R.id.pullToRefreshListView6)
	private PullToRefreshListView pullToRefreshListView6;
	@ViewInject(R.id.pullToRefreshListView7)
	private PullToRefreshListView pullToRefreshListView7;
	@ViewInject(R.id.pullToRefreshListView8)
	private PullToRefreshListView pullToRefreshListView8;
	@ViewInject(R.id.iv_xian0)
	private ImageView iv_xian0;
	@ViewInject(R.id.iv_xian)
	private ImageView iv_xian;

	@ViewInject(R.id.iv_no)
	private LinearLayout iv_no;

	private Intent intent;
	private List<Client> clientList = new ArrayList<Client>();
	private ClientAdapter adapter1, adapter2, adapter3, adapter4, adapter5,
			adapter6, adapter7, adapter8;
	private ListView listView1, listView2, listView3, listView4, listView5,
			listView6, listView7, listView8;
	private PopupWindow popupWindowcustom;
	private PopupWindow popupWindowmoney;
	private int pageIndex;
	private int shopsid = 1;
	private Dialog proDialog;
	private boolean isClosed = true;
	private String keyword = "";
	/**
	 * 筛选：1、外卖客户 2、到店支付客户3、无交易用户 （默认不传全部用户）
	 */
	private int user_type = 0;
	/**
	 * 排序：1、金额 2、订单 3、最近 4、姓名 、默认不传总金额
	 */
	private int order = 0;
	private static final int ALL_Client = 101;
	/**
	 * 外卖
	 */
	private static final int TAKE_OUT = 102;
	private static final int NO_BUSINESS = 103;
	private static final int MONEY_HIGH_TO_DOWN = 104;
	private static final int ORDER_HIGH_TO_DOWN = 105;
	private static final int TIME_SORT = 106;
	private static final int NAME_SORT = 107;
	/**
	 * 到店支付
	 */
	private static final int STORE_PAY_SORT = 108;
	private static int CLIENT_DELETE = 109;
	private static int CURRENT_OPT = ALL_Client;
	private Boolean data1;
	private int clientId = 1;
	private int type = 0;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == CLIENT_DELETE) {
				proDialog.dismiss();
				String error = (String) msg.obj;
				ToastUtil.show(getApplicationContext(), error);
				switch (msg.arg2) {
				case ALL_Client:
					all_business(false, true);
					break;
				case TAKE_OUT:
					take_out(false, true);
					break;
				case NO_BUSINESS:
					no_business(false, true);
					break;
				case MONEY_HIGH_TO_DOWN:
					money_high_to_low(false, true);
					break;
				case ORDER_HIGH_TO_DOWN:
					order_high_to_low(false, true);
					break;
				case TIME_SORT:
					time_sort(false, true);
					break;
				case NAME_SORT:
					name_sort(false, true);
				case STORE_PAY_SORT:
					store_pay_sort(false, true);
					break;
				}
			} else {
				getCurrentOrderList(msg);
			}

		}

	};

	private void getCurrentOrderList(Message msg) {
		if (!isClosed) {
			proDialog.dismiss();
			isClosed = true;
		}
		try {
			Client client = (Client) msg.obj;

			if (client.getRes().equals("0")) {
				iv_no.setVisibility(View.GONE);
				List<clientList> gds = client.getData();

				if (gds.size() == 0 && client.getP() < client.getTotalnum()) {
					ToastUtil.show(getApplicationContext(), "没有数据");
					pullToRefreshListView1.onRefreshComplete();
					pullToRefreshListView2.onRefreshComplete();
					pullToRefreshListView3.onRefreshComplete();
					pullToRefreshListView4.onRefreshComplete();
					pullToRefreshListView5.onRefreshComplete();
					pullToRefreshListView6.onRefreshComplete();
					pullToRefreshListView7.onRefreshComplete();
					pullToRefreshListView8.onRefreshComplete();
					return;
				}

				if (client.getP() == client.getTotalnum()) {
					if (msg.arg1 == 1) {
						ToastUtil.show(getApplicationContext(), "已经到底部了");
					}
				} else if (client.getP() > client.getTotalnum()) {
					if (msg.arg1 == 1) {
						ToastUtil.show(getApplicationContext(), "已经到底部了");
					} else {
						iv_no.setVisibility(View.VISIBLE);
						ll_listView1.setVisibility(View.GONE);
						ll_listView2.setVisibility(View.GONE);
						ll_listView3.setVisibility(View.GONE);
						ll_listView4.setVisibility(View.GONE);
						ll_listView5.setVisibility(View.GONE);
						ll_listView6.setVisibility(View.GONE);
						ll_listView7.setVisibility(View.GONE);
						ll_listView8.setVisibility(View.GONE);
					}
				}

				for (int i = 0; i < gds.size(); i++) {
					try {
						if (gds.get(i).getIs_call().equals("1")) {
							// 可打电话
							gds.get(i).setDown(false);
							gds.get(i).setUp(true);
						} else {
							// 不可打电话
							gds.get(i).setDown(false);
							gds.get(i).setUp(false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						gds.get(i).setDown(false);
						gds.get(i).setUp(false);
					}

				}

				switch (msg.what) {
				case ALL_Client:
					// 近一月
					ll_listView1.setVisibility(View.VISIBLE);
					ll_listView2.setVisibility(View.GONE);
					ll_listView3.setVisibility(View.GONE);
					ll_listView4.setVisibility(View.GONE);
					ll_listView5.setVisibility(View.GONE);
					ll_listView7.setVisibility(View.GONE);
					ll_listView6.setVisibility(View.GONE);
					ll_listView8.setVisibility(View.GONE);
					adapter1.addAllDatas(gds);
					pullToRefreshListView1.onRefreshComplete();
					break;
				case TAKE_OUT:
					ll_listView1.setVisibility(View.GONE);
					ll_listView2.setVisibility(View.VISIBLE);
					ll_listView3.setVisibility(View.GONE);
					ll_listView4.setVisibility(View.GONE);
					ll_listView5.setVisibility(View.GONE);
					ll_listView6.setVisibility(View.GONE);
					ll_listView7.setVisibility(View.GONE);
					ll_listView8.setVisibility(View.GONE);
					adapter2.addAllDatas(gds);
					adapter2.notifyDataSetChanged();
					pullToRefreshListView2.onRefreshComplete();
					break;
				case NO_BUSINESS:
					ll_listView1.setVisibility(View.GONE);
					ll_listView2.setVisibility(View.GONE);
					ll_listView3.setVisibility(View.VISIBLE);
					ll_listView4.setVisibility(View.GONE);
					ll_listView5.setVisibility(View.GONE);
					ll_listView6.setVisibility(View.GONE);
					ll_listView7.setVisibility(View.GONE);
					ll_listView8.setVisibility(View.GONE);
					adapter3.addAllDatas(gds);
					adapter3.notifyDataSetChanged();
					pullToRefreshListView3.onRefreshComplete();
					break;
				case MONEY_HIGH_TO_DOWN:
					ll_listView1.setVisibility(View.GONE);
					ll_listView2.setVisibility(View.GONE);
					ll_listView3.setVisibility(View.GONE);
					ll_listView4.setVisibility(View.VISIBLE);
					ll_listView5.setVisibility(View.GONE);
					ll_listView6.setVisibility(View.GONE);
					ll_listView7.setVisibility(View.GONE);
					ll_listView8.setVisibility(View.GONE);
					adapter4.addAllDatas(gds);
					adapter4.notifyDataSetChanged();
					pullToRefreshListView4.onRefreshComplete();
					break;
				case ORDER_HIGH_TO_DOWN:
					ll_listView1.setVisibility(View.GONE);
					ll_listView2.setVisibility(View.GONE);
					ll_listView3.setVisibility(View.GONE);
					ll_listView4.setVisibility(View.GONE);
					ll_listView5.setVisibility(View.VISIBLE);
					ll_listView6.setVisibility(View.GONE);
					ll_listView7.setVisibility(View.GONE);
					ll_listView8.setVisibility(View.GONE);
					adapter5.addAllDatas(gds);
					adapter5.notifyDataSetChanged();
					pullToRefreshListView5.onRefreshComplete();
					break;
				case TIME_SORT:
					ll_listView1.setVisibility(View.GONE);
					ll_listView2.setVisibility(View.GONE);
					ll_listView3.setVisibility(View.GONE);
					ll_listView4.setVisibility(View.GONE);
					ll_listView5.setVisibility(View.GONE);
					ll_listView6.setVisibility(View.VISIBLE);
					ll_listView7.setVisibility(View.GONE);
					ll_listView8.setVisibility(View.GONE);
					adapter6.addAllDatas(gds);
					adapter6.notifyDataSetChanged();
					pullToRefreshListView6.onRefreshComplete();
					break;
				case NAME_SORT:
					ll_listView1.setVisibility(View.GONE);
					ll_listView2.setVisibility(View.GONE);
					ll_listView3.setVisibility(View.GONE);
					ll_listView4.setVisibility(View.GONE);
					ll_listView5.setVisibility(View.GONE);
					ll_listView6.setVisibility(View.GONE);
					ll_listView7.setVisibility(View.VISIBLE);
					ll_listView8.setVisibility(View.GONE);
					adapter7.addAllDatas(gds);
					adapter7.notifyDataSetChanged();
					pullToRefreshListView7.onRefreshComplete();
					break;
				case STORE_PAY_SORT:
					ll_listView1.setVisibility(View.GONE);
					ll_listView2.setVisibility(View.GONE);
					ll_listView3.setVisibility(View.GONE);
					ll_listView4.setVisibility(View.GONE);
					ll_listView5.setVisibility(View.GONE);
					ll_listView6.setVisibility(View.GONE);
					ll_listView7.setVisibility(View.GONE);
					ll_listView8.setVisibility(View.VISIBLE);
					adapter8.addAllDatas(gds);
					adapter8.notifyDataSetChanged();
					pullToRefreshListView8.onRefreshComplete();
				}

			} else {
				ToastUtil.show(getApplicationContext(), client.getMsg());
				pullToRefreshListView1.onRefreshComplete();
				pullToRefreshListView2.onRefreshComplete();
				pullToRefreshListView3.onRefreshComplete();
				pullToRefreshListView4.onRefreshComplete();
				pullToRefreshListView5.onRefreshComplete();
				pullToRefreshListView6.onRefreshComplete();
				pullToRefreshListView7.onRefreshComplete();
				pullToRefreshListView8.onRefreshComplete();
				iv_no.setVisibility(View.VISIBLE);
				ll_listView1.setVisibility(View.GONE);
				ll_listView2.setVisibility(View.GONE);
				ll_listView3.setVisibility(View.GONE);
				ll_listView4.setVisibility(View.GONE);
				ll_listView5.setVisibility(View.GONE);
				ll_listView6.setVisibility(View.GONE);
				ll_listView7.setVisibility(View.GONE);
				ll_listView8.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_home);
		ViewUtils.inject(this);
		initView();
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

	private void initView() {
		showDialog();
		LoginManager lm = LoginManager.getInstance(ClientsActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		title_font.setText("我的客户");
		search.setVisibility(View.VISIBLE);
		iv_add.setVisibility(View.VISIBLE);
		search.setOnClickListener(this);
		iv_add.setOnClickListener(this);
		rl_all_client.setOnClickListener(this);
		rl_total_money.setOnClickListener(this);
		adapter1 = new ClientAdapter();
		adapter2 = new ClientAdapter();
		adapter3 = new ClientAdapter();
		adapter4 = new ClientAdapter();
		adapter5 = new ClientAdapter();
		adapter6 = new ClientAdapter();
		adapter7 = new ClientAdapter();
		adapter8 = new ClientAdapter();

		ll_listView1 = (LinearLayout) findViewById(R.id.ll_listView1);
		ll_listView2 = (LinearLayout) findViewById(R.id.ll_listView2);
		ll_listView3 = (LinearLayout) findViewById(R.id.ll_listView3);
		ll_listView4 = (LinearLayout) findViewById(R.id.ll_listView4);
		ll_listView5 = (LinearLayout) findViewById(R.id.ll_listView5);
		ll_listView6 = (LinearLayout) findViewById(R.id.ll_listView6);
		ll_listView7 = (LinearLayout) findViewById(R.id.ll_listView7);
		ll_listView8 = (LinearLayout) findViewById(R.id.ll_listView8);

		ll_listView1.setVisibility(View.VISIBLE);
		ll_listView2.setVisibility(View.GONE);
		ll_listView3.setVisibility(View.GONE);
		ll_listView4.setVisibility(View.GONE);
		ll_listView5.setVisibility(View.GONE);
		ll_listView6.setVisibility(View.GONE);
		ll_listView7.setVisibility(View.GONE);
		ll_listView8.setVisibility(View.GONE);

		pullToRefreshListView1 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView1);
		pullToRefreshListView2 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView2);
		pullToRefreshListView3 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView3);
		pullToRefreshListView4 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView4);
		pullToRefreshListView5 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView5);
		pullToRefreshListView6 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView6);
		pullToRefreshListView7 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView7);
		pullToRefreshListView8 = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView8);

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
		pullToRefreshListView7 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView7);
		pullToRefreshListView8 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView8);

		pullToRefreshListView1.setOnRefreshListener(new ClientListListener(
				ClientListListener.ALL_BUSINESS, this));
		pullToRefreshListView2.setOnRefreshListener(new ClientListListener(
				ClientListListener.TAKE_OUT_BUSINESS, this));
		pullToRefreshListView3.setOnRefreshListener(new ClientListListener(
				ClientListListener.NO_BUSINESS, this));
		pullToRefreshListView4.setOnRefreshListener(new ClientListListener(
				ClientListListener.MONEY_HIGH_TO_LOW, this));
		pullToRefreshListView5.setOnRefreshListener(new ClientListListener(
				ClientListListener.ORDER_HIGH_TO_LOW, this));
		pullToRefreshListView6.setOnRefreshListener(new ClientListListener(
				ClientListListener.TIME_SORT, this));
		pullToRefreshListView7.setOnRefreshListener(new ClientListListener(
				ClientListListener.NAME_SORT, this));
		pullToRefreshListView8.setOnRefreshListener(new ClientListListener(
				ClientListListener.STORE_PAY_SORT, this));

		listView1 = pullToRefreshListView1.getRefreshableView();
		listView2 = pullToRefreshListView2.getRefreshableView();
		listView3 = pullToRefreshListView3.getRefreshableView();
		listView4 = pullToRefreshListView4.getRefreshableView();
		listView5 = pullToRefreshListView5.getRefreshableView();
		listView6 = pullToRefreshListView6.getRefreshableView();
		listView7 = pullToRefreshListView7.getRefreshableView();
		listView8 = pullToRefreshListView8.getRefreshableView();

		listView1.setAdapter(adapter1);
		listView2.setAdapter(adapter2);
		listView3.setAdapter(adapter3);
		listView4.setAdapter(adapter4);
		listView5.setAdapter(adapter5);
		listView6.setAdapter(adapter6);
		listView7.setAdapter(adapter7);
		listView8.setAdapter(adapter8);

		ClientsUtil.saveClientState(getApplicationContext(), 0);
		ClientsUtil.saveMoneyState(getApplicationContext(), 0);

		all_business(false, true);
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search:
			intent = new Intent();
			intent.setClass(getApplicationContext(), SearchClientActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_add:
			// 添加
			intent = new Intent();
			intent.setClass(getApplicationContext(), ClientAddActivity.class);
			startActivityForResult(intent, CURRENT_OPT);
			break;
		case R.id.rl_all_client:
			// 全部客户
			CURRENT_OPT = ALL_Client;

			user_type = 0;

			if (tv_all_client.getText().toString().equals("无交易客户")) {
				order = 4;
				tv_total_money.setText("姓名排序");
				iv_xian.setVisibility(View.GONE);
			} else {
				// tv_all_client.setText("全部客户");
				iv_xian.setVisibility(View.VISIBLE);
			}

			// if (tv_total_money.getText().toString().equals("姓名排序")) {
			// iv_xian.setVisibility(View.GONE);
			// } else {
			// iv_xian.setVisibility(View.VISIBLE);
			// }

			tv_total_money.setTextColor(Color.parseColor("#626a7d"));
			// ll_listView1.setVisibility(View.VISIBLE);
			// ll_listView2.setVisibility(View.GONE);
			// ll_listView3.setVisibility(View.GONE);
			// ll_listView4.setVisibility(View.GONE);
			// ll_listView5.setVisibility(View.GONE);
			// ll_listView6.setVisibility(View.GONE);
			// ll_listView7.setVisibility(View.GONE);
			tv_all_client.setTextColor(Color.RED);
			iv_xian0.setImageResource(R.drawable.customer_click_select);
			// Drawable drawable = getResources().getDrawable(
			// R.drawable.customer_click_select);
			// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
			// drawable.getMinimumHeight());
			// tv_all_client.setCompoundDrawables(null, null, drawable,
			// null);
			iv_client_back.setVisibility(View.VISIBLE);
			showPopupWindow_custom(view);

			break;
		case R.id.rl_total_money:
			// 总金额
			if (tv_total_money.getText().toString().equals("姓名排序")
					&& tv_all_client.getText().toString().equals("无交易客户")) {
				order = 4;
				tv_total_money.setText("姓名排序");
				ClientsUtil.saveMoneyState(this, 3);
				CURRENT_OPT = NAME_SORT;
				iv_xian.setVisibility(View.GONE);
			} else {
				order = 0;
				iv_xian.setVisibility(View.VISIBLE);
				CURRENT_OPT = MONEY_HIGH_TO_DOWN;
				tv_total_money.setTextColor(Color.RED);
				iv_xian.setImageResource(R.drawable.customer_click_select);
				iv_money_back.setVisibility(View.VISIBLE);
				showPopupWindow_money(view);
			}

			break;

		case R.id.money_money:
			// 金额从高到低
			Log.i("wwn", "money");
			order = 1;
			tv_total_money.setText("金额从高到低");
			ClientsUtil.saveMoneyState(this, 0);
			CURRENT_OPT = MONEY_HIGH_TO_DOWN;
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.VISIBLE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.GONE);
			ll_listView7.setVisibility(View.GONE);
			money_high_to_low(false, true);
			popupWindowmoney.dismiss();
			break;

		case R.id.money_order:
			// 订单从多到少
			order = 2;
			tv_total_money.setText("订单从多到少");
			ClientsUtil.saveMoneyState(this, 1);
			CURRENT_OPT = ORDER_HIGH_TO_DOWN;
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.VISIBLE);
			ll_listView6.setVisibility(View.GONE);
			ll_listView7.setVisibility(View.GONE);
			order_high_to_low(false, true);
			popupWindowmoney.dismiss();
			break;
		case R.id.money_time:
			// 最近下单时间
			order = 3;
			tv_total_money.setText("最近下单时间");
			ClientsUtil.saveMoneyState(this, 2);
			CURRENT_OPT = TIME_SORT;
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.VISIBLE);
			ll_listView7.setVisibility(View.GONE);
			time_sort(false, true);
			popupWindowmoney.dismiss();
			break;
		case R.id.money_name:
			// 姓名排序
			order = 4;
			tv_total_money.setText("姓名排序");
			ClientsUtil.saveMoneyState(this, 3);
			CURRENT_OPT = NAME_SORT;
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.GONE);
			ll_listView7.setVisibility(View.VISIBLE);
			name_sort(false, true);
			popupWindowmoney.dismiss();
			break;
		case R.id.all_custom:
			// 全部客户
			user_type = 0;
			tv_all_client.setText("全部客户");
			iv_xian.setVisibility(View.VISIBLE);
			ClientsUtil.saveClientState(this, 0);
			popupWindowcustom.dismiss();
			CURRENT_OPT = ALL_Client;
			ll_listView1.setVisibility(View.VISIBLE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.GONE);
			ll_listView7.setVisibility(View.GONE);
			all_business(false, true);
			break;
		case R.id.all_waimai_client:
			// 外卖、上门
			user_type = 1;
			iv_xian.setVisibility(View.VISIBLE);
			tv_all_client.setText("外卖客户");
			ClientsUtil.saveClientState(this, 1);
			CURRENT_OPT = TAKE_OUT;
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.VISIBLE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.GONE);
			ll_listView7.setVisibility(View.GONE);
			take_out(false, true);
			popupWindowcustom.dismiss();
			break;
		case R.id.all_nohave_money:
			// 无交易
			user_type = 3;
			order = 4;
			tv_all_client.setText("无交易客户");
			tv_total_money.setText("姓名排序");
			iv_xian.setVisibility(View.GONE);
			ClientsUtil.saveClientState(this, 3);
			CURRENT_OPT = NO_BUSINESS;
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.VISIBLE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.GONE);
			ll_listView7.setVisibility(View.GONE);
			no_business(false, true);
			popupWindowcustom.dismiss();
			break;
		case R.id.all_shop_pay:
			user_type = 2;
			tv_all_client.setText("到店支付客户");
			ClientsUtil.saveClientState(this, 2);
			CURRENT_OPT = STORE_PAY_SORT;
			ll_listView1.setVisibility(View.GONE);
			ll_listView2.setVisibility(View.GONE);
			ll_listView3.setVisibility(View.GONE);
			ll_listView4.setVisibility(View.GONE);
			ll_listView5.setVisibility(View.GONE);
			ll_listView6.setVisibility(View.GONE);
			ll_listView7.setVisibility(View.GONE);
			ll_listView8.setVisibility(View.VISIBLE);
			store_pay_sort(false, true);
			popupWindowcustom.dismiss();
			break;
		}
	}

	private void showPopupWindow_money(View view) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.popupwindow_custom_money, null);
		Log.i("wwn", "show popwind");
		popupWindowmoney = new PopupWindow(contentView,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

		RelativeLayout money_money = (RelativeLayout) contentView
				.findViewById(R.id.money_money);
		RelativeLayout money_order = (RelativeLayout) contentView
				.findViewById(R.id.money_order);
		RelativeLayout money_time = (RelativeLayout) contentView
				.findViewById(R.id.money_time);
		RelativeLayout money_name = (RelativeLayout) contentView
				.findViewById(R.id.money_name);

		TextView tv_money = (TextView) contentView.findViewById(R.id.tv_money);
		ImageView iv_money_1 = (ImageView) contentView
				.findViewById(R.id.iv_money_1);
		TextView tv_order = (TextView) contentView.findViewById(R.id.tv_order);
		ImageView iv_money_2 = (ImageView) contentView
				.findViewById(R.id.iv_money_2);
		TextView tv_time = (TextView) contentView.findViewById(R.id.tv_time);
		ImageView iv_money_3 = (ImageView) contentView
				.findViewById(R.id.iv_money_3);
		TextView tv_name = (TextView) contentView.findViewById(R.id.tv_name);
		ImageView iv_money_4 = (ImageView) contentView
				.findViewById(R.id.iv_money_4);

		int moneyState = ClientsUtil.getMoneyState(this);
		if (0 == moneyState) {
			// 价格从高到低
			money_money.setBackgroundResource(R.color.white);
			money_order.setBackgroundResource(R.color.background_main);
			money_time.setBackgroundResource(R.color.background_main);
			money_name.setBackgroundResource(R.color.background_main);

			tv_money.setTextColor(Color.RED);
			iv_money_1.setVisibility(View.VISIBLE);
			tv_order.setTextColor(Color.parseColor("#626a7d"));
			iv_money_2.setVisibility(View.GONE);
			tv_time.setTextColor(Color.parseColor("#626a7d"));
			iv_money_3.setVisibility(View.GONE);
			tv_name.setTextColor(Color.parseColor("#626a7d"));
			iv_money_4.setVisibility(View.GONE);

		} else if (1 == moneyState) {
			// 订单从高到低
			money_money.setBackgroundResource(R.color.background_main);
			money_order.setBackgroundResource(R.color.white);
			money_time.setBackgroundResource(R.color.background_main);
			money_name.setBackgroundResource(R.color.background_main);
			tv_money.setTextColor(Color.parseColor("#626a7d"));
			iv_money_1.setVisibility(View.GONE);
			tv_order.setTextColor(Color.RED);
			iv_money_2.setVisibility(View.VISIBLE);
			tv_time.setTextColor(Color.parseColor("#626a7d"));
			iv_money_3.setVisibility(View.GONE);
			tv_name.setTextColor(Color.parseColor("#626a7d"));
			iv_money_4.setVisibility(View.GONE);
		} else if (2 == moneyState) {
			// 最近下单时间
			money_money.setBackgroundResource(R.color.background_main);
			money_order.setBackgroundResource(R.color.background_main);
			money_time.setBackgroundResource(R.color.white);
			money_name.setBackgroundResource(R.color.background_main);

			tv_money.setTextColor(Color.parseColor("#626a7d"));
			iv_money_1.setVisibility(View.GONE);
			tv_order.setTextColor(Color.parseColor("#626a7d"));
			iv_money_2.setVisibility(View.GONE);
			tv_time.setTextColor(Color.RED);
			iv_money_3.setVisibility(View.VISIBLE);
			tv_name.setTextColor(Color.parseColor("#626a7d"));
			iv_money_4.setVisibility(View.GONE);
		} else {
			// 姓名排序
			money_money.setBackgroundResource(R.color.background_main);
			money_order.setBackgroundResource(R.color.background_main);
			money_time.setBackgroundResource(R.color.background_main);
			money_name.setBackgroundResource(R.color.white);

			tv_money.setTextColor(Color.parseColor("#626a7d"));
			iv_money_1.setVisibility(View.GONE);
			tv_order.setTextColor(Color.parseColor("#626a7d"));
			iv_money_2.setVisibility(View.GONE);
			tv_time.setTextColor(Color.parseColor("#626a7d"));
			iv_money_3.setVisibility(View.GONE);
			tv_name.setTextColor(Color.RED);
			iv_money_4.setVisibility(View.VISIBLE);
		}

		money_money.setOnClickListener(this);
		money_order.setOnClickListener(this);
		money_time.setOnClickListener(this);
		money_name.setOnClickListener(this);
		popupWindowmoney.setTouchable(true);

		popupWindowmoney.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		popupWindowmoney.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.no_select_bg));
		popupWindowmoney.showAsDropDown(view);
		popupWindowmoney.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				tv_total_money.setTextColor(Color.parseColor("#626a7d"));
				iv_xian.setImageResource(R.drawable.customer_click_nromall);
				// Drawable drawable1 = getResources().getDrawable(
				// R.drawable.customer_click_nromall);
				// drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
				// drawable1.getMinimumHeight());
				// tv_total_money
				// .setCompoundDrawables(null, null, drawable1, null);
				iv_money_back.setVisibility(View.GONE);
			}
		});
	}

	private void showPopupWindow_custom(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.popupwindow_custom_client, null);
		Log.i("wwn", "show popwind");
		popupWindowcustom = new PopupWindow(contentView,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);

		FrameLayout fl_layout = (FrameLayout) contentView
				.findViewById(R.id.fl_layout);

		RelativeLayout all_custom = (RelativeLayout) contentView
				.findViewById(R.id.all_custom);
		RelativeLayout all_member_card = (RelativeLayout) contentView
				.findViewById(R.id.all_member_card);
		RelativeLayout all_nohave_money = (RelativeLayout) contentView
				.findViewById(R.id.all_nohave_money);
		RelativeLayout all_shop_pay = (RelativeLayout) contentView
				.findViewById(R.id.all_shop_pay);
		RelativeLayout all_waimai_client = (RelativeLayout) contentView
				.findViewById(R.id.all_waimai_client);

		TextView tv_all_custom = (TextView) contentView
				.findViewById(R.id.tv_all_custom);
		ImageView iv_all_1 = (ImageView) contentView
				.findViewById(R.id.iv_all_1);

		TextView tv_all_member_card = (TextView) contentView
				.findViewById(R.id.tv_all_member_card);
		ImageView iv_all_2 = (ImageView) contentView
				.findViewById(R.id.iv_all_2);

		TextView tv_all_shop_pay = (TextView) contentView
				.findViewById(R.id.tv_all_shop_pay);
		ImageView iv_all_3 = (ImageView) contentView
				.findViewById(R.id.iv_all_3);

		TextView tv_waimai_client = (TextView) contentView
				.findViewById(R.id.tv_waimai_client);
		ImageView iv_all_4 = (ImageView) contentView
				.findViewById(R.id.iv_all_4);

		TextView tv_nohave_money = (TextView) contentView
				.findViewById(R.id.tv_nohave_money);
		ImageView iv_all_5 = (ImageView) contentView
				.findViewById(R.id.iv_all_5);
		// fl_layout.getBackground().setAlpha(100);

		int clientState = ClientsUtil.getClientState(this);

		if (0 == clientState) {
			// 全部客户的样式
			all_custom.setBackgroundResource(R.color.white);
			all_member_card.setBackgroundResource(R.color.background_main);
			all_shop_pay.setBackgroundResource(R.color.background_main);
			all_waimai_client.setBackgroundResource(R.color.background_main);
			all_nohave_money.setBackgroundResource(R.color.background_main);

			tv_all_custom.setTextColor(Color.RED);
			iv_all_1.setVisibility(View.VISIBLE);
			tv_all_member_card.setTextColor(Color.parseColor("#626a7d"));
			iv_all_2.setVisibility(View.GONE);
			tv_all_shop_pay.setTextColor(Color.parseColor("#626a7d"));
			iv_all_3.setVisibility(View.GONE);
			tv_waimai_client.setTextColor(Color.parseColor("#626a7d"));
			iv_all_4.setVisibility(View.GONE);
			tv_nohave_money.setTextColor(Color.parseColor("#626a7d"));
			iv_all_5.setVisibility(View.GONE);
		} else if (1 == clientState) {
			// 外卖
			all_custom.setBackgroundResource(R.color.background_main);
			all_member_card.setBackgroundResource(R.color.background_main);
			all_shop_pay.setBackgroundResource(R.color.background_main);
			all_waimai_client.setBackgroundResource(R.color.white);
			all_nohave_money.setBackgroundResource(R.color.background_main);
			tv_all_custom.setTextColor(Color.parseColor("#626a7d"));
			iv_all_1.setVisibility(View.GONE);
			tv_waimai_client.setTextColor(Color.RED);
			iv_all_4.setVisibility(View.VISIBLE);
			tv_all_shop_pay.setTextColor(Color.parseColor("#626a7d"));
			iv_all_3.setVisibility(View.GONE);
			tv_all_member_card.setTextColor(Color.parseColor("#626a7d"));
			iv_all_2.setVisibility(View.GONE);
			tv_nohave_money.setTextColor(Color.parseColor("#626a7d"));
			iv_all_5.setVisibility(View.GONE);
		} else if (2 == clientState) {
			// 到店支付
			all_custom.setBackgroundResource(R.color.background_main);
			all_member_card.setBackgroundResource(R.color.background_main);
			all_shop_pay.setBackgroundResource(R.color.white);
			all_waimai_client.setBackgroundResource(R.color.background_main);
			all_nohave_money.setBackgroundResource(R.color.background_main);
			tv_all_custom.setTextColor(Color.parseColor("#626a7d"));
			iv_all_1.setVisibility(View.GONE);
			tv_nohave_money.setTextColor(Color.parseColor("#626a7d"));
			iv_all_5.setVisibility(View.GONE);
			tv_all_shop_pay.setTextColor(Color.RED);
			iv_all_3.setVisibility(View.VISIBLE);
			tv_all_member_card.setTextColor(Color.parseColor("#626a7d"));
			iv_all_2.setVisibility(View.GONE);
			tv_waimai_client.setTextColor(Color.parseColor("#626a7d"));
			iv_all_4.setVisibility(View.GONE);
		} else if (3 == clientState) {
			// 无交易
			all_custom.setBackgroundResource(R.color.background_main);
			all_member_card.setBackgroundResource(R.color.background_main);
			all_shop_pay.setBackgroundResource(R.color.background_main);
			all_waimai_client.setBackgroundResource(R.color.background_main);
			all_nohave_money.setBackgroundResource(R.color.white);
			tv_all_custom.setTextColor(Color.parseColor("#626a7d"));
			iv_all_1.setVisibility(View.GONE);
			tv_all_shop_pay.setTextColor(Color.parseColor("#626a7d"));
			iv_all_3.setVisibility(View.GONE);
			tv_nohave_money.setTextColor(Color.RED);
			iv_all_5.setVisibility(View.VISIBLE);
			tv_all_member_card.setTextColor(Color.parseColor("#626a7d"));
			iv_all_2.setVisibility(View.GONE);
			tv_waimai_client.setTextColor(Color.parseColor("#626a7d"));
			iv_all_4.setVisibility(View.GONE);
		}
		all_custom.setOnClickListener(this);
		all_shop_pay.setOnClickListener(this);
		all_nohave_money.setOnClickListener(this);
		all_waimai_client.setOnClickListener(this);
		all_member_card.setOnClickListener(this);

		popupWindowcustom.setTouchable(true);

		popupWindowcustom.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		popupWindowcustom.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.no_select_bg));

		// 设置好参数之后再show
		popupWindowcustom.showAsDropDown(view);
		popupWindowcustom.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				tv_all_client.setTextColor(Color.parseColor("#626a7d"));
				iv_xian0.setImageResource(R.drawable.customer_click_nromall);
				// Drawable drawable = getResources().getDrawable(
				// R.drawable.customer_click_nromall);
				// // / 这一步必须要做,否则不会显示.
				// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				// drawable.getMinimumHeight());
				// tv_all_client.setCompoundDrawables(null, null, drawable,
				// null);
				// iv_client.setBackgroundResource(R.drawable.customer_click_nromall);
				iv_client_back.setVisibility(View.GONE);
			}
		});
	}

	private class ClientAdapter extends BaseAdapter {
		List<clientList> clients = new ArrayList<clientList>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<clientList> list) {
			clients.addAll(list);
			notifyDataSetChanged();
		}

		/**
		 * 移除某个数据
		 * 
		 * @param position
		 */
		public void removeDatas(int position) {
			clients.remove(position);
			notifyDataSetChanged();
		}

		/**
		 * 移除全部数据
		 * 
		 * @param position
		 */
		public void removeAllDatas() {
			clients.clear();
			notifyDataSetChanged();
		}

		@ViewInject(R.id.tv_name)
		private TextView tv_name;
		@ViewInject(R.id.tv_phone)
		private TextView tv_phone;
		@ViewInject(R.id.tv_total_price)
		private TextView tv_total_price;
		@ViewInject(R.id.tv_total_order)
		private TextView tv_total_order;
		@ViewInject(R.id.tv_recent)
		private TextView tv_recent;
		@ViewInject(R.id.ll_client)
		private LinearLayout ll_client;

		@ViewInject(R.id.ll_big)
		private LinearLayout ll_big;
		@ViewInject(R.id.ll_little)
		private LinearLayout ll_little;
		@ViewInject(R.id.tv_name1)
		private TextView tv_name1;
		@ViewInject(R.id.tv_phone1)
		private TextView tv_phone1;

		@ViewInject(R.id.iv_head1)
		private ImageView iv_head1;
		@ViewInject(R.id.iv_head)
		private ImageView iv_head;
		@ViewInject(R.id.ll_waimai)
		private LinearLayout ll_waimai;
		@ViewInject(R.id.ll_card)
		private LinearLayout ll_card;
		@ViewInject(R.id.ll_buy_dan)
		private LinearLayout ll_buy_dan;
		@ViewInject(R.id.ll_tu)
		private LinearLayout ll_tu;
		@ViewInject(R.id.iv_1)
		private ImageView iv_1;
		@ViewInject(R.id.iv_2)
		private ImageView iv_2;
		@ViewInject(R.id.iv_3)
		private ImageView iv_3;

		@ViewInject(R.id.rl_down)
		private RelativeLayout rl_down;
		@ViewInject(R.id.ll_big_down)
		private LinearLayout ll_big_down;
		@ViewInject(R.id.rl_phone)
		private RelativeLayout rl_phone;
		@ViewInject(R.id.rl_message)
		private RelativeLayout rl_message;
		@ViewInject(R.id.rl_up)
		private RelativeLayout rl_up;
		@ViewInject(R.id.ll_up)
		private LinearLayout ll_up;

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(ClientsActivity.this).inflate(
						R.layout.client_list_item, parent, false);
				holder = new ViewHolder();
				ViewUtils.inject(this, cv);

				holder.tv_name = tv_name;
				holder.tv_phone = tv_phone;
				holder.tv_total_price = tv_total_price;
				holder.tv_total_order = tv_total_order;
				holder.tv_recent = tv_recent;
				holder.ll_client = ll_client;
				holder.ll_big = ll_big;
				holder.ll_little = ll_little;
				holder.tv_name1 = tv_name1;
				holder.tv_phone1 = tv_phone1;
				holder.iv_head1 = iv_head1;
				holder.iv_head = iv_head;
				holder.ll_waimai = ll_waimai;
				holder.ll_card = ll_card;
				holder.ll_buy_dan = ll_buy_dan;
				holder.ll_tu = ll_tu;
				holder.iv_1 = iv_1;
				holder.iv_2 = iv_2;
				holder.iv_3 = iv_3;
				holder.rl_down = rl_down;
				holder.ll_big_down = ll_big_down;
				holder.rl_phone = rl_phone;
				holder.rl_message = rl_message;
				holder.rl_up = rl_up;
				holder.ll_up = ll_up;
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
				holder.clareHolder();
			}

			final clientList list = clients.get(position);
			holder.ll_tu.setVisibility(View.GONE);

			if (list.getOrdernums().equals("0")) {
				// 没有订单，在我的客户，添加客户中添加的客户
				holder.ll_big.setVisibility(View.GONE);
				holder.ll_little.setVisibility(View.VISIBLE);
				holder.tv_name1.setText(list.getNickname());
				holder.tv_phone1.setText(list.getUserphone());
				String imgPath = list.getHeadimage();

				// if (null != imgPath && imgPath.contains(";")) {
				// String[] splitImage = imgPath.split(";");
				ImageLoadTool.disPlay(imgPath, holder.iv_head1,
						R.drawable.client_head);
				// } else {
				// ImageLoadTool.disPlay(imgPath, holder.iv_head1,
				// R.drawable.client_head);
				// }

			} else {
				holder.ll_big.setVisibility(View.VISIBLE);
				holder.ll_little.setVisibility(View.GONE);
				holder.tv_phone.setVisibility(View.VISIBLE);
				holder.tv_name.setText(list.getNickname());
				holder.tv_phone.setText(list.getUserphone());
				holder.tv_total_price.setText("￥" + list.getTotal());
				holder.tv_total_order.setText(list.getOrdernums() + "单");
				holder.tv_recent.setText(DateUtil.getFormatedDate_1(list
						.getOvertime_order()));
				String imgPath = list.getHeadimage();

				// if (null != imgPath ) {
				ImageLoadTool.disPlay(imgPath, holder.iv_head,
						R.drawable.client_head);
				// } else {
				// ImageLoadTool.disPlay(imgPath, holder.iv_head,
				// R.drawable.client_head);
				// }

				try {
					if (list.getIs_wai().equals("1")
							&& list.getIs_pay().equals("2")) {
						holder.ll_tu.setVisibility(View.VISIBLE);
						// 有外卖订单,无到店支付
						holder.ll_buy_dan.setVisibility(View.VISIBLE);
						holder.ll_buy_dan
								.setGravity(android.view.Gravity.CENTER);
						holder.ll_card.setVisibility(View.GONE);
						holder.ll_waimai.setVisibility(View.GONE);
						holder.iv_1.setImageResource(R.drawable.client_waimai);
						holder.iv_2
								.setImageResource(R.drawable.white_backgroud);
						holder.iv_3
								.setImageResource(R.drawable.white_backgroud);
					} else if (list.getIs_wai().equals("1")
							&& list.getIs_pay().equals("1")) {
						holder.ll_tu.setVisibility(View.VISIBLE);
						// 有外卖订单，有到店支付订单
						holder.ll_buy_dan.setVisibility(View.VISIBLE);
						holder.ll_card.setVisibility(View.VISIBLE);
						holder.ll_waimai.setVisibility(View.GONE);
						holder.ll_buy_dan
								.setGravity(android.view.Gravity.RIGHT);
						holder.ll_buy_dan.setPadding(0, 0, 5, 0);
						holder.ll_card.setPadding(5, 0, 0, 0);
						holder.ll_card.setGravity(android.view.Gravity.LEFT);
						holder.iv_1.setImageResource(R.drawable.client_buy_dan);
						holder.iv_2.setImageResource(R.drawable.client_waimai);
						holder.iv_3
								.setImageResource(R.drawable.white_backgroud);
					} else if (list.getIs_wai().equals("2")
							&& list.getIs_pay().equals("1")) {
						// 无外卖，有到店支付
						holder.ll_tu.setVisibility(View.VISIBLE);
						holder.ll_buy_dan.setVisibility(View.VISIBLE);
						holder.ll_card.setVisibility(View.GONE);
						holder.ll_waimai.setVisibility(View.GONE);
						holder.ll_buy_dan
								.setGravity(android.view.Gravity.CENTER);
						holder.iv_1.setImageResource(R.drawable.client_buy_dan);
						holder.iv_2
								.setImageResource(R.drawable.white_backgroud);
						holder.iv_3
								.setImageResource(R.drawable.white_backgroud);
					} else if (list.getIs_wai().equals("2")
							&& list.getIs_pay().equals("2")) {
						holder.ll_tu.setVisibility(View.GONE);
					}
				} catch (Exception e) {
				}

			}

			try {
				// 是否可打电话：1、是 2、不是
				if (list.getIs_call().equals("1")) {
					// 可打电话
					if (list.isDown()) {
						holder.ll_big_down.setVisibility(View.VISIBLE);
						holder.rl_down.setVisibility(View.GONE);
						// list.setDown(false);
						// list.setUp(false);
					} else if (list.isUp()) {
						holder.ll_big_down.setVisibility(View.GONE);
						holder.rl_down.setVisibility(View.VISIBLE);
						// list.setUp(false);
						// list.setDown(false);
					} else {
						holder.ll_big_down.setVisibility(View.GONE);
						holder.rl_down.setVisibility(View.VISIBLE);
						list.setUp(false);
						list.setDown(false);
					}
				} else {
					holder.ll_big_down.setVisibility(View.GONE);
					holder.rl_down.setVisibility(View.GONE);
					list.setUp(false);
					list.setDown(false);
				}
			} catch (Exception e) {
				holder.ll_big_down.setVisibility(View.GONE);
				holder.rl_down.setVisibility(View.GONE);
				list.setUp(false);
				list.setDown(false);
			}

			holder.ll_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (list.getOrdernums().equals("0")) {

					} else {
						Intent intent = new Intent();
						intent.putExtra("name", list.getNickname());
						intent.putExtra("total_money", list.getTotal());
						intent.putExtra("total_orders", list.getOrdernums());
						intent.putExtra("remark_name", list.getRemark_name());
						intent.putExtra("phone", list.getUserphone());
						intent.putExtra("id", list.getId());
						intent.putExtra("iscall", list.getIs_call());
						intent.putExtra("type", CURRENT_OPT);
						intent.setClass(getApplicationContext(),
								ClientDetailsActivity.class);
						startActivityForResult(intent, CURRENT_OPT);
					}

				}
			});

			holder.rl_down.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					list.setDown(true);
					list.setUp(false);
					notifyDataSetChanged();
				}
			});

			holder.rl_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					list.setUp(true);
					list.setDown(false);
					notifyDataSetChanged();
				}
			});

			// 可以打电话
			holder.rl_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CallPhoneUtil.callPhone(ClientsActivity.this,
							list.getUserphone());
				}
			});
			// 发短信
			holder.rl_message.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 发短信
					Uri smsToUri = Uri.parse("smsto:" + list.getUserphone());
					Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					intent.putExtra("sms_body", "");
					startActivity(intent);
				}
			});

			holder.ll_up.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Delete_client(list.getId());
					return true;
				}
			});
			return cv;
		}

		@Override
		public int getCount() {
			return clients.size();
		}

		@Override
		public Object getItem(int arg0) {
			return clients.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
	}

	public class ViewHolder {
		TextView tv_name;
		TextView tv_phone;
		TextView tv_total_price;
		TextView tv_total_order;
		TextView tv_recent;
		LinearLayout ll_client;
		LinearLayout ll_big;
		LinearLayout ll_little;
		TextView tv_name1;
		TextView tv_phone1;

		ImageView iv_head1;
		ImageView iv_head;
		LinearLayout ll_waimai;
		LinearLayout ll_card;
		LinearLayout ll_buy_dan;
		LinearLayout ll_tu;

		ImageView iv_1;
		ImageView iv_2;
		ImageView iv_3;
		RelativeLayout rl_down;
		LinearLayout ll_big_down;
		RelativeLayout rl_phone;
		RelativeLayout rl_message;
		RelativeLayout rl_up;
		LinearLayout ll_up;

		public void clareHolder() {
			if (null != tv_phone) {
				tv_phone.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 显示提醒取消打电话对话框
	 */
	private void showInfo_call(final String phone) {

		// FIXME　此处需要修改
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.cancel_cancel, null);
		final Dialog dialog = new Dialog(ClientsActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		TextView textView = (TextView) layout.findViewById(R.id.type_name);
		textView.setText(phone + "");
		/**
		 * 发短信
		 */
		layout.findViewById(R.id.rl_message).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Uri smsToUri = Uri.parse("smsto:" + phone);

						Intent intent = new Intent(Intent.ACTION_SENDTO,
								smsToUri);

						intent.putExtra("sms_body", "");

						startActivity(intent);
					}
				});
		/**
		 * 打电话
		 */
		layout.findViewById(R.id.rl_phone).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						CallPhoneUtil.callPhone(ClientsActivity.this, phone);
						dialog.dismiss();
					}
				});
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	// 删除客户
	private void Delete_client(final int id) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.delete_client, null);
		final Dialog dialog = new Dialog(ClientsActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				// 确定 删除客户
				deleteClient(id);
			}

		});
		layout.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	private void deleteClient(int id) {
		clientId = id;
		if (!NetConn.checkNetwork(ClientsActivity.this)) {
		} else {
			if (isClosed) {
				proDialog.show();
				isClosed = false;
			}
			new DeleteAsync().execute();
		}
	}

	private class DeleteAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ClientService clientService = new ClientService(
					ClientsActivity.this);
			Message message = handler.obtainMessage(CLIENT_DELETE);
			String res = clientService.deleteClientList(shopsid, clientId);

			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				int flag = jo.optInt("res");
				String error = jo.getString("msg");

				message.arg1 = flag;
				message.obj = error;
				message.arg2 = CURRENT_OPT;
				handler.sendMessage(message);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void getList(boolean data) {
		data1 = data;
		if (!NetConn.checkNetwork(ClientsActivity.this)) {
		} else {
			if (isClosed) {
				proDialog.show();
				isClosed = false;
			}
			new MyAsync().execute();
		}
	}

	private class MyAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			ClientService clientService = new ClientService(
					ClientsActivity.this);
			Message message = handler.obtainMessage(CURRENT_OPT);
			String res = clientService.getClientList(shopsid, user_type,
					pageIndex, keyword, order);
			Client client = JsonUtils.fromJson(res, Client.class);
			message.obj = client;
			if (data1) {
				message.arg1 = 0;
			} else {
				message.arg1 = 1;
			}
			handler.sendMessage(message);
			return null;
		}
	}

	@Override
	public void all_business(boolean isRefresh, boolean isHeadRefresh) {
		CURRENT_OPT = ALL_Client;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter1.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

	// @Override
	// public void have_business(boolean isRefresh, boolean isHeadRefresh) {
	// // TODO Auto-generated method stub
	// CURRENT_OPT = HAVE_BUSINESS;
	// if (isHeadRefresh) {// 刷新
	// pageIndex = 0;
	// getList(true);
	// adapter2.removeAllDatas();
	// } else {// 加载
	// getList(false);
	// }
	// pageIndex++;
	// }

	@Override
	public void no_business(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = NO_BUSINESS;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter3.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

	@Override
	public void time_sort(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = TIME_SORT;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter6.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

	@Override
	public void name_sort(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = NAME_SORT;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter7.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

	@Override
	public void money_high_to_low(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = MONEY_HIGH_TO_DOWN;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter4.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

	@Override
	public void order_high_to_low(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		CURRENT_OPT = ORDER_HIGH_TO_DOWN;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter5.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ALL_Client) {
				type = ALL_Client;
				all_business(false, true);
			} else if (requestCode == TAKE_OUT) {
				type = TAKE_OUT;
				take_out(false, true);
			} else if (requestCode == NO_BUSINESS) {
				type = NO_BUSINESS;
				no_business(false, true);
			} else if (requestCode == TIME_SORT) {
				type = TIME_SORT;
				time_sort(false, true);
			} else if (requestCode == NAME_SORT) {
				type = NAME_SORT;
				name_sort(false, true);
			} else if (requestCode == MONEY_HIGH_TO_DOWN) {
				type = MONEY_HIGH_TO_DOWN;
				money_high_to_low(false, true);
			} else if (requestCode == ORDER_HIGH_TO_DOWN) {
				type = ORDER_HIGH_TO_DOWN;
				order_high_to_low(false, true);
			} else if (requestCode == STORE_PAY_SORT) {
				type = STORE_PAY_SORT;
				store_pay_sort(false, true);
			}
		}
	}

	@Override
	public void take_out(boolean isRefresh, boolean isHeadRefresh) {
		CURRENT_OPT = TAKE_OUT;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter2.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

	@Override
	public void store_pay_sort(boolean isRefresh, boolean isHeadRefresh) {
		CURRENT_OPT = STORE_PAY_SORT;
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			getList(true);
			adapter8.removeAllDatas();
		} else {// 加载
			getList(false);
		}
		pageIndex++;
	}

}
