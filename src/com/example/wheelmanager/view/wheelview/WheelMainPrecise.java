package com.example.wheelmanager.view.wheelview;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;

import com.example.oto01.R;

public class WheelMainPrecise {

	private View view;
	private WheelView wvYear;
	private WheelView wvMonth;
	private WheelView wvDay;
	private WheelView wvHours;
	private WheelView wvMins;
	public int screenheight;
	private boolean hasSelectTime;
	private static int START_YEAR = 2000, END_YEAR = 2100;
	private int year;
	private int month;
	private int day;

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

	public WheelMainPrecise(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		setView(view);
	}

	public WheelMainPrecise(View view, boolean hasSelectTime, String crtTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);

		String[] items = crtTime.split("-");
//		year = Integer.parseInt(items[0]);
//		month = Integer.parseInt(items[1]);
//		day = Integer.parseInt(items[2]);
	}

	public void initDateTimePicker(Context context, int year, int month, int day) {
		this.initDateTimePicker(context, 0, 0);
	}

	/**
	 * 弹出日期时间选择器
	 */
	public void initDateTimePicker(Context context, int h, int m) {

		// // 大的月份
		// String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		// String[] months_little = { "4", "6", "9", "11" };
		//
		// final List<String> list_big = Arrays.asList(months_big);
		// final List<String> list_little = Arrays.asList(months_little);

		// wvYear = (WheelView) view.findViewById(R.id.year);
		// wvYear.setAdapter(new NumericWheelAdapter(START_YEAR, year)); //
		// 设置"年"的显示数据
		// wvYear.setCurrentItem(year - START_YEAR); // 初始化时显示的数据

		// wvMonth = (WheelView) view.findViewById(R.id.month);
		// wvMonth.setAdapter(new NumericWheelAdapter(1, month));
		// wvMonth.setCurrentItem(mon);
		//
		// wvDay = (WheelView) view.findViewById(R.id.day);
		// wvDay.setCyclic(true);
		// if (list_big.contains(String.valueOf(mon + 1))) {
		// if (mon + 1 == month) {
		// wvDay.setAdapter(new NumericWheelAdapter(1, day));
		// } else {
		// wvDay.setAdapter(new NumericWheelAdapter(1, 31));
		// }
		// } else if (list_little.contains(String.valueOf(mon + 1))) {
		// if (mon + 1 == month) {
		// wvDay.setAdapter(new NumericWheelAdapter(1, day));
		// } else {
		// wvDay.setAdapter(new NumericWheelAdapter(1, 30));
		// }
		// } else {
		// if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) { //
		// 闰年的处理逻辑
		// if (mon + 1 == month) {
		// wvDay.setAdapter(new NumericWheelAdapter(1, day));
		// } else {
		// wvDay.setAdapter(new NumericWheelAdapter(1, 29));
		// }
		// } else {
		// if (mon + 1 == month) {
		// wvDay.setAdapter(new NumericWheelAdapter(1, day));
		// } else {
		// wvDay.setAdapter(new NumericWheelAdapter(1, 28));
		// }
		// }
		// }
		// wvDay.setCurrentItem(d - 1);

		wvHours = (WheelView) view.findViewById(R.id.hour);
		wvMins = (WheelView) view.findViewById(R.id.min);
		if (hasSelectTime) {
			wvHours.setVisibility(View.VISIBLE);
			wvMins.setVisibility(View.VISIBLE);

			wvHours.setAdapter(new NumericWheelAdapter(0, 23));
			wvHours.setCyclic(false); // 可循环滚动
			wvHours.setCurrentItem(h);

			wvMins.setAdapter(new NumericWheelAdapter(0, 59));
			wvMins.setCyclic(false);
			wvMins.setCurrentItem(m);
		} else {
			wvHours.setVisibility(View.GONE);
			wvMins.setVisibility(View.GONE);
		}

		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// int year_num = newValue + START_YEAR;
				// // 判断大小月及是否闰年,用来确定"日"的数据
				// if (list_big
				// .contains(String.valueOf(wvMonth.getCurrentItem() + 1))) {
				// wvDay.setAdapter(new NumericWheelAdapter(1, 31));
				// } else if (list_little.contains(String.valueOf(wvMonth
				// .getCurrentItem() + 1))) {
				// wvDay.setAdapter(new NumericWheelAdapter(1, 30));
				// } else {
				// if ((year_num % 4 == 0 && year_num % 100 != 0)
				// || year_num % 400 == 0)
				// wvDay.setAdapter(new NumericWheelAdapter(1, 29));
				// else
				// wvDay.setAdapter(new NumericWheelAdapter(1, 28));
				// }
			}
		};
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// int month_num = newValue + 1;
				// // 判断大小月及是否闰年,用来确定"日"的数据
				// if (list_big.contains(String.valueOf(month_num))) {
				// if (month_num == month) {
				// wvDay.setAdapter(new NumericWheelAdapter(1, day));
				// } else {
				// wvDay.setAdapter(new NumericWheelAdapter(1, 31));
				// }
				// } else if (list_little.contains(String.valueOf(month_num))) {
				// if (month_num == month) {
				// wvDay.setAdapter(new NumericWheelAdapter(1, day));
				// } else {
				// wvDay.setAdapter(new NumericWheelAdapter(1, 30));
				// }
				// } else {
				// if (((wvYear.getCurrentItem() + START_YEAR) % 4 == 0 &&
				// (wvYear
				// .getCurrentItem() + START_YEAR) % 100 != 0)
				// || (wvYear.getCurrentItem() + START_YEAR) % 400 == 0) {
				// if (month_num == month) {
				// wvDay.setAdapter(new NumericWheelAdapter(1, day));
				// } else {
				// wvDay.setAdapter(new NumericWheelAdapter(1, 29));
				// }
				// } else {
				// if (month_num == month) {
				// wvDay.setAdapter(new NumericWheelAdapter(1, day));
				// } else {
				// wvDay.setAdapter(new NumericWheelAdapter(1, 28));
				// }
				// }
				// }
			}
		};
		// wvYear.addChangingListener(wheelListener_year);
		// wvMonth.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 3;
		else
			textSize = (screenheight / 100) * 3;
		// wvDay.TEXT_SIZE = textSize; // CommonUtil.px2sp(mContext, 30);
		// wvMonth.TEXT_SIZE = textSize;
		// wvYear.TEXT_SIZE = textSize;
		wvHours.TEXT_SIZE = textSize;
		wvMins.TEXT_SIZE = textSize;
	}

	/**
	 * 弹出日期时间选择器(定义以后的时间)
	 */
	public void initDateTimePicker2(Context context, int year, int mon, int d,
			int h, int m) {

		String[] monthsBig = { "1", "3", "5", "7", "8", "10", "12" };
		String[] monthsLittle = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(monthsBig);
		final List<String> list_little = Arrays.asList(monthsLittle);

		wvYear = (WheelView) view.findViewById(R.id.year);
		wvYear.setAdapter(new NumericWheelAdapter(year, year)); // 设置"年"的显示数据
		wvYear.setCurrentItem(0); // 初始化时显示的数据

		wvMonth = (WheelView) view.findViewById(R.id.month);
		wvMonth.setAdapter(new NumericWheelAdapter(month, 12));
		wvMonth.setCurrentItem(0);

		wvDay = (WheelView) view.findViewById(R.id.day);
		wvDay.setCyclic(true);

		if (list_big.contains(String.valueOf(mon + 1))) {
			wvDay.setAdapter(new NumericWheelAdapter(day, 31));
		} else if (list_little.contains(String.valueOf(mon + 1))) {
			wvDay.setAdapter(new NumericWheelAdapter(day, 30));
		} else {
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) // 闰年
				wvDay.setAdapter(new NumericWheelAdapter(day, 29));
			else
				wvDay.setAdapter(new NumericWheelAdapter(day, 28));
		}
		wvDay.setCurrentItem(0);

		wvHours = (WheelView) view.findViewById(R.id.hour);
		wvMins = (WheelView) view.findViewById(R.id.min);
		if (hasSelectTime) {
			wvHours.setVisibility(View.VISIBLE);
			wvMins.setVisibility(View.VISIBLE);

			wvHours.setAdapter(new NumericWheelAdapter(0, 23));
			wvHours.setCyclic(true); // 可循环滚动
			wvHours.setCurrentItem(h);

			wvMins.setAdapter(new NumericWheelAdapter(0, 59));
			wvMins.setCyclic(true); // 可循环滚动
			wvMins.setCurrentItem(m);
		} else {
			wvHours.setVisibility(View.GONE);
			wvMins.setVisibility(View.GONE);
		}

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wvMonth.getCurrentItem() + 1))) {
					wvDay.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wvMonth
						.getCurrentItem() + 1))) {
					wvDay.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wvDay.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wvDay.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				if (list_big.contains(String.valueOf(month_num))) {
					if (month_num == month) {
						wvDay.setAdapter(new NumericWheelAdapter(day, 31));
					} else {
						wvDay.setAdapter(new NumericWheelAdapter(1, 31));
					}
				} else if (list_little.contains(String.valueOf(month_num))) {
					if (month_num == month) {
						wvDay.setAdapter(new NumericWheelAdapter(day, 30));
					} else {
						wvDay.setAdapter(new NumericWheelAdapter(1, 30));
					}
				} else {
					if (((wvYear.getCurrentItem() + START_YEAR) % 4 == 0 && (wvYear
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wvYear.getCurrentItem() + START_YEAR) % 400 == 0) {
						if (month_num == month) {
							wvDay.setAdapter(new NumericWheelAdapter(day, 29));
						} else {
							wvDay.setAdapter(new NumericWheelAdapter(1, 29));
						}
					} else {
						if (month_num == month) {
							wvDay.setAdapter(new NumericWheelAdapter(day, 28));
						} else {
							wvDay.setAdapter(new NumericWheelAdapter(1, 28));
						}
					}
				}
			}
		};
		wvYear.addChangingListener(wheelListener_year);
		wvMonth.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 3;
		else
			textSize = (screenheight / 100) * 3;
		wvDay.TEXT_SIZE = textSize; // CommonUtil.px2sp(mContext, 30);
		wvMonth.TEXT_SIZE = textSize;
		wvYear.TEXT_SIZE = textSize;
		wvHours.TEXT_SIZE = textSize;
		wvMins.TEXT_SIZE = textSize;
	}

	public String getTime() {

		StringBuffer sb = new StringBuffer();
		// int currentYear = wvYear.getCurrentItem() + START_YEAR;
		// int crtMonth = wvMonth.getCurrentItem() + 1;
		// int crtDay = wvDay.getCurrentItem() + 1;
		// if (!hasSelectTime)
		// sb.append(currentYear).append("-").append(crtMonth).append("-")
		// .append(crtDay);
		// else {

		int intHour = wvHours.getCurrentItem();
		String hour = intHour < 10 ? "0" + intHour : String.valueOf(intHour);
		int intMin = wvMins.getCurrentItem();
		String min = intMin < 10 ? "0" + intMin : String.valueOf(intMin);
		// String month = crtMonth < 10 ? "0" + crtMonth : String
		// .valueOf(crtMonth);
		// String day = crtDay < 10 ? "0" + crtDay : String.valueOf(crtDay);
		sb.append(hour).append(":").append(min);
		// }
		return sb.toString();
	}

	/**
	 * 选择后边的时间使用
	 * 
	 * @return
	 */
	public String getTime2() {

		StringBuffer sb = new StringBuffer();
		int currentYear = wvYear.getCurrentItem() + year;
		int crtMonth = wvMonth.getCurrentItem() + month;
		int crtDay = wvDay.getCurrentItem() + day;
		if (!hasSelectTime)
			sb.append(currentYear).append("-").append(crtMonth).append("-")
					.append(crtDay);
		else {

			int intHour = wvHours.getCurrentItem();
			String hour = intHour < 10 ? "0" + intHour : String
					.valueOf(intHour);
			int intMin = wvMins.getCurrentItem();
			String min = intMin < 10 ? "0" + intMin : String.valueOf(intMin);
			String month = crtMonth < 10 ? "0" + crtMonth : String
					.valueOf(crtMonth);
			String day = crtDay < 10 ? "0" + crtDay : String.valueOf(crtDay);
			sb.append(currentYear).append("-").append(month).append("-")
					.append(day).append(" ").append(hour).append(":")
					.append(min);
		}
		return sb.toString();
	}
}
