package com.example.wheelmanager.view.wheelview;

import android.content.Context;
import android.view.View;

import com.example.oto01.R;

/**
 * <pre>   
 * 类名称：WheelMainMonth   
 * 类描述：创建只有年月的滚轮   
 * 创建人：weiTeng   
 * 创建时间：2015-8-21 下午3:22:15   
 * 修改人：weiTeng   
 * 修改时间：2015-8-21 下午3:22:15   
 * 修改备注：   
 * @version v1.0
 * </pre>
 */
public class WheelMainMonth {

	private View view;
	private WheelView myearWheel;
	private WheelView monthWheel;
	public int screenheight;
	private boolean hasSelectTime;
	private static int START_YEAR = 1990, END_YEAR = 2100;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMainMonth(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		setView(view);
	}

	public WheelMainMonth(View view, boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}

	public void initDateTimePicker(Context context, int year, int month) {
		this.initDateTimePicker(context, year, month, 0);
	}
	
	/**
	 * 弹出日期时间选择器
	 */
	public void initDateTimePicker(Context context, int year, int month, int day) {
		// 年
		myearWheel = (WheelView) view.findViewById(R.id.year);
		myearWheel.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		myearWheel.setCyclic(true); // 可循环滚动
		myearWheel.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		monthWheel = (WheelView) view.findViewById(R.id.month);
		monthWheel.setAdapter(new NumericWheelAdapter(1, 12));
		monthWheel.setCyclic(true);
		monthWheel.setCurrentItem(month);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			}
		};
		myearWheel.addChangingListener(wheelListener_year);
		monthWheel.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 3;
		else
			textSize = (screenheight / 100) * 3;
		monthWheel.TEXT_SIZE = textSize;
		myearWheel.TEXT_SIZE = textSize;
	}

	public String getTime() {
		
		StringBuffer sb = new StringBuffer();
		if (!hasSelectTime){
			sb.append((myearWheel.getCurrentItem() + START_YEAR)).append("-").append((monthWheel.getCurrentItem() + 1));
		}else {
			sb.append((myearWheel.getCurrentItem() + START_YEAR)).append("-").append((monthWheel.getCurrentItem() + 1));
		}
		return sb.toString();
	}
}
