package com.example.oto01;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.oto01.model.Constant;
import com.example.oto01.utils.CallPhoneUtil;
import com.example.oto01.utils.VersionUtil;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView tvVersion = (TextView) findViewById(R.id.versionnumber);
		String versionName = VersionUtil.getVersionName(this);

		String webUrl = Constant.webHost;
		String versionClass = ""; // 版本类型
		if (webUrl.contains("dev")) { // 开发版
			versionClass = "开发版";
		} else if (webUrl.contains("moni")) { // 模拟版
			versionClass = "模拟版";
		} else if (webUrl.contains("wu.me")) { // 线上版
		} else if (webUrl.contains("O2O_test")) {
			versionClass = "阿里云版";
		} else if (webUrl.contains("test")) {
			versionClass = "测试版";
		}

		// tvVersionClass.setText(versionClass);
		tvVersion.setText("V" + versionName + versionClass);
	}

	public void call_onClick(View view) {
		showInfo_call("4000789000");
	}

	/**
	 * 显示提醒取消打电话对话框
	 */
	private void showInfo_call(final String phone) {
		// FIXME　此处需要修改
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.cancel_cancel, null);
		final Dialog dialog = new Dialog(AboutActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		TextView textView = (TextView) layout.findViewById(R.id.type_name);
		textView.setText(phone + "");
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CallPhoneUtil.callPhone(AboutActivity.this, phone);
				dialog.dismiss();
			}
		});
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();
	}

}
