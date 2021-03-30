package com.example.oto01;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.TransUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 
 * @类描述:我的二维码界面
 * @项目名称: 社区e商户
 * @类名称: QRCodeActivity
 * @包名称: com.example.oto01
 * @author: cym
 * @date: 2016-9-26上午11:08:23
 */
public class QRCodeActivity extends BaseActivity implements OnClickListener {

	/**
	 * 标题
	 */
	@ViewInject(R.id.title_font)
	private TextView title_font;
	/**
	 * 到店付二维码 图片
	 */
	@ViewInject(R.id.iv_store_pay_qr)
	private ImageView iv_store_pay_qr;
	/**
	 * 到店付二维码 文字
	 */
	@ViewInject(R.id.tv_store_pay_qr)
	private TextView tv_store_pay_qr;
	/**
	 * 店铺二维码 图片
	 */
	@ViewInject(R.id.iv_store_qr)
	private ImageView iv_store_qr;
	/**
	 * 店铺二维码 文字
	 */
	@ViewInject(R.id.tv_store_qr)
	private TextView tv_store_qr;
	/**
	 * 大二维码下面的文字
	 */
	@ViewInject(R.id.tv_qrcode)
	private TextView tv_qrcode;
	/**
	 * 大的二维码
	 */
	@ViewInject(R.id.iv_qrcode)
	private ImageView iv_qrcode;
	/**
	 * 店铺二维码 总体布局
	 */
	@ViewInject(R.id.rl_pr_store)
	private RelativeLayout rl_pr_store;
	/**
	 * 到店支付二维码 总体布局
	 */
	@ViewInject(R.id.rl_qr_store_pay)
	private RelativeLayout rl_qr_store_pay;
	//  店铺id
	private int shopsid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_code);
		ViewUtils.inject(this);
		LoginManager lm = LoginManager.getInstance(QRCodeActivity.this);
		shopsid = lm.getLoginId();
		title_font.setText("我的二维码");
		rl_pr_store.setOnClickListener(this);
		rl_qr_store_pay.setOnClickListener(this);
		//  初始化二维码的图片 是店铺二维码
		TransUtils.createQRImage(
				"http://bjrcb.1fuwu.com.cn/ApkDownload/serve_download?&type=shops&shopsid="
						+ shopsid + "&is_goods=0", getApplicationContext(),
				iv_qrcode);

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_pr_store:
			// 店铺二维码
			tv_qrcode.setText("店铺二维码");
			iv_store_qr.setImageResource(R.drawable.qr_blue_code);
			tv_store_qr.setTextColor(Color.parseColor("#00b4dc"));
			iv_store_pay_qr.setImageResource(R.drawable.qr_black_code);
			tv_store_pay_qr.setTextColor(Color.parseColor("#888888"));
			TransUtils.createQRImage(
					"http://bjrcb.1fuwu.com.cn/ApkDownload/serve_download?&type=shops&shopsid="
							+ shopsid + "&is_goods=0", getApplicationContext(),
					iv_qrcode);

			break;
		case R.id.rl_qr_store_pay:
			// 到店支付二维码
			tv_qrcode.setText("到店付二维码");
			iv_store_qr.setImageResource(R.drawable.qr_black_code);
			tv_store_qr.setTextColor(Color.parseColor("#888888"));
			iv_store_pay_qr.setImageResource(R.drawable.qr_blue_code);
			tv_store_pay_qr.setTextColor(Color.parseColor("#00b4dc"));
			TransUtils.createQRImage(
					"http://bjrcb.1fuwu.com.cn/ApkDownload/serve_download?&type=sale&shopsid="
							+ shopsid + "&is_goods=0", getApplicationContext(),
					iv_qrcode);

			break;

		default:
			break;
		}

	}

	/**
	 * 返回
	 * 
	 * @param view 控件
	 */
	public void back_onClick(View view) {
		finish();
	}
}
