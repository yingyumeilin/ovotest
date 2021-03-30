package com.example.oto01.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

public class NetConn {
	/***************************************************
	 * �?��网络连接
	 * @param context 上下�?
	 * @return true 网络正常 fasle 无网�?并弹出Dialog提示设置网络
	 */
	public static  boolean checkNetwork(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			AlertDialog.Builder builder = new Builder(context);
			  builder.setMessage("网络不给力");
			  builder.setTitle("提示");
			  builder.setPositiveButton("确认", new OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });
			final Dialog dialog = builder.create();
			if(context instanceof Activity)
				if(!((Activity)context).isFinishing()){
					new Handler(context.getMainLooper()).post(new Runnable() {
						public void run() {
							dialog.show();
						}
					});
				}	

			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}
	/**
     * 网络未连接时，调用设置方�?
     */
    public static  void setNetwork(final Context context){
        Toast.makeText(context, "wifi is closed!", Toast.LENGTH_SHORT).show();
         
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络提示信息");
        builder.setMessage("网络不可用，如果继续，请先设置网络！");
        builder.setCancelable(false);
        builder.setPositiveButton("设置", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置�?.0以下的设置不�?��，调用的方法不同
                 */
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        });
 
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
 
            }
        });
        builder.create();
        builder.show();
    }
}
