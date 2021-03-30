//package com.example.oto01.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.SectionIndexer;
//import android.widget.TextView;
//
//import com.example.oto01.R;
//import com.example.oto01.model.Constant;
//import com.example.oto01.model.PersonDto;
//import com.example.oto01.stikylist.StickyListHeadersAdapter;
//import com.example.oto01.utils.HttpPostUtils;
//import com.example.oto01.utils.ToastUtil;
//
//public class MaillistAdapter extends BaseAdapter implements
//		StickyListHeadersAdapter, SectionIndexer {
//
//	/** 内容 */
//	private List<PersonDto> countries;
//	/** head */
//	private ArrayList<String> sections;
//	private BackListen listen;
//	private int shopsid1;
//	private Context context;
//	private LayoutInflater inflater;
//
//	public MaillistAdapter(Context context, List<PersonDto> countries,
//			int shopsid, BackListen listen) {
//		inflater = LayoutInflater.from(context);
//		this.context = context;
//		this.countries = countries;
//		this.shopsid1 = shopsid;
//		this.listen = listen;
//	}
//
//	public void updateListView(List<PersonDto> countries) {
//		this.countries = countries;
//		notifyDataSetChanged();
//	}
//
//	@Override
//	public int getCount() {
//		return countries.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return countries.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		final ViewHolder holder;
//
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.items_person_list_new,
//					parent, false);
//			holder.title = (TextView) convertView.findViewById(R.id.title);
//			holder.phone = (TextView) convertView.findViewById(R.id.phone);
//			holder.bt_add = (Button) convertView.findViewById(R.id.bt_add);
//			holder.bt_al_add = (Button) convertView
//					.findViewById(R.id.bt_al_add);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		if (countries.get(position).getIs_have().equals("1")) {
//			// 已添加
//			holder.bt_add.setVisibility(View.GONE);
//			holder.bt_al_add.setVisibility(View.VISIBLE);
//		} else {
//			holder.bt_add.setVisibility(View.VISIBLE);
//			holder.bt_al_add.setVisibility(View.GONE);
//		}
//
//		holder.title.setText(countries.get(position).getName());
//		holder.phone.setText(countries.get(position).getPhone());
//
//		holder.bt_add.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				new AsyncTask<String, Integer, String>() {
//
//					@Override
//					protected String doInBackground(String... params) {
//						BasicNameValuePair shopsid = new BasicNameValuePair(
//								"shopsid", shopsid1 + "");
//						BasicNameValuePair linkman = new BasicNameValuePair(
//								"linkman", countries.get(position).getName());
//						BasicNameValuePair linkphone = new BasicNameValuePair(
//								"linkphone", countries.get(position).getPhone()
//										.toString().trim());
//						List<BasicNameValuePair> lists = new ArrayList<BasicNameValuePair>();
//						lists.add(shopsid);
//						lists.add(linkman);
//						lists.add(linkphone);
//						return HttpPostUtils.HttpGetJson(params[0], lists);
//					}
//
//					@Override
//					protected void onPostExecute(String result) {
//
//						if (result == null) {
//							return;
//						}
//
//						JSONObject jo = null;
//						try {
//							jo = new JSONObject(result);
//							int flag = jo.optInt("res");
//							String error = jo.getString("msg");
//							ToastUtil.show(context, error);
//							if (flag == 0) {
//								countries.get(position).setIs_have("1");
//								// notifyDataSetChanged();
//								// listen.huidiao();
//							} else {
//								ToastUtil.show(context, error);
//							}
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//					}
//				}.execute(Constant.CLIENT_ADD);
//			}
//		});
//		return convertView;
//	}
//
//	@Override
//	public View getHeaderView(int position, View convertView, ViewGroup parent) {
//		HeaderViewHolder holder;
//		if (convertView == null) {
//			holder = new HeaderViewHolder();
//			convertView = inflater.inflate(R.layout.items_person_list_header,
//					null);
//			holder.text = (TextView) convertView.findViewById(R.id.catalog);
//			convertView.setTag(holder);
//		} else {
//			holder = (HeaderViewHolder) convertView.getTag();
//		}
//		String headerText = "" + countries.get(position).getSortLetters();
//		holder.text.setText(headerText);
//		return convertView;
//	}
//
//	@Override
//	public long getHeaderId(int position) {
//		return countries.get(position).getSortLetters().toUpperCase().charAt(0);
//	}
//
//		TextView text;
//	}
//
//	class ViewHolder {
//		TextView title;
//		TextView phone;
//		Button bt_add;
//		Button bt_al_add;
//	}
//
//	@Override
//	public int getPositionForSection(int section) {
//
//		for (int i = 0; i < getCount(); i++) {
//			String sortStr = countries.get(i).getSortLetters();
//			char firstChar = sortStr.toUpperCase().charAt(0);
//			if (firstChar == section) {
//				return i;
//			}
//		}
//		return -1;
//	}
//
//	@Override
//	public int getSectionForPosition(int position) {
//		if (position >= countries.size()) {
//			position = countries.size() - 1;
//		} else if (position < 0) {
//			position = 0;
//		}
//
//		return sections.indexOf(""
//				+ countries.get(position).getSortLetters().toUpperCase()
//						.charAt(0));
//	}
//
//	@Override
//	public Object[] getSections() {
//		return sections.toArray(new String[sections.size()]);
//	}
//
//	public interface BackListen {
//		public void huidiao();
//	}
//}
