package com.example.oto01.utils;

import android.content.Context;
import android.widget.Toast;

public final class ToastUtil {

	public static void show(Context cx, String msg) {
		Toast.makeText(cx, msg, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context cx, int resId) {
		Toast.makeText(cx, resId, Toast.LENGTH_SHORT).show();
	}
}