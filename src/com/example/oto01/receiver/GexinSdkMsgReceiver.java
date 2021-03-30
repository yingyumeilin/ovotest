package com.example.oto01.receiver;

import java.io.IOException;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.device.PrinterManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.oto01.LoginActivity;
import com.example.oto01.MainActivity;
import com.example.oto01.OrderDetailsActivity;
import com.example.oto01.R;
import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.PrintBillService;
import com.example.oto01.services.RequestServices;
import com.igexin.sdk.Consts;

public class GexinSdkMsgReceiver extends BroadcastReceiver {
	private static final int RANDOM_NUMBER = 1000000;
	private int shopsid = -1;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private AssetManager asm;
	private NotificationManager mNotificationManager;
	private String noticeClues;
	private Intent newIntent;
	private String orderId = "";
	private String order_type;

	private void playMusic() {
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = asm.openFd("dbl.mp3");
			mMediaPlayer.reset();
			/* 设置要播放的文件的路径 */
			mMediaPlayer
					.setDataSource(fileDescriptor.getFileDescriptor(),
							fileDescriptor.getStartOffset(),
							fileDescriptor.getLength());
			/* 准备播放 */
			mMediaPlayer.prepare();
			/* 开始播放 */
			mMediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void stopMusic() {
		/* 是否正在播放 */
		if (mMediaPlayer.isPlaying()) {
			// 重置MediaPlayer到初始状态
			mMediaPlayer.reset();
		}
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		mNotificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Bundle bundle = intent.getExtras();
		LoginManager lm = LoginManager.getInstance(context);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		asm = context.getResources().getAssets();
		Log.d("GexinSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		System.out.println("------action---->" + bundle.getInt("action"));

		newIntent = new Intent();
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

		switch (bundle.getInt(Consts.CMD_ACTION)) {

		case Consts.GET_MSG_DATA:
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				String data = new String(payload);
				System.out.println("@@@透传----->" + data);
				Log.d("GexinSdkMsgReceiver", "透传---" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					int isDown = 0;
					if (!jsonObject.isNull("isdown")) {
						isDown = jsonObject.getInt("isdown");
					}
					if (isDown == 1) {
						String orderNum = "";
						if (!jsonObject.isNull("ordernum")) {
							orderNum = jsonObject.getString("ordernum");
							System.out.println("----ordernum----->" + orderNum);
						}
						String payment = "";
						if (!jsonObject.isNull("payment")) {
							payment = jsonObject.getString("payment");
							System.out.println("----payment----->" + payment);
							if ("1".equals(payment)) {
								noticeClues = "接到订单" + orderNum
										+ ",付款方式为货到付款,请尽快处理。";
							} else {
								noticeClues = "接到订单" + orderNum
										+ ",付款方式为电子支付（未到账），请妥善处理。";
							}
						}
						if (!jsonObject.isNull("orderid")) {
							orderId = jsonObject.getString("orderid");
							System.out.println("----orderid----->" + orderId);
						}
						if (!jsonObject.isNull("order_type")) {
							order_type = jsonObject.getString("order_type");
							System.out.println("----orderid----->" + orderId);
						}

						new Thread() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								super.run();
								try {
									sleep(1000);
									setUpNotification(context, noticeClues,
											orderId, order_type);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();

						try {
							PrinterManager printer = new PrinterManager();
						} catch (Error e) {
							e.printStackTrace();
							return;
						}

						Intent intentService = new Intent(context,
								PrintBillService.class);
						intentService.putExtra("order_id", orderId);
						context.startService(intentService);
					} else {
						if (shopsid != -1) {
							newIntent.putExtra("from", "message");
							newIntent.setClass(context, MainActivity.class);
							intent.putExtra("login", "1");
						} else {
							newIntent.setClass(context, LoginActivity.class);
							intent.putExtra("login", "1");
						}
						context.startActivity(newIntent);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				if (shopsid != -1) {
					newIntent.putExtra("from", "message");
					newIntent.setClass(context, MainActivity.class);
				} else {
					newIntent.setClass(context, LoginActivity.class);
				}
				context.startActivity(newIntent);
			}
			break;
		case Consts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			final String cid = bundle.getString("clientid");
			System.out.println("------获取clientid----->" + cid);
			// if (GexinSdkDemoActivity.tView != null)
			// GexinSdkDemoActivity.tView.setText(cid);

			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("-------start------->" + cid);
					if (cid.length() > 0) {
						System.out.println("-----开始访问接口----->" + cid);
						RequestServices requestServices = new RequestServices(
								context);
						requestServices.request(cid, shopsid);
					}
				}
			}).start();
			break;

		case Consts.BIND_CELL_STATUS:
			String cell = bundle.getString("cell");

			System.out.println("-------BIND_CELL_STATUS----------");

			System.out.println("------获取cell----->" + cell);
			Log.d("GexinSdkDemo", "BIND_CELL_STATUS:" + cell);
			// if (GexinSdkDemoActivity.tLogView != null)
			// GexinSdkDemoActivity.tLogView.append("BIND_CELL_STATUS:" + cell +
			// "\n");
			break;
		case Consts.THIRDPART_FEEDBACK:
			// sendMessage接口调用回执
			String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			System.out.println("-------THIRDPART_FEEDBACK----------");

			System.out.println("------获取appid----->" + appid);
			System.out.println("------获取taskid----->" + taskid);
			System.out.println("------获取actionid----->" + actionid);
			System.out.println("------获取result----->" + result);
			System.out.println("------获取timestamp----->" + timestamp);
			Log.d("GexinSdkDemo", "appid:" + appid);
			Log.d("GexinSdkDemo", "taskid:" + taskid);
			Log.d("GexinSdkDemo", "actionid:" + actionid);
			Log.d("GexinSdkDemo", "result:" + result);
			Log.d("GexinSdkDemo", "timestamp:" + timestamp);
			break;
		default:
			System.out.println("-------default----------");
			break;
		}
	}

	private void setUpNotification(Context context, String content,
			String orderId, String order_type) {
		SharedPreferences sp = context.getSharedPreferences("voiceprompt",
				Context.MODE_PRIVATE);
		boolean flag = sp.getBoolean("voiceflag", true);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = content;
		long when = System.currentTimeMillis();
		Notification mNotification = new Notification(icon, tickerText, when);
		mNotification.flags = Notification.FLAG_SHOW_LIGHTS;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		long[] vibrate = { 0, 100, 200, 300 };
		mNotification.vibrate = vibrate;
		if (flag) {
			if (order_type.equals("1")) {
				mNotification.sound = Uri.parse("android.resource://"
						+ context.getPackageName() + "/" + R.raw.order_voice);
			} else {

			}

		}
		CharSequence contentTitle = "社区e服务订单消息";
		CharSequence contentText = content;
		Intent intent = new Intent();
		if (shopsid != -1) {
			if (orderId != null && !"".equals(orderId)) {
				intent.putExtra("from", "message");
				intent.putExtra("order_id", Integer.parseInt(orderId));
				intent.putExtra("ordertype", "");
				intent.setClass(context, OrderDetailsActivity.class);
			}
		} else {
			intent.setClass(context, LoginActivity.class);
		}
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				new Random().nextInt(RANDOM_NUMBER), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(new Random().nextInt(RANDOM_NUMBER),
				mNotification);
	}
}
