package com.example.oto01.listener;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class ClientListListener implements OnRefreshListener<ListView> {

	/**
	 * 全部客户
	 */
	public static final int ALL_BUSINESS = 0X1;
	/**
	 * 外卖/上门
	 */
	public static final int TAKE_OUT_BUSINESS = 0X2;
	/**
	 * 无交易
	 */
	public static final int NO_BUSINESS = 0X3;
	/**
	 * 价格从高到低
	 */
	public static final int MONEY_HIGH_TO_LOW = 0X4;
	/**
	 * 订单从多到少
	 */
	public static final int ORDER_HIGH_TO_LOW = 0X5;
	/**
	 * 时间排序
	 */
	public static final int TIME_SORT = 0X6;
	/**
	 * 姓名排序
	 */
	public static final int NAME_SORT = 0X7;
	/**
	 * 到店支付
	 */
	public static final int STORE_PAY_SORT = 0X8;

	private int type;
	private CLientListener cLientListener;

	/**
	 * type的类型
	 */
	public ClientListListener(int type, CLientListener cLientListener) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.cLientListener = cLientListener;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		switch (type) {
		case ALL_BUSINESS:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.all_business(true, true);
			else
				cLientListener.all_business(true, false);
			break;
		case TAKE_OUT_BUSINESS:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.take_out(true, true);
			else
				cLientListener.take_out(true, false);
			break;
		case NO_BUSINESS:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.no_business(true, true);
			else
				cLientListener.no_business(true, false);
			break;
		case MONEY_HIGH_TO_LOW:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.money_high_to_low(true, true);
			else
				cLientListener.money_high_to_low(true, false);
			break;
		case ORDER_HIGH_TO_LOW:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.order_high_to_low(true, true);
			else
				cLientListener.order_high_to_low(true, false);
			break;
		case TIME_SORT:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.time_sort(true, true);
			else
				cLientListener.time_sort(true, false);
			break;
		case NAME_SORT:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.name_sort(true, true);
			else
				cLientListener.name_sort(true, false);
			break;
		case STORE_PAY_SORT:
			if (refreshView.isHeaderShown())// 下拉刷新
				cLientListener.store_pay_sort(true, true);
			else
				cLientListener.store_pay_sort(true, false);
		default:
			break;
		}
	}

	public interface CLientListener {

		public void all_business(boolean isRefresh, boolean isHeadRefresh);

		public void take_out(boolean isRefresh, boolean isHeadRefresh);

		public void no_business(boolean isRefresh, boolean isHeadRefresh);

		public void time_sort(boolean isRefresh, boolean isHeadRefresh);

		public void name_sort(boolean isRefresh, boolean isHeadRefresh);

		public void money_high_to_low(boolean isRefresh, boolean isHeadRefresh);

		public void order_high_to_low(boolean isRefresh, boolean isHeadRefresh);

		public void store_pay_sort(boolean isRefresh, boolean isHeadRefresh);
	}

}
