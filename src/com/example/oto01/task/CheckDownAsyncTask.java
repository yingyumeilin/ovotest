package com.example.oto01.task;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.example.oto01.model.Constant;
import com.example.oto01.model.DeviceInfoResponse;
import com.example.oto01.model.DeviceResolution;
import com.example.oto01.services.CommunityManager;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.TelephoneUtil;
import com.google.gson.Gson;

public class CheckDownAsyncTask extends
		AsyncTask<Void, Void, DeviceInfoResponse> {
	private CommunityManager cm;
	private Context context;
	private OnCheckDownListener listener;
	private ApplicationInfo appInfo = null;

	public CheckDownAsyncTask(Context context) {
		this.context = context;
		cm = new CommunityManager(context);
	}

	protected void onPreExecute() {
	}

	protected DeviceInfoResponse doInBackground(Void... params) {

		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		nameValuePairs.clear();
		nameValuePairs.add(new BasicNameValuePair("type", "1"));// 设备类型1：安卓
																// 2：IOS
		nameValuePairs.add(new BasicNameValuePair("imei", TelephoneUtil
				.getImei(context)));
		nameValuePairs.add(new BasicNameValuePair("model_name", TelephoneUtil
				.getVendor()));
		nameValuePairs.add(new BasicNameValuePair("model", TelephoneUtil
				.getModel()));
		nameValuePairs.add(new BasicNameValuePair("system_version",
				TelephoneUtil.getSystemVersion()));
		nameValuePairs.add(new BasicNameValuePair("kernel_version",
				TelephoneUtil.getKernelVersion()));
		nameValuePairs.add(new BasicNameValuePair("processor", TelephoneUtil
				.getCpuName()));
		nameValuePairs.add(new BasicNameValuePair("phone", TelephoneUtil
				.getNumber(context)));
		nameValuePairs.add(new BasicNameValuePair("longitude", cm
				.getLongitude()));
		nameValuePairs
				.add(new BasicNameValuePair("latitude", cm.getLatitude()));
		nameValuePairs.add(new BasicNameValuePair("network_type", TelephoneUtil
				.getCurrentNetType(context)));
		nameValuePairs.add(new BasicNameValuePair("screen_resolution", String
				.valueOf(DeviceResolution.screenWidth)
				+ ","
				+ String.valueOf(DeviceResolution.screenHeight)));
		nameValuePairs.add(new BasicNameValuePair("memory", TelephoneUtil
				.getTotalMemory()));
		nameValuePairs.add(new BasicNameValuePair("channel_no",
				appInfo == null ? "" : appInfo.metaData
						.getString("UMENG_CHANNEL")));
		// [type=1, imei=A0000042E019F1, model_name=HUAWEI C8813D, model=,
		// system_version=4.1.1,
		// kernel_version=3.4.0-perf-g659f716-00059-g122464c, processor=, phone,
		// longitude=116.298046, latitude=40.04896, network_type=,
		// screen_resolution=480,854, memory=]
		DeviceInfoResponse response = null;
		try {
			String request = HttpUtil.getInfoByPost(
					Constant.UPLOAD_DEVICE_INFO, nameValuePairs);

			Gson gson = new Gson();
			response = gson.fromJson(request, DeviceInfoResponse.class);
			System.out.println("response" + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	protected void onPostExecute(DeviceInfoResponse response) {
		if (listener != null) {
			listener.onCheckDown(response);
		}
	}

	public interface OnCheckDownListener {
		public void onCheckDown(DeviceInfoResponse response);
	}

	public void setOnCheckDownListener(OnCheckDownListener listener) {
		this.listener = listener;
	}

}