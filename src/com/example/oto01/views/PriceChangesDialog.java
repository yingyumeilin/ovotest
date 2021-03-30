package com.example.oto01.views;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.oto01.R;
import com.example.oto01.ShopInfoActivity;
import com.example.oto01.utils.ToastUtil;


public class PriceChangesDialog extends Dialog implements OnClickListener {

	private ShopInfoActivity context;
	private OnPriceChangesDialogListener listener;
	private int price;
	private EditText mEditText;

	public PriceChangesDialog(ShopInfoActivity context) {
		super(context);
		this.context = context;
	}

	public PriceChangesDialog(ShopInfoActivity context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public PriceChangesDialog(ShopInfoActivity context,int price ,OnPriceChangesDialogListener listener) {

		super(context, R.style.package_booking_dialog);

		this.context = context;
		this.listener = listener;
		this.price = price;
		/**
		 * 单击dialog之外的地方，可以dismiss掉dialog setCanceledOnTouchOutside(true);
		 * 不写此句话,默认也可以dismiss掉dialog
		 * 
		 * 如果写了此句话，为true时可以dismiss掉dialog，为false时不可以dismiss掉dialog
		 */
		setCanceledOnTouchOutside(true);

		/*
		 * setContentView(R.layout.goods_add_type_dialog); initView();
		 */
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 实例化数据，也可以在构造方法中实现
		 */
		setContentView(R.layout.hint_price_dialog);
		initView();
	}

	private void initView() {
		findViewById(R.id.confirm_btn).setOnClickListener(this);
		findViewById(R.id.cancelbtn).setOnClickListener(this);
		mEditText = (EditText) findViewById(R.id.send_the_price_editText);
		mEditText.setText(String.valueOf(price));
		mEditText.setSelection(mEditText.getText().toString().length());
	}

	public interface OnPriceChangesDialogListener {
		public void onPriceChangesDialogListener(String content);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.confirm_btn:
			if (!TextUtils.isEmpty(mEditText.getText().toString().trim())) {
				if(Integer.parseInt(mEditText.getText().toString().trim())>100){
					ToastUtil.show(context, "请输入正确起送价格(0到100元)");
				}else{
				if (listener != null) {
					listener.onPriceChangesDialogListener(mEditText.getText().toString().trim());
					dismiss();
					}
				}
			} else {
				ToastUtil.show(context, "请输入起送价格(0到100元)");
			}
			break;
		case R.id.cancelbtn:
			dismiss();
			break;
		}
	}
}
