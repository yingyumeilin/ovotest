package com.example.oto01.imageload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.os.StatFs;

import com.example.oto01.utils.ImageUtil;
import com.example.oto01.utils.SDCardUtil;

public class ImgFileCache {
	// SD卡上的缓存文件夹
	private static final String CACHDIR = "EServiceStore1";
	// 定义缓存文件后缀
	private static final String WHOLESALE_CONV = ".cache";
	// 缓存空间大小
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	// 规定的最大缓�?
	private static final int CACHE_SIZE = 10;
	// 规定1MB大小
	private static final int MB = 1024 * 1024;
	// 设置缓存文件过期时间�?�?
	private static final long MTIMEDIFF = 3 * 24 * 60 * 60 * 1000;

	public ImgFileCache() {
		removeCache(getDirectory());
	}

	public Bitmap getImage(final String url) {
		final String path = getDirectory() + "/" + urlToFileName(url);
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

	public void saveBmpToSd(Bitmap bm, String url) {
		if (bm == null) {
			return;
		}
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// SD空间不足
			return;
		}
		String filename = urlToFileName(url);
		String dir = getDirectory();
		File file = new File(dir + "/" + filename);
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}
		// 判断是否可操作SD�?
		if (!SDCardUtil.isSDCardAvailable()) {
			return false;
		}
		// 计算出缓存文件�?大小
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				removeExpiredCache(files[i]);
				dirSize += files[i].length();
			}
		}
		if (dirSize > CACHE_SIZE * MB
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// 删除百分�?0
			int removeFactor = (int) ((0.4 * files.length) + 1);
			// 根据�?��修改时间排序
			Arrays.sort(files, new FileLastModifSort());
			for (int i = 0; i < removeFactor; i++) {
				if(i<files.length){
					if (files[i].getName().contains(WHOLESALE_CONV)) {
						files[i].delete();
					}
				}
				
			}
		}
		if (freeSpaceOnSd() <= CACHE_SIZE) {
			return false;
		}
		return true;
	}

	private class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	private void removeExpiredCache(File file) {
		long time = System.currentTimeMillis();
		if (time - file.lastModified() > MTIMEDIFF) {
			// 清除超过三天的过期缓存文�?
			file.delete();
		}
	}

	private void updateFileTime(String path) {
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	private int freeSpaceOnSd() {
		String path = SDCardUtil.getSDDir().getPath();
		StatFs stat = new StatFs(path);
		double sdFreeMB = (double) stat.getAvailableBlocks()
				* (double) stat.getBlockSize();
		return (int) sdFreeMB / MB;
	}

	private String urlToFileName(String url) {
		String[] strs = url.split("/");
		return strs[strs.length - 1] + WHOLESALE_CONV;
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
}