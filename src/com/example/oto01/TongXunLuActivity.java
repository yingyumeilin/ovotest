package com.example.oto01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.oto01.model.Constant;
import com.example.oto01.model.Login;
import com.example.oto01.model.PersonDto;
import com.example.oto01.model.TongXunList;
import com.example.oto01.services.LoginManager;
import com.example.oto01.stikylist.StickyListHeadersAdapter;
import com.example.oto01.stikylist.StickyListHeadersListView;
import com.example.oto01.utils.HttpPostUtils;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.JsonUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.widget.CharacterParser;
import com.example.oto01.widget.PinyinComparator;
import com.example.oto01.widget.SideBar;

@SuppressLint("HandlerLeak")
public class TongXunLuActivity extends BaseActivity implements
		SideBar.OnTouchingLetterChangedListener, TextWatcher,
		OnItemClickListener {

	private SideBar mSideBar;
	private LinearLayout iv_no;
	private TextView mDialog;

	private StickyListHeadersListView mListView;

	private EditText mSearchInput;

	private static final int TONG_XUN_LIST = 101;

	private CharacterParser characterParser;// 汉字转拼音

	private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

	private List<PersonDto> sortDataList = new ArrayList<PersonDto>();
	private FrameLayout fl;
	// private SchoolFriendMemberListAdapter mAdapter;
	private MaillistAdapter mAdapter;
	private int shopsid = 1;
	private TextView title_font;
	private Dialog proDialog;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			proDialog.dismiss();
			if (msg.what == TONG_XUN_LIST) {
				String res = (String) msg.obj;

				try {
					iv_no.setVisibility(View.GONE);
					fl.setVisibility(View.VISIBLE);
					TongXunList tongxun = JsonUtils.fromJson(res,
							TongXunList.class);
					sortDataList = tongxun.getUser_arr();

					if (sortDataList.isEmpty() || sortDataList.size() == 0) {
						mSearchInput.setVisibility(View.GONE);
					} else {
						fillData(sortDataList);
						mSearchInput.setVisibility(View.VISIBLE);
						// 根据a-z进行排序源数据
						Collections.sort(sortDataList, pinyinComparator);
						mAdapter = new MaillistAdapter(TongXunLuActivity.this,
								sortDataList, shopsid);
						mListView.setAdapter(mAdapter);
					}
				} catch (Exception e) {
					mSearchInput.setVisibility(View.GONE);
					iv_no.setVisibility(View.VISIBLE);
					fl.setVisibility(View.GONE);
					JSONObject jo;
					try {
						jo = new JSONObject(res);
						ToastUtil.show(getApplicationContext(),
								jo.optString("msg"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}

				}

			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tongxunlu);
		fl = (FrameLayout) findViewById(R.id.fl);
		iv_no = (LinearLayout) findViewById(R.id.iv_no);
		title_font = (TextView) findViewById(R.id.title_font);
		mListView = (StickyListHeadersListView) findViewById(R.id.school_friend_member);
		mSideBar = (SideBar) findViewById(R.id.school_friend_sidrbar);
		mDialog = (TextView) findViewById(R.id.school_friend_dialog);
		mSearchInput = (EditText) findViewById(R.id.school_friend_member_search_input);
		title_font.setText("添加手机联系人");
		mSideBar.setTextView(mDialog);
		mSearchInput.setVisibility(View.GONE);
		mSideBar.setOnTouchingLetterChangedListener(this);
		LoginManager lm = LoginManager.getInstance(TongXunLuActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		mSearchInput.addTextChangedListener(this);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (GetNumber.lists.size() > 0) {
			GetNumber.lists.clear();
		}

		GetNumber.getNumber(this);
		initData();

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		GetNumber.lists.clear();
		finish();
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
	 * 初始化数据
	 */
	private void initData() {
		sortDataList = GetNumber.lists;
		if (!NetConn.checkNetwork(TongXunLuActivity.this)) {
		} else {
			showDialog();
			new AddClientAsync().execute();
		}

	}

	private class AddClientAsync extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			try {
				JSONObject jObj = new JSONObject();
				jObj.put("shopsid", shopsid);
				JSONArray jArray = new JSONArray();
				for (int i = 0; i < sortDataList.size(); i++) {
					JSONObject productObj = new JSONObject();
					productObj.put("name", sortDataList.get(i).getName());
					productObj.put("phone", sortDataList.get(i).getPhone());
					jArray.put(i, productObj);
				}
				jObj.put("user_arr", jArray);

				String result = HttpUtil.doPost(Constant.CLINET_CUNZAI, "parm",
						jObj.toString());
				Message message = handler.obtainMessage(TONG_XUN_LIST);
				message.obj = result;
				handler.sendMessage(message);
				System.out
						.println("---------------------TongxunluActivity--------------------------------->"
								+ result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		int position = 0;
		// 该字母首次出现的位置
		if (mAdapter != null) {
			position = mAdapter.getPositionForSection(s.charAt(0));
		}
		if (position != -1) {
			mListView.setSelection(position);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
		filterData(s.toString(), sortDataList);
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr, List<PersonDto> list) {
		List<PersonDto> filterDateList = new ArrayList<PersonDto>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = list;
		} else {
			filterDateList.clear();
			for (PersonDto sortModel : list) {
				String name = sortModel.getName();
				String suoxie = sortModel.getSuoxie();
				if (name.indexOf(filterStr.toString()) != -1
						|| suoxie.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		mAdapter.updateListView(filterDateList);
	}

	/**
	 * 填充数据
	 * 
	 * @param list
	 */
	private void fillData(List<PersonDto> list) {
		for (PersonDto cUserInfoDto : list) {
			if (cUserInfoDto != null && cUserInfoDto.getName() != null) {

				String pinyin = characterParser.getSelling(cUserInfoDto
						.getName());
				String suoxie = CharacterParser.getFirstSpell(cUserInfoDto
						.getName());

				cUserInfoDto.setSuoxie(suoxie);

				if (cUserInfoDto.getIs_have().equals("2")) {
					String sortString = pinyin.substring(0, 1).toUpperCase();
					if ("1".equals(cUserInfoDto.getUtype())) {// 判断是否是管理员
						cUserInfoDto.setSortLetters("已");
					} else if (sortString.matches("[A-Z]")) {// 正则表达式，判断首字母是否是英文字母
						cUserInfoDto.setSortLetters(sortString);
					} else {
						cUserInfoDto.setSortLetters("#");
					}
				} else if (cUserInfoDto.getIs_have().equals("1")) {
					cUserInfoDto.setSortLetters("已添加");
				}

			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	class MaillistAdapter extends BaseAdapter implements
			StickyListHeadersAdapter, SectionIndexer {

		/** 内容 */
		private List<PersonDto> countries;
		/** head */
		private ArrayList<String> sections;
		private int shopsid1;
		private Context context;
		private LayoutInflater inflater;

		public MaillistAdapter(Context context, List<PersonDto> countries,
				int shopsid) {
			inflater = LayoutInflater.from(context);
			this.context = context;
			this.countries = countries;
			this.shopsid1 = shopsid;
		}

		public void updateListView(List<PersonDto> countries) {
			this.countries = countries;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return countries.size();
		}

		@Override
		public Object getItem(int position) {
			return countries.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.items_person_list_new,
						parent, false);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.bt_add = (Button) convertView.findViewById(R.id.bt_add);
				holder.bt_al_add = (Button) convertView
						.findViewById(R.id.bt_al_add);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (countries.get(position).getIs_have().equals("1")) {
				// 已添加
				holder.bt_add.setVisibility(View.GONE);
				holder.bt_al_add.setVisibility(View.VISIBLE);
			} else {
				holder.bt_add.setVisibility(View.VISIBLE);
				holder.bt_al_add.setVisibility(View.GONE);
			}

			holder.title.setText(countries.get(position).getName());
			holder.phone.setText(countries.get(position).getPhone());

			holder.bt_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AsyncTask<String, Integer, String>() {

						@Override
						protected String doInBackground(String... params) {
							BasicNameValuePair shopsid = new BasicNameValuePair(
									"shopsid", shopsid1 + "");
							BasicNameValuePair linkman = new BasicNameValuePair(
									"nickname", countries.get(position)
											.getName());
							BasicNameValuePair linkphone = new BasicNameValuePair(
									"userphone", countries.get(position)
											.getPhone().toString().trim());
							List<BasicNameValuePair> lists = new ArrayList<BasicNameValuePair>();
							lists.add(shopsid);
							lists.add(linkman);
							lists.add(linkphone);
							return HttpPostUtils.HttpGetJsonTongXunLu(
									params[0], lists);
						}

						@Override
						protected void onPostExecute(String result) {

							if (result == null) {
								return;
							}

							JSONObject jo = null;
							try {
								jo = new JSONObject(result);
								int flag = jo.optInt("res");
								String error = jo.getString("msg");
								ToastUtil.show(context, error);
								if (flag == 0) {
									countries.get(position).setIs_have("1");
									fillData(countries);
									// 根据a-z进行排序源数据
									Collections.sort(countries,
											pinyinComparator);
									mAdapter.notifyDataSetChanged();
								} else {
									ToastUtil.show(context, error);
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}.execute(Constant.CLIENT_ADD);
				}
			});
			return convertView;
		}

		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			HeaderViewHolder holder;
			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = inflater.inflate(
						R.layout.items_person_list_header, null);
				holder.text = (TextView) convertView.findViewById(R.id.catalog);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}
			String headerText = "" + countries.get(position).getSortLetters();
			holder.text.setText(headerText);
			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			return countries.get(position).getSortLetters().toUpperCase()
					.charAt(0);
		}

		class HeaderViewHolder {
			TextView text;
		}

		class ViewHolder {
			TextView title;
			TextView phone;
			Button bt_add;
			Button bt_al_add;
		}

		@Override
		public int getPositionForSection(int section) {

			for (int i = 0; i < getCount(); i++) {
				String sortStr = countries.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			if (position >= countries.size()) {
				position = countries.size() - 1;
			} else if (position < 0) {
				position = 0;
			}

			return sections.indexOf(""
					+ countries.get(position).getSortLetters().toUpperCase()
							.charAt(0));
		}

		@Override
		public Object[] getSections() {
			return sections.toArray(new String[sections.size()]);
		}

		// public interface BackListen {
		// public void huidiao();
		// }
	}
}
