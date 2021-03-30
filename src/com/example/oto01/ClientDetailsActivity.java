package com.example.oto01;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.adapter.ClientDetailsTakeDaoDianAdapter;
import com.example.oto01.adapter.ClientDetailsTakeWaiMaiAdapter;
import com.example.oto01.model.ClientDetails;
import com.example.oto01.model.ClientDetails.Data;
import com.example.oto01.model.Login;
import com.example.oto01.services.ClientService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.OrderService;
import com.example.oto01.utils.CallPhoneUtil;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.views.PullRefreshListView;
import com.example.oto01.views.PullRefreshListView.PullRefreshListener;
import com.example.oto01.views.PullRefreshListView.TitleListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ClientDetailsActivity extends BaseActivity implements
		PullRefreshListener, TitleListener {

	private PullRefreshListView waimai;
	private PullRefreshListView daodian;

	private TextView tv_nickname;
	private TextView tv_total_price;
	private TextView tv_total_order;
	private TextView tv_phone;
	private LinearLayout iv_name;
	@ViewInject(R.id.title_font)
	private TextView title_font;

	int j = 0;
	public static int shopsid = 1;
	private static int REMARK_NAME = 101;
	private static int CLIENT_LIST = 102;
	public static final int REQUEST_SHOP_PAY_DETAILS = 103;
	private static final int SHOP_CODE = 104;
	private Dialog proDialog;
	// private Boolean data;
	private int pageIndex = 1;
	private int id;
	private String type = "1";
	private List<Data> dataBeanList;
	private List<Data> storeBeanList;
	private List<Data> memberBeanList;
	public static int searchLayoutTop;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == REMARK_NAME) {
				proDialog.dismiss();

				if (msg.arg1 == 0) {

					if (msg.arg2 == 1
							&& tv_nickname.getText().toString().trim()
									.equals("")) {
						String name = et_name.getText().toString().trim();
						tv_name.setText(name + "");
						tv_nickname.setText("("
								+ getIntent().getStringExtra("name") + ")");
					} else {
						String name = et_name.getText().toString().trim();
						tv_name.setText(name + "");
					}

				}
				String error = (String) msg.obj;
				// 修改备注
				ToastUtil.show(getApplicationContext(), error);

			} else if (msg.what == CLIENT_LIST) {
				if (type.equals("1")) {
					// 外卖
					getHistoryOrderList(msg);
				} else if (type.equals("2")) {
					// 到店支付
					getShopPayList(msg);
				} else if (type.equals("3")) {
					//  会员卡支付
					// getMemberCardList(msg);
				}

			} else if (msg.what == SHOP_CODE) {
				// 到店支付验证
				String res = (String) msg.obj;
				JSONObject jo;
				try {
					jo = new JSONObject(res);
					// shop_pay(false, true);
					ToastUtil
							.show(getApplicationContext(), jo.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		};
	};
	private EditText et_name;

	protected void getHistoryOrderList(Message msg) {
		proDialog.dismiss();
		ClientDetails client = (ClientDetails) msg.obj;
		if (dataBeanList == null) {
			dataBeanList = new ArrayList<Data>();
		}
		waimai.onRefreshComplete(new Date());
		waimai.onLoadMoreComplete();
		if (client.getRes() == 0) {
			System.out.println("pageIndex=" + pageIndex);
			if (pageIndex == 2) {
				waiMaiAdapter.getData().clear();
				waiMaiAdapter.notifyDataSetChanged();
				// if(waiMaiAdapter!=null){
				// waiMaiAdapter=null;
				// }
			}
			tv_on_the_way.setText("外卖/上门(" + client.countnum + "单)");
			tv_store_pay.setText("到店支付(" + client.pay_countnum + "单)");
			tv_on_the_way1.setText("外卖/上门(" + client.countnum + "单)");
			tv_store_pay1.setText("到店支付(" + client.pay_countnum + "单)");
			// dataBeanList.addAll(client.getData());
			for (int i = 0; i < client.getData().size(); i++) {
				client.getData().get(i).setType(0);
			}
			// takeOutListView.setData(dataBeanList,
			// (pageIndex > client.getTotalnum()));
			if (pageIndex > client.getTotalnum()) {
				waimai.setCanLoadMore(false);
			} else {
				waimai.setCanLoadMore(true);
			}
			waiMaiAdapter.getData().addAll(client.getData());
			// if (dataBeanList.size() == 0) {
			// // hideTitle();
			// }
			// pull_refresh_scrollview.onRefreshComplete();
		} else {
			ToastUtil.show(getApplicationContext(), client.msg);
		}

	}

	protected void getShopPayList(Message msg) {
		proDialog.dismiss();
		ClientDetails client = (ClientDetails) msg.obj;
		if (storeBeanList == null) {
			storeBeanList = new ArrayList<Data>();
		}
		daodian.onRefreshComplete(new Date());
		daodian.onLoadMoreComplete();
		if (client.getRes() == 0) {

			if (pageIndex == 2) {
				daoDianAdapter.getData().clear();
				daoDianAdapter.notifyDataSetChanged();

			}
			for (int i = 0; i < client.getData().size(); i++) {
				client.getData().get(i).setType(1);
			}
			if (pageIndex > client.getTotalnum()) {
				daodian.setCanLoadMore(false);
			} else {
				daodian.setCanLoadMore(true);
			}
			daoDianAdapter.getData().addAll(client.getData());
			if (storeBeanList.size() == 0) {
			}
		} else {
			ToastUtil.show(getApplicationContext(), client.msg);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_details_content);
		ViewUtils.inject(this);
		initView();
	}

	/**
	 * 打电话
	 * 
	 * @param view
	 */
	public void call_onClick(View view) {

		if (getIntent().getStringExtra("iscall").equals("1")) {
			showInfo_call(getIntent().getStringExtra("phone"));
		}

	}

	/**
	 * 显示提醒取消打电话对话框
	 */
	private void showInfo_call(final String phone) {
		// FIXME　此处需要修改
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.cancel_cancel, null);
		final Dialog dialog = new Dialog(ClientDetailsActivity.this,
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
						CallPhoneUtil.callPhone(ClientDetailsActivity.this,
								phone);
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

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		Intent intent = new Intent();
		ClientDetailsActivity.this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			ClientDetailsActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	@SuppressLint("NewApi")
	private void initView() {
		LoginManager lm = LoginManager.getInstance(ClientDetailsActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}

		ll_title12 = (LinearLayout) findViewById(R.id.ll_title12);
		rl_on_the_way1 = (RelativeLayout) findViewById(R.id.rl_on_the_way1);
		tv_on_the_way1 = (TextView) findViewById(R.id.tv_on_the_way1);
		tv_store_pay1 = (TextView) findViewById(R.id.tv_store_pay1);

		headView1 = LayoutInflater.from(this).inflate(
				R.layout.activity_client_details_header, null);
		headView2 = LayoutInflater.from(this).inflate(
				R.layout.activity_client_details_header_two, null);

		tv_name = (TextView) headView1.findViewById(R.id.tv_name);
		tv_nickname = (TextView) headView1.findViewById(R.id.tv_nickname);
		ll_header1 = (LinearLayout) headView1.findViewById(R.id.ll_header1);
		tv_total_price = (TextView) headView1.findViewById(R.id.tv_total_price);
		tv_total_order = (TextView) headView1.findViewById(R.id.tv_total_order);
		tv_phone = (TextView) headView1.findViewById(R.id.tv_phone);
		iv_name = (LinearLayout) headView1.findViewById(R.id.iv_name);

		rl_on_the_way = (RelativeLayout) headView2
				.findViewById(R.id.rl_on_the_way);
		tv_on_the_way = (TextView) headView2.findViewById(R.id.tv_on_the_way);
		tv_store_pay = (TextView) headView2.findViewById(R.id.tv_store_pay);

		waimai = (PullRefreshListView) findViewById(R.id.waimai);
		daodian = (PullRefreshListView) findViewById(R.id.daodian);

		waimai.addHeaderView(headView1);
		waimai.addHeaderView(headView2);

		daodian.addHeaderView(headView1);
		daodian.addHeaderView(headView2);

		title_font.setText("客户详情");
		id = getIntent().getIntExtra("id", 0);
		tv_name.setText(getIntent().getStringExtra("name"));

		waiMaiAdapter = new ClientDetailsTakeWaiMaiAdapter(this, shopsid);
		daoDianAdapter = new ClientDetailsTakeDaoDianAdapter(this, shopsid);

		waimai.setAdapter(waiMaiAdapter);
		daodian.setAdapter(daoDianAdapter);

		waimai.setTitleListener(this);
		waimai.setPullRefreshListener(this);
		waimai.setCanRefresh(true);
		waimai.setCanLoadMore(false);
		daodian.setTitleListener(this);
		daodian.setPullRefreshListener(this);
		daodian.setCanRefresh(true);
		daodian.setCanLoadMore(false);

		if (getIntent().getStringExtra("remark_name").isEmpty()) {
		} else {
			tv_nickname.setText("(" + getIntent().getStringExtra("remark_name")
					+ ")");
		}
		tv_total_price.setText("￥" + getIntent().getStringExtra("total_money"));
		tv_total_order
				.setText(getIntent().getStringExtra("total_orders") + "单");
		tv_phone.setText(getIntent().getStringExtra("phone"));
		// 修改备注
		iv_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rename();
			}
		});
		// // 外卖上门
		rl_on_the_way.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "1";
				pageIndex = 1;
				waimai.setVisibility(View.VISIBLE);
				daodian.setVisibility(View.GONE);
				// member.setVisibility(View.GONE);
				rl_on_the_way.setBackgroundResource(R.drawable.select_bg);
				tv_on_the_way.setTextColor(Color.parseColor("#ee5048"));
				tv_store_pay.setBackgroundResource(R.drawable.no_select_bg);
				tv_store_pay.setTextColor(Color.parseColor("#626a7d"));
				rl_on_the_way1.setBackgroundResource(R.drawable.select_bg);
				tv_on_the_way1.setTextColor(Color.parseColor("#ee5048"));
				tv_store_pay1.setBackgroundResource(R.drawable.no_select_bg);
				tv_store_pay1.setTextColor(Color.parseColor("#626a7d"));
				getData();
			}
		});
		// // 到店支付
		tv_store_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "2";
				pageIndex = 1;
				waimai.setVisibility(View.GONE);
				daodian.setVisibility(View.VISIBLE);
				// member.setVisibility(View.GONE);
				rl_on_the_way.setBackgroundResource(R.drawable.no_select_bg);
				tv_on_the_way.setTextColor(Color.parseColor("#626a7d"));
				tv_store_pay.setBackgroundResource(R.drawable.select_bg);
				tv_store_pay.setTextColor(Color.parseColor("#ee5048"));
				rl_on_the_way1.setBackgroundResource(R.drawable.no_select_bg);
				tv_on_the_way1.setTextColor(Color.parseColor("#626a7d"));
				tv_store_pay1.setBackgroundResource(R.drawable.select_bg);
				tv_store_pay1.setTextColor(Color.parseColor("#ee5048"));
				getData();
			}
		});

		rl_on_the_way1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "1";
				pageIndex = 1;
				waimai.setVisibility(View.VISIBLE);
				// member.setVisibility(View.GONE);
				daodian.setVisibility(View.GONE);
				rl_on_the_way.setBackgroundResource(R.drawable.select_bg);
				tv_on_the_way.setTextColor(Color.parseColor("#ee5048"));
				tv_store_pay.setBackgroundResource(R.drawable.no_select_bg);
				tv_store_pay.setTextColor(Color.parseColor("#626a7d"));
				rl_on_the_way1.setBackgroundResource(R.drawable.select_bg);
				tv_on_the_way1.setTextColor(Color.parseColor("#ee5048"));
				tv_store_pay1.setBackgroundResource(R.drawable.no_select_bg);
				tv_store_pay1.setTextColor(Color.parseColor("#626a7d"));
				getData();
			}
		});
		// // 到店支付
		tv_store_pay1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "2";
				pageIndex = 1;
				waimai.setVisibility(View.GONE);
				daodian.setVisibility(View.VISIBLE);
				// member.setVisibility(View.GONE);
				rl_on_the_way.setBackgroundResource(R.drawable.no_select_bg);
				tv_on_the_way.setTextColor(Color.parseColor("#626a7d"));
				tv_store_pay.setBackgroundResource(R.drawable.select_bg);
				tv_store_pay.setTextColor(Color.parseColor("#ee5048"));
				rl_on_the_way1.setBackgroundResource(R.drawable.no_select_bg);
				tv_on_the_way1.setTextColor(Color.parseColor("#626a7d"));
				tv_store_pay1.setBackgroundResource(R.drawable.select_bg);
				tv_store_pay1.setTextColor(Color.parseColor("#ee5048"));
				getData();
			}
		});
		type = "1";
		pageIndex = 1;
		getData();

	}

	protected void rename() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_input_name, null);
		final Dialog dialog = new Dialog(ClientDetailsActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		et_name = (EditText) layout.findViewById(R.id.et_name);
		et_name.setHint("请输入六个字以内");
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				if (!NetConn.checkNetwork(ClientDetailsActivity.this)) {
					// NetConn.setNetwork(OrdersActivity.this);
				} else {
					showDialog();
				}
				new RenameAsync().execute();
			}

		});
		layout.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
		dialog.show();

	}

	/**
	 * 获取数据
	 */
	private void getData() {
		if (!NetConn.checkNetwork(ClientDetailsActivity.this)) {
		} else {
			showDialog();
		}
		new MyAsync().execute();
	}

	private class MyAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			ClientService clientService = new ClientService(
					ClientDetailsActivity.this);
			Message message = handler.obtainMessage(CLIENT_LIST);
			if (type.equals("1")) {
				// 外卖
				String res = clientService.getClientDetailsList(shopsid, id,
						pageIndex, 1);
				ClientDetails client = JsonUtils.fromJson(res,
						ClientDetails.class);
				try {
					if (client.p <= client.getTotalnum()) {
						pageIndex++;
					}
					message.obj = client;
					handler.sendMessage(message);
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (type.equals("2")) {
				// 到店支付
				String res = clientService.getClientDetailsList(shopsid, id,
						pageIndex, 2);
				ClientDetails client = JsonUtils.fromJson(res,
						ClientDetails.class);
				try {
					if (client.p <= client.getTotalnum()) {
						pageIndex++;
					}
					message.obj = client;
					handler.sendMessage(message);
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (type.equals("3")) {
				// 会员卡支付
				String res = clientService.getClientDetailsList(shopsid, id,
						pageIndex, 3);
				ClientDetails client = JsonUtils.fromJson(res,
						ClientDetails.class);
				try {
					if (client.p <= client.getTotalnum()) {
						pageIndex++;
					}
					message.obj = client;
					handler.sendMessage(message);
				} catch (Exception e) {
				}
			}

			return null;
		}
	}

	private class RenameAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			ClientService clientService = new ClientService(
					ClientDetailsActivity.this);
			Message message = handler.obtainMessage(REMARK_NAME);
			// message.arg1 = CURRENT_OPT_STATUS;
			String res = clientService.RemarkNameClient(shopsid, id, et_name
					.getText().toString().trim());
			JSONObject jo = null;
			try {

				jo = new JSONObject(res);
				int flag = jo.optInt("res");
				String error = jo.getString("msg");
				j++;
				message.arg1 = flag;
				message.arg2 = j;
				message.obj = error;
				handler.sendMessage(message);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private String code1;
	private View headView1;
	private View headView2;
	private TextView tv_name;
	private ClientDetailsTakeWaiMaiAdapter waiMaiAdapter;
	private ClientDetailsTakeDaoDianAdapter daoDianAdapter;
	private RelativeLayout rl_on_the_way;
	private TextView tv_store_pay;
	private TextView tv_on_the_way;
	private LinearLayout ll_title12;
	private LinearLayout ll_header1;
	private RelativeLayout rl_on_the_way1;
	private TextView tv_on_the_way1;
	private TextView tv_store_pay1;

	protected void yanzheng(String code) {
		code1 = code;
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ClientDetailsActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否设为已验证？");
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!NetConn.checkNetwork(ClientDetailsActivity.this)) {
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
					ClientDetailsActivity.this);
			Message message = handler.obtainMessage(SHOP_CODE);
			String res = orderService.getShopCode(shopsid, code1);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			if (requestCode == REQUEST_SHOP_PAY_DETAILS) {
				// 到店支付订单
				// shop_pay(false, true);
			}
			// else if (requestCode == REQUEST_FINISH) {
			// // 已完成订单
			// HAVE_SEND(false, true);
			// }

		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pageIndex = 1;
		getData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getData();
	}

	@Override
	public void onCannotLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Visiable() {
		// TODO Auto-generated method stub
		ll_title12.setVisibility(View.VISIBLE);
	}

	@Override
	public void Gone() {
		// TODO Auto-generated method stub
		ll_title12.setVisibility(View.GONE);

	}

}
