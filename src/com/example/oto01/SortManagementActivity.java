package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.oto01.model.Login;
import com.example.oto01.model.SortManager;
import com.example.oto01.services.GoodService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SortManagementActivity extends BaseActivity {
	private RelativeLayout rl_sort_add;
	private static final int REMOVE_STATUS = 7;// 删除商品分类
	private ListView listView;
	private SortManagerAdapter adapter;
	private Dialog proDialog;
	private static final int SORT_MANAGER = 101;
	private static int REMARK_NAME = 102;
	protected static final int ADD_GOOD_TYPE = 103;
	private int shopsid = 0;
	private Dialog newDialog;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SORT_MANAGER) {
				proDialog.dismiss();
				getCurrentOrderList(msg);
			} else if (msg.what == REMOVE_STATUS) {

				// 删除分类
				if (msg.arg1 == 0) {
					initData();
				} else {
					proDialog.dismiss();
					ToastUtil.show(getApplicationContext(), "删除失败！");
				}
			} else if (msg.what == REMARK_NAME) {

				// 修改分类名称
				if (msg.arg1 == 0) {
					ToastUtil.show(getApplicationContext(), msg.obj + "");
					initData();
				} else {
					proDialog.dismiss();
					ToastUtil.show(getApplicationContext(), msg.obj + "");
				}
			} else if (msg.what == ADD_GOOD_TYPE) {

				if (msg.arg1 == 0) {
					ToastUtil.show(SortManagementActivity.this, "添加分类成功");
					initData();
				} else {
					ToastUtil.show(SortManagementActivity.this, "添加分类失败");
				}

			}

		}

	};
	private TextView title_font;
	private int id1;
	private String name1;

	private void getCurrentOrderList(Message msg) {
		proDialog.dismiss();
		SortManager manager = (SortManager) msg.obj;
		adapter.removeAllDatas();
		if (manager.getRes().equals("0")) {
			List<SortManager.list> list = manager.getTypelist();
			if (list.size() == 0) {
				ToastUtil.show(getApplicationContext(), "没有数据");
				return;
			}
			adapter.addAllDatas(list);
			adapter.notifyDataSetChanged();
		} else {
			ToastUtil.show(getApplicationContext(), manager.getMsg());
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort_management);
		rl_sort_add = (RelativeLayout) findViewById(R.id.rl_sort_add);
		listView = (ListView) findViewById(R.id.listView);
		adapter = new SortManagerAdapter();
		listView.setAdapter(adapter);
		title_font = (TextView) findViewById(R.id.title_font);
		title_font.setText("分类管理");
		LoginManager lm = LoginManager.getInstance(SortManagementActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		initData();

		// 添加分类
		rl_sort_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addType();
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		newDialog = showNewDialog();
	}

	private Dialog showNewDialog() {
		Dialog dialog = new Dialog(SortManagementActivity.this,
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
				R.layout.add_type_fenlei, null);
		layout.setMinimumWidth((int) (SortManagementActivity.this
				.getWindowManager().getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
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
						ToastUtil.show(SortManagementActivity.this,
								"分类名称不能超过6个字");
					} else {
						if (!NetConn.checkNetwork(SortManagementActivity.this)) {
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
					ToastUtil.show(SortManagementActivity.this, "请填写名称");
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
		GoodService goodService = new GoodService(SortManagementActivity.this);
		int flag = goodService.addGoodType1(typename);
		Message message = handler.obtainMessage(ADD_GOOD_TYPE);
		message.arg1 = flag;
		message.obj = goodService.errorMsg;
		handler.sendMessage(message);
	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	private void initData() {
		if (!NetConn.checkNetwork(SortManagementActivity.this)) {
		} else {
			showDialog();
			new MyAsync().execute();
		}
	}

	private class MyAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			GoodService service = new GoodService(SortManagementActivity.this);
			Message message = handler.obtainMessage(SORT_MANAGER);
			String res = service.getSortManagerList(shopsid, 3);
			SortManager manager = JsonUtils.fromJson(res, SortManager.class);
			message.obj = manager;
			handler.sendMessage(message);
			return null;
		}
	}

	class SortManagerAdapter extends BaseAdapter {
		List<SortManager.list> list1 = new ArrayList<SortManager.list>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<SortManager.list> list) {
			list1.addAll(list);
			notifyDataSetChanged();
		}

		/**
		 * 移除某个数据
		 * 
		 * @param position
		 */
		public void removeDatas(int position) {
			list1.remove(position);
			notifyDataSetChanged();
		}

		/**
		 * 移除全部数据
		 * 
		 * @param position
		 */
		public void removeAllDatas() {
			list1.clear();
			notifyDataSetChanged();
		}

		@ViewInject(R.id.tv_name)
		private TextView tv_name;

		@ViewInject(R.id.iv_edit)
		private ImageView iv_edit;
		@ViewInject(R.id.iv_delete)
		private ImageView iv_delete;
		@ViewInject(R.id.iv_down)
		private ImageView iv_down;
		@ViewInject(R.id.iv_up)
		private ImageView iv_up;

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(SortManagementActivity.this).inflate(
						R.layout.sort_manager_item, parent, false);
				holder = new ViewHolder();
				ViewUtils.inject(this, cv);
				holder.tv_name = tv_name;
				holder.iv_delete = iv_delete;
				holder.iv_edit = iv_edit;
				holder.iv_down = iv_down;
				holder.iv_up = iv_up;
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}

			final SortManager.list list2 = list1.get(position);
			holder.tv_name.setText(list2.getName());
			holder.iv_down.setVisibility(View.GONE);
			holder.iv_up.setVisibility(View.GONE);
			// 进行修改名字
			holder.iv_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rename(Integer.valueOf(list2.getId()));
				}
			});
			// 删除
			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					delete(Integer.valueOf(list2.getId()));
				}
			});

			return cv;
		}

		@Override
		public int getCount() {
			return list1.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list1.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
	}

	public class ViewHolder {
		TextView tv_name;
		private ImageView iv_edit;
		private ImageView iv_delete;
		private ImageView iv_down;
		private ImageView iv_up;
	}

	private void delete(final int id) {
		// 提示是否要删除该商品分类
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				SortManagementActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否要删除该商品分类(分类中的商品移到“其它”分类中)?");
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 　删除该商品分类
						final Message message = handler
								.obtainMessage(REMOVE_STATUS);
						if (!NetConn.checkNetwork(SortManagementActivity.this)) {
							// NetConn.setNetwork(OrdersActivity.this);
						} else {
							new Thread(new Runnable() {
								@Override
								public void run() {

									final Message message = handler
											.obtainMessage(REMOVE_STATUS);
									new Thread(new Runnable() {
										@Override
										public void run() {
											GoodService goodService = new GoodService(
													SortManagementActivity.this);
											boolean flag = goodService
													.deleteGoodType(id);
											message.arg1 = 1;
											message.arg1 = 0;
											handler.sendMessage(message);
										}
									}).start();

								}
							}).start();
						}
					}
				});
		dialog.setPositiveButton("取消", null);
		dialog.show();

	}

	protected void rename(int id) {
		id1 = id;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.add_type_fenlei, null);
		layout.setMinimumWidth((int) (SortManagementActivity.this
				.getWindowManager().getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
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

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				if (editText.getText().toString().trim().isEmpty()) {
					ToastUtil.show(getApplicationContext(), "分类名称不能为空");
					return;
				}
				name1 = editText.getText().toString().trim();
				newDialog.dismiss();
				if (!NetConn.checkNetwork(SortManagementActivity.this)) {
				} else {
					new RenameAsync().execute();
				}
			}
		});
		newDialog.show();

	}

	private class RenameAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			GoodService service = new GoodService(SortManagementActivity.this);
			Message message = handler.obtainMessage(REMARK_NAME);
			// message.arg1 = CURRENT_OPT_STATUS;
			String res = service.updateSortList(shopsid, id1, name1);
			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				int flag = jo.optInt("res");
				String error = jo.getString("msg");
				message.arg1 = flag;
				message.obj = error;
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
