package com.example.oto01.wheelDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oto01.R;
import com.example.oto01.model.ScreenInfo;
import com.example.oto01.utils.CommonUtil;
import com.example.wheelmanager.adapter.cascade.AbstractWheelTextAdapter;
import com.example.wheelmanager.view.wheelitem.OnWheelChangedListener;
import com.example.wheelmanager.view.wheelitem.WheelView;
import com.example.wheelmanager.view.wheelview.JudgeDate;
import com.example.wheelmanager.view.wheelview.WheelMainPrecise;

public class WheelDialogManager {

	private static WheelDialogManager wdm;
	private String tempString;
	private int postion = 0;
	/** 省的移动位置 */
	public static final int POS_PROVINCE = 0;
	/** 城市移动位置 */
	public static final int POS_CITY = 1;
	private int[] postions = new int[2];
	private String provinCityString;

	private WheelDialogManager() {
	}

	public static WheelDialogManager getInstanse() {
		if (wdm == null) {
			synchronized (WheelDialogManager.class) {
				if (wdm == null) {
					wdm = new WheelDialogManager();
				}
			}
		}
		return wdm;
	}

	public interface OnWheelItemClickListener {

		void onWheelItemClicl(int positon);
	}

	public interface OnCityWheelClickListener {

		/**
		 * 城市滚轮的回调方法
		 * 
		 * @param labelString
		 *            省市的名称(label)
		 * @param proPos
		 *            省的位置
		 * @param cityPos
		 *            城市的位置
		 */
		void onCityWheelItemClick(String labelString, int proPos, int cityPos);
	}

	/**
	 * 填出一个自定义的滚轮
	 * 
	 * @param context
	 *            上下文
	 * @param types
	 *            数据集
	 * @param defaultPos
	 *            默认位置
	 * @param showTv
	 *            显示的TextView
	 * @param owicl
	 *            回调监听器
	 */
	public void createMyItemWheelShop_Type(Activity context,
			final ArrayList<? extends SelectBean> items, int defaultPos,
			final TextView showTv, final OnWheelItemClickListener owicl) {

		postion = defaultPos; // 消除其他弹出wheel对当前的影响
		tempString = items.get(defaultPos).name;
		View view = dialogSingeItem(context, items, defaultPos);
		final MyAlertDialog_shopType dialog2 = new MyAlertDialog_shopType(
				context).builder().setView(view)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
		dialog2.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (owicl != null) {
					owicl.onWheelItemClicl(postion);
				}
				if (showTv != null)
					showTv.setText(tempString);
			}
		});
		dialog2.show();
	}

	/**
	 * 填出一个自定义的滚轮
	 * 
	 * @param context
	 *            上下文
	 * @param types
	 *            数据集
	 * @param defaultPos
	 *            默认位置
	 * @param showTv
	 *            显示的TextView
	 * @param owicl
	 *            回调监听器
	 */
	public void createMyItemWheel(Activity context,
			final ArrayList<? extends SelectBean> items, int defaultPos,
			final TextView showTv, final OnWheelItemClickListener owicl) {

		postion = defaultPos; // 消除其他弹出wheel对当前的影响
		tempString = items.get(defaultPos).name;
		View view = dialogSingeItem(context, items, defaultPos);
		final MyAlertDialog dialog2 = new MyAlertDialog(context).builder()
				.setView(view).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
		dialog2.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (owicl != null) {
					owicl.onWheelItemClicl(postion);
				}
				if (showTv != null)
					showTv.setText(tempString);
			}
		});
		dialog2.show();
	}

	/**
	 * 显示单一的条目滚轮
	 * 
	 * @return view 单一条目的View
	 */
	private View dialogSingeItem(Activity context,
			final ArrayList<? extends SelectBean> items, int defaultPos) {

		View contentView = LayoutInflater.from(context).inflate(
				R.layout.wheel_items_layout, null);
		final WheelView country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new MyItemAdapter(context, items));

		country.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(
					com.example.wheelmanager.view.wheelitem.WheelView wheel,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub
				postion = country.getCurrentItem();
				tempString = items.get(country.getCurrentItem()).name;
			}
		});
		country.setCurrentItem(defaultPos);
		return contentView;
	}

	private class MyItemAdapter extends AbstractWheelTextAdapter {

		private ArrayList<? extends SelectBean> items;

		/**
		 * 自定义的构造方法
		 */
		protected MyItemAdapter(Context context,
				ArrayList<? extends SelectBean> types) {
			super(context, R.layout.wheel_inner_item_layout, NO_RESOURCE);
			this.items = types;
			setItemTextResource(R.id.wheel_item_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return items.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return items.get(index).name;
		}
	}

	private WheelMainPrecise wheelMainPrecise;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm",
			Locale.CHINA);

	/**
	 * 创建日期精确到时间的滚轮控件（只显示今天与明天）
	 * 
	 * @param context
	 *            activity
	 * @param dateTv
	 *            需要将文字显示到的TextView
	 */
	public void createDatePreciseVheel(Activity context, final TextView dateTv) {

		LayoutInflater inflater1 = LayoutInflater.from(context);
		final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
				null);
		ScreenInfo screenInfo = new ScreenInfo(context);
		wheelMainPrecise = new WheelMainPrecise(timepickerview1, true,
				CommonUtil.getCurrentDate());
		wheelMainPrecise.screenheight = screenInfo.getHeight();
		String time = dateTv.getText().toString();
		Calendar calendar1 = Calendar.getInstance();

		if (JudgeDate.isDate(time, "hh:mm")) {
			try {
				calendar1.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// int year = calendar1.get(Calendar.YEAR);
		// int month = calendar1.get(Calendar.MONTH);
		// int day = calendar1.get(Calendar.DAY_OF_MONTH);
		int hour = calendar1.get(Calendar.HOUR_OF_DAY);
		int min = calendar1.get(Calendar.MINUTE);
		wheelMainPrecise.initDateTimePicker(context, hour, min);

		final MyAlertDialog_yingye dialog = new MyAlertDialog_yingye(context)
				.builder().setView(timepickerview1)
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				}).setPositiveButton("", new OnClickListener() {
					@Override
					public void onClick(View v) {

						dateTv.setText(wheelMainPrecise.getTime());
					}
				});
		dialog.show();
	}

}
