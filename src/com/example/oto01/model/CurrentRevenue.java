package com.example.oto01.model;

import java.util.List;

public class CurrentRevenue {
	/**
	 * sumnum int 营收统计总额
	 */
	private String average_revenue;
	private List<Revenue> one_month_revenue;

	public CurrentRevenue(String average_revenue,
			List<Revenue> one_month_revenue) {
		super();
		this.average_revenue = average_revenue;
		this.one_month_revenue = one_month_revenue;
	}

	public String getAverage_revenue() {
		return average_revenue;
	}

	public void setAverage_revenue(String average_revenue) {
		this.average_revenue = average_revenue;
	}

	public List<Revenue> getOne_month_revenue() {
		return one_month_revenue;
	}

	public void setOne_month_revenue(List<Revenue> one_month_revenue) {
		this.one_month_revenue = one_month_revenue;
	}

}
