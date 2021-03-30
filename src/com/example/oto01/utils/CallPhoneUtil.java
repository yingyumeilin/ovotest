package com.example.oto01.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CallPhoneUtil {

	@SuppressWarnings("null")
	public static void callPhone(Activity cx, String phoneNum) {
		if (phoneNum != null || phoneNum.length() > 1) {
			Uri uri = Uri.parse("tel:" + phoneNum);
			cx.startActivity(new Intent(Intent.ACTION_CALL, uri));
		}
	}
}