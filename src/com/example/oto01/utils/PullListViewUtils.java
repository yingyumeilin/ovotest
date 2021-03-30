package com.example.oto01.utils;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 刷新listView工具类
 * 
 * @author chenjia
 */
public class PullListViewUtils {
	private static final String both_to_refresh_label = "上拉加载更多…";
	private static final String pull_to_refresh_refreshing_label = "正在加载…";
	private static final String both_to_release_label = "放开以加载…";

	/**
	 * 添加扩展listView的foot模式
	 * 
	 * @param refreshListView
	 *            传入的refreshListView
	 * @return 返回的refreshListView
	 */
	public static PullToRefreshListView setRefreshBothMode(
			PullToRefreshListView refreshListView) {
		refreshListView.setMode(Mode.BOTH);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				both_to_refresh_label);
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				pull_to_refresh_refreshing_label);
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(
				both_to_release_label);
		return refreshListView;
	}

	// listView 失去刷新的能力
	public static PullToRefreshListView setRefreshBothMode1(
			PullToRefreshListView refreshListView) {
		refreshListView.setMode(Mode.DISABLED);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				both_to_refresh_label);
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				pull_to_refresh_refreshing_label);
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(
				both_to_release_label);
		return refreshListView;
	}

}
