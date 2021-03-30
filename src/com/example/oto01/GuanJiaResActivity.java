package com.example.oto01;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.model.DISInfo;
import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.OrderService;
import com.example.oto01.utils.NetConn;

public class GuanJiaResActivity extends BaseActivity {
	private DISInfo disInfo;
	private TextView resTextView , resMsgTextView , addressTextView  , reasonTextView;
	private TextView addressTitleTextView  , reasonTitleTextView;
	private Button shenqing;
	private static final int CHECK_GUANJIA = 1;
	private static final int GET_GUANJIA_STATUS = 2;
	private int shopsid = 1;
	private int again = -1;
	private RelativeLayout r1,r2;
	
	private  Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHECK_GUANJIA:
				if(msg.arg1 == 0){
					if (!NetConn.checkNetwork(GuanJiaResActivity.this)) {
					} else {
						initGuanJiaInfo();
					}
				}
//				finish();
				break;
			case GET_GUANJIA_STATUS:
				DISInfo dfo = (DISInfo)msg.obj;
				if(dfo!=null){
					disInfo = dfo;
					resMsgTextView.setText(disInfo.getMsg());
					if (disInfo.getStatus() == 1) {// 请求失败
						resTextView.setText("请求失败");
						
					} else if (disInfo.getStatus() == 2) {// 2：尚未审核开通
						resTextView.setText("尚未审核开通");
						again = 1;
						shenqing.setVisibility(View.VISIBLE);
						r1.setVisibility(View.VISIBLE);
						
					} else if (disInfo.getStatus() == 3) {// 3：审核已通过
						resTextView.setText("审核已通过");
						
						r2.setVisibility(View.VISIBLE);
						addressTextView.setText(disInfo.getAddress());
						addressTextView.setVisibility(View.VISIBLE);
						addressTitleTextView.setVisibility(View.VISIBLE);
						shenqing.setVisibility(View.GONE);
						r1.setVisibility(View.GONE);
						
					} else if (disInfo.getStatus() == 4) {// 4：审核中
						resTextView.setText("审核中");
						r2.setVisibility(View.VISIBLE);
						shenqing.setVisibility(View.GONE);
						r1.setVisibility(View.GONE);
						reasonTextView.setVisibility(View.GONE);
						reasonTitleTextView.setVisibility(View.GONE);
						
						addressTextView.setVisibility(View.GONE);
						addressTitleTextView.setVisibility(View.GONE);
						
					} else if (disInfo.getStatus() == 5) {// 5：审核未通过
						resTextView.setText("审核未通过");
						again = 2 ;
						r2.setVisibility(View.VISIBLE);
						reasonTextView.setText(disInfo.getState_result());
						reasonTextView.setVisibility(View.VISIBLE);
						reasonTitleTextView.setVisibility(View.VISIBLE);
						
						addressTextView.setText(disInfo.getAddress());
						addressTextView.setVisibility(View.VISIBLE);
						addressTitleTextView.setVisibility(View.VISIBLE);
						
						shenqing.setVisibility(View.VISIBLE);
					} else if (disInfo.getStatus() == 6) {// 6：请求e管家失败
						resTextView.setText("请求e管家失败");
						r2.setVisibility(View.VISIBLE);
						
					}
				}
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guan_jia_res);
		LoginManager lm = LoginManager.getInstance(GuanJiaResActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		initView();
		if (!NetConn.checkNetwork(GuanJiaResActivity.this)) {
		} else {
					initGuanJiaInfo();
		}
	}

	private void initGuanJiaInfo() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				OrderService service = new OrderService(GuanJiaResActivity.this);
				DISInfo disInfo = service.disRes(shopsid);
				Message message = handler.obtainMessage(GET_GUANJIA_STATUS);
				message.obj = disInfo;
				
				handler.sendMessage(message);
			}
		}).start();
	}
	private void initView(){
		r1 = (RelativeLayout) findViewById(R.id.r1);
		r2 = (RelativeLayout) findViewById(R.id.r2);
		resTextView = (TextView) findViewById(R.id.res);
		resMsgTextView = (TextView) findViewById(R.id.msg);
		addressTextView = (TextView) findViewById(R.id.address);
		addressTitleTextView = (TextView) findViewById(R.id.addressT);
		reasonTextView = (TextView) findViewById(R.id.reason);
		reasonTitleTextView = (TextView) findViewById(R.id.reasonT);
		shenqing = (Button) findViewById(R.id.shenqing);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guan_jia_res, menu);
		return true;
	}
	
	/**
	 * 申请开通
	 * @param view
	 */
	public void shenqing_onClick(View view){
		if(!NetConn.checkNetwork(GuanJiaResActivity.this)){
//			NetConn.setNetwork(OrdersActivity.this);
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					checkGuanJia();
					
				}
			}).start();
		}
	}
	/**
	 * 去申请
	 */
	private void checkGuanJia(){
		OrderService service  = new OrderService(this);
		int flag = service.houserRes(shopsid ,again);
		Message msg = handler.obtainMessage(CHECK_GUANJIA);
		msg.arg1 = flag;
		msg.obj = service.errorMsg;
		handler.sendMessage(msg);
	}
}
