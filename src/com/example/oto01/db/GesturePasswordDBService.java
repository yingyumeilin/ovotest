package com.example.oto01.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GesturePasswordDBService {

	private static GesturePasswordDBOpenHelper dbHelper;

	public GesturePasswordDBService(Context context) {
		dbHelper = new GesturePasswordDBOpenHelper(context);
	}

	/**
	 * 添加手势密码
	 * */
	public void saveSearchTrace(Context context, String shopsid,
			String gestureNumber) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (gestureNumber != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("shopsid", shopsid);
			contentValues.put("gestureNumber", gestureNumber);
			db.insert(SQLDao.SEARCH, null, contentValues);
		}
	}

	/**
	 * 得到手势密码的记录
	 * */
	public String getAllSearchTrace(Context context, String shopsid) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {

			cursor = db.query(SQLDao.SEARCH, new String[] { "_id", "shopsid",
					"gestureNumber" }, "shopsid" + "=?",
					new String[] { shopsid }, null, null, "");
			// ArrayList<String> list = new ArrayList<String>();
			String name = null;
			while (cursor.moveToNext()) {
				name = cursor.getString(cursor
						.getColumnIndexOrThrow("gestureNumber"));
				// list.add(name);
			}

			return name;
		} finally {
			if (null != cursor) {
				if (!cursor.isClosed()) {
					cursor.close();
				}
			}
		}
	}

	/**
	 * 清除全部的记录
	 * */
	public static void clearAll(Context context, String uid) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(SQLDao.SEARCH, "uid" + "=?", new String[] { uid });
	}

	/**
	 * 判断是否存在记录 o
	 * */
	@SuppressWarnings("unused")
	private static boolean hasSearchTrace(Context context, String name) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		int count = 0;
		try {
			cursor = db.query(SQLDao.SEARCH, null, "name" + "=?",
					new String[] { name }, null, null, null);
			if (cursor != null) {
				count = cursor.getCount();
			}
			if (count > 0) {
				return true;
			} else {
				return false;
			}

		} finally {
			if (null != cursor) {
				if (!cursor.isClosed()) {
					cursor.close();
				}
			}
		}

	}

	/**
	 * 
	 * TODO<删除手势密码>
	 * 
	 * @throw
	 * @return void
	 */
	@SuppressWarnings("resource")
	public static void removeOne(Context context, String shopsid) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(SQLDao.SEARCH, new String[] { "_id", "shopsid",
					"gestureNumber" }, "shopsid" + "=?",
					new String[] { shopsid }, null, null, "");
			while (cursor.getCount() >= 1) {
				cursor.moveToNext();
				String name = cursor.getString(cursor
						.getColumnIndexOrThrow("gestureNumber"));
				db.delete(SQLDao.SEARCH, "gestureNumber" + "=?",
						new String[] { name + "" });

				cursor = db.query(SQLDao.SEARCH, new String[] { "_id",
						"shopsid", "gestureNumber" }, "shopsid" + "=?",
						new String[] { shopsid }, null, null, "");
			}
		} finally {
			if (null != cursor) {
				if (!cursor.isClosed()) {
					cursor.close();
				}
			}
		}
	}
}
