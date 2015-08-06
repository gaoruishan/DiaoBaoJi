package com.zjwy.tiaobaojinew;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.zjwy.tiaobaojinew.netstate.NetChangeObserver;
import com.zjwy.tiaobaojinew.netstate.NetWorkUtil.NetType;
import com.zjwy.tiaobaojinew.netstate.NetworkStateReceiver;
import com.zjwy.tiaobaojinew.utils.AppConstant;
import com.zjwy.tiaobaojinew.utils.HttpUtil;

public class BaseApplication extends Application {
	private static BaseApplication application;
	public MeNetChngeOberver observer = new MeNetChngeOberver();

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		// 创建sharePreferences
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 登录判断状态
		initLoginState();
		// 注册网络监听
		NetworkStateReceiver.registerNetworkStateReceiver(this);
		NetworkStateReceiver.registerObserver(observer);
		// 初始化 JPush
		JPushInterface.init(this);
		// 初始化shareSDK
		ShareSDK.initSDK(this);

	}

	/**
	 * 初始化登录状态
	 */
	public void initLoginState() {

		Editor editor = sp.edit();
		if (sp.getBoolean("autoLogin", false)) {
			HttpUtil.autoLogin();// 自动登录
		} else {
			editor.putBoolean("isLogin", false);// 用于设置是否自动登录---换头像
			editor.commit();
		}
	}

	/**
	 * 声明一个application类用来存取cookie：
	 */
	public CookieStore cookies;
	public boolean isFirst = true;
	public static HttpClient httpClient = new DefaultHttpClient();
	public static SharedPreferences sp;

	public CookieStore getCookie() {
		return cookies;
	}

	public void setCookie(CookieStore cks) {
		cookies = cks;
	}

	/**
	 * 提供一个获取应用程序的上下文的静态方法
	 * 
	 * @return
	 */
	public static synchronized BaseApplication getApplication() {
		return application;
	}

	/**
	 * 提供一个静态全局的HttpClinet
	 */
	public static HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * 提供一个公共的可访问sp
	 */
	public static SharedPreferences getSharedPreferences() {
		return sp;
	}

	public void showToast(String msg) {
		Toast.makeText(application, msg, Toast.LENGTH_SHORT).show();
	}

	// 获取屏幕的高度
	public static int getScreenHeight(Activity context) {
		DisplayMetrics outMetrics = getDisplayMetrics(context);
		return outMetrics.heightPixels;
	}

	// 获取屏幕的宽度
	public static int getScreenWidth(Activity context) {
		DisplayMetrics outMetrics = getDisplayMetrics(context);
		return outMetrics.widthPixels;

	}

	private static DisplayMetrics getDisplayMetrics(Activity context) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}

	/**
	 * 注销网络状态接受者
	 */
	public void unRegisterNetworkStateReceiver() {
		NetworkStateReceiver.removeRegisterObserver(observer);
		NetworkStateReceiver.unRegisterNetworkStateReceiver(application);
	}

	/**
	 * 注销登录
	 */
	public void unRegisterLogin() {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean("isLogin", false);// 用于设置是否自动登录---换头像
		editor.commit();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					httpClient.execute(new HttpGet(AppConstant.LOGOUT));
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public class MeNetChngeOberver extends NetChangeObserver {
		@Override
		public void onConnect(NetType type) {
			super.onConnect(type);
			if (isFirst) {
				switch (type) {
				case WIFI:
					BaseApplication.getApplication().showToast("WIFI连接!");
					break;
				case CMWAP:
					BaseApplication.getApplication().showToast("中国移动GPRS网络!");
					break;
				case CMNET:
					BaseApplication.getApplication()
							.showToast("中国移动2/3G/4G网络!");
					break;
				case GWAP_3:
					BaseApplication.getApplication().showToast("中国联通GPRS网络!");
					break;
				case GNET_3:
					BaseApplication.getApplication().showToast("中国联通2/3/4G网络!");
					break;
				case CTNET:
					BaseApplication.getApplication()
							.showToast("中国电信 2/3/4G网络!");
					break;
				case CTWAP:
					BaseApplication.getApplication().showToast("中国电信GPRS网络!");
					break;
				default:
					BaseApplication.getApplication().showToast("网络已连接!");
					break;
				}
				isFirst = false;
			}
		}

		@Override
		public void onDisConnect() {
			super.onDisConnect();
		}
	}
}
