package com.example.oto01;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.model.CurrentRevenue;
import com.example.oto01.model.Login;
import com.example.oto01.model.Revenue;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SaleService;
import com.example.oto01.utils.NetConn;

/**
 * 今日营收界面
 * 
 * @author lqq
 * 
 */
public class TodaySalesActivity extends BaseActivity {
	private TextView titleTextView, headTitleTextView, headTotalTextView;

	private static final String MONTH = "m";
	private static final String WEEK = "w";
	private static final String ALL = "a";
	private static String CURRENT_DURATION = WEEK;
	private Dialog proDialog, dialog;
	private int shopsid = 1;
	private float max = 0f;
	private float min = 0f;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			getMax((CurrentRevenue) msg.obj);
			getMin((CurrentRevenue) msg.obj);
			refeshUI((CurrentRevenue) msg.obj);
		};
	};

	/**
	 * 设置最大值
	 */
	private void setNewMax() {
		if (max / min >= 10) {
			max = min * 10;
		}
	}

	/**
	 * 获取最大值
	 * 
	 * @param currentRevenue
	 */
	private void getMax(CurrentRevenue currentRevenue) {
		if (currentRevenue != null) {
			for (int i = 0; i < currentRevenue.getOne_month_revenue().size(); i++) {
				Revenue revenue = currentRevenue.getOne_month_revenue().get(i);
				float currentValue = Float.parseFloat(revenue
						.getShops_receive_total());
				if (max < currentValue) {
					max = currentValue;
				}
			}
		}
	}

	/**
	 * 获取最小值
	 * 
	 * @param currentRevenue
	 */
	private void getMin(CurrentRevenue currentRevenue) {
		if (currentRevenue != null) {
			for (int i = 0; i < currentRevenue.getOne_month_revenue().size(); i++) {
				Revenue revenue = currentRevenue.getOne_month_revenue().get(i);
				float currentValue = Float.parseFloat(revenue
						.getShops_receive_total());
				if (min > currentValue) {
					min = currentValue;
				}
			}
		}
	}

	@SuppressLint("NewApi")
	private void refeshUI(CurrentRevenue currentRevenue) {
		if (currentRevenue != null) {
			System.out.println("------currentRevenue------>" + currentRevenue);
			System.out.println("------max------>" + max);

			headTotalTextView
					.setText((currentRevenue.getAverage_revenue() == null)
							|| (currentRevenue.getAverage_revenue() == "null"
									|| (currentRevenue.getAverage_revenue() == "") || (currentRevenue
										.getAverage_revenue().equals(""))) ? "0.00"
							: currentRevenue.getAverage_revenue());
			LinearLayout layout = (LinearLayout) findViewById(R.id.content);
			layout.removeAllViews();
			int width = getWindowManager().getDefaultDisplay().getWidth();
			int height = getWindowManager().getDefaultDisplay().getHeight();
			System.out.println("-------屏幕宽度----->" + width);
			int date_width = (int) (width * 0.3);
			System.out.println("-------日期宽度----->" + date_width);
			int total_width = (int) (width * 0.3);
			System.out.println("-------价格宽度----->" + total_width);
			int max_width = width - date_width - total_width;
			System.out.println("-------中间最大宽度----->" + max_width);
			// 从一个Context中，获得一个布局填充器，这样你就可以使用这个填充器来把xml布局文件转为View对象了。
			LayoutInflater inflater = LayoutInflater.from(this);
			layout.setOrientation(1);

			for (int i = 0; i < currentRevenue.getOne_month_revenue().size(); i++) {
				Revenue revenue = currentRevenue.getOne_month_revenue().get(i);
				int my_width = (int) ((Double.parseDouble(revenue
						.getShops_receive_total()) / max) * max_width);
				if (Double.parseDouble(revenue.getShops_receive_total()) != 0) {
					RelativeLayout commentLinearLayout = (RelativeLayout) inflater
							.inflate(R.layout.sales_item_layout, null)
							.findViewById(R.id.salesitemlinearlayout);
					if (i == 0) {
						commentLinearLayout.setBackgroundColor(getResources()
								.getColor(R.color.content_color_red));
					}

					TextView datetv = (TextView) commentLinearLayout
							.findViewById(R.id.dateTextView);
					datetv.setWidth(date_width);
					datetv.setTextSize(16);
					datetv.setText(revenue.getDays());
					// TextView centertv = (TextView)
					// commentLinearLayout.findViewById(R.id.centerTextView);
					// centertv.setWidth(my_width);
					TextView totaltv = (TextView) commentLinearLayout
							.findViewById(R.id.totalTextView);
					totaltv.setText(revenue.getShops_receive_total());
					totaltv.setTextSize(18);
					totaltv.setWidth(total_width);

					int all_width = (int) date_width + (int) my_width
							+ (int) total_width;
					LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					sp_params.width = all_width;
					if (height == 1280) {
						sp_params.height = 130;
						sp_params.bottomMargin = 20;
					} else if (height == 1920) {
						sp_params.height = 150;
						sp_params.bottomMargin = 30;
					} else if (height == 800) {
						sp_params.height = 70;
						sp_params.bottomMargin = 5;
					} else if (height == 854) {
						sp_params.height = 75;
						sp_params.bottomMargin = 7;
					} else {
						sp_params.height = 88;
						sp_params.bottomMargin = 20;
					}
					commentLinearLayout.setLayoutParams(sp_params);
					System.out.println("------all------>" + all_width);
					System.out.println("------my_width------>" + my_width);
					layout.addView(commentLinearLayout);
				}

			}
		} else {
			headTotalTextView.setText(0.00 + "");
			LinearLayout layout = (LinearLayout) findViewById(R.id.content);
			layout.removeAllViews();
		}
		proDialog.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_sales);
		initView();

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		LoginManager lm = LoginManager.getInstance(TodaySalesActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		dialog = new Dialog(TodaySalesActivity.this, R.style.theme_dialog_alert);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		LayoutInflater inflater = (LayoutInflater) TodaySalesActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/**
		 * 标题
		 */
		titleTextView = (TextView) findViewById(R.id.title_font);
		headTitleTextView = (TextView) findViewById(R.id.head_title);
		headTotalTextView = (TextView) findViewById(R.id.head_total);

		if (!NetConn.checkNetwork(TodaySalesActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			CURRENT_DURATION = MONTH;
			initData1();
		}
	}

	private void initData1() {
		if (!NetConn.checkNetwork(TodaySalesActivity.this)) {
		} else {
			showDialog();
			new Thread(new Runnable() {

				@Override
				public void run() {
					SaleService saleService = new SaleService(
							TodaySalesActivity.this);
					CurrentRevenue currentRevenue = saleService
							.getCurrentRevenue(shopsid);
					Message message = handler.obtainMessage();
					message.obj = currentRevenue;
					handler.sendMessage(message);
				}
			}).start();
		}

	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}
}
