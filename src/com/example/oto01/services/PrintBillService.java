package com.example.oto01.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.device.PrinterManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.oto01.model.Login;
import com.example.oto01.model.Order;
import com.example.oto01.model.OrderDetail;
import com.example.oto01.utils.NetConn;

public class PrintBillService extends IntentService {
	private static final int REFRESH_UI = 0;
	private Order order;
	private PrinterManager printer;
	private String orderId;

	public PrintBillService() {
		super("bill");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		printer = new PrinterManager();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		orderId = intent.getStringExtra("order_id");
		if (orderId == null || orderId.equals(""))
			return;
		getDetail();
		// int ret =printer.prn_drawTextEx(context, 5, 0,300,-1, "arial", 20, 0,
		// 0x0001, 0);
		// System.out.println("@@@@@@---4"+ret);
		// android.util.Log.i("debug", "ret:" + ret);
		// ret += printer.prn_drawTextEx(context, 5, ret,300,-1, "arial", 25, 0,
		// 0x0001, 1);
		// ret += printer.prn_drawTextEx(context, 5, ret,-1,-1, "arial", 25, 0,
		// 0x0008, 0);
		// ret +=printer.prn_drawTextEx(context, 300, ret,-1,-1, "arial", 25, 1,
		// 0, 0);
		// ret +=printer.prn_drawTextEx(context, 0, ret,-1,-1,
		// "/system/fonts/DroidSans-Bold.ttf", 25, 0, 0, 0);
		// ret +=printer.prn_drawTextEx(context, 0, ret,-1,-1,
		// "/system/fonts/kaishu.ttf", 25, 0, 0x0001, 0);
		// android.util.Log.i("debug", "ret:" + ret);
		// printer.prn_drawTextEx(context, 5, 60,160,-1, "arial", 25, 0, 0x0001
		// |0x0008, 0);
		// printer.prn_drawTextEx(context, 180, 0,160,-1, "arial", 25, 1,
		// 0x0008, 0);
		// printer.prn_drawTextEx(context, 300, 30,160,-1, "arial", 25, 2,
		// 0x0008, 0);
		// printer.prn_drawTextEx(context, 300, 160,160,-1, "arial", 25, 3,
		// 0x0008, 0);
		// printer.prn_drawTextEx(context, 0, 0,160,-1, "arial", 25, 1, 0x0008,
		// 0);
		// printer.prn_drawTextEx(context, 160, 30,200,-1, "arial", 28, 0, 2,1);
		// printer.prn_drawTextEx(context, 0, 180,-1,-1, "arial", 28, 0, 2,1);

	}

	private int shopsid = -1;

	private void getDetail() {
		LoginManager lm = LoginManager.getInstance(getApplicationContext());
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			shopsid = login.getAdminId();
		}
		if (!NetConn.checkNetwork(PrintBillService.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					getOrderDetail();
				}
			}).start();
		}
	}

	/**
	 * 获取订单详情
	 */
	private void getOrderDetail() {
		Log.d("TAG", "getOrderDetail()");
		OrderService orderService = new OrderService(this);
		Message msg = handler.obtainMessage(REFRESH_UI);
		order = orderService.getOrderDetail(Integer.parseInt(orderId), 0,
				shopsid);
		msg.obj = order;
		msg.sendToTarget();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_UI:
				refreshUI((Order) msg.obj);
				break;
			}
		}
	};

	private void refreshUI(Order order) {
		if (order != null) {
			int p = printer.prn_open();
			int p1 = printer.prn_getStatus();
			int p2 = printer.prn_setupPage(384, -1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(new Date(Integer.parseInt(order
					.getAddtime()) * 1000L));
			System.out.println(date);
			List<OrderDetail> od = order.getOrderDetails();

			String str = "                    社区e服务订单\n订单号:  "
					+ order.getOrdernum() + "\n出单时间:  " + date + "\n收货人:  "
					+ order.getLinkman() + "\n联系电话:  " + order.getPhone()
					+ "\n收货地址:  " + order.getAddress() + "\n买家留言:  "
					+ order.getContent();

			printer.prn_drawTextEx(str, 5, 0, 400, -1, "arial", 20, 0, 0x0001,
					0);

			printer.prn_drawTextEx("商品信息\n序号　　商品　　　　　　数量　　   金额", 5, 200, 400,
					-1, "arial", 20, 0, 0x0001, 0);

			for (int i = 0; i < od.size(); i++) {
				OrderDetail o = od.get(i);
				printer.prn_drawTextEx(i + "", 5, 250 + i * 23, 400, -1,
						"arial", 20, 0, 0x0001, 0);
				printer.prn_drawTextEx(o.getName(), 80, 250 + i * 23, 400, -1,
						"arial", 20, 0, 0x0001, 0);
				printer.prn_drawTextEx(o.getNum() + "", 250, 250 + i * 23, 400,
						-1, "arial", 20, 0, 0x0001, 0);
				printer.prn_drawTextEx(o.getShowPrice() + "", 340,
						250 + i * 23, 400, -1, "arial", 20, 0, 0x0001, 0);
				if (od.size() - 1 == i) {
					printer.prn_drawTextEx("合计:" + order.getTotal(), 250,
							250 + (i + 1) * 23, 400, -1, "arial", 20, 0,
							0x0001, 0);
					printer.prn_drawTextEx(
							"\nwww.efuwu.me\n随时随地享受轻松便捷的周边生活服务\n\n\n\n\n\n", 5,
							250 + (i + 1) * 23, 400, -1, "arial", 20, 0,
							0x0001, 0);
				}
			}
			// printer.prn_drawLine(0, 0, 350, 0, 10);
			// 左
			// printer.prn_drawLine(0, 0, 0, 250, 5);
			// //右
			// printer.prn_drawLine(350, 0, 350, 250, 3);
			// //下
			// printer.prn_drawLine(0, 250, 350, 250, 10);

			printer.prn_printPage(0);
			printer.prn_close();

		}
	}
}