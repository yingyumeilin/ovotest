package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.oto01.model.Constant;
import com.example.oto01.model.CurrentRevenue;
import com.example.oto01.model.Revenue;
import com.example.oto01.model.WeekRevenue;
import com.example.oto01.model.WeekRevenueList;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 销售管理
 * 
 * @author lqq
 * 
 */
public class SaleService extends BaseHttpService {

	public SaleService(Context context) {
		super(context);
	}

	/**
	 * 根据要查询的周期查询当前营收
	 * 
	 * @param duration
	 * @return
	 */
	public CurrentRevenue getCurrentRevenue(int shopsid) {
		try {
			// String res = HttpUtil.doGet(Constant.REVENUE_URI + "&shopsid="
			// + shopsid);
			//
			// System.out.println("---yes->" + Constant.REVENUE_URI +
			// "&shopsid="
			// + shopsid);
			// Log.d("TAG", "res res : " + res);

			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"]]";
			String res = HttpUtil.doPost(Constant.REVENUE_URI, string);
			return handleRevenueRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 获取店铺营收接口
	 * 
	 * @param shopsid
	 * @return
	 */
	public WeekRevenue getWeekRevenue(int shopsid) {
		try {
			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"]]";
			String res = HttpUtil.doPost(Constant.GET_WEEK_SALES_URI, string);

			System.out.println("-----res----->" + res);
			Log.d("TAG", "res res : " + res);
			return handleGetWeekRevenueRes(res);
			// return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private WeekRevenue handleGetWeekRevenueRes(String res) {
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				// int sumnum = jo.getInt("sumnum");
				JSONArray jsonArray = jo.getJSONArray("one_week_revenue");
				List<WeekRevenueList> data = new ArrayList<WeekRevenueList>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					System.out.println("-----jsonObject--->" + jsonObject);
					WeekRevenueList revenue = new WeekRevenueList(
							jsonObject.getString("date"),
							jsonObject.getDouble("total"));
					data.add(revenue);
				}
				WeekRevenue revenue = new WeekRevenue(
						jo.getString("today_revenue"),
						jo.getString("total_revenue"),
						jo.getString("total_reward_price"),
						jo.getString("now_month_revenue"), data);
				return revenue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CurrentRevenue handleGetTodayRevenueRes(String res) {
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				// int sumnum = jo.getInt("sumnum");
				JSONArray jsonArray = jo.getJSONArray("data");
				List<Revenue> data = new ArrayList<Revenue>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					System.out.println("-----jsonObject--->" + jsonObject);
					Revenue revenue = new Revenue(
							jsonObject.getString("total"),
							jsonObject.getString("ordernum"));
					data.add(revenue);
				}
				CurrentRevenue currentRevenue = new CurrentRevenue(
						jo.getString("sumnum"), data);
				System.out.println("------currentRevenue-------->"
						+ currentRevenue);
				return currentRevenue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CurrentRevenue handleRevenueRes(String res) {
		Log.d("TAG", "res handleRevenueRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				// int sumnum = jo.getInt("sumnum");
				JSONArray jsonArray = jo.getJSONArray("one_month_revenue");
				List<Revenue> one_month_revenue = new ArrayList<Revenue>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Revenue revenue = new Revenue(jsonObject.getString("days"),
							jsonObject.getString("shops_receive_total"));
					one_month_revenue.add(revenue);
				}
				CurrentRevenue currentRevenue = new CurrentRevenue(
						jo.getString("average_revenue"), one_month_revenue);
				return currentRevenue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
