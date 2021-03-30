package com.example.oto01.model;

public class GestureNumber {

//	public int id;// 编号
	public int shopsid;// 店铺id
	public String gestureNumber;// 手势密码的保存

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	public int getShopsid() {
		return shopsid;
	}

	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}

	public String getGestureNumber() {
		return gestureNumber;
	}

	public void setGestureNumber(String gestureNumber) {
		this.gestureNumber = gestureNumber;
	}


	public GestureNumber() {
		super();
	}

	public GestureNumber( int shopsid, String gestureNumber) {
		super();
		this.shopsid = shopsid;
		this.gestureNumber = gestureNumber;
	}

}
