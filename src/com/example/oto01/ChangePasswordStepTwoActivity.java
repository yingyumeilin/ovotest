package com.example.oto01;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
/**
 * 修改密码界面
 * @author lqq
 *
 */
public class ChangePasswordStepTwoActivity extends BaseActivity {
	private EditText newPassEditText;//新密码
	private EditText oldPassEditText;//确认新密码
	private int shopsid;
	private Dialog dialog;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			dialog.dismiss();
			if(msg.arg1 == 0){
				ToastUtil.show(ChangePasswordStepTwoActivity.this, "修改密码成功！");
				LoginManager lm = LoginManager.getInstance(ChangePasswordStepTwoActivity.this);
				lm.delLogin();
				Intent intent = new Intent(ChangePasswordStepTwoActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}else {
				ToastUtil.show(ChangePasswordStepTwoActivity.this, msg.obj.toString());
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password_two);
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView(){
		LoginManager lm = LoginManager.getInstance(ChangePasswordStepTwoActivity.this);
		Login login = lm.getLoginInstance();
		 if (login != null && login.getAdminId() != -1) {
			 shopsid = login.getAdminId();
		 }
		 dialog = new Dialog(this, R.style.theme_dialog_alert);
		 dialog.setContentView(R.layout.window_layout);
		newPassEditText = (EditText)findViewById(R.id.newpass);
		oldPassEditText = (EditText)findViewById(R.id.oldpass);
	}
	
	/**
	 * 判断两次输入是否相同
	 */
	private void is_same(){
		final String newPass  = newPassEditText.getText().toString().trim();
		final String oldPass  = oldPassEditText.getText().toString().trim();
		if(!NetConn.checkNetwork(ChangePasswordStepTwoActivity.this)){
//			NetConn.setNetwork(OrdersActivity.this);
		}else{
			/*if(!newPass.trim().equals(pass)){
				ToastUtil.show(ChangePasswordStepTwoActivity.this, "两次输入密码不一样，请重新设置");
				return ;
			}*/
			if(newPass.length()==0){
				ToastUtil.show(ChangePasswordStepTwoActivity.this, "请输入新密码");
				return ;
			}
			if(oldPass.length()==0){
				ToastUtil.show(ChangePasswordStepTwoActivity.this, "请输入旧密码");
				return ;
			}
			if(newPass.length()<6||newPass.length()>16||oldPass.length()<6||oldPass.length()>16){
				ToastUtil.show(ChangePasswordStepTwoActivity.this, "密码长度在6-16位之间");
				return ;
			}else{
				dialog.show();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						SettingService service = new SettingService(ChangePasswordStepTwoActivity.this);
						int flag = service.changePass2(shopsid, oldPass, newPass);
						Message message = handler.obtainMessage();
						message.arg1 = flag ;
						message.obj = service.errorMsg;
						handler.sendMessage(message);
					}
				}).start();
			}
		}
		//FIXME 修改密码
	}
	

	/**
	 * 更新密码
	 * @param view
	 */
	public void update_pass(View view){
		is_same();
	}
	
}
