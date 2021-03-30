package com.example.oto01.services;

import org.json.JSONObject;

import android.content.Context;

import com.example.oto01.model.Constant;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

public class RequestServices extends BaseHttpService{

	public RequestServices(Context context) {
		super(context);
	}
	
	public void request(String clientid,int shopsid){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("clientid", clientid);
			System.out.println("------json----->"+jsonObject.toString());
			String res = HttpUtil.doPost(Constant.GET_CLIENTID_URI,"parm", jsonObject.toString());
			
			
			System.out.println("------request--------->"+res);
//			JSONObject jObject= new JSONObject(res);
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
	}
}
