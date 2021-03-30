package com.example.oto01.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.example.oto01.NotificationUpdateActivity;
import com.example.oto01.NotificationUpdateActivity.ICallbackResult;
import com.example.oto01.R;
import com.example.oto01.SettingsActivity;

public class DownloadService extends Service {
	private static final int NOTIFY_ID = 0;
	private int progress;
	private NotificationManager mNotificationManager;
	private boolean canceled;
	private String apkUrl = "";
	// private String apkUrl = MyApp.downloadApkUrl;
	/* ���ذ�װ·�� */
	private static final String savePath = "/sdcard/EServiceStore/apk/";

	private static final String saveFileName = savePath + "eshop.apk";
	private ICallbackResult callback;
	private DownloadBinder binder;
	// private MyApp app;
	private boolean serviceIsDestroy = false;
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	private Context mContext = this;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				System.out.println("--------0->");
				// app.setDownload(false);
				SettingsActivity.isUpdate = false;
				mNotificationManager.cancel(NOTIFY_ID);
				installApk();
				break;
			case 2:
				System.out.println("--------2->");
				// app.setDownload(false);
				SettingsActivity.isUpdate = false;
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				System.out.println("--------1->");
				int rate = msg.arg1;
				// app.setDownload(true);
				SettingsActivity.isUpdate = true;
				if (rate < 100) {
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, rate + "%");
					contentview.setProgressBar(R.id.progressbar, 100, rate,
							false);
				} else {
					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					mNotification.contentView = null;
					Intent intent = new Intent(mContext,
							NotificationUpdateActivity.class);
					intent.putExtra("completed", "yes");
					PendingIntent contentIntent = PendingIntent.getActivity(
							mContext, 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					mNotification.setLatestEventInfo(mContext, "下载完成",
							"文件已下载完毕", contentIntent);
					serviceIsDestroy = true;
					stopSelf();
				}
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			}
		}
	};

	//
	// @Override
	// public int onStartCommand(Intent intent, int flags, int startId) {
	// // TODO Auto-generated method stub
	// return START_STICKY;
	// }

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("--------onBind------->");
		return binder;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("--------onDestroy------->");
		System.out.println("downloadservice ondestroy");
		// ���类����ˣ�������ζ�Ĭ��ȡ���ˡ�
		// app.setDownload(false);
		SettingsActivity.isUpdate = false;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("--------onUnbind------->");
		System.out.println("downloadservice onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("--------onRebind------->");

		super.onRebind(intent);
		System.out.println("downloadservice onRebind");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("--------onCreate------->");
		binder = new DownloadBinder();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// setForeground(true);
		// app = (MyApp) getApplication();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		System.out.println("--------onStart------->");
		apkUrl = intent.getStringExtra("apkpath");
		super.onStart(intent, startId);
	}

	public class DownloadBinder extends Binder {
		public void start() {
			if (downLoadThread == null || !downLoadThread.isAlive()) {

				progress = 0;
				setUpNotification();
				System.out.println("--------DownloadBinder------->");
				new Thread() {
					public void run() {
						// ����
						startDownload();
					};
				}.start();
			}
		}

		public void cancel() {
			canceled = true;
		}

		public int getProgress() {
			return progress;
		}

		public boolean isCanceled() {
			return canceled;
		}

		public boolean serviceIsDestroy() {
			return serviceIsDestroy;
		}

		public void cancelNotification() {
			mHandler.sendEmptyMessage(2);
		}

		public void addCallback(ICallbackResult callback) {
			DownloadService.this.callback = callback;
		}
	}

	private void startDownload() {
		// TODO Auto-generated method stub
		canceled = false;
		downloadApk();
	}

	//
	Notification mNotification;

	// ֪ͨ��
	/**
	 * ����֪ͨ
	 */
	private void setUpNotification() {
		System.out.println("--------setUpNotification------->");
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		;
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.download_notification_layout);
		contentView.setTextViewText(R.id.name, "eshop.apk 正在下载...");
		mNotification.contentView = contentView;

		Intent intent = new Intent(this, NotificationUpdateActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	//
	/**
	 * ����apk
	 * 
	 * @param url
	 */
	private Thread downLoadThread;

	private void downloadApk() {
		System.out.println("--------downloadApk------->");
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * ��װapk
	 * 
	 * @param url
	 */
	private void installApk() {
		System.out.println("--------installApk------->");
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
		callback.OnBackResult("finish");

	}

	private int lastRate = 0;
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				System.out.println("--------mdownApkRunnable------->");
				URL url = new URL(apkUrl);
				URLConnection conn = null;
				// HttpsURLConnection conn = (HttpsURLConnection)
				// url.openConnection();
				if (url.getProtocol().equals("http")) {
					conn = (HttpURLConnection) url.openConnection();
				} else if (url.getProtocol().equals("https")) {
					conn = (HttpsURLConnection) url.openConnection();
				}
				// HttpURLConnection conn = (HttpURLConnection)
				// url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1) {
						mHandler.sendMessage(msg);
						lastRate = progress;
						if (callback != null)
							callback.OnBackResult(progress);
					}
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(0);
						// 下载完了，cancelled也要设置
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				} while (!canceled);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (Exception e) {
				System.out.println("-----mdownApkRunnable-------->"
						+ e.toString());
				mNotificationManager.cancel(NOTIFY_ID);
				stopSelf();
				callback.OnBackResult("-1");
				System.out.println(e.toString());
				e.printStackTrace();
			}

		}
	};

}
