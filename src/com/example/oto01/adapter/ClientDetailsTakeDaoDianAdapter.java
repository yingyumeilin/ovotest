package com.example.oto01.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.OrderDetailsActivity;
import com.example.oto01.R;
import com.example.oto01.ShopPayDetailsActivity;
import com.example.oto01.model.ClientDetails;
import com.example.oto01.model.ClientDetails.Data;
import com.example.oto01.model.Constant;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.HttpPostUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;

public class ClientDetailsTakeDaoDianAdapter extends BaseAdapter {
	private Context mcontext;
	private List<Data> mDataList;
	private OnClientItemClickListener mListener;
	LayoutInflater mInflater;
	private int shopsid1;

	public interface OnClientItemClickListener {
		public void OnClientItemClickListener(int position);
	}

	public void setOnClientItemClickListener(OnClientItemClickListener listener) {
		this.mListener = listener;
	}

	public void removeOnClientItemClickListener() {
		this.mListener = null;
	}

	public ClientDetailsTakeDaoDianAdapter(Context context, int shopsid) {
		this.mcontext = context;
		this.shopsid1 = shopsid;
		mDataList = new ArrayList<Data>();
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Data> data) {
		this.mDataList = data;
	}

	public List<Data> getData() {
		return mDataList;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public ClientDetails.Data getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View cv, ViewGroup parent) {
		final Data item = getItem(position);
		ViewHolder holder2 = null;

		if (cv == null) {
			// mInflater = LayoutInflater.from(mcontext);
			// 到店支付订单
			cv = mInflater.inflate(R.layout.store_shop_item, parent, false);
			holder2 = new ViewHolder();
			holder2.tv_code = (TextView) cv.findViewById(R.id.tv_code);
			holder2.tv_already_verified = (TextView) cv
					.findViewById(R.id.tv_already_verified);
			holder2.iv_store_pay_not_verified = (ImageView) cv
					.findViewById(R.id.iv_store_pay_not_verified);
			holder2.order_num = (TextView) cv.findViewById(R.id.order_num);
			holder2.tv_order_time = (TextView) cv
					.findViewById(R.id.tv_order_time);
			holder2.order_money = (TextView) cv.findViewById(R.id.order_money);
			holder2.rl_quan = (RelativeLayout) cv.findViewById(R.id.rl_quan);
			holder2.title_address = (TextView) cv
					.findViewById(R.id.title_address);
			holder2.tv1 = (TextView) cv.findViewById(R.id.tv1);
			cv.setTag(holder2);

		} else {
			holder2 = (ViewHolder) cv.getTag();
		}

		holder2.tv_code.setText(item.getAuthcode());
		holder2.order_num.setText(item.getOrdernum());
		holder2.tv_order_time.setText(DateUtil.getFormatedDate_1(item
				.getAddtime()));

		if (item.getOrder_type().equals("1")) {
			holder2.tv1.setText("验证码");
			holder2.rl_quan.setVisibility(View.GONE);
			holder2.title_address.setVisibility(View.VISIBLE);
			holder2.order_money.setText(item.getReceive_total_price() + "元");
			if (item.getIs_confirm().equals("1")) {
				holder2.iv_store_pay_not_verified.setVisibility(View.GONE);
				holder2.tv_already_verified.setVisibility(View.VISIBLE);
			} else if (item.getIs_confirm().equals("2")) {
				holder2.iv_store_pay_not_verified.setVisibility(View.VISIBLE);
				holder2.tv_already_verified.setVisibility(View.GONE);
			}
		} else {
			holder2.tv1.setText("礼品券码");
			holder2.rl_quan.setVisibility(View.VISIBLE);
			holder2.title_address.setVisibility(View.GONE);
			holder2.order_money.setVisibility(View.GONE);
			holder2.iv_store_pay_not_verified.setVisibility(View.GONE);
			holder2.tv_already_verified.setVisibility(View.GONE);
		}

		holder2.iv_store_pay_not_verified
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						yanzheng(item.getAuthcode());
					}
				});

		cv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mcontext, ShopPayDetailsActivity.class);
				if (item.getOrder_type().equals("1")) {
					intent.putExtra("type", "order_details");
				} else {
					intent.putExtra("type", "quan");
				}
				intent.putExtra("id", item.getId() + "");
				mcontext.startActivity(intent);
			}
		});
		return cv;
	}

	protected void jumpToDetails(Data or) {
		// TODO Auto-generated method stub
		if (or != null) {
			Intent it = new Intent();
			it.setClass(mcontext, OrderDetailsActivity.class);
			it.putExtra("order_id", or.getId());
			it.putExtra("uid", or.getUid());
			it.putExtra("ordertype", or.getOrder_type());
			mcontext.startActivity(it);
		} else {
			System.out.println("------无法跳转------>");
		}
	}

	protected void yanzheng(final String code) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);
		dialog.setTitle("提示");
		dialog.setMessage("是否设为已验证？");
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!NetConn.checkNetwork(mcontext)) {
						} else {
							code(code);
						}
					}
				});
		dialog.setPositiveButton("取消", null);
		dialog.show();
	}

	protected void code(final String authcode1) {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				BasicNameValuePair shopsid = new BasicNameValuePair("shopsid",
						shopsid1 + "");
				BasicNameValuePair authcode = new BasicNameValuePair(
						"authcode", authcode1 + "");
				List<BasicNameValuePair> lists = new ArrayList<BasicNameValuePair>();
				lists.add(shopsid);
				lists.add(authcode);
				return HttpPostUtils.HttpGetJson(params[0], lists);
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
					if (flag == 0) {
						for (int i = 0; i < mDataList.size(); i++) {
							if (mDataList.get(i).getAuthcode()
									.equals(authcode1)) {
								mDataList.get(i).setIs_confirm("1");
							}
						}
						notifyDataSetChanged();
					}
					String error = jo.getString("msg");
					ToastUtil.show(mcontext, error);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}.execute(Constant.ORDER_PAY_CODE);
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

}
