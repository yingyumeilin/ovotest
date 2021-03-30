package com.example.oto01.task;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.example.oto01.model.Constant;
import com.example.oto01.model.RongIMBean;
import com.example.oto01.utils.HttpUtil;
import com.google.gson.Gson;

/**
 * 获取融云token
 * */
public class GetTokenTask extends AsyncTask<Void, Void, RongIMBean> {
	
	private Context context;
	private String mUserId;
	private OnGetTokenLisener lisener;
	
	public GetTokenTask(Context context, String userId) {
		this.context = context;
		this.mUserId = userId;// ID
	}

	protected RongIMBean doInBackground(Void... params) {
		JSONObject json = new JSONObject();
		RongIMBean rongBean = null;
		try {
			json.put("shopsid", mUserId);
			String request = HttpUtil.doPost(Constant.GET_RONG_TOKEN,"shopsid",mUserId);
			JSONObject jsonObject =null;
			try{
			    jsonObject = new JSONObject(request);
			    //yes, it is
			}catch(Exception e){
			    //no, it isnt
			}
			if(jsonObject!=null&&jsonObject.optString("res").equals("0")){
				Gson gson = new Gson();
				rongBean = gson.fromJson(jsonObject.optString("data"), RongIMBean.class);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rongBean;
	}

	protected void onPreExecute() {
		
	}

	protected void onPostExecute(RongIMBean rongBean) {
		if (lisener != null) {
			lisener.OnGetToken(rongBean);
		}
	}
	
	public interface OnGetTokenLisener {
		public void OnGetToken(RongIMBean rongBean);
	}
	
	public void setOnGetTokenLisener(OnGetTokenLisener lisener) {
		this.lisener = lisener;
	}
}