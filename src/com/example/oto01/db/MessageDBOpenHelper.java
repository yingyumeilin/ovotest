package com.example.oto01.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDBOpenHelper extends SQLiteOpenHelper {

	public static final String NAME = "message.db";
	private static final int VERSION = 1;

	public MessageDBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + SQLDao.MESSAGETABLE
				+ " ( " + "_id integer primary key autoincrement,"
				+ "id text,uid text," + "orderid text,shopsid text,"
				+ "cid text, infortype text," + "title text,introduction text,"
				+ "addtime text,is_readmessage text,is_mark text,total text,num text,ordernum text)";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS shop");
		switch (oldVersion) {
		case 1:
		case 2:
			db.execSQL("DROP TABLE IF EXISTS " + SQLDao.MESSAGETABLE);
			onCreate(db);
			break;
		}

	}

}
