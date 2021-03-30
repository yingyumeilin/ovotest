package com.example.oto01.imgcrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CropView extends ImageView {
	// 宽和高
	private int width, height;
	// 中心点
	private Point point;

	// private int

	public CropView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CropView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CropView(Context context) {
		super(context);
		// invalidate();
		// TODO Auto-generated constructor stub
	}

	// 重绘
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// int location[] = { 0, 0 };
		// // 获取控件长宽
		// width = this.getWidth();
		// height = this.getHeight();
		// // 获取控件位置
		// this.getLocationOnScreen(location);
		// point = new Point(location[0], location[1]);
		// 画笔
		Paint paint = new Paint();
		// 画笔颜色
		paint.setColor(Color.WHITE);
		// 设置画空心图形
		paint.setStyle(Paint.Style.STROKE);
		// 消除锯齿
		paint.setAntiAlias(true);
		// canvas.drawRect(point.x, point.y, point.y + height, point.x + width,
		// paint);
		canvas.drawRect(point.x, point.y, width, height, paint);
		// 设置画实心图形
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(point.x, point.y, 5, paint);
		canvas.drawCircle(point.x, height, 5, paint);
		canvas.drawCircle(width, point.y, 5, paint);
		canvas.drawCircle( width, height, 5, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_UP:

			break;
		case MotionEvent.ACTION_MOVE:

			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	// 在程序计算view的宽和高时调用
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// 获取控件宽高
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(widthMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// 在计算控件位置时调用
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		point = new Point(left, top);
		super.onLayout(changed, left, top, right, bottom);
	}
}
