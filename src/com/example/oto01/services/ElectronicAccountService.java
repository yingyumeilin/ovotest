package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.oto01.model.BankInfo;
import com.example.oto01.model.Constant;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

public class ElectronicAccountService extends BaseHttpService {
	public String errorReason ;
	public String jumpurl ;
	public ElectronicAccountService(Context context) {
		super(context);
	}

	/**
	 * 开通已有账户的 电子账户开通
	 * @param shopsid
	 * @param MerchName
	 * @param Name
	 * @param PAN
	 * @return
	 */
	public int openElectronicOldAccount(int shopsid,String MerchName,String Name,String PAN){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("MerchName", MerchName);
			jsonObject.put("Name", Name);
			jsonObject.put("frombank", 1);
			jsonObject.put("PAN", PAN);
			System.out.println("------json------->"+jsonObject.toString());
			String res = HttpUtil.doPost(Constant.OPEN_OLD_ELECTRONIC_ACCOUNT_URI,"parm",jsonObject.toString());
			System.out.println("-----openElectronicOldAccount----->"+res);
			
			JSONObject jo = new JSONObject(res);
			if(jo.optInt("res")==0){
				return 0;
			}
			errorReason = jo.optString("msg");
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
		}
		errorReason = "程序错误";
		return -1;
	}
	
	/**
	 * 开通新账户的 电子账户开通
	 * @param shopsid
	 * @param MerchName
	 * @param Name
	 * @param PAN
	 * @return
	 */
	public int openElectronicNewAccount(int shopsid,String MerchName,String Name,String CardNo,String LoginId,String Mobile,String Email,String IdentityImgX,String IdentityImgY){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("MerchName", MerchName);//店铺名称
			jsonObject.put("Name", Name);
			jsonObject.put("CardNo", CardNo);
			jsonObject.put("LoginId", LoginId);
			jsonObject.put("Mobile", Mobile);
			jsonObject.put("Email", Email);
			jsonObject.put("IdentityImgX", IdentityImgX);
			jsonObject.put("IdentityImgY", IdentityImgY);
			String res = HttpUtil.doPost(Constant.OPEN_NEW_ELECTRONIC_ACCOUNT_URI,"parm",jsonObject.toString());
			System.out.println("-------url-------->"+Constant.OPEN_NEW_ELECTRONIC_ACCOUNT_URI);
			System.out.println("------json------->"+jsonObject.toString());
			System.out.println("-----openElectronicNewAccount----->"+res);
			JSONObject jo = new JSONObject(res);
			if(jo.optInt("res")==0){
				return 0;
			}
			errorReason = jo.optString("msg");
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
		}
		errorReason = "程序错误";
		return -1;
	}
	/**
	 * 开通新账户的 电子账户开通
	 * @param shopsid
	 * @param MerchName
	 * @param Name
	 * @param PAN
	 * @return
	 */
	public int openElectronicNewStepOneAccount(int shopsid,String Mobile,String Email,String recode ){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("Mobile", Mobile);
			jsonObject.put("Email", Email);
			jsonObject.put("recode", recode);
			System.out.println("-------url-------->"+Constant.OPEN_NEW_ELECTRONIC_ACCOUNT_STEP_ONE_URI);
			System.out.println("------json------->"+jsonObject.toString());
			String res = HttpUtil.doPost(Constant.OPEN_NEW_ELECTRONIC_ACCOUNT_STEP_ONE_URI,"parm",jsonObject.toString());
			System.out.println("-----openElectronicNewStepOneAccount----->"+res);
			JSONObject jo = new JSONObject(res);
			if(jo.optInt("res")==0){
				return 0;
			}
			errorReason = jo.optString("msg");
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
		}
		errorReason = "程序错误";
		return -1;
	}
	
	
	/**
	 * 获取银行列表
	 * @param parentid
	 * @return
	 */
	public List<BankInfo> getCitieList(int shopsid){
		try {
			String res = null;
			res = HttpUtil.doGet(Constant.BANK_LIST_URI+"&shopsid=" + shopsid);
			return handleBankListRes(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<BankInfo> handleBankListRes(String res) {
		Log.d("TAG", "res handleBankListRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				List<BankInfo> list = new ArrayList<BankInfo>();
				JSONArray ja = jo.getJSONArray("data"); 
				for(int i=0 ;i<ja.length() ; i++){
					JSONObject jsonObject = ja.getJSONObject(i);
					BankInfo info = new BankInfo(jsonObject.getString("BankId"), jsonObject.getString("BankName"));
					list.add(info);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取电子银行绑定手机验证码
	 * @param type
	 * @param phone
	 * @return
	 */
	public int getPhoneCode(int shopsid,String pan,String cardHolderName,String cardHolderId,String phoneNo,String bankId,String amount){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("pan", pan);
			jsonObject.put("cardHolderName", cardHolderName);
			jsonObject.put("cardHolderId", cardHolderId);
			jsonObject.put("phoneNo", phoneNo);
			jsonObject.put("bankId", bankId);
			jsonObject.put("amount", amount);
			System.out.println("------json----->"+jsonObject.toString());
			String res = HttpUtil.doPost(Constant.EA_PHONE_CODE_URI,"parm", jsonObject.toString());
			
			System.out.println("------forgetPass--------->"+res);
			JSONObject jObject= new JSONObject(res);
			if(jObject.optInt("res") == 0){
				return 0;
			}
			errorReason = jObject.optString("msg");
			return jObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		errorReason = "程序错误";
		return -1;
	}
	
	/**
	 * 充值激活
	 * @param type
	 * @param phone
	 * @return
	 */
	public int getRechargeActivate(int shopsid,String pan,String cardHolderName,String cardHolderId,String phoneNo,String bankId,String amount,String validCode){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("pan", pan);
			jsonObject.put("cardHolderName", cardHolderName);
			jsonObject.put("cardHolderId", cardHolderId);
			jsonObject.put("phoneNo", phoneNo);
			jsonObject.put("bankId", bankId);
			jsonObject.put("amount", amount);
			jsonObject.put("validCode", validCode);
			System.out.println("------json----->"+jsonObject.toString());
			String res = HttpUtil.doPost(Constant.RECHARGE_ACTIVATE_PHONE_CODE_URI,"parm", jsonObject.toString());
			
			System.out.println("------forgetPass--------->"+res);
			JSONObject jObject= new JSONObject(res);
			if(jObject.optInt("res") == 0){
				if(!jObject.isNull("jumpurl")){
					jumpurl = jObject.getString("jumpurl");
				}
				return 0;
			}
			errorReason = jObject.optString("msg");
			return jObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		errorReason = "程序错误";
		return -1;
	}
	
	/**
	 * 检验电子账户
	 * @param shopsid
	 * @param cardNo
	 * @param name
	 * @return
	 */
	public int checkElectronicAccount(int shopsid,String cardNo,String name){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopsid", shopsid);
			jsonObject.put("IdNo", cardNo);
			jsonObject.put("Name", name);
			System.out.println("-----------json---------->"+jsonObject.toString());
			String res = HttpUtil.doPost(Constant.ONLINE_CHECK_URI, "parm", jsonObject.toString());
			System.out.println("-----------res---------->"+res);
			JSONObject jObject= new JSONObject(res);
			if(jObject.optInt("res") == 0){
				if(!jObject.isNull("jumpurl")){
					jumpurl = jObject.getString("jumpurl");
				}
				return 0;
			}
			errorReason = jObject.optString("msg");
			return jObject.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if(HttpUtil.TIME_OUT){
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		errorReason = "程序错误";
		return -1;
	}
}
