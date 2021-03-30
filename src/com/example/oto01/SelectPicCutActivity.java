package com.example.oto01;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;

import com.example.oto01.model.Constant;
import com.example.oto01.utils.PictureUtil;
import com.example.oto01.utils.SDCardUtil;
import com.example.oto01.utils.ToastUtil;

public class SelectPicCutActivity extends BaseActivity {

	private int size = 100;
	// private Uri imageUri ;
	private SimpleDateFormat df;
	private File mFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pic);
		size = getIntent().getIntExtra("size",
				getWindowManager().getDefaultDisplay().getWidth());
		System.out.println("------size-------->" + size);
		df = new SimpleDateFormat("HH_mm_ss");// 设置日期格式
		System.out.println(df.format(new Date()));
		mFile = new File(Environment.getExternalStorageDirectory(),
				Constant.PICTURE_DIRECTORY);
		if (!mFile.exists())
			mFile.mkdirs();
	}

	private String mCurrentPhotoPath;// 图片路径
	private String path;

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
						//
						// System.out.println("----------photoUri-------------->"
						// + photoUri);
						// Intent in = new
						// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						// in.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
						// startActivityForResult(in,
						// Constant.SELECT_PIC_BY_TACK_PHOTO);
						Intent takePictureIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						try {
							// 指定存放拍摄照片的位置
							File f = createImageFile();
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(f));
							startActivityForResult(takePictureIntent,
									Constant.SELECT_PIC_BY_TACK_PHOTO);
						} catch (IOException e) {
							e.printStackTrace();
						}
						// startActivityForResult(in, 3);
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
	 * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
	 * 照片的命名规则为：sheqing_20130125_173729.jpg
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "sheqing_" + timeStamp + ".jpg";

		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		Intent data = this.getIntent();
		// data.putExtra(Constant.KEY_PHOTO_PATH, "");
		// setResult(RESULT_OK, data);
		finish();
	}

	/**
	 * 打开相册
	 * 
	 * @param view
	 */
	public void pick_photo_onClick(View view) {
		// Intent in = new Intent();
		// in.setType("image/*");
		// in.setAction(Intent.ACTION_GET_CONTENT);
		// startActivityForResult(in, Constant.SELECT_PIC_BY_PICK_PHOTO);

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			startActivityForResult(intent, Constant.SELECT_PIC_BY_PICK_PHOTO);
		} else {
			startActivityForResult(intent, Constant.SELECT_PIC_BY_PICK_PHOTO2);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constant.SELECT_PIC_BY_TACK_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				File file = new File(mCurrentPhotoPath);
				PictureUtil.galleryAddPic(this, mCurrentPhotoPath);

				if (mCurrentPhotoPath != null) {
					mCurrentPhotoPath = Uri.decode(mCurrentPhotoPath);
					ContentResolver cr = this.getContentResolver();
					StringBuffer buff = new StringBuffer();
					buff.append("(").append(Images.ImageColumns.DATA)
							.append("=").append("'" + mCurrentPhotoPath + "'")
							.append(")");
					Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
							new String[] { Images.ImageColumns._ID },
							buff.toString(), null, null);
					int index = 0;
					for (cur.moveToFirst(); !cur.isAfterLast(); cur
							.moveToNext()) {
						index = cur.getColumnIndex(Images.ImageColumns._ID);
						// set _id value
						index = cur.getInt(index);
					}
					if (index == 0) {
						// do nothing
					} else {
						Uri uri_temp = Uri
								.parse("content://media/external/images/media/"
										+ index);
						if (uri_temp != null) {
							setPicPath(uri_temp);
						}
					}
				}
				// try {
				// FileInputStream fis = new FileInputStream(file);
				// int fileLen = fis.available();
				// if (fileLen >= 512000) {
				// // 图片的字节数 大于等于500kb的时候，就进行压缩
				// Bitmap bitmap = PictureUtil
				// .getSmallBitmap(mCurrentPhotoPath);
				// Uri uri1 = Uri.parse(MediaStore.Images.Media
				// .insertImage(getContentResolver(), bitmap,
				// null, null));
				// mCurrentPhotoPath = getPath(this, uri1);
				// }
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// Intent intent = this.getIntent();
				// intent.putExtra(Constant.KEY_PHOTO_PATH, mCurrentPhotoPath);
				// setResult(RESULT_OK, intent);
				// finish();
			} else {
				PictureUtil.deleteTempFile(mCurrentPhotoPath);
			}
		} else if (requestCode == Constant.SELECT_PIC_BY_PICK_PHOTO
				|| requestCode == Constant.SELECT_PIC_BY_PICK_PHOTO2) {
			if (resultCode == Activity.RESULT_OK) {
				// setImageToLocalServer();
				Uri uri = data.getData();
				path = getPath(this, uri);
				File file = new File(path);
				PictureUtil.galleryAddPic(this, path);
				setPicPath(uri);
				// try {
				// FileInputStream fis = new FileInputStream(file);
				// int fileLen = fis.available();
				// if (fileLen > 512000) {
				// // 图片的字节数 大于等于500kb的时候，就进行压缩
				// Bitmap bitmap = PictureUtil.getSmallBitmap(path);
				// Uri uri1 = Uri.parse(MediaStore.Images.Media
				// .insertImage(getContentResolver(), bitmap,
				// null, null));
				// path = getPath(this, uri1);
				// }
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				//
				// Intent intent = this.getIntent();
				// intent.putExtra(Constant.KEY_PHOTO_PATH, path);
				// setResult(RESULT_OK, intent);
				// finish();
			} else {
				Uri uri = data.getData();
				String path = getPath(this, uri);
				PictureUtil.deleteTempFile(path);
			}
		} else if (requestCode == Constant.SELECT_PIC_CUT) {

			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = data.getExtras().getParcelable("data");

						Uri uri = Uri.parse(MediaStore.Images.Media
								.insertImage(getContentResolver(), bitmap,
										null, null));
						Intent intent = this.getIntent();
						intent.putExtra(Constant.KEY_PHOTO_PATH,
								getRealFilePath(getApplicationContext(), uri));
						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
				}
			}
		}
	}

	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	// 进行剪裁的相关操作
	private void setPicPath(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		intent.putExtra("outputX", 480);
		intent.putExtra("outputY", 800);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.SELECT_PIC_CUT);
	}

	// 以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

}
