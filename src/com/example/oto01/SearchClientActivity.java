package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.ClientsActivity.ViewHolder;
import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.listener.CLientSearchListenerBig;
import com.example.oto01.listener.CLientSearchListenerBig.CLientSearchListener;
import com.example.oto01.model.Client;
import com.example.oto01.model.Client.clientList;
import com.example.oto01.model.Login;
import com.example.oto01.services.ClientService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.CallPhoneUtil;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.PullListViewUtils;
import com.example.oto01.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 搜索功能
 * 
 * @author Administrator
 * 
 */
public class SearchClientActivity extends BaseActivity implements
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
	private ListView listView1;
	private List<Client> clientList = new ArrayList<Client>();
	private ClientAdapter adapter;
	Boolean data1;
	private Dialog proDialog;
	private int clientId;
	private static int CLIENT_DELETE = 108;
	private int shopsid = 1;
	private int pageIndex;
	private String keyword = "";
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == CLIENT_DELETE) {
				// proDialog.dismiss();
				String error = (String) msg.obj;
				ToastUtil.show(getApplicationContext(), error);
			} else if (msg.what == 0) {
				getCurrentOrderList(msg);
			}

		}

	};
	private int i = 0;
	private int y = 0;

	private void getCurrentOrderList(Message msg) {
		Client client = (Client) msg.obj;

		if (client.getRes().equals("0")) {
			List<clientList> gds = client.getData();
			if (gds.size() == 0 && client.getP() < client.getTotalnum()) {
				adapter.removeAllDatas();
				adapter.notifyDataSetChanged();
				// ToastUtil.show(getApplicationContext(), "没有数据");
				pullToRefreshListView1.onRefreshComplete();
				return;
			}
			if (msg.arg1 == 1) {
				if (client.getP() >= client.getTotalnum()) {
					ToastUtil.show(this, "已经到底部了");
					pullToRefreshListView1.onRefreshComplete();
					return;
				}
			} else {
				adapter.addAllDatas(gds);
				pullToRefreshListView1.onRefreshComplete();
			}

		} else {
			ToastUtil.show(getApplicationContext(), client.getMsg());
			pullToRefreshListView1.onRefreshComplete();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_search);
		ViewUtils.inject(this);

		LoginManager lm = LoginManager.getInstance(SearchClientActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		iv_back.setVisibility(View.VISIBLE);
		title_font.setVisibility(View.GONE);
		school_friend.setVisibility(View.VISIBLE);
		school_friend.setHint("请输入姓名/昵称或手机号");
		adapter = new ClientAdapter();
		pullToRefreshListView1 = PullListViewUtils
				.setRefreshBothMode(pullToRefreshListView1);
		listView1 = pullToRefreshListView1.getRefreshableView();
		pullToRefreshListView1
				.setOnRefreshListener(new CLientSearchListenerBig(
						CLientSearchListenerBig.CLIENT_SEARCH, this));
		listView1.setAdapter(adapter);
		school_friend.addTextChangedListener(this);
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		Intent intent = new Intent();
		SearchClientActivity.this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onClick(View v) {
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
				cv = LayoutInflater.from(SearchClientActivity.this).inflate(
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
						// intent.putExtra("type", CURRENT_OPT);
						intent.setClass(getApplicationContext(),
								ClientDetailsActivity.class);
						startActivity(intent);
						// startActivityForResult(intent, CURRENT_OPT);
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
					CallPhoneUtil.callPhone(SearchClientActivity.this,
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

//			holder.ll_up.setOnLongClickListener(new OnLongClickListener() {
//
//				@Override
//				public boolean onLongClick(View v) {
//					Delete_client(list.getId());
//					return true;
//				}
//			});
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
		final Dialog dialog = new Dialog(SearchClientActivity.this,
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
						CallPhoneUtil.callPhone(SearchClientActivity.this,
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

	// 删除客户
	private void Delete_client(final int id) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.delete_client, null);
		final Dialog dialog = new Dialog(SearchClientActivity.this,
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
		if (!NetConn.checkNetwork(SearchClientActivity.this)) {
		} else {
			// proDialog.show();
			new DeleteAsync().execute();
		}
	}

	private class DeleteAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ClientService clientService = new ClientService(
					SearchClientActivity.this);
			Message message = handler.obtainMessage(CLIENT_DELETE);
			String res = clientService.deleteClientList(shopsid, clientId);

			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				int flag = jo.optInt("res");
				String error = jo.getString("msg");

				message.arg1 = flag;
				message.obj = error;
				handler.sendMessage(message);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class MyAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			ClientService clientService = new ClientService(
					SearchClientActivity.this);
			Message message = handler.obtainMessage(0);
			// message.arg1 = CURRENT_OPT_STATUS;
			// int shopsid, int is_trade, int p,String keyword, int order
			String res = clientService.getClientList(shopsid, 0, pageIndex,
					keyword, 0);

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

	@SuppressLint("NewApi")
	@Override
	public void search(boolean isRefresh, boolean isHeadRefresh) {
		// TODO Auto-generated method stub
		if (isHeadRefresh) {// 刷新
			pageIndex = 0;
			if (keyword.isEmpty()) {

			} else {
				getData(true);
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
	private void getData(boolean b) {
		data1 = b;
		if (!NetConn.checkNetwork(SearchClientActivity.this)) {
		} else {
			new MyAsync().execute();
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

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		keyword = s.toString().trim();
		i = count;
		if (keyword.equals("")) {
			adapter.removeAllDatas();
			adapter.notifyDataSetChanged();
		} else {

			if (i != y) {
				adapter.removeAllDatas();
				adapter.notifyDataSetChanged();
			}
			getData(true);
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
