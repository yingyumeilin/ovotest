package com.example.oto01.views;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.oto01.R;
import com.example.oto01.adapter.PhotoAlbumAdapter;
import com.example.oto01.model.AlbumInfo;

public class PhotoAlbumListPopuWindow extends PopupWindow implements
		OnItemClickListener {

	private Context context;

	private View view;
	private LinearLayout containerLayout;

	private ListView rankListView;

	private PhotoAlbumAdapter rankAdapter;

	private List<AlbumInfo> albumList;

	private OnAlbumListPopuClickListener listener;

	public PhotoAlbumListPopuWindow(Context context, List<AlbumInfo> albumList) {

		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.shop_list_rank_popuwindow, null);
		this.albumList = albumList;
		init();

		setListener();

		setShape();
	}

	/** 实例化对象 */
	private void init() {
		containerLayout = (LinearLayout) view.findViewById(R.id.layout_content);
		 DisplayMetrics dm = context.getResources().getDisplayMetrics();
		containerLayout.getLayoutParams().height=(dm.heightPixels-300);
		rankListView = (ListView) view.findViewById(R.id.rank_listView);

		rankAdapter = new PhotoAlbumAdapter(context, albumList);

		rankListView.setAdapter(rankAdapter);
	}

	/** 设置监听事件 */
	private void setListener() {

		rankListView.setOnItemClickListener(this);

		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					PhotoAlbumListPopuWindow.this.dismiss();
				}
				return true;
			}
		});
		containerLayout.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	/** 设置PopupWindow的属性和样式 */
	private void setShape() {
		int h = ((Activity) context).getWindowManager().getDefaultDisplay()
				.getHeight();
		int w = ((Activity) context).getWindowManager().getDefaultDisplay()
				.getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(h);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);

		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
	}

	/** 显示PopupWindow */
	public void show(View parent) {
		if (!this.isShowing()) {
			this.showAsDropDown(parent, 0, 0);
		} else {
			this.dismiss();
		}
	}

	// public void setRankData(List<ShopListRankBean> rankBeans) {
	// this.rankBeans = rankBeans;
	// rankAdapter.setData(rankBeans);
	//
	// /** 如果没有选中一级商圈，则默认选中第一个 */
	// if (TextUtils.isEmpty(rankAdapter.getRankId())) {
	// rankAdapter.setRankId(rankAdapter.getItem(0).getId());
	// }
	//
	// rankAdapter.notifyDataSetChanged();
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		this.dismiss();

		if (listener != null) {
			listener.onAlbumListRankPopuClick(position);
		}
	}

	public interface OnAlbumListPopuClickListener {
		public void onAlbumListRankPopuClick(int position);
	}

	public void setOnShopListRankPopuClickListener(
			OnAlbumListPopuClickListener listener) {
		this.listener = listener;
	}
}
