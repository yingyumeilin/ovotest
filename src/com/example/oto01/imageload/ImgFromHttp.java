package com.example.oto01.imageload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StatFs;

import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.SDCardUtil;

public class ImgFromHttp {
	// SD卡上的缓存文件夹
	private static final String CACHDIR = "EServiceStore2";
	// 定义缓存文件后缀
	private static final String WHOLESALE_CONV = ".cache";
	// 缓存空间大小
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	// 规定1MB大小
	private static final int MB = 1024 * 1024;

	public static Bitmap downloadBitmap(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse res = client.execute(getRequest);
			int statusCode = res.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return instreamToBitmap(res.getEntity());
			}
		} catch (Exception e) {
			getRequest.abort();
		} finally {
			if ((client instanceof DefaultHttpClient)) {
				client.getConnectionManager().shutdown();
			}
		}
		return null;
	}

	private static Bitmap instreamToBitmap(HttpEntity he) throws Exception {
		if (he != null) {
			InputStream is = he.getContent();
			try {
				FilterInputStream fis = new FlushedInputStream(is);
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inDither = false;
//				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//				options.inJustDecodeBounds = true;
				Bitmap bmp = BitmapFactory.decodeStream(fis);
				return bmp;
			} finally {
				if (is != null) {
					is.close();
				}
				he.consumeContent();
			}
		}
		return null;
	}

	public Bitmap getBitMap(String url) {
		Bitmap bm = getImage(url);
		if (bm != null)
			return bm;
		Bitmap bmp = downloadBitmap(url);
		saveBmpToSd(bmp, url);
		return bmp;
	}

	private Bitmap getImage(String url) {
		String path = getDirectory() + "/" + urlToFileName(url);
		File file = new File(path);
		if (file.exists()) {
			Bitmap bmp = ImageUtil.getBitmap(path, 4);
			if (bmp == null) {
				file.delete();
			} else {
				updateFileTime(path);
				return bmp;
			}
		}
		return null;
	}

	private void updateFileTime(String path) {
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	private void saveBmpToSd(Bitmap bm, String url) {
		if (bm == null) {
			return;
		}
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// SD空间不足
			return;
		}
		String fname = urlToFileName(url);
		String dir = getDirectory();
		File file = new File(dir + "/" + fname);
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			OutputStream os = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String urlToFileName(String url) {
		String[] strs = url.split("/");
		return strs[strs.length - 1] + WHOLESALE_CONV;
	}

	private int freeSpaceOnSd() {
		String path = SDCardUtil.getSDDir().getPath();
		StatFs stat = new StatFs(path);
		double sdFreeMB = (double) stat.getAvailableBlocks()
				* (double) stat.getBlockSize();
		return (int) sdFreeMB / MB;
	}

	private String getDirectory() {
		String dir = getSDPath() + "/" + CACHDIR;
		String substr = dir.substring(0, 4);
		if (substr.equals("/mnt")) {
			dir = dir.replace("/mnt", "");
		}
		return dir;
	}

	private String getSDPath() {
		File sdDir = null;
		if (SDCardUtil.isSDCardAvailable()) {
			sdDir = SDCardUtil.getSDDir();
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}

	private static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream is) {
			super(is);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					if (read() < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
}