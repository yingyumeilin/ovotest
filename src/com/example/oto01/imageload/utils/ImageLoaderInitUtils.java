package com.example.oto01.imageload.utils;

import java.io.File;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * IimageLoader初始化
 * 
 * @author chenjia
 */
public class ImageLoaderInitUtils {
	@SuppressWarnings("deprecation")
	public static void ImageLoaderInit(Context context) {
		if (!ImageLoadTool.checkImageLoader()) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.threadPriority(Thread.NORM_PRIORITY)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.discCacheFileCount(50)
					// 缓存文件的最大个数
					// .discCache(new
					// LimitedAgeDiscCache(StorageUtils.getCacheDirectory(context),null,
					// new Md5FileNameGenerator(), discCacheLimitTime))
					.discCache(
							new UnlimitedDiskCache(new File("mnt/sdcard/o2ov2")))
					.discCacheFileCount(100)
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();
			ImageLoader.getInstance().init(config);
		}
	}
}
