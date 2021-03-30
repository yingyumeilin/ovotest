package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.oto01.model.Constant;
import com.example.oto01.model.DISInfo;
import com.example.oto01.model.Good;
import com.example.oto01.model.Houser;
import com.example.oto01.model.Order;
import com.example.oto01.model.OrderDetail;
import com.example.oto01.model.OrderTrackDetail;
import com.example.oto01.model.Revoke;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 订单管理
 * 
 * @author lqq
 * 
 */
public class OrderService extends BaseHttpService {
	private List<Order> orderList = new ArrayList<Order>();
	public int total_page = 0;
	public int p = 0;
	public int waitnum = 0;
	public int havenum = 0;
	public String errorMsg;

	public OrderService(Context context) {
		super(context);
	}

	/**
	 * 获取订单列表
	 * 
	 * @param state
	 * @param p
	 * @return
	 */
	@SuppressLint("NewApi")
	public List<Order> getOrdersList(int shopsid, int state, int p,
			String keyword, int paystate) {
		try {
			String res = null;

			if (state == 0) {
				// 待付款状态
				if (keyword.isEmpty()) {
					// 没有关键字的情况
					res = HttpUtil.doGet(Constant.OREDR_LIST_URI + "&shopsid="
							+ shopsid + "&p=" + p + "&paystate=" + paystate);
					System.out.println("----getOrdersList---->"
							+ Constant.OREDR_LIST_URI + "&shopsid=" + shopsid
							+ "&p=" + p + "&paystate=" + paystate);
				} else {
					// 有关键字的情况
					res = HttpUtil.doGet(Constant.OREDR_LIST_URI + "&shopsid="
							+ shopsid + "&p=" + p + "&keyword=" + keyword);
					System.out.println("----有关键字的情况--------->"
							+ Constant.OREDR_LIST_URI + "&shopsid=" + shopsid
							+ "&keyword=" + keyword);
				}

			} else {
				if (state == 1) {
					// 待结单的情况
					res = HttpUtil.doGet(Constant.OREDR_LIST_URI + "&shopsid="
							+ shopsid + "&state=" + "1,4" + "&p=" + p);
					System.out.println("----getOrdersList---->"
							+ Constant.OREDR_LIST_URI + "&shopsid=" + shopsid
							+ "&state=" + "1,4" + "&p=" + p);

				} else {
					res = HttpUtil.doGet(Constant.OREDR_LIST_URI + "&shopsid="
							+ shopsid + "&state=" + state + "&p=" + p);
					System.out.println("----getOrdersList---->"
							+ Constant.OREDR_LIST_URI + "&shopsid=" + shopsid
							+ "&state=" + state + "&p=" + p);

				}
			}

			return handleOrderListRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<Order> handleOrderListRes(String res) {
		Log.d("TAG", "res handleOrderListRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				total_page = jo.optInt("totalnum", -1);
				waitnum = jo.optInt("waitnum", -1);
				havenum = jo.optInt("havenum", -1);
				System.out.println("-----waitnum----->" + waitnum);
				System.out.println("-----havenum----->" + havenum);
				p = jo.optInt("p", -1);
				JSONArray pa = (JSONArray) jo.get("data");
				for (int i = 0; i < pa.length(); i++) {
					JSONObject po = (JSONObject) pa.get(i);
					List<Good> orderGood = new ArrayList<Good>();
					if (!po.isNull("goods")) {
						if (!po.get("goods").equals(null)) {
							JSONArray jsonArray = (JSONArray) po.get("goods");
							for (int j = 0; j < jsonArray.length(); j++) {
								JSONObject jsonObject = (JSONObject) jsonArray
										.get(j);
								int id = 0;
								if (!jsonObject.isNull("id")) {
									id = jsonObject.getInt("id");
								}
								int shopsid = 0;
								if (!jsonObject.isNull("shopsid")) {
									shopsid = jsonObject.getInt("shopsid");
								}
								String picname = null;
								if (!jsonObject.isNull("picname")) {
									picname = jsonObject.getString("picname");
								}
								String picpath = null;
								if (!jsonObject.isNull("picpath")) {
									picpath = jsonObject.getString("picpath");
								}
								String content = null;
								if (!jsonObject.isNull("content")) {
									content = jsonObject.getString("content");
								}
								Good good = new Good(id, shopsid, picname,
										picpath, content);
								orderGood.add(good);
							}
						}
					}
					int id = 0;
					if (!po.isNull("id")) {
						id = po.getInt("id");
					}
					String total = "";
					// double total = 0;
					if (!po.isNull("total")) {
						total = po.getString("total");
						// total = po.getDouble("total");
					}
					int shopsid = 0;
					if (!po.isNull("shopsid")) {
						shopsid = po.getInt("shopsid");
					}
					int state = 0;
					if (!po.isNull("state")) {
						state = po.getInt("state");
					}
					int uid = 0;
					if (!po.isNull("uid")) {
						uid = po.getInt("uid");
					}
					String ordernum = "";
					if (!po.isNull("ordernum")) {
						ordernum = po.getString("ordernum");
					}
					String linkman = "";
					if (!po.isNull("linkman")) {
						linkman = po.getString("linkman");
					}
					String address = "";
					if (!po.isNull("address")) {
						address = po.getString("address");
					}
					String addtime = "";
					if (!po.isNull("addtime")) {
						addtime = po.getString("addtime");
					}
					String goodsnum = "";
					if (!po.isNull("goodsnum")) {
						goodsnum = po.getString("goodsnum");
					}
					String disnum = "";
					if (!po.isNull("disnum")) {
						disnum = po.getString("disnum");
					}
					String order_type = "";
					if (!po.isNull("order_type")) {
						order_type = po.getString("order_type");
					}
					int is_dis = 0;
					if (!po.isNull("is_dis")) {
						is_dis = po.getInt("is_dis");
					}
					int payment = 0;
					if (!po.isNull("payment")) {
						payment = po.getInt("payment");
					}
					int paystate = 0;
					if (!po.isNull("payment")) {
						paystate = po.getInt("paystate");
					}
					String gift_goods_pic = "";
					try {
						if (!po.isNull("gift_goods_pic")) {
							gift_goods_pic = po.getString("gift_goods_pic");
						}
					} catch (Exception e) {
						// TODO: handle exception
						gift_goods_pic = "";
					}

					Order order = new Order(id, ordernum, shopsid, uid,
							linkman, address, total, state, addtime, orderGood,
							po.getString("total"), goodsnum, disnum, is_dis,
							payment, paystate, order_type, gift_goods_pic);
					orderList.add(order);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderList;
	}

	/**
	 * 获取订单详情
	 * 
	 * @param orderid
	 * @param uid
	 * @return
	 */
	public Order getOrderDetail(int orderid, int uid, int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.ORDER_DETAILS_URI
					+ "&orderid=" + orderid + "&uid=" + uid + "&shopsid="
					+ shopsid);
			System.out.println("----url---->" + Constant.ORDER_DETAILS_URI
					+ "&orderid=" + orderid + "&uid=" + uid);

			return handleOrderDetailRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private Order handleOrderDetailRes(String res) {
		Log.d("TAG", "res handleOrderListRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				// JSONArray jsonArray = (JSONArray) jo.get("ordervo");
				// JSONObject jsonObject = jsonArray.getJSONObject(0);
				JSONObject jsonObject = jo.getJSONObject("ordervo");

				List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
				try {
					if (!jsonObject.get("detail").equals(null)) {

						JSONArray pa = (JSONArray) jsonObject.get("detail");
						for (int i = 0; i < pa.length(); i++) {
							JSONObject po = (JSONObject) pa.get(i);
							int goodsnum = -1;
							if (!po.isNull("goodsnum")) {
								goodsnum = po.getInt("goodsnum");
							}
							OrderDetail orderDetail = new OrderDetail(
									po.getInt("id"), po.getInt("goodsid"),
									po.getInt("orderid"), goodsnum,
									po.getString("name"), Float.parseFloat(po
											.getString("price")),
									po.getInt("num"), po.getString("price"));
							orderDetails.add(orderDetail);
						}
					}
				} catch (Exception e) {
				}
				Order order = null;
				if (jsonObject.getString("order_type").equals("1")) {
					// 普通订单
					order = new Order(jsonObject.getInt("id"),
							jsonObject.getString("ordernum"),
							jsonObject.getInt("payment"),
							jsonObject.getInt("shopsid"),
							jsonObject.getString("linkman"),
							jsonObject.getString("address"),
							jsonObject.getString("phone"),
							jsonObject.getString("total"),
							jsonObject.getInt("state"),
							jsonObject.getString("addtime"),
							jsonObject.getString("username"),
							jsonObject.getString("userphone"),
							jsonObject.getString("content"), orderDetails,
							jsonObject.getInt("paystate"),
							jsonObject.getString("total"),
							jsonObject.getString("goodsnum"),
							jsonObject.getString("hphone"),
							jsonObject.getString("hname"),
							jsonObject.getInt("is_dis"),
							jsonObject.getString("disnum"),
							jsonObject.getString("freight_total_price"), "",
							"", jsonObject.getString("reward_price"), "", "",
							jsonObject.getString("order_type"),
							jsonObject.getString("discount_total_price"),
							jsonObject.getString("shops_receive_total"),
							jsonObject.getString("actual_coupon_total"),
							jsonObject.getString("coupon_name"), "");
				} else {
					// 礼品券订单
					order = new Order(jsonObject.getInt("id"),
							jsonObject.getString("ordernum"),
							jsonObject.getInt("payment"),
							jsonObject.getInt("shopsid"),
							jsonObject.getString("linkman"),
							jsonObject.getString("address"),
							jsonObject.getString("phone"),
							jsonObject.getString("total"),
							jsonObject.getInt("state"),
							jsonObject.getString("addtime"),
							jsonObject.getString("username"),
							jsonObject.getString("userphone"),
							jsonObject.getString("content"), orderDetails,
							jsonObject.getInt("paystate"),
							jsonObject.getString("total"),
							jsonObject.getString("goodsnum"),
							jsonObject.getString("hphone"),
							jsonObject.getString("hname"),
							jsonObject.getInt("is_dis"),
							jsonObject.getString("disnum"),
							jsonObject.getString("freight_total_price"), "",
							"", "", "", "", jsonObject.getString("order_type"),
							"", jsonObject.getString("shops_receive_total"),
							"", jsonObject.getString("coupon_name"),
							jsonObject.getString("total_reward_price"));
				}
				return order;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 订单跟踪
	 * 
	 * @return
	 */
	public List<OrderTrackDetail> getOrderTrackDetail(int orderid) {
		try {
			String res = HttpUtil.doGet(Constant.ORDER_TRACK_URI + "&orderid="
					+ orderid);

			return handleOrderTrackDetailRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<OrderTrackDetail> handleOrderTrackDetailRes(String res) {
		Log.d("TAG", "res handleOrderTrackDetailRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			List<OrderTrackDetail> list = new ArrayList<OrderTrackDetail>();
			if (jo.optInt("res") == 0) {
				JSONArray pa = (JSONArray) jo.get("data");
				for (int i = 0; i < pa.length(); i++) {
					JSONObject jsonObject = pa.getJSONObject(i);
					OrderTrackDetail orderTrackDetail = new OrderTrackDetail(
							jsonObject.getInt("orderid"),
							jsonObject.getString("content"), null,
							jsonObject.getString("addtime"));
					list.add(orderTrackDetail);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改订单状态
	 * 
	 * @param orderid
	 * @param state
	 * @return
	 */
	public int updateOrderState(int orderid, int state) {
		try {
			String res = HttpUtil.doGet(Constant.ORDER_STATES_UPDATE_URI
					+ "&orderid=" + orderid + "&state=" + state);
			System.out.println("-----url---->"
					+ Constant.ORDER_STATES_UPDATE_URI + "&orderid=" + orderid
					+ "&state=" + state);

			return handleUpdateOrderStateRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -2;
	}

	/**
	 * 修改订单状态 接单
	 * 
	 * @param orderid
	 * @param state
	 * @return
	 */
	public String updateOrderStatejiedan(int orderid, int state) {
		try {
			String res = HttpUtil.doGet(Constant.ORDER_STATES_UPDATE_URI
					+ "&orderid=" + orderid + "&state=" + state);
			System.out.println("-----url---->"
					+ Constant.ORDER_STATES_UPDATE_URI + "&orderid=" + orderid
					+ "&state=" + state);

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 取消订单
	 * 
	 * @param orderid
	 * @param state
	 * @param revoke_type
	 * @param revoke_reason
	 * @return
	 */
	public String cancelOrder(int orderid, int state, int revoke_type,
			String revoke_reason) {
		try {
			String res = HttpUtil.doGet(Constant.ORDER_STATES_UPDATE_URI
					+ "&orderid=" + orderid + "&state=" + state
					+ "&revoke_type=" + revoke_type + "&revoke_reason="
					+ revoke_reason);
			System.out.println("-----url---->"
					+ Constant.ORDER_STATES_UPDATE_URI + "&orderid=" + orderid
					+ "&state=" + state + "&revoke_type=" + revoke_type
					+ "&revoke_reason=" + revoke_reason);
			// JSONObject jo = new JSONObject(res);
			// if (jo.optInt("res") == 0) {
			// return 0;
			// }
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;

	}

	/**
	 * 获取取消订单的原因列表
	 * 
	 * @return
	 */
	public List<Revoke> getCancelOrderReasonList() {
		try {
			String res = HttpUtil.doGet(Constant.CANCEL_ORDER_REASON_LIST_URI);
			System.out.println("---cancel Reson url------>"
					+ Constant.CANCEL_ORDER_REASON_LIST_URI);
			return handleGetCancelOrderReasonList(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<Revoke> handleGetCancelOrderReasonList(String res) {
		System.out.println("---handleGetCancelOrderReasonList----->" + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				JSONArray jsonArray = jo.getJSONArray("revoke");
				List<Revoke> list = new ArrayList<Revoke>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject j = jsonArray.getJSONObject(i);
					Revoke revoke = new Revoke(j.getInt("type"),
							j.getString("reason"));
					list.add(revoke);
				}
				return list;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private int handleUpdateOrderStateRes(String res) {
		Log.d("TAG", "res handleOrderTrackDetailRes : " + res);
		System.out.println("-----修改订单状态----->" + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				return 0;
			}
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;
	}

	/**
	 * 提交配送单给管家端 shopsid int 商铺id orderid int 订单id reward double 运费
	 * 
	 * @return
	 */
	public Houser getHouser(int shopsid, int orderid, double reward) {
		try {
			String res = HttpUtil.doGet(Constant.COMMIT_ORDER_TO_HOUSER_URI
					+ "&shopsid=" + shopsid + "&orderid=" + orderid
					+ "&reward=" + reward);
			System.out.println("---getHouser url------>"
					+ Constant.COMMIT_ORDER_TO_HOUSER_URI + "&shopsid="
					+ shopsid + "&orderid=" + orderid + "&reward=" + reward);
			return handleGetHouser(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Houser handleGetHouser(String res) {
		try {
			System.out.println("-----------handleGetHouser---------->" + res);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("status") == 30408 || jo.optInt("status") == 0) {
				Houser houser = new Houser(jo.getString("name"),
						jo.getString("phone"), jo.getString("disnum"),
						jo.getString("goodsnum"));
				return houser;
			} else {
				Houser houser = new Houser(jo.getString("msg"));
				return houser;
			}
			// errorMsg = jo.getString("msg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 审核商铺
	 * 
	 * @param shopsid
	 * @return
	 */
	public int houserRes(int shopsid, int again) {
		try {
			String res = HttpUtil.doGet(Constant.HOUSER_RES_URI + "&shopsid="
					+ shopsid + "&again=" + again);
			System.out.println("---houserRes url------>"
					+ Constant.HOUSER_RES_URI + "&shopsid=" + shopsid
					+ "&again=" + again);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("status") == 0) {
				return 0;
			}
			errorMsg = jo.getString("msg");
			return jo.optInt("status");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 撤销配送单
	 * 
	 * @param shopsid
	 * @param orderid
	 * @return
	 */
	public int revokeDis(int shopsid, int orderid) {
		try {
			String res = HttpUtil.doGet(Constant.REVOKE_DIS_URI + "&shopsid="
					+ shopsid + "&orderid=" + orderid);
			System.out.println("---houserRes url------>"
					+ Constant.REVOKE_DIS_URI + "&shopsid=" + shopsid
					+ "&orderid=" + orderid);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("status") == 0) {
				return 0;
			}
			errorMsg = jo.getString("msg");
			return jo.optInt("status");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取配送信息
	 * 
	 * @param shopsid
	 * @return
	 */
	public int peisongRes(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.GET_HOUSER_RES_URI
					+ "&shopsid=" + shopsid);
			System.out.println("---peisongRes url------>"
					+ Constant.GET_HOUSER_RES_URI + "&shopsid=" + shopsid);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("status") == 30209) {
				return 0;
			}
			errorMsg = jo.getString("msg");
			return jo.optInt("status");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 设置界面的与管家交互信息
	 * 
	 * @param shopsid
	 * @return
	 */
	public DISInfo disRes(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.SETTING_GET_HOUSER_RES_URI
					+ "&shopsid=" + shopsid);
			System.out.println("---disRes url------>"
					+ Constant.SETTING_GET_HOUSER_RES_URI + "&shopsid="
					+ shopsid);
			JSONObject jo = new JSONObject(res);
			String state_result = "";
			if (!jo.isNull("state_result")) {
				state_result = jo.getString("state_result");
			}
			String address = "";
			if (!jo.isNull("address")) {
				address = jo.getString("address");
			}
			DISInfo disInfo = new DISInfo(jo.getInt("status"),
					jo.getString("msg"), address, state_result);
			return disInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 订单数量接口
	 */

	public String getOrderNum(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.GET_ORDER_NUM + "&shopsid="
					+ shopsid);
			System.out.println("---houserRes url------>"
					+ Constant.GET_ORDER_NUM + "&shopsid=" + shopsid);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 到店支付 获取列表页
	 */
	@SuppressLint("NewApi")
	public String getStorePayList(int shopsid, int p, String keyword) {

		try {
			String res = null;

			if (keyword.isEmpty()) {
				// 没有关键字的情况
				res = HttpUtil.doGet(Constant.STORE_PAY_LIST + "&shopsid="
						+ shopsid + "&p=" + p);
				System.out.println("----getStorePayList---->"
						+ Constant.STORE_PAY_LIST + "&shopsid=" + shopsid
						+ "&p=" + p);
			} else {
				// 有关键字的情况
				res = HttpUtil.doGet(Constant.STORE_PAY_LIST + "&shopsid="
						+ shopsid + "&p=" + p + "&keyword=" + keyword);
				System.out.println("----有关键字的情况--------->"
						+ Constant.STORE_PAY_LIST + "&shopsid=" + shopsid
						+ "&p=" + p + "&keyword=" + keyword);
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 优惠买单订单详情接口
	 */
	public String getShopDetails(int shopsid, int id) {
		try {
			String res = null;
			res = HttpUtil.doGet(Constant.STORE_PAY_DETAILS + "&shopsid="
					+ shopsid + "&id=" + id);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	/**
	 * 优惠买单验证订单接口
	 */
	public String getShopCode(int shopsid, String authcode) {
		try {
			String res = null;
			res = HttpUtil.doGet(Constant.ORDER_PAY_CODE + "&shopsid="
					+ shopsid + "&authcode=" + authcode);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;

	}

	/**
	 * 
	 * TODO<礼品券验证订单接口>
	 * 
	 * @throw
	 * @return String
	 */
	public String getShopQuanCode(int shopsid, String code, String imei,
			String longitude, String latitude) {
		try {
			String res = null;
			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"code\",\"" + code
					+ "\"],[\"imei\",\"" + imei + "\"],[\"longitude\",\""
					+ longitude + "\"],[\"latitude\",\"" + latitude + "\"]]";
			res = HttpUtil.doPost(Constant.ORDER_QUAN_CODE, string);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;

	}

}
