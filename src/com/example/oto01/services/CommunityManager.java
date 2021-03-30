package com.example.oto01.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CommunityManager {
	private Context mContext;

	public CommunityManager(Context mContext) {
		super();
		this.mContext = mContext;
	}
	public void setLocation(String longitude,String latitude){
		SharedPreferences sp = mContext.getSharedPreferences("locationInfo", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("longitude", longitude);
		editor.putString("latitude", latitude);
		editor.commit();
	}
	public String getLongitude() {
		SharedPreferences sp = mContext.getSharedPreferences("locationInfo",
				Context.MODE_PRIVATE);
		return sp.getString("longitude", "");
	}

	public String getLatitude() {
		SharedPreferences sp = mContext.getSharedPreferences("locationInfo",
				Context.MODE_PRIVATE);
		return sp.getString("latitude", "");
	}
}
