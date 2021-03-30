package com.example.oto01.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oto01.R;
import com.example.oto01.model.AlbumInfo;
import com.example.oto01.utils.TextUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * 创建时间：2015年4月15日 上午10:15:56 项目名称：ELife
 * 
 * @author G.G.Z
 * @version 1.0
 * @since jdk1.7.0_51 文件名称：PhotoAlbumAdapter.java 类说明：相册列表
 */
public class PhotoAlbumAdapter extends BaseAdapter {

	private Context context;
	private List<AlbumInfo> list;

	public PhotoAlbumAdapter(Context context, List<AlbumInfo> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public AlbumInfo getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final AlbumInfo item = getItem(position);
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_album_list, null);
			holder.mIv_album_img = (ImageView) view
					.findViewById(R.id.iv_album_img);
			holder.mTv_album_title = (TextView) view
					.findViewById(R.id.tv_album_title);
			holder.mTv_original_price = (TextView) view
					.findViewById(R.id.tv_original_price);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}


		holder.mTv_album_title.setText(TextUtil.getString(item.getName_album()));
		holder.mTv_original_price.setText("("+String.valueOf(item.getList().size())+")");
		ImageLoader.getInstance().displayImage(item.getPath_file(), holder.mIv_album_img,
				new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.default_image)
		.showImageForEmptyUri(R.drawable.default_image)
		.showImageOnFail(R.drawable.default_image).cacheInMemory()
		.cacheOnDisc().build());
		return view;
	}

	class ViewHolder {
		ImageView mIv_album_img;
		TextView mTv_album_title;
		TextView mTv_original_price;
	}

	// public void setData(List<ShopListRankBean> list) {
	// this.list = list;
	// }
	//
	// public List<ShopListRankBean> getData() {
	// return list;
	// }
	//
	// public void clearData() {
	// list.clear();
	// }
	//
	// public String getRankId() {
	// return rankId;
	// }
	//
	// public void setRankId(String rankId) {
	// this.rankId = rankId;
	// }

}
