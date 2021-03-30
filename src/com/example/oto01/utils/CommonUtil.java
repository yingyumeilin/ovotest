package com.example.oto01.utils;

import java.math.BigDecimal;
import java.util.Calendar;

public class CommonUtil {

	/**
	 * 获取当前的日期
	 * 
	 * @param model
	 *            :1请求用数据， 2.显示用数据
	 * @return 返回的是当前的日期字符串 如："2015-03-08"
	 */
	public static String getCurrentDateMonth(int model) {

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String dateStr = "";
		if (model == 1) {
			dateStr = year + "" + (month < 10 ? "0" + month : month);
		} else if (model == 2) {
			dateStr = year + "年" + month + "月";
		}
		return dateStr;
	}

	/**
	 * 获取当前的日期
	 * 
	 * @return 返回的是当前的日期字符串 如："2015-03-08"
	 */
	public static String getCurrentDate() {

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String dateStr = year + "-" + (month < 10 ? "0" + month : month) + "-"
				+ (day < 10 ? "0" + day : day);
		return dateStr;
	}

	public static String calcMoneyPoint(int bit, String money) {
		try {
			BigDecimal bigD = new BigDecimal(money);
			bigD = bigD.setScale(bit, BigDecimal.ROUND_HALF_UP);
			return bigD.toPlainString();
		} catch (Exception e) {
			e.printStackTrace();
			return String.valueOf(money);
		}
	}
}
