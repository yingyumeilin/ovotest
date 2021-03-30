package com.example.oto01.model;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import com.example.oto01.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class DeviceResolution {

	public static final int ShOP_CAR_COUNT = 50;
	public static final int BASE_SCREEN_WIDTH = 640;
	public static final int BASE_SCREEN_HEIGHT = 960;

	public static int screenWidth;
	public static int screenHeight;
	public static float screenScale;
	public static float screenHeightScale;
	public static float screenWidthScale;

	public static void InitValue(Context context) {

		initScreenSize(context);
	}

	public static void initScreenSize(Context context) {

		initImageLoader(context);

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		if (screenWidth == 0) {
			screenWidth = BASE_SCREEN_WIDTH;
		}
		if (screenHeight == 0) {
			screenHeight = BASE_SCREEN_HEIGHT;
		}

		if (screenWidth * 1f / screenHeight < BASE_SCREEN_WIDTH * 1f
				/ BASE_SCREEN_HEIGHT) {
			screenScale = screenWidth * 1f / BASE_SCREEN_WIDTH;
		} else {
			screenScale = screenHeight * 1f / BASE_SCREEN_HEIGHT;
		}

		screenWidthScale = screenWidth * 1f / BASE_SCREEN_WIDTH;
		screenHeightScale = screenHeight * 1f / BASE_SCREEN_HEIGHT;
	}

	private static void initImageLoader(Context context) {

		int memoryCacheSize = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(memoryCacheSize)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(
						new DisplayImageOptions.Builder()
								.showStubImage(R.drawable.default_good_img)
								.showImageForEmptyUri(
										R.drawable.default_good_img)
								.showImageOnFail(R.drawable.default_good_img)
								.cacheInMemory().cacheOnDisc().build()).build();
		ImageLoader.getInstance().init(config);
	}
}
