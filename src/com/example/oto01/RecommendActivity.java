package com.example.oto01;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
/**
 * 推荐Activity
 * @author lqq
 *
 */
public class RecommendActivity extends BaseActivity {
	private static final int SHOW_RECODE = 1;
	private int shopsid = 0;
	private String recode;
	private TextView myRecommendTextView;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RECODE:
				if(msg.obj!=null){
					recode = (String)msg.obj;
					myRecommendTextView.setText(recode);
					if(recode!=null&&recode.length()>0){
						myRecommendTextView.setText(recode);
					}else{
						myRecommendTextView.setText("暂无");
					}
				}else{
					myRecommendTextView.setText("暂无");
				}
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		LoginManager loginManager = LoginManager.getInstance(RecommendActivity.this);
		Login login = loginManager.getLoginInstance();
		shopsid = login.getAdminId();
		myRecommendTextView = (TextView) findViewById(R.id.mycode);
		searchRecode();
	}


	/**
	 * 查看推荐用户列表
	 * @param view
	 */
	public void search_user_onClick(View view){
		int tag = 1;
		Intent intent = new Intent(RecommendActivity.this, RecommedListActivity.class);
		intent.putExtra("tag", tag);
		System.out.println("--------recode------->"+recode);
		intent.putExtra("recode", recode);
		startActivity(intent);
	}
	
	/**
	 * 查找我的推荐码
	 */
	private void searchRecode(){
		if(!NetConn.checkNetwork(RecommendActivity.this)){
		}else{
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					SettingService service = new SettingService(RecommendActivity.this);
					String myCode = service.getMyCode(shopsid);
					Message message = handler.obtainMessage(SHOW_RECODE);
					message.obj = myCode;
					handler.sendMessage(message);
				}
			}).start();
		}
	}
	
	/**
	 * 返回
	 * @param view
	 */
	public void back_onClick(View view){
		finish();
	}
	
	/**
	 * 查看推荐商家列表
	 * @param view
	 */
	public void search_shop_onClick(View view){
		int tag = 2;
		Intent intent = new Intent(RecommendActivity.this, RecommedListActivity.class);
		intent.putExtra("tag", tag);
		intent.putExtra("recode", recode);
		startActivity(intent);
	}
}
