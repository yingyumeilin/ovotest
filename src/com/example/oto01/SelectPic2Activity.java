package com.example.oto01;

/**
 * 选择图片
 */
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
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
import com.example.oto01.model.PhotoInfo;
import com.example.oto01.utils.CameraUtil;
import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.SDCardUtil;
import com.example.oto01.utils.ToastUtil;

public class SelectPic2Activity extends BaseActivity implements PictureCallback {

	private String picPath;
	private Uri photoUri;
	private int size = 100;
	// private Uri imageUri ;
	private SimpleDateFormat df;
	private File mFile;
	private int tag;// 判断是否是上传证件，上传证件不要裁剪，压缩到50K左右
	private Uri uritempFile;

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
						// ContentResolver cr = this.getContentResolver();
						// photoUri = cr.insert(Media.EXTERNAL_CONTENT_URI,
						// new ContentValues());
						Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						ContentValues values = new ContentValues();
						photoUri = this.getContentResolver().insert(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								values);
						in.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
						startActivityForResult(in,
								Constant.SELECT_PIC_BY_TACK_PHOTO);
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
		in.setClass(this, AlbumActivity.class);
		// in.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(in, Constant.SELECT_PIC_BY_PICK_PHOTO);
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		// intent.setType("image/*");
		// if (android.os.Build.VERSION.SDK_INT >=
		// android.os.Build.VERSION_CODES.KITKAT) {
		// startActivityForResult(intent, Constant.SELECT_PIC_BY_PICK_PHOTO);
		// } else {
		// startActivityForResult(intent, Constant.SELECT_PIC_BY_PICK_PHOTO2);
		// }
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		// Intent data = this.getIntent();
		// data.putExtra(Constant.KEY_PHOTO_PATH, "");
		// setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resCode, Intent data) {
		super.onActivityResult(requestCode, resCode, data);
		if (resCode == Activity.RESULT_OK) {
			// 相册
			if (requestCode == Constant.SELECT_PIC_BY_PICK_PHOTO) {
				// photoUri = data.getData();
				List<PhotoInfo> list = (List<PhotoInfo>) data
						.getSerializableExtra("list");
				if (list != null && list.size() > 0) {
					PhotoInfo photo = list.get(0);
					// acquireCredentialsPhoto();
					System.out.println("-----相册返回的图片信息------>" + photo);
					String picturePath = photo.getPath_file();
					photoUri = Uri.parse(picturePath);
					setPicPath();

				}
			}
			// 拍照
			if (requestCode == Constant.SELECT_PIC_BY_TACK_PHOTO) {
				// acquireCredentialsPhoto();
				setPicPath();

			}

		}
		// String picturePath = CameraUtil.getPath(
		// getApplicationContext(), photoUri);
		// Bitmap bm = ImageUtil.changeDirectionBitmap(bitmap,
		// picturePath, true);
		if (requestCode == 3) {
			Bitmap bitmap;
			try {

				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uritempFile));
				Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
						getContentResolver(), bitmap, null, null));
				acquireCredentialsPhoto(uri);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 * @param size
	 */
	private void startPhotoZoom(Uri uri, Bitmap bitmap) {

		// Intent intent = new Intent("com.android.camera.action.CROP");
		// intent.setDataAndType(uri, "image/*");
		// intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 4);
		// intent.putExtra("aspectY", 3);
		// intent.putExtra("outputX", 800);
		// intent.putExtra("outputY", 600);
		// // intent.putExtra("scaleUpIfNeeded", true);
		// intent.putExtra("scale", true);
		// intent.putExtra("return-data", true);
		// startActivityForResult(intent, 3);

		// Intent intent = new Intent();
		// intent.setDataAndType(uri, "image/*");
		// intent.putExtra("data", bitmap);
		// intent.setClass(getApplicationContext(), CutOutActivity.class);
		// startActivityForResult(intent, 3);

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		// intent.setClassName("com.android.camera",
		// "com.android.camera.CropImage");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 600);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		intent.putExtra("noFaceDetection", true); // 是否去除面部检测
		uritempFile = Uri.parse("file://" + "/"
				+ Environment.getExternalStorageDirectory().getPath() + "/"
				+ "small.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, 3);

	}

	private void acquireCredentialsPhoto(Uri uri) {

		String picturePath = CameraUtil.getPath(getApplicationContext(), uri);

		if (picturePath == null) {
			return;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(picturePath + "", options);// 此时返回bm为空
		options.inJustDecodeBounds = false;
		// int be = (int) (options.outHeight / (float) 400);// 配置图片分辨率
		// if (be <= 0) {
		int be = 1;
		// }
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(picturePath + "", options);

		Bitmap bm = ImageUtil.changeDirectionBitmap(bitmap, picturePath, true);
		String new_imagePath = Environment.getExternalStorageDirectory()
				+ "/EServiceStore/" + System.currentTimeMillis() + "b.jpg";
		// Bitmap m = ImageUtil.changeDirectionBitmap(bitmap, new_imagePath,
		// true);
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

	private void setPicPath() {
		if (photoUri == null) {
			ToastUtil.show(this, R.string.select_pic_error);
			return;
		}
		// queryPath();
		// setRes();
		System.out.println("-------原图------->" + photoUri.getPath());
		try {
			String picturePath = CameraUtil.getPath(getApplicationContext(),
					photoUri);
			Bitmap bm = ImageUtil.changeDirectionBitmap(
					ImageUtil.getimage(picturePath), picturePath, true);
			Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
					getContentResolver(), bm, null, null));
			startPhotoZoom(uri, bm);
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	/*
	 * private void setRes() { if (picPath != null &&
	 * (picPath.toLowerCase().endsWith(".jpg") || picPath
	 * .toLowerCase().endsWith(".png"))) { Intent data = this.getIntent();
	 * System.out.println("------picPath---->" + picPath);
	 * data.putExtra(Constant.KEY_PHOTO_PATH, picPath); setResult(RESULT_OK,
	 * data); finish(); } else { ToastUtil.show(this,
	 * R.string.select_pic_not_correct); } }
	 */
}