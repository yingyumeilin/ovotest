package com.example.oto01.listener;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class OrderListListener implements OnRefreshListener<ListView> {

	public static final int WAIT_RECEIVE_ORDER = 0X1;
	public static final int WAIT_SEND = 0X2;
	public static final int WAIT_PAY = 0X3;
	public static final int HAVE_SEND = 0X4;
	public static final int FINISH = 0X5;
	public static final int CLOSED = 0X6;

	private int type;
	private DingDanListener dingdanListener;

	/**
	 * type的类型
	 */
	public OrderListListener(int type, DingDanListener dingdanListener) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.dingdanListener = dingdanListener;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		switch (type) {
		case WAIT_RECEIVE_ORDER:
			if (refreshView.isHeaderShown())// 下拉刷新
				dingdanListener.WAIT_RECEIVE_ORDER(true, true);
			else
				dingdanListener.WAIT_RECEIVE_ORDER(true, false);
			break;
		case WAIT_SEND:
			if (refreshView.isHeaderShown())// 下拉刷新
				dingdanListener.WAIT_SEND(true, true);
			else
				dingdanListener.WAIT_SEND(true, false);
			break;
		case WAIT_PAY:
			if (refreshView.isHeaderShown())// 下拉刷新
				dingdanListener.WAIT_PAY(true, true);
			else
				dingdanListener.WAIT_PAY(true, false);
			break;
		case HAVE_SEND:
			if (refreshView.isHeaderShown())// 下拉刷新
				dingdanListener.HAVE_SEND(true, true);
			else
				dingdanListener.HAVE_SEND(true, false);
			break;
		case FINISH:
			if (refreshView.isHeaderShown())// 下拉刷新
				dingdanListener.FINISH(true, true);
			else
				dingdanListener.FINISH(true, false);
			break;
		case CLOSED:
			if (refreshView.isHeaderShown())// 下拉刷新
				dingdanListener.CLOSED(true, true);
			else
				dingdanListener.CLOSED(true, false);
			break;
		default:
			break;
		}
	}

	public interface DingDanListener {

		public void WAIT_RECEIVE_ORDER(boolean isRefresh, boolean isHeadRefresh);

		public void WAIT_SEND(boolean isRefresh, boolean isHeadRefresh);

		public void WAIT_PAY(boolean isRefresh, boolean isHeadRefresh);

		public void HAVE_SEND(boolean isRefresh, boolean isHeadRefresh);

		public void FINISH(boolean isRefresh, boolean isHeadRefresh);

		public void CLOSED(boolean isRefresh, boolean isHeadRefresh);

	}

}
