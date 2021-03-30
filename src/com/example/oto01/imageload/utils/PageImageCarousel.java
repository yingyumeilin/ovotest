package com.example.oto01.imageload.utils;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.example.oto01.R;
import com.example.oto01.model.Constant;
/**、
 * 图片轮播
 * @author chenjia
 */
public class PageImageCarousel extends PagerAdapter{
	private ArrayList<String> loadPaths;
	private Context context;
	
	public PageImageCarousel(ArrayList<String> loadPaths,Context context) {
		// TODO Auto-generated constructor stub
		this.loadPaths = loadPaths;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return loadPaths.size();
	}
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view==obj;
	}
	
	@Override
	public Object instantiateItem(View container, final int position) {
		// TODO Auto-generated method stub
		LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		ImageView iView = new ImageView(context);
		iView.setScaleType(ScaleType.CENTER_INSIDE);
		iView.setLayoutParams(layoutParams);
		ImageLoadTool.disPlay(Constant.webHost+loadPaths.get(position), iView, R.drawable.default_good_img);
//		ImageLoadTool.disPlay(loadPaths.get(position), iView, R.drawable.empty_photo);
		((ViewPager) container).addView(iView, 0);
		iView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ImageLoadView.class);
				intent.putStringArrayListExtra("loadPath", loadPaths);
				intent.putExtra("isNet", true);
				intent.putExtra("postion", position);
				context.startActivity(intent);
			}
		});
		return iView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object obj) {
		// TODO Auto-generated method stub
		container.removeView((View) obj);
	}

}
