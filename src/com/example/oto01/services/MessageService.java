package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.oto01.model.Constant;
import com.example.oto01.model.MessageType;
import com.example.oto01.model.Messages;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * 消息管理
 * 
 * @author lqq
 * 
 */
public class MessageService extends BaseHttpService {
	private List<Messages> messageList = new ArrayList<Messages>();
	public int total_page = 0;
	public int p = 0;
	public String errorMsg;

	public MessageService(Context context) {
		super(context);
	}

	/**
	 * 得到消息总数
	 * 
	 * @return
	 */
	public int getMessageNum(int shopsid) {
		try {
			System.out.println("-----getmsg--num---->" + Constant.MSG_NUM_URI
					+ "&shopsid=" + shopsid);
			String res = HttpUtil.doGet(Constant.MSG_NUM_URI + "&shopsid="
					+ shopsid);
			System.out.println("-----res------>" + res);

			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				return jo.getInt("num");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 得到消息分类数量
	 */

	/**
	 * 得到消息分类数量
	 * 
	 * @return
	 */
	public List<MessageType> getMessageType(int shopsid) {
		try {
			String res = HttpUtil.doGet(Constant.MSG_TYPE_URI + "&shopsid="
					+ shopsid);
			return handleMessageTypeRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private List<MessageType> handleMessageTypeRes(String res) {
		Log.d("TAG", "res handleMessageTypeRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				JSONArray jsonArray = jo.getJSONArray("data");
				List<MessageType> list = new ArrayList<MessageType>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					MessageType messageType = new MessageType(
							object.getInt("infortype"),
							object.getString("title"), object.getInt("num"),
							object.getString("addtime"));
					// MessageType messageType = new
					// MessageType(object.getInt("infortype"),
					// object.getString("title"),
					// object.getInt("num"),object.getString("name"),object.getString("addtime"));
					list.add(messageType);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 分类获取消息列表
	 * 
	 * @param typeid
	 * @param p
	 * @return
	 */
	public String getMessageList(int shopsid, int typeid, int p,
			double longitude, double latitude) {
		try {
			String res = HttpUtil.doGet(Constant.MSG_LIST_URI + "&shopsid="
					+ shopsid + "&infortype=" + typeid + "&p=" + p
					+ "&longitude=" + longitude + "&latitude=" + latitude);
			System.out.println("------url---->" + Constant.MSG_LIST_URI
					+ "&shopsid=" + shopsid + "&infortype=" + typeid + "&p="
					+ p + "&longitude=" + longitude + "&latitude=" + latitude);
			System.out.println("-------res-------->" + res);
			// return handleMessageListRes(res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	//
	// private List<Messages> handleMessageListRes(String res) {
	// Log.d("TAG", "res handleMessageListRes : " + res);
	// try {
	// JSONObject jo = new JSONObject(res);
	// if (jo.optInt("res") == 0) {
	// total_page = jo.optInt("total", -1);
	// p = jo.optInt("nowp", -1);
	// JSONArray pa = (JSONArray) jo.get("data");
	// for (int i = 0; i < pa.length(); i++) {
	// JSONObject jsonObject = pa.getJSONObject(i);
	// int orderid = -1;
	// if (!jsonObject.isNull("orderid")) {
	// orderid = jsonObject.getInt("orderid");
	// }
	// int uid = -1;
	// if (!jsonObject.isNull("uid")) {
	// uid = jsonObject.getInt("uid");
	// }
	// String username = "";
	// if (!jsonObject.isNull("username")) {
	// username = jsonObject.getString("username");
	// }
	// String ordernum = "";
	// if (!jsonObject.isNull("ordernum")) {
	// ordernum = jsonObject.getString("ordernum");
	// }
	// String introduction = "";
	// if (!jsonObject.isNull("introduction")) {
	// introduction = jsonObject.getString("introduction");
	// }
	// int state = -1;
	// if (!jsonObject.isNull("state")) {
	// state = jsonObject.getInt("state");
	// }
	// int num = -1;
	// if (!jsonObject.isNull("num")) {
	// num = jsonObject.getInt("num");
	// }
	// double total = 0;
	// if (!jsonObject.isNull("total")) {
	// total = jsonObject.getDouble("total");
	// }
	// int is_mark = -1;
	// if (!jsonObject.isNull("is_mark")) {
	// is_mark = jsonObject.getInt("is_mark");
	// }
	// Messages message = new Messages(
	// jsonObject.getInt("infortype"), username,
	// jsonObject.getString("title"),
	// jsonObject.getString("addtime"),
	// jsonObject.getInt("id"), orderid, uid, state, num,
	// total, introduction, is_mark, ordernum);
	// messageList.add(message);
	// }
	// return messageList;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	/**
	 * 根据消息id号获取消息详情
	 * 
	 * @param inforid
	 * @return
	 */
	public Messages getMessage(int inforid) {
		try {
			String res = HttpUtil.doGet(Constant.MSG_DETAILS_URI + "&inforid="
					+ inforid);
			// return res;

			return (Messages) handleMessageDetailsRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return null;
	}

	private Messages handleMessageDetailsRes(String res) {
		Log.d("TAG", "res handleMessageDetailsRes : " + res);
		try {
			JSONObject jo = new JSONObject(res);
			
			if (jo.optInt("res") == 0) {
				JSONObject jsonObject=jo.getJSONObject("data");
				Messages message = new Messages(
						jsonObject.getString("infortype"),
						jsonObject.getString("title"),
						jsonObject.getString("content"),
						jsonObject.getString("addtime"));
				return message;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 标记消息接口
	 * 
	 * @param id
	 * @return
	 */
	public int markMessage(int id) {
		try {
			String res = HttpUtil.doGet(Constant.MARK_MSG_URI + "&id=" + id);
			System.out.println("-----markMessage------->" + res);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				return 0;
			}
			errorMsg = jo.getString("msg");
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 取消标记消息接口
	 * 
	 * @param id
	 * @return
	 */
	public int cancelMarkMessage(int id) {
		try {
			String res = HttpUtil.doGet(Constant.CANCEL_MSG_URI + "&id=" + id);
			System.out.println("-----cancelMarkMessage------->" + res);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				return 0;
			}
			errorMsg = jo.getString("msg");
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

	/**
	 * 删除消息
	 * 
	 * @param id
	 * @return
	 */
	public int deleteMessage(int id) {
		try {
			String res = HttpUtil.doGet(Constant.DELETE_MSG_URI + "&id=" + id);
			System.out.println("-----deleteMessage------->" + res);
			JSONObject jo = new JSONObject(res);
			if (jo.optInt("res") == 0) {
				return 0;
			}
			errorMsg = jo.getString("msg");
			return jo.optInt("res");
		} catch (Exception e) {
			e.printStackTrace();
			if (HttpUtil.TIME_OUT) {
				ToastUtil.show(context, "当前网络状况不好！");
			}
		}
		return -1;
	}

}
