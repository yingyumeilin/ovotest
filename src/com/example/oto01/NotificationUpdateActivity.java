package com.example.oto01;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oto01.services.DownloadService;
import com.example.oto01.services.DownloadService.DownloadBinder;
/**
 * 通知更新Activity
 * @author lqq
 *
 */
public class NotificationUpdateActivity extends BaseActivity {
	private Button btn_cancel;// btn_update,
	private TextView tv_progress;
	private DownloadBinder binder;
	private boolean isBinded;
	private ProgressBar mProgressBar;
	private String downloadUrl,mApkPath;
	//
	private boolean isDestroy = true;
//	private MyApp app;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
//		app = (MyApp) getApplication();
		// btn_update = (Button) findViewById(R.id.update);
		Intent intent = getIntent();
		mApkPath = intent.getStringExtra("apkpath");
		btn_cancel = (Button) findViewById(R.id.cancel);
		tv_progress = (TextView) findViewById(R.id.currentPos);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar1);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				binder.cancel();
				binder.cancelNotification();
				finish();
			}
		});
	}

	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBinded = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder = (DownloadBinder) service;
			isBinded = true;
			binder.addCallback(callback);
			binder.start();

		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println(" -----------isDestroy----->"+isDestroy);
		System.out.println(" ---------isUpdate----->"+SettingsActivity.isUpdate);
		if (isDestroy &&SettingsActivity.isUpdate) {
			Intent it = new Intent(NotificationUpdateActivity.this, DownloadService.class);
			it.putExtra("apkpath", mApkPath);
			startService(it);
			bindService(it, conn, Context.BIND_AUTO_CREATE);
		}
		System.out.println(" notification  onresume");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		System.out.println(" -----onNewIntent------isDestroy----->"+isDestroy);
		System.out.println(" ----onNewIntent-----isUpdate----->"+SettingsActivity.isUpdate);
		if (isDestroy && SettingsActivity.isUpdate) {
			Intent it = new Intent(NotificationUpdateActivity.this, DownloadService.class);
			it.putExtra("apkpath", mApkPath);
			startService(it);
			bindService(it, conn, Context.BIND_AUTO_CREATE);
		}
		System.out.println(" notification  onNewIntent");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println(" notification  onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isDestroy = false;
		System.out.println(" notification  onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBinded) {
			System.out.println(" onDestroy   unbindservice");
			unbindService(conn);
		}
		if (binder != null && binder.isCanceled()) {
			System.out.println(" onDestroy  stopservice");
			Intent it = new Intent(this, DownloadService.class);
//			it.putExtra("apkpath", mApkPath);
			stopService(it);
		}
	}

	private ICallbackResult callback = new ICallbackResult() {

		@Override
		public void OnBackResult(Object result) {
			// TODO Auto-generated method stub
			if ("finish".equals(result)) {
				finish();
				return;
			}else if("-1".equals(result)){
				finish();
				return;
			}
			int i = (Integer) result;
			mProgressBar.setProgress(i);
			mHandler.sendEmptyMessage(i);
		}

	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv_progress.setText("当前进度 ： " + msg.what + "%");
			

		};
	};

	public interface ICallbackResult {
		public void OnBackResult(Object result);
	}
}
