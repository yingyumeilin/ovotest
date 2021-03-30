package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.oto01.model.Houser;
import com.example.oto01.model.Messages;
import com.example.oto01.model.Order;
import com.example.oto01.model.OrderDetail;
import com.example.oto01.model.OrderTrackDetail;
import com.example.oto01.model.Revoke;
import com.example.oto01.services.LoginManager;
import com.example.oto01.services.MessageService;
import com.example.oto01.services.OrderService;
import com.example.oto01.utils.CallPhoneUtil;
import com.example.oto01.utils.CommonUtil;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.utils.Utility;
import com.example.oto01.views.ConfirmOrderPopupWindow;
import com.example.oto01.views.RevokedListViewAdapter;

/**
 * 订单详情界面
 * 
 * @author lqq
 * 
 */
public class OrderDetailsActivity extends BaseActivity {

	private static final int REFRESH_UI = 0;
	private static final int ORDER_CONFIRM = 1;
	private static final int REFRESH_TRACK = 2;
	private static final int JIE_DAN = 10;
	private static final int ORDER_CANCEL = 3;
	private static final int ORDER_CANCEL_REASON = 4;
	private static final int CHOOSE_GUANJIA = 5;
	private static final int CHECK_GUANJIA = 6;
	private static final int GET_GUANJIA_STATUS = 7;
	private static final int REVOKE_DIS = 8;
	private int orderid;
	private int uid;
	private int shopsId = 0;
	/**
	 * status 待接单 2 ,已发货 3,已关闭-1,待发货 5
	 */
	private int status = 0;
	private int paystate = 1;// 待付款
	private int time = 0;
	private int mId = 0;
	private TextView orderTotalPrice;
	private TextView distributionCostsPrice;// 配送费
	// private TextView couponPrice;// 代金券
	private TextView orderNum;
	private TextView orderPhone;
	private TextView orderAddress;
	private TextView orderUserName;
	/*
	 * private TextView userName; private TextView userPhone;
	 */
	private TextView userContent;
	private TextView houserName, houserPhone, goodsnum, disnum;
	// private Button checkButton;
	private Button productSave, productCancle;
	private Dialog proDialog, newDialog;
	private ListView listView, myListView;
	private ListView listView2;
	private OutlinesAdapter orderListAdapter;
	private TracksAdapter trackListAdapter;
	private List<OrderDetail> details = new ArrayList<OrderDetail>();
	private List<OrderTrackDetail> tracks = new ArrayList<OrderTrackDetail>();
	private Order order;
	private LayoutInflater inflater;
	private int revoke_type = 1;// 撤销订单的类型id
	private String revoke_reason;// 撤销订单的自定义原因，当且仅当id为-1时有用处
	private List<Revoke> revokes;
	private int guanjiaStatus;
	private RelativeLayout peisongRelativeLayout;
	private LinearLayout peisongLayout;
	private int again = -1;
	private ConfirmOrderPopupWindow menuWindow;
	private boolean isApplyHouser = false;// 有否申请管家
	private boolean isHaveHouser = false;// 有否用管家
	private boolean isSendGoods = false;
	private static final int MESSAGE_DETAILS = 6;
	private String res;
	private String message;
	private TextView tv_order_money;
	private TextView tv_pingtai_butie;
	private TextView tv_custom_pay;
	private TextView tv_pingtai_jiangli;
	private TextView tv_youhui;
	private TextView tv_shop_youhui;
	/*
	 * private boolean checkBoxStatus; private CheckBox checkBox;//是否选择管家配送
	 */

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case MESSAGE_DETAILS:

				break;
			case REFRESH_UI:
				if (proDialog.isShowing())
					proDialog.dismiss();
				refreshUI((Order) msg.obj);
				break;
			case JIE_DAN:
				orderConfirm(msg);
				break;
			case ORDER_CONFIRM:
				// initDatas();
				orderConfirm(msg);
				break;
			case ORDER_CANCEL:
				refreshBtn(-1, msg.obj + "");
				if (proDialog.isShowing())
					proDialog.dismiss();
				break;
			case REFRESH_TRACK:
				if (proDialog.isShowing())
					proDialog.dismiss();
				refreshTrackList((List<OrderTrackDetail>) msg.obj);
				break;
			case ORDER_CANCEL_REASON:
				if (proDialog.isShowing())
					proDialog.dismiss();
				revokes = (List<Revoke>) msg.obj;
				// showInfo((List<Revoke>)msg.obj);
				Dialog_RevokedOrder(revokes);
				break;
			case CHOOSE_GUANJIA:
				Houser houser = (Houser) msg.obj;
				if (houser != null) {
					if (houser.getErrormsg() == null) {
						menuWindow.getContentView()
								.findViewById(R.id.find_steward_flag_layout)
								.setVisibility(View.VISIBLE);
						System.out.println("---------CHOOSE_GUANJIA--------->"
								+ houser);
						houserName.setText(houser.getName());
						houserPhone.setText(houser.getPhone() + "");
						goodsnum.setText(houser.getGoodsnum() + "");
						disnum.setText(houser.getDisnum() + "");
						// checkButton.setText("撤 销");
						// peisongLayout.setVisibility(View.VISIBLE);
						// guanjiaStatus = 2;
						isHaveHouser = true;
						TextView flag = (TextView) menuWindow.getContentView()
								.findViewById(R.id.find_steward_flag_textview);
						flag.setText("找管家成功!");
						flag.setTextColor(getResources()
								.getColor(R.color.green));
						TextView tv = (TextView) menuWindow.getContentView()
								.findViewById(
										R.id.find_steward_flag_hint_textview);
						tv.setText("已分配给" + houser.getName() + ",电话:"
								+ houser.getPhone() + "\n货号:"
								+ houser.getGoodsnum() + ",配单号:"
								+ houser.getDisnum() + "");
						RadioButton rb = (RadioButton) menuWindow
								.getContentView().findViewById(
										R.id.their_distribution_radiobutton);
						rb.setChecked(false);
						rb.setClickable(false);
						RadioButton rb1 = (RadioButton) menuWindow
								.getContentView().findViewById(
										R.id.steward_distribution_radiobutton);
						rb1.setChecked(true);
					} else {
						TextView flag = (TextView) menuWindow.getContentView()
								.findViewById(R.id.find_steward_flag_textview);
						flag.setText("找管家失败!");
						flag.setTextColor(getResources().getColor(R.color.red));
						TextView tv = (TextView) menuWindow.getContentView()
								.findViewById(
										R.id.find_steward_flag_hint_textview);
						tv.setText("很抱歉!此订单社区e管家暂时不能服务");
						menuWindow
								.getContentView()
								.findViewById(
										R.id.steward_failure_reason_layout)
								.setVisibility(View.VISIBLE);
						TextView tv1 = (TextView) menuWindow.getContentView()
								.findViewById(
										R.id.find_steward_fail_hint_textview);
						tv1.setText(houser.getErrormsg());
						RadioButton rb = (RadioButton) menuWindow
								.getContentView().findViewById(
										R.id.their_distribution_radiobutton);
						rb.setChecked(true);
						RadioButton rb1 = (RadioButton) menuWindow
								.getContentView().findViewById(
										R.id.steward_distribution_radiobutton);
						rb1.setChecked(false);
						// ToastUtil.show(OrderDetailsActivity.this,
						// houser.getErrormsg());
					}
				}

				break;
			case REVOKE_DIS:
				if (msg.arg1 == 0) {
					guanjiaStatus = 3; // 已撤销
					peisongRelativeLayout.setVisibility(View.GONE);
				} else {
					// 撤单失败

				}
				break;
			case GET_GUANJIA_STATUS:// 获取当前管家的状态
				// FIXME
				if (msg.arg1 == 0) {
					isApplyHouser = true;
					System.out.println("-------dis-------->"
							+ order.getIs_dis());
					System.out.println("-------status-------->" + status);
					if (order.getIs_dis() == 1
							&& ((status == 1) || (status == 4 && paystate == 2))) {// 待找管家
						// checkButton.setText("找管家");
						guanjiaStatus = 1;
						// peisongLayout.setVisibility(View.GONE);
						peisongRelativeLayout.setVisibility(View.GONE);
					} else if (order.getIs_dis() == 3
							&& ((status == 1) || (status == 4 && paystate == 2))
							&& guanjiaStatus != 3) {// 已找管家
						isHaveHouser = true;
						userContent.setText((order.getContent() == null
								|| order.getContent() == "null" || order
								.getContent().equals("null")) ? "" : ""
								+ order.getContent());
						houserName.setText((order.getHname() == null)
								|| (order.getHname() == "null") ? "" : order
								.getHname());
						houserPhone.setText((order.getHphone() == null)
								|| (order.getHphone() == "null") ? "" : order
								.getHphone());
						goodsnum.setText((order.getGoodsnum() == null)
								|| (order.getGoodsnum() == "null")
								|| order.getGoodsnum().equals("null") ? ""
								: order.getGoodsnum());
						disnum.setText((order.getDisnum() == null)
								|| (order.getDisnum() == "null")
								|| order.getDisnum().equals("null") ? ""
								: order.getDisnum());
						peisongLayout.setVisibility(View.VISIBLE);
						// checkButton.setVisibility(View.VISIBLE);
						// checkButton.setText("撤销");
						// guanjiaStatus = 2;
						peisongRelativeLayout.setVisibility(View.VISIBLE);
					} else if (order.getIs_dis() == 3
							&& (status == 2 || status == 3)
							&& guanjiaStatus != 3) {
						userContent.setText((order.getContent() == null
								|| order.getContent() == "null" || order
								.getContent().equals("null")) ? "" : ""
								+ order.getContent());
						houserName.setText((order.getHname() == null)
								|| (order.getHname() == "null") ? "" : order
								.getHname());
						houserPhone.setText((order.getHphone() == null)
								|| (order.getHphone() == "null") ? "" : order
								.getHphone());
						goodsnum.setText((order.getGoodsnum() == null)
								|| (order.getGoodsnum() == "null")
								|| order.getGoodsnum().equals("null") ? ""
								: order.getGoodsnum());
						disnum.setText((order.getDisnum() == null)
								|| (order.getDisnum() == "null")
								|| order.getDisnum().equals("null") ? ""
								: order.getDisnum());
						peisongLayout.setVisibility(View.VISIBLE);
						// checkButton.setVisibility(View.GONE);
						peisongRelativeLayout.setVisibility(View.VISIBLE);
					} else if (order.getIs_dis() == 2) {
						peisongRelativeLayout.setVisibility(View.GONE);
					} else if (order.getIs_dis() == 4) {
						peisongRelativeLayout.setVisibility(View.GONE);
					} else if (status == -1) {
						peisongRelativeLayout.setVisibility(View.GONE);
					} else if (guanjiaStatus == 1) {
						if (isHaveHouser) {
							peisongRelativeLayout.setVisibility(View.VISIBLE);
							peisongLayout.setVisibility(View.VISIBLE);
						} else {
							peisongRelativeLayout.setVisibility(View.GONE);
							peisongLayout.setVisibility(View.GONE);
						}
					} else if (guanjiaStatus == 2) {
						peisongLayout.setVisibility(View.VISIBLE);
						// checkButton.setVisibility(View.GONE);
						peisongRelativeLayout.setVisibility(View.VISIBLE);
					} else if (guanjiaStatus == 3 || order.getIs_dis() == 4) {
						peisongRelativeLayout.setVisibility(View.GONE);
					}
				} else {
					peisongRelativeLayout.setVisibility(View.GONE);
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		initViews();
		initDatas();
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		Intent intent = new Intent();
		OrderDetailsActivity.this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			OrderDetailsActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 刷新UI
	 * 
	 * @param order
	 */
	private void refreshUI(Order order) {
		if (order != null) {

			refreshList(order.getOrderDetails());
			refreshPrice();

			refreshStartBtn(order.getState());
			Log.d("TAG", "detail : " + order.getStatus());
			Log.d("TAG", "detail getLinkman : " + order.getLinkman());
			orderNum.setText(order.getOrdernum());
			// orderDetailDate
			// .setText(DateUtil.getFormatedDate(order.getAddtime()));
			orderPhone.setGravity(Gravity.RIGHT);
			orderPhone.setText((order.getPhone() == null
					|| order.getPhone() == "null" || order.getPhone().equals(
					"null")) ? "" : order.getPhone());
			orderAddress.setText((order.getAddress() == null
					|| order.getAddress() == "null" || order.getAddress()
					.equals("null")) ? "" : order.getAddress());
			orderUserName.setText((order.getLinkman() == null
					|| order.getLinkman() == "null" || order.getLinkman()
					.equals("null")) ? "" : order.getLinkman());
			userContent.setText((order.getContent() == null
					|| order.getContent() == "null" || order.getContent()
					.equals("null")) ? "" : order.getContent());
			tv_ordertime
					.setText(DateUtil.getFormatedDate_1(order.getAddtime()));
			// userName.setText((order.getUsername()==null||order.getUsername()=="null"||order.getUsername().equals("null"))?"姓名：":"姓名："+order.getUsername());
			// userPhone.setText((order.getUserphone()==null||order.getUserphone()=="null"||order.getUserphone().equals("null"))?"电话：":"电话："+order.getUserphone());

		}
		if (proDialog.isShowing())
			proDialog.dismiss();
	}

	/**
	 * 刷新订单商品详情列表
	 * 
	 * @param ds
	 */
	private void refreshList(List<OrderDetail> ds) {
		if (ds != null && ds.size() > 0) {
			details.clear();
			details.addAll(ds);
			orderListAdapter.notifyDataSetChanged();
			// ListViewUtil.setListViewHeightBaseOnChildren(listView);
			Utility.setListViewHeightBasedOnChildren(listView);
		}
	}

	/**
	 * 刷新订单跟踪列表
	 * 
	 * @param ds
	 */
	private void refreshTrackList(List<OrderTrackDetail> ds) {
		if (ds != null && ds.size() > 0) {
			tracks.clear();
			tracks.addAll(ds);
			trackListAdapter.notifyDataSetChanged();
			Utility.setListViewHeightBasedOnChildren(listView2);
			// ListViewUtil.setListViewHeightBaseOnChildren(listView2);
		}
		if (proDialog.isShowing())
			proDialog.dismiss();
	}

	/**
	 * 刷新价格
	 */
	private void refreshPrice() {
		// 配送费 是0的时候 不显示
		// distribution_costs_price_textview，distribution_costs_price
		if (order.getFreight_total_price().equals("0")) {
			// 配送费为0的情况
			distributionCostsPrice.setVisibility(View.GONE);
			distribution_costs_price_textview.setVisibility(View.GONE);
			line17.setVisibility(View.GONE);
		} else {
			distribution_costs_price_textview.setVisibility(View.VISIBLE);
			distributionCostsPrice.setVisibility(View.VISIBLE);
			distributionCostsPrice.setText("+￥"
					+ CommonUtil.calcMoneyPoint(2,
							order.getFreight_total_price()));
			line17.setVisibility(View.VISIBLE);
		}
		if (order.getOrder_type().equals("1")) {
			// 普通优惠券

			if (order.getActual_coupon_total().equals("0")) {
				// 没有使用优惠券的情况
				// 券名称
				tv_youhui.setVisibility(View.GONE);
				// 券的价格
				tv_shop_youhui.setVisibility(View.GONE);
			} else {
				// 有优惠券的情况
				tv_youhui.setVisibility(View.VISIBLE);
				tv_youhui.setSelected(true);
				tv_youhui.setText((order.getCoupon_name() == "null"
						|| order.getCoupon_name().equals("null") || order
						.getCoupon_name() == null) ? "" : order
						.getCoupon_name());
				// 券的价格
				tv_shop_youhui.setVisibility(View.VISIBLE);
				tv_shop_youhui.setText("-￥"
						+ CommonUtil.calcMoneyPoint(2,
								order.getActual_coupon_total()));
				line17.setVisibility(View.VISIBLE);
			}

			// 平台奖励
			tv_jiangliname.setText("平台奖励");
			tv_pingtai_jiangli.setText("+￥"
					+ CommonUtil.calcMoneyPoint(2, order.getReward_price()));

			// 平台补贴
			tv_pingtai_butie.setText("平台补贴：￥"
					+ CommonUtil.calcMoneyPoint(2,
							order.getDiscount_total_price()));
			// 客户实付
			tv_custom_id.setVisibility(View.VISIBLE);
			// tv_custom_pay.setText("￥" + order.getTotal());
			tv_custom_pay.setText("￥"
					+ CommonUtil.calcMoneyPoint(2, order.getTotal() + ""));
			// 实收：
			orderTotalPrice.setText("￥"
					+ CommonUtil.calcMoneyPoint(2,
							order.getShops_receive_total()) + "");
		} else if (order.getOrder_type().equals("2")) {
			// // 礼品券
			// tv_liquan.setVisibility(View.VISIBLE);
			// // 客户自付名字
			// tv1.setVisibility(View.VISIBLE);
			// // 客户自付后面的价格
			// tv_order_money.setVisibility(View.VISIBLE);
			// // 礼品券下面的线
			// line2222.setVisibility(View.VISIBLE);
			// line20.setVisibility(View.VISIBLE);
			// // 券名称
			// tv_youhui.setVisibility(View.GONE);
			// // 券的价格
			// tv_shop_youhui.setVisibility(View.GONE);
			// // 平台补贴
			// tv_pingtai_butie.setVisibility(View.GONE);
			// // 客户实付
			// tv_custom_pay.setVisibility(View.GONE);

			tv_liquan.setText(order.getCoupon_name());
			// 客户自付的价格
			tv_order_money.setText("￥"
					+ CommonUtil.calcMoneyPoint(2, order.getTotal()) + "");

			tv_jiangliname.setText("平台结算金额");
			// 平台结算金额
			tv_pingtai_jiangli.setText("￥"
					+ CommonUtil.calcMoneyPoint(2,
							order.getTotal_reward_price()));
			// 实收：
			orderTotalPrice.setText("￥"
					+ CommonUtil.calcMoneyPoint(2,
							order.getShops_receive_total()) + "");
			tv_custom_id.setVisibility(View.GONE);
		}
		double price = 0;
		for (OrderDetail gd : details) {
			System.out.println("----single price-->" + gd.getPrice());
			price += gd.getPrice() * gd.getNum();
			System.out.println("----single price-->" + price);
		}

	}

	/**
	 * 刷新按钮
	 * 
	 * @param status
	 */
	private void refreshBtn(int status, String message) {
		if (status == 2) {
			ToastUtil.show(OrderDetailsActivity.this, message);
			// this.status = -1;
		} else if (status == 3) {
			ToastUtil.show(OrderDetailsActivity.this, "您已确认订单！");
		} else if (status == 4) {
			ToastUtil.show(OrderDetailsActivity.this, "订单已完成！");

		} else if (status == 5) {
			ToastUtil.show(OrderDetailsActivity.this, "此订单还没付款！");
		} else if (status == -2) {
			ToastUtil.show(OrderDetailsActivity.this, "此订单操作出错！");
		} else if (status == -1) {
			ToastUtil.show(OrderDetailsActivity.this, message);
		}
		System.out
				.println("------OrderDetailsActivity11111111111111111111111111111111111111------->"
						+ this.status);
		switch (this.status) {
		case 1:
			productSave.setVisibility(View.VISIBLE);
			productSave.setEnabled(true);
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			productSave.setText("发货");
			if (isSendGoods) {
				productSave.setVisibility(View.GONE);
				productSave.setEnabled(false);
			}
			break;
		case 4:
			if (paystate == 1 && order.getPayment() == 2) {
				// 待付款状态
				productSave.setVisibility(View.GONE);
				productSave.setEnabled(false);
			} else {
				productSave.setVisibility(View.VISIBLE);
				productSave.setEnabled(true);
				productSave.setText("发货");

			}
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			break;

		case 5:
			System.out
					.println("------OrderDetailsActivity222222222222222222222222222222222222------->"
							+ this.status);
			// productSave.setText("已完成");
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			productSave.setVisibility(View.GONE);
			productSave.setEnabled(false);
		case 2:

			System.out
					.println("------OrderDetailsActivity222222222222222222222222222222222222------->"
							+ this.status);
			// productSave.setText("已完成");
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			productSave.setVisibility(View.GONE);
			productSave.setEnabled(false);
			break;
		case 3:
			productCancle.setVisibility(View.GONE);
			productCancle.setEnabled(false);
			productSave.setVisibility(View.GONE);
			productSave.setEnabled(false);
			break;
		case -1:
			productCancle.setVisibility(View.GONE);
			productCancle.setEnabled(false);
			productSave.setEnabled(false);
			productSave.setVisibility(View.GONE);
			ToastUtil.show(getApplicationContext(), message);
			// if (isSendGoods) {
			// } else {
			// productCancle.setVisibility(View.VISIBLE);
			// productCancle.setEnabled(true);
			// productSave.setEnabled(true);
			// productSave.setVisibility(View.VISIBLE);
			//
			// }
			break;
		case 0:
			productSave.setVisibility(View.VISIBLE);
			productSave.setEnabled(true);
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			productSave.setText("发货");
			break;
		default:
			productCancle.setVisibility(View.GONE);
			productCancle.setEnabled(false);
			productSave.setVisibility(View.GONE);
			productSave.setEnabled(false);
		}
		getPeiSongDetail();
	}

	/**
	 * 刷新开始按钮
	 * 
	 * @param status
	 */
	private void refreshStartBtn(int status) {

		switch (status) {

		case 1:
			// 待接单
			productSave.setVisibility(View.VISIBLE);
			productSave.setEnabled(true);
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			productSave.setText("接单");
			break;
		case 4:
			// 待接单
			if (paystate == 1 && order.getPayment() == 2) {
				// 待付款状态
				productSave.setVisibility(View.GONE);
				productSave.setEnabled(false);
				productCancle.setVisibility(View.VISIBLE);
				productCancle.setEnabled(true);
			} else {
				// 货到付款
				productSave.setVisibility(View.VISIBLE);
				productSave.setEnabled(true);
				productSave.setText("接单");
				productCancle.setVisibility(View.VISIBLE);
				productCancle.setEnabled(true);
			}
			break;
		case 2:
			// 已发货
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			productSave.setVisibility(View.GONE);
			productSave.setEnabled(false);
			break;
		case 3:
			// 已完成
			productCancle.setVisibility(View.GONE);
			productCancle.setEnabled(false);
			productSave.setVisibility(View.GONE);
			productSave.setEnabled(false);
			break;
		case 5:
			// 待发货
			productCancle.setVisibility(View.VISIBLE);
			productCancle.setEnabled(true);
			productSave.setVisibility(View.VISIBLE);
			productSave.setEnabled(true);
			productSave.setText("发货");
			break;
		case -1:
			// 已关闭
			productCancle.setVisibility(View.GONE);
			productCancle.setEnabled(false);
			productSave.setEnabled(false);
			productSave.setVisibility(View.GONE);
			break;
		default:
			productCancle.setVisibility(View.GONE);
			productCancle.setEnabled(false);
			productSave.setVisibility(View.GONE);
			productSave.setEnabled(false);
		}
		getPeiSongDetail();
	}

	/**
	 * 处理订单
	 * 
	 * @param msg
	 */
	private void orderConfirm(Message msg) {
		System.out
				.println("-------------orderConfirm OrderDetailsActivity--------->"
						+ msg.arg1);
		refreshBtn(msg.arg1, "");
		// getOrderTrackDetail();
		if (proDialog.isShowing())
			proDialog.dismiss();
	}

	/**
	 * 初始化视图
	 */
	private void initViews() {
		inflater = LayoutInflater.from(OrderDetailsActivity.this);
		LoginManager lm = LoginManager.getInstance(OrderDetailsActivity.this);
		shopsId = lm.getLoginId();
		orderNum = (TextView) findViewById(R.id.order_detail_no);
		// orderDetailDate = (TextView) findViewById(R.id.order_detail_date);
		orderUserName = (TextView) findViewById(R.id.order_user_name);
		orderPhone = (TextView) findViewById(R.id.order_phone);
		orderAddress = (TextView) findViewById(R.id.order_location);
		userContent = (TextView) findViewById(R.id.maijia_liuyan);
		// 客户自付名称
		tv1 = (TextView) findViewById(R.id.tv1);
		// 礼品券下面的线
		line2222 = (ImageView) findViewById(R.id.line2222);
		tv_jiangliname = (TextView) findViewById(R.id.tv_jiangliname);
		line17 = (ImageView) findViewById(R.id.line17);
		tv_custom_id = (TextView) findViewById(R.id.tv_custom_id);
		// 配送费
		distributionCostsPrice = (TextView) findViewById(R.id.distribution_costs_price);
		// 配送费的名字
		distribution_costs_price_textview = (TextView) findViewById(R.id.distribution_costs_price_textview);
		// 代金券
		// couponPrice = (TextView) findViewById(R.id.coupon_price);
		// 合计
		orderTotalPrice = (TextView) findViewById(R.id.order_detail_total);
		productSave = (Button) findViewById(R.id.product_save);
		productCancle = (Button) findViewById(R.id.product_cancel);
		tv_ordertime = (TextView) findViewById(R.id.tv_ordertime);
		// 订单金额
		tv_order_money = (TextView) findViewById(R.id.tv_order_money);
		// 平台补贴
		tv_pingtai_butie = (TextView) findViewById(R.id.tv_pingtai_butie);
		// 客户实付
		tv_custom_pay = (TextView) findViewById(R.id.tv_custom_pay);
		// 平台奖励
		tv_pingtai_jiangli = (TextView) findViewById(R.id.tv_pingtai_jiangli);
		// 礼品券名称
		tv_liquan = (TextView) findViewById(R.id.tv_liquan);
		line20 = (ImageView) findViewById(R.id.line20);
		tv_youhui = (TextView) findViewById(R.id.tv_youhui);
		tv_shop_youhui = (TextView) findViewById(R.id.tv_shop_youhui);
		orderListAdapter = new OutlinesAdapter(this, R.layout.order_table_item,
				details);
		peisongLayout = (LinearLayout) findViewById(R.id.l1);
		peisongRelativeLayout = (RelativeLayout) findViewById(R.id.peisong_linear);
		houserName = (TextView) findViewById(R.id.guanjia_name);
		houserPhone = (TextView) findViewById(R.id.guanjia_phone);
		goodsnum = (TextView) findViewById(R.id.goodsnum);
		disnum = (TextView) findViewById(R.id.disnum);

		listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(orderListAdapter);
		listView.setDividerHeight(0);
		listView.setCacheColorHint(0);
		listView.setFocusable(false);
		trackListAdapter = new TracksAdapter(this, R.layout.order_track_item,
				tracks);
		listView2 = (ListView) findViewById(R.id.list_view_2);
		listView2.setAdapter(trackListAdapter);
		listView2.setDividerHeight(0);
		listView2.setCacheColorHint(0);
		listView2.setFocusable(false);
//		ScrollView scrollVIew = (ScrollView) findViewById(R.id.scrollview);
//		scrollVIew.smoothScrollTo(0, 20);
		if (getIntent().getStringExtra("ordertype").equals("1")) {

		} else if (getIntent().getStringExtra("ordertype").equals("2")) {
			// 礼品券
			tv_liquan.setVisibility(View.VISIBLE);
			// 客户自付名字
			tv1.setVisibility(View.VISIBLE);
			// 客户自付后面的价格
			tv_order_money.setVisibility(View.VISIBLE);
			// 礼品券下面的线
			line2222.setVisibility(View.VISIBLE);
			line20.setVisibility(View.VISIBLE);
			// 券名称
			tv_youhui.setVisibility(View.GONE);
			// 券的价格
			tv_shop_youhui.setVisibility(View.GONE);
			// 平台补贴
			tv_pingtai_butie.setVisibility(View.GONE);
			// 客户实付
			tv_custom_pay.setVisibility(View.GONE);
		}

	}

	private void initDataMessage(final int msg_id) {
		if (!NetConn.checkNetwork(OrderDetailsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					MessageService messageService = new MessageService(
							OrderDetailsActivity.this);
					Messages res = messageService.getMessage(msg_id);
					Message message = handler.obtainMessage(MESSAGE_DETAILS);
					message.obj = res;
					handler.sendMessage(message);
				}
			}).start();
		}
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		Intent in = getIntent();
		try {
			if (in.getStringExtra("type").equals("message")) {
				//  从消息页面跳转过来的
				initDataMessage(in.getIntExtra("msg_id", -1));
			}
		} catch (Exception e) {
		}

		orderid = in.getIntExtra("order_id", -1);
		uid = in.getIntExtra("uid", -1);
		if (in != null && orderid != -1) {
			showDialog();
			getDetail();
		}
	}

	/**
	 * 显示对话框
	 */
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}

	/**
	 * 获取订单详情
	 */
	private void getDetail() {
		if (!NetConn.checkNetwork(OrderDetailsActivity.this)) {
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					getOrderDetail();
					getOrderTrackDetail();
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
		order = orderService.getOrderDetail(orderid, uid, shopsId);
		msg.obj = order;
		if (order != null) {
			status = order.getState();
			paystate = order.getPaystate();
		}
		msg.sendToTarget();
	}

	/**
	 * 获取订单跟踪详情
	 */
	private void getOrderTrackDetail() {
		Log.d("TAG", "getOrderTrackDetail()");
		OrderService orderService = new OrderService(this);
		Message msg = handler.obtainMessage(REFRESH_TRACK);
		msg.obj = orderService.getOrderTrackDetail(orderid);
		msg.sendToTarget();
	}

	/**
	 * 找管家
	 */
	private void getGuanJiaDetail() {
		OrderService service = new OrderService(this);
		Houser houser = service.getHouser(shopsId, orderid, 0);// 运费
		Message msg = handler.obtainMessage(CHOOSE_GUANJIA);
		msg.obj = houser;
		handler.sendMessage(msg);
	}

	/*	*//**
	 * 去申请
	 */
	/*
	 * private void checkGuanJia(){ OrderService service = new
	 * OrderService(this); int flag = service.houserRes(shopsId ,again); Message
	 * msg = handler.obtainMessage(CHECK_GUANJIA); msg.arg1 = flag; msg.obj =
	 * service.errorMsg; handler.sendMessage(msg); }
	 */

	/**
	 * 撤销配单
	 * 
	 * @param view
	 */
	private void revokeDis() {
		OrderService service = new OrderService(this);
		int flag = service.revokeDis(shopsId, orderid);
		Message msg = handler.obtainMessage(REVOKE_DIS);
		msg.arg1 = flag;
		msg.obj = service.errorMsg;
		handler.sendMessage(msg);
	}

	/**
	 * 获取配送信息
	 */
	private void getPeiSongDetail() {
		if (!NetConn.checkNetwork(OrderDetailsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					OrderService service = new OrderService(
							OrderDetailsActivity.this);
					int flag = service.peisongRes(shopsId);
					Message msg = handler.obtainMessage(GET_GUANJIA_STATUS);
					msg.arg1 = flag;
					msg.obj = service.errorMsg;
					handler.sendMessage(msg);
				}
			}).start();
		}
	}

	/**
	 * 保存
	 * 
	 * @param view
	 */
	public void save_onClick(View view) {
		// 有否申请管家
		if (isApplyHouser && order.getState() != 1 && order.getState() != 4) {
			if (order.getIs_dis() == 3) {
				// order.getIs_dis() == 3 已经找了管家
				checkedDeliverGoods();
			} else {
				menuWindow = new ConfirmOrderPopupWindow(
						OrderDetailsActivity.this, itemsOnClick, changeOnClick,
						order.getIs_dis());
				menuWindow.showAtLocation(OrderDetailsActivity.this
						.findViewById(R.id.order_details_layout),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				menuWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						// TODO Auto-generated method stub
						initDatas();
					}
				});
			}
		} else {
			checkedDeliverGoods();
		}

	}

	// 为弹出窗口实现监听类 PopupWindow
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.deliver_goods_button:
				if (pop == 0) {

				} else if (pop == 1) {
					if (guanjiaStatus == 1 || guanjiaStatus == 0) {// 找管家
						if (!NetConn.checkNetwork(OrderDetailsActivity.this)) {
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									getGuanJiaDetail();
								}
							}).start();
						}
					} else if (guanjiaStatus == 3) {
						ToastUtil.show(OrderDetailsActivity.this, "审核中!");
					} else if (guanjiaStatus == 4) {// 已派送
					}
				}
				checkedDeliverGoods();
				break;
			}
		}
	};

	private void checkedDeliverGoods() {
		shopsId = order.getShopsid();
		System.out.println("-----now-status------>" + status);
		switch (order.getStatus()) {
		case 1:
			// 待接单
			status = 0;
			order.setStatus(status);
			break;
		case 2:
			// 已发货
			status = 3;
			order.setStatus(status);
			break;
		case 4:
			// 待接单
			if (order.getPaystate() == 1 && order.getPayment() == 2) {
				// 待付款的电子支付

			} else {// 未付款，待接单，货到付款
				status = 0;
				order.setStatus(status);
			}
			break;
		case 5:
			// 待发货
			status = 2;
			order.setStatus(status);
			break;
		case 0:
			// 待发货, 直接从 接单完成后，直接变成发货，然后进行设置
			status = 2;
			order.setStatus(status);
			break;
		}
		System.out
				.println("--------OrderDetailsActivity---order.getStatus()------------------>"
						+ order.getStatus());
		if (orderid != -1)
			confirmOrderAsync(ORDER_CONFIRM, status);
	}

	public int pop = 0;
	// 为弹出窗口实现监听类 PopupWindow
	private android.widget.CompoundButton.OnCheckedChangeListener changeOnClick = new android.widget.CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton view, boolean isCheck) {
			// TODO Auto-generated method stub
			if (isCheck) {
				switch (view.getId()) {
				case R.id.steward_distribution_radiobutton:
					pop = 1;
					break;

				case R.id.their_distribution_radiobutton:
					pop = 0;
					break;
				}
			}
		}
	};
	private TextView tv_liquan;
	private TextView tv_ordertime;
	private TextView distribution_costs_price_textview;
	private TextView tv1;
	private ImageView line2222;
	private ImageView line20;
	private TextView tv_jiangliname;
	private ImageView line17;
	private TextView tv_custom_id;

	/**
	 * 撤销订单
	 */
	public void cancel_onClick(View view) {
		cancelOrder();
	}

	/**
	 * 撤销订单
	 */
	private void cancelOrder() {
		status = -1;
		shopsId = order.getShopsid();
		newDialog = new Dialog(OrderDetailsActivity.this,
				R.style.theme_dialog_alert);

		if (!NetConn.checkNetwork(OrderDetailsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			proDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					getRevokeReasonList();  
				}
			}).start();
		}
	}

	/**
	 * 提交订单
	 * 
	 * @param type
	 */
	private void confirmOrderAsync(final int type, final int status) {
		if (!NetConn.checkNetwork(OrderDetailsActivity.this)) {
			// NetConn.setNetwork(OrdersActivity.this);
		} else {
			productSave.setEnabled(false);
			productCancle.setEnabled(false);
			proDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					confirmOrder(type, status);
					getOrderTrackDetail();
				}
			}).start();
		}
		getPeiSongDetail();
	}

	/**
	 * 根据类型提交订单
	 * 
	 * @param type
	 */
	private void confirmOrder(int type, int statu) {
		OrderService orderService = new OrderService(OrderDetailsActivity.this);
		int flag = -3;
		if (type == ORDER_CANCEL) {
			isSendGoods = false;
			res = orderService.cancelOrder(orderid, status, revoke_type,
					revoke_reason);

			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				flag = jo.optInt("res");
				message = jo.getString("msg");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// if (jo.optInt("res") == 0) {
			//
			// }
		} else if (statu == 0) {
			// 待接单
			type = JIE_DAN;
			String res;
			res = orderService.updateOrderStatejiedan(orderid, 5);
			JSONObject jo = null;
			try {
				jo = new JSONObject(res);
				flag = jo.optInt("res");
				message = jo.getString("msg");
				System.out.println("------------flag-------------->" + flag);
				System.out.println("------------message-------------->"
						+ message);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			flag = orderService.updateOrderState(orderid, status);
			if (flag == 0) {
				isSendGoods = true;
			}
		}
		Message msg;
		if (type != JIE_DAN) {
			msg = handler.obtainMessage(type);
		} else {
			msg = handler.obtainMessage(JIE_DAN);
		}

		msg.arg1 = flag;
		msg.obj = message;
		msg.sendToTarget();
	}

	/**
	 * 获取撤销订单的原因列表
	 */
	private void getRevokeReasonList() {
		OrderService orderService = new OrderService(OrderDetailsActivity.this);
		List<Revoke> list = orderService.getCancelOrderReasonList();
		Message message = handler.obtainMessage(ORDER_CANCEL_REASON);
		message.obj = list;
		handler.sendMessage(message);
	}

	private class OutlinesAdapter extends ArrayAdapter<OrderDetail> {
		public OutlinesAdapter(Context cx, int resId, List<OrderDetail> obj) {
			super(cx, resId, obj);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = inflater.inflate(R.layout.order_table_item, parent, false);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, position, getItem(position));
			return cv;
		}
	}

	/**
	 * 刷新订单商品详情Item
	 * 
	 * @param holder
	 * @param ps
	 * @param line
	 */
	private void refreshItem(ViewHolder holder, int ps, OrderDetail line) {
		holder.orderProduct.setSelected(true);
		holder.orderNo.setText((ps + 1) + "");
		holder.orderProduct
				.setText((line.getName() == "null"
						|| line.getName().equals("null") || line.getName() == null) ? ""
						: line.getName());
		holder.orderCount.setText("×" + line.getNum() + "");

		holder.orderPrice.setText("￥"
				+ CommonUtil.calcMoneyPoint(2, line.getShowPrice()) + "");
	}

	/**
	 * 查找Item的控件
	 * 
	 * @param holder
	 * @param cv
	 */
	private void findViews(ViewHolder holder, View cv) {
		holder.orderNo = (TextView) cv.findViewById(R.id.order_table_no);
		holder.orderProduct = (TextView) cv
				.findViewById(R.id.order_table_content);
		holder.orderCount = (TextView) cv.findViewById(R.id.order_table_count);
		holder.orderPrice = (TextView) cv.findViewById(R.id.order_table_people);
	}

	private class ViewHolder {
		TextView orderNo;
		TextView orderProduct;
		TextView orderCount;
		TextView orderPrice;
	}

	/**
	 * 订单跟踪列表的适配器
	 * 
	 * @author lqq
	 * 
	 */
	private class TracksAdapter extends ArrayAdapter<OrderTrackDetail> {
		public TracksAdapter(Context cx, int resId, List<OrderTrackDetail> obj) {
			super(cx, resId, obj);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder2 holder;
			if (cv == null) {
				cv = inflater.inflate(R.layout.order_track_item, parent, false);
				holder = new ViewHolder2();
				findViews2(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder2) cv.getTag();
			}
			refreshTrackItem(holder, position, getItem(position));
			return cv;
		}
	}

	/**
	 * 刷新订单跟踪Item
	 * 
	 * @param holder
	 * @param ps
	 * @param line
	 */
	private void refreshTrackItem(ViewHolder2 holder, int ps,
			OrderTrackDetail line) {
		holder.orderContent.setSelected(true);
		holder.orderTime.setText(DateUtil.getFormatedDate_3(line.getAddtime()));
		holder.orderContent.setText(line.getContent());
	}

	/**
	 * 查找订单跟踪控件
	 * 
	 * @param holder
	 * @param cv
	 */
	private void findViews2(ViewHolder2 holder, View cv) {
		holder.orderTime = (TextView) cv.findViewById(R.id.order_table_time);
		holder.orderContent = (TextView) cv
				.findViewById(R.id.order_table_content);
	}

	private class ViewHolder2 {
		TextView orderTime;
		TextView orderContent;
	}

	/**
	 * 打电话
	 * 
	 * @param view
	 */
	public void call_onClick(View view) {
		showInfo_call(order.getPhone());
	}

	/**
	 * 显示提醒取消配送单对话框
	 */
	private void showInfo() {
		// FIXME　此处需要修改
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.cancel_dis_dialogview, null);
		final Dialog dialog = new Dialog(OrderDetailsActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				if (!NetConn.checkNetwork(OrderDetailsActivity.this)) {
					// NetConn.setNetwork(OrdersActivity.this);
				} else {
					new Thread(new Runnable() {

						@Override
						public void run() {
							revokeDis();
						}
					}).start();
				}
			}
		});
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	/**
	 * 显示提醒取消打电话对话框
	 */
	private void showInfo_call(final String phone) {
		// FIXME　此处需要修改
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.call_dianhua, null);
		final Dialog dialog = new Dialog(OrderDetailsActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		TextView textView = (TextView) layout.findViewById(R.id.type_name);
		textView.setText(phone + "");
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CallPhoneUtil.callPhone(OrderDetailsActivity.this, phone);
				dialog.dismiss();
			}
		});
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	private void Dialog_RevokedOrder(final List<Revoke> list) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_revokedorder, null);
		myListView = (ListView) layout
				.findViewById(R.id.dialog_revoked_order_list);
		final EditText edt = (EditText) layout.findViewById(R.id.qita_edit);
		final RevokedListViewAdapter mRevokedListViewAdapter = new RevokedListViewAdapter(
				this, list);
		myListView.setAdapter(mRevokedListViewAdapter);
		if (list != null) {
			revoke_type = list.get(0).getType();
			revoke_reason = list.get(0).getReason();
		}
		myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				if (position != mId) {
					edt.clearFocus();
					edt.setText("");
					// 隐藏键盘
					// mInputMethodManager.hideSoftInputFromWindow(edt.getWindowToken(),0);
					hideSoftInput(edt.getWindowToken());
					mRevokedListViewAdapter.setCurrentID(position);
					mRevokedListViewAdapter.notifyDataSetChanged();
					if (list != null) {
						revoke_reason = list.get(position).getReason();
						revoke_type = list.get(position).getType();
					}
				}
				mId = position;
			}
		});

		edt.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 显示键盘
					// mInputMethodManager.showSoftInput(edt, 0);
					edt.setInputType(InputType.TYPE_CLASS_TEXT);
					mRevokedListViewAdapter.setCurrentID(-1);
					mRevokedListViewAdapter.notifyDataSetChanged();
					mId = -1;
				}
			}
		});

		final Dialog dialog = new Dialog(OrderDetailsActivity.this,
				R.style.theme_dialog_alert);
		dialog.setContentView(layout);
		layout.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				// 确定撤销
				if (mId == -1) {
					revoke_reason = edt.getText().toString().trim();
					revoke_type = -1;
				}
				confirmOrderAsync(ORDER_CANCEL, -1);
			}
		});
		layout.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();

		/*
		 * new AlertDialog.Builder(this).setTitle("请输入撤销订单理由").setView(layout)
		 * .setPositiveButton("确定", new AlertDialog.OnClickListener(){ public
		 * void onClick(DialogInterface dialog, int which) { //确定撤销 if(mId ==
		 * -1){ revoke_reason = edt.getText().toString().trim(); revoke_type =
		 * -1; } confirmOrderAsync(ORDER_CANCEL); //
		 * querenBtn.setClickable(false); // new
		 * QuxiaoAsyncTask(orderidString,type,reason).execute(); }})
		 * .setNegativeButton("取消", null).show();
		 */

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}