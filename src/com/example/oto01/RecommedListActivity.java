package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oto01.model.RecommendUser;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.views.PullRefreshView;
import com.example.oto01.views.PullRefreshView.OnFooterRefreshListener;
import com.example.oto01.views.PullRefreshView.OnHeaderRefreshListener;

/**
 * 推荐列表Activity
 * 
 * @author lqq
 * 
 */
public class RecommedListActivity extends ListActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private static final int HEADER = 0;
	private static final int NEWEST = 1;
	private static final int FOOTER = 2;
	private static final int GET_RULE = 3;
	private PullRefreshView pullRefreshView;
	private RecommendAdapter recommendAdapter;
	private Dialog proDialog;
	private List<RecommendUser> recommendUsers = new ArrayList<RecommendUser>();
	private TextView titleTextView, ruleTextView;
	private LinearLayout linearLayout1, linearLayout2;
	private int current_page = 1;
	private int total_page = 0;
	private int tag;// 2.商家 1.用户
	private String recode;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == GET_RULE) {
				String content = (String) msg.obj;
				System.out.println("-------2----->" + content);
				if (content != "null" && content != null
						&& !content.equals("null") && !content.equals(null)) {
					/*
					 * if(content.get(0).equals("1")){
					 * ToastUtil.show(RecommedListActivity.this, "获取失败"); }else
					 * if(content.get(0).equals("2")){
					 * ToastUtil.show(RecommedListActivity.this, "暂无规则"); }else{
					 */
					ToastUtil.show(RecommedListActivity.this, content + "");
					// ruleTextView.setText(content+"");
					// }
				}
			} else {
				List<RecommendUser> list = (List<RecommendUser>) msg.obj;
				statusFilter(list, msg.what, msg);
			}
		}
	};

	/**
	 * 通过数据类型显示数据
	 * 
	 * @param newest
	 * @param type
	 * @param msg
	 */
	private void statusFilter(List<RecommendUser> newest, int type, Message msg) {
		if (newest != null && !newest.isEmpty()) {
			if (type == HEADER || type == NEWEST) {
				recommendUsers.clear();
				recommendUsers.addAll(newest);
			} else if (type == FOOTER) {
				int over = msg.getData().getInt("over", -1);
				if (over == 0) {
					ToastUtil.show(RecommedListActivity.this, "已经到底部了");
				} else if (over == -1) {
					recommendUsers.addAll(newest);
					System.out.println("-----加载新数据----" + newest);
				}
			}
			recommendAdapter.notifyDataSetChanged();
			if (type == HEADER) {
				pullRefreshView.onHeaderRefreshComplete();
			} else if (type == FOOTER) {
				pullRefreshView.onFooterRefreshComplete();
			}
		} else {
			recommendUsers.clear();
			recommendAdapter.notifyDataSetChanged();
			ToastUtil.show(this, "没有要显示的数据");
		}
		proDialog.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommed_list);
		initViews();
		if (!NetConn.checkNetwork(RecommedListActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			initRuleData();
		}
	}

	/**
	 * 初始化数据
	 * 
	 * @param position
	 */
	private void initDatas(final int position) {
		if (!NetConn.checkNetwork(RecommedListActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					getCurMsg(position);
				}
			}).start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// MobclickAgent.onResume(this);
		if (recode != null) {
			initDatas(NEWEST);
		}
	}

	/**
	 * 初始化规则数据
	 */
	private void initRuleData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SettingService service = new SettingService(
						RecommedListActivity.this);
				String content = service.getRule(tag == 1 ? 2 : 1);
				Message message = handler.obtainMessage(GET_RULE);
				System.out.println("------1--------->" + content);
				message.obj = content;
				handler.sendMessage(message);
			}
		}).start();
	}

	@Override
	public void onPause() {
		super.onPause();
		// MobclickAgent.onPause(this);
	}

	/**
	 * 初始化视图
	 */
	private void initViews() {
		tag = getIntent().getIntExtra("tag", -1);
		recode = getIntent().getStringExtra("recode");
		titleTextView = (TextView) findViewById(R.id.title_font);
		ruleTextView = (TextView) findViewById(R.id.info);
		linearLayout1 = (LinearLayout) findViewById(R.id.linear1);
		linearLayout2 = (LinearLayout) findViewById(R.id.linear2);
		if (tag == 2) {
			titleTextView.setText("推荐列表");
			linearLayout1.setBackgroundResource(R.drawable.no_select_bg);
			linearLayout2.setBackgroundResource(R.drawable.select_bg);
		} else if (tag == 1) {
			titleTextView.setText("推荐列表");
			linearLayout2.setBackgroundResource(R.drawable.no_select_bg);
			linearLayout1.setBackgroundResource(R.drawable.select_bg);
		}
		if (recode == null) {
			ToastUtil.show(RecommedListActivity.this, "对不起，您还未生成您的推荐码");
			return;
		} else {

			proDialog = new Dialog(RecommedListActivity.this,
					R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
			proDialog.show();
			pullRefreshView = (PullRefreshView) findViewById(R.id.pull_refresh_view);
			pullRefreshView.setOnHeaderRefreshListener(this);
			pullRefreshView.setOnFooterRefreshListener(this);
			recommendAdapter = new RecommendAdapter(this,
					R.layout.recommend_list_item, recommendUsers);
			setListAdapter(recommendAdapter);
		}

	}

	/**
	 * 得到当前推荐列表
	 * 
	 * @param position
	 */
	private void getCurMsg(int position) {
		SettingService settingService = new SettingService(
				RecommedListActivity.this);
		Message msg = handler.obtainMessage(position);
		Bundle bundle = new Bundle();
		List<RecommendUser> list = new ArrayList<RecommendUser>();
		System.out.println("---current_page->" + current_page
				+ "--total_page-->" + total_page);
		if (position == HEADER || position == NEWEST) {
			list = settingService.getRecommendUsers2(tag, recode, 1);
		} else if (position == FOOTER) {
			if (current_page >= total_page) {
				bundle.putInt("over", 0);
				current_page = total_page;
			} else if (current_page < total_page) {
				current_page++;
			}
			list = settingService.getRecommendUsers2(tag, recode, current_page);
		}
		System.out.println("----list----->" + list);
		current_page = settingService.p;
		total_page = settingService.total_page;
		System.out.println("---current_page->" + current_page
				+ "--total_page-->" + total_page);
		msg.obj = list;
		msg.setData(bundle);
		msg.sendToTarget();
	}

	/**
	 * 推荐列表的适配器
	 */
	private class RecommendAdapter extends ArrayAdapter<RecommendUser> {
		public RecommendAdapter(Context cx, int resId, List<RecommendUser> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				if (tag == 2) {
					cv = LayoutInflater.from(getContext()).inflate(
							R.layout.recommend_list_item, parent, false);
				} else if (tag == 1) {
					cv = LayoutInflater.from(getContext()).inflate(
							R.layout.recommend_list_item_user, parent, false);
				}
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, getItem(position), position);
			return cv;
		}
	}

	/**
	 * 查找视图控件
	 * 
	 * @param holder
	 * @param cv
	 */
	private void findViews(final ViewHolder holder, View cv) {
		holder.name = (TextView) cv.findViewById(R.id.name);
		holder.phone = (TextView) cv.findViewById(R.id.phone);
		// holder.ratingBar = (RatingBar)cv.findViewById(R.id.shopLevel);
		holder.time = (TextView) cv.findViewById(R.id.date);
		// holder.layout = (LinearLayout)cv.findViewById(R.id.shopLevel);
	}

	/**
	 * 刷新Item
	 * 
	 * @param holder
	 * @param recommendUser
	 * @param ps
	 */
	private void refreshItem(ViewHolder holder, RecommendUser recommendUser,
			int ps) {
		holder.time.setText(DateUtil.getFormatedDate_6(recommendUser
				.getAddtime()));
		// holder.name.setSelected(true);
		holder.name.setText(tag == 1 ? recommendUser.getNickname()
				: recommendUser.getName());
		holder.phone.setText(tag == 1 ? recommendUser.getPhone()
				: recommendUser.getTypename());
		// holder.ratingBar.setNumStars(recommendUser.getLevel());
		// holder.ratingBar.setRating(recommendUser.getLevel());
		/*
		 * System.out.println("------level---->"+recommendUser.getLevel());
		 * holder.layout.removeAllViews(); for(int
		 * i=0;i<recommendUser.getLevel();i++){ ImageView imageView = new
		 * ImageView(RecommedListActivity.this);
		 * imageView.setImageResource(R.drawable.recommend_icon);
		 * holder.layout.addView(imageView); }
		 */
	}

	private class ViewHolder {
		TextView phone;
		TextView name;
		TextView time;
		// RatingBar ratingBar;
		// LinearLayout layout;
	}

	@Override
	public void onFooterRefresh(PullRefreshView view) {
		initDatas(FOOTER);
	}

	@Override
	public void onHeaderRefresh(PullRefreshView view) {
		initDatas(HEADER);
	}

	public void yonghu_onClick(View view) {
		tag = 1;
		titleTextView.setText("推荐列表");
		linearLayout1.setBackgroundResource(R.drawable.select_bg);
		linearLayout2.setBackgroundResource(R.drawable.no_select_bg);
		if (recode == null) {
			ToastUtil.show(RecommedListActivity.this, "对不起，您还未生成您的推荐码");
		} else {
			initDatas(NEWEST);
		}
	}

	public void shanghu_onClick(View view) {
		tag = 2;
		titleTextView.setText("推荐列表");
		linearLayout1.setBackgroundResource(R.drawable.no_select_bg);
		linearLayout2.setBackgroundResource(R.drawable.select_bg);
		if (recode == null) {
			ToastUtil.show(RecommedListActivity.this, "对不起，您还未生成您的推荐码");
		} else {
			initDatas(NEWEST);
		}
	}

	public void back_onClick(View view) {
		finish();
	}
}
