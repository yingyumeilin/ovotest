package com.example.oto01.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.oto01.R;
import com.example.oto01.model.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * 相片适配器
 * 
 * @author GuiLin
 */
public class PhotoAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<PhotoInfo> list;
	private ViewHolder viewHolder;
	private GridView gridView;
	private int width ;
	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new SimpleBitmapDisplayer()).build();

	public PhotoAdapter(Context context, List<PhotoInfo> list, GridView gridView) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.gridView = gridView;
		 DisplayMetrics dm = context.getResources().getDisplayMetrics();
		 width = dm.widthPixels/ 3;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 刷新view
	 * 
	 * @param index
	 */
	public void refreshView(int index) {
		int visiblePos = gridView.getFirstVisiblePosition();
		View view = gridView.getChildAt(index - visiblePos);
		ViewHolder holder = (ViewHolder) view.getTag();

		if (list.get(index).isChoose()) {
			holder.selectImage.setVisibility(View.VISIBLE);
		} else {
			holder.selectImage.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_selectphoto, null);
			viewHolder.image = (ImageView)convertView.findViewById(R.id.imageView);
			viewHolder.selectImage = (ImageView)convertView.findViewById(R.id.selectImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.get(position).isChoose()){
			viewHolder.selectImage.setVisibility(View.VISIBLE);
		}else{
			viewHolder.selectImage.setVisibility(View.INVISIBLE);
		}
		LayoutParams layoutParams = viewHolder.image.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width;
		viewHolder.image.setLayoutParams(layoutParams);
		final PhotoInfo photoInfo = list.get(position);
		if(photoInfo!=null){
//			UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),photoInfo.getPath_file()), 
//					new RotateImageViewAware(viewHolder.image,photoInfo.getPath_absolute()), R.drawable.common_defalt_bg);
//			UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),photoInfo.getPath_file()),
//					viewHolder.image, R.drawable.common_defalt_bg);
			ImageLoader.getInstance().displayImage(photoInfo.getPath_file(), viewHolder.image,
					new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.default_image)
			.showImageForEmptyUri(R.drawable.default_image)
			.showImageOnFail(R.drawable.default_image).cacheInMemory()
			.cacheOnDisc().build());
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		public ImageView selectImage;
	}
}
