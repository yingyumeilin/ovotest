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

import com.example.oto01.model.Messages;
import com.example.oto01.services.MessageService;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.NetConn;

public class MessageDetailsActivity extends BaseActivity {
	private Dialog proDialog;
	private WebView wv;
	private TextView msg_title;
	private TextView msg_time;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			showRes((Messages) msg.obj);
		};
	};

	private void showRes(Messages messages) {
		if (messages != null) {
			msg_title.setText(messages.getTitle());
			msg_time.setText(DateUtil.getFormatedDate_3(messages.getAddtime()));
			wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			wv.getSettings().setDefaultTextEncodingName("utf-8");
			String newStr = "<html>" + messages.getContent() + "</html>";
			wv.loadData(newStr, "text/html; charset=UTF-8", null);// 加载文本
		} else {
		}
		proDialog.dismiss();
	}

	//
	/**
	 * 初始化数据
	 */
	private void initData() {
		if (!NetConn.checkNetwork(MessageDetailsActivity.this)) {
		} else {
			proDialog = new Dialog(this, R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
			proDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					MessageService messageService = new MessageService(
							MessageDetailsActivity.this);
					Messages messages = messageService.getMessage(getIntent()
							.getIntExtra("msg_id", 1));
					Message message = handler.obtainMessage();
					message.obj = messages;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadweb);
		init();// 执行初始化函数
		msg_title = (TextView) findViewById(R.id.msg_title);
		msg_time = (TextView) findViewById(R.id.msg_time);
		initData();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
	public void init() {

		// WebView
		wv = (WebView) findViewById(R.id.wv);
		wv.getSettings().setJavaScriptEnabled(true);// 可用JS
		wv.setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				loadurl(view, url);// 载入网页
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

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void btnBack(View v) {
		this.finish();
	}

	public void loadurl(final WebView view, final String url) {

		view.post(new Runnable() {
			public void run() {
				handler.sendEmptyMessage(0);
				view.loadUrl(url);
			}
		});
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
