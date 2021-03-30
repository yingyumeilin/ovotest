package com.example.oto01;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oto01.model.Login;
import com.example.oto01.scan.CameraManager;
import com.example.oto01.scan.CaptureActivityHandler;
import com.example.oto01.scan.InactivityTimer;
import com.example.oto01.scan.ViewfinderView;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.OrderService;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.TelephoneUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * 二维码 页面
 * 
 * @author Administrator
 * 
 */
public class ScanActivity extends BaseActivity implements OnClickListener,
		Callback {
	private TextView title_font;
	private TextView tv_cancle;
	private ViewfinderView viewfinderView;
	private Dialog newDialog, newDialog2;
	private static final int SHOP_CODE = 2;
	private static final int SHOP_QUAN_CODE = 3;
	private String code;
	private int shopsid = 1;
	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private TextView tv_remind;// 提醒的文本
	private Button ok;// 确定
	private boolean isOpen = false;
	private int code1;// 手动输入为1，扫码为2
	private ImageView iv_trun_light;//  闪光灯按钮
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SHOP_CODE) {
				String res = (String) msg.obj;
				try {
					JSONObject jo = new JSONObject(res);
					if (msg.arg1 == 1) {
						// 手动
						ok.setFocusable(true);
						if (jo.getInt("res") == 0) {
							JSONObject jsonObject = jo.getJSONObject("data");
							// 未验证
							tv_remind.setText("");
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									ShopPayDetailsActivity.class);
							intent.putExtra("type", "not");
							intent.putExtra("id", jsonObject.getString("id"));
							startActivity(intent);
							newDialog.dismiss();
						} else if (jo.getInt("res") == 1) {
							// 已验证
							JSONObject jsonObject = jo.getJSONObject("data");
							tv_remind.setText("");
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									ShopPayDetailsActivity.class);
							intent.putExtra("type", "ok");
							intent.putExtra("id", jsonObject.getString("id"));
							// intent.putExtra("data", data);
							startActivity(intent);
							newDialog.dismiss();
						}
						// else if (jo.getInt("res") == 2) {
						// tv_remind.setText("*" + "该验证码不是本店的" + "");
						// }
						else {
							tv_remind.setText("*" + "该编码或验证码无效请重新输入！" + "");
						}
					} else if (msg.arg1 == 2) {
						// 扫描
						if (jo.getInt("res") == 0) {
							// 未验证
							JSONObject jsonObject = jo.getJSONObject("data");
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									ShopPayDetailsActivity.class);
							intent.putExtra("type", "not");
							intent.putExtra("id", jsonObject.getString("id"));
							startActivity(intent);
						} else if (jo.getInt("res") == 1) {
							// 已验证
							JSONObject jsonObject = jo.getJSONObject("data");
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									ShopPayDetailsActivity.class);
							intent.putExtra("type", "ok");
							intent.putExtra("id", jsonObject.getString("id"));
							startActivity(intent);
						} else if (jo.getInt("res") == 2) {
							// 不是本店的二维码
							scanDialog("该二维码不是本店的", "", "");
						} else {
							restartBarCode();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					tv_remind.setText("*" + "该编码或验证码无效请重新输入！" + "");
				}
			} else if (msg.what == SHOP_QUAN_CODE) {
				// 扫礼品券码
				String res = (String) msg.obj;
				try {
					JSONObject jo = new JSONObject(res);
					if (msg.arg1 == 1) {
						// 手动
						ok.setFocusable(true);
						if (jo.getInt("res") == 0) {
							tv_remind.setText("");
							if (jo.getString("pay_money") != ""
									|| jo.getString("pay_money") != null) {
								// 还需支付的金额
								if (jo.getString("pay_money").equals("0.00")) {
									newDialog.dismiss();
									Intent intent = new Intent();
									intent.setClass(getApplicationContext(),
											ShopPayDetailsActivity.class);
									intent.putExtra("type", "scanquan");
									intent.putExtra("id",
											jo.getString("orderid"));
									startActivity(intent);
								} else {
									newDialog.dismiss();
									scanDialog(
											"兑换成功，请另外收取用户"
													+ jo.getString("pay_money")
													+ "元", "1",
											jo.getString("orderid"));
								}

							}

						} else {
							tv_remind.setText("*" + jo.getString("msg") + "");
						}
					} else if (msg.arg1 == 2) {
						// 扫描二维码
						if (jo.getInt("res") == 0) {

							if (jo.getString("pay_money") != ""
									|| jo.getString("pay_money") != null) {
								// 还需支付的金额
								if (jo.getString("pay_money").equals("0.00")) {
									Intent intent = new Intent();
									intent.setClass(getApplicationContext(),
											ShopPayDetailsActivity.class);
									intent.putExtra("type", "scanquan");
									intent.putExtra("id",
											jo.getString("orderid"));
									startActivity(intent);
								} else {
									scanDialog(
											"兑换成功，请另外收取用户"
													+ jo.getString("pay_money")
													+ "元", "1",
											jo.getString("orderid"));
								}

							} else {
								Intent intent = new Intent();
								intent.setClass(getApplicationContext(),
										ShopPayDetailsActivity.class);
								intent.putExtra("type", "scanquan");
								intent.putExtra("id", jo.getString("orderid"));
								startActivity(intent);
							}

						} else {
							scanDialog(jo.getString("msg"), "", "");
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					restartBarCode();
				}

			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_dimension_code);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		title_font = (TextView) findViewById(R.id.title_font);
		tv_cancle = (TextView) findViewById(R.id.tv_cancle);
		iv_trun_light = (ImageView) findViewById(R.id.iv_trun_light);
		title_font.setText("扫码结账");
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		newDialog = showNewDialog();
		newDialog2 = showNewDialog();
		LoginManager lm = LoginManager.getInstance(ScanActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		tv_cancle.setVisibility(View.VISIBLE);
		tv_cancle.setText("手动输入");
		tv_cancle.setOnClickListener(this);
		iv_trun_light.setOnClickListener(this);

		newDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				restartBarCode();
			}
		});

		newDialog2.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				restartBarCode();
			}
		});

	}

	protected void scanDialog(String text, final String type, final String id) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.scan_dialogview, null);
		layout.setMinimumWidth((int) (ScanActivity.this.getWindowManager()
				.getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
		TextView title = (TextView) layout.findViewById(R.id.title);
		ImageView iv = (ImageView) layout.findViewById(R.id.iv);
		if (type.equals("1")) {
			iv.setVisibility(View.GONE);
		} else {
			iv.setVisibility(View.VISIBLE);
		}

		title.setText(text + "");
		newDialog2.setContentView(layout);
		newDialog2.setCancelable(true);
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newDialog2.dismiss();
				if (type.equals("1")) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							ShopPayDetailsActivity.class);
					intent.putExtra("type", "scanquan");
					intent.putExtra("id", id);
					startActivity(intent);
				}
				restartBarCode();
			}
		});
		newDialog2.show();// 显示AlertDialog
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancle:
			// 手动输入
			input();
			break;
		case R.id.iv_trun_light:
			// 闪光灯按钮
			if (!isOpen) {
				iv_trun_light.setBackgroundResource(R.drawable.turn_on);
				CameraManager.get().openLigth();
				isOpen = true;
			} else { // 关灯
				iv_trun_light.setBackgroundResource(R.drawable.turn_off);
				CameraManager.get().stopLigth();
				isOpen = false;
			}
			break;

		default:
			break;
		}

	}

	private void input() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.scan_dialog, null);
		layout.setMinimumWidth((int) (ScanActivity.this.getWindowManager()
				.getDefaultDisplay().getWidth() * 0.8));// 设置dialog的宽度
		newDialog.setContentView(layout);
		newDialog.setCancelable(true);
		final EditText editText = (EditText) layout
				.findViewById(R.id.type_name);
		editText.addTextChangedListener(watcher);
		final TextView title = (TextView) layout.findViewById(R.id.title);
		tv_remind = (TextView) layout.findViewById(R.id.tv_remind);
		title.setText("请输入到店支付验证码或礼品券码：");
		ok = (Button) layout.findViewById(R.id.ok);
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						newDialog.dismiss();
					}
				});
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editText.getText().toString().trim() != null
						&& !editText.getText().toString().equals("")
						&& editText.getText().toString() != ""
						&& editText.getText().length() != 0) {

					if (editText.getText().length() != 6
							&& editText.getText().length() != 8) {
						tv_remind.setText("该编码或验证码无效，请重新输入");
					} else {
						code = editText.getText().toString().trim();
						if (!NetConn.checkNetwork(ScanActivity.this)) {
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									ok.setFocusable(false);
									initCode(1);
								}
							}).start();
						}
					}

				} else {
					editText.setHint("验证码不能为空");
					// ToastUtil.show(ScanActivity.this, "请填写到店支付验证码");
				}
			}
		});
		newDialog.show();
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			if (s.equals("") || s.length() == 0) {
				tv_remind.setText("");
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	private void initCode(int i) {
		code1 = i;
		if (!NetConn.checkNetwork(ScanActivity.this)) {
			restartBarCode();
		} else {
			new CodeAsyc().execute();
		}
	}

	private class CodeAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (code.length() == 6) {
				// 到店支付验证码
				OrderService orderService = new OrderService(ScanActivity.this);
				String res = orderService.getShopCode(shopsid, code);
				Message message = handler1.obtainMessage(SHOP_CODE);
				message.obj = res;
				message.arg1 = code1;
				handler1.sendMessage(message);

			} else if (code.length() == 8) {
				// 礼品券码
				OrderService orderService = new OrderService(ScanActivity.this);
				String res = orderService.getShopQuanCode(shopsid, code,
						TelephoneUtil.getImei(getApplicationContext()),
						MainActivity.geoLng + "", MainActivity.geoLat + "");
				Message message = handler1.obtainMessage(SHOP_QUAN_CODE);
				message.obj = res;
				message.arg1 = code1;
				handler1.sendMessage(message);
			}

			return null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		startBeepSound();
	}

	private Dialog showNewDialog() {
		Dialog dialog = new Dialog(ScanActivity.this,
				R.style.theme_dialog_alert);
		return dialog;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopBeepSound();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		inactivityTimer.shutdown();
		mHandler.removeCallbacksAndMessages(null);
	}

	private void stopBeepSound() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	private void startBeepSound() {
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {

			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		Log.d("dj", "drawViewfinder");
		viewfinderView.drawViewfinder();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		hasSurface = false;
	}

	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		onResultHandler(resultString, barcode);
	}

	/**
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(String resultString, Bitmap bitmap) {
		Log.d("dj", "--5---");

		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(this, "扫描失败!", Toast.LENGTH_SHORT).show();
			return;
		}
		// Intent resultIntent = new Intent();
		// Bundle bundle = new Bundle();
		// bundle.putString("result", resultString);
		// bundle.putParcelable("bitmap", bitmap);
		// resultIntent.putExtras(bundle);
		// this.setResult(RESULT_OK, resultIntent);
		// finish();
		stopBeepSound();

		// if (EventUrlUtil.checkWebEventUrl(resultString)) {
		// //startActivity(ActStartUtil.checkStartEventInfoAct(this,
		// EventUrlUtil.isEasy(resultString),
		// EventUrlUtil.getEventId(resultString), false));
		// return;
		// }
		signAction(resultString);
		// AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		// dialog.setCancelable(false);
		// dialog.setTitle("提示").setMessage(resultString).setPositiveButton("确定",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// startBeepSound();
		// }
		// }).create().show();
	}

	private void signAction(String resultString) {

		if (!NetConn.checkNetwork(ScanActivity.this)) {
			restartBarCode();
		} else {
			try {
				JSONObject jo = new JSONObject(resultString);
				if (jo.getString("type").equals("confirm_authcode")) {
					// 普通到店支付
					code = jo.getString("authcode");
					initCode(2);
				} else if (jo.getString("type").equals("gift_code")) {
					// 礼品券的支付
					code = jo.getString("coupon_code");
					initCode(2);
				} else {
					// 不是本店的二维码
					restartBarCode();
				}
			} catch (Exception e) {
				// 不是用户端的二维码
				restartBarCode();
				e.printStackTrace();
			}
		}

	}

	private ProgressDialog mProgress;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private static final int PARSE_START_BARCODE = 306;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d("dj", "--2---");
			if (mProgress != null) {
				mProgress.dismiss();
			}
			switch (msg.what) {
			case PARSE_BARCODE_SUC:
				// onResultHandler((String) msg.obj, scanBitmap);
				break;
			case PARSE_BARCODE_FAIL:
				Toast.makeText(ScanActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case PARSE_START_BARCODE:
				startBeepSound();
				break;

			}
		}

	};

	/**
	 * 重新开启扫描，延迟执行
	 */
	private void restartBarCode() {
		Message m = mHandler.obtainMessage();
		m.what = PARSE_START_BARCODE;
		mHandler.sendMessageDelayed(m, 2000);
	}

}
