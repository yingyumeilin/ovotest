package com.example.oto01;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.example.oto01.adapter.PhotoAdapter;
import com.example.oto01.model.AlbumInfo;
import com.example.oto01.model.PhotoInfo;
import com.example.oto01.utils.ThumbnailsUtil;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.views.PhotoAlbumListPopuWindow;
import com.example.oto01.views.PhotoAlbumListPopuWindow.OnAlbumListPopuClickListener;

public class AlbumActivity extends BaseActivity implements OnAlbumListPopuClickListener{
	private GridView gvAlbum;
	private ContentResolver cr;
	private List<AlbumInfo> listImageInfo = new ArrayList<AlbumInfo>(); // 相册列表--包含所有照片
	private List<PhotoInfo> singleList = new ArrayList<PhotoInfo>(); // 单个相册图片
	private List<PhotoInfo> hasList = new ArrayList<PhotoInfo>(); // 已选照片
	private PhotoAdapter photoAdapter;
	private int hasSelect = 1;
	private RelativeLayout rlTitle;
	private PhotoAlbumListPopuWindow albumPop;   //相册列表
	private ImageView ivArrow;   //箭头

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		gvAlbum = (GridView) findViewById(R.id.gv_album);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
		ivArrow = (ImageView) findViewById(R.id.iv_arrow);
		cr = getContentResolver();
		listImageInfo.clear();
		hasList.clear();
		new ImageAsyncTask().execute();
		gvAlbum.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (singleList.get(position).isChoose() && hasSelect > 1) {
					singleList.get(position).setChoose(false);
					hasList.remove(singleList.get(position));
					hasSelect--;
				} else if (hasSelect < 2) {
					PhotoInfo info=singleList.get(position);
					if (info.getPath_absolute() != null
							&& (info.getPath_absolute().endsWith(".png") || info.getPath_absolute().endsWith(".PNG")
									|| info.getPath_absolute().endsWith(".jpg") || info.getPath_absolute().endsWith(".JPG"))){
						singleList.get(position).setChoose(true);
						hasList.add(singleList.get(position));
						hasSelect++;
					}else{
						ToastUtil.show(AlbumActivity.this, "上传的图片仅支持png或jpg格式");
					}
					
				} else {
//					ToastUtil.show(AlbumActivity.this, "最多选择1张图片！");
				}
				photoAdapter.refreshView(position);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			this.finish(); // finish当前activity
			// this.overridePendingTransition(R.anim.back_left_in,
			// R.anim.back_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	/**
	 * 显示相册
	 * 
	 * @param v
	 */
	public void btnShowAlbum(View v) {
		// TODO Auto-generated method stub
		if (albumPop != null) {
			albumPop.show(rlTitle);
		} else {
			albumPop = new PhotoAlbumListPopuWindow(this,listImageInfo);
			albumPop.setOnShopListRankPopuClickListener(this);
			albumPop.show(rlTitle);
			albumPop.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					ivArrow.setImageResource(R.drawable.arrow_close);
				}
			});
		}
		
		if(albumPop.isShowing()){
			ivArrow.setImageResource(R.drawable.arrow_open);
		}else{
			ivArrow.setImageResource(R.drawable.arrow_close);
		}
	}

	public void btnConfirm(View v) {
		// TODO Auto-generated method stub
		 Intent mIntent = new Intent();
         mIntent.putExtra("list", (Serializable)hasList);
         setResult(RESULT_OK, mIntent);
         finish();
	}
	
	private class ImageAsyncTask extends AsyncTask<Void, Void, Object> {

		@Override
		protected Object doInBackground(Void... params) {

			// 获取缩略图
			ThumbnailsUtil.clear();
			String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
					Thumbnails.DATA};
			Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
					null, null, null);

			if (cur != null && cur.moveToFirst()) {
				int image_id;
				String image_path;
				int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
				int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
				do {
					image_id = cur.getInt(image_idColumn);
					image_path = cur.getString(dataColumn);
					ThumbnailsUtil.put(image_id, "file://" + image_path);
				} while (cur.moveToNext());
			}

			// 获取原图
			Cursor cursor = cr.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
					null, "date_modified DESC");

			String _path = "_data";
			String _album = "bucket_display_name";

			HashMap<String, AlbumInfo> myhash = new HashMap<String, AlbumInfo>();
			AlbumInfo albumInfo = null;
			PhotoInfo photoInfo = null;
			if (cursor != null && cursor.moveToFirst()) {
				do {
					int index = 0;
					int _id = cursor.getInt(cursor.getColumnIndex("_id"));
					String path = cursor
							.getString(cursor.getColumnIndex(_path));
					String album = cursor.getString(cursor
							.getColumnIndex(_album));
					List<PhotoInfo> stringList = new ArrayList<PhotoInfo>();
					photoInfo = new PhotoInfo();
					if (myhash.containsKey(album)) {
						albumInfo = myhash.remove(album);
						if (listImageInfo.contains(albumInfo))
							index = listImageInfo.indexOf(albumInfo);
						photoInfo.setImage_id(_id);
						photoInfo.setPath_file("file://" + path);
						photoInfo.setPath_absolute(path);
						albumInfo.getList().add(photoInfo);
						listImageInfo.set(index, albumInfo);
						myhash.put(album, albumInfo);
					} else {
						albumInfo = new AlbumInfo();
						stringList.clear();
						photoInfo.setImage_id(_id);
						photoInfo.setPath_file("file://" + path);
						photoInfo.setPath_absolute(path);
						stringList.add(photoInfo);
						albumInfo.setImage_id(_id);
						albumInfo.setPath_file("file://" + path);
						albumInfo.setPath_absolute(path);
						albumInfo.setName_album(album);
						albumInfo.setList(stringList);
						listImageInfo.add(albumInfo);
						myhash.put(album, albumInfo);
					}
				} while (cursor.moveToNext());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			// if(getActivity()!=null){
			singleList.clear();
			singleList.addAll(listImageInfo.get(0).getList());
			photoAdapter = new PhotoAdapter(AlbumActivity.this, singleList, gvAlbum);
			gvAlbum.setAdapter(photoAdapter);
			// }
		}
	}

	@Override
	public void onAlbumListRankPopuClick(int position) {
		// TODO Auto-generated method stub
		hasSelect=1;
		hasList.clear();
		singleList.clear();
		singleList.addAll(listImageInfo.get(position).getList());
		photoAdapter.notifyDataSetChanged();
	}
	/************************************************************
	 * 点击返回事件
	 * @param view
	 */
	public void back_onClick(View view){
		finish();
	}
}
