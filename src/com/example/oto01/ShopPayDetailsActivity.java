package com.example.oto01;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.model.Data;
import com.example.oto01.model.Login;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.OrderService;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ShopPayDetailsActivity extends BaseActivity {

	@ViewInject(R.id.tv_order_num)
	private TextView tv_order_num;
	@ViewInject(R.id.tv_order_time)
	private TextView tv_order_time;
	@ViewInject(R.id.tv_total)
	private TextView tv_total;
	@ViewInject(R.id.tv_use_money)
	private TextView tv_use_money;
	@ViewInject(R.id.tv_use_money1)
	private TextView tv_use_money1;
	@ViewInject(R.id.tv_pingtai)
	private TextView tv_pingtai;
	@ViewInject(R.id.tv_shishou)
	private TextView tv_shishou;
	@ViewInject(R.id.tv_already_code)
	private TextView tv_already_code;
	@ViewInject(R.id.iv_not_code)
	private ImageView iv_not_code;
	@ViewInject(R.id.title_font)
	private TextView title_font;
	@ViewInject(R.id.rl_ok)
	private RelativeLayout rl_ok;
	@ViewInject(R.id.rl_cuowu)
	private RelativeLayout rl_cuowu;
	@ViewInject(R.id.tv_yanzhengma)
	private TextView tv_yanzhengma;
	@ViewInject(R.id.rl_scan_quan)
	private RelativeLayout rl_scan_quan;

	@ViewInject(R.id.tv5)
	private TextView tv5;
	@ViewInject(R.id.tv_shop_youhui)
	private TextView tv_shop_youhui;
	@ViewInject(R.id.tv8)
	private TextView tv8;
	@ViewInject(R.id.rl_order_money)
	private RelativeLayout rl_order_money;
	@ViewInject(R.id.rl_quan)
	private RelativeLayout rl_quan;
	@ViewInject(R.id.rl_pingtaijiangli)
	private RelativeLayout rl_pingtaijiangli;
	@ViewInject(R.id.rl_pingtaijiesuan)
	private RelativeLayout rl_pingtaijiesuan;
	@ViewInject(R.id.tv_pingtaijiesuan)
	private TextView tv_pingtaijiesuan;
	@ViewInject(R.id.rl_shifu)
	private RelativeLayout rl_shifu;
	@ViewInject(R.id.rl_shishou)
	private RelativeLayout rl_shishou;
	@ViewInject(R.id.rl_pay)
	private RelativeLayout rl_pay;

	private String code;

	private static final int SHOP_DETAILS = 1;
	private static final int SHOP_CODE = 2;
	private int shopsid = 1;
	private int id;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == SHOP_DETAILS) {
				// 得到订单详情的内容
				String res = (String) msg.obj;
				try {
					JSONObject jo = new JSONObject(res);
					if (jo.getInt("res") == 0) {
						JSONObject jsonObject = jo.getJSONObject("data");
						Data data = null;
						if (jsonObject.getString("order_type").equals("1")) {
							// 普通优惠券
							data = new Data(
									jsonObject.getString("id"),
									jsonObject.getString("ordernum"),
									jsonObject.getString("goods_total_price"),
									jsonObject
											.getString("discount_total_price"),
									jsonObject.getString("total"),
									jsonObject.getString("reward_price"),
									"",
									jsonObject.getString("authcode"),
									jsonObject.getString("is_confirm"),
									jsonObject.getString("payment"),
									jsonObject.getString("addtime"),
									jsonObject.getString("shops_receive_total"),
									jsonObject.getString("coupon_name"),
									jsonObject.getString("actual_coupon_total"),
									jsonObject.getString("order_type"), "");
						} else {
							// 礼品券
							data = new Data(jsonObject.getString("id"),
									jsonObject.getString("ordernum"), "", "",
									"", "", "",
									jsonObject.getString("authcode"),
									jsonObject.getString("is_confirm"),
									jsonObject.getString("payment"),
									jsonObject.getString("addtime"), "",
									jsonObject.getString("coupon_name"), "",
									jsonObject.getString("order_type"),
									jsonObject.getString("shops_settle_price"));

						}

						code = data.getAuthcode();
						tv_yanzhengma.setText(code + "");
						tv_order_num.setText(data.getOrdernum() + "");
						tv_order_time.setText(DateUtil.getFormatedDate_1(data
								.getAddtime()));
						if (data.getCoupon_name().equals("")) {
							rl_quan.setVisibility(View.GONE);
						} else {
							rl_quan.setVisibility(View.VISIBLE);
						}
						if (data.getOrder_type().equals("1")) {
							rl_shifu.setVisibility(View.VISIBLE);
							rl_shishou.setVisibility(View.VISIBLE);
							// 普通优惠券订单
							if (getIntent().getStringExtra("type")
									.equals("not")
									|| getIntent().getStringExtra("type")
											.equals("ok")) {
								// 从扫码页面进入的普通到店支付
								iv_not_code.setVisibility(View.GONE);
								tv_already_code.setVisibility(View.GONE);
							} else {
								if (data.getIs_confirm().equals("1")) {
									// 已确认
									iv_not_code.setVisibility(View.GONE);
									tv_already_code.setVisibility(View.VISIBLE);
								} else if (data.getIs_confirm().equals("2")) {
									// 未确认
									iv_not_code.setVisibility(View.VISIBLE);
									tv_already_code.setVisibility(View.GONE);
								}
							}
							// 订单金额
							rl_order_money.setVisibility(View.VISIBLE);
							tv_total.setText("￥" + data.getGoods_total_price());
							// 券名称
							// tv5.setText(data.getCoupon_name());
							tv5.setText((data.getCoupon_name() == "null"
									|| data.getCoupon_name().equals("null") || data
									.getCoupon_name() == null) ? "" : data
									.getCoupon_name());
							// 券价格
							tv_shop_youhui.setText("-￥"
									+ data.getActual_coupon_total());
							rl_pingtaijiangli.setVisibility(View.VISIBLE);
							rl_pingtaijiesuan.setVisibility(View.GONE);
							// 平台补贴
							tv_use_money.setText("平台补贴：￥"
									+ data.getDiscount_total_price());
							// 客户实付
							tv_use_money1.setText("客户实付：￥" + data.getTotal());
							// 平台奖励
							tv_pingtai.setText("+￥" + data.getReward_price());
							tv_shishou.setText("实收：￥"
									+ data.getShops_receive_total());

						} else {
							// 礼品券订单
							iv_not_code.setVisibility(View.GONE);
							tv_already_code.setVisibility(View.GONE);
							rl_shifu.setVisibility(View.GONE);
							rl_shishou.setVisibility(View.GONE);
							// 订单金额
							rl_order_money.setVisibility(View.GONE);
							// 券名称
							// tv5.setText(data.getCoupon_name());
							tv5.setText((data.getCoupon_name() == "null"
									|| data.getCoupon_name().equals("null") || data
									.getCoupon_name() == null) ? "" : data
									.getCoupon_name());
							rl_pingtaijiangli.setVisibility(View.GONE);
							rl_pingtaijiesuan.setVisibility(View.VISIBLE);
							tv_pingtaijiesuan.setText("￥"
									+ data.getShops_settle_price());
						}

					} else {
						ToastUtil.show(getApplicationContext(),
								jo.getString("msg"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (msg.what == SHOP_CODE) {
				String res = (String) msg.obj;
				JSONObject jo1;
				try {
					jo1 = new JSONObject(res);
					if (jo1.getInt("res") == 0) {
						iv_not_code.setVisibility(View.GONE);
						tv_already_code.setVisibility(View.VISIBLE);
					} else {
						ToastUtil.show(getApplicationContext(), msg.obj + "");
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

			}

		};
	};

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		if (getIntent().getStringExtra("type").equals("not")
				|| getIntent().getStringExtra("type").equals("ok")
				|| getIntent().getStringExtra("type").equals("scanquan")) {
			finish();
		} else {
			Intent intent = new Intent();
			ShopPayDetailsActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (getIntent().getStringExtra("type").equals("not")
					|| getIntent().getStringExtra("type").equals("ok")
					|| getIntent().getStringExtra("type").equals("scanquan")) {
				finish();
			} else {
				Intent intent = new Intent();
				ShopPayDetailsActivity.this.setResult(RESULT_OK, intent);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_pay_details);
		ViewUtils.inject(this);
		LoginManager lm = LoginManager.getInstance(ShopPayDetailsActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		id = Integer.parseInt(getIntent().getStringExtra("id"));
		if (getIntent().getStringExtra("type").equals("order_details")) {
			// 订单列表页跳转到 到店支付订单详情页面
			title_font.setText("到店支付订单详情");
			rl_ok.setVisibility(View.GONE);
			rl_cuowu.setVisibility(View.GONE);
			rl_pay.setVisibility(View.GONE);
			initData();
		} else if (getIntent().getStringExtra("type").equals("not")) {
			// 二维码或者手动输入跳转进来的页面刚验证好的页面
			title_font.setText("到店支付订单");
			rl_pay.setVisibility(View.VISIBLE);
			rl_ok.setVisibility(View.VISIBLE);
			rl_cuowu.setVisibility(View.GONE);
			initData();
		} else if (getIntent().getStringExtra("type").equals("ok")) {
			// 已经验证过的订单
			title_font.setText("到店支付订单");
			rl_pay.setVisibility(View.VISIBLE);
			rl_ok.setVisibility(View.GONE);
			rl_cuowu.setVisibility(View.VISIBLE);
			initData();
		} else if (getIntent().getStringExtra("type").equals("quan")) {
			// 礼品券订单 除了扫描页面，别的页面跳转过来的
			rl_pay.setVisibility(View.GONE);
			title_font.setText("到店支付订单详情");
			tv8.setText("礼品券码");
			initData();
		} else if (getIntent().getStringExtra("type").equals("scanquan")) {
			// 从扫描页面跳转到过来的
			rl_pay.setVisibility(View.VISIBLE);
			title_font.setText("到店支付订单");
			tv8.setText("礼品券码");
			rl_scan_quan.setVisibility(View.VISIBLE);
			initData();
		}

		// 验证
		iv_not_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				yanzheng();
			}
		});

	}

	protected void yanzheng() {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ShopPayDetailsActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否设为已验证？");
		dialog.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!NetConn.checkNetwork(ShopPayDetailsActivity.this)) {
						} else {
							new CodeAsyc().execute();
						}
					}
				});
		dialog.setPositiveButton("取消", null);
		dialog.show();

	}

	private class CodeAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(
					ShopPayDetailsActivity.this);
			Message message = handler.obtainMessage(SHOP_CODE);
			String res = orderService.getShopCode(shopsid, code);
			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

	// private void initCode() {
	// Data data = (Data) getIntent().getSerializableExtra("data");
	// tv_yanzhengma.setText(data.getAuthcode() + "");
	// tv_order_num.setText(data.getOrdernum() + "");
	// tv_order_time.setText(DateUtil.getFormatedDate_1(data.getAddtime()));
	// tv_total.setText(data.getGoods_total_price() + "元");
	// tv_use_money.setText("-" + data.getDiscount_total_price() + "元");
	// tv_use_money1.setText(data.getTotal() + "元");
	// tv_pingtai.setText("平台奖励：+" + data.getReward_price() + "元");
	// tv_shishou.setText("实收：" + data.getReceive_total_price() + "元");
	// // if (data.getShops_receive_total()().equals("0")) {
	// // // 没有店铺优惠
	// // tv5.setVisibility(View.GONE);
	// // tv_shop_youhui.setVisibility(View.GONE);
	// // } else {
	// tv5.setVisibility(View.VISIBLE);
	// tv_shop_youhui.setVisibility(View.VISIBLE);
	// tv_shop_youhui.setText("-" + data.getShops_receive_total() + "元");
	// // }
	// }

	private void initData() {
		if (!NetConn.checkNetwork(ShopPayDetailsActivity.this)) {
		} else {
			new MyAsyc().execute();
		}
	}

	private class MyAsyc extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			OrderService orderService = new OrderService(
					ShopPayDetailsActivity.this);
			Message message = handler.obtainMessage(SHOP_DETAILS);
			String res = orderService.getShopDetails(shopsid, id);

			message.obj = res;
			handler.sendMessage(message);
			return null;
		}
	}

}
