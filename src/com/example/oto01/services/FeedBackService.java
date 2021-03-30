package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.example.oto01.model.Constant;
import com.example.oto01.model.FeedBack;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

public class FeedBackService extends BaseHttpService {

	public int total_page = 0;
	public int p = 0;
	public FeedBackService(Context context) {
		super(context);
	}
	
	/**
	 * 得到问题反馈列表
	 * @param p
	 * @return
	 */
	public List<FeedBack> getFeedBackList(int p){
		try {
			String res = HttpUtil.doGet(Constant.FEED_BACK_LIST_URI+"&p="+p);
			
			return handleFeedBackListRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}
	
	private List<FeedBack> handleFeedBackListRes(String res){
		try {
			JSONObject jsonObject  = new JSONObject(res);
			if(jsonObject.optInt("res")==0){
				total_page = jsonObject.optInt("totalnum", -1);
				p = jsonObject.optInt("p", -1);
				System.out.println("-----totalnum----->"+total_page);
				System.out.println("-----p----->"+p);
				List<FeedBack> list = new ArrayList<FeedBack>();
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = jsonArray.getJSONObject(i);
					int id = -1;
					if(!jo.isNull("id")){
						id = jo.getInt("id");
					}
					String content = "";
					if(!jo.isNull("content")){
						content = jo.getString("content");
					}
					String addtime = "";
					if(!jo.isNull("addtime")){
						addtime = jo.getString("addtime");
					}
					String phone = "";
					if(!jo.isNull("phone")){
						phone = jo.getString("phone");
					}
					String backcontent = "";
					if(!jo.isNull("backcontent")){
						backcontent = jo.getString("backcontent");
					}
					FeedBack feedBack = new FeedBack(id, content, addtime, phone, backcontent);
					list.add(feedBack);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 得到问题反馈详情
	 * @param id
	 * @return
	 */
	public FeedBack getFeedBackDetails(int id){
		try {
			String res = HttpUtil.doGet(Constant.FEED_BACK_DETAILS_URI+"&id="+id);
			return handleFeedBackDetailsRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}

		}
		return null;
	}
	
	private FeedBack handleFeedBackDetailsRes(String res){
		try {
			JSONObject jsonObject = new JSONObject(res);
			if(jsonObject.optInt("res")==0){
				JSONObject jo = jsonObject.getJSONObject("data");
					String addtime = "";
					if(!jo.isNull("addtime")){
						addtime = jo.getString("addtime");
					}
					String phone = "";
					if(!jo.isNull("phone")){
						phone = jo.getString("phone");
					}
					String backcontent = "";
					if(!jo.isNull("backcontent")){
						backcontent = jo.getString("backcontent");
					}
					FeedBack feedBack = new FeedBack(addtime, phone, backcontent);
					return feedBack;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
