package com.example.oto01.imageload.utils;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.BaseActivity;
import com.example.oto01.R;
import com.example.oto01.model.Constant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 图片加载activity视图
 * 
 * @author chenjia
 */
public class ImageLoadView extends BaseActivity {
	@ViewInject(R.id.imageview_load_vp)
	private ViewPager imageview_load_vp;
	@ViewInject(R.id.show_size)
	private TextView tv_show_size;

	private String loadFile = "file://";

	private boolean isNet;
	private ArrayList<String> loadPaths;
	private int position;

	@OnClick(R.id.imgpage_imgview)
	public void clikc(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_load_layout);
		ViewUtils.inject(this);
		initIntentData();
		loadImageData();
	}

	@OnItemClick(R.id.imageview_load_vp)
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		finish();
	}

	/**
	 * 初始化图片加载的数据以及路径
	 */
	private void initIntentData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		isNet = intent.getBooleanExtra("isNet", true);
		loadPaths = intent.getStringArrayListExtra("loadPath");
		position = intent.getIntExtra("postion", 0);
		tv_show_size.setText(1 + "/" + loadPaths.size());
	}

	/**
	 * 开始加载图片
	 */
	private void loadImageData() {
		// TODO Auto-generated method stub
		ImageLoaderInitUtils.ImageLoaderInit(this);
		if (isNet) {// 为网络图片
			loadFile = Constant.webHost;
		}
		imageview_load_vp.setAdapter(new ImageLoadPageAdapter());
		imageview_load_vp.setOnPageChangeListener(new MyOnPageChangeListener());
		imageview_load_vp.setCurrentItem(position);
	}

	/**
	 * 图片展示PagerAdapter
	 * 
	 * @author chenjia
	 */
	private class ImageLoadPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return loadPaths.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			// TODO Auto-generated method stub
			return view == obj;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			RelativeLayout view = (RelativeLayout) View.inflate(
					ImageLoadView.this, R.layout.item_imgpage_view, null);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.imgpage_imgview);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			final ProgressBar progressBar = (ProgressBar) view
					.findViewById(R.id.imgpage_progressbar);
			ImageLoadTool.disPlay(loadFile + loadPaths.get(position),
					imageView, R.drawable.ic_launcher, new ProgressListener() {
						@Override
						public void loadFinish() {// 加载完成
							// TODO Auto-generated method stub
							progressBar.setVisibility(View.GONE);
						}
					});
			((ViewPager) container).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			// TODO Auto-generated method stub
			container.removeView((View) obj);
		}
	}

	/**
	 * Change
	 * 
	 * @author chenjia
	 */
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int postion) {
			tv_show_size.setText(postion + 1 + "/" + loadPaths.size());
		}
	}

	public interface ProgressListener {
		public void loadFinish();
	}
}