package com.example.oto01.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GesturePasswordDBOpenHelper extends SQLiteOpenHelper {

	public static final String NAME = "search.db";
	private static final int VERSION = 1;

	public GesturePasswordDBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "create table " + SQLDao.SEARCH + " ( "
				+ "_id integer primary key autoincrement,"
				+ "shopsid nvarchar(1000)," + "gestureNumber nvarchar(1000))";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS shop");
		switch (oldVersion) {
		case 1:
		case 2:
			db.execSQL("DROP TABLE IF EXISTS " + SQLDao.SEARCH);
			onCreate(db);
			break;
		}

	}

}
