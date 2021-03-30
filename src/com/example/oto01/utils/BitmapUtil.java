package com.example.oto01.utils;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BitmapUtil {

	/**
	 * 閫氳繃id鑾峰彇鎸囧畾楂樺害锛屽搴︾殑浣嶅浘
	 */
	public static Bitmap ReadBitmapById(Context mContext, int abc, int mWindth,
			int mSurfaceHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(mContext.getResources(), abc, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, mWindth,
				mSurfaceHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				abc, options);
		if (mWindth != 0 && mSurfaceHeight != 0) {
			return Bitmap.createScaledBitmap(bitmap, mWindth, mSurfaceHeight,
					true);

		}
		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * bitmap 杞�byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();

	}

	/**
	 * byte[] 杞�bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 姘村嵃
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 鍒涘缓涓�釜鏂扮殑鍜孲RC闀垮害瀹藉害涓�牱鐨勪綅鍥�
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 鍦�0锛�鍧愭爣寮�鐢诲叆src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 鍦╯rc鐨勫彸涓嬭鐢诲叆姘村嵃
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 淇濆瓨
		// store
		cv.restore();// 瀛樺偍
		return newb;
	}
}
