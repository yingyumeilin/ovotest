package com.example.oto01.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.oto01.model.Messages;
import com.example.oto01.services.LoginManager;

/**
 * 
 * @类描述:消息列表的增删改查
 * @项目名称: 社区e商户
 * @类名称: MessageDBService
 * @包名称: com.example.oto01.db
 * @author: cym
 * @date: 2016-11-16上午10:44:33
 */
public class MessageDBService {

	private MessageDBOpenHelper dbHelper;
	private SQLiteDatabase db;
	private int userId;

	// private ContentResolver resolver;
	public MessageDBService(Context context) {
		try {
			dbHelper = new MessageDBOpenHelper(context);
			db = dbHelper.getWritableDatabase();
			LoginManager lm = LoginManager.getInstance(context);
			// 此处userId 是 店铺id
			userId = lm.getLoginId();
		} catch (Exception e) {
			// TODO: handle exception
			dbHelper = new MessageDBOpenHelper(context);
			db = dbHelper.getWritableDatabase();
			LoginManager lm = LoginManager.getInstance(context);
			userId = lm.getLoginId();
		}

	}

	/**
	 * 删除 delete old person
	 * 
	 * @param person
	 */
	/*
	 * public void deleteById(int productId) { db.delete("product_new",
	 * "product_id=?", new String[] { productId + "" }); if (db != null) {
	 * db.close(); } }
	 */

	/************************************ 新的数据结构 *****************************************************************/

	/**
	 ** 查询所有 消息列表
	 * 
	 * @return List<Person>
	 */
	public List<Messages> getAllMsgs() {
		ArrayList<Messages> list = new ArrayList<Messages>();
		String str = "select * from " + SQLDao.MESSAGETABLE
				+ " where shopsid = '" + userId + "' and infortype !='1'"
				+ "order by addtime desc";
		Cursor c = db.rawQuery(str, null);
		c.moveToFirst();
		c.moveToPrevious();

		while (c.moveToNext()) {

			try {
				Messages messageDataListBean = new Messages();
				messageDataListBean.setAddtime(c.getString(c
						.getColumnIndex("addtime")));
				messageDataListBean
						.setUid(c.getString(c.getColumnIndex("uid")));
				messageDataListBean.setTitle(c.getString(c
						.getColumnIndex("title")));
				messageDataListBean.setOrdernum(c.getString(c
						.getColumnIndex("ordernum")));
				messageDataListBean.setOrderid(c.getString(c
						.getColumnIndex("orderid")));
				messageDataListBean.setIntroduction(c.getString(c
						.getColumnIndex("introduction")));
				messageDataListBean.setInfortype(c.getString(c
						.getColumnIndex("infortype")));
				messageDataListBean.setId(c.getInt(c.getColumnIndex("id")));
				messageDataListBean.setIs_readmessage(c.getString(c
						.getColumnIndex("is_readmessage")));
				messageDataListBean.setShopsid(c.getString(c
						.getColumnIndex("shopsid")));
				messageDataListBean
						.setCid(c.getString(c.getColumnIndex("cid")));
				messageDataListBean.setIs_mark(c.getString(c
						.getColumnIndex("is_mark")));
				messageDataListBean.setTotal(c.getString(c
						.getColumnIndex("total")));
				messageDataListBean
						.setNum(c.getString(c.getColumnIndex("num")));

				list.add(messageDataListBean);
			} catch (Exception e) {
				e.getMessage();
			}
		}
		if (c != null) {
			c.close();
		}
		if (db != null) {
			db.close();
		}
		// c.close();
		return list;
	}

	/**
	 ** 查询 某个消息
	 * 
	 * @return List<Person>
	 */
	public Messages getMessageDataListBean(String id) {
		String str = "select * from " + SQLDao.MESSAGETABLE
				+ " where (shopsid = '" + userId + "' and " + "id = '" + id
				+ "')";
		Cursor c = db.rawQuery(str, null);
		c.moveToFirst();
		c.moveToPrevious();

		Messages messageDataListBean = new Messages();
		while (c.moveToNext()) {
			messageDataListBean.setAddtime(c.getString(c
					.getColumnIndex("addtime")));
			messageDataListBean.setUid(c.getString(c.getColumnIndex("uid")));
			messageDataListBean
					.setTitle(c.getString(c.getColumnIndex("title")));
			messageDataListBean.setOrdernum(c.getString(c
					.getColumnIndex("ordernum")));
			messageDataListBean.setOrderid(c.getString(c
					.getColumnIndex("orderid")));
			messageDataListBean.setIntroduction(c.getString(c
					.getColumnIndex("introduction")));
			messageDataListBean.setInfortype(c.getString(c
					.getColumnIndex("infortype")));
			messageDataListBean.setId(c.getInt(c.getColumnIndex("id")));
			messageDataListBean.setIs_readmessage(c.getString(c
					.getColumnIndex("is_readmessage")));
			messageDataListBean.setShopsid(c.getString(c
					.getColumnIndex("shopsid")));
			messageDataListBean.setCid(c.getString(c.getColumnIndex("cid")));
			messageDataListBean.setIs_mark(c.getString(c
					.getColumnIndex("is_mark")));
			messageDataListBean
					.setTotal(c.getString(c.getColumnIndex("total")));
			messageDataListBean.setNum(c.getString(c.getColumnIndex("num")));
		}
		if (c != null) {
			c.close();
		}
		if (db != null) {
			db.close();
		}
		// c.close();
		return messageDataListBean;
	}

	// public Cursor queryTheCursor(String userId, String shopId, String
	// goodsId) {
	// Cursor c = db.rawQuery("SELECT * FROM " + SQLDao.MESSAGETABLE
	// + " WHERE (uid = '" + userId + "' and " + "shopsid = '"
	// + shopId + "')", null);
	// return c;
	// }

	/***********************************************************
	 ** 添加
	 * 
	 * @param persons
	 */
	public void insertData(List<Messages> list) {
		db.beginTransaction(); // 开始事务
		try {
			for (Messages messageDataListBean : list) {
				String str = ""
						+ "insert into "
						+ SQLDao.MESSAGETABLE
						+ " (id,uid,orderid,shopsid,cid,infortype,title,introduction,addtime,is_readmessage,is_mark,total,num,ordernum)"
						+ "values  ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				db.execSQL(str,
						new Object[] {
								messageDataListBean.getId(),
								messageDataListBean.getUid(),
								messageDataListBean.getOrderid(),
								userId,
								messageDataListBean.getCid(),
								messageDataListBean.getInfortype(),
								messageDataListBean.getTitle(),
								messageDataListBean.getIntroduction(),
								messageDataListBean.getAddtime(),
								"1",// 是否已读 插入时全部未读
								messageDataListBean.getIs_mark(),
								messageDataListBean.getTotal(),
								messageDataListBean.getNum(),
								messageDataListBean.getOrdernum() });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 更新消息的是否已读
	 * */
	public void updateIsReadById(String id, String is_read) {
		db.beginTransaction(); // 开始事务
		try {
			String str = "update " + SQLDao.MESSAGETABLE
					+ " set is_readmessage = " + is_read
					+ " where (shopsid = '" + userId + "' and " + "id = '" + id
					+ "')";
			db.execSQL(str);
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 更新消息的是否已读
	 * */
	public void updateIsRead(List<Messages> list, String is_read) {
		db.beginTransaction(); // 开始事务
		try {
			String str = null;
			for (int i = 0; i < list.size(); i++) {
				str = "update " + SQLDao.MESSAGETABLE
						+ " set is_readmessage = " + is_read
						+ " where (shopsid = '" + userId + "' and " + "id = '"
						+ list.get(i).getId() + "')";
				db.execSQL(str);
			}
			db.execSQL(str);
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 查询 是否还有未读消息
	 */
	public boolean getUnRead() {
		String str = "select is_readmessage from " + SQLDao.MESSAGETABLE
				+ " where shopsid = " + userId;
		Cursor c = db.rawQuery(str, null);
		c.moveToFirst();
		c.moveToPrevious();
		while (c.moveToNext()) {
			try {
				if (c.getString(c.getColumnIndex("is_readmessage")).equals("1")) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (c != null) {
			c.close();
		}
		if (db != null) {
			db.close();
		}
		return false;
	}

	/**
	 * 
	 * 功能:删除消息
	 * 
	 * @param list
	 * @author: liqq
	 * @date:2015-7-15下午4:36:49
	 */
	public void deleteMsg(List<Messages> list) {
		db.beginTransaction(); // 开始事务
		try {
			String str = null;
			for (int i = 0; i < list.size(); i++) {
				str = "delete from " + SQLDao.MESSAGETABLE + " where (id = '"
						+ list.get(i).getId() + "' and " + "shopsid = '"
						+ userId + "')";
				db.execSQL(str);
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 删除某条消息
	 */
	public void deleteMsgById(String id) {
		db.beginTransaction(); // 开始事务
		try {
			String str = "delete from " + SQLDao.MESSAGETABLE
					+ " where (id = '" + id + "' and " + "shopsid = '" + userId
					+ "')";
			db.execSQL(str);
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
			if (db != null) {
				db.close();
			}
		}
	}

	/***************************** 手势密码数据结构 *************************************/

	// /**
	// ** 查询所有 消息列表
	// *
	// * @return List<Person>
	// */
	// public List<Messages> getAllMsgs() {
	// ArrayList<Messages> list = new ArrayList<Messages>();
	// String str = "select * from " + SQLDao.MESSAGETABLE
	// + " where shopsid = '" + userId + "' and infortype !='1'"
	// + "order by addtime desc";
	// Cursor c = db.rawQuery(str, null);
	// c.moveToFirst();
	// c.moveToPrevious();
	//
	// while (c.moveToNext()) {
	//
	// try {
	// Messages messageDataListBean = new Messages();
	// messageDataListBean.setAddtime(c.getString(c
	// .getColumnIndex("addtime")));
	// messageDataListBean
	// .setUid(c.getString(c.getColumnIndex("uid")));
	// messageDataListBean.setTitle(c.getString(c
	// .getColumnIndex("title")));
	// messageDataListBean.setOrdernum(c.getString(c
	// .getColumnIndex("ordernum")));
	// messageDataListBean.setOrderid(c.getString(c
	// .getColumnIndex("orderid")));
	// messageDataListBean.setIntroduction(c.getString(c
	// .getColumnIndex("introduction")));
	// messageDataListBean.setInfortype(c.getString(c
	// .getColumnIndex("infortype")));
	// messageDataListBean.setId(c.getInt(c.getColumnIndex("id")));
	// messageDataListBean.setIs_readmessage(c.getString(c
	// .getColumnIndex("is_readmessage")));
	// messageDataListBean.setShopsid(c.getString(c
	// .getColumnIndex("shopsid")));
	// messageDataListBean
	// .setCid(c.getString(c.getColumnIndex("cid")));
	// messageDataListBean.setIs_mark(c.getString(c
	// .getColumnIndex("is_mark")));
	// messageDataListBean.setTotal(c.getString(c
	// .getColumnIndex("total")));
	// messageDataListBean
	// .setNum(c.getString(c.getColumnIndex("num")));
	//
	// list.add(messageDataListBean);
	// } catch (Exception e) {
	// e.getMessage();
	// }
	// }
	// if (c != null) {
	// c.close();
	// }
	// if (db != null) {
	// db.close();
	// }
	// // c.close();
	// return list;
	// }

	
}
