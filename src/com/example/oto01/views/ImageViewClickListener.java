package com.example.oto01.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.oto01.MessageListActivity;
import com.example.oto01.QRCodeActivity;
import com.example.oto01.R;
import com.example.oto01.SettingsActivity;

/**
 * 点击主界面的右侧导航图片监听事件
 * 
 * @author Administrator
 * 
 */
public class ImageViewClickListener implements OnClickListener {
	private Context context;

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.imageView_message:
			// 消息
			intent = new Intent(context, MessageListActivity.class);
			break;
		case R.id.imageView_setting:
			// 设置
			intent = new Intent(context, SettingsActivity.class);
			break;
		case R.id.imageView_twoCode:
			// 二维码
			intent = new Intent(context, QRCodeActivity.class);
			break;
		}
		if (intent != null) {
			context.startActivity(intent);
			((Activity) context).overridePendingTransition(
					R.anim.in_from_right, R.anim.out_to_left);
		}
	}

	public ImageViewClickListener(Context context) {
		this.context = context;
	}

}
