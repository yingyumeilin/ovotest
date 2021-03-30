package com.example.oto01.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.example.oto01.R;

public class ConfirmOrderPopupWindow extends PopupWindow {

	private ImageButton btn_share_weibo, btn_share_qq, btn_share_taobao;
	private View mMenuView;
	private RadioButton theriDistriRadiobutton,stewardDistriRadiobutton;
	private Button deliverGoodsButton;

	public ConfirmOrderPopupWindow(Activity context, OnClickListener itemsOnClick,OnCheckedChangeListener changeOnClick,int is_dis) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.confirm_order_popupwindow, null);
		theriDistriRadiobutton = (RadioButton)mMenuView.findViewById(R.id.their_distribution_radiobutton);
		stewardDistriRadiobutton = (RadioButton)mMenuView.findViewById(R.id.steward_distribution_radiobutton);
		deliverGoodsButton = (Button) mMenuView.findViewById(R.id.deliver_goods_button);
		deliverGoodsButton.setOnClickListener(itemsOnClick);
		theriDistriRadiobutton.setOnCheckedChangeListener(changeOnClick);
		stewardDistriRadiobutton.setOnCheckedChangeListener(changeOnClick);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(R.color.titlecolor);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.share_pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
