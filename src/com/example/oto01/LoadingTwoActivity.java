package com.example.oto01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 启动页面
 * 
 * @author lqq
 * 
 */
public class LoadingTwoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_two);
	}

	/**
	 * 新开店铺
	 * 
	 * @param view
	 */
	public void open_shop(View view) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RegisterStepOneActivity.class);
		intent.putExtra("type", "loading");
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		finish();
	}

	/**
	 * LOGIN
	 * 
	 * @param view
	 */
	public void login_onClick(View view) {
		Intent intent = new Intent(LoadingTwoActivity.this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		finish();
	}

}
