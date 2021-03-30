package com.example.oto01.views;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.example.oto01.R;


/**
 * 鏄剧ず绉诲姩鑳屾櫙鐨勮鍥�
 * 
 * @author Administrator
 * 
 */
public class MyBackground extends View {
	private Context mContext;
	// 鑳屾櫙鍥剧墖
	private Bitmap back;
	// 璁板綍鑳屾櫙浣嶅浘鐨勫疄闄呴珮搴�
	final int BACK_WIDTH = 960;
	final int BACK_HEIGHT = 960;
	// 鑳屾櫙鍥剧墖鐨勫紑濮嬩綅缃�
	final int WIDTH = getResources().getDisplayMetrics().widthPixels;
	final int HEIGHT = getResources().getDisplayMetrics().heightPixels;
	private int startX = 0;
	private int maxX = 0;
//	private int startY = HEIGHT - BACK_HEIGHT;
	boolean flag = true;// 琛ㄧず鍚戝彸绉诲姩
//	private float scaleX = 1f;

	@SuppressLint("HandlerLeak")
	public MyBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		/**
		 * 鑾峰彇鍏变韩鍙傛暟涓殑鑳屾櫙鍥剧墖鐨刬d
		 */
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				"bj", Context.MODE_PRIVATE);
		int id = sharedPreferences.getInt("bj", -1);
		if (id == -1) {
			id = R.drawable.rootblock_default_bg_1;
		}
		back = BitmapFactory.decodeResource(mContext.getResources(), id);
		System.out.println("--------back------->" + back.getWidth());
		maxX = back.getWidth() - WIDTH;
		System.out.println("--------maxX------->" + maxX);

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 閲嶆柊寮�绉诲姩
					// if (startX <= 1) {
					// startX = BACK_WIDTH - WIDTH;
					// } else {
					// startX -= 1;
					// }
					/*if (startX == BACK_WIDTH - WIDTH) {
						flag = false;
					} else {
						flag = true;
					}
					if (flag) {
						startX++;
					} else {
						startX = 0;
					}
					if (scaleX <= 1f && scaleX >= 0.8f) {
						scaleX -= 0.005;
					} else {
						scaleX = 1f;
					}*/
					if(flag){//向右移动
						if(startX == maxX){
							flag = false;
						}else{
							startX ++ ;
						} 
						
					}else{
						if(startX == 0){
							flag = true;
						}else{
							startX -- ;
						}
					}
				}
				postInvalidate();
			}
		};
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}

		}, 0, 80);
	}

	public MyBackground(Context context) {
		super(context);
		mContext = context;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (back != null) {
			// java.lang.IllegalArgumentException: x + width must be <=
			// bitmap.width()

			// System.out.println("-------WIDTH------->" + WIDTH);
			// System.out.println("-------startX------->" + startX);
			// System.out.println("-------HEIGHT------->" + HEIGHT);
			// System.out.println("-------back------->" + back.getWidth());
			// System.out.println("----(startX / BACK_WIDTH)------>"
			// + (startX / BACK_WIDTH));

			// Matrix matrix = new Matrix();
			// matrix.reset();
			// matrix.postScale(scaleX, scaleX);// 鎺у埗缂╂斁
			// matrix.postTranslate(0, 0);// 鎺у埗骞崇Щ
			// canvas.drawBitmap(back, matrix, null);
			// 鏍规嵁鍘熷浣嶅浘鍒涘缓鏂板浘鐗�
			Bitmap bitmap2 = Bitmap.createBitmap(back, startX, 0, WIDTH, HEIGHT, null, true);
			// 缁樺埗鏂颁綅鍥�
			canvas.drawBitmap(bitmap2, 0, 0, null);
			bitmap2.recycle();
		} else {
			System.out.println("----back涓虹┖---->");
		}
	}
}
