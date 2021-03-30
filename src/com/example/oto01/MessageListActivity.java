package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.oto01.db.MessageDBService;
import com.example.oto01.model.Login;
import com.example.oto01.model.MessageList;
import com.example.oto01.model.Messages;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.MessageService;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.views.PullRefreshView;
import com.example.oto01.views.PullRefreshView.OnFooterRefreshListener;
import com.example.oto01.views.PullRefreshView.OnHeaderRefreshListener;

/**
 * 消息列表
 * 
 * @author chenyamei
 * 
 */
public class MessageListActivity extends BaseActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private static final int HEADER = 10;// 刷新
	private static final int NEWEST = 1;// 首次刷新
	private static final int FOOTER = 12;// 加载

	private ListView listView;// 消息列表

	private MsgAdapter adapter2;// 消息列表的adpater
	private int current_page = 1;// 当前页数的初始化
	private int total_page = 0;// 总页数的初始化
	private int shopsid = 1;// 商店id
	private Double geoLat = MainActivity.geoLat;// 经度
	private Double geoLng = MainActivity.geoLng;// 纬度
	private Dialog proDialog;//
	private PullRefreshView pullRefreshView;// 官方消息的刷新列表
	private TextView tv_cancle;// 一键已读
	private TextView title_font;// 消息中心的文字
	private LinearLayout iv_no;// 没有数据的布局

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			default:
				List<Messages> list = (List<Messages>) msg.obj;
				statusFilter(list, msg.what, msg);
				break;
			}
		}
	};

	/**
	 * 通过数据类型展示数据
	 * 
	 * @param newest
	 * @param type
	 * @param msg
	 */
	private void statusFilter(List<Messages> newest, int type, Message msg) {

		if (newest != null && !newest.isEmpty()) {
			saveData(newest);
			newest = searchAllData();
			if (type == HEADER || type == NEWEST) {
				try {
					adapter2.removeAllDatas();
				} catch (Exception e) {
				}
				adapter2.addAllDatas(searchAllData());
				adapter2.notifyDataSetChanged();
			} else if (type == FOOTER) {
				int over = msg.getData().getInt("over", -1);
				if (over == 0) {
					ToastUtil.show(MessageListActivity.this, "已经到底部了");
				} else if (over == -1) {
					adapter2.addAllDatas(searchAllData());
					adapter2.notifyDataSetChanged();
					System.out.println("-----加载新数据----" + newest);
				}
			}

			adapter2.notifyDataSetChanged();
			if (type == HEADER) {
				pullRefreshView.onHeaderRefreshComplete();
			} else if (type == FOOTER) {
				pullRefreshView.onFooterRefreshComplete();
			}
		} else {

			// 请求到的数据为空，直接是数据库中的东西
			if (searchAllData().size() == 0) {
				iv_no.setVisibility(View.VISIBLE);
				try {
					adapter2.removeAllDatas();
				} catch (Exception e) {
				}
				adapter2.addAllDatas(searchAllData());
				adapter2.notifyDataSetChanged();
			} else {
				iv_no.setVisibility(View.GONE);
				// 官方消息
				listView.setAdapter(adapter2);
				try {
					adapter2.removeAllDatas();
				} catch (Exception e) {
				}
				adapter2.addAllDatas(searchAllData());
				adapter2.notifyDataSetChanged();

			}

			if (type == HEADER) {
				pullRefreshView.onHeaderRefreshComplete();
			} else if (type == FOOTER) {
				pullRefreshView.onFooterRefreshComplete();
			}

		}
		proDialog.dismiss();
	}

	// /**
	// * 通过数据类型展示数据
	// *
	// * @param newest
	// * 承载消息数据的列表
	// * @param type
	// * 类型
	// * @param msg
	// * message的数据
	// */
	// private void statusFilter(int type) {
	//
	// if (searchAllData().size() == 0) {
	// iv_no.setVisibility(View.VISIBLE);
	// try {
	// adapter2.removeAllDatas();
	// } catch (Exception e) {
	// }
	// adapter2.addAllDatas(searchAllData());
	// adapter2.notifyDataSetChanged();
	// } else {
	// iv_no.setVisibility(View.GONE);
	// // 官方消息
	// listView.setAdapter(adapter2);
	// // if (newest == null || newest.size() == 0) {
	// // try {
	// // adapter2.removeAllDatas();
	// // } catch (Exception e) {
	// // }
	// // adapter2.addAllDatas(searchAllData());
	// // }
	// // if (newest != null && !newest.isEmpty()) {
	// // saveData(newest);
	// // newest = searchAllData();
	// if (type == HEADER || type == NEWEST) {
	// try {
	// adapter2.removeAllDatas();
	// } catch (Exception e) {
	// }
	// adapter2.addAllDatas(searchAllData());
	// adapter2.notifyDataSetChanged();
	// } else if (type == FOOTER) {
	// adapter2.addAllDatas(searchAllData());
	// adapter2.notifyDataSetChanged();
	// }
	// if (type == HEADER) {
	// pullRefreshView.onHeaderRefreshComplete();
	// } else if (type == FOOTER) {
	// pullRefreshView.onFooterRefreshComplete();
	// } else {
	// pullRefreshView.onHeaderRefreshComplete();
	// pullRefreshView.onFooterRefreshComplete();
	// adapter2.notifyDataSetChanged();
	// }
	// }
	//
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		setContentView(R.layout.activity_message_list);
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		LoginManager lm = LoginManager.getInstance(MessageListActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		tv_cancle = (TextView) findViewById(R.id.tv_cancle);
		title_font = (TextView) findViewById(R.id.title_font);
		listView = (ListView) findViewById(R.id.list2);
		iv_no = (LinearLayout) findViewById(R.id.iv_no);
		pullRefreshView = (PullRefreshView) findViewById(R.id.pull_refresh_view);
		pullRefreshView.setOnHeaderRefreshListener(this);
		pullRefreshView.setOnFooterRefreshListener(this);
		tv_cancle.setVisibility(View.VISIBLE);
		tv_cancle.setText("一键已读");
		title_font.setText("消息中心");
		adapter2 = new MsgAdapter();
		listView.setAdapter(adapter2);
		tv_cancle.setOnClickListener(this);

	}

	/**
	 * 毁掉这个类
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 恢复执行
	 */
	@Override
	public void onResume() {
		super.onResume();
		initDatas(NEWEST);
	}

	/**
	 * 停止
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 初始化数据
	 * 
	 * @param position
	 *            位置
	 */
	private void initDatas(final int position) {
		if (!NetConn.checkNetwork(MessageListActivity.this)) {
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					getCurMsg(position);
				}
			}).start();
		}
	}

	/**
	 * 获取当前消息
	 * 
	 * @param position
	 */
	private void getCurMsg(int position) {
		MessageService messageService = new MessageService(
				MessageListActivity.this);
		Message msg = handler.obtainMessage(position);
		Bundle bundle = new Bundle();
		MessageList list = new MessageList();
		System.out.println("---current_page->" + current_page
				+ "--total_page-->" + total_page);
		if (position == HEADER || position == NEWEST) {
			String res = messageService.getMessageList(shopsid, 2, 1, geoLng,
					geoLat);
			list = JsonUtils.fromJson(res, MessageList.class);

		} else if (position == FOOTER) {
			if (current_page >= total_page) {
				bundle.putInt("over", 0);
				current_page = total_page;
			} else if (current_page < total_page) {
				current_page++;
			}
			String resString = messageService.getMessageList(shopsid, 2,
					current_page, geoLng, geoLat);
			list = JsonUtils.fromJson(resString, MessageList.class);

		}
		System.out.println("----list----->" + list);
		current_page = list.nowp;
		total_page = list.total;
		System.out.println("---current_page->" + current_page
				+ "--total_page-->" + total_page);
		msg.obj = list.data;
		msg.setData(bundle);
		msg.sendToTarget();
	}

	private class ViewHolder2 {
		TextView title;
		TextView msg;
		TextView time;
		ImageView iv_dian;
		ImageView icon;
	}

	/**
	 * 消息列表的适配器
	 * 
	 * @author lqq
	 */
	private class MsgAdapter extends BaseAdapter {

		List<Messages> messages = new ArrayList<Messages>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<Messages> list) {
			messages.addAll(list);
			notifyDataSetChanged();
		}

		/**
		 * 移除全部数据
		 * 
		 * @param position
		 */
		public void removeAllDatas() {
			messages.clear();
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View cv, ViewGroup parent) {
			ViewHolder2 holder;
			if (cv == null) {
				cv = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.msg_list, parent, false);
				holder = new ViewHolder2();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder2) cv.getTag();
			}

			if (messages.get(position).getInfortype().equals("2")) {
				holder.icon.setImageResource(R.drawable.system_message);
			} else if (messages.get(position).getInfortype().equals("3")) {
				holder.icon.setImageResource(R.drawable.gonglv);
			}
			holder.title.setText(messages.get(position).getTitle());
			holder.msg.setText(messages.get(position).getIntroduction());
			holder.time.setText(DateUtil.getFormatedDate_7(messages.get(
					position).getAddtime()));
			if (messages.get(position).getIs_readmessage().equals("1")) {
				// 未读状态
				holder.iv_dian.setVisibility(View.VISIBLE);
			} else {
				// 已读状态
				holder.iv_dian.setVisibility(View.INVISIBLE);
			}
			// 点击进入消息详情界面
			cv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					updateDataById(messages.get(position).getId() + "", "2");
					Intent in = new Intent(getApplicationContext(),
							MessageDetailsActivity.class);
					in.putExtra("msg_id", messages.get(position).getId());
					in.putExtra("msg_num", messages.size());
					startActivity(in);
				}
			});
			// 消息删除
			cv.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					delete(messages, messages.get(position).getId() + "");
					adapter2.notifyDataSetChanged();
					return false;
				}
			});

			return cv;
		}

		@Override
		public int getCount() {
			return messages.size();
		}

		@Override
		public Object getItem(int position) {
			return messages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	private void delete(final List<Messages> list, final String id) {
		// 提示是否要删除该商品分类
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				MessageListActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否要删除这条消息?");
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateDataById(id, "2");
						removeData(list, id);
						initDatas(HEADER);
					}
				});
		dialog.setPositiveButton("取消", null);
		dialog.show();

	}

	/**
	 * 查找视图控件
	 * 
	 * @param holder
	 * @param cv
	 */
	private void findViews(final ViewHolder2 holder, View cv) {
		holder.title = (TextView) cv.findViewById(R.id.title);
		holder.msg = (TextView) cv.findViewById(R.id.msg);
		holder.time = (TextView) cv.findViewById(R.id.time);
		holder.iv_dian = (ImageView) cv.findViewById(R.id.iv_dian);
		holder.icon = (ImageView) cv.findViewById(R.id.icon);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_cancle:
			// 一键已读
			if (searchAllData().size() != 0) {
				updateData(searchAllData(), "2");
				initDatas(HEADER);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 加载
	 */
	@Override
	public void onFooterRefresh(PullRefreshView view) {
		initDatas(FOOTER);
	}

	/**
	 * 刷新
	 */
	@Override
	public void onHeaderRefresh(PullRefreshView view) {
		initDatas(HEADER);
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

}