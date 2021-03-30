package com.example.oto01;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.oto01.model.Constant;

/**
 * 阅读并同意Activity
 * 
 * @author lqq
 * 
 */
public class ReadAndAgreeEAccountActivity extends BaseActivity {
	private WebView webView;
	private TextView tvTitle;
	private int tag = -1;
	private String url = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_and_agree_e_account);
		tvTitle = (TextView) findViewById(R.id.title_font);
		tag = this.getIntent().getIntExtra("tag", -1);
		if (tag == 1) {
			tvTitle.setText("社区e商户协议");
			url = "file:///android_asset/read.html";
		} else if (tag == 2) {
			tvTitle.setText("软件许可及使用协议");
			url = "file:///android_asset/read2.html";
		}else if (tag==3) {
			tvTitle.setText("“凤凰宝”业务客户服务协议");
			url = "file:///android_asset/read3.html";
		}else if(tag==4){
			tvTitle.setText("北京农商行“凤凰宝”");
			url = Constant.webHost+"?m=ApiPay&a=cashier_desc";
		}
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);// 设置支持Javascript
		webView.requestFocus();// 触摸焦点起作用
		// 　　setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);// 取消滚动条
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		webView.loadUrl(url);// 本地文件，本地文件存放在：assets文件中
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}
}
