package com.example.oto01;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SettingService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
/**
 * 问题反馈
 * @author lqq
 *
 */
public class FeedbackActivity extends BaseActivity {
	private EditText contactEditText;
	private EditText contentEditText;
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				ToastUtil.show(FeedbackActivity.this, "发送成功！");
				
				finish();
				break;
			case 1:
				ToastUtil.show(FeedbackActivity.this, "发送失败,联系方式格式不正确！");
				break;
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initViews();
	}
 
	/**
	 * 初始化视图
	 */
	private void initViews(){
		contentEditText = (EditText) findViewById(R.id.feed_back_content);
		contactEditText = (EditText) findViewById(R.id.body_contact);
		LoginManager lm = LoginManager.getInstance(FeedbackActivity.this);
		String contact = lm.getContact();
		if(contact!=null){
			contactEditText.setText(contact);
		}
	}
	
	/**
	 * 点击发送
	 * @param view
	 */
	public void send_onClick(View view) {
		if(contentEditText.getText().toString().trim().length()==0){
			ToastUtil.show(FeedbackActivity.this, "内容不能为空！");
			return ;
		}
//		if(contactEditText.getText().toString().trim().length()==0){
//			ToastUtil.show(FeedbackActivity.this, "联系方式不能为空！");
//			return ;
//		}
		if(contentEditText.getText().toString().trim().length()>200){
			ToastUtil.show(FeedbackActivity.this, "内容长度不能超过200个字符！");
			return ;
		}
//		String str = contactEditText.getText().toString().trim();
//		Pattern phonePattern = Pattern.compile("(\\+86|86|0086)?((13[0-9]|15[0-35-9]|14[57]|18[0-9])\\d{8})");
//		Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
//		Pattern qqPattern = Pattern.compile("[1-9][0-9]{5,9}");
//		Matcher phoneMatcher = phonePattern.matcher(str);
//		Matcher emailMatcher = emailPattern.matcher(str);
//		Matcher qqMatcher = qqPattern.matcher(str);
//		boolean phoneFlag = phoneMatcher.matches();
//		boolean emailFlag = emailMatcher.matches();
//		boolean qqFlag = qqMatcher.matches();
//		System.out.println("----phoneFlag---->"+phoneFlag);
//		System.out.println("----emailFlag---->"+emailFlag);
//		System.out.println("----qqFlag---->"+qqFlag);
//		if((!phoneFlag)&&(!emailFlag)&&(!qqFlag)){
//			ToastUtil.show(FeedbackActivity.this, "联系方式不正确！");
//			return ;
//		}
		
		/*if(contactEditText.getText().toString().trim().getBytes().length !=11){
			//FIXME 为邮箱格式验证
			if(!str.contains("@")){
				ToastUtil.show(FeedbackActivity.this, "联系方式不正确！");
				return ;
			}
			
		}else{
			for(int i=0;i<str.getBytes().length;i++){
				char c = str.charAt(i);
				if(c>'9'||c<'0'){
					ToastUtil.show(FeedbackActivity.this, "联系方式不正确！");
					return ;
				}
			}
		}*/
		if(!NetConn.checkNetwork(FeedbackActivity.this)){
			
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String contway = contactEditText.getText().toString() ;
					String content = contentEditText.getText().toString() ;
					LoginManager lm = LoginManager.getInstance(FeedbackActivity.this);
					lm.saveContact(contway);
					SettingService service=new SettingService(FeedbackActivity.this);
					boolean flag = service.feedBack(contway, content);
					System.out.println("-----msg------>"+flag);
					Message message=handler.obtainMessage();
					message.arg1 = flag==true?0:1;
					System.out.println("-----msg------>"+message.arg1);
					handler.sendMessage(message);
				}
			}).start();
		}
	}
	

}