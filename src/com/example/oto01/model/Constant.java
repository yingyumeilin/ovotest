package com.example.oto01.model;

import android.content.Context;
import android.util.DisplayMetrics;

public class Constant {

	// public static final String webHost =
	// "http://test.eapi.1fuwu.com.cn/O2OV2/seller/index.php";// 农商银行
	// public static final String webHost =
	// "http://test.eapi.1fuwu.com.cn/O2OV2/seller/index.php";// 微软
	// public static final String webHost =
	// "http://139.217.11.18:81/O2OV2_test/seller/index.php";// 测试
	// public static final String webHost =
	// "https://key.1fuwu.com.cn/O2OV2_test/seller/index.php";// https测试

	// public static final String webHost =
	// "http://eapi.1fuwu.com.cn/O2OV2/seller/index.php";// 线上原来
	
//	public static final String webHost = "https://eapi.1fuwu.com.cn/O2OV3/seller/index.php";// 线上
	
	public static final String webHost = "http://139.217.11.18:81/O2OV2_test/seller_db/index.php";// 线上
	public static final String ceshi = webHost + "?m=RcbShops&a=ceshi";

	/**
	 * �??电子账号
	 */
	// public static final String DIANZIZHANGHAO_URI =
	// "http://222.35.42.101/eaccount/PhoneRegist01.do";
	public static final String DIANZIZHANGHAO_URI = webHost;

	/**
	 * 1.1 首页—店铺信息接�?http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * indexstore&shopsid=1
	 */
	/*
	 * public static final String SHOP_INFO_URI = webHost +
	 * "?m=RcbShops&a=indexstore";
	 *//**
	 * 1.2 首页—订单数接口
	 * :http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=indexordernum
	 * &shopsid=1
	 */
	/*
	 * public static final String OREDR_NUM_URI = webHost +
	 * "?m=RcbShops&a=indexordernum"; z
	 *//**
	 * 1.3 首页—商品数接口:http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * indexgoods&shopsid=1
	 */
	/*
	 * public static final String GOOD_NUM_URI = webHost
	 * +"?m=RcbShops&a=indexgoods";
	 */

	public static final String HOME_PAHE_URI = webHost
			+ "?m=HomePage&a=homepage";

	/**
	 * 分享店铺信息接口：http://221.122.32.41:8088/seller_dev/index.php?m=ApiShop&a=
	 * putshare
	 */
	public static final String SHARE_SHOP_URI = webHost
			+ "?m=ApiShop&a=putshare";

	/**
	 * 1.4 获取城市接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=getcity&
	 * parentid=1（一级省parentid�?，不传此值）
	 */
	public static final String CITY_LIST_URI = webHost
			+ "?m=RcbShops&a=getcity";

	/**
	 * 1.5 获取商店分类接口:http://192.168.2.211/seller/index.php?m=RcbShops&a=
	 * getshopstype
	 */
	public static final String SHOP_TYPE_LIST_URI = webHost
			+ "?m=RcbShops&a=getshopstype";

	/**
	 * 1.6
	 * 校验验证码接口（测试暂用）：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * checkcode
	 */
	public static final String CHECK_PHONE_CODE_URI = webHost
			+ "?m=RcbShops&a=checkcode";

	/**
	 * 1.7 验证注册接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=verify
	 */
	public static final String CHECK_REGISTER_URI = webHost
			+ "?m=RcbShops&a=verify";

	/**
	 * 1.8
	 * 上传营业执照接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=addlic&id=
	 * 4&license=123.jpg
	 */
	public static final String UPLOAD_LICENSE_URI = webHost
			+ "?m=RcbShops&a=addlic";

	/**
	 * 1.9 登录接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=login
	 */
	public static final String LOGIN_URI = webHost + "?m=RcbShops&a=login";

	/**
	 * 1.11
	 * 找回密码接口：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=findpass
	 */
	public static final String FIND_PASS_URI = webHost
			+ "?m=RcbShops&a=findpass";

	/**
	 * 1.11 �?��登录接口:http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * loginout&shopsid=1
	 */
	public static final String EXIT_LOGIN_URI = webHost
			+ "?m=RcbShops&a=loginout";

	/**
	 * 1.10 商品类别接口：http://192.168.2.101/seller/index.php?m=RcbShops&a=goodstype&
	 * shopsid=1
	 */
	public static final String GOOD_TYPES_URI = webHost
			+ "?m=RcbShops&a=goodstype";

	/**
	 * 1.11 添加商品类别接口
	 * �?http://192.168.2.101/seller/index.php?m=RcbShops&a=addtype
	 * &shopsid=1&typename=儿童套餐
	 */
	public static final String ADD_GOOD_TYPE_URI = webHost
			+ "?m=RcbShops&a=addtype";

	/**
	 * 1.12 删除商品分类接口
	 * �?http://192.168.2.101/seller/index.php?m=RcbShops&a=deltype&typeid=11
	 */
	public static final String DELETE_GOOD_TYPE_URI = webHost
			+ "?m=RcbShops&a=deltype";

	/**
	 * 1.13 浏览商品接口
	 * �?http://192.168.2.101/seller/index.php?m=RcbShops&a=goodslist
	 * &shopsid=1&sortattr=num&sortmode=desc
	 */
	public static final String GOOD_LIST_URI = webHost
			+ "?m=RcbShops&a=goodslist_v2";

	/**
	 * 1.14 商品详情接口
	 * �?http://192.168.2.101/seller/index.php?m=RcbShop&a=goodsvo&shopsid
	 * =1&goodsid=198
	 */
	public static final String GOOD_DETAILS_URI = webHost
			+ "?m=RcbShops&a=goodsvo";

	/**
	 * 1.8 添加店铺logo接口:http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * makelogo
	 */
	public static final String uploadLogoURL = webHost
			+ "?m=RcbShops&a=makelogo";

	/**
	 * 1.15
	 * 图片上传接口:http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=upload
	 */
	public static final String uploadImgURL = webHost + "?m=RcbShops&a=upload";

	/**
	 * 1.16 添加商品信息接口
	 * �?http://192.168.2.101/seller/index.php?m=RcbShop&a=addgoods
	 */
	public static final String ADD_GOOD_URI = webHost
			+ "?m=RcbShops&a=addgoods";

	/**
	 * 1.17 修改商品信息接口 : http://192.168.2.101/seller/index.php?m=RcbShop&a=upgoods
	 */
	public static final String UPDATE_GOOD_URI = webHost
			+ "?m=RcbShops&a=upgoods";

	/**
	 * 1.20 修改商品图片接口:
	 * http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=upimage
	 */
	public static final String UPDATE_GOOD_IMG_URI = webHost
			+ "?m=RcbShops&a=upimage";

	/**
	 * 1.18 订单列表接口：http://192.168.2.101/seller/index.php?m=RcbShops&a=orderlist&
	 * shopsid=1
	 */
	public static final String OREDR_LIST_URI = webHost
			+ "?m=RcbShops&a=orderlist_v3";

	/**
	 * 1.19 订单详情接口：http://192.168.2.101/seller/index.php?m=RcbShops&a=ordervo&
	 * orderid=241&uid=1
	 */
	public static final String ORDER_DETAILS_URI = webHost
			+ "?m=RcbShops&a=ordervo_v3";

	/**
	 * 1.20
	 * 订单跟踪接口：http://192.168.2.101/seller/index.php?m=RcbShops&a=ordertrack&
	 * orderid=241
	 */
	public static final String ORDER_TRACK_URI = webHost
			+ "?m=RcbShops&a=ordertrack";

	/**
	 * 1.21 修改订单状�?接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=upload&
	 * orderid=1&state=-1
	 */
	public static final String ORDER_STATES_UPDATE_URI = webHost
			+ "?m=RcbShops&a=uporder_v2";

	/**
	 * 1.22 消息列表接口：http://192.168.2.101/seller/index.php?m=RcbShops&a=inforlist&
	 * shopsid=1&typeid=1
	 */
	public static final String MSG_LIST_URI = webHost
			+ "?m=RcbShops&a=inforlist_v2";

	/**
	 * 1.23 消息总数量接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=infornum&
	 * shopsid=4
	 */
	public static final String MSG_NUM_URI = webHost + "?m=RcbShops&a=infornum";

	/**
	 * 1.24
	 * 消息分类数量接口�?http://192.168.2.211/seller/index.php?m=RcbShops&a=infortype
	 * &shopsid=1
	 */
	public static final String MSG_TYPE_URI = webHost
			+ "?m=RcbShops&a=infortype";

	/**
	 * 1.25 消息详情接口：http://192.168.2.101/seller/index.php?m=RcbShops&a=inforvo&
	 * inforid=1
	 */
	public static final String MSG_DETAILS_URI = webHost
			+ "?m=RcbShops&a=inforvo_v2";

	/**
	 * 标记消息接口
	 */
	public static final String MARK_MSG_URI = webHost + "?m=Message&a=addmark";

	/**
	 * 取消标记
	 */
	public static final String CANCEL_MSG_URI = webHost
			+ "?m=Message&a=delmark";

	/**
	 * 删除消息
	 */
	public static final String DELETE_MSG_URI = webHost + "?m=Message&a=delmsg";

	/**
	 * 评论列表
	 */
	public static final String COMMON_MSG_URI = webHost
			+ "?m=Preview&a=reviewlist";

	/**
	 * 问题反馈接口：http://192.168.2.101/seller/index.php?m=RcbShops&a=feedback
	 */
	public static final String FEED_BACK_URI = webHost
			+ "?m=RcbShops&a=feedback";

	/**
	 * 问题反馈列表接口：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * feedbacklist&p=1
	 */
	public static final String FEED_BACK_LIST_URI = webHost
			+ "?m=RcbShops&a=feedbacklist";

	/**
	 * 问题反馈详情接口：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * feedbackvo&id=21
	 */
	public static final String FEED_BACK_DETAILS_URI = webHost
			+ "?m=RcbShops&a=feedbackvo";

	/**
	 * 查询营收接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=selrevenue&
	 * shopsid=1&duration=1
	 */
	public static final String REVENUE_URI = webHost
			+ "?m=Revenue&a=get_shop_one_month_revenue";

	/**
	 * 查看店铺信息接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=checkstore&
	 * shopsid=1
	 */
	public static final String SEARCH_SHOP_INFO_URI = webHost
			+ "?m=RcbShops&a=checkstore";

	/**
	 * 更新店铺信息接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=uplogo&
	 * shopsid=1&oldpic=122.png&newpic=123344.jpg
	 */
	public static final String UPDATE_SHOP_INFO_URI = webHost
			+ "?m=RcbShops&a=uplogo";

	/**
	 * 查看店铺资料接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=checkdata&
	 * shopsid=1
	 */
	public static final String SEARCH_SHOP_DATA_URI = webHost
			+ "?m=SetUp&a=storedata";

	/**
	 * 修改密码接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=passedit
	 */
	public static final String UPDATE_PASSWORD_URI = webHost
			+ "?m=RcbShops&a=passedit";
	/**
	 * 修改密码接口：http://192.168.2.211/seller/index.php?m=RcbShops&a=passedit
	 */
	public static final String UPDATE_PASSWORD_URI2 = webHost
			+ "?m=User&a=passedit";

	/**
	 * 获取我的推荐码接口：http:///221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * shoprecode&id=4
	 */
	public static final String MY_RECOMMEND_CODE_URI = webHost
			+ "?m=RcbShops&a=shoprecode";

	/**
	 * 奖励规则接口：http://221.122.32.41:8088/user/index.php?m=RcbShops&a=getrules&
	 * prep=1&type=1
	 */
	public static final String RULE_URI = webHost + "?m=RcbShops&a=getrules";

	/**
	 * 获取他人推荐码接口：http:///221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * otherrecode&id=4
	 */
	public static final String OTHER_RECOMMEND_CODE_URI = webHost
			+ "?m=RcbShops&a=otherrecode";
	/**
	 * 添加他人推荐码接口：http:///221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * setrecode&id=4
	 */
	public static final String ADD_OTHER_RECOMMEND_CODE_URI = webHost
			+ "?m=RcbShops&a=setrecode";

	/**
	 * 获取Clientid
	 */
	public static final String GET_CLIENTID_URI = webHost
			+ "?m=RcbShops&a=getcid";

	/**
	 * 推荐码列表接口：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * recodelist&recode=456789&tag=2 tag int 2商家 1.用户 recode int 个人推荐�? p int
	 * 分页页数
	 * 
	 */
	public static final String RECOMMEND_LIST_URI = webHost
			+ "?m=RcbShops&a=recodelist";

	/**
	 * 推荐码列表接口：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * recodelist&recode=456789&tag=2 tag int 2商家 1.用户 recode int 个人推荐�? p int
	 * 分页页数
	 * 
	 */
	public static final String RECOMMEND_LIST_URI2 = webHost
			+ "?m=Recode&a=recodelist";

	// /**
	// *
	// 获取手机验证码接口：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=getauth
	// */
	// public static final String PHONE_CODE_URI = webHost
	// + "?m=RcbShops&a=getauth";

	/**
	 * 获取服务器时间戳
	 */
	public static final String GET_TIME_SERVER = webHost
			+ "?m=ApiDevice&a=get_timestamp_info";

	/**
	 * 发送验证码接口新
	 */
	public static final String SEND_CODE = webHost + "?m=RcbShops&a=getauth_v2";

	/**
	 * �?��版本是否�?��更新：http://221.122.32.41:8088/seller/index.php?m=RcbShops&a=
	 * get_version&code=1
	 */
	public static final String APP_UPDATE_URI = webHost
			+ "?m=RcbShops&a=get_version";

	/**
	 * 周边店铺：http://221.122.32.41:8088/user/index.php?m=RcbShops&a=rangelist
	 */
	public static final String RANGE_SHOP_LIST_URI = webHost
			+ "?m=RcbShops&a=rangelist";

	/**
	 * 获取注册参数是否为必填
	 */
	public static final String CHECK_INPUT_INFO_SPACE_URI = webHost
			+ "?m=RcbShops&a=register_explain";

	/**
	 * 新版注册接口
	 */
	public static final String REGISTER_NEW_URI = webHost
			+ "?m=RcbShops&a=verify_v2";

	/**
	 * 撤销订单原因：http://221.122.32.41:8088/seller/index.php?m=ApiShop&a=getrevoke
	 */
	public static final String CANCEL_ORDER_REASON_LIST_URI = webHost
			+ "?m=ApiShop&a=getrevoke";

	/**
	 * 获取联系人方�?
	 */
	public static final String GET_CONTACT_LIST_URI = webHost
			+ "?m=ApiShop&a=showfixphone";

	/**
	 * 更新联系人方�?
	 */
	public static final String UPDTAE_CONTACT_LIST_URI = webHost
			+ "?m=ApiShop&a=editfixphone";

	/**
	 * 获取店铺介绍
	 */
	public static final String GET_SHOP_CONTENT_URI = webHost
			+ "?m=ApiShop&a=showcon";

	/**
	 * 更新店铺介绍
	 */
	public static final String UPDATE_SHOP_CONTENT_URI = webHost
			+ "?m=ApiShop&a=editcon";

	/**
	 * 查看店铺营收接口
	 */
	public static final String GET_WEEK_SALES_URI = webHost
			+ "?m=Revenue&a=get_shop_revenue";

	/**
	 * �??已有账户�?
	 */
	public static final String OPEN_OLD_ELECTRONIC_ACCOUNT_URI = webHost
			+ "?m=ApiShop&a=cashier_y";

	/**
	 * �??新账户的
	 */
	public static final String OPEN_NEW_ELECTRONIC_ACCOUNT_URI = webHost
			+ "?m=ApiShop&a=cashier_n";
	/**
	 * �??新账户的
	 */
	public static final String OPEN_NEW_ELECTRONIC_ACCOUNT_STEP_ONE_URI = webHost
			+ "?m=ApiPay&a=cashier_n";

	/**
	 * 上传身份�?
	 */
	public static final String UPLOAD_CARDNO_IMG_URI = webHost
			+ "?m=ApiShop&a=upload";

	/**
	 * 电子账户�??状�?
	 */
	public static final String SETTING_STATUS_URI = webHost
			+ "?m=ApiShop&a=mycashier";

	/**
	 * 1.4 获取银行接口：http://221.122.32.41:8088/seller/index.php?m=ApiPay&a=
	 * cashier_n_two
	 */
	public static final String BANK_LIST_URI = webHost
			+ "?m=ApiPay&a=cashier_n_two";

	/**
	 * 获取电子绑定手机验证码接口：http://221.122.32.41:8088/seller/index.php?m=ApiPay&a=
	 * getrechargecode
	 */
	public static final String EA_PHONE_CODE_URI = webHost
			+ "?m=ApiPay&a=getrechargecode";
	/**
	 * 充�?�?��接口：http://221.122.32.41:8088/seller/index.php?m=ApiPay&a=recharge
	 */
	public static final String RECHARGE_ACTIVATE_PHONE_CODE_URI = webHost
			+ "?m=ApiPay&a=recharge";

	/**
	 * 联网核查
	 */
	public static final String ONLINE_CHECK_URI = webHost
			+ "?m=ApiPay&a=pay_check_shop";

	/**
	 * 提交配单给管�?
	 */
	public static final String COMMIT_ORDER_TO_HOUSER_URI = webHost
			+ "?m=Houser&a=send_distribution";
	/**
	 * 审核结果
	 */
	public static final String HOUSER_RES_URI = webHost
			+ "?m=Houser&a=apply_service";
	/**
	 * 获取配送信息
	 */
	public static final String GET_HOUSER_RES_URI = webHost
			+ "?m=Houser&a=check_result";

	/**
	 * 获取配送信息
	 */
	public static final String SETTING_GET_HOUSER_RES_URI = webHost
			+ "?m=Houser&a=sethouser";

	/**
	 * 取消配送信息
	 */
	public static final String REVOKE_DIS_URI = webHost
			+ "?m=Houser&a=revoke_dis";

	/**
	 * 获取订单数量
	 */
	public static final String GET_ORDER_NUM = webHost
			+ "?m=RcbShops&a=get_order_state_num_v2";

	/**
	 * 获取店铺信息接口
	 */
	public static final String GET_SHOP_INFO_URI = webHost
			+ "?m=SetUp&a=storeinfor";

	/**
	 * 获取店铺信息接口(新)
	 */
	public static final String NEW_GET_SHOP_INFO_URI = webHost
			+ "?m=SetUp&a=getshopinfo";

	/**
	 * 更新店铺信息接口
	 */
	public static final String UPDATE_SHOP_INFO_URI2 = webHost
			+ "?m=SetUp&a=upstoreinfor";

	/**
	 * 获取店铺预览详情信息接口
	 */
	public static final String UPDATE_SHOP_INFO_DETAILS = webHost
			+ "?m=Preview&a=shopvo";

	/**
	 * 获取店铺预览商品列表接口
	 */
	public static final String UPDATE_SHOP_LIST = webHost
			+ "?m=RcbShops&a=goodslist_v2";

	/**
	 * 获取店铺预览商品列表接口
	 */
	public static final String UPDATE_GOOD_DETAILS = webHost
			+ "?m=Preview&a=goodvo";

	/**
	 * 删除商品接口
	 */
	public static final String DELETE_GOOD_URI = webHost
			+ "?m=RcbShops&a=delgoods";
	/**
	 * 获取融云token
	 */
	public static final String GET_RONG_TOKEN = webHost
			+ "?m=ApiRongIM&a=get_im_token";

	/**
	 * 客户列表
	 */
	// http://192.168.1.211:8088/seller_dev/index.php?m=User&a=get_users_list
	public static final String CLIENT_LIST_URL = webHost
			+ "?m=User&a=get_users_list_v2";

	/**
	 * 删除客户
	 */
	public static final String CLIENT_DELETE = webHost
			+ "?m=User&a=delete_user_v2";

	/**
	 * 添加客户
	 */
	public static final String CLIENT_ADD = webHost + "?m=User&a=add_user_v2";

	public static final String aaa = webHost + "?m=ApiPay&a=get_cashier_result";

	/**
	 * 客户详情
	 */
	public static final String CLIENT_DETAILS = webHost
			+ "?m=User&a=get_users_detail_list_v2";

	/**
	 * 客户 修改备注
	 */
	public static final String CLIENT_REMARK_NAME = webHost
			+ "?m=User&a=update_remark_name_v2";

	/**
	 * 开通电子收银台查询接口
	 */
	public static final String SELECT_ONLINE = webHost
			+ "?m=ApiPay&a=check_cashier_status";

	/**
	 * 验证客户是否存在
	 */
	public static final String CLINET_CUNZAI = webHost
			+ "?m=User&a=is_have_user_v2";

	/**
	 * 与某某是同一个人，是否合并
	 */
	public static final String CLIENT_TONGXUN_HEBING = webHost
			+ "?m=User&a=add_user_confirm";

	/**
	 * 开通电子收银台身份证图片上传接口
	 */
	public static final String UPDATA_ID_CARD = webHost
			+ "?m=ApiPay&a=upload_to_bank";

	/**
	 * 
	 */
	public static final String SUBMIT_ALL = webHost
			+ "?m=ApiPay&a=cashier_n_v2";

	/**
	 * 设备信息上传
	 */
	public static final String UPLOAD_DEVICE_INFO = webHost
			+ "?m=ApiDevice&a=upload_device_info";

	public static final String GOOD_SETTING = webHost
			+ "?m=SetUp&a=shop_takeaway_set";

	/**
	 * 商品类别列表
	 */
	public static final String GOOD_SORT_LIST = webHost
			+ "?m=RcbShops&a=goodstype_v2";

	/**
	 * 修改商品类别
	 */
	public static final String UPDATE_SORT_LIST = webHost
			+ "?m=RcbShops&a=edit_goodtype";

	/**
	 * 优惠买单列表接口
	 */
	public static final String STORE_PAY_LIST = webHost
			+ "?m=RcbShops&a=order_sale_list";
	/**
	 * 获取优惠买单详情接口
	 */
	public static final String STORE_PAY_DETAILS = webHost
			+ "?m=RcbShops&a=order_sale_detail_v2";

	/**
	 * 优惠买单验证订单接口
	 */
	public static final String ORDER_PAY_CODE = webHost
			+ "?m=RcbShops&a=confirm_authcode";
	/**
	 * 礼品券验证订单接口
	 */
	public static final String ORDER_QUAN_CODE = webHost
			+ "?m=Order&a=gift_scan_code";

	/**
	 * 注册功能
	 */
	public static final String REGISTER_UPDATE = webHost
			+ "?m=RcbShops&a=verify_v3";

	/**
	 * 店铺信息修改功能
	 */
	public static final String SHOP_UPDATE = webHost
			+ "?m=SetUp&a=shop_info_set";

	/**
	 * 开店资质信息修改接口
	 */
	public static final String UPDATE_SHOP_ZIZHI = webHost
			+ "?m=SetUp&a=shop_require_set";

	/**
	 * 查看开店资质接口
	 */
	public static final String LOOK_SHOP_ZIZHI = webHost
			+ "?m=SetUp&a=get_shop_require_info";

	/**
	 * 　查看 店铺的状态
	 */
	public static final String SHOP_STATUS = webHost
			+ "?m=SetUp&a=getshop_state";

	public static final String UPDATE_SERVER = webHost
			+ "/Update/AndriodUpdate";
	public static final String UPDATE_SAVENAME = "eshop.apk";

	public static final int notForSale = 0;
	public static final int readTimeOut = 10 * 1000;
	public static final int connectTimeout = 10 * 1000;
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	public static final int SELECT_PIC_BY_PICK_PHOTO2 = 100;
	public static final int SELECT_PIC_CUT = 3;
	public static final int UPLOAD_SUCCESS_CODE = 1;
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	public static final int WHAT_TO_UPLOAD = 1;
	public static final int WHAT_UPLOAD_DONE = 2;
	public static final String KEY_PHOTO_PATH = "photo_path";
	public static final String SELLERID = "2";

	public final static String PICTURE_DIRECTORY = "/EServiceStore";

	public static void InitValue(Context context) {

		initScreenSize(context);
	}

	public static final int BASE_SCREEN_WIDTH = 640;
	public static final int BASE_SCREEN_HEIGHT = 960;
	public static int screenWidth;
	public static int screenHeight;
	public static float screenScale;
	public static float screenHeightScale;
	public static float screenWidthScale;

	public static void initScreenSize(Context context) {

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		if (screenWidth == 0) {
			screenWidth = BASE_SCREEN_WIDTH;
		}
		if (screenHeight == 0) {
			screenHeight = BASE_SCREEN_HEIGHT;
		}

		if (screenWidth * 1f / screenHeight < BASE_SCREEN_WIDTH * 1f
				/ BASE_SCREEN_HEIGHT) {
			screenScale = screenWidth * 1f / BASE_SCREEN_WIDTH;
		} else {
			screenScale = screenHeight * 1f / BASE_SCREEN_HEIGHT;
		}

		screenWidthScale = screenWidth * 1f / BASE_SCREEN_WIDTH;
		screenHeightScale = screenHeight * 1f / BASE_SCREEN_HEIGHT;
	}
}