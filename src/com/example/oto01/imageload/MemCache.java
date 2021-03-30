package com.example.oto01.imageload;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;

public class MemCache {
	private static final int HARD_CACHE_CAPACITY = 30;
	private HashMap<String, Bitmap> mHardBitmapCache;
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_CACHE_CAPACITY / 2);

	public MemCache() {
		// 按从近期访问�?��到访问最多的顺序来保存元素�?排序模式accessOrder，该属�?为boolean型变量，对于访问顺序，为true�?
		mHardBitmapCache = new LinkedHashMap<String, Bitmap>(
				HARD_CACHE_CAPACITY / 2, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(
					LinkedHashMap.Entry<String, Bitmap> eldest) {
				if (size() > HARD_CACHE_CAPACITY) {
					// Entries push-out of hard reference cache are transferred
					// to soft reference cache
					mSoftBitmapCache.put(eldest.getKey(),
							new SoftReference<Bitmap>(eldest.getValue()));
					return true;
				} else
					return false;
			}
		};
	}

	public Bitmap getBitmapFromCache(String url) {
		if (url == null) {
			return null;
		}
		// 先从mHardBitmapCache缓存中获�?
		synchronized (mHardBitmapCache) {
			final Bitmap bitmap = mHardBitmapCache.get(url);
			if (bitmap != null) {
				// 如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是�?��被删�?
				mHardBitmapCache.remove(url);
				mHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}
		// 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
		SoftReference<Bitmap> reference = mSoftBitmapCache.get(url);
		if (reference != null) {
			final Bitmap bmp = reference.get();
			if (bmp != null) {
				// 将图片移回硬缓存
				mHardBitmapCache.put(url, bmp);
				mSoftBitmapCache.remove(url);
				return bmp;
			} else {
				mSoftBitmapCache.remove(url);
			}
		}
		return null;
	}

	public void addBitmapToCache(String url, Bitmap bmp) {
		if (bmp != null) {
			synchronized (mHardBitmapCache) {
				mHardBitmapCache.put(url, bmp);
			}
		}
	}
}