package com.example.oto01.imageload.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.oto01.imageload.utils.ImageLoadView.ProgressListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图片加载类
 * 
 * @author chenjia
 */
public class ImageLoadTool {
	private static ImageLoader imageLoader = ImageLoader.getInstance();

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static boolean checkImageLoader() {
		return imageLoader.isInited();
	}

	/**
	 * 图片加载的方法
	 * 
	 * @param uri
	 *            记载的网络请求地址
	 * @param imageView
	 *            显示的图片控件
	 * @param default_pic
	 *            默认的图片资源
	 */
	@SuppressWarnings("deprecation")
	public static void disPlay(String uri, ImageView imageView, int default_pic) {
		try {

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(default_pic)
					.showImageForEmptyUri(default_pic)
					.showImageOnFail(default_pic).cacheInMemory(true)
					.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new SimpleBitmapDisplayer()).build();
			imageLoader.displayImage(uri, imageView, options);
		} catch (OutOfMemoryError e) {
			ImageLoadTool.clear();
		}
	}

	/**
	 * 图片加载的方法
	 * 
	 * @param uri
	 *            记载的网络请求地址
	 * @param imageView
	 *            显示的图片控件
	 * @param default_pic
	 *            默认的图片资源
	 * @param listener
	 *            加载的接口
	 */
	@SuppressWarnings("deprecation")
	public static void disPlay(String uri, ImageView imageView,
			int default_pic, final ProgressListener listener) {
		try {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(default_pic)
					.showImageForEmptyUri(default_pic)
					.showImageOnFail(default_pic).cacheInMemory(true)
					.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new SimpleBitmapDisplayer()).build();
			imageLoader.displayImage(uri, imageView, options,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							// TODO Auto-generated method stub
							// 加载成功的时候执行
							listener.loadFinish();
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}
					});
		} catch (OutOfMemoryError e) {
			ImageLoadTool.clear();
		}
	}

	@SuppressWarnings("deprecation")
	public static void clear() {
		imageLoader.clearMemoryCache();
		imageLoader.clearDiscCache();
	}

	public static void resume() {
		imageLoader.resume();
	}

	/**
	 * 暂停加载
	 */
	public static void pause() {
		imageLoader.pause();
	}

	/**
	 * 停止加载
	 */
	public static void stop() {
		imageLoader.stop();
	}

	/**
	 * 销毁加载
	 */
	public static void destroy() {
		imageLoader.destroy();
	}
}
