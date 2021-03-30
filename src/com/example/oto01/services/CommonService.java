package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.example.oto01.model.Constant;
import com.example.oto01.model.Consumer;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;
/**
 * 评论
 * @author liqq
 *
 */
public class CommonService extends BaseHttpService {
	public int total_page = 0;
	public int p = 0;
	public String errorMsg;
	public CommonService(Context context) {
		super(context);
	}
	/**
	 * shopsid	int	店铺id
goodsid	int	商品id（type为2时，必传）
type	int	查看类型（1，店铺；2，商品）
p	int	分页页数
	 * @return
	 */
	public List<Consumer> getConsumers(int shopsid, int goodsid,int type ,int p){
		try {
			String res = null;
			if(type == 1){//店铺评论
				res = HttpUtil.doGet(Constant.COMMON_MSG_URI+"&shopsid="+shopsid+"&type=1&p"+p);
			}else if(type == 2){//商品评论
				res = HttpUtil.doGet(Constant.COMMON_MSG_URI+"&shopsid="+shopsid+"&type=2&goodsid="+goodsid+"&p"+p);
			}
			JSONObject jsonObject = new JSONObject(res);
			if(jsonObject.optInt(res) == 0){
				total_page = jsonObject.optInt("totalnum", -1);
				p = jsonObject.optInt("p", -1);
				JSONArray jsonArray = jsonObject.getJSONArray("reviewlist");
				List<Consumer> list = new ArrayList<Consumer>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.getJSONObject(i);
					Consumer consumer = new Consumer(jObject.getInt("uid"), jObject.getString("nickname"), jObject.getString("content"), jObject.getString("addtime"), jObject.getString("avatarpath"));
					list.add(consumer);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
			 if(HttpUtil.TIME_OUT){
					ToastUtil.show(context, "当前网络状况不好！");
				}
		}
		return null;
	}
}
