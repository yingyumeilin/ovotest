package com.example.oto01.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class TextUtil {

	public static String getString(String string) {
		if (!TextUtils.isEmpty(string)) {
			return string;
		} else {
			return "";
		}
	}

	public static float getScreenHeight(Context context) {

		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int height = dm.heightPixels;

		return height;
	}

}
