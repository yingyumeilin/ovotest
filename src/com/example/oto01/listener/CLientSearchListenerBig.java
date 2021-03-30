package com.example.oto01.listener;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class CLientSearchListenerBig implements OnRefreshListener<ListView> {

	public static final int CLIENT_SEARCH = 0X1;
	public static final int CLIENT_SHOP_PAY = 0X2;
	public static final int CLIENT_MEMBER_CARD = 0X3;

	private int type;
	private CLientSearchListener clientSearchListener;

	/**
	 * type的类型
	 */
	public CLientSearchListenerBig(int type,
			CLientSearchListener clientSearchListener) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.clientSearchListener = clientSearchListener;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		switch (type) {
		case CLIENT_SEARCH:
			if (refreshView.isHeaderShown())// 下拉刷新
				clientSearchListener.search(true, true);
			else
				clientSearchListener.search(true, false);
			break;
		case CLIENT_SHOP_PAY:
			if (refreshView.isHeaderShown())// 下拉刷新
				clientSearchListener.shop_pay(true, true);
			else
				clientSearchListener.shop_pay(true, false);
			break;
		case CLIENT_MEMBER_CARD:
			if (refreshView.isHeaderShown())// 下拉刷新
				clientSearchListener.member_card(true, true);
			else
				clientSearchListener.member_card(true, false);
			break;
		default:
			break;
		}
	}

	public interface CLientSearchListener {

		public void search(boolean isRefresh, boolean isHeadRefresh);

		public void shop_pay(boolean isRefresh, boolean isHeadRefresh);

		public void member_card(boolean isRefresh, boolean isHeadRefresh);

	}

}
