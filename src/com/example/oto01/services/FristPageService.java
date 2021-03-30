package com.example.oto01.services;

import org.json.JSONObject;

import android.content.Context;

import com.example.oto01.model.Constant;
import com.example.oto01.model.FristPage;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

public class FristPageService extends BaseHttpService {

	public FristPageService(Context context) {
		super(context);
	}

	/**
	 * 得到首页商铺信息
	 * @param shopsid
	 * @return
	 */
	public FristPage getFristPage(int shopsid){
		try {
			String res = HttpUtil.doGet(Constant.HOME_PAHE_URI
					+ "&shopsid=" + shopsid);
			System.out.println("-------url------->"+Constant.HOME_PAHE_URI
					+ "&shopsid=" + shopsid);
			System.out.println("-------res------->"+res);
			
			return handleGetShopInfoRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}
	private FristPage handleGetShopInfoRes(String res) {
		// TODO Auto-generated method stub
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				JSONObject jsonObject = jo.getJSONObject("data");
				int waitorder = 0;
				if(!jsonObject.isNull("waitorder")&& jsonObject.get("waitorder")!=null){
					waitorder = jsonObject.getInt("waitorder");
				}
				int haveorder = 0;
				if(!jsonObject.isNull("haveorder")&& jsonObject.get("haveorder")!=null){
					haveorder = jsonObject.getInt("haveorder");
				}
				FristPage fristPage = new FristPage(jsonObject.getString("shopsname"), jsonObject.getString("total_today"), jsonObject.getString("total_yesterday"), waitorder, haveorder,jsonObject.getString("comname"),jsonObject.getString("logo"));
				System.out.println("-------fristPage------->"+fristPage);
				return fristPage;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*public ShopInfo getShopInfo(int shopsid){
		try {
			String res = HttpUtil.doGet(Constant.SHOP_INFO_URI
					+ "&shopsid=" + shopsid);
			System.out.println("-------url------->"+Constant.SHOP_INFO_URI
					+ "&shopsid=" + shopsid);
			System.out.println("-------res------->"+res);
			
			return handleGetShopInfoRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private ShopInfo handleGetShopInfoRes(String res) {
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
//				JSONArray jsonArray = (JSONArray) jo.get("indexstore");
//				for(int i=0;i<jsonArray.length();i++){
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					ShopInfo shopInfo = new ShopInfo(jsonObject.getInt("id"), jsonObject.getString("shopsname"), jsonObject.getString("picpath"), jsonObject.getString("logo"), jsonObject.getInt("level"));
//				}
				JSONObject jsonObject = jo.getJSONObject("indexstore");
				int level = 0;
				if(!jsonObject.isNull("level")&& jsonObject.get("level")!=null){
					level = jo.getInt("level");
				}
				ShopInfo shopInfo = new ShopInfo(jsonObject.getString("shopsname"), jsonObject.getString("picpath"), jsonObject.getString("logo"), jsonObject.getString("comname"));
				System.out.println("-------shopinfo------->"+shopInfo);
				return shopInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	*//**
	 * 得到首页订单数量
	 * @param shopsid
	 * @return
	 *//*
	public Order getOrderNum(int shopsid){
		try {
			System.out.println("-------url------>"+Constant.OREDR_NUM_URI
					+ "&shopsid=" + shopsid);
			String res = HttpUtil.doGet(Constant.OREDR_NUM_URI
					+ "&shopsid=" + shopsid);
			
			System.out.println("-------getOrderNum---------->"+res);
			return handleGetOrderNumRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private Order handleGetOrderNumRes(String res) {
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				int todayorder = 0;
				if(jo.getString("todayorder").length()>0){
					todayorder = jo.getInt("todayorder");
				}
				String todayrev = "";
				if(jo.getString("todayrev").length()>0){
					todayrev = jo.getString("todayrev");
				}
				int haveorder = 0;
				if(jo.getString("haveorder").length()>0){
					haveorder = jo.getInt("haveorder");
				}
				int waitorder = 0;
				if(jo.getString("waitorder").length()>0){
					waitorder = jo.getInt("waitorder");
				}
				Order order = new Order(todayorder,todayrev, waitorder, haveorder);
				return order;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	*//**
	 * 得到首页商品数量
	 * @param shopsid
	 * @return
	 *//*
	public Good getGoodNum(int shopsid){
		try {
			String res = HttpUtil.doGet(Constant.GOOD_NUM_URI
					+ "&shopsid=" + shopsid);
			System.out.println("------url----->"+Constant.GOOD_NUM_URI
					+ "&shopsid=" + shopsid);
			
			System.out.println("------getGoodNum-------->"+res);
			return handleGetGoodNumRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private Good handleGetGoodNumRes(String res) {
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				int regoods = 0;
				if(jo.getString("regoods").length()>0){
					regoods = jo.getInt("regoods");
				}
				int soldgoods = 0;
				if(jo.getString("soldgoods").length()>0){
					soldgoods = jo.getInt("soldgoods");
				}
				String yesrevenue = "";
				if(jo.getString("yesrevenue").length()>0){
					yesrevenue = jo.getString("yesrevenue");
				}
				Good good = new Good(regoods, soldgoods, yesrevenue);
				return good;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	//Key：parm

/*请求格式：
{
    "sharefrom": 1,
    "shopsid": 1,
    "uid": ,
    "share_shopsid": "3",
    "share_goodsid": "34",
    "url": ""
}*/

	

	/**
	 * 首页分享店铺信息
	 * @param sharefrom
	 * @param shopsid
	 * @param share_goodsid
	 * @param url
	 * @return
	 */
	public boolean shareShopInfo(int sharefrom,int shopsid ,int share_shopsid ,String url,String content){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sharefrom", sharefrom);
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("share_shopsid", share_shopsid);
			jsonObject.put("url", url);
			jsonObject.put("content", content);
			
			String res = HttpUtil.doPost(Constant.SHARE_SHOP_URI, "parm", jsonObject.toString());
			System.out.println("-----shareShopInfo-------->"+res);
			JSONObject jObject = new JSONObject(res);
			if( jObject.optInt("res") == 0 ){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
