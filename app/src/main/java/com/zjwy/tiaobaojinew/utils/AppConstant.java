package com.zjwy.tiaobaojinew.utils;

/**
 * 应用程序常量类
 * 
 * @author wangpanpan
 * 
 */
public class AppConstant {
	/**
	 * 正式服务端接口
	 */

	// public static String HOME_ACTIVITY =
	// "http://www.tiaobaoji.com/client?action=getBanner";// 焦点图片-惊喜活动
	// public static String HOME_HEAD =
	// "http://www.tiaobaoji.com/client?action=goodshow&id=";// 商品详情
	// public static String HOME_BEST_RECOMMEND =
	// "http://www.tiaobaoji.com/client?action=goodlist&pageSize=20&position=best";//
	// // 商品列表-精品推荐
	// public static String HOME_NEWS_POP =
	// "http://www.tiaobaoji.com/client?action=goodlist&pageSize=20&position=newest";//
	// // 商品列表-最新流行
	// public static String SORT_BRAND =
	// "http://www.tiaobaoji.com/client?action=getbrand";// 品牌
	// public static String SORT_BRAND_HEAD =
	// "http://www.tiaobaoji.com/client?action=goodlist&pageSize=20&brand_id=";//
	// // 商品列表-品牌
	// public static String SORT_PRICE_HEAD =
	// "http://www.tiaobaoji.com/client?action=goodlist&pageSize=10&price_rent=";//
	// // 商品列表-价格
	// public static String SORT_PRICE_ALL =
	// "http://www.tiaobaoji.com/client?action=goodlist&pageSize=30&price_rent[]=99&price_rent[]=199";//
	// // 商品列表-价格全部
	// public static String SORT_STYLE_HEAD =
	// "http://www.tiaobaoji.com/client?action=goodlist&pageSize=20&sort_id=";//
	// // 商品列表-款式
	// public static String SORT_STYLE =
	// "http://www.tiaobaoji.com/client?action=getsort";// 款式(共有7种
	// public static String SEARCH =
	// "http://www.tiaobaoji.com/client?action=search&pageSize=30&keyword=";//
	// // 搜索
	// public static String REMIND =
	// "http://www.tiaobaoji.com/client?action=remind";// 到货提醒
	// public static String KUFU = "http://www.tiaobaoji.com/kefu.html";// 客服
	// public static String LOGIN =
	// "http://passport.tiaobaoji.com/client?action=login";// 登录
	// public static String REGISTER =
	// "http://passport.tiaobaoji.com/client?isTest=no&action=register";// 注册
	// public static String LOGOUT =
	// "http://passport.tiaobaoji.com/client?isTest=no&action=logout";// 注销
	// public static String GETSERIAL =
	// "http://passport.tiaobaoji.com/client?action=getSerial";// 获取序列号
	// public static String USER_EDIT =
	// "http://passport.tiaobaoji.com/client?action=useredit";// 编辑用户信息
	// public static String EDIT_PWD =
	// "http://passport.tiaobaoji.com/client?action=editpwd";// 忘记密码
	// public static String EDIT_OLD_PWD =
	// "http://passport.tiaobaoji.com/client?action=editoldpwd";// 修改旧密码
	// public static String UPLOAD_FILE =
	// "http://passport.tiaobaoji.com/client?action=avatarinput";// 上传文件
	// public static String VALIDRENT =
	// "http://pay.tiaobaoji.com/client?action=validRent&good_id=";// 商品是否可租
	// public static String ORDERPAY =
	// "http://pay.tiaobaoji.com/client/paypage?";// 生成订单(页面版)
	// public static String GETCOUPON =
	// "http://pay.tiaobaoji.com/client?action=getCoupon&user_id=";// 获取优惠券
	// public static String GETPAYINFOS =
	// "http://pay.tiaobaoji.com/client?action=getaccount&pageSize=20";// 获取订单
	// public static String DELETEPAYINFOS =
	// "http://pay.tiaobaoji.com/client?action=deleteaccount&orderid=";// 取消订单
	// public static String GOPAYINFOS =
	// "http://pay.tiaobaoji.com/client?action=payaccount&orderid=";// 继续支付
	// public static String SHOPPING =
	// "http://pay.tiaobaoji.com/client?action=cartshow";// 我的购物车
	// public static String ADD_SHOPPING =
	// "http://pay.tiaobaoji.com/client?action=cartadd&good_id=";// 添加到购物车
	// public static String DELETE_SHOPPING =
	// "http://pay.tiaobaoji.com/client?action=cartdelete&good_id[]=";// 删除到购物车

	/**
	 * 测试服务端接口
	 */
	public static String HOME_ACTIVITY = "http://dev.luxury.alyee.com/client?action=getBanner";// 焦点图片-惊喜活动
	public static String HOME_HEAD = "http://dev.luxury.alyee.com/client?action=goodshow&id=";// 商品详情
	public static String HOME_BEST_RECOMMEND = "http://dev.luxury.alyee.com/client?action=goodlist&pageSize=20&position=best";//
	// 商品列表-精品推荐
	public static String HOME_NEWS_POP = "http://dev.luxury.alyee.com/client?action=goodlist&pageSize=20&position=newest";//
	// 商品列表-最新
	public static String SORT_BRAND = "http://dev.luxury.alyee.com/client?action=getbrand";// 品牌
	public static String SORT_BRAND_HEAD = "http://dev.luxury.alyee.com/client?action=goodlist&brand_id=";// 商品列表-品牌
	public static String SORT_PRICE_HEAD = "http://dev.luxury.alyee.com/client?action=goodlist&price_rent=";//
	// 商品列表-价格
	public static String SORT_PRICE_ALL = "http://dev.luxury.alyee.com/client?action=goodlist&&pageSize=20&price_rent[]=99&price_rent[]=199";//
	// 商品列表-价格全部
	public static String SORT_STYLE_HEAD = "http://dev.luxury.alyee.com/client?action=goodlist&sort_id=";// 商品列表-款式
	public static String SORT_STYLE = "http://dev.luxury.alyee.com/client?action=getsort";// 款式(共有7种
	public static String SEARCH = "http://dev.luxury.alyee.com/client?action=search&pageSize=30&keyword=";//
	// 搜索
	public static String REMIND = "http://dev.luxury.alyee.com/client?action=remind";// 到货提醒
	public static String KUFU = "http://dev.luxury.alyee.com/kefu.html";// 客服
	public static String LOGIN = "http://dev.passport.alyee.com/client?isTest=no&action=login";// 登录
	public static String REGISTER = "http://dev.passport.alyee.com/client?isTest=no&action=register";// 注册
	public static String USER_EDIT = "http://dev.passport.alyee.com/client?action=useredit";// 编辑用户信息
	public static String EDIT_PWD = "http://dev.passport.alyee.com/client?action=editpwd";// 找回忘记密码
	public static String EDIT_OLD_PWD = "http://dev.passport.alyee.com/client?action=editoldpwd";// 修改旧密码
	public static String UPLOAD_FILE = "http://dev.passport.alyee.com/client?action=avatarinput";// 上传文件
	public static String GETSERIAL = "http://dev.passport.alyee.com/client?action=getSerial";// 获取序列号
	public static String LOGOUT = "http://dev.passport.alyee.com/client?action=logout";// 注销
	public static String ORDERPAY = "http://dev.pay.alyee.com/client/paypage?version=1.0";// 生成订单(页面版)
	public static String GETCOUPON = "http://dev.pay.alyee.com/client?action=getCoupon&user_id=";// 获取优惠券
	public static String GETPAYINFOS = "http://dev.pay.alyee.com/client?action=getaccount&pageSize=20";// 获取订单
	public static String DELETEPAYINFOS = "http://dev.pay.alyee.com/client?action=deleteaccount&pageSize=20&orderid=";//
	// 取消订单
	public static String GOPAYINFOS = "http://dev.pay.alyee.com/client?action=payaccount&orderid=";// 继续支付
	public static String SHOPPING = "http://dev.pay.alyee.com/client?action=cartshow";// 我的购物车
	public static String ADD_SHOPPING = "http://dev.pay.alyee.com/client?action=cartadd&good_id=";// 添加到购物车
	public static String DELETE_SHOPPING = "http://dev.pay.alyee.com/client?action=cartdelete&good_id[]=";// 删除到购物车
	public static String VALIDRENT = "http://dev.pay.alyee.com/client?action=validRent&good_id=";// 商品是否可租

	/**
	 * 客服电话
	 */
	public static final String KEFU = "01053349177";
}
