package com.example.oto01.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.oto01.R;

public class CoverView extends View {
	private static Context mContext;
	private Bitmap mBitmap, mBitmap1;
	private Rect mRect;
	private RectF mRectF;
	private Paint mPaint, mPaintOther, mPaint2;

	public static float IMAGE_X;
	public static float IMAGE_Y;
	public static float IMAGE_WIDTH;
	public static float IMAGE_HEIGHT;
	public static float IMAGE_XX;
	public static float IMAGE_YY;

	public CoverView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public CoverView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		IMAGE_X = mContext.getResources().getDimension(R.dimen.iv_padding_left);
		IMAGE_Y = mContext.getResources().getDimension(R.dimen.iv_padding_top);
		IMAGE_WIDTH = mContext.getResources().getDimension(R.dimen.iv_width);
		IMAGE_HEIGHT = mContext.getResources().getDimension(R.dimen.iv_height);
		IMAGE_XX = mContext.getResources().getDimension(R.dimen.w_44);
		IMAGE_YY = mContext.getResources().getDimension(R.dimen.h_75);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		mBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.online_kuang, opts);
		mBitmap1 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.online_wenzi, opts);

		mRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());

		mRectF = new RectF(IMAGE_X, IMAGE_Y, IMAGE_X + IMAGE_WIDTH, IMAGE_Y
				+ IMAGE_HEIGHT);

		mPaint = new Paint();

		mPaintOther = new Paint();
		mPaint2 = new Paint();
		// 设置Paint对象颜色，参数一为alpha透明通道
		mPaintOther.setARGB(178, 0, 0, 0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawBitmap(mBitmap, mRect, mRectF, mPaint);

		mPaintOther.setARGB(178, 70, 70, 70);
		canvas.drawRect(0, 0, IMAGE_X, getScreenHeight(), mPaintOther);// 左

		mPaintOther.setARGB(178, 70, 70, 70);
		canvas.drawRect(IMAGE_X, 0, IMAGE_X + IMAGE_WIDTH, IMAGE_Y, mPaintOther);// 上

		mPaintOther.setARGB(178, 70, 70, 70);
		canvas.drawRect(IMAGE_X, IMAGE_Y + IMAGE_HEIGHT, IMAGE_X + IMAGE_WIDTH,
				getScreenHeight(), mPaintOther);// 下

		mPaintOther.setARGB(178, 70, 70, 70);
		canvas.drawRect(IMAGE_X + IMAGE_WIDTH, 0, getScreenWidth(),
				getScreenHeight(), mPaintOther);// 右

		canvas.drawBitmap(mBitmap1, IMAGE_XX, IMAGE_Y + IMAGE_YY, mPaint2);

	}

	public float getScreenWidth() {
		Resources resources = mContext.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int width = dm.widthPixels;

		return width;
	}

	public float getScreenHeight() {
		Resources resources = mContext.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int height = dm.heightPixels;

		return height;
	}

}
