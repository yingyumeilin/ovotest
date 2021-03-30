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
import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.imageload.utils.ImageLoadTool;
import com.example.oto01.model.ClientDetails;
import com.example.oto01.model.ClientDetails.Data;
import com.example.oto01.model.Constant;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.HttpPostUtils;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;

public class ClientDetailsTakeWaiMaiAdapter extends BaseAdapter {
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

	public ClientDetailsTakeWaiMaiAdapter(Context context, int shopsid) {
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

	// @Override
	// public int getViewTypeCount() {
	// return 4;
	// }

	// @Override
	// public int getItemViewType(int position) {
	// if (mDataList.get(position).getType() == 0) {
	// return 0;
	// }
	// if (mDataList.get(position).getType() == 1) {
	// return 1;
	// }
	// if (mDataList.get(position).getType() == 2) {
	// return 2;
	// }
	// return 10;
	// }

	@Override
	public View getView(int position, View cv, ViewGroup parent) {
		final Data item = getItem(position);
		ViewHolder holder1 = null;

		// final int type = getItemViewType(position);
		if (cv == null) {
			// 外卖（上门）订单
			holder1 = new ViewHolder();
			cv = mInflater.inflate(R.layout.order_list_item, parent, false);

			holder1.consiginee = (TextView) cv
					.findViewById(R.id.order_consiginee);
			holder1.disnum = (TextView) cv.findViewById(R.id.disnum);
			holder1.goodsnum = (TextView) cv.findViewById(R.id.goodsnum);
			holder1.disnum_title = (TextView) cv
					.findViewById(R.id.title_disnum);
			holder1.goodsnum_title = (TextView) cv
					.findViewById(R.id.title_goodsnum);
			holder1.line = (ImageView) cv.findViewById(R.id.line1);
			// holder.phone = (TextView) cv.findViewById(R.id.order_phone);
			holder1.location = (TextView) cv.findViewById(R.id.order_location);
			holder1.time = (TextView) cv.findViewById(R.id.order_time);
			holder1.status = (TextView) cv.findViewById(R.id.order_status);
			holder1.goodTitle = (TextView) cv.findViewById(R.id.good_title);
			holder1.orderPic = (LinearLayout) cv.findViewById(R.id.order_pic);
			holder1.ll_two = (LinearLayout) cv.findViewById(R.id.ll_two);
			holder1.iv_mine1 = (ImageView) cv.findViewById(R.id.iv_mine1);
			holder1.iv_mine3 = (ImageView) cv.findViewById(R.id.iv_mine3);
			holder1.iv_mine2 = (ImageView) cv.findViewById(R.id.iv_mine2);
			holder1.iv_mine4 = (ImageView) cv.findViewById(R.id.iv_mine4);
			holder1.order_status1 = (TextView) cv
					.findViewById(R.id.order_status1);
			holder1.rl_quan = (RelativeLayout) cv.findViewById(R.id.rl_quan);
			cv.setTag(holder1);

		} else {
			holder1 = (ViewHolder) cv.getTag();
		}

		refreshItem(holder1, item);

		cv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpToDetails(item);
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

	private void refreshItem(ViewHolder holder, Data order) {

		if (order.getIs_dis() == 3) {// 已找管家
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

		if (order.getOrder_type().equals("1")) {
			holder.rl_quan.setVisibility(View.GONE);
		} else {
			holder.rl_quan.setVisibility(View.VISIBLE);
		}
		holder.consiginee.setText(order.getLinkman());
		holder.location.setText(order.getAddress());
		// holder.price.setText("￥" + order.getShowTotal());

		holder.time.setText(DateUtil.getFormatedDate_1(order.getAddtime()));

		holder.goodTitle.setText(order.getOrdernum());
		// holder.paymentType.setText(order.getPayment() == 1 ? "货到付款" : "电子支付"
		// + (order.getPaystate() == 1 ? "(未付款)" : "(已付款)"));
		if (order.getOrder_type().equals("1")) {
			// 普通优惠券
			int size = order.getGoods().size();
			System.out.println("------------size---------->" + size);
			// ImgLoad loader = ImgLoad.getInstance();
			System.out.println("-------size------->" + size);
			System.out.println("----order---->" + order);
			System.out.println("----order.getOrderGood()---->"
					+ order.getGoods());
			if (size > 4) {
				size = 4;
			}
			if (size == 1) {
				// holder.goodTitle.setText(order.getOrderGood().get(0).getContent());
				String imgPath = order.getGoods().get(0).getPicpath();
				if (imgPath != null) {
					ImageLoadTool.disPlay(imgPath, holder.iv_mine1,
							R.drawable.default_image);
					// holder.iv_mine1.setTag(imgPath);
					// loader.addTask(imgPath, holder.iv_mine1);
					// loader.doTask();
				}
				holder.orderPic.setVisibility(View.VISIBLE);
				holder.iv_mine1.setVisibility(View.VISIBLE);
				holder.ll_two.setVisibility(View.GONE);
				holder.iv_mine2.setVisibility(View.GONE);
				holder.iv_mine3.setVisibility(View.GONE);
				holder.iv_mine4.setVisibility(View.GONE);

				// holder.mlv.setVisibility(View.GONE);
			} else if (size == 2) {
				String imgPath = order.getGoods().get(0).getPicpath();
				String imgPath1 = order.getGoods().get(1).getPicpath();
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
				String imgPath = order.getGoods().get(0).getPicpath();
				String imgPath1 = order.getGoods().get(1).getPicpath();
				String imgPath2 = order.getGoods().get(2).getPicpath();
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
				String imgPath = order.getGoods().get(0).getPicpath();
				String imgPath1 = order.getGoods().get(1).getPicpath();
				String imgPath2 = order.getGoods().get(2).getPicpath();
				String imgPath3 = order.getGoods().get(3).getPicpath();
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
			// 礼品券
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

	}

}
