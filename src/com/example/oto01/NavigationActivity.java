package com.example.oto01;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.ClientsUtil;
import com.example.oto01.utils.NetConn;

public class NavigationActivity extends BaseActivity {
	private List<View> goodPicList;
	private ViewPager viewPager;
	private LinearLayout mDotsLayout;
	private List<Integer> imgPathList;
	private LoginManager lm;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		lm = LoginManager.getInstance(getApplicationContext());
		lm.saveTag(2);// 应用开启

		ClientsUtil.setFirstLogin(getApplicationContext(), true);
		ClientsUtil.setFirstLoginShopCheck(getApplicationContext(), true);
		ClientsUtil.setFirstLoginGesture(getApplicationContext(), true);
		ClientsUtil.setFirstLoginSettingKuang(getApplicationContext(), true);
		System.out.println("------NavigationActivity tag----->" + lm.getTag());
		viewPager = (ViewPager) findViewById(R.id.guide_viewpager_head);
		mDotsLayout = (LinearLayout) findViewById(R.id.guide_dots_head);
		initViewPager();
	}

	private void initViewPager() {
		goodPicList = new ArrayList<View>();
		imgPathList = new ArrayList<Integer>();
		timer = new Timer();
		imgPathList.add(0, R.drawable.navigation_01);
		imgPathList.add(1, R.drawable.navigation_02);
		imgPathList.add(2, R.drawable.navigation_03);
		initDots(imgPathList.size());
		LayoutInflater inflater = LayoutInflater.from(NavigationActivity.this);
		for (int i = 0; i < imgPathList.size(); i++) {
			goodPicList.add(initGoodPicView(inflater));
		}
		GoodPicPagerAdapter adapter = new GoodPicPagerAdapter(goodPicList);
		adapter.notifyDataSetChanged();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
					if (i == arg0) {
						mDotsLayout.getChildAt(i).setSelected(true);
					} else {
						mDotsLayout.getChildAt(i).setSelected(false);
					}
				}
				if (arg0 == imgPathList.size() - 1) {
					timer = new Timer();
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// FIXME 进入登陆页
							Intent intent = new Intent();
							if (lm.isLogined() && lm.getLoginId() != 0) {
								if (!NetConn
										.checkNetwork(NavigationActivity.this)) {
									intent.setClass(getApplicationContext(),
											LoadingTwoActivity.class);
								} else {
									intent.setClass(getApplicationContext(),
											MainActivity.class);
									intent.putExtra("login", "1");
								}
							} else {
								intent.setClass(getApplicationContext(),
										LoadingTwoActivity.class);
							}
							startActivity(intent);
							// 设置切换动画，从右边进入，左边退出
							overridePendingTransition(R.anim.in_from_right,
									R.anim.out_to_left);
							finish();
						}
					}, 3000);

				} else {
					timer.cancel();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		viewPager.setAdapter(adapter);
		for (int i = 0; i < imgPathList.size(); i++) {
			int path = imgPathList.get(i);
			View view = goodPicList.get(i);
			ImageView img = (ImageView) view.findViewById(R.id.img);
			img.setImageResource(path);
		}
	}

	private View initGoodPicView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.navigation_viewpager_item, null);
	}

	private void initDots(int count) {
		mDotsLayout.setVisibility(View.VISIBLE);
		for (int j = 0; j < count; j++) {
			mDotsLayout.addView(initDot());
		}
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	private View initDot() {
		return LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.layout_dot, null);
	}

	private class GoodPicPagerAdapter extends PagerAdapter {
		private List<View> data;

		public GoodPicPagerAdapter(List<View> list) {
			this.data = list;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(data.get(position));
			return data.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(data.get(position));
		}

	}

}
