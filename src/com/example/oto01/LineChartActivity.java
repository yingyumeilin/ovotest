package com.example.oto01;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.oto01.model.Login;
import com.example.oto01.model.WeekRevenue;
import com.example.oto01.model.WeekRevenueList;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.SaleService;
import com.example.oto01.utils.CommonUtil;
import com.example.oto01.utils.DensityUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.views.LineChart;

/**
 * 
 * @类描述:财务营收
* @项目名称: 社区e商户
* @类名称: LineChartActivity 
* @包名称: com.example.oto01
* @author: cym 
* @date: 2017-2-14上午10:19:29
 */
public class LineChartActivity extends BaseActivity implements OnClickListener {

	private TextView title_font;
	private Dialog proDialog;
	private int shopsid = 1;
	private TextView tv_money_today;
	private TextView tv_money_month;
	private TextView tv_all_money;
	private TextView tv_pingtai_money;

	private String x[] = { "0", "0", "0", "0", "0", "0", "0" };
	String y[] = { "0", "1", "1", "1", "1" };
	float point[] = { 0, 0, 0, 0, 0, 0, 0 };
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {

				WeekRevenue revenue = (WeekRevenue) msg.obj;
				// // res.
				// WeekRevenue revenue = JsonUtils
				// .fromJson(res, WeekRevenue.class);
				getMax(revenue);
				getMin(revenue);
				refeshUI(revenue);

			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};
	private LineChart lineChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_chart);
		title_font = (TextView) findViewById(R.id.title_font);
		title_font.setText("财务营收");
		tv_money_today = (TextView) findViewById(R.id.tv_money_today);
		tv_money_month = (TextView) findViewById(R.id.tv_money_month);
		tv_all_money = (TextView) findViewById(R.id.tv_all_money);
		tv_pingtai_money = (TextView) findViewById(R.id.tv_pingtai_money);
		lineChart = (LineChart) findViewById(R.id.lineChart);
		lineChart.setxLength(DensityUtil.dip2px(getApplicationContext(), 285));
		lineChart.setyLength(DensityUtil.dip2px(getApplicationContext(), 160));
		lineChart.setStartPoint(new Point(DensityUtil.dip2px(
				getApplicationContext(), 60), DensityUtil.dip2px(
				getApplicationContext(), 70)));
		lineChart.setBarColor(Color.argb(90, 204, 232, 240));
		lineChart.setTextColor(Color.GRAY);
		lineChart.setTitleSize(getApplicationContext().getResources()
				.getDimension(R.dimen.size_16));
		lineChart.setTextSize(getApplicationContext().getResources()
				.getDimension(R.dimen.size_12));
		lineChart.setTitle("近一周营收(元)");
		lineChart.setyArr(y);
		lineChart.setxArr(x);
		lineChart.setDataArr(point);// 数据数组长度必须小于等于x轴数组长度，否则数组越界报错
		lineChart.setOnClickListener(this);

		LoginManager lm = LoginManager.getInstance(LineChartActivity.this);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LineChart.i = 0;
		data();
	}

	public static String GetStringFromLong(long millis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date dt = new Date(millis);
		return sdf.format(dt);
	}

	protected void refeshUI(WeekRevenue obj) {
		tv_money_today.setText(obj.today_revenue);
		if (tv_money_today.getText().length() >= 9) {
			tv_money_today.setTextSize(60f);
		}
		tv_money_month.setText(obj.now_month_revenue + "");
		tv_all_money.setText(obj.total_revenue);
		tv_pingtai_money.setText(obj.total_reward_price);
		// String x[] = new String[obj.getOne_week_revenue().size()];

		float point[] = new float[obj.getOne_week_revenue().size()];
		for (int i = 0; i < obj.getOne_week_revenue().size(); i++) {
			x[i] = obj.getOne_week_revenue().get(i).getDate();
			// float pointf = Float.parseFloat(obj.getOne_week_revenue().get(i)
			// .getTotal());
			double pointf = obj.getOne_week_revenue().get(i).getTotal()
					- Math.floor(min);
			point[i] = (float) pointf;
		}

		max = (long) Math.ceil(max);
		min = (long) Math.floor(min);
		double all = (double) (max - min);

		if (all == 0.0) {
		} else {
			double middle = all / 4;

			if (middle <= 1) {
				y[0] = CommonUtil.calcMoneyPoint(2, min + "");
				y[1] = CommonUtil.calcMoneyPoint(2, min + middle + "");
				y[2] = CommonUtil.calcMoneyPoint(2, min + middle + middle + "");
				y[3] = CommonUtil.calcMoneyPoint(2, min + middle + middle
						+ middle + "");
				y[4] = CommonUtil.calcMoneyPoint(2, max + "");
			} else {
				int middleint = (int) Math.ceil(middle);
				y[0] = (int) Math.ceil(min) + "";
				y[1] = (int) Math.ceil(min + middleint) + "";
				y[2] = (int) Math.ceil(min + middleint + middleint) + "";
				y[3] = (int) Math.ceil(min + middleint + middleint + middleint)
						+ "";
				y[4] = (int) max + "";
			}

		}

		lineChart.setyArr(y);
		lineChart.setxArr(x);
		lineChart.setDataArr(point);// 数据数组长度必须小于等于x轴数组长度，否则数组越界报错
		lineChart.postInvalidate();
		proDialog.dismiss();

	}

	/**
	 * 显示Dialog
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	private void data() {

		if (!NetConn.checkNetwork(LineChartActivity.this)) {
		} else {
			showDialog();
			new Thread(new Runnable() {

				@Override
				public void run() {
					SaleService saleService = new SaleService(
							LineChartActivity.this);
					WeekRevenue revenue = saleService.getWeekRevenue(shopsid);
					// String res = saleService.getWeekRevenue(shopsid);
					Message message = handler.obtainMessage();
					message.obj = revenue;
					handler.sendMessage(message);
				}
			}).start();
		}

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lineChart:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), TodaySalesActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	private double max = 0;
	private double min = 0;
	public static double maxdouble = 0;

	/**
	 * 获取最大值
	 * 
	 * @param currentRevenue
	 */
	private void getMax(WeekRevenue currentRevenue) {
		if (currentRevenue != null) {
			for (int i = 0; i < currentRevenue.getOne_week_revenue().size(); i++) {
				WeekRevenueList revenue = currentRevenue.getOne_week_revenue()
						.get(i);
				maxdouble = currentRevenue.getOne_week_revenue()
						.get(currentRevenue.getOne_week_revenue().size() - 1)
						.getTotal();
				// double currentValue = Double.parseDouble(revenue.getTotal());
				if (max < revenue.getTotal()) {
					max = revenue.getTotal();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		maxdouble = 0;
	}

	/**
	 * 获取最小值
	 * 
	 * @param currentRevenue
	 */
	private void getMin(WeekRevenue currentRevenue) {
		if (currentRevenue != null) {
			min = currentRevenue.getOne_week_revenue().get(0).getTotal();
			for (int i = 0; i < currentRevenue.getOne_week_revenue().size(); i++) {
				WeekRevenueList revenue = currentRevenue.getOne_week_revenue()
						.get(i);

				double currentValue = revenue.getTotal();
				if (min > currentValue) {
					min = currentValue;
				}
			}
		}

	}

}
