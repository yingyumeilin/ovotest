package com.example.oto01.model;

public class WeekRevenueList {

	public String date;

	public Double total;

	// public Long getTotal() {
	// return total;
	// }
	//
	// public void setTotal(Long total) {
	// this.total = total;
	// }

	public WeekRevenueList(String date, Double total) {
		super();
		this.date = date;
		this.total = total;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
