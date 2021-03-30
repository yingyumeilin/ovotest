package com.example.oto01.services;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.oto01.model.Client;
import com.example.oto01.model.Constant;
import com.example.oto01.utils.HttpUtil;
import com.example.oto01.utils.ToastUtil;

public class ClientService extends BaseHttpService {

	private static List<Client> clientList = new ArrayList<Client>();

	public ClientService(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取客户列表 get请求
	 * 
	 * @param state
	 * @param p
	 * @return
	 */
	@SuppressLint("NewApi")
	public String getClientList(int shopsid, int user_type, int p,
			String keyword, int order) {
		try {
			String res = null;

			if (keyword.isEmpty()) {
				// 没有关键字的情况
				if (user_type == 0 && order == 0) {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&p=" + p);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&p=" + p);
				} else if (user_type == 0 && order != 0) {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&p=" + p + "&order=" + order);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&p=" + p + "&order=" + order);
				} else if (user_type != 0 && order == 0) {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&user_type=" + user_type + "&p=" + p);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&user_type=" + user_type + "&p=" + p);
				} else {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&user_type=" + user_type + "&p=" + p
							+ "&order=" + order);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&user_type=" + user_type + "&p=" + p + "&order="
							+ order);
				}

			} else {
				// 有关键字的情况

				if (user_type == 0 && order == 0) {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&p=" + p + "&keyword=" + keyword);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&p=" + p + "&keyword=" + keyword);
				} else if (user_type == 0 && order != 0) {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&p=" + p + "&order=" + order
							+ "&keyword=" + keyword);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&p=" + p + "&order=" + order + "&keyword="
							+ keyword);
				} else if (user_type != 0 && order == 0) {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&user_type=" + user_type + "&p=" + p
							+ "&keyword=" + keyword);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&user_type=" + user_type + "&p=" + p
							+ "&keyword=" + keyword);
				} else {
					res = HttpUtil.doGet(Constant.CLIENT_LIST_URL + "&shopsid="
							+ shopsid + "&user_type=" + user_type + "&p=" + p
							+ "&order=" + order + "&keyword=" + keyword);
					System.out.println("----getClientList---->"
							+ Constant.CLIENT_LIST_URL + "&shopsid=" + shopsid
							+ "&user_type=" + user_type + "&p=" + p + "&order="
							+ order + "&keyword=" + keyword);
				}

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
	 * 删除客户 post请求
	 * 
	 * @param shopsid
	 * @param id
	 * @return
	 */
	public String deleteClientList(int shopsid, int id) {
		try {
			String res = null;

			String string = "";
			string = "[[\"id\",\"" + id + "\"],[\"shopsid\",\"" + shopsid
					+ "\"]]";
			res = HttpUtil.doPost(Constant.CLIENT_DELETE, string);
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
	 * 添加客户 post请求
	 * 
	 * @param shopsid
	 * @param linkman
	 * @param linkphone
	 * @return
	 */
	public String AddClientList(int shopsid, String nickname, String userphone) {
		try {
			String res = null;

			String string = "";
			string = "[[\"shopsid\",\"" + shopsid + "\"],[\"nickname\",\""
					+ nickname + "\"],[\"userphone\",\"" + userphone + "\"]]";
			res = HttpUtil.doPost(Constant.CLIENT_ADD, string);
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
	 * 得到客户详情
	 * 
	 * @param shopsid
	 * @param id
	 * @param p
	 * @return
	 */
	public String getClientDetailsList(int shopsid, int id, int p, int user_type) {
		try {
			String res = null;
			res = HttpUtil.doGet(Constant.CLIENT_DETAILS + "&id=" + id
					+ "&shopsid=" + shopsid + "&p=" + p + "&user_type="
					+ user_type);
			System.out.println("----getClientDetailsList---->"
					+ Constant.CLIENT_DETAILS + "&id=" + id + "&shopsid="
					+ shopsid + "&p=" + p + "&user_type=" + user_type);
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
	 * 修改备注 id shopsid remark_name
	 */
	public String RemarkNameClient(int shopsid, int id, String remark_name) {
		try {
			String res = null;

			String string = "";
			string = "[[\"id\",\"" + id + "\"],[\"shopsid\",\"" + shopsid
					+ "\"],[\"remark_name\",\"" + remark_name + "\"]]";
			res = HttpUtil.doPost(Constant.CLIENT_REMARK_NAME, string);
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
	 * 验证已存在客户更新为通讯录昵称
	 */

	public String ClientUPDATE(int shopsid, String nickname, String userphone) {
		try {
			String res = null;

			String string = "";
			string = "[[\"nickname\",\"" + nickname + "\"],[\"shopsid\",\""
					+ shopsid + "\"],[\"userphone\",\"" + userphone + "\"]]";
			res = HttpUtil.doPost(Constant.CLIENT_TONGXUN_HEBING, string);
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
