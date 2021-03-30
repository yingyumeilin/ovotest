package com.example.oto01.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oto01.model.ClientDetails;
import com.example.oto01.model.ClientDetails.Data;

public class ClientDetailsMemberCardAdapter extends BaseAdapter {
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

	public ClientDetailsMemberCardAdapter(Context context, int shopsid) {
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

//		if (cv == null) {
//			cv = mInflater.inflate(R.layout.member_order_item, parent, false);
//			holder2 = new ViewHolder();
//			holder2.tv_order_num = (TextView) cv
//					.findViewById(R.id.tv_order_num);
//			holder2.tv_time = (TextView) cv.findViewById(R.id.tv_time);
//			holder2.tv_name = (TextView) cv.findViewById(R.id.tv_name);
//			holder2.iv_card = (ImageView) cv.findViewById(R.id.iv_card);
//			holder2.tv_card_holder = (TextView) cv
//					.findViewById(R.id.tv_card_holder);
//			holder2.tv_card_num = (TextView) cv.findViewById(R.id.tv_card_num);
//			holder2.tv_seil = (TextView) cv.findViewById(R.id.tv_seil);
//			holder2.tv_money_shi = (TextView) cv
//					.findViewById(R.id.tv_money_shi);
//			cv.setTag(holder2);
//
//		} else {
//			holder2 = (ViewHolder) cv.getTag();
//		}
//		holder2.tv_order_num.setText(mDataList.get(position).getOrdernum());
//		holder2.tv_time.setText(DateUtil.getFormatedDate_1(mDataList.get(
//				position).getAddtime()));
//		holder2.tv_name.setText(mDataList.get(position).getName());
//		holder2.tv_card_holder.setText(mDataList.get(position).getNickname());
//		holder2.tv_card_num.setText(mDataList.get(position).getCardnum());
//		holder2.tv_seil.setText(mDataList.get(position).getGoods_total_price());
//		holder2.tv_money_shi.setText(mDataList.get(position).getTotal());
//		String imgPath = mDataList.get(position).getImage();
//
//		if (null != imgPath && imgPath.contains(";")) {
//			String[] splitImage = imgPath.split(";");
//			ImageLoadTool.disPlay(splitImage[0], holder2.iv_card,
//					R.drawable.client_head);
//		} else {
//			ImageLoadTool.disPlay(imgPath, holder2.iv_card,
//					R.drawable.client_head);
//		}

		// cv.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(mcontext, ShopPayDetailsActivity.class);
		// intent.putExtra("type", "order_details");
		// intent.putExtra("id", item.getId() + "");
		// mcontext.startActivity(intent);
		// }
		// });
		return cv;
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
		ImageView status;
		TextView goodTitle;
		LinearLayout orderPic;
		LinearLayout ll_two;
		TextView paymentType;
		ImageView iv_mine1;
		ImageView iv_mine3;
		ImageView iv_mine2;
		ImageView iv_mine4;
		ImageView order_status1;
		/**
		 * 到店支付
		 */
		TextView tv_code;
		TextView tv_already_verified;
		ImageView iv_store_pay_not_verified;
		TextView order_num;
		TextView tv_order_time;
		TextView order_money;

		// 会员卡
		TextView tv_order_num;
		TextView tv_time;
		ImageView iv_card;
		TextView tv_name;
		TextView tv_card_holder;
		TextView tv_card_num;
		TextView tv_seil;
		TextView tv_money_shi;

	}

}
