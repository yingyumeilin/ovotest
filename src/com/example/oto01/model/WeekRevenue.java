package com.example.oto01.model;

import java.io.Serializable;
import java.util.List;

public class WeekRevenue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String res;
	// 今日营收
	public String today_revenue;
	// 全部营收
	public String total_revenue;
	// 平台累计补贴
	public String total_reward_price;
	// 本月营收
	public String now_month_revenue;

	private List<WeekRevenueList> one_week_revenue;


	public String getNow_month_revenue() {
		return now_month_revenue;
	}

	public void setNow_month_revenue(String now_month_revenue) {
		this.now_month_revenue = now_month_revenue;
	}

	public WeekRevenue(String today_revenue, String total_revenue,
			String total_reward_price, String now_month_revenue,
			List<WeekRevenueList> one_week_revenue) {
		super();
		this.today_revenue = today_revenue;
		this.total_revenue = total_revenue;
		this.total_reward_price = total_reward_price;
		this.now_month_revenue = now_month_revenue;
		this.one_week_revenue = one_week_revenue;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getToday_revenue() {
		return today_revenue;
	}

	public void setToday_revenue(String today_revenue) {
		this.today_revenue = today_revenue;
	}

	public String getTotal_revenue() {
		return total_revenue;
	}

	public void setTotal_revenue(String total_revenue) {
		this.total_revenue = total_revenue;
	}

	public String getTotal_reward_price() {
		return total_reward_price;
	}

	public void setTotal_reward_price(String total_reward_price) {
		this.total_reward_price = total_reward_price;
	}
//
//	public String getNow_month_revenue() {
//		return now_month_revenue;
//	}
//
//	public void setNow_month_revenue(String now_month_revenue) {
//		this.now_month_revenue = now_month_revenue;
//	}
	

	public List<WeekRevenueList> getOne_week_revenue() {
		return one_week_revenue;
	}

	


	public void setOne_week_revenue(List<WeekRevenueList> one_week_revenue) {
		this.one_week_revenue = one_week_revenue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
