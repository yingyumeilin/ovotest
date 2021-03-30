package com.example.oto01;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.oto01.model.Constant;
import com.example.oto01.utils.FileUtils;

/**
 * 开通电子账号
 * 
 * @author lqq
 * 
 */
public class ElectronicAccountActivity extends BaseActivity implements
		OnClickListener {

	private Handler handler;
	private Dialog mProgressDialog;
	public static final String TAG = "MainActivity";
	public ValueCallback<Uri> mUploadMessage;
	PopupWindow popupWindow;// 自定义菜单
	private WebView mWebView;
	private int fromWhere = 1; // 从哪儿来 1 js 2,ChromeClient
	public ValueCallback<Uri[]> mFilePathCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.electronic_account_layout);
		initView();
	}

	public void back_onClick(View view) {
		finish();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initView() {
		mWebView = (WebView) findViewById(R.id.wv);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setSavePassword(false);
		mWebView.getSettings().setSaveFormData(false);
		mWebView.setWebViewClient(new MyWebViewClient(this));
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// mWebView.addJavascriptInterface(this, "my_pic");
		// mWebView.loadUrl("file:///android_asset/index.html");
		// OrderData date=OrderData.getInstance();
		String url = Constant.DIANZIZHANGHAO_URI
				+ getIntent().getStringExtra("url");
		mWebView.loadUrl(url);
	}

	// private void setListener() {
	// findViewById(R.id.webviewgobackbutton).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// mWebView.goBack();
	// }
	// });
	//
	// findViewById(R.id.webviewgoforwardbutton).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// mWebView.goForward();
	// }
	// });
	//
	// findViewById(R.id.webviewreloadbutton).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// mWebView.reload();
	// }
	// });
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// if (keyCode == KeyEvent.KEYCODE_BACK && popupWindow != null
		// && popupWindow.isShowing()) {
		// popupWindow.dismiss();
		// if (mUploadMessage != null) {
		// mUploadMessage.onReceiveValue(null);
		// mUploadMessage = null;
		// }
		// return true;
		// }
		// if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
		// mWebView.goBack();
		// return true;
		// } else if(keyCode == KeyEvent.KEYCODE_BACK){
		// finish();
		// }
		// return super.onKeyDown(keyCode, event);

		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			if (mUploadMessage != null) {
				mUploadMessage.onReceiveValue(null);
				mUploadMessage = null;
			}
			if (mFilePathCallback != null) {
				mFilePathCallback.onReceiveValue(null);
				mFilePathCallback = null;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 拍照菜单
	 */
	public void initMenu() {
		View view = this.getLayoutInflater().inflate(R.layout.item_web_upload,
				null);
		view.setFocusableInTouchMode(true);
		view.findViewById(R.id.btn_web_camera).setOnClickListener(this);
		view.findViewById(R.id.btn_web_gallery).setOnClickListener(this);
		view.findViewById(R.id.btn_web_cancel).setOnClickListener(this);
		view.setOnKeyListener(new OnKeyListener() {
			@SuppressWarnings("static-access")
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK && popupWindow.isShowing()
						&& event.getAction() == event.ACTION_DOWN) {
					popupWindow.dismiss();// menu的PopupWindow退出
					if (mUploadMessage != null) {
						mUploadMessage.onReceiveValue(null);
						mUploadMessage = null;
					}
					if (mFilePathCallback != null) {
						Uri[] uris = new Uri[1];
						uris[0] = Uri.parse("");
						mFilePathCallback.onReceiveValue(uris);
						mFilePathCallback = null;
					} else {
						mUploadMessage.onReceiveValue(Uri.parse(""));
						mUploadMessage = null;
					}
					return true;
				}
				return false;
			}
		});
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);// 设置焦点
		if (popupWindow != null && !popupWindow.isShowing()) {
			popupWindow.showAtLocation(this.findViewById(R.id.title),
					Gravity.CENTER, 0, 0);
		}
	}

	private class MyWebViewClient extends WebViewClient {
		private Context mContext;

		public MyWebViewClient(Context context) {
			super();
			mContext = context;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "URL地址:" + url);
			// Toast.makeText(mContext, "URL地址:" + url,
			// Toast.LENGTH_SHORT).show();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		/*
		 * public void onPageFinished(WebView view, String url) { Log.i(TAG,
		 * "onPageFinished"); String successUrl = AppParameters.getInstance() //
		 * 支付成功Url .paySuccess().trim(); if (url.trim().equals(successUrl)) {
		 * paySuccessdialog(); } super.onPageFinished(view, url); }
		 */
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// handler.cancel(); 默认的处理方式，WebView变成空白页
			handler.proceed();// 接受证书
			// handleMessage(Message msg); 其他处理
		}
	}

	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE + 1;
	private static final int REQ_CHOOSE = REQ_CAMERA + 1;

	private class MyWebChromeClient extends WebChromeClient {

		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType) {

			if (mUploadMessage != null)
				return;
			mUploadMessage = uploadMsg;
			// selectImage();
			fromWhere = 2;
			initMenu();
			// Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			// i.addCategory(Intent.CATEGORY_OPENABLE);
			// i.setType("*/*");
			// startActivityForResult( Intent.createChooser( i, "File Chooser"
			// ), FILECHOOSER_RESULTCODE );
		}

		// For Lollipop 5.0+ Devices
		public boolean onShowFileChooser(WebView mWebView,
				ValueCallback<Uri[]> filePathCallback,
				WebChromeClient.FileChooserParams fileChooserParams) {
			if (mFilePathCallback != null) {
				mFilePathCallback.onReceiveValue(null);
				mFilePathCallback = null;
			}

			System.out.println(" For Lollipop 5.0+ Devices");
			mFilePathCallback = filePathCallback;
			// intentFile = fileChooserParams.createIntent();
			try {
				// isFive = true;
				fromWhere = 2;
				initMenu();
				// startActivityForResult(intent, REQUEST_SELECT_FILE);
			} catch (ActivityNotFoundException e) {
				mUploadMessage = null;
				Toast.makeText(ElectronicAccountActivity.this,
						"Cannot Open File Chooser", Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		}

		// For Android < 3.0
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooser(uploadMsg, "");
		}

		// For Android > 4.1.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture) {
			openFileChooser(uploadMsg, acceptType);
		}

	}

	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public final boolean checkSDcard() {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(this, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
		}
		return flag;
	}

	String compressPath = "";

	protected final void selectImage() {
		if (!checkSDcard())
			return;
		String[] selectPicTypeStr = { "相机", "相册" };
		new AlertDialog.Builder(this).setItems(selectPicTypeStr,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						// 相机拍摄
						case 0:
							openCarcme();
							break;
						// 手机相册
						case 1:
							chosePic();
							break;
						default:
							break;
						}
						compressPath = Environment
								.getExternalStorageDirectory().getPath()
								+ "/fuiou_wmp/temp";
						new File(compressPath).mkdirs();
						compressPath = compressPath + File.separator + "p"
								+ System.currentTimeMillis() + ".jpg";
					}
				}).show();
	}

	String imagePaths;
	Uri cameraUri;

	/**
	 * 打开照相机
	 */
	private void openCarcme() {
		// FileUtils.delFile(compressPath);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp/" + (System.currentTimeMillis() + ".jpg");
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		cameraUri = Uri.fromFile(vFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, REQ_CAMERA);
	}

	/**
	 * 拍照结束后
	 */
	private void afterOpenCamera() {
		File f = new File(imagePaths);
		addImageGallery(f);
		File newFile = FileUtils.compressFile(f.getPath(), compressPath);
		cameraUri = Uri.fromFile(newFile);
	}

	/** 解决拍照后在相册中找不到的问题 */
	private void addImageGallery(File file) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	/**
	 * 本地相册选择图片
	 */
	private void chosePic() {
		Intent intentxuan = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intentxuan.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intentxuan, REQ_CHOOSE);
		// FileUtils.delFile(compressPath);
		// Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //
		// "android.intent.action.GET_CONTENT"
		// String IMAGE_UNSPECIFIED = "image/*";
		// innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
		// Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		// startActivityForResult(wrapperIntent, REQ_CHOOSE);
	}

	/**
	 * 选择照片后结束
	 * 
	 * @param data
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private Uri afterChosePic(Intent data) {

		// 获取图片的路径：
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		// Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
		Cursor cursor = null;
		int currentSdk = android.os.Build.VERSION.SDK_INT;
		if (currentSdk >= 11) {
			cursor = new CursorLoader(this, data.getData(), proj, null, null,
					null).loadInBackground();
		} else {
			cursor = managedQuery(data.getData(), proj, null, null, null);
		}

		if (cursor == null) {
			Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT)
					.show();
			return null;
		}
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		if (cursor != null)
			cursor.close();
		if (path != null
				&& (path.endsWith(".png") || path.endsWith(".PNG")
						|| path.endsWith(".jpg") || path.endsWith(".JPG"))) {
			File newFile = FileUtils.compressFile(path, compressPath);
			// try {
			// newFile.createNewFile();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			return Uri.fromFile(newFile);
		} else {
			Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT)
					.show();
		}
		return null;
	}

	/**
	 * 返回文件选择
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// Uri uri = null;
		// if (fromWhere == 2) {
		// if (null == mUploadMessage && null == mFilePathCallback)
		// return;
		//
		// if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {
		// afterOpenCamera();
		// uri = cameraUri;
		// } else if (requestCode == REQ_CHOOSE && resultCode == RESULT_OK) {
		// uri = afterChosePic(intent);
		// // mUploadMessage.onReceiveValue(uri);
		// }
		//
		// if(mFilePathCallback != null){
		// Uri[] uris = new Uri[1];
		// uris[0] = uri;
		// mFilePathCallback.onReceiveValue(uris);
		// }else {
		// mUploadMessage.onReceiveValue(uri);
		// }
		// mFilePathCallback = null;
		// // mUploadMessage.onReceiveValue(uri);
		// mUploadMessage = null;
		// } else {
		// if (requestCode == REQ_CAMERA) {
		// afterOpenCamera();
		// uri = cameraUri;
		// mWebView.loadUrl("javascript:callback('" + uri + "')");
		//
		// } else if (requestCode == REQ_CHOOSE && intent != null) {
		// uri = afterChosePic(intent);
		// }
		// }

		Uri uri = null;
		if (fromWhere == 2) {
			if (null == mUploadMessage && null == mFilePathCallback)
				return;
			// if(intent.getData()==null && requestCode == REQ_CHOOSE){
			// if (mUploadMessage != null) {
			// mUploadMessage.onReceiveValue(null);
			// mUploadMessage = null;
			// }
			// if (mFilePathCallback != null) {
			// mFilePathCallback.onReceiveValue(null);
			// mFilePathCallback = null;
			// }
			// return;
			// }
			if (requestCode == REQ_CAMERA && intent == null) {
				afterOpenCamera();
				uri = cameraUri;
			} else if (intent != null && requestCode == REQ_CAMERA) {
				if (mUploadMessage != null) {
					mUploadMessage.onReceiveValue(null);
					mUploadMessage = null;
				}
				if (mFilePathCallback != null) {
					mFilePathCallback.onReceiveValue(null);
					mFilePathCallback = null;
				}
				return;
			} else if (intent == null && requestCode == REQ_CHOOSE) {
				if (mUploadMessage != null) {
					mUploadMessage.onReceiveValue(null);
					mUploadMessage = null;
				}
				if (mFilePathCallback != null) {
					mFilePathCallback.onReceiveValue(null);
					mFilePathCallback = null;
				}
				return;
			} else if (intent.getData() == null && requestCode == REQ_CHOOSE) {
				if (mUploadMessage != null) {
					mUploadMessage.onReceiveValue(null);
					mUploadMessage = null;
				}
				if (mFilePathCallback != null) {
					mFilePathCallback.onReceiveValue(null);
					mFilePathCallback = null;
				}
				return;
			} else if (intent.getData() != null && requestCode == REQ_CHOOSE) {
				uri = afterChosePic(intent);
			}

			if (mFilePathCallback != null) {
				Uri[] uris = new Uri[1];
				uris[0] = uri;
				mFilePathCallback.onReceiveValue(uris);
			} else {
				mUploadMessage.onReceiveValue(uri);
			}
			mFilePathCallback = null;
			mUploadMessage = null;
		} else {
			if (requestCode == REQ_CAMERA) {
				afterOpenCamera();
				uri = cameraUri;
				mWebView.loadUrl("javascript:callback('" + uri + "')");

			} else if (requestCode == REQ_CHOOSE && intent != null) {
				uri = afterChosePic(intent);
			}
		}

		super.onActivityResult(requestCode, resultCode, intent);

	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * 
	 * // if (keyCode == KeyEvent.KEYCODE_BACK && popupWindow != null // &&
	 * popupWindow.isShowing()) { // popupWindow.dismiss(); // if
	 * (mUploadMessage != null) { // mUploadMessage.onReceiveValue(null); //
	 * mUploadMessage = null; // } // return true; // } if ((keyCode ==
	 * KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) { mWebView.goBack();
	 * return true; } else { Intent intent = new Intent(OrderWebActivity.this,
	 * MainTabActivity.class); startActivity(intent); exit();//
	 * 按了返回键，但已经不能返回，则执行�?出确�? } return super.onKeyDown(keyCode, event); }
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_web_camera:
			popupWindow.dismiss();
			openCarcme();
			break;
		case R.id.btn_web_gallery:
			popupWindow.dismiss();
			chosePic();
			break;
		case R.id.btn_web_cancel:
			popupWindow.dismiss();
			if (mUploadMessage != null) {
				mUploadMessage.onReceiveValue(null);
				mUploadMessage = null;
			}
			if (mFilePathCallback != null) {
				mFilePathCallback.onReceiveValue(null);
				mFilePathCallback = null;
			}

			break;
		default:
			break;
		}
		compressPath = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp";
		new File(compressPath).mkdirs();
		compressPath = compressPath + File.separator + "p"
				+ System.currentTimeMillis() + ".jpg";

	}

}
