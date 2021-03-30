package com.example.oto01;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.KeyEvent;
import android.view.View;

import com.example.oto01.model.Constant;
import com.example.oto01.utils.CameraUtil;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.SDCardUtil;
import com.example.oto01.utils.ToastUtil;

public class Fenbinglv300 extends BaseActivity implements PictureCallback {

	private String picPath;
	private Uri photoUri;
	private int size = 100;
	// private Uri imageUri ;
	private SimpleDateFormat df;
	private File mFile;
	private int tag;// 判断是否是上传证件，上传证件不要裁剪，压缩到50K左右

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pic);
		tag = getIntent().getIntExtra("tag", -1);
		size = getIntent().getIntExtra("size",
				getWindowManager().getDefaultDisplay().getWidth());
		System.out.println("------size-------->" + size);
		df = new SimpleDateFormat("HH_mm_ss");// 设置日期格式
		System.out.println(df.format(new Date()));
		mFile = new File(Environment.getExternalStorageDirectory(),
				Constant.PICTURE_DIRECTORY);
		if (!mFile.exists())
			mFile.mkdirs();
		// imageUri = Uri.parse(ff.getPath()+"/temp_"+df.format(new
		// Date())+".jpg");
	}

	/**
	 * 拍照
	 * 
	 * @param view
	 */
	public void take_photo_onClick(View view) {
		if (SDCardUtil.isSDCardAvailable()) {
			if (computationSpace()) {
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					try {
						ContentResolver cr = this.getContentResolver();
						photoUri = cr.insert(Media.EXTERNAL_CONTENT_URI,
								new ContentValues());

						System.out.println("----------photoUri-------------->"
								+ photoUri);
						Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						in.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
						// startActivityForResult(in,
						// Constant.SELECT_PIC_BY_TACK_PHOTO);
						startActivityForResult(in, 3);
					} catch (Exception e) {
						ToastUtil.show(this, "无法启动相机");
					}
				} else {

					ToastUtil.show(this, "无法启动相机");
				}
			} else {
				ToastUtil.show(this, "存储空间不足");
			}
		} else {
			ToastUtil.show(this, R.string.sd_not_exit);
		}
	}

	/**
	 * 计算剩余空间
	 * 
	 * @return
	 */
	private boolean computationSpace() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long availCount = sf.getAvailableBlocks();
			int str = (int) (availCount * blockSize / 1024 / 1024);
			if (str < 10) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 打开相册
	 * 
	 * @param view
	 */
	public void pick_photo_onClick(View view) {
		Intent in = new Intent();
		in.setType("image/*");
		in.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(in, Constant.SELECT_PIC_BY_PICK_PHOTO);
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
//		Intent data = this.getIntent();
//		data.putExtra(Constant.KEY_PHOTO_PATH, "");
//		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resCode, Intent data) {
		super.onActivityResult(requestCode, resCode, data);

		System.out
				.println("afshjsjsjsjsjjsjsjsjsjsjsjjsjsjsjsjsjsjjsjsjsjsjsjjjs");

		if (resCode == Activity.RESULT_OK) {
			// 相册
			if (requestCode == Constant.SELECT_PIC_BY_PICK_PHOTO) {
				photoUri = data.getData();
				// acquireCredentialsPhoto();
				setPicPath();
			}
			// 拍照
			if (requestCode == Constant.SELECT_PIC_BY_TACK_PHOTO) {
				// acquireCredentialsPhoto();
				setPicPath();

			}
			if (requestCode == 3) {
				setPicPath();
				// if (data != null) {
				// Bundle extras = data.getExtras();
				// if (extras != null) {
				// System.out.println("------setRes----->" + extras);
				// //
				// System.out.println("------pic_path----->"+imageUri.getPath());

				// }
				// }
			}
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 * @param size
	 */
	private void startPhotoZoom(Uri uri, int size) {
		// File f=new File(mFile.getPath(), "/temp_"+df.format(new
		// Date())+".jpg");
		// photoUri=Uri.fromFile(f);
		String picturePath = CameraUtil.getPath(getApplicationContext(), uri);
		Bitmap bm = ImageUtil.changeDirectionBitmap(
				ImageUtil.getimage(picturePath), picturePath, true);
		uri = Uri.parse(MediaStore.Images.Media.insertImage(
				getContentResolver(), bm, null, null));
		acquireCredentialsPhoto(uri);
		// Intent intent = new Intent("com.android.camera.action.CROP");
		// intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		// // intent.setClassName("com.android.camera",
		// // "com.android.camera.CropImage");
		// intent.setDataAndType(uri, "image/*");
		// // crop为true是设置在开启的intent中设置显示的view可以剪裁
		// intent.putExtra("crop", "true");
		// // aspectX aspectY 是宽高的比例
		// intent.putExtra("aspectX", 4);
		// intent.putExtra("aspectY", 3);
		// // outputX,outputY 是剪裁图片的宽高
		// intent.putExtra("outputX", 400);
		// intent.putExtra("outputY", 300);
		// intent.putExtra("scale", true);// 去黑边
		// intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// intent.putExtra("noFaceDetection", true); // 是否去除面部检测
		// intent.putExtra("return-data", true);
		// startActivityForResult(intent, 3);
	}

	private void acquireCredentialsPhoto(Uri uri) {
		// String[] filePathColumns={MediaStore.Images.Media.DATA};
		// Cursor c = this.getContentResolver().query(photoUri, filePathColumns,
		// null,null, null);
		// c.moveToFirst();
		// String picturePath=
		// c.getString(c.getColumnIndex(filePathColumns[0]));

		String picturePath = CameraUtil.getPath(getApplicationContext(), uri);

		if (picturePath == null) {
			return;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(picturePath + "", options);
		options.inJustDecodeBounds = false;
		int be = (int) (options.outHeight / (float) 400);
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(picturePath + "", options);

		// Bitmap bm = ImageUtil.changeDirectionBitmap(bitmap, picturePath,
		// true);
		String new_imagePath = Environment.getExternalStorageDirectory()
				+ "/EServiceStore/" + System.currentTimeMillis() + "b.jpg";
		try {
			FileOutputStream out = new FileOutputStream(new File(new_imagePath));
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			bitmap.recycle();
			// bm.recycle();
			e.printStackTrace();
		}

		Intent intent = this.getIntent();
		intent.putExtra(Constant.KEY_PHOTO_PATH, new_imagePath);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	/**
	 * 设置返回数据
	 * 
	 * @param intent
	 */
	// private void setRes(Intent intent) {
	// Bundle extras = intent.getExtras();
	// if (extras != null) {
	// System.out.println("------setRes----->"+extras);
	// System.out.println("------pic_path----->"+imageUri.getPath());
	// Intent data = this.getIntent();
	// data.putExtra(AppParameters.KEY_PHOTO_PATH, imageUri.getPath());
	// data.putExtra("bundle", extras);
	// setResult(RESULT_OK, data);
	// finish();
	// }
	// }
	private void handleRes(Intent data) {
		if (data == null) {
			ToastUtil.show(this, R.string.select_pic_error);
			return;
		}
		photoUri = data.getData();
		if (photoUri == null) {
			ToastUtil.show(this, R.string.select_pic_error);
			return;
		}
	}

	private void setPicPath() {
		if (photoUri == null) {
			ToastUtil.show(this, R.string.select_pic_error);
			return;
		}
		// queryPath();
		// setRes();
		System.out.println("-------原图------->" + photoUri.getPath());
		startPhotoZoom(photoUri, 300);
	}

	@SuppressWarnings("deprecation")
	private void queryPath() {
		String[] pj = { Media.DATA };
		Cursor cr = managedQuery(photoUri, pj, null, null, null);
		if (cr != null) {
			cr.moveToFirst();
			picPath = cr.getString(cr.getColumnIndexOrThrow(pj[0]));
			cr.close();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (photoUri == null) {
			return;
		}
		savedInstanceState.putString("photoUri", photoUri.toString());
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		photoUri = Uri.parse(savedInstanceState.getString("photoUri"));
	}

	@Override
	public void onPictureTaken(byte[] arg0, Camera arg1) {
		// TODO Auto-generated method stub

	}
}
