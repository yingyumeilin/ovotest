package com.example.oto01;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/****************************************************************
 * 载入WEB广告
 * 
 * @author G.G.Z
 */
public class LoadWebActivity extends BaseActivity {
	private WebView wv;
	private Handler handler;
	private Dialog mProgressDialog;
	private String url;
	private String title;
	private TextView tvTitle;
	private boolean strUrl = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadweb);
		init();// 执行初始化函数
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		strUrl = getIntent().getBooleanExtra("strUrl", false);
//		if (title != null) {
//			tvTitle = (TextView) findViewById(R.id.tv_title);
//			tvTitle.setText(title);
//		}
		System.out.println("---strUrl---" + strUrl);
		System.out.println("---strUrl---" + url);
		/** 判断是否加载纯文本 */
		if (!strUrl) {
			wv.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					wv.loadUrl(url);
				}
			});
//			loadurl(wv, url);// url
		} else {
			wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			wv.getSettings().setDefaultTextEncodingName("utf-8");
			String newStr = "<html>" + url + "</html>";
			wv.loadData(newStr, "text/html; charset=UTF-8", null);// 加载文本
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
	public void init() {
		mProgressDialog = new Dialog(LoadWebActivity.this,
				R.style.theme_dialog_alert);
		mProgressDialog.setContentView(R.layout.window_layout);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case 0:
						try {
							mProgressDialog.show();
						} catch (Exception e) {
						}
						break;
					case 1:
						mProgressDialog.dismiss();
						break;
					}
				}
				super.handleMessage(msg);
			}
		};

		// WebView
		wv = (WebView) findViewById(R.id.wv);
		wv.getSettings().setJavaScriptEnabled(true);// 可用JS
		wv.setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {

//				loadurl(view, url);// 载入网页
				wv.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						wv.loadUrl(url);
					}
				});
				return true;
			}// 重写点击动作,用webview载入

		});
		wv.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
				if (progress == 100) {
					handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
				}
				super.onProgressChanged(view, progress);
			}
		});
	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键 if
	 * ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) { wv.goBack();
	 * return true; }else if(keyCode == KeyEvent.KEYCODE_BACK){
	 * ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认 return true; } return
	 * super.onKeyDown(keyCode, event); } public void ConfirmExit(){//退出确认
	 * AlertDialog.Builder ad=new AlertDialog.Builder(OrderWebActivity.this);
	 * ad.setTitle("退出"); ad.setMessage("是否退出软件?"); ad.setPositiveButton("是",
	 * new DialogInterface.OnClickListener() {//退出按钮
	 * 
	 * @Override public void onClick(DialogInterface dialog, int i) {
	 * //MyData.flag = 0; exit();
	 * 
	 * } }); ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int i) {
	 * //不退出不用执行任何操作 } }); ad.show();//显示对话框 }
	 */

	public void loadurl(final WebView view, final String url) {
		new Thread() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
				view.loadUrl(url);// 载入网页
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back_onClick(View v) {
		finish();
	}
}
